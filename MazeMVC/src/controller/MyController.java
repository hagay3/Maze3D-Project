package controller;



import view.View;
import model.Model;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

/**
 * specify the current controller,which passes the commands from view to model
 *
 */
public class MyController extends CommonController 
{
	
	private View v;
	private Model m;
	
	/**
	 * constructor
	 */
	public MyController(View v1, Model m1) {
		this.v = v1;
		this.m = m1;
		initCommands();
		v.setCommands(stringToCommand);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initCommands() 
	{
		stringToCommand.put("dir",new Command()
		{
			@Override
			public void doCommand(String[] args) {
				m.HandleDirPath(args);
			}
		});
		
		stringToCommand.put("generate_3d_maze", new Command()
		{

			@Override
			public void doCommand(String[] args) {
				m.handleGenerate3dMaze(args);
				
			}
			
		});
		
		stringToCommand.put("help", new Command(){

			@Override
			public void doCommand(String[] args) {
				v.showHelp();
				
			}
			
		});
		stringToCommand.put("display", new Command()
		{

			@Override
			public void doCommand(String[] args) {
				m.handleDisplayName(args);
				
			}
			
		});
		
		stringToCommand.put("display_cross_section", new Command()
		{

			@Override
			public void doCommand(String[] args) 
			{
				m.handleDisplayCrossSectionBy(args);
				
			}
		
		});
		stringToCommand.put("save_maze",new Command() {
			
			@Override
			public void doCommand(String[] args) {
				m.handleSaveMaze(args);
				
			}
		});
		stringToCommand.put("load_maze", new Command(){

			@Override
			public void doCommand(String[] args) {
				m.handleLoadMaze(args);
				
			}
			
		});
		
		stringToCommand.put("solve", new Command(){

			@Override
			public void doCommand(String[] args) {
				m.handleSolve(args);
				
			}
			
		});
		stringToCommand.put("display_solution", new Command() {
			
			@Override
			public void doCommand(String[] args) {
				m.handleDisplaySolution(args);
				
			}
		});
		
		
	}
	
	@Override
	public void notifyMazeIsReady(String name) {
		v.notifyMazeIsReady(name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void passDirPath(String[] dirArray)
	{
		v.showDirPath(dirArray);
	}
	/**
	 * {@inheritDoc}
	 */
	public void passError(String message)
	{
		v.showError(message);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void passDisplayName(byte[] byteArr)
	{
		v.showDisplayName(byteArr);
	}
	/**
	 * {@inheritDoc}
	 */
	public void passDisplayCrossSectionBy(String crossSection)
	{
		v.showDisplayCrossSectionBy(crossSection);
	}
	/**
	 * {@inheritDoc}
	 */
	public void passSaveMaze(String str)
	{
		v.showSaveMaze(str);
	}
	/**
	 * {@inheritDoc}
	 */
	public void passLoadMaze(String str)
	{
		v.showLoadMaze(str);
	}
	/**
	 * {@inheritDoc}
	 */
	public void passMazeSize(int size)
	{
		v.showMazeSize(size);
	}
	/**
	 * {@inheritDoc}
	 */
	public void passFileSize(long length)
	{
		v.showFileSize(length);
	}
	/**
	 * {@inheritDoc}
	 */
	public void passSolve(String message)
	{
		v.showSolve(message);
		
	}
	/**
	 * {@inheritDoc}
	 */
	public void passDisplaySolution(Solution<Position> sol )
	{
		v.showDisplaySolution(sol);
	}

	
}
