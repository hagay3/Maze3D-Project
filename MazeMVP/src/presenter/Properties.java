package presenter;

import java.io.Serializable;

/**
 * a class that holds the properties of the maze creation
 */
public class Properties implements Serializable {

	/**
	 * serial number
	 */
	private static final long serialVersionUID = 42L;

	private int numberOfThreads;
	private String algorithmToSearch;
	private String algorithmToGenerateMaze;
	private String typeOfUserInterfece;

	/**
	 * constructor using fields
	 * 
	 * @param numberOfThreads
	 *            number of threads allowed to run in the model
	 * @param algorithmToSearch
	 *            which algorithm to search the maze:bfs, dfs
	 * @param algorithmToGenerateMaze
	 *            which algorithm to generate the maze: simple,growing random, growing newest
	 * @param typeOfUserInterfece
	 *            which user interface to use:cli/gui
	 */
	public Properties(int numberOfThreads, String algorithmToSearch,
			String algorithmToGenerateMaze, String typeOfUserInterfece) {
		this.numberOfThreads = numberOfThreads;
		this.algorithmToSearch = algorithmToSearch;
		this.algorithmToGenerateMaze = algorithmToGenerateMaze;
		this.typeOfUserInterfece = typeOfUserInterfece;
	}


	/**
	 * default constructor 
	 * @param properties object(containing the properties of the project)
	 */
	public Properties(){
		this.numberOfThreads = 10;
		this.algorithmToGenerateMaze = "growing random";
		this.algorithmToSearch = "bfs";
		this.typeOfUserInterfece = "gui";
	}
	
	
	/**
	 * copy constructor 
	 * @param properties object(containing the properties of the project)
	 */
	public Properties(Properties p) {
		this.numberOfThreads = p.numberOfThreads;
		this.algorithmToGenerateMaze = p.algorithmToGenerateMaze;
		this.algorithmToSearch = p.algorithmToSearch;
		this.typeOfUserInterfece = p.typeOfUserInterfece;
	}

	/**
	 * return the type of user interface
	 * 
	 * @return String with the type of user interface:cli/gui
	 */
	public String getTypeOfUserInterfece() {
		return typeOfUserInterfece;
	}

	/**
	 * setting the type of user interface
	 * 
	 * @param typeOfUserInterfece
	 *            String with the type of user interface:cli/gui
	 */
	public void setTypeOfUserInterfece(String typeOfUserInterfece) {
		this.typeOfUserInterfece = typeOfUserInterfece;
	}

	/**
	 * return the number of threads allowed
	 * 
	 * @return number of threads
	 */
	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	/**
	 * setting the number of threads
	 * 
	 * @param numberOfThreads
	 *            number of threads
	 */
	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	/**
	 * getting the algorithm,with whom we search the maze: bfs,dfs
	 * 
	 * @return String with the name of the algorithm
	 */
	public String getAlgorithmToSearch() {
		return algorithmToSearch;
	}

	/**
	 * setting the algorithm we search with it the path from the begging to the
	 * end of the game
	 * 
	 * @param algorithmToSearch
	 *            name of the algorithm (bfs, dfs)
	 */
	public void setAlgorithmToSearch(String algorithmToSearch) {
		this.algorithmToSearch = algorithmToSearch;
	}

	/**
	 * getting the algorithm we generate with,the maze(simple ,growing random, growing newest)
	 * 
	 * @return name of the algorithm
	 */
	public String getAlgorithmToGenerateMaze() {
		return algorithmToGenerateMaze;
	}

	/**
	 * setting the algorithm the generate with the maze
	 * 
	 * @param algorithmToGenerateMaze
	 *            name of algorithm(simple ,growing_newest,growing_random)
	 */
	public void setAlgorithmToGenerateMaze(String algorithmToGenerateMaze) {
		this.algorithmToGenerateMaze = algorithmToGenerateMaze;
	}

}
