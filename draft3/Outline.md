
 * Alisa Wallace
 * CPSC 5600 Parallel Computing 
 * Seattle University, WQ 2021 with Kevin Lundeen
 * Final Project
 * 
 * This is free and unencumbered software released into the public domain.
 
 # Project Purpose

 The purpose of this project to is locate matching DNA sequences between two inputs.  It is a specificialization of basic string-matching: given one string (the pattern), find all instances where that exact character sequence is located in another, usually larger, string (the text).  

 Here "pattern" and "text" are replaced by more subject-appropriate terms: primer and template, respectively.  Primer and template refer to the two pieces of DNA that must be present for DNA replication, both in-vivo and in-vitro. This application is specifically developed with th in-vitro biochemical method of Polymerase Chain Reaction (PCR) in mind. 

# Context

 In vivo, DNA is not made from scratch and requires a pre-existing strand to replicate - this is the template.  During replication the double-helix structure of DNA is unwound, allowing much smaller, complementary pieces of DNA (or RNA) known as primers to align to the template and initiate replication.  In order for this to happen the primer's sequence must be the reverse complement of the template's sequence according to the base-pairing rules of DNA.  By the same logic, a primer's sequence must therefore match the template strand's reverse complement strand.  Thus string-matching is relevant for DNA sequencing applications.  

 PCR mimics natural DNA replication and is used for two fundamental purposes:
    1.  To make exponential copies of a target DNA sequence (the template)
    2.  To detect if target DNA sequences are present in a sample

PCR products are passed on to detection assays, including sequencing analysis.  It is an extremely widely used method since its inception in the 1970s and is used in virtually every application that involves DNA.

An important first step of PCR is designing primers based on your target template sequence (these primers are often made of synthesized DNA rather than origniating from natural sources).  This is not always a simple task, particularly if you are targeting large genes or even entire genomes.  Though there are a variety of factors to consider for primer design one of the most important is that the primer sequence is specific to your target of interest.  This means it is not repeated in other template sequences that can or will be present but also means it is not repeated within the target sequence itself; otherwise you will end up with multiple products (which can be undesireable depending on your application).

Furthermore, when amplifying large templates it is often necessary to have multiple target replication sites and thus multiple primers.  To make matters even more complicated, two primers are required if you want to amplify both strands of DNA; each strand needs its own primer to replicate and because they are the reverse complements of each other their sequences do not match.  Thus, cross-referencing multiple sequences and knowing which align and where becomes vital for successful PCR.  


 