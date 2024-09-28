package puzzles.water;
import puzzles.common.solver.*;

import java.util.*;

/**
 * WaterConfig implements WaterConfig objects and has methods that check whether the object is the solution and gets neighbors of
 * those config objects
 * Author: Benson Zhou and Jet Li
 */
public class WaterConfig implements Configuration{
    private int amount; /** goal: number of gallons for one of the buckets to be holding */
    private ArrayList<Integer> buckets; /** buckets that are manipulated to reach the amount */
    private ArrayList<Integer> maxBuckets; /** max amounts of water per bucket */

    /**
     * Constructor to construct WaterConfig objects with amount, buckets, maxbuckets
     * @param amount , number of gallons a bucket has to be holding
     * @param buckets , list of buckets being changed
     * @param maxBuckets , max amounts of water per bucket
     */
    public WaterConfig(int amount, ArrayList<Integer> buckets, ArrayList<Integer> maxBuckets) {
        this.amount = amount;
        this.buckets = buckets;
        this.maxBuckets = maxBuckets;
    }


    /**
     * Makes all possible neighbors for a single configuration object.
     * Possibility 1: A bucket can be emptied if it isn't empty already
     * Possibility 2: A bucket can be filled if it isn't filled to the max already
     * Possibility 3: A bucket can be poured into another bucket if it is not empty and not the same bucket and the
     * bucket we're pouring into isn't full
     * @return
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        for(int i = 0; i < buckets.size(); i++) {
            /**
             * 1st Possibility
             */
            if(buckets.get(i) != 0) {
                ArrayList<Integer> temp1 = new ArrayList<>(buckets);
                temp1.set(i, 0);
                neighbors.add(new WaterConfig(amount, temp1, maxBuckets));
            }
            /**
             * 2nd Possibility
             */
            if(!buckets.get(i).equals(maxBuckets.get(i))) {
                ArrayList<Integer> temp2 = new ArrayList<>(buckets);
                temp2.set(i, maxBuckets.get(i));
                neighbors.add(new WaterConfig(amount, temp2, maxBuckets));
            }
            /**
             * 3rd Possibility
             */
            if(buckets.get(i) != 0) {
                for(int c = 0; c < buckets.size(); c++) {
                    ArrayList<Integer> temp3 = new ArrayList<>(buckets);
                    /**
                     * Makes sure the bucket isn't equal to each other
                     */
                    if(c != i) {
                        /**
                         * Makes sure the bucket being poured into isn't full
                         */
                        if(!buckets.get(c).equals(maxBuckets.get(c))){
                            int difference = maxBuckets.get(c) - buckets.get(c);
                            if(buckets.get(i) > difference) {
                                temp3.set(i, buckets.get(i) - difference);
                                temp3.set(c, maxBuckets.get(c));
                            }
                            else{
                                temp3.set(c, buckets.get(i) + buckets.get(c));
                                temp3.set(i, 0);
                            }
                            neighbors.add(new WaterConfig(amount, temp3, maxBuckets));
                        }
                    }
                }
            }
        }
        /**
         * Returns all neighbors
         */
        return neighbors;
    }

    /**
     * Checks if any of the buckets in buckets has amount
     * @return true if solution is true
     */
    @Override
    public boolean isSolution() {
        return buckets.contains(amount);
    }

    /**
     * Gets the value from amount
     * @return amount
     */
    public int getAmount() {
        return amount;
    }
    /**
     * Gets the list from buckets
     * @return buckets
     */
    public ArrayList<Integer> getBuckets() {
        return buckets;
    }

    /**
     * Gets the list from maxBuckets
     * @return maxBuckets
     */
    public ArrayList<Integer> getMaxBuckets() {
        return maxBuckets;
    }

    /**
     * Returns the bucket with an empty string to be printed
     * @return empty string + buckets
     */
    @Override
    public String toString() {
        return "" + buckets;
    }

    /**
     * Checks whether the objects are equal
     * @param obj , the object being equaled to
     * @return true if the objects are equal and false if it's not equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WaterConfig water){
            for (int i = 0; i < this.buckets.size(); i++){
                if (!water.buckets.get(i).equals(this.buckets.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Creates a hashcode for the predecessor table
     * @return int of amount + hashcode of buckets and maxbuckets
     */
    @Override
    public int hashCode() {
        return amount + buckets.hashCode() + maxBuckets.hashCode();
    }
}
