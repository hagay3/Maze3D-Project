package boot;
import java.util.ArrayList;
import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.mazeGenerators.newestCell;


public class Run {

private static void testMazeGenerator(Maze3dGenerator mg){
	
	// prints the time it takes the algorithm to run
	System.out.println(mg.measureAlgorithmTime(10,10,50));
	System.out.print("\n");
	
	// generate another 3d maze
	Maze3d maze=mg.generate(3,6,5);
	
	// get the maze entrance
	Position p=maze.getStartPosition();
	
	// print the position
	p.print(); // format "{x,y,z}"
	System.out.print("\n");
	
	// get all the possible moves from a position
	ArrayList<Position> moves=maze.getPossibleMoves(p);
	
	// print the moves
	System.out.println("Possible moves:");
	for(Position move : moves)
		move.print();
	System.out.print("-------------------");
	
	
	System.out.print("\n");
	
	// prints the maze exit position
	maze.getGoalPosition().print();
	System.out.print("\n");
	
	try{
		// get 2d cross sections of the 3d maze
		
		int[][] maze2dx=maze.getCrossSectionByX(2);
		
		maze.printMaze2d(maze2dx);
		
		System.out.print("\n");
		
		int[][] maze2dy=maze.getCrossSectionByY(3);
		
		maze.printMaze2d(maze2dy);
		
		System.out.print("\n");
		
		int[][] maze2dz=maze.getCrossSectionByZ(0);
		
		maze.printMaze2d(maze2dz);
		
		// this should throw an exception!
		
		maze.getCrossSectionByX(-1);
	
	} 
	
	catch (IndexOutOfBoundsException e){
	
		System.out.println("good!");
	
	 }
	
	}
	
	public static void main(String[] args) {
	
		testMazeGenerator(new SimpleMaze3dGenerator());
		
		testMazeGenerator(new GrowingTreeGenerator(new newestCell()));

	}

}