
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;


/**
 * HMM.java
 *
 * @author Yogesh Pandit
 *         Date: Dec 2, 2010
 *         Time: 1:42:34 AM
 * @version 2.0
 */
public class HMM {

    private static ArrayList<String[]> hiddenStates;
    DecimalFormat df = new DecimalFormat("0.0000");
    Profile profile;
    private String[] EMISSION_STATE = {"M", "I", "D"};
    boolean useSlideWindow;

    private static double aMM = 0.0;
    private static double aMI = 0.0;
    private static double aMD = 0.0;
    private static double aIM = 0.0;
    private static double aID = 0.0;
    private static double aII = 0.0;
    private static double aDM = 0.0;
    private static double aDD = 0.0;
    private static double aDI = 0.0;
    private static double aBM = 0.0;
    private static double aBI = 0.0;
    private static double aBD = 0.0;
    private static double aME = 0.0;
    private static double aIE = 0.0;
    private static double aDE = 0.0;

    /**
     * @param p Profile Matrix
     * @param slideWindow True if using sliding window
     */
    public HMM(Profile p, boolean slideWindow) {
        this.profile = p;
        this.useSlideWindow = slideWindow;
        calculateTransitionProbabilities();
    }

    /**
     * @return Return a <code>String</code> object of Profile HMM Hidden State Path
     */
    private String getStatePath() {
        StringBuffer sb = new StringBuffer();

        if (hiddenStates.size() == 0) {
            return null;
        }

        for (String[] hiddenState : hiddenStates) {
            sb.append(hiddenState[0]);
        }

        return sb.toString();
    }

    /**
     * @return Returns a <code>double</code> for alignment score based on the state transitions
     */
    private double getAlignmentScore() {
        double align = 2.0;
        char[] hStates = getStatePath().toCharArray();

        for (int a = 0; a < (hStates.length - 1); a++) {
            if ((hStates[a] == 'M') && (hStates[a + 1] == 'M')) {
                align += 1.0;
            } else if ((hStates[a] == 'M') && (hStates[a + 1] == 'I')) {
                align -= profile.getGapOpenPenalty();
            } else if ((hStates[a] == 'M') && (hStates[a + 1] == 'D')) {
                align -= profile.getGapOpenPenalty();
            } else if ((hStates[a] == 'I') && (hStates[a + 1] == 'M')) {
                align -= profile.getGapExtendPenalty();
            } else if ((hStates[a] == 'I') && (hStates[a + 1] == 'I')) {
                align -= profile.getGapExtendPenalty();
            } else if ((hStates[a] == 'I') && (hStates[a + 1] == 'D')) {
                align -= profile.getGapOpenPenalty();
            } else if ((hStates[a] == 'D') && (hStates[a + 1] == 'M')) {
                align -= profile.getGapExtendPenalty();
            } else if ((hStates[a] == 'D') && (hStates[a + 1] == 'I')) {
                align -= profile.getGapOpenPenalty();
            } else if ((hStates[a] == 'D') && (hStates[a + 1] == 'D')) {
                align -= profile.getGapExtendPenalty();
            }
        }
        return Double.parseDouble(df.format(align));
    }

    /**
     * @return Returns a {@link StatePath} object of the best viterbi state path
     */
    public StatePath viterbiStatePath() {
        hiddenStates = new ArrayList<String[]>();

        String sequence = profile.getSequence();


        if (sequence.length() < profile.getProbabilityMatrix().getColCount()) {
            this.useSlideWindow = false;
        }

        StatePath sp;

        if (!this.useSlideWindow) {
            sp = doTheDeed(sequence);

        } else {
            int n = sequence.length();
            int l = profile.getProbabilityMatrix().getColCount();
            List<StatePath> slider = new ArrayList<StatePath>();

            for (int i = 0; i < (n - l + 1); i++) {
                String s = sequence.substring(i, i + l);
                StatePath tempSP = doTheDeed(s);
                tempSP.setSequence(sequence);
                tempSP.setSeqStartPosition(i);
                slider.add(tempSP);
                hiddenStates = new ArrayList<String[]>();
            }
            slider = sort(slider);
            sp = slider.get(0);
        }
        return sp;
    }

