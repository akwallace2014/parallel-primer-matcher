/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

/**
 * Notes: to conserve space, setting reverse sequence and reverse
 * complement must be done manually 
 */
public class Sequence {

    private String name;
    private String sequence;            // DNA sequence 
    private String reverse;         
    private Directionality direction;   // 5' or 3' when read left to right
    private Sequence reverseComplement; 
    private int length;

    /**
     * Constructor
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
    Sequence(String name, String sequence, Directionality direction) throws IllegalArgumentException {

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
     * 
     * @return
     */
    public Directionality direction() {
        return direction;
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
     * Getter for reverse sequence
     * If a sequence direction is 5', then the reverse sequence will be 3'
     * Example: sequence = 5' CAT 3', reverse = 3' TAC 5'
     * 
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
     * 
     * @param name name to be stored for the reverse complement
     * 
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
     * Getter for reverse complement
     * Example 1:  sequence = 5' CAT 3', reverse complement = 3' GTA 5'
     * Example 2:  sequence = 3' GTC 5', reverse complement = 5' CAC 3'
     * 
     * @return a Sequence object encapsulating this sequence's reverse complement or null if it has not been set yet
     */
    public Sequence reverseComplement() {
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
     * Determines whether 2 Sequence objects are equal
     * Does not account for name, only sequence and directionality
     * @return true if internal sequences are the same, false if not
     */
    @Override
    public boolean equals(Object that) {

        if (this == that) 
            return true;

        if (!(that instanceof Sequence)) 
            return false;   
        
        Sequence thatObj = (Sequence) that;
        
        return this.direction.equals(thatObj.direction) && this.sequence.equals(thatObj.sequence);
        }

    
    public static String generateComplement(String seq) throws IllegalArgumentException {
        validateSequence(seq);

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
