package GUI;
import java.util.List;
import algorithms.mazeGenerators.Position;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import algorithms.mazeGenerators.Maze3d;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;


/**
 * MazeDisplay extends Canvas
 * This class will paint the maze
 */


public class MazeDisplay extends Canvas {
	

	private int[][] crossSection = { {0}, {0} };
	private Character character;
	final private Image imgHint = new Image(null,"resources/images/hint.png") ;
	final private Image imgWinner = new Image(null,"resources/images/winner.jpg");
	final private Image imgWall = new Image(null, "resources/images/wall.gif");
	final private Image imgFinish = new Image (null, "resources/images/finish.png");
	private boolean drawMeAHint;
	private Position hintPosition;
	private boolean winner;
	private Position goalPosition;
	private Position startPosition;
	private Maze3d maze;
	MazeWindow mazeWindow;
	
	
	/**
	 * Constructor 
	 * @param Composite parent, int style, MyView view,Maze3d maze
	 * draw the maze
	 */
	public MazeDisplay(Composite parent, int style,MazeWindow mazeWindow,Maze3d maze) {
		super(parent, style);
		this.maze = maze;
		this.mazeWindow = mazeWindow;
		character = new Character();
		character.setPos(new Position(-1, -1, -1));
		drawMeAHint = false;
		winner = false;

		// draw the maze
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				int z, y;
				int canvasWidth = getSize().x;
				int canvasHeight = getSize().y;
				int cellHeight = canvasHeight / crossSection[0].length;
				int cellWidth = canvasWidth / crossSection.length;
				

				e.gc.setForeground(new Color(null, 1, 255, 0));
				e.gc.setBackground(new Color(null, 0, 0, 0));
				
				for (int i = 0; i < crossSection.length; i++) {
					for (int j = 0; j < crossSection[i].length; j++) {
						y = i * cellHeight;
						z = j * cellWidth;
							
							//Draw finish flag - goal sign
							if(!mazeWindow.getMazeName().equals("") && crossSection[i][j] == 0){ 
								//Draw flags for start / goal
								if( i == 0 || i == crossSection.length-1 || j == 0 || j == crossSection[i].length-1){
									if( i!= startPosition.getY() && j!= startPosition.getZ())
										e.gc.drawImage(imgFinish, 0, 0,imgFinish.getBounds().width,imgFinish.getBounds().height, z, y,cellWidth, cellHeight);
								}
							}
							//Draw walls
							if(crossSection[i][j] == 1)  
								e.gc.drawImage(imgWall, 0, 0,imgWall.getBounds().width,imgWall.getBounds().height, z, y,cellWidth, cellHeight);
						}
					}

				if (drawMeAHint) {
					drawMeAHint = false;
					e.gc.drawImage(imgHint, 0, 0, imgHint.getBounds().width, imgHint.getBounds().height, (cellWidth * hintPosition.getZ()) + (cellWidth / 4), (cellHeight * hintPosition.getY()) + (cellHeight / 4), cellWidth/2, cellHeight/2);
				}
				
				if (!winner) {
					character.draw(cellWidth,cellHeight, e.gc);
				} else{
					winner = false;
					showMessageBox("Winner!!!");
					e.gc.drawImage(imgWinner, 0, 0, imgWinner.getBounds().width, imgWinner.getBounds().height, 0, 0, canvasWidth, canvasHeight);
				}
				
				forceFocus();
			}
		});
		
		
		
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				String direction = null;
				switch (e.keyCode) {
				case SWT.ARROW_RIGHT:
					direction = "right";
					break;
				case SWT.ARROW_LEFT:
					direction = "left";
					break;
				case SWT.ARROW_UP:
					direction = "up";
					break;
				case SWT.ARROW_DOWN:
					direction = "down";
					break;
				case SWT.PAGE_DOWN:
					direction = "backward";
					break;
				case SWT.PAGE_UP:
					direction = "forward";
					break;
				default:
					break;
				}
				if (direction != null) {
					moveChracter(direction);
					redrawMe();
				}
			}
		});
	}
		
	
	/**
	 * Check is the character can move by given direction
	 * @param pos, current Position
	 * @param direction, specific direction
	 * @return boolean
	 */
	public boolean possibleMoveFromPosition(Position pos,String direction){
		if (maze!= null && maze.possibleCharacterMove(pos, direction)) {
			return true;			
		}else
			return false;		
		
	}
	/**
	 * Move the character by given direction
	 * @param direction, specific direction
	 * @return if the move is possible
	 */
	public boolean moveChracter(String direction) {
		if (possibleMoveFromPosition(character.getPos(), direction)) {
			Position nextPos = maze.moveCharacter(character.getPos(), direction);
			int[][] crossSection = maze.getCrossSectionByX(nextPos.getX());
			setCrossSection(crossSection, null, null);
			setCharacterPosition(nextPos);
			checkIfImWinner();
			return true;			
		}else
			return false;
	}
	/**
	 * Check if the current character position is on the goal mens the player won
	 * If yes drawing a proper image
	 */
	public void checkIfImWinner(){
		String[] directions = {"right","left","up","down","backward","forward"};
		for (String direction : directions){
			Position next = maze.moveCharacter(character.getPos(), direction);
			if(next.equals(goalPosition))
					this.winner = true;
		}
	}
	
	/**
	 * Move character to a given position
	 * @param pos, the next position
	 */
	public void moveChracter(Position pos) {
		int[][] crossSection = maze.getCrossSectionByX(pos.getX());
		setCrossSection(crossSection, null, null);
		setCharacterPosition(pos);
	}

	/**
	 * setWinner
	 * @param winner, boolean
	 */

	public void setWinner(boolean winner) {
		this.winner = winner;
		redrawMe();
	}

	/**
	 * paint the maze in crossSection [][]
	 * @param crossSection, crossSection
	 * @param upHint, List<Point>
	 * @param downHint, List<Point>
	 */
	public void setCrossSection(int[][] crossSection, List<Point> upHint, List<Point> downHint) {
		this.crossSection = crossSection;
		redrawMe();
	}

	/**
	 * set the character position then draw the maze
	 * @param pos, the position 
	 */
	public void setCharacterPosition(Position pos) {
		this.character.setPos(pos);
		redrawMe();
	}

	/**
	 * move the character
	 * @param pos, the position
	 */
	public void moveTheCharacter(Position pos) {
		this.character.setPos(pos);
		redrawMe();
	}
	
	/**
	 * set goal position
	 * @param Position goalPosition
	 */
	public void setGoalPosition(Position goalPosition) {
		this.goalPosition = goalPosition;
	}
	
	/**
	 *This method draw a hint to the player
	 * @param PositionhintPos
	 */
	public void drawHint(Position hintPos) {
		this.drawMeAHint = true;
		this.hintPosition = hintPos;
		redrawMe();
	}
	
	/**
	 *This method readraw the canvas in runnable sync
	 */
	public void redrawMe() {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				setEnabled(true);
				redraw();
			}
		});
	}
	/**
	 * Shiw nice message box to the player
	 * @param str, the message content
	 */
	public void showMessageBox(String str) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageBox msg = new MessageBox(new Shell(),
						SWT.ICON_INFORMATION);
				msg.setMessage(str);
				msg.open();
			}
		});
	}

	/**
	 * Setter Maze3d maze
	 * @param maze
	 */
	public void setMaze(Maze3d maze) {
		this.maze = maze;
	}


	/**
	 * Getter Maze3d maze
	 * @return Maze3d
	 */
	public Maze3d getMaze() {
		return maze;
	}


	/**
	 * Getter for start position
	 * @return Position startPosition
	 */
	public Position getStartPosition() {
		return startPosition;
	}


	/**
	 * Setter start position
	 * @param startPosition
	 */
	public void setStartPosition(Position startPosition) {
		this.startPosition = startPosition;
	}
	
	

}
