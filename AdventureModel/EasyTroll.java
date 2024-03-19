package AdventureModel;

import java.io.Serializable;

/**
 * Represents an EasyTroll in the adventure game.
 * This class extends the Troll class, defining a less challenging type of troll.
 * EasyTrolls have predefined lower damage health ranges compared to other trolls,
 * making them an easier opponent in the game.
 */
public class EasyTroll extends Troll implements Serializable {

    /**
     * Constructor for EasyTroll.
     * Initializes the troll with a specific name, damage range, and health range.
     * The damage range is set between 20 and 40, and the health range is set between 80 and 100.
     * The actual health of the troll is randomly determined within the health range.
     */
    public EasyTroll() {
        this.name = "Zombie";
        this.damageRange = new int[]{10, 20};
        this.healthRange = new int[]{80, 100};
        this.health = (int)(Math.random() * (healthRange[1] - healthRange[0] + 1) + healthRange[0]);
    }
}
