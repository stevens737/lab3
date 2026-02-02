import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStarSearch implements SearchAlgorithm {

	private int nbNodeExpansions;
	private int maxFrontierSize;

	private Heuristics heuristics;

	private Node goalNode;

	public AStarSearch(Heuristics h) {
		this.heuristics = h;
	}

	@Override
	public void doSearch(Environment env) {
		heuristics.init(env);
		nbNodeExpansions = 0;
		maxFrontierSize = 0;
		goalNode = null;

		PriorityQueue<Node> frontier = new PriorityQueue<>();
		State startEnv = env.getCurrentState();
		Node root = new Node(startEnv, heuristics.eval(startEnv));
		frontier.add(root);

		while (!frontier.isEmpty()) {
			if (frontier.size() > maxFrontierSize) {
				maxFrontierSize = frontier.size();
			}

			Node currNode = frontier.poll();
			nbNodeExpansions++;

			if (nbNodeExpansions % 1000 == 0) {
                System.out.println("Expansions: " + nbNodeExpansions + " | Frontier: " + frontier.size());
            }

			if (env.isGoalState(currNode.state)) {
				this.goalNode = currNode;
				return;
			}

			int currGN = currNode.evaluation - heuristics.eval(currNode.state);

			for (Action action : env.legalMoves(startEnv)) {
				State nextState = env.getNextState(currNode.state, action);
				int actionCost = env.getCost(currNode.state, action);

				int nextGN = currGN + actionCost;
				int nextFN = nextGN + heuristics.eval(nextState);

				Node child = new Node(currNode, nextState, action, nextFN);
				frontier.add(child);
			}
		}

	}

	@Override
	public List<Action> getPlan() {
		if (goalNode == null) return null;
		else return goalNode.getPlan();
	}

	@Override
	public int getNbNodeExpansions() {
		return nbNodeExpansions;
	}

	@Override
	public int getMaxFrontierSize() {
		return maxFrontierSize;
	}

	@Override
	public int getPlanCost() {
		if (goalNode != null) return goalNode.evaluation;
		else return 0;
	}

}
