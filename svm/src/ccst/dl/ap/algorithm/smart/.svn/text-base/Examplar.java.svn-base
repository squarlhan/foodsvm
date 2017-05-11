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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author misiek
 */
public class Examplar implements Comparable<Examplar> {

    private Map<Integer, EdgeOutData> edgesOutDataMap = new HashMap<Integer, EdgeOutData>();
    private Collection<Integer> edgesIn = new TreeSet<Integer>();
    private Integer name;
    private Vector<Boolean> imcenter = null;
    private Integer convits = null;

    public Examplar(final Integer name, final Integer convits) {
        this.name = name;
        this.convits = convits;
        if (convits != null) {
            imcenter = new Vector<Boolean>();
        }
    }

    public void createSibling(final double s, final Integer siblingName) {
        EdgeOutData sibling = new EdgeOutData(s, siblingName);
        edgesOutDataMap.put(siblingName, sibling);

    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer(name);
        ret.append(": ");
        for (Integer key : edgesOutDataMap.keySet()) {
            ret.append(edgesOutDataMap.get(key).toString());
            ret.append("\n");
        }
        return ret.toString();
    }

    public Map<Integer, EdgeOutData> getSiblingMap() {
        return edgesOutDataMap;
    }

    public Integer getName() {
        return name;
    }

    public void setImCenter(final Boolean c, final int iteration) {
        if (convits != null) {

            imcenter.add(0, c);
            if (imcenter.size() == convits.intValue() + 1) {
                imcenter.remove(convits.intValue());
            }
        }
    }

    public boolean changed() {

        if (imcenter.size() < convits) {
            return true;
        }

        boolean res = false;
        boolean value = imcenter.firstElement();

        for (Boolean b : imcenter) {
            if (b != value) {
                res = true;
                break;
            }
        }

        return res;
    }

    public int compareTo(Examplar ex) {

        return this.getName().compareTo(ex.getName());

    }

    void addEdgeIn(Integer from) {
        edgesIn.add(from);
    }

    public Collection<Integer> getEdgesIn() {
        return edgesIn;
    }
}
