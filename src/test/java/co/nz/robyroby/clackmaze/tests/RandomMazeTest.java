package co.nz.robyroby.clackmaze.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import co.nz.robyroby.clackmaze.model.GraphNode;
import co.nz.robyroby.clackmaze.solver.Solver;
import co.nz.robyroby.clackmaze.solver.exceptions.NoPossiblePathException;
import co.nz.robyroby.clackmaze.solver.impl.BreadthFirstSolverImpl;

/**
 * This is debatably a real unit test case because of the 'indeterminate' nature of the Graph.
 * This is a good snapshot test case for sealing the behaviour against future changes.
 * The shortest path over this graph is 5, when and if this number changes it means that the solver has been modified.
 * Having also this test mitigates the "not on purpose" option.  
 */
public class RandomMazeTest {
	private static final int ULTIMATE_ANSWER = 42; // 42 tribute to Douglas Adams
	private GraphNode rootNode, endNode;
	private List<GraphNode> nodes = new ArrayList<GraphNode>(); 
	// add seed to make it consistently reproducible
	private Random r = new Random(ULTIMATE_ANSWER); 
	
	class RandomGraphNode implements GraphNode{

		private static final int MAX_LINKED_NODES = 3;
		Set<GraphNode> linkedNodes = new HashSet<GraphNode>();
		
		// I'm sorry... this graph allows loops, but it is was nice test case 
		public RandomGraphNode(List<GraphNode> nodes) {
			if (nodes.size() > 1) {
				// add a number of nodes nodes
				for (int i = 0 ; i < MAX_LINKED_NODES; i++) {
					// the set will avoid by design duplications
					GraphNode targetNode = nodes.get(r.nextInt(nodes.size()));
					linkedNodes.add(targetNode);
				}	
			}
		}

		@Override
		public Set<GraphNode> getDirectlyLinkedNodes() {
			return linkedNodes;
		}

		@Override
		public String toString() {
			// as it might (certainly) take to circularities I just output the number of the connected nodes
			return "RandomGraphNode [linkedNodes=" + linkedNodes.size() + "]";
		}

	}
	
	@Before
	public void createGraph() {
		endNode = new RandomGraphNode(nodes);
		nodes.add(endNode);
		for (int i = 0; i < 10000; i++) {
			nodes.add(new RandomGraphNode(nodes));
		}
		rootNode = new RandomGraphNode(nodes);
		nodes.add(rootNode);
		System.out.println(rootNode);
	}
	
	@Test
	public void testBigRandomGraphNode() {
		Solver solver = new BreadthFirstSolverImpl();
		try {
			final int numSteps = solver.getShortestPathDistance(rootNode, endNode);
			assertEquals(5, numSteps);
		} catch (NoPossiblePathException e) {
			fail(e.getMessage());
		}
	}

}

