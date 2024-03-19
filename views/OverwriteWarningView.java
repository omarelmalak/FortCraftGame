package views;

import AdventureModel.AdventureGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;


/**
 * Class OverwriteWarningView.
 *
 * Loads Serialized adventure games.
 */
public class OverwriteWarningView {

    private SaveView saveView;
    private AdventureGameView adventureGameView;
    private Label popupMessage = new Label("Warning: Save will overwrite existing file");
    private Button popupConfirm = new Button("Save Anyways");
    private Button popUpCancel = new Button("Don't Save");
    private Button closeWindowButton = new Button("Close Window");
    private ListView<String> GameList;
    private String filename = null;
    final Stage dialog = new Stage();

    public OverwriteWarningView(SaveView view, AdventureGameView adView){

        //note that the buttons in this view are not accessible!!
        this.saveView = view;
        this.adventureGameView = adView;


        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");


        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the load game window", "Use this button to close the load game window.");




        VBox selectGameBox = new VBox(10, popupMessage, popupConfirm, popUpCancel);

        // Default styles which can be modified
        popupMessage.setStyle("-fx-text-fill: #e8e6e3");
        popupMessage.setFont(new Font(16));

        popupConfirm.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        popupConfirm.setPrefSize(200, 50);
        popupConfirm.setFont(new Font(16));
        addConfirmEvent();

        popUpCancel.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        popUpCancel.setPrefSize(200, 50);
        popUpCancel.setFont(new Font(16));
        addCancelEvent();

        selectGameBox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(selectGameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * This method handles the event related to the
     * confirm button.
     */
    public void addConfirmEvent() {
        popupConfirm.setOnAction(e -> {
            saveView.replaceSave();
            dialog.close();
        });
    }


    /**
     * This method handles the event related to the
     * cancel button.
     */
    public void addCancelEvent() {
        popUpCancel.setOnAction(e -> {
            dialog.close();
        });
    }

}
