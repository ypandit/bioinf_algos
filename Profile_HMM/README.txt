This code for ProfileHMM algorithm has been written by Yogesh Pandit as an assignment for CSCI-59000 course.

Using this program, the user can align a given sequence with an already give profile. For input sequences longer than the profile we support sliding windows also. If not, there is no emission in Match state. In insertion state, the emission probability is the background probability.

Also, here the state transition probabilities are being computed dynamically based on the Gap Initiation and Gap Extension Penalties

For compiling the code, go the the folder and type the following at the shell/cmd

	> ant

To clean the compilation,

	> ant clean

For running the program (once jar file has been built) at the shell/cmd run the following

	> java -jar ProfileHMM.jar <input.profile> --use-slide-window <true|false>



No external packages have been used. Complete code is written by the author.

Contributions;

Yogesh Pandit:
	ant build
	handling the profile
	recurrences for HMM
	computing state probabilities
	getting the viterbi path
	alignment
	alignment score

