/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit Testing for ReplicationProduct class.
 * 
 * @author Alisa Wallace
 * @version 1.0
 */
public class ReplicationProductTest {
    
    // for easier reference
    Directionality five = Directionality.FIVE_PRIME;
    Directionality three = Directionality.THREE_PRIME;

    String t1 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTAAAAAAAAAAAAGCTGCT";

    String p1Fwd = "AAAAAAAAAAAA";      // shouldn't match
    String p1Rev = "TTTTTTTTTTTT";      // should match

    // very close to match, shouldn't match
    String p1RevPartialMatch = "TTTTTTTTTTAT";  

    Sequence template1 = new Sequence("template1", t1, five);

    // should throw exception with template1
    Sequence primer1Fwd = new Sequence("primer1Fwd", p1Fwd, five);

    // should make no products with template1
    Sequence primer1Rev = new Sequence("primer1Fwd", p1Fwd, three);

    // should find 4 matches with template1
    Sequence primer1RC = new Sequence("primer1Rev", p1Rev, three);

    /**
     * Tests that ReplicationProduct constructor initializes fields correctly.
     */
    @Test
    public void testConstructor() {

        ReplicationProduct rp1 = new ReplicationProduct("Test 1", template1, primer1Rev);
        
        assertFalse(rp1.isAnalyzed());
        assertTrue(rp1.isEmpty());
        assertEquals(0, rp1.numMatches());
        assertSame(template1, rp1.getTemplate());
        assertSame(primer1Rev, rp1.getPrimer());
    }

    /**
     * Tests that ReplicationProduct constructor throws an IllegalArgument exception when 2 sequences with the same directionality are passed in.
     */
    @Test
    public void testConstructorException() {
        
        try {
            ReplicationProduct rp1 = new ReplicationProduct("Test 1", template1, primer1Fwd);
            fail();
        }
        catch (IllegalArgumentException e) {}
    }

    /**
     * Tests sequential findAllMatches with a 5' template and 3' primer.
     * 4 replication products expected.
     */
    @Test 
    public void testSequentialFindAllMatchesMatchesExpected5PrimeTemplate() {
        // Test with template = 5', primer = 3'

        // Expected products (3' to 5')
        // String t1sub1 = "GCTGCTGCTGCTGCTAAAAAAAAAAAA";
        String t1sub1 = "CGACGACGACGACGATTTTTTTTTTTT";

        // String t1sub2 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAA";
        String t1sub2 = "CGACGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTT";

        // String t1sub3 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAA";
        String t1sub3 = "CGACGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTT";

        // String t1sub4 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTAAAAAAAAAAAA";
        String t1sub4 = "CGACGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGATTTTTTTTTTTT";

        ReplicationProduct rp1 = new ReplicationProduct("Test 1", template1, primer1RC);

        Sequence expected1 = new Sequence("t1subproduct1", t1sub1, three);
        int loc1 = t1sub1.length() - p1Rev.length();    // 15

        Sequence expected2 = new Sequence("t1subproduct2", t1sub2, three);
        int loc2 = t1sub2.length() - p1Rev.length();    // 39

        Sequence expected3 = new Sequence("t1subproduct3", t1sub3, three);
        int loc3 = t1sub3.length() - p1Rev.length();    // 63

        Sequence expected4 = new Sequence("t1subproduct4", t1sub4, three);
        int loc4 = t1sub4.length() - p1Rev.length();    // 84


        rp1.findAllMatches();

        System.out.println("Num matches = " + rp1.numMatches());

        for (int i = 0; i < rp1.numMatches(); i++) {
            System.out.println("Product " + (i+1) + " (" +rp1.getMatchLocation(i) + ")" + " = " + rp1.getMatchProduct(i).sequence());
        }

        assertTrue(rp1.isAnalyzed());
        assertFalse(rp1.isEmpty());
        assertEquals(4, rp1.numMatches());
    
        // first product
        Sequence match1 = rp1.getMatchProduct(0);
        assertEquals(loc1, rp1.getMatchLocation(0));
        assertEquals(expected1, match1);

        // second product
        Sequence match2 = rp1.getMatchProduct(1);
        assertEquals(loc2, rp1.getMatchLocation(1));
        assertEquals(expected2, match2);

        // third product
        Sequence match3 = rp1.getMatchProduct(2);
        assertEquals(loc3, rp1.getMatchLocation(2));
        assertEquals(expected3, match3);

        // fourth product
        Sequence match4 = rp1.getMatchProduct(3);
        assertEquals(loc4, rp1.getMatchLocation(3));
        assertEquals(expected4, match4);

    }