    private List<StatePath> sort(List<StatePath> list) {
        Collections.sort(list,
                new Comparator<StatePath>() {
                    public int compare(StatePath c1, StatePath c2) {
                        double a = c1.getProbability();
                        double b = c2.getProbability();

                        if (a < b) {
                            return 1;
                        } else if (a > b) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
        return list;
    }

    /**
     * @param sequence DN Sequence to be aligned
     * @return Returns max scoring StatePath object
     */
    private StatePath doTheDeed(String sequence) {
        Hashtable<String, Double> prevComput = new Hashtable<String, Double>();
        char[] seq = sequence.toCharArray();

        for (int S = 0; S < seq.length; S++) {
            boolean begin = false;
            boolean end = false;
            if (S == 0) {
                begin = true;
            } else if (S == seq.length - 1) {
                end = true;
            }

            Hashtable<String, Double> currComput = new Hashtable<String, Double>();

            for (String eS : EMISSION_STATE) {
                double score = computeDPScore(String.valueOf(seq[S]), S,
                        eS, prevComput, begin, end);
                currComput.put(eS, score);
            }

            String[] bestRes = getBestState(currComput);

            String eState = bestRes[0];
            double maxProb = Double.parseDouble(bestRes[1]);
            //System.out.println(eState + "\t" + maxProb);
            hiddenStates.add(new String[]{eState, String.valueOf(maxProb)});

            prevComput = currComput;
        }

        return new StatePath(sequence, getStatePath(), getSumOfProbabilities(), getAlignmentScore());
    }

    /**
     * @return Sum of probabilities in viterbi path
     */
    private double getSumOfProbabilities() {
        double sumProb = 0.0;

        if (hiddenStates.size() == 0) {
            return 0.0;
        }

        for (String[] hiddenState : hiddenStates) {
            sumProb = sumProb + Double.parseDouble(hiddenState[1]);
        }

        return Double.parseDouble(df.format(sumProb));
    }

    /**
     * @param state1 From state in transition
     * @param state2 To state in transition
     * @return Returns state transition probability
     */
    private double getTransitionProbability(String state1, String state2) {
        double prob;

        if (state1.equals("M") && state2.equals("I")) {
            prob = aMI;
        } else if (state1.equals("M") && state2.equals("D")) {
            prob = aMD;
        } else if (state1.equals("M") && state2.equals("M")) {
            prob = aMM;
        } else if (state1.equals("I") && state2.equals("D")) {
            prob = aID;
        } else if (state1.equals("I") && state2.equals("M")) {
            prob = aIM;
        } else if (state1.equals("I") && state2.equals("I")) {
            prob = aII;
        } else if (state1.equals("D") && state2.equals("D")) {
            prob = aDD;
        } else if (state1.equals("D") && state2.equals("M")) {
            prob = aDM;
        } else if (state1.equals("D") && state2.equals("I")) {
            prob = aDI;
        } else if (state1.equals("B") && state2.equals("M")) {
            prob = aBM;
        } else if (state1.equals("B") && state2.equals("I")) {
            prob = aBI;
        } else if (state1.equals("B") && state2.equals("D")) {
            prob = aBD;
        } else if (state1.equals("M") && state2.equals("E")) {
            prob = aME;
        } else if (state1.equals("I") && state2.equals("E")) {
            prob = aIE;
        } else if (state1.equals("D") && state2.equals("E")) {
            prob = aDE;
        } else {
            throw new IllegalStateException("Error: Improper State Transition: " +
                    state1 + "->" + state2);
        }

        return prob;
    }

    /**
     * @param symbol Alphabet (A|T|G|C)
     * @return Returns frequency of occurrence of input symbol in Profile
     */
    public double getBackgroundProbability(String symbol) {
        double dm = profile.getSumOfAllProbabilitiesInMatrix();
        double nm = 0.0;
        String[] A = profile.getAlphabets();
        Matrix M = profile.getProbabilityMatrix();

        for (int i = 0; i < A.length; i++) {
            if (symbol.equals(A[i])) {
                nm = 0.0;

                for (int j = 0; j < profile.getProbabilityMatrix().getColCount(); j++) {
                    nm = nm + M.getValue(i, j);
                }

                break;
            }
        }
        return Double.parseDouble(df.format(nm / dm));
    }

    /**
     * @param symbol Alphabet (A|T|G|C)
     * @return Returns emission probability in Insertion state
     */
    public double getInsertionStateProbability(String symbol) {
        return getBackgroundProbability(symbol);
    }

    /**
     * @param symbol     Alphabet (A|T|G|C)
     * @param pos        Position in the sequence
     * @param state      Recurrec for this emission state
     * @param prevComput Earlier state computation
     * @param begin      True of sequence begins here
     * @param end        True is sequence ends here
     * @return Returns a score for the recurrence
     */
    private double computeDPScore(String symbol, int pos, String state,
                                  Hashtable<String, Double> prevComput, boolean begin, boolean end) {

        int r = profile.getIndexInProfileMatrix(symbol);
        double emitP = 0.0;

        if (pos > profile.getProbabilityMatrix().getColCount() - 1) {
            if (state.equals("M")) {
                emitP = 0.0;
            } else if (state.equals("I")) {
                emitP = getBackgroundProbability(symbol);
            } else if (state.equals("D")) {
                emitP = 0.0;
            }
        } else {
            if (state.equals("M")) {
                emitP = profile.getProbabilityMatrix().getValue(r, pos);
            } else if (state.equals("I")) {
                emitP = getBackgroundProbability(symbol);
            } else if (state.equals("D")) {
                emitP = 0.0;
            }
        }

        double firstPart = Math.log(emitP / getBackgroundProbability(symbol));

        double[] transP = new double[3];

        if (begin) {
            transP[0] = getTransitionProbability("B", state);
            transP[1] = getTransitionProbability("B", state);
            transP[2] = getTransitionProbability("B", state);
        } else if (end) {
            transP[0] = getTransitionProbability(state, "E");
            transP[1] = getTransitionProbability(state, "E");
            transP[2] = getTransitionProbability(state, "E");
        } else {
            transP[0] = getTransitionProbability("M", state);
            transP[1] = getTransitionProbability("I", state);
            transP[2] = getTransitionProbability("D", state);
        }

        double[] secondPart = new double[3];

        if (begin) {
            secondPart[0] = 1.0 + Math.log(transP[0]);
            secondPart[1] = 1.0 + Math.log(transP[1]);
            secondPart[2] = 1.0 + Math.log(transP[2]);
        } else if (end) {
            secondPart[0] = 1.0 + Math.log(transP[0]);
            secondPart[1] = 1.0 + Math.log(transP[1]);
            secondPart[2] = 1.0 + Math.log(transP[2]);
        } else {
            secondPart[0] = prevComput.get("M") + Math.log(transP[0]);
            secondPart[1] = prevComput.get("I") + Math.log(transP[1]);
            secondPart[2] = prevComput.get("D") + Math.log(transP[2]);
        }

        /**
         * If I do getMax(secondPart), handling -Infinity gives an error. So I do the following
         */
        double score1 = firstPart + secondPart[0];
        double score2 = firstPart + secondPart[1];
        double score3 = firstPart + secondPart[2];
        double[] scores = new double[3];
        scores[0] = score1;
        scores[1] = score2;
        scores[2] = score3;

        return Double.parseDouble(df.format(getMax(scores)));
    }

    private String[] getBestState(Hashtable<String, Double> currComput) {
        String[] bestRes = new String[2];
        String state = "";
        double score = -Double.MAX_VALUE;

        for (String eS : EMISSION_STATE) {
            if (currComput.get(eS) > score) {
                score = currComput.get(eS);
                state = eS;
            }
        }

        bestRes[0] = state;
        bestRes[1] = df.format(score);

        return bestRes;
    }

    private double getMax(double[] scores) {
        double score = -Double.MAX_VALUE;

        for (double temp : scores) {
            if (temp > score) {
                score = temp;
            }
        }

        return score;
    }

    /**
     *
     */
    private void calculateTransitionProbabilities() {

        aBM = 0.34;
        aBI = 0.33;
        aBD = 0.33;

        aME = 0.33;
        aIE = 0.34;
        aDE = 0.33;

        aII = Math.pow(10, -profile.getGapExtendPenalty());
        aDD = aII;

        double root1 = 1 - aII;
        double root2 = Math.pow(10, -profile.getGapOpenPenalty());
        if (root1 < root2) {
            aMI = root1;
        } else {
            aMI = root2;
        }

        aID = aMI;
        aMD = aMI;
        aMM = 1 - aMI - aMD;
        aIM = 1 - aII - aMI;
        aDM = aIM;
    }
}
