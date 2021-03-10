import static org.junit.Assert.*;

import org.junit.Test;

public class ReplicationProductTest {
    
    Directionality five = Directionality.FIVE_PRIME;
    Directionality three = Directionality.THREE_PRIME;

    String t1 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTAAAAAAAAAAAAGCTGCT";

    String t1sub4 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTAAAAAAAAAAAA";

    String t1sub3 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAA";

    String t1sub2 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAA";

    String t1sub1 = "GCTGCTGCTGCTGCTAAAAAAAAAAAA";

    String p1Fwd = "AAAAAAAAAAAA";      // shouldn't match
    String p1Rev = "TTTTTTTTTTTT";      // should match

    // very close to match, shouldn't match
    String p1RevPartialMatch = "TTTTTTTTTTAT";  

    Sequence template1 = new Sequence("template1", t1, five);

    // should throw exception
    Sequence primer1Fwd = new Sequence("primer1Fwd", p1Fwd, five);

    // should make no products with template1
    Sequence primer1Rev = new Sequence("primer1Fwd", p1Fwd, three);

    // should find matches with template1
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
    public void testFindAllMatchesMatchesExpected() {

        System.out.println(Sequence.generateComplement("CAT"));
    

        ReplicationProduct rp1 = new ReplicationProduct("Test 1", template1, primer1RC);

        Sequence expected1 = new Sequence("t1subproduct1", Sequence.generateComplement(t1sub1), three);
        System.out.println("Expected = " + Sequence.generateComplement(t1sub1));
        int loc1 = 15;

        // Sequence sub2 = new Sequence("t1subproduct2", t1sub2, false);
        // int loc2 = 27;
        // String expected2 = sub2.complement();

        // Sequence sub3 = new Sequence("t1subproduct3", t1sub3, false);
        // int loc3 = 51;
        // String expected3 = sub3.complement();

        // Sequence sub4 = new Sequence("t1subproduct4", t1sub4, false);
        // int loc4 = 75;
        // String expected4 = sub4.complement();

        rp1.findAllMatches();

        assertTrue(rp1.isAnalyzed());
        assertFalse(rp1.isEmpty());
        assertEquals(4, rp1.numMatches());

        int rpLoc0 = rp1.getMatchLocation(0);
        System.out.println(rpLoc0);

        Sequence match1 = rp1.getMatchProduct(0);
        System.out.println("Direction = " + match1.direction().toString() + ", sequence = " + match1.sequence());

        // first product
        assertEquals(loc1, rp1.getMatchLocation(0));
        assertEquals(expected1.sequence(), rp1.getMatchProduct(0).sequence());

        // second product
        // assertEquals(loc2, rp1.getMatchLocation(1));
        // assertEquals(expected2, rp1.getMatchProduct(1));

        // // third product
        // assertEquals(loc3, rp1.getMatchLocation(2));
        // assertEquals(expected3, rp1.getMatchProduct(2));

        // // fourth product
        // assertEquals(loc4, rp1.getMatchLocation(3));
        // assertEquals(expected4, rp1.getMatchProduct(3));
    }

    @Test
    public void testFindAllMatchesNoMatches() {

        ReplicationProduct rp2 = new ReplicationProduct(template1, primer1Fwd);

        rp2.findAllMatches();

        assertTrue(rp2.isAnalyzed());
        assertTrue(rp2.isEmpty());
        assertEquals(0, rp2.numMatches());
    }

    @Test
    public void testFindAllMatchesPartialMatch() {
        Sequence p1partial = new Sequence("t1 partial match", p1RevPartialMatch, false);
        ReplicationProduct rp2 = new ReplicationProduct(template1, p1partial);

        rp2.findAllMatches();

        assertTrue(rp2.isAnalyzed());
        assertTrue(rp2.isEmpty());
        assertEquals(0, rp2.numMatches());
    }

    @Test
    public void testMatcherException() {

        // swap template and primer so matching looks for template sequence
        // in primer sequence
        ReplicationProduct rp3 = new ReplicationProduct(primer1Rev, template1);

        try {
            rp3.findAllMatches();
            fail();
        }
        catch (IllegalArgumentException e) {}


    }

}
