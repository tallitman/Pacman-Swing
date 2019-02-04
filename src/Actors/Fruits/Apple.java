package Actors.Fruits;

import java.util.List;

import Actors.Pair;

/**
 * This class represent apple that the pacman can eat and appear randomally
 *
 */
public class Apple extends Fruits {

	private int[] amount = { 2, 4, 5 };
	private final  String name="greenapple";
	public Apple(List<Pair> places) {
		super(places);
		score = 200;
	}

	@Override
	public void init() {
		String path = "res/" + name + ".png";
		loadFrames(path);
	}

	public int getAmount(int level) {
		return amount[level - 1];
	}

	@Override
	public void eat(int level) {
		amount[level - 1] = amount[level - 1] - 1;
		active = false;
		visible = false;
		state = fruitsState.VISIBLE;
	}
	public String getName() {
		return name;
	}
}
