/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

package main;

import java.util.*;

/**
 * SequenceMatcher is a string-matching class specialized for DNA sequence
 * inputs.  It uses a naive string-matching algorithm to search for a smaller
 * DNA sequence within a larger one, and allows the application programmer
 * to specify whether they wish to match based on an exact match (e.g.,
 * template base = A so pattern base must also equal A) or based on the base
 * complimentarity (e.g., template base = A so pattern base must equal T).  
 * A match is only counted if every base in the pattern is found in the
 * template (i.e., 100% match).
 * 
 * Both sequential and parallel version of the algorithm are available, and the
 * number of threads to use in the parallel implmentation can be specified.
 * 
 * References: GeeksforGeeks 
 * (https://www.geeksforgeeks.org/naive-algorithm-for-pattern-searching/)
 * 
 * @author Alisa Wallace
 * @version 1.4
 */
public class SequenceMatcher {
    
    private static final int MIN_LENGTH = 1;
    private int numThreads;     
    private String template;            // Larger sequence
    private String pattern;             // what we're looking for in template
    private ArrayList<Integer> matches;
    private ArrayList<Thread> threads;
    boolean matchComplement;         // false for exact match between template
                                    // and pattern bases, true to match based
                                    // on complement between bases

                                    
    /**
     * Constructor.
     * @param template larger sequence to in which to search for pattern
     * @param pattern  smaller sequence to find in template
     * @throws IllegalArgumentException when:
     *      - Any input string is null
     *      - Any input string is empty
     *      - Any input string is constituted of whitespaces only
     *      - pattern size is larger than template size
     */
    public SequenceMatcher(String template, String pattern) throws IllegalArgumentException {
        
        validateInput(template, pattern);

        this.template = template;
        this.pattern = pattern;
        matches = new ArrayList<Integer>();
        threads = new ArrayList<Thread>();
    }


    /**
     * Parallelized naive string-matching algorithm
     * Looks for all instances of exact matches between template and pattern
     * @param numThreads number of threads with which to parallelize
     * @param matchComplement true if a looking for complement between template
     * and pattern, false if looking for exact same sequence
     * @return a list of start indices in template where there is a match or 
     * null if there are no matches
     */
    public ArrayList<Integer> findMatchesParallel(int numThreads, boolean matchComplement) {
        
        this.matchComplement = matchComplement;
        this.numThreads = numThreads;
        int tLength = template.length();
    
        int sectionSize = tLength / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * sectionSize;
            int end;
            if (i == numThreads - 1)
                end = tLength;
            else {
                end = start + sectionSize;
            }
            Thread t = new Thread(new ThreadTask(i + 1, start, end));
            threads.add(t);
            t.start();
        }

        try {
            for (int i = 0; i < numThreads; i++)
            threads.get(i).join();
        }
        catch (InterruptedException e) {
            System.out.println("Process interrupted, results nullified");
            return null;
        }

        return matches;

    }

    /**
     * Sequential naive string-matching algorithm
     * Looks for all instances of exact matches between template and pattern
     * @param matchComplement true if a looking for complement between template
     * and pattern, false if looking for exact same sequence
     * @return a list of start indices in template where there is a match or 
     * null if there are no matches
     */
    public ArrayList<Integer> findMatchesSequential(boolean matchComplement) {
    
        this.matchComplement = matchComplement;
        int tLength = template.length();
        int pLength = pattern.length();

        for (int t = 0; t <= tLength - pLength; t++) {
            
            int p;      // tracks pattern index
            for (p = 0; p < pLength; p++) {
                char tempBase = template.charAt(t + p);
                char primBase = pattern.charAt(p);
                    if (! isMatch(tempBase, primBase))
                        break;
            }

            if (p == pLength) {
                // we've found the complete pattern
                matches.add(t);
            } 
        }
        
        return matches;
    }

    /**
     * Determines whether two bases count as match based on matching settings
     * @param a first base
     * @param b second base
     * @return true if matched, false if not
     */
    private boolean isMatch(char a, char b) {
        if (matchComplement) {
            return isComplement(a, b);
        }
        return a == b;
    }

    /**
     * Determines whether 2 bases are complements of each other based on 
     * natural base-pairing rules
     * @param base1 first base
     * @param base2 second base
     * @return true if complements (A and T, G and C), false if not 
     */
    private boolean isComplement(Character base1, Character base2) {

        base1 = Character.toUpperCase(base1);
        base2 = Character.toUpperCase(base2);
        switch (base1){
            case 'A':
                return base2.equals('T');
            case 'C':
                return base2.equals('G');
            case 'G':
                return base2.equals('C');
            case 'T':
                return base2.equals('A');
            default:
                return false;
        }
    }

    /**
     * Validates input Strings to ensure proper function when matching.
     * @param template
     * @param pattern
     * @throws IllegalArgumentException
     */
    private void validateInput(String template, String pattern) throws IllegalArgumentException {
        if (template == null || pattern == null)
            throw new IllegalArgumentException("Input strings cannot be null.");

        if (template.isBlank() || pattern.isBlank())
            throw new IllegalArgumentException("Input strings cannot be blank.");
        
        if (template.length() < MIN_LENGTH || pattern.length() < MIN_LENGTH)
            throw new IllegalArgumentException("Input strings must have length >= " + MIN_LENGTH);

        if (template.length() < pattern.length())
            throw new IllegalArgumentException("Template must have length >= pattern");
    }

    /**
     * Class for individual thread tasks.
     * Each thread is given a portion of the template to search for matches
     * using the naive string-matching algorithm.
     * When a match is found uses a synchronized method to add result to global
     * ArrayList to avoid data races.
     */
    class ThreadTask implements Runnable {

        int id;     // thread ID
        int start;  // first index of template to search
        int end;    // index in template to search up to

        public ThreadTask(int id, int start, int end) {
            this.id = id;
            this.start = start;
            this.end = end;
        }

        /**
         * Calculates thread's section of the template to search, searches
         * it using naive string-matching algorithm, and stores any results. 
         */
        public void run() {

            int pLength = pattern.length();
            
            // Only last thread needs to worry about index out of bounds 
            // exception - other threads will potentially cross into 
            // subsequent threads' sections when matching but only up to 
            // p - 1 indices
            // Not allowing this means matches starting after index 
            // (end - pLength) will be missed for all threads but the last one
            int last;
            if (id == numThreads) {
                last = end - pLength;
            }
            else {
                last = end - 1;
            }

            for (int t = start; t <= last; t++) {
            
                int p;      // tracks pattern index
                for (p = 0; p < pLength; p++) {
                    
                    char tempBase = template.charAt(t + p);
                    char primBase = pattern.charAt(p);
                    if (! isMatch(tempBase, primBase)) 
                        break;
                }
    
                if (p == pLength) {
                    // we've found the complete pattern
                    addMatch(t);
                } 
            }
        }

        // ArrayList is not thread safe so we need to synchronize here
        // to avoid a data race
        // Since we are only ever adding (and not deleting) no need to move
        // to a thread-safe collection
        private synchronized void addMatch(int location) {
            matches.add(location);
        }
    }
}

