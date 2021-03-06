/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

import java.util.List;
import java.util.ArrayList;

/**
 * Static string-matching class
 * Uses a naive string-matching algorithm to find a pattern string in a 
 * template string.
 * Naive algorithm sourced from GeeksForGeeks 
 * (https://www.geeksforgeeks.org/naive-algorithm-for-pattern-searching/)
 * 
 * Tests for 100% match between pattern and template
 * 
 * @author Alisa Wallace
 * @version 1.0
 * 
 */
public class Matcher {

    protected static final int MIN_LENGTH = 1;
    /**
     * Naive string-matching algorithm
     * Looks for all instances of exact matches between template and pattern
     * @return a list of start indices in template where there is a match or 
     * null if there are no matches
     */
    public static List<Integer> findMatches(String template, String pattern) throws IllegalArgumentException {
        
        validateInput(template, pattern);
        
        ArrayList<Integer> matches = new ArrayList<>();

        if (pattern.equals(template)) {
            matches.add(0);
            return matches;
        }
    
        int tLength = template.length();
        int pLength = pattern.length();

        for (int t = 0; t <= tLength - pLength; t++) {
            
            int p;      // tracks pattern index
            for (p = 0; p < pLength; p++) {
                if (template.charAt(t + p) != pattern.charAt(p)) 
                    break;
            }

            if (p == tLength)   // we've found the complete pattern
                matches.add(t);
        }

        if (matches.isEmpty()) 
            return null;
        
        return matches;

    }

    private static void validateInput(String template, String pattern) throws IllegalArgumentException {
        if (template == null || pattern == null)
            throw new IllegalArgumentException("Input strings cannot be null.");

        if (template.isBlank() || pattern.isBlank())
            throw new IllegalArgumentException("Input strings cannot be blank.");
        
        if (template.length() < MIN_LENGTH || pattern.length() < MIN_LENGTH)
            throw new IllegalArgumentException("Input strings must have length >= " + MIN_LENGTH);

        if (template.length() < pattern.length())
            throw new IllegalArgumentException("Template must have length >= pattern");
    }
}
