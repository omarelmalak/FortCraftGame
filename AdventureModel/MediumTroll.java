package AdventureModel;

import java.io.Serializable;

/**
 * Represents a MediumTroll in the adventure game.
 * This class extends the Troll class and represents a moderately challenging type of troll.
 * MediumTrolls have balanced damage and health ranges, offering a standard level of difficulty.
 * They are designed to provide a fair challenge without being overly difficult.
 */
public class MediumTroll extends Troll implements Serializable {

    /**
     * Constructor for MediumTroll.
     * Initializes the troll with a specific name, damage range, and health range.
     * The damage range is set between 35 and 55, and the health range is set between 100 and 120.
     * The actual health of the troll is randomly determined within the health range.
     */
    public MediumTroll() {
        this.name = "Creeper";
        this.damageRange = new int[]{20, 30};
        this.healthRange = new int[]{100, 120};
        this.health = (int)(Math.random() * (healthRange[1] - healthRange[0] + 1) + healthRange[0]);
    }

}
