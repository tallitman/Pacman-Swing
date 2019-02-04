package Actors.Ghost;

import Actors.Pair;
import Actors.Pacman.PacmanL1;
import Actors.Pacman.PacmanL2;
import Actors.Pacman.PacmanL3;

public class Psyduck extends Ghost {

	public Psyduck(Pair pair) {
		super(pair);
		speed = 5;

	}

	@Override
	public void visit(PacmanL1 p) {
		p.confuse();

	}

	@Override
	public void visit(PacmanL2 p) {
		p.confuse();

	}

	@Override
	public void visit(PacmanL3 p) {
		p.confuse();

	}

	protected void updateAnimation() {

	}

	protected void init() {
		String[] ghostFrameNames = new String[1];
		for (int i = 0; i < 1; i++) {
			ghostFrameNames[i] = "res/psyduck_" + i + ".png";
		}
		loadFrames(ghostFrameNames);

	}

	/**
	 * waiting 7 second until go out
	 */
	@Override
	public void action() {
		if (counter > 2 && counter < 200) {
			canReleased = true;
			super.action();
		}
		if (counter < 201)
			counter++;
		else {
			visible = false;
			canChase = false;

		}
	}

}
