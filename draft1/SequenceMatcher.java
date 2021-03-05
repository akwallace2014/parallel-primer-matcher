/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */


import java.util.Map;

public class SequenceMatcher extends Matcher {
    
    private Map<Integer, String> matchOutput;  

    public SequenceMatcher(Sequence template, Sequence primer) {
        super(template.forward(), primer.forward());
    }
}
