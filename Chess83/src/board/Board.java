package board;
import chess.Chess;
import piece.*;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is Board
 *
 * @author Jaehyun
 * @author Drew
 */
public class Board {
	/**
	 * board string indicator for column
	 */
	private static final String BOARD_STRING_INDICATOR = " a  b  c  d  e  f  g  h";

    /**
     * This field is for board. Board is 8x8 size and all type is Piece which is abstract class.
     */
    private Piece[][] board = new Piece[8][8];
    /**
     * This field is for checking white king is checked
     */
    private King wKing = null;
    /**
     * This field is for checking black king is checked
     */
    private King bKing = null;

    /**
     * This field is for getting piece which checking King
     */
    private Point getCheckPiece;
    /**
     * This field is for getting specific object that checking king.
     */
    private Piece getCheckPieceObject;

    /**
     * This is for constructor that initializes board and set pieces on the board.
     */
    public  Board(){
        /*
        This is for testing to make board.
         */
        board[0][0] = new Rook("bR",Color.BLACK);
        board[0][1] = new Knight("bN",Color.BLACK);
        board[0][2] = new Bishop("bB",Color.BLACK);
        board[0][3] = new Queen("bQ",Color.BLACK);
        board[0][4] = new King("bK",Color.BLACK);
        bKing = (King) board[0][4];
        bKing.setKingPosition(0,4);
        board[0][5] = new Bishop("bB",Color.BLACK);
        board[0][6] = new Knight("bN",Color.BLACK);
        board[0][7] = new Rook("bR",Color.BLACK);

        for (int p=0; p<8;p++ )
            board[1][p] = new Pawn("bP",Color.BLACK);

        board[7][0] = new Rook("wR",Color.WHITE);
        board[7][1] = new Knight("wN",Color.WHITE);
        board[7][2] = new Bishop("wB",Color.WHITE);
        board[7][3] = new Queen("wQ",Color.WHITE);
        board[7][4] = new King("wK",Color.WHITE);
        wKing = (King) board[7][4];
        wKing.setKingPosition(7,4);
        board[7][5] = new Bishop("wB",Color.WHITE);
        board[7][6] = new Knight("wN",Color.WHITE);
        board[7][7] = new Rook("wR",Color.WHITE);

        for(int b=0; b<8;b++)
            board[6][b] = new Pawn("wP",Color.WHITE);
    }

    /**
     * This method is for castling.
     *
     * @param board get board to check board
     * @param to get Point instance to check King's position
     */
    private void castling(Piece[][] board, Point to){
        //white king
        if(to.getX() == 7 && to.getY() == 6 && !isCheck()){
            if(board[7][7] instanceof Rook){
                board[7][5] = board[7][7];
                board[7][7] = null;
                //System.out.println("Castling");
            }
        }
        else if(to.getX() == 7 && to.getY() == 2 && !isCheck()){
            if(board[7][0] instanceof Rook){
                board[7][3] = board[7][0];
                board[7][0] = null;
                //System.out.println("Castling");
            }
        }
        //Black King
        if(to.getX() == 0 && to.getY() == 6 && !isCheck()){
            if(board[0][7] instanceof Rook){
                board[0][5] = board[0][7];
                board[0][7] = null;
                //System.out.println("Castling");
            }
        }
        else if(to.getX() == 0 && to.getY() == 2 && !isCheck()){
            if(board[0][0] instanceof Rook){
                board[0][3] = board[0][0];
                board[0][0] = null;
                //System.out.println("Castling");
            }
        }
    }

