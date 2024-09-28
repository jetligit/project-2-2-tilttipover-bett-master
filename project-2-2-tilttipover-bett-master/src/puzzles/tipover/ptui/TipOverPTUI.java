package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;

import java.util.*;

/**
 * TipOverTUI is the view for the game on the console. It's responsible for displaying the board, updating it whenever
 * an action occurs, and presenting multiple commands for the user to use.
 * @author Benson Zhou
 */
public class TipOverPTUI implements Observer<TipOverModel, String> {
    private TipOverModel model; // object to handle TipOverModel methods
    private Scanner in; // Scanner object to take in user input

    private String filename; // Filename
    private boolean gameOn; // where the game is on or off

    private boolean stop = false; // on when quit is activated

    /**
     * Instantiates filename, model, scanner, and adds an observer.
     * @param filename
     */
    public TipOverPTUI(String filename) {
        this.filename = filename;
        model = new TipOverModel();
        model.addObserver(this);
        in = new Scanner( System.in );
    }

    /**
     * Update checks the message that observer sends and updates console accordingly to the specific message (whether
     * it was loading a file, moving the tipper, make an invalid move, and more. The message is displayed and the board
     * is updated.
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TipOverModel model, String message) {
        if (message.startsWith(TipOverModel.LOADED)) {
            System.out.println("> " +message);
            displayBoard();
            return;
        }
        else if(message.equals("Current board is already solved.")) {
            System.out.println("> " + message);
            displayBoard();
            return;
        }
        else if(message.equals("I WON!")) {
            System.out.println("> " + message);
            displayBoard();
            return;
        }
        else if(message.equals(TipOverModel.NOSOL)) {
            System.out.println(message);
            displayBoard();
            return;
        }
        else if (message.startsWith(TipOverModel.FAILED)){
            System.out.println("> " + message);
            displayBoard();
            return;
        }
        else if (message.equals(TipOverModel.TIPPER)) {
            System.out.println("> Next step!");
            displayBoard();
            return;
        }
        else if(message.equals(TipOverModel.RESET)) {
            System.out.println("> " + message);
            displayBoard();
            return;
        }
        else if(message.equals("Move updated")) {
            System.out.println(">");
            displayBoard();
            return;
        }
        else if(message.equals("No crate or tower there.")) {
            System.out.println("> " + message);
            displayBoard();
            return;
        }
        else if(message.equals("Move goes off the board.")) {
            System.out.println("> " + message);
            displayBoard();
            return;
        }
        else if(message.equals("A tower has been tipped over.")) {
            System.out.println("> " + message);
            displayBoard();
            return;
        }
        else if(message.equals("Tower cannot be tipped over.")) {
            System.out.println("> " + message);
            displayBoard();
            return;
        }
        displayBoard();
    }

    /**
     * Checks if there are arguments in PTUI configuration
     * If there is, the argument is placed into the constructor and the method run() is run.
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverPTUI filename");
        }
        else {
            TipOverPTUI top = new TipOverPTUI(args[0]);
            top.run();
        }
    }

    /**
     * loads the board from the file found from the args
     * gameOn is flipped to true and gameLoop is run under a while loop until stop is true.
     */
    public void run() {
        model.loadBoardFromFile(filename, false);
        gameOn = true;
        while (!stop) {
            gameLoop(); // gameplay
        }

    }

    /**
     * displayBoard() prints out the string that represents/depicts the board using model to call the toString in
     * TipOverConfig
     */
    public void displayBoard() {
        System.out.println(model.displayBoard());
    }

    /**
     * Game loop prints out the commands users can use and reads for the user input. Depending on the user input, model runs
     * different commands. When the user types quit, gameOn is changed to false, stopping the while loop.
     */
    public void gameLoop() {
        System.out.println("h(int)              -- hint next move\nl(oad) filename     -- load new puzzle file\nm(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                "q(uit)              -- quit the game\nr(eset)             -- reset the current game");
        while (gameOn) {
            String command = in.nextLine().strip();
            command = command.toLowerCase();
            if(command.equals("h")) {
                model.getHint();
            }
            else if(command.startsWith("l") || command.startsWith("load")) {
                if(command.startsWith("l")) {
                    if(command.length() > 2)
                        model.loadBoardFromFile(command.substring(2), false);
                }
                else {
                    if(command.length() > 5) {
                        model.loadBoardFromFile(command.substring(5), false);
                    }
                }
            }
            else if(command.startsWith("m")) {
                if(command.length() > 2) {
                    String direction = command.substring(2);
                    model.move(direction);
                }
            }
            else if(command.equals("q") || command.equals("quit")) {
                gameOn = false;
                stop = true;
                return;
            }
            else if(command.equals("r")) {
                model.loadBoardFromFile(filename, true);
            }
            else{
                System.out.println("> h(int)              -- hint next move\nl(oad) filename     -- load new puzzle file\nm(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                        "q(uit)              -- quit the game\nr(eset)             -- reset the current game");
            }
        }
    }
}