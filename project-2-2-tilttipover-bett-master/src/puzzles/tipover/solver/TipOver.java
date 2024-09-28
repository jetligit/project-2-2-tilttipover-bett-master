package puzzles.tipover.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tipover.model.TipOverConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * TipOver handles the file reading and initializing the grid, tipper position, goal, and board. After, TipOverConfig,
 * Solver are initialized to find a solution.
 * @author Benson Zhou
 */
public class TipOver {
    private int[] grid = {0,0}; /** Coordinates of board */
    private static int[] tipper = {0,0}; /** Tipper position */
    private static int[] goal = {0,0}; /** Goal position */
    private static int[][] board; /** 2d Array holding the values of the board of a file */
    /**
     * Main sends args to a TipOver object to read the file. Objects of TipOverConfig and Solver are made to initialize
     * a path towards the solution of the given file. If the path is null, main prints no solution and quits the program.
     * Otherwise, each step towards the goal is printed.
     * @param args (arguments from configurations)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOver filename");
        }
        else{
            int i = 0;
            TipOver tp = new TipOver(args[0]);
            TipOverConfig tpc = new TipOverConfig(board, goal, tipper);
            System.out.println(tpc);
            Solver solve = new Solver();
            List<Configuration> path = solve.BFS(tpc);
            System.out.println("Total configs: " + solve.getTotal());
            System.out.println("Unique configs: " + solve.getUnique());
            if(path.contains(null)) {
                System.out.println("No Solution");
                System.exit(0);
            }
            for(Configuration c : path) {
                System.out.println("Step " + i + ": " + c.toString());
                i++;
            }
        }
    }

    /**
     * TipOver Constructor: reads the file using a BufferedReader. The first line gets split and allots the first two
     * digits to grid, next two to tipper, and the final two to goal. After the first line, the board is initialized.
     * If a file doesn't exist, IOEXception catches that.
     * @param filename (filename of the file in args)
     */
    public TipOver(String filename){
        try{
            int row = 0;
            BufferedReader bf = new BufferedReader(new FileReader(filename));
            String line = bf.readLine();
            boolean button = true;
            while(line != null){
                String[] stringArray = line.split(" ");
                //initializing grid, tipper, goal
                if(button) {
                    grid[0] = Integer.parseInt(stringArray[0]);
                    grid[1] = Integer.parseInt(stringArray[1]);
                    tipper[0] = Integer.parseInt(stringArray[2]);
                    tipper[1] = Integer.parseInt(stringArray[3]);
                    goal[0] = Integer.parseInt(stringArray[4]);
                    goal[1] = Integer.parseInt(stringArray[5]);
                    board = new int[grid[0]][grid[1]];
                    button = false;
                }
                //Initializing board
                else {
                    if (row < grid[0]) {
                        for (int c = 0; c < grid[1]; c++) {
                            board[row][c] = Integer.parseInt(stringArray[c]);
                        }
                        row++;
                    }
                }
                line = bf.readLine();
            }
        }
        catch(IOException e){
            System.out.println("File doesn't exist.");
        }
    }
}
