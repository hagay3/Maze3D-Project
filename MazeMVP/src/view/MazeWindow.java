package view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import presenter.Properties;
import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.newestCell;

public class MazeWindow extends BasicWindow {

	private MazeDisplay mazeDisplay;
	private Properties properties;
	private HashMap<String, String> guiCommands;
	
	
	public MazeWindow(Properties p) {
		this.properties = p;
		this.guiCommands = new GUICommands().getGuiCommands();
	}
	
	@Override
	protected void initWidgets() {
		GridLayout gridLayout = new GridLayout(2, false);
		shell.setLayout(gridLayout);				
		
		Composite btnGroup = new Composite(shell, SWT.BORDER);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		btnGroup.setLayout(rowLayout);
		
		Button btnGenerateMaze = new Button(btnGroup, SWT.PUSH);
		btnGenerateMaze.setText("Generate maze");	
		
		btnGenerateMaze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showGenerateMazeOptions();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btnSolveMaze = new Button(btnGroup, SWT.PUSH);
		btnSolveMaze.setText("Solve maze");
		
		
		Button btnDisplayMaze = new Button(btnGroup, SWT.PUSH);
		btnDisplayMaze.setText("Display maze");	
		
		btnDisplayMaze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showMaze("Maze1");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	protected void showGenerateMazeOptions() {
		Shell shell = new Shell();
		shell.setText("Generate Maze");
		shell.setSize(300, 200);
		
		
		
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		
		Label lblFloors = new Label(shell, SWT.NONE);
		lblFloors.setText("Floors: ");
		Text txtFloors = new Text(shell, SWT.BORDER);
		txtFloors.setText("3");
		
		Label lblRows = new Label(shell, SWT.NONE);
		lblRows.setText("Rows: ");
		Text txtRows = new Text(shell, SWT.BORDER);
		txtRows.setText("3");
		
		Label lblCols = new Label(shell, SWT.NONE);
		lblCols.setText("Cols: ");
		Text txtCols = new Text(shell, SWT.BORDER);
		txtCols.setText("3");
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setText("Maze name: ");
		Text txtName = new Text(shell, SWT.BORDER);
		txtName.setText("Maze1");
		
		Button btnGenerate = new Button(shell, SWT.PUSH);
		btnGenerate.setText("Generate maze");
		
		btnGenerate.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {	
				setChanged();
				notifyObservers(guiCommands.get(btnGenerate.getText()) + " "
						+ txtName.getText() + " " + txtFloors.getText() + " "
						+ txtRows.getText() + " " + txtCols.getText() + " "
						+ properties.getAlgorithmToGenerateMaze());
				shell.close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
		
		mazeDisplay = new MazeDisplay(shell, SWT.BORDER);			
		shell.open();		
	}

	@Override
	public void notifyMazeIsReady(String name) {
		setChanged();
		notifyObservers("display " + name);
		
		/*display.syncExec(new Runnable() {
			@Override
			public void run() {
				setChanged();
				notifyObservers("display " + name);
			}
		});			*/
	}

	public void showMessageBox(String str){
		MessageBox msg = new MessageBox(shell);
		msg.setMessage(str);
		msg.open();	
	}
	
	//@Override
	public void displayMessage(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		run();		
	}

	@Override
	public void showDirPath(String dirArray) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showError(String message) {
		showMessageBox(message);
		
	}


	@Override
	public void showDisplayCrossSectionBy(String crossMazeBySection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showSaveMaze(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLoadMaze(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showSolve(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDisplaySolution(String sol) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printMenu(String menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showMaze(String mazeByteArrString) {
		//try {
			
			
			
			//byte[] byteArr = mazeByteArrString.getBytes(StandardCharsets.UTF_8);
			//Convert maze from byeArray
			//Maze3d maze3d = new Maze3d(byteArr);
			//Set growing maze generator
			GrowingTreeGenerator mg = new GrowingTreeGenerator(new newestCell());
			// generate another 3d maze
			Maze3d maze3d	=	mg.generate(3,3,3);
		
			mazeDisplay = new MazeDisplay(shell, SWT.NONE);			
			shell.open();
			
			
			this.mazeDisplay.setCharacterPosition(maze3d.getStartPosition());
			
			int [][] crossSection = maze3d.getCrossSectionByZ(0);
			
			this.mazeDisplay.setCrossSection(crossSection, null, null);
			this.mazeDisplay.setGoalPosition(new Position(3,4,2));
			this.mazeDisplay.setMazeName("Maze1");
		
		//} 
		/*catch (IOException e) 
		{
			e.printStackTrace();
		}*/

		
	}

}
