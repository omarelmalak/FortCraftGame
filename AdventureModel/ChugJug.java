package AdventureModel;

public class ChugJug extends Potion {
    /**
     * Chug Jug Constructor
     * ___________________________
     * This constructor sets the name, description, location, and amount of the Chug Jug.
     */
    public ChugJug(Room location){
        this.objectName = "CHUG JUG";
        this.description = "a barrel of chug juice";
        this.location = location;
        this.amount = 100;
    }
}
