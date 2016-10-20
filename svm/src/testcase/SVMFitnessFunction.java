package testcase;


import org.jgap.*;

public class SVMFitnessFunction
  extends FitnessFunction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String CVS_REVISION = "$Revision: 1.1 $";
    
    private double[][] trianset;

    public SVMFitnessFunction(double[][] trianset) {
      this.trianset = trianset;
    }


    public double evaluate(IChromosome a_subject) {
      double fitness;
      
      Double g = (Double) a_subject.getGene(0).getAllele();
      Double c = (Double) a_subject.getGene(1).getAllele();
//      int c = (int) a_subject.getGene(1).getAllele();
      SVModel svm = new SVModel();
//      fitness = svm.do_cross_validation_onebyone(trianset, g, c, 10);
      fitness = svm.do_cross_validation(trianset, g, c, 10);
      return fitness;
    }
}


