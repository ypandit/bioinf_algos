
/**
 * @author Yogesh
 *
 */
public class Motif {
    private String motif;
    private int start;
    private int end;
    private int score;

    /**
     *
     * @param motif
     * @param length
     * @param start
     * @param end
     * @param score
     */
    public Motif(String motif, int start, int end, int score) {
        this.motif = motif;
        this.start = start;
        this.end = end;
        this.score = score;
    }

    /**
     *
     * @return
     */
    public String getMotifString() {
        return this.motif;
    }

    /**
     *
     * @return
     */
    public int getLength() {
        return this.motif.length();
    }

    /**
     *
     * @return
     */
    public int getStartPosition() {
        return this.start;
    }

    /**
     *
     * @return
     */
    public int getEndPosition() {
        return this.end;
    }

    /**
     *
     * @return
     */
    public int getScore() {
        return this.score;
    }
}
