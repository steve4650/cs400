import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegrationTests {

  // test size after inserting different things with submit commands
  @Test
  public void IntegrationTest1() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    int initialSize = tree.size();

    frontend.processSingleCommand("submit Stephen AFRICA 1 2 3 456:57:59");
    Assertions.assertEquals(initialSize + 1, tree.size());

    frontend.processSingleCommand("submit Alice EUROPE 10 20 50 100:00:00");
    Assertions.assertEquals(initialSize + 2, tree.size());

    frontend.processSingleCommand("submit Bob ASIA 30 40 70 200:00:00");
    Assertions.assertEquals(initialSize + 3, tree.size());

    frontend.processSingleCommand("submit Bad AMERICA 1 2 3 456:57:59");
    frontend.processSingleCommand("submit Bad AFRICA x 2 3 456:57:59");
    frontend.processSingleCommand("submit Bad AFRICA 1 2 x 456:57:59");
    frontend.processSingleCommand("submit Bad AFRICA 1 2 3 456:57");
    Assertions.assertEquals(initialSize + 3, tree.size());

    List<String> topTen = backend.getTopTen();
    Assertions.assertTrue(topTen.contains("Stephen"));
    Assertions.assertTrue(topTen.contains("Alice"));
    Assertions.assertTrue(topTen.contains("Bob"));
  }

  // submit records.csv and test size of backend after applying different filters
  @Test
  public void IntegrationTest2() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    int initialSize = tree.size();
    frontend.processSingleCommand("submit multiple records.csv");

    Assertions.assertEquals(initialSize + 600, tree.size());

    List<String> levelFiltered = backend.getAndSetRange(400, 500);
    Assertions.assertNotNull(levelFiltered);
    Assertions.assertEquals(60, levelFiltered.size());

    List<String> timeFiltered = backend.applyAndSetFilter("900:00:00");
    Assertions.assertNotNull(timeFiltered);
    Assertions.assertEquals(44, timeFiltered.size());

    List<String> timeFilterCleared = backend.applyAndSetFilter(null);
    Assertions.assertEquals(60, timeFilterCleared.size());

    List<String> noFilters = backend.getAndSetRange(null, null);
    Assertions.assertEquals(initialSize + 600, noFilters.size());
  }

  // test size after inserting different things with submit commands THEN trying different filters
  @Test
  public void IntegrationTest3() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    int initialSize = tree.size();

    frontend.processSingleCommand("submit Alice EUROPE 100 20 250 500:00:00");
    frontend.processSingleCommand("submit Bob ASIA 200 30 350 550:00:00");
    frontend.processSingleCommand("submit Carol AFRICA 300 40 450 650:00:00");
    frontend.processSingleCommand("submit Dave NORTH_AMERICA 400 50 550 750:00:00");
    frontend.processSingleCommand("submit Eve SOUTH_AMERICA 500 60 650 850:00:00");
    frontend.processSingleCommand("submit Frank AUSTRALIA 600 70 750 950:00:00");

    frontend.processSingleCommand("submit Bad AMERICA 1 2 3 100:00:00");
    frontend.processSingleCommand("submit Bad AFRICA 1 2 3 100:00");

    Assertions.assertEquals(initialSize + 6, tree.size());

    frontend.processSingleCommand("level 300 to 600");
    List<String> levelFiltered = backend.getAndSetRange(300, 600);
    Assertions.assertEquals(6, levelFiltered.size());

    frontend.processSingleCommand("time 700:00:00");
    List<String> timeFiltered = backend.applyAndSetFilter("700:00:00");
    Assertions.assertEquals(4, timeFiltered.size());

    List<String> results = backend.getTopTen();
    Assertions.assertNotNull(results);
    Assertions.assertEquals(4, results.size());
  }

  // like test 3 except iterate and test specific values in sorted order
  @Test
  public void IntegrationTest4() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    frontend.processSingleCommand("submit Virginia EUROPE 9 8 20 640:19:12");
    frontend.processSingleCommand("submit Bunk ASIA 48563 130 300 816:08:06");
    frontend.processSingleCommand("submit McNulty AFRICA 42023 140 600 634:06:42");
    frontend.processSingleCommand("submit Omar NORTH_AMERICA 1000 150 700 400:00:00");

    List<String> allRecords = backend.getAndSetRange(null, null);
    Assertions.assertEquals(
        List.of(
            "Virginia", "Bunk", "v0idt3mp0", "speedRoyalty", "xXxgamer47xXx", "McNulty", "Omar"),
        allRecords);

    List<String> timeFiltered = backend.applyAndSetFilter("700:00:00");
    Assertions.assertEquals(
        List.of("Virginia", "v0idt3mp0", "speedRoyalty", "McNulty", "Omar"), timeFiltered);

    List<String> rangeAndTimeFiltered = backend.getAndSetRange(300, 600);
    Assertions.assertEquals(List.of("v0idt3mp0", "speedRoyalty", "McNulty"), rangeAndTimeFiltered);

    List<String> rangeFiltered = backend.applyAndSetFilter(null);
    Assertions.assertEquals(
        List.of("Bunk", "v0idt3mp0", "speedRoyalty", "xXxgamer47xXx", "McNulty"), rangeFiltered);
  }
}
