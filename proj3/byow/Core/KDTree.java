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

    /**
     * A comparator used to compare two objects based on a partition axis.
     */
    private static class LocationComparator implements Comparator<Dimension> {
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

    /**
     * A custom class represents MaxPriorityQueue
     * based on the distance to the target in K-nearest neighbor search.
     */
    private static class MaxDistancePriorityQueue extends PriorityQueue<Dimension> {

        /**
         * A comparator used in the MaxDistancePriorityQueue.
         */
        private static class MaxDistanceComparator implements Comparator<Dimension> {
            private Dimension target;

            MaxDistanceComparator(Dimension t) {
                this.target = t;
            }

            @Override
            public int compare(Dimension o1, Dimension o2) {
                Double d1 = distance(o1, target);
                Double d2 = distance(o2, target);
                return -1 * d1.compareTo(d2);
            }
        }

        MaxDistancePriorityQueue(Dimension t) {
            super(11, new MaxDistanceComparator(t));
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

    /**
     * Returns the nearest node to the target.
     */
    public KDNode<? extends Dimension> nearestNeighbor(Dimension target) {
        return nearest(rootNode, target, null);
    }

    public List<KDNode<? extends Dimension>> kNearestNeighbor(Dimension target, int k) {
        MaxDistancePriorityQueue pq = new MaxDistancePriorityQueue(target);
        kNearest(rootNode, target, pq, k);

        List<KDNode<? extends Dimension>> kNearestNodes = new ArrayList<>();
        for (Dimension item : pq) {
            kNearestNodes.add((KDNode<? extends Dimension>) item);
        }

        return kNearestNodes;
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

    // Helper method for nearestNeighbor.
    private KDNode<? extends Dimension> nearest(KDNode<?> n, Dimension target, KDNode<?> best) {
        if (n == null) {
            return best;
        }

        // Update the best node when there has not been a node
        // or when the distance between current node and the target is smaller.
        if (best == null) {
            best = n;
        } else if (distance(n, target) < distance(best, target)) {
            best = n;
        }

        // Determines which is the good side and which is the bad side.
        KDNode goodNode;
        KDNode badNode;
        int axisToCompare = n.getDepth() % DIMENSIONS;
        LocationComparator comparator = new LocationComparator(axisToCompare);

        int deltaToPartition = comparator.compare(target, n);
        if (deltaToPartition >= 0) {
            goodNode = n.getRightNode();
            badNode = n.getLeftNode();
        } else {
            goodNode = n.getLeftNode();
            badNode = n.getRightNode();
        }

        best = nearest(goodNode, target, best);

        // If bad side could still have something useful to explore.
        if (Math.abs(deltaToPartition) <= distance(best, target)) {
            best = nearest(badNode, target, best);
        }

        return best;
    }

    // Helper method for kNearestNeighbor.
    private void kNearest(KDNode<?> n, Dimension target, MaxDistancePriorityQueue pq, int k) {
        if (n == null) {
            return;
        }

        // Add the node into the priority if
        // 1. size of the queue is below k
        // 2. when the distance between current node and the target is smaller than
        // the head of the node in the MaxDistancePriority Queue.
        if (pq.size() < k) {
            pq.add(n);
        } else if (distance(n, target) < distance(pq.peek(), target)) {
            pq.poll();
            pq.add(n);
        }

        // Determines which is the good side and which is the bad side.
        KDNode goodNode;
        KDNode badNode;
        int axisToCompare = n.getDepth() % DIMENSIONS;
        LocationComparator comparator = new LocationComparator(axisToCompare);

        int deltaToPartition = comparator.compare(target, n);
        if (deltaToPartition >= 0) {
            goodNode = n.getRightNode();
            badNode = n.getLeftNode();
        } else {
            goodNode = n.getLeftNode();
            badNode = n.getRightNode();
        }

        kNearest(goodNode, target, pq, k);

        // If bad side could still have something useful to explore.
        if (Math.abs(deltaToPartition) <= distance(pq.peek(), target)) {
            kNearest(badNode, target, pq, k);
        }
    }

    // Helper method for calculating the Euclidean distance
    // between two nodes in 2D space.
    private static double distance(Dimension a, Dimension b) {
        int[] ca = a.getCoords();
        int[] cb = b.getCoords();

        return Math.sqrt(
                Math.pow(ca[0] - cb[0], 2)
                        + Math.pow(ca[1] - cb[1], 2)
        );
    }
}
