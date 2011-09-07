
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


/**
 * Profile.java
 *
 * @author Yogesh Pandit
 * Date: Dec 2, 2010
 * Time: 1:42:23 AM
 */
public class Profile {
    private static boolean VALID;
    static Profile p;
    private static String SEQUENCE;
    public static String[] ALPHABETS;
    private static Matrix M;
    private static double GAP_OPEN;
    private static double GAP_EXTEND;

    public Profile() {
    }

    /**
     *
     * @param matrix Profile Matrix
     * @param sequence Sequence to be aligned
     * @param gap_init Gap Initiation Penalty
     * @param gap_continue Gap Extension Penalty
     */
    private Profile(Matrix matrix, String sequence, double gap_init, double gap_continue) {
        M = matrix;
        SEQUENCE = sequence;
        GAP_OPEN = gap_init;
        GAP_EXTEND = gap_continue;
    }

    /**
     * @param file Input Profile file
     */
    public void setInputFile(String file) {
        try {
            read(file);
        } catch (IOException ioe) {
            System.out.println("Error: Trouble handling Profile file");
            ioe.printStackTrace();
        }
    }

    /**
     * @param file Input file with Profile
     * @throws IOException Error handling input file
     */
    private static void read(String file) throws IOException {
        List<String> temp = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            temp.add(line);
        }

        String[] nums = temp.get(0).split("\t");
        int ROWS;

        if (nums.length == 1) {
            ROWS = Integer.parseInt(nums [0]);
            GAP_OPEN = 0.0;
            GAP_EXTEND = 0.0;
            System.out.println("Warn: Gap Initiation and Extension Penalty not given");
        } else if (nums.length == 3) {
            ROWS = Integer.parseInt(nums [0]);
            GAP_OPEN = Double.parseDouble(nums [1]);
            GAP_EXTEND = Double.parseDouble(nums [2]);
        } else {
            throw new IllegalStateException("Error: Incorrect Profile file");
        }

        ALPHABETS = new String[ROWS];

        String row = temp.get(1).trim();
        String[] forCount = row.split("\t");
        int COLS = forCount.length - 1;
        M = new Matrix(ROWS, COLS);

        for (int l = 1; l < (ROWS + 1); l++) {
            row = temp.get(l).trim();

            String[] alpha_prob = row.split("\t");
            ALPHABETS [l - 1] = alpha_prob [0];

            for (int v = 1; v < alpha_prob.length; v++) {
                M.setValue(Double.parseDouble(alpha_prob [v]), l - 1, v - 1);
            }
        }

        validate();

        String s = temp.get(temp.size() - 1);

        p = new Profile(M, s, GAP_OPEN, GAP_EXTEND);
    }

    /**
     * @return Returns <code>TRUE|FALSE</code> if Profile is valid
     */
    private static boolean validate() {
        VALID = false;

        for (int c = 0; c < M.getColCount(); c++) {
            double sum = 0.0;

            for (int r = 0; r < M.getRowCount(); r++) {
                sum = sum + M.getValue(r, c);
            }

            if (sum != 1.0) {
                VALID = false;
                throw new IllegalStateException("Error: Incorrect values in Column #: " +
                    c);
            }
        }

        VALID = true;

        return VALID;
    }

    public Matrix getProbabilityMatrix() {
        return M;
    }

    public String[] getAlphabets() {
        return ALPHABETS;
    }

    public String getSequence() {
        return SEQUENCE;
    }

    public double getGapOpenPenalty() {
        return GAP_OPEN;
    }

    public double getGapExtendPenalty() {
        return GAP_EXTEND;
    }

    /**
     *
     */
    public void viewProfile() {
        System.out.println("Size of Profile: " + M.getRowCount() + " x " +
        M.getColCount());
        System.out.println("# of alphabets: " + M.getRowCount());
        System.out.println("Gap opening penalty: " + GAP_OPEN);
        System.out.println("Gap extension penalty: " + GAP_EXTEND);
        System.out.println();
        M.print();
        System.out.println();
        System.out.println("Input sequence: " + SEQUENCE);
        System.out.println();
    }

    /**
     * @return Returns sum of all the probabilities in Profile
     */
    public double getSumOfAllProbabilitiesInMatrix() {
        if (VALID) {
            return (1.0 * M.getColCount());
        } else {
            return 0.0;
        }
    }

    /**
     * @param symbol Alphabet (A|T|G|C)
     * @return Returns index of symbol in profile matrix
     */
    public int getIndexInProfileMatrix(String symbol) {
        int idx = 0;

        for (int i = 0; i < ALPHABETS.length; i++) {
            if (String.valueOf(symbol).equals(ALPHABETS [i])) {
                idx = i;

                break;
            }
        }

        return idx;
    }
}
