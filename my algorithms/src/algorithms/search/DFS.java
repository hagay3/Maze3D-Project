package algorithms.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * DFS generic class extends CommonSearcher<T>
 * It is an Iterative DFS using stack
 * @param <T> the type of class we will work with
 */
public class DFS<T> extends CommonSearcher<T> {
	
	private Stack<State<T>> stateStack;
	private ArrayList<State<T>> visited;
	
	public DFS() {
		evaluatedNodes = 0;
		this.stateStack=new Stack<State<T>>();
		this.visited = new ArrayList<State<T>>();
	}
	/**
	* {@inheritDoc}
	*/
	@Override
	public Solution<T> search(Searchable s) {
		//Add the startState to the stack
		stateStack.add(s.getStartState());
		//Set the goal state, this one will be the loop breaker
		State<T> goalState = s.getGoalState();
		
		//Keep going until there is no more States to check
		while (!stateStack.isEmpty()) {
			//Pop form stack the last pushed State
			State<T> currState  = stateStack.pop();
			//This State marked visited
			visited.add(currState);
			//Increase the number of checked nodes counter
			evaluatedNodes++;
			
			//If we got here we found the solution and algorithm is finished
			if (currState.equals(goalState)) {
				return backTrace(currState);
			}
			
			//Fetch all the possdible states for currState
			List<State<T>> neighbors = s.getAllPossibleStates(currState);
			
			//Loop all over the possible States for currState
			for (State<T> neighbor : neighbors) {
				//If this State not visited yet, make it part of the solution
				if(!visited.contains(neighbor)) {	
					neighbor.setCameFrom(currState);
					stateStack.push(neighbor);
			    }//if
					
			 }//for
			
		}
		return null;
	}
}
