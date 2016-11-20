package algorithms.mazeGenerators;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class Implements selection of cell of random cell in a List.
 * For growing tree maze generator.
 */

public class randomCell implements CellSelection{
	
	/**
	   * This method is used to return a random cell in a given List.
	   * @param visited is a list of visited positions.
	   * @return Position It is a random position in the list.
	   */
	@Override
	public Position chooseNextCell(ArrayList<Position> visited){
		Random rand=new Random();
		return visited.get(rand.nextInt(visited.size()));
	}
}
