package algorithms.mazeGenerators;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class defines a 3D Maze in a real world.
 */
public class Maze3d {
	
	
	protected int[][][] maze;
	protected Position startPosition,goalPosition;
	protected int xLength,yLength,zLength;
	
	public Maze3d(int x, int y, int z) {
		
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
	public int[][] getCrossSectionByX(int index){
		index = index * 2 + 1;
		
		if(index >= xLength || index < 0)
			throw new IndexOutOfBoundsException("Illegal Index: "+index);
		
		int [][]maze2d = new int[yLength][zLength];
		
		for(int i=0;i<yLength;i++){
			for(int j=0;j<zLength;j++){
				maze2d[i][j] = maze[index][yLength-1-i][j];
			}
		}
		
		return maze2d;
	}
	
	 /**
	   * This method is used to generate 2D maze by index for Y Axis.
	   * @param index This is the index of y Axis to cut the maze on. 
	   * @return int[][] Defines a 2D maze.
	   */
	public int[][] getCrossSectionByY(int index){
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
	public int[][] getCrossSectionByZ(int index){
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
	
		//Move position by direction
		public Position movePosition(Position p, String direction){
			int x = p.getX();
			int y = p.getY();
			int z = p.getZ();
			
			switch (direction){
				case "right":
					return new Position(x,y,z+2);
				case "left":
					return new Position(x,y,z-2);					
				case "up":
					return new Position(x,y+2,z);
				case "down":
					return new Position(x,y-2,z);
				case "forward":
					return new Position(x+2,y,z);
				case "backward":
					return new Position(x-2,y,z);
				default:
					throw new IllegalArgumentException("Illegal Direction: "+direction);
			}
		}
	
		//Check by the position given if its on the shell of the maze
		public boolean isInShell(Position p)
		{
			return ((p.getZ() == 0 || p.getZ() == this.zLength - 1) || (p.getY() == 0 || p.getY() == this.yLength - 1) || (p.getX() == 0 || p.getX() == this.xLength - 1));
		}
		
		//Check by the position given if its out of the shell boundaries
		public boolean isOutOfMaze(Position p)
		{
			return ((p.getZ() < 0 || p.getZ() > this.zLength - 1) || (p.getY() < 0 || p.getY() > this.yLength - 1) || (p.getX() < 0 || p.getX() > this.xLength - 1));
		}
	
	
		//Returns the first cell after shell position that is inside the maze and not in the shell
		public Position getFirstCellAfterShellPostition(Position p){
			int x = p.getX();
			int y = p.getY();
			int z = p.getZ();
			
			if(x == 0)
				x ++;
			if(x == xLength - 1)
				x --;
			if(y == 0)
				y ++;
			if(y == yLength - 1)
				y --;
			if(z == 0)
				z ++;
			if(z == zLength - 1)
				z --;
			
			return new Position(x, y, z);
			
		}
		 
	
	//Check the positions given is a neighbors,space is to manipulate the check for shell positions when needed
	public boolean isNeighbors(Position p1,Position p2,int space){
		int p1x = p1.getX();
		int p1y = p1.getY();
		int p1z = p1.getZ();
		
		int p2x = p2.getX();
		int p2y = p2.getY();
		int p2z = p2.getZ();
		
		if(p1x == p2x && p1y == p2y && (p1z == p2z-space || p1z == p2z+space))
			return true;
		if(p1x == p2x && p1z == p2z && (p1y == p2y-space || p1y == p2y+space))
			return true;
		if(p1y == p2y && p1z == p2z && (p1x == p2x-space || p1x == p2x+space))
			return true;
			
		return false;
	}
	
	
	//Randomize a position in the shell of the cube (maze)
	public Position randomPositionInShell()
	{
		int x ,y ,z;
		//Random number
		Random r = new Random();
	
		//Randomize on the 6 sides of the cube
		int side = r.nextInt(6);
		
		//Randomize x,y,z axis with length
		//Make sure the number is not in the shell
		int []xValidValues = oddNumbersInRange(0, xLength);
		int []yValidValues = oddNumbersInRange(0, yLength);
		int []zValidValues = oddNumbersInRange(0, zLength);
		
		x = xValidValues[r.nextInt(xValidValues.length)];
		y = yValidValues[r.nextInt(yValidValues.length)];
		z = zValidValues[r.nextInt(zValidValues.length)];
		
		
		switch (side) {
		
		//First slice of the maze (x axis)
		case 0:
			x = 0;
			break;
		//Last slice of the maze (x axis)
		case 1: 
			x = xLength - 1;
			break;
		//First slice of the maze (y axis) 
		case 2:
			y = 0;
			break;
		//Last slice of the maze (y axis)
		case 3:
			y = yLength - 1;
			break;
	    //First slice of the maze (z axis)
		case 4:
			z = 0;
			break;
		//Last slice of the maze (z axis)
		case 5:
			z = zLength - 1;
			break;
		}
		
		return  new Position(x, y, z);
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
	
	//Print the maze, Y Axis is diverse for readability
	public void printMaze(){
		for (int i=0; i< xLength;i++){
			for (int j=yLength-1; j>= 0;j--){
				for (int k=0; k<zLength;k++){
					if(i== startPosition.getX() && j == startPosition.getY() && k == startPosition.getZ())
						System.out.print("E"+" ");
					else if(i== goalPosition.getX() && j == goalPosition.getY() && k == goalPosition.getZ())
						System.out.print("X"+" ");
					else{
						if(maze[i][j][k] == 1 && k==0)
							System.out.print(j+" ");
						else if(maze[i][j][k] == 1)
							System.out.print("| ");
						else
							System.out.print(maze[i][j][k]+" ");
					}
				}
				System.out.print("\n");
			}
			System.out.print("\n");
		}		
	}
	//Print maze2d from getcross method
	public void printMaze2d(int [][]maze2d){
		
		for(int i=0; i<maze2d.length;i++){
			for (int j=0;j<maze2d[i].length;j++){
				System.out.print(maze2d[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
	
	
	//Return odd numbers in range, assumes start is 0
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
	
	//Fill maze with walls (1)
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
	
}
