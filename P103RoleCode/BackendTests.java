import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * BackendTests - JUnit 5 tests for testing the functionality of the Backend class in combination
 * with the Tree_Placeholder.
 */
public class BackendTests {

  /**
   * roleTest1 tests adding a record using addRecord and setting level range using getAndSetRange.
   * It checks that records outside the specified level range are filtered out and records within
   * the level range are included.
   */
  @Test
  public void roleTest1() {
    Tree_Placeholder tree = new Tree_Placeholder();
    Backend backend = new Backend(tree);

    // Call 1: addRecord
    GameRecord newRecord =
        new GameRecord(
            "testHero", GameRecord.Continent.NORTH_AMERICA, 45000, 200, 450, "500:00:00");
    backend.addRecord(newRecord);
    assertEquals(newRecord, tree.lastAddedGameRecord);

    // Call 2: getAndSetRange (range 300 to 480)
    // Placeholder has: speedRoyalty (479), xXxgamer47xXx (493), v0idt3mp0 (353), plus testHero
    // (450)
    List<String> rangeNames = backend.getAndSetRange(300, 480);
    assertNotNull(rangeNames);
    assertTrue(rangeNames.contains("v0idt3mp0"), "v0idt3mp0 (level 353) should be in range");
    assertTrue(rangeNames.contains("speedRoyalty"), "speedRoyalty (level 479) should be in range");
    assertTrue(rangeNames.contains("testHero"), "testHero (level 450) should be in range");
    assertFalse(
        rangeNames.contains("xXxgamer47xXx"), "xXxgamer47xXx (level 493) should be out of range");
  }

  /**
   * roleTest2 tests applying completion time filter using applyAndSetFilter and retrieving top ten
   * records using getTopTen. It verifies that time filter properly excludes records exceeding max
   * time and getTopTen returns top records ordered by collectables.
   */
  @Test
  public void roleTest2() {
    Tree_Placeholder tree = new Tree_Placeholder();
    Backend backend = new Backend(tree);

    // Establish range covering all elements
    backend.getAndSetRange(0, 1000);

    // Call 3: applyAndSetFilter ("640:00:00")
    // speedRoyalty: "640:09:00" (> 640:00:00), xXxgamer47xXx: "956:48:46" (> 640:00:00),
    // v0idt3mp0: "634:06:42" (< 640:00:00)
    List<String> filteredNames = backend.applyAndSetFilter("640:00:00");
    assertNotNull(filteredNames);
    assertTrue(filteredNames.contains("v0idt3mp0"), "v0idt3mp0 should pass time filter");
    assertFalse(filteredNames.contains("speedRoyalty"), "speedRoyalty should fail time filter");
    assertFalse(filteredNames.contains("xXxgamer47xXx"), "xXxgamer47xXx should fail time filter");

    // Call 4: getTopTen
    List<String> topTen = backend.getTopTen();
    assertNotNull(topTen);
    assertEquals(1, topTen.size());
    assertEquals("v0idt3mp0", topTen.get(0));
  }

  /**
   * roleTest3 tests loading data from a CSV file using readData. It verifies that reading valid CSV
   * data populates the backend tree without throwing IOException.
   */
  @Test
  public void roleTest3() {
    Tree_Placeholder tree = new Tree_Placeholder();
    Backend backend = new Backend(tree);

    // Call 5: readData
    assertDoesNotThrow(
        () -> {
          backend.readData("records.csv");
        },
        "readData should successfully read records.csv without throwing exceptions");

    assertNotNull(tree.lastAddedGameRecord, "readData should have added records to tree");
    List<String> records = backend.getAndSetRange(null, null);
    assertFalse(records.isEmpty(), "backend should return records after reading dataset");
  }
}
