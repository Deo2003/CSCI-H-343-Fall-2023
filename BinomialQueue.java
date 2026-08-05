import java.util.function.BiPredicate;

class BinomialHeap<K> {
    K key;
    int height;
    PList<BinomialHeap<K>> children;
    BiPredicate<K, K> lessEq;

    BinomialHeap(K k, int h, PList<BinomialHeap<K>> kids, BiPredicate<K, K> le) {
        this.key = k;
        this.height = h;
        this.children = kids;
        this.lessEq = le;
    }

    /*
     * @precondition this.height == other.height
     */
    BinomialHeap<K> link(BinomialHeap<K> other) {
        if (this.height != other.height)
            throw new UnsupportedOperationException("attempt to link trees of different height");
        if (lessEq.test(other.key, this.key)) {
            PList<BinomialHeap<K>> kids = PList.addFront(other, this.children);
            return new BinomialHeap<>(this.key, this.height + 1, kids, lessEq);
        } else {
            PList<BinomialHeap<K>> kids = PList.addFront(this, other.children);
            return new BinomialHeap<>(other.key, other.height + 1, kids, lessEq);
        }
    }

        /**
         * TODO
         * <p>
         * The isHeap method checks whether or not the subtree of this node
         * satisfies the heap property.
         */
        
    boolean isHeap() {
        if (children == null) {
            return true;
        }
        for (PList<BinomialHeap<K>> child = children; child != null; child = PList.getNext(child)) {
            if (lessEq.test(this.key, PList.getFirst(child).key)) {
                return false;
            }
            if (!PList.getFirst(child).isHeap()) {
                return false;
            }
        }
        return true;
    }

        
        
        
    public String toString() {
        String ret = "(" + key.toString();
        if (children != null)
            ret = ret + " " + children.toString();
        return ret + ")";
    }

}

public class BinomialQueue<K> {
    PList<BinomialHeap<K>> forest;
    BiPredicate<K, K> lessEq;

    public BinomialQueue(BiPredicate<K, K> le) {
        forest = null;
        lessEq = le;
    }

    public void push(K key) {
        BinomialHeap<K> heap = new BinomialHeap<>(key, 0, null, lessEq);
        this.forest = insert(heap, this.forest);
    }

    public K pop() {
        BinomialHeap<K> max = PList.find_max(this.forest, (h1,h2) -> this.lessEq.test(h1.key, h2.key));
        this.forest = PList.remove(max, this.forest);
        PList<BinomialHeap<K>> kids = PList.reverse(max.children, null);
        this.forest = merge(this.forest, kids);
        return max.key;
    }

    public boolean isEmpty() {
        return forest == null;
    }

    /**
     * TODO
     * The isHeap method returns whether or not the Binomial Queue (a forest of Binomial Trees)
     * satisfies the heap property.
     */
    public boolean isHeap() {
        if (forest == null) {
            return true;
        }

        for (PList<BinomialHeap<K>> binomialTree = forest; binomialTree != null; binomialTree = PList.getNext(binomialTree)) {
            if (!PList.getFirst(binomialTree).isHeap()) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        if (this.forest == null)
            return "";
        else
            return this.forest.toString();
    }

        /**********************************
         * Helper functions
         */
        
        /**
         * TODO
         * The insert method is analogous to binary addition. That is,
         * it inserts the node n into the list ns to produce a new list
         * that is still sorted by height.
         *
         * @param <K> The type of the keys.
         * @param n   The node to insert (must not be null).
         * @param ns  The binomial forest into which to insert, ordered by height. (may be null)
         * @return A new binomial forest that includes the new node.
         */
    static <K> PList<BinomialHeap<K>> insert(BinomialHeap<K> n, PList<BinomialHeap<K>> ns) {
        return merge(ns, PList.addFront(n, null));
    }

    /**
     * TODO
     * The merge method is analogous to the merge part of merge sort. That is,
     * it takes two lists that are sorted (by height) and returns a new list that
     * contains the elements of both lists, and the new list is sorted by height.
     *
     * @param ns1
     * @param ns2
     * @return A list that is sorted and contains all and only the elements in ns1 and ns2.
     */
        
        static <K> PList<BinomialHeap<K>> merge(PList<BinomialHeap<K>> ns1, PList<BinomialHeap<K>> ns2) {
    PList<BinomialHeap<K>> result = null;

    PList<BinomialHeap<K>> current1 = ns1;
    PList<BinomialHeap<K>> current2 = ns2;

    while (current1 != null && current2 != null) {
        if (PList.getFirst(current1).height < PList.getFirst(current2).height) {

            result = PList.addFront(PList.getFirst(current1), result);
            current1 = PList.getNext(current1);
        } else {
            result = PList.addFront(PList.getFirst(current2), result);
            current2 = PList.getNext(current2);
        }
    }


    if (current1 != null) {
        while (current1 != null) {
            result = PList.addFront(PList.getFirst(current1), result);
            current1 = PList.getNext(current1);
        }
    } else if (current2 != null) {
        while (current2 != null) {
            result = PList.addFront(PList.getFirst(current2), result);
            current2 = PList.getNext(current2);
        }
    }

    return PList.reverse(result, null);
        }

}