package com.mycompany.csd420_project01;

/* Scott Macioce
 * 09/13/2025 
 * CSD420-J307 
 * Purpose: Implement two generic bubble sort methods: one using Comparable<E>
 * and another using Comparator<? super E>
 */

import java.util.Arrays;
import java.util.Comparator;

public class BubbleSort {

    /**
     * Generic bubble sort using Comparable.
     * Elements must implement Comparable<E>.
     *
     * @param <E>  a type that extends Comparable
     * @param list the array to sort
     */
    public static <E extends Comparable<E>> void bubbleSort(E[] list) {
        boolean swapped;
        // Repeat until no swaps are made
        for (int pass = 0; pass < list.length - 1; pass++) {
            swapped = false;
            for (int i = 0; i < list.length - 1 - pass; i++) {
                if (list[i].compareTo(list[i + 1]) > 0) {
                    // Swap adjacent elements
                    E temp = list[i];
                    list[i] = list[i + 1];
                    list[i + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break; // Optimization: stop if already sorted
        }
    }

    /**
     * Generic bubble sort using Comparator.
     *
     * @param <E>        the element type
     * @param list       the array to sort
     * @param comparator custom comparator for ordering
     */
    public static <E> void bubbleSort(E[] list, Comparator<? super E> comparator) {
        boolean swapped;
        for (int pass = 0; pass < list.length - 1; pass++) {
            swapped = false;
            for (int i = 0; i < list.length - 1 - pass; i++) {
                if (comparator.compare(list[i], list[i + 1]) > 0) {
                    // Swap adjacent elements
                    E temp = list[i];
                    list[i] = list[i + 1];
                    list[i + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    /**
     * Main method to test both bubble sort implementations.
     */
    public static void main(String[] args) {
        // Test Comparable version with Integers
        Integer[] numbers = {34, 7, 23, 32, 5, 62};
        System.out.println("Original Integers: " + Arrays.toString(numbers));
        bubbleSort(numbers);
        System.out.println("Sorted Integers (Comparable): " + Arrays.toString(numbers));

        // Test Comparable version with Strings
        String[] words = {"zebra", "apple", "pear", "banana"};
        System.out.println("\nOriginal Strings: " + Arrays.toString(words));
        bubbleSort(words);
        System.out.println("Sorted Strings (Comparable): " + Arrays.toString(words));

        // Test Comparator version - descending order integers
        Integer[] numbersDesc = {34, 7, 23, 32, 5, 62};
        System.out.println("\nOriginal Integers for Comparator: " + Arrays.toString(numbersDesc));
        bubbleSort(numbersDesc, Comparator.reverseOrder());
        System.out.println("Sorted Integers (Comparator - Descending): " + Arrays.toString(numbersDesc));

        // Test Comparator version - custom comparator for String length
        String[] animals = {"dog", "elephant", "cat", "hippopotamus"};
        System.out.println("\nOriginal Animals: " + Arrays.toString(animals));
        bubbleSort(animals, Comparator.comparingInt(String::length));
        System.out.println("Sorted Animals (Comparator - by length): " + Arrays.toString(animals));
    }
}

