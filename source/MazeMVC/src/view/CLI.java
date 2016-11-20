package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import controller.Command;

/**
 * defines the starting point of command line interface
 *
 */

public class CLI {
	BufferedReader in;
	PrintWriter out;
	HashMap<String, Command> stringToCommand;

	/**
	 * constructor using fields
	 * @param in the input source
	 * @param out the output source
	 */
	public CLI(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;

	}
	
	/**
	 * Setter for commands
	 * @param commands , HashMap for commands
	 */
	public void setCommands(HashMap<String, Command> commands) {
		this.stringToCommand = commands;
	}

	private void printMenu() {
		out.print("Choose command: \n\n");

		for (String command : stringToCommand.keySet()) {
			out.print(command + "\n");
		}

		out.flush();
	}

	/**
	 * starts thread for command line interface
	 */
	public void start() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					printMenu();
					try {
						String commandLine = in.readLine();
						String arr[] = commandLine.split(" ");
						String command = arr[0];

						
						if (!stringToCommand.containsKey(command)) {
							out.println("Command doesn't exist");
							out.flush();
						}

						else {
							String[] args = null;
							if (arr.length > 1) {
								String commandArgs = commandLine
										.substring(commandLine.indexOf(" ") + 1);
								args = commandArgs.split(" ");
							}
							
							// Execute desired command
							Command cmd = stringToCommand.get(command);
							cmd.doCommand(args);
							
							// Break if the user chose 'exit'
							if (command.equals("exit")) {
								out.println("Bye bye");
								out.flush();
								break;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		//Start the thread
		thread.start();
	}
	/**
	 * getter of input stream source
	 * @return input stream source
	 */
	public BufferedReader getIn() {
		return in;
	}
	/**
	 * sets the input stream source
	 * @param in input stream source
	 */
	public void setIn(BufferedReader in) {
		this.in = in;
	}
	
	/**
	 * getter of the output stream source
	 * @return output stream source
	 */
	public PrintWriter getOut() {
		return out;
	}
	/**
	 * sets the output stream source
	 * @param out output stream source
	 */
	public void setOut(PrintWriter out) {
		this.out = out;
	}
	/**
	 * gets the hash map with the commands
	 * @return hash map with key-name of command,value-the command
	 */
	public HashMap<String, Command> getStringToCommand() {
		return stringToCommand;
	}
	/**
	 * sets the hash map with the commands
	 * @param stringToCommand hash map with key-name of command,value-the command
	 */
	public void setStringToCommand(HashMap<String, Command> stringToCommand) {
		this.stringToCommand = stringToCommand;
	}
	
	
}
