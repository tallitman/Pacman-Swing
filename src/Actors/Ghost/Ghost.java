package Actors.Ghost;

import Actors.Actor;
import Actors.Pair;
import Actors.Visitor;
import Actors.Pacman.Pacman;
import Game.Board;
import Game.ShortestPathFinder;
import Game.TimerListener;

/**
 * This class represent ghost in the game, the enemy of the pacman. every ghost
 * has his abilities and some of them got a weapon.
 *
 */
public abstract class Ghost extends Actor implements Visitor, TimerListener {
	private int direction = 0;
	private final int holdTime = 15;
	private int hold = 0;
	protected int frameIndex = 0;
	protected int changeAnimation = 0;
	protected Ghost weapon;
	protected ShortestPathFinder shortestPathFinderGhost;
	protected int counter = 0;
	protected double speed;
	protected int[][] maze;
	protected boolean canReleased = false;
	protected boolean canChase = true; // if ghost can go after pacman
	public boolean armed = false;
	protected boolean canArm = false;

	public enum State {
		CAGE, SCATTER, CHASE, ARM, READY, FREEZE, DIED
	}

	protected State state = State.CAGE;

	public Ghost(Pair p) {
		super(p);
		visible = true;
	}

	public Ghost() {
		super(new Pair(0, 0));
	}

	public void move(Pair pair) {
		if (canChase) {
			if (pair != null)
				super.pair = pair;
			updateAnimation();
		}
	}

	public Ghost armGhost() {
		return null;
	}

	public boolean isCanArm() {
		return canArm;
	}

	public boolean isArmed() {
		return armed;
	}

	public void attack(Pacman pacman) {
		pacman.impact(this);
		setCanChase(false);
	}

	/**
	 * change the picture every second for animation;
	 */
	protected void updateAnimation() {
		changeAnimation = (changeAnimation + 1) % 4;
		frameIndex = 4 * direction + changeAnimation;
		frame = frames[frameIndex];
	}

	public ShortestPathFinder pathToCorner() {
		return findShorterPath(1, 1);
	}

	public ShortestPathFinder goAfter(Pair pair) {
		return findShorterPath(pair.getX(), pair.getY());
	}

	private ShortestPathFinder findShorterPath(int x, int y) {
		ShortestPathFinder originalMaze = new ShortestPathFinder(maze);
		originalMaze.fillPath(this.getPair().getX(), this.getPair().getY(), x, y);
		return originalMaze;
	}

	public void release() {
		move(new Pair(this.pair.getX(), this.pair.getY() - 2));
		shortestPathFinderGhost = pathToCorner();
		state = State.SCATTER;
	}

	/**
	 * the ghost number i start to mover to the corner on the smaller path
	 * 
	 * @return boolean true when the ghost d'ont arrive to the corner, and false
	 *         when she arrive
	 */
	public boolean moveToCorner() {

		if (isCanChase() && shortestPathFinderGhost.hasNext()) {
			move(shortestPathFinderGhost.getNext());
			return true;
		} else
			return false;// arrive to the corner
	}

	/**
	 * Chase after the pacman using the shortest path
	 * 
	 * @return return true if the ghost catch the pacman
	 */
	public boolean goAfterPcaman() {
		Pair p = findPacman();
		if (p != null)
			shortestPathFinderGhost = goAfter(p);
		if (shortestPathFinderGhost.hasNext()) {
			// shortestPathFinderGhost.getNext();
			Pair pair = shortestPathFinderGhost.getNext();
			move(pair);
			return true;
		} else
			return false;
	}

	private Pair findPacman() {
		boolean found = false;
		Pair res = null;
		for (int i = 0; i < maze.length && !found; i++)
			for (int j = 0; j < maze.length && !found; j++) {
				if (maze[i][j] == 8) {
					res = new Pair(i, j);
					found = true;
				}
			}
		return res;
	}

	/**
	 * Make a caper to the ghost
	 * 
	 * @param i
	 * @param whatDo
	 *            if true delete him if false draw him
	 */
	public boolean flicker() {// capper the ghosts
		if (state == State.DIED)
			return true;
		visible = !visible;
		if (hold > holdTime) {
			hold = 0;
			visible = true;
			canChase = true;
			return false;
		} else
			hold++;
		return true;

	}

	public Ghost checkCollison(Pacman pacman) {
		if (pair.getX() == pacman.getPair().getX() && pair.getY() == pacman.getPair().getY())
			return this;
		else
			return null;

	}

	public boolean fire() {
		return false;
	}

	public void restart(Board board) {
		// TODO Auto-generated method stub

	}

	public void action() {
		if (counter % speed == 0) {
			if (canReleased) {
				switch (state) {
				case CAGE:
					release();
					state = State.SCATTER;
					break;
				case SCATTER:// move to corner

					if (!moveToCorner()) {
						setState(State.CHASE);
					}
					break;

				case CHASE: // chase after pacman
					goAfterPcaman();
					if (!isCanArm()) {
						setState(State.READY);
					} else {
						setState(State.ARM);
					}
					break;
				case ARM:
					goAfterPcaman();
					if (!flicker()) {
						setState(State.READY);
						armGhost();
						fire();
					}
					break;
				case READY:
					goAfterPcaman();
					if (canArm)
						if (weapon == null)
							state = State.ARM;
						else
							fire();
					break;
				case FREEZE:
					canChase = false;
					if (!freeze())
						state = State.READY;
					break;
				case DIED:
					break;

				}
			}

		}
		counter++;
	}

	public void setMaze(int[][] maze) {
		this.maze = maze;
	}

	public boolean isCanReleased() {
		return canReleased;
	}

	public void setCanReleased(boolean canReleased) {
		this.canReleased = canReleased;
	}

	public boolean isCanChase() {
		return canChase;
	}

	public void setCanChase(boolean canChase) {
		this.canChase = canChase;
	}

	public boolean freeze() {
		if (hold > holdTime) {
			hold = 0;
			canChase = true;
			return false;
		} else
			hold++;
		return true;
	}

	public Ghost getWeapon() {
		return weapon;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isFire() {
		return weapon.fire();
	}

	public void setArmed(boolean b) {
		armed = b;
	}

	public void die() {
		state = State.DIED;
		canChase = false;
		visible = false;

	}

	public boolean dissaper() {
		if (state == State.DIED)
			return true;
		if (hold > holdTime) {
			hold = 0;
			canChase = true;
			visible = true;
			return false;
		} else
			hold++;
		return true;
	}

}
