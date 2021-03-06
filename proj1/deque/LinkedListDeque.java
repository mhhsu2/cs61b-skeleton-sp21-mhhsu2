package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {

    /** Generic doubly linked node implementation */
    private class StuffNode {
        private T item;
        private StuffNode next;
        private StuffNode prev;

        StuffNode(T i, StuffNode n, StuffNode p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    /** Circular Doubly linked list deque implementation */
    private StuffNode sentinel;
    private int size;

    /** Empty LinkedListDeque constructor */
    public LinkedListDeque() {
        sentinel = new StuffNode(null, null, null);
        size = 0;
    }

    /** Add x to the front of the list. */
    public void addFirst(T x) {
        if (sentinel.next == null) {
            sentinel.next = sentinel.prev = new StuffNode(x, sentinel, sentinel);
        } else {
            StuffNode firstNode = new StuffNode(x, sentinel.next, sentinel);
            sentinel.next.prev = firstNode;
            sentinel.next = firstNode;
        }
        size += 1;
    }

    /** Add x to the last of the list. */
    public void addLast(T x) {
        if (sentinel.prev == null) {
            sentinel.prev = sentinel.next = new StuffNode(x, sentinel, sentinel);
        } else {
            StuffNode lastNode = new StuffNode(x, sentinel, sentinel.prev);
            sentinel.prev.next = lastNode;
            sentinel.prev = lastNode;
        }
        size += 1;
    }

    /** return the size of a deque */
    public int size() {
        return size;
    }
//
//    /** Check if a deque is empty of not */
//    public boolean isEmpty() {
//        return size == 0;
//    }

    /** Print the items in a deque from first to last */
    public void printDeque() {
        StuffNode p = sentinel.next;
        for (int i = 0; i < size; i += 1) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println(" ");
    }

    /** Remove and return the first item in a deque */
    public T removeFirst() {
        if (isEmpty()) {
            return null;

        }
        StuffNode firstNode = sentinel.next;
        sentinel.next = firstNode.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return firstNode.item;
    }

    /** Remove and return the last item in a deque */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        StuffNode lastNode = sentinel.prev;
        sentinel.prev = lastNode.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return lastNode.item;
    }

    /** Get the item at the given index,
     * where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     */
    public T get(int index) {
        if (this.isEmpty()) {
            return null;
        }

        StuffNode node = sentinel.next;
        for (int i = 0; i < index; i += 1) {
            if (node.next == sentinel) {
                return null;
            }
            node = node.next;
        }
        return node.item;
    }

    public T getRecursive(int index) {
        StuffNode node = getRecursiveNode(index, sentinel.next);
        if (node != null) {
            return node.item;
        }
        return null;
    }

    private StuffNode getRecursiveNode(int index, StuffNode node) {
        // When circles back, no item is found, return null.
        if (node == sentinel) {
            return null;
        }
        if (index == 0) {
            return node;
        } else {
            return getRecursiveNode(index - 1, node.next);
        }
    }


    /** Return an iterator. */
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private StuffNode p;

        LinkedListDequeIterator() {
            p = sentinel;
        }

        public boolean hasNext() {
            // Support empty deque.
            if (p.next == null) {
                return false;
            }
            return p.next.item != null;
        }

        public T next() {
            T returnItem = p.next.item;
            p = p.next;
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
        if (other.size() != this.size()) {
            return false;
        }

        for (int i = 0; i < other.size(); i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }
}
