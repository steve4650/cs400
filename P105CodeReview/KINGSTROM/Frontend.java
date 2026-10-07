import java.util.Scanner;
import java.io.IOException;
import java.util.List;

public class Frontend implements FrontendInterface {
    private Scanner in;
    private BackendInterface backend;
    private String filterTime;

    public Frontend(Scanner in, BackendInterface backend) {
        this.in = in;
        this.backend = backend;
        this.filterTime = null;
    }

    public void runCommandLoop() {
        String line;
        showCommandInstructions();
        while(true) {
            if(!this.in.hasNextLine()) break;
            line = this.in.nextLine();
            if(line.trim().equals("quit")) {
                System.out.println("Quitting...");    
                break;
            }
            else { processSingleCommand(line.trim()); }
        }
        System.out.println("See you next time o/");  
    }

    public void showCommandInstructions() {
        String instructions = 
        """
        +------------------+
        |     Help Menu    |
        +------------------+
        |- submit: add a record to the leaderboard.
        |-- submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME
        |--- CONTINENT can be AFRICA, ASIA, ANTARCTICA, AUSTRALIA, EUROPE, NORTH_AMERICA, or SOUTH_AMERICA
        |--- COMPLETION_TIME should be formatted as HHH:MM:SS
        |- submit multiple: add multiple records to the leaderboard.
        |-- submit multiple FILEPATH
        |- level: updates the filter criteria for the show command based on level.
        |-- level MAX
        |-- level MIN to MAX
        |- time: updates the filter criteria for the show command based on completion time.
        |-- time TIME
        |--- TIME is the maximum time included in the filter criteria.
        |--- TIME is formatted as HHH:MM:SS
        |- show: show a specified number of records according to specified filters.
        |-- show MAX_COUNT
        |--- show MAX_COUNT records or all available 
        |-- show most collectables.
        |--- display top ten records with most collectables
        |- help: displays this menu.
        |- quit: closes this program.
        """;
        System.out.print(instructions);
    }

    public void processSingleCommand(String command) {
        String[] commandArr = command.split(" ");
        if(command.startsWith("submit")) { processSubmitCommand(commandArr); }
        else if(command.startsWith("level")) { processLevelCommand(commandArr); } 
        else if(command.startsWith("time"))  { processTimeCommand(commandArr); }
        else if(command.startsWith("show"))  { processShowCommand(commandArr); }
        else if(command.startsWith("help"))  { System.out.println("Reprinting help menu..."); showCommandInstructions(); }
        else invalidCommand(commandArr);
        

    }

    void processSubmitCommand(String[] commandArr) {
        if(commandArr[1].equals("multiple")) { // If importing from file
            /**
             Validate the number of parameters and import from file

             Length should be 3 where:
             - commandArr[0] = "submit"
             - commandArr[1] = "multiple"
             - commandArr[2] = <filename>
            **/
            if(commandArr.length == 3) { 
                try {
                    this.backend.readData(commandArr[2]);
                    System.out.println("Adding records from file "+commandArr[2]+"...");
                }
                catch(IOException ioex) {
                    System.out.println("Could not read from "+commandArr[2]+". Confirm that the path is correct and that the file is correctly formatted and accessible.");
                }
            }
            else {
                System.out.println("Malformed Command: to add records from file, use 'submit multiple FILENAME'");
            }
        }
        else { // If not reading from file
            /**
             Validate the number of parameters and create a new GameRecord for the leaderboard

             Length should be 7 where:
             - commandArr[0] = "submit"
             - commandArr[1] = NAME
             - commandArr[2] = CONTINENT
             - commandArr[3] = SCORE
             - commandArr[4] = COLLECTABLES
             - commandArr[5] = LEVEL
             - commandArr[6] = COMPLETION_TIME
           **/
            if(commandArr.length == 7) { 
                if(isValidRecord(commandArr)) {
                    this.backend.addRecord(new GameRecord(commandArr[1],getLocation(commandArr[2]),Integer.valueOf(commandArr[3]),Integer.valueOf(commandArr[4]),Integer.valueOf(commandArr[5]),commandArr[6])); 
                    System.out.println("Record added successfully");
                }
                else { System.out.println("Could not add record. Ensure that you've chosen a valid continent, the score is calculated correctly, and the time parameter is properly formatted."); }
            } 
            else {
                System.out.println("Malformed Command: to add record to leaderboard, use 'submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME'");
            }
        }
    }

    GameRecord.Continent getLocation(String passed) throws IllegalArgumentException {
        GameRecord.Continent continent;
        switch(passed.toUpperCase()) {
            case "AFRICA": continent = GameRecord.Continent.AFRICA;
            case "ASIA": continent = GameRecord.Continent.ASIA;
            case "ANTARCTICA": continent = GameRecord.Continent.ANTARCTICA;
            case "AUSTRALIA": continent = GameRecord.Continent.AUSTRALIA;
            case "EUROPE": continent = GameRecord.Continent.EUROPE;
            case "NORTH_AMERICA": continent = GameRecord.Continent.NORTH_AMERICA;
            case "SOUTH_AMERICA": continent = GameRecord.Continent.SOUTH_AMERICA;
            default: continent = null;
            return continent;
        }
    }

