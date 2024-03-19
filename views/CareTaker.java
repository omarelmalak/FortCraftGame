package views;

import java.io.*;
import java.util.*;

import java.io.Serializable;
import java.util.ArrayList;

import AdventureModel.AdventureGame;
import AdventureModel.Memento;
import AdventureModel.Player;
import views.AdventureGameView;

/**
 * This class keeps track of the progress
 * of the player in the game.
 */
public final class CareTaker implements Serializable{
    private ArrayList<Memento> saves = new ArrayList<Memento>();
    private AdventureGameView adventureGameView;


    /**
     * Constructor is given an adventureGame to load to and save from
     */
    public CareTaker (AdventureGameView view){
        this.adventureGameView = view;
    }

    /**
     * Saves the current game to the caretaker
     */
    public void save(String name){
        Memento save = new Memento(adventureGameView.model, name);
        saves.add(save);

        // update serialized caretaker
        serialize();
    }

    /**
     * Replaces save already in list.
     */
    public void replaceSave(String svName){
        Memento save = new Memento(adventureGameView.model, svName);

        //int index = 0;
        for (int i = 0 ; i < saves.size(); i++){
            if (saves.get(i).getName().equals(svName)){
                saves.remove(i);
                saves.add(i, save);
            }
        }

        // update serialized caretaker
        serialize();
    }


    /**
     * Replaces the current game with the save at a given index.
     * Returns true iff the save was found and loaded successfully.
     */
    public boolean load(String name){
        this.adventureGameView.firstTrollEncounter = false;
        //find game
        for (Memento memento: saves){
            if (memento.getName().equals(name)){
                AdventureGame loadedGame = memento.getState();

                // store player from previous game
                Player oldPlayer = Player.getInstance();

                //clear player attributes
                oldPlayer.inventory.clear();
                oldPlayer.rooms.clear();

                // update player attributes to match loaded game
                oldPlayer.health = memento.savedHealth;
                oldPlayer.currentRoom = loadedGame.getRooms().get(memento.savedCurrentRoomNum);
                oldPlayer.inventory.addAll(memento.savedInventory);
                oldPlayer.name = memento.savedPlayerName;
                oldPlayer.rooms.addAll(memento.savedRooms);

                // give updated player to new game
                loadedGame.player = oldPlayer;

                //replace model
                this.adventureGameView.model = loadedGame;

                //update scene
                this.adventureGameView.updateScene("");
                this.adventureGameView.updateItems();
                this.adventureGameView.setColorTheme(adventureGameView.currentColorTheme);

                return true;

            }
        }
        return false;
    }


    /**
     * Initializes a new game in the adventure game application.
     * This method loads the game directory name, creates a new instance of
     * the AdventureGame model with the directory name and updates the game view.
     * It refreshes the game scene, updates the items displayed, and sets the
     * color theme based on the current setting.
     */
    public void loadNewGame(){
        String s = this.adventureGameView.model.getDirectoryName();
        this.adventureGameView.model = new AdventureGame(s.substring(5));

        this.adventureGameView.firstTrollEncounter = false;

        //update scene
        this.adventureGameView.updateScene("");
        this.adventureGameView.updateItems();
        this.adventureGameView.setColorTheme(adventureGameView.currentColorTheme);

    }

    /**
     * Get details for load view to display
     */
    public String getDetails(int index){
        return saves.get(index).getDetails();
    }

    /**
     * Get details for load view to display
     */
    public ArrayList<Memento> getSaves(){
        return this.saves;
    }

    /**
     * Save the current caretaker to a file
     *
     */
    public void serialize() {
        try {
            File saveLocation = new File("Games/Caretaker.ser");
            if (saveLocation.exists()){
                saveLocation.delete();
            }
            FileOutputStream outfile = new FileOutputStream(saveLocation);
            ObjectOutputStream oos = new ObjectOutputStream(outfile);
            oos.writeObject(saves);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Setter for the caretaker's adventureGameView
     *
     */
    public void setAdventureGameView(AdventureGameView adView) {
        this.adventureGameView = adView;
    }

    /**
     * Setter for the caretaker's saves
     *
     */
    public void setSaves(ArrayList<Memento> savess) {
        this.saves = savess;
    }


}
