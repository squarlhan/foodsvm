package testcase;


import org.dmlc.xgboost4j.DMatrix;
import org.dmlc.xgboost4j.demo.util.Params;
import org.jgap.*;

public class XGBOOSTFitnessFunction
  extends FitnessFunction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String CVS_REVISION = "$Revision: 1.1 $";
    
    private DMatrix trainMat;

    public XGBOOSTFitnessFunction(DMatrix trainMat) {
      this.trainMat = trainMat;
    }


    public double evaluate(IChromosome a_subject) {
      double fitness;
      
      Double eta = (Double) a_subject.getGene(0).getAllele();
      Double gamma = (Double) a_subject.getGene(1).getAllele();
      int max_depth = (Integer) a_subject.getGene(2).getAllele();
      Double min_child_weight = (Double) a_subject.getGene(3).getAllele();
      Double subsample = (Double) a_subject.getGene(4).getAllele();
      Double colsample_bytree = (Double) a_subject.getGene(5).getAllele();
      
//      int c = (int) a_subject.getGene(1).getAllele();
      
      Params param = new Params() {
			{
				put("eta", eta);
				put("max_depth", max_depth);
				put("silent", 1);
				put("nthread", 6);
				put("objective", "multi:softmax");
				put("gamma", gamma);
				put("min_child_weight", min_child_weight);
				put("subsample", subsample);
				put("colsample_bytree", colsample_bytree);
				put("num_class", 3);
				put("eval_metric", "merror");
			}
		};
      RunXGBOOST rx = new RunXGBOOST();
//      fitness = rx.runboost(trainMat, param);
      fitness = rx.do_cross_validation(param, trainMat, 201, 5);
      return fitness;
    }
}


