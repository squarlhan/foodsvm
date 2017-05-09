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

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Simple class that demonstrates the basic usage of JGAP.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 2.0
 */
public class WholeSVMGA {
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
		SVModel svm = new SVModel();
		long start = System.currentTimeMillis();
//		double[][] trainset = svm.readdata("./matrix_data/allResult_0.25_6.txt");
		double[][] trainset = svm.readdata("C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/matrix_data/allResult_0.25_6.txt");
		
		double[][] strainset = svm.scale(0, 1, trainset);
//		trainset = svm.scale(-1, 1, pl.train_xy);
//		testset = svm.scale(-1, 1, pl.test_xy);

		int feasure_num = strainset[0].length-1;
		int numEvolutions = 100;
		Configuration gaConf = new DefaultConfiguration();
		gaConf.setPreservFittestIndividual(true);
		gaConf.setKeepPopulationSizeConstant(false);
		Genotype genotype = null;
		int chromeSize;
		if (args.length > 0) {
			chromeSize = Integer.parseInt(args[0]);
		} else {
			chromeSize = feasure_num+2;
		}
		double maxFitness = 1;
		if (chromeSize > feasure_num+2) {
			System.err.println("This example does not handle " + "Chromosomes greater than 4 bits in length.");
			System.exit(-1);
		}
		try {
			Gene[] sampleGenes = new Gene[chromeSize];
		    sampleGenes[chromeSize-2] = new DoubleGene(gaConf, 0, 1); // g
		    sampleGenes[chromeSize-1] = new DoubleGene(gaConf, 0, 200); // c
		    for(int i = 0;i<=feasure_num-1;i++){
		    	sampleGenes[i] = new BooleanGene(gaConf);
		    }
//		    sampleGenes[1] = new IntegerGene(gaConf, 1, 200); // c
		    IChromosome sampleChromosome = new Chromosome(gaConf, sampleGenes);
			gaConf.setSampleChromosome(sampleChromosome);
			gaConf.setPopulationSize(50);
			gaConf.setFitnessFunction(new WholeSVMFitnessFunction(strainset));
			genotype = Genotype.randomInitialGenotype(gaConf);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			System.exit(-2);
		}
		int progress = 0;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/matrix_data/galogcvall5.txt"));
		
		int percentEvolution = numEvolutions / 10;
		for (int i = 0; i < numEvolutions; i++) {
			genotype.evolve();
			// Print progress.
			// ---------------
//			if (percentEvolution > 0 && i % percentEvolution == 0) {
				progress++;
				IChromosome fittest = genotype.getFittestChromosome();
				double fitness = fittest.getFitnessValue();
				System.out.println("Currently fittest Chromosome has fitness " + fitness);
				for(int c = 0; c<= chromeSize-1;c++){
					bw.write(fittest.getGene(c).getAllele()+"\t");
				}
				bw.write("\n");
				bw.flush();
				if (fitness >= maxFitness) {
					break;
				}
//			}
		}
		// Print summary.
		// --------------
		IChromosome fittest = genotype.getFittestChromosome();
		System.out.println("Onebyone Fittest Chromosome has fitness " + fittest.getFitnessValue() +
				"in g="+ fittest.getGene(fittest.size()-2).getAllele()+"and c=" + fittest.getGene(fittest.size()-1).getAllele());
		
		for(int i = 0; i<=fittest.size()-3;i++){
	   		 if((Boolean)fittest.getGene(i).getAllele()){
	   			System.out.print(i+"\t");
	   		 }
		}
		long stop = System.currentTimeMillis();
		System.out.println("Runing Time: "+(stop-start)/1000+"s.");
	}
}
