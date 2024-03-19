package AdventureModel;

public class Battle {


    private Command playerCommand;
    private Command trollCommand;

    public Battle(){}

    public void setPlayerCommand(Command command){
        this.playerCommand = command;
    }
    public void setTrollCommand(Command command){
        this.trollCommand = command;
    }

    public void executeAttack(){
        Integer playerDamage = (Integer) playerCommand.execute();
        Integer trollDamage = (Integer) trollCommand.execute();

        Player.getInstance().getCurrentRoom().roomTroll.takeDamage(playerDamage);
        Player.getInstance().takeDamage(trollDamage);
    }
    public String executeEscape(){
        return (String) this.playerCommand.execute();
    }



    public String executeHeal(){
        return (String) this.playerCommand.execute();

    }



}
