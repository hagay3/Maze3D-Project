package algorithms.mazeGenerators;
/**
 * This abstract class is used as a common class to maze generators.
 * This class is a layer between all kinds of generators and the interface.
 * It is part of a scalable design and define maze generator.
 */

public abstract class Maze3dGeneratorAbstract implements Maze3dGenerator{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Maze3d generate(int x, int y, int z);
	
	/**
	 * Measures the time takes to generate a maze
	 * @param x axis
	 * @param y axis
	 * @param z axis
	 * @return String time in the format "100 Milliseconds" 
	 */
	@Override
	public String measureAlgorithmTime(int x, int y, int z)
	{
		// Declare variables
		long timeMillisStart,timeMillisEnd;
		String Time;
		
		// Calculate the time of generate function
		timeMillisStart = System.currentTimeMillis();
		this.generate(x, y, z);
		
		timeMillisEnd = System.currentTimeMillis();
		
		// Convert time to string
		long elapsed = timeMillisEnd - timeMillisStart;
		Time = String.valueOf(elapsed);
		return Time + " MilliSeconds";
	}
}
	
	
