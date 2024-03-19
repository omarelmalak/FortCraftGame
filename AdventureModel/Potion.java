package AdventureModel;
public class Potion extends AdventureObject {
    public int amount;

    /**
     * Returns the integer amount that the Potion heals the player upon its usage.
     */
    public int getAmount() {
        return this.amount;
    }
}
