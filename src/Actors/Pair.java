package Actors;

public class Pair {

	/**
	 * represnt pair of x and y 
	 */
	private int x;
	private int y;
	/**
	 * constructor
	 * @param x
	 * @param y
	 */
	public Pair(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String toString(){
		return "["+this.x+","+this.y+"]";
	}

	public boolean equals(Pair p) {
		return this.x==p.getX() && this.y==p.getY();
	}
}
