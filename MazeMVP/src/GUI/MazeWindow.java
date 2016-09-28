package GUI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import java.util.Timer;
import java.util.TimerTask;

import boot.Run;
import presenter.Properties;
import view.XMLManager;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

public class MazeWindow extends BasicWindow {

	String mazeName = "";
	final private Image imgBackgroundSmall = new Image(null, "resources/images/backgroundSmall.png");
	private MazeDisplay mazeDisplay;
	private Properties properties;
	public Text comments = null;
	Label metaDataLabel;
	Label possibleKeysLabel;
	TimerTask animationSolutionTask;
	Timer showSolutionByAnimation;
	boolean hint = false;
	XMLManager xml;
	int numOfhints = 0;
	
	public MazeWindow(Properties properties) {
		this.properties = properties;
		xml = new XMLManager();
	}
	
	@Override
	protected void initWidgets() {
		/* General Grid Stuff */
		GridLayout grid = new GridLayout(2,false); 
		GridData stretchGridData = new GridData();
		stretchGridData.verticalAlignment = GridData.FILL; 
		stretchGridData.grabExcessVerticalSpace = true;
		GridData gd = new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1);
		gd.heightHint = 100;
		gd.widthHint = 110;
		shell.setText("Maze3d - Hagai Ovadia");
		
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
    	Button hintButton = createButton(playForm, "  Hint     ", "resources/TabFolder/Hint.png");
    	Button solveButton = createButton(playForm, "  Solve Maze   ", "resources/TabFolder/Solve.png");
    	Button cliButton = createButton(playForm, "  CLI   ", "resources/TabFolder/Solve.png");
        	
	    /* Labels */
		metaDataLabel = createLabel(playForm, SWT.FILL, "", 120, 45);
		possibleKeysLabel = createLabel(playForm, SWT.FILL, "", 120, 60); 
	
	    /* "Options" Tab */
	    TabItem optionsTab = new TabItem(MazeFolder, SWT.NULL);
	    optionsTab.setText("Options");
	    Composite optionsForm = new Composite(MazeFolder, SWT.NULL);
	    optionsForm.setLayout(rowLayout);
	    optionsTab.setControl(optionsForm);
		    
		/* Maze Generation Algorithm - Default: growing_random */ 
		createLabel(optionsForm,SWT.NULL, "Generation Algorithm:", 130,15); 
		String[] options = {"simple", "growing random","growing newest"};
		Combo generationAlgorithmCombo = createCombo(optionsForm, SWT.READ_ONLY, options, "growing random");
		
		/* If new algo generation is chosen update the properties */
		generationAlgorithmCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				properties.setAlgorithmToGenerateMaze(generationAlgorithmCombo.getText());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	
		
	    /* Maze Solving Algorithm - Default: bfs  */ 
		createLabel(optionsForm, SWT.FILL, "Solving Algorithm:", 120, 15);
		String[] solvingOptions = {"dfs", "bfs"};
		Combo solvingAlgorithmCombo = createCombo(optionsForm, SWT.READ_ONLY, solvingOptions, "bfs");
		
		/* If new search generation is chosen update the properties */
		solvingAlgorithmCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				properties.setAlgorithmToSearch(solvingAlgorithmCombo.getText());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		/* Maze UI - Default: gui  */ 
		createLabel(optionsForm, SWT.FILL, "UI: ", 120, 15);
		String[] uiOptions = {"gui", "cli"};
		Combo uiCombo = createCombo(optionsForm, SWT.READ_ONLY, uiOptions, "gui");
		
		/* If new UI is chosen update the properties */
		uiCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				properties.setTypeOfUserInterfece(uiCombo.getText());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		/* Maze Threads Number - Default: 10  */ 
		createLabel(optionsForm, SWT.FILL, "Threads Number: ", 120, 15);
		String[] threadsOptions = {"5", "10","20","30"};
		Combo threadsCombo = createCombo(optionsForm, SWT.READ_ONLY, threadsOptions, "10");
		
		/* If new num of threads  is chosen update the properties */
		threadsCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				properties.setNumberOfThreads(Integer.parseInt(threadsCombo.getText()));
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		//Empty line
		createLabel(optionsForm,SWT.FILL, "", 120, 10);
		
		//Save button
	    Button submitButton = createButton(optionsForm, " Save   ", "resources/TabFolder/Save.png");
	
	    
		/* What happens when a user clicks "CLI" */  
	    cliButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.setMinimized(true);
				String[] args = {"cli"};
				try {
					Run.main(args);
				} catch (ClassNotFoundException e) {
					showMessageBox(e.getMessage());
				} catch (IOException e) {
					showMessageBox(e.getMessage());
				}
				
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
	    
