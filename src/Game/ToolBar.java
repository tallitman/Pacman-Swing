package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import Actors.Fruits.Fruits;

@SuppressWarnings("serial")
public class ToolBar extends JPanel implements ActionListener {
	/**
	 * the class display all the lifes,fruits,score of the play and in addtion the time of the game 
	 */
	private JButton homeButton;// button for back to gamesettings and close this window 
	private JButton fastButton;// button change the speed of the game
	private JButton pauseButton;//button for pause the game
	private JLabel scoreLbl;// for displat the score of the player
	private JLabel[] lifeLbl;// for displat the life of the player
	private JLabel apple;// for count the number of apples the player take
	private JLabel pineapple;// for count the number of pineapples the player take
	private JLabel strawberry;// for count the number of strawberrys the player take
	private int score = 0;// the count of the score, init to zero
	private int maxLives = 3;// init the number max of lives to 3 
	private int lives = 3;// init the count of lives to 3 
	private final String IMG_PATH = "res/";// path to the dictiorny with the images
	private int earnedApple = 0;// count of apples
	private int earnedPinapple = 0;//count of pinapple
	private int earnedStrawberry = 0;//count of strawberry
	private JLabel lblTime;// label display the time of the game
	private Timer t;// the timer of the toolbar
	private long startTime = 0;//initial of the start game 
	private long endTime = 0;
	private long runTime = 0;
	private boolean paused = false;// for know if the playes pause the game or not 
	private boolean fast = false;// for know if the play change the speed of the game 

	/**
	 *constructor -initial all the labels of boolbar and the bouttons. in addtion define the size of this panel. 
	 *get homebutton for enable to close the window of the game from here
	 */
	public ToolBar(JButton homeButton) {
		super(new FlowLayout(FlowLayout.LEFT, 20, 5));
		this.homeButton= homeButton;
		// the size of this window 
		setSize(800, 40);
		setMinimumSize(new Dimension(800, 40));
		setMaximumSize(new Dimension(800, 40));
		init();
		// timer initial
		t = new Timer(1000, this);
	}

	/**
	 * initial the toolbar of the game
	 */

