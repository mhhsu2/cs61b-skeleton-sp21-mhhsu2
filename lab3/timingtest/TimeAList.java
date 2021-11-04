package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        for (int N = 1000; N <= 1000000; N *= 2) {
            AList<Integer> a = new AList<>();
            int opCount = 0;

            long startTime = System.nanoTime();
            for (int i = 0; i < N; i += 1) {
                a.addLast(1);
                opCount += 1;
            }
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1e9;

            // Append results
            Ns.addLast(N);
            times.addLast(duration);
            opCounts.addLast(opCount);
        }


        printTimingTable(Ns, times, opCounts);
    }
}
