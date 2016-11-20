package algorithms.mazeGenerators;

import java.io.Serializable;

/**
 * This class Defines a Position in 3D maze in real world.
 */

public class Position implements Serializable{
	
	private static final long serialVersionUID = 42L;
	private int x,y,z;
	
	public Position(int x, int y, int z) {
		super();
		this.z = z;
		this.y = y;
		this.x = x;
	}
	
	public Position(Position p) {
		super();
		this.x = p.getX();
		this.y = p.getY();
		this.z = p.getZ();
	}
	
	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public String getPoint(){
		return "{"+Integer.toString(z)+","+Integer.toString(y)+","+Integer.toString(x)+"}";
	}

	/**
	 * Check if 2 given positions is equal
	 * @param obj , its the other position
	 * @return boolean true for equality and false either 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
	/**
	 * Convert position to printable String
	 */
	@Override
	public String toString(){
		return "{"+x+","+y+","+z+"}";
	}
	
	public void print(){
		System.out.println(this.toString());
	}

}
