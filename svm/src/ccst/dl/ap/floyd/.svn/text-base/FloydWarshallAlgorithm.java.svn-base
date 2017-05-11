package floyd;

import algorithm.abs.Cluster;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author misiek (mw219725@gmail.com)
 */
public class FloydWarshallAlgorithm {

    private Map<IntegerPair, Double> d = new TreeMap<IntegerPair, Double>();
    private Map<IntegerPair, Integer> prev = new TreeMap<IntegerPair, Integer>();

    public void init(Collection<Integer> examplars, Collection<Integer> centers) {
        for (Integer v1 : examplars) {
            for (Integer v2 : examplars) {
                d.put(new IntegerPair(v1, v2), null);
                prev.put(new IntegerPair(v1, v2), null);
            }
            d.put(new IntegerPair(v1, v1), Double.valueOf(0));
        }
    }

    public void addEdge(Integer from, Integer to, Double weight) {
        //    d.get()
    }

    public Map<Integer, Cluster<Integer>> run() {



        return null;
    }
}
