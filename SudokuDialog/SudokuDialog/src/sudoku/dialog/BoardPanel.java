/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * CS3331 HW1a - Board.java 
 * DEPENDENCIES: SudokuDialog.java, BoardPanel.java
 * INSTRUCTOR: Dr. Yoonsik Cheon
 * PURPOSE: Generate and solve a Sudoku puzzle using Graphics
 * AUTHOR: Malika S. Ramirez, George Juarez
 * LAST MODIFIED:03/01/2018
 *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

package sudoku.dialog;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import sudoku.model.Board;

/**
 * A special panel class to display a Sudoku board modeled by the
 * {@link sudoku.model.Board} class. You need to write code for
 * the paint() method.
 *
 * @see sudoku.model.Board
 * @author Yoonsik Cheon
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

	public interface ClickListener {

		/** Callback to notify clicking of a square. 
		 * 
		 * @param x 0-based column index of the clicked square
		 * @param y 0-based row index of the clicked square
		 */
		void clicked(int x, int y);
	}

	/** Background color of the board. */
	private static final Color boardColor = new Color(247, 223, 150);

	/** Board to be displayed. */
	private Board board;

	/** Width and height of a square in pixels. */
	private int squareSize;


	/** Create a new board panel to display the given board. */
	public BoardPanel(Board board, ClickListener listener) {
		this.board = board;
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int xy = locateSquaree(e.getX(), e.getY());
				if (xy >= 0) {
					listener.clicked(xy / 100, xy % 100);
					//repaint();
				}
			}
		});
	}

	/** Set the board to be displayed. */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * Given a screen coordinate, return the indexes of the corresponding square
	 * or -1 if there is no square.
	 * The indexes are encoded and returned as x*100 + y, 
	 * where x and y are 0-based column/row indexes.
	 */
	private int locateSquaree(int x, int y) {
		if (x < 0 || x > board.size * squareSize
				|| y < 0 || y > board.size * squareSize) {
			return -1;
		}
		int xx = x / squareSize;
		int yy = y / squareSize;
		return xx * 100 + yy;
	}

	/** Draw the associated board. */
	@Override
	public void paint(Graphics g) {
		super.paint(g); 

		// determine the square size
		Dimension dim = getSize();
		squareSize = Math.min(dim.width, dim.height) / board.size;

		// draw background
		final Color oldColor = g.getColor();
		g.setColor(boardColor);
		g.fillRect(0, 0, squareSize * board.size, squareSize * board.size);

		//Draw squares
		drawSquares(g, dim);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		//Draw grid outline
		drawOutline(g, dim);

		//Draw subgrid
		drawSubgridLines(g, dim);

		//Draw values
		drawValues(g);
	}

	private void drawSquares(Graphics g, Dimension dim) {
		//Draw squares
		g.setColor(Color.GRAY);
		for(int i = 0; i < dim.width; i+=squareSize) {
			for(int j = 0; j < dim.height; j+=squareSize) {
				g.drawLine(i, j, i, squareSize);
				g.drawLine(i, j, squareSize, j);
			}
		}
	}

	private void drawOutline(Graphics g, Dimension dim) {
		//Graphics2D g2 = (Graphics2D) g;
		//g2.setStroke(new BasicStroke(3));
		//Draw grid outline
		//int subGrid = (int) Math.sqrt(board.size);
		g.setColor(Color.BLACK);
		for(int i = 0; i < dim.width; i+=squareSize * board.size) {
			for(int j = 0; j < dim.height; j+=squareSize * board.size) {
				g.drawLine(i, j, i, squareSize * board.size);
				g.drawLine(i, j, squareSize * board.size, j);
			}
		}
	}

	private void drawSubgridLines(Graphics g, Dimension dim) {
		//Draw subgrid
		int subGrid = (int) Math.sqrt(board.size);
		for(int i = 0; i < dim.width; i+=squareSize*subGrid) {
			for(int j = 0; j < dim.height; j+=squareSize*subGrid) {
				g.drawLine(i, j, i, squareSize);
				g.drawLine(i, j, subGrid, j);
			}
		}
	}

	private void drawValues(Graphics g) {
		//Draw values
		Font f = new Font("SANS SERIF", Font.BOLD, squareSize/2);//4:32.5, 9:14.5
		FontMetrics fm = g.getFontMetrics(f);
		g.setFont(f);
		int offset1 = (squareSize/(board.size*8));//4:65/4=16.25, 9:29/9=3
		int offset2 = (squareSize/(board.size*4));
		for(int i = 0; i < board.size; i++) {
			int offsety = (i+1) * (squareSize-offset2);
			for(int j = 0; j < board.size; j++) {
				int offsetx = j * (squareSize+offset1);
				if(board.getValue(i, j) != 0) {
					g.drawString(""+board.getValue(i, j), offsetx+12, offsety-9);
				} 
			}
		}
	}
}
