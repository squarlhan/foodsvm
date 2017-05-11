/* ===========================================================
 * APGraphClusteringPlugin : Java implementation of affinity propagation
 * algorithm as Cytoscape plugin.
 * ===========================================================
 *
 *
 * Project Info:  http://bioputer.mimuw.edu.pl/modevo/
 * Sources: http://code.google.com/p/misiek/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * APGraphClusteringPlugin  Copyright (C) 2008-2010
 * Authors:  Michal Wozniak (code) (m.wozniak@mimuw.edu.pl)
 *           Janusz Dutkowski (idea) (j.dutkowski@mimuw.edu.pl)
 *           Jerzy Tiuryn (supervisor) (tiuryn@mimuw.edu.pl)
 */
package algorithm.smart;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Map;
import java.util.TreeSet;

public class ExamplarsCollection {

    private Examplar[] examplars;
    private Integer convits = null;
    private int size;

    public ExamplarsCollection(int size, final Integer convits) {
        this.size = size;
        this.convits = convits;
        this.examplars = new Examplar[size];
        for (int i = 0; i < size; i++) {
            examplars[i] = null;
        }
    }

    public ExamplarsCollection(int size) {
        this.size = size;
        this.examplars = new Examplar[size];
        for (int i = 0; i < size; i++) {
            examplars[i] = null;
        }
    }

    public void setConvits(Integer convits) {
        this.convits = convits;
    }

    public int size() {
        return size;
    }

    public void setSimilarity(final Integer from, final Integer to, final double sim) {
        Examplar exfrom = examplars[from];
        if (exfrom == null) {
            exfrom = new Examplar(from, convits);
            examplars[from] = exfrom;
        }
        Examplar exto = examplars[to];
        if (exto == null) {
            exto = new Examplar(to, convits);
            examplars[to] = exto;
        }
        exfrom.createSibling(sim, to);
        exto.addEdgeIn(from);
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < size; i++) {
            ret.append(i);
            ret.append(": ");
            ret.append(examplars[i].toString());
            ret.append("\n");
        }
        return ret.toString();
    }

    public Examplar[] getExamplars() {
        return examplars;
    }

    public Collection<Integer> getKeySet() {
        Collection<Integer> keys = new TreeSet<Integer>();
        for (int i = 0; i < size; i++) {
            keys.add(i);
        }
        return keys;
    }

    private String valuesToString(String kind) {
        int N = size;
        StringBuffer res = new StringBuffer();
        Double[][] matrix = new Double[N][N];
        Map<Integer, Integer> mapper = new TreeMap<Integer, Integer>();

        Integer[] examplarsPom = new Integer[N];
        int pos = 0;
        for (Examplar examplar : examplars) {
            examplarsPom[pos] = (examplar.getName());
            pos++;
        }
        Arrays.sort(examplarsPom);

        for (int i = 0; i < N; i++) {
            mapper.put(examplarsPom[i], Integer.valueOf(i));
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = Double.valueOf(-100000);
            }
        }

        for (Examplar examplar : examplars) {
            Collection<EdgeOutData> sibling = examplar.getSiblingMap().values();
            for (EdgeOutData data : sibling) {
                Integer row = mapper.get(examplar.getName());
                Integer col = mapper.get(data.getName());

                Double value;
                if (kind.equals("R")) {
                    value = data.getR();
                } else if (kind.equals("S")) {
                    value = data.getS();
                } else {
                    value = data.getA();
                }
                matrix[row][col] = value;
            }
        }

        for (int i = 0; i < N; i++) {

            for (int j = 0; j < N; j++) {
                res.append(matrix[i][j] + " ");
            }
            res.append("\n");
        }

        return res.toString();
    }

    public String responsiblitiesToString() {
        return valuesToString("R");
    }

    public String availibilitiesToString() {
        return valuesToString("A");
    }

    public String similaritiesToString() {
        return valuesToString("S");
    }

    Examplar get(int k) {
        return examplars[k];
    }
}
