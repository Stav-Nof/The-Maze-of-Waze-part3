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
import dataStructure.edge_data;
import dataStructure.node_data;
import gameUtils.Fruit;
import gameUtils.robot;
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
	LinkedList<Fruit> runFruits;
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
		int id = 0;
		String idS = JOptionPane.showInputDialog(new JFrame(),"enter your id", null);
		try {
			id = Integer.parseInt(idS);
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(new JFrame(), "the action was canceled");
			return;
		}
		Game_Server.login(id);
		this.game.startGame();
		StdDraw.enableDoubleBuffering();
		Runnable gameShow = new Runnable() {

/*
 * MyGameGUI implements the interface Runnable, therefore, we have to use the run method in order to 
 * start using the thread. While playing, the thread adds the specific String of each robot and fruit to the kml file.
 */
			@Override
			public void run() {
				moveGame();
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

/*
 * Automatically runs the game by doing many different actions.
 * The graphic interface gives the player to choose wich level he would like to play,
 * all the rest is automatically decided by the server. We can see in this method that 
 * we call the server in order to move the robots and the fruits.
 * Mostly, the game is ran by the help of the server.
 */
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
		Runnable gameShow = new Runnable() {
			@Override
			public void run() {
				moveGame();

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
		double epsilon = 0.00000001;
		int dest = -1;
		if (this.runFruits == null) {
			List<String> tempFruits = this.game.getFruits();
			LinkedList<Fruit> Fruits = new LinkedList<Fruit>();
			for (String string : tempFruits) {
				Fruit newFruits = new Fruit(string);
				Fruits.add(newFruits);
			}
			this.runFruits = Fruits;
		}
		List<String> tempFruits = this.game.getFruits();
		LinkedList<Fruit> Fruits = new LinkedList<Fruit>();
		for (String string : tempFruits) {
			Fruit newFruits = new Fruit(string);
			Fruits.add(newFruits);
		}
		LinkedList<Fruit> todelete = new LinkedList<Fruit>();
		for (Fruit ifruit : this.runFruits) {
			boolean delete = true;
			for (Fruit jfruit : Fruits) {
				if (ifruit.location.x() == jfruit.location.x() && ifruit.location.y() == jfruit.location.y()) delete = false;
			}
			if (delete) todelete.add(ifruit);
		}
		this.runFruits.removeAll(todelete);
		for (Fruit ifruit : Fruits) {
			boolean add = true;
			for (Fruit jfruit : this.runFruits) {
				if (ifruit.location.x() == jfruit.location.x() && ifruit.location.y() == jfruit.location.y()) add = false;
			}
			if (add) this.runFruits.add(ifruit);
		}
		List<String> robots = this.game.getRobots();
		for (String string : robots) {
			Fruit destFruit = null;
			robot temp = new robot(string);
			boolean hasFruits = false;
			for (Fruit Fruit : this.runFruits) {
				if (Fruit.onSight == temp.id) {
					destFruit = Fruit;
					hasFruits = true;
					break;
				}
			}
			if (hasFruits) {
				edge_data fruitIn = null;
				Collection<node_data> Nodes = this.g.getV();
				for (node_data i : Nodes) {
					for (node_data j : Nodes) {
						double x1 = i.getLocation().x(), x2 = j.getLocation().x();
						double y1 = i.getLocation().y(), y2 = j.getLocation().y();
						double m = (y1 - y2) / (x1 - x2);
						double y = (m * (destFruit.location.x() - x1)) + y1;
						if (y + epsilon > destFruit.getLocation().y() && y - epsilon < destFruit.getLocation().y())
							fruitIn = this.g.getEdge(i.getKey(), j.getKey());
					}
				}
				if (destFruit.type == -1) dest = Math.max(fruitIn.getSrc(), fruitIn.getDest());
				if (destFruit.type == 1) dest = Math.min(fruitIn.getSrc(), fruitIn.getDest());
				if (dest == temp.src) {
					if (destFruit.type == -1) {
						dest = Math.min(fruitIn.getSrc(), fruitIn.getDest());
						this.game.chooseNextEdge(temp.id, dest);
						continue;
					}
					if (destFruit.type == 1) {
						dest = Math.max(fruitIn.getSrc(), fruitIn.getDest());
						this.game.chooseNextEdge(temp.id, dest);
						continue;
					}
				}
				List<node_data> path =  this.ga.shortestPath(temp.src, dest);
				this.game.chooseNextEdge(temp.getId(), path.get(1).getKey());

			}
			else {
				double Weight = Double.POSITIVE_INFINITY;
				Fruit fruitDest = null;
				for (Fruit fruit : this.runFruits) {
					edge_data fruitIn = null;
					if (fruit.onSight == -1) {
						Collection<node_data> Nodes = this.g.getV();
						for (node_data i : Nodes) {
							for (node_data j : Nodes) {
								double x1 = i.getLocation().x(), x2 = j.getLocation().x();
								double y1 = i.getLocation().y(), y2 = j.getLocation().y();
								double m = (y1 - y2) / (x1 - x2);
								double y = (m * (fruit.location.x() - x1)) + y1;
								if (y + epsilon > fruit.getLocation().y() && y - epsilon < fruit.getLocation().y()) {
									fruitIn = this.g.getEdge(i.getKey(), j.getKey());
								}
							}
							if (fruitIn != null) {
								if (fruit.type == -1)dest = Math.max(fruitIn.getDest(), fruitIn.getSrc());
								if (fruit.type == 1)dest = Math.min(fruitIn.getDest(), fruitIn.getSrc());
							}
						}
					}
					Graph_Algo t = new Graph_Algo(this.g);
					System.out.println(temp.src + "   " + dest);
					if(dest != -1) {
						double tempWeight = t.shortestPathDist(temp.src, dest);
						if (tempWeight < Weight) {
							Weight = tempWeight;
							fruitDest = fruit;
						}
					}
				}
				fruitDest.onSight = temp.id;
			}
		}
	}


/*
 * This method is one of the components in order to refresh the screen while the game is Running.
 */
	public void moveGame() {
		while (this.game.isRunning()){
			this.game.move();
			this.movesCounter++;
			try {
				Thread.sleep(1);		//TODO
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		System.out.println(this.game.toString());
		int points = 0;
		for (String i : this.game.getRobots()) {
			robot temp = new robot(i);
			points = points + temp.getPoints();
		}
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 2), ((StdDraw.ymax + StdDraw.ymin) / 2) + 0.001, "game end");
		StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 2), ((StdDraw.ymax + StdDraw.ymin) / 2), "your points: " + points);
	}

	/*
	 * This method adds Robots automatically to the graph. It is used in the automaticGame method above. 
	 * Depending on the location of each fruit on the graph, the robot will be set on the graph in order to get the highest score.
	 */
	public void addAutomaticlRobots() {		
		this.game.addRobot(39);
		this.game.addRobot(16);
		
//		double epsilon = 0.000000001;
//		List<String> fruitsString = this.game.getFruits();
//		LinkedList<Fruit> fruits = new LinkedList<Fruit>();
//		for (String string : fruitsString) {
//			fruits.add(new Fruit(string));
//		}
//		int x = 0;
//		for (; x < this.robots; x++) {
//			Fruit max = null;
//			for (Fruit fruit : fruits) {
//				if (max == null)max = fruit;
//				else if(max.value < fruit.value);
//				max = fruit;
//			}
//			Collection<node_data> Nodes = this.g.getV();
//			edge_data fruitIn = null;
//			for (node_data i : Nodes) {
//				for (node_data j : Nodes) {
//					if (i.getKey() == j.getKey())continue;
//					double x1 = i.getLocation().x(), x2 = j.getLocation().x();
//					double y1 = i.getLocation().y(), y2 = j.getLocation().y();
//					double m = (y1 - y2) / (x1 - x2);
//					double y = (m * (max.location.x() - x1)) + y1;
//					if (y + epsilon > max.getLocation().y() && y - epsilon < max.getLocation().y()) fruitIn = this.g.getEdge(i.getKey(), j.getKey());
//					if (fruitIn != null)break;
//				}
//
//				if (fruitIn != null)break;
//			}
//			int srcKey = fruitIn.getSrc();
//			int destKey = fruitIn.getDest();
//			if (max.type == -1) {
//				this.game.addRobot(Math.max(srcKey, destKey));
//
//			}
//			else {
//				this.game.addRobot(Math.min(srcKey, destKey));
//			}
//			fruits.remove(max);
//		}
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
		if (this.type.equals("your score")) yourScore();
		if (this.type.equals("global score")) globalScore();
	}


	//////////////////////////score//////////////////////////

/*
 * This methods displays to the players (according to it's ID that was entered), how many games were played, how many time
 * the player played, the score for each stage and the current level the player has to pass.
 */
	public void yourScore() {
		StdDraw.clear();
		StdDraw.setFont(new Font("arial", Font.PLAIN, 20));
		StdDraw.setPenColor(Color.GREEN);
		int id = 0;
		String idS = JOptionPane.showInputDialog(new JFrame(),"enter your id", null);
		try {
			id = Integer.parseInt(idS);
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(new JFrame(), "the action was canceled");
		}
		StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "you played " + score.numberOfGames(id) +" times");
		int []Stages = score.CurrentStage(id);
		for (int i = 0; i < Stages.length; i++) {
			if (Stages[i] == 0) {
				if (i == 0) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 0");
					break;
				}
				if (i == 1) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 1");
					break;
				}
				if (i == 2) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 3");
					break;
				}
				if (i == 3) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 5");
					break;
				}
				if (i == 4) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 9");
					break;
				}
				if (i == 5) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 11");
					break;
				}
				if (i == 6) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 13");
					break;
				}
				if (i == 7) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 16");
					break;
				}
				if (i == 8) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 19");
					break;
				}
				if (i == 9) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 20");
					break;
				}
				if (i == 10) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "you now at level 23");
					break;
				}
				if (i == 11) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 3 , "You have finished the game");
					break;
				}
			}
		}
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "Your highest score at stage 0 is: " + Stages[0]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.05 , "Your highest score at stage 1 is:" + Stages[1]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.1 , "Your highest score at stage 3 is:" + Stages[2]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.15 , "Your highest score at stage 5 is:" + Stages[3]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.2 , "Your highest score at stage 9 is:" + Stages[4]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.25 , "Your highest score at stage 11 is:" + Stages[5]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.3 , "Your highest score at stage 13 is:" + Stages[6]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.35 , "Your highest score at stage 16 is:" + Stages[7]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.4 , "Your highest score at stage 19 is:" + Stages[8]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.45 , "Your highest score at stage 20 is:" + Stages[9]);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5)*3, (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.5 , "Your highest score at stage 23 is:" + Stages[10]);
	}

