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
 * Enum to encapsulate the directionality of a single stranded (ss) DNA sequence
 * when it is read from left to right.
 * 
 * 5' (FIVE_PRIME) means the leftmost base has a phosphate group on the 5'
 * carbon of the deoxyribose sugar ring
 * 
 * 3' (THREE_PRIME) means the leftmost base has a hydroxyl (OH) group on the 3'
 * carbon of the deoxyribose sugar ring 
 * 
 * Since DNA is anti-parallel, a double-stranded (ds) DNA sequence will have
 * one strand reading from 5' to 3' left to right and its reverse complement
 * reading 3' to 5' left to right
 * 
 * Directionality is essential to know in a DNA sequence because DNA 
 * replication and extension can only occur 5' to 3' and it impacts how
 * sequences can be compared via string-matching algorithms.
 * 
 * @author Alisa Wallace
 * @version 1.0
 */
public enum Directionality { FIVE_PRIME, THREE_PRIME };
    
