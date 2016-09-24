package controller;



import java.util.HashMap;

import view.View;
import model.Model;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

/**
 * specify the current controller,which passes the commands from view to model
 *
 */
public class MyController implements Controller 
{
	
	HashMap<String, Command> stringToCommand;
	private View v;
	private Model m;
	
	/**
	 * MyController constructor
	 * @param v View Object
	 * @param m Model Object
	 * @return MyController Object
	 */
	public MyController(View v, Model m) {
		this.v = v;
		this.m = m;
		stringToCommand = new HashMap<String,Command>();
		initCommands();
		v.setCommands(stringToCommand);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initCommands() 
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
		stringToCommand.put("display", new Command() {

			@Override
			public void doCommand(String[] args) {
				m.handleDisplayName(args);

			}

		});
		
		stringToCommand.put("display_cross_section", new Command()
		{

			@Override
			public void doCommand(String[] args) {
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
		stringToCommand.put("exit", new Command() {

			@Override
			public void doCommand(String[] args) {
				m.handleExit(args);

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


	/**
	 * {@inheritDoc}
	 */
	public void setM(Model m) {
		this.m = m;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setV(View v) {
		this.v = v;
		v.setStringToCommand(stringToCommand);
	}

}
