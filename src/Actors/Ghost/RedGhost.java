package Actors.Ghost;

import Actors.Pair;
import Actors.Pacman.PacmanL1;
import Actors.Pacman.PacmanL2;
import Actors.Pacman.PacmanL3;

public class RedGhost extends Ghost {

	private final int type=0;
	public RedGhost(Pair p) {
		super(p);
		speed=5;
		canArm = true;
	}

	public boolean isCanArm() {
		return canArm;
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

	public Ghost armGhost() {
		if(weapon==null)
			weapon = new FireBall(maze,new Pair(pair.getX(), pair.getY()));
		
		weapon.setState(State.READY);	
		weapon.setVisible(true);
		return weapon;
	}

	@Override
	protected void init() {
		String[] ghostFrameNames = new String[8];
		for (int i = 0; i < 8; i++) {
			ghostFrameNames[i] = "res/ghost_" + type + "_" + i + ".png";
		}
		loadFrames(ghostFrameNames);

	}

	public boolean fire() {
		if(weapon!=null && !weapon.fire()) {
			weapon.move(new Pair (this.pair.getX(),this.pair.getY()));
			if(shortestPathFinderGhost!=null) {
				Pair next = shortestPathFinderGhost.peek();
			if(next!=null) {
			Pair pair=new Pair(shortestPathFinderGhost.peek().getX(),shortestPathFinderGhost.peek().getY() );
			weapon.setPair(pair);
			}
			}
			
			return false;
	}
		return true;
	}

}
