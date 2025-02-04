import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the
 * values it stores in sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>>
        extends RedBlackTree<T> implements IterableSortedCollection<T> {
    // private max and min iterator variables
    private Comparable<T> iteratorMin = null;
    private Comparable<T> iteratorMax = null;

    /**
     * Allows setting the start (minimum) value of the iterator. When this method is
     * called,
     * every iterator created after it will use the minimum set by this method until
     * this method
     * is called again to set a new minimum value.
     * 
     * @param min the minimum for iterators created for this tree, or null for no
     *            minimum
     */
    public void setIteratorMin(Comparable<T> min) {
        this.iteratorMin = min; // store min parameter into iteratorMin variable
    }

    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is
     * called,
     * every iterator created after it will use the maximum set by this method until
     * this method
     * is called again to set a new maximum value.
     * 
     * @param min the maximum for iterators created for this tree, or null for no
     *            maximum
     */
    public void setIteratorMax(Comparable<T> max) {
        this.iteratorMax = max; // store max parameter into iteratorMax variable
    }

    /**
     * Returns an iterator over the values stored in this tree. The iterator uses
     * the
     * start (minimum) value set by a previous call to setIteratorMin, and the stop
     * (maximum)
     * value set by a previous call to setIteratorMax. If setIteratorMin has not
     * been called
     * before, or if it was called with a null argument, the iterator uses no
     * minimum value
     * and starts with the lowest value that exists in the tree. If setIteratorMax
     * has not been
     * called before, or if it was called with a null argument, the iterator uses no
     * maximum
     * value and finishes with the highest value that exists in the tree.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Iterator<T> iterator() {
        return new RBTIterator(this.root, iteratorMin, iteratorMax); // create new instance of RBTIterator class by
                                                                     // passing
        // iterator max and min values
    }

    /**
     * Nested class for Iterator objects created for this tree and returned by the
     * iterator method.
     * This iterator follows an in-order traversal of the tree and returns the
     * values in sorted,
     * ascending order.
     */
    protected static class RBTIterator<R> implements Iterator<R> {

        // stores the start point (minimum) for the iterator
        Comparable<R> min = null;
        // stores the stop point (maximum) for the iterator
        Comparable<R> max = null;
        // stores the stack that keeps track of the inorder traversal
        Stack<BSTNode<R>> stack = null;

        /**
         * Constructor for a new iterator if the tree with root as its root node, and
         * min as the start (minimum) value (or null if no start value) and max as the
         * stop (maximum) value (or null if no stop value) of the new iterator.
         * 
         * @param root root node of the tree to traverse
         * @param min  the minimum value that the iterator will return
         * @param max  the maximum value that the iterator will return
         */
        public RBTIterator(BSTNode<R> root, Comparable<R> min, Comparable<R> max) {
            this.min = min;
            this.max = max;
            this.stack = new Stack<>();
            buildStackHelper(root);
        }

        /**
         * Helper method for initializing and updating the stack. This method both
         * - finds the next data value stored in the tree (or subtree) that is bigger
         * than or equal to the specified start point (maximum), and
         * - builds up the stack of ancestor nodes that contain values larger than or
         * equal to the start point so that those nodes can be visited in the future.
         * 
         * @param node the root node of the subtree to process
         */
        private void buildStackHelper(BSTNode<R> node) {
            // base case
            if (node == null) {
                return;
            }

            // recursive case
            if (min == null || min.compareTo(node.getData()) <= 0) {
                stack.push(node);
                buildStackHelper(node.getLeft());
            } else {
                buildStackHelper(node.getRight());
            }
        }

        /**
         * Returns true if the iterator has another value to return, and false
         * otherwise.
         */
        public boolean hasNext() {
            // ensure stack is not empty and the top node's value is within the max bound
            while (!stack.isEmpty()) {
                BSTNode<R> nextNode = stack.peek();
                if (max == null || max.compareTo(nextNode.getData()) >= 0) {
                    return true;
                } else {
                    // if top node is out of the max bound, pop it
                    stack.pop();
                }
            }

            return false;
        }

        /**
         * Returns the next value of the iterator.
         * 
         * @throws NoSuchElementException if the iterator has no more values to return
         */
        public R next() {
            // if iterator is empry then throw exception
            if (!hasNext()) {
                throw new NoSuchElementException(
                        "no more nodes to visit with values smaller than or equal to the set stop point (maximum).");
            }

            // pop the next node to return its data
            BSTNode<R> nextNode = stack.pop();
            R nextValue = nextNode.getData();

            // build the stack with the right subtree of the current node
            buildStackHelper(nextNode.getRight());

            return nextValue;
        }
    }

    /**
     * Tests iterator with no start or stop points specified.
     */
    @Test
    public void iteratorWithoutStartStopPoint() {
        // Create tree of type Integer
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();

        // Insert values into tree
        tree.insert(5);
        tree.insert(10);
        tree.insert(15);
        tree.insert(20);
        tree.insert(25);
        tree.insert(30);
        tree.insert(35);
        tree.insert(40);

        // Create an iterator with no specified start or stop point
        Iterator<Integer> iterator = tree.iterator();

        // Verify the values returned by the iterator
        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.next());
        assertEquals(10, iterator.next());
        assertEquals(15, iterator.next());
        assertEquals(20, iterator.next());
        assertEquals(25, iterator.next());
        assertEquals(30, iterator.next());
        assertEquals(35, iterator.next());
        assertEquals(40, iterator.next());

        // Ensure the iterator has no more elements
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests iterator with both a specified start point and a specified stop point.
     */
    @Test
    public void iteratorWithStartStopPoint() {
        // Create tree of type Integer
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();

        // Insert values into tree
        tree.insert(5);
        tree.insert(10);
        tree.insert(15);
        tree.insert(20);
        tree.insert(25);
        tree.insert(30);
        tree.insert(35);
        tree.insert(40);

        // Set the start (minimum) and stop (maximum) points for the iterator
        tree.setIteratorMin(10);
        tree.setIteratorMax(30);

        // Create an iterator with the specified start and stop points
        Iterator<Integer> iterator = tree.iterator();

        // Verify that the iterator only returns values within the start-stop range
        assertTrue(iterator.hasNext());
        assertEquals(10, iterator.next());
        assertEquals(15, iterator.next());
        assertEquals(20, iterator.next());
        assertEquals(25, iterator.next());
        assertEquals(30, iterator.next());

        // Ensure the iterator has no more elements within the stop point
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests the iterator containing duplicate values.
     */
    @Test
    public void iteratorDuplicates() {
        // Create tree of type String
        IterableRedBlackTree<String> tree = new IterableRedBlackTree<>();

        // Insert values into tree
        tree.insert("peach");
        tree.insert("strawberry");
        tree.insert("melon");
        tree.insert("blackberry");
        tree.insert("orange");
        tree.insert("apple");
        tree.insert("banana");
        tree.insert("peach");

        // Create an iterator with no specified start or stop point
        Iterator<String> iterator = tree.iterator();

        // Verify the order of returned values, including the duplicate
        assertTrue(iterator.hasNext());
        assertEquals("apple", iterator.next());
        assertEquals("banana", iterator.next());
        assertEquals("blackberry", iterator.next());
        assertEquals("melon", iterator.next());
        assertEquals("orange", iterator.next());
        assertEquals("peach", iterator.next());
        assertEquals("peach", iterator.next()); // Duplicate value
        assertEquals("strawberry", iterator.next());

        // Ensure the iterator has no more elements
        assertFalse(iterator.hasNext());
    }
}