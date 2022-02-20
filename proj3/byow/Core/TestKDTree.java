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
}
