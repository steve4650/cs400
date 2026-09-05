package P101BinarySearchTree;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;

public class Main {
  public static void main(String[] args) {
    testClassRequirements();
    testEmptyTree();
    testSingleValue();
    testInsertionAndContains();
    testDuplicateValues();
    testTreeStructureAndOrdering();
    testNullSubtreeHelper();
    testClearAndReuse();
    testClearEmptyTree();
    System.out.println("All BinarySearchTree tests passed.");
  }

  private static void testClassRequirements() {
    Class<?> treeClass = BinarySearchTree.class;
    check(Modifier.isPublic(treeClass.getModifiers()), "BinarySearchTree should be public");
    check(
        !Modifier.isAbstract(treeClass.getModifiers()), "BinarySearchTree should be instantiable");
    check(
        SortedCollection.class.isAssignableFrom(treeClass),
        "BinarySearchTree should implement SortedCollection");
    TypeVariable<?> typeParameter = treeClass.getTypeParameters()[0];

    try {
      Constructor<?> constructor = treeClass.getConstructor();
      check(
          Modifier.isPublic(constructor.getModifiers()),
          "BinarySearchTree should have a public no-argument constructor");

      Field[] fields = treeClass.getDeclaredFields();
      check(fields.length == 1, "BinarySearchTree should only declare the root field");
      check(fields[0].getName().equals("root"), "the tree field should be named root");
      check(Modifier.isProtected(fields[0].getModifiers()), "root should be protected");
      check(fields[0].getType().equals(BinaryNode.class), "root should have BinaryNode type");
      check(
          !Modifier.isPrivate(fields[0].getModifiers()),
          "BinarySearchTree should not have private fields");

      Method helper = treeClass.getDeclaredMethod("addHelper", BinaryNode.class, BinaryNode.class);
      check(Modifier.isProtected(helper.getModifiers()), "addHelper should be protected");
      check(helper.getReturnType().equals(Void.TYPE), "addHelper should return void");
    } catch (ReflectiveOperationException exception) {
      throw new AssertionError(
          "required BinarySearchTree constructor or helper is missing", exception);
    }

    check(
        new BinarySearchTree<Integer>().root == null,
        "root should be null immediately after construction");
  }

  private static void testEmptyTree() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    check(tree.isEmpty(), "new tree should be empty");
    check(tree.size() == 0, "new tree should have size 0");
    check(!tree.contains(42), "empty tree should not contain a value");
  }

  private static void testSingleValue() {
    BinarySearchTree<String> tree = new BinarySearchTree<String>();
    tree.add("middle");
    check(!tree.isEmpty(), "tree should not be empty after an insertion");
    check(tree.size() == 1, "single insertion should produce size 1");
    check(tree.contains("middle"), "tree should contain its inserted value");
    check(!tree.contains("missing"), "tree should not contain an uninserted value");
  }

  private static void testInsertionAndContains() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    int[] values = {50, 25, 75, 10, 30, 60, 90, 5, 15, 80, 100};
    for (int value : values) {
      tree.add(value);
    }

    check(tree.size() == values.length, "size should count every inserted value");
    for (int value : values) {
      check(tree.contains(value), "tree should contain inserted value " + value);
    }
    int[] missingValues = {-1, 0, 20, 55, 70, 95, 101};
    for (int value : missingValues) {
      check(!tree.contains(value), "tree should not contain missing value " + value);
    }
  }

  private static void testDuplicateValues() {
    BinarySearchTree<String> tree = new BinarySearchTree<String>();
    tree.add("same");
    tree.add("same");
    tree.add("same");
    tree.add("before");
    tree.add("after");

    check(tree.size() == 5, "duplicates should each count toward size");
    check(tree.contains("same"), "tree should contain a duplicated value");
    check(tree.contains("before"), "tree should contain a value less than the root");
    check(tree.contains("after"), "tree should contain a value greater than the root");
  }

  private static void testTreeStructureAndOrdering() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    int[] values = {50, 25, 75, 25, 10, 30, 60, 90};
    for (int value : values) {
      tree.add(value);
    }

    check(tree.root.getEntry() == 50, "first inserted value should be the root");
    check(tree.root.downLeft().getEntry() == 25, "smaller values should be in the left subtree");
    check(tree.root.downRight().getEntry() == 75, "larger values should be in the right subtree");
    check(
        tree.root.downLeft().downLeft().getEntry() == 25,
        "duplicate values should be stored in the left subtree");
    check(tree.root.downLeft().up() == tree.root, "left child should reference its parent");
    check(tree.root.downRight().up() == tree.root, "right child should reference its parent");
    check(
        tree.root.downLeft().downLeft().up() == tree.root.downLeft(),
        "duplicate node should reference its parent");
    check(
        tree.root.downLeft().downLeft().downRight() == null,
        "new nodes should retain null child references");
    check(
        tree.root.toInOrderString().equals("[ 10, 25, 25, 30, 50, 60, 75, 90 ]"),
        "in-order traversal should be sorted and include duplicates");
  }

  private static void testNullSubtreeHelper() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
  }

  private static void testClearAndReuse() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    tree.add(2);
    tree.add(1);
    tree.add(3);
    tree.clear();

    check(tree.isEmpty(), "clear should make the tree empty");
    check(tree.size() == 0, "clear should reset the size to 0");
    check(!tree.contains(2), "cleared tree should not contain old values");

    tree.add(99);
    check(tree.size() == 1, "tree should accept values after clear");
    check(tree.contains(99), "tree should contain a value added after clear");
  }

  private static void testClearEmptyTree() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    tree.clear();
    check(tree.isEmpty(), "clearing an empty tree should keep it empty");
    check(tree.root == null, "clearing an empty tree should keep root null");
  }

  private static void checkThrowsNullPointer(Runnable action, String message) {
    try {
      action.run();
    } catch (NullPointerException exception) {
      return;
    }
    throw new AssertionError(message);
  }

  private static void check(boolean condition, String message) {
    if (!condition) {
      throw new AssertionError(message);
    }
  }
}
