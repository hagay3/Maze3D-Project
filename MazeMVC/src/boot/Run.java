package boot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import model.Model;
import model.MyModel;
import view.MyView;
import view.View;
import controller.Controller;
import controller.MyController;

public class Run {

	public static void main(String[] args) {
       		
       		Model m = new MyModel();
       		View  v = new MyView(new BufferedReader(new InputStreamReader(System.in)),new PrintWriter(System.out));
       		Controller c = new MyController(v,m);
       		v.setController(c);
       		m.setController(c);
       		v.start();
	}

}
