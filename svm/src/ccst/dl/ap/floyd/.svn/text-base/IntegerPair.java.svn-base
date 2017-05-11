package floyd;

/**
 *
 * @author misiek (mw219725@gmail.com)
 */
public class IntegerPair implements Comparable<IntegerPair> {

    private Integer first;
    private Integer second;

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public IntegerPair(Integer first, Integer second) {
        this.first = first;
        this.second = second;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        } else {
            IntegerPair pair = (IntegerPair) obj;
            return (this.compareTo(pair) == 0);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.first != null ? this.first.hashCode() : 0);
        hash = 71 * hash + (this.second != null ? this.second.hashCode() : 0);
        return hash;
    }

    public int compareTo(final IntegerPair pair) {
        if (pair == null) {
            return -1;
        }
        if (this.getFirst() > pair.getFirst()) {
            return -1;
        } else if (this.getFirst() < pair.getFirst()) {
            return 1;
        } else if (this.getSecond() > pair.getSecond()) {
            return -1;
        } else if (this.getSecond() < pair.getSecond()) {
            return 1;
        } else {
            return 0;
        }
    }
}
