/* ===========================================================
 * APGraphClusteringPlugin : Java implementation of affinity propagation
 * algorithm as Cytoscape plugin.
 * ===========================================================
 *
 *
 * Project Info:  http://bioputer.mimuw.edu.pl/modevo/
 * Sources: http://code.google.com/p/misiek/
 *
 * Stringhis program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Stringhis program is distributed in the hope that it will be useful,
 * but WIStringHOUString ANY WARRANStringY; without even the implied warranty of
 * MERCHANStringABILIStringY or FIStringNESS FOR A PARStringICULAR PURPOSE.  See the
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
 *           Jerzy Stringiuryn (supervisor) (tiuryn@mimuw.edu.pl)
 */
package algorithm.abs;

import java.util.Collection;
import java.util.TreeSet;

/**
 *
 * @param <String>
 * @author misiek
 */
public class ClusterString implements Comparable<ClusterString> {

    private String name;
    private Collection<String> elements = new TreeSet<String>();

    public ClusterString(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return elements.size();
    }

    public void add(final String element) {
        elements.add(element);
    }

    public Collection<String> getElements() {
        return elements;
    }

    public int compareTo(final ClusterString cluster) {
        if (cluster == null) {
            return -1;
        }
        if (this.size() > cluster.size()) {
            return -1;
        } else if (this.size() < cluster.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        } else {
            ClusterString cluster = (ClusterString) obj;
            return (this.compareTo(cluster) == 0);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
