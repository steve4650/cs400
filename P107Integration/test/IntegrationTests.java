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

    frontend.processSingleCommand("submit Johnny AFRICA 1000 200 600 100:00:00");
    Assertions.assertEquals(1, tree.size());

    frontend.processSingleCommand("submit Ziggy EUROPE 1001 210 650 200:00:00");
    Assertions.assertEquals(2, tree.size());

    frontend.processSingleCommand("submit Landsman ASIA 1002 220 700 300:00:00");
    Assertions.assertEquals(3, tree.size());

    frontend.processSingleCommand("submit Wee-Bey NORTH_AMERICA 1003 230 750 400:00:00");
    Assertions.assertEquals(4, tree.size());

    frontend.processSingleCommand("submit Lester SOUTH_AMERICA 1004 240 800 500:00:00");
    Assertions.assertEquals(5, tree.size());

    frontend.processSingleCommand("submit Kima AUSTRALIA 1005 250 850 600:00:00");
    Assertions.assertEquals(6, tree.size());

    frontend.processSingleCommand("submit Pearlman ANTARCTICA 1006 260 900 700:00:00");
    Assertions.assertEquals(7, tree.size());

    frontend.processSingleCommand("submit Daniels AFRICA 1007 270 950 800:00:00");
    Assertions.assertEquals(8, tree.size());

    frontend.processSingleCommand("submit Cheese EUROPE 1008 280 1000 900:00:00");
    Assertions.assertEquals(9, tree.size());

    frontend.processSingleCommand("submit Lamar ASIA 1009 290 1050 950:00:00");
    Assertions.assertEquals(10, tree.size());

    Assertions.assertEquals(10, tree.size());

    List<String> topTen = backend.getTopTen();
    Assertions.assertEquals(10, topTen.size());
    Assertions.assertTrue(
        topTen.containsAll(
            List.of(
                "Johnny",
                "Ziggy",
                "Landsman",
                "Wee-Bey",
                "Lester",
                "Kima",
                "Pearlman",
                "Daniels",
                "Cheese",
                "Lamar")));
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

    Assertions.assertEquals(600, tree.size());

    List<String> levelFiltered = backend.getAndSetRange(400, 500);
    Assertions.assertNotNull(levelFiltered);
    Assertions.assertEquals(58, levelFiltered.size());

    List<String> timeFiltered = backend.applyAndSetFilter("900:00:00");
    Assertions.assertNotNull(timeFiltered);
    Assertions.assertEquals(43, timeFiltered.size());

    List<String> timeFilterCleared = backend.applyAndSetFilter(null);
    Assertions.assertEquals(58, timeFilterCleared.size());

    List<String> noFilters = backend.getAndSetRange(null, null);
    Assertions.assertEquals(600, noFilters.size());
  }

  // test size after inserting different things with submit commands THEN trying different filters
  @Test
  public void IntegrationTest3() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    int initialSize = tree.size();

    frontend.processSingleCommand("submit Johnny AFRICA 1000 200 600 100:00:00");
    frontend.processSingleCommand("submit Ziggy EUROPE 1001 210 650 200:00:00");
    frontend.processSingleCommand("submit Landsman ASIA 1002 220 700 300:00:00");
    frontend.processSingleCommand("submit Wee-Bey NORTH_AMERICA 1003 230 750 400:00:00");
    frontend.processSingleCommand("submit Lester SOUTH_AMERICA 1004 240 800 500:00:00");
    frontend.processSingleCommand("submit Kima AUSTRALIA 1005 250 850 600:00:00");
    frontend.processSingleCommand("submit Pearlman ANTARCTICA 1006 260 900 700:00:00");
    frontend.processSingleCommand("submit Daniels AFRICA 1007 270 950 800:00:00");
    frontend.processSingleCommand("submit Cheese EUROPE 1008 280 1000 900:00:00");
    frontend.processSingleCommand("submit Lamar ASIA 1009 290 1050 950:00:00");

    Assertions.assertEquals(10, tree.size());

    frontend.processSingleCommand("level 700 to 1000");
    List<String> levelFiltered = backend.getAndSetRange(700, 1000);
    Assertions.assertEquals(7, levelFiltered.size());

    frontend.processSingleCommand("time 750:00:00");
    List<String> timeFiltered = backend.applyAndSetFilter("750:00:00");
    Assertions.assertEquals(5, timeFiltered.size());

    List<String> results = backend.getTopTen();
    Assertions.assertNotNull(results);
    Assertions.assertEquals(5, results.size());
  }

  // like test 3 except iterate and test specific values in sorted order
  @Test
  public void IntegrationTest4() {
    RBTreeIterable<GameRecord> tree = new RBTreeIterable<GameRecord>();
    Backend backend = new Backend(tree);
    Scanner in = new Scanner(System.in);
    Frontend frontend = new Frontend(in, backend);

    frontend.processSingleCommand("submit Johnny AFRICA 1000 200 600 100:00:00");
    frontend.processSingleCommand("submit Ziggy EUROPE 1001 210 650 200:00:00");
    frontend.processSingleCommand("submit Landsman ASIA 1002 220 700 300:00:00");
    frontend.processSingleCommand("submit Wee-Bey NORTH_AMERICA 1003 230 750 400:00:00");
    frontend.processSingleCommand("submit Lester SOUTH_AMERICA 1004 240 800 500:00:00");
    frontend.processSingleCommand("submit Kima AUSTRALIA 1005 250 850 600:00:00");
    frontend.processSingleCommand("submit Pearlman ANTARCTICA 1006 260 900 700:00:00");
    frontend.processSingleCommand("submit Daniels AFRICA 1007 270 950 800:00:00");
    frontend.processSingleCommand("submit Cheese EUROPE 1008 280 1000 900:00:00");
    frontend.processSingleCommand("submit Lamar ASIA 1009 290 1050 950:00:00");

    List<String> allRecords = backend.getAndSetRange(550, 1100);
    Assertions.assertEquals(
        List.of(
            "Johnny",
            "Ziggy",
            "Landsman",
            "Wee-Bey",
            "Lester",
            "Kima",
            "Pearlman",
            "Daniels",
            "Cheese",
            "Lamar"),
        allRecords);

    List<String> timeFiltered = backend.applyAndSetFilter("750:00:00");
    Assertions.assertEquals(
        List.of("Johnny", "Ziggy", "Landsman", "Wee-Bey", "Lester", "Kima", "Pearlman"),
        timeFiltered);

    List<String> rangeAndTimeFiltered = backend.getAndSetRange(700, 1000);
    Assertions.assertEquals(
        List.of("Landsman", "Wee-Bey", "Lester", "Kima", "Pearlman"), rangeAndTimeFiltered);

    List<String> rangeFiltered = backend.applyAndSetFilter(null);
    Assertions.assertEquals(
        List.of("Landsman", "Wee-Bey", "Lester", "Kima", "Pearlman", "Daniels", "Cheese"),
        rangeFiltered);
  }
}
