package byow.Core;

import org.checkerframework.checker.units.qual.K;

import java.util.*;

/**
 * Represents the K-D tree implementation and its algorithm for a 2-D space problem.
 */
public class KDTree {

    private KDNode<? extends Dimension> rootNode;
    private int size;

    // In the 2-D space problem, the dimension is 2 (x, y).
    public final static int DIMENSIONS = 2;

    private class LocationComparator implements Comparator<Dimension> {

        /* Determines which coordinate is used to conduct comparison. */
        private int axis;

        // Constructor for LocationComparator
        LocationComparator(int axis) {
            this.axis = axis;
        }

        @Override
        public int compare(Dimension o1, Dimension o2) {
            return o1.getCoords()[axis] - o2.getCoords()[axis];
        }
    }

    // Constructor for KDTree
    KDTree() {
        this.size = 0;
    }

    KDTree(List<? extends Dimension> items) {
        this.size = 0;
        buildTreeRecursive(items);
    }

    /**
     * Builds the K-D tree with the given items recursively.
     */
    public void buildTreeRecursive(List<? extends Dimension> items) {
        rootNode = buildTreeRecursiveHelper(items, 0);
    }

    // Get methods for instance variable.
    public KDNode<? extends Dimension> getRootNode() {
        return rootNode;
    }

    public int getSize() {
        return size;
    }

    // Helper method for buildTreeRecursive
    private KDNode<? extends Dimension> buildTreeRecursiveHelper(List<? extends Dimension> items, int depth) {
        if (items.isEmpty()) {
            return null;
        }

        // Sort the items by a custom comparator.
        int axisToCompare = depth % DIMENSIONS;
        items.sort(new LocationComparator(axisToCompare));

        int len = items.size();
        int medianNodeIdx = len / 2;

        Dimension medianItem = items.get(medianNodeIdx);
        List<? extends Dimension> leftItems = items.subList(0, medianNodeIdx);
        List<? extends Dimension> rightItems = items.subList(medianNodeIdx + 1, len);

        KDNode<? extends  Dimension> leftNode = buildTreeRecursiveHelper(leftItems, depth + 1);
        KDNode<? extends  Dimension> rightNode = len > 1 ? buildTreeRecursiveHelper(rightItems, depth + 1) : null;

        KDNode<? extends Dimension> medianNode = new KDNode(null, leftNode, rightNode, depth, medianItem);
        size += 1;
        return medianNode;
    }
}
