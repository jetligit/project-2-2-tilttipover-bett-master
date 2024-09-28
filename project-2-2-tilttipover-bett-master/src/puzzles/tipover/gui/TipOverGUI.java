package puzzles.tipover.gui;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;


import java.io.File;


/**
 * TipOverGUI creates the GUI view of the game. It's responsible for displaying the board, updating it whenever
 * an action occurs, and presents buttons for users to move the tippers.
 * @author Benson Zhou
 */
public class TipOverGUI extends Application implements Observer<TipOverModel, String> {
    private TextField l; //A textfield
    private TipOverModel model; // TipOverModel object that can access TipOverModel methods
    private GridPane gp; // GridPane object

    private String filename; // Filename
    private BorderPane borderPane; //BorderPane object

    private Stage stage; // Stage object

    /**
     * Instantiates filename, model object, adds an observer, instantiates gridpane object, and makes the config for
     * model using filename
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.model = new TipOverModel();
        this.model.addObserver(this);
        this.filename = filename;
        gp = new GridPane();
        model.makeConfig(filename);
    }

    /**
     * Initializes the view and controller of the MVC pattern for TipOver, which includes borderpane, gridpane,
     * buttons, and text fields.
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        borderPane = new BorderPane();
        l = new TextField();
        l.setText("Message: ");
        Font font2 = new Font(20);
        l.setFont(font2);
        gp = new GridPane();
        // Creates the board
        for(int r = 0; r < model.getGridRow(); r++) {
            for(int c = 0; c < model.getGridCol(); c++) {
                Button b = new Button("" + model.getTipperValue(r, c));
                b.setMinWidth(75);
                b.setMinHeight(75);
                if(model.getTipperX() == r & model.getTipperY() == c) {
                    b.setStyle("-fx-background-color: #FF0000"); //Red (Tipper)
                }
                if(model.getGoalX() == r & model.getGoalY() == c){
                    b.setStyle("-fx-background-color: #00FF00"); //Green (Goal)
                }
                Font font1 = new Font(40);
                b.setFont(font1);
                gp.add(b, c, r);
            }
        }
        //Creates the buttons that allows users to make moves
        GridPane gp1 = new GridPane();
        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 3; c++) {
                if(r == 0 & c == 1) {
                    Button b = new Button("^");
                    b.setMinWidth(25);
                    b.setMinHeight(25);
                    Font font = new Font(25);
                    b.setFont(font);
                    b.setOnAction((event -> {model.move("n");}));
                    gp1.add(b, c, r);
                }
                if(r == 1 & c == 0) {
                    Button b = new Button("<");
                    b.setMinWidth(25);
                    b.setMinHeight(25);
                    Font font = new Font(25);
                    b.setFont(font);
                    b.setOnAction((event -> {model.move("w");}));
                    gp1.add(b, c, r);
                }
                if(r == 1 & c == 2) {
                    Button b = new Button(">");
                    b.setMinWidth(25);
                    b.setMinHeight(25);
                    Font font = new Font(25);
                    b.setFont(font);
                    b.setOnAction((event -> {model.move("e");}));
                    gp1.add(b, c, r);
                }
                if(r == 2 & c == 1) {
                    Button b = new Button("v");
                    b.setMinWidth(25);
                    b.setMinHeight(25);
                    Font font = new Font(25);
                    b.setFont(font);
                    b.setOnAction((event -> {model.move("s");}));
                    gp1.add(b, c, r);
                }
            }
        }
        // Creates the load, reset, and hint buttons
        HBox hBox1 = new HBox();
        Button loadB = new Button("Load");
        loadB.setOnAction(event -> {chooseFile(stage);});

        Button reset = new Button("Reset");
        reset.setOnAction((event -> {model.loadBoardFromFile(filename, true);}));

        Button hint = new Button("Hint");
        hint.setOnAction(event -> {model.getHint();});

        hBox1.getChildren().addAll(loadB, reset, hint);
        borderPane.setTop(l);
        l.setAlignment(Pos.CENTER);
        borderPane.setCenter(gp);
        borderPane.setRight(gp1);
        borderPane.setBottom(hBox1);
        hBox1.setAlignment(Pos.CENTER);
        Scene scene = new Scene(borderPane);
        stage.setTitle("Tip Over Game");
        stage.setScene(scene);
        stage.setResizable(false);
        this.stage = stage;
        stage.show();
    }

    /**
     * Allows the user to choose a file from data/tipover when they click the load button to load a file
     * @param stage
     */
    public void chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a game board.");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tipover"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
            String file = "data/tipover/" + selectedFile.getName();
            filename = file;
            model.loadBoardFromFile(file, false);
        }
    }

    /**
     *
     * @param tipOverModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TipOverModel tipOverModel, String message) {
        if (message.startsWith(TipOverModel.LOADED)) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if(message.equals("Current board is already solved.")) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if(message.equals("I WON!")) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if(message.equals(TipOverModel.NOSOL)) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if (message.startsWith(TipOverModel.FAILED)){
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if (message.equals(TipOverModel.TIPPER)) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if(message.equals(TipOverModel.RESET)) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if(message.equals("Move updated")) {
            l.setText("");
            displayBoard();
            return;
        }
        else if(message.equals("No crate or tower there.")) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if(message.equals("Move goes off the board.")) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if(message.equals("A tower has been tipped over.")) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        else if(message.equals("Tower cannot be tipped over.")) {
            l.setText("Message: " + message);
            displayBoard();
            return;
        }
        displayBoard();
    }

    /**
     * Creates a new gridpane to update the board. Borderpane sets the gridpane at the center and stage gets updated
     * according to the board size.
     */
    public void displayBoard() {
    GridPane gp2 = new GridPane();
    for(int r = 0; r < model.getGridRow(); r++) {
        for(int c = 0; c < model.getGridCol(); c++) {
            Button b = new Button("" + model.getTipperValue(r, c));
            b.setMinWidth(75);
            b.setMinHeight(75);
            if(model.getTipperX() == r & model.getTipperY() == c) {
                b.setStyle("-fx-background-color: #FF0000"); //Red (Tipper)
            }
            if(model.getGoalX() == r & model.getGoalY() == c){
                b.setStyle("-fx-background-color: #00FF00"); //Green (Goal)
            }
            Font font = new Font(40);
            b.setFont(font);
            gp2.add(b, c, r);
        }
    }
    borderPane.setCenter(gp2);
    if(gp2.getChildren().size() == 1) {
        Font font2 = new Font(15);
        l.setFont(font2);
        stage.setWidth(375);
        stage.setHeight(250);
    }
    else {
        stage.setWidth((model.getGridCol() + 3) * 70);
        stage.setHeight((model.getGridRow() + 1) * 85);
    }
}

    /**
     * The main function launches the application (GUI) if there are arguments in the config.
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}
