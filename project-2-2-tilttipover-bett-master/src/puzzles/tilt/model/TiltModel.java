package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.solver.Tilt;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/** the model class responsible for handling the logic behind the view and controller
 * Author: Jet Li */
public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;

    // Message constants for Views
    /**
     * Message sent when a board has successfully loaded.
     */
    public static String LOADED = "loaded";
    /**
     * Message sent when a board has failed to load.
     */
    public static String LOAD_FAILED = "loadFailed";
    /**
     * The message that will precede a hint.
     */
    public static String HINT = "Next step!";

    public static String HINT_FAILED = "hint did the Failed";

    public static String MADE_MOVE = "made move";

    public static String RESET = "Puzzle reset!";

    private char[][] board;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
    /**
     * Attempts to load a board from a given file name. It will announce to the observers if it was loaded successfully or not.
     * @param filename The file to load
     * @return True iff loaded successfully
     */
    public boolean loadBoardFromFile(String filename) throws IOException {
        return loadBoardFromFile(new File(filename));
    }

    /**
     * Constructor to build the model based on the file as a File
     * @param file: the file used to build the configuration
     * @throws IOException
     */
    public TiltModel(File file) throws IOException {
        String theFile = String.valueOf(file);
        currentConfig = new TiltConfig(theFile);
    }

    /**
     * Constructor to build the model based on the file as a String
     * @param file: the file used to build the configuration
     * @throws FileNotFoundException
     */
    public TiltModel(String file) throws FileNotFoundException {
        currentConfig = new TiltConfig(file);
    }

    /**
     * Attempts to load a board from a file object. It will announce to the observers if it was loaded successfully or not.
     * @param file The file to load
     * @return True if loaded successfully
     */
    public boolean loadBoardFromFile(File file) throws IOException{
        try {
            currentConfig = new TiltModel(file).getCurrentConfig();
            alertObservers(LOADED);
            return true;
        } catch (IOException io){
            alertObservers(LOAD_FAILED);
            //throw new FileNotFoundException("The file does not exist!");
            return false;
        }
    }

    /**
     * @return a boolean of whetehr the game is finished or not
     */
    public boolean gameOver(){
        return currentConfig.isSolution();
    }

    /**
     * alerts the observers of a hint and makes the current configuration the next move
     * unless there is no possible solution or the board is already solved
     */
    public void getHint(){
        Solver solver = new Solver();
        List<Configuration> path = solver.BFS(currentConfig);
        if (path.size() > 1){
            currentConfig = (TiltConfig) path.get(1);
            //probably need to change this
            alertObservers(HINT);
        }
        //else if (path.size() == 1 && path != null){
        else if (path.size() == 1 && currentConfig.isSolution()){
            alertObservers("Already solved!");
        }
        else{
            //cannot do the hint
            alertObservers("No solution!");
        }
    }

    /**
     * @returns the current configuration
     */
    public TiltConfig getCurrentConfig() {
        return currentConfig;
    }

    /**
     * Sets the current configuration to be the inputted configuration
     * @param currentConfig: the configuration to set the current configuration to be
     */
    public void setCurrentConfig(TiltConfig currentConfig) {
        this.currentConfig = currentConfig;
    }
}