package AdventureModel;

/**
 * The PlayerHealCommand class implements the Command interface
 * to define the heal action for a player in the game. It handles the logic for
 * healing the player using a certain potion, discarding the potion afterward.
 */
public class PlayerHealCommand implements Command {
    private Player player;

    private Potion potion;

    /**
     * Constructs a PlayerHealCommnand and initializes it with the singleton
     * instance of the Player, along with the Potion object to be consumed.
     */
    public PlayerHealCommand(Potion potion) {
        this.player = Player.getInstance();
        this.potion = potion;
    }

    /**
     * Executes the heal command for the player. The execution logic happens
     * in player and is called in here.
     */
    @Override
    public String execute() {
        return player.heal(this.potion);
    }
}
