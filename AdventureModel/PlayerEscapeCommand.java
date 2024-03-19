package AdventureModel;

/**
 * The PlayerEscapeCommand class implements the Command interface
 * to define the escape action for a player in the game. It handles the logic for
 * the player's attempt to escape from the current situation.
 */
public class PlayerEscapeCommand implements Command {
    private Player player;

    /**
     * Constructs a PlayerEscapeCommand and initializes it with the singleton
     * instance of the Player.
     */
    public PlayerEscapeCommand() {
        this.player = Player.getInstance();
    }

    /**
     * Executes the escape command for the player. The execution logic happens
     * in player and is called in here.
    */
    @Override
    public String execute() {
        return player.escapeRoom();
    }
}
