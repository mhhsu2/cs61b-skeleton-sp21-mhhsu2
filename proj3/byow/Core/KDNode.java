package byow.Core;

import java.util.Arrays;

/**
 * This class represents the node in the K-D tree which partitions the space into two based on the axis selected.
 * The code is followed https://github.com/stanislav-antonov/kdtree/blob/7638a527215276c1d13521ccb17d06a8e3ec5b4e/src/pse/Node.java
 * by @author: Stanislav Antonov
 */
public class KDNode<T extends Dimension> implements Dimension {
    private KDNode<T> parentNode;
    private KDNode<T> leftNode;
    private KDNode<T> rightNode;

    private int depth;
    private T data;



    // Constructors
    KDNode(KDNode<T> parentNode, KDNode<T> leftNode, KDNode<T> rightNode, int depth, T data) {
        this.parentNode = parentNode;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.depth = depth;
        this.data = data;
    }

    public boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }

    public int getDepth() {
        return depth;
    }

    public KDNode<T> getParentNode() {
        return parentNode;
    }

    public KDNode<T> getRightNode() {
        return rightNode;
    }

    public KDNode<T> getLeftNode() {
        return leftNode;
    }

    public T getData() {
        return data;
    }

    @Override
    public int[] getCoords() {
        return data.getCoords();
    }

    public void setParentNode(KDNode<T> node) {
        parentNode = node;
    }

    public void setLeftNode(KDNode<T> node) {
        leftNode = node;
    }

    public void setRightNode(KDNode<T> node) {
        rightNode = node;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof KDNode) {
            int[] c1 = ((KDNode<?>) o).getCoords();
            int[] c2 = this.getCoords();

            return Arrays.equals(c1, c2);
        }

        return false;
    }




}
