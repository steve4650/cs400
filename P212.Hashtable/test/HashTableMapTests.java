import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * JUnit 5 test suite for HashTableMap.
 */
public class HashTableMapTests {

  /**
   * Test 1: inserting and retrieving key-value pairs.
   */
  @Test
  public void test1() {
    HashTableMap<String, Integer> map = new HashTableMap<>();
    map.put("apple", 1);
    map.put("banana", 2);
    Assertions.assertEquals(2, map.getSize());
    Assertions.assertTrue(map.containsKey("apple"));
    Assertions.assertTrue(map.containsKey("banana"));
    Assertions.assertFalse(map.containsKey("pear"));
    Assertions.assertEquals(1, map.get("apple"));
    Assertions.assertEquals(2, map.get("banana"));
    Assertions.assertThrows(NoSuchElementException.class, () -> map.get("pear"));
  }

  /**
   * Test 2: removing key-value pairs.
   */
  @Test
  public void test2() {
    HashTableMap<String, Integer> map = new HashTableMap<>();
    map.put("apple", 1);
    map.put("banana", 2);
    Assertions.assertEquals(1, map.remove("apple"));
    Assertions.assertEquals(1, map.getSize());
    Assertions.assertFalse(map.containsKey("apple"));
    Assertions.assertThrows(NoSuchElementException.class, () -> map.get("apple"));
    Assertions.assertEquals(2, map.get("banana"));
  }

  /**
   * Test 3: clearing.
   */
  @Test
  public void test3() {
    HashTableMap<String, Integer> map = new HashTableMap<>();
    map.put("apple", 1);
    map.put("banana", 2);
    map.clear();
    Assertions.assertEquals(0, map.getSize());
    Assertions.assertFalse(map.containsKey("apple"));
    map.put("apple", 1);
    map.put("banana", 2);
    Assertions.assertEquals(2, map.getSize());
    Assertions.assertEquals(1, map.get("apple"));
    Assertions.assertEquals(2, map.get("banana"));
    Assertions.assertThrows(NoSuchElementException.class, () -> map.get("pear"));
  }

  /**
   * Test 4: Tests collision handling: 0 and 8 should not clobber each other.
   */
  @Test
  public void test4() {
    HashTableMap<Integer, String> map = new HashTableMap<>(8);
    map.put(0, "zero");
    map.put(8, "eight");
    Assertions.assertEquals("zero", map.get(0));
    Assertions.assertEquals("eight", map.get(8));
    List<Integer> keys = map.getKeys();
    Assertions.assertEquals(2, keys.size());
    Assertions.assertTrue(keys.contains(0));
    Assertions.assertTrue(keys.contains(8));
  }

  /**
   * Test 5: Tests that the table resizes at 75% load factor and rehashes correctly.
   */
  @Test
  public void test5() {
    HashTableMap<String, Integer> map = new HashTableMap<>(8);
    Assertions.assertEquals(8, map.getCapacity());
    map.put("k1", 1);
    map.put("k2", 2);
    map.put("k3", 3);
    map.put("k4", 4);
    map.put("k5", 5);
    Assertions.assertEquals(8, map.getCapacity());
    map.put("k6", 6);
    Assertions.assertEquals(16, map.getCapacity());
    Assertions.assertEquals(6, map.getSize());
    Assertions.assertEquals(1, map.get("k1"));
    Assertions.assertEquals(2, map.get("k2"));
    Assertions.assertEquals(3, map.get("k3"));
    Assertions.assertEquals(4, map.get("k4"));
    Assertions.assertEquals(5, map.get("k5"));
    Assertions.assertEquals(6, map.get("k6"));
  }
}
