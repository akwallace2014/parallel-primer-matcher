/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */


import java.util.HashMap;
import java.util.Map;

/**
 * Specifies pattern matching for DNA sequences.
 * Determines not only start location of matches but the sequence and length of
 * the expected product (if any) that would result from using the REVERSE 
 * COMPLEMENT of either the primer or the template sequence provided 
 * in a Polymerase Chain Reaction (PCR) or other
 * DNA replication reaction.
 * 
 * Looks for EXACT match between sequences, meaning 
 * 
 * Example:
 * 
 * @author Alisa Wallace
 * @version 1.0
 * 
 */
public class SequenceMatcher extends Matcher {
    
    // maps start index of match to the sequence of the PCR product resulting
    // from using this primer-template pair
    private Map<Integer, String> matchProducts;  

    /**
     * Constructor
     */
    public SequenceMatcher(String template, String primer) {
        super(template, primer);
        matchProducts = new HashMap<Integer, String>();
    }

    /**
     * Finds all starting locations for primer/template matches and determines
     * the PCR product for using 
     */
    public Map<Integer, String> findMatchProducts() {
        if(!analyzed)
            findMatches();

        if(matches == null) // no matches so no products
            return null;

        // use matches to get substrings and store
        for (int i = 0; i < matches.size(); i++) {
            int matchLoc = matches.get(i);
            String product = template.substring(matchLoc);
            matchProducts.put(matchLoc, product);
        }

        return matchProducts;

    }
}
