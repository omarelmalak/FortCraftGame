package views;

import AdventureModel.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Slider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;

import javax.speech.synthesis.Synthesizer;

import static views.AdventureGameView.makeButtonAccessible;


/**
 * Class MenuView.
 *
 * Toggles various accessibility features.
 */
public class MenuView {

    private AdventureGameView adventureGameView;

    private Button closeWindowButton;

    Button saveButton, loadButton;

    private ComboBox colorTheme;
    private ComboBox difficulty;
    private Button txtToSpeech;
    private Label txtSizeLabel;// to label slider
    private Label difficultyLabel = new Label("Difficulty");// to label difficulty dropdown
    private Label themeLabel = new Label("Color Theme");// to label color theme dropdown
    private Slider txtSize;

    private static String defaultDiff = "Medium";

    private ListView<String> GameList;
    private String filename = null;

    public MenuView(AdventureGameView adventureGameView){

        //note that the buttons in this view are not accessible!!
        this.adventureGameView = adventureGameView;

        GameList = new ListView<>(); //to hold all the file names

        final Stage dialog = new Stage(); //dialogue box
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");

        // style text size slider
        txtSize = new Slider(14, 24, 0.5);
        txtSize.setShowTickMarks(true);
        txtSize.setValue(adventureGameView.trollHpLabel.getFont().getSize());
        txtSize.setShowTickLabels(true);
        txtSize.setMajorTickUnit(1);
        txtSize.setMinorTickCount(1);
        txtSize.setSnapToTicks(true);
        txtSize.setBlockIncrement(10);

        // add label "Text Size" above slider
        txtSizeLabel = new Label("Text Size");
        txtSizeLabel.setStyle("-fx-text-fill: white;");
        txtSizeLabel.setFont(new Font("Arial", 16));

        // add label "Difficulty" above difficulty dropdown
        themeLabel.setStyle("-fx-text-fill: white;");
        themeLabel.setFont(new Font("Arial", 16));

        // add label "Color Theme" above color theme dropdown
        difficultyLabel.setStyle("-fx-text-fill: white;");
        difficultyLabel.setFont(new Font("Arial", 16));



        txtToSpeech = new Button("Enable Text To Speech");
        txtToSpeech.setId("text to speech"); // DO NOT MODIFY ID
        makeButtonAccessible(txtToSpeech, "decrease text size", "This is the button to decrease text size", "Use this button to make the text smaller.");
        customizeButton(txtToSpeech, 200,50);

        // create dropdown for difficulties
        String[] dif = {"Easy", "Medium", "Hard"};


        difficulty = new ComboBox<>(FXCollections.observableArrayList(dif));
//        difficulty.setPromptText("Current Game Difficulty");

        difficulty.setPromptText(defaultDiff);

        // by default set the difficulty to medium and check others
        if(defaultDiff.equalsIgnoreCase("easy")){
            difficulty.getSelectionModel().select(0);
        }else if(defaultDiff.equalsIgnoreCase("medium")) {
            difficulty.getSelectionModel().select(1);
        }else if(defaultDiff.equalsIgnoreCase("hard")){
            difficulty.getSelectionModel().select(2);
        }


        // get current difficulty from game if trolls exist
        if (!(this.adventureGameView.model.getActiveTrolls().isEmpty())){
            // get first troll and check difficulty
            Troll troll = this.adventureGameView.model.getActiveTrolls().get(0);

            if (troll.getClass() == EasyTroll.class){
                difficulty.setPromptText(defaultDiff);
            } else if(troll.getClass() == MediumTroll.class){
                difficulty.setPromptText(defaultDiff);
            } else{
                difficulty.setPromptText(defaultDiff);
            }
        }

        // instantiate and call state class appropriately to element selected
        difficulty.setOnAction(e -> {
            String selected = (String) difficulty.getSelectionModel().getSelectedItem();
            String successTxt = "";
            if (selected.equalsIgnoreCase("easy")) {
                defaultDiff = "Easy";
                difficulty.setPromptText(defaultDiff);

                DifficultyState state = new EasyState();
                state.applyState(this.adventureGameView.model);
                successTxt = "The easy difficulty has been set.";
            } else if (selected.equalsIgnoreCase("medium")) {
                defaultDiff = "Medium";
                difficulty.setPromptText(defaultDiff);

                DifficultyState state = new MediumState();
                state.applyState(this.adventureGameView.model);
                successTxt = "The medium difficulty has been set.";
            } else if (selected.equalsIgnoreCase("hard")) {
                defaultDiff = "Hard";
                difficulty.setPromptText(defaultDiff);


                DifficultyState state = new HardState();
                state.applyState(this.adventureGameView.model);
                successTxt = "The hard difficulty has been set.";
            } else {
                successTxt = "Invalid difficulty option selected.";
                System.out.println("Invalid difficulty option selected.");
            }
            this.adventureGameView.updateScene(successTxt);
        });

        // create dropdown for color themes
        String[] themes = {"Light Theme", "Dark Theme"};


        colorTheme = new ComboBox<>(FXCollections.observableArrayList(themes));
        colorTheme.setPromptText(this.adventureGameView.currentColorTheme);

        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();


        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> dialog.close());
        makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the load game window", "Use this button to close the load game window.");

