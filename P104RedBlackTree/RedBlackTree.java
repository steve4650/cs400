public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

  /** Explicit no-argument constructor. */
  public RedBlackTree() {
    super();
  }

  /**
   * Checks if a new red node in the RedBlackTree causes a red property violation by having a red
   * parent. If this is not the case, the method terminates without making any changes to the tree.
   * If a red property violation is detected, then the method repairs this violation and any
   * additional red property violations that are generated as a result of the applied repair
   * operation. Using this method might cause nodes with a value equal to the value of one of their
   * ancestors to appear within the left and the right subtree of that ancestor, even if the
   * original insertion procedure consistently inserts such nodes into only the left or the right
   * subtree. But it will preserve the ordering of nodes within the tree.
   *
   * @param newNode a newly inserted red node, or a node turned red by previous repair
   */
  protected void ensureRedProperty(RedBlackNode<T> newNode) {
    if (newNode == null) {
      return;
    }

    RedBlackNode<T> parent = newNode.up();
    // If there is no parent or the parent is black, there is no red property violation.
    if (parent == null || parent.isBlackNode()) {
      return;
    }

    // Since parent is red, parent cannot be the root (root is always black).
    // Thus, grandparent must exist.
    RedBlackNode<T> grandparent = parent.up();
    if (grandparent == null) {
      return;
    }

    // Determine the aunt (sibling of parent)
    boolean isParentRightChild = parent.isRightChild();
    RedBlackNode<T> aunt = isParentRightChild ? grandparent.downLeft() : grandparent.downRight();

    // Case 1: Aunt is RED
    if (aunt != null && !aunt.isBlackNode()) {
      parent.isBlackNode = true;
      aunt.isBlackNode = true;
      grandparent.isBlackNode = false;
      // Recurse on grandparent to repair potential red property violation higher up
      ensureRedProperty(grandparent);
    } else {
      // Case 2: Aunt is BLACK (or null)
      boolean isNodeRightChild = newNode.isRightChild();

      if (!isParentRightChild) {
        // Parent is left child of grandparent
        if (isNodeRightChild) {
          // Case 2a: Left-Right (LR) - double rotation
          rotate(newNode, parent);
          rotate(newNode, grandparent);
          newNode.isBlackNode = true;
          grandparent.isBlackNode = false;
        } else {
          // Case 2b: Left-Left (LL) - single rotation
          rotate(parent, grandparent);
          parent.isBlackNode = true;
          grandparent.isBlackNode = false;
        }
      } else {
        // Parent is right child of grandparent
        if (!isNodeRightChild) {
          // Case 2c: Right-Left (RL) - double rotation
          rotate(newNode, parent);
          rotate(newNode, grandparent);
          newNode.isBlackNode = true;
          grandparent.isBlackNode = false;
        } else {
          // Case 2d: Right-Right (RR) - single rotation
          rotate(parent, grandparent);
          parent.isBlackNode = true;
          grandparent.isBlackNode = false;
        }
      }
    }
  }

  /**
   * Overrides the add method inherited from BinarySearchTree. Inserts a new element into the
   * Red-Black Tree while maintaining Red-Black Tree properties.
   *
   * @param data the item to be inserted into the tree
   * @throws NullPointerException if data is null
   */
  @Override
  public void add(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException("Cannot insert null data into RedBlackTree.");
    }

    RedBlackNode<T> newNode = new RedBlackNode<>(data);

    if (this.root == null) {
      this.root = newNode;
    } else {
      addHelper(newNode, this.root);
      ensureRedProperty(newNode);
    }

    // Ensure that the root node is always black after insertion and repairs
    ((RedBlackNode<T>) this.root).isBlackNode = true;
  }
}
