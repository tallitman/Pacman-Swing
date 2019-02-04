package Actors.Fruits;

import java.util.List;

import Actors.Pair;
import Actors.Fruits.Fruits.fruitsState;

/**
 * This class represent poisoned apple that the pacman can eat and appear
 * randomally
 *
 */
public class ApplePoison extends Fruits {

	private int[] amount = { 2, 2, 2 };
	private final  String name="fruit_poison";
	public ApplePoison(List<Pair> places) {
		super(places);
		score = -200;
	}

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
