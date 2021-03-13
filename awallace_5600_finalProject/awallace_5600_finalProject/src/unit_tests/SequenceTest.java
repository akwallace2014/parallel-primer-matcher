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

import main.Directionality;
import main.Sequence;

/**
 * JUnit Test for Sequence class
 * sX = sequence number X
 * c = complement of sequence
 * r = reverse of sequence
 * 
 * @author Alisa Wallace
 * @version 1.0
 */

public class SequenceTest {

    Directionality five = Directionality.FIVE_PRIME;
    Directionality three = Directionality.THREE_PRIME;

    String s1 = "ACGTACGTAAAA";
    String s1c = "TTTTACGTACGT";
    String s1r = "AAAATGCATGCA";
    String s1rc = "TGCATGCATTTT";


    String s2 = "AAAAA";
    String s2c = "TTTTT";
    String s2r = "AAAAA";
    String s2rc = "TTTTT";

    String s3 = "TCTCTCTC";
    String s3c = "GAGAGAGA";
    String s3r = "CTCTCTCT";
    String s3rc = "AGAGAGAG";


    // test constructor (pass)
    @Test
    public void testConstructorBasicPass() {

        Sequence seq1 = new Sequence("s1", s1, five);
        Sequence seq2 = new Sequence("s2", s2, five);
        Sequence seq3 = new Sequence("s3", s3, five);

        assertTrue(s1.equals(seq1.sequence()));
        assertTrue(s2.equals(seq2.sequence()));
        assertTrue(s3.equals(seq3.sequence()));
    }

    // test constructor string transformations
    @Test
    public void testConstructorTransformations() {

        String t1 = "A C G T A C G T A A A A";
        Sequence seq1 = new Sequence("t1", t1, five);
        assertTrue(s1.equals(seq1.sequence()));

        String t2 = "acgtacgtaaaa";
        Sequence seq2 = new Sequence("t2", t2, five);
        assertTrue(s1.equals(seq2.sequence()));

    }

    // test constructor exceptions
    @Test
    public void testConstructorExceptions() {

        String e1 = null;
        String e2 = "";
        String e3 = "     ";
        String e5 = "A";

        try {
            Sequence seq1 = new Sequence("exception", e1, five);
            fail();
        }
        catch (IllegalArgumentException e) {}

        try {
            Sequence seq1 = new Sequence("exception", e2, five);
            fail();
        }
        catch (IllegalArgumentException e) {}

        try {
            Sequence seq1 = new Sequence("exception", e3, five);
            fail();
        }
        catch (IllegalArgumentException e) {}

        try {
            Sequence seq1 = new Sequence("exception", e5, five);
            fail();
        }
        catch (IllegalArgumentException e) {}

    }

    // test complement
    @Test
    public void testComplement() {

        Sequence seq1 = new Sequence("s1", s1, five);
        Sequence seq2 = new Sequence("s2", s2, five);
        Sequence seq3 = new Sequence("s3", s3, five);

        assertEquals(s1c, Sequence.generateComplement(s1));
        assertEquals(s2c, Sequence.generateComplement(s2));
        assertEquals(s3c, Sequence.generateComplement(s3));
    }

    @Test
    public void testComplementExceptions() {
        String e = "Not native bases!";

        try {
            Sequence.generateComplement(e);
            fail();
        }
        catch (IllegalArgumentException exc) {}
    }

    // test reverse

    @Test
    public void testReverse() {

        Sequence seq1 = new Sequence("s1", s1, five);
        Sequence seq2 = new Sequence("s2", s2, five);
        Sequence seq3 = new Sequence("s3", s3, five);

        assertNull(seq1.reverse());
        assertNull(seq2.reverse());
        assertNull(seq3.reverse());

        seq1.setReverse();
        seq2.setReverse();
        seq3.setReverse();

        String seq1R = seq1.reverse();
        String seq2R = seq2.reverse();
        String seq3R = seq3.reverse();

        assertEquals(s1r, seq1R);
        assertEquals(s2r, seq2R);
        assertEquals(s3r, seq3R);

        assertEquals(s1r, Sequence.generateReverse(seq1.sequence()));
        assertEquals(s2r, Sequence.generateReverse(seq2.sequence()));
        assertEquals(s3r, Sequence.generateReverse(seq3.sequence()));
    }

    // test reverse complement

    @Test
    public void testReverseComplement() {

        Sequence seq1 = new Sequence("s1", s1, five);
        Sequence seq2 = new Sequence("s2", s2, five);
        Sequence seq3 = new Sequence("s3", s3, three);

        assertNull(seq1.reverseComplement());
        assertNull(seq2.reverseComplement());
        assertNull(seq3.reverseComplement());

        seq1.setReverseComplement("s1 RC");
        seq2.setReverseComplement("s2 RC");
        seq3.setReverseComplement("s3 RC");

        Sequence seq1RC = seq1.reverseComplement();
        Sequence seq2RC = seq2.reverseComplement();
        Sequence seq3RC = seq3.reverseComplement();

        assertEquals(s1rc, seq1RC.sequence());
        assertEquals(s2rc, seq2RC.sequence());
        assertEquals(s3rc, seq3RC.sequence());

        assertEquals(three, seq1RC.direction());
        assertEquals(three, seq2RC.direction());
        assertEquals(five, seq3RC.direction());
    }

    // TODO test equals
}
