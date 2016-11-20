package algorithms.search;

import java.util.ArrayList;

/**
 * This interface is used define the Searchable object behavior.
 * A Searchable object can be solved by Searcher.
 */
public interface Searchable {
	<T> State<T> getStartState();
	<T> State<T> getGoalState();
	<T> ArrayList<State<T>> getAllPossibleStates(State<T> s);
	<T> double getMoveCost(State<T> currState, State<T> neighbor);
}
