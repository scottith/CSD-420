package com.mycompany.csd420_project01;

/*
 * Scott Macioce | 08/31/2025 | CSD420-J307 - Module 3 Programming Assignment
 * Purpose: Create a program that fills an ArrayList with 50 random integers (1–20)
 * then uses a generic static method to return a new ArrayList with duplicates removed
 */

import java.util.ArrayList;
import java.util.Random;

public class RemoveDupesTest {

    public static void main(String[] args) {
        // Step 1: Fill an ArrayList with 50 random integers between 1 and 20
        ArrayList<Integer> originalList = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 50; i++) {
            originalList.add(rand.nextInt(20) + 1); // random numbers 1–20
        }

        // Step 2: Display original list
        System.out.println("Original ArrayList (50 random integers with duplicates):");
        System.out.println(originalList);

        // Step 3: Call the removeDuplicates method
        ArrayList<Integer> uniqueList = removeDuplicates(originalList);

        // Step 4: Display new list with duplicates removed
        System.out.println("\nArrayList after removing duplicates:");
        System.out.println(uniqueList);
    }

    /**
     * Generic method to remove duplicates from an ArrayList.
     * @param list the original ArrayList containing possible duplicates
     * @return a new ArrayList with duplicates removed
     */
    public static <E> ArrayList<E> removeDuplicates(ArrayList<E> list) {
        ArrayList<E> result = new ArrayList<>();

        for (E element : list) {
            if (!result.contains(element)) {
                result.add(element); // only add unique elements
            }
        }

        return result;
    }
}

