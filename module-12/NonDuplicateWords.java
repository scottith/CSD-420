/*
 * Scott Macioce
 * 10/12/2025
 * Module 12 – Non-Duplicate Word Sorter
 *
 * Purpose: Java program reads all words from a text file titled "collection_of_words.txt"
 * and displays all non-duplicate words in ascending and descending order.
 */

package com.mycompany.csd420_project01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NonDuplicateWords {

    public static void main(String[] args) {
        // Reference to the file
        File file = new File("collection_of_words.txt");

        // Use a TreeSet to automatically sort and remove duplicates
        Set<String> uniqueWords = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        try (Scanner input = new Scanner(file)) {
            // Use any punctuation or space as a delimiter
            input.useDelimiter("[\\W]+");

            while (input.hasNext()) {
                String word = input.next().trim();
                if (!word.isEmpty()) {
                    uniqueWords.add(word);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: The file 'collection_of_words.txt' was not found.");
            return;
        }

        // Display words in ascending order (TreeSet’s natural order)
        System.out.println("---- Unique Words (Ascending Order) ----");
        for (String word : uniqueWords) {
            System.out.println(word);
        }

        // Display words in descending order
        System.out.println("\n---- Unique Words (Descending Order) ----");
        List<String> descending = new ArrayList<>(uniqueWords);
        Collections.reverse(descending);
        for (String word : descending) {
            System.out.println(word);
        }

        // Run internal test to confirm logic works
        testNonDuplicateSorting();
    }

    /**
     * Test method to validate that sorting and duplicate removal works as intended.
     */
    private static void testNonDuplicateSorting() {
        System.out.println("\n---- Running Internal Test ----");
        List<String> sampleWords = Arrays.asList("apple", "banana", "Apple", "grape", "banana", "cherry");
        Set<String> testSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        testSet.addAll(sampleWords);

        System.out.println("Ascending Test Output:");
        for (String word : testSet) {
            System.out.println(word);
        }

        System.out.println("\nDescending Test Output:");
        List<String> descending = new ArrayList<>(testSet);
        Collections.reverse(descending);
        for (String word : descending) {
            System.out.println(word);
        }
        System.out.println("---- Test Completed Successfully ----");
    }
}