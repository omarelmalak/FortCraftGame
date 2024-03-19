
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

import AdventureModel.*;
import org.junit.jupiter.api.Test;
import views.AdventureGameView;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("WEST, UP, NORTH, IN, SOUTH, DOWN", commands);
    }

    @Test
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird, an assault rifle, a kit containing sprays, scissors, and wraps, a barrel of chug juice, a set of bandages to wrap a player's wounds", objects);
    }

    @Test
    void playerEscapeRoomWorks(){

        Room r1 = new Room("Room 1", 1, "This is room 1", "TinyGame"); //Create room 1
        Room r2 = new Room("Room 2", 2, "This is room 2", "TinyGame"); //Create room 2
        Player player = Player.getInstance(); //get player

        EasyTroll easy = new EasyTroll(); //create Troll
        r2.roomTroll = easy; //set room 2 to contain troll

        player.setCurrentRoom(r1); //start player in room 1
        player.setCurrentRoom(r2); //move to room 2(room with troll)

        player.escapeRoom(); // escape back to room 1
        assertEquals(player.getCurrentRoom().getRoomNumber(), 1); //check if player in room 1
    }

    @Test
    void playerEscapeRoomForced(){

        Room r1 = new Room("Room 1", 1, "This is room 1", "TinyGame"); //Create room 1
        Passage forced = new Passage("FORCED", "2"); //make room 1 forced
        r1.getMotionTable().passageTable.add(forced);


        Room r2 = new Room("Room 2", 2, "This is room 2", "TinyGame"); //Create room 2
        Player player = Player.getInstance(); //get player



        EasyTroll easy = new EasyTroll(); //create Troll
        r2.roomTroll = easy; //set room 2 to contain troll

        player.setCurrentRoom(r1); //start player in room 1
        player.setCurrentRoom(r2); //move to room 2(room with troll)

        player.escapeRoom(); // escape back to room 1
        assertEquals(player.getCurrentRoom(), r2); //check if player in room 2 because cant escape to a forced room
    }

    @Test
    void mementoTest(){

       AdventureGame game = new AdventureGame("test game");
       Memento memento = new Memento(game, "test save");
       assertEquals(memento.getState(),game);

    }

    @Test
    void loadTxtCmdTest1(){
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals(game.interpretAction("load"),"THE LOAD COMMAND REQUIRES THE NAME OF THE SAVE");

    }

    @Test
    void loadTxtCmdTest2(){
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals(game.interpretAction("load 1 2"),"THE LOAD COMMAND TAKES IN ONLY ONE ARGUMENT");

    }


    @Test
    void killTroll(){
        Room r1 = new Room("Room 1", 1, "This is room 1", "TinyGame"); //Create room 1

        Player player = Player.getInstance();
        player.setCurrentRoom(r1);
        Weapon gun = new Weapon("Scar", "Scar", r1, new int[]{50, 51});
        player.setInventory();
        player.addToInventory(gun);

        EasyTroll easy = new EasyTroll(); //create Troll
        r1.roomTroll = easy; //set room 2 to contain troll

        Command<Integer> playerAttack = new PlayerAttackCommand();
        Command<Integer> trollAttack = new TrollAttackCommand();
        Integer playerDamage = playerAttack.execute();
        Integer trollDamage = trollAttack.execute();

        easy.takeDamage(playerDamage);
        player.takeDamage(trollDamage);
        easy.takeDamage(playerDamage);
        player.takeDamage(trollDamage);
        easy.takeDamage(playerDamage);
        player.takeDamage(trollDamage);
        easy.takeDamage(playerDamage);
        player.takeDamage(trollDamage);

        assertEquals(r1.roomTroll.health, 0);


    }

    @Test
    void playerDies(){
        Room r1 = new Room("Room 1", 1, "This is room 1", "TinyGame"); //Create room 1


        Player player = Player.getInstance();
        player.setCurrentRoom(r1);
        player.setInventory();

        EasyTroll easy = new EasyTroll(); //create Troll
        r1.roomTroll = easy; //set room 2 to contain troll

        Command<Integer> playerAttack = new PlayerAttackCommand();
        Command<Integer> trollAttack = new TrollAttackCommand();
        Integer playerDamage = playerAttack.execute();
        Integer trollDamage = trollAttack.execute();

        easy.takeDamage(playerDamage);
        player.takeDamage(trollDamage);
        easy.takeDamage(playerDamage);
        player.takeDamage(trollDamage);
        easy.takeDamage(playerDamage);
        player.takeDamage(trollDamage);
        easy.takeDamage(playerDamage);
        player.takeDamage(trollDamage);

        assertEquals(player.health, 0);


    }

    @Test
    void potionsParseTest(){
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals(game.getRooms().get(1).getObject("MED KIT").getName(),"MED KIT");
        assertEquals(game.getRooms().get(1).getObject("CHUG JUG").getName(),"CHUG JUG");
    }

    @Test
    void weaponsParseTest(){
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals(game.getRooms().get(3).getObject("RPG").getName(),"RPG");
        assertEquals(game.getRooms().get(3).getObject("PISTOLS").getName(),"PISTOLS");
    }

    @Test
    void trollsParseTest(){
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals(game.getRooms().get(2).roomTroll.getName(),"Creeper");
        assertEquals(game.getRooms().get(3).roomTroll.getName(),"Ender Dragon");
    }

    @Test
    void objectsParseTest(){
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals(game.getPlayer().getCurrentRoom().getObjectString(),"a water bird, an assault rifle, a kit containing sprays, scissors, and wraps, a barrel of chug juice, a set of bandages to wrap a player's wounds");
    }

    @Test
    void easyStateTest(){
        AdventureGame game = new AdventureGame("TinyGame");

        // check number of trolls (excluding the final boss)
        assertEquals(game.getActiveTrolls().size(),2);

        // check if state was applied correctly
        assertEquals(game.getActiveTrolls().get(0).getName(), "Creeper");
        assertEquals(game.getActiveTrolls().get(1).getName(), "Creeper");

        DifficultyState state = new EasyState();
        state.applyState(game);

        for (Troll i: game.getActiveTrolls()) {
            assertEquals(i.getName(), "Zombie");
        }
        assertEquals(game.getActiveTrolls().size(),2);
    }

    @Test
    void hardStateTest() {
        AdventureGame game = new AdventureGame("TinyGame");

        // check number of trolls (excluding the final boss)
        assertEquals(game.getActiveTrolls().size(), 2);

        // check if state was applied correctly
        assertEquals(game.getActiveTrolls().get(0).getName(), "Creeper");
        assertEquals(game.getActiveTrolls().get(1).getName(), "Creeper");

        DifficultyState state = new HardState();
        state.applyState(game);

        for (Troll i : game.getActiveTrolls()) {
            assertEquals(i.getName(), "Herobrine");
        }
        assertEquals(game.getActiveTrolls().size(), 2);
    }

    @Test
    void mediumStateTest() {
        AdventureGame game = new AdventureGame("TinyGame");

        // check number of trolls (excluding the final boss)
        assertEquals(game.getActiveTrolls().size(), 2);

        // check if state was applied correctly
        assertEquals(game.getActiveTrolls().get(0).getName(), "Creeper");
        assertEquals(game.getActiveTrolls().get(1).getName(), "Creeper");

        DifficultyState state = new MediumState();
        state.applyState(game);

        for (Troll i : game.getActiveTrolls()) {
            assertEquals(i.getName(), "Creeper");
        }
        assertEquals(game.getActiveTrolls().size(), 2);
    }

    @Test
    void takeAndHealTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        Player.getInstance().setHealth(10);
        Player.getInstance().takeObject("CHUG JUG");
        Player.getInstance().heal(Player.getInstance().getPotions().get(0));
        assertEquals(100, Player.getInstance().getHp());

        Player.getInstance().setHealth(10);
        Player.getInstance().takeObject("BANDAGES");
        Player.getInstance().takeObject("MED KIT");
        assertTrue(Player.getInstance().getPotionsStringFormat().contains("BANDAGES"));
        assertTrue(Player.getInstance().getPotionsStringFormat().contains("MED KIT"));
        assertFalse(Player.getInstance().getPotionsStringFormat().contains("CHUG JUG"));
        assertTrue(Player.getInstance().getInventory().contains("BANDAGES"));
        assertTrue(Player.getInstance().getInventory().contains("MED KIT"));
        assertFalse(Player.getInstance().getInventory().contains("CHUG JUG"));


        Player.getInstance().heal(Player.getInstance().getPotions().get(1));
        assertEquals(60, Player.getInstance().getHp());
        assertFalse(Player.getInstance().getPotionsStringFormat().contains("MED KIT"));
        Player.getInstance().heal(Player.getInstance().getPotions().get(0));
        assertEquals(85, Player.getInstance().getHp());
        assertFalse(Player.getInstance().getPotionsStringFormat().contains("BANDAGES"));
    }

    @Test
    void parsePotionsTest() throws IOException {
        // ALSO TESTS CORRECTNESS OF POLYMORPHIC BEHAVIOR
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals(game.getRooms().get(1).getObject("BANDAGES").getName(),"BANDAGES");
        assertEquals(game.getRooms().get(1).getObject("MED KIT").getName(),"MED KIT");
        assertEquals(game.getRooms().get(1).getObject("CHUG JUG").getName(),"CHUG JUG");

        assertTrue(game.getRooms().get(1).getObject("BANDAGES").getClass() == Bandages.class);
        assertFalse(game.getRooms().get(1).getObject("MED KIT").getClass() == Bandages.class);
        assertTrue(game.getRooms().get(1).getObject("MED KIT").getClass() == MedKit.class);
        assertTrue(game.getRooms().get(1).getObject("CHUG JUG").getClass() == ChugJug.class);

        assertTrue(game.getRooms().get(1).getObject("BANDAGES") instanceof Potion);
        assertTrue(game.getRooms().get(1).getObject("MED KIT") instanceof Potion);
        assertTrue(game.getRooms().get(1).getObject("CHUG JUG") instanceof Potion);

        assertTrue(game.getRooms().get(1).getObject("BANDAGES") instanceof AdventureObject);
        assertTrue(game.getRooms().get(1).getObject("MED KIT") instanceof AdventureObject);
        assertTrue(game.getRooms().get(1).getObject("CHUG JUG") instanceof AdventureObject);
    }

    @Test
    void keysAndInventoryTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        Player.getInstance().takeObject("BIRD");
        assertFalse(Player.getInstance().getKeysStringFormat().contains("BANDAGES"));
        assertTrue(Player.getInstance().getKeysStringFormat().contains("BIRD"));
        assertTrue(Player.getInstance().getInventory().contains("BIRD"));
        Player.getInstance().dropObject("BIRD");
        assertFalse(Player.getInstance().getWeaponsStringFormat().contains("BIRD"));
        assertFalse(Player.getInstance().getInventory().contains("BIRD"));
    }


    @Test
    void battleAttack(){

        Room r1 = new Room("Room 1", 1, "This is room 1", "TinyGame"); //Create room 1

        Player player = Player.getInstance();
        player.setCurrentRoom(r1);
        Weapon gun = new Weapon("Scar", "Scar", r1, new int[]{50, 51});
        player.setInventory();
        player.addToInventory(gun);

        EasyTroll easy = new EasyTroll(); //create Troll
        r1.roomTroll = easy; //set room 2 to contain troll

        Command<Integer> playerAttack = new PlayerAttackCommand();
        Command<Integer> trollAttack = new TrollAttackCommand();
        Battle battle = new Battle();

        battle.setPlayerCommand(playerAttack);
        battle.setTrollCommand(trollAttack);
        battle.executeAttack();
        battle.executeAttack();
        battle.executeAttack();
        battle.executeAttack();


        assertEquals(r1.roomTroll.health, 0);
        assertEquals(Player.getInstance().health, 0);
    }

    @Test
    void battleEscape(){

        Room r1 = new Room("Room 1", 1, "This is room 1", "TinyGame"); //Create room 1
        Passage forced = new Passage("FORCED", "2"); //make room 1 forced
        r1.getMotionTable().passageTable.add(forced);


        Room r2 = new Room("Room 2", 2, "This is room 2", "TinyGame"); //Create room 2
        Player player = Player.getInstance(); //get player



        EasyTroll easy = new EasyTroll(); //create Troll
        r2.roomTroll = easy; //set room 2 to contain troll

        player.setCurrentRoom(r1); //start player in room 1
        player.setCurrentRoom(r2); //move to room 2(room with troll)

        Battle battle = new Battle();
        Potion p = new Bandages(r1);

        Command playerHeal = new PlayerHealCommand(p);
        player.setInventory();
        player.addToInventory(p);
        player.health = 75;
        battle.setPlayerCommand(playerHeal);
        battle.executeEscape();


        assertEquals(player.health, 100); //check if player in room 2 because cant escape to a
    }


    @Test
    void battleHeal(){

        Room r1 = new Room("Room 1", 1, "This is room 1", "TinyGame"); //Create room 1
        Passage forced = new Passage("FORCED", "2"); //make room 1 forced
        r1.getMotionTable().passageTable.add(forced);


        Room r2 = new Room("Room 2", 2, "This is room 2", "TinyGame"); //Create room 2
        Player player = Player.getInstance(); //get player



        EasyTroll easy = new EasyTroll(); //create Troll
        r2.roomTroll = easy; //set room 2 to contain troll

        player.setCurrentRoom(r1); //start player in room 1
        player.setCurrentRoom(r2); //move to room 2(room with troll)

        Battle battle = new Battle();

        Command playerEscape = new PlayerEscapeCommand();

        battle.setPlayerCommand(playerEscape);
        battle.executeEscape();

        assertEquals(player.getCurrentRoom(), r2); //check if player in room 2 because cant escape to a
    }

}
