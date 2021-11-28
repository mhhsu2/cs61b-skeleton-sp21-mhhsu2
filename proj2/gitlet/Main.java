package gitlet;

import static gitlet.Utils.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Min-Hsiu Hsu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            errorExit("Must have at least one argument", (Object) args);
        }

        /* Create a repo or load a saved repo. */
        Repository repo;
        if (Repository.REPO_FILE.exists()) {
            repo = Repository.loadRepo();
        } else {
            repo = new Repository();
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                repo.init();
                break;
            case "add":
                validateNumArgs("add", args, 2);
                String filePathName = args[1];
                repo.add(filePathName);
                break;
            case "commit":
                validateNumArgs("commit", args, 2);
                String commitMsg = args[1];
                repo.commit(commitMsg);
                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                String removePathName = args[1];
                repo.rm(removePathName);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                repo.log();
                break;
        }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
