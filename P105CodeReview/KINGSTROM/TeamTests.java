import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TeamTests {

  /*
   * Test that running `quit` successfully exits the CLI interface. Uses TextUITester as a dependency.
   */
  @Test
  void testRunCommandLoop1() {
    IterableSortedCollection<GameRecord> tree = new Tree_Placeholder();
    BackendInterface backend = new Backend_Placeholder(tree);
    TextUITester textUITester = new TextUITester("quit\n");
    Scanner in = new Scanner(System.in);
    FrontendInterface frontend = new Frontend(in, backend);
    frontend.runCommandLoop();
    String result = textUITester.checkOutput();
    // Without a 'quit' command, this cannot be reached. The CLI either throws an exception
    // reading empty input is is in an infinite loop.
    Assertions.assertTrue(true);
  }

  /*
   * Test that running `help` successfully prints something that documents the "submit" command
   * (minimally, by checking that "submit" is in stdout from the command results.)
   */
  @Test
  void testShowCommandInstructions1() {
    IterableSortedCollection<GameRecord> tree = new Tree_Placeholder();
    BackendInterface backend = new Backend_Placeholder(tree);
    TextUITester textUITester = new TextUITester("help\nquit\n");
    Scanner in = new Scanner(System.in);
    FrontendInterface frontend = new Frontend(in, backend);
    frontend.runCommandLoop();
    String result = textUITester.checkOutput();
    // Test the submit document is documented, or at least mentioned.
    Assertions.assertTrue(result.contains("submit"));
  }

  /*
   * Test that an error message is printed when an invalid command `helpo` is run.`
   */
  @Test
  void testProcessSingleCommand1() {
    IterableSortedCollection<GameRecord> tree = new Tree_Placeholder();
    BackendInterface backend = new Backend_Placeholder(tree);
    TextUITester textUITester = new TextUITester("helpo\nquit\n");
    Scanner in = new Scanner(System.in);
    FrontendInterface frontend = new Frontend(in, backend);
    frontend.runCommandLoop();
    String result = textUITester.checkOutput();
    // Test the submit document is documented, or at least mentioned.
    Assertions.assertTrue(
        result.toLowerCase().contains("error")
            || result.toLowerCase().contains("invalid")
            || result.toLowerCase().contains("unknown"));
  }
}
