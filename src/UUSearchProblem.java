import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class UUSearchProblem {

	// used to store performance information about search runs.

	protected int nodesExplored;
	protected int maxMemory;

	protected UUSearchNode startNode;

	protected interface UUSearchNode {
		public ArrayList<UUSearchNode> getSuccessors();

		public boolean goalTest();

		public int getDepth();
	}

	// breadthFirstSearch: return a list of connecting Nodes, or null
	// no parameters, since start and goal descriptions are problem-dependent.
	// therefore, constructor of specific problems should set up start
	// and goal conditions, etc.

	public List<UUSearchNode> breadthFirstSearch() {
		resetStats();

		UUSearchNode goalz = null;
		HashMap<UUSearchNode, UUSearchNode> nodeMap = new HashMap<UUSearchNode, UUSearchNode>();
		Queue<UUSearchNode> q = new LinkedList<UUSearchNode>();
		nodeMap.put(startNode, null);
		q.add(startNode);
		incrementNodeCount();

		whyloop: while (!q.isEmpty()) {
			UUSearchNode blah = q.remove();
			ArrayList<UUSearchNode> poo = blah.getSuccessors();
			for (UUSearchNode i : poo) {
				if (!nodeMap.containsKey(i)) {
					incrementNodeCount();
					nodeMap.put(i, blah);
					q.add(i);
					updateMemory(nodeMap.size());
					if (i.goalTest()) {
						goalz = i;
						break whyloop;
					}
				}
			}
		}
		List<UUSearchNode> backchainz = backchain(goalz, nodeMap);
		return backchainz;

	}

	private List<UUSearchNode> backchain(UUSearchNode node,
			HashMap<UUSearchNode, UUSearchNode> visited) {

		if (!visited.containsKey(node))
			return null;
		LinkedList<UUSearchNode> lolz = new LinkedList<UUSearchNode>();
		lolz.addFirst(node);
		UUSearchNode boop = visited.get(node);
		lolz.addFirst(boop);
		while (boop != null) {
			boop = visited.get(boop);
			lolz.addFirst(boop);
			if (visited.get(boop) == null) {
				break;
			}
		}
		return lolz;
	}

	public List<UUSearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats();

		HashMap<UUSearchNode, Integer> currentPath = new HashMap<UUSearchNode, Integer>();
		return dfsrm(startNode, currentPath, 0, maxDepth);
	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.

	private List<UUSearchNode> dfsrm(UUSearchNode currentNode,
			HashMap<UUSearchNode, Integer> visited, int depth, int maxDepth) {

		LinkedList<UUSearchNode> path = new LinkedList<UUSearchNode>();
		visited.put(currentNode, depth);
		updateMemory(visited.size());
		incrementNodeCount();

		if (depth > maxDepth)
			return null;

		if (currentNode.goalTest()) {
			path.addFirst(currentNode);
			return path;
		}

		ArrayList<UUSearchNode> poo = currentNode.getSuccessors();
		for (UUSearchNode i : poo) {

			if (!visited.containsKey(i)
					|| (visited.containsKey(i) && depth < visited.get(i))) {
				path = (LinkedList<UUSearchNode>) dfsrm(i, visited, depth + 1,
						maxDepth);
				if (path != null) {
					path.addFirst(currentNode);
					return path;
				}
			}
		}
		return null;
	}

	// set up the iterative deepening search, and make use of dfspc
	public List<UUSearchNode> IDSearch(int maxDepth) {
		resetStats();
		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();
		HashMap<UUSearchNode, Integer> currentPathz = new HashMap<UUSearchNode, Integer>();
		List<UUSearchNode> path = new LinkedList<UUSearchNode>();
		for (int i = 0; i <= maxDepth; i++) {
			path = dfsrm(startNode, currentPathz, 0, i);
			if (path != null) {
				return path;
			}
		}
		return null;
	}

	// set up the depth-first-search (path-checking version),
	// but call dfspc to do the real work
	public List<UUSearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();

		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();
		return dfsrpc(startNode, currentPath, 0, maxDepth);
	}

	private List<UUSearchNode> dfsrpc(UUSearchNode currentNode,
			HashSet<UUSearchNode> currentPath, int depth, int maxDepth) {

		currentPath.add(currentNode);
		LinkedList<UUSearchNode> path = new LinkedList<UUSearchNode>();
		incrementNodeCount();
		updateMemory(currentPath.size());

		if (currentNode.goalTest()) {
			path.addFirst(currentNode);
			return path;
		}
		ArrayList<UUSearchNode> poo = currentNode.getSuccessors();

		for (UUSearchNode i : poo) {

			if (!currentPath.contains(i) && depth < maxDepth) {

				path = (LinkedList<UUSearchNode>) dfsrpc(i, currentPath,
						depth + 1, maxDepth);
				if (path != null) {
					path.addFirst(currentNode);
					return path;
				}
				currentPath.remove(i);
			}

		}
		return null;
	}

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}

	protected void printStats() {
		System.out.println("Nodes explored during last search:  "
				+ nodesExplored);
		System.out.println("Maximum memory usage during last search "
				+ maxMemory);
	}

	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}

	protected void incrementNodeCount() {
		nodesExplored++;
	}

}
