package view;
import java.util.HashMap;
/**
 * GUICommands 
 */
public class GUICommands {
	
	private HashMap<String, String> guiCommands;
	
	/**
	 * GuiCommands constructor 
	 */
	public GUICommands(){
		guiCommands = new HashMap<String, String>();
		initGuiCommands();
	}

	/**
	 * Initialize guiCommands HashMap
	 */
	public void initGuiCommands(){
		guiCommands.put("Generate maze", "generate_3d_maze");
		guiCommands.put("Solve maze", "solve");
		guiCommands.put("Display maze", "display");
	}

	public HashMap<String, String> getGuiCommands() {
		return guiCommands;
	}

	public void setGuiCommands(HashMap<String, String> guiCommands) {
		this.guiCommands = guiCommands;
	};

}
