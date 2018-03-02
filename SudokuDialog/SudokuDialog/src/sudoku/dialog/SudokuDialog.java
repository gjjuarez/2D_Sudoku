/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * CS3331 HW1a - Board.java 
 * DEPENDENCIES: SudokuDialog.java, BoardPanel.java
 * INSTRUCTOR: Dr. Yoonsik Cheon
 * PURPOSE: Generate and solve a Sudoku puzzle using Graphics
 * AUTHOR: Malika S. Ramirez, George Juarez
 * LAST MODIFIED: 03/01/2018
 *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

package sudoku.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;

import java.applet.*;
import java.net.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sudoku.model.Board;


/**
 * A dialog template for playing simple Sudoku games.
 * You need to write code for three callback methods:
 * newClicked(int), numberClicked(int) and boardClicked(int,int).
 *
 * @author Yoonsik Cheon
 */
@SuppressWarnings("serial")
public class SudokuDialog extends JFrame {

	/** Default dimension of the dialog. */
	private final static Dimension DEFAULT_SIZE = new Dimension(310, 430);

	private final static String IMAGE_DIR = "/image/";

	/** Sudoku board. */
	private Board board;

	/** Special panel to display a Sudoku board. */
	private BoardPanel boardPanel;

	/** Message bar to display various messages. */
	private JLabel msgBar = new JLabel("");

	/** Create a new dialog. */
	public SudokuDialog() {
		this(DEFAULT_SIZE);
	}

	public int v = 0;
	public int xlocation = 0;
	public int ylocation = 0;

	/** Create a new dialog of the given screen dimension. */
	public SudokuDialog(Dimension dim) {
		super("Sudoku");
		setSize(dim);
		board = new Board(9);
		boardPanel = new BoardPanel(board, this::boardClicked);
		configureUI();
		//setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		//setResizable(false);
	}

	public SudokuDialog(Dimension dim, int size) {
		super("Sudoku");
		setSize(dim);
		board = new Board(size);
		boardPanel = new BoardPanel(board, this::boardClicked);
		configureUI();
		//setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		//setResizable(false);
	}
	//TODO arraylist
	//TODO switch boardclicked and numberclicked
	//TODO highlight square with rectangles?
	/**
	 * Callback to be invoked when a square of the board is clicked.
	 * @param x 0-based row index of the clicked square.
	 * @param y 0-based column index of the clicked square.
	 */
	private void boardClicked(int x, int y) {
		//just show x and y coordinates and showmessage
		if(v==0) {
			board.placeValue(y, x, y);
			boardPanel.repaint();
		} else
		//if(!board.isSolved(board.getBoardGame())) {
		if(v!=0)
			if(Board.verifyValues(board.getBoardGame(), v, y, x)) {
				board.placeValue(y, x, v);
				playSetSound();
				//setboard?
				boardPanel.repaint();
				if(!board.isSolved(board.getBoardGame())) {
					//testing purposes
					//board.displayPuzzle(board.getBoardGame());
					showMessage(String.format("Board clicked: x = %d, y = %d",  x, y));
				} else {
					playWinSound();
					showMessage("Congratulations! You solved the puzzle!");
				}
			} else {
				playErrorSound();
				showMessage(String.format("Invalid placement at (%d,%d). Try again.", x, y));
			}
	}

	/**
	 * Callback to be invoked when a number button is clicked.
	 * @param number Clicked number (1-9), or 0 for "X".
	 */
	private void numberClicked(int number) {

		v = number;
		board.setValue(v);
		//System.out.println(v); //testing purposes
		showMessage("Number clicked: " + number);
	}

	/**
	 * Callback to be invoked when a new button is clicked.
	 * If the current game is over, start a new game of the given size;
	 * otherwise, prompt the user for a confirmation and then proceed
	 * accordingly.
	 * @param size Requested puzzle size, either 4 or 9.
	 */
	private void newClicked(int size) {

		boolean startNewGame = restartNewGame(); 
		if(startNewGame==true) {
			dispose();
			new SudokuDialog(DEFAULT_SIZE, size);
			board = new Board(size);

			//testing purposes
			//for(int i = 0; i < board.getBoardSize(); i++) {
			//	for(int j = 0; j < board.getBoardSize(); j++) {
			//System.out.print(board.getBoardGame()[i][j] + " "); 
			//	}
			//System.out.println();
			//}
		}
		//System.out.println();
		showMessage("New clicked: " + size);
	}


