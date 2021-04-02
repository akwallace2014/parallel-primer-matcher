/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */
package main;

import java.util.*;

/**
 * Encapsulates the DNA replication results of a primer and template Sequence.
 * Uses SequenceMatcher to find locations, if any, where primer and template
 * are complementary, meaning the primer can anneal and extend there.
 * Then replication products are generated based on the directionality of the 
 * primer, which will always be included in the replication product.
 * Match locations are stored in an ArrayList of integers with corresponding
 * replication products for those locations in a separate ArrayList of Sequences.
 * ArrayLists are not returned - the application programmer must use the number
 * of matches to iterate through and access location or product. 
 * 
 * Requires that primer and template be of opposite directionality regardless
 * of sequence contents to support the logic of DNA replication.
 * 
 * @author Alisa Wallace
 * @version 1.4
 */
public class ReplicationProduct {
    
    private String name;        // name for this combo
    private Sequence template;  // template sequence
    private Sequence primer;    // primer sequence
    private ArrayList<Integer> matchLocations;
    private ArrayList<Sequence> matchProducts;
    private boolean analyzed;   // whether or not results are available
    
    /**
     * Constructor.
     * Generates a new object to encapsulate replication products bewtween a
     * template and a primer.  Primer and template must be of opposite
     * directionality.
     * 
     * @param name name for this primer/template combo
     * @param template template Sequence
     * @param primer primer Sequence
     * @throws IllegalArgumentException if template and primer are of same direction 
     */
    public ReplicationProduct(String name, Sequence template, Sequence primer) throws IllegalArgumentException {

        validateDirectionality(template, primer);
        this.template = template;
        this.primer = primer;

        this.name = name;

        matchLocations = new ArrayList<Integer>();
        matchProducts = new ArrayList<Sequence>();

        analyzed = false;
    }

    /**
     * Determines if there were any matches.
     * @return true if empty, false if not
     */
    public boolean isEmpty() {
        return matchLocations.isEmpty();
    }

    /**
     * Whether or not the template/primer combo have been analyzed for matches.
     * @return true if analyzed, false if not
     */
    public boolean isAnalyzed() {
        return analyzed;
    }

    /**
     * Number of matches found for template/primer combo.
     * @return number of matches in the ArrayList
     */
    public int numMatches() {
        return matchLocations.size();
    }

    /**
     * Returns the index in the template where the primer sequence was found
     * for a given match
     * @param i index in ArrayList
     * @return the location of the match
     */
    public int getMatchLocation(int i) {
        return matchLocations.get(i);
    }

    /**
     * Returns the replication product for a given match between the primer and 
     * template
     * @param i index in ArrayList
     * @return Sequence result of the replication product
     */
    public Sequence getMatchProduct(int i) {
        return matchProducts.get(i);
    }

    /**
     * Getter for this template.
     * @return template
     */
    public Sequence getTemplate() {
        return template;
    }

    /**
     * Getter for this primer
     * @return primer
     */
    public Sequence getPrimer() {
        return primer;
    }

    /**
     * Uses sequential SequenceMatcher method to find all matches and 
     * populate list of replication products.  
     * Results are sorted such that the earliest matches are first in the 
     * output list (when reading template left to right)
     */
    public void findAllMatchesSequential() {
        
        String text = template.sequence();
        String pattern = primer.sequence();
        
        SequenceMatcher sm = new SequenceMatcher(text, pattern);
        matchLocations = sm.findMatchesSequential(true);
        Collections.sort(matchLocations);

        for (int i = 0; i < matchLocations.size(); i++) {
            int location = matchLocations.get(i);

            String product;
            if (primer.direction().equals(Directionality.FIVE_PRIME)) {
               product = text.substring(location);
            }
            else {
                product = text.substring(0, location + primer.length());
            }

            product = Sequence.generateComplement(product);
    
            String productName = this.name + " product " + i;
            Sequence s = new Sequence(productName, product, primer.direction());
            matchProducts.add(s);
        }

        analyzed = true;
    }

    /**
     * Parallelized version of findAllMatchesSequential
     * @param numThreads number of threads to use in parallel algorithm
     */
    public void findAllMatchesParallel(int numThreads) {
        
        // if (template.reverseComplement() == null) 
        //     template.setReverseComplement(null);
        
        // Sequence templateRC = template.reverseComplement();
        // String text = templateRC.sequence();
        // String pattern = primer.sequence();
        
        // ParallelMatcher pm = new ParallelMatcher(text, pattern, numThreads);
        // matchLocations = pm.findMatches();
        // Collections.sort(matchLocations);

        String text = template.sequence();
        String pattern = primer.sequence();
        
        SequenceMatcher sm = new SequenceMatcher(text, pattern);
        matchLocations = sm.findMatchesParallel(numThreads, true);
        Collections.sort(matchLocations);

        for (int i = 0; i < matchLocations.size(); i++) {
            int location = matchLocations.get(i);

            String product;
            if (primer.direction().equals(Directionality.FIVE_PRIME)) {
               product = text.substring(location);
            }
            else {
                product = text.substring(0, location + primer.length());
            }

            product = Sequence.generateComplement(product);
    
            String productName = this.name + " product " + i;
            Sequence s = new Sequence(productName, product, primer.direction());
            matchProducts.add(s);
        }

        analyzed = true;
    }


    /**
     * Ensures that 2 Sequences are of opposite directionality for proper
     * DNA replication logic.
     * @param a first sequence
     * @param b second sequence
     * @throws IllegalArgumentException if Sequences are the same directionality
     */
    private void validateDirectionality(Sequence a, Sequence b) throws IllegalArgumentException {
        if (a.direction().equals(b.direction()))
            throw new IllegalArgumentException("Invalid sequence inputs.  Sequences must be of opposite directionality or no replication can occur.");
    }
}
