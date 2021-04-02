/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

 package main;

/**
 * This class encapsulates a single stranded DNA sequence.
 * It also includes two static class methods for convenient manipulation of the
 * String representation of a sequence.
 * 
 * Only native bases (A, C, G, T) are currently supported in the sequence 
 * String when generating the reverse complement.  To save time and account for
 * very large input Strings the constructor does not test for native bases -
 * instead input is verified when the reverse complement is generated.  Though
 * the reverse complement of a sequence is a field it is by default set to null
 * and the reverse complement must be manually set (with another Sequence
 * object) or generated.  This is to again account for very large input Strings
 * conserve space unless the application programmer deems it necessary.  The
 * same is true for setting and storing the reverse of a sequence. 
 * 
 * Sequence examples:
 * sequence:            5' A A A C G T C G 3'
 * complement:             T T T G C A G C      (no inherent directionality)
 * reverse complement:  3' T T T G C A G C 5'   (directional)
 * reverse:             3' G C T G C A A A 5'   (directional)
 * 
 * @author Alisa Wallace
 * @version 2.0  
 * 
 */
public class Sequence {

    private String name;                // name for this sequence
    private String sequence;            // DNA sequence (e.g ACGT)
    private String reverse;             // Sequence read in opposite direction
    private Directionality direction;   // 5' or 3' when read left to right
    private Sequence reverseComplement; // 
    private int length;                 // length of the sequence

    /**
     * Constructor.
     * Accepts a single sequence and auto-generates reverse complement
     * Trims incoming sequence of all whitespaces and converts to all uppercase
     * Only native bases (A, C, G, T) supported
     * 
     * Defaults to reverse and reverseComplement fields being null to 
     * conserve space in instances of very large sequences.  These fields
     * must be manually set instead.
     * 
     * @param name identifier for this sequence
     * @param sequence the DNA sequence
     * @param direction whether sequence is read 5' to 3' (FIVE_PRIME) or 
     * 3' to 5' (THREE_PRIME)
     * @throws IllegalArgumentException for the following cases:
     *      - Blank sequence
     *      - Null sequence
     *      - Sequence < 2 characters long
     *      - Non-native bases detected in sequence
     */
    public Sequence(String name, String sequence, Directionality direction) throws IllegalArgumentException {

        validateSequence(sequence);

        this.name = name;
        String trimmed = sequence.replaceAll("\\s", "");
        trimmed = trimmed.toUpperCase();
        this.sequence = trimmed;

        this.length = this.sequence.length();

        this.direction = direction;

        reverseComplement = null;

        reverse = null;
    }

    /**
     * Getter for direction field
     * @return direction
     */
    public Directionality direction() {
        return direction;
    }


    /**
     * Getter for sequence
     * @return the sequence of bases
     */
    public String sequence() {
        return sequence;
    }

    /**
     * Getter for reverse sequence
     * If a sequence direction is 5', then the reverse sequence will be 3'
     * Example: sequence = 5' CAT 3', reverse = 3' TAC 5'
     * @return the reverse of the sequence or null if it's not set
     */
    public String reverse() {
        return reverse;
    }

    /**
     * Auto-generates and sets reverse sequence field for this Sequence.
     */
    public void setReverse() {
        if (reverse == null) {
            reverse = generateReverse(this.sequence);
        }
    }


    /**
     * Auto-generates and sets the reverse complement for this Sequence.  
     * @param name name to be stored for the reverse complement Sequence
     */
    public void setReverseComplement(String name) {
        
        if (reverseComplement == null) {
            String rcName;
            if (name == null || name.isEmpty()) 
                rcName = this.name + " RC";
            else {
                rcName = name;
            }

            Directionality d;
            if (direction.equals(Directionality.FIVE_PRIME))
                d = Directionality.THREE_PRIME;
            else {
                d = Directionality.FIVE_PRIME;
            }
            
            String rcSeq = generateComplement(this.sequence);

            this.reverseComplement = new Sequence(rcName, rcSeq, d);
        }
    }

    /**
     * Other setter for reverse complement.
     * Accepts a pre-existing Sequence object.
     * @param rc the Sequence object that is this sequence's reverse complement
     * @throws IllegalArgumentException if Sequence is not of the opposite
     * directionality of this sequence(e.g. if this sequence is 5', rc must be
     * 3')
     */
    public void setReverseComplementSequence(Sequence rc) throws IllegalArgumentException {
        
        if (rc.direction().equals(this.direction)) {
            throw new IllegalArgumentException("The reverse complement of this sequence must be of the opposite sequence direction (e.g., 3' if this sequence is 5' and vice versa).");
        }

        reverseComplement = rc;
    }

    /**
     * Getter for reverse complement
     * Example 1:  sequence = 5' CAT 3', reverse complement = 3' GTA 5'
     * Example 2:  sequence = 3' GTC 5', reverse complement = 5' CAG 3'
     * 
     * @return a Sequence object encapsulating this sequence's reverse complement or null if it has not been set yet
     */
    public Sequence reverseComplement() {
        return reverseComplement;
    }

    /**
     * Getter for length of sequences
     * @return length of the sequences
     */
    public int length() {
        return length;
    }


    /**
     * Getter for name of the sequences
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * Determines whether 2 Sequence objects are equal
     * @return true if internal sequences are the same, false if not
     */
    @Override
    public boolean equals(Object that) {
        // TODO add in other fields to test
        if (this == that) 
            return true;

        if (!(that instanceof Sequence)) 
            return false;   
        
        Sequence thatObj = (Sequence) that;
        
        return this.direction.equals(thatObj.direction) && this.sequence.equals(thatObj.sequence);
        }

    
    /**
     * Class method to generate the complement of a provided sequence String.
     * Note the returned String implies no directionality.
     * Only native bases accepted (Characters A/a, C/c, G/g, and T/t).
     * Removes all whitespaces and casts all characters to upper-case.
     * Example:  input = CAT, output = GTA
     */
    public static String generateComplement(String seq) throws IllegalArgumentException {
        
        validateSequence(seq);
        seq = seq.replaceAll("\\s", "");
        seq = seq.toUpperCase();
        char[] original = seq.toCharArray();
        char[] complement = new char[seq.length()];

        for (int i = 0; i < seq.length(); i++) {

            char current = original[i];
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

            complement[i] = comp;
        }

        return new String(complement);
    }  

    /**
     * Reverses the direction of a sequence 
     * Application programmer must keep track of directionality 
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
