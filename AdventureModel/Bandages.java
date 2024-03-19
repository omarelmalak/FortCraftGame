package AdventureModel;

public class Bandages extends Potion {
    /**
     * Bandages Constructor
     * ___________________________
     * This constructor sets the name, description, location, and amount of the Bandages.
     */
    public Bandages(Room location){
        this.objectName = "BANDAGES";
        this.description = "a set of bandages to wrap a player's wounds";
        this.location = location;
        this.amount = 25;
    }
}
