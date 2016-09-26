package GUI;
import java.util.ArrayList;
import java.util.List;
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
import algorithms.mazeGenerators.Position;


/**
 * MazeDisplay
 * extends Canvas
 * this class will paint the maze
 * Data Member String mazeName,int whichFloorAmI, int [][] crossSection, Character character
 * Data Member Image imgGoal, Image imgWinner,Image imgUp, Image imgDown,Image imgUpDown
 * Data Member Image imgWall, boolean drawMeAHint, Position hintPosition,  boolean winner,
 * Data Member Position goalPosition, List<Point> downHint,List<Point> upHint
 */
public class MazeDisplay extends Canvas {
	
	@SuppressWarnings("unused")
	private String mazeName;
	private int whichFloorAmI;
	private int[][] crossSection = { {0}, {0} };
	private Character character;
	private Image imgGoal;
	private Image imgWinner;
	private Image imgUp;
	private Image imgDown;
	private Image imgUpDown;
	private Image imgWall;
	private boolean drawMeAHint;
	private Position hintPosition;
	private boolean winner;
	private Position goalPosition;
	private List<Point> downHint;
	private List<Point> upHint;

	/**
	 * Constructor 
	 * @param Composite parent, int style, MyView view
	 * draw the maze
	 */
	public MazeDisplay(Composite parent, int style) {
		super(parent, style);
		
		this.mazeName = null;
		this.whichFloorAmI = 0;
		this.character = new Character();
		this.character.setPos(new Position(-1, -1, -1));
		this.imgGoal = new Image(null,"resources/images/apple.png");
		this.imgWinner = new Image(null,"resources/images/winner.gif");
		this.imgUp = new Image(null, "resources/images/up.gif");
		this.imgDown = new Image(null, "resources/images/down.gif");
		this.imgUpDown = new Image(null, "resources/images/updown.gif");
		this.imgWall = new Image(null, "resources/images/wall.gif");
		this.drawMeAHint = false;
		this.hintPosition = null;
		this.winner = false;
		this.goalPosition= new Position(-1, -1, -1);
		this.upHint = new ArrayList<Point>();
		this.downHint = new ArrayList<Point>();

		// draw the maze
		this.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				int x, y;
				int canvasWidth = getSize().x;
				int canvasHeight = getSize().y;
				int cellWidth = canvasWidth / crossSection[0].length;
				int cellHeight = canvasHeight / crossSection.length;

				e.gc.setForeground(new Color(null, 1, 255, 0));
				e.gc.setBackground(new Color(null, 0, 0, 0));

				
				for (int i = 0; i < crossSection.length; i++) {
					for (int j = 0; j < crossSection[i].length; j++) {
						x = j * cellWidth;
						y = i * cellHeight;
						if (crossSection[i][j] != 0)
							//e.gc.fillRectangle(x, y, cellWidth, cellHeight);
							e.gc.drawImage(imgWall, 0, 0, imgWall.getBounds().width, imgWall.getBounds().height, x, y, cellWidth, cellHeight);
					}
				}
				
				if (drawMeAHint) {
					drawMeAHint = false;
					e.gc.drawImage(imgGoal, 0, 0, imgGoal.getBounds().width, imgGoal.getBounds().height, (cellWidth * hintPosition.getX()) + (cellWidth / 4), (cellHeight * hintPosition.getY()) + (cellHeight / 4), cellWidth/2, cellHeight/2);
				}
				
				
				if (!winner) {
					character.draw(cellWidth, cellHeight, e.gc);
					if (whichFloorAmI == goalPosition.getZ())
						e.gc.drawImage(imgGoal, 0, 0, imgGoal.getBounds().width, imgGoal.getBounds().height, cellWidth * goalPosition.getX(), cellHeight * goalPosition.getY(), cellWidth, cellHeight);
				} else
					e.gc.drawImage(imgWinner, 0, 0, imgWinner.getBounds().width, imgWinner.getBounds().height, cellWidth * goalPosition.getX(), cellHeight * goalPosition.getY(), cellWidth, cellHeight);
				forceFocus();
			}
			
			@SuppressWarnings("unused")
			private void paintUpDownHints(PaintEvent e, int i, int j, int cellWidth, int cellHeight) {
				Point upDownHint = new Point(i, j);
				if (upHint.contains(upDownHint) && downHint.contains(upDownHint))
					e.gc.drawImage(imgUpDown, 0, 0, imgUpDown.getBounds().width, imgUpDown.getBounds().height, cellWidth * j, cellHeight * i, cellWidth, cellHeight);
				else {
					if (upHint.contains(upDownHint))
						e.gc.drawImage(imgUp, 0, 0, imgUp.getBounds().width, imgUp.getBounds().height, cellWidth * j, cellHeight * i, cellWidth, cellHeight);
					else if (downHint.contains(upDownHint))
							e.gc.drawImage(imgDown, 0, 0, imgDown.getBounds().width, imgDown.getBounds().height, cellWidth * j, cellHeight * i, cellWidth, cellHeight);
				}
			}
		});
	}
		
	
	/**
	 * setWinner
	 * @param winner, boolean
	 */

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	/**
	 *This method tell us where are we in the maze
	 * @param whichFloorAmI, int
	 */
	public void setWhichFloorAmI(int whichFloorAmI) {
		this.whichFloorAmI = whichFloorAmI;
	}

	/**
	 * paint the maze in crossSection [][]
	 * @param crossSection, crossSection
	 * @param upHint, List<Point>
	 * @param downHint, List<Point>
	 */
	public void setCrossSection(int[][] crossSection, List<Point> upHint, List<Point> downHint) {
		this.crossSection = crossSection;
		this.upHint = upHint;
		this.downHint = downHint;
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
		//redrawMe();
	}
	/**
	 * setMazeName 
	 * @param String mazeName
	 */
	public void setMazeName(String mazeName) {
		this.mazeName = mazeName;
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
		//redrawMe();
	}
	
	/**
	 *This method readraw the canvas in runnable sync
	 */
	public void redrawMe() {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				//setEnabled(true);
				redraw();
			}
		});
	}

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
}
