package byow.Core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    // An inner class, edge, represents the relation and weight
    private class Edge {
        private Room src;
        private Room dest;
        private double dist;

        Edge(Room src, Room dest, double dist) {
            this.src = src;
            this.dest = dest;
            this.dist = dist;
        }
    }


    // A map stores rooms and their edges
    private Map<Room, Set<Edge>> roomMap;
    private int numRooms;
    private int numEdges;

    // Constructors
    Graph() {
        this.roomMap = new HashMap<>();
        this.numRooms = 0;
        this.numEdges = 0;
    }

    public void addRoom(Room r) {
        if (!roomMap.containsKey(r)) {
            roomMap.put(r, new HashSet<>());
            this.numRooms += 1;
        }
    }

    public void addEdge(Room roomA, Room roomB) {
        addRoom(roomA);
        addRoom(roomB);

        double d = roomA.getDistance(roomB);
        roomMap.get(roomA).add(new Edge(roomA, roomB, d));
        roomMap.get(roomB).add(new Edge(roomB, roomA, d));
        this.numEdges += 1;
    }

    public Map<Room, Double> getAdjRoomDist(Room r) {
        Map<Room, Double> adjRooms = new HashMap<>();
        for (Edge e : roomMap.get(r)) {
            System.out.println("Room " + e.dest + ", Distance" + e.dist);
            adjRooms.put(e.dest, e.dist);
        }
        return adjRooms;
    }

    public int getNumRooms() {
        return this.numRooms;
    }

    public int getNumEdges() {
        return this.numEdges;
    }
}
