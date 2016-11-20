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

public class MyView implements View
{
	CLI cli;
    @SuppressWarnings("unused")
	private BufferedReader in;
    private PrintWriter out;
	HashMap<String,Command> stringToCommand;
	@SuppressWarnings("unused")
	private Controller controller;
	/**
	 * constructor cli arguments
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
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public void notifyMazeIsReady(String name) {
		out.println("maze " + name + " is ready");
		out.flush();
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public void setCommands(HashMap<String, Command> commands) {
		cli.setCommands(commands);
	}
	
	/**
	* {@inheritDoc}
	*/
	public void setStringToCommand(HashMap<String, Command> stringToCommand) 
	{
		this.stringToCommand = stringToCommand;
		cli.setStringToCommand(stringToCommand);
	}
	
	/**
	 * {@inheritDoc}
	 */
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
	    out.println(str);
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
}
