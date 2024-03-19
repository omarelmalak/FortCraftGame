package AdventureModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class AdventureLoader. Loads an adventure from files.
 */
public class AdventureLoader {

    private AdventureGame game; //the game to return
    private String adventureName; //the name of the adventure

    /**
     * Adventure Loader Constructor
     * __________________________
     * Initializes attributes
     * @param game the game that is loaded
     * @param directoryName the directory in which game files live
     */
    public AdventureLoader(AdventureGame game, String directoryName) {
        this.game = game;
        this.adventureName = directoryName;
    }

     /**
     * Load game from directory
     */
    public void loadGame() throws IOException {
        parseRooms();
        parseKeys();
        parseSynonyms();
        parseWeapons();
        parsePotions();
        parseTrolls();
        this.game.setHelpText(parseOtherFile("help"));
    }

     /**
     * Parse Rooms File
     */
    private void parseRooms() throws IOException {

        int roomNumber;

        String roomFileName = this.adventureName + "/roomsORIGINAL.txt";
        BufferedReader buff = new BufferedReader(new FileReader(roomFileName));

        while (buff.ready()) {

            String currRoom = buff.readLine(); // first line is the number of a room

            roomNumber = Integer.parseInt(currRoom); //current room number

            // now need to get room name
            String roomName = buff.readLine();

            // now we need to get the description
            String roomDescription = "";
            String line = buff.readLine();
            while (!line.equals("-----")) {
                roomDescription += line + "\n";
                line = buff.readLine();
            }
            roomDescription += "\n";

            // now we make the room object
            Room room = new Room(roomName, roomNumber, roomDescription, adventureName);

            // now we make the motion table
            line = buff.readLine(); // reads the line after "-----"
            while (line != null && !line.equals("")) {
                String[] part = line.split(" \s+"); // have to use regex \\s+ as we don't know how many spaces are between the direction and the room number
                String direction = part[0];
                String dest = part[1];
                if (dest.contains("/")) {
                    String[] blockedPath = dest.split("/");
                    String dest_part = blockedPath[0];
                    String object = blockedPath[1];
                    Passage entry = new Passage(direction, dest_part, object);
                    room.getMotionTable().addDirection(entry);
                } else {
                    Passage entry = new Passage(direction, dest);
                    room.getMotionTable().addDirection(entry);
                }
                line = buff.readLine();
            }
            this.game.getRooms().put(room.getRoomNumber(), room);
        }

    }

     /**
     * Parse Keys File
     */
    public void parseKeys() throws IOException {

        String objectFileName = this.adventureName + "/keys.txt";
        BufferedReader buff = new BufferedReader(new FileReader(objectFileName));

        while (buff.ready()) {
            String objectName = buff.readLine();
            String objectDescription = buff.readLine();
            String objectLocation = buff.readLine();
            String separator = buff.readLine();
            if (separator != null && !separator.isEmpty())
                System.out.println("Formatting Error!");
            int i = Integer.parseInt(objectLocation);
            Room location = this.game.getRooms().get(i);
            AdventureObject key = new Key(objectName, objectDescription, location);
            location.addGameObject(key);
        }

    }

     /**
     * Parse Synonyms File
     */
    public void parseSynonyms() throws IOException {
        String synonymsFileName = this.adventureName + "/synonyms.txt";
        BufferedReader buff = new BufferedReader(new FileReader(synonymsFileName));
        String line = buff.readLine();
        while(line != null){
            String[] commandAndSynonym = line.split("=");
            String command1 = commandAndSynonym[0];
            String command2 = commandAndSynonym[1];
            this.game.getSynonyms().put(command1,command2);
            line = buff.readLine();
        }

    }

    /**
     * Parse Files other than Rooms, Keys and Synonyms
     *
     * @param fileName the file to parse
     */
    public String parseOtherFile(String fileName) throws IOException {
        String text = "";
        fileName = this.adventureName + "/" + fileName + ".txt";
        BufferedReader buff = new BufferedReader(new FileReader(fileName));
        String line = buff.readLine();
        while (line != null) { // while not EOF
            text += line+"\n";
            line = buff.readLine();
        }
        return text;
    }

    /**
     * Parse Weapons File
     */
    public void parseWeapons() throws IOException {
        String weaponsFileName = this.adventureName + "/weapons.txt";
        BufferedReader buff = new BufferedReader(new FileReader(weaponsFileName));

        while (buff.ready()) {
            String weaponName = buff.readLine();
            String weaponDescription = buff.readLine();
            String weaponLocation = buff.readLine();
            String weaponDmgRange = buff.readLine();
            String separator = buff.readLine();
            if (separator != null && !separator.isEmpty())
                System.out.println("Formatting Error!");
            int i = Integer.parseInt(weaponLocation);
            Room location = this.game.getRooms().get(i);
            weaponDmgRange = weaponDmgRange.substring(1,weaponDmgRange.length() - 1);
            String[] splitedDmgRange = weaponDmgRange.split(", ");
            int[] intDmgRange = {Integer.parseInt(splitedDmgRange[0]), Integer.parseInt(splitedDmgRange[1])};
            AdventureObject weapon = new Weapon(weaponName, weaponDescription, location, intDmgRange);
            location.addGameObject(weapon);
        }
    }

    /**
     * Parse Potions File
     */
    public void parsePotions() throws IOException {
        String potionsFileName = this.adventureName + "/potions.txt";
        BufferedReader buff = new BufferedReader(new FileReader(potionsFileName));

        while (buff.ready()) {
            String potionName = buff.readLine();
            String potionLocation = buff.readLine();
            String separator = buff.readLine();
            if (separator != null && !separator.isEmpty())
                System.out.println("Formatting Error!");
            int i = Integer.parseInt(potionLocation);
            Room location = this.game.getRooms().get(i);

            Potion potion;
            if (potionName.toLowerCase().equals("med kit")) {
                potion = new MedKit(location);
                location.addGameObject(potion);
            } else if (potionName.toLowerCase().equals("bandages")) {
                potion = new Bandages(location);
                location.addGameObject(potion);
            } else if (potionName.toLowerCase().equals("chug jug")) {
                potion = new ChugJug(location);
                location.addGameObject(potion);
            } else {
                System.out.println("Invalid Potion!");
            }
        }
    }

    /**
     * Parse Trolls File
     */
    public void parseTrolls() throws IOException {
        String potionsFileName = this.adventureName + "/trolls.txt";
        BufferedReader buff = new BufferedReader(new FileReader(potionsFileName));

        while (buff.ready()) {
            String trollFinalBoss = buff.readLine();
            String trollLocation = buff.readLine();
            String separator = buff.readLine();
            if (separator != null && !separator.isEmpty())
                System.out.println("Formatting Error!");
            int i = Integer.parseInt(trollLocation);
            Room location = this.game.getRooms().get(i);

            if (trollFinalBoss.equalsIgnoreCase("true")) {
                location.roomTroll = new FinalBoss();
                location.roomTroll.currentRoom = location;
            } else if (trollFinalBoss.equalsIgnoreCase("false")) {
                location.roomTroll = new MediumTroll();
                location.roomTroll.currentRoom = location;
                this.game.getActiveTrolls().add(location.roomTroll);
            } else {
                System.out.println("Invalid boolean value given for final boss.");
            }
        }
    }
}
