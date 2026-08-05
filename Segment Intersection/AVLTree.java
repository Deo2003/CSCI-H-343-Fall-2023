import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * TODO: This is your second major task.
 * <p>
 * This class implements a height-balanced binary search tree,
 * using the AVL algorithm. Beyond the constructor, only the insert()
 * and remove() methods need to be implemented. All other methods are unchanged.
 */

public class AVLTree<K> extends BinarySearchTree<K> {

    /**
     * Creates an empty AVL tree as a BST organized according to the
     * lessThan predicate.
     */
    public AVLTree(BiPredicate<K, K> lessThan) {
        super(lessThan);
    }

    public boolean isAVL() {
        if (root == null)
            return true;
        else
            return root.isAVL();
    }

    /**
     * TODO
     * Inserts the given key into this AVL tree such that the ordering
     * property for a BST and the balancing property for an AVL tree are
     * maintained.
     */
    @Override
    public Node<K> insert(K key) {
        Node<K> newNode = super.insert(key);
        balance(newNode);
        return newNode;
    }

    /**
     * TODO
     * <p>
     * Removes the key from this BST. If the key is not in the tree,
     * nothing happens.
     */

    @Override
    public void remove(K key) {
        Node<K> node = search(key);
        if(node != null) {
            Node<K> parent = node.parent;
            super.remove(key);
            if(parent != null) {
                balance(parent);
            } else {
                balance(root);
            }
        }
    }

    private void balance(Node<K> node) {
        while (node != null) {
            node.updateHeight(); // Update the height of the current node

            if (get_height(node.left) - get_height(node.right) > 1) { // Left-heavy
                if (get_height(node.left.left) < get_height(node.left.right)) {
                    left_rotate(node.left); // Double rotation
                }
                right_rotate(node); // Single rotation
            } else if (get_height(node.right) - get_height(node.left) > 1) { // Right-heavy
                if (get_height(node.right.right) < get_height(node.right.left)) {
                    right_rotate(node.right); // Double rotation
                }
                left_rotate(node); // Single rotation
            }

            node = node.parent; // Move to the parent
        }
    }


    private void left_rotate(Node<K> x) {
        Node<K> y = x.right;
        x.right = y.left;

        if (y.left != null) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;

        x.updateHeight();
        y.updateHeight();
    }

    private void right_rotate(Node<K> y) {
        Node<K> x = y.left;
        y.left = x.right;

        if (x.right != null) {
            x.right.parent = y;
        }

        x.parent = y.parent;

        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }

        x.right = y;
        y.parent = x;

        y.updateHeight();
        x.updateHeight();
    }

}