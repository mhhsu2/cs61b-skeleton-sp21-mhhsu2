package deque;

import net.sf.saxon.functions.Minimax;
import org.junit.Test;

import java.util.Comparator;
import static  org.junit.Assert.*;

public class MaxArrayDequeTest {
    @Test
    /* Test IntComparator of MaxArrayDeque */
    public void testIntComparator() {
        Comparator<Integer> ct = MaxArrayDeque.getIntComparator();
        MaxArrayDeque<Integer> a = new MaxArrayDeque<>(ct);
        assertNull("Max should be null.", a.max());

        for (int i = 0; i < 5; i++) {
            a.addFirst(i);
        }

        assertEquals("Max should be 4.", 4, (double) a.max(), 0.0);


        for (int i = 5; i < 10; i++) {
            a.addLast(i);
        }
        assertEquals("Max should be 9.", 9, (double) a.max(), 0.0);
    }

    @Test
    /* Test IntComparator of MaxArrayDeque */
    public void testLastCharComparator() {
        Comparator<String> ct = MaxArrayDeque.getLastCharComparator();
        MaxArrayDeque<String> a = new MaxArrayDeque<>(ct);
        assertNull("Max should be null.", a.max());


        a.addFirst("bbeada");
        a.addFirst("adasdqrt");
        a.addLast("asdezqs");


        assertEquals("Max should be 'adasdqrt'.", "adasdqrt", a.max());

    }

    @Test
    /* Test IntComparator of MaxArrayDeque */
    public void testAssignedComparator() {
        Comparator<Integer> ct = MaxArrayDeque.getIntComparator();
        MaxArrayDeque<String> a = new MaxArrayDeque(ct);
        Comparator<String> assignedCt = MaxArrayDeque.getLastCharComparator();
        assertNull("Max should be null.", a.max(assignedCt));


        a.addFirst("Jack");
        a.addFirst("Alex");
        a.addLast("Brian");
        a.addFirst("NoName");


        assertEquals("Max should be 'Alex'.", "Alex", a.max(assignedCt));

    }
}
