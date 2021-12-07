package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a Gitlet repository.
 * A Repository object persistently stores
 * the data in a Git repository and performs
 * various operations as in the real Git repository.
 *  @author Min-Hsiu Hsu
 */
public class Repository implements Serializable, Dumpable {
    /**
     * Instance variables:
     * 1. head: the pointer points at the current branch.
     * 2. branches <branchName, commitFile>:
     *      a data structure that stores branch names and the head of that branch.
     * 2. commits <commitFile, parentFile>:
     *      a data structure that stores all the references of commits in this repository.
     */
    private String head;
    private Map<String, File> branches;
    private Map<File, File> commits;
    private HashMap<String, File> stagingArea;

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The commits directory. */
    public static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    /** The objects directory storing hashed files. */
    public static final File OBJECT_DIR = join(GITLET_DIR, "objects");
    /** The filename of the repo snapshot instance. */
    public static final File REPO_FILE = join(GITLET_DIR, "HEAD");

    private transient TreeMap<String, File> headCommitBlobs;
    private transient TreeMap<String, File> inputCommitBlobs;
    private transient List<String> untrackedFiles;

    /** Persistence structure.
     * CWD                              <==== Whatever the current working directory is.
     *  └── .gitlet                     <==== All persistant data is stored within here
     *      ├── repo                    <==== A Repository instance stored as a file
     *      ├── commits                 <==== Where the commits are stored
     *      ├   ├── commit1             <==== A single Commit instance stored as a file
     *      ├   ├── ...
     *      ├   └── commitN
     *      ├
     *      └── objects                 <==== All serialized files are stored in this directory
     *          ├── Object1             <==== A single object instance stored to a file
     *          ├── Object2
     *          ├── ...
     *          └── ObjectN
     */

    /** Gitlet repository constructor. */
    Repository() {
        head = "master";
        branches = new HashMap<>();
        commits = new HashMap<>();
        stagingArea = new HashMap<>();
    }

