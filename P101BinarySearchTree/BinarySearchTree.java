// TODO remove do not add any package statements to any of your java files
package P101BinarySearchTree;

class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

  protected BinaryNode<T> root = null;

  public BinarySearchTree() {}

  public BinarySearchTree(T rootData) {
    this.root = new BinaryNode<T>(rootData);
  }

  @Override
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
      return;
    }
    if (newNode.getEntry().compareTo(subtree.getEntry()) <= 0) {
      addHelper(newNode, subtree.downLeft());
      return;
    }
    if (newNode.getEntry().compareTo(subtree.getEntry()) > 0 && subtree.downRight() == null) {
      subtree.setRight(newNode);
      return;
    }
    addHelper(newNode, subtree.downRight());
  }

  @Override
  public boolean contains(Comparable<T> find) {
    return containsHelper(find, this.root);
  }

  protected boolean containsHelper(Comparable<T> find, BinaryNode<T> subtree) {
    if (subtree.getEntry() == null) {
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

  @Override
  public int size() {
    return sizeHelper(this.root);
  }

  protected int sizeHelper(BinaryNode<T> subtree) {
    if (subtree.getEntry() == null) {
      return 0;
    }
    return 1 + sizeHelper(subtree.downLeft()) + sizeHelper(subtree.downRight());
  }

  @Override
  public boolean isEmpty() {
    return this.root.getEntry() == null;
  }

  @Override
  public void clear() {
    this.root = null;
  }
}
