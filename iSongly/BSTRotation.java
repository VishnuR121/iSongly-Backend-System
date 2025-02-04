/**
 * This class implements the rotation operation in a BST.
 * 
 * @author Vishnu Rallapalli
 */
@SuppressWarnings("rawtypes")
public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {
    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this
     * method will either throw a NullPointerException: when either reference is
     * null, or otherwise will throw an IllegalArgumentException.
     *
     * @param child  is the node being rotated from child to parent position
     * @param parent is the node being rotated from parent to child position
     * @throws NullPointerException     when either passed argument is null
     * @throws IllegalArgumentException when the provided child and parent
     *                                  nodes are not initially (pre-rotation)
     *                                  related that way
     */
    protected void rotate(BSTNode<T> child, BSTNode<T> parent)
            throws NullPointerException, IllegalArgumentException {
        if ((child == null) || (parent == null)) {
            throw new NullPointerException();
        }

        // if child is left of parent node, do right rotation
        if (child.equals(parent.getLeft())) {
            rightRotationHelper(child, parent);
            // if child is left of parent node, do left rotation
        } else if (child.equals(parent.getRight())) {
            leftRotationHelper(child, parent);
        } else
            throw new IllegalArgumentException();
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation.
     */
    protected void rightRotationHelper(BSTNode<T> child, BSTNode<T> parent) {
        parent.setLeft(child.getRight());

        // set child node's right child as the parent node
        child.setRight(parent);

        // update grandparent link
        if (parent == root) {
            root = child;
        } else {
            if (parent.getUp().getLeft() == parent) {
                parent.getUp().setLeft(child);
            } else {
                parent.getUp().setRight(child);
            }
            child.setUp(parent.getUp());
        }

        // update parent
        parent.setUp(child);
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a right child of the provided parent, this method
     * will perform a left rotation.
     */
    protected void leftRotationHelper(BSTNode<T> child, BSTNode<T> parent) {
        parent.setRight(child.getLeft());

        // set child node's left child as the parent node
        child.setLeft(parent);

        // update grandparent link
        if (parent == root) {
            root = child;
        } else {
            if (parent.getUp().getLeft() == parent) {
                parent.getUp().setLeft(child);
            } else {
                parent.getUp().setRight(child);
            }
            child.setUp(parent.getUp());
        }

        // update parent
        parent.setUp(child);
    }

    /**
     * Main method to run tester cases and output results
     */
    public static void main(String[] args) {
        BSTRotation<Boolean> tree = new BSTRotation<>();

        // Run test1
        System.out.println("Test 1: " + (tree.test1() ? "PASS" : "FAIL"));

        // Run test2
        System.out.println("Test 2: " + (tree.test2() ? "PASS" : "FAIL"));

        // Run test3
        System.out.println("Test 3: " + (tree.test3() ? "PASS" : "FAIL"));
    }

    /**
     * Private helper method to quickly build hard-coded subtree in constructor.
     * This method create a new node containing data which is correctly linked
     * (with both parent and child references) with the left and right nodes.
     * 
     * @param data  is stored within the newly created node
     * @param left  is linked as the new node's left child
     * @param right is linked as the new node's right child
     * @return the newly created and linked node
     */
    private BSTNode<String> newTree(String data, BSTNode<String> left, BSTNode<String> right) {
        BSTNode<String> node = new BSTNode<>(data);
        node.setLeft(left);
        node.setRight(right);
        if (left != null)
            left.setUp(node);
        if (right != null)
            right.setUp(node);
        return node;
    }

    /**
     * Testing on performing both left and right rotations test.
     */
    @SuppressWarnings("unchecked")
    public boolean test1() {
        root = (BSTNode<T>) newTree("E",
                newTree("B",
                        new BSTNode("A"),
                        newTree("D",
                                new BSTNode("C"),
                                null)),
                newTree("H",
                        newTree("F",
                                null,
                                new BSTNode("G")),
                        newTree("J",
                                new BSTNode("I"),
                                new BSTNode("K"))));

        // right rotation
        BSTNode<T> parent = root.getLeft(); // Node "B"
        BSTNode<T> child = parent.getLeft(); // Node "A"
        rotate(child, parent);

        if ((!(root.getData().equals("E"))) || (!(root.getLeft().getData().equals("A")))
                || (!(root.getLeft().getRight().getData().equals("B")))) {
            return false;
        }

        // left rotation
        parent = root.getRight(); // Node "H"
        child = parent.getRight(); // Node "J"
        rotate(child, parent);

        if ((!(root.getData().equals("E"))) || (!(root.getRight().getData().equals("J")))
                || (!(root.getRight().getLeft().getData().equals("H")))) {
            return false;
        }

        return true;
    }

    /**
     * Tesing on performing rotations that include the root node, and some that do
     * not.
     */
    @SuppressWarnings("unchecked")
    public boolean test2() {
        root = (BSTNode<T>) newTree("E",
                newTree("B",
                        new BSTNode("A"),
                        newTree("D",
                                new BSTNode("C"),
                                null)),
                newTree("H",
                        newTree("F",
                                null,
                                new BSTNode("G")),
                        newTree("J",
                                new BSTNode("I"),
                                new BSTNode("K"))));

        // include root node rotation
        BSTNode<T> parent = root; // Node "E"
        BSTNode<T> child = root.getRight(); // Node "H"
        rotate(child, parent);

        if ((!(root.getData().equals("H"))) || (!(root.getRight().getData().equals("J")))
                || (!(root.getLeft().getData().equals("E")))) {
            return false;
        }

        return true;
    }

    /**
     * Testing on performing rotations on parent-child pairs of nodes that have
     * between them 0, 1, 2, and 3 shared children (that do not include the child
     * being rotated).
     */
    @SuppressWarnings("unchecked")
    public boolean test3() {
        root = (BSTNode<T>) newTree("E",
                newTree("B",
                        new BSTNode("A"),
                        newTree("D",
                                new BSTNode("C"),
                                null)),
                newTree("H",
                        newTree("F",
                                null,
                                new BSTNode("G")),
                        newTree("J",
                                new BSTNode("I"),
                                new BSTNode("K"))));

        // rotations on parent-child pairs of nodes that have between them 0, 1, 2, and
        // 3 shared children
        BSTNode<T> parent = root.getRight(); // Node "H"
        BSTNode<T> child = parent.getRight(); // Node "J"
        rotate(child, parent);

        if ((!(root.getData().equals("E"))) || (!(root.getRight().getData().equals("J")))
                || (!(root.getRight().getLeft().getData().equals("H")))) {
            return false;
        }

        if ((!(root.getRight().getRight().getData().equals("K")))
                || (!(root.getRight().getLeft().getData().equals("H")))
                || (!(root.getRight().getLeft().getRight().getData().equals("I")))
                || (!(root.getRight().getLeft().getLeft().getData().equals("F")))
                || (!(root.getRight().getLeft().getLeft().getRight().getData().equals("G")))) {
            return false;
        }

        root = (BSTNode<T>) newTree("E",
                newTree("B",
                        new BSTNode("A"),
                        newTree("D",
                                new BSTNode("C"),
                                null)),
                newTree("H",
                        newTree("F",
                                null,
                                new BSTNode("G")),
                        newTree("J",
                                new BSTNode("I"),
                                new BSTNode("K"))));

        parent = root.getLeft(); // Node "B"
        child = parent.getRight(); // Node "D"
        rotate(child, parent);

        if ((!(root.getData().equals("E"))) ||
                (!(root.getRight().getData().equals("H")))
                || (!(root.getLeft().getData().equals("D")))) {
            return false;
        }

        if ((!(root.getLeft().getLeft().getData().equals("B")))
                || (!((root.getLeft().getRight()) == null))
                || (!(root.getLeft().getLeft().getRight().getData().equals("C")))
                || (!(root.getLeft().getLeft().getLeft().getData().equals("A")))) {
            return false;
        }

        return true;
    }
}
