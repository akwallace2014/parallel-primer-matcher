/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

import java.util.*;

/**
 * If primer can anneal to template replication products will read 5' to 3' 
 * starging with primer sequence.
 * 
 */
public class ReplicationProduct {
    
    private String name;        // name for this combo
    private Sequence template;
    private Sequence primer;
    private ArrayList<Integer> matchLocations;
    private ArrayList<Sequence> matchProducts;
    private boolean analyzed;
    
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

    public boolean isEmpty() {
        return matchLocations.isEmpty();
    }

    public boolean isAnalyzed() {
        return analyzed;
    }

    public int numMatches() {
        return matchLocations.size();
    }

    public int getMatchLocation(int i) {
        return matchLocations.get(i);
    }

    public Sequence getMatchProduct(int i) {
        return matchProducts.get(i);
    }

    public Sequence getTemplate() {
        return template;
    }

    public Sequence getPrimer() {
        return primer;
    }

    public void findAllMatches() {
        
        // if (template.reverseComplement() == null) 
        //     template.setReverseComplement(null);
        
        // Sequence templateRC = template.reverseComplement();
        // String text = templateRC.sequence();
        String text = template.sequence();
        String pattern = primer.sequence();
        
        // matchLocations = Matcher.findMatches(text, pattern);
        SequenceMatcher sm = new SequenceMatcher(text, pattern, 1);
        matchLocations = sm.findMatchesSequential();
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
     * Parallelized version of findAllMatches
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
        
        SequenceMatcher sm = new SequenceMatcher(text, pattern, numThreads);
        matchLocations = sm.findMatchesParallel();
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


    private void validateDirectionality(Sequence a, Sequence b) throws IllegalArgumentException {
        if (a.direction().equals(b.direction()))
            throw new IllegalArgumentException("Invalid sequence inputs.  Sequences must be of opposite directionality or no replication can occur.");
    }
}
