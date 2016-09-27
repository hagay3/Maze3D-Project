package presenter;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;


@SuppressWarnings("unused")
public class Presenter implements Observer {
	private View v;
	private Model m;
	HashMap<String, Command> viewCommands;
	HashMap<String, Command> modelCommands;
	CommandsManager commandsManager;
	
	public Presenter(View view, Model model) {
		this.v = view;
		this.m = model;
		commandsManager = new CommandsManager(model, view);
		viewCommands = commandsManager.getViewCommands();
		modelCommands = commandsManager.getModelCommands();
	}
	
	
	/**
	 * Setter for model commands
	 * @param commands , HashMap for commands
	 */
	public void setModelCommands(HashMap<String, Command> commands) {
		this.modelCommands = commands;
		
	}
	
	/**
	 * Setter for view commands
	 * @param commands , HashMap for commands
	 */
	public void setViewCommands(HashMap<String, Command> commands) {
		this.viewCommands = commands;
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o == v) { // notification came from view
			
			String commandLine = (String)arg;
			String arr[] = commandLine.split(" ");
			String command = arr[0];
			
			if(command.equals("menu")){
				v.printMenu(commandsManager.buildMenuForCommands());
			}
			else if (!viewCommands.containsKey(command)){
				v.showError("Command doesn't exist");
			}
			else {
				String[] args = null;
				if (arr.length > 1) {
					String commandArgs = commandLine
							.substring(commandLine.indexOf(" ") + 1);
					args = commandArgs.split(" ");
				}
				// Execute desired command
				Command cmd = viewCommands.get(command);
				cmd.doCommand(args);
			}	
		}
		else{ // notification came from model
			
			if(!arg.getClass().equals(String.class)){
				commandsManager.doOtherCommand(arg);
				return;
			}
			
			String commandLine = (String)arg;
			String arr[] = commandLine.split(" ");
			String command = arr[0];
			arr[0] = "";
			
			StringBuilder strBuilder = new StringBuilder();
			for (int i = 1; i < arr.length; i++) {
			   strBuilder.append(arr[i]+ " ");
			}
			String[] args = {strBuilder.toString()}; 
			
			if (!modelCommands.containsKey(command)) {
				v.showError("Command doesn't exist");
			}
			
			Command cmd = modelCommands.get(command);
			cmd.doCommand(args);
		}
	}
}
