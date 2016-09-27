package GUI;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import presenter.Properties;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;

public class MazeWindow extends BasicWindow {

	private MazeDisplay mazeDisplay;
	private Properties properties;
	public Text comments = null;
	Label metaDataLabel;
	Label possibleKeysLabel;
	final private Image imgCLI = new Image(null, "resources/images/CLI.png");
	final private Image imgBackground = new Image(null, "resources/images/light-blue-background.jpg");
	final private Image imgBackgroundSmall = new Image(null, "resources/images/backgroundSmall.png");
	
	public MazeWindow(Properties p) {
		this.properties = p;
	}
	
	@Override
	@SuppressWarnings("unused")
	protected void initWidgets() {
		/* General Grid Stuff */
		GridLayout grid = new GridLayout(2,false); 
		GridData stretchGridData = new GridData();
		stretchGridData.verticalAlignment = GridData.FILL; 
		stretchGridData.grabExcessVerticalSpace = true;
		GridData gd = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd.heightHint = 100;
		gd.widthHint = 110;
		
		GridData fitGridData = new GridData(); 
		fitGridData.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING; 
		fitGridData.grabExcessVerticalSpace = false; 
		
		/* General Shell Stuff */ 
		shell.setLayout(grid);


        /* Main Bar Menu Items: File, Maze */
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText("&File");
        MenuItem cascadeMazeMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeMazeMenu.setText("Maze");
		
	        /* File Menu Items: Open Properties, Exit */  
	        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	        cascadeFileMenu.setMenu(fileMenu);
	        MenuItem openProperties = new MenuItem(fileMenu, SWT.PUSH);
	        openProperties.setText("Open Properties");
	        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
	        exitItem.setText("&Exit");
	        
	        
	        
	        /* Maze Menu Items: Load, Save Maze */  
	        Menu mazeMenu = new Menu(shell, SWT.DROP_DOWN);
	        cascadeMazeMenu.setMenu(mazeMenu);
	        MenuItem loadMaze = new MenuItem(mazeMenu, SWT.PUSH);
	        loadMaze.setText("Load maze from file");
	        MenuItem saveMaze = new MenuItem(mazeMenu, SWT.PUSH);
	        saveMaze.setText("Save Maze to file");
	        shell.setMenuBar(menuBar); // Generate Menu.
	        MenuItem GenerateItem = new MenuItem(mazeMenu, SWT.PUSH);
	        GenerateItem.setText("Generate Maze");


        /* Tab Folder */ 
        TabFolder MazeFolder = new TabFolder(shell, SWT.NULL);
        MazeFolder.setLayoutData(stretchGridData);

        /* "Play" Tab */ 
    	TabItem playTab = new TabItem(MazeFolder, SWT.NULL);
        playTab.setText("Play");
        Composite playForm = new Composite(MazeFolder, SWT.NONE);
        RowLayout rowLayout = new RowLayout();
		rowLayout.justify = false;
		rowLayout.pack = true;
		rowLayout.type = SWT.VERTICAL;
		playForm.setLayout(rowLayout);
		
        playTab.setControl(playForm);
    
    	Button generateNewMazeButton = createButton(playForm, "  Generate    ", "resources/TabFolder/Generate.png");
    	Button quickStartButton = createButton(playForm, "  Quick Start", "resources/TabFolder/quickStart.png");
    	Button hintButton = createButton(playForm, "  Get Help     ", "resources/TabFolder/Hint.png");
    	Button solveButton = createButton(playForm, "  Solve Maze   ", "resources/TabFolder/Solve.png");
        	
	    /* Labels */
		metaDataLabel = createLabel(playForm, SWT.FILL, "", 120, 45);
		setMetaDataLabel(0, 0, 0, 0, 0, 0);
		possibleKeysLabel = createLabel(playForm, SWT.FILL, "", 120, 60); 
	
	    /* "Options" Tab */
	    TabItem optionsTab = new TabItem(MazeFolder, SWT.NULL);
	    optionsTab.setText("Options");
	    Composite optionsForm = new Composite(MazeFolder, SWT.NONE);
	    optionsForm.setLayout(rowLayout);
	    optionsTab.setControl(optionsForm);
		    
	
		/* Maze Generation Algorithm - Default: Complicated (MyMazeGenerator) */ 
		createLabel(optionsForm,SWT.NULL, "Generation Algorithm:", 120,15); 
	
		String[] options = {"simple", "grwoing_random","grwoing_newest"};
		Combo generationAlgorithmCombo = createCombo(optionsForm, SWT.NULL, options, "grwoing_random");
		
		
	    /* Maze Solving Algorithm - Default: A*  */ 
		createLabel(optionsForm, SWT.FILL, "Solving Algorithm:", 120, 15);
		String[] solvingOptions = {"DFS", "BFS"};
		Combo solvingAlgorithmCombo = createCombo(optionsForm, SWT.NULL, solvingOptions, "A*");
	
		createLabel(optionsForm,SWT.FILL, "", 120, 10); 
	
	    Button submitButton = createButton(optionsForm, " Save   ", "resources/TabFolder/Save.png");
	
		/* What happens when a user clicks "File" > "Open Properties" */  
		openProperties.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell, SWT.OPEN);
				fileDialog.setText("Open Properties");
				//fileDialog.setFilterPath("C:/");
				String[] fileTypes = {"*.xml"}; 
				fileDialog.setFilterExtensions(fileTypes);
				String selectedFile = fileDialog.open();
				String[] args = {selectedFile};
				setChanged();
				notifyObservers("open_properties_file "+args);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		/* What happens when a user clicks "File" > "Generate Maze" */ 
		GenerateItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showGenerateMazeOptions();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});

		/* What happens when a user clicks "File" > "Exit" */ 
		exitItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION| SWT.YES | SWT.NO);
				messageBox.setMessage("Are you sure you want to exit?");
				messageBox.setText("Exit Confirmation");
				int response = messageBox.open();
				if (response == SWT.YES)
					display.dispose();// dispose OS components
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		/* What happens when a user clicks "Maze" > "Save maze to file" */  
		saveMaze.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell, SWT.SAVE);
				if(mazeDisplay.getMaze() != null && mazeDisplay != null)
				{
					fileDialog.setText("Save Maze to file");
					//fileDialog.setFilterPath("C:/");
					String[] fileTypes = {"*.Game"}; 
					fileDialog.setFilterExtensions(fileTypes);
					fileDialog.setFileName("Game.Game");
					@SuppressWarnings("unused")
					String selectedFile = fileDialog.open();
					String selectedName = fileDialog.getFileName();
					setChanged();
					notifyObservers("save maze "+mazeDisplay.getMaze().getName()+" "+ fileDialog.getFilterPath()+"\\"+selectedName);
				}
				else
					showMessageBox("Generate a maze first!");
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		

		/* What happens when a user clicks "Maze" > "Load maze from file" */  
		loadMaze.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				org.eclipse.swt.widgets.FileDialog fileLoadDialog = new org.eclipse.swt.widgets.FileDialog(shell, SWT.OPEN);
				{
					fileLoadDialog.setText("Load maze from file");
					//fileLoadDialog.setFilterPath("C:\\");
					String[] fileTypes = {"*.Game"}; 
					fileLoadDialog.setFilterExtensions(fileTypes);
					fileLoadDialog.setFileName("Game.Game");
					@SuppressWarnings("unused")
					String selectedFileToLoad = fileLoadDialog.open();
					String selectedName = fileLoadDialog.getFileName();
					//String[] args = {fileLoadDialog.getFilterPath()+"\\"+selectedName,mazeObjectName};
					//viewCommandMap.get("load maze").doCommand(args);
					//if(mazeObject!=null && mazeDisplayerCanvas!=null)
					//{
					//	started=true;
					//}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		/* What happens when a user clicks "[Generate]". */ 
		generateNewMazeButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showGenerateMazeOptions();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		/* What happens when a user clicks "[Quick Start]". */ 
		quickStartButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Random rand = new Random();
				int  n = rand.nextInt(100) + 1;
				String quickname = "QuickStartMaze"+Integer.toString(n);
				mazeDisplay.setMazeName(quickname); 
				setChanged();
				notifyObservers("generate_3d_maze "+quickname+" 3 6 6 "+ properties.getAlgorithmToGenerateMaze());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		
		/* What happens when a user clicks "[Solve]". */
		solveButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (mazeDisplay != null) {
					// {mazeObjectName,currentFloor+"",mazeDisplayerCanvas.getCharacterX()+"",mazeDisplayerCanvas.getCharacterY()+""};
					// notifyObservers(params);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		/* What happens when a user clicks "[Hint]". */
		hintButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(mazeDisplay != null)
				{
					//Updating the model about current place in maze
					//String[] params = {mazeObjectName,currentFloor+"",mazeDisplayerCanvas.getCharacterX()+"",mazeDisplayerCanvas.getCharacterY()+""};
					//notifyObservers(params);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
        /* What happens when a user clicks [Save] */
        submitButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String generator="";
				if(generationAlgorithmCombo.getText().equals("Complicated"))
					generator="MyMaze3dGenerator";
				else
					generator="SimpleMazeGenerator";
				String solver="";
				if(solvingAlgorithmCombo.getText().equals("DFS"))
					solver="DFS";
				else
					solver="BFS";
				
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});	

        
        /* Canvas Section */ 
	    mazeDisplay = new MazeDisplay(shell, SWT.BORDER,this,null);
		mazeDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true,1,1));
		mazeDisplay.setFocus();
       
        
        
	}

	

	protected void showGenerateMazeOptions() {
		Shell generateWindowShell = new Shell(display, SWT.TITLE | SWT.CLOSE);
		generateWindowShell.setText("Generate maze window");
		generateWindowShell.setLayout(new GridLayout(2, false));
		generateWindowShell.setSize(215, 215);
		generateWindowShell.setBackgroundImage(imgBackgroundSmall);
		generateWindowShell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		// Open in center of screen
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = generateWindowShell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		generateWindowShell.setLocation(x, y);
		
		Label lblHead = new Label(generateWindowShell, SWT.BOLD);
		FontData fontData = lblHead.getFont().getFontData()[0];
		Font font = new Font(display, new FontData(fontData.getName(), fontData.getHeight()+1, SWT.BOLD));
		lblHead.setFont(font);
		lblHead.setText("Enter maze dimensions");
		lblHead.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		
		Label lblFloors = new Label(generateWindowShell, SWT.NONE);
		lblFloors.setText("Floors: ");
		Text txtFloors = new Text(generateWindowShell, SWT.BORDER);
		txtFloors.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtFloors.setText("3");
		
		Label lblRows = new Label(generateWindowShell, SWT.NONE);
		lblRows.setText("Rows: ");
		Text txtRows = new Text(generateWindowShell, SWT.BORDER);
		txtRows.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtRows.setText("6");
		
		Label lblCols = new Label(generateWindowShell, SWT.NONE);
		lblCols.setText("Columns: ");
		Text txtCols = new Text(generateWindowShell, SWT.BORDER);
		txtCols.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtCols.setText("6");
		
		Label lblName = new Label(generateWindowShell, SWT.NONE);
		lblName.setText("Name: ");
		Text txtName = new Text(generateWindowShell, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		Random rand = new Random();
		int  n = rand.nextInt(100) + 1;
		String randomMazeName = "Maze"+Integer.toString(n);
		txtName.setText(randomMazeName);
		
		Button btnStartGame = new Button(generateWindowShell, SWT.PUSH);
		btnStartGame.setText("Play!");
		btnStartGame.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		btnStartGame.addSelectionListener(new SelectionListener() {

			/**
			 * take all the floors, cols, rows and make them from Text Box 
			 * 
			 * @param SelectionEvent
			 */
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setChanged();
				notifyObservers("generate_3d_maze" + " " + txtName.getText()
						+ " " + txtFloors.getText() + " " + txtRows.getText()
						+ " " + txtCols.getText() + " "
						+ properties.getAlgorithmToGenerateMaze());
				//Close the dialog window
				generateWindowShell.dispose();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) { }
			
		});

		generateWindowShell.open();

	}

	@Override
	public void notifyMazeIsReady(String name) {

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				setChanged();
				notifyObservers("display " + name);
			}
		});
	
	
	}

	public void showMessageBox(String str) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageBox msg = new MessageBox(shell, SWT.ICON_INFORMATION);
				msg.setMessage(str);
				msg.open();
			}
		});
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
		try {

			byte[] byteArr = mazeByteArrString.getBytes(StandardCharsets.UTF_8);
			// Convert maze from byeArray
			Maze3d maze3d = new Maze3d(byteArr);
			mazeDisplay.setMaze(maze3d);
			Position startPos = maze3d.getFirstCellAfterShellPostition(maze3d.getStartPosition());
			mazeDisplay.setCharacterPosition(startPos);
			int[][] crossSection = maze3d.getCrossSectionByX(startPos.getX());
			mazeDisplay.setCrossSection(crossSection, null, null);
			mazeDisplay.setGoalPosition(maze3d.getGoalPosition());
			mazeDisplay.setMazeName(maze3d.getName());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * This method will create label with\by the following values
	 * @param parent represent the composite to be added to
	 * @param style represent the style
	 * @param placeholder represent the text value of the label
	 * @param width represent label width
	 * @param height represent label height
	 * @return label
	 */
	private Label createLabel(Composite parent, int style, String placeholder, int width, int height){
		Label label = new Label(parent, style);
		label.setLayoutData(new RowData(width, height));
		label.setText(placeholder);
		return label; 		
	}
	/**
	 * This method create a button with the following parameters
	 * @param parent represent the Composite to being added to
	 * @param text represent the text value of thr button
	 * @param image represent the image that will be added to button
	 * @return button
	 */
	private Button createButton(Composite parent, String text, String image) {
	    Button button = new Button(parent, SWT.PUSH);
	    button.setImage(new Image(Display.getCurrent(), image));
	    button.setLayoutData(new RowData(120, 30));
	    button.setText(text);	    
	    return button;
	}
	
	public void setMetaDataLabel(int x,int y,int z,int goalX,int goalY,int goalZ)
	{
		String metaData =   "You:\t"+x+": {"+y+","+z+"}\nGoal:\t"+goalX+": {"+goalY+","+goalZ+"}";
		
		if (mazeDisplay != null && mazeDisplay.getMaze() != null){
			if (x == goalX && y == goalY && z == goalZ)
			{
				metaData += "\nYou Won!";
			}
			metaDataLabel.setText(metaData);
			//setPossibleKeysLabel(x, y, z);
		}
	}
	
	/**
	 * This method will create text box with\by the following values
	 * @param parent represent the composite to be added to
	 * @param style represent the style
	 * @param placeholder represent the text value of the label
	 * @param width represent Text width
	 * @param height represent Text height
	 * @return the text box
	 */
	private Text createText(Composite parent, int style, String placeholder, int width, int height){
		Text text = new Text(parent, style);
	    text.setText(placeholder);
	    text.setLayoutData(new RowData(width, height));
	    return text; 
	}
	
	/**
	 * This method will create combo box with\by the following values
	 * @param parent represent the parent to be added to
	 * @param style represent the style
	 * @param options represent the String[] of strings to put at the combo
	 * @param placeholder represent the first value of the combo
	 * @param width represent the combo width
	 * @param height represent the combo height
	 * @return combo 
	 */
	private Combo createCombo(Composite parent, int style, String[] options, String placeholder, int width, int height){
		Combo combo = new Combo(parent, style);
		for (int i = 0; i < options.length; i++) {
			combo.add(options[i]);
		}
		combo.setText(placeholder);
		combo.setLayoutData(new RowData(width, height));
		return combo;
	}
	/**
	 * This method will create combo box with\by the following values
	 * @param parent represent the parent to be added to
	 * @param style represent the style
	 * @param options represent the String[] of strings to put at the combo
	 * @param placeholder represent the first value of the combo
	 * @return combo 
	 */
	private Combo createCombo(Composite parent, int style, String[] options, String placeholder){
		return createCombo(parent, style, options, placeholder, 90, 20);
	}
}