	private void init() {
		ImageIcon icon;
		// Home
		newButton(homeButton, "Home");
		add(homeButton);
		setBackground(new Color(25, 181, 254));
		// Score
		scoreLbl = new JLabel(score + "");
		icon = new ImageIcon(
				new ImageIcon(IMG_PATH + "score" + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		scoreLbl.setIcon(icon);
		add(scoreLbl);
		// Fruits
		icon = new ImageIcon(new ImageIcon(IMG_PATH + "pineapple" + ".png").getImage().getScaledInstance(30, 30,
				Image.SCALE_SMOOTH));
		pineapple = new JLabel(icon);
		pineapple.setText("X" + earnedPinapple);
		add(pineapple);
		icon = new ImageIcon(new ImageIcon(IMG_PATH + "greenapple" + ".png").getImage().getScaledInstance(30, 30,
				Image.SCALE_SMOOTH));
		apple = new JLabel(icon);
		apple.setText("X" + earnedApple);
		add(apple);
		icon = new ImageIcon(new ImageIcon(IMG_PATH + "strawberry" + ".png").getImage().getScaledInstance(30, 30,
				Image.SCALE_SMOOTH));
		strawberry = new JLabel(icon);
		strawberry.setText("X" + earnedStrawberry);
		add(strawberry);

		// Fast Button
		fastButton = new JButton();
		newButton(fastButton, "Faster");
		add(fastButton);
		fastButton.addActionListener(this);
		fastButton.setRequestFocusEnabled(false);

		// Pause Button
		pauseButton = new JButton();
		newButton(pauseButton, "Pause");
		pauseButton.addActionListener(this);
		pauseButton.setRequestFocusEnabled(false);
		add(pauseButton);
		// lives lbl
		lifeLbl = new JLabel[3];
		for (int i = 0; i < 3; i++)
			lifeLbl[i] = new JLabel();
		updateLives();
		add(lifeLbl[0]);
		add(lifeLbl[1]);
		add(lifeLbl[2]);
		// jLabel timer
		icon = new ImageIcon(
				new ImageIcon(IMG_PATH + "clock" + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		lblTime = new JLabel("00:00:00");
		lblTime.setIcon(icon);
		add(lblTime);

	}
/**
 * update the reprenst of the lives in the boolbar ,work when the player is died
 */
	private void updateLives() {
		ImageIcon icon = new ImageIcon(new ImageIcon(IMG_PATH + "pacmanlives" + ".png").getImage().getScaledInstance(20,
				20, Image.SCALE_SMOOTH));
		int i = 0;
		while (i < lives) {
			lifeLbl[i].setIcon(icon);
			i++;
		}
		while (i <= lives && i < maxLives) {
			lifeLbl[i].setIcon(null);
			i++;
		}
	}


	/**
	 * make new button
	 * 
	 * @param jb
	 * @param name
	 */
	private void newButton(JButton jb, String name) {
		ImageIcon icon = new ImageIcon(
				new ImageIcon(IMG_PATH + name + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		jb.setText(name);
		jb.setIcon(icon);
		jb.setBorderPainted(false);
		jb.setBorder(null);
		jb.setContentAreaFilled(false);
	}

	@Override
	/**
	 * play all the opertions of :bottons, update the display of the time
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == t) {// if timer play this 
			// long milliseconds = (System.currentTimeMillis() - startTime);
			long milliseconds = (System.currentTimeMillis() - runTime);
			long seconds = (milliseconds / 1000) % 60;
			long minutes = (int) ((milliseconds / (1000 * 60)) % 60);
			long hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
			lblTime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
		}
		if (e.getSource() == pauseButton) {// if click on button of pause
			if (!paused) {//for pause the game
				Game.state = Game.State.PAUSED;
				PacmanTimer t = PacmanTimer.getInstance();
				t.stop();
				this.t.stop();
				endTime = System.currentTimeMillis();
				pauseButton.setText("Resume");

			} else {
				Game.state = Game.State.STARTED;// for play the game 
				PacmanTimer t = PacmanTimer.getInstance();
				t.restart();
				this.t.restart();
				runTime += System.currentTimeMillis() - endTime;
				pauseButton.setText("Pause");
			}
			paused = !paused;
		}
		if (e.getSource() == fastButton)// if click on fast button
			if (!fast) {
				PacmanTimer t = PacmanTimer.getInstance();
				t.setFastFactor(2);
				fastButton.setText("Slower");
			} else {
				PacmanTimer t = PacmanTimer.getInstance();
				t.setFastFactor(1);
				fastButton.setText("Faster");
			}
		fast = !fast;

	}
/**
 * for update the score of the player 
 * @param addScore
 */
	public void updateScore(int addScore) {
		this.score += addScore;
		scoreLbl.setText(score + "");
	}
/**
 * for add new fruits that the playes earned
 * @param f
 */
	public void addAmount(Fruits f) {
		if (f.getName().equals("pineapple")) {//if earn pineapple
			earnedPinapple++;
			pineapple.setText("X" + earnedPinapple);
		}
		if (f.getName().equals("greenapple")) {//if earn greenapple
			earnedApple++;
			apple.setText("X" + earnedApple);
		}
		if (f.getName().equals("strawberry")) {//if earn strawberry
			earnedStrawberry++;
			strawberry.setText("X" + earnedStrawberry);
		}
	}

	public int getScore() {
		return score;
	}

	public int getLives() {
		return lives;
	}
/**
 * donw the lifes of the playes at 1 , and update the disaply 
 */
	public void takeLife() {
		lives--;
		updateLives();

	}
/**
 * for start the time of the game 
 * @param time
 */
	public void setStartTime(long time) {
		this.startTime = time;
		runTime = startTime;
		t.start();
	}

	public Timer getTimer() {
		return t;
	}
}
