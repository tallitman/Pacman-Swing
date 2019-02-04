package Game;

import java.util.ArrayList;
import java.util.List;

import Actors.Pair;

public class PathForArm {

	/**
	 * find the path to weapon of ghost 
	 */
	private int[][] maze;// them maze of the game 
	public List<Pair> path = new ArrayList<Pair>();// the path 
	private Pair pathPosition;// the curently pair 
	private int pathIndex=1;// the index of curently pair 
	private final int RIGHT = 0, DOWN = 1, LEFT = 2, UP = 3;//for now the direction
	private int direction;// the direction culculation with two pair
	private boolean isFire;// if his FireBall or WaterBall
	private int size;//the size of the maze (size*size )

	/**
	 * constructor - init the maze and create the path 
	 * @param originalMaze- the original maze
	 * @param isfire- true if is fire ball , false if hs water ball
	 * @param currntly- this pair
	 * @param next- next pair 
	 */
	public PathForArm(int[][] originalMaze, boolean isfire, Pair currntly, Pair next) {
		this.isFire = isfire;
		initMaze(originalMaze);// initial the maze
		this.size = maze.length;
		checkDirection(currntly, next);// find the direction that need move 
		createPath(currntly.getX(), currntly.getY());//create the path 
	}
/**
 * init the field direction
 * @param currntly
 * @param next
 */
	private void checkDirection(Pair currntly, Pair next) {
		if (currntly.getX() - next.getX() > 0) {
			this.direction = LEFT;
		} else if (currntly.getX() - next.getX() < 0) {
			this.direction = RIGHT;
		}
		// if arrivare here the x equiles
		else if (currntly.getY() - next.getY() < 0) {
			this.direction = DOWN;
		} else if (currntly.getY() - next.getY() > 0) {
			this.direction = UP;
		} else {
			this.direction = -1;
		}
	}
/**
 * initial the maze for culcalutor the path
 * @param originalMaze
 */
	private void initMaze(int[][] originalMaze) {
		maze = new int[originalMaze.length][originalMaze[0].length];
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[0].length; x++) {
				if (originalMaze[y][x] == 0 || originalMaze[y][x] == 2 || originalMaze[y][x] == 4
						|| originalMaze[y][x] == 7 || originalMaze[y][x] == 8)
					maze[y][x] = 0;
				else if (isFire) {
					if (originalMaze[y][x] == 1 || originalMaze[y][x] == 3 || originalMaze[y][x] == 6)
						maze[y][x] = 0;
					else
						maze[y][x] = 1;
				} else {
					maze[y][x] = 1;
				}
			}
		}
	}
	/**
	 * check if the indexs are valid
	 * @param i
	 * @param j
	 * @return
	 */
	private boolean valid(int i, int j) { // checking if current index is outside the array limits
		if (i < 0 || j < 0 || i >= size || j >= size)
			return false;
		return true;
	}

	/**
	 * create path with the direction 
	 * @param x
	 * @param y
	 */
	private void createPath(int x, int y) {
		if (valid(x, y) && maze[x][y] != 1) {
			int xCell = x;
			int yCell = y;
			if (direction == RIGHT) {
				path.add(new Pair(xCell, yCell));
				createPath(x + 1, y);
			} else if (direction == DOWN) {
				path.add(new Pair(xCell, yCell));
				createPath(x, y + 1);
			} else if (direction == LEFT) {
				path.add(new Pair(xCell, yCell));
				createPath(x - 1, y);
			} else if (direction == UP) {
				path.add(new Pair(xCell, yCell));
				createPath(x, y - 1);
			} else
				return;
		}

	}
/**
 * check if have next 
 * @return true if have next ,false if not have next 
 */
	public boolean hasNext() {

		return pathIndex <= path.size() - 1;
	}

	/**
	 * return the next pair if have and advanced the index 
	 * @return
	 */
	public Pair getNext() {
		if (!hasNext()) {
			return null;
		}
		pathPosition = new Pair(path.get(pathIndex).getX(), path.get(pathIndex).getY());
		pathIndex += 1;
		return pathPosition;
	}
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isFire() {
		return isFire;
	}

	public void setFire(boolean isFire) {
		this.isFire = isFire;
	}
}
