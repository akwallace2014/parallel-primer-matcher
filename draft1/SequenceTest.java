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
 * JUnit Test for Sequence class
 * sX = sequence number X
 * c = complement of sequence
 * r = reverse of sequence
 * 
 * @author Alisa Wallace
 * @version 1.0
 */

public class SequenceTest {

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

        Sequence seq1 = new Sequence("s1", s1, false);
        Sequence seq2 = new Sequence("s2", s2, false);
        Sequence seq3 = new Sequence("s3", s3, true);

        assertTrue(s1.equals(seq1.sequence()));
        assertTrue(s2.equals(seq2.sequence()));
        assertTrue(s3.equals(seq3.sequence()));
    }

    // test constructor string transformations
    @Test
    public void testConstructorTransformations() {

        String t1 = "A C G T A C G T A A A A";
        Sequence seq1 = new Sequence("t1", t1, false);
        assertTrue(s1.equals(seq1.sequence()));

        String t2 = "acgtacgtaaaa";
        Sequence seq2 = new Sequence("t2", t2, false);
        assertTrue(s1.equals(seq2.sequence()));

    }

    // test constructor exceptions
    @Test
    public void testConstructorExceptions() {

        String e1 = null;
        String e2 = "";
        String e3 = "     ";
        String e4 = "Not native bases!";
        String e5 = "A";

        try {
            Sequence seq1 = new Sequence("exception", e1, false);
            fail();
        }
        catch (IllegalArgumentException e) {}

        try {
            Sequence seq1 = new Sequence("exception", e2, false);
            fail();
        }
        catch (IllegalArgumentException e) {}

        try {
            Sequence seq1 = new Sequence("exception", e3, false);
            fail();
        }
        catch (IllegalArgumentException e) {}

        try {
            Sequence seq1 = new Sequence("exception", e4, false);
            fail();
        }
        catch (IllegalArgumentException e) {}

        try {
            Sequence seq1 = new Sequence("exception", e5, false);
            fail();
        }
        catch (IllegalArgumentException e) {}

    }

    // test complement
    @Test
    public void testComplement() {

        Sequence seq1 = new Sequence("s1", s1, false);
        Sequence seq2 = new Sequence("s2", s2, false);
        Sequence seq3 = new Sequence("s3", s3, true);

        assertEquals(s1c, seq1.complement());
        assertEquals(s2c, seq2.complement());
        assertEquals(s3c, seq3.complement());
    }

    // test reverse

    @Test
    public void testReverse() {

        Sequence seq1 = new Sequence("s1", s1, false);
        Sequence seq2 = new Sequence("s2", s2, false);
        Sequence seq3 = new Sequence("s3", s3, true);

        assertEquals(s1r, Sequence.generateReverse(seq1.sequence()));
        assertEquals(s2r, Sequence.generateReverse(seq2.sequence()));
        assertEquals(s3r, Sequence.generateReverse(seq3.sequence()));
    }

    // test reverse complement

    @Test
    public void testReverseComplement() {

        Sequence seq1 = new Sequence("s1", s1, false);
        Sequence seq2 = new Sequence("s2", s2, false);
        Sequence seq3 = new Sequence("s3", s3, true);

        assertEquals(s1rc, seq1.reverseComplement());
        assertEquals(s2rc, seq2.reverseComplement());
        assertEquals(s3rc, seq3.reverseComplement());
    }
}
