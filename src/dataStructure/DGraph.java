package dataStructure;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.gson.Gson;

import utils.Point3D;


public class DGraph implements graph, Serializable{


	private HashMap <Integer,node_data> vertices;
	private HashMap <Integer,HashMap<Integer,edge_data>> edges;
	private int verticeCounter;
	private int edgesCounter;
	int mc;

	/*
	 * Default Constructor of a DGraph.
	 * A default graph of type DGraph contains a Hashmap of vertices and another Hashmap of edges which contains key numbers and a HashMap of edges.
	 * Counts the number of vertices, edges of the graph and the numbers of changes that were made on the graph.
	 */
	public DGraph() {
		vertices = new HashMap <Integer,node_data>();
		edges = new HashMap <Integer,HashMap<Integer,edge_data>>();
		verticeCounter = 0;
		edgesCounter = 0;
		mc=0;
	}


	public void init(String s) {
		DGrapgArrays temp = new DGrapgArrays(s);
		this.vertices = new HashMap <Integer,node_data>();
		for (int i = 0; i < temp.Nodes.length; i++) {
			this.addNode(new NodeData(temp.Nodes[i].id, new Point3D(temp.Nodes[i].pos)));
		}
		for (int i = 0; i < temp.Edges.length; i++) {
			this.connect(temp.Edges[i].src, temp.Edges[i].dest, temp.Edges[i].w);
		}
	}


	private class DGrapgArrays{
		Nodes []Nodes;
		Edge []Edges;


		public DGrapgArrays(String s) {
			Gson gson = new Gson();
			DGrapgArrays temp = gson.fromJson(s,DGrapgArrays.class);
			this.Nodes = temp.Nodes;
			this.Edges = temp.Edges;
		}

		private class Nodes{
			String pos;
			int id;
		}

		private class Edge{
			int src;
			double w;
			int dest;
		}

	}

	/*
	 * Returns the node data by the key node.
	 */
	@Override
	public node_data getNode(int key) {
		if(vertices.containsKey(key)){
			return vertices.get(key);
		} else
			return null;
	}



	/*
	 * returns the data of the specified edge by using its source and its destination.
	 * 
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		if (edges.containsKey(src) && edges.get(src).containsKey(dest)) {
			return edges.get(src).get(dest);
		}
		else {
			return null;
		}
	}


	/*
	 * Adds a node to the graph with a given node_data.
	 * 
	 */
	@Override
	public void addNode(node_data n) {
		if (this.vertices == null) {
			DGraph temp = new DGraph();
			this.edges = temp.edges;
			this.vertices = temp.vertices;
			this.verticeCounter = temp.verticeCounter;
		}
		if (this.vertices.get(n.getKey()) == null) {
			this.vertices.put(n.getKey(),n);
			this.edges.put(n.getKey(),new HashMap<Integer,edge_data>());
			verticeCounter++;
		}
		else {
			this.vertices.put(n.getKey(),n);
		}
		mc++;
	}


	/*
	 * Creates an edge and connect between two given node source and node dest. Adds a weight value to the edge.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		node_data a = getNode(src);
		node_data b = getNode(dest);
		if(a != null && b!=null) {
			EdgeData e = new EdgeData(this.vertices.get(src), this.vertices.get(dest), w);
			this.edges.get(src).put(dest,e);
			this.edgesCounter++;
			this.mc++;
		}


	}



	/*
	 * Returns a shallow pointer to a collection of all the vertices in this graph.
	 */
	@Override
	public Collection<node_data> getV() {
		Collection<node_data> ans =  new LinkedList<node_data>();
		ans.addAll(this.vertices.values());
		return ans;
	}



	/*
	 *  Returns a shallow pointer to a collection of all the edges of this graph.
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		Collection<edge_data> ans = new LinkedList<edge_data>();
		ans.addAll(this.edges.get(node_id).values());
		return ans;
	}


	/*
	 * removes the Node by using its key from this graph and removes all the edges that starts and end by this node.
	 */
	@Override
	public node_data removeNode(int key) {
		if (this.vertices.containsKey(key)) {
			node_data toReturn = this.vertices.remove(key);
			this.edges.remove(key);
			this.verticeCounter--;
			mc++;
			return toReturn;
		}
		return null;
	}

	/*
	 * Removes the edge that connect between a source node and destination node from this graph.
	 */

	@Override
	public edge_data removeEdge(int src, int dest) {
		if (this.edges.containsKey(src)) {
			edge_data toReturn = this.edges.get(src).remove(dest);
			if (toReturn != null) {
				this.edgesCounter--;
				mc++;
			}
			return toReturn;
		}
		return null;
	}


	/*
	 * Returns the number of Vertices nodes in this graph.
	 */
	@Override
	public int nodeSize() {
		return this.verticeCounter;
	}

	/*
	 * Returns the number of edges in this graph.
	 */
	@Override
	public int edgeSize() {
		return this.edgesCounter;
	}

	/*
	 * returns the numbers of changes that were made on this graph.
	 */
	@Override
	public int getMC() {
		return mc;
	}


}
