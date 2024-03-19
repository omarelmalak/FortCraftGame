package AdventureModel;

/**
 * The TrollAttackCommand class implements the Command interface
 * to define the attack action for a troll in the game. It handles the logic for
 * the troll's attack to the player in the game.
 */
public class TrollAttackCommand implements Command<Integer> {
    private Troll troll;

    /**
     * Constructs a TrollAttackCommand and initializes it with the troll in the room.
     */
    public TrollAttackCommand() {
        this.troll = Player.getInstance().getCurrentRoom().roomTroll;
    }

    /**
     * Executes the attack command for the troll. The execution logic happens
     * in the troll class and is called in here.
     */
    @Override
    public Integer execute() {
        return troll.attack();
    }
}
