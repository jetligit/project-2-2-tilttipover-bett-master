package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

/**
 * Main class for the clock puzzle.
 *
 * @author Benson Zhou
 */
public class Clock {
    /**
     * Run an instance of the clock puzzle.
     * Prints arguments, total/unique configs and steps done to get to goal
     * @param args [0]: the number of hours in the clock;
     *             [1]: the starting hour;
     *             [2]: the finish hour.
     */
    public static void main(String[] args) {
        int i = 0;
        if (args.length < 3) {
            System.out.println(("Usage: java Clock hours start finish"));
        } else {
            // TODO YOUR MAIN CODE HERE
            System.out.println("Hours: " + args[0] + ", Start: " + args[1] + ", End: " + args[2]);
            ClockConfig cc = new ClockConfig(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            Solver solve = new Solver();
            List<Configuration> path = solve.BFS(cc);
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
}
