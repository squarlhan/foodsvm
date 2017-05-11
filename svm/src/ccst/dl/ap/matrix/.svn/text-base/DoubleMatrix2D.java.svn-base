package matrix;

public class DoubleMatrix2D implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private double[][] matrix;
    private int N,  M;

    public DoubleMatrix2D(final int M, final double[] v) {
        this.N = v.length;
        this.M = M;
        this.matrix = new double[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                this.matrix[i][j] = v[i];
            }
        }
    }

    public DoubleMatrix2D(final int N, final int M, final double[][] m) {
        this.N = N;
        this.M = M;
        this.matrix = new double[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                this.matrix[i][j] = m[i][j];
            }
        }
    }

    public DoubleMatrix2D(final int N, final int M, final double v) {
        this.N = N;
        this.M = M;
        this.matrix = new double[N][M];
        setValues(v);
    }

    private DoubleMatrix2D(final int N, final int M) {
        this.N = N;
        this.M = M;
        this.matrix = new double[N][M];
    }

    private DoubleMatrix2D(final DoubleMatrix2D m) {
        this.N = m.N;
        this.M = m.M;
        this.matrix = new double[N][M];
        set(m.matrix);
    }

    public DoubleMatrix2D sum() {
        DoubleMatrix2D res = new DoubleMatrix2D(1, M, 0);
        for (int j = 0; j < M; j++) {
            double s = 0;
            for (int i = 0; i < N; i++) {
                s += this.matrix[i][j];

            }
            res.matrix[0][j] = s;
        }

        return res;
    }

    public DoubleMatrix1D diag() {
        DoubleMatrix1D res = new DoubleMatrix1D(N, 0);
        for (int i = 0; i < N; i++) {
            res.set(i, Double.valueOf(this.matrix[i][i]));
        }

        return res;
    }

    public DoubleMatrix2D transpose() {
        DoubleMatrix2D res = new DoubleMatrix2D(M, N);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                res.matrix[j][i] = this.matrix[i][j];
            }
        }

        return res;
    }

    public void set(final int i, final int j, final double v) {
        matrix[i][j] = v;
    }

    public double get(final int i, final int j) {
        return matrix[i][j];
    }

    public double[] getVector(final int i) {
        return matrix[i];
    }

    /*   public double[][] get() {
    return matrix;
    }*/
    public void set(final int n, final int m, final double[][] matrix) {
        this.N = n;
        this.M = m;
        set(matrix);
    }

    public void set(final double[][] matrix) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    public void setValues(final double v) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                matrix[i][j] = v;
            }
        }
    }

    public void plusTo(final DoubleMatrix2D m) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                matrix[i][j] += m.get(i, j);
            }
        }
    }

    public DoubleMatrix2D plus(final DoubleMatrix2D m) {
        DoubleMatrix2D result = new DoubleMatrix2D(N, M);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                result.set(i, j, this.matrix[i][j] + m.get(i, j));
            }
        }
        return result;
    }

    public void minusTo(final DoubleMatrix2D m) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                matrix[i][j] -= m.get(i, j);
            }
        }
    }

    public DoubleMatrix2D minus(final DoubleMatrix2D m) {
        DoubleMatrix2D result = new DoubleMatrix2D(N, M);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                result.set(i, j, this.matrix[i][j] - m.get(i, j));
            }
        }
        return result;
    }

    public DoubleMatrix2D mul(final double c) {
        DoubleMatrix2D result = new DoubleMatrix2D(N, M);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                result.set(i, j, this.matrix[i][j] * c);
            }
        }
        return result;
    }

    public void plusTo(final double[][] m) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                matrix[i][j] = m[i][j];
            }
        }
    }

    public void mulTo(final double c) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                matrix[i][j] *= c;
            }
        }
    }

    public DoubleMatrix2D copy() {
        return new DoubleMatrix2D(this);
    }

    public int getM() {
        return M;
    }

    public void setM(final int M) {
        this.M = M;
    }

    public int getN() {
        return N;
    }

    public void setN(final int N) {
        this.N = N;
    }

    public DoubleMatrix2D maxr() {
        DoubleMatrix2D res = new DoubleMatrix2D(N, 2, 0);

        for (int i = 0; i < N; i++) {
            int maxj = 0;
            for (int j = 0; j < M; j++) {
                if (this.get(i, j) > this.get(i, maxj)) {
                    maxj = j;
                }
            }
            res.set(i, 0, maxj);
            res.set(i, 1, this.matrix[i][maxj]);
        }
        return res;
    }

    public IntegerMatrix1D maxrIndexes() {
        IntegerMatrix1D res = new IntegerMatrix1D(N);

        for (int i = 0; i < N; i++) {
            int maxj = 0;
            for (int j = 0; j < M; j++) {
                if (this.matrix[i][j] > this.matrix[i][maxj]) {
                    maxj = j;
                }
            }
            res.set(i, Integer.valueOf(maxj));
        }
        return res;
    }

    public DoubleMatrix2D max(final double v) {
        DoubleMatrix2D res = new DoubleMatrix2D(this);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (v > this.matrix[i][j]) {
                    res.matrix[i][j] = v;
                }
            }
        }
        return res;
    }

    public DoubleMatrix2D getColumns(final IntegerMatrix1D indexes) {
        DoubleMatrix2D res = new DoubleMatrix2D(this.N, indexes.size());
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < indexes.size(); j++) {
                res.set(i, j, this.matrix[i][indexes.get(j).intValue()]);
            }
        }

        return res;
    }

    public DoubleMatrix1D getColumn(final int j) {
        DoubleMatrix1D res = new DoubleMatrix1D(N);
        for (int i = 0; i < N; i++) {
            res.set(i, this.matrix[i][j]);
        }

        return res;
    }

    public DoubleMatrix1D getRow(final int i) {
        DoubleMatrix1D res = new DoubleMatrix1D(M);
        for (int j = 0; j < M; j++) {
            res.set(j, this.matrix[i][j]);
        }

        return res;
    }

    public DoubleMatrix2D min(final double v) {
        DoubleMatrix2D res = new DoubleMatrix2D(this);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (v < this.matrix[i][j]) {
                    res.matrix[i][j] = v;
                }
            }
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                res.append(this.matrix[i][j]);
                res.append(" ");
            }
            res.append("\n");

        }
        return res.toString();
    }
}