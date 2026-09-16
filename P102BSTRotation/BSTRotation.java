public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a left child of the provided parent, this method will perform a right rotation. When the
   * provided child is a right child of the provided parent, this method will perform a left
   * rotation.
   *
   * @param child is the node being rotated from child to parent position
   * @param parent is the node being rotated from parent to child position
   */
  protected void rotate(BinaryNode<T> child, BinaryNode<T> parent) {
    // Case 1: `child` it not a child of `parent`: do nothing
    if (parent.downLeft() != child && parent.downRight() != child) {
      return;
    }
    // Update root if needed
    if (this.root == parent) {
      this.root = child;
    }
    /*
     * Case 2: `child` is a left child of `parent`: right rotation
     * Every relationship below changes except parent <--> C and child <--> A
     *
     *   Before:
     *
     *             grandparent
     *                |
     *                |
     *              parent
     *               /
     *              / \
     *             /   \
     *            /     \
     *          child    C
     *           /
     *          / \
     *         /   \
     *        /     \
     *       A       B
     *
     *
     * After:
     *
     *             grandparent
     *                |
     *                |
     *              child
     *               /
     *              / \
     *             /   \
     *            /     \
     *           A      parent
     *                   /
     *                  / \
     *                 /   \
     *                /     \
     *               B       C
     *
     */
    if (parent.downLeft() == child) {
      BinaryNode<T> b = child.downRight();
      BinaryNode<T> grandparent = parent.up();
      child.setUp(grandparent);
      child.setRight(parent);
      parent.setUp(child);
      parent.setLeft(b);
      b.setUp(parent);
    }
    /*
     * Case 3: `child` is a right child of `parent`: left rotation
     * Every relationship below changes except child <--> C and parent <--> A
     *
     *   Before:
     *
     *             grandparent
     *                |
     *                |
     *              parent
     *               /
     *              / \
     *             /   \
     *            /     \
     *           A     child
     *                  /
     *                 / \
     *                /   \
     *               /     \
     *              B       C
     *
     *
     * After:
     *
     *             grandparent
     *                |
     *                |
     *              child
     *               /
     *              / \
     *             /   \
     *            /     \
     *         parent    C
     *           /
     *          / \
     *         /   \
     *        /     \
     *       A       B
     *
     */
    else {
      BinaryNode<T> b = child.downLeft();
      BinaryNode<T> grandparent = parent.up();
      child.setUp(grandparent);
      child.setLeft(parent);
      parent.setUp(child);
      parent.setRight(b);
      b.setUp(parent);
    }
  }

  public static void main(String[] args) {
    BSTRotation<Integer> testRotation = new BSTRotation<>();
    boolean result = testRotation.test1();
    if (!result) {
      System.out.println("failed test1");
      return;
    }
    result = testRotation.test2();
    if (!result) {
      System.out.println("failed test2");
      return;
    }
    System.out.println("passed everything");
  }

  /**
   * Test performing both left and right rotations
   *
   * @return whether or not the test passes
   */
  public boolean test1() {
    BSTRotation<Integer> intTree = new BSTRotation<Integer>();
    intTree.add(50);
    intTree.add(17);
    intTree.add(9);
    intTree.add(23);
    intTree.add(76);
    // left rotation
    intTree.rotate(intTree.root.downLeft(), intTree.root);
    if (!(intTree.root.getEntry() == 17
        && intTree.root.downLeft().getEntry() == 9
        && intTree.root.downLeft().downLeft() == null
        && intTree.root.downLeft().downRight() == null
        && intTree.root.downRight().getEntry() == 50
        && intTree.root.downRight().downLeft().getEntry() == 23
        && intTree.root.downRight().downRight().getEntry() == 76
        && intTree.root.downRight().downLeft().downLeft() == null
        && intTree.root.downRight().downLeft().downRight() == null)) {

      return false;
    }
    // right rotation
    intTree.rotate(intTree.root.downRight(), intTree.root);
    if (!(intTree.root.getEntry() == 50
        && intTree.root.downLeft().getEntry() == 17
        && intTree.root.downLeft().downLeft().getEntry() == 9
        && intTree.root.downLeft().downLeft().downLeft() == null
        && intTree.root.downLeft().downLeft().downRight() == null
        && intTree.root.downLeft().downRight().getEntry() == 23
        && intTree.root.downLeft().downRight().downLeft() == null
        && intTree.root.downLeft().downRight().downRight() == null
        && intTree.root.downRight().getEntry() == 76
        && intTree.root.downRight().downLeft() == null
        && intTree.root.downRight().downRight() == null)) {
      return false;
    }
    return true;
  }

  /**
   * Test performing rotations that include the root node, and some that do not.
   *
   * @return whether or not the test passes
   */
  public boolean test2() {
    // includes root
    BSTRotation<Integer> intTree = new BSTRotation<Integer>();
    intTree.add(100);
    intTree.add(34);
    intTree.add(18);
    intTree.add(46);
    intTree.add(152);
    // left rotation
    intTree.rotate(intTree.root.downLeft(), intTree.root);
    if (!(intTree.root.getEntry() == 34
        && intTree.root.downLeft().getEntry() == 18
        && intTree.root.downLeft().downLeft() == null
        && intTree.root.downLeft().downRight() == null
        && intTree.root.downRight().getEntry() == 100
        && intTree.root.downRight().downLeft().getEntry() == 46
        && intTree.root.downRight().downRight().getEntry() == 152
        && intTree.root.downRight().downLeft().downLeft() == null
        && intTree.root.downRight().downLeft().downRight() == null)) {

      return false;
    }
    
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
    if (!(intTree2.root.downLeft().getEntry() == 34)) {
      System.out.println("MOMOP");
      System.out.println(intTree2.root.downLeft().getEntry());
      return  false;
    }
    if((
       intTree2.root.downLeft().downLeft().getEntry() == 18
        && intTree2.root.downLeft().downLeft().downLeft() == null
        && intTree2.root.downLeft().downLeft().downRight() == null
        && intTree2.root.downLeft().downRight().getEntry() == 100
        && intTree2.root.downLeft().downRight().downLeft().getEntry() == 46
        && intTree2.root.downLeft().downRight().downRight().getEntry() == 101
        && intTree2.root.downLeft().downRight().downLeft().downLeft() == null
        && intTree2.root.downLeft().downRight().downLeft().downRight() == null)) {

      return false;
    }
 
    return true;
  }

  /**
   * Test performing rotations on parent-child pairs of nodes that have between them 0, 1, 2, and 3
   * shared children (that do not include the child being rotated).
   *
   * @return whether or not the test passes
   */
  public boolean test3() {
    return true;
  }
}
