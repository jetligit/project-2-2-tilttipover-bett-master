package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Benson Zhou and Jet Li
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     * Prints arguments, total/unique configs and steps done to get to goal
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            // TODO YOUR MAIN CODE HERE
            ArrayList<Integer> maxBuckets = new ArrayList<>();
            for(int i = 1; i < args.length; i++) {
                maxBuckets.add(Integer.parseInt(args[i]));
            }
            int size = maxBuckets.size();
            ArrayList<Integer> empty = new ArrayList<>();
            for(int i = 0; i < size; i++){
                empty.add(0);
            }
            WaterConfig waterC = new WaterConfig(Integer.parseInt(args[0]), empty, maxBuckets);
            System.out.print("Amount: " + waterC.getAmount() + ", ");
            System.out.println("Buckets: "+waterC.getMaxBuckets());
            Solver solve = new Solver();
            List<Configuration> path = solve.BFS(waterC);
            System.out.println("Total configs: " + solve.getTotal());
            System.out.println("Unique configs: " + solve.getUnique());
            if(path.contains(null)) {
                System.out.println("No Solution");
                System.exit(0);
            }
            int i = 0;
            for(Configuration c : path) {
                System.out.println("Step " + i + ": " + c.toString());
                i++;
            }
        }
    }
}
