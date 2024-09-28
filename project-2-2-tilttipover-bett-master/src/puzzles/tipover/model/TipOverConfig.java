package puzzles.tipover.model;

// TODO: implement your TipOverConfig for the common solver

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * TipOverConfig is the heart of the program. The way the program functions backend is all handled here. The neighbors are
 * gotten and returned, the validity of moves are checked and handled, and the solution is calculated here as well.
 * Many getter methods are also made for other classes to access certain information.
 * @author Benson Zhou
 */

public class TipOverConfig implements Configuration {
    int[] dimensions = {0,0}; /** dimensions of the board */
    int[][] grid; /** 2d array of the board */
    int[] goal = {0,0}; /** holds the coords of the goal */
    int[] tipper = {0,0}; /** holds the coords of the tipper */

    boolean alreadySolved = false; /** If the program already solved the file */
    boolean outOfBounds = false; /** If a move went out of bounds */
    boolean solved = false;
    boolean tipOverTower = false; /** If a move tipped a tower over */
    boolean noCrate = false; /** If a move is trying to move to an empty spot */
    boolean cantTip = false; /** If a move is trying to tip a tower in an unavailable spot */

    boolean fileLoad; /** If a file can be loaded */

    /**
     * Initializes grid, goal, and tipper
     * @param grid
     * @param goal
     * @param tipper
     */
    public TipOverConfig(int[][] grid, int[] goal, int[] tipper) {
        this.grid = grid;
        this.goal = goal;
        this.tipper = tipper;
    }

    /**
     * Instantiates grid, goal, and tipper by reading a file using a bufferedReader
     * @param file
     */
    public TipOverConfig(String file){
        try{
            int row = 0;
            BufferedReader bf = new BufferedReader(new FileReader(file));
            String line = bf.readLine();
            boolean button = true;
            while(line != null){
                String[] stringArray = line.split(" ");
                // Instantiates dimensions, tipper, and goal
                if(button) {
                    dimensions[0] = Integer.parseInt(stringArray[0]);
                    dimensions[1] = Integer.parseInt(stringArray[1]);
                    tipper[0] = Integer.parseInt(stringArray[2]);
                    tipper[1] = Integer.parseInt(stringArray[3]);
                    goal[0] = Integer.parseInt(stringArray[4]);
                    goal[1] = Integer.parseInt(stringArray[5]);
                    grid = new int[dimensions[0]][dimensions[1]];
                    button = false;
                }
                // Instantiates grid
                else {
                    if (row < dimensions[0]) {
                        for (int c = 0; c < dimensions[1]; c++) {
                            grid[row][c] = Integer.parseInt(stringArray[c]);
                        }
                        row++;
                    }
                }
                line = bf.readLine();
            }
            fileLoad = true;
        }
        catch(IOException e){
            fileLoad = false;
        }
    }

    /**
     * Checks if the coordinates for goal and tipper is the same
     * @return true if both are equal
     */
    @Override
    public boolean isSolution() {
        return goal[0] == tipper[0] & goal[1] == tipper[1];
    }

    /**
     * Makes a temporary grid
     * @param grid
     * @return a copy of the previous grid
     */
    public int[][] duplicateGrid(int[][] grid) {
        int[][] tempGrid = new int[grid.length][grid[0].length];
        for(int r = 0; r < grid.length; r++) {
            for(int c = 0; c < grid[0].length; c++) {
                tempGrid[r][c] = grid[r][c];
            }
        }
        return tempGrid;
    }

