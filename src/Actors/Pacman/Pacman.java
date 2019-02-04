package Actors.Pacman;

import java.awt.event.KeyEvent;
import Actors.Actor;
import Actors.Pair;
import Actors.Visited;
import Game.Board;

public abstract class Pacman extends Actor implements Visited {
	protected final static int RIGHT = 0, DOWN = 1, LEFT = 2, UP = 3;
	protected final int SIZE = 800;
	protected String[] pacmanFrameNames;
	protected int frameIndex = 0;
	private int changeAnimation = 0;
	private int direction;
	private int lastDirection;
	private boolean active = true;
	private final int holdTime = 10;
	private int hold = 0;
	private int confusedTime= 0;
	public enum State {
		LIVE, DIED, FREEZE
	};

	private State state = State.LIVE;
	private boolean confused = false;
	private int delta = 1;

	public Pacman(Pair pair) {
		super(pair);
		visible = true;
	}

	/**
	 * update automatic move of the pacman by the dpm
	 */
	public void updateMove(Board board) {

		if (direction == LEFT) {
			if (canMove(pair.getX() - delta, pair.getY(), board))
				pair.setX(pair.getX() - delta);
		} else if (direction == RIGHT) {
			if (canMove(pair.getX() + delta, pair.getY(), board))
				pair.setX(pair.getX() + delta);
		} else if (direction == UP) {
			if (canMove(pair.getX(), pair.getY() - delta, board))
				pair.setY(pair.getY() - delta);
		} else if (direction == DOWN) {
			if (canMove(pair.getX(), pair.getY() + delta, board))
				pair.setY(pair.getY() + delta);
		} else if (direction == -1)
			direction = lastDirection;
		return;

	}

	/**
	 * change the picture every second for animation;
	 */
	private void updateAnimation() {
		changeAnimation = (changeAnimation + 1) % 4;
		frameIndex = 4 * direction + changeAnimation;
		frame = frames[frameIndex];
	}

	public Pair getPair() {
		return pair;
	}

	/**
	 * move by the keyboard press
	 * 
	 * @param e
	 */
	public void move(KeyEvent e, Board board) {
		if(confused)
			confusedTime++;
		if(confusedTime>15) {
			confuse();
			confusedTime=0;
		}
		if (state != State.LIVE)
			return;
		if (direction != -1) {
			lastDirection = direction;
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (canMove(pair.getX() - delta, pair.getY(), board))
				direction = LEFT;
			else
				direction = -1;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (canMove(pair.getX() + delta, pair.getY(), board))
				direction = RIGHT;
			else
				direction = -1;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (canMove(pair.getX(), pair.getY() - delta, board))
				direction = UP;
			else
				direction = -1;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (canMove(pair.getX(), pair.getY() + delta, board))
				direction = DOWN;
			else
				direction = -1;
		}
		updateMove(board);
		updateAnimation();
	}

	// }
	/**
	 * Check before UpdateMove. if there is no wall
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean canMove(int x, int y, Board board) {
		return board.legalMoves(x, y);
	}

	public void die() {
		active = false;
		state = State.DIED;
		visible = false;
		changeAnimation = -1;
		updateAnimation();

	}

	public void freeze() {
		active = false;
		state = State.FREEZE;

	}

	public boolean hold() {
		if (hold > holdTime) {
			hold = 0;
			state = State.LIVE;
			return false;
		} else
			hold++;
		return true;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean checkCollison(Pair pair) {

		return this.pair.equals(pair);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean getActive() {
		return active;
	}

	public void confuse() {
		confused = !confused;
		delta = delta * (-1);
	}
}
