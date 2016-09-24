package algorithms.mazeGenerators;
/**
 * This interface is used define the maze generators behavior.
 */
public interface Maze3dGenerator {
	
	 /**
	   * This method is used to generate the maze.
	   * @param x This is length of x Axis for the maze,in real world it means
	   *  floors,or layers of 3D Cube.
	   * @param y This is the y Axis of the maze. It is the rows of a 2D maze.
	   * @param z This is the z Axis of the maze, It is the columns of a 2D maze. 
	   * @return Maze3D , It is a 3d Maze object.
	   */
	public Maze3d generate(int x,int y, int z); 
	
	
	/**
	   * This method is used to measure maze generation in time.
	   * @param x,y,z is the maze size. 
	   * @return String is the genration time in ms.
	   * e.g "50 MilliSeconds"
	   */
	public String measureAlgorithmTime(int x, int y, int z);
}
