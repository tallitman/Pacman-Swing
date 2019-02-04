package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import Main.SettingsGame;

@SuppressWarnings("serial")
public class Game extends JFrame implements KeyListener, ActionListener {
	/**
	 * the manager of the game , create the board and toolbar.
	 */
	private ToolBar toolBar;// the tool bar of the game
	private Board board;// the board of the game
	private PacmanTimer timer;// the timer of the game
	private boolean initial = true;// if the game in in condtion initital or not
	private boolean _control = false;// for cheat
	private boolean _vk_t = false;// for cheat

	public static enum State {// all the condtion of the game
		GET_READY, READY, PAUSED, STARTED, GAME_OVER
	};

	public static State state = State.GET_READY;// the initial of the game

	/**
	 * the constrctor of the game, initial all panel and condtion that need
	 * 
	 * @param color
	 * @param mazes
	 */
	public Game(Color color, List<int[][]> mazes) {
		super("Pacman");
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton homeButton = new JButton();
		homeButton.addActionListener(this);// home button for alow to toolbar close the window of game
		toolBar = new ToolBar(homeButton);
		this.getContentPane().add(this.toolBar, BorderLayout.NORTH);
		board = new Board(mazes, color, toolBar);
		initTimer();
		this.getContentPane().add(board, BorderLayout.CENTER);
		setSize(800, 840);
		setMinimumSize(new Dimension(800, 840));
		setMaximumSize(new Dimension(800, 840));
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setLocationRelativeTo(null); // Appears in center on the display -must be after setVisible
		setVisible(true);
		pack();
		this.setResizable(false);

	}

	/**
	 * initial the timer, and register all actors to him.
	 */
	private void initTimer() {
		timer = PacmanTimer.getInstance(board);

	}

	/**
	 * if any button on keyboard click , this merthod uppen
	 * 
	 * @param arr
	 * @return
	 */

	@Override
	public void keyPressed(KeyEvent e) {
		if (state != State.GAME_OVER && (timer.isRunning() || state != State.STARTED)) {

			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {// close window
				dispose();
			}
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {// for cheat
				_control = true;

			}
			if (e.getKeyCode() == KeyEvent.VK_T && _control) {// for cheat
				_vk_t = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_R && _control && _vk_t) {// for cheat
				_control = false;
				_vk_t = false;
				if (board.getLevel() < 3) {// cheat to up level
					board.upLevel();
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_SPACE && state == State.GET_READY) {// start the game
				state = State.READY;
				if (!initial)
					toolBar.getTimer().start();
			}

			if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT
					|| e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)) {// all the arrows
				if (state == State.READY) {
					board.movePacman(e);
					if (initial) {
						toolBar.setStartTime(System.currentTimeMillis());
						timer.start();
						initial = false;

					}

					board.activePacman();
					state = State.STARTED;
				}
			}

			if (state == State.STARTED && board.getPacman().getActive()) {
				board.movePacman(e);
			}

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	public Board getBoard() {
		return board;
	}

	/**
	 * for close this window , action for home bottons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		state = State.GET_READY;
		this.timer.stop();
		this.dispose();
		new SettingsGame();

	}

}
