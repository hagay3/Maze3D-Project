package model;
import controller.Controller;

/**
 * the model interface perform all the background calculations,than pass them to the controller.
 * this is the facade of the model
 *
 */

public interface Model {
	/**
	 * Handling the command :dir <path>
	 * @param args Array of strings,containing one string with path
	 */
	
	public void setController(Controller controller);
	public void HandleDirPath(String[] paramArray);
	/**
	 * Handling the command:generate 3d maze <name> <x> <y> <z> <algorithm>
	 * name-name of the maze,x-amount of floors in maze,y-amount of rows,z-amount of columns,
	 * algorithm-prim/simple (generating algorithms)
	 * @param paramArray Array of strings with the parameters i mentioned above
	 */
	public void handleGenerate3dMaze(String[] paramArray);
	/**
	 * Handling the command:display <name>(name of the maze)
	 * @param paramArray Array with one string,the name of the maze that needs to be displayed
	 */
	public void handleDisplayName(String[] paramArray);
	/**
	 * Handling the command:display cross section by {x,y,z} <index> for <name>
	 * @param paramArray Array of strings containing the parameters above
	 */
	public void handleDisplayCrossSectionBy(String[] paramArray);
	/**
	 * Handling the command:save_maze <name> <file name>
	 * name-maze name generated before,file name-the name of the file to save_maze to
	 * @param paramArray array of strings with the parameters above
	 */
	public void handleSaveMaze(String[] paramArray);
	/**
	 * handling command:load maze <file name> <name>
	 * loading maze to the file specified
	 * @param paramArray array of strings with file name and maze name
	 */
	public void handleLoadMaze(String[] paramArray);
	/**
	 * handling command:maze size <name>
	 * measure the size of maze in memory
	 * @param paramArray array of one string with the name of the file
	 */
	public void handleMazeSize(String[] paramArray);
	/**
	 * handling command:file size <name>
	 * measure the size of maze in a file
	 * @param paramArray array with one string that contain the maze name,than i will find the file of this maze,
	 * and check the size of the maze in this file
	 */
	public void handleFileSize(String[] paramArray);
	/**
	 * handle command:solve <name> <algorithm>
	 * solves the maze specified,with specified algorithm
	 * @param paramArray array of 2 strings:cell 0-name of the maze,1-the algorithm that meant to find the solution of the maze
	 */
	public void handleSolve(String[] paramArray);
	/**
	 * handle command:display solution <name>
	 * display an existing solution
	 * @param paramarray array of one string with the name of the maze
	 */
	public void handleDisplaySolution(String[] paramarray);
	/**
	 * closing the running threads
	 * @param emptyArr there is nothing there
	 */
	public void handleExitCommand(String[] emptyArr);
}
