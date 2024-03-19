// UNLISTED YOUTUBE VIDEO DEMO:
// https://www.youtube.com/watch?v=a4LR-8xgPCQ

package views;

import AdventureModel.AdventureGame;
import java.io.*;

import AdventureModel.*;
import AdventureModel.AdventureObject;
import AdventureModel.Passage;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.scene.AccessibleRole;

import javax.speech.Central;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Synthesizer;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 * recording. Place a link to your recording below.
 *
 * ZOOM LINK: <URL HERE>
 * PASSWORD: <PASSWORD HERE>
 */
public class AdventureGameView {

    AdventureGame model; //model of the game
    Stage stage; //stage on which all is rendered
    Button helpButton, menuButton, escapeButton, attackButton; //buttons
    Boolean helpToggle = false; //is help on display?

    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label roomDescLabel = new Label(); //to hold room description and/or instructions

    boolean firstTrollEncounter = false;

    Label battleLabel, trollHpLabel; // to hold health of troll and command outcome
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox keysBox = new VBox(); //to hold keys
    VBox weaponsBox = new VBox(); //to hold weapons
    VBox potionsBox = new VBox(); //to hold potions

    VBox trollVBox = new VBox();

    VBox rightColumn = new VBox(); //to contain/format all right column nodes
    VBox leftColumn = new VBox(); //to contain/format all left column nodes
    Button playerHealthInfo = new Button(); // to contain/format all player health info

    ImageView roomImageView; //to hold room image

    TextField inputTextField; //for user input

    private final Battle battle = new Battle(); // Holds reference to battle class (invoker)
    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing
    private boolean txtToSpeech; //to know if text to speech is enabled.
    public double textSize;
    private VBox textEntry = new VBox(); // store refrence to change color theme
    private VBox roomPane = new VBox();
    private ScrollPane scIII = new ScrollPane();
    private ScrollPane scII = new ScrollPane();
    private ScrollPane scI = new ScrollPane();
    private ScrollPane scO = new ScrollPane();
    private Label commandLabel = new Label();
    String currentColorTheme;
    Synthesizer synthesizer; // for text to speech
    private CareTaker careTaker; // stores and loads saves

