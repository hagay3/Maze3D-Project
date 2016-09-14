package model;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;




public class RunnableMazeGenerator implements Runnable{
	
	private Maze3dGenerator mazeGenerator;
	private Maze3d maze;
	private String name;
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
		
	}
	
	public Maze3d getMaze(){
		return maze;
	}

}
