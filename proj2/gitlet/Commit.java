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
    private String parentId;
    private Date date;
    private String commitMsg;
    private HashMap<String, String> blobs;

    Commit() {
        this.date = new Date(0);
        this.commitMsg = "initial commit";
    }

    Commit(String parentId, String author, String commitMsg, HashMap<String, String> blobs) {
        this.parentId = parentId;
        this.date = new Date();
        this.commitMsg = commitMsg;
        this.blobs = blobs;
    }


    /** Saves this commit as a persistent file,
     * and returns the File of this commit.
     */
    public File saveCommit() {
        File commitFile = join(Repository.COMMIT_DIR, this.getHashCode());
        writeObject(commitFile, this);
        return commitFile;
    }

    /** Returns the hash code of this commit. */
    public String getHashCode() {
        byte[] serializedCommit = serialize(this);
        return sha1(serializedCommit);
    }

    /** Returns the File of the parent of this commit*/
    public File getParentFile() {
        if (this.parentId == null) {
            return null;
        }

        return join(Repository.COMMIT_DIR, this.parentId);
    }
}
