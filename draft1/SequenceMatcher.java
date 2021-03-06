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
public class SequenceMatcher {
    
    // maps start index of match to the sequence of the PCR product resulting
    // from using this primer-template pair
    private HashMap<Integer, String> matchProducts;
    private Sequence template;  

    
    /**
     * Constructor.
     * Accepts parameters on the template sequence to be searched.
     *  
     * @param templateName name associated with this sequence
     * @param templateSeq 5' to 3' 
     * @param templateDirection
     */
    public SequenceMatcher(String templateName, String templateSeq, Strand templateDirection) {

        this.template = new Sequence(templateName, templateSeq, templateDirection);

        matchProducts = new HashMap<Integer, String>();
    }

    /**
     * Additional constructor.
     * Take a pre-existing Sequence object as a parameter.
     *  
     * @param template a Sequence object encapsulating the template
     */
    public SequenceMatcher(Sequence template) {

        this.template = template;

        matchProducts = new HashMap<Integer, String>();
    }



    /**
     * Finds all starting locations for primer/template matches and determines
     * the PCR product for using this primer with this object's template.
     * 
     * If there's a sequence alignment, the FWD primer's sequence will match
     * the template's FWD strand sequence exactly; it will pair with the 
     * template's REV strand and generate the FWD sequence of the template 
     * during replication.  The opposite is true for a REV primer. 
     *  
     * @param primerSeq 5' to 3' sequence of the primer
     * @param primerDirection FWD (forward) or REV (reverse)
     */
    public HashMap<Integer, String> findMatchProducts(String primerSeq, Strand primerDirection) {
        
        String searchTemplate;
        if (primerDirection.equals(Strand.FWD)) {
            searchTemplate = template.forward();
        }
        else {
            searchTemplate = template.reverse();
        }

        
        // use matches to get substrings and store
        for (int i = 0; i < matches.size(); i++) {
            int matchLoc = matches.get(i);
            String product = template.substring(matchLoc);
            matchProducts.put(matchLoc, product);
        }

        return matchProducts;

    }
}
