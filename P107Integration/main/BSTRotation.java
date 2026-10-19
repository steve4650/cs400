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
    // Update grandparent <--> child link
    BinaryNode<T> grandparent = parent.up();
    if (grandparent != null) {
      if (grandparent.downLeft() == parent) {
        grandparent.setLeft(child);
      } else {
        grandparent.setRight(child);
      }
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
      child.setUp(grandparent);
      child.setRight(parent);
      parent.setUp(child);
      parent.setLeft(b);
      if (b != null) {
        b.setUp(parent);
      }
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
      child.setUp(grandparent);
      child.setLeft(parent);
      parent.setUp(child);
      parent.setRight(b);
      if (b != null) {
        b.setUp(parent);
      }
    }
  }
}
