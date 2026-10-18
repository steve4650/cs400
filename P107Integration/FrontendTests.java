/*
 * Author: Douglas Graham
 * Email: dgraham23@wisc,edu
 * Course: CS400, Fall 2026
 * Assignment: P103.RoleCode
 */

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Scanner;

public class FrontendTests {
	
	/**
	 * Tests the Frontend's submit commands by attempting valid
	 * and invalid 'submit' and 'submit multiple' commands
	 */
	@Test
	public void roleTest1() {
		// Valid 'submit'
		testHelper("submit", "New game record added:", "submit ne0nVandal NORTH_AMERICA 49723 140 507 902:36:58", true);
		// Invalid 'submit'
		testHelper("submit", "is not a valid continent.", "submit ne0mVandal AMERICA 49723 140 507 902:36:58", false);

		// Valid 'submit multiple'
		testHelper("submit multiple", "File accepted:", "submit multiple records.csv", true);
		// Invalid 'submit multiple'
		testHelper("submit multiple", "Invalid command:", "submit multiple", false);
	}

	/**
	 * Tests the Frontend's level, time, & show commands by attempting
	 * a valid and invalid submission of each.
	 */
	@Test
	public void roleTest2() {
		// Valid 'level'
		testHelper("level", "level filters set", "level 20 to 150", true);
		// Invalid 'level'
		testHelper("level", "is not a valid integer", "level five", false);

		// Valid time
		testHelper("time", "added", "time 111:11:11", true);
		// Invalid time
		testHelper("time", "is not a valid time", "time 99:99", false);

		// Valid show
		testHelper("show", "Top 2 Players:", "show 2", true);
		// Invalid show
		testHelper("show", "is not a valid integer", "show seven", false);
	}

	/**
	 * Tests the main quit, help, and
	 * show most collectables commands.
	 */
	@Test
	public void roleTest3() {
		// 'help'
		testHelper("help", "Commands are case sensitive", "help", true);

		// 'show most collectables'
		testHelper("show most collectables", "speedRoyalty", "show most collectables", true);

		// 'quit'
		testHelper("quit", "", "quit", true);
	}

	protected void testHelper(String commandType, String expected, String command, boolean validCommand) {
		String validity = "invalid";
		if (validCommand) {validity = "valid";}
		// Initialize tester
		TextUITester tester = new TextUITester(command);
		// Run command
		testCommand();
		// Check output
		String output = tester.checkOutput();
		String fail = "Failed " + validity + " '" + commandType + "' command:\n\tInput: "
			+ command + "\n\tOutput: " + output;
		Assertions.assertTrue(output.contains(expected), fail);
	}

	/**
	 * Creates a Frontend object and passes a single command
	 * to it for testing with TextUITester
	 */
	protected void testCommand() {
		Scanner in = new Scanner(System.in);
		String command = in.nextLine();
		BackendInterface backend = new Backend_Placeholder(new Tree_Placeholder());
		Frontend frontend = new Frontend(in, backend);
		frontend.processSingleCommand(command);
	}
}
