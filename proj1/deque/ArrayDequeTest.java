package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void testAddFirstNoResizing() {
        ArrayDeque<Integer> aDeque = new ArrayDeque<>();
        for (int i = 3; i < 7; i += 1) {
            aDeque.addFirst(i);
        }

        aDeque.printDeque();
    }

    @Test
    public void testAddFirstResizing() {
        ArrayDeque<Integer> aDeque = new ArrayDeque<>();

        for (int i = 0; i < 24; i += 1) {
            aDeque.addFirst(i);
        }
        aDeque.printDeque();
    }

    @Test
    public void testAddLastResizing() {
        ArrayDeque<Integer> aDeque = new ArrayDeque<>();

        for (int i = 0; i < 100; i += 1) {
            aDeque.addLast(i);
        }
        aDeque.printDeque();
    }

    @Test
    public void testRemoveFirstNoResizing() {
        ArrayDeque<Integer> aDeque = new ArrayDeque<>();

        // Add some items
        for (int i = 0; i < 4; i += 1) {
            aDeque.addFirst(i);
        }
        for (int i = 4; i < 6; i += 1) {
            aDeque.addLast(i);
        }
        System.out.println("Original deque");
        aDeque.printDeque();

        for (int i = 0; i < 4; i += 1) {
            System.out.println("Remove " + aDeque.removeFirst());
        }
        System.out.println("Deque after removal");
        aDeque.printDeque();
    }

    @Test
    public void testRemoveFirstResizing() {
        ArrayDeque<Integer> aDeque = new ArrayDeque<>();

        // Add some items
        for (int i = 0; i < 4; i += 1) {
            aDeque.addFirst(i);
        }
        for (int i = 4; i < 9; i += 1) {
            aDeque.addLast(i);
        }
        for (int i = 9; i < 20; i += 1) {
            aDeque.addFirst(i);
        }
        System.out.println("Original deque");
        aDeque.printDeque();

        for (int i = 0; i < 17; i += 1) {
            System.out.println("Remove " + aDeque.removeFirst());
        }
        System.out.println("Deque after removal");
        aDeque.printDeque();
    }

    @Test
    public void testRemoveLastResizing() {
        ArrayDeque<Integer> aDeque = new ArrayDeque<>();

        // Add some items
        for (int i = 0; i < 4; i += 1) {
            aDeque.addFirst(i);
        }
        for (int i = 4; i < 9; i += 1) {
            aDeque.addLast(i);
        }
        for (int i = 9; i < 20; i += 1) {
            aDeque.addFirst(i);
        }
        System.out.println("Original deque");
        aDeque.printDeque();

        for (int i = 0; i < 17; i += 1) {
            System.out.println("Remove " + aDeque.removeLast());
        }
        System.out.println("Deque after removal");
        aDeque.printDeque();
    }

    @Test
    public void testGet() {
        ArrayDeque<Integer> aDeque = new ArrayDeque<>();

        // Add some items
        for (int i = 0; i < 4; i += 1) {
            aDeque.addFirst(i);
        }
        for (int i = 4; i < 9; i += 1) {
            aDeque.addLast(i);
        }
        for (int i = 9; i < 20; i += 1) {
            aDeque.addFirst(i);
        }
        System.out.println("Original deque");
        aDeque.printDeque();

        int index = 5;
        System.out.println("Get index " + index + " : " + aDeque.get(index));
    }

    @Test
    public void testEmptyReturnNull() {
    ArrayDeque<Integer> aDeque = new ArrayDeque<>();

    boolean passed1 = false;
    boolean passed2 = false;
    assertEquals("Should return null when removeFirst is called on an empty Deque,", null, aDeque.removeFirst());
    assertEquals("Should return null when removeLast is called on an empty Deque,", null, aDeque.removeLast());
    }
}
