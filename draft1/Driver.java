/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

import java.util.*;

public class Driver {

    public static void main(String[] args) {
        
    }

    private static ReplicationProduct findMatchProducts(Sequence template, Sequence primer) {

        String searchTemplate;

        ArrayList<Integer> matches = Matcher.findMatches(template.sequence(), primer.sequence());

        if (matches.isEmpty())
            return null;
        
        HashMap<Integer, String> products = new HashMap<>();
        
        // use matches to get substrings and store
        for (int i = 0; i < matches.size(); i++) {
            int matchLoc = matches.get(i);
            String product = template.sequence().substring(matchLoc);
            products.put(matchLoc, product);
        }

        return products;

    }
}
