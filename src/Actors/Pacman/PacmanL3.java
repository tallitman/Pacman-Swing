package Actors.Pacman;

import Actors.Pair;
import Actors.Visitor;

public class PacmanL3 extends Pacman {
	
	private int level=3;
	public PacmanL3(Pair pair) {
		super(pair);
	}
	@Override
	public void impact(Visitor v) {
		v.visit(this);		
	}
	protected void init() {
		pacmanFrameNames = new String[30];
		for (int d=0; d<4; d++) {
			for (int i=0; i<9; i++) {
				pacmanFrameNames[i + 4 * d] = "res/pcaman_red_cry/pacman_" + d + "_" + i + ".png";
			}
		}
		for (int i=0; i<14; i++) {
			pacmanFrameNames[16 + i] = "res/pcaman_red_cry/pacman_died_" + i + ".png";
		}
		loadFrames(pacmanFrameNames);
	}


}
