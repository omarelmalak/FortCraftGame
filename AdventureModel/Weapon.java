package AdventureModel;

import java.util.Random;

public class Weapon extends AdventureObject {
    private int[] damageRange;

    /**
     * Weapon Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the Weapon.
     *
     * @param name The name of the Weapon in the game.
     * @param description One line description of the Weapon.
     * @param location The location of the Weapon in the game.
     * @param damageRange The possible range of possible damage the Weapon can deal to a Troll (must be a strictly
     *                    2-element list).
     */
    public Weapon(String name, String description, Room location, int[] damageRange){
        this.objectName = name;
        this.description = description;
        this.location = location;

        this.damageRange = damageRange;
    }

    /**
     * Returns the damage range of the weapon in an integer list format.
     */
    public int[] getDamageRange() {
        return damageRange;
    }

    /**
     * Calculates a damage amount for an attack by returning a random integer within the damage range.
     */
    public int calculateDamage() {
        Random r = new Random();
        int randomInt = r.nextInt(this.damageRange[1] - this.damageRange[0]) + this.damageRange[0];
        return randomInt;
    }
}