    /**
     * Tests sequential findAllMatches with a 3' template and 5' primer.
     * 4 replication products expected.
     */
    @Test 
    public void testSequentialFindAllMatchesMatchesExpected3PrimeTemplate() {
        // Test with template = 3', primer = 5'
        
        // Expected subproducts (5' to 3')
        String sub1 = "TTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGATTTTTTTTTTTTCGACGA";

        String sub2 = "TTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGATTTTTTTTTTTTCGACGA";

        String sub3 = "TTTTTTTTTTTTCGACGACGATTTTTTTTTTTTCGACGA";

        String sub4 = "TTTTTTTTTTTTCGACGA";

        Sequence template13P = new Sequence("template1", t1, three);
        Sequence primer1RC5P = new Sequence("primer1Rev", p1Rev, five);

        ReplicationProduct rp1 = new ReplicationProduct("Test 2", template13P, primer1RC5P);

        Sequence expected1 = new Sequence("t1subproduct1", sub1, five);
        int loc1 = t1.length() - sub1.length();     // 15

        Sequence expected2 = new Sequence("t1subproduct2", sub2, five);
        int loc2 = t1.length() - sub2.length();     // 39

        Sequence expected3 = new Sequence("t1subproduct3", sub3, five);
        int loc3 = t1.length() - sub3.length();     // 63

        Sequence expected4 = new Sequence("t1subproduct4", sub4, five);
        int loc4 = t1.length() - sub4.length();     // 84

        rp1.findAllMatches();

        assertTrue(rp1.isAnalyzed());
        assertFalse(rp1.isEmpty());
        assertEquals(4, rp1.numMatches());
    
        // first product
        Sequence match1 = rp1.getMatchProduct(0);
        assertEquals(loc1, rp1.getMatchLocation(0));
        assertEquals(expected1, match1);

        // second product
        Sequence match2 = rp1.getMatchProduct(1);
        assertEquals(loc2, rp1.getMatchLocation(1));
        assertEquals(expected2, match2);

        // third product
        Sequence match3 = rp1.getMatchProduct(2);
        assertEquals(loc3, rp1.getMatchLocation(2));
        assertEquals(expected3, match3);

        // fourth product
        Sequence match4 = rp1.getMatchProduct(3);
        assertEquals(loc4, rp1.getMatchLocation(3));
        assertEquals(expected4, match4);

    }

    /**
     * Tests sequential findAllMatches with a 5' template and 3' primer
     * where primer sequence should find no complement in template and thus
     * not produce any products. 
     */
    @Test
    public void testSequentialFindAllMatchesNoMatchesExpected() {

        ReplicationProduct rp2 = new ReplicationProduct("Test 3", template1, primer1Rev);

        rp2.findAllMatches();

        assertTrue(rp2.isAnalyzed());
        assertTrue(rp2.isEmpty());
        assertEquals(0, rp2.numMatches());
    }

    /**
     * Tests sequential findAllMatches with a 5' template and 3' primer where
     * the primer sequence has a 90% complement in template.
     * Because only 100% matches are accepted, should be no matches.
     */
    @Test
    public void testSequentialFindAllMatchesPartialMatchNoMatchesExpected() {
        Sequence p1partial = new Sequence("t1PartialMatch", p1RevPartialMatch, three);
        ReplicationProduct rp2 = new ReplicationProduct("Test 4", template1, p1partial);

        rp2.findAllMatches();

        assertTrue(rp2.isAnalyzed());
        assertTrue(rp2.isEmpty());
        assertEquals(0, rp2.numMatches());
    }

    /**
     * Tests sequential findAllMatches where the template and the primer both
     * have the same directionality and therefore cannot make any products.
     * Should throw IllegalArgumentException.
     */
    @Test
    public void testSequentialFindAllMatchesException() {

        // sequences have the same directionality so product cannot be made
        try {
            ReplicationProduct rp = new ReplicationProduct("Test 5", template1, primer1Fwd);
            fail();
        }
        catch (IllegalArgumentException e) {}
    }

    /**
     * Tests sequential findAllMatches where the "template" is the primer and
     * the "primer" is the template, i.e., searching for a larger string in a 
     * smaller string.
     * Should throw IllegalArgumentException.
     */
    @Test
    public void testMatcherException() {

        // swap template and primer so matching looks for template sequence
        // in primer sequence
        ReplicationProduct rp3 = new ReplicationProduct("Exception", primer1Rev, template1);

        try {
            rp3.findAllMatches();
            fail();
        }
        catch (IllegalArgumentException e) {}
    }