    /** Initializes a Gitlet repository. */
    public void init() {
        /* Create .gitlet workspace. */
        if (GITLET_DIR.exists()) {
            errorExit("A Gitlet version-control system already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        COMMIT_DIR.mkdir();
        OBJECT_DIR.mkdir();

        /* Add the first commit. */
        Commit initialCommit = new Commit();
        File initialCommitFile = initialCommit.saveCommit();
        commits.put(initialCommitFile, initialCommit.getParentFile());

        /* Create a branch "master". */
        branches.put("master", initialCommitFile);

        /* Save the state of this repo. */
        this.saveRepo();
    }

    /** Adds the input file to the staging area. */
    public void add(String filePathName) {
        File inputFile = new File(filePathName);
        if (!inputFile.exists()) {
            errorExit("File does not exist.");
        }

        /* Get the content and File of the input file after SHA-1 hash. */
        byte[] inputContent = readContents(inputFile);
        File inputBlob = join(OBJECT_DIR, sha1(inputContent));

        /* Get the blobs in the head commit and the corresponding blob of the input file */
        /** Transient variables */
        Commit headCommit = getHeadCommit();
        headCommitBlobs = headCommit.getBlobs();
        File prevBlob = headCommitBlobs.get(filePathName);

        if (prevBlob == null) {
            stageFile(filePathName, inputBlob, inputContent);
            stagingArea.put(filePathName, inputBlob);
            return;
        }

        if (prevBlob.equals(inputBlob)) {
            unStageFile(filePathName, inputBlob);
            return;
        }

        stageFile(filePathName, inputBlob, inputContent);
    }

    /** Saves the current snapshot with staged files as  a commit. */
    public void commit(String commitMsg) {
        if (stagingArea.isEmpty()) {
            errorExit("No changes added to the commit.");
        }

        if (commitMsg == null || commitMsg.equals("")) {
            errorExit("Please enter a commit message.");
        }

        /* Updates the head commit with staged files. */
        headCommitBlobs = getHeadCommit().getBlobs();
        headCommitBlobs.putAll(stagingArea);

        /* Removes keys with null values as untracking files. */
        headCommitBlobs.values().remove(null);

        /* Creates a new commit. */
        Commit c = new Commit(getHeadCommitFile(), commitMsg, headCommitBlobs);
        File cFile = c.saveCommit();
        commits.put(cFile, c.getParentFile());
        branches.put(head, cFile);

        /* Clears the staging area. */
        stagingArea.clear();

        saveRepo();
    }

    /** Unstages a tracked file.  */
    public void rm(String filePathName) {
        boolean isStaged = stagingArea.containsKey(filePathName);
        boolean isTracked = getHeadCommit().getBlobs().containsKey(filePathName);
        if (!isStaged && !isTracked) {
            errorExit("No reason to remove the file.");
        }

        /* Unstages the file if it is added in the staging area. */
        stagingArea.remove(filePathName);

        /* Stages the file for removal if it exists in the head commit. */
        if (isTracked) {
            stagingArea.put(filePathName, null);
            /* Removes the file from the working directory. */
            restrictedDelete(filePathName);
        }

        saveRepo();
    }

    /** Displays the log of the current head commit. */
    public void log() {
        Commit curCommit = getHeadCommit();
        while (curCommit != null) {
            curCommit.printLog();
            curCommit = Commit.loadCommit(curCommit.getParentFile());
        }
    }

    /** Displays the logs about all the commits ever made. */
    public void globalLog() {
        for (File cFile : commits.keySet()) {
            Commit.loadCommit(cFile).printLog();
        }
    }

    /** Prints the commit IDs of those commits matches
     * the input commitMsg.
     */
    public void find(String searchMsg) {
        boolean found = false;
        for (File cFile: commits.keySet()) {
            String commitMsg = Commit.loadCommit(cFile).getCommitMsg();
            String commitId = Commit.loadCommit(cFile).getSha1();
            if (commitMsg.equals(searchMsg)) {
                System.out.println(commitId);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    /** Displays what branches currently exist, and marks the current branch with a *.
     *  Also displays what files have been staged for addition or removal
     */
    public void status() {
        /* Collects sorted branches, staged, and removed files. */
        ArrayList<String> branchNames = new ArrayList<>(branches.keySet());
        Collections.sort(branchNames);

        List<String> stagedFiles = getStagedFiles();
        List<String> addedFiles = getAddedFiles(stagedFiles);
        List<String> removedFiles = getRemovedFiles(stagedFiles);
        untrackedFiles = getUntrackedFiles(stagedFiles, removedFiles);

        /* Prints branches. */
        System.out.println("=== Branches ===");
        for (String bn : branchNames) {
            if (bn.equals(head)) {
                System.out.println("*" + bn);
            } else {
                System.out.println(bn);
            }
        }
        System.out.println();

        /* Prints staged files. */
        System.out.println("=== Staged Files ===");
        for (String af : addedFiles) {
            System.out.println(af);
        }
        System.out.println();

        /* Prints removed files. */
        System.out.println("=== Removed Files ===");
        for (String rf : removedFiles) {
            System.out.println(rf);
        }
        System.out.println();

        /* Prints modification but not staged info. (Not implemented.) */
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        /* Prints untracked files. */
        System.out.println("=== Untracked Files ===");
        for (String uf : untrackedFiles) {
            System.out.println(uf);
        }
        System.out.println();

    }

    /** Performs checkouts to a file in the current head commit. */
    public void checkout(String inputFileName) {
        headCommitBlobs = getHeadCommit().getBlobs();
        if (!headCommitBlobs.containsKey(inputFileName)) {
            errorExit("File does not exist in that commit.");
        }

        File checkoutFile = join(CWD, inputFileName);
        byte[] checkoutContent = readContents(headCommitBlobs.get(inputFileName));
        writeContents(checkoutFile, checkoutContent);
    }

    /** Performs checkouts to a file in a given commitId. */
    public void checkout(String inputCommitId, String inputFileName) {
        inputCommitId = searchCommitId(inputCommitId);
        if (inputCommitId == null) {
            errorExit("No commit with that id exists.");
        }

        Commit inputCommit = Commit.loadCommit(join(COMMIT_DIR, inputCommitId));
        inputCommitBlobs = inputCommit.getBlobs();
        if (!inputCommitBlobs.containsKey(inputFileName)) {
            errorExit("File does not exist in that commit.");
        }

        File checkoutFile = join(CWD, inputFileName);
        byte[] checkoutContent = readContents(inputCommitBlobs.get(inputFileName));
        writeContents(checkoutFile, checkoutContent);
    }

    /** Performs checkouts to a given branch. */
    public void checkoutBranch(String inputBranchName) {
        if (!branches.containsKey(inputBranchName)) {
            errorExit("No such branch exists.");
        }

        if (head.equals(inputBranchName)) {
            errorExit("No need to checkout the current branch.");
        }

        /* Makes sure no untracked file is overwritten. */
        overwrittenHelper(inputBranchName);

        /* Sets the HEAD to be the head commit of the given branch. */
        head = inputBranchName;

        /* Overwrites/checkouts a file to the given branch (the new head commit.) */
        for (String fileName : inputCommitBlobs.keySet()) {
            checkout(fileName);
        }

        /* Removes the files tracked in the current branch but not present in the given branch */
        List<String> trackedFiles = new ArrayList<>(plainFilenamesIn(CWD));
        trackedFiles.removeAll(untrackedFiles);

        for (String tf : trackedFiles) {
            if (!inputCommitBlobs.containsKey(tf)) {
                File rf = join(CWD, tf);
                rf.delete();
            }
        }

        /* Clears the staging area. */
        stagingArea.clear();

        saveRepo();
    }

    /** Creates a new branch in this repo. */
    public void branch(String inputBranchName) {
        if (branches.containsKey(inputBranchName)) {
            errorExit("A branch with that name already exists.");
        }

        branches.put(inputBranchName, getHeadCommitFile());
        saveRepo();
    }

    /** Deletes a branch. */
    public void removeBranch(String inputBranchName) {
        if (!branches.containsKey(inputBranchName)) {
            errorExit("A branch with that name does not exist.");
        }

        if (head.equals(inputBranchName)) {
            errorExit("Cannot remove the current branch.");
        }

        branches.remove(inputBranchName);

        saveRepo();
    }

    /** Checks out all the files tracked by the given commit. */
    public void reset(String inputCommitId) {
        inputCommitId = searchCommitId(inputCommitId);
        if (inputCommitId == null) {
            errorExit("No commit with that id exists.");
        }

        /* Moves the head commit to the given commit. */
        String curBranch = head;
        branches.put(curBranch, join(COMMIT_DIR, inputCommitId));

        /* Makes sure no untracked file is going to be overwritten. */
        overwrittenHelper(curBranch);

        /* Creates a dummy branch. */
        branch("dummy");
        checkoutBranch("dummy");
        head = curBranch;
        removeBranch("dummy");

        saveRepo();
    }

    /** Merge the current branch with a given branch. */
    public void merge(String inputBranchName) {
        File curHeadCommitFile = branches.get(head);
        File givenHeadCommitFile = branches.get(inputBranchName);
        File splitCommitFile = findLatestSplitCommit(curHeadCommitFile, givenHeadCommitFile);

        String givenHeadCommitId = Commit.loadCommit(givenHeadCommitFile).getSha1();

        /* Given branch is an ancestor of the current branch. */
        if (splitCommitFile.equals(givenHeadCommitFile)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        /* Current branch fast-forwarded. */
        if (splitCommitFile.equals(curHeadCommitFile)) {
            checkoutBranch(inputBranchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        /* Gets all files in the three commits. */
        TreeMap<String, File> curHeadCommitBlobs =
                Commit.loadCommit(curHeadCommitFile).getBlobs();
        TreeMap<String, File> givenHeadCommitBlobs =
                Commit.loadCommit(givenHeadCommitFile).getBlobs();
        TreeMap<String, File> splitCommitBlobs =
                Commit.loadCommit(splitCommitFile).getBlobs();
        Set<String> unionFileNames = new HashSet<>();
        unionFileNames.addAll(curHeadCommitBlobs.keySet());
        unionFileNames.addAll(givenHeadCommitBlobs.keySet());
        unionFileNames.addAll(splitCommitBlobs.keySet());

        boolean isInConflict = false;

        for (String n : unionFileNames) {
            /* Checks the presence of a file in the above three commits. */
            Boolean existsCur = curHeadCommitBlobs.containsKey(n);
            Boolean existsGiven = givenHeadCommitBlobs.containsKey(n);
            Boolean existsSplit = splitCommitBlobs.containsKey(n);

            if (existsSplit && existsCur && existsGiven) {
                if (splitCommitBlobs.get(n).equals(curHeadCommitBlobs.get(n))
                && !splitCommitBlobs.get(n).equals(givenHeadCommitBlobs.get(n))) {
                    checkout(givenHeadCommitId, n);
                    add(n);
                }

                if (!splitCommitBlobs.get(n).equals(curHeadCommitBlobs.get(n))
                        && !splitCommitBlobs.get(n).equals(givenHeadCommitBlobs.get(n))) {
                    mergeConflict(n, curHeadCommitBlobs.get(n), givenHeadCommitBlobs.get(n));
                    add(n);
                    isInConflict = true;
                }
            } else if (!existsSplit && !existsCur && existsGiven) {
                checkout(givenHeadCommitId, n);
                add(n);
            } else if (existsSplit && existsCur && !existsGiven) {
                if (splitCommitBlobs.get(n).equals(curHeadCommitBlobs.get(n))) {
                    rm(n);
                } else {
                    mergeConflict(n, curHeadCommitBlobs.get(n), givenHeadCommitBlobs.get(n));
                    add(n);
                    isInConflict = true;
                }
            } else if (!existsSplit && existsCur && existsGiven) {
                if (!curHeadCommitBlobs.get(n).equals(givenHeadCommitBlobs.get(n))) {
                    mergeConflict(n, curHeadCommitBlobs.get(n), givenHeadCommitBlobs.get(n));
                    add(n);
                    isInConflict = true;
                }
            } else if (existsSplit && !existsCur && existsGiven) {
                if (!splitCommitBlobs.get(n).equals(givenHeadCommitBlobs.get(n))) {
                    mergeConflict(n, curHeadCommitBlobs.get(n), givenHeadCommitBlobs.get(n));
                    add(n);
                    isInConflict = true;
                }
            }
        }


        String commitMsg = "Merged " + inputBranchName + " into " + head + "\\.";

        commit(commitMsg);

        if (isInConflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** Saves the current state of this repo. */
    public void saveRepo() {
        writeObject(REPO_FILE, this);
    }

    /** Loads a repo instance saved with name "HEAD" in the .gitlet directory. */
    public static Repository loadRepo() {
        return readObject(REPO_FILE, Repository.class);
    }

    @Override
    public void dump() {
        System.out.printf("headCommitFile: %s%nstagingAreaKeys: %s%n",
                branches.get(head),
                stagingArea.keySet());
    }

    /** Helper methods. */

    /** Returns the File of the head commit. */
    private File getHeadCommitFile() {
        return branches.get(head);
    }

    /** Returns the commit object of the head commit. */
    private Commit getHeadCommit() {
        return Commit.loadCommit(getHeadCommitFile());
    }
    /** Staged a file into the staging area,
     *  i.e., add the filename and File object into the staging area
     *  and saves a blob in OBJECT_DIR.
     */
    private void stageFile(String filePathName, File blob, byte[] content) {
        stagingArea.put(filePathName, blob);
        writeContents(blob, content);
        saveRepo();
    }

    private void unStageFile(String filePathName, File blob) {
        stagingArea.remove(filePathName);
        saveRepo();
    }

    /** Returns current staged files. */
    private List<String> getStagedFiles() {
        ArrayList<String> stagedFiles = new ArrayList<>(stagingArea.keySet());
        Collections.sort(stagedFiles);
        return stagedFiles;
    }

    /** Returns current added files. */
    private List<String> getAddedFiles(List<String> stagedFiles) {
        ArrayList<String> addedFiles = new ArrayList<>();
        /* Differentiates the files staged to be added or removed. */
        for (String key : stagedFiles) {
            if (stagingArea.get(key) != null) { // files staged for removal
                addedFiles.add(key);
            }
        }
        return addedFiles;
    }

    /** Returns current removed files. */
    private List<String> getRemovedFiles(List<String> stagedFiles) {
        ArrayList<String> removedFiles = new ArrayList<>();
        /* Differentiates the files staged to be added or removed. */
        for (String key : stagedFiles) {
            if (stagingArea.get(key) == null) { // files staged for removal
                removedFiles.add(key);
            }
        }
        return removedFiles;
    }

    /** Returns current untracked files. */
    private List<String> getUntrackedFiles(List<String> stagedFiles, List<String> removedFiles) {
        /* All files in the working directory. */
        ArrayList<String> outFiles = new ArrayList<>(plainFilenamesIn(CWD));
        /* Collects the files that are staged for removal. */
        ArrayList<String> recreatedFiles = new ArrayList<>(removedFiles);
        recreatedFiles.retainAll(outFiles);
        /* Removes the files tracked in the head commit. */
        outFiles.removeAll(getHeadCommit().getBlobs().keySet());
        /* Removes the files added in the staging area. */
        outFiles.removeAll(stagedFiles);
        /* Adds the files that are staged for removal but then recreated. */
        outFiles.addAll(recreatedFiles);

        return outFiles;
    }

    /** Returns true if a commitId exists.
     * False, if it does not exist.
     */
    private String searchCommitId(String inputCommitId) {
        List<String> commitIDs = plainFilenamesIn(COMMIT_DIR);
        for (String commitId : commitIDs) {
            if (commitId.startsWith(inputCommitId)) {
                return commitId;

            }
        }
        return null;
    }

    /** Makes sure no untracked file is overwritten.
     * And, sets the untrackedFiles and inputCommitBlobs
     * for checkoutBranch method.
     */
    private void overwrittenHelper(String inputBranchName) {
        List<String> stagedFiles = getStagedFiles();
        untrackedFiles = getUntrackedFiles(stagedFiles, getRemovedFiles(stagedFiles));
        Commit givenCommit = Commit.loadCommit(branches.get(inputBranchName));
        inputCommitBlobs = givenCommit.getBlobs();

        for (String u: untrackedFiles) {
            if (inputCommitBlobs.containsKey(u)) {
                errorExit("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }
    }

    /** Returns the latest split commitFile for two branches. */
    private File findLatestSplitCommit(File headCommitFileA, File headCommitFileB) {
        Set<File> pSetA = new HashSet<>();

        for (File a = headCommitFileA; a != null; a = commits.get(a)) {
            pSetA.add(a);
        }

        for (File b = headCommitFileB; b != null; b = commits.get(b)) {
            if (pSetA.contains(b)) {
                return b;
            }
        }
        return null;
    }

    /** Combines two file contents when merging in conflict. */
    private void mergeConflict(String filePathName, File curFile, File givenFile) {
        String curContent = "";
        String givenContent = "";
        if (curFile != null) {
            curContent = readContentsAsString(curFile);
        }

        if (givenFile != null) {
            givenContent = readContentsAsString(givenFile);
        }

        String mergedContent = "<<<<<<< HEAD\n"
                + curContent
                + "=======\n"
                + givenContent
                + ">>>>>>>";

        writeContents(join(CWD, filePathName), mergedContent);
    }
}
