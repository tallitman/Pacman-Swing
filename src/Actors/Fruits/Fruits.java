package Actors.Fruits;

import java.util.List;
import java.util.Random;
import Actors.Actor;
import Actors.Pair;
import Game.TimerListener;

/**
 * This class represent fruits in the game.
 * 
 * @author Tal
 *
 */
public abstract class Fruits extends Actor implements TimerListener {

	private List<Pair> freePlaces;
	private final int flickerTime = 10;
	private int flicker = 0;
	private int opacity = 7;
	private int time = 25;
	protected int counter = 0;
	protected boolean active = false;
	protected boolean canEat = false;
	private boolean fruitDone = false;// true-id have more fruit from this type ,false- if done fruit from this
	protected int score;
	public fruitsState state = fruitsState.HIDDEN;

	public static enum fruitsState {
		HIDDEN, FLICKER, OPACITY, VISIBLE
	}

	public Fruits(List<Pair> places) {
		super();
		freePlaces = places;
		visible = false;
	}

	public abstract int getAmount(int level);

	public int getScore() {
		return score;
	}

	public void action() {
		if (!fruitDone)
			active = true;

		if (this.counter > time && !fruitDone && active) {
			switch (state) {
			case HIDDEN:

				if (!visible && active) {
					changePair();
					state = fruitsState.FLICKER;
					canEat = true;
				}
				break;
			case FLICKER:
				if (!active) {
					resestState();
				}
				if (!flicker()) {
					state = fruitsState.VISIBLE;
					counter = 0;
				}
				break;
			case VISIBLE:
				if (counter > time) {
					if (active && visible) {
						state = fruitsState.OPACITY;
						opacity = 10;
						counter = 0;
					} else {
						resestState();
						counter = 0;
					}
				}
				break;
			case OPACITY:
				if (!active || counter > 10) {
					resestState();
					counter = 0;
				}
				break;

			}

		}
		counter++;

	}

	private void resestState() {
		pair = null;
		visible = false;
		state = fruitsState.HIDDEN;
		active = false;
		counter = 0;
		canEat = false;
		opacity = 10;
	}

	public void setPair(Pair pair) {
		this.pair = pair;
	}

	public fruitsState getState() {
		return state;
	}

	/**
	 * Flicker the fruit
	 * 
	 * @param i
	 * @param whatDo
	 *            if true delete him if false draw him
	 */
	public boolean flicker() {// capper the ghosts
		visible = !visible;
		if (flicker > flickerTime) {
			flicker = 0;
			visible = true;
			return false;
		} else
			flicker++;
		return true;

	}

	public abstract void eat(int level);

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public boolean getVisible() {
		return visible;
	}

	public Pair findFruitPlace() {
		int max = freePlaces.size();
		Random r = new Random();
		int next = r.nextInt(max);
		Pair res = freePlaces.get(next);
		freePlaces.remove(next);
		return res;
	}

	public boolean isCanEat() {
		return canEat;
	}

	public void setCanEat(boolean canEat) {
		this.canEat = canEat;
	}

	public void changePair() {
		pair = findFruitPlace();

	}

	public int getOpacity() {
		return opacity;
	}

	public void reduceOpacity() {
		if(opacity+3<255)
		this.opacity += 3;
	}

	public void disable() {
		active = false;
		fruitDone = true;
		pair = null;
		visible = false;

	}

	public boolean getFruitDone() {
		return fruitDone;
	}
	public abstract String getName();
}
