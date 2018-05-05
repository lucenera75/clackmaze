package co.nz.robyroby.clackmaze.model;

import java.util.Set;

public interface GraphNode {
	/**
	 * Returns all the GraphNodes directly linked to this GraphNode. These are considered to be distance 1 from this node.
	 */
	Set<GraphNode> getDirectlyLinkedNodes();
}