	/**
	 * Display the given string in the message bar.
	 * @param msg Message to be displayed.
	 */
	private void showMessage(String msg) {
		msgBar.setText(msg);
	}

	/** Configure the UI. */
	private void configureUI() {
		setIconImage(createImageIcon("sudoku.png").getImage());
		setLayout(new BorderLayout());

		JPanel buttons = makeControlPanel();
		// boarder: top, left, bottom, right
		buttons.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
		add(buttons, BorderLayout.NORTH);

		JPanel board = new JPanel();
		board.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
		board.setLayout(new GridLayout(1,1));
		board.add(boardPanel);
		add(board, BorderLayout.CENTER);

		msgBar.setBorder(BorderFactory.createEmptyBorder(10,16,10,0));
		add(msgBar, BorderLayout.SOUTH);
	}

	/** Create a control panel consisting of new and number buttons. */
	private JPanel makeControlPanel() {
		JPanel newButtons = new JPanel(new FlowLayout());
		JButton new4Button = new JButton("New (4x4)");
		for (JButton button: new JButton[] { new4Button, new JButton("New (9x9)") }) {
			button.setFocusPainted(false);
			button.addActionListener(e -> {
				newClicked(e.getSource() == new4Button ? 4 : 9);
			});
			newButtons.add(button);
		}
		newButtons.setAlignmentX(LEFT_ALIGNMENT);

		// buttons labeled 1, 2, ..., 9, and X.
		JPanel numberButtons = new JPanel(new FlowLayout());
		int maxNumber = board.getBoardSize() + 1;
		for (int i = 1; i <= maxNumber; i++) {
			int number = i % maxNumber;
			JButton button = new JButton(number == 0 ? "X" : String.valueOf(number));
			button.setFocusPainted(false);
			button.setMargin(new Insets(0,2,0,2));
			button.addActionListener(e -> numberClicked(number));
			numberButtons.add(button);
		}
		numberButtons.setAlignmentX(LEFT_ALIGNMENT);

		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		content.add(newButtons);
		content.add(numberButtons);
		return content;
	}

	/** Create an image icon from the given image file. */
	private ImageIcon createImageIcon(String filename) {
		URL imageUrl = getClass().getResource(IMAGE_DIR + filename);
		if (imageUrl != null) {
			return new ImageIcon(imageUrl);
		}
		return null;
	}

	/**
	 * Restart a new game when user presses New buttons
	 * @return boolean
	 */
	private boolean restartNewGame() {
		playPopupSound();
		boolean startNewGame = false;
		int answer = JOptionPane.showConfirmDialog(null, "Do you want to play a new game?", "New Game?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(answer==0)  {
			startNewGame = true;
			playNewGameSound();
		}
		if(answer==1)
			startNewGame = false;
		return startNewGame; 	
	}

	//public int getValue() {
	//	return v;
	//}

	/**
	 * Play sound for solving puzzle
	 */
	private void playWinSound() {
		try {
			//URL url = getClass().getResource("/sound/win.wav");
			URL url = getClass().getResource("/sound/fanfare_x.wav");
			AudioInputStream as = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(as);
			clip.start();
		} catch (MalformedURLException mal) {
			System.out.println(mal);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play sound for placing values on the puzzle
	 */
	private void playSetSound() {
		try {
			//	URL url = getClass().getResource("/sound/floop2_x.wav");
			URL url = getClass().getResource("/sound/click_x.wav");
			AudioInputStream as = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(as);
			clip.start();
		} catch (MalformedURLException mal) {
			System.out.println(mal);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play sound for incorrect placement of values in the puzzle
	 */
	private void playErrorSound() {
		try {
			//URL url = getClass().getResource("/sound/bad_disk_x.wav");
			URL url = getClass().getResource("/sound/err1.wav");
			AudioInputStream as = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(as);
			clip.start();
		} catch (MalformedURLException mal) {
			System.out.println(mal);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play sound for dialog popup
	 */
	private void playPopupSound() {
		try {
			URL url = getClass().getResource("/sound/err2.wav");
			AudioInputStream as = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(as);
			clip.start();
		} catch (MalformedURLException mal) {
			System.out.println(mal);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play sound when new game is restarted
	 */
	private void playNewGameSound() {
		try {
			URL url = getClass().getResource("/sound/chime.wav");
			AudioInputStream as = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(as);
			clip.start();
		} catch (MalformedURLException mal) {
			System.out.println(mal);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new SudokuDialog();
	}
}
