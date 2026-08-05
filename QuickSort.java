
public class QuickSort {

    /**
     * TODO
     * @param begin The position of the first element in the sequence to be sorted.
     * @param end   The position that is one-past the last element in the sequence to be sorted.
     * @param <E>   The element type for the sequence.
     */
    public static <E extends Comparable<? super E>> void quicksort(Iterator<E> begin, Iterator<E> end) {
        if (begin == null || end == null || begin.equals(end)) return;

        Iterator<E> last = Algorithms.last(begin, end);
        if (!begin.equals(last)) {
            Iterator<E> p = partition(begin, end, last);
            quicksort(begin, p);
            p.advance();
            quicksort(p, end);
        }
    }

    private static <E extends Comparable<? super E>>
    Iterator<E> partition(Iterator<E> begin, Iterator<E> end, Iterator<E> last) {
        E pivot = last.get();
        Iterator<E> i = begin.clone();

        for (Iterator<E> j = begin.clone(); !j.equals(last); j.advance()) {
            if (j.get().compareTo(pivot) <= 0) {
                Algorithms.iter_swap(i, j);
                i.advance();
            }
        }
        Algorithms.iter_swap(i, last);
        return i;
    }

}