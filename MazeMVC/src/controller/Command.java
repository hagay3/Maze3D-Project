package controller;
/**
 * defines the command interface,where each command must implement doCommand
 *
 */
public interface Command 
{
	/**
	 * defines what each command do.
	 * @param args array of parameters
	 */
	public void doCommand(String[] args);
}
