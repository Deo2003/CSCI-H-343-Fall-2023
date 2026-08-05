import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * TODO: This is your first major task.
 * <p>
 * This class implements a generic unbalanced binary search tree (BST).
 */

public class BinarySearchTree<K> implements OrderedSet<K> {

    /**
     * A Node<K> is a Location (defined in OrderedSet.java), which
     * means that it can be the return value of a search on the tree.
     */

    static class Node<K> implements Location<K> {

        protected K data;
        protected Node<K> left, right;
        protected Node<K> parent;
        protected int height;

        /**
         * Constructs a leaf Node<K> with the given key.
         */
        public Node(K key) {
            this(key, null, null);
        }

        /**
         * TODO
         * <p>
         * Constructs a new Node<K> with the given values for fields.
         */
        public Node(K data, Node<K> left, Node<K> right) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.parent = null;
            this.height = (left != null || right != null) ? 1 : 0;
        }


        /*
         * Provide the get() method required by the Location interface.
         */
        @Override
        public K get() {
            return data;
        }

        /**
         * Return true iff this Node<K> is a leaf in the tree.
         */
        protected boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * TODO
         * <p>
         * Performs a local update on the height of this Node<K>. Assumes that the
         * heights in the child Node<K>s are correct. Returns true iff the height
         * actually changed. This function *must* run in O(1) time.
         */
        protected boolean updateHeight() {
            int oldHeight = height;
            height = 1 + Math.max(get_height(left), get_height(right));
            return height != oldHeight;
        }


        /**
         * TODO
         * <p>
         * Returns the location of the Node<K> containing the inorder predecessor
         * of this Node<K>.
         */
        @Override
        public Location<K> previous() {
            if (left != null) {
                Node<K> p = left;
                while (p.right != null) {
                    p = p.right;
                }
                return p;
            } else {
                Node<K> p = this;
                while (p.parent != null && p.parent.left == p) {
                    p = p.parent;
                }
                return p.parent;
            }
        }

        /**
         * TODO
         * <p>
         * Returns the location of the Node<K> containing the inorder successor
         * of this Node<K>.
         */
        @Override
        public Location<K> next() {
            if (right != null) {
                Node<K> p = right;
                while (p.left != null) {
                    p = p.left;
                }
                return p;
            } else {
                Node<K> p = this;
                while (p.parent != null && p.parent.right == p) {
                    p = p.parent;
                }
                return p.parent;
            }
        }

        public boolean isAVL() {
            int h1, h2;
            h1 = get_height(left);
            h2 = get_height(right);
            return Math.abs(h2 - h1) < 2;
        }

