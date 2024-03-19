import java.io.IOException;

import AdventureModel.AdventureGame;
import AdventureModel.Player;
import AdventureModel.Weapon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeaponsTest {
    @Test
    void getStrongestWeaponTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        Player.getInstance().takeObject("SCAR");
        game.movePlayer("IN");
        Player.getInstance().takeObject("RPG");
        Player.getInstance().takeObject("PISTOLS");
        Weapon strongest = Player.getInstance().getStrongestWeapon();
        assertTrue(strongest.getName().equals("RPG"));
    }

    @Test
    void weaponsAndInventoryTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        Player.getInstance().takeObject("SCAR");
        assertTrue(Player.getInstance().getWeaponsStringFormat().contains("SCAR"));
        game.movePlayer("IN");
        Player.getInstance().takeObject("RPG");
        assertTrue(Player.getInstance().getWeaponsStringFormat().contains("SCAR"));
        assertTrue(Player.getInstance().getWeaponsStringFormat().contains("RPG"));
        Player.getInstance().dropObject("RPG");
        assertTrue(Player.getInstance().getWeaponsStringFormat().contains("SCAR"));
        assertTrue(Player.getInstance().getInventory().contains("SCAR"));
        assertFalse(Player.getInstance().getWeaponsStringFormat().contains("RPG"));
        assertFalse(Player.getInstance().getInventory().contains("RPG"));
        assertNotNull(Player.getInstance().getCurrentRoom().getObject("RPG"));
        assertNull(Player.getInstance().getCurrentRoom().getObject("SCAR"));
    }

    @Test
    void calculateDamageTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        Player.getInstance().takeObject("SCAR");
        game.movePlayer("IN");
        Player.getInstance().takeObject("RPG");
        Player.getInstance().takeObject("PISTOLS");

        for (int i = 0; i < 1000; i++) {
            assertTrue(Player.getInstance().getWeapons().get(0).calculateDamage() >= Player.getInstance().getWeapons().get(0).getDamageRange()[0]);
            assertTrue(Player.getInstance().getWeapons().get(0).calculateDamage() <= Player.getInstance().getWeapons().get(0).getDamageRange()[1]);

            assertTrue(Player.getInstance().getWeapons().get(1).calculateDamage() >= Player.getInstance().getWeapons().get(1).getDamageRange()[0]);
            assertTrue(Player.getInstance().getWeapons().get(1).calculateDamage() <= Player.getInstance().getWeapons().get(1).getDamageRange()[1]);

            assertTrue(Player.getInstance().getWeapons().get(2).calculateDamage() >= Player.getInstance().getWeapons().get(2).getDamageRange()[0]);
            assertTrue(Player.getInstance().getWeapons().get(2).calculateDamage() <= Player.getInstance().getWeapons().get(2).getDamageRange()[1]);
        }
    }



}