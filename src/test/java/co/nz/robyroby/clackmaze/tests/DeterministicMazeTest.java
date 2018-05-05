package co.nz.robyroby.clackmaze.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import co.nz.robyroby.clackmaze.model.GraphNode;
import co.nz.robyroby.clackmaze.solver.Solver;
import co.nz.robyroby.clackmaze.solver.exceptions.NoPossiblePathException;
import co.nz.robyroby.clackmaze.solver.impl.BreadthFirstSolverImpl;

public class DeterministicMazeTest {
	class MyGraphNode implements GraphNode{
		Set<GraphNode> linkedNodes = new HashSet<GraphNode>();
		
		public void link(MyGraphNode randomGraphNode) {
			this.linkedNodes.add(randomGraphNode);
		}

		public MyGraphNode createLinkedNode() {
			MyGraphNode newLinkedNode = new MyGraphNode();
			this.linkedNodes.add(newLinkedNode);
			return newLinkedNode;
		}

		@Override
		public Set<GraphNode> getDirectlyLinkedNodes() {
			return linkedNodes;
		}

		@Override
		public String toString() {
			return "MyGraphNode [linkedNodes=" + linkedNodes + "]";
		}

	}
		
	@Test
	public void testMiniPath() {
		MyGraphNode startNode = new MyGraphNode();
		MyGraphNode finishNode = new MyGraphNode();
		startNode.link(finishNode);
		Solver solver = new BreadthFirstSolverImpl();
		try {
			final int numSteps = solver.getShortestPathDistance(startNode, finishNode);
			assertEquals(1, numSteps);
		} catch (NoPossiblePathException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testShortPath() {
		MyGraphNode startNode = new MyGraphNode();
		MyGraphNode finishNode = new MyGraphNode();
		startNode.createLinkedNode().createLinkedNode().link(finishNode);
		Solver solver = new BreadthFirstSolverImpl();
		try {
			final int numSteps = solver.getShortestPathDistance(startNode, finishNode);
			assertEquals(3, numSteps);
		} catch (NoPossiblePathException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testShortestPath() {
		MyGraphNode startNode = new MyGraphNode();
		MyGraphNode finishNode = new MyGraphNode();
		startNode.createLinkedNode().createLinkedNode().createLinkedNode().link(finishNode);
		startNode.createLinkedNode().createLinkedNode().link(finishNode);
		startNode.createLinkedNode().link(finishNode);
		Solver solver = new BreadthFirstSolverImpl();
		try {
			final int numSteps = solver.getShortestPathDistance(startNode, finishNode);
			assertEquals(2, numSteps);
		} catch (NoPossiblePathException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testShortestPathMixed() {
		MyGraphNode startNode = new MyGraphNode();
		MyGraphNode finishNode = new MyGraphNode();
		MyGraphNode secondLastNode = startNode.createLinkedNode().createLinkedNode();
		Solver solver = new BreadthFirstSolverImpl();
		try {
			secondLastNode.link(finishNode);
			assertEquals(3, solver.getShortestPathDistance(startNode, finishNode));
			
			startNode.createLinkedNode().link(secondLastNode);
			assertEquals(3, solver.getShortestPathDistance(startNode, finishNode));
			
			startNode.link(secondLastNode);
			assertEquals(2, solver.getShortestPathDistance(startNode, finishNode));
			
			startNode.link(finishNode);
			assertEquals(1, solver.getShortestPathDistance(startNode, finishNode));
			
		} catch (NoPossiblePathException e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test(expected = NoPossiblePathException.class)
	public void testImpossiblePath() throws NoPossiblePathException {
		MyGraphNode startNode = new MyGraphNode();
		MyGraphNode finishNode = new MyGraphNode();
		Solver solver = new BreadthFirstSolverImpl();
		solver.getShortestPathDistance(startNode, finishNode);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testStartNodeNull() throws NoPossiblePathException {
		Solver solver = new BreadthFirstSolverImpl();
		solver.getShortestPathDistance(null, new MyGraphNode());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFinishNodeNull() throws NoPossiblePathException {
		Solver solver = new BreadthFirstSolverImpl();
		solver.getShortestPathDistance(new MyGraphNode(), null);
	}
}

