package controller;
import model.Model;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import view.View;

/**
 * the controller links between the view and the model,which don't know each other
 */
public interface Controller {

	/**
	 * notify the view when the maze is ready
	 * @param name of the maze
	 */
	public void notifyMazeIsReady(String name);
	
	/**
	 * initcommands will create the map between the cli command and the command object
	 */
	public void initCommands();
	
	/**
	 * sets the model of the controller,in order to initialize the controller in the main
	 * @param m model
	 */
	void setM(Model m);
	
	/**
	 * sets the view of the controller
	 * @param v view
	 */
	void setV(View v);
	

	/**
	 * transfer the names of files and directories to the view
	 * @param dirArray array of strings containing the names of files and directories in the specified path
	 */
	public void passDirPath(String[] dirArray);
	
	/**
	 * if there is an error in the command writen by the client,passes the error to the view
	 * @param message the error string
	 */
	public void passError(String message);
	
	
	/**
	 * passes the generated maze to the view,to display it.
	 * pass it as byte array in order to save space
	 * @param byteArr byte array containing the maze
	 */
	public void passDisplayName(byte[] byteArr);
	
	/**
	 * passes the cross section to view
	 * @param crossSection cross section 2d array
	 */
	public void passDisplayCrossSectionBy(String crossSection);
	
	/**
	 * pass the string that saying the maze has been saved
	 * @param str string that tells-maze has been saved
	 */
	public void passSaveMaze(String str);
	
	/**
	 * pass the string that states:the maze has been loaded
	 * @param str message,maze has been loaded
	 */
	public void passLoadMaze(String str);
	
	/**
	 * passing a message that the maze has been solved
	 * @param message string with the phrase maze has been solved
	 */
	public void passSolve(String message);
	
	/**
	 * passing the solution of the maze
	 * @param sol solution of the maze
	 */
	public void passDisplaySolution(Solution<Position> sol);
	
	
}
