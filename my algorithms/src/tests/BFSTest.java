package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import algorithms.demo.SearchableMaze;
import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.mazeGenerators.newestCell;
import algorithms.mazeGenerators.randomCell;
import algorithms.search.BFS;
import algorithms.search.Searcher;
import algorithms.search.Solution;

public class BFSTest {

	@Test
	public void SerachGrowingTreeNewesetTest() {
		// Set growing maze generator
		GrowingTreeGenerator mg = new GrowingTreeGenerator(new newestCell());
		// generate another 3d maze
		Maze3d maze = mg.generate(10, 10, 10);

		// Declare search algorithm - BFS
		SearchableMaze<Position> mySearchableMaze = new SearchableMaze<Position>(
				maze);
		Searcher<Position> mySearcher1 = new BFS<Position>();
		Solution<Position> sol1 = new Solution<Position>();
		assertEquals(sol1.getClass(), mySearcher1.search(mySearchableMaze).getClass());

	}

	@Test
	public void SerachGrowingTreeRandomTest() {
		// Set growing maze generator
		GrowingTreeGenerator mg = new GrowingTreeGenerator(new randomCell());
		// generate another 3d maze
		Maze3d maze = mg.generate(10, 10, 10);

		// Declare search algorithm - BFS
		SearchableMaze<Position> mySearchableMaze = new SearchableMaze<Position>(
				maze);
		Searcher<Position> mySearcher1 = new BFS<Position>();
		Solution<Position> sol1 = new Solution<Position>();
		assertEquals(sol1.getClass(), mySearcher1.search(mySearchableMaze).getClass());

	}

	@Test
	public void SerachSimpleTest() {
		// Set growing maze generator
		SimpleMaze3dGenerator mg = new SimpleMaze3dGenerator();
		// generate another 3d maze
		Maze3d maze = mg.generate(10, 10, 10);

		// Declare search algorithm - BFS
		SearchableMaze<Position> mySearchableMaze = new SearchableMaze<Position>(maze);
		Searcher<Position> mySearcher1 = new BFS<Position>();
		Solution<Position> sol1 = new Solution<Position>();
		assertEquals(sol1.getClass(), mySearcher1.search(mySearchableMaze)
				.getClass());
	}

	@Test
	public void SerachNullTest() {

		// generate another 3d maze
		Maze3d maze = new Maze3d(10, 10, 10);
		Position start = new Position(1, 1, 1);
		Position end = new Position(3, 1, 1);
		maze.setStartPosition(start);
		maze.setGoalPosition(end);

		// Declare search algorithm - BFS
		SearchableMaze<Position> mySearchableMaze = new SearchableMaze<Position>(maze);
		Searcher<Position> mySearcher1 = new BFS<Position>();
		assertNull(mySearcher1.search(mySearchableMaze));

		Position start2 = new Position(-11, 1, 1);
		maze.setStartPosition(start2);
		assertNull(mySearcher1.search(mySearchableMaze));

	}

	@Test(timeout = 10000)
	public void SerachTimeoutTest() {

		// Set growing maze generator
		SimpleMaze3dGenerator mg = new SimpleMaze3dGenerator();
		// generate another 3d maze
		Maze3d maze = mg.generate(50, 10, 10);

		// Declare search algorithm - BFS
		SearchableMaze<Position> mySearchableMaze = new SearchableMaze<Position>(maze);
		Searcher<Position> mySearcher1 = new BFS<Position>();
		Solution<Position> sol1 = new Solution<Position>();
		assertEquals(sol1.getClass(), mySearcher1.search(mySearchableMaze).getClass());

	}

	@Test
	public void SerachEvaluatedNodesTest() {

		// Set growing maze generator
		SimpleMaze3dGenerator mg = new SimpleMaze3dGenerator();
		// generate another 3d maze
		Maze3d maze = mg.generate(5, 5, 5);
		Position start = new Position(1, 1, 1);
		Position end = new Position(1, 2, 1);
		maze.setStartPosition(start);
		maze.setGoalPosition(end);

		// Declare search algorithm - BFS
		SearchableMaze<Position> mySearchableMaze = new SearchableMaze<Position>(maze);
		Searcher<Position> mySearcher1 = new BFS<Position>();
		mySearcher1.search(mySearchableMaze);
		assertEquals(1, mySearcher1.getNumberOfNodesEvaluated());
	}

}
