package com.mycompany.csd420_project01;
/*
 * Scott Macioce | 08/31/2025 | CSD420-J307 - Module 4: LinkedList Traversal Benchmark
 * Purpose: Store N integers in a LinkedList and compare traversal time using an Iterator vs. get(index).
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LinkedListTraverseTime {

    public static void main(String[] args) {
        // Test both sizes
        int[] sizes = {50_000, 500_000};

        // Warm-up
        warmUp();

        for (int n : sizes) {
            System.out.println("\n=== Benchmark for N = " + n + " ===");

            // Build LinkedList with sequential integers
            LinkedList<Integer> list = buildSequentialLinkedList(n);

            // Traverse using an iterator
            Result iterResult = timeTraversalWithIterator(list);
            // Traverse using get(index)
            Result indexResult = timeTraversalWithGetIndex(list);

            // Sanity checks: sums must match, and count must equal N
            boolean sumsEqual = (iterResult.sum == indexResult.sum);
            boolean countsOK = (iterResult.count == n) && (indexResult.count == n);

            // Print results
            System.out.printf("Iterator   : %,d elements | sum=%d | time=%d ms%n",
                    iterResult.count, iterResult.sum, iterResult.millis());
            System.out.printf("get(index) : %,d elements | sum=%d | time=%d ms%n",
                    indexResult.count, indexResult.sum, indexResult.millis());

            // Test code
            if (!sumsEqual || !countsOK) {
                throw new AssertionError("Validation failed: sumsEqual=" + sumsEqual + ", countsOK=" + countsOK);
            } else {
                System.out.println("Validation: OK (same sum and count)");
            }
        }

        System.out.println("\nDone.");
    }

    /** Simple container to hold traversal metrics. */
    private static class Result {
        final long nanos;
        final long sum;
        final int count;
        Result(long nanos, long sum, int count) {
            this.nanos = nanos; this.sum = sum; this.count = count;
        }
        long millis() { return TimeUnit.NANOSECONDS.toMillis(nanos); }
    }

    /** Build a LinkedList with integers */
    private static LinkedList<Integer> buildSequentialLinkedList(int n) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++) list.add(i);
        return list;
    }

    /** Warm-up */
    private static void warmUp() {
        LinkedList<Integer> warm = buildSequentialLinkedList(10_000);
        timeTraversalWithIterator(warm);
        timeTraversalWithGetIndex(warm);
    }

    /** Traverse via Iterator */
    private static Result timeTraversalWithIterator(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        int count = 0;

        // Using an explicit iterator to be clear about the mechanism
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            sum += it.next();
            count++;
        }

        long end = System.nanoTime();
        return new Result(end - start, sum, count);
    }

    /**
     * Traverse via get(index) (LinkedList.get(i) is O(i), so this overall loop is O(n^2)).
     */
    private static Result timeTraversalWithGetIndex(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        int count = 0;

        for (int i = 0, n = list.size(); i < n; i++) {
            sum += list.get(i); // For LinkedList this walks from a head/tail each time → O(n^2) total
            count++;
        }

        long end = System.nanoTime();
        return new Result(end - start, sum, count);
    }
}

/*
Analysis
- Iterator traversal is O(n) for LinkedList and should complete relatively quickly for both 50,000 and 500,000 elements.
- get(index) traversal is O(n^2) on LinkedList because each get(i) takes O(i) time on average
- The program prints per-approach times in milliseconds and verifies by comparing:
    (1) the total number of elements visited
    (2) the sum of all values
  If either check fails, an AssertionError is thrown.
*/