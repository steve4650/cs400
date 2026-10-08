import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TeamTests {

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
        Assertions.assertTrue(result.toLowerCase().contains("error") || result.toLowerCase().contains("invalid") || result.toLowerCase().contains("unknown"));
    }
}
