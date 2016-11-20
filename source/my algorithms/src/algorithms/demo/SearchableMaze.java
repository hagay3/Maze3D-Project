package algorithms.demo;
import java.util.ArrayList;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Searchable;
import algorithms.search.State;

/**
 * This is an object adpater class that implements Searchable interface.
 * Its purpose is to be sent to the actual Searcher algorithm.
 */

@SuppressWarnings({ "hiding", "unchecked" })
public class SearchableMaze<Position> implements Searchable{

	private Maze3d maze;
	final double COST=1;
	
	public SearchableMaze(Maze3d maze) {
		this.maze = maze;
	}
	
	
	
	@Override
	public <Position> State<Position> getStartState() {
		return new State<Position>((Position)maze.getFirstCellAfterShellPostition(maze.getStartPosition()));
	}
	

	
	@Override
	public <Position> State<Position> getGoalState() {
		return new State<Position>((Position)maze.getFirstCellAfterShellPostition(maze.getGoalPosition()));
	}

	/**
	  * This method is used to get all possible States biy given State.
	  * @param s This is the State to check what next steps can be from it.
	  * @return ArrayList of possible States.
	  */
	
	@Override
	public <Position> ArrayList<State<Position>> getAllPossibleStates(State<Position> s) {
		ArrayList<State<Position>> states = new ArrayList<State<Position>>();
		
		ArrayList<Position> moves = (ArrayList<Position>) maze.getPossibleMoves((algorithms.mazeGenerators.Position)s.getValue());
		
		for (Position pos : moves) {
			states.add(new State<Position>(pos));
		}
		return states;
	}

	
	/**
	  * This method is used to get the "cost" to move from one State to another.
	  * @param currState This is the first State
	  * @return neighboor This is the second State
	  */
	@Override
	public <Position> double getMoveCost(State<Position> currState, State<Position> neighbor) {
		return COST;
	}
	

}
