package Game;

import java.util.ArrayList;
import java.util.List;
import Actors.Pair;

public class ShortestPathFinder {
	/**
	 * culculation the path the pacman
	 */
	private int[][] maze;// the maze of the game 
	public List<Pair> path = new ArrayList<Pair>();// the path need to move
	private Pair pathPosition;//the pair that we find now in the path   
	private int pathIndex=1;// the index that we find now in the path 

	/**
	 * constructor - get maze and initial  hime for find the path
	 * @param originalMaze- the original maze befor the change 
	 */
	public ShortestPathFinder(int[][] originalMaze) {
		maze = new int[originalMaze.length][originalMaze[0].length];
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[0].length; x++) {
				if (originalMaze[y][x] == 0 || originalMaze[y][x] == 2 || originalMaze[y][x] == 4 || originalMaze[y][x] == 8|| originalMaze[y][x] == 7)
					maze[y][x] = 0;// can pass on this 
				else
					maze[y][x] = 1;// cant pass on this 
			}
		}
	}
/**
 * check if the indexs are valid 
 * @param i
 * @param j
 * @param size
 * @return
 */
	private boolean valid(int i, int j, int size) { // checking if current index is outside the array limits
		if (i < 0 || j < 0 || i >= size || j >= size)
			return false;
		return true;
	}
/**
 * check if the neighbor is legal
 * @param i1
 * @param j1
 * @param i2
 * @param j2
 * @return
 */
	private boolean legalNeighbor(int i1, int j1, int i2, int j2) {
		if (valid(i1, j1, maze.length) && valid(i2, j2, maze.length)) {
			if (maze[i2][j2] != 1)
				return true;
		}
		return false;
	}
/**
 * create matrix of distance 
 * @param M
 * @return
 */
	private int[][] createDisMatrix(int[][] m) {
		// matrix of distances
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m.length; j++)
				m[i][j] = -1;
		return m;
	}
/**
 * return the distance from 1 point to aother 
 * @param i1
 * @param j1
 * @param i2
 * @param j2
 * @return
 */
	public  int distance(int i1, int j1, int i2, int j2) {
		if (valid(i1, j1, maze.length) == false)
			return -1;
		int[][] M = new int[maze.length][maze.length]; // matrix of distances
		M = createDisMatrix(M);
		M[i1][j1] = 0;
		int k = 0;
		int P = (maze.length * (maze.length + 1)) / 2; // The worst case that can be.
		distance(i1, j1, i2, j2, k, M, P);
		return M[i2][j2];
	}
/**
 * help to find the distance 
 * @param i1
 * @param j1
 * @param i2
 * @param j2
 * @param k
 * @param M
 * @param P
 */
	private void distance(int i1, int j1, int i2, int j2, int k, int[][] M, int P) {

		if (k == P)// if the recursion working P times , its better to stop because we will not get
					// shorter way.
			return;
		if (i1 == i2 && j1 == j2) // if we arrived to the point, don't try to move anymore.
			return;
		if (legalNeighbor(i1, j1, i1 + 1, j1)) // try down
		{
			if (M[i1 + 1][j1] == -1 || M[i1 + 1][j1] > k + 1) {
				M[i1 + 1][j1] = k + 1;
				distance(i1 + 1, j1, i2, j2, k + 1, M, P);
			}
		}
		if (legalNeighbor(i1, j1, i1 - 1, j1)) { // try up
			if (M[i1 - 1][j1] == -1 || M[i1 - 1][j1] > k + 1) {
				M[i1 - 1][j1] = k + 1;
				distance(i1 - 1, j1, i2, j2, k + 1, M, P);
			}
		}
		if (legalNeighbor(i1, j1, i1, j1 + 1)) // try right
		{
			if (M[i1][j1 + 1] == -1 || M[i1][j1 + 1] > k + 1) {
				M[i1][j1 + 1] = k + 1;
				distance(i1, j1 + 1, i2, j2, k + 1, M, P);
			}
		}
		if (legalNeighbor(i1, j1, i1, j1 - 1)) { // try left

			if (M[i1][j1 - 1] == -1 || M[i1][j1 - 1] > k + 1) {
				M[i1][j1 - 1] = k + 1;
				distance(i1, j1 - 1, i2, j2, k + 1, M, P);
			}
		}
	}

	/**
	 * find the min path from one point to aother 
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 * @return
	 */
	private int[][] minPath(int i1, int j1, int i2, int j2) {
		int[][] M = new int[maze.length][maze.length]; // matrix of distances
		M = createDisMatrix(M);
		M[i1][j1] = 0;
		int k = 0;
		int P = (maze.length * (maze.length + 1)) / 2;// Gets a table of distances.
		distance(i1, j1, i2, j2, k, M, P);
		int r = M[i2][j2]; // the number of minimum steps to the point.
		if (r == -1)
			return null;
		int[][] D = new int[r + 1][2];
		D[r][0] = i2;
		D[r][1] = j2;
		minPath(i1, j1, i2, j2, r, D, M); // Update the matrix of the moves.
		return D;
	}
/**
 * help for find the min path
 * @param i1
 * @param j1
 * @param i2
 * @param j2
 * @param r
 * @param D
 * @param M
 */
	private void minPath(int i1, int j1, int i2, int j2, int r, int[][] D, int[][] M) {
		if (r == 0)
			return;

		if (valid(i2 - 1, j2, M.length) && M[i2 - 1][j2] == r - 1) // try up
		{
			D[r - 1][0] = i2 - 1;
			D[r - 1][1] = j2;
			minPath(i1, j1, i2 - 1, j2, r - 1, D, M);
		} else if (valid(i2 + 1, j2, M.length) && M[i2 + 1][j2] == r - 1)// try down
		{
			D[r - 1][0] = i2 + 1;
			D[r - 1][1] = j2;
			minPath(i1, j1, i2 + 1, j2, r - 1, D, M);
		} else if (valid(i2, j2 + 1, M.length) && M[i2][j2 + 1] == r - 1)// try right
		{
			D[r - 1][0] = i2;
			D[r - 1][1] = j2 + 1;
			minPath(i1, j1, i2, j2 + 1, r - 1, D, M);
		} else if (valid(i2, j2 - 1, M.length) && M[i2][j2 - 1] == r - 1)// try left
		{
			D[r - 1][0] = i2;
			D[r - 1][1] = j2 - 1;
			minPath(i1, j1, i2, j2 - 1, r - 1, D, M);
		}
	}
/**
 * conver from index to pair all the path 
 * @param i1
 * @param j1
 * @param i2
 * @param j2
 */
	public void fillPath(int i1, int j1, int i2, int j2) {
		int[][] temp = minPath(i1, j1, i2, j2);
		if (temp != null)
			for (int i = 0; i < temp.length; i++)
				for (int k = 0; k < temp[i].length - 1; k++) {
					path.add(new Pair(temp[i][k], temp[i][k + 1]));
				}
	}
/**
 * if has next for move
 * @return
 */
	public boolean hasNext() {

		return pathIndex <= path.size() - 1;
	}
/**
 * return the next Pair if have and Does promote indexes
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
/**
 * return the pair but Does not promote indexes
 * @return
 */
	public Pair peek() {
		if (!hasNext()) {
			return null;
		}
		pathPosition = new Pair(path.get(pathIndex).getX(), path.get(pathIndex).getY());
		return pathPosition;
	}

}
