package model;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import presenter.Properties;
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
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;

/**
 * Maintain the backend side within "my algorithms" package
 * It handles the commands and returns the right output back to the controller.
 *
 */

public class MyModel extends Observable implements Model{
	//hash map with key-name of maze,value-maze 3d object
	private HashMap<String, Maze3d> mazeCollection;
	//Threads pool to handle threads efficiently
	private ExecutorService threadPool;
	//hash map with key-name of maze,value-the file name where the maze is saved
	private HashMap<String, String> mazeToFile;	
	//hash map that hold the solution for mazes
	HashMap<Maze3d, Solution<Position>> mazeSolutions;
	//Properties that loaded from xml
	Properties properties;

	/**
	 * MyMazeModel constructor
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public MyModel(Properties properties) throws ClassNotFoundException, IOException {
		this.properties = properties;
		mazeCollection = new HashMap<String, Maze3d>();
		threadPool = Executors.newFixedThreadPool(properties.getNumberOfThreads());
		mazeToFile = new HashMap<String, String>();
		mazeSolutions = loadGzipSolutions();
		
		if(mazeSolutions == null){
			mazeSolutions = new HashMap<Maze3d, Solution<Position>>();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void HandleDirPath(String[] paramArray) {

		if (paramArray == null || paramArray.length != 1) {
			notifyMyObservers("error Invalid path");	
			return;
		}
		File f = new File(paramArray[0].toString());

		if ((f.list() != null) && (f.list().length > 0)) {
			notifyMyObservers("passDirpath "+f.list().toString());	
			return;
			// invalid path
		} else if (f.list() == null) {
			notifyMyObservers("error Invalid path");	
			return;
		} else // if there is nothing in the list
		{
			notifyMyObservers("error Empty folder");	
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleGenerate3dMaze(String[] paramArray) {

		if (paramArray == null) {
			notifyMyObservers("error Invalid number of parameters");	
			return;
		}
		
		if (paramArray.length != 5 && paramArray.length != 6) {
			notifyMyObservers("error Invalid number of parameters");	
			return;
		}
		try {
			if ((Integer.parseInt(paramArray[1]) <= 0)
					|| (Integer.parseInt(paramArray[2]) <= 0)
					|| (Integer.parseInt(paramArray[3]) <= 0)) {
				notifyMyObservers("error Invalid parameters");	
				return;
			}
		} catch (NumberFormatException e) {
			notifyMyObservers("error Invalid parameters");	
			return;
		}
		
		//Set the paramArray to variables
		String mazeName = paramArray[0];
		int x = Integer.parseInt(paramArray[1]);
		int y = Integer.parseInt(paramArray[2]);
		int z = Integer.parseInt(paramArray[3]);
		String algorithm = paramArray[4];
		

		if (mazeCollection.containsKey(mazeName)) {
			notifyMyObservers("error This maze name already exists,choose another one.");	
			return;
		}
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {

				String growingOption = "";
				if (paramArray.length == 6)
					growingOption = paramArray[5].toString();
				Maze3dGenerator mg = null;

				if (algorithm.equals("simple") && paramArray.length == 5) {
					// Set Simple maze generator
					mg = new SimpleMaze3dGenerator();
				} else if (algorithm.equals("growing")
						&& paramArray.length == 6) {

					if (growingOption.equals("newest")) {
						// Set growing maze generator
						mg = new GrowingTreeGenerator(new newestCell());
						// generate another 3d maze
					} else if (growingOption.equals("random")) {
						// Set growing maze generator
						mg = new GrowingTreeGenerator(new randomCell());
					}else{
						notifyMyObservers("error Invalid algorithm name or parameters");
						return;
					}
				}
				
				//Generate the maze
				Maze3d maze = mg.generate(x,y,z);
				// Add the maze to maze collection
				mazeCollection.put(mazeName, maze);
				maze.setName(mazeName);
				notifyMyObservers("notifyMazeIsReady "+ mazeName);	
				return;
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void handleShowMaze(String[] paramArray) {
		if (paramArray == null || paramArray.length != 1) {
			notifyMyObservers("error Invalid command");
			return;
		}

		if (!mazeCollection.containsKey(paramArray[0].toString())) {
			notifyMyObservers("error Maze doesn't exists");
			return;
		}
		try {
			//Convert maze to byte array
			byte[] mazeByteArray = mazeCollection.get(paramArray[0].toString()).toByteArray();
			
			// From byte array to string
			String mazeByteArrayString = new String(mazeByteArray, "UTF-8");
			
			notifyMyObservers("passDisplayMaze "+mazeByteArrayString);	
			return;
		} catch (IOException e) {
			notifyMyObservers("error "+e.getMessage());
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDisplayCrossSectionBy(String[] paramArray) {
		if (paramArray== null || paramArray.length != 3) {
			notifyMyObservers("error Invalid amount of parameters");
			return;
		}

		// Checking if maze is in the collection
		if (!mazeCollection.containsKey(paramArray[2].toString())) {
			notifyMyObservers("error This maze doesn't exists");
			return;
		}
		
		//Checking if actual valid digits given
		try {
			if (Integer.parseInt(paramArray[1]) < 0) {
				notifyMyObservers("error Invalid parameters");
				return;
			}
		} catch (NumberFormatException e) {
			notifyMyObservers("error Invalid parameters");
			return;
		}
		//Get the index
		int index;
		try{
			index = Integer.parseInt(paramArray[1]);
		} catch(NumberFormatException e){
			notifyMyObservers("error The index is not an integer");
			return;
		}
		
		String axis = paramArray[0];
		String mazeName = paramArray[2];
		Maze3d maze = mazeCollection.get(mazeName);
		String crossSectionMazeString = "";
		
		
		if (axis.equals("x")) {
			try {
				int[][] crossSection = maze.getCrossSectionByX(index);
				crossSectionMazeString = maze.printMaze2d(crossSection);
			} catch (IndexOutOfBoundsException e) {
				notifyMyObservers("error Invalid c coordinate");
				return;
			}
		} else if (axis.equals("y")) {
			try {
				int[][] crossSection = maze.getCrossSectionByY(index);
				crossSectionMazeString = maze.printMaze2d(crossSection);
			} catch (IndexOutOfBoundsException e) {
				notifyMyObservers("error Invalid y coordinate");
				return;
			}
		} else if (axis.equals("z")) {
			try{
				int[][] crossSection = maze.getCrossSectionByZ(index);
				crossSectionMazeString = maze.printMaze2d(crossSection);
			} catch (IndexOutOfBoundsException e) {
				notifyMyObservers("error Invalid z coordinate");
				return;
			}
		} else {
			notifyMyObservers("error Invalid parmaters");
			return;
		}
		
		notifyMyObservers("passDisplayCrossSectionBy " + crossSectionMazeString);
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleSaveMaze(String[] paramArray) {

		if (paramArray == null || paramArray.length != 2) {
			notifyMyObservers("error Invalid amount of parmaters");
			return;
		}
		
		String mazeName = paramArray[0];
		String mazeFileName = paramArray[1];

		if (!(mazeCollection.containsKey(mazeName))) {
			notifyMyObservers("error maze doesn't exists");
			return;
		}
		
		if (mazeToFile.containsValue(mazeFileName)) {
			notifyMyObservers("error File already exists");
			return;
		}
		
		// get Maze3d object
		Maze3d maze = mazeCollection.get(mazeName);

		try {			
			OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
			//Convert maze to byte array
			byte[] byteArr = maze.toByteArray();
			//Write the size first
			out.write(ByteBuffer.allocate(4).putInt(byteArr.length).array());
			out.write(byteArr);
			out.close();
			mazeToFile.put(mazeName, mazeFileName);
			notifyMyObservers("passSaveMaze "+mazeName+" has been saved");
			return;
		} catch (FileNotFoundException e) {
			notifyMyObservers("error File not found");
			return;
		} catch (IOException e) {
			notifyMyObservers("error "+e.getMessage());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleLoadMaze(String[] paramArray) {
		if (paramArray == null || paramArray.length != 2) {
			notifyMyObservers("error Invalid amount of parmaters");
			return;
		}
		
		String mazeName = paramArray[1];
		String mazeFileName = paramArray[0];
		
		// checking if there are already maze with the same name
		if (mazeCollection.containsKey(mazeName) || mazeToFile.containsKey(mazeName)) {
			notifyMyObservers("error Invalid name,this maze name is taken");
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
			notifyMyObservers("passLoadMaze "+ mazeName + " has been loaded from file "+ mazeFileName);
			return;
		} catch (FileNotFoundException e) {
			notifyMyObservers("error File not found " + e.getMessage());
			return;
		} catch (IOException e) {
			notifyMyObservers("error "+e.getMessage());
			return;
		}

	}



	/**
	 * {@inheritDoc}
	 */
	public void handleSolve(String[] paramArray) {
		if (paramArray == null ||  paramArray.length != 2) {
			notifyMyObservers("error Invalid amount of parmaters\n");
			return;
		}

		
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				String mazeName = paramArray[0];
				String algorithm = paramArray[1];

				
				// check if i generated maze with this name
				if (!(mazeCollection.containsKey(mazeName))) {
					notifyMyObservers("error Maze doesn't exist");
					return;
				}
				// if solution exists for this maze
				if (mazeSolutions.containsKey(mazeCollection.get(mazeName))) {
					notifyMyObservers("passSolve " + mazeName);
					return;
				}

				Searcher<Position> searcher = null;
				Solution<Position> sol;
				Maze3d mazeToSolve = mazeCollection.get(mazeName);
				SearchableMaze<Position> searchableMaze = new SearchableMaze<Position>(mazeToSolve);

				switch (algorithm) {
				case "bfs":
					searcher = new BFS<Position>();
				case "dfs":
					searcher = new DFS<Position>();
				default:
					if(searcher == null){
						notifyMyObservers("error Invalid algorithm");
						return;
					}
				}
				// Generate solution
				sol = searcher.search(searchableMaze);
				mazeSolutions.put(mazeToSolve, sol);
			
				notifyMyObservers("passSolve " + mazeName);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDisplaySolution(String[] paramarray) {

		if (paramarray == null || paramarray.length != 1) {
			notifyMyObservers("error Invalid amount of parmaters");
			return;
		}
		String mazeName = paramarray[0];
		if (!(mazeCollection.containsKey(mazeName))) {
			notifyMyObservers("error Maze with this name,doesn't exists");
			return;
		}

		Maze3d maze = mazeCollection.get(mazeName);
		if (mazeSolutions.containsKey(maze)) {
			// take the object of maze from maze 3d,and pass it to the maze
			// Solution,and it return the solution
			notifyMyObservers("passDisplaySolution "+ mazeSolutions.get(maze).toString());
			return;
		} else {
			notifyMyObservers("error Solution doesn't exists (use solve command first)");
			return;
		}
	}
	
	/**
	 * get the solution object to process on more complex view
	 * @param paramarray - the maze name to solve and the algorithm bfs/dfs
	 */
	@Override
	public void handleGetSolution(String[] paramarray) {

		if (paramarray == null || paramarray.length != 1) {
			notifyMyObservers("error Invalid amount of parmaters");
			return;
		}
		String mazeName = paramarray[0];
		if (!(mazeCollection.containsKey(mazeName))) {
			notifyMyObservers("error Maze with this name,doesn't exists");
			return;
		}

		Maze3d maze = mazeCollection.get(mazeName);
		if (mazeSolutions.containsKey(maze)) {
			// take the object of maze from maze 3d,and pass it to the maze
			// Solution,and it return the solution
			setChanged();
			notifyObservers(mazeSolutions.get(maze));
			return;
		} else {
			notifyMyObservers("error Solution doesn't exists (use solve command first)");
			return;
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleExit(String[] paramarray) {
		// Save solutions to file
		try {
			saveGzipSolutions(mazeSolutions);
		} catch (IOException e) {
			notifyMyObservers("error " + e.getMessage());
		}
		// Terminate threads
		threadPool.shutdownNow();
		try {
			threadPool.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			notifyMyObservers("error " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyMyObservers(String s) {
		setChanged();
		notifyObservers(s);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveGzipSolutions(HashMap<Maze3d, Solution<Position>> solutions)
			throws IOException {
		ObjectOutputStream objectOut = null;
		FileOutputStream out = null;
		File file = new File("resources/solutions.zip");
		try {
			file.createNewFile();
			out = new FileOutputStream(file);
			objectOut = new ObjectOutputStream(new GZIPOutputStream(out));
			objectOut.writeObject(solutions);
			objectOut.flush();
		} catch (IOException e) {
			notifyObservers("error " + e.getMessage());
		} finally {
			if (objectOut != null)
				objectOut.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public HashMap<Maze3d, Solution<Position>> loadGzipSolutions()
			throws IOException, ClassNotFoundException {
		File file = new File("resources/solutions.zip");
		ObjectInputStream objectIn = null;
		HashMap<Maze3d, Solution<Position>> loadedSolutions = new HashMap<Maze3d, Solution<Position>>();
		try {
			objectIn = new ObjectInputStream(new GZIPInputStream(
					new FileInputStream(file)));
			loadedSolutions = (HashMap<Maze3d, Solution<Position>>) objectIn
					.readObject();
			objectIn.close();
			for (Maze3d maze : loadedSolutions.keySet()) {
				mazeCollection.put(maze.getName(), maze);
			}
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
		return loadedSolutions;
	}
}

