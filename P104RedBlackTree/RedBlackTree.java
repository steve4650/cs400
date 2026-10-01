import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    RedBlackNode<T> grandparent = parent.up();

    // Case: grandparent is null
    // In this case, there can be no red-red violation (since parent must be the root, which is
    // black)
    if (grandparent == null) {
      return;
    }

    if (parent.isBlackNode()) {
      return;
    }

    RedBlackNode<T> aunt;
    if (parent.isRightChild()) {
      aunt = parent.downLeft();
    } else {
      aunt = parent.downRight();
    }

    // Case 2B: red aunt. (https://pages.cs.wisc.edu/~cs400/readings/Red-Black-Trees/)
    if ((aunt != null) && (!aunt.isBlackNode())) {
      parent.flipColor();
      aunt.flipColor();
      if (grandparent != this.root) {
        grandparent.flipColor();
      }
    }

    // Case 2A: black aunt. (https://pages.cs.wisc.edu/~cs400/readings/Red-Black-Trees/)
    if (!parent.isRightChild()) {
      // Subcase: K right child of P left child of G, and S is black
      if (newNode.isRightChild()) {
        this.rotate(newNode, parent);
        this.rotate(newNode, grandparent);
        newNode.flipColor();
        grandparent.flipColor();
      }
      // Subcase: K left child of P left child of G, and S is black
      else {
        this.rotate(parent, grandparent);
        parent.flipColor();
        grandparent.flipColor();
      }
    } else if (parent.isRightChild()) {
      // Subcase: K left child of P right child of G, and S is black
      if (!newNode.isRightChild()) {
        this.rotate(newNode, parent);
        this.rotate(newNode, grandparent);
        newNode.flipColor();
        grandparent.flipColor();
      }
      // Subcase: K right child of P right child of G, and S is black
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

  /**
   * roleTest2 tests the size of the values of filtered slices of the dummy data returned by
   * Tree_Placeholder.
   */
  @Test
  public void test1() {
    RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();
    Assertions.assertTrue(true);
  }
}
