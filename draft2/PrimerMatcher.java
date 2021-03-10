/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

import java.util.*;

public class PrimerMatcher {

    private static final Directionality FIVE = Directionality.FIVE_PRIME;
    private static final Directionality THREE = Directionality.THREE_PRIME;

    public static void main(String[] args) {
        
        String primer = "TTTTTTTTTTTTTT";
        Sequence primer5p = new Sequence("test primer", primer, FIVE);
        int targetLocation = 30;

        System.out.println("Primer sequence 5' " + primer + " 3'");

        String template = buildTemplate(50, primer, targetLocation, FIVE);

        Sequence template5p = new Sequence("test template", template, FIVE);
        template5p.setReverseComplement(null);
        Sequence template3p = template5p.reverseComplement();

        System.out.println("Template sequence 5' " + template5p.sequence() + " 3'");

        System.out.println("Template sequence 3' " + template3p.sequence() + " 5'");

        ReplicationProduct rp = new ReplicationProduct("Test 1", template3p, primer5p);

        rp.findAllMatches();

        System.out.println("Number of matches found = " + rp.numMatches());

        for (int i = 0; i < rp.numMatches(); i++) {
            Sequence product = rp.getMatchProduct(i);
            String sequence = product.sequence();
            System.out.println("Match found at template index " + rp.getMatchLocation(i) + ", product = " + sequence);
        }


        // Create very large template

        // Create Sequence objects from input

        // Create replication result

        // Print output

    }

    /**
     * Generates a random template for testing purposes
     * @param templateSize  size of template (including target primer)
     * @param targetPrimer target primer to insert into template
     * @param targetPrimerLocations indices where target primer should occur (in the reverse complement of this template)
     * @return the complete template
     */
    private static String buildTemplate(int templateSize, String targetPrimer, int targetPrimerLocation, Directionality primerDirection) {

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

    private static String getRandomBase() {

        double x = Math.random();
        if (x < 0.33) {
            return "C";
        }
        else if (x >= 0.33 && x < 0.66) {
            return "G";
        }
        else {
            return "A";
        }
    }
}
