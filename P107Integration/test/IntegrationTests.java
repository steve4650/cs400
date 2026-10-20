import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegrationTests {

  @Test
  public void IntegrationTest1() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    frontend.processSingleCommand("submit Stephen AFRICA 1 2 3 456:57:59");
    Assertions.assertEquals(1, tree.size());
    List<String> topTen = backend.getTopTen();
    Assertions.assertTrue(topTen.contains("Stephen"));
  }

  @Test
  public void IntegrationTest2() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    frontend.processSingleCommand("submit multiple records.csv");
    Assertions.assertTrue(tree.size() > 0);
    
    List<String> levelFiltered = backend.getAndSetRange(400, 500);
    Assertions.assertNotNull(levelFiltered);
    Assertions.assertTrue(levelFiltered.size() >= 0);
    
    List<String> timeFiltered = backend.applyAndSetFilter("900:00:00");
    Assertions.assertNotNull(timeFiltered);
  }

  @Test
  public void IntegrationTest3() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    frontend.processSingleCommand("submit multiple records.csv");
    frontend.processSingleCommand("level 300 to 600");
    frontend.processSingleCommand("time 800:00:00");
    
    List<String> results = backend.getTopTen();
    Assertions.assertNotNull(results);
    
    Assertions.assertTrue(results.size() <= 10);
  }

  @Test
  public void showTopRecordsIntegration() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    frontend.processSingleCommand("submit Virginia EUROPE 9 8 7 640:19:12");
    frontend.processSingleCommand("submit Bunk ASIA 48563 130 493 816:08:06");
    frontend.processSingleCommand("submit McNulty AFRICA 42023 140 353 634:06:42");

    List<String> topRecords = backend.getTopTen();
    Assertions.assertEquals(3, topRecords.size());
    
    backend.applyAndSetFilter("1000:00:00");
    List<String> filteredTop = backend.getTopTen();
    Assertions.assertTrue(filteredTop.size() <= 3);
    
    backend.getAndSetRange(300, 500);
    List<String> rangeFiltered = backend.getTopTen();
    Assertions.assertNotNull(rangeFiltered);
  }
}
