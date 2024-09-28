package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;
import puzzles.tilt.solver.Tilt;

import java.io.*;
import java.util.Scanner;

/**
 * PTUI class to display the Tilt game in text form
 * Author: Jet Li
 */
public class TiltPTUI implements Observer<TiltModel, String> {
    /** the model object */
    private TiltModel model;
    /** Scanner variable to read in user input*/
    private Scanner in;
    /** boolean to run the game */
    private boolean gameOn;
    /** filename as a string for resetting and initial loading */
    private String fileName;

    /** main method to create PTUI object and to initially load board */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        try{
            System.out.println("Loaded: " + args[0]);
            TiltPTUI myTiltP = new TiltPTUI(args[0]);
            myTiltP.run();
        }
        catch(IOException io){
            System.out.println("The file does not exist!");
        }
    }

    /**
     * constructor that creates the model based on the filename as a string
     * @param filename
     * @throws IOException
     */
    public TiltPTUI(String filename) throws IOException {
        model = new TiltModel(filename);
        model.addObserver(this);
        gameOn = true;
        in = new Scanner( System.in );
        this.fileName = filename;
    }

    /**
     * constructor that creates the model based on the filename as a string
     * @param filename
     * @throws IOException
     */
    public TiltPTUI(File filename) throws IOException {
        model = new TiltModel(filename);
        model.addObserver(this);
        gameOn = true;
        in = new Scanner( System.in );
        this.fileName = String.valueOf(filename);
    }

    /**
     * Gets a filename from the user and attempts to load the file.
     * @return true iff the game was loaded successfully
     */
    public boolean loadFromFile() throws IOException {
        boolean ready = false;
        while(!ready){
            System.out.println("Enter a valid file name or type Q to go back.");
            String command =  in.next();
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("going back...");
                return false;
            }
            ready = model.loadBoardFromFile(command);
        }
        return true;
    }

    /**
     * Loads a new game
     * @return True if the user starts a game
     */
    public boolean gameStart() throws IOException {
        System.out.println(model.getCurrentConfig());
        gameOn = true;
        in = new Scanner(System.in);
        return true;
    }

    /**
     * displays the board
     */
    public void displayBoard(){
        System.out.println(model.getCurrentConfig());
        System.out.print("> ");
    }

    /**
     * The main program loop
     */
    public void run() throws IOException {
        while (gameOn) {
            if (!gameStart()) //loads new games or quits
                break;
            gameLoop(); // gameplay
        }
    }

    /**
     * the game looping that takes in the input and prints out the designated message and the board if needed
     * @throws IOException
     */
    private void gameLoop() throws IOException {
        System.out.println("h(int)              -- hint next move\n" +
                "l(oad) filename     -- load new puzzle file\n" +
                "t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                "q(uit)              -- quit the game\n" +
                "r(eset)             -- reset the current game");

        Scanner scn = new Scanner(System.in);
        System.out.print("> ");
        String theLine = scn.nextLine();
        while (!theLine.equals("q") && !theLine.equals("quit")){
            String[] splitted = theLine.split(" ");
            if (splitted.length == 1){
                if (splitted[0].equals("h") || splitted[0].equals("H")){
                    if (model.gameOver()){
                        System.out.println("Already solved!");
                        displayBoard();
                    }
                    else{
                        model.getHint();
                    }
                }
                //reset the board to the original board
                else if(splitted[0].equals("r") || splitted[0].equals("R") || splitted[0].equals("reset")){
                    //model = new TiltModel(fileName);
                    System.out.println("Puzzle reset!");
                    TiltModel theModel = new TiltModel(fileName);
                    model.setCurrentConfig(theModel.getCurrentConfig());
                    displayBoard();
                }
                else{
                    //might need to reprompt user somehow
                    System.out.println("h(int)              -- hint next move\n" +
                            "l(oad) filename     -- load new puzzle file\n" +
                            "t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                            "q(uit)              -- quit the game\n" +
                            "r(eset)             -- reset the current game\n>");
                }
            }
            else if (splitted.length == 2){
                if (splitted[0].equals("l") || splitted[0].equals("L")){
                    fileName = splitted[1];
                    model.loadBoardFromFile(splitted[1]);
                }
                else if (splitted[0].equals("t") || splitted[0].equals("T")){
                    String move = splitted[1];
                    if (move.equals("N") || move.equals("E") || move.equals("S") || move.equals("W")){
                        if (model.gameOver()){
                            System.out.println("Already solved!");
                            displayBoard();
                        }
                        else{
                            if (model.getCurrentConfig().isValid(move)){
                                model.setCurrentConfig(model.getCurrentConfig().makeMove(move));
                                displayBoard();
                            }
                            else{
                                System.out.println("Illegal move. A blue slider will fall through the hole!");
                                displayBoard();
                            }
                        }
                    }
                    else{
                        //might need to reprompt user somehow
                        System.out.println(">h(int)              -- hint next move\n" +
                                "l(oad) filename     -- load new puzzle file\n" +
                                "t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                                "q(uit)              -- quit the game\n" +
                                "r(eset)             -- reset the current game\n>");
                    }
                }
            }
            theLine = scn.nextLine();
        }
        gameOn = false;
        return;
    }

    /**
     * Updates the board based on the msg inputted
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel model, String msg) {
        if (msg.equals(TiltModel.LOADED)){ // game is loaded successfully
            System.out.println("Loaded: " + fileName);
            displayBoard();
            return;
        }else if (msg.equals(TiltModel.LOAD_FAILED)){ //Game failed to load
            System.out.println("Failed to load: " + fileName);
            displayBoard();
            return;
        } else if (msg.equals(TiltModel.HINT)) { //Model is reporting a hint
            System.out.println(msg);
            displayBoard();
            return;
        }else if (msg.equals("Already solved!")) { //Model is reporting the board already being solved
            System.out.println(msg);
            displayBoard();
            return;
        }
        else if (msg.equals("No solution!")) {  //Model is reporting no solution
            System.out.println(msg);
            displayBoard();
            return;
        }
        displayBoard(); // renders the board
        System.out.println(msg);
    }

}