    Label objLabel =  new Label("Objects in Room");
    Label invLabel =  new Label("Objects in Inventory");
    Label weaponsLabel = new Label("Weapons");
    Label potionsLabel = new Label("Potions");
    Label keysLabel = new Label("Keys");
    Label healingLabel = new Label();

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        this.txtToSpeech = false;
        this.textSize = 14;
        try
        {
            intiUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Initialize the UI
     */
    public void intiUI()  throws IOException, ClassNotFoundException {

        //initialize caretaker
        careTaker = new CareTaker(this);

        // see if saved caretaker exists
        File test = new File("Games/Caretaker.ser");

        // load saves if exists
        if (test.exists()){
            FileInputStream file = null;
            ObjectInputStream in = null;
            try {
                file = new FileInputStream("Games/Caretaker.ser");
                in = new ObjectInputStream(file);
                ArrayList<Memento> saves = (ArrayList<Memento>) in.readObject();
                careTaker.setSaves(saves);
            } finally {

                if (in != null) {
                    in.close();
                    file.close();
                }
            }
        }


        //initialize tts
        try
        {
            //setting properties as Kevin Dictionary
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");
            //registering speech engine
            Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");
            //create a Synthesizer that generates voice
            synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synthesizer.allocate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //set default color theme
        currentColorTheme = "Dark Theme";

        // setting up the stage
        this.stage.setTitle("group 28's Adventure Game"); //Replace <YOUR UTORID> with your UtorID

        //Inventory + Room items
        keysBox.setSpacing(10);
        keysBox.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);
        weaponsBox.setSpacing(10);
        weaponsBox.setAlignment(Pos.TOP_CENTER);
        potionsBox.setSpacing(10);
        potionsBox.setAlignment(Pos.TOP_CENTER);


        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints( );
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );



        helpButton = new Button("Help");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 100, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        menuButton = new Button("Menu");
        menuButton.setId("Menu");
        customizeButton(menuButton, 100, 50);
        makeButtonAccessible(menuButton, "Menu Button", "This button opens the accessibility menu.", "This button allows you to toggle various accessibility features");
        addMenuEvent();


        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(helpButton, menuButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.TOP_RIGHT);
        topButtons.setPadding(new Insets(0, 10, 0, 0));




        //ESCAPE BUTTON
        escapeButton = new Button("Escape");
        escapeButton.setId("escape");
        escapeButton.setPrefSize(100, 50);
        escapeButton.setFont(new Font("Palatino", 16));
        escapeButton.setStyle("-fx-background-color: #4f807c; -fx-text-fill: white;");
        makeButtonAccessible(escapeButton, "Escape Button", "This button allows the user to escape.", "This button allows the player to escape the troll by reverting them to the last room they were in, if it was not forced.");
        addEscapeEvent();

        //ATTACK BUTTON
        attackButton = new Button("Attack");
        attackButton.setId("attack");
        attackButton.setPrefSize(100, 50);
        attackButton.setFont(new Font("Palatino", 16));
        attackButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        makeButtonAccessible(attackButton, "Attack Button", "This button allows the user to attack the troll.", "This button allows the player to attack the troll by dealing damage to them.");
        addAttackEvent();



        HBox battleButton = new HBox();
        battleButton.getChildren().addAll(attackButton, escapeButton);
        battleButton.setSpacing(10);
        battleButton.setAlignment(Pos.TOP_LEFT);
        battleButton.setPadding(new Insets(0, 10, 0, 0));
        battleButton.setMaxWidth(300);
        battleButton.setPrefWidth(400);

        trollHpLabel = new Label("");
        battleLabel = new Label("");


        inputTextField = new TextField();
        inputTextField.setFont(new Font("Palatino", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        objLabel =  new Label("  Objects in Room");
        objLabel.setWrapText(true);
        objLabel.setTextAlignment(TextAlignment.CENTER);
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Palatino", 16));

        invLabel =  new Label("     Your Inventory");
        invLabel.setWrapText(true);
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setTextAlignment(TextAlignment.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Palatino", 16));

        weaponsLabel.setAlignment(Pos.CENTER);
        weaponsLabel.setStyle("-fx-text-fill: white;");
        weaponsLabel.setFont(new Font("Palatino", 16));

        potionsLabel.setAlignment(Pos.CENTER);
        potionsLabel.setStyle("-fx-text-fill: white;");
        potionsLabel.setFont(new Font("Palatino", 16));

        keysLabel.setAlignment(Pos.CENTER);
        keysLabel.setStyle("-fx-text-fill: white;");
        keysLabel.setFont(new Font("Palatino", 16));

        rightColumn.setAlignment(Pos.CENTER);
        leftColumn.setAlignment(Pos.CENTER);

        /*
        rightColumn.getChildren().add(invLabel);
        rightColumn.getChildren().add(objectsInInventory);
        rightColumn.getChildren().add(weaponsLabel);
        rightColumn.getChildren().add(weaponsBox);
        */

        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( battleButton, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label
        gridPane.add( rightColumn, 2, 1, 1, 1 );  // Add full column
        gridPane.add( leftColumn, 0, 1, 1, 1); // Add full column
        gridPane.add( playerHealthInfo, 0, 2, 2, 2); // Add health info
        addTTSEvent();




        commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Palatino", 16));

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms

        // adding the text area and submit button to a VBox
        textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 1, 2, 2, 1 );

        // HANDLE OFF-THE-BAT FORCED AS NEEDED
        if (Player.getInstance().getCurrentRoom().getMotionTable().optionExists("FORCED")) {
            handleForced();
        }

        setTextSize(textSize);
        setColorTheme(currentColorTheme);

        // Render everything
        StackPane grid = new StackPane(gridPane);
        var scene = new Scene( grid ,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

    }


    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Palatino", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the inputTextField attribute
     *
     * Your event handler should respond when users
     * hits the ENTER or TAB KEY. If the user hits
     * the ENTER Key, strip white space from the
     * input to inputTextField and pass the stripped
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus
     * of the scene onto any other node in the scene
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        // TextEventHandler textHandler = new TextEventHandler(this);
        // this.inputTextField.addEventHandler(KeyEvent.KEY_PRESSED, textHandler);
        String inputString = this.inputTextField.getText();
        AdventureGameView view = this;

        // BLUEPRINT COURTESY OF
        // Geesh_SO, et al. “How Do I Pick up the Enter Key Being Pressed in Javafx2?” Stack Overflow.
        // stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2.
        // 14 Oct. 2023.
        this.inputTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    submitEvent(view.inputTextField.getText().strip());
                    inputTextField.clear();
                } else if (keyEvent.getCode().equals(KeyCode.TAB)) {
                    // IS THIS OK?
                    gridPane.requestFocus();
                }
            }
        });
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop
        String[] splited = text.split(" ");

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = Player.getInstance().getCurrentRoom().getRoomDescription();
            String objectString = Player.getInstance().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        }else if (text.equalsIgnoreCase("SAVE")) {
            String gameName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".ser";
            SaveView.saveGameTextCmd(gameName, this);
            roomDescLabel.setText("SAVED GAME AS " + gameName);
            return;
        }else if (splited[0].equalsIgnoreCase("SAVE")) {
            if (splited.length == 2) {
                try {
                    SaveView.saveGameTextCmd(splited[1], this);
                    roomDescLabel.setText("SAVED GAME AS " + splited[1]);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                return;
            }
        }else if(splited[0].equalsIgnoreCase("LOAD")) {
            if (splited.length == 2) {
                try {
                    if (this.careTaker.load(splited[1])) {
                        roomDescLabel.setText("LOADED GAME FILE " + splited[1]);
                    } else {
                        roomDescLabel.setText("GAME FILE NOT FOUND");
                    }
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                return;
            }
        }


        //try to move!
        String output = this.model.interpretAction(text); //process the command!

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            updateScene(output);
            updateItems();
            setColorTheme(currentColorTheme);
        } else if (output.equals("GAME OVER")) {
            updateScene("");
            updateItems();
            setColorTheme(currentColorTheme);
            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            //Your code will need to display the image in the
            //current room and pause, then transition to
            //the forced room.

            // UPDATE SCREEN FOR CURRENT ROOM WITH FORCED PASSAGES
            updateScene("");
            updateItems();
            setColorTheme(currentColorTheme);

            // EXECUTE THE FORCED
            handleForced();

        }
    }

    /**
     * handleForced
     * __________________________
     *
     * Handle all forced possibilities upon being in a room with a FORCED passage.
     *
     * This includes consecutive forced rooms, the restriction of user input upon forced movement
     * (5 seconds per forced room), and handling articulation (start and stop) when entering and exiting a forced room.
     */
    private void handleForced() {
        // DISABLE INPUTS TO ALL OTHER NODES
        // DEFAULT BUTTONS
        helpButton.setDisable(true);
        menuButton.setDisable(true);


        // OBJECT BUTTONS (IN INVENTORY)
        rightColumn.setDisable(true);
        leftColumn.setDisable(true);

        // OBJECT BUTTONS (IN ROOM)
        objectsInRoom.setDisable(true);

        // TEXT INPUT
        inputTextField.setDisable(true);

        // DELAY 5 SECONDS FIRST
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        // WHEN THE TIME IS UP, MOVE THE PLAYER TO THE APPROPRIATE NEXT FORCED ROOM
        pause.setOnFinished(event -> {
            boolean currentForcedDone = false;
            ArrayList<Passage> blockedPassages = new ArrayList<>();
            ArrayList<Passage> unblockedPassages = new ArrayList<>();

            // SPLIT (WHILE MAINTAINING ORDER) INTO BLOCKED AND UNBLOCKED PASSAGES
            for (Passage passage : Player.getInstance().getCurrentRoom().getMotionTable().getDirection()) {
                if (passage.getIsBlocked()) {
                    blockedPassages.add(passage);
                } else {
                    unblockedPassages.add(passage);

                }
            }

                // IF WE HAVE BLOCKED PASSAGES, CHECK IF WE CAN ENTER THOSE FIRST
                if (!blockedPassages.isEmpty()) {
                    for (Passage passage : blockedPassages) {
                        if (Player.getInstance().getInventory().contains(passage.getKeyName())) {
                            // MOVE TO THIS ROOM, SET currentForcedDone TO TRUE, AND REFLECT THE CHANGES IN THE VIEW
                            Player.getInstance().setCurrentRoom(this.model.getRooms().get(passage.getDestinationRoom()));
                            currentForcedDone = true;
                            stopArticulation();
                            updateScene("");
                            updateItems();
                            setColorTheme(currentColorTheme);
                        }
                    }
                }

                // AT LEAST ONE PASSAGE MUST BE UNBLOCKED ACCORDING TO THE RULES OF FORCED
                if (!currentForcedDone) {
                    for (Passage passage : unblockedPassages) {
                        // ENTER THE FIRST ONE AND REFLECT THE CHANGES IN THE VIEW
                        Player.getInstance().setCurrentRoom(this.model.getRooms().get(passage.getDestinationRoom()));
                        stopArticulation();
                        updateScene("");
                        updateItems();
                        setColorTheme(currentColorTheme);
                        break;
                    }
                }

            // RE-ENABLE INPUTS TO ALL OTHER NODES
            // DEFAULT BUTTONS
            helpButton.setDisable(false);
            menuButton.setDisable(false);

            // OBJECT BUTTONS (IN INVENTORY)
            rightColumn.setDisable(false);
            leftColumn.setDisable(false);


            // OBJECT BUTTONS (IN ROOM)
            objectsInRoom.setDisable(false);


            // TEXT INPUT
            inputTextField.setDisable(false);


                // IDEA INSPIRED BY
                // "How can I make JavaFX program stop after each pause transition and only start once the pause transition is done?"
                // ChatGPT, 14 Feb. version, OpenAI, 14 Oct. 2023, chat.openai.com/chat.
                if (Player.getInstance().getCurrentRoom().getMotionTable().optionExists("FORCED") &&
                        Player.getInstance().getCurrentRoom().getMotionTable().getDirection().get(0).getDestinationRoom() != 0) {
                    handleForced();
                }
            });
            setColorTheme(currentColorTheme);
            pause.play();
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the
     * current room.
     */
    private void showCommands() {
        this.roomDescLabel.setText("These are all the possible moves:\n\n" + Player.getInstance().getCurrentRoom().getCommands());
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     *
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {


        Player player = Player.getInstance();
        if(player.getCurrentRoom().checkTroll()){
            healingLabel.setText("");
            encounterTroll();
        }else {

            ObservableList<Node> children = gridPane.getChildren();
            for (Node node : children) {
                int nodeRow = GridPane.getRowIndex(node);
                int nodeColumn = GridPane.getColumnIndex(node);
                if (nodeRow == 1 && nodeColumn == 1) {
                    gridPane.getChildren().remove(node);
                    break;
                }
            }

            getRoomImage(); //get the image of the current room
            formatText(textToDisplay); //format the text to display
            roomDescLabel.setPrefWidth(500);
            roomDescLabel.setPrefHeight(500);
            roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
            roomDescLabel.setWrapText(true);
            roomPane = new VBox(roomImageView,roomDescLabel);
            roomPane.setPadding(new Insets(10));
            roomPane.setAlignment(Pos.TOP_CENTER);
            roomPane.setStyle("-fx-background-color: #000000;");
            addTTSEvent();

            gridPane.add(roomPane, 1, 1);
            stage.sizeToScene();


            // HELP BUTTONS ENABLED
            helpButton.setDisable(false);


            // OBJECT BUTTONS (IN INVENTORY)
            keysBox.setDisable(false);

            // OBJECT BUTTONS (IN ROOM)
            objectsInRoom.setDisable(false);

            // TEXT INPUT
            inputTextField.setDisable(false);

            // ESCAPE AND ATTACK BUTTON DISABLED
            escapeButton.setDisable(true);
            attackButton.setDisable(true);
            potionsBox.setDisable(true);
            weaponsBox.setDisable(false);
            keysBox.setDisable(false);
            addTTSEvent();




            //finally, articulate the description
            if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();
        }
    }



    /**
     * encounterTroll
     * __________________________
     *
     * Manages the visual encounter with a troll in the game. It checks for any
     * existing entity at a specific grid location (row 1, column 1) and
     * removes it. Then, it retrieves the troll associated with the player's
     * current room, loads an image representing this troll, and displays
     * the image at the specified location on the grid.
     */
    public void encounterTroll() {


        stopArticulation();


        Player player = Player.getInstance();
        Room room = player.getCurrentRoom();

        Troll troll = room.roomTroll;

        if(troll instanceof FinalBoss){
            if(!firstTrollEncounter) {
                firstTrollEncounter = true;

                final int repeatCount = 3;
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.seconds(2),
                        e -> {
                            shakeStage(stage);
                            String audioFile = "./combat-audio/stomp1.mp3";

                            Media sound = new Media(new File(audioFile).toURI().toString());

                            mediaPlayer = new MediaPlayer(sound);
                            mediaPlayer.play();
                            mediaPlaying = true;
                        }
                ));

                timeline.setCycleCount(repeatCount);


                timeline.setOnFinished(event -> {

                    Scene scene = stage.getScene();
                    Pane root = (Pane) scene.getRoot();


                    PauseTransition pauseBeforeFade = new PauseTransition(Duration.seconds(2));
                    pauseBeforeFade.setOnFinished(event1 -> {
                        String audioFile = "./combat-audio/laugh.mp3";

                        Media sound = new Media(new File(audioFile).toURI().toString());

                        mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();
                        mediaPlaying = true;

                        Rectangle blackOverlayRec = new Rectangle(100000, 1000000);
                        StackPane blackOverlay = new StackPane(blackOverlayRec);

                        blackOverlayRec.setFill(Color.BLACK);

                        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), blackOverlay);
                        fadeIn.setFromValue(0);
                        fadeIn.setToValue(1);

                        PauseTransition pause = new PauseTransition(Duration.seconds(3));

                        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.0001), blackOverlay);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);
                        fadeOut.setOnFinished(e -> {
                            root.getChildren().remove(blackOverlay);
                            ObservableList<Node> children = gridPane.getChildren();
                            for (Node node : children) {
                                int nodeRow = GridPane.getRowIndex(node);
                                int nodeColumn = GridPane.getColumnIndex(node);
                                if (nodeRow == 1 && nodeColumn == 1) {
                                    gridPane.getChildren().remove(node);
                                    break;
                                }

                            }

                            String trollName = troll.getName();
                            Image trollImage = new Image("/game-images/" + trollName + ".png");
                            ImageView trollImageView = new ImageView(trollImage);


                            trollImageView.setPreserveRatio(true);
                            trollImageView.setFitWidth(400);
                            trollImageView.setFitHeight(400);

//                        battleLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");



                            //set accessible text
                            trollImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
                            trollImageView.setAccessibleText(trollName);
                            trollImageView.setFocusTraversable(true);


                            String trollHp = String.valueOf(troll.getHp());
                            trollHpLabel = new Label("Troll HP: " + trollHp);
                            trollHpLabel.setFont(new Font("Palatino", textSize));
                            setTextSize(textSize);


                            battleLabel = new Label("");
                            battleLabel.setFont(new Font("Palatino", textSize));
                            setColorTheme(currentColorTheme);


                            VBox trollVBox = new VBox(5);
                            trollVBox.getChildren().addAll(trollImageView, healingLabel, trollHpLabel, battleLabel);
                            trollVBox.setAlignment(Pos.CENTER);
                            trollHpLabel.setPadding(new Insets(0, 0, 30, 0));


                            gridPane.add(trollVBox, 1, 1);
                            GridPane.setHalignment(trollVBox, HPos.CENTER);
                            GridPane.setValignment(trollVBox, VPos.CENTER); // Center VBox Vertically


                            // HELP BUTTONS DISABLED
                            helpButton.setDisable(true);
                            menuButton.setDisable(false);


                            // OBJECT BUTTONS (IN INVENTORY)
                            keysBox.setDisable(true);

                            // OBJECT BUTTONS (IN ROOM)
                            objectsInRoom.setDisable(true);

                            // TEXT INPUT
                            inputTextField.setDisable(true);

                            // ESCAPE AND ATTACK BUTTON ENABLED
                            escapeButton.setDisable(false);
                            attackButton.setDisable(false);
                            potionsBox.setDisable(false);
                            weaponsBox.setDisable(true);
                            keysBox.setDisable(true);

                            String audioFile2 = "./combat-audio/barney.mp3";

                            Media sound1 = new Media(new File(audioFile2).toURI().toString());

                            mediaPlayer = new MediaPlayer(sound1);
                            mediaPlayer.play();
                            mediaPlaying = true;


                        });

                        pause.setOnFinished(ev -> {

                            fadeOut.play();
                        });
                        fadeIn.setOnFinished(e -> {

                            pause.play();

                        });

                        fadeIn.play();
                        root.getChildren().add(blackOverlay);

                    });
                    pauseBeforeFade.play();
                });

                timeline.play();


            }

        }else {
            ObservableList<Node> children = gridPane.getChildren();
            for (Node node : children) {
                int nodeRow = GridPane.getRowIndex(node);
                int nodeColumn = GridPane.getColumnIndex(node);
                if (nodeRow == 1 && nodeColumn == 1) {
                    gridPane.getChildren().remove(node);
                    break;
                }
            }


            String trollName = troll.getName();
            Image trollImage = new Image("/game-images/" + trollName + ".png");
            ImageView trollImageView = new ImageView(trollImage);

        String audioFile;
        String combatAudioType = "";

        if (trollName.equals("Creeper")) {
            combatAudioType = "creeper";
        } else if (trollName.equals("Ender Dragon")) {
            combatAudioType = "enderdragon";
        } else if (trollName.equals("Herobrine")) {
            combatAudioType = "herobrine";
        } else if (trollName.equals("Zombie")) {
            combatAudioType = "zombie";
        }

            trollImageView.setPreserveRatio(true);
            trollImageView.setFitWidth(400);
            trollImageView.setFitHeight(400);
        audioFile = "./combat-audio/" + combatAudioType + ".mp3";

        Media sound = new Media(new File(audioFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;

        trollImageView.setPreserveRatio(true);
        trollImageView.setFitWidth(400);
        trollImageView.setFitHeight(400);


            //set accessible text
            trollImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
            trollImageView.setAccessibleText(trollName);
            trollImageView.setFocusTraversable(true);


            String trollHp = String.valueOf(troll.getHp());
            trollHpLabel = new Label("Troll HP: " + trollHp);
            trollHpLabel.setFont(new Font("Palatino", 16));


            battleLabel = new Label("");
//            battleLabel.setFont(new Font("Palatino", 16));


            VBox trollVBox = new VBox(5);
            trollVBox.getChildren().addAll(trollImageView, healingLabel, trollHpLabel, battleLabel);
            trollVBox.setAlignment(Pos.CENTER);
            trollHpLabel.setPadding(new Insets(0, 0, 30, 0));


            gridPane.add(trollVBox, 1, 1);
            GridPane.setHalignment(trollVBox, HPos.CENTER);
            GridPane.setValignment(trollVBox, VPos.CENTER); // Center VBox Vertically


            // HELP BUTTONS DISABLED
            helpButton.setDisable(true);
            menuButton.setDisable(false);


            // OBJECT BUTTONS (IN INVENTORY)
            keysBox.setDisable(true);

            // OBJECT BUTTONS (IN ROOM)
            objectsInRoom.setDisable(true);

            // TEXT INPUT
            inputTextField.setDisable(true);

            // ESCAPE AND ATTACK BUTTON ENABLED
            escapeButton.setDisable(false);
            attackButton.setDisable(false);
            potionsBox.setDisable(false);
            weaponsBox.setDisable(true);
            keysBox.setDisable(true);
            addTTSEvent();

        }




    }


    /**
     * Shakes a stage by applying a random shakey effect to its position.
     *
     * @param stage The JavaFX stage to be shaken.
     */
    public void shakeStage(Stage stage) {
        final double originalX = stage.getX();
        final double originalY = stage.getY();
        final int shakeDistance = 100;
        final int shakeCount = 15;

        Timeline shakeTimeline = new Timeline();

        for (int i = 0; i < shakeCount; i++) {
            shakeTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 50), e -> {
                stage.setX(originalX + Math.random() * shakeDistance - shakeDistance / 2);
                stage.setY(originalY + Math.random() * shakeDistance - shakeDistance / 2);
            }));
        }
        shakeTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(shakeCount * 50), e -> {
            stage.setX(originalX);
            stage.setY(originalY);
        }));
        shakeTimeline.playFromStart();
    }



    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     *
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = Player.getInstance().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = Player.getInstance().getCurrentRoom().getObjectString();
            if (objectString != null && !objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            else roomDescLabel.setText(roomDesc);
        } else roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Palatino", 16));
        roomDescLabel.setAlignment(Pos.CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place
     * it in the roomImageView
     */
    private void getRoomImage() {

        int roomNumber = Player.getInstance().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";

        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(Player.getInstance().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the leftColumn and rightColumn Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {

        //write some code here to add images of objects in a given room to the objectsInRoom Vbox
        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox
        //please use setAccessibleText to add "alt" descriptions to your images!
        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";


        if(Player.getInstance().getCurrentRoom().roomTroll instanceof FinalBoss){
            PauseTransition p1 = new PauseTransition(Duration.seconds(12));
            p1.setOnFinished(event1 -> {
                // CLEAR ANY EXISTING BUTTONS IN ORDER TO REFRESH IN CASE OF USER INPUT
                keysBox.getChildren().clear();
                weaponsBox.getChildren().clear();
                potionsBox.getChildren().clear();
                rightColumn.getChildren().clear();


                leftColumn.getChildren().clear();
                objectsInRoom.getChildren().clear();

                // CREATE AND ADD ALL INVENTORY ITEMS TO THE objectsInInventory VBOX
                for (String inventoryItem : Player.getInstance().getInventory()) {
                    // CONSTRUCT AN ImageView NODE TO HOLD THE OBJECT IMAGE
                    boolean objectIsPotionORIG = false;
                    Potion potionObject = null;

                    for (AdventureObject object : Player.getInstance().getPotions()) {
                        if (inventoryItem.equals(object.getName())) {
                            objectIsPotionORIG = true;
                            potionObject = (Potion) object;
                            break;
                        }
                    }

                    final Potion potionObjectFinal = potionObject;

                    Image roomImageFile;
                    if (objectIsPotionORIG) {
                        roomImageFile = new Image("/game-images/" + inventoryItem + ".png");
                    } else {
                        roomImageFile = new Image(this.model.getDirectoryName() + "/objectImages/" + inventoryItem + ".jpg");
                    }
                    ImageView objectImageView = new ImageView(roomImageFile);
                    objectImageView.setPreserveRatio(true);
                    objectImageView.setFitWidth(100);
                    objectImageView.setFitHeight(100);

                    // CONSTRUCT A Button NODE
                    Button itemButton = new Button(inventoryItem, objectImageView);
                    itemButton.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
                    // COURTESY OF
                    // “JavaFX Button Position the Text under the Image.” JavaFX Button Position the Text under the Image,
                    // www.java2s.com/ref/java/javafx-button-position-the-text-under-the-image.html. 14 Oct. 2023.
                    itemButton.setContentDisplay(ContentDisplay.TOP);
                    // itemButton.setGraphic(objectImageView);
                    itemButton.setId(inventoryItem);
                    itemButton.setTextFill(Color.BLACK);
                    itemButton.setWrapText(true);
                    itemButton.setTextAlignment(TextAlignment.CENTER);
                    // customizeButton(itemButton, 100, 120);
                    makeButtonAccessible(itemButton, inventoryItem + " Button", "This button acts as a TAKE or DROP of this item: " + inventoryItem, "This button acts as a TAKE or DROP of this item: " + inventoryItem + ". Click it to TAKE (if it is in the room) or DROP (if it is in the inventory) the item.");
                    // UPDATE LOCATION BASED ON ID
                    // COURTESTY OF
                    // Stanly Medjo, and Matt. “Getting the ID of a Button in Javafx.” Stack Overflow,
                    // stackoverflow.com/questions/47700220/getting-the-id-of-a-button-in-javafx. 14 Oct. 2023.

                    // IF IT'S A POTION IN THE INVENTORY, THEN THE BUTTON NEEDS TO BE MADE SO THAT THE POTION CAN BE USED
                    if (objectIsPotionORIG) {
                        itemButton.setOnAction(e -> {
                            Button button = (Button) e.getSource();
                            Command healCommand = new PlayerHealCommand(potionObjectFinal);

                            battle.setPlayerCommand(healCommand);

                            if (Player.getInstance().getHp() != 100) {
                                String outcome = battle.executeHeal();



                                itemButton.setDisable(true);

                                String audioFile = "./combat-audio/healaudio.mp3";

                                Media sound = new Media(new File(audioFile).toURI().toString());

                                mediaPlayer = new MediaPlayer(sound);
                                mediaPlayer.play();
                                mediaPlaying = true;

                                ObservableList<Node> children = gridPane.getChildren();
                                for (Node node : children) {
                                    int nodeRow = GridPane.getRowIndex(node);
                                    int nodeColumn = GridPane.getColumnIndex(node);
                                    if (nodeRow == 1 && nodeColumn == 1) {
                                        gridPane.getChildren().remove(node);
                                        break;
                                    }
                                }

                                Player player = Player.getInstance();
                                Room room = player.getCurrentRoom();

                                Troll troll = room.roomTroll;
                                String trollName = troll.getName();

                                Image trollImage = new Image("/game-images/" + trollName + ".png");
                                ImageView trollImageView = new ImageView(trollImage);



                                trollImageView.setPreserveRatio(true);
                                trollImageView.setFitWidth(400);
                                trollImageView.setFitHeight(400);



                                //set accessible text
                                trollImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
                                trollImageView.setAccessibleText(trollName);
                                trollImageView.setFocusTraversable(true);


                                healingLabel = new Label("YOU ARE HEALING USING: " + button.getId() + "!");
                                healingLabel.setFont(new Font("Palatino", 16));
                                healingLabel.setDisable(false);

                                setColorTheme(currentColorTheme);

                                battleLabel = new Label("");
                                healingLabel.setFont(new Font("Palatino", 16));
                                healingLabel.setDisable(false);



                                VBox trollVBox = new VBox(5);
                                trollVBox.getChildren().addAll(trollImageView,healingLabel, trollHpLabel, battleLabel);
                                trollVBox.setAlignment(Pos.CENTER);
                                healingLabel.setPadding(new Insets(0, 0, 30, 0));
                                trollVBox.setDisable(false);
                                addTTSEvent();

                                gridPane.add(trollVBox, 1, 1);
                                GridPane.setHalignment(trollVBox, HPos.CENTER);
                                GridPane.setValignment(trollVBox, VPos.CENTER); // Center VBox Vertically


                                helpButton.setDisable(true);
                                menuButton.setDisable(true);


                                // OBJECT BUTTONS (IN INVENTORY)
                                keysBox.setDisable(true);

                                // OBJECT BUTTONS (IN ROOM)
                                objectsInRoom.setDisable(true);

                                // TEXT INPUT
                                inputTextField.setDisable(true);

                                // ESCAPE AND ATTACK BUTTON DISABLED
                                helpButton.setDisable(true);
                                menuButton.setDisable(true);

                                // ESCAPE AND ATTACK BUTTON DISABLED
                                escapeButton.setDisable(true);
                                attackButton.setDisable(true);
                                keysBox.setDisable(true);
                                potionsBox.setDisable(true);

                                PauseTransition pause = new PauseTransition(Duration.seconds(8));
                                pause.setOnFinished(event -> {
                                    if (outcome.equals("Healed")) {
                                        updateScene("YOU HAVE HEALED USING: " + potionObjectFinal.getName());
                                        healingLabel.setText("YOU HAVE HEALED USING: " + potionObjectFinal.getName());
                                        updateItems();
                                        updateAttackHP();
                                    } else {
                                        updateScene("UNABLE TO HEAL");
                                    }
                                    stopArticulation();

                                    potionsBox.getChildren().remove(itemButton);
                                    setColorTheme(currentColorTheme);

                                    helpButton.setDisable(false);
                                    menuButton.setDisable(false);

                                    // ESCAPE AND ATTACK BUTTON DISABLED
                                    escapeButton.setDisable(false);
                                    attackButton.setDisable(false);
                                    potionsBox.setDisable(false);

                                });

                                pause.play();
                            }
                        });
                    } else {
                        itemButton.setOnAction(e -> {
                            Button button = (Button) e.getSource();
                            updateLocation(button);
                            setColorTheme(currentColorTheme);
                        });
                    }
                    addTTSEvent();



                    // set button font size
                    itemButton.setFont(new Font("Palatino", textSize));

                    boolean objectIsWeapon = false;
                    Weapon weaponObject = null;

                    boolean objectIsPotion2 = false;
                    Potion potionObject2 = null;

                    if (!(Player.getInstance().getWeapons() == null)) {
                        for (AdventureObject object : Player.getInstance().getWeapons()) {
                            if (itemButton.getId().equals(object.getName())) {
                                objectIsWeapon = true;
                                weaponObject = (Weapon) object;
                                break;
                            }
                        }
                    }

                    for (AdventureObject object : Player.getInstance().getPotions()) {
                        if (itemButton.getId().equals(object.getName())) {
                            objectIsPotion2 = true;
                            potionObject2 = (Potion) object;
                            break;
                        }
                    }

                    if (objectIsWeapon) {
                        if (15 >= ((Weapon) weaponObject).getDamageRange()[1]) {
                            itemButton.setStyle("-fx-background-color: #D3D3D3;");
                        } else if (25 >= ((Weapon) weaponObject).getDamageRange()[1]) {
                            itemButton.setStyle("-fx-background-color: #319236;");
                        } else if (35 >= ((Weapon) weaponObject).getDamageRange()[1]) {
                            itemButton.setStyle("-fx-background-color: #4c51f7;");
                        } else if (45 >= ((Weapon) weaponObject).getDamageRange()[1]) {
                            itemButton.setStyle("-fx-background-color: #9d4dbb;");
                        } else {
                            itemButton.setStyle("-fx-background-color: #f3af19;");
                        }
                        weaponsBox.getChildren().add(itemButton);
                    } else if (objectIsPotion2) {
                        if (potionObject2.getClass() == Bandages.class) {
                            itemButton.setStyle("-fx-background-color: #D3D3D3;");
                        } else if (potionObject2.getClass() == MedKit.class) {
                            itemButton.setStyle("-fx-background-color: #319236;");
                        } else if (potionObject2.getClass() == ChugJug.class) {
                            itemButton.setStyle("-fx-background-color: #f3af19;");
                        }

                        potionsBox.getChildren().add(itemButton);
                    } else {
                        keysBox.getChildren().add(itemButton);
                    }


                }

                // CREATE AND ADD ALL OBJECTS IN ROOM TO THE objectsInRoom VBOX
                for (AdventureObject roomItemObject : Player.getInstance().getCurrentRoom().objectsInRoom) {
                    // FOR objectsInRoom WE HAVE NO WAY OF GETTING A LIST OF STRINGS WITHOUT MAKING EXTRA METHODS IN MODEL,
                    // SO WE MUST JUST GET THE NAME MANUALLY
                    String roomItem = roomItemObject.getName();

                    // CONSTRUCT AN ImageView NODE TO HOLD THE OBJECT IMAGE
                    Image roomImageFile;
                    if (roomItemObject instanceof Potion) {
                        roomImageFile = new Image("/game-images/" + roomItem + ".png");
                    } else {
                        roomImageFile = new Image(this.model.getDirectoryName() + "/objectImages/" + roomItem + ".jpg");
                    }
                    ImageView objectImageView = new ImageView(roomImageFile);
                    objectImageView.setPreserveRatio(true);
                    objectImageView.setFitWidth(100);
                    objectImageView.setFitHeight(100);

                    // CONSTRUCT A Button NODE
                    Button itemButton = new Button(roomItem, objectImageView);
                    // COURTESY OF
                    // “JavaFX Button Position the Text under the Image.” JavaFX Button Position the Text under the Image,
                    // www.java2s.com/ref/java/javafx-button-position-the-text-under-the-image.html. 14 Oct. 2023.
                    itemButton.setContentDisplay(ContentDisplay.TOP);
                    // itemButton.setGraphic(objectImageView);
                    itemButton.setId(roomItem);
                    itemButton.setTextFill(Color.BLACK);
                    itemButton.setWrapText(true);
                    itemButton.setTextAlignment(TextAlignment.CENTER);
                    // customizeButton(itemButton, 100, 100);
                    makeButtonAccessible(itemButton, roomItem + " Button", "This button acts as a TAKE or DROP of this item: " + roomItem, "This button acts as a TAKE or DROP of this item: " + roomItem + ". Click it to TAKE (if it is in the room) or DROP (if it is in the inventory) the item.");
                    // UPDATE LOCATION BASED ON ID
                    // COURTESY OF
                    // Stanly Medjo, and Matt. “Getting the ID of a Button in Javafx.” Stack Overflow,
                    // stackoverflow.com/questions/47700220/getting-the-id-of-a-button-in-javafx. 14 Oct. 2023.
                    itemButton.setOnAction(e -> {
                        Button button = (Button) e.getSource();
                        updateLocation(button);
                        setColorTheme(currentColorTheme);
                    });
                    //itemButton.setOnAction(event -> updateLocation((Button) event.getSource()));

                    // set button font size
                    itemButton.setFont(new Font("Palatino", textSize));

                    // Special styles (for weapons)
                    if (roomItemObject.getClass() == Weapon.class) {
                        if (15 >= ((Weapon) roomItemObject).getDamageRange()[1]) {
                            itemButton.setStyle("-fx-background-color: #D3D3D3;");
                        } else if (25 >= ((Weapon) roomItemObject).getDamageRange()[1]) {
                            itemButton.setStyle("-fx-background-color: #319236;");
                        } else if (35 >= ((Weapon) roomItemObject).getDamageRange()[1]) {
                            itemButton.setStyle("-fx-background-color: #4c51f7;");
                        } else if (45 >= ((Weapon) roomItemObject).getDamageRange()[1]) {
                            itemButton.setStyle("-fx-background-color: #9d4dbb;");
                        } else {
                            itemButton.setStyle("-fx-background-color: #f3af19;");
                        }
                        objectsInRoom.getChildren().add(itemButton);
                    } else if (roomItemObject instanceof Potion) {
                        if (roomItemObject.getClass() == Bandages.class) {
                            itemButton.setStyle("-fx-background-color: #D3D3D3;");
                        } else if (roomItemObject.getClass() == MedKit.class) {
                            itemButton.setStyle("-fx-background-color: #319236;");
                        } else if (roomItemObject.getClass() == ChugJug.class) {
                            itemButton.setStyle("-fx-background-color: #f3af19;");
                        }
                        objectsInRoom.getChildren().add(itemButton);
                    } else {
                        objectsInRoom.getChildren().add(itemButton);
                    }

                }
                scO = new ScrollPane(objectsInRoom);
                scO.setPadding(new Insets(10));
                scO.setFitToWidth(true);
                gridPane.add(scO, 0, 1);

                scI = new ScrollPane(keysBox);
                scI.setFitToWidth(true);
                scI.setMaxHeight(140);

                scII = new ScrollPane(weaponsBox);
                scII.setFitToWidth(true);
                scII.setMaxHeight(140);

                scIII = new ScrollPane(potionsBox);
                scIII.setFitToWidth(true);
                scIII.setMaxHeight(140);

                leftColumn.getChildren().add(scO);
                updateHeart();
                leftColumn.setVgrow(scO, Priority.ALWAYS);


                rightColumn.getChildren().add(keysLabel);
                rightColumn.getChildren().add(scI);
                rightColumn.getChildren().add(weaponsLabel);
                rightColumn.getChildren().add(scII);
                rightColumn.getChildren().add(potionsLabel);
                rightColumn.getChildren().add(scIII);
                rightColumn.setVgrow(scI, Priority.ALWAYS);
                rightColumn.setVgrow(scII, Priority.ALWAYS);
                rightColumn.setVgrow(scIII, Priority.ALWAYS);
                rightColumn.setSpacing(10);
                setColorTheme(currentColorTheme);

            });
            p1.play();
        }
        else {

            // CLEAR ANY EXISTING BUTTONS IN ORDER TO REFRESH IN CASE OF USER INPUT
            keysBox.getChildren().clear();
            weaponsBox.getChildren().clear();
            potionsBox.getChildren().clear();
            rightColumn.getChildren().clear();


            leftColumn.getChildren().clear();
            objectsInRoom.getChildren().clear();

            // CREATE AND ADD ALL INVENTORY ITEMS TO THE objectsInInventory VBOX
            for (String inventoryItem : Player.getInstance().getInventory()) {
                // CONSTRUCT AN ImageView NODE TO HOLD THE OBJECT IMAGE
                boolean objectIsPotionORIG = false;
                Potion potionObject = null;

                for (AdventureObject object : Player.getInstance().getPotions()) {
                    if (inventoryItem.equals(object.getName())) {
                        objectIsPotionORIG = true;
                        potionObject = (Potion) object;
                        break;
                    }
                }

                final Potion potionObjectFinal = potionObject;

                Image roomImageFile;
                if (objectIsPotionORIG) {
                    roomImageFile = new Image("/game-images/" + inventoryItem + ".png");
                } else {
                    roomImageFile = new Image(this.model.getDirectoryName() + "/objectImages/" + inventoryItem + ".jpg");
                }
                ImageView objectImageView = new ImageView(roomImageFile);
                objectImageView.setPreserveRatio(true);
                objectImageView.setFitWidth(100);
                objectImageView.setFitHeight(100);

                // CONSTRUCT A Button NODE
                Button itemButton = new Button(inventoryItem, objectImageView);
                itemButton.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
                // COURTESY OF
                // “JavaFX Button Position the Text under the Image.” JavaFX Button Position the Text under the Image,
                // www.java2s.com/ref/java/javafx-button-position-the-text-under-the-image.html. 14 Oct. 2023.
                itemButton.setContentDisplay(ContentDisplay.TOP);
                // itemButton.setGraphic(objectImageView);
                itemButton.setId(inventoryItem);
                itemButton.setTextFill(Color.BLACK);
                itemButton.setWrapText(true);
                itemButton.setTextAlignment(TextAlignment.CENTER);
                // customizeButton(itemButton, 100, 120);
                makeButtonAccessible(itemButton, inventoryItem + " Button", "This button acts as a TAKE or DROP of this item: " + inventoryItem, "This button acts as a TAKE or DROP of this item: " + inventoryItem + ". Click it to TAKE (if it is in the room) or DROP (if it is in the inventory) the item.");
                // UPDATE LOCATION BASED ON ID
                // COURTESTY OF
                // Stanly Medjo, and Matt. “Getting the ID of a Button in Javafx.” Stack Overflow,
                // stackoverflow.com/questions/47700220/getting-the-id-of-a-button-in-javafx. 14 Oct. 2023.

                // IF IT'S A POTION IN THE INVENTORY, THEN THE BUTTON NEEDS TO BE MADE SO THAT THE POTION CAN BE USED
                if (objectIsPotionORIG) {
                    itemButton.setOnAction(e -> {
                        Button button = (Button) e.getSource();
                        Command healCommand = new PlayerHealCommand(potionObjectFinal);

                        battle.setPlayerCommand(healCommand);

                        if (Player.getInstance().getHp() != 100) {
                            String outcome = battle.executeHeal();



                            itemButton.setDisable(true);

                            String audioFile = "./combat-audio/healaudio.mp3";

                            Media sound = new Media(new File(audioFile).toURI().toString());

                            mediaPlayer = new MediaPlayer(sound);
                            mediaPlayer.play();
                            mediaPlaying = true;

                            ObservableList<Node> children = gridPane.getChildren();
                            for (Node node : children) {
                                int nodeRow = GridPane.getRowIndex(node);
                                int nodeColumn = GridPane.getColumnIndex(node);
                                if (nodeRow == 1 && nodeColumn == 1) {
                                    gridPane.getChildren().remove(node);
                                    break;
                                }
                            }

                            Player player = Player.getInstance();
                            Room room = player.getCurrentRoom();

                            Troll troll = room.roomTroll;
                            String trollName = troll.getName();

                            Image trollImage = new Image("/game-images/" + trollName + ".png");
                            ImageView trollImageView = new ImageView(trollImage);



                            trollImageView.setPreserveRatio(true);
                            trollImageView.setFitWidth(400);
                            trollImageView.setFitHeight(400);



                            //set accessible text
                            trollImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
                            trollImageView.setAccessibleText(trollName);
                            trollImageView.setFocusTraversable(true);


                            healingLabel = new Label("YOU ARE HEALING USING: " + button.getId() + "!");
                            healingLabel.setFont(new Font("Palatino", 16));
                            healingLabel.setDisable(false);

                            setColorTheme(currentColorTheme);

                            battleLabel = new Label("");
                            healingLabel.setFont(new Font("Palatino", 16));
                            healingLabel.setDisable(false);



                            VBox trollVBox = new VBox(5);
                            trollVBox.getChildren().addAll(trollImageView,healingLabel, trollHpLabel, battleLabel);
                            trollVBox.setAlignment(Pos.CENTER);
                            healingLabel.setPadding(new Insets(0, 0, 30, 0));
                            trollVBox.setDisable(false);
                            addTTSEvent();

                            gridPane.add(trollVBox, 1, 1);
                            GridPane.setHalignment(trollVBox, HPos.CENTER);
                            GridPane.setValignment(trollVBox, VPos.CENTER); // Center VBox Vertically


                            helpButton.setDisable(true);
                            menuButton.setDisable(true);


                            // OBJECT BUTTONS (IN INVENTORY)
                            keysBox.setDisable(true);

                            // OBJECT BUTTONS (IN ROOM)
                            objectsInRoom.setDisable(true);

                            // TEXT INPUT
                            inputTextField.setDisable(true);

                            // ESCAPE AND ATTACK BUTTON DISABLED
                            helpButton.setDisable(true);
                            menuButton.setDisable(true);

                            // ESCAPE AND ATTACK BUTTON DISABLED
                            escapeButton.setDisable(true);
                            attackButton.setDisable(true);
                            keysBox.setDisable(true);
                            potionsBox.setDisable(true);

                            PauseTransition pause = new PauseTransition(Duration.seconds(8));
                            pause.setOnFinished(event -> {
                                if (outcome.equals("Healed")) {
                                    updateScene("YOU HAVE HEALED USING: " + potionObjectFinal.getName());
                                    healingLabel.setText("YOU HAVE HEALED USING: " + potionObjectFinal.getName());
                                    updateItems();
                                    updateAttackHP();
                                } else {
                                    updateScene("UNABLE TO HEAL");
                                }
                                stopArticulation();

                                potionsBox.getChildren().remove(itemButton);
                                setColorTheme(currentColorTheme);

                                helpButton.setDisable(false);
                                menuButton.setDisable(false);

                                // ESCAPE AND ATTACK BUTTON DISABLED
                                escapeButton.setDisable(false);
                                attackButton.setDisable(false);
                                potionsBox.setDisable(false);

                            });

                            pause.play();
                        }
                    });
                } else {
                    itemButton.setOnAction(e -> {
                        Button button = (Button) e.getSource();
                        updateLocation(button);
                        setColorTheme(currentColorTheme);
                    });
                }
                addTTSEvent();



                // set button font size
                itemButton.setFont(new Font("Palatino", textSize));

                boolean objectIsWeapon = false;
                Weapon weaponObject = null;

                boolean objectIsPotion2 = false;
                Potion potionObject2 = null;

                if (!(Player.getInstance().getWeapons() == null)) {
                    for (AdventureObject object : Player.getInstance().getWeapons()) {
                        if (itemButton.getId().equals(object.getName())) {
                            objectIsWeapon = true;
                            weaponObject = (Weapon) object;
                            break;
                        }
                    }
                }

                for (AdventureObject object : Player.getInstance().getPotions()) {
                    if (itemButton.getId().equals(object.getName())) {
                        objectIsPotion2 = true;
                        potionObject2 = (Potion) object;
                        break;
                    }
                }

                if (objectIsWeapon) {
                    if (15 >= ((Weapon) weaponObject).getDamageRange()[1]) {
                        itemButton.setStyle("-fx-background-color: #D3D3D3;");
                    } else if (25 >= ((Weapon) weaponObject).getDamageRange()[1]) {
                        itemButton.setStyle("-fx-background-color: #319236;");
                    } else if (35 >= ((Weapon) weaponObject).getDamageRange()[1]) {
                        itemButton.setStyle("-fx-background-color: #4c51f7;");
                    } else if (45 >= ((Weapon) weaponObject).getDamageRange()[1]) {
                        itemButton.setStyle("-fx-background-color: #9d4dbb;");
                    } else {
                        itemButton.setStyle("-fx-background-color: #f3af19;");
                    }
                    weaponsBox.getChildren().add(itemButton);
                } else if (objectIsPotion2) {
                    if (potionObject2.getClass() == Bandages.class) {
                        itemButton.setStyle("-fx-background-color: #D3D3D3;");
                    } else if (potionObject2.getClass() == MedKit.class) {
                        itemButton.setStyle("-fx-background-color: #319236;");
                    } else if (potionObject2.getClass() == ChugJug.class) {
                        itemButton.setStyle("-fx-background-color: #f3af19;");
                    }

                    potionsBox.getChildren().add(itemButton);
                } else {
                    keysBox.getChildren().add(itemButton);
                }


            }

            // CREATE AND ADD ALL OBJECTS IN ROOM TO THE objectsInRoom VBOX
            for (AdventureObject roomItemObject : Player.getInstance().getCurrentRoom().objectsInRoom) {
                // FOR objectsInRoom WE HAVE NO WAY OF GETTING A LIST OF STRINGS WITHOUT MAKING EXTRA METHODS IN MODEL,
                // SO WE MUST JUST GET THE NAME MANUALLY
                String roomItem = roomItemObject.getName();

                // CONSTRUCT AN ImageView NODE TO HOLD THE OBJECT IMAGE
                Image roomImageFile;
                if (roomItemObject instanceof Potion) {
                    roomImageFile = new Image("/game-images/" + roomItem + ".png");
                } else {
                    roomImageFile = new Image(this.model.getDirectoryName() + "/objectImages/" + roomItem + ".jpg");
                }
                ImageView objectImageView = new ImageView(roomImageFile);
                objectImageView.setPreserveRatio(true);
                objectImageView.setFitWidth(100);
                objectImageView.setFitHeight(100);

                // CONSTRUCT A Button NODE
                Button itemButton = new Button(roomItem, objectImageView);
                // COURTESY OF
                // “JavaFX Button Position the Text under the Image.” JavaFX Button Position the Text under the Image,
                // www.java2s.com/ref/java/javafx-button-position-the-text-under-the-image.html. 14 Oct. 2023.
                itemButton.setContentDisplay(ContentDisplay.TOP);
                // itemButton.setGraphic(objectImageView);
                itemButton.setId(roomItem);
                itemButton.setTextFill(Color.BLACK);
                itemButton.setWrapText(true);
                itemButton.setTextAlignment(TextAlignment.CENTER);
                // customizeButton(itemButton, 100, 100);
                makeButtonAccessible(itemButton, roomItem + " Button", "This button acts as a TAKE or DROP of this item: " + roomItem, "This button acts as a TAKE or DROP of this item: " + roomItem + ". Click it to TAKE (if it is in the room) or DROP (if it is in the inventory) the item.");
                // UPDATE LOCATION BASED ON ID
                // COURTESY OF
                // Stanly Medjo, and Matt. “Getting the ID of a Button in Javafx.” Stack Overflow,
                // stackoverflow.com/questions/47700220/getting-the-id-of-a-button-in-javafx. 14 Oct. 2023.
                itemButton.setOnAction(e -> {
                    Button button = (Button) e.getSource();
                    updateLocation(button);
                    setColorTheme(currentColorTheme);
                });
                //itemButton.setOnAction(event -> updateLocation((Button) event.getSource()));

                // set button font size
                itemButton.setFont(new Font("Palatino", textSize));

                // Special styles (for weapons)
                if (roomItemObject.getClass() == Weapon.class) {
                    if (15 >= ((Weapon) roomItemObject).getDamageRange()[1]) {
                        itemButton.setStyle("-fx-background-color: #D3D3D3;");
                    } else if (25 >= ((Weapon) roomItemObject).getDamageRange()[1]) {
                        itemButton.setStyle("-fx-background-color: #319236;");
                    } else if (35 >= ((Weapon) roomItemObject).getDamageRange()[1]) {
                        itemButton.setStyle("-fx-background-color: #4c51f7;");
                    } else if (45 >= ((Weapon) roomItemObject).getDamageRange()[1]) {
                        itemButton.setStyle("-fx-background-color: #9d4dbb;");
                    } else {
                        itemButton.setStyle("-fx-background-color: #f3af19;");
                    }
                    objectsInRoom.getChildren().add(itemButton);
                } else if (roomItemObject instanceof Potion) {
                    if (roomItemObject.getClass() == Bandages.class) {
                        itemButton.setStyle("-fx-background-color: #D3D3D3;");
                    } else if (roomItemObject.getClass() == MedKit.class) {
                        itemButton.setStyle("-fx-background-color: #319236;");
                    } else if (roomItemObject.getClass() == ChugJug.class) {
                        itemButton.setStyle("-fx-background-color: #f3af19;");
                    }
                    objectsInRoom.getChildren().add(itemButton);
                } else {
                    objectsInRoom.getChildren().add(itemButton);
                }

            }
            scO = new ScrollPane(objectsInRoom);
            scO.setPadding(new Insets(10));
            scO.setFitToWidth(true);
            gridPane.add(scO, 0, 1);

            scI = new ScrollPane(keysBox);
            scI.setFitToWidth(true);
            scI.setMaxHeight(140);

            scII = new ScrollPane(weaponsBox);
            scII.setFitToWidth(true);
            scII.setMaxHeight(140);

            scIII = new ScrollPane(potionsBox);
            scIII.setFitToWidth(true);
            scIII.setMaxHeight(140);

            leftColumn.getChildren().add(scO);
            updateHeart();
            leftColumn.setVgrow(scO, Priority.ALWAYS);


            rightColumn.getChildren().add(keysLabel);
            rightColumn.getChildren().add(scI);
            rightColumn.getChildren().add(weaponsLabel);
            rightColumn.getChildren().add(scII);
            rightColumn.getChildren().add(potionsLabel);
            rightColumn.getChildren().add(scIII);
            rightColumn.setVgrow(scI, Priority.ALWAYS);
            rightColumn.setVgrow(scII, Priority.ALWAYS);
            rightColumn.setVgrow(scIII, Priority.ALWAYS);
            rightColumn.setSpacing(10);
            setColorTheme(currentColorTheme);
        }
    }


    /**
     * updateHeart
     * __________________________
     *
     * This method will update the visual display of the player's health.
     * The correct image for the player's health will be displayed in the bottom left
     * corner.
     */
    public void updateHeart() {
        String selectedImage = "";
        if (Player.getInstance().getHp() == 0) {
            selectedImage = "empty";
        } else if (Player.getInstance().getHp() <= 25) {
            selectedImage = "quarter";
        } else if (Player.getInstance().getHp() <= 50) {
            selectedImage = "half";
        } else if (Player.getInstance().getHp() <= 75) {
            selectedImage = "threequarter";
        } else if (Player.getInstance().getHp() <= 100) {
            selectedImage = "full";
        }

        Image heartImageFile = new Image("/game-images/" + selectedImage + "heart.png");
        ImageView heartImageView = new ImageView(heartImageFile);
        heartImageView.setPreserveRatio(true);
        heartImageView.setFitWidth(60);
        heartImageView.setFitHeight(60);

        String hp = "PLAYER HP:\n " + String.valueOf(Player.getInstance().getHp());

        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            int nodeRow = GridPane.getRowIndex(node);
            int nodeColumn = GridPane.getColumnIndex(node);
            if (nodeRow == 2 && nodeColumn == 0) {
                gridPane.getChildren().remove(node);
                break;
            }
        }

        playerHealthInfo = new Button(hp, heartImageView);
        playerHealthInfo.setFont(new Font("Palatino", 12));

        playerHealthInfo.setTextFill(Color.WHITE);
        playerHealthInfo.setTextAlignment(TextAlignment.CENTER);
        setColorTheme(currentColorTheme);

        playerHealthInfo.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#964B00"),
                new CornerRadii(10),
                new Insets(0)
        )));

        addTTSEvent();

        makeButtonAccessible(playerHealthInfo, "Player Health Display", "This node displays the player's health visually and with words.", "This node displays a heart that changes its amount upon taking damage from a troll, along with displaying the health as an integer quantity beside it.");


        gridPane.add( playerHealthInfo, 0, 2, 2, 2); // Add health info
    }

    /**
     * updateLocation
     * __________________________
     *
     * This method will update the location of the item on the screen upon the press of a Button in the scene graph.
     *
     * This method will also update the model's logic to ensure the items' state in the inventory and in the room
     * are being updated appropriately.
     *
     * @param button the button input on the object to pick up or drop
     */
    private void updateLocation(Button button) {
        boolean inInventory = false;

        // TRAVERSE THE NODES IN ALL VBOXES TO FIND IF THE NODE IS PRESENT
        ObservableList<Node> inventoryObjects = keysBox.getChildren();
        for(Node node : inventoryObjects) {
            if (node.getId().equals(button.getId())) {
                inInventory = true;
                break;
            }
        }

        ObservableList<Node> weaponObjects = weaponsBox.getChildren();
        for(Node node : weaponObjects) {
            if (node.getId().equals(button.getId())) {
                inInventory = true;
                break;
            }
        }

        ObservableList<Node> potionObjects = potionsBox.getChildren();
        for(Node node : potionObjects) {
            if (node.getId().equals(button.getId())) {
                inInventory = true;
                break;
            }
        }

        if (inInventory) {
            // IF IN THE INVENTORY, WE TRAVERSE THE INVENTORY
            for (String inventoryItem : Player.getInstance().getInventory()) {
                if (button.getId().equals(inventoryItem)) {

                    boolean objectIsWeapon = false;
                    boolean objectIsPotion = false;

                    // IF THE ID OF THE BUTTON MATCHES THE ITEM WE NEED, MOVE IT TO THE VBOX OF THE ROOM...
                    if(Player.getInstance().getWeapons() != null) {
                        for (Weapon weaponObject : Player.getInstance().getWeapons()) {
                            if (weaponObject.getName().equals(button.getId())) {
                                objectIsWeapon = true;
                            }
                        }
                    }

                    for (Potion potionObject : Player.getInstance().getPotions()) {
                        if (potionObject.getName().equals(button.getId())) {
                            objectIsPotion = true;
                        }
                    }

                    if (objectIsWeapon) {
                        weaponsBox.getChildren().remove(button);
                    } else if (objectIsPotion) {
                        potionsBox.getChildren().remove(button);
                    } else {
                        keysBox.getChildren().remove(button);
                    }


                    objectsInRoom.getChildren().add(button);
                    updateScene("YOU HAVE DROPPED:\n " + inventoryItem);


                    // UPDATE THE MODEL'S LOGIC KEEPING TRACK OF WHERE ITEMS ARE
                    for (AdventureObject inventoryObject : Player.getInstance().inventory) {
                        if (inventoryObject.getName().equals(inventoryItem)) {
                            Player.getInstance().inventory.remove(inventoryObject);
                            Player.getInstance().getCurrentRoom().objectsInRoom.add(inventoryObject);
                            break;
                        }
                    }



                    break;
                }
            }
        } else {
            // OTHERWISE, TRAVERSE THE ITEMS IN THE ROOM AND MOVE TO INVENTORY
            for (AdventureObject roomItem : Player.getInstance().getCurrentRoom().objectsInRoom) {
                // AGAIN, WE NEED TO GET THE STRING MANUALLY FROM THE AdventureObject INSTANCE VIA .getName()
                if (button.getId().equals(roomItem.getName())) {
                    // IF THE ID OF THE BUTTON MATCHES THE ITEM WE NEED, MOVE IT TO THE VBOX OF THE INVENTORY...

                    objectsInRoom.getChildren().remove(button);
                    if (roomItem.getClass() == Weapon.class) {
                        weaponsBox.getChildren().add(button);
                    } else if (roomItem instanceof Potion) {
                        // IF IT'S A POTION, REPROGRAM LOGIC SO IT CANNOT BE RETURNED TO A ROOM
                        button.setOnAction(e -> {
                            Button but = (Button) e.getSource();

                            Command healCommand = new PlayerHealCommand((Potion) roomItem);
                            battle.setPlayerCommand(healCommand);
                            String outcome = (String) battle.executeHeal();
                            stopArticulation();

                            updateScene("HEALING USING:\n " + button.getId() + "!");

                            String audioFile = "/combat-audio/healaudio.mp3";

                            Media sound = new Media(new File(audioFile).toURI().toString());

                            mediaPlayer = new MediaPlayer(sound);
                            mediaPlayer.play();
                            mediaPlaying = true;

                            PauseTransition pause = new PauseTransition(Duration.seconds(3));
                            pause.setOnFinished(event -> {
                                if (outcome.equals("Healed")) {
                                    updateScene("YOU HAVE HEALED USING: " + roomItem.getName());
                                    updateItems();
                                } else {
                                    updateScene("UNABLE TO HEAL");
                                }

                                potionsBox.getChildren().remove(button);
                                setColorTheme(currentColorTheme);
                            });

                            pause.play();


                        });
                        potionsBox.getChildren().add(button);
                    } else {
                        keysBox.getChildren().add(button);
                    }

                    updateScene("YOU HAVE TAKEN:\n " + roomItem.getName());

                    // AND UPDATE THE MODEL'S LOGIC KEEPING TRACK OF WHERE ITEMS ARE
                    for (AdventureObject roomObject : Player.getInstance().getCurrentRoom().objectsInRoom) {
                        if (roomObject.getName().equals(roomItem.getName())) {
                            Player.getInstance().getCurrentRoom().objectsInRoom.remove(roomObject);
                            Player.getInstance().inventory.add(roomObject);
                            break;
                        }
                    }
                    break;
                }
            }
        }

    }


    /*
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        if (!this.helpToggle) {
            // SETUP LABEL FRONT-END CHARACTERISTICS
            Label instructionsLabel =  new Label();
            instructionsLabel.setText(this.model.getInstructions());
            instructionsLabel.setAlignment(Pos.CENTER);
            // set help text color based on theme
            if (currentColorTheme.equals("Dark Theme")){
                instructionsLabel.setStyle("-fx-text-fill: white;");
            } else{
                instructionsLabel.setStyle("-fx-text-fill: black;");
            }

            // instructionsLabel.setFont(new Font("Arial", 13));
            instructionsLabel.setWrapText(true);

            // REMOVE OLD NODE BY FINDING NODE AT CELL (1, 1)
            // BLUEPRINT COURTESY OF
            // Rongeegee, et al. “In Javafx, How to Remove a Specific Node from a Gridpane with the Coordinate of It?”
            // Stack Overflow,
            // stackoverflow.com/questions/45778386/in-javafx-how-to-remove-a-specific-node-from-a-gridpane-with-the-coordinate-of.
            // 14 Oct. 2023.
            ObservableList<Node> children = gridPane.getChildren();
            for(Node node : children) {
                int nodeRow = GridPane.getRowIndex(node);
                int nodeColumn = GridPane.getColumnIndex(node);
                if (nodeRow == 1 && nodeColumn == 1) {
                    gridPane.getChildren().remove(node);
                    break;
                }
            }

            // ADD THE LABEL TO THE CELL
            gridPane.add(instructionsLabel, 1, 1);
            this.helpToggle = true;
        } else {
            ObservableList<Node> children = gridPane.getChildren();
            for(Node node : children) {
                int nodeRow = GridPane.getRowIndex(node);
                int nodeColumn = GridPane.getColumnIndex(node);
                if (nodeRow == 1 && nodeColumn == 1) {
                    gridPane.getChildren().remove(node);
                    break;
                }
            }

            updateScene("");
            this.helpToggle = false;
        }
    }


    /**
     * This button handles the event related to the
     * escape button by reverting the player back
     * to the last room they were in, then updating
     * the scene
     */
    public void addEscapeEvent(){

        escapeButton.setOnAction(e -> {
            stopArticulation();
            Player player = Player.getInstance();
            Command<String> escapeCommand = new PlayerEscapeCommand();
//            String outcome = escapeCommand.execute(); // change to battle.execute();

            battle.setPlayerCommand(escapeCommand);
            String outcome = battle.executeEscape();

            if (outcome.equals("Stayed")) {
                battleLabel.setText("Cannot escape to room with forced passages!");
            }else if(outcome.equals("Escaped")){
                updateScene("");
                updateItems();
            }else{
                battleLabel.setText("Cannot escape!");
            }
        });

    }



    /**
     * This button handles the event related to the
     * attack button by dealing damage to the troll,
     * and the troll dealing damage back. If the player
     * the troll, the player will have access to the room.
     * If the player dies, either a new game is loaded or
     * the last saved game is loaded.
     */
    public void addAttackEvent(){

        attackButton.setOnAction(e -> {


            Player player = Player.getInstance();
            Troll troll = player.getCurrentRoom().roomTroll;

            Command<Integer> playerAttack = new PlayerAttackCommand();
            Command<Integer> trollAttack = new TrollAttackCommand();

            Integer playerDamage = playerAttack.execute();
            Integer trollDamage = trollAttack.execute();

            battle.setPlayerCommand(playerAttack);
            battle.setTrollCommand(trollAttack);

            battle.executeAttack();
            updateAttackHP();

            // Declare combat audio variables
            boolean playerAttackSuccess = false;
            boolean trollDeath = false;
            boolean playerDeath = false;


            setColorTheme(currentColorTheme);
            setTextSize(textSize);

            healingLabel.setText("");
            if(player.getHp() <= 0){
                attackButton.setDisable(true);
                escapeButton.setDisable(true);
                potionsBox.setDisable(true);
                weaponsBox.setDisable(true);
                keysBox.setDisable(true);

                playerDeath = true;
                playCombatAudio(playerAttackSuccess, trollDeath, playerDeath);

                if(careTaker.getSaves().isEmpty()){
                    battleLabel.setText("You have died... You will be loaded into a new game.");
                }else{
                    battleLabel.setText("You have died... You will be loaded into your most recently saved game.");
                }

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(event -> {
                    ArrayList<Memento> saves = careTaker.getSaves();

                    if(!saves.isEmpty()){
                        int lastElement = saves.size() - 1;
                        Memento lastSave = saves.get(lastElement);
                        careTaker.load(lastSave.getName());
                    }else{
                        careTaker.loadNewGame();
                    }
                });
                pause.play();


            }
            else if(troll.getHp() <= 0){
                battleLabel.setText("You have defeated the troll. You will now enter the room...");
                player.getCurrentRoom().removeTroll();

                attackButton.setDisable(true);
                escapeButton.setDisable(true);
                potionsBox.setDisable(true);
                weaponsBox.setDisable(true);
                keysBox.setDisable(true);

                trollDeath = true;
                playCombatAudio(playerAttackSuccess, trollDeath, playerDeath);

                // if troll dies, enter room.

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(event -> {
                    updateScene("");
                    updateItems();

                });
                pause.play();

            }
            else{

                // if player attack was a success, set the boolean var. accordingly
                if (playerDamage > 0) {
                    playerAttackSuccess = true;
                }
                playCombatAudio(playerAttackSuccess, trollDeath, playerDeath);

                battleLabel.setText("You have dealt " + playerDamage + " to the troll. \nTroll has dealt " + trollDamage + " to you.");

            }


        });

    }


    /**
     * Calling this method will play the appropriate player death sound/troll death sound/player attack successful/
     * player taking damage.
     */
    public void playCombatAudio(boolean playerAttackSuccess, boolean trollDeath, boolean playerDeath){
        this.stopArticulation();

        String audioFile;
        String combatAudioType = "";

        if (playerDeath) {
            combatAudioType = "playerdeath.wav";
        } else if (playerAttackSuccess) {
            combatAudioType = "attacksuccess.mp3";
        } else if (trollDeath) {
            combatAudioType = "trolldeath.wav";
        } else {
            combatAudioType = "attackmiss.wav";
        }

        audioFile = "./combat-audio/" + combatAudioType.toLowerCase();

        Media sound = new Media(new File(audioFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;
    }

    /**
     * This method handles the event related to updating the Troll and
     * Players' HP. It retrieves the player instance and accesses the Troll
     * in the current room. The HP of the Troll and Player are then updated
     * visually.
     */
    public void updateAttackHP(){

        Player player = Player.getInstance();
        Troll troll = player.getCurrentRoom().roomTroll;


        trollHpLabel.setText("Troll HP: " + troll.getHp());
        updateHeart();
        addTTSEvent();


    }


    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
            setColorTheme(currentColorTheme);
        });
    }

    /**
     * This method reads the text in the center when clicked on
     */
    public void addTTSEvent() {
        roomDescLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(roomDescLabel);
            }
        });

        objLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(objLabel);
            }
        });

        weaponsLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(weaponsLabel);
            }
        });

        potionsLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(potionsLabel);
            }
        });

        healingLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(healingLabel);
            }
        });

        keysLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(keysLabel);
            }
        });

        invLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(invLabel);
            }
        });

        trollHpLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(trollHpLabel);
            }
        });

        battleLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(battleLabel);
            }
        });

        playerHealthInfo.setOnAction(event -> {
            if (txtToSpeech){
                try
                {
                    //synthesizer.allocate();
                    //resume a Synthesizer
                    synthesizer.resume();
                    //synthesizer.deallocate();
                    //speak the specified text until the QUEUE become empty
                    synthesizer.speakPlainText(playerHealthInfo.getText(), null);
                    synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
                    //deallocating the Synthesizer
                    //synthesizer.deallocate();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        commandLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(commandLabel);
            }
        });

        healingLabel.setOnMouseClicked(e -> {
            if (txtToSpeech){
                readText(healingLabel);
            }
        });

    }


    /**
     * This method handles the event related to the
     * menu button.
     */
    public void addMenuEvent() {

        menuButton.setOnAction(e -> {
            gridPane.requestFocus();
            MenuView menuView = new MenuView(this);
        });
    }


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = Player.getInstance().getCurrentRoom().getRoomName();

        if (!Player.getInstance().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        musicFile = musicFile.replace(" ","-");

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;

    }

    /**
     * This method reads whatever is in the center out loud
     */
    public void readText(Label txt) {
        try
        {
            //synthesizer.allocate();
            //resume a Synthesizer
            synthesizer.resume();
            //synthesizer.deallocate();
            //speak the specified text until the QUEUE become empty
            synthesizer.speakPlainText(txt.getText(), null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            //deallocating the Synthesizer
            //synthesizer.deallocate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method stops articulations
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }

    /**
     * toggle value of txtToSpeech attribute
     */
    public void toggleTTS() {
        if (txtToSpeech){
            this.txtToSpeech = false;
        } else{
            this.txtToSpeech = true;
        }
    }

    /**
     * Getter for caretaker attribute
     */
    public CareTaker getCareTaker() {
        return this.careTaker;
    }

    /**
     * get value of txtToSpeech attribute
     */
    public boolean getTTS() {
        return this.txtToSpeech;
    }

    /**
     * Change size of text on screen
     */
    public void setTextSize(double size) {
        Font font = (new Font("Palatino", size));
        roomDescLabel.setFont(font);
        menuButton.setFont(font);
        helpButton.setFont(font);
        // objects in room, inventory
        textSize = size;
        updateItems();
        invLabel.setFont(font);
        objLabel.setFont(font);
        keysLabel.setFont(font);
        potionsLabel.setFont(font);
        weaponsLabel.setFont(font);
        commandLabel.setFont(font);
        trollHpLabel.setFont(font);
        escapeButton.setFont(font);
        battleLabel.setFont(font);
        attackButton.setFont(font);
        // adjust command input box to accomidate player health info
//        inputTextField.setMaxWidth(760 - size*4);
//        inputTextField.setMinWidth(760 - size*4);
//        inputTextField.setTranslateX(20 + size*1.1);
        //inputTextField.set
    }



    public void setColorTheme(String color){
        currentColorTheme = color;

        String backColorCode = "";
        String txtColorCode = "";
        if (color.equals("Light Theme")){
            backColorCode = "#FFFFFF";
            txtColorCode = "black";
            weaponsBox.setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
            scIII.setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
            scII.setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
            scI.setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
            scO.setStyle("-fx-background: #FFFFFF; -fx-background-color:transparent;");
            inputTextField.setStyle("-fx-background-color: " + "#000000" + "; -fx-text-fill: #FFFFFF;");
        } else if (color.equals("Dark Theme")){
            backColorCode = "#000000";
            txtColorCode = "white";
            weaponsBox.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            scIII.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            scII.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            inputTextField.setStyle("-fx-background-color: " + "#FFFFFF" + "; -fx-text-fill: #000000;");
        }

        // change colors
        Background BkGrd = new Background(new BackgroundFill(Color.valueOf(backColorCode), new CornerRadii(0), new Insets(0)));
        gridPane.setBackground(BkGrd);
        roomDescLabel.setBackground(BkGrd);
        roomDescLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        commandLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        battleLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        trollHpLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        objLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        invLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        weaponsLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        potionsLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        keysLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        roomPane.setStyle("-fx-background-color: " + backColorCode + ";");
        textEntry.setStyle("-fx-background-color: " + backColorCode + ";");
        commandLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");
        healingLabel.setStyle("-fx-text-fill: " + txtColorCode + ";");

        // refresh help text if currently displayed
        if (helpToggle){
            showInstructions();
            showInstructions();
            stopArticulation();
        }
    }

}
