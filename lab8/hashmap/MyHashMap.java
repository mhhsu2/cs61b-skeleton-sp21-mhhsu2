package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Min-Hsiu Hsu
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int initialSize = 16;
    private double maxLoad = 0.75;
    private int size = 0;
    private double resizeFactor = 2.0;
    private HashSet<K> keys;

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(initialSize);
        keys = new HashSet<>();

    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        keys = new HashSet<>();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.maxLoad = maxLoad;
        buckets = createTable(initialSize);
        keys = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    public void put(K key, V value) {
        if (key == null) {
            return;
        }

        /* Resizes the table when reach load > maxLoad */
        double load = (double) size / buckets.length;
        if (load > maxLoad) {
            this.resize((int) resizeFactor * buckets.length);
        }

        Node newNode = createNode(key, value);
        int idx = Math.floorMod(key.hashCode(), buckets.length);

        if (containsKey(key)) {
            for (Node n : buckets[idx]) {
                if (n.key.equals(key)) {
                    n.value = value;
                    return;
                }
            }
        }
        if (buckets[idx] == null) {
            buckets[idx] = createBucket();
        }
        buckets[idx].add(newNode);
        keys.add(key);
        size += 1;
    }

    public void resize(int capacity) {
        Collection<Node>[] oldBuckets = createTable(this.buckets.length);
        System.arraycopy(this.buckets, 0, oldBuckets, 0, this.buckets.length);
        this.buckets = createTable(capacity);
        this.size = 0;

        for (Collection<Node> bucket : oldBuckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    put(node.key, node.value);
                }
            }
        }
    }

    /** Removes all of the mappings from this map. */
    public void clear() {
        buckets = null;
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }

        if (buckets == null) {
            return false;
        }

        int idx = Math.floorMod(key.hashCode(), buckets.length);
        if (buckets[idx] == null) {
            return false;
        }

        for (Node node : buckets[idx]) {
            if (key.equals(node.key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }

        if (buckets == null) {
            return null;
        }

        int idx = Math.floorMod(key.hashCode(), buckets.length);
        if (buckets[idx] == null) {
            return null;
        }

        for (Node node : buckets[idx]) {
            if (key.equals(node.key)) {
                return node.value;
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        return keys;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return this.keySet().iterator();
    }
}
