package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Game.Game;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 * This class represent the window of choosing a starting maze of a new pacman
 * game.
 */
@SuppressWarnings("serial")
public class SettingsGame extends JFrame implements ActionListener {

	private Map<Integer, List<int[][]>> mapCsv;// for load the board
	private final int widthWindow = 900;// the width of this window
	private final int heightWindow = 780;// the height of the this window
	private final String PATH = "res/";// path to dictionry where all the images
	private final int sizeArray = 32;// in the game all board is 32*32
	private JLabel labelTopicImage;// the topic image of this window , this is the label
	private JLabel selectToStart;// the text "select to start "
	private JButton[] choicePicture;// buttons with board on background , for choice board
	private BufferedImage[] framesForMain;// for load the images of the board
	private final String pathToAnimationMain = PATH + "logo_settings_window/";// the path to animation of this window
	private int changeAnimation = 0;// for change the frame in animtion all time
	private final int numberBoard = 3;// in this game have there board only
	private AudioPlayer MGP = AudioPlayer.player;
	private AudioStream BGM;// for audio in this window
	private AudioData MD;// for audio in this window
	ContinuousAudioDataStream loop = null;// for audio in this window
	Timer timer = new Timer(100, this);// timer for animation

	public SettingsGame() {
		loadCsv(); // for load csv with the level
		initText();// initial the part of the text "select board to start"
		initBoardToChoise();// initial the board that load from csv
		initAnimation();// initial the animation in the part up of the window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// close the window when click on the button "x" in window
		setLayout(null);// for to locate all the things with setborder
		music();// for play the music of this window
		this.setSize(widthWindow, heightWindow);// for defind the size of this window
		this.setVisible(true);// for see this window
		this.setLocationRelativeTo(null); // Appears in center on the display -must be after setVisible
		this.getContentPane().setBackground(new Color(34, 49, 63));// the background color of this window
		timer.start();// start the timer
		this.setResizable(false);// for prevet change the size of this window
	}

	/**
	 * initial the buttons with the images of the board
	 */
	private void initBoardToChoise() {
		choicePicture = new JButton[numberBoard];// array -we put into the images of the boards
		for (int i = 0; i < choicePicture.length; i++) {
			ImageIcon iconBoard = new ImageIcon(new ImageIcon(PATH + "option_" + (i + 1) + ".PNG").getImage()
					.getScaledInstance(300, 300, Image.SCALE_SMOOTH));
			choicePicture[i] = new JButton("option_" + (i + 1), iconBoard);
			add(choicePicture[i]);
			choicePicture[i].addActionListener(this);
			// organnize the size and the places of the buttons
			choicePicture[i].setBounds(0 + i * 300, 450, 300, 300);
			choicePicture[i].setForeground(Color.white);
			choicePicture[i].setBorderPainted(false);
			choicePicture[i].setBorder(null);
			choicePicture[i].setContentAreaFilled(false);
			choicePicture[i].setBounds(0 + i * 300, 450, 302, 302);
		}
	}

	/**
	 * initial the animation: load the images and put the first in the icon of label
	 * of topic image
	 */
	private void initAnimation() {
		framesForMain = new BufferedImage[20];
		loadFrames(pathToAnimationMain, framesForMain);
		Image temp = new ImageIcon(framesForMain[changeAnimation]).getImage().getScaledInstance(widthWindow,
				heightWindow - 330, Image.SCALE_SMOOTH);
		labelTopicImage = new JLabel(new ImageIcon(temp));
		add(labelTopicImage);
		labelTopicImage.setBounds(0, 0, widthWindow, heightWindow - 330);
	}

	/**
	 * initial the text of "select board to start
	 */
	private void initText() {
		selectToStart = new JLabel("Select Board To Start");
		add(selectToStart);
		selectToStart.setBounds(0, 270, 500, 300);
		selectToStart.setFont(new Font("Jokerman", Font.PLAIN, 35));
		selectToStart.setForeground(Color.red);

	}

	/**
	 * Loading a csv file into a map
	 */
	private void loadCsv() {
		mapCsv = new LinkedHashMap<>();
		String fileName = "boards.csv";// path of the file with the boards
		File file = new File(fileName);
		try {
			Scanner inputStream = new Scanner(file);
			while (inputStream.hasNext()) {
				String data = inputStream.next();
				Integer n = Integer.parseInt(data);
				int[][] array = new int[sizeArray][sizeArray];// new array
				if (!mapCsv.containsKey(n))
					mapCsv.put(n, new ArrayList<int[][]>());
				mapCsv.get(n).add(array);
				int numberLine = 0;
				while (numberLine < sizeArray) {
					data = inputStream.next();
					String[] values = data.split(",");
					for (int i = 0; i < sizeArray; i++) {
						array[numberLine][i] = Integer.parseInt(values[i]);
					}
					numberLine++;
				}
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {// update the frame of animation
			Image temp = new ImageIcon(framesForMain[changeAnimation]).getImage().getScaledInstance(widthWindow,
					heightWindow - 330, Image.SCALE_SMOOTH);
			labelTopicImage.setIcon(new ImageIcon(temp));
			updateAnimation();
		}
		if (e.getSource() == choicePicture[0]) {// choice the first board (blue)

			new Game(Color.blue, mapCsv.get(2));
			closeThisWindow();
		}
		if (e.getSource() == choicePicture[1]) {// choice the second board (red)
			new Game(Color.red, mapCsv.get(2));
			closeThisWindow();

		}
		if (e.getSource() == choicePicture[2]) {// chocie the three board (green)
			new Game(Color.GREEN, mapCsv.get(3));
			closeThisWindow();

		}
	}

	/**
	 * the method stop the time, the music , and close this window
	 */
	private void closeThisWindow() {
		MGP.stop(loop);
		timer.stop();
		this.setVisible(false);
		this.dispose();
	}

	/**
	 * change the frame of the animation
	 */
	private void updateAnimation() {
		changeAnimation = (changeAnimation + 1) % 20;
	}

	/**
	 * load 20 frame of image and with this create animation
	 * 
	 * @param pathOfAnmiation
	 * @param framesOfImage
	 */
	protected void loadFrames(String pathOfAnmiation, BufferedImage[] framesOfImage) {
		for (int i = 0; i < 20; i++) {
			String path = pathOfAnmiation + (i) + ".gif";
			File file = new File(path);
			try {
				framesOfImage[i] = ImageIO.read(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * initial the music :load the file audio and play him in loop
	 */
	private void music() {

		try {
			BGM = new AudioStream(new FileInputStream("res/sound/pacman_beginning.wav"));
			MD = BGM.getData();
			loop = new ContinuousAudioDataStream(MD);

		} catch (IOException error) {

		}
		MGP.start(loop);
	}
/**
 * from here the game started ,open the window of game settings
 * @param args
 */
	public static void main(String[] args) {
		new SettingsGame();//from this window sekect the boaed want play with him
	}
}
