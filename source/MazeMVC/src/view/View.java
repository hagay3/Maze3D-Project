package view;



import java.util.HashMap;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import controller.Command;
import controller.Controller;


/**
 * display all the outcome of the commands
 *
 */
public interface View {
	/**
	 * starting the command line interface
	 */
	public void start();
	
	/**
	 * Sets controller
	 * @param Controller 
	 */
	public void setController(Controller controller);
	
	/**
	* Setter for commands
	* @param commands 
	*/
	public void setCommands(HashMap<String, Command> commands);
	
	
	/**
	* Add a notification that maze is ready 
	* @param name of the maze
	*/
	public void notifyMazeIsReady(String name);
	/**
	 * initialize the data structure which holds the commands names and objects
	 * @param stringToCommand hash map with key-command name,value-command object
	 */
	public void setStringToCommand(HashMap<String, Command> stringToCommand);
	/**
	 * display the outcome of command:dir <path>
	 * displaying the files and directories of the specified path
	 * @param dirArray string's array with the names of files and directories in the specified path
	 */
	public void showDirPath(String[] dirArray);
	/**
	 * display the error in the commands that the client wrote
	 * @param message string telling what is the error
	 */
	public void showError(String message);
	
	/**
	 * displaying the specified maze
	 * @param byteArr byte array representing the maze
	 */
	public void showDisplayName(byte[] byteArr);
	/**
	 * displaying the cross section which the client asked for
	 * @param crossMazeBySection 2d array with the cross section asked
	 */
	public void showDisplayCrossSectionBy(String crossMazeBySection);
	/**
	 * displaying the string:the maze has been saved
	 * @param str string with the word:maze has been saved
	 */
	public void showSaveMaze(String str);
	/**
	 * displaying the string:the maze has been loaded
	 * @param str string with the word:maze has been loaded
	 */
	public void showLoadMaze(String str);
	/**
	 * displaying the string:solution for maze is ready
	 * @param message string with the words:solution for maze is ready
	 */
	public void showSolve(String message);
	/**
	 * displaying the solution of the specified maze
	 * @param sol the solution of the maze
	 */
	public void showDisplaySolution(Solution<Position> sol);
}
