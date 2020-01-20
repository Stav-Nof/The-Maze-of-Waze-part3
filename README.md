![](https://i.imgur.com/FJk2Z3V.png)

This repository based on a our previous project: [The-Maze-of-Waze-part1](https://github.com/Stav-Nof/The-Maze-of-Waze).  
This project represents a game of robots (Pikachu) and pokeballs.

There are two modes of game:
1. Automatic- you choose a level between 1-24 and the server play the best moves for the robots.
2. Manual- you choose a level between 1-24 and you move the robots Towards the pokeballs.  
Note: if you choose any other number, the server will choose random level for you.

All the Pikachu on the graph need to collect the red and blue pokeballs in order to gain as much score and speed as possible.

- Node- A class represents a vertex in the graph defined by an id and has set of operations applicable on a vertex.

- Edge- A class represents an edge in the graph defined by Source and destination vertices and has set of operations applicable on a directional edge(src,dest) in a (directional) weighted graph. 

- Dgraph- A class represents a directional weighted graph.
It has two data structure from type of hash map that represents the vertices and the edges. This class has all the operations to create and change a graph.

- Graph_Algo- A class represents the "regular" Graph Theory algorithms that can be done on a variable graph.

- Fruit - A class that represents an Object which is really an image with a position on the graph. After a set of operations on this object, we will be able to see that this "fruit" is moving on the graph.

- Robot- A class that represents an Object which "picks up" the Fruits on the graph. After that a set of operations are applied on it, the robot will be able to move on the graph from a source node to a destination node and its purpose is to find and pick up the fruits on the graph.

- MyGameGUI- A class that includes all the methods in order to build the game, that is to say, its graphic interface. Starting from the 
interface of the window, selecting levels, adding robots, select manual or automatic mode and starting the game from the server.

- Window- A class represents the graphical user interface.

Writing this class We used These links:
https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html,  
https://stackoverflow.com/questions/16995308/can-you-increase-line-thickness-when-using-java-graphics-for-an-applet-i-dont,  
https://stackoverflow.com/questions/8852560/how-to-make-popup-window-in-java,  
https://www.javatpoint.com/java-jcheckbox  

- DGraphTest & Graph_AlgoTest-Those are JUnit testers that test minor and difficult cases of the four classes methods.

***Best result for any scenario (auto-play):***

![](https://i.ibb.co/bHn7DrQ/score-1.png)
