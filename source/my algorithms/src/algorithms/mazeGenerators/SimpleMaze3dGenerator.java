package algorithms.mazeGenerators;
import java.util.Random;


/**
 * This class is the SimpleMaze algorithm to generate a maze.
 * It overrides Maze3dGeneratorAbstract which defines mazes functionality.
 * Basically, it`s throwing random walls in the maze, them create
 * one possible pass through between entrance and exit.
 */

public class SimpleMaze3dGenerator extends Maze3dGeneratorAbstract{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Maze3d generate (int x, int y, int z){
		Maze3d simpleMaze = new Maze3d(2*x+1,2*y+1,2*z+1);
		
		//Random number
		Random rand=new Random();
		
		//Set the entrance
		simpleMaze.setStartPosition(simpleMaze.randomPositionInShell());
		simpleMaze.setGoalPosition(simpleMaze.randomPositionInShell());
		
		
		
		//Check if entrance and goal is not equal
		while(simpleMaze.getStartPosition().equals(simpleMaze.getGoalPosition())){
			simpleMaze.setGoalPosition(simpleMaze.randomPositionInShell());
		}
		
		//Put 0 on entrance and goal
		simpleMaze.setCellValue(simpleMaze.getStartPosition(), 0);
		simpleMaze.setCellValue(simpleMaze.getGoalPosition(), 0);
		
		//count zeroes to avoid maze with no walls
		int countZero = 0;
		int r;
		
		//Put random walls
		for(int i=1;i<x*2;i++)
		{
			for(int j=2;j<y*2;j++)
			{
				for(int n=2;n<z*2;n++)
				{
					r = rand.nextInt(2);
					countZero++;
					if(countZero == 3)
					{
						r = 1;
						countZero = 0;
					}
					simpleMaze.maze[i][j][n] = r;
				}
			}
		}
		
		//Make random path
		String []directions={"backward","forward","right","left","up","down"};
		Position pathPosition = simpleMaze.getFirstCellAfterShellPostition(simpleMaze.startPosition);
		Position nextPosition;
		simpleMaze.setCellValue(pathPosition, 0);
		int randDirection;
		String randDirectionText;
		
		
		while(!simpleMaze.isNeighbors(pathPosition, simpleMaze.getGoalPosition(),1)){
			//Algorithm is too random so when its reach to the right X Position its stops random "backward","forward"
			if(pathPosition.getX() == simpleMaze.getGoalPosition().getX()){
				directions[0] = "right";
				directions[1] = "left";
			}
			//Covering use case if eXit is on boundaries of X
			if(simpleMaze.getGoalPosition().getX() == 0 || simpleMaze.getGoalPosition().getX() == simpleMaze.xLength - 1){
				if(pathPosition.getX() == simpleMaze.getGoalPosition().getX() - 1){
					directions[0] = "right";
					directions[1] = "left";
				}
			}
			//Randomize direction
			randDirection = rand.nextInt(6);
			randDirectionText = directions[randDirection];
			//Make the path less random as it can make empty mazes
			if(simpleMaze.possibleStep(pathPosition, randDirectionText)){
				nextPosition = simpleMaze.movePosition(pathPosition, randDirectionText);
				simpleMaze.setCellValue(nextPosition, 0);
				simpleMaze.removeWallBetweenPositions(pathPosition, nextPosition);
				pathPosition = simpleMaze.movePosition(pathPosition, randDirectionText);
			}
			
		}	
		return simpleMaze;
	}
}
