public class Search {
    public static int find_first_true(boolean[] A, int begin, int end) {
        for (int i = begin; i < end; i++) {
            if (A[i]) {
                return i; // Return the index of the first true value
            }
        }
        return end; // Return the end position if no true value is found
    }

    public static int find_first_equal(int[] A, int x) {
        for (int i = 0; i < A.length; i++) {
            if (A[i] == x) {
                return i; // Return the index of the first element equal to x
            }
        }
        return A.length; // Return the length of the array if no element is equal to x
    }

    public static int find_first_true_sorted(boolean[] A, int begin, int end) {
        while (begin < end) {
            int mid = begin + (end - begin) / 2; // Calculate middle index

            if (A[mid]) {
                // If the middle element is true, move left to find the first true
                end = mid;
            } else {
                // If the middle element is false, move right
                begin = mid + 1;
            }
        }

        return begin; // begin will be the index of the first true value
    }

}
