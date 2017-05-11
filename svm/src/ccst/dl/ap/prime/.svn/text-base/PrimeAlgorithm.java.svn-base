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
package prime;

import algorithm.abs.ClusterInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class PrimeAlgorithm {

    private PrimeGraph graph;
    private PriorityQueue<PrimeEdge> edges;
    private Collection<Integer> sources;

    public PrimeAlgorithm(PrimeGraph graph, Collection<Integer> sources) {
        this.graph = graph;
        this.sources = sources;
        this.edges = new PriorityQueue<PrimeEdge>();
        init(sources);
    }

    public Map<Integer, ClusterInteger> run() {
        Map<Integer, ClusterInteger> ret = new TreeMap<Integer, ClusterInteger>();

        while (edges.size() > 0) {
            PrimeEdge minEdge = edges.poll();
            //System.out.println(minEdge.getWeight());

            Integer nodeFromStr = minEdge.getFrom();
            Integer nodeToStr = minEdge.getTo();
            Double weight = minEdge.getWeight();

            PrimeNode nodeFrom = graph.getNode(nodeFromStr);
            PrimeNode nodeTo = graph.getNode(nodeToStr);

            if (nodeTo.getDistance() == null || nodeTo.getDistance() > nodeFrom.getDistance() + weight) {
                nodeTo.setDistance(nodeFrom.getDistance() + weight);
                nodeTo.setSourceName(nodeFrom.getSourceName());

                for (Entry<Integer, Double> entryEdge : nodeTo.getEdges().entrySet()) {
                    PrimeEdge edge = new PrimeEdge(nodeFromStr, entryEdge.getKey(), entryEdge.getValue());
                    edges.add(edge);
                }
            }
        }

        for (Integer source : sources) {
            ClusterInteger clust = new ClusterInteger(source);
            clust.add(source);
            ret.put(source, clust);
            //  current.addNode(node);
        }

        for (PrimeNode node : graph.getNodes()) {
            if (node.getSourceName() != null) {
                if (!sources.contains(node.getName())) {
                    ClusterInteger cluster = ret.get(node.getSourceName());
                    cluster.add(node.getName());
                }
            }
        }

        return ret;
    }

    private void init(Collection<Integer> sources) {
        for (Integer source : sources) {
            PrimeNode node = graph.getNode(source);
            node.setDistance(Double.valueOf(0));
            node.setSourceName(source);
            //        current.addNode(node);
            for (Entry<Integer, Double> entryEdge : node.getEdges().entrySet()) {
                PrimeEdge edge = new PrimeEdge(source, entryEdge.getKey(), entryEdge.getValue());
                edges.add(edge);
            }
        }
    }
}
