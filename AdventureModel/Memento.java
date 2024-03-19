package AdventureModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Stack;

/**
 * This class stores the state of an AdventureGame object
 */
public class Memento implements Serializable {

    private String savedGame;
    private String gameDetails;
    public int savedHealth; // attributes of player in save
    /**
     * The current room that the player is located in.
     */
    public int savedCurrentRoomNum;

    /**
     * The name of the player.
     */
    public String savedPlayerName;

    /**
     * The last room in which the player was located.
     */
    public Stack<Room> savedRooms = new Stack<>();

    /**
     * The list of items that the player is carrying at the moment.
     */
    public ArrayList<AdventureObject> savedInventory = new ArrayList<AdventureObject>();

    /**
     * Name of save file
     */
    String saveName;

    /**
     * AdventureGame is provided upon construction and cannot be changed later
     */
    public Memento(AdventureGame gameToSave, String name) {
        // record player attributes
        Player player = gameToSave.getPlayer();

        this.savedHealth = player.getHp();
        this.savedPlayerName = player.getName();
        this.savedInventory.addAll(player.inventory);
        this.savedRooms.addAll(player.rooms);
        this.savedCurrentRoomNum = player.currentRoom.getRoomNumber();


        // record details
        // room name and number
        StringBuilder details = new StringBuilder();
        details.append("Room Number: " + player.getCurrentRoom().getRoomNumber() + "\n");
        details.append("Room Name: " + player.getCurrentRoom().getRoomName() + "\n");

        // append inventory
        details.append("Inventory: \n");
        for (String objName: player.getInventory()){
            details.append(objName);
            details.append("\n");
        }
        gameDetails = details.toString();

        // convert game to string
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameToSave);
            oos.close();
            this.savedGame = Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.saveName = name;
    }

    /**
     * Getter to read AdventureGame stored in the memento
     */
    public AdventureGame getState(){
        AdventureGame restoredGame = null;
        // convert string to game
        try {
            byte[] data = Base64.getDecoder().decode(savedGame);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            restoredGame = (AdventureGame) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException e) {
            System.out.print("ClassNotFoundException occurred.");
        } catch (IOException e) {
            System.out.print("IOException occurred.");
        }
        return restoredGame;
    }

    /**
     * Getter to read the name of the save.
     */
    public String getName(){
        return this.saveName;
    }

    /**
     * Getter to see details of the AdventureGame stored in the memento
     * such as, player room name, number, inventory
     */
    public String getDetails(){
        return this.gameDetails;
    }
}
