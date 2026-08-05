public class MergeSort {
    // Helper method to find the middle node of the linked list
    private static Node findMiddle(Node head) {
        if (head == null) {
            return head;
        }

        Node slow = head, fast = head.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    // Merge function to merge two sorted lists into a new sorted list
    public static Node merge(Node A, Node B) {
        if (A == null) return Utils.copy(B);
        if (B == null) return Utils.copy(A);

        if (A.data < B.data) {
            return new Node(A.data, merge(A.next, B));
        } else {
            return new Node(B.data, merge(A, B.next));
        }
    }

    // Sort function that sorts the list using a functional approach and returns a new sorted list
    public static Node sort(Node head) {
        if (head == null || head.next == null) {
            return head;
        }

        int len = Utils.length(head);
        Node left = Utils.take(head, len / 2);
        Node right = Utils.drop(head, len / 2);

        left = sort(left);
        right = sort(right);

        return merge(left, right);
    }

    // Merge function that merges two sorted lists in-place
    public static Node merge_in_place(Node A, Node B) {
        if (A == null) return B;
        if (B == null) return A;

        Node head, current;
        if (A.data < B.data) {
            head = current = A;
            A = A.next;
        } else {
            head = current = B;
            B = B.next;
        }

        while (A != null && B != null) {
            if (A.data < B.data) {
                current.next = A;
                A = A.next;
            } else {
                current.next = B;
                B = B.next;
            }
            current = current.next;
        }

        if (A != null) {
            current.next = A;
        } else {
            current.next = B;
        }

        return head;
    }

    // Sort function that sorts the list in-place
    public static Node sort_in_place(Node head) {
        if (head == null || head.next == null) {
            return head;
        }

        Node middle = findMiddle(head);
        Node nextOfMiddle = middle.next;
        middle.next = null;

        Node left = sort_in_place(head);
        Node right = sort_in_place(nextOfMiddle);

        return merge_in_place(left, right);
    }
}
