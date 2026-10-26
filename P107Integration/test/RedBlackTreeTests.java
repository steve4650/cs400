import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** RedBlackTreeTests - JUnit 5 tests for testing the functionality of the RedBlackTree class. */
public class RedBlackTreeTests {

  /**
   * Tests single rotation insertion when inserting nodes causing line violations with a black/null
   * aunt.
   */
  @Test
  public void testRedBlackNodeInsertionLine() {
    // Left-Left Line violation: insert 30, 20, 10
    RedBlackTree<Integer> rbt1 = new RedBlackTree<>();
    rbt1.add(30);
    rbt1.add(20);
    rbt1.add(10);
    Assertions.assertEquals("[ 20.b, 10.r, 30.r ]", rbt1.root.toLevelOrderString());

    // Right-Right Line violation: insert 30, 40, 50
    RedBlackTree<Integer> rbt2 = new RedBlackTree<>();
    rbt2.add(30);
    rbt2.add(40);
    rbt2.add(50);
    Assertions.assertEquals("[ 40.b, 30.r, 50.r ]", rbt2.root.toLevelOrderString());
  }

  /**
   * Test case 1 of https://pages.cs.wisc.edu/~cs400/readings/Red-Black-Trees/ specifically, that a
   * new red child of a black node is red.
   */
  @Test
  public void case1() {
    RedBlackTree<Integer> rbt1 = new RedBlackTree<>();
    rbt1.add(4);
    Assertions.assertEquals("[ 4.b ]", rbt1.root.toLevelOrderString());
    rbt1.add(3);
    Assertions.assertEquals("[ 4.b, 3.r ]", rbt1.root.toLevelOrderString());
  }

  /** Test case 2B: red aunt (https://pages.cs.wisc.edu/~cs400/readings/Red-Black-Trees/) */
  @Test
  public void case2B() {
    RedBlackTree<Integer> rbt1 = new RedBlackTree<>();
    rbt1.add(4);
    Assertions.assertEquals("[ 4.b ]", rbt1.root.toLevelOrderString());
    rbt1.add(3);
    Assertions.assertEquals("[ 4.b, 3.r ]", rbt1.root.toLevelOrderString());
    rbt1.add(2);
    Assertions.assertEquals("[ 3.b, 2.r, 4.r ]", rbt1.root.toLevelOrderString());
    // Now, when 1 is inserted, it will have a black aunt
    rbt1.add(1);
    Assertions.assertEquals("[ 3.b, 2.b, 4.b, 1.r ]", rbt1.root.toLevelOrderString());
  }

  /**
   * Test case 2A: black (null) aunt (https://pages.cs.wisc.edu/~cs400/readings/Red-Black-Trees/)
   */
  @Test
  public void case2A() {
    RedBlackTree<Integer> rbt1 = new RedBlackTree<>();
    rbt1.add(50);
    rbt1.add(60);
    rbt1.add(70);
    Assertions.assertEquals("[ 60.b, 50.r, 70.r ]", rbt1.root.toLevelOrderString());
  }
}
