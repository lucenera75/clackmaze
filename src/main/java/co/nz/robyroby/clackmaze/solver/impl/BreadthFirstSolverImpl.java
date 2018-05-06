package co.nz.robyroby.clackmaze.solver.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import co.nz.robyroby.clackmaze.model.GraphNode;
import co.nz.robyroby.clackmaze.solver.Solver;
import co.nz.robyroby.clackmaze.solver.exceptions.NoPossiblePathException;

/**
 * A bfs implementation guarantee that whenever I touch the finishNode that would be the shortest path. The Big O (the wow effect for
 * friends) is supposed to be O(|N| + |E|) tending to O(|N|) if the graph is sparse and O(|N^2|) if the graph is dense (and cyclic). The
 * number of "edges" to traverse |E| is |N|^2 in the most dense case, considering also loops. Again, in this case it is not actually N^2 but
 * the most dense case would be ( |N|-1 + |N|-2 + |N|-3 + |N|-4 ... + |N| - m) where m < |N|
 * 
 * Even if we assume the Graph as acyclic I'm recording the visited nodes anyways to avoid the multifather effect This is not necessary,
 * because we don't risk a loop (being the graph acyclic), but in this case I would discuss my concerns before choosing the final
 * implementation.
 * 
 * Assuming an acyclic graph allows us to use a dfs (but without recording visited nodes, because this would 'blind' other branches). The
 * issue in this case is that we would need to traverse all the possible paths and find all the ways we have to reach the finishPoint, and
 * only at the end compare the costs. That makes it an inelegant solution.
 */
public class BreadthFirstSolverImpl extends Solver {

	@Override
	public int getShortestPathDistance(GraphNode startNode, GraphNode finishNode) throws NoPossiblePathException {
		if (startNode == null || finishNode == null) {
			throw new IllegalArgumentException("Both startNode and finishNode must be not null");
		}
		Set<GraphNode> alreadyVisitedNodes = new HashSet<>();

		Queue<GraphNode> queue = new LinkedList<>();
		int numSteps = 0;

		// visit the startNode
		visitNode(alreadyVisitedNodes, queue, startNode);

		while (!queue.isEmpty()) {

			numSteps += 1;
			Set<GraphNode> nodes = new HashSet<>(queue);
			queue.clear();

			nodes.forEach(node -> {
				// visit the all the linked nodes
				node.getDirectlyLinkedNodes().forEach(linkedNode -> visitNode(alreadyVisitedNodes, queue, linkedNode));
			});

			if (queue.contains(finishNode)) {
				return numSteps;
			}
		}

		// If the queue is empty and I didn't get to the finishNode that means that the finish node is unreachable
		throw new NoPossiblePathException("It is not possible to go from startNode to finishNode");
	}

	private void visitNode(Set<GraphNode> alreadyVisitedNodes, Queue<GraphNode> queue, GraphNode node) {
		if (!alreadyVisitedNodes.contains(node)) {
			queue.add(node);
			alreadyVisitedNodes.add(node);
		}
	}

}
