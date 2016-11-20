package model;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import algorithms.demo.SearchableMaze;
import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.mazeGenerators.newestCell;
import algorithms.mazeGenerators.randomCell;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import controller.Controller;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;

/**
 * Maintain the backend side within "my algorithms" package
 * It handles the commands and returns the right output back to the controller.
 *
 */

public class MyModel implements Model {
	//hash map with key-name of maze,value-maze 3d object
	private HashMap<String, Maze3d> mazeCollection;
	//Threads pool to handle threads efficiently
	private ExecutorService threadPool;
	//hash map with key-name of maze,value-the file name where the maze is saved
	private HashMap<String, String> mazeToFile;
	private Controller controller;	
	//hash map that hold the solution for mazes
	HashMap<String, Solution<Position>> mazeSolutions;

	/**
	 * {@inheritDoc}
	 */
	public MyModel() {
		mazeCollection = new HashMap<String, Maze3d>();
		threadPool = Executors.newCachedThreadPool();
		mazeToFile = new HashMap<String, String>();
		mazeSolutions = new HashMap<String, Solution<Position>>();
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void HandleDirPath(String[] paramArray) {

		if (paramArray == null || paramArray.length != 1) {
			controller.passError("Invalid path");
			return;
		}
		File f = new File(paramArray[0].toString());

		if ((f.list() != null) && (f.list().length > 0)) {
			controller.passDirPath(f.list());
			// invalid path
		} else if (f.list() == null) 
		{
			controller.passError("Invalid path");
			return;
		} else // if there is nothing in the list
		{
			controller.passError("Empty folder");
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleGenerate3dMaze(String[] paramArray) {

		if (paramArray == null) {
			controller.passError("Invalid number of parameters");
			return;
		}
		
		if (paramArray.length != 5 && paramArray.length != 6) {
			controller.passError("Invalid number of parameters");
			return;
		}
		try {
			if ((Integer.parseInt(paramArray[1]) <= 0)
					|| (Integer.parseInt(paramArray[2]) <= 0)
					|| (Integer.parseInt(paramArray[3]) <= 0)) {
				controller.passError("Invalid parameters");
				return;
			}
		} catch (NumberFormatException e) {
			controller.passError("Invalid parameters");
			return;
		}
		
		//Set the paramArray to variables
		String mazeName = paramArray[0];
		int x = Integer.parseInt(paramArray[1]);
		int y = Integer.parseInt(paramArray[2]);
		int z = Integer.parseInt(paramArray[3]);
		String algorithm = paramArray[4];
		

		if (mazeCollection.containsKey(mazeName)) {
			controller.passError("This maze name already exists,choose another one.");
			return;
		}
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				
				String growingOption = "";
				if(paramArray.length == 6)
					growingOption = paramArray[5].toString();
				Maze3dGenerator mg = null;
				

				if (algorithm.equals("simple") && paramArray.length == 5) {
					//Set Simple maze generator
					mg = new SimpleMaze3dGenerator();
				} else if (algorithm.equals("growing") && paramArray.length == 6) {
					
					if (growingOption.equals("newest")) {
						// Set growing maze generator
						mg = new GrowingTreeGenerator(new newestCell());
						// generate another 3d maze
					} else if (growingOption.equals("random")) {
						// Set growing maze generator
						mg = new GrowingTreeGenerator(new randomCell());
					}
				} else {
					controller.passError("Invalid algorithm name or parameters");
					return;
				}

				//Generate the maze
				Maze3d maze = mg.generate(x,y,z);
				// Add the maze to maze collection
				mazeCollection.put(mazeName, maze);
				controller.notifyMazeIsReady(mazeName);
			}
		});
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	public void handleDisplayName(String[] paramArray) {
		if (paramArray == null || paramArray.length != 1) {
			controller.passError("Invalid command");
			return;
		}

		if (!mazeCollection.containsKey(paramArray[0].toString())) {
			controller.passError("Maze doesn't exists");
			return;
		}

		try {
			// getting the maze from maze collection
			controller.passDisplayName(mazeCollection.get(
					paramArray[0].toString()).toByteArray());
		} catch (IOException e) {
			controller.passError(e.getMessage());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDisplayCrossSectionBy(String[] paramArray) {
		if (paramArray== null || paramArray.length != 3) {
			controller.passError("Invalid amount of parameters");
			return;
		}

		// Checking if maze is in the collection
		if (!mazeCollection.containsKey(paramArray[2].toString())) {
			controller.passError("This maze doesn't exists");
			return;
		}
		
		//Checking if actual valid digits given
		try {
			if (Integer.parseInt(paramArray[1]) < 0) {
				controller.passError("Invalid parameters");
				return;
			}
		} catch (NumberFormatException e) {
			controller.passError("Invalid parameters");
			return;
		}
		//Get the index
		int index;
		try{
			index = Integer.parseInt(paramArray[1]);
		} catch(NumberFormatException e){
			controller.passError("The index is not an integer:" + e.getMessage());
			return;
		}
		
		String axis = paramArray[0];
		String mazeName = paramArray[2];
		Maze3d maze = mazeCollection.get(mazeName);
		
		
		
		if (axis.equals("x")) {
			try {
				int[][] crossSection = maze.getCrossSectionByX(index);
				controller.passDisplayCrossSectionBy(maze
						.printMaze2d(crossSection));
				return;
			} catch (IndexOutOfBoundsException e) {
				controller.passError("Invalid x coordinate");
				return;
			}
		} else if (axis.equals("y")) {
			try {
				int[][] crossSection = maze.getCrossSectionByY(index);
				controller.passDisplayCrossSectionBy(maze
						.printMaze2d(crossSection));
				return;
			} catch (IndexOutOfBoundsException e) {
				controller.passError("Invalid y coordinate");
				return;
			}
		} else if (axis.equals("z")) {
			try{
				int[][] crossSection = maze.getCrossSectionByZ(index);
				controller.passDisplayCrossSectionBy(maze
						.printMaze2d(crossSection));
				return;
			} catch (IndexOutOfBoundsException e) {
				controller.passError("Invalid z coordinate");
				return;
			}
		} else {
			controller.passError("Invalid parameters");
			return;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleSaveMaze(String[] paramArray) {

		if (paramArray == null || paramArray.length != 2) {
			controller.passError("Invalid amount of parameters");
			return;
		}
		
		String mazeName = paramArray[0];
		String mazeFileName = paramArray[1];

		if (!(mazeCollection.containsKey(mazeName))) {
			controller.passError("maze doesn't exists");
			return;
		}
		
		if (mazeToFile.containsValue(mazeFileName)) {
			controller.passError("File already exists");
			return;
		}
		
		// get Maze3d object
		Maze3d maze = mazeCollection.get(mazeName);

		try {			
			OutputStream out = new MyCompressorOutputStream(
					new FileOutputStream(mazeFileName));
			//Convert maze to byte array
			byte[] byteArr = maze.toByteArray();
			//Write the size first
			out.write(ByteBuffer.allocate(4).putInt(byteArr.length).array());
			out.write(byteArr);
			out.close();
			mazeToFile.put(mazeName, mazeFileName);
			controller.passSaveMaze(mazeName + " has been saved");
			return;
		} catch (FileNotFoundException e) {
			controller.passError("File not found");
			return;
		} catch (IOException e) {
			controller.passError(e.getMessage());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleLoadMaze(String[] paramArray) {
		if (paramArray == null || paramArray.length != 2) {
			controller.passError("Invalid amount of parameters");
			return;
		}
		
		String mazeName = paramArray[1];
		String mazeFileName = paramArray[0];
		
		// checking if there are already maze with the same name
		if (mazeCollection.containsKey(mazeName) || mazeToFile.containsKey(mazeName)) {
			controller.passError("Invalid name,this maze name is taken");
			return;
		}
		

		try {
			MyDecompressorInputStream in = new MyDecompressorInputStream(new FileInputStream(mazeFileName));

			// The ByteArrayOutputStream class stream creates a buffer in memory
			// and all the data sent to the stream is stored in the buffer.
			ByteArrayOutputStream outByte = new ByteArrayOutputStream();

			// reading 4 bytes from file,which shows the size of the maze
			outByte.write(in.read());
			outByte.write(in.read());
			outByte.write(in.read());
			outByte.write(in.read());

			// creates input stream from a buffer initialize in bracelet
			ByteArrayInputStream inByte = new ByteArrayInputStream(outByte.toByteArray());
			DataInputStream dis = new DataInputStream(inByte);

			byte[] byteArr = new byte[dis.readInt()];// construct a array of byte,in the size readen from file.
			in.read(byteArr);
			in.close();
			Maze3d loaded = new Maze3d(byteArr);
			
			mazeCollection.put(mazeName, loaded);
			mazeToFile.put(mazeName, mazeFileName);
			controller.passLoadMaze(mazeName + " has been loaded from file "+ mazeFileName);
			return;
		} catch (FileNotFoundException e) {
			controller.passError("File not found " + e.getMessage());
			return;
		} catch (IOException e) {

			e.printStackTrace();
		}

	}



	/**
	 * {@inheritDoc}
	 */
	public void handleSolve(String[] paramArray) {
		if (paramArray == null ||  paramArray.length != 2) {
			controller.passError("invalid amount of parameters");
			return;
		}

		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				String mazeName = paramArray[0];
				String algorithm = paramArray[1];

				// check if i generated maze with this name
				if (!(mazeCollection.containsKey(mazeName))) {
					controller.passError("Maze doesn't exist");
					return;
				}
				// if solution exists for this maze
				if (mazeSolutions.containsKey(mazeName)) {
					controller.passSolve("Solution for maze " + mazeName
							+ " is already done");
					return;
				}

				Searcher<Position> searcher;
				Solution<Position> sol;
				SearchableMaze<Position> searchableMaze = new SearchableMaze<Position>(
						mazeCollection.get(mazeName));

				if (algorithm.equals("bfs")) {
					searcher = new BFS<Position>();
				} else if (algorithm.equals("dfs")) {
					searcher = new DFS<Position>();
				} else {
					controller.passError("Invalid algorithm");
					return;
				}
				// Generate solution
				sol = searcher.search(searchableMaze);
				mazeSolutions.put(mazeName, sol);
				controller.passSolve("Solution for " + mazeName + " is ready");
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDisplaySolution(String[] paramarray) {
		if (paramarray == null || paramarray.length != 1) {
			controller.passError("Invalid amount of parameters");
			return;
		}
		
		String mazeName = paramarray[0];
		if (!(mazeCollection.containsKey(mazeName))) {
			controller.passError("Maze with this name,doesn't exists");
			return;
		}

		if (mazeSolutions.containsKey(mazeName)) {
			// take the object of maze from maze 3d,and pass it to the maze
			// Solution,and it return the solution
			controller.passDisplaySolution(mazeSolutions.get(mazeName));
			return;
		} else {
			controller
					.passError("Solution doesn't exists(use solve command first)");
			return;
		}
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	public void handleExit(String[] paramarray) {	
		//Terminate threads
		threadPool.shutdownNow();
		try{
			threadPool.awaitTermination(100,TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e){
			controller.passError(e.getMessage());
		}	
	}
	
}
