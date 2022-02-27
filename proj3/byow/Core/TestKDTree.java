package byow.Core;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestKDTree {

    @Test
    public void testBuildTreeRecursive() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(new Position(0, 38), 3, 3));
        rooms.add(new Room(new Position(5, 24), 5, 5));
        rooms.add(new Room(new Position(12, 12), 3, 3));
        rooms.add(new Room(new Position(24, 5), 5, 5));
        rooms.add(new Room(new Position(38, 0), 5, 5));

        KDTree roomTree = new KDTree();
        roomTree.buildTreeRecursive(rooms);

        assertEquals("Size of the K-D tree", 5, roomTree.getSize());
        assertEquals("Root node x", 12, roomTree.getRootNode().getCoords()[0]);
        assertEquals("left child y", 38, roomTree.getRootNode().getLeftNode().getCoords()[1]);
        assertEquals("right child y", 5, roomTree.getRootNode().getRightNode().getCoords()[1]);
        assertEquals("left/left child x", 5, roomTree.getRootNode().getLeftNode().getLeftNode().getCoords()[0]);
        assertEquals("right/left child x", 38, roomTree.getRootNode().getRightNode().getLeftNode().getCoords()[0]);
        assertNull("right/right child", roomTree.getRootNode().getRightNode().getRightNode());
    }

    @Test
    public void testNearestNeighbor() {
        List<Room> rooms = new ArrayList<>();

        Room r1 = new Room(new Position(0, 38), 3, 3);
        Room r2 = new Room(new Position(5, 24), 5, 5);
        Room r3 = new Room(new Position(12, 12), 3, 3);
        Room r4 = new Room(new Position(24, 5), 5, 5);
        Room r5 = new Room(new Position(38, 0), 5, 5);

        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);
        rooms.add(r4);
        rooms.add(r5);

        KDTree roomTree = new KDTree(rooms);

        Room targetRoom5 = new Room(new Position(35, 2), 5, 5);
        Room nearestRoom5 = (Room) roomTree.nearestNeighbor(targetRoom5).getData();
        assertEquals(r5, nearestRoom5);

        Room targetRoom2 = new Room(new Position(-5, 24), 5, 5);
        Room nearestRoom2 = (Room) roomTree.nearestNeighbor(targetRoom2).getData();
        assertEquals(r2, nearestRoom2);

    }

    @Test
    public void testKNearestNeighbor() {
        List<Room> rooms = new ArrayList<>();

        Room r1 = new Room(new Position(0, 38), 3, 3);
        Room r2 = new Room(new Position(5, 24), 5, 5);
        Room r3 = new Room(new Position(12, 12), 3, 3);
        Room r4 = new Room(new Position(24, 5), 5, 5);
        Room r5 = new Room(new Position(38, 0), 5, 5);

        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);
        rooms.add(r4);
        rooms.add(r5);

        KDTree roomTree = new KDTree(rooms);

        Room targetRoom = new Room(new Position(6, 12), 5, 5);
        List<KDNode<? extends Dimension>> kNearestNodes = roomTree.kNearestNeighbor(targetRoom, 3);

        assertEquals("Size of returned list:", 3, kNearestNodes.size());
        assertEquals(r4, kNearestNodes.get(0).getData());
        assertEquals(r3, kNearestNodes.get(1).getData());
        assertEquals(r2, kNearestNodes.get(2).getData());
    }
}
