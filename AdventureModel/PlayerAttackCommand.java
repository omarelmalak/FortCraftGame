package AdventureModel;

/**
 * The PlayerAttackCommand class implements the Command interface
 * to define the attack action for a player in the game. It handles the logic for
 * the player's attack  the troll in the room with them.
 */
public class PlayerAttackCommand implements Command<Integer> {
    private Player player;

    /**
     * Constructs a PlayerAttackCommand and initializes it with the singleton
     * instance of the Player.
     */
    public PlayerAttackCommand() {
        this.player = Player.getInstance();
    }

    /**
     * Executes the attack command for the player. The execution logic happens
     * in player and is called in here.
     */
    @Override
    public Integer execute() {
        return player.attack();
    }
}
