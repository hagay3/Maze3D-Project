package GUI;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import algorithms.mazeGenerators.Position;
/**
 * class Character
 */
public class Character {
	private Position pos;
	private Image img;
	
	/**
	 * constractor
	 */
	public Character() {
		this.img = new Image(null, "resources/images/ghost-character.png");
	}

	/**
	 * getPos
	 */
	public Position getPos() {
		return pos;
	}

	/**
	 * setPos
	 * @param pos, Position
	 */
	public void setPos(Position pos) {
		this.pos = pos;
	}
	/**
	 *this method draw the image
	 *@param cellWidth, int
	 *@param cellHeight, int
	 *@param gc, GC
	 */
	public void draw(int cellWidth, int cellHeight, GC gc) {
		gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height, cellWidth * pos.getX(), cellHeight * pos.getY(), cellWidth, cellHeight);
	}
	
}
