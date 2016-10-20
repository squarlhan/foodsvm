package testcase;


import java.util.ArrayList;
import java.util.List;

import org.jgap.*;

import adaboost.DataSet;
import adaboost.Evaluation;

public class WholeADFitnessFunction
  extends FitnessFunction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String CVS_REVISION = "$Revision: 1.1 $";
    
    private DataSet dataset;

    public WholeADFitnessFunction(DataSet dataset) {
      this.dataset = dataset;
    }
    
    public DataSet selectrain(IChromosome a_subject){
    	List<Integer> rslist = new ArrayList();
    	 int chsize = a_subject.size();
    	 for(int i = 0; i<=chsize-3;i++){
    		 if((Boolean)a_subject.getGene(i).getAllele()){
    			 rslist.add(i);
    		 }
    	 }
    	 DataSet res = new DataSet();
    	 int m = dataset.getNumInstnaces();
    	 int n = rslist.size();
    	 double[][] feasures = new double[m][n];
    	 for(int i = 0;i<=m-1;i++){
    		 for(int j = 0;j<=n-1;j++){
    			 feasures[i][j] = dataset.getFeatures()[i][rslist.get(j)];
    		 }
    	 }
    	 
    	 boolean[] iscat = new boolean[n];
    	 for(boolean b : iscat){
    		 b = false;
    	 }
    	 iscat[n-1] = true;
    	 res.setFeatures(feasures);
    	 res.setLabels(dataset.getLabels());
    	 res.setNumAttributes(n);
    	 res.setNumInstnaces(m);
    	 res.setIsCategory(iscat);
    	 return res;
    }


    public double evaluate(IChromosome a_subject) {
      double fitness;
      int chsize = a_subject.size();
      DataSet res = selectrain(a_subject);
//      Integer num_trees = (Integer) a_subject.getGene(chsize-2).getAllele();
//      Integer num_feats = (Integer) a_subject.getGene(chsize-1).getAllele();
      Evaluation eva = new Evaluation(res, "AdaBoost");
      eva.crossValidation();
//      System.out.println(eva.getAccMean());
      return eva.getAccMean();
    }
}


