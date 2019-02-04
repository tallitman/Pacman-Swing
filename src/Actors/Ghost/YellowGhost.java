package Actors.Ghost;

import Actors.Pair;
import Actors.Pacman.Pacman;
import Actors.Pacman.PacmanL1;
import Actors.Pacman.PacmanL2;
import Actors.Pacman.PacmanL3;
import Game.Board;
/**
 * This class represent yellow ghost in the game. the yellow ghost has a special ability- water balls.
 *
 */
public class YellowGhost extends Ghost {
	private final int type=1;
	public YellowGhost(Pair p) {
		super(p);
		speed=4;
		canArm=true;
	
	}

	/**
	 * kill the pacman ----cant reach this----
	 */
	@Override
	public void visit(PacmanL1 p) {
		p.die();
	}

	/**
	 * make the pacman freeze
	 */
	@Override
	public void visit(PacmanL2 p) {
		shortestPathFinderGhost = pathToCorner();
		this.state = State.SCATTER;
		p.setState(Pacman.State.FREEZE);
	}

	/**
	 * the ghost get freeze
	 */
	@Override
	public void visit(PacmanL3 p) {
		state = Ghost.State.FREEZE;
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
 * Arm the ghost with his special ability
 */
	public Ghost armGhost() {
		if (weapon == null)
			weapon = new WaterBall(maze, new Pair(pair.getX(), pair.getY()));

		weapon.setState(State.READY);
		return weapon;
	}
/**
 * restart the weapon 
 */
	public void restart(Board board) {
		if (weapon != null) {
			weapon.setState(State.CAGE);
		}

	}

	public boolean fire() {
		if (weapon != null && !weapon.fire()) {
			weapon.move(new Pair(this.pair.getX(), this.pair.getY()));
			if (shortestPathFinderGhost != null) {
				Pair next = shortestPathFinderGhost.peek();
				if (next != null) {
					Pair pair = new Pair(shortestPathFinderGhost.peek().getX(), shortestPathFinderGhost.peek().getY());
					weapon.setPair(pair);
				}
			}

			return false;
		}
		return true;
	}

}
