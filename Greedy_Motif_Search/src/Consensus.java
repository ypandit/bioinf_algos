
/**
 * @author Yogesh
 *
 */
public class Consensus {
    private String motif;
    private int[] start;
    private int score;

    public Consensus(String motif, int[] start, int score) {
        this.setMotifString(motif);
        this.setStartPositions(start);
        this.setConsensusScore(score);
    }

    public void setMotifString(String motif) {
        this.motif = motif;
    }

    public String getMotifString() {
        return motif;
    }

    public void setStartPositions(int[] start) {
        this.start = start;
    }

    public int[] getStartPositions() {
        return start;
    }

    public void setConsensusScore(int score) {
        this.score = score;
    }

    public int getConsensusScore() {
        return score;
    }
}
