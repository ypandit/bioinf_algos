This is a repository for some algorithms implemented as class projects.

To run the Greedy motif search:
	
	java -jar GreedyMotifSearch.jar <input.fasta> <l-mer>

and to run the Profile HMM:

	ant
	java -jar ProfileHMM.jar <input.profile> --use-slide-window <true|false>