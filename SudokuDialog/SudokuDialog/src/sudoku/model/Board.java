/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * CS3331 HW1a - Board.java 
 * DEPENDENCIES: SudokuDialog.java, BoardPanel.java
 * INSTRUCTOR: Dr. Yoonsik Cheon
 * PURPOSE: Generate and solve a Sudoku puzzle using Graphics
 * AUTHOR: Malika S. Ramirez, George Juarez
 * LAST MODIFIED: 03/01/2018
 *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

package sudoku.model;

/** An abstraction of Sudoku puzzle. */
public class Board {

	/** Size of this board (number of columns/rows). */
	public final int size;
	private int boardGame[][];
	private int boardValue;

	/** Create a new board of the given size. */
	public Board(int size) {
		this.size = size;
		//this.setBoardGame(new int[size][size]);
		this.boardGame = new int[size][size];
	}

	/** Return the size of this board. */
	public int getBoardSize() {
		return size;
	}

	/*
	 * Places the values entered by player
	 */
	/**
	 * places values in boardGame array
	 * @param y y-axis
	 * @param x x-axis
	 * @param v value
	 */
	public void placeValue(int y, int x, int v) {
		boardGame[y][x]=v;
	}

	/**
	 * Sets value to a global variable in SudokuDialog
	 * @param boardValue
	 */
	public void setValue(int boardValue) {
		this.boardValue= boardValue;
	}

	//public int getValue() {
	//	return boardValue;
	//}

	/**
	 * get the values from the boardGame array
	 * @param y y-axis
	 * @param x x-axis
	 * @return boardGame array
	 */
	public int getValue(int y, int x) {
		return boardGame[y][x];
	}


	/**
	 * Creates an empty puzzle
	 */
	public int[][] createBoard() { 
		//int[][]a = new int[n][n];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++)
				boardGame[i][j] = 0;
			//System.out.print("-");
		}
		return boardGame;
	}

	/**
	 * Creates and empty puzzle with size
	 * @param size int size of puzzle
	 * @return boardGame array
	 */
	public int[][] createBoard(int size) { 
		//int[][]a = new int[n][n];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++)
				boardGame[i][j] = 0;
			//System.out.print("-");
		}
		return boardGame;
	}

	/**
	 * @return the boardGame
	 */
	public int[][] getBoardGame() {
		return boardGame;
	}

	/**
	 * @param boardGame the boardGame to set
	 */
	public void setBoardGame(int boardGame[][]) {
		this.boardGame = boardGame;
	}

	/**
	 * Displays the puzzle progress
	 * @param A boardGame array
	 */
	public void displayPuzzle(int[][]A) {
		int size = A.length;
		int sqrt = (int) Math.sqrt(size);
		int count = 0;
		int count2 = 0;
		// Print top line
		//System.out.print("y/x");
		int p = 1;
		for(int m = 0; m < sqrt; m++) {
			for(int k = 0;k < sqrt; k++) {
				System.out.printf("%3s", p);
				p++;
			}
			System.out.print("   ");
		}

		System.out.println();
		System.out.printf("%2s "," ");
		for(int k = 0; k <=size*3.8; k++) // n*3.8 approximate length of grid
			System.out.print("="); 
		//System.out.printf("%2s ","=");
		System.out.println();

		// Print puzzle
		for(int i = 0; i < A.length; i++) {
			System.out.print(i+1 + " | "); //y axis numbers
			for(int j = 0; j < A.length; j++) {
				if(A[i][j] != 0)
					System.out.printf("%2d ", A[i][j]);
				//System.out.print("|");
				else
					System.out.print(" - "); // Print dashes instead of zeros for easier legibility

				// Print right line
				count++;
				if(count == Math.sqrt(size)) {
					System.out.print(" | "); 
					count = 0;
				} 
			}// end j

			// Print bottom line
			count2++;
			if(count2 == Math.sqrt(size)) {
				System.out.println();
				System.out.printf("%2s "," ");
				for(int k = 0; k <=size*3.8; k++) // n*3.8 approximate length of grid
					System.out.print("="); 
				count2 = 0;
			}
			System.out.println();
		}// end i
	}

	/**
	 * Determines if the puzzle is solved
	 * @param a boardGame array
	 * @return boolean
	 */
	public boolean isSolved(int[][] a) {
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a.length; j++)
				if (a[i][j] < 1 || a[i][j] > a.length)
					return false;
		return true; // The solution is valid
	}

	/**
	 * Verifies if the value placed in the row is valid
	 * @param a boardGame array
	 * @param v int value
	 * @param y int y-axis
	 * @return boolean
	 */
	public static boolean verifyRow(int[][] a, int v, int y) {
		//y is row
		for(int r = 0; r < a.length; r++){ //a[y].length
			if(a[y][r] == v) {
				//System.out.println("Invalid row entry."); //tesing purposes
				return true;
			}
		} 
		return false;
	}

	/**
	 * Verifies if the value placed in the column is valid
	 * @param a boardGame array
	 * @param v int value
	 * @param x int x-axis column
	 * @return boolean
	 */
	public static boolean verifyColumn(int[][] a, int v, int x) {
		//x is col
		for(int c = 0; c < a.length; c++){
			if(a[c][x] == v) {
				//System.out.println("Invalid column entry."); //tesing purposes
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifies if the subgrid is valid
	 * @param a boardGame array
	 * @param v int value
	 * @param y int y-axis
	 * @param x int x-axis
	 * @return boolean
	 */
	public static boolean verifySubgrid(int[][] a, int v, int y, int x) {
		int sqrt = (int) Math.sqrt(a.length);
		int r = y-y%sqrt;
		int c = x-x%sqrt;

		for(int i = r; i < r+sqrt; i++) {
			for(int j = c; j < c+sqrt; j++){
				if(a[i][j] == v) {
					//System.out.println("Invalid subgrid entry."); //Tesing purposes
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Verifies all the values at once to determine if the values
	 * entered by the user are valid
	 * @param a boardGame array
	 * @param v int value
	 * @param y int y-axis
	 * @param x int x-axis
	 * @return boolean
	 */
	public static boolean verifyValues(int[][]a, int v, int y, int x) {
		return !(verifyRow(a,v,y) || verifyColumn(a,v,x) || verifySubgrid(a,v,y,x));
	}
}
