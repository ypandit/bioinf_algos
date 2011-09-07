
/**
 * StatePath.java
 *
 * @author Yogesh Pandit
 * Date: 12/12/10
 * Time: 4:18 PM
 */
public class StatePath {
    private double alignScore;
    private double finalProb;
    private String seq;
    private String statePath;
    private int seqStartPosn;

    public StatePath(String sequence, int seqStartPos, String state, double probability,
        double score) {
        this.seq = sequence;
        this.statePath = state;
        this.alignScore = score;
        this.finalProb = probability;
        this.seqStartPosn = seqStartPos;
    }

    public StatePath(String sequence, String state, double probability, double score) {
        this.seq = sequence;
        this.statePath = state;
        this.alignScore = score;
        this.finalProb = probability;
        this.seqStartPosn = 0;
    }

    /**
     *
     * @param sp An existing StatePath
     */
    public StatePath(StatePath sp) {
        this.seq = sp.getSequence();
        this.statePath = sp.getStatePath();
        this.alignScore = sp.getAlignmentScore();
        this.finalProb = sp.getProbability();
        this.seqStartPosn = sp.getSeqStartPosition();
    }

    public String getSequence() {
        return this.seq;
    }

    public String getStatePath() {
        return this.statePath;
    }

    public double getProbability() {
        return this.finalProb;
    }

    public double getAlignmentScore() {
        return this.alignScore;
    }

    public int getSeqStartPosition() {
        return this.seqStartPosn;
    }

    public void setSeqStartPosition(int pos) {
        this.seqStartPosn = pos;
    }

    public void setProbability(double prob) {
        this.finalProb = prob;
    }

    public void view() {
        System.out.println();
        System.out.println("Viterbi State Path: ");
        System.out.println(getAlignment());
        System.out.println("Final Probability: " + getProbability());
        System.out.println("Alignment Score (Based on State Transition): " +
            getAlignmentScore());
    }

    public void setSequence(String sequence) {
        this.seq = sequence;
    }

    public String getAlignment() {
        StringBuffer sb = new StringBuffer();
        String before = "";
        String after = "";
        String s = this.seq;

        if (this.seqStartPosn > 0) {
            before = s.substring(0, this.seqStartPosn);
            after = s.substring((this.seqStartPosn + getStatePath().length()), s.length());
            s = s.substring(this.seqStartPosn, this.seqStartPosn +
                    getStatePath().length());
        }

        sb.append(before);
        sb.append(s);
        sb.append(after);
        sb.append("\n");

        for (int b = 0; b < before.length(); b++) {
            sb.append(" ");
        }

        for (int i = 0; i < getStatePath().length(); i++) {
            sb.append("|");
        }

        sb.append("\n");

        for (int b = 0; b < before.length(); b++) {
            sb.append(" ");
        }

        sb.append(getStatePath());

        return sb.toString();
    }
}
