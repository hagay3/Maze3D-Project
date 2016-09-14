package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;
import controller.Command;
import controller.Controller;

public class MyView extends CommonView
{
	CLI cli;
    private BufferedReader in;
    private PrintWriter out;
	HashMap<String,Command> stringToCommand;
	private Controller controller;
	/**
	 * constructor cli arguments
	 * @param cm controller of this view
	 * @param input, object for the controller coming data
	 * @param output, object for the controller outcoming data
	 */
	public MyView(BufferedReader input, PrintWriter output) 
	{
		this.in = input;
		this.out = output;
		cli = new CLI(input,output);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		cli.start();
		
	}
	
	@Override
	public void notifyMazeIsReady(String name) {
		out.println("maze " + name + " is ready");
		out.flush();
	}
	
	@Override
	public void setCommands(HashMap<String, Command> commands) {
		cli.setCommands(commands);
	}
	
	/**
	 * setting the hash map that was initialized in the controller to the view
	 */
	public void setStringToCommand(HashMap<String, Command> stringToCommand) 
	{
		this.stringToCommand = stringToCommand;
		cli.setStringToCommand(stringToCommand);
	}
	
	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void showDirPath(String[] dirArray)
	{
				
		out.println("The files and directories in this path are:");
		out.flush();
		
		for(String s:dirArray)
		{
			out.println(s);
			out.flush();
		}
		
	}
	/**
	 * {@inheritDoc}
	 */
	public void showError(String message)
	{
		out.println(message);
		out.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	public void showDisplayName(byte[] byteArr)
	{
		try {
			//Convert maze from byeArray
			Maze3d maze3d = new Maze3d(byteArr);
			
			//Print Maze3d
			out.println(maze3d.printMaze());
			out.println("The start position: ");
			out.println(maze3d.getStartPosition());
			out.println("\nThe goal position: ");
			out.println(maze3d.getGoalPosition());
			out.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public void showDisplayCrossSectionBy(String crossMazeBySection)
	{
		out.println(crossMazeBySection);
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	public void showSaveMaze(String str)
	{
		System.out.println(str);
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	public void showLoadMaze(String str)
	{
		out.println(str);
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	public void showMazeSize(int size)
	{
		out.println("The size of the maze is "+size);
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	public void showFileSize(long length)
	{
		out.println("The size of the maze in file is: "+length);
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	public void showSolve(String message)
	{
		out.println(message);
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	public void showDisplaySolution(Solution<Position> sol)
	{
		ArrayList<State<Position>> al= (ArrayList<State<Position>>) sol.getStates();
		
		out.println(al.toString());
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	public void showHelp()
	{
       out.println("Help Center:");
       out.println("dir <path>                                           						-display the files and directories in this specific path.");
       out.println("generate_3d_maze <name> <x> <y> <z> <algorithm> [growing_tree_option]");
       out.println("-generating maze with given name,with xyz dimensions");
       out.println("algorithm: simple/growing");
       out.println("growing_tree_option: newest/random for cell selection\n");
       out.println("display <name>                                       						-display the specified maze");
       out.println("display_cross_section {x,y,z} <index> <maze_name>  				    		-diplaying cross section(x,y or z,chose one) in the index specified for maze with this name");
       out.println("save_maze <name> <file name>                         						-save maze in file name specified");
       out.println("load_maze <file name> <name>                        					    -load maze from file specified");
       out.println("maze_size <name>                                     						-display the size of maze in ram");
       out.println("file_size <name>                                     						-display the size of maze in file");
       out.println("solve <name> <algorithm>                             						-solve maze with specified algorithm:bfs/dfs");
       out.println("display_solution <name>                              						-solve the maze and show the solution");
       out.println("exit                                                 						-exit the program");
       out.println();
       out.println("<> -You have to write the requested string inside,{} -choose one of the fallwing and write inside the brackets");
       out.flush();
	}
	
}