    /**
     * Validate as many portions of the passed score as possible.
     * Returns true unless:
     * - An invalid continent is passed.
     * - The score does not match the intended format.
     * - The time is improperly formatted.
     */
    boolean isValidRecord(String[] commandArr) {
        String continent = commandArr[2].toUpperCase();
        // Validate Continent
        // Return false if passed continent is not a listed option.
        if(getLocation(continent)!=null) 
           { return false; }

        // Validate Score
        // Should be calculated by 46*collectables + 50*level + 17933
        Integer score,collectables,level;
        score = Integer.valueOf(commandArr[3]);
        collectables = Integer.valueOf(commandArr[4]);
        level = Integer.valueOf(commandArr[5]);
        
        if(score != 46*collectables + 50*level + 17933) { 
            System.out.println("Invalid Score: Score should be equal to 46*collectables + 50*level + 17933");
            return false; 
        }
        
        // Validate time
        // Should be a string with any 3 digit number of hours, then a value between 00 and 59 for minutes and seconds.
        // Strictly enforcing 3 digits for hours, so 001 works but 1 doesn't.
        // I know regex is later on in the course, but this one is fairly simple, so it was easier to implement than manual string processing.
        if(!isValidTime(commandArr[6])) { return false; }

        return true;
    }


    // Validate time
    // Should be a string with any 3 digit number of hours, then a value between 00 and 59 for minutes and seconds.
    // Strictly enforcing 3 digits for hours, so 001 works but 1 doesn't.
    // I know regex is later on in the course, but this one is fairly simple, so it was easier to implement than manual string processing.
    boolean isValidTime(String time) { return time.matches("\\d{3}:[0-5]{1}\\d{1}:[0-5]{1}\\d{1}"); }

    void processLevelCommand(String[] commandArr) {
        // Check for level MAX command.
        // Ensure that MAX parameter is numeric. '\d*' excludes '.', so it should exclude non-integers.
        Integer min,max;
        max = Integer.valueOf(commandArr[1]);
        if(commandArr.length == 2 && commandArr[1].matches("\\d*")) { 
            System.out.println("Filtering for records with a level up to "+commandArr[1]);
            this.backend.getAndSetRange(null,max);
        }
        // Check for level MIN to MAX command.
        // Ensure that MIN and MAX are numeric and have 'to' between them. 
        else if(commandArr.length == 4 && commandArr[1].matches("\\d*") && commandArr[2].equals("to") && commandArr[3].matches("\\d*")) {
            min = Integer.valueOf(commandArr[1]);
            max = Integer.valueOf(commandArr[3]);
            System.out.println("Filtering for records with a level between "+commandArr[1]+" and "+commandArr[3]);
            this.backend.getAndSetRange(min,max); 
        }
        else {
            System.out.println("Malformed Command: to filter leaderboard by level, use 'level MAX' or 'level MIN to MAX' where MIN/MAX are integers indicating the level.");
        }
    }

    /**
     * Run time command
     * Validate that command and passed time is properly formatted
     */
    void processTimeCommand(String[] commandArr) {
        if(commandArr.length != 2 || !isValidTime(commandArr[1])) { System.out.println("Malformed Command: to filter based on time, use 'time TIME' where TIME is formatted as HHH:MM:SS"); }
        else { 
            this.filterTime = commandArr[1];
            this.backend.applyAndSetFilter(commandArr[1]); 
            System.out.println("Filtering for records with a time up to "+commandArr[1]+".");
        }
    }

    void processShowCommand(String[] commandArr) {
        if(commandArr.length == 3 && commandArr[1].equals("most") && commandArr[2].equals("collectables")) {
            System.out.println("Returning top ten records with most collectables.");
            System.out.println(this.backend.getTopTen());
        }
        else if(commandArr.length == 2 && commandArr[1].matches("\\d*")) {
            List<String> outputList = this.backend.getTopTen(); // No backend implementation
            Integer max = Integer.valueOf(commandArr[1]);
            if(max > outputList.size()) { max = outputList.size(); } // If MAX parameter > List Size, return whole list
            for(int i=0; i < max; i++) {
                System.out.println(outputList.get(i));
            }
            System.out.println("Returning up to "+commandArr[1]+" records.");
        }
        else { System.out.println("Malformed command: Should be formatted as show MAX_COUNT or show most collectables."); }
    }

    void invalidCommand(String[] commandArr) { System.out.println("Unknown Command: should be submit, level, time, show, help, or quit. For further instructions, enter help."); }
}