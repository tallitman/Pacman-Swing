package Actors.Pacman;

import Actors.Pair;
import Actors.Visitor;

public class PacmanL1 extends Pacman {
	private int level= 1;
public PacmanL1(Pair pair) {
	super(pair);
}
	@Override
	public void impact(Visitor v) {
		v.visit(this);		
	}
protected void init() {
	  pacmanFrameNames = new String[30];
      for (int d=0; d<4; d++) {
          for (int i=0; i<4; i++) {
              pacmanFrameNames[i + 4 * d] = "res/pacman_" + d + "_" + i + ".png";
          }
      }
      for (int i=0; i<14; i++) {
          pacmanFrameNames[16 + i] = "res/pacman_died_" + i + ".png";
      }
      loadFrames(pacmanFrameNames);
      }

}
