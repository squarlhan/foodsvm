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
package algorithm.abs;

import java.util.Vector;

public class ConvitsVector implements Comparable<ConvitsVector> {

    private Vector<Boolean> convits;
    private int current;
    private int len;
    private boolean ready;
    private Integer name;

    public ConvitsVector(int len, Integer n) {
        this.len = len;
        this.convits = new Vector<Boolean>(len);
        this.current = 0;
        this.ready = false;
        this.name = n;
    }

    public void addCovits(boolean b) {
        convits.set(current, b);
        if (current == len - 1) {
            this.ready = true;
        }
        current = (current + 1) % len;
    }

    public boolean checkConvits() {
        if (ready == false) {
            return false;
        } else {
            boolean first = convits.firstElement().booleanValue();

            for (Boolean b : convits) {
                if (b != first) {
                    return false;
                }
            }
            return true;
        }
    }

    public void init() {
        for (int i = 0; i < len; i++) {
            this.convits.add(Boolean.valueOf(true));
        }
    }

    public int compareTo(ConvitsVector o) {
        return this.name.compareTo(o.name);
    }
}
