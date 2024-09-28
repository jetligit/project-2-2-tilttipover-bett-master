package puzzles.tilt.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/** the class used to represent the game graphically
 * Author: Jet Li */
public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    /** the green piece as an image */
    private Image green = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));
    /** the block piece as an image */
    private Image block = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"));
    /** the blue piece as an image */
    private Image blue = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"));
    /** the hole piece as an image */
    private Image hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"));
    /** the message that describes what action is being done */
    private Label message;
    /** the dimension of the board */
    private int theDimension;
    /** the model */
    private TiltModel model;
    /** the fileName as a String used for resetting */
    private String fileName;
    /** the inner border of the display */
    private BorderPane innerBorder;
    /** the gridPane */
    private GridPane gridPane;

    /** creates a new model object and initializes the filename */
    public void init() throws FileNotFoundException {
        String filename = getParameters().getRaw().get(0);
        this.fileName = filename;
        this.model = new TiltModel(filename);
        model.addObserver(this);
    }

    /** displays the new board after it has been updated */
    public void displayBoard(){
        gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        theDimension = model.getCurrentConfig().getDIMENSION();
        for (int r = 0; r < theDimension; r++){
            for (int c = 0; c < theDimension; c++){
                char theElement = model.getCurrentConfig().getElement(r, c);
                ImageView image;
                if (theElement == 'G') {
                    image = new ImageView(green);
                }
                else if (theElement == 'B') {
                    image = new ImageView(blue);
                }
                else if (theElement == 'O') {
                    image = new ImageView(hole);
                }
                else if (theElement == '*') {
                    image = new ImageView(block);
                }
                else {
                    image = new ImageView();
                }
                image.setFitWidth(75);
                image.setFitHeight(75);
                gridPane.add(image, c, r);
            }
        }

        this.innerBorder.setCenter(gridPane);
    }

    /**
     * The method responsible for creating the layout of the GUI, calling displayBoard when
     * the board is updated, and setting the message based on the action done
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Tilt");
        message = new Label("");
        BorderPane outerBorder = new BorderPane();
        outerBorder.setTop(message);

        innerBorder = new BorderPane();
        outerBorder.setCenter(innerBorder);

        Button up = new Button("^");
        up.setOnAction(event -> {
            if (model.gameOver()){
                message.setText("Already solved!");
                displayBoard();
            }
            else {
                if (model.getCurrentConfig().isValid("N")) {
                    model.setCurrentConfig(model.getCurrentConfig().makeMove("N"));
                    displayBoard();
                } else {
                    message.setText("Illegal move. A blue slider will fall through the hole!");
                    displayBoard();
                }
            }
        });

        up.setPrefWidth(200);
        innerBorder.setTop(up);
        innerBorder.setAlignment(up, Pos.CENTER);

        Button right = new Button(">");
        right.setOnAction(event -> {
            if (model.gameOver()){
                message.setText("Already solved!");
                displayBoard();
            }
            else {
                if (model.getCurrentConfig().isValid("E")) {
                    model.setCurrentConfig(model.getCurrentConfig().makeMove("E"));
                    displayBoard();
                } else {
                    message.setText("Illegal move. A blue slider will fall through the hole!");
                    displayBoard();
                }
            }
        });
        right.setPrefHeight(200);
        innerBorder.setRight(right);
        innerBorder.setAlignment(right, Pos.CENTER);

        Button down = new Button("v");
        down.setOnAction(event -> {
            if (model.gameOver()){
                message.setText("Already solved!");
                displayBoard();
            }
            else {
                if (model.getCurrentConfig().isValid("S")) {
                    model.setCurrentConfig(model.getCurrentConfig().makeMove("S"));
                    displayBoard();
                } else {
                    message.setText("Illegal move. A blue slider will fall through the hole!");
                    displayBoard();
                }
            }
        });
        down.setPrefWidth(200);
        innerBorder.setBottom(down);
        innerBorder.setAlignment(down, Pos.CENTER);

        Button left = new Button("<");
        left.setOnAction(event -> {
            if (model.gameOver()){
                message.setText("Already solved!");
                displayBoard();
            }
            else {
                if (model.getCurrentConfig().isValid("W")) {
                    model.setCurrentConfig(model.getCurrentConfig().makeMove("W"));
                    displayBoard();
                } else {
                    message.setText("Illegal move. A blue slider will fall through the hole!");
                    displayBoard();
                }
            }
        });
        left.setPrefHeight(200);
        innerBorder.setLeft(left);
        innerBorder.setAlignment(left, Pos.CENTER);

        GridPane gridPane = new GridPane();
        this.gridPane = gridPane;
        Button greenDisk = new Button();
        greenDisk.setGraphic(new ImageView(green));

        Scene scene = new Scene(outerBorder);

        theDimension = model.getCurrentConfig().getDIMENSION();
        for (int r = 0; r < theDimension; r++){
            for (int c = 0; c < theDimension; c++){
                char theElement = model.getCurrentConfig().getElement(r, c);
                ImageView image;
                if (theElement == 'G') {
                    image = new ImageView(green);
                }
                else if (theElement == 'B') {
                    image = new ImageView(blue);

                }
                else if (theElement == 'O') {
                    image = new ImageView(hole);

                }
                else if (theElement == '*') {
                    image = new ImageView(block);
                }
                else {
                    image = new ImageView();
                }
                image.setFitHeight(75);
                image.setFitWidth(75);
                gridPane.add(image, c, r);
            }
        }
        gridPane.setGridLinesVisible(true);
        innerBorder.setCenter(gridPane);

        VBox vbox = new VBox();
        Button loadGame = new Button("Load Game");
        loadGame.setOnAction(event -> {
            //create a new FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load a game board.");
            //open up a window for the user to interact with.
            //set the directory to the boards folder in the current working directory
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tilt"));
            fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            try {

                model.loadBoardFromFile(selectedFile);
                fileName = "data/tilt/" + selectedFile.getName();
                message.setText("Loaded: " + selectedFile);
                stage.setHeight(75 * theDimension + 150);
                stage.setWidth(75 * theDimension + 150);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button newGame = new Button("Reset");
        newGame.setOnAction(event -> {
            try {
                model.loadBoardFromFile(fileName);
                message.setText("Puzzle reset!" + fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button hint = new Button("Hint");
        hint.setOnAction(event -> {
            if (model.gameOver()){
                message.setText("Already solved!");
                displayBoard();
            }
            else{
                model.getHint();
            }
        });
        vbox.getChildren().addAll(newGame, loadGame, hint);

        outerBorder.setRight(vbox);
        outerBorder.setAlignment(vbox, Pos.CENTER);
        stage.setHeight(75 * theDimension + 150);
        stage.setHeight(75 * theDimension + 150);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method responsible for updating the board based on the command specified (which is the action)
     * @param tiltModel the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel tiltModel, String msg) {
        if (msg.equals(TiltModel.LOADED)){ // game is loaded successfully
            message.setText("Loaded: " + this.fileName);
            displayBoard();
            return;
        }else if (msg.equals(TiltModel.LOAD_FAILED)){ //Game failed to load
            message.setText("Failed to load: " + fileName);
            displayBoard();
            return;
        } else if (msg.equals(TiltModel.HINT)) { //Model is reporting a  hint
            message.setText(msg);
            displayBoard();
            return;
        }else if (msg.equals("Already solved!")) { //game is already over
            message.setText(msg);
            displayBoard();
            return;
        }
        else if (msg.equals("No solution!")) { //Model is reporting a  hint
            message.setText(msg);
            displayBoard();
            return;
        }
        displayBoard();
        message.setText(msg);

    }

    /**
     * method responsible for handling it when there are no arguments specified
     * @param args: the arguments in the commandLine
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}