/*
 * This method gives the global score of the player compared to the scores of the other students. 
 * The method is getting the scores of each players from the database. 
 */
	public void globalScore() {
		StdDraw.clear();
		StdDraw.setFont(new Font("arial", Font.PLAIN, 20));
		StdDraw.setPenColor(Color.GREEN);
		int id = 0;
		String idS = JOptionPane.showInputDialog(new JFrame(),"enter your id", null);
		try {
			id = Integer.parseInt(idS);
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(new JFrame(), "the action was canceled");
			return;
		}
		int []Stages = score.CurrentStage(id);
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "In Stage 0 your place in relative to the class is: " + score.global(id, 0, Stages[0]) + "."); 
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.05 , "In Stage 1 your place in relative to the class is: " + score.global(id, 1, Stages[1]) + ".");
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.1 , "In Stage 3 your place in relative to the class is: " + score.global(id, 3, Stages[2]) + ".");
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.15 , "In Stage 5 your place in relative to the class is: " + score.global(id, 5, Stages[3]) + ".");
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.2 , "In Stage 9 your place in relative to the class is: " + score.global(id, 9, Stages[4]) + ".");
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.25 , "In Stage 11 your place in relative to the class is: " + score.global(id, 11, Stages[5]) + ".");
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.3 , "In Stage 13 your place in relative to the class is: " + score.global(id, 13, Stages[6]) + ".");
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.35 , "In Stage 16 your place in relative to the class is: " + score.global(id, 16, Stages[7]) + ".");
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.4 , "In Stage 19 your place in relative to the class is: " + score.global(id, 19, Stages[8]) + ".");
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.45 , "In Stage 20 your place in relative to the class is: " + score.global(id, 20, Stages[9]) + ".");
		StdDraw.textLeft(((StdDraw.xmax + StdDraw.xmin) / 5), (((StdDraw.ymax + StdDraw.ymin) / 5) * 4) - 0.5 , "In Stage 23 your place in relative to the class is: " + score.global(id, 23, Stages[10]) + ".");



	}
}