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
package algorithm.matrix;

import algorithm.abs.AffinityPropagationAlgorithm;
import algorithm.abs.ConvitsVector;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;
import matrix.DoubleMatrix1D;
import matrix.DoubleMatrix2D;
import matrix.IntegerMatrix1D;

public class MatrixPropagationAlgorithm extends AffinityPropagationAlgorithm {

    private int N;
    private DoubleMatrix2D A;
    private IntegerMatrix1D I = null;
    private DoubleMatrix2D R;
    private DoubleMatrix2D aold = null;
    private DoubleMatrix2D rold = null;
    private DoubleMatrix2D S;
    private double inf = 1000000000.0;
    private int clustersNumber = 0;

    @Override
    public void init() {
        A = new DoubleMatrix2D(N, N, 0);
        R = new DoubleMatrix2D(N, N, 0);
        S = new DoubleMatrix2D(N, N, -inf);
    }

    public int getN() {
        return N;
    }

    public void setN(final int N) {
        this.N = N;
    }

    public IntegerMatrix1D idx(final IntegerMatrix1D C, final IntegerMatrix1D I) {

        IntegerMatrix1D res = new IntegerMatrix1D(C.size());

        for (int i = 0; i < C.size(); i++) {
            res.set(i, I.get(C.get(i).intValue()));
        }

        return res;
    }

    public IntegerMatrix1D tmp(final IntegerMatrix1D C, final IntegerMatrix1D I) {
        IntegerMatrix1D res = C.copy();
        for (int i = 0; i < I.size(); i++) {
            res.set(I.get(i), Integer.valueOf(i));
        }

        return res;
    }

    public void setSimilarities(final double[][] similarities) {
        N = similarities.length;
        this.S = new DoubleMatrix2D(N, N, similarities);
    }

    @Override
    public void setSimilarityInt(final Integer x, final Integer y, final Double sim) {

        //int i = Integer.valueOf(x);
        //int j = Integer.valueOf(y);
        //   if (graphMode == AffinityGraphMode.DIRECTED) {
        //if(x > N || y > N) {
        //    System.out.println("ROZMIAR: "+N+ "query: "+x +" "+y);
        //}
        S.set(x, y, sim.doubleValue());
        //   } else {
        //       S.set(x, y, sim.doubleValue());
        //       S.set(y, x, sim.doubleValue());
        //   }
    }

    @Override
    public void setSimilarity(final String from, final String to, final Double sim) {

        Integer x = getExamplarID(from);
        Integer y = getExamplarID(to);
        //     if (graphMode == AffinityGraphMode.DIRECTED) {
        S.set(x, y, sim.doubleValue());
        //     } else {
        //         S.set(x, y, sim.doubleValue());
        //         S.set(y, x, sim.doubleValue());
        //     }
    }

    @Override
    protected void copyResponsibilies() {
        rold = R.copy();
    }

    @Override
    protected void computeResponsibilities() {
        DoubleMatrix2D YI2;
        DoubleMatrix2D YI;
        DoubleMatrix2D AS;

        double[] pom = new double[N];
        AS = A.plus(S);
        YI = AS.maxr();

        for (int i = 0; i < N; i++) {
            int y = (int) YI.get(i, 0);
            AS.set(i, y, -inf);
        }
        YI2 = AS.maxr();

        for (int i = 0; i < N; i++) {
            pom[i] = YI.get(i, 1);
        }
        DoubleMatrix2D Rep = new DoubleMatrix2D(N, pom);
        R = S.minus(Rep);

        for (int i = 0; i < N; i++) {
            R.set(i, (int) YI.get(i, 0), S.get(i, (int) YI.get(i, 0)) - YI2.get(i, 1));
        }

    }

    @Override
    protected void avgResponsibilies() {
        R = R.mul(1 - getLambda()).plus(rold.mul(getLambda()));
        // System.out.println("R: "+R.toString());
    }

    @Override
    protected void copyAvailabilities() {
        aold = A.copy();
    }

    @Override
    protected void computeAvailabilities() {
        DoubleMatrix1D dA;
        DoubleMatrix2D rp;

        //System.out.println("R: " + R.toString());
        rp = R.max(0);
        for (int i = 0; i < N; i++) {
            rp.set(i, i, R.get(i, i));
        }
        // System.out.println("rp: "+rp.toString());
        A = (new DoubleMatrix2D(N, rp.sum().getVector(0))).transpose().minus(rp);
        //  System.out.println("A-pom: "+A.toString());
        dA = A.diag();

        A = A.min(0);
        for (int i = 0; i < N; i++) {
            A.set(i, i, dA.get(i));
        }
        // System.out.println("A-last: "+A.toString());
    }

    @Override
    protected void avgAvailabilities() {
        //  System.out.println("Aold: "+aold.toString());
        A = A.mul((1 - getLambda())).plus(aold.mul(getLambda()));
        // System.out.println("A: "+A.toString());
    }

    @Override
    protected void computeCenters() {
        DoubleMatrix2D E;
        E = R.plus(A);
        I = E.diag().findG(0);
        clustersNumber = I.size();
    }

    @Override
    public int getClustersNumber() {
        return I.size();
    }

    @Override
    public void setConstPreferences(Double preferences) {
        for (int i = 0; i < N; i++) {
            S.set(i, i, preferences);
        }
    }

    @Override
    public Collection<Integer> getCentersAlg() {
        Collection<Integer> res = new TreeSet<Integer>();
        for (int i = 0; i < I.size(); i++) {
            res.add(Integer.valueOf(I.get(i)));
        }

        return res;
    }

    @Override
    protected Collection<Integer> getAllExamplars() {
        Collection<Integer> res = new TreeSet<Integer>();
        for (int i = 0; i < N; i++) {
            res.add(Integer.valueOf(i));
        }

        return res;
    }

    protected Double tryGetSimilarityInt(Integer i, Integer j) {
        double sim = S.get(i.intValue(), j.intValue());
        if (sim > -inf) {
            return sim;
        } else {
            return null;
        }
    }

    @Override
    protected Double tryGetSimilarity(String from, String to) {
        double sim = S.get(idMapper.get(from), idMapper.get(to));
        if (sim > -inf) {
            return sim;
        } else {
            return null;
        }
    }

    @Override
    protected void calculateCovergence() {
        if (convits != null) {
            Vector<Integer> c = I.getVector();
            for (int i = 0; i < N; i++) {
                Integer ex = Integer.valueOf(i);
                if (c.contains(ex)) {
                    convitsVectors.get(ex).addCovits(true);
                } else {
                    convitsVectors.get(ex).addCovits(false);
                }
            }
        }
    }

    @Override
    protected void initConvergence() {
        //     System.out.println("S: " + S.toString());
        if (convits != null) {
            for (int i = 0; i < N; i++) {
                ConvitsVector vec = new ConvitsVector(convits.intValue(), Integer.valueOf(i));
                vec.init();
                convitsVectors.put(Integer.valueOf(i), vec);
            }
        }
    }

    @Override
    protected void generateNoise() {
        for (int i = 0; i < N; i++) {
            double s = S.get(i, i);
            s = generateNoiseHelp(s);
            S.set(i, i, s);

        }
    }

    @Override
    protected void showInfo() {
    }
}
