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
public class Repository implements Serializable {
    /**
     * Instance variables:
     * 1. head: the pointer points at the current branch.
     * 2. branches <branchName, commitFile>: a data structure that stores branch names and the head of that branch.
     * 2. commits <commitFile, parentFile>: a data structure that stores all the references of commits in this repository.
     */
    private String head;
    private Map<String, File> branches;
    private Map<File, File> commits;

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
    }

    /** Initializes a Gitlet repository. */
    public void init() {
        /* Create .gitlet workspace. */
        if (GITLET_DIR.exists()) {
            errorExit("A Gitlet version-control system already exists in the current directory.");
        }
        boolean isCreated = GITLET_DIR.mkdir();
        if (isCreated) {
            message("A Gitlet version-control system is initialized.");
        }
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
        File f = new File(filePathName);

    }

    /** Saves the current state of this repo. */
    public void saveRepo() {
        writeObject(REPO_FILE, this);
    }

    /** Loads a repo instance saved with name "HEAD" in the .gitlet directory. */
    public static Repository loadRepo() {
        return readObject(REPO_FILE, Repository.class);
    }

}
