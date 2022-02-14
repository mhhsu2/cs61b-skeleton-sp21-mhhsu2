package byow.Core;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestGraph {
    @Test
    public void testAddRoom() {
        Graph g = new Graph();
        Room r = new Room(new Position(5, 3), 5, 5);
        g.addRoom(r);

        assertEquals("# of rooms in the graph:", 1, g.getNumRooms());
    }

    @Test
    public void testAddEdge() {
        Graph g = new Graph();
        Room rA = new Room(new Position(5, 3), 3, 3);
        Room rB = new Room(new Position(9, 6), 5, 5);

        g.addEdge(rA, rB);
        Map<Room, Double> adj = g.getAdjRoomDist(rA);
        double d = adj.get(rB);

        assertEquals("# of rooms in the graph:", 2, g.getNumRooms());
        assertEquals("# of edges in the graph:", 1, g.getNumEdges());
        assertEquals(5.0, d, 0.0);
    }
}
