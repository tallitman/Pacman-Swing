package Actors.Ghost;

import Actors.Pair;
import Actors.Pacman.Pacman;
import Actors.Pacman.PacmanL1;
import Actors.Pacman.PacmanL2;
import Actors.Pacman.PacmanL3;
import Game.PathForArm;

public class WaterBall extends Ghost {
	protected String[] pacmanFrameNames;
	private PathForArm pathForArm;
	private boolean shoot = false;

	public WaterBall(int[][] originalMaze, Pair p) {
		super(p);
		speed=3;
		this.maze = originalMaze;
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

	}

	

	/**
	 * Chase after the pacman using the shortest path
	 * 
	 * @return return true if the ghost catch the pacman
	 */
	public boolean goAfterPcaman() {
		if (pathForArm != null) {
			if (pathForArm.hasNext()) {
				
				//pathForArm.getNext();
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
			//updateAnimation();
		}
	}

	@Override
	public void visit(PacmanL1 p) {
		p.die();
	}

	@Override
	public void visit(PacmanL2 p) {
		this.state = State.SCATTER;
		p.setState(Pacman.State.FREEZE);

	}

	@Override
	public void visit(PacmanL3 p) {
		state = Ghost.State.FREEZE;

	}

	public void setPair(Pair pair) {
		shoot = true;
		pathForArm = new PathForArm(maze, false, this.pair, pair);
	}

	public boolean fire() {
		return shoot;
	}

	protected void init() {
		pacmanFrameNames = new String[1];
		pacmanFrameNames[0] = "res/waterBall_0_" + 0 + ".png";
		loadFrames(pacmanFrameNames);
	}
	public void action() {
		if (shoot)
			goAfterPcaman();
	}
}
