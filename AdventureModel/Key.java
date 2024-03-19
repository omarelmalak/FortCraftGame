package AdventureModel;
public class Key extends AdventureObject {
    /**
     * Key Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the Key.
     *
     * @param name The name of the Key in the game.
     * @param description One line description of the Key.
     * @param location The location of the Key in the game.
     */
    public Key(String name, String description, Room location){
        this.objectName = name;
        this.description = description;
        this.location = location;
    }
}
