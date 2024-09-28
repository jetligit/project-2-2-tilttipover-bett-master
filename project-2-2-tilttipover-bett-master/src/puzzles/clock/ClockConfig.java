package puzzles.clock;

import puzzles.common.solver.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ClockConfig makes config objects and has methods that check whether the object is the solution and gets neighbors of
 * those config objects
 * Author: Benson Zhou and Jet Li
 */
public class ClockConfig implements Configuration {
    private int hours; /** Max hours start can hit before resetting to 1 */
    private int start; /** Start/current hour */
    private int end; /** Goal for start to reach */

    /**
     * Constructor to construct ClockConfig objects with hours, start, end parameter
     * @param hours , max number of hours
     * @param start , time it starts
     * @param end , goal for start to reach
     */
    public ClockConfig(int hours, int start, int end) {
        this.hours = hours;
        this.start = start;
        this.end = end;
    }

    /**
     * getNeighbors gets the neighbors of the object that's calling this method
     * @return all neighbors holding ClockConfig objects that are neighbors to the original node.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        int leftN = start - 1;
        int rightN = start + 1;
        if(leftN == 0) {
            leftN = hours;
        }
        if(rightN > hours){
            rightN = 1;
        }
        ClockConfig ccL = new ClockConfig(hours, leftN, end);
        ClockConfig ccR = new ClockConfig(hours, rightN, end);
        neighbors.add(ccL);
        neighbors.add(ccR);
        return neighbors;
    }

    /**
     * Checks whether the object calling this method has the start equal to the end
     * @return true if solution is matched, false if solution is the solution
     */
    @Override
    public boolean isSolution() {
        return start == end;
    }

    /**
     * Checks whether the objects are equal
     * @param obj , the object being equaled to
     * @return true if the objects are equal and false if it's not equal
     */
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ClockConfig) {
            ClockConfig objClock = (ClockConfig) obj;
            return this.end == objClock.end && this.hours == objClock.hours && this.start == objClock.start;
        }
        return result;
    }

    /**
     * Creates a hashcode for the predecessor table
     * @return int of hours + end + start
     */
    @Override
    public int hashCode() {
        return hours + end + start;
    }

    /**
     * prints the start value
     * @return the start with an empty string so it can be printed
     */
    @Override
    public String toString() {
        return "" + start;
    }
}
