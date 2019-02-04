package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import Actors.Actor;
import Actors.Pair;
import Actors.Fruits.Apple;
import Actors.Fruits.ApplePoison;
import Actors.Fruits.Fruits;
import Actors.Fruits.Fruits.fruitsState;
import Actors.Fruits.Pineapple;
import Actors.Fruits.Strawberry;
import Actors.Ghost.SurpriseGhost;
import Actors.Ghost.Ghost;
import Actors.Ghost.GreenGhost;
import Actors.Ghost.Psyduck;
import Actors.Ghost.RedGhost;
import Actors.Ghost.YellowGhost;
import Actors.Pacman.Pacman;
import Actors.Pacman.Pacman.State;
import Actors.Pacman.PacmanL1;
import Actors.Pacman.PacmanL2;
import Actors.Pacman.PacmanL3;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class Board extends JPanel {
	/**
	 * Represents the maze that appears on screen. Creates the maze data using a 2D
	 * maze of Cell objects, and renders the maze on screen.
	 *
	 */
	public final static int CELL = 25;// the size width and long of all cell
	private String pathUpLevel = "res/uplevel.png";// path for icon up level
	private String pathFruit = "res/fruit.png";// path for icon of fruit
	private String pathDie = "res/die.png";// path icon for die
	private String pathFrezze = "res/frezze.png";// path icon fir frezze
	private int ghostInitialColumn;// Initial column index of first ghost
	private int ghostInitialRow;// Initial row index of first ghost
	private int pacmanInitialColumn;// Initial column index of pacman
	private int pacmanInitialRow;//// Initial column index of pacman
	private Cell[][] cells;// the maze that display consist form this cells
	private int[][] maze;// array [][] with numbers for create the maze
	private final int tileHeight = 32;// numbers of column on the maze
	private final int tileWidth = 32;// number of rows on the maze

	private Color colorSelect;// color of the maze
	private List<Cell> cageCell;// cell in the cage
	private List<Pair> freePlaces = new ArrayList<Pair>();// free place for fruits
	private boolean isCageOpen = false;// indctor to know if cage is open or close
	private ToolBar toolBar;// refernce to toolBar
	private List<Fruits> fruits;// refernce to fruits
	private List<Ghost> ghosts;// refernce to ghosts
	private Pacman pacman;// refernce to pacman
	private List<int[][]> mazes;// all the maze from level 1 to level 3
	private int level = 0;// the current level
	private Ghost lastAttacker;// the ghsot that attack last
	private AudioPlayer MGP = AudioPlayer.player;// for audio
	private AudioStream BGM;// for audio
	private AudioData MD;// for audio
	private AudioDataStream sound = null;// for audio
	private int counterPellets = 240;// number of cureent pellets
	// private int[] scoreToUpLevel = { 2000, 4500, 4000 };// score to up level
	// private int[] scoreToRelaseGhost = { 50, 1700, 2000 };// score to relase
	// ghost
	private Image imgUpLevel;// image with icon of up level
	private Image imgfruit;// img with icon of fruit
	private Image imgDie;// img with icon of die
	private Image imgFrezze;// image with icon of frezze
	// for trying fitchers
	private int[] scoreToUpLevel = { 10, 20, Integer.MAX_VALUE };
	private int[] scoreToRelaseGhost = { 50, 50, 50 };

	//
	/**
	 * Initial the game board
	 * 
	 * @param maze
	 */
	public Board(List<int[][]> mazes, Color color, ToolBar toolBar) {
		colorSelect = color;
		this.mazes = mazes;
		this.toolBar = toolBar;
		cageCell = new ArrayList<>();
		ghosts = new ArrayList<>();
		fruits = new ArrayList<>();
		startNewLevel();// start new level
		initActors();// intiail actors
		initImgUpLevel();// initail imge up level
		initImgFruit();// initail image of fruit
		initImgDie();// initial image of die
		initImgFreeze();// initatil image of freeze
		setPreferredSize(new Dimension(CELL * tileWidth, CELL * tileHeight));// Organize the size of the board 800*800
	}

	/**
	 * start new level
	 */
	private void startNewLevel() {
		level++;
		maze = transpose(mazes.get(level - 1));
		createCellmaze();
		initPacman();
		for (Ghost g : ghosts) {
			g.setMaze(maze);
		}
		repaint();

	}

	/**
	 * add new actor to the game
	 * 
	 * @param newActor
	 */
	public void addActor(Actor newActor) {
		newActor.draw(this.getGraphics());
	}

	/**
	 * create cells with the accordion value according to array [][]
	 */
	private void createCellmaze() {
		cells = new Cell[tileHeight][tileWidth];
		for (int row = 0; row < cells.length; row++) {
			for (int column = 0; column < cells[0].length; column++) {
				char type = findType(row, column);
				cells[row][column] = new Cell(row, column, type, colorSelect);
				if (type == 'e')
					cageCell.add(cells[row][column]);
				if (maze[row][column] == 4)
					freePlaces.add(new Pair(row, column));
				if (maze[row][column] == 6) {
					ghostInitialColumn = column;// the first ghost column
					ghostInitialRow = row - 4;// the first ghost row
					pacmanInitialColumn = column - 2;// above the cage
					pacmanInitialRow = row - 2;// center of the cage
				}
			}
		}
		maze[pacmanInitialRow][pacmanInitialColumn] = 8;

	}

	/**
	 * Generic paint method Iterates through each cell/tile in the 2D maze, drawing
	 * each in the appropriate location on screen
	 * 
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);// initial all the screen black
		g.fillRect(0, 0, tileWidth * CELL, tileHeight * CELL);
		for (int row = 0; row < tileHeight; row++)
			for (int column = 0; column < tileWidth; column++)
				cells[row][column].drawBackground(g);// draw all the cells 32*32
		for (Ghost ghost : ghosts) {
			ghost.draw(g);// draw all the ghost
		}
		pacman.draw(g);// draw the pacmen
		for (Fruits fruit : fruits) {
			fruit.draw(g);// draw the fruit
			if (fruit.state == fruitsState.OPACITY) {
				Color myColour = new Color(0, 0, 0, fruit.getOpacity());
				g.setColor(myColour);
				if (fruit.getPair() != null)
					g.fillRect(fruit.getPair().getX() * CELL, fruit.getPair().getY() * CELL, CELL, CELL);
				fruit.reduceOpacity();
			}

		}
		if (Game.state == Game.State.GET_READY || Game.state == Game.State.READY) {
			g.setColor(Color.WHITE);// for start of thr game draw the text "Get ready "
			g.drawString("Get Ready", 400, 300);
		}
	}

	/**
	 * reset all what need for up level
	 */
	private void playUpLevel() {
		music("pacman_extrapac");
		counterPellets = 240;
		this.maze = mazes.get(level);
		cageCell = new ArrayList<>();
		ghosts = new ArrayList<>();
		fruits = new ArrayList<>();
		freePlaces = new ArrayList<Pair>();
		startNewLevel();
		initActors();
	}

	/**
	 * the manger of the procces of up level, this public for the cheat
	 */
	public void upLevel() {
		PacmanTimer.getInstance(this).waitTimer();
		this.getGraphics().drawImage(imgUpLevel, 300, 300, null);
		playUpLevel();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Cell[][] getCells() {
		return cells;
	}

	/**
	 * get the index of value in the maze and find the type of him for draw him on
	 * the board table value of all type: 0- point to eat 1- inner frame 2-energy to
	 * eat 3-corral white 4-empty 5-out frame 6- the place of the ghost
	 * 
	 * @return the type of the value
	 */
	private char findType(int x, int y) {
		if (maze[x][y] == 3)// Coral exit
			return 'e';
		else if (maze[x][y] == 2)// energy food
			return 'p';
		else if (maze[x][y] == 0)// point to food
			return 'd';
		else if (maze[x][y] == 4 || maze[x][y] == 6)// empty
			return 'x';
		else if (maze[x][y] == 1 || maze[x][y] == 5)// frame
			return helpFindType(x, y);
		else
			return 'x';// temporary for check
	}

	/**
	 * this method help the find type if the the value is 5 or 1
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private char helpFindType(int x, int y) {

		if (y - 1 < 0 && x - 1 < 0)// exit the maze northwest corner
			return '2';
		else if (y - 1 < 0 && x + 1 >= maze.length)// exit the maze -northeast // corner
			return '1';
		else if (x - 1 < 0 && y + 1 >= maze.length)// exit the maze - southwest // corner
			return '4';
		else if (y + 1 >= maze.length && x + 1 >= maze.length)// exit the maze // -southeast // corner
			return '3';
		else if (y - 1 < 0 || y + 1 >= maze.length)// horizontal line
			return 'h';
		else if (x - 1 < 0 || x + 1 >= maze.length)// vertical line
			return 'v';
		else if ((maze[x - 1][y] == 5 || maze[x - 1][y] == 1) && (maze[x][y - 1] == 1 || maze[x][y - 1] == 5)
				&& (maze[x + 1][y] == 1 || maze[x + 1][y] == 5)
				&& (maze[x - 1][y - 1] == 4 || maze[x - 1][y - 1] == 0 || maze[x - 1][y - 1] == 2))
			return '3';
		else if ((maze[x - 1][y] == 5 || maze[x - 1][y] == 1) && (maze[x][y - 1] == 1 || maze[x][y - 1] == 5)
				&& (maze[x + 1][y] == 1 || maze[x + 1][y] == 5)
				&& (maze[x + 1][y - 1] == 4 || maze[x + 1][y - 1] == 0 || maze[x + 1][y - 1] == 2))
			return '4';
		else if ((maze[x + 1][y + 1] == 0 || maze[x + 1][y + 1] == 4 || maze[x + 1][y + 1] == 2)
				&& (maze[x][y + 1] == 1 || maze[x][y + 1] == 5) && (maze[x][y - 1] == 1 || maze[x][y - 1] == 5)
				&& (maze[x + 1][y] == 1 || maze[x + 1][y] == 5))
			return '2';
		else if ((maze[x - 1][y + 1] == 0 || maze[x - 1][y + 1] == 4 || maze[x - 1][y + 1] == 2)
				&& (maze[x][y - 1] == 1 || maze[x][y - 1] == 5) && (maze[x][y + 1] == 1 || maze[x][y + 1] == 5)
				&& (maze[x - 1][y] == 1 || maze[x - 1][y] == 5))
			return '1';
		else if ((maze[x][y - 1] == 4 || maze[x][y - 1] == 0 || maze[x][y - 1] == 2)
				&& (maze[x - 1][y] == 4 || maze[x - 1][y] == 0 || maze[x - 1][y] == 2))// northwest
			// corner
			return '2';
		else if ((maze[x][y - 1] == 4 || maze[x][y - 1] == 0 || maze[x][y - 1] == 2)
				&& (maze[x + 1][y] == 4 || maze[x + 1][y] == 0 || maze[x + 1][y] == 2))// northeast // corner
			return '1';
		else if ((maze[x - 1][y] == 4 || maze[x - 1][y] == 0 || maze[x - 1][y] == 2)
				&& (maze[x][y + 1] == 4 || maze[x][y + 1] == 0 || maze[x][y + 1] == 2))// southwest
			// corner
			return '4';
		else if ((maze[x + 1][y] == 4 || maze[x + 1][y] == 0 || maze[x + 1][y] == 2)
				&& (maze[x][y + 1] == 4 || maze[x][y + 1] == 0 || maze[x][y + 1] == 2))// southeast
			// corner
			return '3';
		else if ((maze[x - 1][y] == 4 || maze[x - 1][y] == 0 || maze[x - 1][y] == 2)
				|| (maze[x + 1][y] == 4 || maze[x + 1][y] == 0 || maze[x + 1][y] == 2))// vertical
			// line
			return 'v';
		else if ((maze[x][y - 1] == 4 || maze[x][y - 1] == 0 || maze[x][y - 1] == 2)
				|| (maze[x][y + 1] == 4 || maze[x][y + 1] == 0 || maze[x][y + 1] == 2))// horizontal
			// line
			return 'h';
		else
			return 'x';

	}

	/**
	 * check if the point of (x,y) is legal
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean legalMoves(int x, int y) {
		if (maze[x][y] == 5 || maze[x][y] == 3 || maze[x][y] == 1)
			return false;
		return true;
	}

	public int getGhostInitialColumn() {
		return ghostInitialColumn;
	}

	public int getGhostInitialRow() {
		return ghostInitialRow;
	}

	public int[][] getMaze() {
		return maze;
	}

	public void setMaze(int[][] maze) {
		this.maze = maze;
	}

	public int getPacmanInitialRow() {
		return pacmanInitialRow;
	}

	public int getPacmanInitialColumn() {
		return pacmanInitialColumn;
	}

	public void setPacmanInitialColumn(int pacmanInitialColumn) {
		this.pacmanInitialColumn = pacmanInitialColumn;
	}

	public void setPacmanInitialRow(int pacmanInitialRow) {
		this.pacmanInitialRow = pacmanInitialRow;
	}

	/**
	 * change the status of cage
	 */
	public void changeCageStatus() {
		boolean change = false;
		if (ghosts.get(1).isCanReleased() == false)
			if (level >= 2 && toolBar.getScore() > scoreToRelaseGhost[1] * level) {
				ghosts.get(1).setCanReleased(true);
				change = true;
			}
		if (ghosts.get(2).isCanReleased() == false)
			if (level >= 3 && toolBar.getScore() > scoreToRelaseGhost[2] * level) {
				ghosts.get(2).setCanReleased(true);
				change = true;
			}
		if (change) {
			for (Cell c : cageCell) {

				c.setType('x');
				c.drawBackground(getGraphics());
			}
			isCageOpen = !isCageOpen;
		}
	}

	/**
	 * Update the score when pacman reach to new point.
	 * 
	 * @param pair
	 */
	public void updateScore(Pair pair) {
		int x = pair.getX();
		int y = pair.getY();
		boolean updated = false;
		if (maze[x][y] == 0) {// if have pellet
			counterPellets--;
			toolBar.updateScore(10);
			updated = true;
		}
		if (maze[x][y] == 2) {// if have enrgy pellet
			music("pacman_chomp");
			toolBar.updateScore(50);
			updated = true;

		}
		if (maze[x][y] == 7) {// if have fruit
			Fruits f = findFruit(x, y);// Return the fruits that in (x,y) at maze
			if (f != null && f.isActive()) {
				toolBar.addAmount(f);
				f.eat(level);
				f.changePair();
				music("pacman_eatfruit");
				toolBar.updateScore(f.getScore());
				updated = true;
				PacmanTimer.getInstance(this).waitTimer();
				this.getGraphics().drawImage(imgfruit, 300, 300, null);// draw the image of fruit on the center of the
																		// display
				try {

					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		if (updated = true) {
			checkDone();// check if the game is done
			freePlaces.add(new Pair(x, y));// add new place for the fruit
		}
	}

	/**
	 * initail the music of the game
	 * 
	 * @param music
	 */
	private void music(String music) {

		try {
			BGM = new AudioStream(new FileInputStream("res/sound/" + music + ".wav"));
			MD = BGM.getData();
			sound = new AudioDataStream(MD);
		} catch (IOException error) {

		}
		MGP.start(sound);
	}

	/**
	 * the game is over- case to open summay panel on the center of the display
	 */
	private void gameOver() {
		music("pacman_intermission");
		Game.state = Game.State.GAME_OVER;
		PacmanTimer timer = PacmanTimer.getInstance(this);
		timer.stop();
		toolBar.getTimer().stop();
		JPanel panel = (JPanel) this;
		panel.setLayout(null);// for allow to use setborder in the panel of layout
		Summary summary = new Summary(getLevel(), toolBar.getScore(), toolBar.getLives());
		this.add(summary);
	}

	/**
	 * check if the game is done : up level or die or finsh the game
	 */
	private void checkDone() {
		if ((counterPellets == 0 || toolBar.getScore() > scoreToUpLevel[level - 1]) && level == 3) {
			gameOver();
		} else if (counterPellets == 0 || toolBar.getScore() > scoreToUpLevel[level - 1])
			upLevel();
	}

	/**
	 * check if have fruit on the point (x,y)
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Fruits findFruit(int x, int y) {
		Fruits res = null;
		for (Fruits f : fruits)
			if (f != null && f.getPair() != null && f.getPair().getX() == x && f.getPair().getY() == y)
				return f;
		return res;
	}

	/**
	 * initial new actors- pacman, ghosts, fruits.
	 */
	private void initActors() {
		initGhosts();
		initFruits();

	}

	/**
	 * Initial the ghosts at the level
	 */
	private void initGhosts() {
		PacmanTimer timer = PacmanTimer.getInstance(this);
		int xGhost = getGhostInitialRow();
		int yGhost = getGhostInitialColumn();
		Ghost greenGhost = new GreenGhost(new Pair(xGhost, yGhost));
		ghosts.add(greenGhost);
		greenGhost.setCanReleased(false);
		timer.addListener(greenGhost);
		greenGhost.setCanReleased(false);
		Ghost yellowGhost = new YellowGhost(new Pair(xGhost + 1, yGhost));
		ghosts.add(yellowGhost);
		timer.addListener(yellowGhost);
		Ghost redGhost = new RedGhost(new Pair(xGhost + 2, yGhost));
		ghosts.add(redGhost);
		timer.addListener(redGhost);
		Ghost psyduck = new Psyduck(new Pair(xGhost + 3, yGhost));
		ghosts.add(psyduck);
		timer.addListener(psyduck);
		Ghost fastGhost = new SurpriseGhost(new Pair(xGhost + 4, yGhost));
		ghosts.add(fastGhost);
		timer.addListener(fastGhost);
		for (Ghost g : ghosts) {
			g.setMaze(maze);
		}
	}

	/**
	 * initail the pacman accordig by the level
	 */
	private void initPacman() {
		int x = getPacmanInitialRow();
		int y = getPacmanInitialColumn();
		if (level == 1)
			pacman = new PacmanL1(new Pair(x, y));
		if (level == 2)
			pacman = new PacmanL2(new Pair(x, y));
		if (level == 3)
			pacman = new PacmanL3(new Pair(x, y));
	}

	/**
	 * Initial the fruits at the current level
	 */
	private void initFruits() {
		PacmanTimer timer = PacmanTimer.getInstance(this);
		Fruits pineapple = new Pineapple(freePlaces);
		fruits.add(pineapple);
		timer.addListener(pineapple);
		Fruits apple = new Apple(freePlaces);
		fruits.add(apple);
		timer.addListener(apple);
		Fruits strawberry = new Strawberry(freePlaces);
		fruits.add(strawberry);
		timer.addListener(strawberry);
		Fruits applePoison = new ApplePoison(freePlaces);
		fruits.add(applePoison);
		timer.addListener(applePoison);
	}

	/**
	 * Move the pacman
	 * 
	 * @param e
	 */
	public void movePacman(KeyEvent e) {
		if (pacman.getActive())
			checkCollisons();
		maze[pacman.getPair().getX()][pacman.getPair().getY()] = 4;
		pacman.move(e, this);
		updateFruits();
		checkCollison(pacman.getPair());
		updateBoard();
		repaint();

	}

	/**
	 * active the pacman after died/freeze
	 */
	public void activePacman() {
		pacman.setVisible(true);
		maze[pacman.getPair().getX()][pacman.getPair().getY()] = 4;
		pacman.setPair(new Pair(pacmanInitialRow, pacmanInitialColumn));
		for (Ghost gh : ghosts)
			if (pacman.checkCollison(gh.getPair()))
				pacman.setPair(new Pair(1, 1));
		pacman.setActive(true);
		maze[pacman.getPair().getX()][pacman.getPair().getY()] = 8;
	}

	public State getPacmanState() {
		return pacman.getState();
	}

	/**
	 * update the board status after tick
	 */
	public void updateBoard() {
		checkCollison(pacman.getPair());// collisons with fruits
		if (pacman.getState() == Pacman.State.LIVE && pacman.getActive()) {// if the pacamn live
			checkCollisons();
		} else if (pacman.getState() == Pacman.State.DIED) {// if the pacmen died
			Game.state = Game.State.STARTED;

			if (pacman.getActive() == false) {
				activePacman();
				afterDie();
			}

			if (!lastAttacker.flicker()) {
				pacman.setState(Pacman.State.LIVE);
				lastAttacker.setState(Ghost.State.READY);
			}
		} else if (pacman.getState() == Pacman.State.FREEZE) {// if the pacman freeze
			if (!pacman.hold()) {
				toolBar.updateScore(-10);
				lastAttacker.setState(Ghost.State.READY);
			}
		}

		if (pacman.getState() == Pacman.State.LIVE && !pacman.getActive()) {
			if (!lastAttacker.freeze())
				pacman.setActive(true);
		}

		if (lastAttacker != null && !lastAttacker.dissaper() && lastAttacker.getState() != Ghost.State.DIED)
			lastAttacker.setVisible(true);

		changeCageStatus();
	}

	/**
	 * Check collisons between pacman and ghosts
	 */
	private void checkCollisons() {
		for (Ghost g : ghosts) {
			if (pacman.checkCollison(g.getPair()) && pacman.getActive()) {
				if (g.isCanChase())
					g.attack(pacman);
				lastAttacker = g;
				if (pacman.getState() == Pacman.State.DIED) {
					PacmanTimer.getInstance(this).waitTimer();
					this.getGraphics().drawImage(this.imgDie, 300, 300, null);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}

	/**
	 * Some operations need to do after pacman is died
	 */
	public void afterDie() {
		toolBar.takeLife();
		if (isGameOver()) {
			gameOver();
		}

	}

	/**
	 * Check if the game is over
	 * 
	 * @return
	 */
	private boolean isGameOver() {
		return toolBar.getLives() == 0;
	}

	/**
	 * Make fruit active if he's visible.
	 * 
	 * @param f
	 */
	private void fruitActive(Fruits f) {
		if (!f.getVisible()) {
			maze[f.getPair().getX()][f.getPair().getY()] = 7;
		}
	}

	/**
	 * Make a fruit non-active if his fields say that
	 * 
	 * @param f
	 */
	private void fruitNotActive(Fruits f) {
		f.setVisible(false);
		maze[f.getPair().getX()][f.getPair().getY()] = 4;
		freePlaces.add(new Pair(f.getPair().getX(), f.getPair().getY()));
		f.setPair(f.findFruitPlace());
		f.setActive(true);
	}

	/**
	 * Update the state of the fruits in the board
	 */
	public void updateFruits() {
		for (Fruits f : fruits) {
			if (f.getAmount(level) <= 0) {
				PacmanTimer t = PacmanTimer.getInstance();
				t.removeListener(f);
				fruits.remove(f);
			}
			if (f != null && f.getPair() != null) {
				if (f.isActive() && f.getAmount(level) > 0)
					fruitActive(f);
				else
					fruitNotActive(f);
			}
		}
	}

	/**
	 * Check collison on the maze between pacman and fruits/pillis
	 * 
	 * @param pair
	 */
	private void checkCollison(Pair pair) {
		if (maze[pair.getX()][pair.getY()] == 0 || maze[pair.getX()][pair.getY()] == 2
				|| maze[pair.getX()][pair.getY()] == 7) {
			updateScore(pair);
			cells[pair.getX()][pair.getY()].setType('x');

		}
		maze[pacman.getPair().getX()][pacman.getPair().getY()] = 8;

	}

	/**
	 * check if the ghsot have weapon now
	 */
	public Ghost checkNewActors() {
		Ghost temp = null;
		for (int i = 0; i < 3; i++) {
			temp = ghosts.get(i).getWeapon();
			if (temp != null && ghosts.get(i).isArmed() == false) {
				ghosts.add(temp);
				ghosts.get(i).setArmed(true);
				return temp;
			}
		}
		return null;
	}

	public int getLevel() {
		return level;
	}

	public Pacman getPacman() {
		return pacman;
	}

	/**
	 * help function to transpose the maze
	 * 
	 * @param arr
	 * @return
	 */
	private int[][] transpose(int arr[][]) {
		int m = arr.length;
		int n = arr[0].length;
		int ret[][] = new int[n][m];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				ret[j][i] = arr[i][j];
			}
		}
		return ret;
	}

	/**
	 * Install the images between screen
	 * 
	 * @param path
	 * @return
	 */
	private BufferedImage loadFrames(String path) {
		File file = new File(path);
		BufferedImage temp = null;
		try {
			temp = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;

	}

	/**
	 * initail the image of freeze
	 */
	private void initImgFreeze() {
		BufferedImage temp1 = loadFrames(this.pathFrezze);
		this.imgFrezze = new ImageIcon(temp1).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
	}

	/**
	 * initail the image of die
	 */
	private void initImgDie() {
		BufferedImage temp1 = loadFrames(this.pathDie);
		this.imgDie = new ImageIcon(temp1).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
	}

	/**
	 * initail the image of fruit
	 */
	private void initImgFruit() {
		BufferedImage temp1 = loadFrames(this.pathFruit);
		this.imgfruit = new ImageIcon(temp1).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
	}

	/**
	 * initail the image og up level
	 */
	private void initImgUpLevel() {
		BufferedImage temp1 = loadFrames(this.pathUpLevel);
		imgUpLevel = new ImageIcon(temp1).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
	}

}
