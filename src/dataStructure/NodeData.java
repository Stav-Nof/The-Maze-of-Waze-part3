package dataStructure;

import java.io.Serializable;

import utils.Point3D;

public class NodeData implements node_data, Serializable {
	private int key;
	private Point3D location;
	private double weight;
	private String info;
	private int tag;


/*
 * Default constructor for the Node class.
 */
	public NodeData() {
		this.key=0;
		this.location=null;
		this.weight=0;
		this.info="";
		this.tag=0;
	}
	
/*
 * Constructor that receives a key number, and coordinate points (of type Point 3D).
 */	
	public NodeData(int key,double x, double y) {
		this.key = key;
		this.location = new Point3D (x,y);
	}


/*
 * Constructor that receives a key number and a location.
 */
	public NodeData(int key,Point3D location) {
		this.key = key;
		this.location = location;
	}
	
/*
 * Gets the key of this node.	
 */
	@Override
	public int getKey() {
		return this.key;
	}

/*
 * Gets the location of this Node.
 */
	@Override
	public Point3D getLocation() {
		return this.location;
	}


/*
 * Gets the weight of this node.
 */
	@Override
	public void setLocation(Point3D p) {
		this.location = p;

	}


/*
 * Sets the weight of this Node.
 */
	@Override
	public double getWeight() {
		return this.weight;
	}

/*
 * Sets the weight of this node.
 */
	@Override
	public void setWeight(double w) {
		this.weight = w;
	}
/*
 * Gets the info of this node.
 */
	@Override
	public String getInfo() {
		return this.info;
	}

/*
 * Sets the info of this node.
 */
	@Override
	public void setInfo(String s) {
		this.info = s;
	}

/*
 * Gets  the tag of this Node.
 */
	@Override
	public int getTag() {
		return this.tag;
	}


/*
 * Sets the tag of this Node.
 */
	@Override
	public void setTag(int t) {
		this.tag = t;
	}
	
	
}
