package AdventureModel;

import java.io.Serializable;

/**
 * Represents a HardTroll in the adventure game.
 * This class extends the Troll class, defining a more challenging type of troll.
 * HardTrolls have predefined higher damage and health ranges compared to other trolls,
 * making them a tougher opponent in the game.
 */
public class HardTroll extends Troll implements Serializable {

    /**
     * Constructor for HardTroll.
     * Initializes the troll with a specific name, damage range, and health range.
     * The damage range is set between 50 and 70, and the health range is set between 120 and 140.
     * The actual health of the troll is randomly determined within the health range.
     */
    public HardTroll() {
        this.name = "Herobrine";
        this.damageRange = new int[]{40, 50};
        this.healthRange = new int[]{120, 140};
        this.health = (int)(Math.random() * (healthRange[1] - healthRange[0] + 1) + healthRange[0]);
    }

}
