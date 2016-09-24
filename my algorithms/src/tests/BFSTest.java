package tests;
import static org.junit.Assert.*;
import org.junit.Test;
import algorithms.demo.SearchableMaze;
import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.newestCell;
import algorithms.search.BFS;
import algorithms.search.Searcher;
import algorithms.search.Solution;

public class BFSTest {
	
	private Solution<Position> solution;
	private Searcher<Position> mySearcher;
	Maze3d maze;
	SearchableMaze<Position> mySearchableMaze;

	public BFSTest(){
		// Set growing maze generator
		GrowingTreeGenerator mg = new GrowingTreeGenerator(new newestCell());
		// generate another 3d maze
		this.maze = mg.generate(10, 10, 10);

		// Declare search algorithm - BFS
		this.mySearchableMaze = new SearchableMaze<Position>(maze);
		this.mySearcher = new BFS<Position>();
		this.solution = mySearcher.search(mySearchableMaze);
	}
	
	
	@Test
	public void mySearcherIsNotNullTest() {
		assertNotNull(mySearcher);
	}


	@Test
	public void validNumberOfEvalutedNodes() {
		assertEquals(true, mySearcher.getNumberOfNodesEvaluated() > 0);
	}

	@Test
	public void numOfEvalutedNodesVsNumOfStates() {
		assertEquals(true, mySearcher.getNumberOfNodesEvaluated() >= solution.getStates().size());
	}
	
	@Test
	public void searcherIsNullWhenMazeIsNullTest() {
		assertEquals(null, mySearcher.search(null));
	}

}
