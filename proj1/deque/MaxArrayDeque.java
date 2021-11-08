package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> ct;

    public MaxArrayDeque(Comparator assignedComparator) {
        super();
        ct = assignedComparator;
    }

    public static class IntComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }

    public static class LastCharComparator implements Comparator<String> {
        public int compare(String a, String b) {
            String lastCharA = a.substring(a.length() - 1);
            String lastCharB = b.substring(b.length() - 1);
            return lastCharA.compareTo(lastCharB);
        }
    }

    public T max() {
        if (size == 0) { return null; }
        int front = nextFront + 1;
        if (front > items.length - 1) { front = 0;}
        T maxItem = items[front];
        for (T item : this) {
            if (this.ct.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }

    public T max(Comparator<T> c) {
        if (size == 0) { return null; }
        int front = nextFront + 1;
        if (front > items.length - 1) { front = 0;}
        T maxItem = items[front];
        for (T item : this) {
            if (c.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }
}
