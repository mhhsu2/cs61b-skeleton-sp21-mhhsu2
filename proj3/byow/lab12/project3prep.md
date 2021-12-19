# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer:

My implementation is based on for loops and mathematical calculation, which is straightforward but hard to maintain (high cognitive load).
The suggested implentation is by decomposing a big task into smaller pieces (helper methods), which produces more codes, but it is actually less complex.
-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer:

- Tessellating hexagons -> world 
- Hexagon -> room and hallway 

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer:

- addRoom 
- addHallway 
- connectRooms
...

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer:

- Difference: Height/width of the floor space 
- Similarity: Rectangular shape with walls