package Actors.Fruits;

import java.util.List;

import Actors.Pair;
import Actors.Fruits.Fruits.fruitsState;
/**
 * This class represent Pineapple that the pacman can eat and appear randomally
 *
 */
public class Pineapple extends Fruits {

	private int[] amount = { 2, 4, 5 };
	private final  String name="pineapple";
	public Pineapple(List<Pair> places) {
		super(places);
		score = 100;
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
