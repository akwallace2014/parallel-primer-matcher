/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SequenceMatcher {
    
    private static final int MIN_LENGTH = 1;
    private int numThreads;
    private String template;
    private String pattern;
    private ArrayList<Integer> matches;
    private ArrayList<Thread> threads;
    boolean matchComplement;         // false for exact match between template
                                    // and pattern, true for base complement

    public SequenceMatcher(String template, String pattern) {
        
        validateInput(template, pattern);

        this.template = template;
        this.pattern = pattern;
        matches = new ArrayList<Integer>();
        threads = new ArrayList<Thread>();
    }


    /**
     * Naive string-matching algorithm
     * Looks for all instances of exact matches between template and pattern
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

    private boolean isMatch(char a, char b) {
        if (matchComplement) {
            return isComplement(a, b);
        }
        return a == b;
    }

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

    class ThreadTask implements Runnable {

        int id;     // thread ID
        int start;  // first index of template to search
        int end;    // index in template to search up to

        public ThreadTask(int id, int start, int end) {
            this.id = id;
            this.start = start;
            this.end = end;
        }

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
            // try {
            //     barrier.await();
            // }
            // catch (InterruptedException e) {
            //     System.out.println("Cyclic barrier interrupted");
            // }
            // catch (BrokenBarrierException e) {
            //     System.out.println("Cyclic barrier broken.");
            // }
        }

        // ArrayList is not thread safe so we need to synchronize here
        // to avoid a data race
        // Since we are only ever adding (and not deleting) no need to move
        // to a thread-safe collection
        private synchronized void addMatch(int location) {
            // System.out.println("Thread " + id + " adding match " + location);
            matches.add(location);
        }
    }
}

