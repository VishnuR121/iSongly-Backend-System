import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * This class implements a Red-Black tree insertion and checks for validity.
 * 
 * @author Vishnu Rallapalli
 */
public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {
  /**
   * Checks if a new red node in the RedBlackTree causes a red property violation
   * by having a red parent. If this is not the case, the method terminates
   * without
   * making any changes to the tree. If a red property violation is detected, then
   * the method repairs this violation and any additional red property violations
   * that are generated as a result of the applied repair operation.
   * 
   * @param newRedNode a newly inserted red node, or a node turned red by previous
   *                   repair
   */
  protected void ensureRedProperty(RBTNode<T> newRedNode) {
    if (newRedNode.getUp() == null || !newRedNode.getUp().isRed()) {
      return; // No violation
    }

    RBTNode<T> parent = newRedNode.getUp();
    RBTNode<T> grandParent = parent.getUp();

    if (grandParent != null) {
      RBTNode<T> aunt = (parent.equals(grandParent.getLeft())) ? grandParent.getRight() : grandParent.getLeft();

      if (aunt != null && aunt.isRed()) {
        // Recoloring
        parent.flipColor();
        aunt.flipColor();
        grandParent.flipColor();
        ensureRedProperty(grandParent); // Recurse up the tree
      } else {
        if (parent.equals(grandParent.getLeft())) {
          if (newRedNode.equals(parent.getRight())) {
            // Case 2: Left rotation at parent
            rotate(newRedNode, parent);
            newRedNode = parent; // Update parent
            parent = newRedNode.getUp();
          }
          // Case 3: Right rotation at grandParent
          rotate(parent, grandParent);
        } else {
          if (newRedNode.equals(parent.getLeft())) {
            // Case 4: Right rotation at parent
            rotate(newRedNode, parent);
            newRedNode = parent; // Update parent
            parent = newRedNode.getUp();
          }
          // Case 5: Left rotation at grandParent
          rotate(parent, grandParent);
        }
        parent.flipColor();
        grandParent.flipColor();
      }
    } else {
      ((RBTNode<T>) this.root).isRed = false; // Ensure root remains black
    }
  }

  /**
   * Inserts new data value into the tree.
   * This method overrides the insert method inherited from BinarySearchTree.
   */
  @Override
  public void insert(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException("Null data argument");
    }

    RBTNode<T> newNode = new RBTNode<>((T) data); // Casting to T

    if (root == null) {
      root = newNode;
      ((RBTNode<T>) this.root).isRed = false;
    } else {
      insertHelper(newNode, root);
      ensureRedProperty(newNode);
      ((RBTNode<T>) this.root).isRed = false;
    }
  }

  /**
   * Tester method for cases where the aunt is red.
   */
  @Test
  public void testAuntRedCase() {
    RedBlackTree<Integer> tree = new RedBlackTree<>();

    // Case 1: Aunt is red
    tree.insert(30); // root
    tree.insert(20); // left child
    tree.insert(10); // left-left child (aunt of the new node)

    // Insert a new red node that causes a violation
    tree.insert(25); // should trigger ensureRedProperty for the left child

    RBTNode<Integer> root = (RBTNode<Integer>) tree.root;

    // Verify structure after insertion
    assertEquals(Integer.valueOf(20), root.getData(), "Root should balance to 20.");
    assertFalse(root.isRed, "Root should be black.");
    assertFalse(root.getRight().isRed, "Right child of root should be black.");
  }

  /**
   * Tester method for cases where the aunt is black.
   */
  @Test
  public void testAuntBlackCases() {
    RedBlackTree<Integer> tree = new RedBlackTree<>();

    // Case 2: Aunt is black (left-right relationship)
    tree.insert(30); // root
    tree.insert(20); // left child
    tree.insert(10); // left-left child (aunt)

    // Insert a new red node that causes a violation
    tree.insert(25); // should trigger ensureRedProperty for the left child

    RBTNode<Integer> root = (RBTNode<Integer>) tree.root;

    // Verify structure after left-right insertion
    assertEquals(Integer.valueOf(20), root.getData(), "Root should balance to 20.");
    assertFalse(root.isRed, "Root should be black.");

    // Reset and test right-left relationship
    tree = new RedBlackTree<>();
    tree.insert(30); // root
    tree.insert(50); // right child
    tree.insert(60); // right-right child (aunt)

    // Insert a new red node that causes a violation
    tree.insert(55); // should trigger ensureRedProperty for the right child

    // Verify structure after right-left insertion
    root = (RBTNode<Integer>) tree.root;

    assertEquals(Integer.valueOf(50), root.getData(), "Root should balance to 50.");
    assertFalse(root.isRed, "Root should be black.");
    assertTrue(root.getRight().getLeft().isRed, "Node '55' should be red.");
  }

  /**
   * Tester method from quiz 3, question 4.
   */
  @Test
  public void quizQuestionTester() {
    RedBlackTree<String> tree = new RedBlackTree<>();

    // Insert the nodes in the specified order
    tree.insert("L");
    tree.insert("F");
    tree.insert("R");
    tree.insert("D");
    tree.insert("I");
    tree.insert("P");
    tree.insert("W");
    tree.insert("H");
    tree.insert("J");

    // Now insert "V"
    tree.insert("V");

    // Perform assertions based on the expected state of the tree
    RBTNode<String> root = (RBTNode<String>) tree.root;

    // Root should still be black
    assertFalse(root.isRed, "Root should be black after insertion.");

    // Node "R" should be red (as specified in the example)
    assertTrue(root.getRight().isRed, "Node 'R' should be red.");

    // Node "V" should be red
    assertTrue(root.getRight().getRight().getLeft().isRed, "Node 'V' should be red.");
  }

  @Test
  public void sampleTest1() {
    RedBlackTree<Integer> tree = new RedBlackTree<>();

    tree.insert(34);
    tree.insert(63);
    tree.insert(37);

    assertEquals("[ 37(b), 34(r), 63(r) ]", tree.root.toLevelOrderString());
  }

  @Test
  public void sampleTest3() {
    // After inserting 32, 41, 57, 62, 79, 81, 93, 97 into an empty tree
    RedBlackTree<Integer> tree = new RedBlackTree<>();

    tree.insert(32);
    tree.insert(41);
    tree.insert(57);
    tree.insert(62);
    tree.insert(79);
    tree.insert(81);
    tree.insert(93);
    tree.insert(97);

    assertEquals("[ 62(b), 41(r), 81(r), 32(b), 57(b), 79(b), 93(b), 97(r) ]", tree.root.toLevelOrderString());
  }
}
