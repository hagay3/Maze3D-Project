package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import algorithms.search.Searcher;
import algorithms.search.Solution;
import controller.Controller;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;
/**
 * generating the code behind each command
 *
 */

public class MyModel extends CommonModel {
	private HashMap<String, Maze3d> mazeCollection;//hash map with key-name of maze,value-maze 3d object
	private ExecutorService threadPool;
	private HashMap<String, String> mazeToFile;//hash map with key-name of maze,value-the file name where the maze is saved
	private Controller controller;	
	
	//hash map that hold the solution for mazes
	//I generated hashCode in maze3d,in order to find solution i already have
	//(if i got the same maze the hash code will find it,and the program will not need to solve them again)
	HashMap<Maze3d, Solution<Position>> mazeSolutions;

	/**
	 * {@inheritDoc}
	 */
	public MyModel() {
		mazeCollection = new HashMap<String, Maze3d>();
		threadPool = Executors.newCachedThreadPool();
		mazeToFile = new HashMap<String, String>();
		mazeSolutions = new HashMap<Maze3d, Solution<Position>>();
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void HandleDirPath(String[] paramArray) {

		if (paramArray == null || paramArray.length != 1) // path does'nt
															// entered
		{
			controller.passError("Invalid path");
			return;
		}
		File f = new File(paramArray[0].toString());

		if ((f.list() != null) && (f.list().length > 0)) {
			controller.passDirPath(f.list());
		} else if (f.list() == null) // invalid path
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
		
		threadPool.execute(new Runnable(){
			@Override
			public void run() {
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

		Maze3dGenerator mg = null;
		String mazeName = paramArray[0].toString(); 
		
		if (paramArray[4].intern() == "simple") {
			mg = new SimpleMaze3dGenerator();
		} else if (paramArray[4].intern() == "growing") {

			if (paramArray.length != 6) {
				controller.passError("Invalid number of parameters");
				return;
			} else if (paramArray[5].intern() == "newest") {
				// Set growing maze generator
				mg = new GrowingTreeGenerator(new newestCell());
				// generate another 3d maze
			} else if (paramArray[5].intern() == "random") {
				// Set growing maze generator
				mg = new GrowingTreeGenerator(new randomCell());
			}
		} else {
			controller.passError("Invalid algorithm name");
			return;
		}

		if (mazeCollection.containsKey(mazeName) {
			controller.passError("This name already exists,choose another one.");
			return;
		}
				Maze3d maze = mg.generate(Integer.parseInt(paramArray[1]),
						Integer.parseInt(paramArray[2]),
						Integer.parseInt(paramArray[3]));
				// Add to maze collection the maze
				mazeCollection.put(mazeName, maze);
				controller.notifyMazeIsReady(mazeName);
			}
		});

	}
	

	public class RunnableMazeGenerator implements Runnable{
		
		private Maze3dGenerator mazeGenerator;
		private Maze3d maze;
		private int x,y,z;
		
		public RunnableMazeGenerator(Maze3dGenerator m,int x,int y,int z){
			this.mazeGenerator = m;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		@Override
		public void run() {
			maze = mazeGenerator.generate(x, y, z);
			controller.notifyMazeIsReady();
			
		}
		
		public Maze3d getMaze(){
			return maze;
		}

	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void handleDisplayName(String[] paramArray) {
		if (paramArray.length != 1) {
			controller.passError("Invalid command");
			return;
		}

		if (!mazeCollection.containsKey(paramArray[0].toString())) {
			controller.passError("Maze doesn't exists");
			return;
		}

		// getting the maze from maze collection
		controller.passDisplayName(mazeCollection.get(paramArray[0].toString()).toByteArray());
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDisplayCrossSectionBy(String[] paramArray) {
		if (paramArray.length != 3) {
			controller.passError("Invalid amount of parameters");
			return;
		}

		// Checking if maze is in the collection
		if (!mazeCollection.containsKey(paramArray[2].toString())) {
			controller.passError("This maze doesn't exists");
			return;
		}

		if (paramArray[0].intern() == "x") {
			Maze3d maze = mazeCollection.get(paramArray[2].toString());
			try {
				int[][] crossSection = maze.getCrossSectionByX(Integer
						.parseInt(paramArray[1]));
				controller.passDisplayCrossSectionBy(maze
						.printMaze2d(crossSection));
				return;
			} catch (NumberFormatException e) {
				controller.passError("Invalid parameters");
				return;
			} catch (IndexOutOfBoundsException e) {
				controller.passError("Invalid x coordinate");
				return;
			}

		} else if (paramArray[0].intern() == "y") {
			Maze3d maze = mazeCollection.get(paramArray[2].toString());
			try {
				int[][] crossSection = maze.getCrossSectionByY(Integer
						.parseInt(paramArray[1]));
				controller.passDisplayCrossSectionBy(maze
						.printMaze2d(crossSection));
				return;
			} catch (NumberFormatException e) {
				controller.passError("Invalid parameters");
				return;
			} catch (IndexOutOfBoundsException e) {
				controller.passError("Invalid y coordinate");
				return;
			}
		} else if (paramArray[0].intern() == "z") {
			Maze3d maze = mazeCollection.get(paramArray[2].toString());
			try {
				int[][] crossSection = maze.getCrossSectionByZ(Integer
						.parseInt(paramArray[1]));
				controller.passDisplayCrossSectionBy(maze
						.printMaze2d(crossSection));
				return;
			} catch (NumberFormatException e) {
				controller.passError("Invalid parameters");
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

		if (paramArray.length != 2) {
			controller.passError("Invalid amount of parameters");
			return;
		}

		if (!(mazeCollection.containsKey(paramArray[0]))) {
			controller.passError("maze doesn't exists");
			return;
		}
		// saving the name of the maze,with the file where the maze is saved,in
		// order to use it later in file size command
		mazeToFile.put(paramArray[0], paramArray[1]);

		Maze3d maze = mazeCollection.get(paramArray[0].toString());

		int size = maze.toByteArray().length;

		try {
			MyCompressorOutputStream out = new MyCompressorOutputStream(
					new FileOutputStream(paramArray[1].toString()));
			// first writing the size of the maze
			out.write(ByteBuffer.allocate(4).putInt(size).array());
			out.write(maze.toByteArray());
			controller.passSaveMaze(paramArray[0].toString()
					+ " has been saved");
			out.close();
			return;
		} catch (FileNotFoundException e) {
			controller.passError("File not found");
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleLoadMaze(String[] paramArray) {
		if (paramArray.length != 2) {
			controller.passError("Invalid amount of parameters");
			return;
		}
		// checking if there are already maze with the same name
		if (mazeCollection.containsKey(paramArray[1])) {
			controller.passError("Invalid name,this name is taken");
			return;
		}

		if (mazeToFile.containsKey(paramArray[1])) {
			if (mazeToFile.get(paramArray[1]) != paramArray[0]) {
				controller.passError("This name already exists");
				return;
			}
		}
		mazeToFile.put(paramArray[1], paramArray[0]);

		try {
			MyDecompressorInputStream in = new MyDecompressorInputStream(
					new FileInputStream(paramArray[0].toString()));

			// The ByteArrayOutputStream class stream creates a buffer in memory
			// and all the data sent to the stream is stored in the buffer.
			ByteArrayOutputStream outByte = new ByteArrayOutputStream();

			// reading 4 bytes from file,which shows the size of the maze
			outByte.write(in.read());
			outByte.write(in.read());
			outByte.write(in.read());
			outByte.write(in.read());

			// creates input stream from a buffer initialize in bracelet
			ByteArrayInputStream inByte = new ByteArrayInputStream(
					outByte.toByteArray());// taking out the buffer I wrote to
			DataInputStream dis = new DataInputStream(inByte);// easier to read
																// data from
																// stream(can
																// read
																// primitive
																// types)

			// What I did:created a input stream and wrote there my 4 bytes from
			// file.
			// than created input stream,where i put my byte array,than read
			// from this buffer integer.

			byte[] byteArr = new byte[dis.readInt()];// construct a array of
														// byte,in the size
														// readen from file.
			in.read(byteArr);
			mazeCollection.put(paramArray[1], new Maze3d(byteArr));
			controller.passLoadMaze(paramArray[1]
					+ " has been loaded from file: " + paramArray[0]);
			in.close();
			return;
		} catch (FileNotFoundException e) {
			controller.passError("File not found");
			return;
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleMazeSize(String[] paramArray) 
	{
		if (paramArray.length != 1) 
		{
			controller.passError("Invalid number of parameters");
			return;
		}
		if (!mazeCollection.containsKey(paramArray[0])) 
		{
			controller.passError("maze doesn't exists");
			return;
		}

		Maze3d maze = mazeCollection.get(paramArray[0]);

		
		controller.passMazeSize(maze.toByteArray().length);
		
	}
	/**
	 * {@inheritDoc}
	 */
	public void handleFileSize(String[] paramArray)
	{
		if(paramArray.length!=1)
		{
			controller.passError("Invalid amount of parameters");
			return;
		}
		
		//checking if there is a file that have this maze
		if(!(mazeToFile.containsKey(paramArray[0])))
		{
			controller.passError("maze doesn't exists in file");
			return;
		}
		
		try
		{
			File file=new File(mazeToFile.get(paramArray[0]));//taking the file name from the hashmap
			controller.passFileSize(file.length());
			return;
		}
		catch (NullPointerException e)
		{
			controller.passError("you didn't entered path name");
			return;
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void handleSolve(String[] paramArray)
	{
		if(paramArray.length!=4)
		{
			if(paramArray.length!=2)
			{
				controller.passError("invalid amount of parameters");
				return;
			}
		}
		
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() 
			{
				StringBuilder algo=new StringBuilder();
				for(int i=1;i<paramArray.length;i++)
				{
					if(i==paramArray.length-1)
					{
						algo.append(paramArray[i]);
					}
					else
					{
						algo.append(paramArray[i]+" ");
					}
					

				}
				//check if i generated maze with this name
				if(!(mazeCollection.containsKey(paramArray[0])))
				{
					controller.passError("Maze doesn't exist");
					return;
				}
				//if i have solution for this maze
				if(mazeSolutions.containsKey(mazeCollection.get(paramArray[0])))
				{
					controller.passSolve("Solution for "+paramArray[0]+ " is ready");
					return;
				}
				
				if(algo.toString().equals("bfs"))
				{
					SearchableMaze<Position> ms=new SearchableMaze<Position>(mazeCollection.get(paramArray[0]));
					
					Searcher<Position> bfs=new BFS<Position>();
					Solution<Position> sol=bfs.search(ms);
					mazeSolutions.put(mazeCollection.get(paramArray[0]), sol);
					
					controller.passSolve("Solution for "+paramArray[0]+ " is ready");
					return;
				}
				else
				{
					controller.passError("Invalid algorithm");
					return;
				}
				
			}
		});
	}
	/**
	 * {@inheritDoc}
	 */
	public void handleDisplaySolution(String[] paramarray)
	{
		if(paramarray.length!=1)
		{
			controller.passError("Invalid amount of parameters");
			return;
		}
		
		if(!(mazeCollection.containsKey(paramarray[0])))
		{
			controller.passError("Maze with this name,doesn't exists");
			return;
		}
		
		if(mazeSolutions.containsKey(mazeCollection.get(paramarray[0])))
		{
			//take the object of maze from maze 3d,and pass it to the maze Solution,and it return the solution
			controller.passDisplaySolution(mazeSolutions.get(mazeCollection.get(paramarray[0])));
			return;
		}
		else
		{
			controller.passError("Solution doesn't exists(use solve command first)");
			return;
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public void handleExitCommand(String[] emptyArr)
	{
		threadPool.shutdown();
		try 
		{
			while(!(threadPool.awaitTermination(10, TimeUnit.SECONDS)));
		} 
		catch (InterruptedException e) {

			e.printStackTrace();
		}
		
	}
	
}
