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

    @Test
    public void testGenerateComplementPass() {

        String cs1Expected = "TTTTACGTACGT";
        String cs1Actual = Sequence.generateComplement(s1);
        String ccs1 = Sequence.generateComplement(cs1Actual);

        assertTrue(cs1Actual.equals(cs1Expected));
        assertTrue(ccs1.equals(s1));
    }

    @Test
    public void testGenerateReversePass() {

        String rs1Expected = "AAAATGCATGCA";
        String rs1Actual = Sequence.generateReverse(s1);
        String rrs1 = Sequence.generateReverse(rs1Actual);

        assertTrue(rs1Expected.equals(rs1Actual));
        assertTrue(rrs1.equals(s1));
    }
}
