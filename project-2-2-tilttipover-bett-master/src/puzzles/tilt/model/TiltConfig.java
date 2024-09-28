package puzzles.tilt.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import puzzles.tilt.solver.Tilt;

/** Tilt Configuration class to make possible configurations to solve the puzzle
 * Author: Jet Li */
public class TiltConfig implements Configuration {
    /** the board as a 2D character array  */
    private char [][] board;
    /** the hole as a character */
    private static char HOLE = 'O';
    /** the green piece as a character */
    private static char GREEN = 'G';
    /** the blue piece as a character */
    private static char BLUE = 'B';
    /** the space as a character */
    private static char SPACE = '.';
    /** the dimension of the board as an int */
    private static int DIMENSION;
    /** the boolean used to stop a configuration from being made if the tilt causes
     * a blue piece will fall into the hole  */
    private boolean denyB;
    /** the boolean used for when a tilt changes the board  */
    private boolean changed;

    /**
     * constructor to create a new TiltConfig when a tilt changes the board
     * @param board
     */
    public TiltConfig(char[][] board){
        this.board = board;
        this.DIMENSION = board.length;
    }


    /**
     * constructor to read in the file and build the board
     * @param theFile
     * @throws FileNotFoundException
     */
    public TiltConfig(String theFile) throws FileNotFoundException {
        try {
            // load data from file
            BufferedReader bf = new BufferedReader(new FileReader(theFile));
            // read entire line as string
            String line = bf.readLine();
            DIMENSION = Integer.parseInt(line);
            board = new char[DIMENSION][DIMENSION];
            // checking for end of file
            int counter = 0;
            while (line != null) {
                String[] fields = line.split(" ");
                char[] charFields = new char[fields.length];
                for (int i = 0; i < fields.length; i++){
                    char theCharacter = fields[i].charAt(0);
                    charFields[i] = theCharacter;
                }
                if (fields.length == DIMENSION){
                    System.arraycopy(charFields, 0, board[counter++], 0, DIMENSION);
                }
                line = bf.readLine();
            }
            bf.close();
        } catch (IOException io){
            throw new FileNotFoundException("The file does not exist!");
        }
    }

