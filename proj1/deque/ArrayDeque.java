package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    /**
     * Invarints:
     *  1. The index of the front is always nextFront + 1
     *  2. The index of the back is always nextBack - 1
     */
    protected T[] items;
    protected int size;
    protected int nextFront;
    private int nextBack;
    private int resizeFactor;
    private double minUsageRatio;

    public ArrayDeque() {
        items = (T []) new Object[8];
        size = 0;
        nextFront = 3;
        nextBack = 4;
        resizeFactor = 2;
        minUsageRatio = 0.25;
    }

    /** Add x to the front of a deque. */
    public void addFirst(T x) {
        items[nextFront] = x;
        size += 1;
        nextFront -= 1;

        // When nextFront < 0, nextFront goes back to the end of the array.
        if (nextFront < 0) {
            nextFront = items.length - 1;
        }

        // Resize array if nextFront == nextBack.
        if (nextFront == nextBack) {
            resize(items.length * resizeFactor);
        }

    }

    /** Add x to the back of a deque. */
    public void addLast(T x) {
        items[nextBack] = x;
        size += 1;
        nextBack += 1;

        // When nextBack > items.length - 1, nextBack goes back to the beginning of the array.
        if (nextBack > items.length - 1) {
            nextBack = 0;
        }


        // Resize array if nextFront == nextBack.
        if (nextFront == nextBack) {
            resize(items.length * resizeFactor);
        }
    }

    /** Remove the first item in a deque. */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        int idxFront = nextFront + 1;
        if (idxFront > items.length - 1) {
            idxFront = 0;
        }
        T first = items[idxFront];
        items[idxFront] = null;
        size -= 1;

        // Reset nextFront;
        nextFront = idxFront;

        // Resize an array if usage ratio belows usageRatio.
        double usageRatio = (double) size / items.length;
        if (items.length > 8 && usageRatio < minUsageRatio) {
            resize(items.length / resizeFactor);
        }
        return first;
    }

    /** Remove the first item in a deque. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        int idxBack = nextBack - 1;
        if (idxBack < 0) {
            idxBack = items.length - 1;
        }
        T last = items[idxBack];
        items[idxBack] = null;
        size -= 1;

        // Reset nextBack;
        nextBack = idxBack;

        // Resize an array if usage ratio belows usageRatio.
        double usageRatio = (double) size / items.length;
        if (items.length > 8 && usageRatio < minUsageRatio) {
            resize(items.length / resizeFactor);
        }
        return last;
    }


    /** Resize an array for a given capacity. */
    private void resize(int capacity) {
        T[] resizedArray = (T[]) new Object[capacity];

        int idxFront = nextFront + 1;
        int idxBack = nextBack - 1;

        if (idxBack > idxFront) {
            System.arraycopy(items, idxFront, resizedArray, 0, size);
        } else {
            System.arraycopy(items, idxFront, resizedArray, 0, items.length - idxFront);
            System.arraycopy(items, 0, resizedArray,
                    items.length - idxFront, size - (items.length - idxFront));
        }
        items = resizedArray;
        nextFront = items.length - 1;
        nextBack = size;
    }

    /** Print the items in a deque from first to last. */
    public void printDeque() {
        int p = nextFront + 1;
        for (int i = 0; i < size; i += 1) {
            // If hit the end of an array, start from the beginning.
            if (p > items.length - 1) {
                p = 0;
            }

            System.out.print(items[p] + " ");
            p += 1;
        }
        System.out.println(" ");
    }

    /** Return the size of a deque. */
    public int size() {
        return size;
    }

    /** Get the item at the given index. */
    public T get(int index) {
        int relativeIndex = nextFront + 1 + index;
        if (relativeIndex > items.length - 1) {
            relativeIndex -= items.length;
        }
        return items[relativeIndex];
    }
//
//    /** Check if a deque is empty of not */
//    public boolean isEmpty() {
//        return size == 0;
//    }

    /** Return an iterator. */
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int pos = nextFront + 1;

        public boolean hasNext() {
//            if (pos == nextBack) { return false; } // Hit the end of the deque.
            if (pos > items.length - 1) {
                pos = 0;
            }
            return items[pos] != null;
        }

        public T next() {
            T returnItem = items[pos];
            pos += 1;
            return returnItem;
        }
    }

    /** Returns whether the parameter o is equal to the Deque. */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }

        Deque other = (Deque) o;
        if (other.size() != this.size()) { return false; }

        for (int i = 0; i < other.size(); i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }
}