        public String toString() {
            return toStringPreorder(this);
        }

    }

    protected Node<K> root;
    protected int numNodes;
    protected BiPredicate<K, K> lessThan;

    /**
     * Constructs an empty BST, where the data is to be organized according to
     * the lessThan relation.
     */
    public BinarySearchTree(BiPredicate<K, K> lessThan) {
        this.lessThan = lessThan;
    }

    /**
     * TODO
     * <p>
     * Looks up the key in this tree and, if found, returns the
     * location containing the key.
     */
    public Node<K> search(K key) {
        Node<K> current = root;
        while (current != null) {
            if (lessThan.test(key, current.data)) {
                current = current.left;
            } else if (lessThan.test(current.data, key)) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    /**
     * TODO
     * <p>
     * Returns the height of this tree. Runs in O(1) time!
     */
    public int height() {
        return get_height(root);
    }

    /**
     * TODO
     * <p>
     * Clears all the keys from this tree. Runs in O(1) time!
     */
    public void clear() {
        root = null;
        numNodes = 0;
    }

    /**
     * Returns the number of keys in this tree.
     */
    public int size() {
        return numNodes;
    }

    /**
     * TODO
     * <p>
     * Inserts the given key into this BST, as a leaf, where the path
     * to the leaf is determined by the predicate provided to the tree
     * at construction time. The parent pointer of the new Node<K> and
     * the heights in all Node<K> along the path to the root are adjusted
     * accordingly.
     * <p>
     * Note: we assume that all keys are unique. Thus, if the given
     * key is already present in the tree, nothing happens.
     * <p>
     * Returns the location where the insert occurred (i.e., the leaf
     * Node<K> containing the key), or null if the key is already present.
     */
    public Node<K> insert(K key) {
        if (root == null) {
            root = new Node<K>(key);
            numNodes++;
            return root;
        }

        Node<K> currentNode = root;
        Node<K> parentNode = null;

        while (currentNode != null) {
            parentNode = currentNode;
            if (lessThan.test(key, currentNode.data)) {
                currentNode = currentNode.left;
            } else if (lessThan.test(currentNode.data, key)) {
                currentNode = currentNode.right;
            } else {
                return null; // Key already exists, so return null
            }
        }

        Node<K> newNode = new Node<K>(key);
        if (lessThan.test(key, parentNode.data)) {
            parentNode.left = newNode;
        } else {
            parentNode.right = newNode;
        }
        newNode.parent = parentNode;

        // Update height for all ancestors
        while (parentNode != null) {
            parentNode.updateHeight();
            parentNode = parentNode.parent;
        }

        numNodes++;
        return newNode;
    }


    /**
     * Returns a textual representation of this BST.
     */
    public String toString() {
        return toStringPreorder(root);
    }

    /**
     * Returns true iff the given key is in this BST.
     */
    public boolean contains(K key) {
        Node<K> p = search(key);
        return p != null;
    }

    /**
     * TODO
     * <p>
     * Removes the key from this BST. If the key is not in the tree,
     * nothing happens.
     */
    public void remove(K key) {
        Node<K> nodeToRemove = search(key);
        if (nodeToRemove == null) return; // Key not found

        // Case 1: Node to remove has two children
        if (nodeToRemove.left != null && nodeToRemove.right != null) {
            Node<K> successor = (Node<K>) nodeToRemove.next();
            nodeToRemove.data = successor.data;
            nodeToRemove = successor;
        }

        // Case 2 & 3: Node has one child or no child
        Node<K> child;
        if (nodeToRemove.left != null) {
            child = nodeToRemove.left;
        } else {
            child = nodeToRemove.right;
        }

        if (child != null) {
            child.parent = nodeToRemove.parent;
        }

        if (nodeToRemove.parent == null) {
            root = child;
        } else if (nodeToRemove == nodeToRemove.parent.left) {
            nodeToRemove.parent.left = child;
        } else {
            nodeToRemove.parent.right = child;
        }

        // Update height for all ancestors
        Node<K> parentNode = nodeToRemove.parent;
        while (parentNode != null) {
            parentNode.updateHeight();
            parentNode = parentNode.parent;
        }

        numNodes--;
    }


    private K findMinValue(Node<K> root) {
        K minValue = root.data;
        while (root.left != null) {
            minValue = root.left.data;
            root = root.left;
        }
        return minValue;
    }



    /**
     * TODO * <p> * Returns a sorted list of all the keys in this tree.
     */
    public List<K> keys() {
        List<K> result = new LinkedList<>();
        inorderTraversal(root, result);
        return result;
    }

    private void inorderTraversal(Node<K> node, List<K> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.data);
            inorderTraversal(node.right, result);
        }
    }


    static private <K> String toStringPreorder(Node<K> p) {
        if (p == null) return ".";
        String left = toStringPreorder(p.left);
        if (left.length() != 0) left = " " + left;
        String right = toStringPreorder(p.right);
        if (right.length() != 0) right = " " + right;
        String data = p.data.toString();
        return "(" + data + "[" + p.height + "]" + left + right + ")";
    }

    /**
     * The get_height method returns the height of the Node<K> n, which may be null.
     */
    static protected <K> int get_height(Node<K> n) {
        if (n == null) return -1;
        else return n.height;
    }
}