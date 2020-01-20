package algorithms;

import java.util.Collection;
import java.util.LinkedList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;
import dataStructure.DGraph;
import dataStructure.NodeData;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;


/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable{
	public graph g;
	public int mc;

	/*
	 * Constructor for Graph_Algo that receives a graph of type "graph".
	 */
	public Graph_Algo(graph _graph) {
		this.g = _graph;
		this.mc = g.getMC();
	}

	/*
	 * Default Constructor for Graph_Algo.
	 */
	public Graph_Algo() {
		this.g = null;
		this.mc = 0;
	}


	/*
	 * Initializes a graph (receives a graph of type graph).
	 */
	@Override
	public void init(graph g) {
		this.g = g;
		this.mc = g.getMC();
	}



	/*
	 * Initializes a graph from a file.
	 * The purpose is to read a file from the computer. The function should read a specific given file (a graph file) and 
	 * from it, initializes a graph.
	 */
	@Override
	public void init(String file_name) {
		Graph_Algo temp = null; 
		try{    
			FileInputStream file = new FileInputStream(file_name); 
			ObjectInputStream in = new ObjectInputStream(file); 
			temp = (Graph_Algo)in.readObject(); 
			in.close(); 
			file.close(); 
		} 
		catch(IOException ex) { 
			System.out.println("IOException is caught");
			return;
		} 
		catch(ClassNotFoundException ex) { 
			System.out.println("ClassNotFoundException is caught"); 
			return;
		}
		this.g = temp.g;
	}


	/*
	 * Saves a file on the computer with a given file name.
	 */
	@Override
	public void save(String file_name) {
		try{    
			FileOutputStream file = new FileOutputStream(new File(file_name)); 
			ObjectOutputStream ous = new ObjectOutputStream(file); 
			ous.writeObject(this); 
			ous.close(); 
			file.close(); 
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}


	/*
	 * This methods check if all the nodes in the graph are connected to each other.
	 * That is to say that from every Node , we can find a valid path to any other node in the graph.
	 */
	@Override
	public boolean isConnected() {
		Collection<node_data> collection = this.g.getV();
		for (node_data i : collection) {
			for (node_data j : collection) {
				if (i.getKey() != j.getKey()) {
					if(!(isReachable(i.getKey(), j.getKey()))) {
						return false;
					}
				}
			}

		}
		return true;
	}

	/*
	 * Extends the isReachable method below. 
	 * Traverse the graph and more formally , gets every edges of the graph and by recursion sets the tag of the actual node to be 1, and 
	 * that way it allows to know wether or not if there is a way to get to the dest node.
	 * More details in the function below.
	 */
	private void isReachableExtend(int src) {
		if (this.g.getNode(src).getTag() == 1) {
			return;
		}
		this.g.getNode(src).setTag(1);
		Collection<edge_data> collection = this.g.getE(src);
		for (edge_data i : collection) {
			if(this.g.getNode(i.getDest()).getTag() == 1) {
				continue;
			}
			isReachableExtend(i.getDest());
		}
	}

	/*
	 * Helping function which aims to tell if from a source node, we can reach a given destination node in the graph.
	 * Uses the isReachableExtend function above.
	 * This function resets each node's tag in the graph to 0.
	 * Finally if the value of the tag of the dest node is 1, it means that we reached him and that he is reachable.
	 * 
	 */
	public boolean isReachable(int src,int dest) {
		resetNodeTags();
		isReachableExtend(src);
		if (this.g.getNode(dest).getTag() == 1)return true;
		return false;
	}

	/*
	 * Returns the shortest path in terms of edge weight in order to go from a source node to a dest node in the graph.
	 * By using a helping function, the function calculates all the possible paths and their costs (additioning the values of each edge to get to 
	 * a specific dest node). return the lowest cost in order to get to the dest node.
	 */

	@Override
	public double shortestPathDist(int src, int dest) {
		if (!(isReachable(src, dest))) {
			return 0;
		}
		shortestPathcalc(src, dest);
		return this.g.getNode(dest).getWeight();
	}


	/*
	 * sets all nodes weight to positive infinity.
	 */
	private void setAllWeight() {
		Collection<node_data> collection = this.g.getV();
		for (node_data i : collection) {
			i.setWeight(Double.POSITIVE_INFINITY);
		}
	}


	/*
	 * The all nodes tags in the graph to 0.
	 */
	private void resetNodeTags() {
		Collection<node_data> collection = this.g.getV();
		for (node_data i : collection) {
			i.setTag(0);
		}
	}


	/*
	 * Returns the node_data with the minimum weight value weight. 
	 */

	private node_data minWeightVal() {
		Collection<node_data> collection = this.g.getV();
		node_data ans = null;
		for (node_data i : collection) {
			if (ans == null && i.getTag() ==  0) {
				ans = i;
			}
			if (i.getTag() ==  0 && i.getWeight() < ans.getWeight()) {
				ans = i;
			}
		}
		return ans;
	}

	/*
	 * return the shortestPath between two node as an ordered list of nodes, that is to say, the exact path (all the node we have to 
	 * visit ) to get from source node to dest node.
	 */

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if (!(isReachable(src, dest))) {
			return null;
		}
		shortestPathcalc(src, dest);
		List<node_data> ans = new LinkedList<node_data>();
		String path = this.g.getNode(dest).getInfo();
		StringTokenizer tokenizer = new StringTokenizer(path);
		while (tokenizer.hasMoreElements()) {
			String nodeKey = tokenizer.nextToken(",");
			ans.add(this.g.getNode(Integer.parseInt(nodeKey)));
		}
		ans.add(this.g.getNode(dest));
		return ans;
	}


	/*
	 * Traverse the graph and sets each node's weight to positive infinity and each node's tag to 0.
	 * Uses the helping function "shortestPathcalcExtend" below.
	 */
	private void shortestPathcalc(int src, int dest) {
		this.setAllWeight();
		this.resetNodeTags();
		this.g.getNode(src).setWeight(0);
		this.g.getNode(src).setInfo("");
		shortestPathcalcExtend(src);
	}

	/*
	 * Recursive method in order to calculate the shortest Path to get from src node to dest node.
	 * To do so, the function traverses the graph and saves into the info the "route" to get to the dest node.
	 * Recursively doing this on every source node gets use to find the shortest path in the graph.
	 * This function is used in the function "shortestPath" above.
	 * 
	 */
	private void shortestPathcalcExtend(int src){
		if (this.g.getNode(src).getTag() == 1) {
			return;
		}
		this.g.getNode(src).setTag(1);
		Collection<edge_data> collection = this.g.getE(src);
		for (edge_data i : collection) {
			if(this.g.getNode(i.getDest()).getTag() == 1) {
				continue;
			}
			double newWeight = this.g.getNode(src).getWeight() + i.getWeight();
			String newpath = this.g.getNode(src).getInfo() + "," + g.getNode(src).getKey();
			if (newWeight < this.g.getNode(i.getDest()).getWeight()) {
				this.g.getNode(i.getDest()).setInfo(newpath);
				this.g.getNode(i.getDest()).setWeight(newWeight);
			}
		}
		node_data temp = minWeightVal();
		if (temp != null) {
			shortestPathcalcExtend(temp.getKey());
		}
	}

	/*
	 * This methods receives a list of Nodes. The function computes all the possible paths and return the shortest one.(Returns a 
	 * list of keys). 
	 * In the case where Nodes in the list have no direct path from one to another, the function will calculate how to get to the specific 
	 * node, and add the specific key in order to get to the precise Node, which means that in some cases the output can be bigger than the input.
	 */
	@Override
	public List<node_data> TSP(List<Integer> targets) {
		if(!isConnected())return null;
		if (targets.isEmpty())return new LinkedList<node_data>();
		if (targets.size() == 1) {
			List<node_data> ans = new LinkedList<node_data>();
			ans.add(this.g.getNode(targets.get(0)));
			return ans;
		}
		if (targets.size() == 2) {
			if (this.shortestPathDist(targets.get(0), targets.get(1)) > this.shortestPathDist(targets.get(1), targets.get(0))) {
				List<node_data> ans = this.shortestPath(targets.get(1), targets.get(0));
				return ans;
			}
			else {
				List<node_data> ans = this.shortestPath(targets.get(0), targets.get(1));
				return ans;
			}

		}
		List<node_data> ans = new LinkedList<node_data>();
		int destId = targets.get(1);
		List<node_data> toAdd = shortestPath(targets.get(0), destId);
		ans.addAll(toAdd);
		for (node_data i : toAdd) {
			if (targets.contains(i.getKey())) {
				targets.remove(targets.indexOf(i.getKey()));
			}
		}
		ans.addAll(TSP(destId, targets));
		return ans;
	}


	private List<node_data> TSP(int src, List<Integer> targets){
		if (targets.isEmpty())return new LinkedList<node_data>();
		if (targets.size() == 1) {
			List<node_data> ans = this.shortestPath(src, targets.get(0));
			ans.remove(0);
			return ans;
		}
		List<node_data> ans = new LinkedList<node_data>();
		int destId = targets.get(1);
		List<node_data> toAdd = shortestPath(targets.get(0), destId);
		ans.addAll(toAdd);
		for (node_data i : toAdd) {
			if (targets.contains(i.getKey())) {
				targets.remove(targets.indexOf(i.getKey()));
			}
		}
		ans.addAll(TSP(destId, targets));
		return ans;
	}

	/*
	 * This method makes a deep copy of this graph.
	 */
	@Override
	public graph copy() {
		DGraph copy = new DGraph();
		Collection<node_data> collection = this.g.getV();
		for (node_data i : collection) {
			NodeData temp = new NodeData(i.getKey(),new Point3D(i.getLocation()));
			copy.addNode(temp);
		}
		for (node_data i : collection) {
			Collection<edge_data> edges = this.g.getE(i.getKey());
			for (edge_data j : edges) {
				copy.connect(i.getKey(), j.getDest(), j.getWeight());
			}
		}
		return copy;
	}


}