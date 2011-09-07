
/**
 * Matrix.java
 *
 * @author Yogesh Pandit
 * Date: Dec 7, 2010
 * Time: 1:23:58 AM
 */
public class Matrix {
    private final int M; // Number of rows
    private final int N; // Number of columns
    private final double[][] data; // M-by-N array

    /**
     * Constructor class
     * @param M Number of Rows
     * @param N Number of Columns
     */
    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new double[this.M][this.N];
    }

    public int getRowCount() {
        return this.M;
    }

    public int getColCount() {
        return this.N;
    }

    public void setValue(double val, int row, int col) {
        this.data [row] [col] = val;
    }

    /**
     *
     */
    public void print() {
        for (int i = 0; i < this.M; i++) {
            System.out.print(Profile.ALPHABETS [i] + "\t");

            for (int j = 0; j < this.N; j++) {
                System.out.print(this.data [i] [j]);
                System.out.print("\t");
            }

            System.out.println();
        }
    }

    public double getValue(int row, int col) {
        return this.data [row] [col];
    }
}
