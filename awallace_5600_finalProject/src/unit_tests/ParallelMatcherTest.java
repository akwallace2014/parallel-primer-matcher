/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */
package unit_tests;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class ParallelMatcherTest {
    
    String template1 = "-----xxx------";    // match expected at 5
    String pattern = "xxx";

    // matches expected at 0, 11, 21, 29
    String template2 = "xxx--------xxx-------xxx-----xxx"; 

    @Test
    public void testFindMatchesSingleThread() {

        ParallelMatcher pm = new ParallelMatcher(template1, pattern, 1);

        ArrayList<Integer> matches = pm.findMatches();

        assertEquals(matches.size(), 1);

        int matchLoc = matches.get(0);
        assertEquals(matchLoc, 5);

        ParallelMatcher pm2 = new ParallelMatcher(template2, pattern, 1);
        
        ArrayList<Integer> matches2 = pm2.findMatches();
        int numMatches = matches2.size();

        assertEquals(numMatches, 4);

        int matchLoc1 = matches2.get(0);
        int matchLoc2 = matches2.get(1);
        int matchLoc3 = matches2.get(2);
        int matchLoc4 = matches2.get(3);

        assertEquals(matchLoc1, 0);
        assertEquals(matchLoc2, 11);
        assertEquals(matchLoc3, 21);
        assertEquals(matchLoc4, 29);

    }

    @Test
    public void testFindMatchesMultipleThreads() {

        ParallelMatcher pm2 = new ParallelMatcher(template2, pattern, 4);
        
        ArrayList<Integer> matches2 = pm2.findMatches();
        int numMatches = matches2.size();

        assertEquals(numMatches, 4);

        Collections.sort(matches2);
        int matchLoc1 = matches2.get(0);
        int matchLoc2 = matches2.get(1);
        int matchLoc3 = matches2.get(2);
        int matchLoc4 = matches2.get(3);

        assertTrue(matchLoc1 == 0);
        assertTrue(matchLoc2 == 11);
        assertTrue(matchLoc3 == 21);
        assertTrue(matchLoc4 == 29);
    }

    @Test
    public void testFindMatchesMultipleThreadsLargeInput() {

        int patternSize = 10;
        int repeatSize = 100;
        int repeats = 10;

        // Pattern occurs [repeats] number of times in size [repeats * repeatSize] template 

        String pattern = "x".repeat(patternSize);
        String template = makeTemplate(pattern, repeatSize, repeats);

        ParallelMatcher pm = new ParallelMatcher(template, pattern, 8);

        ArrayList<Integer> matches = pm.findMatches();

        assertTrue(matches.size() == repeats);

        Collections.sort(matches);
        for (int i = 0; i < matches.size(); i++) {
            int expected = (repeatSize - patternSize) + (i * repeatSize);
            int actual = matches.get(i);
            assertTrue(expected == actual);
        }
    }

    private String makeTemplate(String pattern, int sectionLength, int repeats) {
        String str = "-".repeat(sectionLength - pattern.length());
        str += pattern;
        String result = str;

        for (int i = 1; i < repeats; i++) {
            result += str;
        }
        System.out.println(result);
        return result;
    }
}
