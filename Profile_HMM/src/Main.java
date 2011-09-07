
/**
 * Main.java
 *
 * @author Yogesh Pandit
 * Date: Dec 2, 2010
 * Time: 1:41:43 AM
 */
public class Main {

    /**
     * @param args Default arguments
     */
    public static void main(String[] args) {
        long time = System.currentTimeMillis();

        if (args.length == 3) {
            System.out.println("Profile HMM implementation for CSCI-59000");


			String infile = args [0];
            Profile p = new Profile();
            p.setInputFile(infile);
			//p.viewProfile();

			boolean useSlideWindow = false;
            if (args[1].equals("--use-slide-window") ) {
                useSlideWindow = Boolean.parseBoolean(args[2]);
            }

            HMM hmm = new HMM(p, useSlideWindow);
            StatePath sp = hmm.viterbiStatePath();
            sp.view();

            time = System.currentTimeMillis() - time;
            System.out.println();
            System.out.println("Approx. execution time for ProfileHMM: " + time + " milliseconds");

        } else {
            System.out.println("Error: Incorrect arguments");
            System.out.println("Usage:\n\t java -jar ProfileHMM.jar <input.profile> --use-slide-window <true|false>");
        }

    }
}
