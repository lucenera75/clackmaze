package co.nz.robyroby.clackmaze.solver;

import co.nz.robyroby.clackmaze.model.GraphNode;
import co.nz.robyroby.clackmaze.solver.exceptions.NoPossiblePathException;

public abstract class Solver {
	public abstract int getShortestPathDistance(
			GraphNode startNode,
			GraphNode finishNode) throws NoPossiblePathException;
}