    /**
     * Tests parallel findAllMatches with 5' template and 3' primer.
     * 4 matches expected.
     */
    @Test
    public void testParallelFindAllMatchesMatchesExpected5PrimeTemplate() {
        // Test with template = 5', primer = 3'

        int numThreads = 4;

        // Expected products (3' to 5')
        // String t1sub1 = "GCTGCTGCTGCTGCTAAAAAAAAAAAA";
        String t1sub1 = "CGACGACGACGACGATTTTTTTTTTTT";

        // String t1sub2 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAA";
        String t1sub2 = "CGACGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTT";

        // String t1sub3 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAA";
        String t1sub3 = "CGACGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTT";

        // String t1sub4 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTAAAAAAAAAAAA";
        String t1sub4 = "CGACGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGATTTTTTTTTTTT";

        ReplicationProduct rp1 = new ReplicationProduct("Test 1", template1, primer1RC);

        System.out.println("template length = " + template1.length() + ", primer length = " + primer1RC.length());

        Sequence expected1 = new Sequence("t1subproduct1", t1sub1, three);
        int loc1 = t1sub1.length() - p1Rev.length();    // 15

        Sequence expected2 = new Sequence("t1subproduct2", t1sub2, three);
        int loc2 = t1sub2.length() - p1Rev.length();    // 39

        Sequence expected3 = new Sequence("t1subproduct3", t1sub3, three);
        int loc3 = t1sub3.length() - p1Rev.length();    // 63

        Sequence expected4 = new Sequence("t1subproduct4", t1sub4, three);
        int loc4 = t1sub4.length() - p1Rev.length();    // 84


        rp1.findAllMatchesParallel(numThreads);

        System.out.println("Num matches = " + rp1.numMatches());

        for (int i = 0; i < rp1.numMatches(); i++) {
            System.out.println("Product " + (i+1) + " (" +rp1.getMatchLocation(i) + ")" + " = " + rp1.getMatchProduct(i).sequence());
        }

        assertTrue(rp1.isAnalyzed());
        assertFalse(rp1.isEmpty());
        assertEquals(4, rp1.numMatches());
    
        // first product
        Sequence match1 = rp1.getMatchProduct(0);
        assertEquals(loc1, rp1.getMatchLocation(0));
        assertEquals(expected1, match1);

        // second product
        Sequence match2 = rp1.getMatchProduct(1);
        assertEquals(loc2, rp1.getMatchLocation(1));
        assertEquals(expected2, match2);

        // third product
        Sequence match3 = rp1.getMatchProduct(2);
        assertEquals(loc3, rp1.getMatchLocation(2));
        assertEquals(expected3, match3);

        // fourth product
        Sequence match4 = rp1.getMatchProduct(3);
        assertEquals(loc4, rp1.getMatchLocation(3));
        assertEquals(expected4, match4);

    }

    /**
     * Tests parallel findAllMatches with 3' template and 5' primer.
     * 4 matches expected. 
     */
    @Test
    public void testParallelFindAllMatchesMatchesExpected3PrimeTemplate() {
        // Test with template = 3', primer = 5'
        
        int numThreads = 4;

        // Expected subproducts (5' to 3')
        String sub1 = "TTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGATTTTTTTTTTTTCGACGA";

        String sub2 = "TTTTTTTTTTTTCGACGACGACGATTTTTTTTTTTTCGACGACGATTTTTTTTTTTTCGACGA";

        String sub3 = "TTTTTTTTTTTTCGACGACGATTTTTTTTTTTTCGACGA";

        String sub4 = "TTTTTTTTTTTTCGACGA";

        Sequence template13P = new Sequence("template1", t1, three);
        Sequence primer1RC5P = new Sequence("primer1Rev", p1Rev, five);

        ReplicationProduct rp1 = new ReplicationProduct("Test 2", template13P, primer1RC5P);

        Sequence expected1 = new Sequence("t1subproduct1", sub1, five);
        int loc1 = t1.length() - sub1.length();     // 15

        Sequence expected2 = new Sequence("t1subproduct2", sub2, five);
        int loc2 = t1.length() - sub2.length();     // 39

        Sequence expected3 = new Sequence("t1subproduct3", sub3, five);
        int loc3 = t1.length() - sub3.length();     // 63

        Sequence expected4 = new Sequence("t1subproduct4", sub4, five);
        int loc4 = t1.length() - sub4.length();     // 84

        rp1.findAllMatchesParallel(numThreads);

        assertTrue(rp1.isAnalyzed());
        assertFalse(rp1.isEmpty());
        assertEquals(4, rp1.numMatches());
    
        // first product
        Sequence match1 = rp1.getMatchProduct(0);
        assertEquals(loc1, rp1.getMatchLocation(0));
        assertEquals(expected1, match1);

        // second product
        Sequence match2 = rp1.getMatchProduct(1);
        assertEquals(loc2, rp1.getMatchLocation(1));
        assertEquals(expected2, match2);

        // third product
        Sequence match3 = rp1.getMatchProduct(2);
        assertEquals(loc3, rp1.getMatchLocation(2));
        assertEquals(expected3, match3);

        // fourth product
        Sequence match4 = rp1.getMatchProduct(3);
        assertEquals(loc4, rp1.getMatchLocation(3));
        assertEquals(expected4, match4);

    }
}

