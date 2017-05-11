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

import algorithm.abs.AffinityPropagationAlgorithm;
import algorithm.abs.ConvitsVector;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

/** You have to set parameters and do init() befor clustering. */
public class SmartPropagationAlgorithm extends AffinityPropagationAlgorithm {

    private ExamplarsCollection examplars = null;
    private double INF = 100000000;
    protected Collection<Integer> centers;

    public ExamplarsCollection getExamplars() {
        return examplars;
    }

    public void setExamplars(final ExamplarsCollection examplars) {
        this.examplars = examplars;
    }

    protected void avgAvailabilities() {
        for (Examplar examplar : examplars.getExamplars()) {
            Collection<EdgeOutData> siblings = examplar.getSiblingMap().values();
            for (EdgeOutData sibling : siblings) {
                sibling.setA(sibling.getA() * (1 - lambda) + lambda * sibling.getAold());
            }
        }
    }

    protected void avgResponsibilies() {
        for (Examplar examplar : examplars.getExamplars()) {
            Collection<EdgeOutData> siblings = examplar.getSiblingMap().values();
            for (EdgeOutData sibling : siblings) {
                sibling.setR(sibling.getR() * (1 - lambda) + lambda * sibling.getRold());
            }
        }
    }

    protected void computeAvailabilities() {
        for (int k = 0; k < examplars.size(); k++) {
            Examplar exK = examplars.get(k);
            double sum = 0;
            for (int ip : exK.getEdgesIn()) {
                if (ip != k) {
                    Examplar exIp = examplars.get(ip);
                    double ripk = exIp.getSiblingMap().get(k).getR();
                    sum = sum + Math.max(0, ripk);
                }
            }

            for (int i : exK.getEdgesIn()) {
                Examplar exI = examplars.get(i);
                EdgeOutData edgeIK = exI.getSiblingMap().get(k);
                if (i == k) {
                    edgeIK.setA(sum);
                } else {
                    double rik = exI.getSiblingMap().get(k).getR();
                    double sumI = sum - Math.max(0, rik);
                    double rkk = exK.getSiblingMap().get(k).getR();

                    edgeIK.setA(Math.min(0, rkk + sumI));
                }

            }
        }
    }

    protected void computeCenters() {
        Collection<Integer> ret = new TreeSet<Integer>();
        for (Examplar examplar : examplars.getExamplars()) {
            EdgeOutData sibling = examplar.getSiblingMap().get(examplar.getName());
            double e = sibling.getA() + sibling.getR();
            if (e > 0) {
                ret.add(examplar.getName());
            }
        }

        centers = ret;
    }

    protected void computeResponsibilities() {
        for (int i = 0; i < examplars.size(); i++) {
            Examplar exI = examplars.get(i);

            int bestK = i;
            double max1 = -INF;
            double max2 = -INF;
            for (EdgeOutData sibIKp : exI.getSiblingMap().values()) {
                double pom = sibIKp.getA() + sibIKp.getS();
                if (pom > max1) {
                    max2 = max1;
                    max1 = pom;
                    bestK = sibIKp.getName();
                } else if (pom > max2) {
                    max2 = pom;
                }
            }

            for (EdgeOutData sibIK : exI.getSiblingMap().values()) {
                double sik = sibIK.getS();
                double maxpom;
                if (sibIK.getName() == bestK) {
                    maxpom = max2;
                } else {
                    maxpom = max1;
                }
                sibIK.setR(sik - maxpom);
            }
        }
    }

    protected void copyAvailabilities() {
        for (Examplar examplar : examplars.getExamplars()) {
            Collection<EdgeOutData> siblings = examplar.getSiblingMap().values();
            for (EdgeOutData sibling : siblings) {
                sibling.setAold(sibling.getA());
            }

        }
    }

    protected void copyResponsibilies() {
        for (Examplar examplar : examplars.getExamplars()) {
            Collection<EdgeOutData> siblings = examplar.getSiblingMap().values();
            for (EdgeOutData sibling : siblings) {
                sibling.setRold(sibling.getR());
            }

        }
    }

    /**
     *
     * @param from
     * @param to
     * @param sim
     */
    @Override
    public void setSimilarityInt(final Integer from,
            final Integer to,
            final Double sim) {
        //     if (graphMode == AffinityGraphMode.DIRECTED) {
        examplars.setSimilarity(from, to, sim);
        //    } else {
        //        examplars.setSimilarity(from, to, sim);
        //        examplars.setSimilarity(to, from, sim);
        //    }
    }

    @Override
    public void setSimilarity(final String from,
            final String to,
            final Double sim) {
        Integer i = getExamplarID(from);
        Integer j = getExamplarID(to);
        //      if (graphMode == AffinityGraphMode.DIRECTED) {
        examplars.setSimilarity(i, j, sim);
        //    } else {
        //        examplars.setSimilarity(i, j, sim);
        //        examplars.setSimilarity(j, i, sim);
        //    }
    }

    @Override
    public void init() {
        //examplars = new ExamplarsCollection(convits);
    }

    @Override
    public void setN(final int N) {
        //  System.out.println("tworze");
        examplars = new ExamplarsCollection(N, convits);
    }

    public void setSimilarities(double[][] sim) {
        int N = sim.length;
        if (N == 0) {
            return;
        }

        if (N != sim[0].length) {
            return;
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.setSimilarityInt(i, j, sim[i][j]);
            }

        }

    }

    @Override
    public int getClustersNumber() {
        return centers.size();
    }

    @Override
    public void setConstPreferences(Double preferences) {
        for (int i = 0; i < examplars.size(); i++) {
            setSimilarityInt(i, i, preferences);
        }
    }

    @Override
    public Collection<Integer> getCentersAlg() {
        return new TreeSet<Integer>(centers);
    }

    @Override
    protected Collection<Integer> getAllExamplars() {
        return new TreeSet<Integer>(examplars.getKeySet());
    }

    protected Double tryGetSimilarityInt(Integer i, Integer j) {

        Examplar ix = examplars.getExamplars()[i];
        EdgeOutData sibling = ix.getSiblingMap().get(j);
        if (sibling == null) {
            return null;
        } else {
            return sibling.getS();
        }
    }

    @Override
    protected Double tryGetSimilarity(String from, String to) {

        Integer i = idMapper.get(from);
        Integer j = idMapper.get(to);

        Examplar ix = examplars.getExamplars()[i];
        EdgeOutData sibling = ix.getSiblingMap().get(j);
        if (sibling == null) {
            return null;
        } else {
            return sibling.getS();
        }
    }

    @Override
    protected void calculateCovergence() {
        if (convits != null) {
            for (int ex = 0; ex < examplars.size(); ex++) {
                if (centers.contains(ex)) {
                    convitsVectors.get(ex).addCovits(true);
                } else {
                    convitsVectors.get(ex).addCovits(false);
                }
            }
        }
    }

    @Override
    protected void initConvergence() {
        if (convits != null) {

            for (int ex = 0; ex < examplars.size(); ex++) {
                ConvitsVector vec = new ConvitsVector(convits.intValue(), ex);
                vec.init();
                convitsVectors.put(ex, vec);
            }
        }
    }

    @Override
    protected void generateNoise() {
        for (Examplar examplar : examplars.getExamplars()) {
            Map<Integer, EdgeOutData> siblings = examplar.getSiblingMap();
            EdgeOutData sibling = siblings.get(examplar.getName());
            double s = sibling.getS();
            s = generateNoiseHelp(s);
            sibling.setS(s);
        }
    }

    @Override
    protected void showInfo() {
    }
}
