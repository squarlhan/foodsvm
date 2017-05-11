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

import java.util.Map;
import java.util.TreeMap;

public class PrimeNode implements Comparable<PrimeNode> {

    private Double distance;
    private Integer sourceName;
    private Integer name;
    private Map<Integer, Double> edges;

    public PrimeNode(Integer nodename) {
        this.name = nodename;
        this.edges = new TreeMap<Integer, Double>();
    }

    public int size() {
        return edges.size();
    }

    public Integer getName() {
        return name;
    }

    public void addEdge(Integer edgeName, Double sim) {
        this.edges.put(edgeName, sim);
    }

    public Double getEdgeWeight(Integer edgeName) {
        return edges.get(edgeName);
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getSourceName() {
        return sourceName;
    }

    public void setSourceName(Integer sourceName) {
        this.sourceName = sourceName;
    }

    public Map<Integer, Double> getEdges() {
        return edges;
    }

    public int compareTo(PrimeNode node) {
        return this.getName().compareTo(node.getName());
    }
}
