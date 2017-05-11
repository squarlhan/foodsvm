package matrix;

public class DoubleMatrix1D extends Matrix1D<Double> implements DoubleMatrix1DInterface {

    public DoubleMatrix1D(final Double[] vector) {
        super(vector.length);
        this.setVector(new Double[this.size()]);
        for (int i = 0; i < this.size(); i++) {
            this.setValue(i, vector[i]);
        }
    }

    public DoubleMatrix1D(final int N) {
        super(N);
    }

    public DoubleMatrix1D(final int N, final double t) {
        super(N);
        this.setVector(new Double[N]);
        for (int i = 0; i < N; i++) {
            this.setValue(i, Double.valueOf(t));
        }
    }

    public void set(final int i, final double t) {
        super.set(i, Double.valueOf(t));
    }

    public IntegerMatrix1D findG(final double x) {
        int count = 0;
        for (int i = 0; i < this.size(); i++) {
            if (this.getValue(i).doubleValue() > x) {
                count++;
            }
        }
        IntegerMatrix1D res = new IntegerMatrix1D(count);
        count = 0;
        for (int i = 0; i < this.size(); i++) {
            if (this.getValue(i).doubleValue() > x) {
                res.set(count, Integer.valueOf(i));
                count++;
            }
        }
        return res;
    }

    @Override
    public DoubleMatrix1D copy() {
        DoubleMatrix1D res = new DoubleMatrix1D(this.size());
        for (int i = 0; i < this.size(); i++) {
            res.set(i, this.get(i));
        }

        return res;
    }
}
