import java.util.Comparator;
import java.util.HashSet;
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

	//Task 4 Implementation

	// @Override
	// public void doSearch(Environment env) {
	// 	heuristics.init(env);
	// 	nbNodeExpansions = 0;
	// 	maxFrontierSize = 0;
	// 	goalNode = null;

	// 	PriorityQueue<Node> frontier = new PriorityQueue<>();
    //     State startState = env.getCurrentState();
        
    //     // Diagnostic: Check if the environment actually sees legal moves at the start
    //     System.out.println("DEBUG: Root legal moves: " + env.legalMoves(startState));

    //     Node root = new Node(startState, heuristics.eval(startState));
    //     frontier.add(root);

    //     while (!frontier.isEmpty()) {
    //         maxFrontierSize = Math.max(maxFrontierSize, frontier.size());
    //         Node currNode = frontier.poll();
    //         nbNodeExpansions++;

    //         if (nbNodeExpansions % 1000 == 0) {
    //             System.out.println("Expansions: " + nbNodeExpansions + " | Frontier: " + frontier.size());
    //         }

    //         if (env.isGoalState(currNode.state)) {
    //             this.goalNode = currNode;
    //             return;
    //         }

    //         // g(n) = f(n) - h(n)
    //         int currGN = currNode.evaluation - heuristics.eval(currNode.state);

    //         for (Action action : env.legalMoves(currNode.state)) {
    //             State nextState = env.getNextState(currNode.state, action);
    //             int actionCost = env.getCost(currNode.state, action);
                
    //             int nextGN = currGN + actionCost;
    //             int nextFN = nextGN + heuristics.eval(nextState);

    //             Node child = new Node(currNode, nextState, action, nextFN);
    //             frontier.add(child);
    //         }
    //     }
	// }


	// Task 6 Implementation
	
	@Override
	public void doSearch(Environment env) {
		heuristics.init(env);
		nbNodeExpansions = 0;
		maxFrontierSize = 0;
		goalNode = null;

		PriorityQueue<Node> frontier = new PriorityQueue<>();
		
		HashSet<State> closedList = new HashSet<>();

		State startState = env.getCurrentState();
		Node root = new Node(startState, heuristics.eval(startState));
		frontier.add(root);

		while (!frontier.isEmpty()) {
			maxFrontierSize = Math.max(maxFrontierSize, frontier.size());
			Node currNode = frontier.poll();

			if (closedList.contains(currNode.state)) {
				continue;
			}
			closedList.add(currNode.state);

			nbNodeExpansions++;

			if (nbNodeExpansions % 1000 == 0) {
				System.out.println("Expansions: " + nbNodeExpansions + " | Closed List: " + closedList.size());
			}

			if (env.isGoalState(currNode.state)) {
				this.goalNode = currNode;
				return;
			}

			int currGN = currNode.evaluation - heuristics.eval(currNode.state);

			for (Action action : env.legalMoves(currNode.state)) {
				State nextState = env.getNextState(currNode.state, action);
				
				if (!closedList.contains(nextState)) {
					int actionCost = env.getCost(currNode.state, action);
					int nextGN = currGN + actionCost;
					int nextFN = nextGN + heuristics.eval(nextState);

					Node child = new Node(currNode, nextState, action, nextFN);
					frontier.add(child);
				}
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
