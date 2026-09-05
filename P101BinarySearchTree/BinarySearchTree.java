package P101BinarySearchTree;

public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

  // This is the root note of the binary search tree implemented here.
  protected BinaryNode<T> root = null;

  public BinarySearchTree() {}

  public void add(T data) throws NullPointerException {
    BinaryNode<T> newNode = new BinaryNode<T>(data);
    if (this.root == null) {
      this.root = newNode;
      return;
    }
    addHelper(newNode, this.root);
  }

  /**
   * Performs the naive binary search tree insert algorithm to recursively insert the provided
   * newNode (which has already been initialized with a data value) into the provided tree/subtree.
   * When the provided subtree is null, this method does nothing.
   */
  protected void addHelper(BinaryNode<T> newNode, BinaryNode<T> subtree) {
    if (newNode.getEntry().compareTo(subtree.getEntry()) <= 0 && subtree.downLeft() == null) {
      subtree.setLeft(newNode);
      newNode.setUp(subtree);
      return;
    }
    if (newNode.getEntry().compareTo(subtree.getEntry()) <= 0) {
      addHelper(newNode, subtree.downLeft());
      return;
    }
    if (newNode.getEntry().compareTo(subtree.getEntry()) > 0 && subtree.downRight() == null) {
      subtree.setRight(newNode);
      newNode.setUp(subtree);
      return;
    }
    addHelper(newNode, subtree.downRight());
  }

  public boolean contains(Comparable<T> find) {
    return containsHelper(find, this.root);
  }

  /**
   * Helper method to check whether a particular is stored in a subtree defined by a BinaryNode object.
   *
   * @param find the value to check for in the collection
   * @param subtree the binary tree to search (represented by its root node, a BinaryNode, which may be null)
   * @return true if subtree contains data one or more times, and false otherwise
   */
  protected boolean containsHelper(Comparable<T> find, BinaryNode<T> subtree) {
    if (subtree == null) {
      return false;
    }
    int cmp = find.compareTo(subtree.getEntry());
    if (cmp == 0) {
      return true;
    }
    if (cmp < 0) {
      return containsHelper(find, subtree.downLeft());
    }
    return containsHelper(find, subtree.downRight());
  }

  public int size() {
    return sizeHelper(this.root);
  }

  /**
   * Helper method to find the size of a subtree defined by a BinaryNode object.
   *
   * @param subtree the binary tree to compute the size of (represented by its root node, a BinaryNode, which may be null)
   * @return the number of values in the subtree (counting duplicates)
   */
  protected int sizeHelper(BinaryNode<T> subtree) {
    if (subtree == null) {
      return 0;
    }
    return 1 + sizeHelper(subtree.downLeft()) + sizeHelper(subtree.downRight());
  }

  public boolean isEmpty() {
    return this.root == null;
  }

  public void clear() {
    if(this.root.downLeft() != null) {
      this.root.downLeft().setUp(null);
    }
    if(this.root.downRight() != null) {
      this.root.downRight().setUp(null);
    }
    this.root = null;
  }
}
