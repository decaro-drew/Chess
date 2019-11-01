package chess;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import board.Board;
import piece.*;

/**
 * Chess Game
 *
 * @author Jaehyun
 * @author Drew
 */
public class Chess {
	/**
	 * This field is for count movings
	 */
	public static int countMovingWholePieces = 0;

	/**
	 * main method
	 * @param args arguments
	 */
	public static void main(String[] args) {
		String input;

		boolean user = true; //true: white

		Board board = new Board();
		board.print();

		Scanner scan = new Scanner(System.in);

		while(true) {
			String start = null,land = null;
			//White input -------------------------------------------------------
			if (user){
				//get from to strings
				System.out.print("White's move: ");
				input = scan.nextLine();
				System.out.println();
				
				terminate(input,Color.WHITE);

				try{
					start = input.substring(0,2); //used to test the color
				}
				catch (StringIndexOutOfBoundsException e){
					System.out.println("Illegal move, try again");
					user = true;
					continue; //if not continue this while loop. if we keep this using while loop then it doesn't work correctly
				}
				try{
					land =  input.substring(3,5);
				}
				catch (StringIndexOutOfBoundsException e){
					System.out.println("Illegal move, try again");
					user = true;
					continue; //if not continue this while loop. if we keep this using while loop then it doesn't work correctly
				}

				//check valid input
				if(!checkValidInput(board, input, start, land, Color.WHITE)) {
					user = true;
					continue; //if not continue this while loop. if we keep this using while loop then it doesn't work correctly
					// so I changed this.
				}else{

				}

				//check to see if user is trying to do promotion:
				char promote;
				if(input.length() == 7) {
					promote = input.charAt(6);
				}
				else
					promote = ' ';

				//******* call movingPiece method here  *******
//				board.movingPiece(board.makePoint(start), board.makePoint(land));

				if(!board.movingPiece(makePoint(start), makePoint(land), promote)){
					user = true;
					continue;
				}else {
					countMovingWholePieces++;
				}

				board.print();
				user = false;
			}
			//black input --------------------------------------------------------
			else{
				System.out.print("Black's move: ");
				input = scan.nextLine();
				System.out.println();

				terminate(input,Color.BLACK);

				try{
					start = input.substring(0,2); //used to test the color
				}
				catch (StringIndexOutOfBoundsException e){
					System.out.println("Illegal move, try again");
					user = false;
					continue; //if not continue this while loop. if we keep this using while loop then it doesn't work correctly
				}
				try{
					land =  input.substring(3,5);
				}
				catch (StringIndexOutOfBoundsException e){
					System.out.println("Illegal move, try again");
					user = false;
					continue; //if not continue this while loop. if we keep this using while loop then it doesn't work correctly
				}


				if(!checkValidInput(board, input, start, land, Color.BLACK)) {
					user = false;
					continue; //if not continue this while loop. if we keep this using while loop then it doesn't work correctly
					// so I changed this.
				}else{


				}
				char promote;
				if(input.length() == 7) {
					promote = input.charAt(6);
				}
				else
					promote = ' ';

				//******* call movingPiece method here  *******
//				board.movingPiece(board.makePoint(start), board.makePoint(land));
				if(!board.movingPiece(makePoint(start), makePoint(land), promote)){
					user = false;
					continue;
				}else {
					countMovingWholePieces++;
				}

				board.print();
				user = true;
			}
		}
	}

	//Helper Methods
	/**
	 * To determine which options are terminated program.
	 * @param input input from users and determine resign/draw
	 * @param color to determine which color's input
	 */
	private static void terminate(String input, Color color){
		if(input.equals("resign")){
			System.out.println("resign");
			if(color == Color.BLACK)
				System.out.println("White wins");
			else
				System.out.println("Black wins");

		}
		else if(input.equals("draw")){
			System.out.println("draw");
		}
		else
			return;
		System.exit(0);

	}

