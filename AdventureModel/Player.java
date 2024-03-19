package AdventureModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * This class keeps track of the progress
 * of the player in the game.
 */
public final class Player implements Attackable,Serializable{
    // KEEP USING this KEYWORD AS PER
    // https://stackoverflow.com/questions/41709340/using-this-keyword-in-a-self-maintained-singleton-class-or-use-instance-always#:~:text=field1%20will%20refer%20to%20the,trick%20of%20the%20this%20keyword%20!
    private static Player instance;
    public int health;

    /**
     * The current room that the player is located in.
     */
    public Room currentRoom;

    /**
     * The name of the player.
     */
    public String name;

    /**
     * The list of every room in which the player has entered, in order.
     */

    public Stack<Room> rooms = new Stack<>();


    /**
     * The list of items that the player is carrying at the moment.
     */
    public ArrayList<AdventureObject> inventory;

    /**
     * Private constructor for the Player class to maintain singleton pattern.
     */
    private Player() {
        this.name = "Player";
    }


    /**
     * Singleton instance method to get the single instance of the Player.
     *
     * @return The single instance of the Player.
     */
    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }

        return instance;
    }

    /**
     * This method adds an object into players inventory if the object is present in
     * the room and returns true. If the object is not present in the room, the method
     * returns false.
     *
     * @param object name of the object to pick up
     * @return true if picked up, false otherwise
     */
    public boolean takeObject(String object){
        if(this.currentRoom.checkIfObjectInRoom(object)){
            AdventureObject object1 = this.currentRoom.getObject(object);
            this.currentRoom.removeGameObject(object1);
            this.addToInventory(object1);
            return true;
        } else {
            return false;
        }
    }


    /**
     * checkIfObjectInInventory
     * __________________________
     * This method checks to see if an object is in a player's inventory.
     *
     * @param s the name of the object
     * @return true if object is in inventory, false otherwise
     */
    public boolean checkIfObjectInInventory(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) return true;
        }
        return false;
    }


    /**
     * This method drops an object in the players inventory and adds it to the room.
     * If the object is not in the inventory, the method does nothing.
     *
     * @param s name of the object to drop
     */
    public void dropObject(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) {
                this.currentRoom.addGameObject(this.inventory.get(i));
                this.inventory.remove(i);
            }
        }
    }

    /**
     * Setter method for the current room attribute.
     *
     * @param currentRoom The location of the player in the game.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.rooms.add(currentRoom);
        this.currentRoom = currentRoom;
    }

    /**
     * This method adds an object to the inventory of the player.
     *
     * @param object Prop or object to be added to the inventory.
     */
    public void addToInventory(AdventureObject object) {
        this.inventory.add(object);
    }



    /**
     * Getter method for the current room attribute.
     *
     * @return current room of the player.
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    public void setInventory() {
        this.inventory = new ArrayList<AdventureObject>();
    }

    public void setHealth(int health) {
        this.health = health;
    }


    /**
     * Getter method for string representation of inventory.
     *
     * @return ArrayList of names of items that the player has.
     */
    public ArrayList<String> getInventory() {
        ArrayList<String> objects = new ArrayList<>();
        for(int i=0;i<this.inventory.size();i++){
            objects.add(this.inventory.get(i).getName());
        }
        return objects;
    }


    /**
     * Performs an attack and returns the amount of damage dealt.
     *
     * @return An integer representing the damage dealt.
     */
    @Override
    public int attack() {
        Weapon strongestWeapon = getStrongestWeapon();
        if(strongestWeapon != null){
            return strongestWeapon.calculateDamage();
        }else{
            Random r = new Random();
            return r.nextInt(20 - 10) + 10;
        }
    }

    /**
     * Retrieves the current health points of the player.
     *
     * @return The current health points as an integer.
     */
    @Override
    public int getHp() {
        return this.health;
    }

    /**
     * Retrieves the name of the player.
     *
     * @return The name of the player as a String.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Reduces the health of the player by a specified amount.
     * If the health falls below zero, it is set to zero.
     *
     * @param hp The amount of health to be deducted from the player's health.
     */
    public void takeDamage(int hp) {
        this.health -= hp;
        if (this.health < 0) this.health = 0;
    }

    /**
     * Returns a list of all Key objects (in a Key format)
     *
     */
    public ArrayList<Key> getKeys() {
        ArrayList<Key> potionsLst = new ArrayList<Key>();

        for (AdventureObject object : this.inventory) {
            if (object instanceof Key) {
                potionsLst.add((Key) object);
            }
        }

        return potionsLst;
    }

    /**
     * Returns a list of all Key objects (in a String format)
     *
     */
    public ArrayList<String> getKeysStringFormat() {
        ArrayList<String> objects = new ArrayList<>();
        for(int i=0;i<this.inventory.size();i++){
            if (this.inventory.get(i) instanceof Key) {
                objects.add(this.inventory.get(i).getName());
            }
        }
        return objects;
    }

    /**
     * Returns a list of all Potion objects (in a Potion format)
     *
     */
    public ArrayList<Potion> getPotions() {
        ArrayList<Potion> potionsLst = new ArrayList<Potion>();

        for (AdventureObject object : this.inventory) {
            if (object instanceof Potion) {
                potionsLst.add((Potion) object);
            }
        }

        return potionsLst;
    }

    /**
     * Returns a list of all Potion objects (in a String format)
     *
     */
    public ArrayList<String> getPotionsStringFormat() {
        ArrayList<String> objects = new ArrayList<>();
        for(int i=0;i<this.inventory.size();i++){
            if (this.inventory.get(i) instanceof Potion) {
                objects.add(this.inventory.get(i).getName());
            }
        }
        return objects;
    }

    /**
     * Returns a list of all Weapon objects (in a Weapon format)
     *
     */
    public ArrayList<Weapon> getWeapons() {
        ArrayList<Weapon> weaponsLst = new ArrayList<Weapon>();
        for (AdventureObject object : this.inventory) {
            if (object.getClass() == Weapon.class) {
                weaponsLst.add((Weapon) object);
            }
        }

        if(!weaponsLst.isEmpty()) {
            return weaponsLst;
        }
        return null;
    }

    /**
     * Returns a list of all Weapon objects (in a String format)
     *
     */
    public ArrayList<String> getWeaponsStringFormat() {
        ArrayList<String> objects = new ArrayList<>();
        for(int i=0;i<this.inventory.size();i++){
            if (this.inventory.get(i).getClass() == Weapon.class) {
                objects.add(this.inventory.get(i).getName());
            }
        }
        return objects;
    }

    /**
     * Returns the strongest weapon the Player has in their inventory.
     *
     */
    public Weapon getStrongestWeapon() {
        Weapon strongest = null;

        if(this.getWeapons() != null) {

            strongest = this.getWeapons().get(0);

            for (Weapon w : this.getWeapons()) {
                if (w.getDamageRange()[1] >= strongest.getDamageRange()[1]) {
                    strongest = w;
                }
            }

            return strongest;
        }
        return null;
    }


    /**
     * Reverts the player to the last room they were in.
     * If the last room is a FORCED room, they are forced
     * to stay.
     */
    public String escapeRoom(){
        System.out.println(this.rooms);
        if(!this.rooms.isEmpty()){ // Check if the stack of entered rooms is empty
            int size = this.rooms.size();
            if(this.rooms.get(size-2).getMotionTable().optionExists("FORCED")){ // check if last room is forced
                return "Stayed"; // if last room is blocked
            }else {

                if(this.rooms.size() > 1){ //check if there is more than one room
                    if(this.currentRoom.roomTroll instanceof FinalBoss){
                        return "";
                    }
                    this.currentRoom = this.rooms.pop();
                    Room cur = this.rooms.pop();
                    this.currentRoom = cur;
                    this.rooms.push(cur);
                    return "Escaped"; // if player successfully escaped

                }else {
                    return ""; // IF no room before (should never happen cuz troll wont be in starting room)
                }
            }
        }
        return ""; // IF no room before (should never happen cuz troll wont be in starting room)

    }

    /**
     * Returns "Healed" if the player has been successfully healed using the selected potion, or "" otherwise.
     * Takes into account using the potion and discarding from inventory.
     */
    public String heal(Potion potionObject) {

        if (potionObject != null) {
            this.health += potionObject.getAmount();
            if (this.health > 100) {
                this.health = 100;
            }

            this.inventory.remove(potionObject);

            return "Healed";
        }

        return "";
    }


}
