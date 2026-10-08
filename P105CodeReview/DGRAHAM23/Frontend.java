/*
 *  Author: Douglas Graham
 *  Email: dgraham23@wisc.edu
 *  Course: CS400, Fall 2026
 *  Assignment: P103.RoleCode
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Frontend implements FrontendInterface {

  // Initialize scanner and backend objects
  protected Scanner in;
  protected BackendInterface backend;

  /**
   * Constructor for the frontend.
   *
   * @param in scanner to take user inputs
   * @param backend reference for the backend that will be used to process valid user commands
   */
  public Frontend(Scanner in, BackendInterface backend) {
    this.in = in;
    this.backend = backend;
  }

  /** Entry point for the frontend. Displays command instructions and processes user inputs. */
  @Override
  public void runCommandLoop() {
    showCommandInstructions();

    // Get the first input
    String command = this.in.nextLine();

    // Continue to take user inputs until 'quit' is entered
    while (!command.equals("quit")) {
      processSingleCommand(command);
      command = this.in.nextLine();
    }
  }

  /**
   * Displays instructions for the user to understand the syntax of commands that they are able to
   * enter.
   */
  @Override
  public void showCommandInstructions() {
    System.out.println("Commands are case sensitive and must be lowercase.");
    System.out.println(
        "Arguements must be provided in the specified format and order for each command.");
    System.out.println("Available commands:");

    // submit command instructions
    System.out.println();
    System.out.println("'submit' Command:");
    System.out.println(
        "\tThe 'submit' command allows you to add new game records to the leaderboard.");
    System.out.println("'submit' Arguements:");
    System.out.println("\tNAME: The name of the player.");
    System.out.println("\tCONTINENT: The continent where the player is from.");
    System.out.println(
        "\t           Allowed entries:" + Arrays.asList(GameRecord.Continent.values()));
    System.out.println("\tSCORE: The score achieved by the player.");
    System.out.println("\tCOLLECTABLES: The number of collectables found by the player.");
    System.out.println("\tLEVEL: The max level number that the player reached.");
    System.out.println(
        "\tCOMPLETION_TIME: THe time taken to complete the game (format: hhh:mm:ss).");
    System.out.println("'submit' Formatting:");
    System.out.println("\tsubmit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME");

    // submit multiple command instructions
    System.out.println();
    System.out.println("'submit multiple' Command:");
    System.out.println(
        "\tThe 'submit multiple' command allows you to add multiple game records from a file.");
    System.out.println("'submit multiple' Arguement:");
    System.out.println("\tFILEPATH: The file path to be used.");
    System.out.println("'submit multiple' Formatting:");
    System.out.println("\tsubmit multiple FILEPATH");

    // level command instructions
    System.out.println();
    System.out.println("'level' Command:");
    System.out.println(
        "\tThe 'level command updates the range of records displayed by the 'show' command.");
    System.out.println("\tYou can set a maximum level, or both a minimum and maximum level.");
    System.out.println("\tSubmitting without arguments will clear existing filters.");
    System.out.println("'level' Arguments:");
    System.out.println("\tMIN: The minimum level to filter by (inclusive).");
    System.out.println("\tMAX: The maximum level to filter by (inclusive).");
    System.out.println("'level' Formatting:");
    System.out.println("\tlevel MAX");
    System.out.println("\tlevel MIN to MAX");

    // time command instructions
    System.out.println();
    System.out.println("'time' Command:");
    System.out.println(
        "\tThe 'time' command updates the range of records displayed by the 'show' command.");
    System.out.println(
        "\tYou can set a maximum completion time. Only records with less than this time will be"
            + " shown.");
    System.out.println("'time' Argument:");
    System.out.println("\tTIME: The time to filter by in the format hhh:mm:ss");
    System.out.println("'time' Formatting:");
    System.out.println("\ttime TIME");

    // show command instructions
    System.out.println();
    System.out.println("'show' Command:");
    System.out.println(
        "\tThe 'show' command will display the records that meet the current filter settings.");
    System.out.println("'show' Arguments;");
    System.out.println("\tMAX_COUNT: The number of records to show (up to 10)");
    System.out.println("'show' Formatting:");
    System.out.println("\tshow MAX_COUNT");

    System.out.println();
    System.out.println("'show most collectables' Command:");
    System.out.println(
        "\tWill display the top ten records that match the current filter settings.");
    System.out.println("\tThis is equivalent to 'show 10'.");

    // other command instructions
    System.out.println();
    System.out.println("Other No Argument Commands:");
    System.out.println("\tThe 'help' command will redisplay these instructions.");
    System.out.println("\tThe 'quit' command will exit the app.");
    System.out.println();
  }

  /**
   * Triages commands input by the user and sends them to the appropriate method for processing.
   *
   * @param command the command string input by the user
   */
  @Override
  public void processSingleCommand(String command) {
    if (command.startsWith("submit")) {
      processSubmitCommand(command);
    } else if (command.startsWith("level")) {
      processLevelCommand(command);
    } else if (command.startsWith("time")) {
      processTimeCommand(command);
    } else if (command.startsWith("show")) {
      processShowCommand(command);
    } else if (command.equals("help")) {
      showCommandInstructions();
    } else {
      invalidCommand(command);
    }
    System.out.println(); // For formatting
  }

  /**
   * Processes 'submit' commands entered by the user and validates each arument against its
   * description in showCommandInstructions()
   *
   * @param command the command string input by the user
   */
  protected void processSubmitCommand(String command) {
    String[] commandParts = command.split(" ");

    if (commandParts.length != 7 && commandParts.length != 3) {
      invalidCommand(command);
      System.out.println("\tRecieved and invalid number of arguments.");

      // Process 'submit multiple'
    } else if (commandParts[1].equals("multiple")) {
      String filePath = commandParts[2];
      try {
        this.backend.readData(filePath);
        System.out.println("File accepted: " + filePath);
      } catch (IOException e) {
        invalidCommand(command);
        System.out.println("\tError in filepath: " + e.getMessage());
      }

      // Process 'submit'
    } else if (commandParts.length == 7) {
      boolean printed = false;

      String name = commandParts[1];

      GameRecord.Continent continent = inputIsContinent(commandParts[2]);
      if (continent == null) {
        printed = invalidCommand(command, printed);
        System.out.println("\t" + commandParts[2] + " is not a valid continent.");
      }

      Integer score = inputIsNumber(commandParts[3]);
      printed = invalidInteger(score, commandParts[3], command, printed);

      Integer collectables = inputIsNumber(commandParts[4]);
      printed = invalidInteger(collectables, commandParts[4], command, printed);

      Integer level = inputIsNumber(commandParts[5]);
      printed = invalidInteger(level, commandParts[5], command, printed);

      String time = inputIsTime(commandParts[6]);
      if (time == null) {
        printed = invalidCommand(command, printed);
        System.out.println("\t" + commandParts[6] + " is not a valid time [hhh:mm:ss]");
      }

      // If invalid statement has not printed, file score
      if (!printed) {
        GameRecord record = new GameRecord(name, continent, score, collectables, level, time);
        this.backend.addRecord(record);

        System.out.println("New game record added:");
        System.out.println("\tName: " + name);
        System.out.println("\tContinent: " + continent.toString());
        System.out.println("\tScore: " + score);
        System.out.println("\tLevel: " + level);
        System.out.println("\tTime: " + time);
      }
    } else {
      invalidCommand(command);
    }
  }

  /**
   * Processes 'level' commands entered by the user and validates each argument against its
   * description in showCommandInstructions()
   *
   * @param command the command sting input by the user
   */
  protected void processLevelCommand(String command) {
    String[] commandParts = command.split(" ");

    // Reset filters
    if (commandParts.length == 1) {
      this.backend.getAndSetRange(null, null);
      System.out.println("Level filters removed");

      // level MAX
    } else if (commandParts.length == 2) {
      Integer max = inputIsNumber(commandParts[1]);
      if (!invalidInteger(max, commandParts[1], command, false)) {
        this.backend.getAndSetRange(null, max);
        System.out.println("Max level filter, " + max + ", added.");
      }
      // level MIN to MAX
    } else if (commandParts.length == 4 && commandParts[2].equals("to")) {
      boolean printed = false;

      Integer min = inputIsNumber(commandParts[1]);
      printed = invalidInteger(min, commandParts[1], command, printed);

      Integer max = inputIsNumber(commandParts[3]);
      printed = invalidInteger(max, commandParts[3], command, printed);

      if (!printed) {
        this.backend.getAndSetRange(min, max);
        System.out.println("Minimum, " + min + ", and maximum, " + max + ", level filters set.");
      }

      // General failure
    } else {
      invalidCommand(command);
    }
  }

  /**
   * Processes 'time' commands entered by the user and validates each argument against its
   * description in showCommandInstructions()
   *
   * @param command the command string imput by the user
   */
  protected void processTimeCommand(String command) {
    String[] commandParts = command.split(" ");

    // Reset filters
    if (commandParts.length == 1) {
      this.backend.applyAndSetFilter(null);
      System.out.println("Time filter removed.");

      // Set filter
    } else if (commandParts.length == 2) {
      String time = inputIsTime(commandParts[1]);
      if (time == null) {
        invalidCommand(command);
        System.out.println("\t" + commandParts[1] + " is not a valid time [hhh:mm:ss].");
      } else {
        this.backend.applyAndSetFilter(time);
        System.out.println("Time filter, " + time + ", added.");
      }

      // Generic invalid
    } else {
      invalidCommand(command);
    }
  }

  /**
   * Processes 'show' commands entered by the user and validates each agument against its
   * description in showCommandInstructions()
   */
  protected void processShowCommand(String command) {
    String[] commandParts = command.split(" ");

    // show top tem
    if (commandParts.length == 3
        && commandParts[1].equals("most")
        && commandParts[2].equals("collectables")) {
      showTopCount(10);

      // show MAX_COUNT
    } else if (commandParts.length == 2) {
      Integer maxCount = inputIsNumber(commandParts[1]);
      if (maxCount == null) {
        invalidCommand(command);
        System.out.println("\t" + commandParts[1] + " is not a valid integer.");
      } else {
        showTopCount(maxCount);
      }

      // Generic invalid
    } else {
      invalidCommand(command);
    }
  }

  /**
   * Helper method for processShowCommand(). Gathers and diaplsy the top maxCount user records (up
   * to ten).
   *
   * @param maxCount count of user records to show (will be set to 10 if > 10)
   */
  protected void showTopCount(Integer maxCount) {
    String[] topTen = this.backend.getTopTen().toArray(new String[10]);

    if (maxCount > 10) {
      maxCount = 10;
    }
    System.out.println("Top " + maxCount + " Players:");

    for (int i = 0; i < maxCount; i++) {
      if (topTen[i] != null) {
        System.out.println('\t' + topTen[i]);
      } else {
        break;
      }
    }
  }

  /**
   * Prints invalid message for values that should be integers. There are a lost of these, so this
   * gets its own method for the sake of less typing.
   *
   * @param integer the integer to validate as not null
   * @param commandPart the command part that produced integer via inputIsNumber()
   * @param command the command string input by the user
   * @param printed true if an invalid message had been shown before, false otherwise
   * @return updated true/false for printed
   */
  protected boolean invalidInteger(
      Integer integer, String commandPart, String command, boolean printed) {
    if (integer == null) {
      printed = invalidCommand(command, printed);
      System.out.println("\t" + commandPart + " is not a valid integer.");
    }
    return printed;
  }

  /**
   * Displays a generic invalid message.
   *
   * @param command the command string input by the user
   */
  protected void invalidCommand(String command) {
    System.out.println("Invalid command: " + command);
  }

  /**
   * Displays a generic invalid message if not previously displayed.
   *
   * @param command the command string input by the user
   * @param printed true if and invalid message has been shown before, false otherwise
   * @return updated true/fasle for printed
   */
  protected boolean invalidCommand(String command, boolean printed) {
    if (!printed) {
      System.out.println("Invalid command: " + command);
    }
    return true;
  }

  /**
   * Validates that the input string only contains numeric characters.
   *
   * @param input the string to validate
   * @return null if the string is not a number, otherwise the string as an integer
   */
  protected Integer inputIsNumber(String input) {
    char[] charArray = input.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      if (!Character.isDigit(charArray[i])) {
        return null;
      }
    }
    return Integer.valueOf(input);
  }

  /**
   * Validates that the input string is equal to a value in the GameRecord enum list Continent.
   *
   * @param input the input string
   * @return null if the input is not in the enum list, otherwise the enum value it matches
   */
  protected GameRecord.Continent inputIsContinent(String input) {
    for (GameRecord.Continent continent : GameRecord.Continent.values()) {
      if (input.equals(continent.toString())) {
        return continent;
      }
    }
    return null;
  }

  /**
   * Validates that the input string is a time in the form hhh:mm:ss
   *
   * @param input the input string
   * @return null if the input is not of the correct format, otherwise the input is returned
   */
  protected String inputIsTime(String input) {
    char[] charArray = input.toCharArray();

    if (charArray.length != 9) {
      return null;
    }

    // Split the array values for number and colons
    int[] numbers = {0, 1, 2, 4, 5, 7, 8};
    int[] colons = {3, 6};

    for (int number : numbers) {
      if (!Character.isDigit(charArray[number])) {
        return null;
      }
    }

    for (int colon : colons) {
      if (charArray[colon] != ':') {
        return null;
      }
    }

    // 60 sec -> 1 min, 60 min -> 1 hour
    if (Character.getNumericValue(charArray[4]) > 5
        || Character.getNumericValue(charArray[7]) > 5) {
      return null;
    }

    return input;
  }
}
