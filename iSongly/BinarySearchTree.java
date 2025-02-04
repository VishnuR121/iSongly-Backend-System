/**
 * This class implements an instantiable binary search tree class.
 * 
 * @author Vishnu Rallapalli
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {
    // protected root variable
    protected BSTNode<T> root;

    /**
     * Performs the naive binary search tree insert algorithm to recursively
     * insert the provided newNode (which has already been initialized with a
     * data value) into the provided tree/subtree. When the provided subtree
     * is null, this method does nothing.
     */
    protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {
        if (subtree == null) {
            return;
        }

        // if value is less than or equal to current node then go to left subtree
        if (newNode.getData().compareTo(subtree.getData()) <= 0) {
            // if left subtree node is null then insert node in that spot
            if (subtree.getLeft() == null) {
                subtree.setLeft(newNode);
                newNode.setUp(subtree);
            } else {
                // if left subtree node isnt null then keeping going down the tree
                insertHelper(newNode, subtree.getLeft());
            }
            // if value is less than current node then go to right subtree
        } else {
            // if right subtree node is null then insert node in that spot
            if (subtree.getRight() == null) {
                subtree.setRight(newNode);
                newNode.setUp(subtree);
            } else {
                // if right subtree node isnt null then keeping going down the tree
                insertHelper(newNode, subtree.getRight());
            }
        }
    }

    /**
     * Inserts new data value into the tree.
     */
    @Override
    public void insert(T data) throws NullPointerException {
        BSTNode<T> newNode = new BSTNode<>(data);

        if (data == null) {
            throw new NullPointerException("Null data argument");
        }

        if (root == null) {
            root = newNode;
        } else {
            // recursively calls insetHelper() method to insert a new node
            insertHelper(newNode, root);
        }

        size();
    }

    /**
     * Check whether data is stored in the tree.
     * 
     * @param data the value to check for in the collection
     * @return true if the collection contains data one or more times,
     *         and false otherwise
     */
    @Override
    public boolean contains(Comparable<T> data) {
        BSTNode<T> curr = root;

        while (curr != null) {
            // if value is less than current node then go to left subtree
            if (data.compareTo(curr.getData()) < 0) {
                curr = curr.getLeft();
                // if value is less than current node then go to right subtree
            } else if (data.compareTo(curr.getData()) > 0) {
                curr = curr.getRight();
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * Counts the number of values in the collection, with each duplicate value
     * being counted separately within the value returned.
     * 
     * @return the number of values in the collection, including duplicates
     */
    @Override
    public int size() {
        return sizeHelper(root);
    }

    /**
     * Recursive helper method to calculate the size of the tree.
     * 
     * @param node the root of the subtree
     * @return the size of the subtree rooted at the given node
     */
    private int sizeHelper(BSTNode<T> node) {
        if (node == null) {
            return 0;
        }

        return 1 + sizeHelper(node.getLeft()) + sizeHelper(node.getRight());
    }

    /**
     * Checks if the collection is empty.
     * 
     * @return true if the collection contains 0 values, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Removes all values and duplicates from the collection.
     */
    @Override
    public void clear() {
        root = null;
    }

    /**
     * Main method to run tester cases and output results
     */
    public static void main(String[] args) {
        BinarySearchTree<Boolean> tree = new BinarySearchTree<>();

        // Run test1
        System.out.println("Test 1: " + (tree.test1P01() ? "PASS" : "FAIL"));

        // Run test2
        System.out.println("Test 2: " + (tree.test2P01() ? "PASS" : "FAIL"));

        // Run test3
        System.out.println("Test 3: " + (tree.test3P01() ? "PASS" : "FAIL"));
    }

    /**
     * Insertion tests. Inserting multiple values as both left and right children in
     * different orders to create differently shaped trees.
     */
    public boolean test1P01() {
        // integer tree test
        BinarySearchTree<Integer> treeInt1 = new BinarySearchTree<>();

        treeInt1.insert(30);
        treeInt1.insert(50);
        treeInt1.insert(60);
        treeInt1.insert(70);
        treeInt1.insert(80);

        if (treeInt1.root.getData() != 30) {
            return false;
        }

        if (treeInt1.root.getRight().getData() != 50) {
            return false;
        }

        if (treeInt1.root.getLeft() != null) {
            return false;
        }

        // integer tree test, different shape
        BinarySearchTree<Integer> treeInt2 = new BinarySearchTree<>();

        treeInt2.insert(50);
        treeInt2.insert(40);
        treeInt2.insert(30);
        treeInt2.insert(20);
        treeInt2.insert(10);

        if (treeInt2.root.getData() != 50) {
            return false;
        }

        if (treeInt2.root.getRight() != null) {
            return false;
        }

        if (treeInt2.root.getLeft().getData() != 40) {
            return false;
        }

        // integer tree test, different shape
        BinarySearchTree<Integer> treeInt3 = new BinarySearchTree<>();

        treeInt3.insert(50);
        treeInt3.insert(30);
        treeInt3.insert(70);
        treeInt3.insert(40);
        treeInt3.insert(60);
        treeInt3.insert(10);
        treeInt3.insert(55);
        treeInt3.insert(65);
        treeInt3.insert(20);

        if (treeInt3.root.getData() != 50) {
            return false;
        }

        if (treeInt3.root.getRight().getData() != 70) {
            return false;
        }

        if (treeInt3.root.getLeft().getData() != 30) {
            return false;
        }

        // string tree test
        BinarySearchTree<String> treeStr = new BinarySearchTree<>();

        treeStr.insert("orange");
        treeStr.insert("grape");
        treeStr.insert("plum");
        treeStr.insert("apple");
        treeStr.insert("pinapple");
        treeStr.insert("strawberry");
        treeStr.insert("watermelon");

        if (!(treeStr.root.getData().equals("orange"))) {
            return false;
        }

        if (!(treeStr.root.getRight().getData().equals("plum"))) {
            return false;
        }

        if (!(treeStr.root.getLeft().getData().equals("grape"))) {
            return false;
        }

        return true;
    }

    /**
     * Finding values tests. Finding values that are both left and right leaves as
     * well as values stored in the interior of a tree (including at the root
     * position).
     */
    public boolean test2P01() {
        // integer tree test
        BinarySearchTree<Integer> treeInt1 = new BinarySearchTree<>();

        treeInt1.insert(50);
        treeInt1.insert(30);
        treeInt1.insert(70);
        treeInt1.insert(40);
        treeInt1.insert(60);
        treeInt1.insert(10);
        treeInt1.insert(55);
        treeInt1.insert(65);
        treeInt1.insert(20);

        if (treeInt1.contains(50) != true) {
            return false;
        }

        if (treeInt1.contains(20) != true) {
            return false;
        }

        if (treeInt1.contains(55) != true) {
            return false;
        }

        if (treeInt1.contains(1) != false) {
            return false;
        }

        // integer tree test, different shape
        BinarySearchTree<Integer> treeInt2 = new BinarySearchTree<>();

        treeInt2.insert(40);
        treeInt2.insert(3);
        treeInt2.insert(46);
        treeInt2.insert(12);
        treeInt2.insert(60);

        if (treeInt2.contains(40) != true) {
            return false;
        }

        if (treeInt2.contains(12) != true) {
            return false;
        }

        if (treeInt2.contains(60) != true) {
            return false;
        }

        if (treeInt2.contains(43) != false) {
            return false;
        }

        // string tree test
        BinarySearchTree<String> treeStr = new BinarySearchTree<>();

        treeStr.insert("orange");
        treeStr.insert("grape");
        treeStr.insert("plum");
        treeStr.insert("apple");
        treeStr.insert("pinapple");
        treeStr.insert("strawberry");
        treeStr.insert("watermelon");

        if (treeStr.contains("orange") != true) {
            return false;
        }

        if (treeStr.contains("apple") != true) {
            return false;
        }

        if (treeStr.contains("watermelon") != true) {
            return false;
        }

        if (treeStr.contains("grapefruit") != false) {
            return false;
        }

        return true;
    }

    /**
     * Size and clear tests. Ensuring that the size and clear methods are working
     * through the building and clearing of a few different trees worth of data.
     */
    public boolean test3P01() {
        // integer tree test
        BinarySearchTree<Integer> treeInt = new BinarySearchTree<>();

        treeInt.insert(50);
        treeInt.insert(30);
        treeInt.insert(70);
        treeInt.insert(40);
        treeInt.insert(60);
        treeInt.insert(10);
        treeInt.insert(55);
        treeInt.insert(65);
        treeInt.insert(20);

        if (treeInt.size() != 9) {
            return false;
        }

        treeInt.insert(65);

        if (treeInt.size() != 10) {
            return false;
        }

        treeInt.clear();

        if (treeInt.size() != 0) {
            return false;
        }

        treeInt.insert(40);
        treeInt.insert(3);
        treeInt.insert(46);
        treeInt.insert(12);
        treeInt.insert(60);

        if (treeInt.size() != 5) {
            return false;
        }

        treeInt.clear();

        if (treeInt.size() != 0) {
            return false;
        }

        // string tree test
        BinarySearchTree<String> treeStr = new BinarySearchTree<>();

        treeStr.insert("orange");
        treeStr.insert("grape");
        treeStr.insert("plum");
        treeStr.insert("pinapple");
        treeStr.insert("apple");
        treeStr.insert("pears");
        treeStr.insert("watermelon");

        if (treeStr.size() != 7) {
            return false;
        }

        treeStr.clear();

        if (treeStr.size() != 0) {
            return false;
        }

        return true;
    }
}