        // set button events
        txtToSpeech.setOnAction(e -> {
            if (!adventureGameView.getTTS()){
                // set text to disable
                txtToSpeech.setText("Disable Text To Speech");
                // darken button
                txtToSpeech.setStyle("-fx-background-color: #13691a; -fx-text-fill: white;");
                // enable tts
                this.adventureGameView.toggleTTS();
            } else{
                // set text to enable
                txtToSpeech.setText("Enable Text To Speech");
                // lighten button
                txtToSpeech.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
                //disable
                this.adventureGameView.toggleTTS();
            }
        });

        // set slider event
        txtSize.setOnMouseDragged(e -> {
            adventureGameView.setTextSize(txtSize.getValue());
        });
        txtSize.setOnMouseClicked(e -> {
            adventureGameView.setTextSize(txtSize.getValue());
        });

        // set color theme combobox event
        colorTheme.setOnAction(e -> {
            // send choice to adventure game method
            adventureGameView.setColorTheme((String)colorTheme.getValue());
        });

        VBox selectGameBox = new VBox(10, txtToSpeech, difficultyLabel,difficulty, themeLabel,colorTheme, saveButton, loadButton, txtSizeLabel, txtSize);


        // darken text to speech if enabled when menu opened
        if (this.adventureGameView.getTTS()){
            txtToSpeech.setStyle("-fx-background-color: #13691a; -fx-text-fill: white;");
            txtToSpeech.setText("Disable Text To Speech");
        }

        selectGameBox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(selectGameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();

        addTTSEvent();
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
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            adventureGameView.gridPane.requestFocus();
            SaveView saveView = new SaveView(adventureGameView);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            adventureGameView.gridPane.requestFocus();
            LoadView loadView = new LoadView(adventureGameView);
        });
    }

    /**
     * This method reads the text in the center when clicked on
     */
    public void addTTSEvent() {
        txtSizeLabel.setOnMouseClicked(e -> {
            if (adventureGameView.getTTS()) {
                readText(txtSizeLabel);
            }
        });

        difficultyLabel.setOnMouseClicked(e -> {
            if (adventureGameView.getTTS()) {
                readText(difficultyLabel);
            }
        });

        themeLabel.setOnMouseClicked(e -> {
            if (adventureGameView.getTTS()) {
                readText(themeLabel);
            }
        });
    }
    /**
     * This method reads whatever is in the center out loud
     */
    public void readText(Label txt) {
        try
        {
            //synthesizer.allocate();
            //resume a Synthesizer
            adventureGameView.synthesizer.resume();
            //synthesizer.deallocate();
            //speak the specified text until the QUEUE become empty
            adventureGameView.synthesizer.speakPlainText(txt.getText(), null);
            adventureGameView.synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            //deallocating the Synthesizer
            //synthesizer.deallocate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
