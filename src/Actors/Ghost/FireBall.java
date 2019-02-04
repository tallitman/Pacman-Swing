package Actors.Ghost;

import Actors.Pair;
import Actors.Pacman.PacmanL1;
import Actors.Pacman.PacmanL2;
import Actors.Pacman.PacmanL3;
import Game.PathForArm;

public class FireBall extends Ghost {
	private int changeAnimation = 0;
	private int direction;
	protected String[] pacmanFrameNames;
	private PathForArm pathForArm;
	private boolean shoot = false;

	public FireBall(int[][] originalMaze, Pair p) {
		super(p);
		this.maze = originalMaze;
		speed = 2;
	}

	public boolean isShoot() {
		return shoot;
	}

	public void setIsShoot(boolean shoot) {
		this.shoot = shoot;
	}

	public void setShoot(boolean shoot) {
		this.shoot = shoot;
	}

	protected void updateAnimation() {
		changeAnimation = (changeAnimation + 1) % 3;
		frameIndex = 3 * direction + changeAnimation;
		frame = frames[frameIndex];
	}

	public void updateDirection() {
		direction = pathForArm.getDirection();
	}

	/**
	 * Chase after the pacman using the shortest path
	 * 
	 * @return return true if the ghost catch the pacman
	 */
	public boolean goAfterPcaman() {
		if (pathForArm != null) {
			if (pathForArm.hasNext()) {
				updateDirection();
				move(pathForArm.getNext());
				return true;
			} else {
				shoot = false;
			}
		}
		return false;
	}

	public void move(Pair pair) {
		if (canChase) {
			if (pair != null)
				super.pair = pair;
			updateAnimation();
		}
	}

	protected void init() {
		pacmanFrameNames = new String[12];
		for (int i = 0; i < 12; i++) {
			pacmanFrameNames[i] = "res/fireBall_0_" + i + ".png";
		}
		loadFrames(pacmanFrameNames);
	}

	@Override
	public void visit(PacmanL1 p) {
		p.die();
		shoot = false;
	}

	@Override
	public void visit(PacmanL2 p) {
		p.die();
		shoot = false;

	}

	@Override
	public void visit(PacmanL3 p) {
		p.die();
		shoot = false;

	}

	public void setPair(Pair pair) {
		shoot = true;
		pathForArm = new PathForArm(maze, true, this.pair, pair);

	}

	@Override
	public void action() {
		if (shoot)
			goAfterPcaman();
	}

	public boolean fire() {
		return shoot;
	}

	@Override
	public String toString() {
		return "FireBall []";
	}

}
