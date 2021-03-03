/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

/**
 * Encapsulates a double-stranded DNA sequence
 * 
 * @author Alisa Wallace
 * @version 1.0
 * 
 */

public class Sequence {

    private String name;
    private String fwdStrand;   // 5' to 3'
    private String revStrand;   // 5' to 3'
    private int length;

    /**
     * Constructor
     * Trims incoming sequence of whitespaces and coverts to all uppercase
     * Only native bases (A, C, G, T) supported
     * 
     * @param name identifier for this sequence
     * @param sequence the DNA sequence 5' to 3'
     * @param forward true if provided sequence is forward strand, false if it is reverse strand
     * @throws IllegalArgumentException for the following cases:
     *      - Blank sequence
     *      - Null sequence
     *      - Sequence < 2 characters long
     *      - Non-native bases detected in sequence
     */
    Sequence(String name, String sequence, boolean forward) throws IllegalArgumentException {

        if (sequence == null) {
            throw new IllegalArgumentException("sequence cannot be null");
        } 
        else if (sequence.isBlank()) {
            throw new IllegalArgumentException("sequence cannot be blank");
        } 
        else if (sequence.length() < 2) {
            throw new IllegalArgumentException("sequence length must be > 2");
        }

        this.name = name;
        String trimmed = sequence.trim().toUpperCase();

        if (forward) {
            this.fwdStrand = trimmed;
            this.revStrand = generateComplement(trimmed);
        }
        else {
            this.revStrand = trimmed;
            this.fwdStrand = generateComplement(trimmed);
        }
        
        this.length = trimmed.length();

    }

    /**
     * Generates the complement of a sequence
     * Only processes native bases (A/a, G/g, C/c, T/t)
     * 
     * @param seq the DNA sequence
     * @return the complement of the sequence in 5' to 3' directionality
     * @throws IllegalArgumentException if non-native bases detected
     */
    private String generateComplement(String seq) throws IllegalArgumentException {
        // FIXME can be parallelized

        char[] original = seq.toCharArray();
        char[] complement = new char[seq.length()];

        final int MAX = seq.length();
        final int MIN = 0;

        for (int o = MIN, c = (MAX - 1); o < MAX && c <= MIN; o++, c--) {

            char current = original[o];
            char comp;
            switch (current) {
                case 'A':
                    comp = 'T';
                    break;
                case 'C':
                    comp = 'G';
                    break;
                case 'G':
                    comp = 'C';
                    break;
                case 'T':
                    comp = 'A';
                    break;
                default:
                    throw new IllegalArgumentException("Only native bases (A, C, G, T) supported");
            }

            complement[c] = comp;
        }

        return new String(complement);
    }

}
