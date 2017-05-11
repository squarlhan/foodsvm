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

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class PrimeGraph {

    private Map<Integer, PrimeNode> nodes;

    public PrimeGraph() {
        this.nodes = new TreeMap<Integer, PrimeNode>();
    }

    public void addEdge(Integer from, Integer to, Double sim) {
        PrimeNode nodeFrom = nodes.get(from);
        nodeFrom.addEdge(to, sim);
    }

    public void addNode(Integer nodeName) {
        PrimeNode node = new PrimeNode(nodeName);
        this.nodes.put(nodeName, node);
    }

    public PrimeNode getNode(Integer nodeName) {
        return nodes.get(nodeName);
    }

    public Collection<PrimeNode> getNodes() {
        return nodes.values();
    }
}