    /**
     * Gets the neighbors of the current tipper position (North, South, East, West)
     * neighbors depend on whether the current tipper position is on a crate or a tower
     * @return a list of all neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        /** If tipper is on a tower */
        if(grid[tipper[0]][tipper[1]] > 1) {
            /** Checks for surroundings for possible spaces to hop to */
            //North
            if(tipper[0] - 1 >= 0 && grid[tipper[0] - 1][tipper[1]] >= 1) {
                int[] tempTipper = {tipper[0] - 1, tipper[1]};
                neighbors.add(new TipOverConfig(grid, goal, tempTipper));
            }
            // South
            if(tipper[0] + 1 < grid.length && grid[tipper[0] + 1][tipper[1]] >= 1) {
                int[] tempTipper = {tipper[0] + 1, tipper[1]};
                neighbors.add(new TipOverConfig(grid, goal, tempTipper));
            }
            // West
            if(tipper[1] - 1 >= 0 && grid[tipper[0]][tipper[1] - 1] >= 1) {
                int[] tempTipper = {tipper[0], tipper[1] - 1};
                neighbors.add(new TipOverConfig(grid, goal, tempTipper));
            }
            // East
            if(tipper[1] + 1 < grid[0].length && grid[tipper[0]][tipper[1] + 1] >= 1) {
                int[] tempTipper = {tipper[0], tipper[1] + 1};
                neighbors.add(new TipOverConfig(grid, goal, tempTipper));
            }
            /** Checks for surroundings to tip over the tower */
            // North
            if(tipper[0] - 1 >= 0 && grid[tipper[0] - 1][tipper[1]] == 0) {
                int changeNum = 0;
                int[][] tempGrid = duplicateGrid(grid);
                tempGrid[tipper[0] - 1][tipper[1]] = 1;
                int[] tempTipper = {tipper[0] - 1, tipper[1]};
                changeNum++;
                for(int i = 2; i <= grid[tipper[0]][tipper[1]]; i++){
                    if(tipper[0] - i >= 0 && grid[tipper[0] - i][tipper[1]] == 0) {
                        tempGrid[tipper[0] - i][tipper[1]] = 1;
                        changeNum++;
                    }
                }
                tempGrid[tipper[0]][tipper[1]] = 0;
                if(changeNum == grid[tipper[0]][tipper[1]]) {
                    neighbors.add(new TipOverConfig(tempGrid, goal, tempTipper));
                }
            }
            // South
            if(tipper[0] + 1 < grid.length && grid[tipper[0] + 1][tipper[1]] == 0) {
                int changeNum = 0;
                int[][] tempGrid = duplicateGrid(grid);
                tempGrid[tipper[0] + 1][tipper[1]] = 1;
                int[] tempTipper = {tipper[0] + 1, tipper[1]};
                changeNum++;
                for(int i = 2; i <= grid[tipper[0]][tipper[1]]; i++){
                    if(tipper[0] + i < grid.length && grid[tipper[0] + i][tipper[1]] == 0) {
                        tempGrid[tipper[0] + i][tipper[1]] = 1;
                        changeNum++;
                    }
                }
                tempGrid[tipper[0]][tipper[1]] = 0;
                if(changeNum == grid[tipper[0]][tipper[1]]) {
                    neighbors.add(new TipOverConfig(tempGrid, goal, tempTipper));
                }
            }
            // West
            if(tipper[1] - 1 >= 0 && grid[tipper[0]][tipper[1] - 1] == 0) {
                int changeNum = 0;
                int[][] tempGrid = duplicateGrid(grid);
                tempGrid[tipper[0]][tipper[1] - 1] = 1;
                int[] tempTipper = {tipper[0], tipper[1] - 1};
                changeNum++;
                for(int i = 2; i <= grid[tipper[0]][tipper[1]]; i++){
                    if(tipper[1] - i >= 0 && grid[tipper[0]][tipper[1] - i] == 0) {
                        tempGrid[tipper[0]][tipper[1] - i] = 1;
                        changeNum++;
                    }
                }
                tempGrid[tipper[0]][tipper[1]] = 0;
                if(changeNum == grid[tipper[0]][tipper[1]]) {
                    neighbors.add(new TipOverConfig(tempGrid, goal, tempTipper));
                }
            }
            // East
            if(tipper[1] + 1 < grid[0].length && grid[tipper[0]][tipper[1] + 1] == 0) {
                int changeNum = 0;
                int[][] tempGrid = duplicateGrid(grid);
                tempGrid[tipper[0]][tipper[1] + 1] = 1;
                int[] tempTipper = {tipper[0], tipper[1] + 1};
                changeNum++;
                for(int i = 2; i <= grid[tipper[0]][tipper[1]]; i++){
                    if(tipper[1] + i < grid[0].length && grid[tipper[0]][tipper[1] + i] == 0) {
                        tempGrid[tipper[0]][tipper[1] + i] = 1;
                        changeNum++;
                    }
                }
                tempGrid[tipper[0]][tipper[1]] = 0;
                if(changeNum == grid[tipper[0]][tipper[1]]) {
                    neighbors.add(new TipOverConfig(tempGrid, goal, tempTipper));
                }
            }
        }
        else{
            //North
            if(tipper[0] - 1 >= 0 && grid[tipper[0] - 1][tipper[1]] >= 1) {
                int[] tempTipper = {tipper[0] - 1, tipper[1]};
                neighbors.add(new TipOverConfig(grid, goal, tempTipper));
            }
            // South
            if(tipper[0] + 1 < grid.length && grid[tipper[0] + 1][tipper[1]] >= 1) {
                int[] tempTipper = {tipper[0] + 1, tipper[1]};
                neighbors.add(new TipOverConfig(grid, goal, tempTipper));
            }
            // West
            if(tipper[1] - 1 >= 0 && grid[tipper[0]][tipper[1] - 1] >= 1) {
                int[] tempTipper = {tipper[0], tipper[1] - 1};
                neighbors.add(new TipOverConfig(grid, goal, tempTipper));
            }
            // East
            if(tipper[1] + 1 < grid[0].length && grid[tipper[0]][tipper[1] + 1] >= 1) {
                int[] tempTipper = {tipper[0], tipper[1] + 1};
                neighbors.add(new TipOverConfig(grid, goal, tempTipper));
            }
        }
        return neighbors;
    }


    public int getGridRow(){ return grid.length; } // Gets number of rows

    public int getGridCol() {
        return grid[0].length;
    } // Gets number of columns

    public int getGoalX(){
        return goal[0];
    } // Gets x coord of goal

    public int getGoalY(){
        return goal[1];
    } // Gets y coord of goal

    public int getTipperX(){
        return tipper[0];
    } // Gets x coord of tipper

    public int getTipperY(){
        return tipper[1];
    } // Gets y coord of tipper

    public int getTipperValue(int r, int c) {
        return grid[r][c];
    } // Value of the grid based on tipper position

    /**
     * makeMove reads the direction from the parameter and checks whether the user wants to move north, south, east, west.
     * Helper methods for each direction is ran.
     * @param direction (String for n, s, e, w)
     * @return value from helper methods
     */
    public TipOverConfig makeMove(String direction){
        if(isSolution()) {
           alreadySolved = true;
           return this;
        }
        outOfBounds = false;
        tipOverTower = false;
        noCrate = false;
        cantTip = false;
        direction = direction.toLowerCase();
        // North
        if(direction.equals("n")) {
            return goNorth();
        }
        // South
        if(direction.equals("s")) {
            return goSouth();
        }
        // East
        if(direction.equals("e")) {
            return goEast();
        }
        // West
        else {
            return goWest();
        }
    }

    /** Returns whether the configuration is solved */
    public boolean isSolved() {
        return solved;
    }

    /** Returns whether the config is already solved */
    public boolean isAlreadySolved() {
        return alreadySolved;
    }

    /** Returns if the move cant tip over a tower */
    public boolean isCantTip() {
        return cantTip;
    }

    /** Returns if the move is trying to move to an empty crate */
    public boolean isNoCrate() {
        return noCrate;
    }

    /** Returns if the move is going out of bounds */
    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    /** Returns if move tips over a tower */
    public boolean isTipOverTower() {
        return tipOverTower;
    }


    /**
     * This helper method checks the validity of the move north. If valid, a new config is made with the move forward.
     * Otherwise, some condition is passed.
     * @return TipOverConfig configuration
     */
    public TipOverConfig goNorth() {
        if(solved) {
            alreadySolved = true;
            return this;
        }
        // On a Tower
        if(grid[tipper[0]][tipper[1]] > 1) {
            if(tipper[0] - 1 >= 0) {
                if(grid[tipper[0] - 1][tipper[1]] >= 1) {
                    int[] tempTipper = {tipper[0] - 1, tipper[1]};
                    TipOverConfig tp = new TipOverConfig(grid, goal, tempTipper);
                    if(tp.isSolution()) {
                        tp.solved = true;
                    }
                    return tp;
                }
                else if(grid[tipper[0] - 1][tipper[1]] == 0) {
                    int changeNum = 0;
                    int[][] tempGrid = duplicateGrid(grid);
                    tempGrid[tipper[0] - 1][tipper[1]] = 1;
                    int[] tempTipper = {tipper[0] - 1, tipper[1]};
                    changeNum++;
                    for(int i = 2; i <= grid[tipper[0]][tipper[1]]; i++){
                        if(tipper[0] - i >= 0 && grid[tipper[0] - i][tipper[1]] == 0) {
                            tempGrid[tipper[0] - i][tipper[1]] = 1;
                            changeNum++;
                        }
                    }
                    tempGrid[tipper[0]][tipper[1]] = 0;
                    if(changeNum == grid[tipper[0]][tipper[1]]) {
                        TipOverConfig tp = new TipOverConfig(tempGrid, goal, tempTipper);
                        tp.tipOverTower = true;
                        if(tp.isSolution()) {
                            tp.solved = true;
                        }
                        return tp;
                    }
                    else{
                        cantTip = true;
                        return this;
                    }
                }
                else {
                    tipOverTower = false;
                    return this;
                }
            }
            else{
                outOfBounds = true;
                return this;
            }
        }
        // On a Crate
        else {
            if (tipper[0] - 1 >= 0) {
                if (grid[tipper[0] - 1][tipper[1]] >= 1) {
                    int[] tempTipper = {tipper[0] - 1, tipper[1]};
                    TipOverConfig tp = new TipOverConfig(grid, goal, tempTipper);
                    if(tp.isSolution()) {
                        tp.solved = true;
                    }
                    return tp;
                }
                else{
                    noCrate = true;
                    return this;
                }
            }
            else{
                outOfBounds = true;
            }
        }
        return this;
    }
    /**
     * This helper method checks the validity of the move south. If valid, a new config is made with the move backwards.
     * Otherwise, some condition is passed.
     * @return TipOverConfig configuration
     */
    public TipOverConfig goSouth() {
        if(solved) {
            alreadySolved = true;
            return this;
        }
        // On a Tower
        if(grid[tipper[0]][tipper[1]] > 1) {
            if(tipper[0] + 1 < grid.length) {
                if(grid[tipper[0] + 1][tipper[1]] >= 1) {
                    int[] tempTipper = {tipper[0] + 1, tipper[1]};
                    TipOverConfig tp = new TipOverConfig(grid, goal, tempTipper);
                    if(tp.isSolution()) {
                        tp.solved = true;
                    }
                    return tp;
                }
                else if(grid[tipper[0] + 1][tipper[1]] == 0) {
                    int changeNum = 0;
                    int[][] tempGrid = duplicateGrid(grid);
                    tempGrid[tipper[0] + 1][tipper[1]] = 1;
                    int[] tempTipper = {tipper[0] + 1, tipper[1]};
                    changeNum++;
                    for(int i = 2; i <= grid[tipper[0]][tipper[1]]; i++){
                        if(tipper[0] + i < grid.length && grid[tipper[0] + i][tipper[1]] == 0) {
                            tempGrid[tipper[0] + i][tipper[1]] = 1;
                            changeNum++;
                        }
                    }
                    tempGrid[tipper[0]][tipper[1]] = 0;
                    if(changeNum == grid[tipper[0]][tipper[1]]) {
                        TipOverConfig tp = new TipOverConfig(tempGrid, goal, tempTipper);
                        tp.tipOverTower = true;
                        if(tp.isSolution()) {
                            tp.solved = true;
                        }
                        return tp;
                    }
                    else {
                        cantTip = true;
                        return this;
                    }
                }
                else {
                    tipOverTower = false;
                    return this;
                }
            }
            else{
                outOfBounds = true;
                return this;
            }
        }
        // On a crate
        else {
            if (tipper[0] + 1 < grid.length) {
                if (grid[tipper[0] + 1][tipper[1]] >= 1) {
                    int[] tempTipper = {tipper[0] + 1, tipper[1]};
                    TipOverConfig tp = new TipOverConfig(grid, goal, tempTipper);
                    if(tp.isSolution()) {
                        tp.solved = true;
                    }
                    return tp;
                }
                else{
                    noCrate = true;
                    return this;
                }
            }
            else{
                outOfBounds = true;
            }
        }
        return this;
    }
    /**
     * This helper method checks the validity of the move east. If valid, a new config is made with the move right.
     * Otherwise, some condition is passed.
     * @return TipOverConfig configuration
     */
    public TipOverConfig goEast() {
        if(solved) {
            alreadySolved = true;
            return this;
        }
        // On a Tower
        if(grid[tipper[0]][tipper[1]] > 1) {
            if(tipper[1] + 1 < grid[0].length) {
                if(grid[tipper[0]][tipper[1] + 1] >= 1) {
                    int[] tempTipper = {tipper[0], tipper[1] + 1};
                    TipOverConfig tp = new TipOverConfig(grid, goal, tempTipper);
                    if(tp.isSolution()) {
                        tp.solved = true;
                    }
                    return tp;
                }
                else if(grid[tipper[0]][tipper[1] + 1] == 0) {
                    int changeNum = 0;
                    int[][] tempGrid = duplicateGrid(grid);
                    tempGrid[tipper[0]][tipper[1] + 1] = 1;
                    int[] tempTipper = {tipper[0], tipper[1] + 1};
                    changeNum++;
                    for(int i = 2; i <= grid[tipper[0]][tipper[1]]; i++){
                        if(tipper[1] + i < grid[0].length && grid[tipper[0]][tipper[1] + i] == 0) {
                            tempGrid[tipper[0]][tipper[1] + i] = 1;
                            changeNum++;
                        }
                    }
                    tempGrid[tipper[0]][tipper[1]] = 0;
                    if(changeNum == grid[tipper[0]][tipper[1]]) {
                        TipOverConfig tp = new TipOverConfig(tempGrid, goal, tempTipper);
                        tp.tipOverTower = true;
                        if(tp.isSolution()) {
                            tp.solved = true;
                        }
                        return tp;
                    }
                    else {
                        cantTip = true;
                        return this;
                    }
                }
                else {
                    tipOverTower = false;
                    return this;
                }
            }
            else{
                outOfBounds = true;
                return this;
            }
        }
        // On a Crate
        else {
            if(tipper[1] + 1 < grid[0].length) {
                if(grid[tipper[0]][tipper[1] + 1] >= 1) {
                    int[] tempTipper = {tipper[0], tipper[1] + 1};
                    TipOverConfig tp = new TipOverConfig(grid, goal, tempTipper);
                    if(tp.isSolution()) {
                        tp.solved = true;
                    }
                    return tp;
                }
                else{
                    noCrate = true;
                    return this;
                }
            }
            else{
                outOfBounds = true;
            }
        }
        return this;
    }
    /**
     * This helper method checks the validity of the move west. If valid, a new config is made with the move left.
     * Otherwise, some condition is passed.
     * @return TipOverConfig configuration
     */
    public TipOverConfig goWest() {
        if(solved) {
            alreadySolved = true;
            return this;
        }
        // On a Tower
        if(grid[tipper[0]][tipper[1]] > 1) {
            if(tipper[1] - 1 >= 0) {
                if(grid[tipper[0]][tipper[1] - 1] >= 1) {
                    int[] tempTipper = {tipper[0], tipper[1] - 1};
                    TipOverConfig tp = new TipOverConfig(grid, goal, tempTipper);
                    if(tp.isSolution()) {
                        tp.solved = true;
                    }
                    return tp;
                }
                else if(grid[tipper[0]][tipper[1] - 1] == 0) {
                    int changeNum = 0;
                    int[][] tempGrid = duplicateGrid(grid);
                    tempGrid[tipper[0]][tipper[1] - 1] = 1;
                    int[] tempTipper = {tipper[0], tipper[1] - 1};
                    changeNum++;
                    for(int i = 2; i <= grid[tipper[0]][tipper[1]]; i++){
                        if(tipper[1] - i >= 0 && grid[tipper[0]][tipper[1] - i] == 0) {
                            tempGrid[tipper[0]][tipper[1] - i] = 1;
                            changeNum++;
                        }
                    }
                    tempGrid[tipper[0]][tipper[1]] = 0;
                    if(changeNum == grid[tipper[0]][tipper[1]]) {
                        TipOverConfig tp = new TipOverConfig(tempGrid, goal, tempTipper);
                        tp.tipOverTower = true;
                        if(tp.isSolution()) {
                            tp.solved = true;
                        }
                        return tp;
                    }
                    else {
                        cantTip = true;
                        return this;
                    }
                }
                else {
                    tipOverTower = false;
                    return this;
                }
            }
            else{
                outOfBounds = true;
                return this;
            }
        }
        // On a Crate
        else {
            if (tipper[1] - 1 >= 0) {
                if(grid[tipper[0]][tipper[1] - 1] >= 1) {
                    int[] tempTipper = {tipper[0], tipper[1] - 1};
                    TipOverConfig tp = new TipOverConfig(grid, goal, tempTipper);
                    if(tp.isSolution()) {
                        tp.solved = true;
                    }
                    return tp;
                }
                else{
                    noCrate = true;
                    return this;
                }
            }
            else{
                outOfBounds = true;
            }
        }
        return this;
    }

    /**
     * Checks whether the objects are equal
     * @param other , the object being equaled to
     * @return true if the objects are equal and false if it's not equal
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof TipOverConfig toc) {
            for(int r  = 0; r < grid.length; r++) {
                for(int c = 0; c < grid[0].length; c++) {
                    if(this.grid[r][c] != toc.grid[r][c]) {
                        return false;
                    }
                }
            }
            if(!Arrays.equals(this.tipper, toc.tipper)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Creates a hashcode for the predecessor table
     * @return hashcode of tipper and goal
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(tipper) + Arrays.hashCode(goal);
    }


    /**
     * this toString builds the string representation of the current config
     * @return string variable representation of the board
     */
    @Override
    public String toString() {
        String output ="\n    ";
        for(int i = 0; i < grid[0].length; i++) {
            output += "  " + i;
        }
        output += "\n    ";
        for(int i = 0; i < grid[0].length; i++) {
            output += "___";
        }
        output += "\n";
        for(int r = 0; r < grid.length; r++) {
            output += " " + r + " " + "| ";
            for (int c = 0; c < grid[0].length; c++) {
                if(grid[r][c] == 0) {
                    output += " _ ";
                }
                else if (tipper[0] == r & tipper[1] == c) {
                    output += "*" + grid[r][c] + " ";
                }
                else if(goal[0] == r & goal[1] == c) {
                    output += "!" + grid[r][c] + " ";
                }
                else {
                    output += " " + grid[r][c] + " ";
                }
            }
            output += "\n";
        }
        return output;
    }
}
