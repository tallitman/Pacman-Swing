package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.JTableHeader;

@SuppressWarnings("serial")
public class Summary extends JPanel implements ActionListener {
	/**
	 * display the summary of the game :table with all the score in the game and in addtion the curently score of the playes
	 * enable to player save him score and see the palce of him in the champion table 
	 */
	public final int WIDETH_WINDOW=400;//the wideth of window
	public final int HEIGHT_WINDOW=400;// the height of the window
	private JLabel lineLbl;// label with icon of line 
	private JLabel wirteLbl;// label with text " wirte yourname
	private JTextField errorLbl;// Text for repesnt errors to user when wirte his name
	private JLabel[] lifeLbl;// label with icon of life
	private JLabel[] plusLbl=new JLabel[2];// label with icon of +
	private JLabel[] mulLbl=new JLabel[2];//label with icon of * 
	private JLabel[] levelLbl;// label with icon of level
	private int score = 0;// the score of the playes  that the game is over for him
	private JLabel scoreLbl;// label with icon of $ , for the score 
	private JLabel sumScoreLbl;// label with icon of $ for the sum score
	private int sumScore=0;// init the sum score before do culcator of him
	private int life=0;//numbers of lifes of player , initial to 0
	private int level=1;// number of level , initial to 1 
	private final String IMG_PATH = "res/";//path to dictornay on images 
	private final String FILE_PATH="Table_Sum.csv";// path to csv of the table 
	private final String PATH_OF_ANIMATION=IMG_PATH+"logo_summary/";//path to animation
	private final String PATH_FOR_CUP=IMG_PATH+"cup_champion.png";//path for the icon of cup 
	private JTable tableJt;// champin table 
	private final String[] columnNames={"rank","Name","Score"};// The column names of the table
	private Object[][] data;// the fata in the table
	private final int BOUNS_LEVEL=500;//bouns level
	private final int BOUNS_LIFE=1000;//bouns life
	private Timer t;//timer 
	private int numberRow=0;//number of row when load the csv file
	private JScrollPane scrollPane;// the table find on this scroll
	private JTextField jt;// field that the player can wirte is name 
	private JButton saveButton;// butoon to save in the table 
	private String name=""; // the name of the player ,initial to ""
	private JLabel titleScore;// the title of this panel 
	private final Color COLOR_BACGROUND =new Color(34, 49, 63);// the color of this backgrond 
	private int changeAnimation = 0;// for know the firme of animation need to display 
	protected BufferedImage[] framesForAnimation;// for load the image of animation 
	private JLabel labelForAnimation;// for display the currntly frame 
	private JButton close;//for button close 
	private int rowPlayerInTable=-1;// for know if save the player in table or not ,initial to -1 
	private Image imgeCup;//the image of cup 
	private JLabel cupLabel;// the label of image of cup 

/**
 * counstructor -initial the table champion and calcultor the sum score of player 
 * @param level-the level that the player arriave 
 * @param score-the score that the player arriave 
 * @param life-the life that have to player 
 */
	public Summary (int level , int score , int life){
		setLayout(null);// for alow the use setborder in this panel
		this.setBackground(COLOR_BACGROUND);// define the color of the background in this window
		setBounds(200,200,WIDETH_WINDOW,HEIGHT_WINDOW);// define the size of the window
		this.level=level;
		this.score=score;
		this.life=life;
		initAnimation();// initial the animation
		culSumScore();// Calculates the sum score
		initSumScore();//displays the calculation
		initSave();// inital the button save and the text field 
		initTable();//initial the table 
		initClose();//initial the close button but dont add him 
		initImgCup();// initial the cup img but dont display him 
		setVisible(true);// allow to visible this panel
		// timer initial
		t = new Timer(20, this);//initial the timer
		t.start();//start the timer 
	}
	/**
	 * initial the label on the icon cup
	 */
	private void initImgCup() {
		BufferedImage temp1 = loadFrames(this.PATH_FOR_CUP);
		this.imgeCup = new ImageIcon(temp1).getImage().getScaledInstance(230, 250, Image.SCALE_SMOOTH);
		this.cupLabel=new JLabel();
		this.cupLabel.setIcon(new ImageIcon( this.imgeCup));
		this.cupLabel.setBounds(0, 50, 230, 250);
		
	}
	/**
	 * load image that find in the the path 
	 * @param path- from where to load the image
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
	 * initial the boutton of close but dont add him
	 */
	private void initClose(){
		close=new JButton("Close");
		close.setBounds(HEIGHT_WINDOW-150,230,100,30);
		close.addActionListener(this);
	}
	/**
	 * init the label of the culctor of sum score
	 */
	private void  initSumScore(){
		//your score-label
		titleScore= initJabel (50,0,"Your Score :", Color.red);
		add(titleScore);

		//score - the user get in the game
		scoreLbl= initJabel (50,30,score + "", Color.blue);
		add(scoreLbl);
		ImageIcon icon = new ImageIcon(
				new ImageIcon(IMG_PATH + "score" + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		scoreLbl.setIcon(icon);

		// label of +
		plusLbl [0]= initJabel (65,60,"", Color.BLUE);
		add(plusLbl [0]);
		plusLbl [1]= initJabel (65,115,"", Color.BLUE);
		add(plusLbl [1]);
		icon = new ImageIcon(
				new ImageIcon(IMG_PATH + "plus" + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		plusLbl [1].setIcon(icon);
		plusLbl [0].setIcon(icon);

		//label of *
		mulLbl [0]= initJabel (65,90,this.BOUNS_LIFE+"", Color.BLUE);
		add(mulLbl [0]);
		mulLbl [1]= initJabel (65,145,this.BOUNS_LEVEL+"", Color.BLUE);
		add(mulLbl [1]);
		icon = new ImageIcon(
				new ImageIcon(IMG_PATH + "multiply" + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		mulLbl [1].setIcon(icon);
		mulLbl [0].setIcon(icon);

		//label of life
		icon = new ImageIcon(new ImageIcon(IMG_PATH + "pacmanlives" + ".png").getImage().getScaledInstance(20,
				20, Image.SCALE_SMOOTH));
		lifeLbl = new JLabel[3];
		for (int i = 0; i < life; i++){
			lifeLbl[i] = new JLabel();
			lifeLbl[i].setIcon(icon);
			lifeLbl[i].setBounds(40-20*i, 95, 20, 20);
			add(lifeLbl[i]);
		}
		//label of loss life work when the life list from 3 
		icon = new ImageIcon(new ImageIcon(IMG_PATH + "pacmandie" + ".png").getImage().getScaledInstance(20,
				20, Image.SCALE_SMOOTH));
		for (int i = life; i < 3; i++){
			lifeLbl[i] = new JLabel();
			lifeLbl[i].setIcon(icon);
			lifeLbl[i].setBounds(40-20*i, 95, 20, 20);
			add(lifeLbl[i]);
		}
		//label of level
		levelLbl=new JLabel[level];
		icon = new ImageIcon(new ImageIcon(IMG_PATH + "level" + ".png").getImage().getScaledInstance(20,
				20, Image.SCALE_SMOOTH));
		for (int i = 0; i < level; i++){
			levelLbl[i] = new JLabel();
			levelLbl[i].setIcon(icon);
			levelLbl[i].setBounds(40-20*i, 150, 20, 20);
			add(levelLbl[i]);
		}
		//*
		
		// line 
		lineLbl=new JLabel();
		icon = new ImageIcon(
				new ImageIcon(IMG_PATH + "lineBlack" + ".png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH));
		lineLbl.setIcon(icon);
		add(lineLbl);
		lineLbl.setBounds(20, 175, 150, 30);
		// sum score
		this.sumScoreLbl=new JLabel(this.sumScore+"");
		add(sumScoreLbl);
		sumScoreLbl.setBounds(60, 190, 200, 30);
		sumScoreLbl.setFont(new Font("Jokerman", Font.PLAIN, 15));
		sumScoreLbl.setForeground(Color.red);

	}
	/**
	 * help function to init Jabel 
	 * @param x - location on axis x
	 * @param y-loction on axis y
	 * @param nubmer- the number of point that display in this label
	 * @param color- the color of ther font 
	 * @return label with the request 
	 */
	private JLabel initJabel (int x,int y, String nubmer, Color color){
		JLabel jLabel= new JLabel();
		jLabel.setBounds(x, y, 100, 30);
		jLabel.setForeground(color);		
		jLabel.setText(nubmer);
		jLabel.setFont(new Font("Jokerman", Font.PLAIN, 15));
		return jLabel;
	}
	/**
	 * initail all the part of the save name in table 
	 */
	private void initSave(){
		//Write your name
		wirteLbl =new JLabel("Wirte Your Name");
		add(wirteLbl);
		wirteLbl.setBounds(20, 220, 200, 30);
		wirteLbl.setFont(new Font("Jokerman", Font.PLAIN, 15));
		wirteLbl.setForeground(Color.red);
		// text field
		jt=new JTextField(10);
		jt.setBounds(0, 270, 100, 30);
		add(jt);
		saveButton=new JButton("save");
		add(saveButton);
		saveButton.setBounds(100,270,100,30);
		saveButton.addActionListener(this);
		// error text
		errorLbl =new JTextField(10);
		add(errorLbl);
		errorLbl.setBounds(0, 310, 230, 30);
		errorLbl.setEditable(false);
		errorLbl.setBackground(this.COLOR_BACGROUND);
		errorLbl.setForeground(Color.RED);
		errorLbl.setBorder(null);
		errorLbl.setSelectedTextColor(Color.red);

	}
	/**
	 * initial the animation 
	 */
	private void initAnimation() {
		framesForAnimation = new BufferedImage[20];
		loadFrames();
		Image temp = new ImageIcon(framesForAnimation[changeAnimation]).getImage().getScaledInstance(150,
				150, Image.SCALE_SMOOTH);
		labelForAnimation = new JLabel(new ImageIcon(temp));
		add(labelForAnimation);
		labelForAnimation.setBounds(WIDETH_WINDOW-170, HEIGHT_WINDOW-200, 150, 150);
	}
	/**
	 * update the animation 
	 */
	private void updateAnimation() {
		changeAnimation = (changeAnimation + 1) % 20;
	}
	/**
	 * load frames for animation 
	 */
	protected void loadFrames() {
		for (int i = 0; i < 20; i++) {
			String path = PATH_OF_ANIMATION + (i) + ".gif";
			File file = new File(path);
			try {
				framesForAnimation[i] = ImageIO.read(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * do all the operation of butoons and timer 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton){// save the name and disaply hin in the table 
			if (jt.getText().length()>0 && jt.getText().length()<8 && jt.getText().trim().length()==jt.getText().length() && !jt.getText().contains(",") ){
				this.t.stop();
				name=jt.getText();
				addNewLine();
				saveInFile();
				setVisible(false); //you can't see me!
				this.removeAll();	
				numberRow=0;
				initTable();
				add(close);
				add(this.cupLabel);
				titleScore= initJabel (this.WIDETH_WINDOW/2,this.HEIGHT_WINDOW-100,"Table Champion", Color.red);
				titleScore.setBounds(this.WIDETH_WINDOW/2, this.HEIGHT_WINDOW-100, 200, 30);
				add(titleScore);
				if ( rowPlayerInTable!=-1){
					//// need color the rowPlayInTable in jTable 
				}
				setVisible(true);
				
				}
			else// if have error when wirth the name and click on save 
			{
				errorLbl.setText("To 6 charters,without space or , ");
			}
		}
		if (e.getSource() == t) {// update the frame when timer say 
			Image temp = new ImageIcon(framesForAnimation[changeAnimation]).getImage().getScaledInstance(200,
					200, Image.SCALE_SMOOTH);
			labelForAnimation.setIcon(new ImageIcon(temp));
			updateAnimation();
		}
		if (e.getSource()==close) {// if close botton click 
			this.setVisible(false);
		}

	}
	/**
	 * init the table form csv file 
	 */
	private  void initTable(){
		data=new Object[10][3];
		loadFile();
		for (;numberRow<10;numberRow++){
			data[numberRow][0]=numberRow+1;
			data[numberRow][1]="none";
			data[numberRow][2]=0;
		}
		tableJt=new JTable(data,columnNames);
		tableJt.setFont(new Font("Jokerman", Font.PLAIN, 15));
		tableJt.setForeground(Color.blue);
		JTableHeader theader=tableJt.getTableHeader();
		theader.setBackground(Color.RED);
		theader.setForeground(Color.WHITE);
		theader.setFont(new Font("Jokerman", Font.PLAIN, 15));
		tableJt.setFillsViewportHeight(true);
		tableJt.setDefaultEditor(Object.class, null);
		scrollPane = new JScrollPane(tableJt);
		tableJt.setFillsViewportHeight(true);
		tableJt.setDefaultEditor(Object.class, null);
		scrollPane.setPreferredSize(new Dimension(300,192));
		tableJt.getTableHeader().setReorderingAllowed(false);
		add(scrollPane);
		scrollPane.setBounds(HEIGHT_WINDOW-200,0,200,192);

	}
	/**
	 * add new name to the table if him in the 10 top 
	 */
	private void addNewLine() {
		culSumScore();
		Object []temp=new Object[3];
		temp[1]=name;
		temp[2]=sumScore;
		if (Integer.parseInt(data[9][2].toString()) <Integer.parseInt(temp[2].toString())){
			data[9][2]=(Object)temp[2];
			data[9][1]=(Object)temp[1];
			rowPlayerInTable=9;
			int key=Integer.parseInt(data[8][2].toString());
			while(rowPlayerInTable-1>=0 && Integer.parseInt(data[rowPlayerInTable][2].toString())>key){
				Object tName=data[rowPlayerInTable][1];
				data[rowPlayerInTable][1]=data[rowPlayerInTable-1][1];
				data[rowPlayerInTable-1][1]=tName;
				Object tScore=data[rowPlayerInTable][2];
				data[rowPlayerInTable][2]=data[rowPlayerInTable-1][2];
				data[rowPlayerInTable-1][2]=tScore;
				rowPlayerInTable--;
				if(rowPlayerInTable-1>=0)
					key=Integer.parseInt(data[rowPlayerInTable-1][2].toString());

			}
		}
	}
	/**
	 *  calculation the sum score with the bounses
	 */
	private void culSumScore(){
		sumScore=score+life*BOUNS_LIFE+level*BOUNS_LEVEL;
	}
	/**
	 * load the csv file 
	 */
	private void loadFile(){
		File file = new File(FILE_PATH);
		try {
			Scanner inputStream = new Scanner(file);
			while (inputStream.hasNext()) {
				numberRow++;
				String dataIn = inputStream.next();
				//Integer n = Integer.parseInt(data);
				String[] values = dataIn.split(",");
				data[numberRow-1][0]=values[0];
				data[numberRow-1][1]=values[1];
				data[numberRow-1][2]= values[2];
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	/**
	 * save in file new row 
	 */
	private void saveInFile(){
		try {
			PrintWriter pw = new PrintWriter(new File(FILE_PATH));
			StringBuilder sb = new StringBuilder();
			for (int i=0;i<data.length;i++){
				if (i>0)
					sb.append("\r\n");
				sb.append(data[i][0]);
				sb.append(',');
				sb.append((String)data[i][1]);
				sb.append(',');
				sb.append(data[i][2]);
			}
			pw.write(sb.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
