package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> a = new AListNoResizing<>();
        BuggyAList<Integer> b = new BuggyAList<>();

        for (int i = 3; i < 6; i += 1) {
            a.addLast(i);
            b.addLast(i);
        }

        for (int k = 0; k < 3; k += 1) {
            int aRemoved = a.removeLast();
            int bRemoved = b.removeLast();
            assertEquals(aRemoved, bRemoved);
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int sizeB = B.size();
                System.out.println("size: " + size);
                assertEquals(size, sizeB);
            } else if (operationNumber == 2) {
                // getLast
                if (L.size() > 0 & B.size() > 0) {
                    int last = L.getLast();
                    int lastB = B.getLast();
                    System.out.println(last);
                    assertEquals(last, lastB);
                }
            } else if (operationNumber == 3) {
                // removeLast
                if (L.size() > 0 & B.size() > 0) {
                    int last = L.removeLast();
                    int lastB = B.removeLast();
                    System.out.println(last);
                    assertEquals(last, lastB);
                }
            }
        }

    }
}
