package controller;
import java.util.HashMap;
import model.Model;
import view.View;

/**
 * abstract class which has the common methods and data members of all controllers
 *
 */

public abstract class CommonController implements Controller 
{	
	Model m;
	View v;
	HashMap<String, Command> stringToCommand;
	
	/**
	 * constructor
	 */
	public CommonController() 
	{
		super();
		
		stringToCommand=new HashMap<String, Command>();
		initCommands();
		
		
		stringToCommand.put("exit", new Command() {
			
			@Override
			public void doCommand(String[] args) {
				
				m.handleExitCommand(args);
			}
		});
		
		
	}
	
	
	/**
	 * sets the model of the controller,to whom it passes the command to calculate
	 */
	public void setM(Model m) {
		this.m = m;
	}

	
	/**
	 * sets the view of the controller,to whom it passes the command to be displayed
	 */
	public void setV(View v) {
		this.v = v;
		v.setStringToCommand(stringToCommand);
	}
	
	/**
	 * initialize the commands
	 */
	protected abstract void initCommands();

}
