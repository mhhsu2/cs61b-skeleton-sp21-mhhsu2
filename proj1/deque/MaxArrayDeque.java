package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> ct;

    public MaxArrayDeque(Comparator<T> assignedComparator) {
        super();
        ct = assignedComparator;
    }

    private static class IntComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }

    private static class LastCharComparator implements Comparator<String> {
        public int compare(String a, String b) {
            String lastCharA = a.substring(a.length() - 1);
            String lastCharB = b.substring(b.length() - 1);
            return lastCharA.compareTo(lastCharB);
        }
    }

    public static Comparator<String> getLastCharComparator() {
        return new LastCharComparator();
    }

    public static Comparator<Integer> getIntComparator() {
        return new IntComparator();
    }

    public T max() {
        if (this.size() == 0) {
            return null;
        }

        T maxItem = this.get(0);
        for (T item : this) {
            if (this.ct.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }

    public T max(Comparator<T> c) {
        if (this.size() == 0) {
            return null;
        }

        T maxItem = this.get(0);
        for (T item : this) {
            if (c.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }
}
