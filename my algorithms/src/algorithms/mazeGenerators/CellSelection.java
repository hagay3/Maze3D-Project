package algorithms.mazeGenerators;
import java.util.ArrayList;

/**
 * This interface define the way we choose the next cell in growing tree algorithm
 */
public interface CellSelection {
	public Position chooseNextCell(ArrayList<Position> visited);
}
