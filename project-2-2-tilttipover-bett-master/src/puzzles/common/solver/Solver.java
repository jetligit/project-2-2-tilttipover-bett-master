package puzzles.common.solver;

import java.util.*;

/**
 * Solver is the main program that handles BFS. It isn't specific for any program and handles the general procedure.
 * Author: Benson Zhou and Jet Li
 */
public class Solver {
    private List<Configuration> path = new LinkedList<>(); /** Shortest path to the goal */
    private Map<Configuration, Configuration> predTable = new HashMap<>(); /** Holds all visited Config objects so cycling doesn't occur */
    private List<Configuration> queue = new LinkedList<>(); /** Holds all Config objects to get neighbors for, for BFS */
    Configuration finalC = null; /** The goal node, assigned during the BFS program */
    private int total; /** Total amount of configurations made */
    private int unique; /** Unique amount of configurations made */

    /**
     * Returns total amount of configs
     * @return total variable
     */
    public int getTotal() {
        return total;
    }

    /**
     * Returns total amount of UNIQUE configs
     * @return unique variable
     */
    public int getUnique() {
        return unique;
    }

    /**
     * This is the heart of Solver, BFS (Breadth First Search). Taking in a configuration through its parameters,
     * the program performs a breadth first search using, queue, current, and a predecessor table. After finding the solution,
     * the shortest path is searched for and construct path is run.
     * @param cc
     * @return
     */
    public List<Configuration> BFS(Configuration cc) {
        total = 0;
        unique = 0;
        predTable.put(cc, null);
        queue.add(cc);
        total++;
        if (cc.isSolution()) {
            /**
             * Checks the solution, if true: finalc is assigned to parameter config and construct path is returned.
             */
            unique = predTable.size();
            finalC = cc;
            return constructPath(cc, cc);
        }
        /**
         * While loop for going through all the values in queue.
         */
        while (!queue.isEmpty()) {
            Configuration numNode = queue.remove(0);
            /**
             * Checks for solution, breaks while loop if found
             */
            if (numNode.isSolution()) {
                finalC = numNode;
                break;
            }
            /**
             * Neighbors are gotten and added to predecessor table if not previously visited, otherwise, just the total
             * config is added. The queue is updated by neighbors.
             */
            for (Configuration ccn : numNode.getNeighbors()) {
                total++;
                if (!predTable.containsKey(ccn)) {
                    predTable.put(ccn, numNode);
                    queue.add(ccn);
                }
            }

        }
        unique = predTable.size();
        return constructPath(cc, finalC);
    }

    /**
     * @param start , returned original node
     * @param finalC , returned solution node
     * @return path
     */
    public List<Configuration> constructPath(Configuration start, Configuration finalC) {
        /**
         * path is constructed with the predecessor map
         */
        path.add(0, finalC);
        Configuration former = predTable.get(finalC);
        while (former != null) {
            path.add(0, former);
            former = predTable.get(former);
        }
        return path;
    }
}
