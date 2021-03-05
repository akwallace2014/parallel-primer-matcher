/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */


import java.util.Map;

/**
 * Specific to DNA sequences
 */
public class SequenceMatcher extends Matcher {
    
    // maps start index of match to the sequence of the PCR product resulting
    // from using this primer-template pair
    private Map<Integer, String> matchProduct;  

    public SequenceMatcher(Sequence template, Sequence primer) {
        super(template.forward(), primer.forward());
    }
}
