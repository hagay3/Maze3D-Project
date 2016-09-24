package view;

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
import algorithms.mazeGenerators.Maze3d;

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
				displayMaze();
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
		
		Label lblRows = new Label(shell, SWT.NONE);
		lblRows.setText("Rows: ");
		Text txtRows = new Text(shell, SWT.BORDER);
		
		Label lblCols = new Label(shell, SWT.NONE);
		lblCols.setText("Cols: ");
		Text txtCols = new Text(shell, SWT.BORDER);
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setText("Maze name: ");
		Text txtName = new Text(shell, SWT.BORDER);
		
		Button btnGenerate = new Button(shell, SWT.PUSH);
		btnGenerate.setText("Generate maze");
		
		

		
		btnGenerate.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setChanged();
				notifyObservers(guiCommands.get(btnGenerate.getText()) + " "
						+ txtName.getText() + " " + txtFloors.getText() + " "
						+ txtRows.getText() + " " + txtCols.getText()
						+ properties.getAlgorithmToGenerateMaze());
				shell.close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
		
		mazeDisplay = new MazeDisplay(shell, SWT.NONE);			
		shell.open();		
	}

	@Override
	public void notifyMazeIsReady(String name) {
		display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				showMessageBox("Maze " + name + " is ready");
				
				setChanged();
				notifyObservers("display_maze " + name);
			}
		});			
	}

	//@Override
	public void displayMaze() {
		
		int[][] mazeData={
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,0,0,1,1,0,1,0,0,1},
				{0,0,1,1,1,1,1,0,0,1,0,1,0,1,1},
				{1,1,1,0,0,0,1,0,1,1,0,1,0,0,1},
				{1,0,1,0,1,1,1,0,0,0,0,1,1,0,1},
				{1,1,0,0,0,1,0,0,1,1,1,1,0,0,1},
				{1,0,0,1,0,0,1,0,0,0,0,1,0,1,1},
				{1,0,1,1,0,1,1,0,1,1,0,0,0,1,1},
				{1,0,0,0,0,0,0,0,0,1,0,1,0,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,0,1,1},
			};			
		mazeDisplay.setMazeData(mazeData);
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
		// TODO Auto-generated method stub
		
	}

}
