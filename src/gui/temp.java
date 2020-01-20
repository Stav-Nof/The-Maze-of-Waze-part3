package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import algorithms.Graph_Algo;
import dataStructure.NodeData;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;

public class temp extends JFrame implements ActionListener {
	Graph_Algo ga;
	private int width = 1000;
	private int height = 500;
	private int scaleUp = 70;
	private int scaleRest = 20;
	


	public temp(Graph_Algo g) {
		this.ga = g;
		firstWindow();
		
	}


	public temp(graph g) { 
		this.ga = new Graph_Algo (g);
		firstWindow();
	}


	/**
	 * 
	 * @param data denote some data to be scaled
	 * @param r_min the minimum of the range of your data
	 * @param r_max the maximum of the range of your data
	 * @param t_min the minimum of the range of your desired target scaling
	 * @param t_max the maximum of the range of your desired target scaling
	 * @return
	 */
	private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}


	private void firstWindow() {
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font myFont = new Font ("Arial", 1, 15);
		this.setFont (myFont);
		MenuBar menuBar = new MenuBar();
		Menu graph = new Menu("graph");
		Menu algorithms = new Menu("algorithms");
		menuBar.add(algorithms);
		menuBar.add(graph);
		this.setMenuBar(menuBar);
		//algorithms menu
		MenuItem isConnected = new MenuItem("is Connected");
		isConnected.addActionListener(this);
		MenuItem shortestPathDist = new MenuItem("shortest Path distance");
		shortestPathDist.addActionListener(this);
		MenuItem shortestPath = new MenuItem("shortest Path");
		shortestPath.addActionListener(this);
		MenuItem TSP = new MenuItem("TSP");
		TSP.addActionListener(this);
		algorithms.add(isConnected);
		algorithms.add(shortestPathDist);
		algorithms.add(shortestPath);
		algorithms.add(TSP);
		//graph menu
		MenuItem loadGraph = new MenuItem("load graph");
		loadGraph.addActionListener(this);
		MenuItem saveGraph = new MenuItem("save graph");
		saveGraph.addActionListener(this);
		MenuItem addvertex = new MenuItem("add vertex");
		addvertex.addActionListener(this);
		MenuItem connect = new MenuItem("connect");
		connect.addActionListener(this);
		MenuItem Refresh = new MenuItem("Refresh");
		Refresh.addActionListener(this);
		graph.add(loadGraph);
		graph.add(saveGraph);
		graph.add(addvertex);
		graph.add(connect);
		graph.add(Refresh);
		this.setVisible(true);
		

	}

	public void paint(Graphics g) {
		super.paint(g);
		Collection<node_data> collection = this.ga.g.getV();
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		for (node_data i : collection) {
			Point3D temp = i.getLocation();
			double tempX = temp.x();
			double tempY = temp.y();
			if(tempX > maxX) maxX = tempX;
			if(tempX < minX) minX = tempX;
			if(tempY > maxY) maxY = tempY;
			if(tempY < minY) minY = tempY;
		}
		for (node_data i : collection) {
			Point3D Location = i.getLocation();
			String ix = "" + Location.x();
			if (ix.length() > 3) {
				ix = ix.substring(3);
			}
			else ix = "0";
			String iy = "" + Location.y();
			if (iy.length() > 3) {
				iy = iy.substring(3);
			}
			else iy = "0";
			g.setColor(Color.BLUE);
			g.fillOval((int)scale(Integer.parseInt(ix)-5, minX, maxX, scaleRest, width - scaleRest),
					(int)scale(Integer.parseInt(iy)-5, minY, maxY, scaleUp, height - scaleRest), 10, 10);
			
		}
		
		
		
		
		
		
//		for (node_data i : collection) {
//			Point3D Location = i.getLocation();
//			g.setColor(Color.BLUE);
//			g.fillOval((int)scale(Location.ix()-5, minX, maxX, scaleRest, width - scaleRest), (int)scale(Location.iy()-5, minY, maxY, scaleUp, height - scaleRest), 10, 10);
//			g.drawString("" + i.getKey(), (int)scale(Location.ix(), minX, maxX, scaleRest, width - scaleRest), (int)scale(Location.iy(), minY, maxY, scaleUp, height - scaleRest));
//		}
//		
		
		
		Graphics2D g2 = (Graphics2D) g;
		for (node_data i : collection) {
			g2.setStroke(new BasicStroke(3));
			Collection<edge_data> collection1 = ga.g.getE(i.getKey());
			for (edge_data j : collection1) {
				g.setColor(Color.RED);
				Point3D from = i.getLocation();
				Point3D to = this.ga.g.getNode(j.getDest()).getLocation();
				g2.drawLine((int)scale(from.ix(), minX, maxX, scaleRest, width - scaleRest), (int)scale(from.iy(), minY, maxY, scaleUp, height - scaleRest),
						(int)scale(to.ix(), minX, maxX, scaleRest, width - scaleRest), (int)scale(to.iy(), minY, maxY, scaleUp, height - scaleRest));
				g.drawString("" + j.getWeight(), (int)scale((from.ix() + to.ix())/2, minX, maxX, scaleRest, width - scaleRest), (int)scale((from.iy() + to.iy())/2, minY, maxY, scaleUp, height - scaleRest));
				g.setColor(Color.YELLOW);
				int xGoBack = (from.ix() - to.ix())/10;
				int yGoBack = (from.iy() - to.iy())/10;
				g.fillOval((int)scale(to.ix() + xGoBack - 5, minX, maxX, scaleRest, width - scaleRest),
						(int)scale(to.iy() + yGoBack - 5, minY, maxY, scaleUp, height - scaleRest) , 10, 10);
			}
		}
	}





	@Override
	public void actionPerformed(ActionEvent e) {
		
		String action = e.getActionCommand();
		if(action.equals("Refresh")) {
			repaint();
		}
		if (action.equals("TSP")) {
			if (!this.ga.isConnected()) {
				JOptionPane.showMessageDialog(null, "the graph not conected.\nyou cent use this option");
				return;

			}
			final JFrame TSP = new JFrame();
			Collection<node_data> temp = this.ga.g.getV();
			JCheckBox choices [] = new JCheckBox[temp.size()];
			int i = 0;
			for(node_data j: temp) {
				choices[i] = new JCheckBox("" + j.getKey());
				i++;
			}
			Object[] lines = {"chooses nodes", choices};
			if(JOptionPane.showConfirmDialog(TSP, lines, "TSP", JOptionPane.DEFAULT_OPTION) == -1)return;
			List<Integer> nodesSelected = new LinkedList<Integer>();
			for (i = 0; i < choices.length; i++) {
				if(choices[i].isSelected()) {
					nodesSelected.add(Integer.parseInt(choices[i].getText()));
				}
			}
			List<node_data> ans = this.ga.TSP(nodesSelected);
			String sAns = "";
			for (node_data j : ans) {
				sAns = sAns + "," + j.getKey();
			}
			sAns = (String) sAns.subSequence(1,sAns.length());
			JOptionPane.showMessageDialog(null, "the TSP is:\n" + sAns);
			return;
		}

		if (action.equals("shortest Path")) {
			final JFrame shortestPathDist = new JFrame();
			String sStart = JOptionPane.showInputDialog(shortestPathDist,"enter a start vertex key", null);
			int start = 0;
			if(sStart == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			try {
				start = Integer.parseInt(sStart);
			}
			catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you entered a char instead of a number!\nthe Action canceled");
				return;
			}
			if (this.ga.g.getNode(start) == null) {
				JOptionPane.showMessageDialog(null, "the key you enterd does not exist\nthe Action canceled");
				return;
			}
			String sEnd = JOptionPane.showInputDialog(shortestPathDist,"enter a end vertex key", null);
			int end = 0;
			if(sEnd == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			try {
				end = Integer.parseInt(sEnd);
			}
			catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you entered a char instead of a number!\nthe Action canceled");
				return;
			}
			if (this.ga.g.getNode(end) == null) {
				JOptionPane.showMessageDialog(null, "the key you enterd does not exist\nthe Action canceled");
				return;
			}
			if (start == end) {
				JOptionPane.showMessageDialog(null, "the start and the end key is equals");
				return;
			}
			if (!this.ga.isReachable(start, end)) {
				JOptionPane.showMessageDialog(null, "the source and destination are not connected");
				return;
			}
			List<node_data> ans = this.ga.shortestPath(start, end);
			String sAns = "";
			for (node_data i : ans) {
				sAns = sAns + "," + i.getKey();
			}
			sAns = (String) sAns.subSequence(1,sAns.length());
			JOptionPane.showMessageDialog(null, "the shortest Path is:\n" + sAns);
			return;
		}
		if (action.equals("shortest Path distance")) {
			final JFrame shortestPathDist = new JFrame();
			String sStart = JOptionPane.showInputDialog(shortestPathDist,"enter a start vertex key", null);
			int start = 0;
			if(sStart == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			try {
				start = Integer.parseInt(sStart);
			}
			catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you entered a char instead of a number!\nthe Action canceled");
				return;
			}
			if (this.ga.g.getNode(start) == null) {
				JOptionPane.showMessageDialog(null, "the key you enterd does not exist\nthe Action canceled");
				return;
			}
			String sEnd = JOptionPane.showInputDialog(shortestPathDist,"enter a end vertex key", null);
			int end = 0;
			if(sEnd == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			try {
				end = Integer.parseInt(sEnd);
			}
			catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you entered a char instead of a number!\nthe Action canceled");
				return;
			}
			if (this.ga.g.getNode(end) == null) {
				JOptionPane.showMessageDialog(null, "the key you enterd does not exist\nthe Action canceled");
				return;
			}
			if (start == end) {
				JOptionPane.showMessageDialog(null, "the shortest Path distance is:\n0.0");
				return;
			}
			double ans = this.ga.shortestPathDist(start, end);
			if (ans == 0) {
				JOptionPane.showMessageDialog(null, "this graph is not conected.\ncent find path");
				return;
			}
			JOptionPane.showMessageDialog(null, "the shortest Path distance is:\n" + this.ga.shortestPathDist(start, end));
			return;
		}
		if (action.equals("is Connected")) {
			if (this.ga.isConnected()) {
				JOptionPane.showMessageDialog(null, "this graph is conected");
				return;
			}
			else {
				JOptionPane.showMessageDialog(null, "this graph is not conected");
				return;
			}
		}
		if (action.equals("load graph")) {
			final JFrame load = new JFrame();
			String file_name = JOptionPane.showInputDialog(load,"enter an file name", null);
			if(file_name == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			this.ga.init(file_name);
			repaint();
		}
		if (action.equals("save graph")) {
			final JFrame save = new JFrame();
			String file_name = JOptionPane.showInputDialog(save,"enter an file name", null);
			if(file_name == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			this.ga.save(file_name);
			repaint();
		}
		if(action.equals("add vertex")){
			final JFrame toAdd = new JFrame();
			String sX = JOptionPane.showInputDialog(toAdd,"enter an x value", null);
			if(sX == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			String sY = JOptionPane.showInputDialog(toAdd,"enter an y value", null);
			if(sY == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			String sKey = JOptionPane.showInputDialog(toAdd,"enter an key", null);
			if(sKey == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			node_data temp = null;
			try {
				temp = new NodeData(Integer.parseInt(sKey), new Point3D(Double.parseDouble(sX),Double.parseDouble(sY)));
			}
			catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you entered a char instead of a number!\nthe vertex did not added");
				return;
			}
			ga.g.addNode(temp);
			repaint();
		}
		if(action.equals("connect")) {
			final JFrame connect = new JFrame();
			int from = 0;
			int to = 0;
			double weight = 0;
			String sFrom = JOptionPane.showInputDialog(connect,"enter a vertex key to conect from", null);
			if(sFrom == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			try {
				from = Integer.parseInt(sFrom);
			}
			catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you entered a char instead of a number!\nthe vertex did not conect");
				return;
			}
			if (this.ga.g.getNode(from) == null) {
				JOptionPane.showMessageDialog(null, "the key you entered doesnt exist\nthe vertex did not conect");
				return;
			}
			String sto = JOptionPane.showInputDialog(connect,"enter a vertex key to conect to", null);
			if(sto == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			try {
				to = Integer.parseInt(sto);
			}
			catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you entered a char instead of a number!\nthe vertex did not conect");
				return;
			}
			if (this.ga.g.getNode(to) == null) {
				JOptionPane.showMessageDialog(null, "the key you entered doesnt exist\nthe vertex did not conect");
				return;
			}
			String sweight = JOptionPane.showInputDialog("enter a edge weight");
			if(sweight == null) {
				JOptionPane.showMessageDialog(null, "Action canceled");
				return;
			}
			try {
				weight = Double.parseDouble(sweight);
			}
			catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you entered a char instead of a number!\nthe vertex did not conect");
				return;
			}
			this.ga.g.connect(from, to, weight);
			repaint();
		}

	}
}