package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import dataStructure.DGraph;
import dataStructure.NodeData;
import dataStructure.edge_data;
import dataStructure.node_data;

class DGraphTest {


	@Test
	final void testGetNode() {
		DGraph g = new DGraph();

		node_data n = new NodeData(2,250,250);
		node_data n1 = new NodeData(4,25,20);
		node_data n2 = new NodeData(20,245.0,40.0);

		g.addNode(n);
		g.addNode(n1);
		g.addNode(n2);

		assertEquals(g.getNode(n.getKey()), n);
		assertEquals(g.getNode(n1.getKey()),n1);

	}

	@Test
	final void testGetEdge() {
		DGraph g = new DGraph();

		node_data n = new NodeData(2,250,250);
		node_data n1 = new NodeData(4,25,20);
		node_data n2 = new NodeData(20,245,40);

		g.addNode(n);
		g.addNode(n1);
		g.addNode(n2);

		g.connect(n.getKey(), n1.getKey(), 0);
		//g.connect(n.getKey(), n2.getKey(), 0);
		g.connect(n2.getKey(), n1.getKey(), 0);

		assertEquals(g.getEdge(n.getKey(), n1.getKey()).getSrc(),n.getKey());
		assertEquals(g.getEdge(n.getKey(), n1.getKey()).getDest(),n1.getKey());
		//assertEquals(g.getEdge(n.getKey(), n2.getKey()).getSrc(),n.getKey()); //if n, n2 are not connected
		//assertEquals(g.getEdge(n.getKey(), n2.getKey()).getDest(),n2.getKey());

	}

	@Test
	final void testAddNode() {
		DGraph g = new DGraph();
		node_data n = new NodeData(2,250,250);
		g.addNode(n);
		assertEquals(g.getNode(n.getKey()),n);
	}

	@Test
	final void testConnect() {
		DGraph g = new DGraph();
		node_data n = new NodeData(2,250,250);
		node_data n1 = new NodeData(4,25,20);
		node_data n2 = new NodeData(21,245,40);

		g.addNode(n);
		g.addNode(n1);
		g.addNode(n2);

		g.connect(n1.getKey(), n2.getKey(), 0);

		assertEquals(g.getEdge(n1.getKey(), n2.getKey()).getSrc(),n1.getKey());

		edge_data e1= g.getEdge(n.getKey(), n2.getKey()); //n, n2 are not connected

		assertEquals(null,e1);

	}

	@Test
	final void testGetV() {
		DGraph g = new DGraph();

		assertTrue(g.getV().size()==g.nodeSize());
	}

	/* @Test
	final void testGetE() {
	} */

	@Test
	final void testRemoveNode() {
		DGraph g = new DGraph();
		node_data n = new NodeData(2,250,250);
		g.addNode(n);

		g.removeNode(n.getKey());

		assertTrue(g.getNode(n.getKey())==null);
	}

	@Test
	final void testRemoveEdge() {
		DGraph g = new DGraph();
		node_data n = new NodeData(2,250,250);
		node_data n1 = new NodeData(4,25,20);

		g.addNode(n);
		g.addNode(n1);

		g.connect(n.getKey(), n1.getKey(), 0);

		g.removeEdge(n.getKey(), n1.getKey());

		assertTrue(g.getEdge(n.getKey(), n1.getKey()) ==null);
	}

	@Test
	final void testNodeSize() {
		 DGraph g = new DGraph();
			node_data n = new NodeData(2,250,250);
			node_data n1 = new NodeData(4,25,20);

			g.addNode(n);
			g.addNode(n1);

			assertEquals(g.nodeSize(), 2);

			g.removeNode(n.getKey()); //remove one vertex

			assertEquals(g.nodeSize(), 1);
	}

	@Test
	final void testEdgeSize() {
		DGraph g = new DGraph();
		node_data n = new NodeData(2,250,250);
		node_data n1 = new NodeData(4,25,20);
		node_data n2 = new NodeData(21,245,40);

		g.addNode(n);
		g.addNode(n1);
		g.addNode(n2);

		g.connect(n.getKey(), n1.getKey(), 0);
		g.connect(n1.getKey(), n2.getKey(), 0);

		assertEquals(g.edgeSize(), 2);

		g.removeEdge(n.getKey(), n1.getKey()); //remove one edge

		assertEquals(g.edgeSize(), 1);
	}

	@Test
	final void testGetMC() {
		DGraph g = new DGraph();
		node_data n = new NodeData(2,250,250);
		g.addNode(n);
		
		assertEquals(g.getMC(), 1);
		
		g.removeNode(n.getKey());
		
		assertEquals(g.getMC(), 2);
		
		
	}

}
