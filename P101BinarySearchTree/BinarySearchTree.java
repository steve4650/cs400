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
    // Case: new node belongs to the left, and current left node is null
    if (newNode.getEntry().compareTo(subtree.getEntry()) <= 0 && subtree.downLeft() == null) {
      subtree.setLeft(newNode);
      newNode.setUp(subtree);
      return;
    }
    // Case: new node belongs to the left, and current left node is not null. So we should recurse!
    if (newNode.getEntry().compareTo(subtree.getEntry()) <= 0) {
      addHelper(newNode, subtree.downLeft());
      return;
    }
    // Case: new node belongs to the right, and current right node is null
    if (newNode.getEntry().compareTo(subtree.getEntry()) > 0 && subtree.downRight() == null) {
      subtree.setRight(newNode);
      newNode.setUp(subtree);
      return;
    }
    // Case: new node belongs to the right, and current right node is not null. So we should
    // recurse!
    addHelper(newNode, subtree.downRight());
  }

  public boolean contains(Comparable<T> find) {
    return containsHelper(find, this.root);
  }

  /**
   * Helper method to check whether a particular is stored in a subtree defined by a BinaryNode
   * object.
   *
   * @param find the value to check for in the collection
   * @param subtree the binary tree to search (represented by its root node, a BinaryNode, which may
   *     be null)
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
    // else: cmd >= 0
    return containsHelper(find, subtree.downRight());
  }

  public int size() {
    return sizeHelper(this.root);
  }

  /**
   * Helper method to find the size of a subtree defined by a BinaryNode object.
   *
   * @param subtree the binary tree to compute the size of (represented by its root node, a
   *     BinaryNode, which may be null)
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
    if (this.root == null) {
      return;
    }
    if (this.root.downLeft() != null) {
      this.root.downLeft().setUp(null);
    }
    if (this.root.downRight() != null) {
      this.root.downRight().setUp(null);
    }
    this.root = null;
  }

  public static void main(String[] args) {
    BinarySearchTree<Integer> testTree1 = new BinarySearchTree<Integer>();
    boolean result = testTree1.test1();
    if (!result) {
      System.out.println("failed test1");
      return;
    }
    result = testTree1.test2();
    if (!result) {
      System.out.println("failed test2");
      return;
    }
    BinarySearchTree<String> testTree2 = new BinarySearchTree<String>();
    result = testTree2.test3();
    if (!result) {
      System.out.println("failed test3");
      return;
    }
    System.out.println("passed everything");
  }

  /**
   * Inserting multiple values as both left and right children in different orders to create
   * differently shaped trees
   *
   * @return whether or not the test passes
   */
  public boolean test1() {
    BinarySearchTree<Integer> intTree = new BinarySearchTree<Integer>();
    intTree.add(0);
    intTree.add(-1);
    intTree.add(-1);
    intTree.add(2);
    intTree.add(1);
    intTree.add(1);
    if (intTree.size() != 6) {
      return false;
    }
    if (intTree.root.getEntry() != 0) {
      return false;
    }
    if (intTree.root.downLeft().getEntry() != -1) {
      return false;
    }
    if (intTree.root.downLeft().downLeft().getEntry() != -1) {
      return false;
    }
    if (intTree.root.downLeft().downLeft().downLeft() != null) {
      return false;
    }
    if (intTree.root.downRight().getEntry() != 2) {
      return false;
    }
    if (intTree.root.downRight().downLeft().getEntry() != 1) {
      return false;
    }
    if (intTree.root.downRight().downLeft().downLeft().getEntry() != 1) {
      return false;
    }
    return true;
  }

  /**
   * finding values that are both left and right leaves as well as values stored in the interior of
   * a tree (including at the root position).
   *
   * @return whether or not the test passes
   */
  public boolean test2() {
    BinarySearchTree<Integer> intTree = new BinarySearchTree<Integer>();
    intTree.add(-1);
    intTree.add(1);
    intTree.add(-1);
    intTree.add(2);
    intTree.add(0);
    intTree.add(1);

    if (!(intTree.contains(0)
        || intTree.contains(1)
        || intTree.contains(2)
        || intTree.contains(-1))) {
      return false;
    }
    if (intTree.contains(-3) || intTree.contains(3) || intTree.contains(-2)) {
      return false;
    }
    return true;
  }

  /**
   * Ensuring that the size and clear methods are working through the building and clearing of a few
   * different trees worth of data.
   *
   * @return whether or not the test passes
   */
  public boolean test3() {
    BinarySearchTree<String> stringTree = new BinarySearchTree<String>();
    stringTree.add("bud");
    stringTree.add("bud");
    stringTree.add("mud");
    if (stringTree.size() != 3) {
      return false;
    }
    stringTree.clear();
    if (stringTree.size() != 0) {
      return false;
    }
    stringTree.add("bud");
    stringTree.add("bud");
    stringTree.add("mud");
    stringTree.add("mud");
    stringTree.add("mud");
    stringTree.add("zud");
    stringTree.add("zud");
    stringTree.add("zud");
    if (stringTree.size() != 8) {
      return false;
    }
    stringTree.clear();
    if (stringTree.size() != 0) {
      return false;
    }
    stringTree.add("mud");
    stringTree.add("mud");
    stringTree.add("mud");
    stringTree.add("mud");
    if (stringTree.size() != 4) {
      return false;
    }
    stringTree.clear();
    if (stringTree.size() != 0) {
      return false;
    }
    return true;
  }
}
