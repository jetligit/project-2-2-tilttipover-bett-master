package puzzles.tilt.solver;

import puzzles.tilt.model.TiltConfig;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Tilt {
    private static char [][] board;
    private static int dimension;
    public static void main(String[] args) throws IOException{
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        //try{
            TiltConfig myTiltC = new TiltConfig(args[0]);
//        }
//        catch(IOException io){
//            System.out.println("The file does not exist!");
//        }
        //TiltConfig myTiltC = new TiltConfig(board);
        System.out.println("File: " + args[0]);
        System.out.print(myTiltC.toString());
        //printing out the original board
//        for (int r = 0; r < dimension; r++){
//            for (int c = 0; c < dimension; c++){
//                System.out.print(board[r][c]);
//                if (c != dimension - 1){
//                    System.out.print(" ");
//                }
//            }
//            System.out.println();
//        }
        Solver solver = new Solver();
        List<Configuration> path = solver.BFS(myTiltC);
        System.out.println("Total configs: " + solver.getTotal());
        System.out.println("Unique configs: " + solver.getUnique());
        if(path.get(0) == null) {
            System.out.println("No Solution");
            System.exit(0);
        }
        int i = 0;
        for(Configuration c : path) {
            System.out.println("Step " + i + ":\n" + c.toString());
            i++;
        }
    }
    public Tilt(String filename) throws IOException {
        try {
            // load data from file
            BufferedReader bf = new BufferedReader(new FileReader(filename));
            // read entire line as string
            String line = bf.readLine();
            dimension = Integer.parseInt(line);
            board = new char[dimension][dimension];
            // checking for end of file
            int counter = 0;
            while (line != null) {
                String[] fields = line.split(" ");
                char[] charFields = new char[fields.length];
                for (int i = 0; i < fields.length; i++){
                    char theCharacter = fields[i].charAt(0);
                    charFields[i] = theCharacter;
                }
                if (fields.length == dimension){
                    System.arraycopy(charFields, 0, board[counter++], 0, dimension);
                }
                line = bf.readLine();
            }
            bf.close();
        } catch (IOException io){
            throw new FileNotFoundException("The file does not exist!");
        }
    }

}