    /**
     * This method is for checking pieces that can protect King when King is on check and checkmate.
     *
     * @return return boolean if same color pieces can protect their King, then return true else return false.
     */
    /**
     * @return return true if piece can rescue king or return false.
     */
    private boolean isRescueKing(){
        boolean validCheckMate = false;
        outLoop:
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board[i][j] != null){
                    if(wKing.isChecked){
                        if(board[i][j].getColor() == Color.WHITE){
                            if (board[i][j].valid_move(new Point(i,j), getCheckPiece, board)){  // 1. can same color piece can catch to enemy which made check to protect King ?
//                                System.out.println("1");
                                validCheckMate = false;
                                break outLoop;
                            }
                            else{
//                                System.out.println("2");
                                validCheckMate = true;
                            }
                        }
                    }
                    else if(bKing.isChecked){
                        if(board[i][j].getColor() == Color.BLACK){
                            if (board[i][j].valid_move(new Point(i,j), getCheckPiece, board)){
//                                System.out.println("3");
                                validCheckMate = false;
                                break outLoop;
                            }
                            else{
//                                System.out.println("4");
                                validCheckMate = true;
                            }
                        }
                    }
                }

                if( board[i][j] != null){   //2. can same color piece can protect King to move path btw made check piece and king.
                    if(wKing.isChecked) {
                        if (board[i][j].getColor() == Color.WHITE) {
//                            System.out.println(getCheckPieceObject.getPath_pieceToKing());
                            for (Point p : getCheckPieceObject.getPath_pieceToKing()) {
//                                System.out.println(p.getX()+" "+p.getY());
                                if (!(board[i][j] instanceof King) && board[i][j].valid_move(new Point(i, j), p, board)) {
//                                    System.out.println("33");
                                    validCheckMate = false;
                                    break;
                                } else {
//                                    System.out.println("44");
                                    validCheckMate = true;
                                }
                            }
                        }
                    }
                    else if(bKing.isChecked) {
                        if (board[i][j].getColor() == Color.BLACK) {
                            for (Point p : getCheckPieceObject.getPath_pieceToKing()) {
                                if (!(board[i][j] instanceof King) && board[i][j].valid_move(new Point(i, j), p, board)) {
                                    validCheckMate = false;
                                    break;
                                } else {
                                    validCheckMate = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return validCheckMate;
    }

    /**
     * This method is for getting valid King's position to avoid from check and checkmate.
     *
     * @param x getting integer position row.
     * @param y getting integer position column.
     * @return return ArrayList that includes king's available position.
     */
    private ArrayList<Point> getValidKingPosition(int x, int y){
        ArrayList<Point> KingCanMovePosition = new ArrayList<>();

        KingCanMovePosition.add(new Point(x+1, y+1));
        KingCanMovePosition.add(new Point(x+1,y-1));
        KingCanMovePosition.add(new Point(x+1, y));
        KingCanMovePosition.add(new Point(x, y+1));
        KingCanMovePosition.add(new Point(x, y-1));
        KingCanMovePosition.add(new Point(x-1, y+1));
        KingCanMovePosition.add(new Point(x-1, y-1));
        KingCanMovePosition.add(new Point(x-1, y));

        return KingCanMovePosition;

    }

    /**
     * This method is for determining checkmate.
     */
    private void checkmate(){
        boolean validCheckMate;

        ArrayList<Point> getWhiteKingsPosition;
        ArrayList<Point> getBlackKingsPosition;

        getBlackKingsPosition = getValidKingPosition(bKing.getKingPosition().getX(), bKing.getKingPosition().getY());
        getWhiteKingsPosition = getValidKingPosition(wKing.getKingPosition().getX(), wKing.getKingPosition().getY());


        //Protect condition 1
        validCheckMate = isRescueKing();



        //Protect condition 3
        Point saveOriginal = wKing.getKingPosition();
        Point saveOriginalB = bKing.getKingPosition();
        if(wKing.isChecked){

            breakLoop:
            for(Point validKing : getWhiteKingsPosition){
                wKing.KingPosition = saveOriginal;
                if(wKing.valid_move(wKing.getKingPosition(), validKing ,board)){
                    wKing.KingPosition = validKing;
                    if(isCheck()){
                        validCheckMate = isRescueKing();
                    }
                    else {
                        validCheckMate = false;
                        wKing.KingPosition = saveOriginal;
                        break;
                    }
                }
                else {
                    validCheckMate = true;
                }
            }
            if(validCheckMate){
                print();
                System.out.println("Checkmate");
                System.out.println("Black Wins");
                System.exit(0);
            }
        }
        else if(bKing.isChecked){
            breakLoop:
            for(Point validKing : getBlackKingsPosition){
                bKing.KingPosition = saveOriginalB;
                if(bKing.valid_move(bKing.getKingPosition(), validKing, board)){
                    bKing.KingPosition = validKing;
                    if(isCheck()){
                        validCheckMate = isRescueKing();
                    }
                    else {
                        validCheckMate = false;
                        bKing.KingPosition = saveOriginalB;
                        break;
                    }
                }
                else
                    validCheckMate = true;
            }
            if(validCheckMate){
                print();
                System.out.println("Checkmate");
                System.out.println("White Wins");
                System.exit(0);
            }
        }
    }

    /**
     * This method is for check King.
     *
     * @return return true when any pieces attack another color's king.
     */
    private boolean isCheck(){
        for(int i =0; i < 8; i++) {
            for(int j = 0; j<8; j++) {
                int x,y;
                if(board[i][j] != null){
                    if(board[i][j].getColor() == Color.BLACK){
                        Point from = new Point(i,j);
                        if(board[i][j].valid_move(from, wKing.getKingPosition(), board)){ // if king is on the path, check!
                            if (board[i][j].isCheckKing()){
                                getCheckPieceObject = board[i][j];
                                getCheckPiece = new Point(i,j);
                                wKing.isChecked = true;
                                return true;
                            }
                            else {
                                wKing.isChecked = false;
                                return false;
                            }
                        }
                        else{
                            wKing.isChecked = false;
                        }
                    }
                    else{
                        Point from = new Point(i,j);
                        if(board[i][j].valid_move(from, bKing.getKingPosition(), board)){ // if king is on the path, check!
                            if (board[i][j].isCheckKing()){
                                getCheckPieceObject = board[i][j];
                                getCheckPiece = new Point(i,j);
                                bKing.isChecked = true;
                                return true;
                            }
                            else{
                                bKing.isChecked = false;
                                return false;
                            }
                        }
                        else{
                            bKing.isChecked = false;
                        }
                    }
                }
            }
        }
        wKing.isChecked = false;
        bKing.isChecked = false;
        return false;
    }

    /**
     * This method is moving engine after check valid move.
     *
     * @param cur get current piece
     * @param from get point original position
     * @param to get point destination position
     * @param board get board
     * @param promote to get character to determine replace pawn
     * @return return boolean if all conditions are matching
     */
    private boolean move (Piece cur, Point from, Point to, Piece[][] board, char promote){
        if(cur.valid_move(from, to, board)){
            board[to.getX()][to.getY()] = cur;
            board[from.getX()][from.getY()] = null;

            if(cur instanceof King){
                if(cur.getColor() == Color.WHITE){ //update king position to check check
                    wKing = (King) board[to.getX()][to.getY()];
                    wKing.setKingPosition(to.getX(),to.getY());
                }

                else{
                    bKing = (King) board[to.getX()][to.getY()];
                    bKing.setKingPosition(to.getX(),to.getY());
                }

                if (isCheck()){
                	System.out.println("Illegal move, try again");
                    board[from.getX()][from.getY()] = cur;
                    board[to.getX()][to.getY()] = null;
                    return false;
                }

                castling(board, to);
            }



            //call promotion here:
            if(cur instanceof Pawn) {
                //call promotion
                if(to.getX() == 0 || to.getX() == 7)
                    Promotion(cur, to, promote);
                //this avoids false enpassant call
                if(to.getX() - from.getX() == 2 || to.getX() -from.getX() == -2) {
                    return true;
                }
            }
            falseEnPassant();

            if(isCheck()){
            	checkmate();
                System.out.println("Check!");

                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if(bKing.isChecked && cur.getColor() == Color.BLACK ){
                    board[from.getX()][from.getY()] = cur;
                    board[to.getX()][to.getY()] = null;
                    return false;
                }
                else if(wKing.isChecked && cur.getColor() == Color.WHITE ){
                    board[from.getX()][from.getY()] = cur;
                    board[to.getX()][to.getY()] = null;
                    return false;
                }

            }
            return true;

        }
        else{
            System.out.println("Illegal move, try again");
            return false;
        }
    }                    


    /**
     * This method is for checking moving pieces.
     *
     * @param from need to get original position.
     * @param to need to get destination position.
     * @param promote send the indicated piece that a user wants to promote to, if needed
     * @return return boolean if all conditions are matching.
     */
    public boolean movingPiece(Point from, Point to, char promote) {
        boolean isMoving;
        Piece cur = board[from.getX()][from.getY()];

        if (cur instanceof King){
//            System.out.println("King");
            isMoving = move(cur, from, to, board, promote);
            return isMoving;
        }
        else if(cur instanceof Queen) {
//            System.out.println("Queen");
            isMoving = move(cur, from, to, board, promote);
            return isMoving;

        }
        else if (cur instanceof Bishop){
//            System.out.println("Bishop");
            isMoving = move(cur, from, to, board, promote);
            return isMoving;
        }
        else if (cur instanceof Knight){
//            System.out.println("Knight");
            isMoving = move(cur, from, to, board, promote);
            return isMoving;
        }
        else if (cur  instanceof Rook){
//            System.out.println("Rook");
            isMoving = move(cur, from, to, board, promote);
            return isMoving;
        }
        else if (cur instanceof Pawn){
//            System.out.println("Pawn");
            isMoving = move(cur, from, to, board, promote);
            return isMoving;
        }
        else{
            //System.out.println("No pieces");
            return false;
        }

    }
    /*
    * This method sets the possibility of en Passant of all pieces to false.
    *
    *
    */
    private void falseEnPassant() {
        for(int i =0; i < 8; i++) {
            for(int j = 0; j<8; j++) {
                if(board[i][j] instanceof Pawn) {
                    board[i][j].setEnPassant(false);
                }
            }
        }
        return;
    }

    /**
     * This method is for promotion. When pawn arrives end of board, then can switch to queen, rook, bishop,and knight.
     *
     * @param cur get current position to check current color
     * @param to get Point instance to know destination.
     * @param promote get the kind of piece a user wants to promote to
     */
    private void Promotion(Piece cur, Point to, char promote) {
        int x = to.getX();
        int y = to.getY();
        Color color = cur.getColor();

        if(promote == 'N') {
        	if(x == 0){
                board[x][y] = new Knight("wN", color);
            }
            else {
                board[x][y] = new Knight("bN", color);
            }
        }
        else if(promote == 'R') {
        	if(x == 0){
                board[x][y] = new Rook("wR", color);
            }
            else {
                board[x][y] = new Rook("bR", color);
            }
        }
        else if(promote == 'B') {
        	if(x == 0){
                board[x][y] = new Bishop("wB", color);
            }
            else {
                board[x][y] = new Bishop("bB", color);
            }

        }
        else {
        	if(x == 0){
                board[x][y] = new Queen("wQ", color);
            }
            else {
                board[x][y] = new Queen("bQ", color);
            }
        }


        return;


    }

    /**
     * This method is for getting board to use anywhere.
     *
     * @return return current board.
     */
    public Piece[][] getBoard(){
        return board;
    }

    /**
     * This method is for print out board with correct format.
     */
    public void print(){
        int BOARD_NUMBER_INDICATOR = 8;
//        System.out.println();
        for(int i = 0; i < 8; i++){
            for(int j =0; j <9; j++){
                if(j<8){
                    if(board[i][j] != null)
                        System.out.print(board[i][j]); //toString returns " " + name
                    else{
                        if((i+j)%2 == 0){
                            System.out.print("   ");
                        }
                        else{
                            System.out.print("## ");
                        }
                    }
                }
                else{
                    System.out.println(" "+BOARD_NUMBER_INDICATOR--);
                }
            }
        }
        System.out.println(BOARD_STRING_INDICATOR);
        System.out.println();
    }
}
