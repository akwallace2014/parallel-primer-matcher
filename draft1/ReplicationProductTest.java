import static org.junit.Assert.*;

import org.junit.Test;

public class ReplicationProductTest {
    
    String t1 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTAAAAAAAAAAAAGCTGCT";

    String t1sub1 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTAAAAAAAAAAAA";

    String t1sub2 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAA";

    String t1sub3 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAA";

    String t1sub4 = "GCTGCTGCTGCTGCTAAAAAAAAAAAA";

    String p1Fwd = "AAAAAAAAAAAA";
    String p1Rev = "TTTTTTTTTTTT";

    Sequence template1 = new Sequence("template1", t1, false);
    Sequence primer1Fwd = new Sequence("primer1Fwd", p1Fwd, false);
    Sequence primer1Rev = new Sequence("primer1Rev", p1Rev, false);

    @Test
    // test constructor 
    public void testConstructor() {

        ReplicationProduct rp1 = new ReplicationProduct(template1, primer1Rev);
        
        assertFalse(rp1.isAnalyzed());
        assertTrue(rp1.isEmpty());
        assertEquals(0, rp1.numMatches());
        assertSame(template1, rp1.getTemplate());
        assertSame(primer1Rev, rp1.getPrimer());
    }

    @Test 
    public void testFindAllMatchesMatches() {

        ReplicationProduct rp1 = new ReplicationProduct(template1, primer1Rev);

        Sequence sub1 = new Sequence("t1subproduct1", t1sub1, false);
        int loc1 = 6;
        String expected1 = sub1.complement();

        Sequence sub2 = new Sequence("t1subproduct2", t1sub2, false);
        int loc2 = 27;
        String expected2 = sub2.complement();

        Sequence sub3 = new Sequence("t1subproduct3", t1sub3, false);
        int loc3 = 51;
        String expected3 = sub3.complement();

        Sequence sub4 = new Sequence("t1subproduct4", t1sub4, false);
        int loc4 = 75;
        String expected4 = sub4.complement();

        rp1.findAllMatches();

        assertTrue(rp1.isAnalyzed());
        assertFalse(rp1.isEmpty());
        assertEquals(4, rp1.numMatches());

        // first product
        assertEquals(loc1, rp1.getMatchLocation(0));
        assertEquals(expected1, rp1.getMatchProduct(0));

        // second product
        assertEquals(loc2, rp1.getMatchLocation(1));
        assertEquals(expected2, rp1.getMatchProduct(1));

        // third product
        assertEquals(loc3, rp1.getMatchLocation(2));
        assertEquals(expected3, rp1.getMatchProduct(2));

        // fourth product
        assertEquals(loc4, rp1.getMatchLocation(3));
        assertEquals(expected4, rp1.getMatchProduct(3));
    }

    @Test
    public void testFindAllMatchesNoMatches() {

        ReplicationProduct rp2 = new ReplicationProduct(template1, primer1Fwd);

        rp2.findAllMatches();

        assertTrue(rp2.isAnalyzed());
        assertTrue(rp2.isEmpty());
        assertEquals(0, rp2.numMatches());
    }
}
