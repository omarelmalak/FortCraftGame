package AdventureModel;

public class MedKit extends Potion {
    /**
     * MedKit Constructor
     * ___________________________
     * This constructor sets the name, description, location, and amount of the MedKit.
     */
    public MedKit(Room location){
        this.objectName = "MED KIT";
        this.description = "a kit containing sprays, scissors, and wraps";
        this.location = location;
        this.amount = 50;
    }
}
