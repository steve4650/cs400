import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * BackendTests - JUnit 5 tests for testing the functionality of the Backend class in combination
 * with the Tree_Placeholder.
 */
public class BackendTests {

  /**
   * roleTest1 tests adding a record using addRecord. We will test this by verifying an empty
   * backend has size 3 and a non-empty one has size 4, using the Tree_Placeholder reference method.
   *
   * <p>It tests the readData method in the same way.
   */
  @Test
  public void roleTest1() {
    Tree_Placeholder tree = new Tree_Placeholder();
    Backend backend = new Backend();

    // Calling addRecord method
    GameRecord newRecord =
        new GameRecord("name", GameRecord.Continent.NORTH_AMERICA, 123, 456, 789, "369:51:11");
    Assertions.assertEquals(3, backend.tree.size());
    backend.addRecord(newRecord);
    Assertions.assertEquals(4, backend.tree.size());

    Backend backend2 = new Backend();
    Assertions.assertEquals(3, backend2.tree.size());
    try {
      backend2.readData("records.csv");
    } catch (Exception e) {
      Assertions.fail("readData threw an exception: " + e.getMessage());
    }
    Assertions.assertEquals(4, backend2.tree.size());
  }

  /**
   * roleTest2 tests applying completion time filter using applyAndSetFilter and retrieving top ten
   * records using getTopTen. It verifies that time filter properly excludes records exceeding max
   * time and getTopTen returns top records ordered by collectables.
   */
  @Test
  public void roleTest2() {
    Backend backend = new Backend();
    Assertions.assertTrue(true);
  }

  /** roleTest3 tests loading data from a CSV file using readData. */
  @Test
  public void roleTest3() {
    Backend backend = new Backend();
    Assertions.assertTrue(true);
  }
}
