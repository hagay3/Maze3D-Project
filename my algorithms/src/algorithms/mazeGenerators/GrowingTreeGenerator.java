package algorithms.mazeGenerators;
import java.util.Random;
import java.util.ArrayList;

/**
 * This class is the GrowingTree algorithm to generate a maze.
 * It overrides Maze3dGeneratorAbstract which defines mazes functionality.
 * Basically, it`s choosing cell by cell and back trace then until
 * it get`s to a cell with no neighbors and after all the neighbors
 * back traced and their neighbors visited.
 */

public class GrowingTreeGenerator extends Maze3dGeneratorAbstract {
	
	//The way to choose next cell
	CellSelection cellSelector;
	
	public GrowingTreeGenerator(CellSelection selector){
		this.cellSelector = selector;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Maze3d generate (int x, int y, int z){
		Maze3d growMaze = new Maze3d(2*x+1, 2*y+1, 2*z+1);
		growMaze.fillWithWalls();
		growMaze.setStartPosition(growMaze.randomPositionInShell());
		growMaze.setCellValue(growMaze.getStartPosition(), 0);
		
		//cellsList is used to backtrack p and next neighbors
		ArrayList<Position> cellsList=new ArrayList<Position>();
		Position p = growMaze.getFirstCellAfterShellPostition(growMaze.startPosition);
		growMaze.setCellValue(p, 0);
		
		//Start from the startPosition
		cellsList.add(p);
		
		//Random number
		Random rand=new Random();
		Position neigbor = new Position (0,0,0);
		
		//Keep going until cellsList is empty
		while (!cellsList.isEmpty()){
			
			//Choose next cell by given choice in the constructor
			p=cellSelector.chooseNextCell(cellsList);
			growMaze.setCellValue(p, 0);
			
			//Set the unvisited neighbors
			ArrayList<Position> unvisited=unvisitedNeighbors(p, growMaze);
			
			//If the algorithm visited all neighbors of p, remove p 
			if(unvisited.isEmpty())
				cellsList.remove(p);
			//Else - keep break through some neigbor of p
			else{	
				
				neigbor = unvisited.get(rand.nextInt(unvisited.size()));
				cellsList.add(neigbor);
				growMaze.setCellValue(neigbor, 0);
				growMaze.removeWallBetweenPositions(p, neigbor);
				
			}
			
		}
		
		//Break path until it get to upper boundary and then set Goal Position
		Position next = neigbor;
		while(neigbor.getY() != growMaze.yLength-2){
			next = growMaze.movePosition(neigbor, "up");
			growMaze.setCellValue(next, 0);
			growMaze.removeWallBetweenPositions(neigbor, next);
			neigbor = next;
		}
		//Set goal position after breaing a way through
		growMaze.setGoalPosition(new Position(neigbor.getX(),neigbor.getY()+1,neigbor.getZ()));
		
		return growMaze;
	
	}
	
	/**
	   * This method is used to check which neighbors is not visited yes
	   * by a given position and maze.
	   * @param p This is the position that will be checked for unvisited neigbors.
	   * @param maze This is the maze to check the unvisited neighbors of p.
	   * @return ArrayList<Position> It is a list of unvisited neigbors,
	   */
	//Returns unvisited neighbors 
	public ArrayList<Position> unvisitedNeighbors(Position p,Maze3d maze)
	{
		ArrayList<Position> neighbors=new ArrayList<Position>();
		String []directions={"backward","forward","right","left","up","down"};
		Position neigbor;
	
		for(int i=0; i<directions.length; i++){
			
			if(maze.possibleStep(p,directions[i])){
				neigbor = maze.movePosition(p,directions[i]);
				if(maze.getValueOfPosition(neigbor) == 1){
					neighbors.add(neigbor);
				}
					
			}
		}
		return neighbors;
	}
}
