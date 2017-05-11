package listeners;

import algorithm.smart.IterationData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsoleIterationListener implements ActionListener {

    int n;

    public ConsoleIterationListener(final int iterations) {
        this.n = iterations;
    }

    public void actionPerformed(final ActionEvent e) {
        IterationData data = (IterationData) e.getSource();
        Integer iter = data.getIter();
        System.out.println("PERCENT COMPLETED: " + (iter * 100 / n));
        System.out.println("STATUS: " + "Iteration: " + iter + ", number of clusters: " + data.getClusters());
    }
}
