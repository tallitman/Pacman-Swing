package Actors.Ghost;

import Actors.Pair;
import Actors.Pacman.Pacman;
import Actors.Pacman.PacmanL1;
import Actors.Pacman.PacmanL2;
import Actors.Pacman.PacmanL3;
/**
 * This class represent green ghost in the game
 * he cant arm and released first from the cage every level.
 *
 */
public class GreenGhost extends Ghost {
	private final int type=2;
	public GreenGhost(Pair p) {
		super(p);
		speed = 1;
		canArm = false;
	}

	/**
	 * Kill the pacman
	 */
	@Override
	public void visit(PacmanL1 p) {
		p.die();
		setState(Ghost.State.FREEZE);
	}

	/**
	 * Ghost gone for 5 seconds mode
	 */
	@Override
	public void visit(PacmanL2 p) {
		visible= false;
		dissaper();
	}

	/**
	 * pacman kill the ghost
	 */
	@Override
	public void visit(PacmanL3 p) {
		die();
	}

	@Override
	protected void init() {
		String[] ghostFrameNames = new String[8];
		for (int i = 0; i < 8; i++) {
			ghostFrameNames[i] = "res/ghost_" + type + "_" + i + ".png";
		}
		loadFrames(ghostFrameNames);
	}

	/**
	 * waiting 7 second until go out
	 */
	@Override
	public void action() {
		if (counter >= 25) {
			canReleased = true;
			super.action();
		}
		if (counter < 25)
			counter++;
	}

}
