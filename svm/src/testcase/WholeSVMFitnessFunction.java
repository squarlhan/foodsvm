package testcase;


import java.util.ArrayList;
import java.util.List;

import org.jgap.*;

public class WholeSVMFitnessFunction
  extends FitnessFunction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String CVS_REVISION = "$Revision: 1.1 $";
    
    private double[][] trainset;

    public WholeSVMFitnessFunction(double[][] trainset) {
      this.trainset = trainset;
    }
    
    public double[][] selectrain(IChromosome a_subject){
    	List<Integer> rslist = new ArrayList();
    	 int chsize = a_subject.size();
    	 for(int i = 0; i<=chsize-3;i++){
    		 if((Boolean)a_subject.getGene(i).getAllele()){
    			 rslist.add(i);
    		 }
    	 }
    	 int m = trainset.length;
    	 int n = rslist.size()+1;//the last column is label
    	 double[][] res = new double[m][n];
    	 for(int i = 0;i<=m-1;i++){
    		 for(int j = 0;j<=n-2;j++){
    			 res[i][j] = trainset[i][rslist.get(j)];
    		 }
    		 res[i][n-1] = trainset[i][trainset[0].length-1];
    	 }
    	 return res;
    }


    public double evaluate(IChromosome a_subject) {
      double fitness;
      int chsize = a_subject.size();
      Double g = (Double) a_subject.getGene(chsize-2).getAllele();
      Double c = (Double) a_subject.getGene(chsize-1).getAllele();
//      int c = (int) a_subject.getGene(1).getAllele();
      double[][] res = selectrain(a_subject);
      SVModel svm = new SVModel();
//      fitness = svm.do_cross_validation(res, g, c, 10);
      fitness = svm.do_cross_validation_onebyone(res, g, c, 10);
      return fitness;
    }
}


