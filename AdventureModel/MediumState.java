package AdventureModel;

public class MediumState implements DifficultyState{

    /**
     * applyState method.
     * __________________________
     * This applies a state change to a given AdventureGame (i.e. changing troll difficulty).
     *
     * @param game the game to be modified.
     */
    public void applyState(AdventureGame game){
        for (int i = 0; i < game.getActiveTrolls().size(); i++) {
            Troll t = game.getActiveTrolls().get(i);
            if (t.currentRoom.checkTroll()) {
                Troll newTroll = new MediumTroll();
                newTroll.currentRoom = game.getActiveTrolls().get(i).currentRoom;
                game.getActiveTrolls().get(i).currentRoom.roomTroll = newTroll;
                game.getActiveTrolls().set(i, newTroll);
            }
        }
    }
}
