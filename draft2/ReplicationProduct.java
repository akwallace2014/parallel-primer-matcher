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

    public String getMatchProduct(int i) {
        return matchProducts.get(i);
    }

    public Sequence getTemplate() {
        return template;
    }

    public Sequence getPrimer() {
        return primer;
    }

    public void findAllMatches() {
        
        if (template.reverseComplement() == null) 
            template.setReverseComplement();
        
        Sequence templateRC = template.reverseComplement();
        String text = templateRC.sequence();
        String pattern = primer.sequence();
        
        matchLocations = Matcher.findMatches(text, pattern);

        for (int i = 0; i < matchLocations.size(); i++) {
            int location = matchLocations.get(i);
            String product = text.substring(location);
            String productName = this.name + " product " + i;
            Sequence s = new Sequence(productName, product, primer.direction());
            matchProducts.add(s);
        }

        analyzed = true;
    }

    private validateDirectionality(Sequence a, Sequence b) throws IllegalArgumentException {
        if (a.direction().equals(b.direction()))
            throw new IllegalArgumentException("Invalid sequence inputs.  Sequences must be of opposite directionality or no replication can occur.");
    }
}
