package fred.AI;

import java.util.List;

import fred.model.Territory;

public interface PathHeuristic {
	int pathValue(List<Territory> path);
	int pathLastValue(List<Territory> path, int penultimateValue);
	boolean IsNodeValid(Territory node, List<Territory> path,int index);
}
