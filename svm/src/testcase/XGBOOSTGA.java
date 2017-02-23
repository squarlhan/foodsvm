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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.dmlc.xgboost4j.DMatrix;
import org.dmlc.xgboost4j.util.XGBoostError;
import org.jgap.*;
import org.jgap.impl.*;

/**
 * Simple class that demonstrates the basic usage of JGAP.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 2.0
 */
public class XGBOOSTGA {
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
	 * @throws XGBoostError 
	 * @since 2.0
	 */
	public static void main(String[] args) throws IOException, XGBoostError {
		SVModel svm = new SVModel();
		
		DMatrix trainMat = new DMatrix(
				"C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/libsvm/allResult_0.25_6.txt");
//		double[][] strainset = svm.scale(0, 1, trainset);
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
			chromeSize = 6;
		}
		double maxFitness = 0.98;
		if (chromeSize > 6) {
			System.err.println("This example does not handle " + "Chromosomes greater than 6 bits in length.");
			System.exit(-1);
		}
		try {
			Gene[] sampleGenes = new Gene[6];
		    sampleGenes[0] = new DoubleGene(gaConf, 0.1, 1); // eta
		    sampleGenes[1] = new DoubleGene(gaConf, 0, 50); // gamma
		    sampleGenes[2] = new IntegerGene(gaConf, 3, 6); // max_depth
		    sampleGenes[3] = new DoubleGene(gaConf, 1, 50); // min_child_weight
		    sampleGenes[4] = new DoubleGene(gaConf, 0.2, 1); // subsample
		    sampleGenes[5] = new DoubleGene(gaConf, 0.6, 1); // colsample_bytree
//		    sampleGenes[1] = new IntegerGene(gaConf, 1, 200); // c
		    IChromosome sampleChromosome = new Chromosome(gaConf, sampleGenes);
			gaConf.setSampleChromosome(sampleChromosome);
			gaConf.setPopulationSize(50);
			gaConf.setFitnessFunction(new XGBOOSTFitnessFunction(trainMat));
			genotype = Genotype.randomInitialGenotype(gaConf);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			System.exit(-2);
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/libsvm/galogcv2.txt"));
		
		int progress = 0;
		int percentEvolution = numEvolutions / 10;
		for (int i = 0; i < numEvolutions; i++) {
			genotype.evolve();
			// Print progress.
			// ---------------
//			if (percentEvolution > 0 && i % percentEvolution == 0) {
				progress++;
				IChromosome fittest = genotype.getFittestChromosome();
				double fitness = fittest.getFitnessValue();
				System.out.println("Generation "+i+" has fitness " + fitness);
				bw.write(fittest.getGene(0).getAllele()+
				"\t" + fittest.getGene(1).getAllele()+
				"\t" + fittest.getGene(2).getAllele()+
				"\t" + fittest.getGene(3).getAllele()+
				"\t" + fittest.getGene(4).getAllele()+
				"\t" + fittest.getGene(5).getAllele()+
				"\t" + fitness + "\n");
				bw.flush();
				if (fitness >= maxFitness) {
					break;
				}
//			}
		}
		
		bw.close();
		// Print summary.
		// --------------
		IChromosome fittest = genotype.getFittestChromosome();
		System.out.println("Fittest Chromosome has fitness " + fittest.getFitnessValue() +
				"in eta="+ fittest.getGene(0).getAllele()+
				"and gamma=" + fittest.getGene(1).getAllele()+
				"and max_depth=" + fittest.getGene(2).getAllele()+
				"and min_child_weight=" + fittest.getGene(3).getAllele()+
				"and subsample=" + fittest.getGene(4).getAllele()+
				"and colsample_bytree=" + fittest.getGene(5).getAllele());
	}
}
