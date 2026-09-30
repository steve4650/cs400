package P104RedBlackTree;

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
    if (this.root == newNode && (!newNode.isBlackNode())) {
      newNode.flipColor();
      return;
    }

    RedBlackNode<T> p = newNode.up();
    RedBlackNode<T> g = p.up();

    if (p.isBlackNode()) {
      return;
    }

    RedBlackNode<T> aunt;
    if (p.isRightChild()) {
      aunt = p.downLeft();
    } else {
      aunt = p.downRight();
    }

    // red aunt
    if ((aunt != null) && (!aunt.isBlackNode())) {
      p.flipColor();
      aunt.flipColor();
      if (g != this.root) {
        g.flipColor();
      }
    }

    // else: black aunt
    /*
     * https://pages.cs.wisc.edu/~cs400/readings/Red-Black-Trees/
     */
    if (!p.isRightChild()) {
      // Case 2A: K right child of P left child of G, and S is black
      if (newNode.isRightChild()) {
        this.rotate(newNode, p);
        this.rotate(newNode, g);
        newNode.flipColor();
        g.flipColor();
      }
      // Case 2A: K left child of P left child of G, and S is black
      else {
        this.rotate(p, g);
        p.flipColor();
        g.flipColor();
      }
    } else if (p.isRightChild()) {
      // Case 2A: K left child of P right child of G, and S is black
      if (!newNode.isRightChild()) {
        this.rotate(newNode, p);
        this.rotate(newNode, g);
        newNode.flipColor();
        g.flipColor();
      }
      // Case 2A: K right child of P right child of G, and S is black
      else {
        this.rotate(p, g);
        p.flipColor();
        g.flipColor();
      }
    }
  }

  @Override
  public void add(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException("Null data! Throwing!");
    }
    RedBlackNode<T> newNode = new RedBlackNode<T>(data);
    if (this.root == null) {
      this.root = newNode;
      return;
    }
    addHelper(newNode, this.root);
    if (newNode.isBlackNode()) {
      newNode.flipColor();
    }
    ensureRedProperty(newNode);
    // TODO: set root to black?
  }
}
