/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

public class Sequence {

    private String name;
    private String sequence;            // 5' to 3'
    private String complement;          // 5' to 3'
    private String reverseComplement;   // 3' to 5'
    private int length;
    private boolean doubleStranded;

    /**
     * Constructor
     * Accepts a single sequence and auto-generates reverse complement
     * Trims incoming sequence of all whitespaces and converts to all uppercase
     * Only native bases (A, C, G, T) supported
     * 
     * @param name identifier for this sequence
     * @param sequence the DNA sequence 5' to 3'
     * @param doubleStranded true if this sequence represents dsDNA, false if
     * single-stranded
     * @throws IllegalArgumentException for the following cases:
     *      - Blank sequence
     *      - Null sequence
     *      - Sequence < 2 characters long
     *      - Non-native bases detected in sequence
     */
    Sequence(String name, String sequence, boolean doubleStranded) throws IllegalArgumentException {

        validateSequence(sequence);

        this.name = name;
        String trimmed = sequence.replaceAll("\\s", "");
        trimmed = trimmed.toUpperCase();

        this.sequence = trimmed;
        this.doubleStranded = doubleStranded;
        length = trimmed.length();
        this.complement = generateComplement(trimmed);
        this.reverseComplement = generateReverse(complement);
    }

    /**
     * Getter for sequence
     * 
     * @return the sequence read 5' to 3', left to right
     */
    public String sequence() {
        return sequence;
    }

    /**
     * Getter for complement
     * 
     * @return the complement of the sequence read 5' to 3', left to right
     */
    public String complement() {
        return complement;
    }

    /**
     * Getter for reverse complement
     * 
     * @return the reverse complement of the sequence read 3' to 5', left to right
     */
    public String reverseComplement() {
        return reverseComplement;
    }

    /**
     * Getter for length of sequences
     * 
     * @return length of the sequences
     */
    public int length() {
        return length;
    }


    /**
     * Getter for name of the sequences
     * 
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * Getter for double-stranded
     * 
     * @return true if double-stranded, false if single-stranded
     */
    public boolean isDoubleStranded() {
        return doubleStranded;
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
        
        return this.sequence.equals(thatObj.sequence);
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
    private String generateComplement(String seq) throws IllegalArgumentException {
        // FIXME can be parallelized

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

        validateSequence(seq);
        
        // FIXME can be parallelize
        char[] reverse = new char[seq.length()];

        for (int i = 0, j = seq.length() - 1; i < seq.length() && j >= 0; i++, j--) {

            reverse[j] = seq.charAt(i);
        }

        return new String(reverse);

    }

    /**
     * Validates a sequence for use in operations that involve Sequence
     * objects
     * 
     * @param str the String to validate
     * @throws IllegalArgumentException for the following cases:
     *      - Blank sequence
     *      - Null sequence
     *      - Sequence < 2 characters long
     */
    private static void validateSequence(String str) throws IllegalArgumentException {

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
