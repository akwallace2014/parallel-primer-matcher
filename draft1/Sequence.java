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

        validateString(sequence);

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
     * Getter for forward strand
     * 
     * @return the forward strand, read 5' to 3', left to right
     */
    public String getForward() {
        return fwdStrand;
    }

    /**
     * Getter for reverse strand
     * 
     * @return the reverse strand, read 5' to 3', left to right
     */
    public String getReverse() {
        return revStrand;
    }


    /**
     * Determines whether 2 Sequence objects are equal
     * Does not account for name, only sequence contents 
     * Does not test size or revStrand fields as they are all derived from fwdStrand field
     * @return true if internal sequences are the same, false if not
     */
    @Override
    public boolean equals(Object that) {

        if (this == that) 
            return true;

        if (!(that instanceof Sequence)) 
            return false;   
        
        Sequence thatObj = (Sequence) that;
        
        return this.fwdStrand.equals(thatObj.fwdStrand);
        }

    /**
     * Generates the complement of a sequence (read 5' to 3')
     * Trims whitespaces from sequence
     * Only processes native bases (A/a, G/g, C/c, T/t)
     * 
     * @param seq the DNA sequence
     * @return the complement of the sequence in 5' to 3' directionality
     * @throws IllegalArgumentException if non-native bases detected
     */
    public static String generateComplement(String seq) throws IllegalArgumentException {
        // FIXME can be parallelized

        validateString(seq);
        seq = seq.trim();

        char[] original = seq.toCharArray();
        char[] complement = new char[seq.length()];

        final int MAX = seq.length();
        final int MIN = 0;

        for (int o = MIN, c = (MAX - 1); o < MAX && c >= MIN; o++, c--) {

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

    /**
     * Reverses the direction of a sequence to read 3' to 5', left to right
     * Does not change the input sequence
     * 
     * @param seq input sequence
     * @return a new sequence read 3' to 5'
     * @throws IllegalArgumentException for the following cases:
     *      - Blank sequence
     *      - Null sequence
     *      - Sequence < 2 characters long
     */
    public static String generateReverse(String seq) throws IllegalArgumentException {

        validateString(seq);

        // FIXME can be parallelize
        char[] reverse = new char[seq.length()];

        for (int i = 0, j = seq.length() - 1; i < seq.length() && j >= 0; i++, j--) {

            reverse[j] = seq.charAt(i);
        }

        return new String(reverse);

    }

    /**
     * Validates a sequence for use in Sequence class operations
     * 
     * @param str the String to validate
     * @throws IllegalArgumentException for the following cases:
     *      - Blank sequence
     *      - Null sequence
     *      - Sequence < 2 characters long
     */
    private static void validateString(String str) throws IllegalArgumentException {

        if (str == null) {
            throw new IllegalArgumentException("sequence cannot be null");
        } 
        else if (str.isBlank()) {
            throw new IllegalArgumentException("sequence cannot be blank");
        } 
        else if (str.length() < 2) {
            throw new IllegalArgumentException("sequence length must be > 2");
        }
    }

}
