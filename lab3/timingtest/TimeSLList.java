package timingtest;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        for (int N = 1000; N <= 128000; N *= 2) {
            SLList<Integer> a = new SLList<>();
            int opCount = 0;

            for (int i = 0; i < N; i+=1) {
                a.addFirst(1);
            }

            long startTime = System.nanoTime();
            for (int k = 0; k < 10000; k+=1) {
                a.getLast();
                opCount += 1;
            }
            Double time = (System.nanoTime() - startTime) / 1e9;

            Ns.addLast(N);
            times.addLast(time);
            opCounts.addLast(opCount);
        }

        printTimingTable(Ns, times, opCounts);

    }

}
