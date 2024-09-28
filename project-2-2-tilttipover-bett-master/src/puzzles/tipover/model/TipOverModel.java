package puzzles.tipover.model;

import javafx.scene.Node;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class TipOverModel {
    /** the collection of observers of this model */
    private final List<Observer<TipOverModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TipOverConfig currentConfig;
    public static String LOADED = "Loaded: ";
    public static String FAILED = "Failed to Load: ";
    public static String TIPPER = "Tipper";
    public static String RESET = "Puzzle reset!";
    public static String NOSOL = "No Solution.";

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TipOverModel, String> observer) {
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
     * Gets the hint by getting the next configuration after the current one (only if this current config can be solved).
     * Conditions such as already solved and isSolution are checked as hint makes the moves.
     */
    public void getHint() {
        if(currentConfig.isAlreadySolved()){
            alertObservers("Current board is already solved.");
            return;
        }
        Solver solve = new Solver();
        List<Configuration> path = solve.BFS(currentConfig);
        // If current config has no solution
        if(path.size() == 1) {
            alertObservers(NOSOL);
        }
        else {
            currentConfig = (TipOverConfig) path.get(1);
            //Checks if hint solves the board game.
            if (currentConfig.isSolution()) {
                alertObservers("I WON!");
                currentConfig.solved = true;
                currentConfig.alreadySolved = true;
            } else {
                alertObservers(TIPPER);
            }
        }
    }

    /** Getter method for number of rows */
    public int getGridRow() {
        return currentConfig.getGridRow();
    }

    /** Getter method for number of rows */
    public int getGridCol() {
        return currentConfig.getGridCol();
    }

    /** Getter method for number of columns */
    public int getTipperValue(int r, int c) {
        return currentConfig.getTipperValue(r, c);
    }

    /** Getter method for the value of where tipper is on */
    public int getTipperX(){
        return currentConfig.getTipperX();
    }

    /** Getter method for x coord of tipper */
    public int getTipperY(){
        return currentConfig.getTipperY();
    }

    /** Getter method for x coord of goal */
    public int getGoalX() {
        return currentConfig.getGoalX();
    }

    /** Getter method for y coord of goals */
    public int getGoalY() {
        return currentConfig.getGoalY();
    }
    /** calls currentConfig's toString to depict current board in console as Strings */
    public String displayBoard() {
        return currentConfig.toString();
    }

    /**
     * Current config is set to the new config returned from makeMove(direction).
     * Each condition is checked to alert an appropriate update
     * @param direction (n, s, e, w)
     */
    public void move(String direction) {
        currentConfig = currentConfig.makeMove(direction);
        if(currentConfig.isAlreadySolved()) {
            alertObservers("Current board is already solved.");
        }
        else if(currentConfig.isSolved()) {
            alertObservers("I WON!");
        }
        else if(currentConfig.isNoCrate()) {
            alertObservers("No crate or tower there.");
        }
        else if(currentConfig.isOutOfBounds()) {
            alertObservers("Move goes off the board.");
        }
        else if(currentConfig.isTipOverTower()) {
            alertObservers("A tower has been tipped over.");
        }
        else if(currentConfig.isCantTip()){
            alertObservers("Tower cannot be tipped over.");
        }
        else{
            alertObservers("Move updated");
        }
    }

    /**
     * Sets current config as a new TipOverConfig obj being made in TipOverConfig(file);
     * @param file
     */
    public void makeConfig(String file) {
        currentConfig = new TipOverConfig(file);
    }


    /**
     * This method checks for the existence of the given file. If it exists, current config is set to the cne TipOverConfig
     * object. Reset is checked if the board is asking for a reset or a simple load. Alert Observers send appropriate messages.
     * @param file (File inputted)
     * @param reset (true or false on whether reset was called)
     */
    public void loadBoardFromFile(String file, boolean reset) {
        File filer = new File(file);
        if(!filer.exists()) {
            alertObservers(FAILED + file);
        }
        else{
        currentConfig = new TipOverConfig(file);
            if(reset) {
                alertObservers(RESET);
            }
            else {
                alertObservers(LOADED + file);
            }
        }
    }

}
