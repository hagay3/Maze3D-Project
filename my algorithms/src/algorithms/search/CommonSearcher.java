package algorithms.search;
import java.util.List;


/**
 * This method is used to be a common base class for search algorithms.
 * It is a layer between Searcher interface and the algorithms.
 * @param <T> is the type of class the algorithm will work with.
 */

public abstract class CommonSearcher<T> implements Searcher<T> {

	protected int evaluatedNodes;
	/**
	* {@inheritDoc}
	*/
	@Override
	public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}
	
	
	/**
	 * This method is used to backtrace the path between GoalState to StartState,
	 * it generates the content of the solution.
	 * @param goalState is the goalState to backtrace from.
	 * @return Solution<T> It includes the list of States for solving the problem.
	 */
	protected Solution<T> backTrace(State<T> goalState) {
		Solution<T> sol = new Solution<T>();
		
		State<T> currState = goalState;
		List<State<T>> states = sol.getStates();
		while (currState != null) {		
			states.add(0, currState);
			currState = currState.getCameFrom();
		}
		sol.setStates(states);
		return sol;
	}
	

}
