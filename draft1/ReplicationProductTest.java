import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

public class ReplicationProductTest {
    
    String t1 = "GCTGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTAAAAAAAAAAAAGCTGCT";

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

    public void testFindAllMatches() {


    }

}
