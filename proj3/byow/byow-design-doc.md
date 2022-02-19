# BYOD Design Documentation

**Name**: Min-Hsiu Hsu

## Classes and Data Structures

### World
This class represents the world of the BYOW program, which stores the information about rooms, hallways, etc.

#### Fields
- `private int width`: the width of a world.
- `private int height`: the height of a world.
- `private TETile[][] tiles`: the tiles of a world.
- `private SomeDataStructures rooms`:
- `private long seed`: the seed used to control the pseudorandom of a world.
- `private Random random`: the random object from the `java.util.Random` class.

### Room
This class is the basic unit in the world.

#### Fields
- `private int width`: the width of a room.
- `private int height`: the height of a room.
- `private Position pos`: the position of a room (the bottom left corner).

### Graph
This class stores the relation between rooms in a world.

#### Fields
- `private Map<Room, Set<Edge>> roomMap`: a map stores `Room` objects as keys and a set of corresponding `Edge` objects as values.
- `private int numRooms`: the number of rooms in the graph.
- `private int numEdges`: the number of edges in the graph.

#### Inner Classes
- `private class Edge`: this class is an inner class of a graph, which represents the relation between rooms.
  - `private Room src`: the source room of this edge.
  - `private Room dest`: the destination room of this edge.
  - `private double dist`: the distance between the src and the dest room.

### Position
This class represents a location on the 2-dimensional space.

#### Fields
- `private int x`: the x coordinate of this position.
- `private int y`: the y corrdinate of this position.

## Algorithms

### World

- `public void initWorld()`: initializes a world with `Tileset.NOTHING`.
- `public void genWorld()`: generates a pseudorandom world.
  1. **build rooms sequentially**?
     - Pros:
       - Makes sure no overlaps.
     - Cons:
       - Inefficient if lots of rooms already exist.
     1. Create a `Room` with a random position.
     2. Check overlapping and boundary condition,
        1. If `isOverlap() == True || isOutOfBoundary == True`, don't add the room. 
        2. If not, add the room.
     3. Repeat 1 & 2 until reaches `minOccupancy`. 
  2. build rooms once and then connected them together?
     - Pros:
       - More efficient
     - Cons:
       - How to avoid overlapping?
- `public void addRoom(Position pos, int height, int width)`: Creates a room with a given position and dimension. Checks if the room is valid with `room.isOutOfBoundary` and `room.isOverlapped`. If valid, draws and adds the room to `rooms`. 
- `public void addRandomRooms(int numRooms)`: Adds rooms with random positions and dimensions until `numRooms` rooms exists.

### Room
- `public void draw(TETile[][] tiles)`: draws the room on a world.
- `public boolean isOverlapped(TETile[][] tiles)`: returns true if this room is overlapped with other objects on a world.
- `public boolean isOutOfBoundary(World world)`: returns true if the room is out of the boundary of the world.
- `private void buildWall(TETile[][] tiles)`: builds the wall of a room by substituting one-layer-thick floor tiles with `Tileset.WALL`. 
- `public double getDistance(Room roomB)`: returns the distance from this room to another room.

### Graph
- `public void addRoom(Room r)`: adds a room to the graph.
- `public void addEdge(Room roomA, Room roomB)`: adds an edge which connects two rooms into the graph.
- `public Map<Room, Double> getAdjRoomDist(Room r)`: returns the adjacent rooms of a given room and the distance to those adjacent rooms.
- `public int getNumRooms()`: returns the number of rooms in the graph.
- `public int getNumEdges()`: return the number of edges in the graph.

## Persistence