package boot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import GUI.MazeWindow;
import presenter.*;
import view.CommonView;
import view.MyView;
import view.XMLManager;
import model.MyModel;


public class Run {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		
		XMLManager xml = new XMLManager();
		xml.readXML("resources/properties.xml");
		Properties properties = xml.getProperties();
		
		if(args.length == 1){
			properties.setTypeOfUserInterfece(args[0]);
		}
			
		
		
		MyModel model = new MyModel(properties);
		CommonView view = null;
		
		if(properties.getTypeOfUserInterfece().equals("gui")){
			view = new MazeWindow(properties);
		}else{
			view = new MyView(new BufferedReader(new InputStreamReader(System.in)),new PrintWriter(System.out));
		}
		
		Presenter presenter = new Presenter(view, model);
		view.addObserver(presenter);
		model.addObserver(presenter);
		view.start();
		
	}
}
