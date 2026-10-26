import java.util.Iterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * RBTreeIterableTests - JUnit 5 tests for testing the functionality of the RBTreeIterable class.
 */
public class RBTreeIterableTests {

  /*
   * Test integers, duplicates, and no start/stop points
   */
  @Test
  public void testIter1() {
    RBTreeIterable<Integer> tree = new RBTreeIterable<>();
    tree.add(5);
    tree.add(3);
    tree.add(3);
    tree.add(1);

    tree.setIteratorMin(null);
    tree.setIteratorMax(null);

    Iterator<Integer> iter = tree.iterator();
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals(1, iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals(3, iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals(3, iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals(5, iter.next());
    Assertions.assertFalse(iter.hasNext());
  }

  /*
   * Tests strings, no duplicates, start points with no stop points, and stop points with no start
   * points
   */
  @Test
  public void testIter2() {
    RBTreeIterable<String> tree = new RBTreeIterable<>();
    tree.add("andromeda");
    tree.add("zulu");
    tree.add("tomato");
    tree.add("iceberg");
    tree.add("fortune");

    // start point, no stop point
    tree.setIteratorMin("barbados");
    tree.setIteratorMax(null);

    Iterator<String> iter = tree.iterator();
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("fortune", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("iceberg", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("tomato", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("zulu", iter.next());
    Assertions.assertFalse(iter.hasNext());

    // stop point, no start point
    tree.setIteratorMin(null);
    tree.setIteratorMax("andromeda");

    iter = tree.iterator();
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("andromeda", iter.next());
    Assertions.assertFalse(iter.hasNext());
  }

  /*
   * Tests strings, duplicates, and start and stop points
   */
  @Test
  public void testIter3() {
    RBTreeIterable<String> tree = new RBTreeIterable<>();
    tree.add("borealis");
    tree.add("pumpkin");
    tree.add("pumpkin");
    tree.add("fish");
    tree.add("fish");

    tree.setIteratorMin("borealis");
    tree.setIteratorMax("pumpkin");

    Iterator<String> iter = tree.iterator();
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("borealis", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("fish", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("fish", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("pumpkin", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("pumpkin", iter.next());
    Assertions.assertFalse(iter.hasNext());
  }
}
