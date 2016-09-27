package algorithms.mazeGenerators;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class defines a 3D Maze in a real world.
 */
public class Maze3d implements Serializable{
	
	
	/**
	 * serial number
	 */
	private static final long serialVersionUID = 42L;
	private String name;
	protected int[][][] maze;
	protected Position startPosition,goalPosition;
	protected int xLength,yLength,zLength;
	
	
	
	/**
	 * constructor that gets byte array and construct a maze3d from its bytes
	 * @param byteArr byte array
	 * @throws IOException 
	 */
	public Maze3d(byte[] byteArr) throws IOException {
		name = "";
		ByteArrayInputStream in = new ByteArrayInputStream(byteArr);
		DataInputStream dis = new DataInputStream(in);

		// creating a stream that reads primitive types easier
		this.xLength = dis.readInt();
		this.yLength = dis.readInt();
		this.zLength = dis.readInt();

		maze = new int[xLength][yLength][zLength];

		for (int i = 0; i < xLength; i++) {
			for (int j = 0; j < yLength; j++) {
				for (int n = 0; n < zLength; n++) {
					maze[i][j][n] = dis.read(); // reads byte

				}
			}
		}

		startPosition = new Position(dis.readInt(), dis.readInt(),dis.readInt());
		goalPosition = new Position(dis.readInt(), dis.readInt(), dis.readInt());
	
		
	}
	
