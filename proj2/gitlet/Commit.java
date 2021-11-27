package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Min-Hsiu Hsu
 */
public class Commit implements Serializable {
    /**
     * Instance variables:
     * 1. parentId: the SHA-1 code of the parent of the current commit.
     * 2. date: the date of the commit.
     * 3. commitMsg: the description of the commit.
     * 4. blobs: a list of the references of committed files.
     */

    /** The message of this Commit. */
    private File parentFile;
    private Date date;
    private String commitMsg;
    private HashMap<String, File> blobs;

    Commit() {
        this.date = new Date(0);
        this.commitMsg = "initial commit";
        this.blobs = new HashMap<>();
    }

    Commit(File parentFile, String author, String commitMsg, HashMap<String, File> blobs) {
        this.parentFile = parentFile;
        this.date = new Date();
        this.commitMsg = commitMsg;
        this.blobs = blobs;
    }


    /** Saves this commit as a persistent file,
     * and returns the File of this commit.
     */
    public File saveCommit() {
        File commitFile = join(Repository.COMMIT_DIR, getHashCode());
        writeObject(commitFile, this);
        return commitFile;
    }

    /** Loads a saved commit. */
    public static Commit loadCommit(File commitFile) {
        return readObject(commitFile, Commit.class);
    }

    /** Returns the hash code of this commit. */
    public String getHashCode() {
        byte[] serializedCommit = serialize(this);
        return sha1(serializedCommit);
    }

    /** Returns the File of the parent of this commit*/
    public File getParentFile() {
        return parentFile;
    }

    /** Returns the blobs of this commit. */
    public HashMap<String, File> getBlobs() {
        return blobs;
    }
}
