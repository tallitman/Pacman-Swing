package Actors;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import Game.Board;

/**
 * This class represent Actor in the game.
 *
 */
public abstract class Actor {
	protected Pair pair;
	protected BufferedImage frame;
	protected BufferedImage[] frames;
	protected boolean visible = false;
	protected int counter;

	public Actor(Pair pair) {
		this.pair = pair;
		init();
	}

	/**
	 * update the animation, place
	 * 
	 * @return
	 */
	protected abstract void init();

	public void setPair(Pair pair) {
		this.pair = pair;
	}

	public Actor() {
		init();
	}

	public Pair getPair() {
		return pair;
	}

	/**
	 * draw the current actor frame to the graphics
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (visible)
			g.drawImage(frame, (int) pair.getX() * Board.CELL, (int) pair.getY() * Board.CELL, 25, 25, null);
	}

	/**
	 * loading the actor pictures.
	 * 
	 * @param framesRes
	 */
	protected void loadFrames(String... framesRes) {
		frames = new BufferedImage[framesRes.length];
		for (int i = 0; i < framesRes.length; i++) {
			String frameRes = framesRes[i];
			try {
				
				frames[i] = ImageIO.read(new File(frameRes));
			} catch (IOException e) {
				System.out.println(i+"= "+frameRes);
				e.printStackTrace();
			}
		}
		frame = frames[0];
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean getVisible() {
		return visible;
	}

}
