package ccst.dl.ap.affinitymain;

import ccst.dl.ap.algorithm.abs.AffinityPropagationAlgorithm;
import ccst.dl.ap.algorithm.abs.AffinityPropagationAlgorithm.AffinityConnectingMethod;
import ccst.dl.ap.algorithm.abs.Cluster;
import ccst.dl.ap.algorithm.abs.ClusterInteger;
import ccst.dl.ap.algorithm.matrix.MatrixPropagationAlgorithm;
import ccst.dl.ap.algorithm.smart.SmartPropagationAlgorithm;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import ccst.dl.ap.listeners.ConsoleIterationListener;

/**
 *
 * @author misiek (mw219725@gmail.com)
 */
public class NewRunAlgorithm {

	private Collection<InteractionData> inputs;
	// private String outpath;
	private AffinityPropagationAlgorithm af = new SmartPropagationAlgorithm();
	private double lambda;
	private int iterations;
	private double preferences;
	private Integer convits;
	private Collection<String> nodeNames = new HashSet<String>();
	private String kind;
	private boolean takeLog;
	private boolean refine;
	private Integer steps = null;
	private AffinityConnectingMethod connMode;

	public NewRunAlgorithm(Collection<InteractionData> inputs, double lambda, int iterations, Integer convits,
			double preferences, String kind) {
		this.inputs = inputs;
		this.lambda = lambda;
		this.iterations = iterations;
		this.preferences = preferences;
		this.kind = kind;
		this.convits = convits;
	}

	public void setRefine(boolean refine) {
		this.refine = refine;
	}

	public void setConnMode(AffinityConnectingMethod connMode) {
		this.connMode = connMode;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public void setParemeters() {
		af.setLambda(lambda);
		af.setIterations(iterations);
		af.setConvits(convits);
		af.setSteps(steps);
		af.setRefine(refine);
		af.setConnectingMode(connMode);
		af.setConnectingMode(AffinityPropagationAlgorithm.AffinityConnectingMethod.ORIGINAL);
		af.addIterationListener(new ConsoleIterationListener(iterations));

		Iterator iter = inputs.iterator();
		while (iter.hasNext()) {
			InteractionData temp = (InteractionData) iter.next();
			nodeNames.add(temp.getFrom());
			nodeNames.add(temp.getTo());
		}
		// af.setN(ints.size());
		af.setN(nodeNames.size() + 0);

		af.init();
		for (InteractionData intData : inputs) {
			// System.out.println(intData.getFrom() + " " + intData.getTo() + "
			// " + intData.getSim());
			Double val;
			if (takeLog) {
				if (intData.getSim() > 0) {
					val = Math.log(intData.getSim());
				} else {
					val = Double.valueOf(0);
				}
			} else {
				val = intData.getSim();
			}
			int source = (int) Double.valueOf(intData.getFrom()).doubleValue();
			int target = (int) Double.valueOf(intData.getTo()).doubleValue();
			af.setSimilarityInt(source, target, val);
			// af.setSimilarityInt(target, source, val);
			// af.setSimilarityInt(Integer.valueOf(intData.getFrom()),
			// Integer.valueOf(intData.getTo()), val);
		}
		Double pref;
		if (takeLog) {
			if (preferences > 0) {
				pref = Math.log(preferences);
			} else {
				pref = Double.valueOf(0);
			}
		} else {
			pref = preferences;
		}

		// System.out.println("pref: " + pref);
		af.setConstPreferences(pref);
	}

	public List<Integer> run() {

		List<Integer> classes = new ArrayList();
		// System.out.println(kind);
		if (kind.equals("centers")) {
			Map<Integer, ClusterInteger> clusters = af.doClusterAssocInt();
			if (clusters != null) {
				for (Integer clustName : clusters.keySet()) {
					classes.add(clustName);
				}
			}
		} else {
			Map<Integer, Integer> clusters = af.doClusterInt();
			if (clusters != null) {
				for (Entry<Integer, Integer> entry : clusters.entrySet()) {
					classes.add(entry.getValue());
				}
			}
		}
		return classes;

	}

	public Map<String, String> runstring() {

		Map<String, String> clusters = af.doCluster();
		if (clusters != null) {
			return clusters;
		}

		return null;

	}

	public void setTakeLog(boolean takeLog) {
		this.takeLog = takeLog;
	}
}
