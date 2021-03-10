import static org.junit.Assert.*;

import org.junit.Test;

public class ReplicationProductTest {
    
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

    @Test
    // test constructor 
    public void testConstructor() {

        ReplicationProduct rp1 = new ReplicationProduct("Test 1", template1, primer1Rev);
        
        assertFalse(rp1.isAnalyzed());
        assertTrue(rp1.isEmpty());
        assertEquals(0, rp1.numMatches());
        assertSame(template1, rp1.getTemplate());
        assertSame(primer1Rev, rp1.getPrimer());
    }

    @Test
    public void testConstructorException() {
        
        try {
            ReplicationProduct rp1 = new ReplicationProduct("Test 1", template1, primer1Fwd);
            fail();
        }
        catch (IllegalArgumentException e) {}
    }

    @Test 
    public void testFindAllMatchesMatchesExpected5PrimeTemplate() {
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
        int loc2 = t1sub2.length() - p1Rev.length();

        Sequence expected3 = new Sequence("t1subproduct3", t1sub3, three);
        int loc3 = t1sub3.length() - p1Rev.length();

        Sequence expected4 = new Sequence("t1subproduct4", t1sub4, three);
        int loc4 = t1sub4.length() - p1Rev.length();


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

    @Test 
    public void testFindAllMatchesMatchesExpected3PrimeTemplate() {
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
        int loc1 = t1.length() - sub1.length();

        Sequence expected2 = new Sequence("t1subproduct2", sub2, five);
        int loc2 = t1.length() - sub2.length();

        Sequence expected3 = new Sequence("t1subproduct3", sub3, five);
        int loc3 = t1.length() - sub3.length();

        Sequence expected4 = new Sequence("t1subproduct4", sub4, five);
        int loc4 = t1.length() - sub4.length();

        rp1.findAllMatches();

        assertTrue(rp1.isAnalyzed());
        assertFalse(rp1.isEmpty());
        assertEquals(4, rp1.numMatches());
    
        // first product
        Sequence match1 = rp1.getMatchProduct(0);
        System.out.println("Calculated = " + loc1 + ", actual = " + rp1.getMatchLocation(0));
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

    @Test
    public void testFindAllMatchesNoMatchesExpected() {

        ReplicationProduct rp2 = new ReplicationProduct("Test 3", template1, primer1Rev);

        rp2.findAllMatches();

        assertTrue(rp2.isAnalyzed());
        assertTrue(rp2.isEmpty());
        assertEquals(0, rp2.numMatches());
    }

    @Test
    public void testFindAllMatchesPartialMatchNoMatchesExpected() {
        Sequence p1partial = new Sequence("t1PartialMatch", p1RevPartialMatch, three);
        ReplicationProduct rp2 = new ReplicationProduct("Test 4", template1, p1partial);

        rp2.findAllMatches();

        assertTrue(rp2.isAnalyzed());
        assertTrue(rp2.isEmpty());
        assertEquals(0, rp2.numMatches());
    }

    @Test
    public void testFindAllMatchesException() {

        // sequences have the same directionality so product cannot be made
        try {
            ReplicationProduct rp = new ReplicationProduct("Test 5", template1, primer1Fwd);
            fail();
        }
        catch (IllegalArgumentException e) {}
    }

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

}
