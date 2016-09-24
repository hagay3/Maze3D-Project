package boot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import presenter.*;
import view.CommonView;
import view.MazeWindow;
import view.MyView;
import model.MyModel;


public class Run {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		XMLManager xml = new XMLManager();
		xml.readXML("resources/properties.xml");
		MyModel model = new MyModel(xml.getProperties());
		CommonView view = null;
		
		if(xml.getProperties().getTypeOfUserInterfece().equals("gui")){
			view = new MazeWindow(xml.getProperties());
		}else{
			view = new MyView(new BufferedReader(new InputStreamReader(System.in)),new PrintWriter(System.out));
		}
		
		Presenter presenter = new Presenter(view, model);
		view.addObserver(presenter);
		model.addObserver(presenter);
		view.start();
	}
}
