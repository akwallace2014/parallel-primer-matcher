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
 * Driver class to demonstrate function and performance of ReplicationProduct
 * class and its constituent SequenceMatcher class.
 * 
 * Demonstrates a simple example of primer/template matching and and subsequent
 * replication products via printing to console, then demonstrates sequential
 * vs parallel performance of the matching algorithm in SequenceMatcher.  
 * Performance is quantified by throughput: the number of X sized templates that
 * can be searched in Y time.  Template size and time allowed are adjustable
 * variables, and parallel performance is demonstrated with 1, 2, 4, 8 and 16
 * threads.
 * 
 * Throughput is tested under worst case scenarios (i.e., primer is not found
 * at any location in template).
 * 
 * @author Alisa Wallace
 * @version 1.4
 */
public class PrimerMatcher {

    // for easier reference
    private static final Directionality FIVE = Directionality.FIVE_PRIME;
    private static final Directionality THREE = Directionality.THREE_PRIME;

    private static final long TIME_ALLOWED = 1;     // seconds
    private static int numThreads = 4; 

    private static final int TEMPLATE_SIZE = 1_000_000;
    private static final int PRIMER_SIZE = 20;

    /**
     * Main method.
     * @param args not used.
     */
    public static void main(String[] args) {

        runReplicationProductDemo();
        runThroughputDemo();

    }

    /**
     * Demonstrates SequenceMatcher and ReplicationProduct behavior.
     */
    private static void runReplicationProductDemo() {
        String primer = "T".repeat(12);
        String template = "GCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCTAAAAAAAAAAAAGCTGCTGCTGCT";
        Sequence primer5p = new Sequence("test primer", primer, FIVE);

        Sequence template3p = new Sequence("test template", template, THREE);

        ReplicationProduct rp = new ReplicationProduct("Test 1", template3p, primer5p);

        rp.findAllMatchesParallel(numThreads);

        int printLength = template.length();
        System.out.println("\n------- Input primer & template --------\n");
        printSequence(primer5p, 9, printLength);
        printSequence(template3p, 0, printLength);
        System.out.println();

        System.out.println("-------- Results --------");

        System.out.print("\nFound " + rp.numMatches() + " primer matches at template location(s):");
        for (int i = 0; i < rp.numMatches(); i++) {
            System.out.print(" " + rp.getMatchLocation(i));
            if (i != rp.numMatches() - 1) {
                System.out.print(",");
            }
        }
        System.out.println("\n\nReplication Products:\n");

        for (int i = 0; i < rp.numMatches(); i++) {
            Sequence product = rp.getMatchProduct(i);
            printSequence(product, rp.getMatchLocation(i), printLength);
            printSequence(template3p, 0, printLength);
            System.out.println("\n");
        }
    }

    /**
     * Demonstrates performance of sequential vs parallel matching algorithm.
     */
    private static void runThroughputDemo() {
       
        System.out.println("-------- Demoing Sequential vs Parallel Throughput --------\n");
       
        boolean parallel = false;   // toggle to true to demo parallel
        demoThroughput(parallel);
        parallel = true;
        numThreads = 1;
        demoThroughput(parallel);
        numThreads = 2;
        demoThroughput(parallel);
        numThreads = 4;
        demoThroughput(parallel);
        numThreads = 8;
        demoThroughput(parallel);
        numThreads = 16;
        demoThroughput(parallel);
        System.out.println();
    }

    /**
     * Helper method to print aligned Sequences and their directionality.
     * @param s Sequence to print
     * @param padSize whitespace padding before printing sequence
     * @param fullSize size of longtest sequence being printed, to help with alignment
     */
    private static void printSequence(Sequence s, int padSize, int fullSize) {

        Directionality d = s.direction();
        
        String dir1, dir2;
        if (d.equals(FIVE)) {
            dir1 = " 5' ";
            dir2 = " 3' ";
        }
        else {
            dir1 = " 3' ";
            dir2 = " 5' ";
        }

        String sequence = s.sequence();
        String paddingFront = " ".repeat(padSize);
        int padBackSize = fullSize - padSize - sequence.length();
        String paddingBack = " ".repeat(padBackSize);
        System.out.println(dir1 + paddingFront + sequence + paddingBack + dir2);
    }

    /**
     * Generates a random template for testing purposes with a target
     * primer sequence injected at a specified location
     * @param templateSize  size of template (including target primer)
     * @param targetPrimer target primer to insert into template
     * @param targetPrimerLocations indices where target primer should occur (in the reverse complement of this template)
     * @return the complete template
     */
    private static String buildTemplateWithPrimer(int templateSize, String targetPrimer, int targetPrimerLocation) {

        int index = 0; 
        StringBuilder sb = new StringBuilder();

        while (index < templateSize) {
            // 
            if (index == targetPrimerLocation) {
                sb.append(targetPrimer);
                index += targetPrimer.length();
            }
            else {
                sb.append(getRandomBase());
                index++;
            }
        }

        return sb.toString();
    }

    /**
     * Generates a random sequence of designated size.
     * @param templateSize  size of sequence
     * @return random sequence of designated size
     */
    private static String buildTemplateNoPrimer(int templateSize) {

        int index = 0; 
        StringBuilder sb = new StringBuilder();

        while (index < templateSize) {
                sb.append(getRandomBase());
                index++;
        }

        return sb.toString();
    }

    /**
     * Generates a random base (A, C, G, T) with a 25% chance of either.
     * @return a random base
     */
    private static String getRandomBase() {

        double x = Math.random();
        if (x < 0.25) {
            return "C";
        }
        else if (x >= 0.25 && x < 0.5) {
            return "G";
        }
        else if (x >= 0.5 && x < 0.75) {
            return "T";
        }
        else {
            return "A";
        }
    }

    /**
     * Demonstrates throughput of sequential vs parallel matching algorithms.
     * Uses a randomly generated template and a primer that contains no 
     * base characters and thus will not be found.  Since no reverse complements
     * are generated this primer can be stored as a Sequence object without issue.
     * 
     * @param parallel if true, runs parallel method, if false runs sequential method
     */
    private static void demoThroughput(boolean parallel) {
        
        String primer = "X".repeat(PRIMER_SIZE);

        int totalSequences = 0;

        String template = buildTemplateNoPrimer(TEMPLATE_SIZE);
        Sequence primer5p = new Sequence("Latency test primer", primer, FIVE);
        Sequence template3p = new Sequence("Latency test template", template, THREE);

        ReplicationProduct rp = new ReplicationProduct("Latency test", template3p, primer5p);

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + TIME_ALLOWED * 1000) {

            if (parallel)
                rp.findAllMatchesParallel(numThreads);
            else {
                rp.findAllMatchesSequential();
            }
            totalSequences++;
        }

        String type;
        if (parallel)
            type = "\nParallel (" + numThreads + " thread) ";
        else {
            type = "\nSequential ";
        }
        System.out.println(type + "search: " + totalSequences + " " + TEMPLATE_SIZE + " bp templates searched in " + TIME_ALLOWED + " seconds");

    }
}
