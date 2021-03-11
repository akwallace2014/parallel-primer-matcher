/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ParallelMatcher {
    
    private static final int MIN_LENGTH = 1;
    private int numThreads;
    private String template;
    private String pattern;
    private ArrayList<Integer> matches;
    private ArrayList<Thread> threads;


    public ParallelMatcher(String template, String pattern, int numThreads) {
        
        validateInput(template, pattern);

        this.template = template;
        this.pattern = pattern;
        this.numThreads = numThreads;
        matches = new ArrayList<Integer>();
        threads = new ArrayList<Thread>();
    }


    /**
     * Naive string-matching algorithm
     * Looks for all instances of exact matches between template and pattern
     * @return a list of start indices in template where there is a match or 
     * null if there are no matches
     */
    public ArrayList<Integer> findMatches() {
    

        // if (pattern.equals(template)) {
        //     matches.add(0);
        //     return matches;
        // }

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
     * For testing purposes only
     * @return
     */
    public ArrayList<Thread> getThreads() {
        return threads;
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
            
            // if (id == 0) {
            //     try {
            //         TimeUnit.MILLISECONDS.sleep(100);
            //     }
            //     catch (InterruptedException e) {}
            // }
            System.out.println("Running from thread " + id);

            int pLength = pattern.length();

            for (int t = start; t <= end - pLength; t++) {
            
                int p;      // tracks pattern index
                for (p = 0; p < pLength; p++) {
                    if (template.charAt(t + p) != pattern.charAt(p)) 
                        break;
                }
    
                if (p == pLength) {
                    // we've found the complete pattern
                    addMatch(t);
                } 
            }

            // check if remainder has pattern
            // if (id == numThreads) {
            //     int nextIndex = end - pLength + 1;
            //     String remainder = template.substring(nextIndex);
            //     if (remainder.equals(pattern)) {
            //         addMatch(nextIndex);
            //     }
            // }
        }

        private synchronized void addMatch(int location) {
            System.out.println("Thread " + id + " adding match " + location);
            matches.add(location);
        }
    }
}
