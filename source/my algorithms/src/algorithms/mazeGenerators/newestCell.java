package algorithms.mazeGenerators;
import java.util.ArrayList;


/**
 * This class Implements selection of cell of the newest cell in a list.
 * for growing tree maze generator
 */

public class newestCell implements CellSelection{
	 
	/**
	   * This method is used to return the newest cell in the List
	   * @param visited is a list of visited positions
	   * @return Position It is the newest position in the list.
	   */
	@Override
	public Position chooseNextCell(ArrayList<Position> visited){
		return visited.get(visited.size()-1);
	}
}
