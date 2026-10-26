import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** BSTRotationTests - JUnit 5 tests for testing the functionality of the BSTRotation class. */
public class BSTRotationTests {

  /** Test performing both left and right rotations */
  @Test
  public void test1() {
    BSTRotation<Integer> intTree = new BSTRotation<Integer>();
    intTree.add(50);
    intTree.add(17);
    intTree.add(9);
    intTree.add(23);
    intTree.add(76);
    // left rotation
    intTree.rotate(intTree.root.downLeft(), intTree.root);
    Assertions.assertTrue(
        intTree.root.getEntry() == 17
            && intTree.root.downLeft().getEntry() == 9
            && intTree.root.downLeft().downLeft() == null
            && intTree.root.downLeft().downRight() == null
            && intTree.root.downRight().getEntry() == 50
            && intTree.root.downRight().downLeft().getEntry() == 23
            && intTree.root.downRight().downRight().getEntry() == 76
            && intTree.root.downRight().downLeft().downLeft() == null
            && intTree.root.downRight().downLeft().downRight() == null);
    // right rotation
    intTree.rotate(intTree.root.downRight(), intTree.root);
    Assertions.assertTrue(
        intTree.root.getEntry() == 50
            && intTree.root.downLeft().getEntry() == 17
            && intTree.root.downLeft().downLeft().getEntry() == 9
            && intTree.root.downLeft().downLeft().downLeft() == null
            && intTree.root.downLeft().downLeft().downRight() == null
            && intTree.root.downLeft().downRight().getEntry() == 23
            && intTree.root.downLeft().downRight().downLeft() == null
            && intTree.root.downLeft().downRight().downRight() == null
            && intTree.root.downRight().getEntry() == 76
            && intTree.root.downRight().downLeft() == null
            && intTree.root.downRight().downRight() == null);
  }

  /** Test performing rotations that include the root node, and some that do not. */
  @Test
  public void test2() {
    // includes root
    BSTRotation<Integer> intTree = new BSTRotation<Integer>();
    intTree.add(100);
    intTree.add(34);
    intTree.add(18);
    intTree.add(46);
    intTree.add(152);
    // left rotation
    intTree.rotate(intTree.root.downLeft(), intTree.root);
    Assertions.assertTrue(
        intTree.root.getEntry() == 34
            && intTree.root.downLeft().getEntry() == 18
            && intTree.root.downLeft().downLeft() == null
            && intTree.root.downLeft().downRight() == null
            && intTree.root.downRight().getEntry() == 100
            && intTree.root.downRight().downLeft().getEntry() == 46
            && intTree.root.downRight().downRight().getEntry() == 152
            && intTree.root.downRight().downLeft().downLeft() == null
            && intTree.root.downRight().downLeft().downRight() == null);

    // does not include root
    BSTRotation<Integer> intTree2 = new BSTRotation<Integer>();
    intTree2.add(105);
    intTree2.add(100);
    intTree2.add(34);
    intTree2.add(18);
    intTree2.add(46);
    intTree2.add(101);
    // left rotation
    intTree2.rotate(intTree2.root.downLeft().downLeft(), intTree2.root.downLeft());
    Assertions.assertTrue(
        intTree2.root.downLeft().getEntry() == 34
            && intTree2.root.downLeft().downLeft().getEntry() == 18
            && intTree2.root.downLeft().downLeft().downLeft() == null
            && intTree2.root.downLeft().downLeft().downRight() == null
            && intTree2.root.downLeft().downRight().getEntry() == 100
            && intTree2.root.downLeft().downRight().downLeft().getEntry() == 46
            && intTree2.root.downLeft().downRight().downRight().getEntry() == 101
            && intTree2.root.downLeft().downRight().downLeft().downLeft() == null
            && intTree2.root.downLeft().downRight().downLeft().downRight() == null);
  }

  /**
   * Test performing rotations on parent-child pairs of nodes that have between them 0, 1, 2, and 3
   * shared children (that do not include the child being rotated).
   */
  @Test
  public void test3() {
    BSTRotation<String> stringTree = new BSTRotation<String>();
    stringTree.add("zoe");
    stringTree.add("roe");
    // 0 shared children
    stringTree.rotate(stringTree.root.downLeft(), stringTree.root);
    Assertions.assertTrue(
        "roe".equals(stringTree.root.getEntry())
            && stringTree.root.downLeft() == null
            && "zoe".equals(stringTree.root.downRight().getEntry()));
    // 1 shared child
    stringTree.add("yoe");
    stringTree.rotate(stringTree.root.downRight(), stringTree.root);
    Assertions.assertTrue(
        "zoe".equals(stringTree.root.getEntry())
            && "roe".equals(stringTree.root.downLeft().getEntry())
            && "yoe".equals(stringTree.root.downLeft().downRight().getEntry()));
    // 2 shared children
    stringTree.add("xoe");
    stringTree.rotate(stringTree.root.downLeft(), stringTree.root);
    Assertions.assertTrue(
        "roe".equals(stringTree.root.getEntry())
            && "zoe".equals(stringTree.root.downRight().getEntry())
            && "yoe".equals(stringTree.root.downRight().downLeft().getEntry())
            && "xoe".equals(stringTree.root.downRight().downLeft().downLeft().getEntry()));
    // 3 shared children
    stringTree.add("voe");
    stringTree.rotate(stringTree.root.downRight(), stringTree.root);
    Assertions.assertTrue(
        "zoe".equals(stringTree.root.getEntry())
            && "roe".equals(stringTree.root.downLeft().getEntry())
            && "yoe".equals(stringTree.root.downLeft().downRight().getEntry())
            && "xoe".equals(stringTree.root.downLeft().downRight().downLeft().getEntry())
            && "voe"
                .equals(stringTree.root.downLeft().downRight().downLeft().downLeft().getEntry()));
  }
}
