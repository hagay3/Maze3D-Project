package algorithms.search;
import java.util.ArrayList;
import java.util.PriorityQueue;


/**
 * BFS generic class extends CommonSearcher<T>
 * @param <T> the type of class we will work with
 */
public class BFS<T> extends CommonSearcher<T> {
	//Priority queue to hold all the opened state
	private PriorityQueue<State<T>> openList;
	//ArrayList to see what States actually checked already 
	private ArrayList<State<T>> closedList;
	
	public BFS() {
		evaluatedNodes = 0;
		this.openList=new PriorityQueue<State<T>>();
		this.closedList= new ArrayList<State<T>>();
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public Solution<T> search(Searchable s) {
		//Fetch the start state from Searchable and add it to open list
		openList.add(s.getStartState());
		//Get the goal state, this will be the loop breaker to know when we have the solution
		State<T> goalState = s.getGoalState();
		//Keep going until there is no more States to check
		while (!openList.isEmpty()) {
			//Poll the best State form queue,dequeue
			State<T> currState = openList.poll();
			
			
			//This State has ben checked!
			closedList.add(currState);
			//Increase the number of checked nodes counter
			evaluatedNodes++;
			
			//If we got here we found the solution and algorithm is finished
			if (currState.equals(goalState)) {
				return backTrace(currState);
			}
			
			//Fetch all the possdible states for currState
			ArrayList<State<T>> neighbors = s.getAllPossibleStates(currState);
			
			//Loop all over the possible States for currState
			for (State<T>neighbor : neighbors) {
				
				
				//If the State is not in open list and not in the closedList go in and make it part of
				//the solution
				if (!openList.contains(neighbor) && !openList.contains(neighbor)) {
					
					neighbor.setCameFrom(currState);
					neighbor.setCost(currState.getCost() + s.getMoveCost(currState, neighbor));
					openList.add(neighbor);
				}
				//Otherwise check if there is a better path to go with
				else {
					double newPathCost = currState.getCost() + s.getMoveCost(currState, neighbor);
					if (neighbor.getCost() > newPathCost) {
						
						neighbor.setCost(newPathCost);
						neighbor.setCameFrom(currState);
						
						openList.remove(neighbor);
						openList.add(neighbor);
						
						if (!openList.contains(neighbor))
							openList.add(neighbor);
						
						else{
							closedList.remove(neighbor);
						}
					}
				}		
			}
		}
		return null;
	}

	
}
