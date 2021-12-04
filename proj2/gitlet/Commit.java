package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TreeMap;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Min-Hsiu Hsu
 */
public class Commit implements Serializable, Dumpable {
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
    private TreeMap<String, File> blobs;

    Commit() {
        this.date = new Date(0);
        this.commitMsg = "initial commit";
        this.blobs = new TreeMap<>();
    }

    Commit(File parentFile, String commitMsg, TreeMap<String, File> blobs) {
        this.parentFile = parentFile;
        this.date = new Date();
        this.commitMsg = commitMsg;
        this.blobs = blobs;
    }


    /** Saves this commit as a persistent file,
     * and returns the File of this commit.
     */
    public File saveCommit() {
        File commitFile = join(Repository.COMMIT_DIR, getSha1());
        writeObject(commitFile, this);
        return commitFile;
    }

    /** Loads a saved commit. */
    public static Commit loadCommit(File commitFile) {
        if (commitFile == null) {
            return null;
        }
        return readObject(commitFile, Commit.class);
    }

    /** Returns the hash code of this commit. */
    public String getSha1() {
        byte[] serializedCommit = serialize(this);
        return sha1(serializedCommit);
    }

    /** Returns the File of the parent of this commit*/
    public File getParentFile() {
        return parentFile;
    }

    /** Returns the commitMsg of this commit. */
    public String getCommitMsg() {
        return commitMsg;
    }

    /** Returns the blobs of this commit. */
    public TreeMap<String, File> getBlobs() {
        return blobs;
    }

    /** Prints the log od this commit. */
    public void printLog() {
        String pattern = "EEE MMM d HH:mm:ss yyyy Z";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);

        System.out.println("===");
        System.out.println("commit " + getSha1());
        System.out.println("Date: " + dateFormatter.format(date));
        System.out.println(commitMsg);
        System.out.println();
    }

    /** Prints info about this commit by DumpObj class. */
    @Override
    public void dump() {
        System.out.printf("parentFile: %s%ndate: %s%ncommitMsg %s%nblobsKey: %s%n", parentFile, date, commitMsg, blobs.keySet());
    }
}
