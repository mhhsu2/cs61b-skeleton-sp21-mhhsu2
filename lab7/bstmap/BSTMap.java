package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;
    private int size = 0;

    /** Node implementation for a binary search tree. */
    private class BSTNode {
        private K key;
        private V val;
        private BSTNode left;
        private BSTNode right;

        BSTNode(K k, V v) {
            key = k;
            val = v;
        }
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return containsKey(key, this.root);
    }

    private boolean containsKey(K key, BSTNode node) {
        if (node == null) {
            return false;
        }
        int equality = key.compareTo(node.key);
        if (equality == 0) {
            return true;
        } else if (equality > 0) {
            return containsKey(key, node.right);
        } else {
            return containsKey(key, node.left);
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(key, this.root);
    }

    private V get(K key, BSTNode node) {
        if (node == null) {
            return null;
        }

        int equality = key.compareTo(node.key);
        if (equality == 0) {
            return node.val;
        } else if (equality > 0) {
            return get(key, node.right);
        } else {
            return get(key, node.left);
        }
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /** Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        this.root = put(key, value, this.root);
    }

    private BSTNode put(K key, V value, BSTNode node) {
        if (node == null) {
            size += 1;
            return new BSTNode(key, value); // If the input node is empty, add the node here.
        } else {
            int equality = key.compareTo(node.key);
            if (equality == 0) {
                return new BSTNode(key, value);// If the input key == node.key, update the node with new (key, value).
            } else if (equality > 0) {
                node.right = put(key, value, node.right);
            } else {
                node.left = put(key, value, node.left);
            }
        }
        return node;
    }

    /** Prints out the BSTMap in order of increasing key. */
    public void printInOrder() {

    }

    /**
     * Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