    /**
     * loops through the board and makes sure the board does not contain any greens
     * @returns a boolean of whether the board (configuration) is a valid one
     */
    @Override
    public boolean isSolution() {
        for (int r = 0; r < DIMENSION; r++){
            for (int c = 0; c < DIMENSION; c++){
                if (this.board[r][c] == GREEN){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * gets the possible configurations (neighbors) from the current configuration
     * @return a collection of configuration
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();

        ArrayList<String> cardinals = new ArrayList<>();
        cardinals.add("N");
        cardinals.add("E");
        cardinals.add("S");
        cardinals.add("W");

        while(!cardinals.isEmpty()){
            char[][] copyBoard = new char[DIMENSION][DIMENSION];
            for (int i = 0; i < DIMENSION; i++){
                System.arraycopy(board[i], 0, copyBoard[i], 0, DIMENSION);
            }
            denyB = false;
            changed = false;
            String theDirection = cardinals.remove(0);
            if (theDirection.equals("N")) {
                for (int r = 0; r < DIMENSION; r++) {
                    for (int c = 0; c < DIMENSION; c++) {
                        copyBoard = changeBoard(r,c,copyBoard,theDirection);
                    }
                }
            }
            else if (theDirection.equals("E")) {
                for (int r = 0; r < DIMENSION; r++) {
                    for (int c = DIMENSION - 1; c >= 0; c--) {
                        copyBoard = changeBoard(r,c,copyBoard,theDirection);
                    }
                }
            }
            else if (theDirection.equals("S")) {
                for (int r = DIMENSION - 1; r >= 0; r--) {
                    for (int c = 0; c < DIMENSION; c++) {
                        copyBoard=changeBoard(r,c,copyBoard,theDirection);
                    }
                }
            }
            else if (theDirection.equals("W")) {
                for (int r = 0; r < DIMENSION; r++) {
                    for (int c = 0; c < DIMENSION; c++) {
                        copyBoard = changeBoard(r,c,copyBoard,theDirection);
                    }
                }
            }

            //only create new TiltConfig and add it as a new neighbor now because this is when the board is completely
            //updated from the moving green and blue pieces of one cardinal
            if (changed && !denyB) {
                neighbors.add(new TiltConfig(copyBoard));
            }
        }
        return neighbors;
    }

    /**
     * Helper method that getNeighbors and makeMove use to change the board. It returns a 2D character array (the board)
     * @param r: row
     * @param c: column
     * @param copyBoard: a copy of the board
     * @param theDirection: the direction to tilt the board
     * @return
     */
    public char[][] changeBoard(int r, int c, char[][] copyBoard, String theDirection){
        int moveR = 0;
        int moveC = 0;

        switch (theDirection) {
            case "N" -> moveR = -1;
            case "E" -> moveC = 1;
            case "S" -> moveR = 1;
            case "W" -> moveC = -1;
        }

        char curChar = copyBoard[r][c];
        if (curChar == GREEN || curChar == BLUE) {
            int changedRow = r + moveR;
            int changedCol = c + moveC;

            //the new coordinates to move the piece to if it can be moved
            int newRow = r;
            int newCol = c;

            //checking if the new spot is in the board
            if (changedRow >= 0 && changedRow < DIMENSION && changedCol >= 0 && changedCol < DIMENSION) {
                boolean deleted = false;
                while (copyBoard[changedRow][changedCol] == SPACE || copyBoard[changedRow][changedCol] == HOLE) {
                    changed = true;
                    //player is only allowed to get rid of the green
                    if (copyBoard[changedRow][changedCol] == HOLE) {
                        //System.out.println("THIS THING ACTUALLY WORKED");
                        if (curChar == GREEN) {
                            newRow = changedRow;
                            newCol = changedCol;
                            deleted = true;
                        }
                        //if current piece is blue and can slide into a hole in a direction, then that direction
                        //is invalid
                        else {
                            changed = false;
                            denyB = true;
                        }
                        break;
                    } else {
                        newRow = changedRow;
                        newCol = changedCol;

                        /** break the while loop of finding the furthest the piece can slide if the piece is out
                         * of bounds */
                        changedRow += moveR;
                        if (changedRow < 0 || changedRow >= DIMENSION) {
                            break;
                        }
                        changedCol += moveC;
                        if (changedCol < 0 || changedCol >= DIMENSION) {
                            break;
                        }
                    }
                }
                // if the row or column was changed (piece can move in that direction) then swap
                if (newRow != r || newCol != c && changed) {
                    //changed = true;
                    if (deleted) {
                        copyBoard[r][c] = SPACE;
                    } else {
                        copyBoard[newRow][newCol] = curChar;
                        copyBoard[r][c] = SPACE;
                    }
                }
            }
        }
        return copyBoard;
    }

    /**
     * check if the move is valid based on the board
     * @param move: the direction to tilt the board
     * @return a boolean for whether a tilt is valid or not
     */
    public boolean isValid(String move){
        for (int r = 0; r < DIMENSION; r++){
            for (int c = 0; c < DIMENSION; c++){
                if (board[r][c] == BLUE){
                    int moveR = 0;
                    int moveC = 0;
                    //loop in the given direction until you reach either a wall or hole
                    switch (move) {
                        case "N" -> moveR = -1;
                        case "E" -> moveC = 1;
                        case "S" -> moveR = 1;
                        case "W" -> moveC = -1;
                    }
                    int changedR = r + moveR;
                    int changedC = c + moveC;

                    //checking if the new spot is in the board
                    if (changedR >= 0 && changedR < DIMENSION && changedC >= 0 && changedC < DIMENSION) {
                        changed = false;
                        while (board[changedR][changedC] == SPACE || board[changedR][changedC] == HOLE){
                            changed = true;
                            if (board[changedR][changedC] == HOLE){
                                return false;
                            }
                            changedR += moveR;
                            changedC += moveC;
                            if (changedR < 0 || changedR >= DIMENSION || changedC < 0 || changedC >= DIMENSION){
                                break;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param theDirection: the direction that the board is tilting
     * @return the configuration made after making a move
     */
    public TiltConfig makeMove(String theDirection) {
        char[][] copyBoard = board;

        if (theDirection.equals("N")) {
            for (int r = 0; r < DIMENSION; r++) {
                for (int c = 0; c < DIMENSION; c++) {
                    copyBoard = changeBoard(r,c,copyBoard,theDirection);
                }
            }
        }
        else if (theDirection.equals("E")) {
            for (int r = 0; r < DIMENSION; r++) {
                for (int c = DIMENSION - 1; c >= 0; c--) {
                    copyBoard = changeBoard(r,c,copyBoard,theDirection);
                }
            }
        }
        else if (theDirection.equals("S")) {
            for (int r = DIMENSION - 1; r >= 0; r--) {
                for (int c = 0; c < DIMENSION; c++) {
                    copyBoard=changeBoard(r,c,copyBoard,theDirection);
                }
            }
        }
        else if (theDirection.equals("W")) {
            for (int r = 0; r < DIMENSION; r++) {
                for (int c = 0; c < DIMENSION; c++) {
                    copyBoard = changeBoard(r,c,copyBoard,theDirection);
                }
            }
        }
        return new TiltConfig(copyBoard);
    }

    /**
     * @param r: row
     * @param c: column
     * @return returns the character in the board
     */
    public char getElement(int r, int c){
        return board[r][c];
    }

    /**
     * @return the dimension of the baord
     */
    public int getDIMENSION() {
        return DIMENSION;
    }

    /**
     * @param other: object being compared with
     * @return boolean of whether the object is equal or not
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof TiltConfig) {
            TiltConfig otherTiltC = (TiltConfig) other;
            result = Arrays.deepEquals(this.board, otherTiltC.board);
        }
        return result;
    }

    /**
     * @return hashcode made by array
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }

    /**
     * @return the board as a string
     */
    @Override
    public String toString() {
        StringBuilder theString = new StringBuilder();
        for (int r = 0; r < DIMENSION; r++){
            for (int c = 0; c < DIMENSION; c++){
                theString.append(this.board[r][c]);
                if(c != DIMENSION - 1){
                    theString.append(" ");
                }

            }
            theString.append("\n");
        }
        return theString.toString();
    }
}