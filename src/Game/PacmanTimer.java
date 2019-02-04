package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class PacmanTimer implements ActionListener {
	/**
	 * the timer of the game , all the action ocuur when this class say
	 */
	private Timer mainTimer;// Main timer

	private List<TimerListener> listeners;// list of listeners
	private final int miliseconds = 260;
	private int fastFactor = 1;// The speed of the game
	private int delay_main = miliseconds / fastFactor;//
	private int wait = 0;// for to prevent timer play the listeners action
	private static PacmanTimer instance;// for singeltion of this class
	private Board board;// the board of the game

	/**
	 * constructor - initial the fields :board, timer ,listeners
	 * 
	 * @param board
	 */
	private PacmanTimer(Board board) {
		this.board = board;
		mainTimer = new Timer(delay_main, this);
		listeners = new ArrayList<TimerListener>();
	}

	/**
	 * for that toolbar can to update the timer clock of him ,defualt constrcutor
	 */
	private PacmanTimer() {
	}

	/**
	 * for singeltion of this this game
	 * 
	 * @param board
	 * @return
	 */
	public static PacmanTimer getInstance(Board board) {
		if (instance == null)
			instance = new PacmanTimer(board);
		return instance;
	}

	/**
	 * for singeltion of this this game only for toolbar
	 * 
	 * @param board
	 * @return
	 */
	public static PacmanTimer getInstance() {
		if (instance == null)
			instance = new PacmanTimer();
		return instance;
	}

	@Override
	/**
	 * Main timer tik
	 */
	public void actionPerformed(ActionEvent e) {
		if (wait == 0) {
			board.updateBoard();// update board
			board.updateFruits();// update fruit
			TimerListener temp = null;
			for (TimerListener listener : listeners) {// play all the listeners
				listener.action();
			}
			board.repaint();// repaint the board
			temp = board.checkNewActors();// check if have new actors
			if (temp != null)
				addListener(temp);// if have join him to listeners
		} else {
			wait--;// if the timer is on condtion wait
		}
	}

	/**
	 * add new actor to listeners
	 * 
	 * @param listener
	 */
	public void addListener(TimerListener listener) {
		listeners.add(listener);
	}

	/**
	 * clear thr listeners for up level
	 */
	public void clearListeners() {
		listeners.clear();
	}

	/**
	 * start the main timer
	 */
	public void start() {
		mainTimer.start();
	}

	/**
	 * stop the main timer
	 */
	public void stop() {
		mainTimer.stop();
	}

	/**
	 * restart the main timer
	 */
	public void restart() {
		mainTimer.restart();
	}

	public Timer getMainTimer() {
		return mainTimer;
	}

	public int getFastFactor() {
		return fastFactor;
	}

	/**
	 * change the fastfactor of the game
	 * 
	 * @param fastFactor
	 */
	public void setFastFactor(int fastFactor) {

		this.fastFactor = fastFactor;
		delay_main = miliseconds / fastFactor;
		mainTimer.setDelay(delay_main);
		this.mainTimer.restart();
	}

	/**
	 * change the delay od the main timer
	 * 
	 * @param delay
	 */
	public void TimerDelay(int delay) {
		this.mainTimer.setDelay(delay);
	}

	/**
	 * retrive to the orginial delay
	 */
	public void delayMain() {
		this.mainTimer.setDelay(delay_main);
	}

	/**
	 * check if the timer is play and dont on condition wait
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return mainTimer.isRunning() && wait == 0;
	}

	/**
	 * change timer to condtion wait
	 */
	public void waitTimer() {
		wait = 6;
	}

	public void removeListener(TimerListener f) {
		listeners.remove(f);

	}

}
