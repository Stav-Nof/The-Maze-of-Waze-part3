package Test;

import static org.junit.Assert.*;
import org.junit.Test;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.NodeData;

public class Graph_AlgoTest {

	
	@Test
	public final void testInitGraph() {
		Graph_Algo g = new Graph_Algo();
		Graph_Algo g1 = new Graph_Algo();
		assertNotEquals(g, g1);
		assertEquals(g, g);
	}

	//	@Test
	//	public final void testInitString() {
	//
	//		
	//	}
	//
	//	@Test
	//	public final void testSave() {
	//		fail("Not yet implemented"); // TODO
	//	}

	@Test
	public final void testIsConnected() {
		Graph_Algo g = new Graph_Algo();
		g.init(new DGraph());

		NodeData n = new NodeData(2,250,250);
		NodeData n1 = new NodeData(4,25,20);
		NodeData n2 = new NodeData(20,245,40);
		NodeData n3 = new NodeData(6,60,75);

		g.g.addNode(n);
		g.g.addNode(n1);
		g.g.addNode(n2);
		g.g.addNode(n3);

		g.g.connect(n.getKey(), n1.getKey(), 5);
		g.g.connect(n.getKey(), n2.getKey(), 5);
		g.g.connect(n2.getKey(), n3.getKey(), 5);

		assertFalse(g.isConnected());
	}

//	@Test
//	public final void testIsReachable() {
//
//		Graph_Algo g = new Graph_Algo();
//		g.init(new DGraph());
//
//		Node n = new Node(2,250,250);
//		Node n1 = new Node(4,25,20);
//		Node n2 = new Node(20,245,40);
//		Node n3 = new Node(6,60,75);
//
//		g.g.addNode(n);
//		g.g.addNode(n1);
//		g.g.addNode(n2);
//		g.g.addNode(n3);
//
//		g.g.connect(n.getKey(), n1.getKey(), 5);
//		g.g.connect(n.getKey(), n2.getKey(), 5);
//		g.g.connect(n2.getKey(), n3.getKey(), 5);
//		g.g.connect(n3.getKey(), n1.getKey(), 5);
//		
//		assertTrue(g.isReachable(n.getKey(), n3.getKey()));
//		
//	}
	//
	//	@Test
	//	public final void testResetNodeTags() {
	//		fail("Not yet implemented"); // TODO
	//	}
	//
	//	@Test
	//	public final void testShortestPathDist() {
	//		fail("Not yet implemented"); // TODO
	//	}
	//
	//	@Test
	//	public final void testShortestPath() {
	//		fail("Not yet implemented"); // TODO
	//	}
	//
	//	@Test
	//	public final void testTSP() {
	//		fail("Not yet implemented"); // TODO
	//	}
	//
	//	@Test
	//	public final void testCopy() {
	//		fail("Not yet implemented"); // TODO
	//	}

}
