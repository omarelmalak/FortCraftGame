package AdventureModel;

public interface DifficultyState {

    /**
     * applyState method.
     * __________________________
     * This applies a state change to a given AdventureGame (i.e. changing troll difficulty).
     * This is an abstract method.
     *
     * @param game the game to be modified.
     */
    void applyState(AdventureGame game);
}
