package testcase;


import java.util.ArrayList;

import org.jgap.*;

public class RFFitnessFunction
  extends FitnessFunction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String CVS_REVISION = "$Revision: 1.1 $";
    
    private ArrayList<ArrayList<String>> all;

    public RFFitnessFunction(ArrayList<ArrayList<String>> all) {
      this.all = all;
    }


    public double evaluate(IChromosome a_subject) {
      double fitness;
      
      Integer num_trees = (Integer) a_subject.getGene(0).getAllele();
      Integer num_feats = (Integer) a_subject.getGene(1).getAllele();
//      int c = (int) a_subject.getGene(1).getAllele();
      MainRunCateg mrc = new MainRunCateg();
      fitness = mrc.do_cross_validate(all, 10, num_trees, num_feats);
      return fitness;
    }
}


