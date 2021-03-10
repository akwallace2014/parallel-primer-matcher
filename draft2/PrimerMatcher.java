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

    public static void main(String[] args) {
        
        String primerSeq = "TTT";
        String template = buildTemplate(25, primerSeq, 10);
        String templateReverse = Sequence.generateReverse(template);
        System.out.println("Template = " + template);
        System.out.println("Template reverse = " + templateReverse);

        Sequence p = new Sequence("test primer", primerSeq, false);
        Sequence t = new Sequence("test template", template, false);
        ReplicationProduct rp = new ReplicationProduct(t, p);
        rp.findAllMatches();
        // expected location = templateSize - (targetLocIndex + primerLength)
        for (int i = 0; i < rp.numMatches(); i++) {
            System.out.println("Match found at template index " + rp.getMatchLocation(i) + ", product = " + rp.getMatchProduct(i));
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
    private static String buildTemplate(int templateSize, String targetPrimer, int targetPrimerLocation) {

        // ArrayList<Integer> templateInserts = reconfigure(templateSize, targetPrimerLocations);

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

        // return sb.toString();
        String templateRC = sb.toString();
        Sequence template = new Sequence(null, templateRC, false);
        return template.reverseComplement();
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

    private static ArrayList<Integer> reconfigure(int size, ArrayList<Integer> locs) {
        ArrayList<Integer> updated = new ArrayList<>();
        
        for (int i = 0; i < locs.size(); i++) {
            int original = locs.get(i);
            updated.add(size - original - 1);
        }
        return updated;
    }
}
