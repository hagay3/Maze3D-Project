package view;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import algorithms.mazeGenerators.Maze3d;

public class MyView extends CommonView {
	
	private BufferedReader in;
    private PrintWriter out;

	/**
	 * constructor cli arguments
	 * @param input, object for the controller coming data
	 * @param output, object for the controller outcoming data
	 */
	public MyView(BufferedReader input, PrintWriter output) 
	{
		this.in = input;
		this.out = output;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				setChanged();
				notifyObservers("menu");
				while (true) {
					try {
						String commandLine = in.readLine();
						String arr[] = commandLine.split(" ");
						String command = arr[0];
						setChanged();
						notifyObservers(commandLine);
						
						// Break if the user chose 'exit'
						if (command.equals("exit")) {
							out.println("Bye bye");
							out.flush();
							break;
						}
						else if(command.isEmpty()){
							setChanged();
							notifyObservers("menu");
						}

					} catch (IOException e) {
						out.println(e.getMessage());
						out.flush();
						break;
					}
				}
			}
		});
		// Start the thread
		thread.start();

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
	public void showDirPath(String dirArray)
	{
				
		out.println("The files and directories in this path are:");
		out.flush();
		
		String arr[] = dirArray.split(" ");
		for(String s:arr)
		{
			out.println(s);
			out.flush();
		}
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showError(String message)
	{
		out.println(message);
		out.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showMaze(String mazeByteArrString)
	{
		try {
			
			byte[] byteArr = mazeByteArrString.getBytes(StandardCharsets.UTF_8);
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
	@Override
	public void showDisplayCrossSectionBy(String crossMazeBySection)
	{
		out.println(crossMazeBySection);
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showSaveMaze(String str)
	{
	    out.println(str);
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showLoadMaze(String str)
	{
		out.println(str);
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void solutionIsReady(String name)
	{
		out.println("maze " + name + " is ready");
		out.flush();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDisplaySolution(String sol)
	{	
		out.println(sol);
		out.flush();
	}

	@Override
	public void printMenu(String menu) {
		out.println(menu);
		out.flush();
		
	}

	@Override
	public void processSolution(Object solution) {
		// TODO Auto-generated method stub
		
	}

}