		/* What happens when a user clicks "File" > "Open Properties" */  
		openProperties.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
				{
					fileDialog.setText("Open Properties");
					String[] fileTypes = { "*.xml" };
					fileDialog.setFilterExtensions(fileTypes);
					String selectedFile = fileDialog.open();

					try {
						xml.readXML(selectedFile);
						properties = xml.getProperties();
						solvingAlgorithmCombo.setText(properties.getAlgorithmToSearch());
						generationAlgorithmCombo.setText(properties.getAlgorithmToGenerateMaze());
						uiCombo.setText(properties.getTypeOfUserInterfece());
						threadsCombo.setText(Integer.toString(properties.getNumberOfThreads()));
					} catch (FileNotFoundException | ClassCastException e) {
						showMaze("Error " + e.getMessage());
					}
				}
				
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
					exit();// dispose OS components
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		/* What happens when a user clicks "Maze" > "Save maze to file" */  
		saveMaze.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
				if(mazeDisplay.getMaze() != null && mazeDisplay != null)
				{
					fileDialog.setText("Save Maze to file");
					String[] fileTypes = {"*.maze3d"}; 
					fileDialog.setFilterExtensions(fileTypes);
					fileDialog.setFileName(getMazeName().concat(".maze3d"));
					String selectedFile = fileDialog.open();
					setChanged();
					notifyObservers("save_maze "+getMazeName()+" "+ selectedFile);
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
				FileDialog fileLoadDialog = new FileDialog(shell, SWT.OPEN);
				{
					fileLoadDialog.setText("Load maze from file");
					//fileLoadDialog.setFilterPath("C:\\");
					String[] fileTypes = {"*.maze3d"}; 
					fileLoadDialog.setFilterExtensions(fileTypes);
					String selectedFullPathAndName = fileLoadDialog.open();
					String fileName = fileLoadDialog.getFileName().replace(".maze3d", "");
					setMazeName(fileName);
					setChanged();
					showMessageBox("load_maze "+selectedFullPathAndName+" "+fileName);
					notifyObservers("load_maze "+selectedFullPathAndName+" "+fileName);
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
				if (mazeDisplay != null && !getMazeName().equals("")) {
					setChanged();
					notifyObservers("solve "+ getMazeName() +" "+ properties.getAlgorithmToSearch());
				}else
					showMessageBox("Generate maze first!");
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		/* What happens when a user clicks "[Hint]". */
		hintButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (mazeDisplay != null && !getMazeName().equals("")) {
					hint = true;
					setChanged();
					notifyObservers("solve "+ getMazeName() +" "+ properties.getAlgorithmToSearch());
				} else
					showMessageBox("Generate maze first!");
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
        /* What happens when a user clicks [Save] */
        submitButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				properties.setAlgorithmToGenerateMaze(generationAlgorithmCombo.getText());
				properties.setAlgorithmToSearch(solvingAlgorithmCombo.getText());
				properties.setTypeOfUserInterfece(uiCombo.getText());
				properties.setNumberOfThreads(Integer.parseInt(threadsCombo.getText()));
				FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
				
				{
					fileDialog.setText("Save Properties to file");
					// fileDialog.setFilterPath("C:/");
					String[] fileTypes = { "*.xml" };
					fileDialog.setFilterExtensions(fileTypes);
					fileDialog.setFileName("properties.xml");
					String selectedPath = fileDialog.open();
					if (selectedPath != null) {
						try {
							xml.setFilename(selectedPath);
							xml.writeXML(properties);
						} catch (FileNotFoundException | ClassCastException e) {
							showMessageBox("Error " + e.getMessage());
						}
					}
				}
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
				numOfhints = 0;
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
		String nameFix = name.replace(" ", "");
		setMazeName(nameFix);
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				setChanged();
				notifyObservers("display " + nameFix);
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
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				setChanged();
				notifyObservers("display " + mazeName);
			}
		});
		
	}

	@Override
	public void solutionIsReady(String name) {
		setChanged();
		notifyObservers("get_solution " + name);
	}

	@Override
	public void showDisplaySolution(String sol) {
		
		
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
			mazeDisplay.setStartPosition(maze3d.getStartPosition());
			
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
	
	/**
	 * This method will create combo box with\by the following values
	 * @param parent represent the parent to be added to
	 * @param style represent the style
	 * @param options represent the String[] of strings to put at the combo
	 * @param placeholder represent the first value of the combo
	 * @return combo 
	 */
	private Combo createCombo(Composite parent, int style, String[] options, String placeholder){
		Combo combo = new Combo(parent, style);
		for (int i = 0; i < options.length; i++) {
			combo.add(options[i]);
		}
		combo.setText(placeholder);
		return combo;
	}
	public String getMazeName() {
		return mazeName;
	}

	public void setMazeName(String mazeName) {
		this.mazeName = mazeName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void processSolution(Object solution) {
		Solution<Position> mazeSolution = (Solution<Position>) solution;

		// the user asked for only one step from the
		if (hint) {
			if (mazeSolution.getStates().size() > numOfhints) {
				hint = false;
				Position pos = mazeSolution.getStates().get(numOfhints).getValue();
				numOfhints++;
				mazeDisplay.drawHint(pos);
			}
		} else {
			
			this.animationSolutionTask = new TimerTask() {
				int i = 0;
				@Override
				public void run() {
					if (i < mazeSolution.getStates().size()){
						Position pos = mazeSolution.getStates().get(i++).getValue();
						mazeDisplay.moveChracter(pos);
					} else {
						display.syncExec(new Runnable() {
							@Override
							public void run() {
								//winner();
							}
						});
						cancel();
					}
				}
			};
			showSolutionByAnimation = new Timer();
			showSolutionByAnimation.scheduleAtFixedRate(this.animationSolutionTask, 0, 500);

		}
	}

}