	/**
	 * returning all the maze3d data converted to byte array
	 * format:
	 * 4 bytes of size of x axis,4 bytes of size of y axis,4 bytes of size of z axis,
	 * all the cells of maze 3d matrix,each one as byte,
	 * the start position:3 integers represented by 4 bytes each
	 * the goal position :3 integers represented by 4 bytes each
	 * 
	 * @return byte array with the maze details
	 * @throws IOException 
	 */
	public byte[] toByteArray() throws IOException
	{
		//creating a stream that reads primitive types easier
		ByteArrayOutputStream bb=new ByteArrayOutputStream();
		DataOutputStream dis=new DataOutputStream(bb);
		try {
			
			dis.writeInt(xLength);
			dis.writeInt(yLength);
			dis.writeInt(zLength);
			
			for(int i=0;i<xLength;i++)
			{
				for(int j=0;j<yLength;j++)
				{
					for(int n=0;n<zLength;n++)
					{
						dis.write(maze[i][j][n]);
					}
				}
			}
			dis.writeInt(startPosition.getX());
			dis.writeInt(startPosition.getY());
			dis.writeInt(startPosition.getZ());

			dis.writeInt(goalPosition.getX());
			dis.writeInt(goalPosition.getY());
			dis.writeInt(goalPosition.getZ());
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
		return bb.toByteArray();
	}
	
	/**
	 * Maze3d constructor
	 * @param x axis (floors)
	 * @param y axis (rows)
	 * @param z axis (columns)
	 * @return Maze3d object
	 */
	public Maze3d(int x, int y, int z) {
		name = new String("");
		this.startPosition = null;
		this.goalPosition = null;
		this.xLength = x;
		this.yLength = y;
		this.zLength = z;
		this.maze = new int[x][y][z];
		
        for(int i=0;i<x;i++)
        {
                for(int j=0;j<y;j++)
                {
                        for(int n=0;n<z;n++)
                        {
                        	maze[i][j][n]=1;
                        }
                }
        }
        
        for(int i=1;i<xLength-1;i+=2)
        {
                for(int j=1;j<yLength-1;j+=2)
                {
                        for(int n=1;n<zLength-1;n+=2)
                        {
                                maze[i][j][n]=0;
                        }
                }
        }
	}
	
	 /**
	   * This method is used to generate 2D maze by index for X Axis.
	   * @param index This is the index of x Axis to cut the maze on. 
	   * @return int[][] Defines a 2D maze.
	   */
	public int[][] getCrossSectionByX(int index) throws IndexOutOfBoundsException{
		//index = index * 2 + 1;
		if(index >= xLength || index < 0)
			throw new IndexOutOfBoundsException("Illegal Index: "+index);
		int [][]maze2d = new int[yLength][zLength];
			for (int j=0; j< yLength; j++){
				for (int k=0; k<zLength;k++){
					maze2d[j][k] = maze[index][j][k];
				}
			}
			return maze2d;
		}
	 /**
	   * This method is used to generate 2D maze by index for Y Axis.
	   * @param index This is the index of y Axis to cut the maze on. 
	   * @return int[][] Defines a 2D maze.
	   */
	public int[][] getCrossSectionByY(int index) throws IndexOutOfBoundsException{
		index = index * 2 + 1;
		
		if(index >= yLength || index < 0)
			throw new IndexOutOfBoundsException("Illegal Index: "+index);
		
		int [][]maze2d = new int[xLength][zLength];
		
		for(int i=0;i<xLength;i++){
			for(int j=0;j<zLength;j++){
				maze2d[i][j] = maze[i][index][j];
			}
		}
		
		return maze2d;
	}
	
	 /**
	   * This method is used to generate 2D maze by index for Z Axis.
	   * @param index This is the index of z Axis to cut the maze on. 
	   * @return int[][] Defines a 2D maze.
	   */
	public int[][] getCrossSectionByZ(int index) throws IndexOutOfBoundsException{
		index = index * 2 + 1;
		
		if(index >= zLength || index < 0)
			throw new IndexOutOfBoundsException("Illegal Index: "+index);
		
		int [][]maze2d = new int[xLength][yLength];
		
		for(int i=0;i<xLength;i++){
			for(int j=0;j<yLength;j++){
				maze2d[i][j] = maze[i][j][index];
			}
		}
		
		return maze2d;
	}
	
	 /**
	   * This method is used to return possible moves by given position
	   * @param p This is the given position to check the moves from. 
	   * @return ArrayList<Position> is a list of possible moves(Positions) from p.
	   */
	//Return possible moves by given position
	public ArrayList<Position> getPossibleMoves(Position p){
		//If the position given is the start position turn p into the first cell after start 
		if(p.equals(startPosition))
			p = getFirstCellAfterShellPostition(startPosition);
		
		ArrayList<Position> moves = new ArrayList<Position>();
		String []directions={"backward","forward","right","left","up","down"};
		int valueBetween;
		Position p2;
		
		for(int i=0; i<directions.length; i++){
			if(possibleStep(p,directions[i])){
				p2 = movePosition(p, directions[i]);
				valueBetween = maze[(p.getX() + p2.getX())/2][(p.getY() + p2.getY())/2][(p.getZ() + p2.getZ())/2];
				if(valueBetween == 0 && getCellValue(p2) == 0)
					moves.add(p2);
			}
		}
		return moves;
	}
	
	//Get Value of position
	public int getValueOfPosition(Position p){
		return maze[p.getX()][p.getY()][p.getZ()];
	}
	
	//Remove a wall between 2 given positions
	public void removeWallBetweenPositions(Position p1,Position p2){
		maze[(p1.getX() + p2.getX())/2][(p1.getY() + p2.getY())/2][(p1.getZ() + p2.getZ())/2] = 0;
	}
	
	//Check if the step is possible with a given direction
	public boolean possibleStep(Position p, String direction){
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();
		
		switch (direction){
			case "right":
				if(!isInShell(new Position(x,y,z+2)) && !isOutOfMaze(new Position(x,y,z+2)))
						return true;
				else return false;
			case "left":
				if(!isInShell(new Position(x,y,z-2)) && !isOutOfMaze(new Position(x,y,z-2)))
					return true;
				else return false;
			case "up":
				if(!isInShell(new Position(x,y+2,z)) && !isOutOfMaze(new Position(x,y+2,z)))
					return true;
				else return false;
			case "down":
				if(!isInShell(new Position(x,y-2,z)) && !isOutOfMaze(new Position(x,y-2,z)))
					return true;
				else return false;
			case "forward":
				if(!isInShell(new Position(x+2,y,z)) && !isOutOfMaze(new Position(x+2,y,z)))
					return true;
				else return false;
			case "backward":
				if(!isInShell(new Position(x-2,y,z)) && !isOutOfMaze(new Position(x-2,y,z)))
					return true;
				else return false;
			default:
				throw new IllegalArgumentException("Illegal Direction: "+direction);
		}
	}
	
	
	public boolean possibleCharacterMove(Position p, String direction) throws IllegalArgumentException{
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();
		
		Position nextPosition1 = null ,nextPosition2 = null;
		
		switch (direction) {
		case "right":
			nextPosition1 = new Position(x, y, z + 1);
			break;
		case "left":
			nextPosition1 = new Position(x, y, z - 1);
			break;
		case "up":
			nextPosition1 = new Position(x, y - 1, z);
			break;
		case "down":
			nextPosition1 = new Position(x, y + 1, z);
			break;
		case "forward":
			nextPosition1 = new Position(x + 1, y, z);
			nextPosition2 = new Position(x + 2, y, z);
			break;
		case "backward":
			nextPosition1 = new Position(x - 1, y, z);
			nextPosition2 = new Position(x - 2, y, z);
			break;
		default:
			throw new IllegalArgumentException("Illegal Direction: "
					+ direction);
		}
		
		//Handles moving between floors
		if (direction.equals("forward") || direction.equals("backward")) {
			if (!isOutOfMaze(nextPosition1) && !isOutOfMaze(nextPosition2)) {
				if (!isInShell(nextPosition1) && getValueOfPosition(nextPosition1) != 1){
					return (!isInShell(nextPosition2) && getValueOfPosition(nextPosition2) != 1);
				} else
					return false;
			} else
				return false;
		}
		
		return (!isInShell(nextPosition1) && getValueOfPosition(nextPosition1) != 1);
	}
	// Move character by direction
	public Position moveCharacter(Position p, String direction) {
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();

		switch (direction) {
		case "right":
			return new Position(x, y, z + 1);
		case "left":
			return new Position(x, y, z - 1);
		case "up":
			return new Position(x, y - 1, z);
		case "down":
			return new Position(x, y + 1, z);
		case "forward":
			return new Position(x + 2, y, z);
		case "backward":
			return new Position(x - 2, y, z);
		default:
			throw new IllegalArgumentException("Illegal Direction: "
					+ direction);
		}
	}
	
	
	// Move position by direction
	public Position movePosition(Position p, String direction) {
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();

		switch (direction) {
		case "right":
			return new Position(x, y, z + 2);
		case "left":
			return new Position(x, y, z - 2);
		case "up":
			return new Position(x, y + 2, z);
		case "down":
			return new Position(x, y - 2, z);
		case "forward":
			return new Position(x + 2, y, z);
		case "backward":
			return new Position(x - 2, y, z);
		default:
			throw new IllegalArgumentException("Illegal Direction: "
					+ direction);
		}
	}



	// Check by the position given if its on the shell of the maze
	public boolean isInShell(Position p) {
		return ((p.getZ() == 0 || p.getZ() == this.zLength - 1)
				|| (p.getY() == 0 || p.getY() == this.yLength - 1) || (p.getX() == 0 || p
				.getX() == this.xLength - 1));
	}

	// Check by the position given if its out of the shell boundaries
	public boolean isOutOfMaze(Position p) {
		return ((p.getZ() < 0 || p.getZ() > this.zLength - 1)
				|| (p.getY() < 0 || p.getY() > this.yLength - 1) || (p.getX() < 0 || p
				.getX() > this.xLength - 1));
	}

	// Returns the first cell after shell position that is inside the maze and
	// not in the shell
	public Position getFirstCellAfterShellPostition(Position p) {
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();

		if (x == 0)
			x++;
		if (x == xLength - 1)
			x--;
		if (y == 0)
			y++;
		if (y == yLength - 1)
			y--;
		if (z == 0)
			z++;
		if (z == zLength - 1)
			z--;

		return new Position(x, y, z);

	}

	// Check the positions given is a neighbors,space is to manipulate the check
	// for shell positions when needed
	public boolean isNeighbors(Position p1, Position p2, int space) {
		int p1x = p1.getX();
		int p1y = p1.getY();
		int p1z = p1.getZ();

		int p2x = p2.getX();
		int p2y = p2.getY();
		int p2z = p2.getZ();

		if (p1x == p2x && p1y == p2y
				&& (p1z == p2z - space || p1z == p2z + space))
			return true;
		if (p1x == p2x && p1z == p2z
				&& (p1y == p2y - space || p1y == p2y + space))
			return true;
		if (p1y == p2y && p1z == p2z
				&& (p1x == p2x - space || p1x == p2x + space))
			return true;

		return false;
	}

	// Randomize a position in the shell of the cube (maze)
	public Position randomPositionInShell() {
		int x, y, z;
		// Random number
		Random r = new Random();

		// Randomize on the 6 sides of the cube
		int side = r.nextInt(6);

		// Randomize x,y,z axis with length
		// Make sure the number is not in the shell
		int[] xValidValues = oddNumbersInRange(0, xLength);
		int[] yValidValues = oddNumbersInRange(0, yLength);
		int[] zValidValues = oddNumbersInRange(0, zLength);

		x = xValidValues[r.nextInt(xValidValues.length)];
		y = yValidValues[r.nextInt(yValidValues.length)];
		z = zValidValues[r.nextInt(zValidValues.length)];

		switch (side) {

		// First slice of the maze (x axis)
		case 0:
			x = 0;
			break;
		// Last slice of the maze (x axis)
		case 1:
			x = xLength - 1;
			break;
		// First slice of the maze (y axis)
		case 2:
			y = 0;
			break;
		// Last slice of the maze (y axis)
		case 3:
			y = yLength - 1;
			break;
		// First slice of the maze (z axis)
		case 4:
			z = 0;
			break;
		// Last slice of the maze (z axis)
		case 5:
			z = zLength - 1;
			break;
		}

		return new Position(x, y, z);
	}

	

	public void setCellValue(Position p, int value){
		maze[p.getX()][p.getY()][p.getZ()] = value;
	}
	
	public int getCellValue(Position p){
		return maze[p.getX()][p.getY()][p.getZ()];
	}
	
	public void setStartPosition(Position startPosition) {
		this.startPosition = startPosition;
	}

	public Position getStartPosition() {
		return startPosition;
	}
	
	public Position getGoalPosition() {
		return goalPosition;
	}

	public void setGoalPosition(Position goalPosition) {
		this.goalPosition = goalPosition;
	}

	public int getxLength() {
		return xLength;
	}

	public void setxLength(int xLength) {
		this.xLength = xLength;
	}

	public int getyLength() {
		return yLength;
	}

	public void setyLength(int yLength) {
		this.yLength = yLength;
	}

	public int getzLength() {
		return zLength;
	}

	public void setzLength(int zLength) {
		this.zLength = zLength;
	}

	public int[][][] getMaze() {
		return maze;
	}

	public void setMaze(int[][][] maze) {
		this.maze = maze;
	}
	
	/**
	 * Turns maze into String for printable version
	 * @return String
	 */
	public String printMaze(){
		StringBuilder sb = new StringBuilder();
		for (int i=0; i< xLength;i++){
			for (int j=yLength-1; j>= 0;j--){
				for (int k=0; k<zLength;k++){
					if(i== startPosition.getX() && j == startPosition.getY() && k == startPosition.getZ())
						sb.append("E"+" ");
					else if(i== goalPosition.getX() && j == goalPosition.getY() && k == goalPosition.getZ())
						sb.append("X"+" ");
					else{
						if(maze[i][j][k] == 1)
							sb.append("| ");
						else
							sb.append(maze[i][j][k]+" ");
					}
				}
				sb.append("\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	//Print maze2d from getcross method
	public String printMaze2d(int [][]maze2d){
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < maze2d.length ; i++)
		{
			for(int j = 0 ; j < maze2d[0].length ; j++)
			{
				sb.append(maze2d[i][j]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	

	/**
	 * Return odd numbers in given range
	 * @param start number
	 * @param end number
	 * @return intp[] , array of odd numbers
	 */
	public int[] oddNumbersInRange(int start, int end){
		int aLen = (end-start-1)/2;
		int []a = new int[aLen];
		int number = start+1;
		for(int i=0;i<aLen;i++){
			a[i] = number;
			number+=2;
		}
		return a;
	}
	
	/**
	 * Fill maze with digit 1 values
	 */
	public void fillWithWalls(){
		for(int i=0;i<xLength;i++)
        {
                for(int j=0;j<yLength;j++)
                {
                        for(int n=0;n<zLength;n++)
                        {
                                maze[i][j][n]=1;
                        }
                }
        }
	}
	
	/**
	 * equals method checks if this Maze3d equal to other Maze3d
	 * @param obj is an other Maze3d
	 * @return true if Maze3ds equals, false either
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Maze3d other = (Maze3d) obj;
		
		if (this.xLength != other.xLength)
			return false;
		if (this.yLength != other.yLength)
			return false;
		if (this.zLength != other.zLength)
			return false;
		if(!this.startPosition.equals(other.startPosition))
			return false;
		if(!this.goalPosition.equals(other.goalPosition))
			return false;
		
		for(int i=0;i<xLength;i++){
		  for(int j=0;j<yLength;j++){
		    for(int k=0;k<zLength;k++){
		    	if(this.maze[i][j][k] != other.maze[i][j][k])
		    		return false;
			}
		  }
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
