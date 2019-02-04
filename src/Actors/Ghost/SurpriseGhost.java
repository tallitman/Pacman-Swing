package Actors.Ghost;

import Actors.Pair;
import Actors.Ghost.Ghost.State;
import Actors.Pacman.PacmanL1;
import Actors.Pacman.PacmanL2;
import Actors.Pacman.PacmanL3;

public class SurpriseGhost extends Ghost {
	private final int type = 4;
	private final int dissapearTime = 20;
	private int dissapear = 0;
	private boolean canDisappear = true;

	public SurpriseGhost(Pair p) {
		super(p);
		speed = 2;
		canReleased=false;

	}

	@Override
	public void visit(PacmanL1 p) {
		p.die();

	}

	@Override
	public void visit(PacmanL2 p) {
		p.die();

	}

	@Override
	public void visit(PacmanL3 p) {
		p.die();

	}

	@Override
	protected void init() {
		String[] ghostFrameNames = new String[1];
		for (int i = 0; i < 1; i++) {
			ghostFrameNames[i] = "res/fastghost_" + i + ".png";
		}
		loadFrames(ghostFrameNames);

	}

	/**
	 * waiting 7 second until go out
	 */
	@Override
	public void action() {
		if (counter > 60 && counter < 660) {
			canReleased = false;
			if(canReleased)
			switch (state) {
			case CAGE:
				release();
				canChase = true;
				state = State.READY;
				break;
			case SCATTER:
				break;
			case ARM:
				break;
			case CHASE:
				break;
			case DIED:
				break;
			case FREEZE:
				break;
			case READY:
				goAfterPcaman();
				break;
			}

			if (canDisappear)
				dissappearMe();
		}
		if (counter < 660)
			counter++;
		else {
			visible = false;
			canChase = false;
			canDisappear = false;
		}
	}

	protected void updateAnimation() {

	}

	private void dissappearMe() {
		visible = true;
		if (dissapear < dissapearTime) {
			visible = false;
		} else if (dissapear == 30) {
			dissapear = 0;
		}
		dissapear++;
	}

}
