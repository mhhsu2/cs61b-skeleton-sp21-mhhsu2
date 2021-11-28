package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable, Dumpable {
    /**
     * Instance variables:
     * 1. head: the pointer points at the current branch.
     * 2. branches <branchName, commitFile>: a data structure that stores branch names and the head of that branch.
     * 2. commits <commitFile, parentFile>: a data structure that stores all the references of commits in this repository.
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

    /** Transient variables */
    private transient Commit headCommit;
    private transient HashMap<String, File> headCommitBlobs;

    /** Persistence structure.
     * CWD                              <==== Whatever the current working directory is.
     *  └── .gitlet                     <==== All persistant data is stored within here
     *      ├── repo                    <==== A Repository instance stored as a file
     *      ├── commits                 <==== Where the commits are stored
     *      ├   ├── commit1             <==== A single Commit instance stored as a file
     *      ├   ├── ...
     *      ├   └── commitN
     *      ├
     *      └── objects                 <==== All objects (serialized files) are stored in this directory
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
        headCommit = getHeadCommit();
        HashMap<String, File> headCommitBlobs = headCommit.getBlobs();
        File prevBlob = headCommitBlobs.get(filePathName);

        if (prevBlob == null) {
            stageFile(filePathName, inputBlob, inputContent);
            stagingArea.put(filePathName, inputBlob);
            return;
        }

        if (prevBlob.equals(inputBlob)) {
            System.out.println("Unstage a file");
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

        if (commitMsg == null) {
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
        /* Unstages the file if it is added in the staging area. */
        stagingArea.remove(filePathName);

        /* Stages the file for removal if it exists in the head commit. */
        headCommitBlobs = getHeadCommit().getBlobs();
        if (headCommitBlobs.containsKey(filePathName)) {
            stagingArea.put(filePathName, null);
        }

        /* Removes the file from the working directory. */
        restrictedDelete(filePathName);

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
        System.out.printf("headCommitFile: %s%nstagingAreaKeys: %s%n", branches.get(head), stagingArea.keySet());
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
}
