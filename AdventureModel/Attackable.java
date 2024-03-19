package AdventureModel;

/**
 * Interface representing attackable entities in the adventure game.
 * This interface requires implementing entities to have attack capabilities,
 * a way to return health points, and a method to get the entity's name.
 * Additionally, it includes the ability to take damage.
 */
public interface Attackable {

    /**
     * Performs an attack and returns the amount of damage dealt.
     *
     * @return An integer representing the damage dealt.
     */
    public int attack();

    /**
     * Retrieves the current health points of the entity.
     *
     * @return The current health points as an integer.
     */
    public int getHp();

    /**
     * Retrieves the name of the entity.
     *
     * @return The name of the entity as a String.
     */
    public String getName();

    /**
     * Reduces the health points of the entity based on the specified damage.
     * This method is used to apply damage to the entity.
     *
     * @param damage The amount of damage to be applied.
     */
    public void takeDamage(int damage);
}
