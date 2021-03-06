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
    
    private static String template = "AAGTCGCTACGTAACCGTTTACGATAGGGCATTTAATG";
    private static String primer = Sequence.generateComplement(template.substring(28));


    public static void main(String[] args) {
        System.out.println("Template = " + template);
        System.out.println("Primer = " + primer);
        SequenceMatcher test1 = new SequenceMatcher(template, primer);
        HashMap<Integer, String> test1Results = test1.findMatchProducts();

        Set<Integer> indices = test1Results.keySet();
        for (int i : indices) {
            System.out.println("Match at template location " + i + ", output:");
            System.out.println(test1Results.get(i));
        }

    }
}
