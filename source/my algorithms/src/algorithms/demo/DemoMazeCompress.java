package algorithms.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.newestCell;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;


public class DemoMazeCompress {

	public static void main(String[] args){
		try{
			
			//Set growing maze generator
			GrowingTreeGenerator mg = new GrowingTreeGenerator(new newestCell());
			// generate another 3d maze
			Maze3d maze=mg.generate(10,30,10);
			
			// save it to a file
			OutputStream out = new MyCompressorOutputStream(new FileOutputStream("2.maz"));
			
			out.write(maze.toByteArray());
			out.flush();
			out.close();
			InputStream in= new MyDecompressorInputStream(new FileInputStream("2.maz"));
			byte b[]=new byte[maze.toByteArray().length];
			in.read(b);
			in.close();
			Maze3d loaded=new Maze3d(b);
			
			//Print mazes
			System.out.println(loaded.printMaze());
			System.out.println("-------------------");
			System.out.println(maze.printMaze());
		
			//Check if the mazes equal to each other
			System.out.println(maze.equals(loaded));
		}
		//Throw IOException in case reading or writing to file fails
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

}
