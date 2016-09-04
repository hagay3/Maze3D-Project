package algorithms.demo;

import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.mazeGenerators.newestCell;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Searcher;
import algorithms.search.Solution;

/**
 * This is the Demo class which makes use of maze generators and 
 * solving the mazes with DFS & BFS
 */

public class Demo {
	public static void run() {
	
		//Set growing maze generator
		GrowingTreeGenerator mg = new GrowingTreeGenerator(new newestCell());
		// generate another 3d maze
		Maze3d maze=mg.generate(3,3,3);
	
		//Declare search algorithims - BFS
		SearchableMaze<Position> mySearchableMaze = new SearchableMaze<Position>(maze);
		Searcher<Position> mySearcher = new BFS<Position>();
		Solution<Position> sol= mySearcher.search(mySearchableMaze);

		//Declare search algorithims - DFS
		Searcher<Position> mySearcher1 = new DFS<Position>();
		Solution<Position> sol1= mySearcher1.search(mySearchableMaze);
		
		//Print
		maze.printMaze();
		System.out.println("BFS: "+mySearcher.getNumberOfNodesEvaluated());
		System.out.println("DFS: "+mySearcher1.getNumberOfNodesEvaluated());

		
		
	}
	
	public static void main(String[] args){
		Demo.run();
	}
	
	
}