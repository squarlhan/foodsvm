/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package testcase;

import java.io.IOException;
import java.util.ArrayList;

import org.jgap.*;
import org.jgap.impl.*;

import randomforest.DescribeTreesCateg;

/**
 * Simple class that demonstrates the basic usage of JGAP.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 2.0
 */
public class RFGA {
	/** String containing the CVS revision. Read out via reflection! */
	private static final String CVS_REVISION = "$Revision: 1.9 $";

	/**
	 * Starts the example.
	 * 
	 * @param args
	 *            if optional first argument provided, it represents the number
	 *            of bits to use, but no more than 32
	 *
	 * @author Neil Rotstan
	 * @author Klaus Meffert
	 * @throws IOException 
	 * @since 2.0
	 */
	public static void main(String[] args) throws IOException {
		MainRunCateg mrc = new MainRunCateg();
		String addr = "C:/Users/install/Desktop/hxs/TCM/hnc/anoval.txt";
		DescribeTreesCateg DT = new DescribeTreesCateg(addr);
		ArrayList<ArrayList<String>> all = DT.CreateInputCateg(addr);
		int M=all.get(0).size()-1;
//		trainset = svm.scale(-1, 1, pl.train_xy);
//		testset = svm.scale(-1, 1, pl.test_xy);

		int numEvolutions = 200;
		Configuration gaConf = new DefaultConfiguration();
		gaConf.setPreservFittestIndividual(true);
		gaConf.setKeepPopulationSizeConstant(false);
		Genotype genotype = null;
		int chromeSize;
		if (args.length > 0) {
			chromeSize = Integer.parseInt(args[0]);
		} else {
			chromeSize = 2;
		}
		double maxFitness = 1;
		if (chromeSize > 2) {
			System.err.println("This example does not handle " + "Chromosomes greater than 4 bits in length.");
			System.exit(-1);
		}
		try {
			Gene[] sampleGenes = new Gene[2];
		    sampleGenes[0] = new IntegerGene(gaConf, 1, 20); // num of sub trees
		    sampleGenes[1] = new IntegerGene(gaConf, 1, M); // num of features
		    IChromosome sampleChromosome = new Chromosome(gaConf, sampleGenes);
			gaConf.setSampleChromosome(sampleChromosome);
			gaConf.setPopulationSize(50);
			gaConf.setFitnessFunction(new RFFitnessFunction(all));
			genotype = Genotype.randomInitialGenotype(gaConf);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			System.exit(-2);
		}
		int progress = 0;
		int percentEvolution = numEvolutions / 10;
		for (int i = 0; i < numEvolutions; i++) {
			genotype.evolve();
			// Print progress.
			// ---------------
			if (percentEvolution > 0 && i % percentEvolution == 0) {
				progress++;
				IChromosome fittest = genotype.getFittestChromosome();
				double fitness = fittest.getFitnessValue();
				System.out.println("Currently fittest Chromosome has fitness " + fitness);
				if (fitness >= maxFitness) {
					break;
				}
			}
		}
		// Print summary.
		// --------------
		IChromosome fittest = genotype.getFittestChromosome();
		System.out.println("Fittest Chromosome has fitness " + fittest.getFitnessValue() +
				"in g="+ fittest.getGene(0).getAllele()+"and c=" + fittest.getGene(1).getAllele());
	}
}
