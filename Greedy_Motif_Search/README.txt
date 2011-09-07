This code for GreedyMotifSearch algorithm has been written as an assignment for CSCI-59000 course.

This program searches sequences multiple protein sequences for top 20 motifs using a greedy search algorithm.
It takes input as a FastA file and the l-mer is the size of the window/motif.


For running the program at the shell/cmd run the following:

  java -jar GreedyMotifSearch.jar <input.fasta> <l-mer>


We have used the BioJava package for file handling. Rest of the code is completely written by the authors.

Contributions;

Yogesh Pandit has written the code for file handling, running iterations and scoring
Rohit Jadhav has written the code for scoring.

**Note: The scoring is not very efficient, right now ;-)
