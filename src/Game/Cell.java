package Game;


import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Cell {
	/**
	 * reprsent cell in the board 25*25
	 */
	private final int CELL = 25;
	private char type;
	private int x, y;
	private int xPiexel;
	private int yPiexel;
	private Color colorSelect;

	/**
	 * Constructor -create new cell
	 *
	 */
	public Cell(int x, int y, char type, Color color){
		this.colorSelect = color;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public void setColorSelect(Color color) {
		this.colorSelect = color;
	}

	public void setType(char type) {
		this.type = type;
	}

	/**
	 * Gets the type
	 *
	 */
	public char getType() {
		return type;
	}

	/**
	 * Draw the cell
	 *
	 */
	public void drawBackground(Graphics g) {

		switch (type) {
		case 'e': // corral exit
			g.setColor(Color.WHITE);
			this.xPiexel = x * CELL;
			this.yPiexel = y * CELL + CELL / 2 - 10;
			g.fillRect(xPiexel, yPiexel + 10, CELL, 1);

			break;

		case 'h': // horizontal line
			g.setColor(colorSelect);
			g.fillRect(x * CELL, y * CELL + CELL / 2 - 1, CELL, 3);
			this.xPiexel = x * CELL;
			this.yPiexel = y * CELL + CELL / 2 - 1;

			break;

		case 'v': // vertical line
			g.setColor(colorSelect);
			g.fillRect(x * CELL + CELL / 2 - 1, y * CELL, 3, CELL);
			this.xPiexel = x * CELL + CELL / 2 - 1;
			this.yPiexel = y * CELL;

			break;

		case '1': // northeast corner
			this.xPiexel = x * CELL - CELL / 2;
			this.yPiexel = y * CELL + CELL / 2;
			drawCorner(g, this.xPiexel, this.yPiexel);

			break;

		case '2': // northwest corner
			this.xPiexel = x * CELL + CELL / 2;
			this.yPiexel = y * CELL + CELL / 2;
			drawCorner(g, this.xPiexel, this.yPiexel);

			break;

		case '3': // southeast corner
			this.xPiexel = x * CELL - CELL / 2;
			this.yPiexel = y * CELL - CELL / 2;
			drawCorner(g, this.xPiexel, this.yPiexel);

			break;

		case '4': // southwest corner
			this.xPiexel = x * CELL + CELL / 2;
			this.yPiexel = y * CELL - CELL / 2;
			drawCorner(g, this.xPiexel, this.yPiexel);

			break;

		case 'o':
			break; // empty navigable cell

		case 'd': // navigable cell with pill
			g.setColor(Color.WHITE);
			g.fillRect(x * CELL + CELL / 2 - 1, y * CELL + CELL / 2 - 1, 3, 3);
			this.xPiexel = x * CELL + CELL / 2 - 1;
			this.yPiexel = y * CELL + CELL / 2 - 1;
			break;

		case 'p': // navigable cell with power pellet
			g.setColor(Color.RED);
			g.fillOval(x * CELL + CELL / 2 - 7, y * CELL + CELL / 2 - 7, 13, 13);
			this.xPiexel = x * CELL + CELL / 2 - 7;
			this.yPiexel = y * CELL + CELL / 2 - 7;
			break;

		case 'x':
			g.clearRect(this.xPiexel, this.yPiexel, 25, 25);
			g.setColor(Color.black);
			g.fillRect(this.xPiexel, this.yPiexel, 25, 25);// empty non-navigable cell
			break;
		default:
			break;
		}
	}

	/**
	 * Draw 3px rounded corner
	 *
	 */
	private void drawCorner(Graphics g, int xBase, int yBase) {
		Graphics2D g2 = (Graphics2D) g;
		Rectangle oldClip = g.getClipBounds();

		g2.setClip(x * CELL, y * CELL, CELL, CELL);
		g2.setColor(colorSelect);

		Shape oval = new Ellipse2D.Double(xBase, yBase, CELL, CELL);

		g2.setStroke(new BasicStroke(3));
		g2.draw(oval);
		g2.setClip(oldClip);
	}

	public int getxPiexel() {
		return xPiexel;
	}

	public void setxPiexel(int xPiexel) {
		this.xPiexel = xPiexel;
	}

	public int getyPiexel() {
		return yPiexel;
	}

	public void setyPiexel(int yPiexel) {
		this.yPiexel = yPiexel;
	}

}
