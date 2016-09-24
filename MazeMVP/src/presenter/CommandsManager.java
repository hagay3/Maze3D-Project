package presenter;

import java.util.HashMap;

import model.Model;
import view.View;

public class CommandsManager {

	HashMap<String, Command> viewCommands;
	HashMap<String, Command> modelCommands;
	private View v;
	private Model m;

	public CommandsManager(Model model, View view) {
		this.v = view;
		this.m = model;
		viewCommands = new HashMap<String, Command>();
		modelCommands = new HashMap<String, Command>();
		initCommands();
	}

	public void initCommands() {
		viewCommands.put("dir", new Command() {
			@Override
			public void doCommand(String[] args) {
				m.HandleDirPath(args);
			}
		});

		viewCommands.put("generate_3d_maze", new Command() {

			@Override
			public void doCommand(String[] args) {
				m.handleGenerate3dMaze(args);

			}

		});
		viewCommands.put("display", new Command() {

			@Override
			public void doCommand(String[] args) {
				m.handleDisplayName(args);

			}

		});

		viewCommands.put("display_cross_section", new Command() {
			@Override
			public void doCommand(String[] args) {
				m.handleDisplayCrossSectionBy(args);
			}

		});
		viewCommands.put("save_maze", new Command() {

			@Override
			public void doCommand(String[] args) {
				m.handleSaveMaze(args);

			}
		});
		viewCommands.put("load_maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				m.handleLoadMaze(args);

			}

		});

		viewCommands.put("solve", new Command() {
			@Override
			public void doCommand(String[] args) {
				m.handleSolve(args);

			}

		});
		viewCommands.put("display_solution", new Command() {
			@Override
			public void doCommand(String[] args) {
				m.handleDisplaySolution(args);

			}
		});
		viewCommands.put("exit", new Command() {
			@Override
			public void doCommand(String[] args) {
				m.handleExit(args);

			}
		});
		modelCommands.put("error", new Command() {
			@Override
			public void doCommand(String[] args) {
				v.showError(args[0]);
			}
		});

		modelCommands.put("notifyMazeIsReady", new Command() {
			@Override
			public void doCommand(String[] args) {
				v.notifyMazeIsReady(args[0]);
			}
		});

		modelCommands.put("passDirPath", new Command() {
			@Override
			public void doCommand(String[] args) {
				v.showDirPath(args[0]);
			}
		});

		modelCommands.put("passDisplayMaze", new Command() {
			@Override
			public void doCommand(String[] args) {
				v.showMaze(args[0]);
			}
		});

		modelCommands.put("passDisplayCrossSectionBy", new Command() {
			@Override
			public void doCommand(String[] args) {
				v.showDisplayCrossSectionBy(args[0]);
			}
		});

		modelCommands.put("passSaveMaze", new Command() {
			@Override
			public void doCommand(String[] args) {
				v.showSaveMaze(args[0]);
			}
		});

		modelCommands.put("passLoadMaze", new Command() {
			@Override
			public void doCommand(String[] args) {
				v.showLoadMaze(args[0]);
			}
		});
		modelCommands.put("passSolve", new Command() {
			@Override
			public void doCommand(String[] args) {
				v.showSolve(args[0]);
			}
		});

		modelCommands.put("passDisplaySolution", new Command() {
			@Override
			public void doCommand(String[] args) {
				v.showDisplaySolution(args[0]);
			}
		});
	}

	public void setM(Model m) {
		this.m = m;
	}

	public void setV(View v) {
		this.v = v;
	}

	public HashMap<String, Command> getViewCommands() {
		return viewCommands;
	}

	public void setViewCommands(HashMap<String, Command> viewCommands) {
		this.viewCommands = viewCommands;
	}

	public HashMap<String, Command> getModelCommands() {
		return modelCommands;
	}

	public void setModelCommands(HashMap<String, Command> modelCommands) {
		this.modelCommands = modelCommands;
	}

	public String buildMenuForCommands(){
		StringBuilder sb = new StringBuilder();
		sb.append("Choose command: \n\n");

		for (String command : viewCommands.keySet()) {
			sb.append(command + "\n");
		}

		return sb.toString();
	}
}