	/**
	 * Make Point row and column to set pieces' location.
	 *
	 * @param sub this is tokenized string.
	 * @return make new Point instance and return
	 */
	public static Point makePoint(String sub){
		int snum = 0;
		try{
			snum  = Integer.parseInt(String.valueOf(sub.charAt(1)));
		}catch (NumberFormatException e){
			System.out.println("Illegal move, try again");
		}

		int col = Chess.validChar(sub.charAt(0));
		int row = Chess.validInt(snum);

		return new Point(row, col);
	}

	/**
	 * This method is for check valid input
	 *
	 * @param board get board from Board class to check color
	 * @param input get input from user to check valid input
	 * @param start This is tokenized string, such as e2
	 * @param land This is tokenized string, such as h1
	 * @param color get color to check correct color.
	 * @return check valid input and color and if they are valid, then return true, else return false.
	 */
	private static boolean checkValidInput(Board board, String input ,String start, String land, Color color){
		Piece[][] checkBoard =  board.getBoard();
		String tmp = input.substring(0,5);


		if(input.endsWith("draw?")){
			if(inputformat(tmp)){
//				isDraw = true;
				return true;
			}
			else{
//				System.out.println("1");
				System.out.println("Illegal move, try again");
				return false;
			}

		}
		else if(!inputformat(input)) {
//			System.out.println("2");
			System.out.println("Illegal move, try again");
			return false;
		}

		if(checkColor(checkBoard, start) != color || checkColor(checkBoard, start) == Color.NULL) {
			System.out.println("Illegal move, try again");
			return false;
		}

		return true;
	}

	/**
	 * This method is for check Color.
	 *
	 * @param checkBoard get board from Board class
	 * @param sub get user input
	 * @return check color and valid color then return color.
	 */
	private static Color checkColor(Piece[][] checkBoard, String sub) {

		int snum = 0;
		try{
			snum  = Integer.parseInt(String.valueOf(sub.charAt(1)));
		}catch (NumberFormatException e){
			System.out.println("Illegal move, try again");
		}

		int col = Chess.validChar(sub.charAt(0));
		int row = Chess.validInt(snum);

		Piece p = checkBoard[row][col];

		if(p == null) {
			//System.out.println("empty");
			return Color.NULL;
		}

		return p.getColor();
	}

	//returns true if input format is correct
	/**
	 * This method is for checking input format
	 *
	 * @param string get input that user entered
	 * @return return true or false after check string.
	 */
	private static boolean inputformat(String string) {
		if(string.length() < 5 || string.charAt(2) != ' ') {

			return false;
		}
		for(int i =0; i<5; i++) {
			if(i == 0 || i == 3) {
				if(validChar(string.charAt(i)) == -1) {
					return false;
				}
			}

			if(i == 1 || i == 4) {
				int a = 0;
				try{
					a = Integer.parseInt(String.valueOf(string.charAt(i)));
				} catch (NumberFormatException e){
					System.out.println("Illegal move, try again");
				}

				if(validInt(a) == -1) {
					return false;
				}
			}
		}
		return true;
	}

	//below two methods are also used at Board, so I set public
	/**
	 * This method is for input handling. User input character but to handle to board which is double array, need to use index number
	 *
	 * @param c get character
	 * @return return integer which match character index.
	 */
	public static int validChar(char c) {
		String ch = "abcdefgh";
		int col = ch.indexOf(c);
//    	System.out.println(col);

		if(col > -1)
			return col;
		else
			return -1; //if input was invalid
	}

	//returns the int value we can use to find a piece's row
	/**
	 * This method is for input handling. User input number but opposite with double array.
	 *
	 * @param a get interger which tokenizer number from user input
	 * @return return integer after opposite process.
	 */
	public static int validInt(int a) {
		if(a < 1 || a > 8) {
			return -1;
		}
		else {
			return (8 - a);
		}
	}
	//public boolean checkPromotion(Piece [][] board)
}