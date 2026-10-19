public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

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
    if (this.root == newNode) {
      if (!newNode.isBlackNode()) {
        newNode.flipColor();
      }
      return;
    }

    RedBlackNode<T> parent = newNode.up();
    if (parent == null || parent.isBlackNode()) {
      return;
    }

    RedBlackNode<T> grandparent = parent.up();
    if (grandparent == null) {
      return;
    }

    RedBlackNode<T> aunt = parent.isRightChild() ? grandparent.downLeft() : grandparent.downRight();

    // Case 2B: red aunt
    if (aunt != null && !aunt.isBlackNode()) {
      parent.flipColor();
      aunt.flipColor();
      if (grandparent == this.root) {
        if (!grandparent.isBlackNode()) {
          grandparent.flipColor();
        }
      } else {
        grandparent.flipColor();
        ensureRedProperty(grandparent);
      }
      return;
    }

    // Case 2A: black aunt
    if (!parent.isRightChild()) {
      // Subcase: K right child of P left child of G, and S is black (Left-Right)
      if (newNode.isRightChild()) {
        this.rotate(newNode, parent);
        this.rotate(newNode, grandparent);
        newNode.flipColor();
        grandparent.flipColor();
      }
      // Subcase: K left child of P left child of G, and S is black (Left-Left)
      else {
        this.rotate(parent, grandparent);
        parent.flipColor();
        grandparent.flipColor();
      }
    } else {
      // Subcase: K left child of P right child of G, and S is black (Right-Left)
      if (!newNode.isRightChild()) {
        this.rotate(newNode, parent);
        this.rotate(newNode, grandparent);
        newNode.flipColor();
        grandparent.flipColor();
      }
      // Subcase: K right child of P right child of G, and S is black (Right-Right)
      else {
        this.rotate(parent, grandparent);
        parent.flipColor();
        grandparent.flipColor();
      }
    }
  }

  /*
   * Override the BSTRotation implementation, but also call ensureRedProperty to ensure the RB-tree red property
   * remains after insertion.
   *
   * @param data Data point to add to the tree
   */
  @Override
  public void add(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException("Null data! Throwing!");
    }
    RedBlackNode<T> newNode = new RedBlackNode<T>(data);
    if (this.root == null) {
      this.root = newNode;
      if (!newNode.isBlackNode()) {
        newNode.flipColor();
      }
      return;
    }
    addHelper(newNode, this.root);
    if (newNode.isBlackNode()) {
      newNode.flipColor();
    }
    ensureRedProperty(newNode);
  }
}
