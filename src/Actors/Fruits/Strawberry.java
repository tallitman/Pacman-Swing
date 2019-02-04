package Actors.Fruits;

import java.util.List;

import Actors.Pair;
import Actors.Fruits.Fruits.fruitsState;

/**
 * This class represent Strawberry that the pacman can eat and appear randomally
 *
 */
public class Strawberry extends Fruits {

	private int[] amount = { 0, 1, 2 };
	private final String name = "strawberry";

	public Strawberry(List<Pair> places) {
		super(places);
		state = fruitsState.HIDDEN;
		score = 300;
	}

	@Override
	public void init() {
		String path = "res/" + name + ".png";
		loadFrames(path);
	}

	@Override
	public int getAmount(int level) {
		return amount[level - 1];
	}

	@Override
	public void eat(int level) {
		amount[level - 1]--;
		active = false;
		visible = false;
		state = fruitsState.VISIBLE;
	}

	public String getName() {
		return name;
	}

}
