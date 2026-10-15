import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it stores in
 * sorted, ascending order.
 */
public class RBTreeIterable<T extends Comparable<T>> extends RedBlackTree<T>
    implements IterableSortedCollection<T> {

  protected Comparable<T> maximum = null;

  protected Comparable<T> minimum = null;

  /**
   * Allows setting the start (minimum) value of the iterator. When this method is called, every
   * iterator created after it will use the minimum set by this method until this method is called
   * again to set a new minimum value.
   *
   * @param min the minimum for iterators created for this tree, or null for no minimum
   */
  public void setIteratorMin(Comparable<T> min) {
    this.minimum = min;
  }

  /**
   * Allows setting the stop (maximum) value of the iterator. When this method is called, every
   * iterator created after it will use the maximum set by this method until this method is called
   * again to set a new maximum value.
   *
   * @param max the maximum for iterators created for this tree, or null for no maximum
   */
  public void setIteratorMax(Comparable<T> max) {
    this.maximum = max;
  }

  /**
   * Returns an iterator over the values stored in this tree. The iterator uses the start (minimum)
   * value set by a previous call to setIteratorMin, and the stop (maximum) value set by a previous
   * call to setIteratorMax. If setIteratorMin has not been called before, or if it was called with
   * a null argument, the iterator uses no minimum value and starts with the lowest value that
   * exists in the tree. If setIteratorMax has not been called before, or if it was called with a
   * null argument, the iterator uses no maximum value and finishes with the highest value that
   * exists in the tree.
   */
  public Iterator<T> iterator() {
    return new TreeIterator<T>(this.root, this.minimum, this.maximum);
  }

  /**
   * Nested class for Iterator objects created for this tree and returned by the iterator method.
   * This iterator follows an in-order traversal of the tree and returns the values in sorted,
   * ascending order.
   */
  protected static class TreeIterator<R extends Comparable<R>> implements Iterator<R> {

    // stores the start point (minimum) for the iterator
    Comparable<R> start = null;
    // stores the stop point (maximum) for the iterator
    Comparable<R> stop = null;
    // stores the stack that keeps track of the inorder traversal
    Stack<BinaryNode<R>> stack = null;

    /**
     * Constructor for a new iterator if the tree with root as its root node, and min as the start
     * (minimum) value (or null if no start value) and max as the stop (maximum) value (or null if
     * no stop value) of the new iterator. Time complexity should be O(log n).
     *
     * @param root root node of the tree to traverse
     * @param min the minimum value that the iterator will return
     * @param max the maximum value that the iterator will return
     */
    public TreeIterator(BinaryNode<R> root, Comparable<R> min, Comparable<R> max) {
      this.start = min;
      this.stop = max;
      this.stack = new Stack<BinaryNode<R>>();
      this.buildStack(root);
    }

    /**
     * Helper method for initializing and updating the stack. This method both - finds the next data
     * value stored in the tree (or subtree) that is between start(minimum) and stop(maximum) point
     * (including start and stop points themselves), and - builds up the stack of ancestor nodes
     * that contain values between start(minimum) and stop(maximum) values (including start and stop
     * values themselves) so that those nodes can be visited in the future.
     *
     * @param node the root node of the subtree to process
     */
    private void buildStack(BinaryNode<R> node) {
      // Base case
      if (node == null) {
        return;
      }
      // Recursive case 1
      if (this.start != null && this.start.compareTo(node.getEntry()) > 0) {
        this.buildStack(node.downRight());
        return;
      }
      // Else: recursive case 2
      this.stack.push(node);
      this.buildStack(node.downLeft());
    }

    /** Returns true if the iterator has another value to return, and false otherwise. */
    public boolean hasNext() {
      try {
        // Throws exception if empty
        BinaryNode<R> successor = this.stack.peek();
        if (this.stop == null) {
          return true;
        }
        return this.stop.compareTo(successor.getEntry()) >= 0;
      } catch (EmptyStackException e) {
        return false;
      }
    }

    /**
     * Returns the next value of the iterator. Amortized time complexity should be O(1). Worst case
     * time complexity should be O(log n). Do not implement this method by linearly walking through
     * the entire tree from the smallest element until the start bound is reached. That process
     * should occur only once during construction of the iterator object.
     *
     * @throws NoSuchElementException if the iterator has no more values to return
     */
    public R next() {
      try {
        BinaryNode<R> successor = this.stack.peek();

        if (this.stop != null && this.stop.compareTo(successor.getEntry()) < 0) {
          throw new NoSuchElementException();
        }

        successor = this.stack.pop();
        if (successor.downRight() != null) {
          this.buildStack(successor.downRight());
        }
        return successor.getEntry();
      } catch (EmptyStackException e) {
        throw new NoSuchElementException();
      }
    }
  }

  /*
   * Test integers, duplicates, and no start/stop points
   */
  @Test
  public void testIter1() {
    RBTreeIterable<Integer> tree = new RBTreeIterable<>();
    tree.add(5);
    tree.add(3);
    tree.add(3);
    tree.add(1);

    tree.setIteratorMin(null);
    tree.setIteratorMax(null);

    Iterator<Integer> iter = tree.iterator();
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals(1, iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals(3, iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals(3, iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals(5, iter.next());
    Assertions.assertFalse(iter.hasNext());
  }

  /*
   * Tests strings, no duplicates, start points with no stop points, and stop points with no start
   * points
   */
  @Test
  public void testIter2() {
    RBTreeIterable<String> tree = new RBTreeIterable<>();
    tree.add("andromeda");
    tree.add("zulu");
    tree.add("tomato");
    tree.add("iceberg");
    tree.add("fortune");

    // start point, no stop point
    tree.setIteratorMin("barbados");
    tree.setIteratorMax(null);

    Iterator<String> iter = tree.iterator();
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("fortune", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("iceberg", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("tomato", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("zulu", iter.next());
    Assertions.assertFalse(iter.hasNext());

    // stop point, no start point
    tree.setIteratorMin(null);
    tree.setIteratorMax("andromeda");

    iter = tree.iterator();
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("andromeda", iter.next());
    Assertions.assertFalse(iter.hasNext());
  }

  /*
   * Tests strings, duplicates, and start and stop points
   */
  @Test
  public void testIter3() {
    RBTreeIterable<String> tree = new RBTreeIterable<>();
    tree.add("borealis");
    tree.add("pumpkin");
    tree.add("pumpkin");
    tree.add("fish");
    tree.add("fish");

    tree.setIteratorMin("borealis");
    tree.setIteratorMax("pumpkin");

    Iterator<String> iter = tree.iterator();
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("borealis", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("fish", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("fish", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("pumpkin", iter.next());
    Assertions.assertTrue(iter.hasNext());
    Assertions.assertEquals("pumpkin", iter.next());
    Assertions.assertFalse(iter.hasNext());
  }
}
