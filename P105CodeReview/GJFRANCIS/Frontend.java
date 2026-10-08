import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Frontend implementation if a BST used to display game records which includes data such as level
 * and time. Users are able to continuously execute multiple commands such as time, submit, level,
 * and show to interact with the BST
 */
public class Frontend implements FrontendInterface {
  Scanner in;
  BackendInterface backend;

  // public Frontend(Scanner in, BackendInterface backend)
  // Your constructor must have the signature above. This class must rely
  // only on the provided Scanner to read input from the user, and must
  // use the provided BackendInterface reference to compute the results
  // of a command requested by the user.

  public Frontend(Scanner in, BackendInterface backend) {
    this.in = in;
    this.backend = backend;
  }

  /*
   * Displays instructions for the syntax of user commands. And then repeatedly
   * gives the user an opportunity to issue new commands until they enter "quit".
   * Uses the processSingleCommand method below to parse and run each command
   * entered by the user. If the backend ever throws any exceptions, they should
   * be caught here and reported to the user. The user should then continue to be
   * able to issue subsequent commands until they enter "quit". This method must
   * use the scanner passed into the constructor to read commands input by the
   * user.
   */
  @Override
  public void runCommandLoop() {
    String input = "";
    showCommandInstructions();
    while (!input.equals("quit")) {
      input = in.nextLine();
      String inputArray[] = input.split(" ");
      if (inputArray[0].equals("quit")) {
        break;
      }
      try {
        processSingleCommand(input);
      } catch (Exception e) {
        System.out.println("Bad input" + e.getMessage());
      }
    }
  }

  /*
   * Displays instructions for the user to understand the syntax of commands that
   * they are able to enter. This should be displayed once from the command loop,
   * before the first user command is read in, and then later in response to the
   * user entering the command: help.
   *
   * The lowercase words in the following examples are keywords that the user must
   * match exactly in their commands, while the upper case words are placeholders
   * for arguments that the user can specify. The following are examples of valid
   * command syntax that your frontend should be able to handle correctly.
   *
   * submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME
   * submit multiple FILEPATH
   * level MAX
   * level MIN to MAX
   * time TIME
   * show MAX_COUNT
   * show most collectables
   * help
   * quit
   */
  @Override
  public void showCommandInstructions() {
    // Prints out possible commands to user
    System.out.println("These are the commands you have access to, you must match command case");
    System.out.println("You can: ");
    System.out.println("1.) submit");
    System.out.println("This is used to add files to the backend");
    System.out.println("You can add one records in the format: ");
    System.out.println("submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME");
    System.out.println("OR");
    System.out.println("submit multiple FILEPATH");
    System.out.println("Note, COMPLETION TIME must be in the format hh:mm:ss");
    System.out.println("2.) level");
    System.out.println("Updates range of records to return using MIN and MAX commands");
    System.out.println("Example commands: ");
    System.out.println("level 60");
    System.out.println("level 60 to 70");
    System.out.println("3.) time");
    System.out.println("Updates backend filter criteria by time");
    System.out.println("Example command: ");
    System.out.println(" time 10:20:58");
    System.out.println("4.) show");
    System.out.println("Displays specified amount of records based on set filters/thresholds");
    System.out.println("Example commands: ");
    System.out.println("show 5");
    System.out.println("show most collectables");
    System.out.println("5.) help");
    System.out.println("Prints list of commands");
    System.out.println("Example command: ");
    System.out.println("help");
    System.out.println("6.) quit");
    System.out.println("Exits the program");
    System.out.println("Example command: ");
    System.out.println("quit");
  }

  /*
   * This method takes a command entered by the user as input. It parses that
   * command to determine what kind of command it is, and then makes use of the
   * backend (which was passed to the constructor) to update the state of that
   * backend. When a show or help command is issued, this method prints the
   * appropriate results to standard out. When a command does not follow the
   * syntax rules described above, this method should print out an error message
   * that describes at least one defect in the syntax of the provided command
   * argument.
   *
   * Some notes on the expected behavior of the different commands:
   * submit: results in backend adding a new record with the specific NAME, CONTINENT, SCORE, COLLECTABLES, LEVEL, COMPLETION_TIME
   * COMPLETION_TIME is of the format "hhh:mm:ss"
   *
   * submit multiple: results in backend loading data from specified path
   *
   * level: updates backend's range of records to return should not result in any records being displayed
   *
   * time: updates backend's filter criteria should not result in any records being displayed
   *
   * show: displays list of records with currently set thresholds and filters
   *
   * MAX_COUNT: argument limits the number of record names displayed to the first MAX_COUNT in the list returned from backend
   * most collectables: argument displays results returned from the backend's getTopTen method
   *
   * help: displays command instructions
   *
   * quit: ends this program (handled by runCommandLoop method above) (do NOT use System.exit(), as this will interfere with tests)
   */
  @Override
  public void processSingleCommand(String command) {

    // Checks if command is null and returns
    if (command == null) {
      System.out.println("Bad input");
      return;
    }

    // Used to check what command was entered
    String[] split = command.split(" ");
    String input = split[0];

    // Early quit if quit is entered
    if (input.equals("quit")) {
      return;
    }

    // Checks if user tried to submit a single record and attempts to submit
    if (input.equals("submit") && !split[1].equals("multiple")) {
      GameRecord newRecord =
          new GameRecord(
              split[1],
              GameRecord.Continent.valueOf(split[2].toUpperCase()),
              Integer.parseInt(split[3]),
              Integer.parseInt(split[4]),
              Integer.parseInt(split[5]),
              split[6]);
      backend.addRecord(newRecord);
      return;
    }

    // Checks if a user tried to submit multiple and sends filepath to backend
    if (input.equals("submit") && split[1].equals("multiple")) {
      try {
        backend.readData(split[2]);
        return;
      } catch (IOException e) {
        System.out.println("File path not found correctly");
        return;
      }
    }

    // Checks if user tried to set level to a MAX
    if (input.equals("level") && split[2] == null) {
      backend.getAndSetRange(null, Integer.parseInt(split[1]));
      return;
    }

    // Otherwise checks if user tried to set a level range
    if (input.equals("level") && split[2].equals("to")) {
      backend.getAndSetRange(Integer.parseInt(split[1]), Integer.parseInt(split[3]));
      return;
    }

    // Checks if user tried to submit a time
    if (input.equals("time")) {
      backend.applyAndSetFilter(split[1]);
      return;
    }

    // Checks if user tried to show the top ten records and displays 10 or max amount
    if (input.equals("show") && split[1].equals("most") && split[2].equals("collectables")) {
      List<String> names = backend.getTopTen();
      int size = 0;
      if (names.size() < 10) {
        size = names.size();
      } else {
        size = 10;
      }
      for (int i = 0; i < size; i++) {
        System.out.println(names.get(i));
      }
      return;
    }

    // Checks if user tried to show a specific amount of records
    if (input.equals("show")) {
      List<String> names = backend.getTopTen();
      int size = 0;
      if (Integer.parseInt(split[1]) > names.size()) {
        size = names.size();
      } else {
        size = Integer.parseInt(split[1]);
      }
      for (int i = 0; i < size; i++) {
        System.out.println(names.get(i));
      }
      return;
    }

    // Displays command instructions
    if (input.equals("help")) {
      showCommandInstructions();
      return;
    }

    // User entered some unknown command
    System.out.println("Unknown command found, please try again");
  }
}
