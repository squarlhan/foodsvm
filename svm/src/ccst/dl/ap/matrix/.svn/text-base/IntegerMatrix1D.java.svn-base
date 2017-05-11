package matrix;

public class IntegerMatrix1D extends Matrix1D<Integer> {

    public IntegerMatrix1D(final int N) {
        super(N);
        this.setVector(new Integer[N]);
        for (int i = 0; i < N; i++) {
            setValue(i, Integer.valueOf(0));
        }
    }

    public static IntegerMatrix1D range(final int r) {
        IntegerMatrix1D res = new IntegerMatrix1D(r);
        res.setVector(new Integer[r]);
        for (int i = 0; i < r; i++) {
            res.setValue(i, Integer.valueOf(i));
        }
        return res;
    }

    @Override
    public IntegerMatrix1D copy() {
        IntegerMatrix1D res = new IntegerMatrix1D(this.size());
        for (int i = 0; i < this.size(); i++) {
            res.set(i, this.get(i));
        }

        return res;
    }
}
