/*
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 */

/**
 * Generic string-matching class
 * Uses a naive string-matching algorithm to find a pattern string in a 
 * template string.
 * 
 * Tests for 100% match between pattern and template
 */
public class Matcher {

    private static final int MIN_LENGTH = 1;
    private String template;    // string in which we are searching for pattern
    private String pattern;     // string we are looking for in template

    /**
     * Constructor
     * @param template
     * @param pattern
     * @throws IllegalArgumentException
     */
    public Matcher(String template, String pattern) throws IllegalArgumentException{
        validateInput(template, pattern);
        this.template = template;
        this.pattern = pattern;

    }

    private void validateInput(String template, String pattern) throws IllegalArgumentException {
        if (template == null || pattern == null)
            throw new IllegalArgumentException("Input strings cannot be null.");

        if (template.isBlank() || pattern.isBlank())
            throw new IllegalArgumentException("Input strings cannot be blank.");
        
        if (template.length() < MIN_LENGTH || pattern.length() < MIN_LENGTH)
            throw new IllegalArgumentException("Input strings must have length >= " + MIN_LENGTH);

        if (template.length() < pattern.length())
            throw new IllegalArgumentException("Template must have length >= pattern");
    }
}
