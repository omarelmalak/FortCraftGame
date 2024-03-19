package AdventureModel;

import java.io.Serializable;

/**
 * Abstract class representing a Troll in the adventure game.
 * This class implements the Attackable interface, providing
 * functionality for a Troll to attack and take damage.
 * Trolls have varying health and damage ranges and can be
 * situated in different rooms.
 */
public abstract class Troll implements Attackable, Serializable {

    /**
     * The name of the troll.
     */
    public String name;

    /**
     * Array representing the damage range of the troll.
     * damageRange[0] is the minimum damage,
     * damageRange[1] is the maximum damage.
     */
    public int[] damageRange;

    /**
     * Array representing the health range of the troll.
     * healthRange[0] is the minimum health,
     * healthRange[1] is the maximum health.
     */
    public int[] healthRange;

    /**
     * Current health of the troll.
     */
    public int health;

    /**
     * The current room of the troll within the adventure game.
     */
    public Room currentRoom;

    /**
     * Performs an attack by the troll, returning a random damage
     * value within the troll's damage range.
     *
     * @return An integer representing the damage dealt.
     */
    @Override
    public int attack() {
        return (int)(Math.random() * (damageRange[1] - damageRange[0] + 1) + damageRange[0]);
    }

    /**
     * Retrieves the current health points of the troll.
     *
     * @return Current health of the troll as an integer.
     */
    @Override
    public int getHp() {
        return health;
    }

    /**
     * Retrieves the name of the troll.
     *
     * @return The name of the troll as a String.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the current room of the troll to the specified room.
     *
     * @param room The room to set as the current room for the troll.
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * Reduces the health of the troll by a specified amount.
     * If the health falls below zero, it is set to zero.
     *
     * @param hp The amount of health to be deducted from the troll's health.
     */
    public void takeDamage(int hp) {
        this.health -= hp;
        if (this.health < 0) this.health = 0;
    }

}
