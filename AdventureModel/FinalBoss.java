package AdventureModel;

/**
 * Represents the FinalBoss in the adventure game.
 * This class extends the Troll class, representing the most challenging troll in the game.
 * The FinalBoss, named "Ender Dragon", is characterized by its high damage and health ranges,
 * signifying its role as the ultimate adversary for players to overcome.
 */
public class FinalBoss extends Troll {

    /**
     * Constructor for FinalBoss.
     * Initializes the final boss with a distinct name, high damage range, and health range.
     * The damage range is set between 80 and 100, and the health range is set between 180 and 200.
     * The actual health of the final boss is randomly determined within the health range,
     * making each encounter potentially unique.
     */
    public FinalBoss() {
        this.name = "Barney";
        this.damageRange = new int[]{50, 70};
        this.healthRange = new int[]{140, 150};
        this.health = (int)(Math.random() * (healthRange[1] - healthRange[0] + 1) + healthRange[0]);
    }
}
