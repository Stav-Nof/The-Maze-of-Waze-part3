package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.google.gson.Gson;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.EdgeData;
import dataStructure.edge_data;
import dataStructure.node_data;
import gameUtils.Fruit;
import gameUtils.robot;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI implements Runnable {
	game_service game;
	DGraph g;
	int robots;
	GameServer GameServer;
	int level;
	int thread;
	Graph_Algo ga;
	public String type;
	int movesCounter = 0;

	public MyGameGUI() {
		this.level = -1;
		openWindow();
	}

	/*
	 * returns the level of this game.
	 */
	public synchronized int getLevel() {
		return this.level;
	}
	/*
	 * set the level of this game.
	 */
	public synchronized void setLevel(int level) {
		this.level = level;
	}

	/*
	 * Sets the size of the Window of the game.
	 */
	private void openWindow() {
		StdDraw.setCanvasSize(1000, 500);
	}

	/*
	 * This method draws the graph. In more details, this function draws the graph with certain scales 
	 * that we defined in the function, gets the location of each Node , draws them and draws there edges on the Window.
	 * This method also sets colors and all what is linked to graphics and visualization of the graph.
	 */
	private void drawGraph() {
		Collection<node_data> nodes = this.g.getV();
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		for (node_data i : nodes) {
			Point3D temp = i.getLocation();
			double tempX = temp.x();
			double tempY = temp.y();
			if(tempX > maxX) maxX = tempX;
			if(tempX < minX) minX = tempX;
			if(tempY > maxY) maxY = tempY;
			if(tempY < minY) minY = tempY;
		}
		StdDraw.setXscale(minX-0.0005, maxX+0.0005);
		StdDraw.setYscale(minY-0.0005, maxY+0.0005);
		StdDraw.setPenColor(Color.BLACK);
		for (node_data i : nodes) {
			double x0 = i.getLocation().x();
			double y0 = i.getLocation().y();
			Collection<edge_data> edges = this.g.getE(i.getKey());
			for (edge_data j : edges) {
				double x1 = this.g.getNode(j.getDest()).getLocation().x();
				double y1 = this.g.getNode(j.getDest()).getLocation().y();
				StdDraw.line(x0, y0, x1, y1);
			}
		}
		StdDraw.setPenColor(Color.RED);
		for (node_data i : nodes) {
			StdDraw.setPenRadius();
			Point3D Location = i.getLocation();
			StdDraw.filledCircle(Location.x(), Location.y(), 0.0001);
			StdDraw.setFont(new Font("arial", Font.PLAIN, 20));
			StdDraw.text(Location.x() + 0.0002, Location.y() + 0.0002, "" + i.getKey());
		}
	}


	/*
	 * This method gets each Fruit (the string , which is the path to a picture) and draws it on the graph by getting its location and its image.
	 */
	private void drawFruit() {
		List<String> Fruit = this.game.getFruits();
		for (String i : Fruit) {
			Fruit temp = new Fruit(i);
			StdDraw.picture(temp.getLocation().x(), temp.getLocation().y(), temp.getImage(), 0.0008, 0.0008);
		}
	}

	/*
	 * This method gets each Robot (the string , which is the path to a picture) and draws it on the graph by getting its location and its image.
	 */
	private void drawrobots() {
		List<String> robots = this.game.getRobots();
		for (String i : robots) {
			robot temp = new robot(i);
			StdDraw.picture(temp.getLocation().x(), temp.getLocation().y(), temp.getImage(), 0.0008, 0.0008);
		}
	}

	/*
	 * This method make a visualization of the time while the game is running.
	 */
	public void drawTime() {
		long time = this.game.timeToEnd();
		StdDraw.setPenColor(Color.ORANGE);
		if(this.game.isRunning())
			StdDraw.textRight(StdDraw.xmax-0.0005, StdDraw.ymax-0.0005, "" + time/1000);
		else StdDraw.textRight(StdDraw.xmax-0.0005, StdDraw.ymax-0.0005, "game over");
	}

	/*
	 * This method inits the Game from the server.
	 */
	private void initGameServer(String s) {
		Gson gson = new Gson();
		MyGameGUI temp = gson.fromJson(s, MyGameGUI.class);
		this.GameServer = temp.GameServer;
	}


	private class GameServer {
		int robots;
	}

	/*
	 * This method enables the user to choose which level he wants to play. 
	 * Depending on the level, the graph will be loaded with its fruits and Robots. In this mode, the user can choose from where he wants to move the robots to.
	 * This method will use other methods in order to set everything in place. 
	 */
	public void manualGame() {
		int levelToPrint = levelSelect();
		this.GameServer = new GameServer();
		this.level = levelToPrint;
		this.game = Game_Server.getServer(levelToPrint - 1);
		this.initGameServer(game.toString());
		this.robots = this.GameServer.robots;
		game = Game_Server.getServer(this.level-1);
		this.g = new DGraph();
		this.g.init(this.game.getGraph());
		this.ga = new Graph_Algo(this.g);
		this.drawGraph();
		this.drawFruit();
		this.addManualRobots();
		this.game.startGame();
		StdDraw.enableDoubleBuffering();
		while (this.game.isRunning()) {
			startGameManual();
			runGame();
		}
		StdDraw.disableDoubleBuffering();
		StdDraw.clear();
		end();

	}

	/*
	 * This method enables the user to choose which level he wants to play but doesn't give him the choice to move the robots. 
	 * Everything is done in an automatic manner.  
	 */
	public void automaticGameWithKML() {
		int levelToPrint = levelSelect();
		this.GameServer = new GameServer();
		this.level = levelToPrint;
		this.game = Game_Server.getServer(levelToPrint);
		this.initGameServer(game.toString());
		this.robots = this.GameServer.robots;
		game = Game_Server.getServer(this.level);
		this.g = new DGraph();
		this.g.init(this.game.getGraph());
		this.ga = new Graph_Algo(this.g);
		this.drawGraph();
		this.drawFruit();
		this.addAutomaticlRobots();
		this.drawrobots();
		KML_Logger.openFile((level) + ".kml");
		Game_Server.login(9999);//TODO add your id 
		this.game.startGame();
		StdDraw.enableDoubleBuffering();
		game_service temp = this.game;
		int counter = 0;
		Runnable gameShow = new Runnable() {
			@Override
			public void run() {
				moveGame(temp);

			}
		};
		Thread thread1 = new Thread(gameShow);
		thread1.start();
		
		while (this.game.isRunning()) {
			startGameAutomatic();
			runGame();
			List<String> fruits = this.game.getFruits();
			List<String> robots = this.game.getRobots();

			Runnable runKML = new Runnable() {
				@Override
				public void run() {
					LocalDateTime time = java.time.LocalDateTime.now();
					for (String i : fruits) {
						KML_Logger.addFruit(level + ".kml", i, time);
					}
					for (String i : robots) {
						KML_Logger.addRobot(level + ".kml", i, time);
					}
				}
			};
			Thread thread = new Thread(runKML);
			thread.start();

		}
		StdDraw.disableDoubleBuffering();
		StdDraw.clear();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end();
		KML_Logger.closeFile(level + ".kml");
		System.out.println(this.movesCounter);
		game.sendKML(KML_Logger.KMLtoString(level + ".kml"));
	}


	public void automaticGame() {
		int levelToPrint = levelSelect();
		this.GameServer = new GameServer();
		this.level = levelToPrint;
		this.game = Game_Server.getServer(levelToPrint);
		this.initGameServer(game.toString());
		this.robots = this.GameServer.robots;
		game = Game_Server.getServer(this.level);
		this.g = new DGraph();
		this.g.init(this.game.getGraph());
		this.ga = new Graph_Algo(this.g);
		this.drawGraph();
		this.drawFruit();
		this.addAutomaticlRobots();
		this.drawrobots();
		this.game.startGame();
		StdDraw.enableDoubleBuffering();
		game_service temp = this.game;
		Runnable gameShow = new Runnable() {
			@Override
			public void run() {
				moveGame(temp);

			}
		};
		Thread thread1 = new Thread(gameShow);
		thread1.start();
		
		while (this.game.isRunning()) {
			startGameAutomatic();
			runGame();
		}
		StdDraw.disableDoubleBuffering();
		StdDraw.clear();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end();
		System.out.println(this.movesCounter);
	}

	/*
	 * Like its name says, this method enables the user to add Robots manually. 
	 * The function allow the user to place the robots on a specific node by giving it's key number.
	 */
	public boolean addManualRobots() {
		for (int i = 0; i < this.robots; i++) {
			final JFrame addManual = new JFrame();
			int robotI = 0;
			String robotS = JOptionPane.showInputDialog(addManual,"enter a vertex key to add robot to\nrobot id: " + i, null);
			if(robotS == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return false;
			}
			try {
				robotI = Integer.parseInt(robotS);
			}
			catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you entered a char instead of a number!\ngame has bin cancel");
				return false;
			}
			if (this.g.getNode(robotI) == null) {
				JOptionPane.showMessageDialog(null, "the key you entered doesnt exist\ngame has bin cancel");
				return false;
			}
			this.game.addRobot(robotI);
			this.drawrobots();
		}
		return true;
	}

	/*
	 * Returns an integer and allows the user to select a specific level from the 24 levels existing.
	 */
	private int  levelSelect() {
		String levelS = JOptionPane.showInputDialog("select a level Between 0 and 23\nAny other character for random", null);
		int level = -1;
		try {
			level = Integer.parseInt(levelS);
		}catch (Exception e1) {
			level = (int) (Math.random()*25);
		}
		if (level < 0 || level > 23) {
			level = (int) (Math.random()*25);
		}
		return level;
	}

	/*
	 * This methods is used by the manualGame function above, it enables the user to select a destination node to go to.
	 */
	public void startGameManual() {
		List<String> robots = this.game.move();
		this.movesCounter++;
		for (String i : robots) {
			robot temp = new robot(i);
			if (temp.getDest() == -1) {
				String robotDes = JOptionPane.showInputDialog(new JFrame(),"select a node to go to\nrobot id: " + temp.id + " corent node: " + temp.src, null);
				int des = -1;
				try {
					des = Integer.parseInt(robotDes);
				}catch (Exception e) {
				}
				this.game.chooseNextEdge(temp.getId(), des);
			}
		}
	}

	/*
	 * This methods is used by the automaticGame function above, this function will start the game and every action is done 
	 * automatically : how the robots move, how the fruits are set on the graph ,etc...
	 */
	public void startGameAutomatic() {
		List<String> fruitsS = this.game.getFruits();
		LinkedList<Fruit> fruits = new LinkedList<Fruit>();
		for (String string : fruitsS) {
			fruits.add(new Fruit(string));
		}
		List<String> robots = this.game.getRobots();
		for (String i : robots) {
			System.out.println(i);
			robot temp = new robot(i);
			if (temp.getDest() == -1) {
				double epsilon = 0.000000001;
				int dest = -1;
				Collection<node_data> Nodes = this.g.getV();
				double distance = Double.POSITIVE_INFINITY;
				Fruit theChosenOne = null;
				edge_data fruitIn = null;
				for (Fruit f : fruits) {
					if (f.onSight) continue;
					for (node_data i1 : Nodes) {
						for (node_data j : Nodes) {
							double x1 = i1.getLocation().x(), x2 = j.getLocation().x();
							double y1 = i1.getLocation().y(), y2 = j.getLocation().y();
							double m = (y1 - y2) / (x1 - x2);
							double y = (m * (f.location.x() - x1)) + y1;
							if (y + epsilon > f.getLocation().y() && y - epsilon < f.getLocation().y())
								fruitIn = this.g.getEdge(i1.getKey(), j.getKey());
						}
					}
					if (fruitIn != null) {
						if (f.type == -1) {
							double srcY = this.g.getNode(fruitIn.getSrc()).getLocation().y();
							double destY = this.g.getNode(fruitIn.getDest()).getLocation().y();
							if (srcY > destY) {
								double tempDistance = this.ga.shortestPathDist(temp.src, fruitIn.getSrc());
								if (tempDistance < distance) {
									distance = tempDistance;
									theChosenOne = f;
									dest = fruitIn.getSrc();
									if (dest == temp.src) {
										dest = fruitIn.getDest();
									}
								}
							}
							else {
								double tempDistance = this.ga.shortestPathDist(temp.src, fruitIn.getDest());
								if (tempDistance < distance) {
									distance = tempDistance;
									theChosenOne = f;
									dest = fruitIn.getDest();
									if (dest == temp.src) {
										dest = fruitIn.getSrc();
									}
								}
							}
						}
						else {
							double srcY = this.g.getNode(fruitIn.getSrc()).getLocation().y();
							double destY = this.g.getNode(fruitIn.getDest()).getLocation().y();
							if (srcY < destY) {
								double tempDistance = this.ga.shortestPathDist(temp.src, fruitIn.getSrc());
								if (tempDistance < distance) {
									distance = tempDistance;
									theChosenOne = f;
									dest = fruitIn.getSrc();
									if (dest == temp.src) {
										dest = fruitIn.getDest();
									}
								}
							}
							else {
								double tempDistance = this.ga.shortestPathDist(temp.src, fruitIn.getDest());
								if (tempDistance < distance) {
									distance = tempDistance;
									theChosenOne = f;
									dest = fruitIn.getDest();
									if (dest == temp.src) {
										dest = fruitIn.getSrc();
									}
								}
							}
						}
					}
				}

				if (dest == -1) {
					System.out.println("Error"); 
					this.game.chooseNextEdge(temp.getId(), -1);
				}
				else {
					List<node_data> path =  this.ga.shortestPath(temp.src, dest);
					theChosenOne.onSight = true;
					this.game.chooseNextEdge(temp.getId(), path.get(1).getKey());
				}
			}
		}
	}


	public static void moveGame(game_service game) {
		int counter = 0;
		while (game.isRunning()){
			game.move();
			counter++;
			try {
				Thread.sleep(50);		//TODO
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(counter);
	}


	/*
	 * Using the StdDraw class and sets the Graph, its timer , robots , fruits and at the end shows the Graphic interface with all its components. 
	 */
	public void runGame() {
		StdDraw.clear();
		drawGraph();
		drawFruit();
		drawrobots();
		drawTime();
		StdDraw.show();
	}

	/*
	 * When the game ends, this method will show on the screen a "game end message" and how much points were gained during the game.
	 */
	public void end() {
		int points = 0;
		for (String i : this.game.getRobots()) {
			robot temp = new robot(i);
			points = points + temp.getPoints();
		}
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.textRight(((StdDraw.xmax + StdDraw.xmin) / 2), ((StdDraw.ymax + StdDraw.ymin) / 2) + 0.001, "game end");
		StdDraw.textRight(((StdDraw.xmax + StdDraw.xmin) / 2), ((StdDraw.ymax + StdDraw.ymin) / 2), "your points: " + points);
	}

	/*
	 * This method adds Robots automatically to the graph. It is used in the automaticGame method above. 
	 * Depending on the location of each fruit on the graph, the robot will be set on the graph in order to get the highest score.
	 */
	public void addAutomaticlRobots() {		
		double epsilon = 0.000000001;
		List<String> fruitsString = this.game.getFruits();
		LinkedList<Fruit> fruits = new LinkedList<Fruit>();
		for (String string : fruitsString) {
			fruits.add(new Fruit(string));
		}
		int x = 0;
		for (; x < this.robots; x++) {
			Fruit max = null;
			for (Fruit fruit : fruits) {
				if (max == null)max = fruit;
				else if(max.value < fruit.value);
				max = fruit;
			}
			Collection<node_data> Nodes = this.g.getV();
			edge_data fruitIn = null;
			for (node_data i : Nodes) {
				for (node_data j : Nodes) {
					if (i.getKey() == j.getKey())continue;
					double x1 = i.getLocation().x(), x2 = j.getLocation().x();
					double y1 = i.getLocation().y(), y2 = j.getLocation().y();
					double m = (y1 - y2) / (x1 - x2);
					double y = (m * (max.location.x() - x1)) + y1;
					if (y + epsilon > max.getLocation().y() && y - epsilon < max.getLocation().y()) fruitIn = this.g.getEdge(i.getKey(), j.getKey());
					if (fruitIn != null)break;
				}

				if (fruitIn != null)break;
			}
			int srcKey = fruitIn.getSrc();
			int destKey = fruitIn.getDest();
			if (max.type == -1) {
				this.game.addRobot(Math.max(srcKey, destKey));

			}
			else {
				this.game.addRobot(Math.min(srcKey, destKey));
			}
			fruits.remove(max);
		}
	}

	/*
	 * If the selected mode was "manual", the threads in charge will be in action to run the game in manual mode.
	 * If the selected mode was automatic, the game will run through different threads.
	 */
	@Override
	public void run() {
		if (this.type.equals("manual")) manualGame();
		if (this.type.equals("automatic")) automaticGame();
		if (this.type.equals("automatic with KML")) automaticGameWithKML();

	}
}