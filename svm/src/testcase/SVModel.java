package testcase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import knn.KNN;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import randomforest.DescribeTreesCateg;
import randomforest.RandomForestCateg;

public class SVModel {

	private svm_model model;

	public svm_model getModel() {
		return model;
	}

	public void setModel(svm_model model) {
		this.model = model;
	}

	public SVModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void getDefaultModel() {
		double[][] results = generatetestdata();
		int lenth = results[0].length;
		trian(results, 1.0 / (lenth - 1.0), 50);
	}

	public void loadmodel(String svmurl) {
		try {
			model = svm.svm_load_model(svmurl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<double[]> scores = null;
	public List<Double> pre_ys = null;

	public double predict(double[] data, boolean istrain) {
		int lenth = data.length;
		svm_node[] datanode = new svm_node[lenth];
		for (int i = 1; i <= lenth; i++) {
			datanode[i - 1] = new svm_node();
			datanode[i - 1].index = i;
			datanode[i - 1].value = data[i - 1];
		}
		// pre_y = svm.svm_predict(model, datanode);
		if (scores == null) {
			scores = new ArrayList();
			pre_ys = new ArrayList();
		}
		int nr_class = model.nr_class;
		double[] dec_values;
		if (model.param.svm_type == svm_parameter.ONE_CLASS || model.param.svm_type == svm_parameter.EPSILON_SVR
				|| model.param.svm_type == svm_parameter.NU_SVR)
			dec_values = new double[1];
		else
			dec_values = new double[nr_class * (nr_class - 1) / 2];

		double pred_result = svm.svm_predict_values(model, datanode, dec_values);
		if (!istrain) {
			scores.add(dec_values);
			pre_ys.add(pred_result);
		}
		return pred_result;
	}

	public double predict(double[][] data, boolean istrain) {
		int h = data.length;
		int lenth = data[0].length;
		int count = 0;
		double[] ys = new double[h];
		for (int i = 0; i <= h - 1; i++) {
			ys[i] = predict(data[i], istrain);
			count = ys[i] == data[i][lenth - 1] ? count + 1 : count;
		}
		// System.out.println(count+" / "+h+" = "+(double)count/h);
		return (double) count / h;

	}

	public double trian(double[][] results, double gamma, double c) {
		svm_problem prob_train = getdata(results);
		svm_parameter param = initsvm(gamma, c);
		model = svm.svm_train(prob_train, param);
		return predict(results, true);
	}

	public void computemodel(double[][] results, String svmurl, double gamma, double c) {
		trian(results, gamma, c);
		try {
			svm.svm_save_model(svmurl, model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public svm_parameter initsvm(double gamma, double c) {

		svm_parameter param = new svm_parameter();
		// default values
		param.svm_type = svm_parameter.NU_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = gamma; // 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = c;
		param.eps = 0.1;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];

		return param;
	}

	public double[][] scale(double lower, double upper, double[][] input) {
		int m = input.length;
		int n = input[0].length;
		double[] min = new double[n - 1];
		double[] max = new double[n - 1];
		double[][] output = new double[m][n];
		for (int j = 0; j <= n - 2; j++) {
			min[j] = Double.MAX_VALUE;
			max[j] = -Double.MAX_VALUE;
			for (int i = 0; i <= m - 1; i++) {
				min[j] = Math.min(min[j], input[i][j]);
				max[j] = Math.max(max[j], input[i][j]);
				// output[i][n-1] = input[i][n-1]==0?lower:upper;
				output[i][n - 1] = input[i][n - 1];
			}
		}
		for (int j = 0; j <= n - 2; j++) {
			for (int i = 0; i <= m - 1; i++) {
				output[i][j] = ((upper - lower) * (input[i][j] - min[j]) / (max[j] - min[j])) + lower;
			}
		}
		return output;
	}

	public svm_problem getdata(double[][] results) {
		svm_problem prob = new svm_problem();
		prob.l = results.length;
		double[] y = new double[prob.l];
		int lenth = results[0].length;
		svm_node[][] x = new svm_node[prob.l][lenth - 1];
		for (int i = 0; i <= prob.l - 1; i++) {
			for (int j = 1; j <= lenth - 1; j++) {
				x[i][j - 1] = new svm_node();
				x[i][j - 1].index = j;
				x[i][j - 1].value = results[i][j - 1];
			}
			y[i] = results[i][lenth - 1];
		}
		prob.x = x;
		prob.y = y;
		return prob;
	}

	public double[][] generatetestdata() {

		double[][] temp_arr = { { 0.05, 7.0, 0.1, 0.08574 }, { 0.05, 7.0, 0.2, 0.05789 }, { 0.05, 7.0, 0.3, 0.05002 },
				{ 0.05, 7.0, 0.4, 0.06347 }, { 0.05, 7.0, 0.5, 0.06603 }, { 0.05, 7.0, 0.6, 0.07771 },
				// {0.05, 7.0, 0.7, 0.082},
				{ 0.05, 7.0, 0.8, 0.13 }, { 0.05, 7.0, 0.9, 0.17 }, { 0.05, 5.0, 0.7, 0.079 },
				{ 0.05, 5.5, 0.7, 0.069 }, { 0.05, 6.0, 0.7, 0.064 }, { 0.05, 6.5, 0.7, 0.0608 },
				{ 0.05, 7.0, 0.7, 0.06649 }, { 0.05, 7.5, 0.7, 0.1514 }, { 0.05, 8.0, 0.7, 0.1682 },
				// {0.05, 6.5, 0.7, 0.06675},
				{ 0.10, 6.5, 0.7, 0.0517 }, { 0.20, 6.5, 0.7, 0.082 }, { 0.30, 6.5, 0.7, 0.2445 },
				{ 0.40, 6.5, 0.7, 0.3275 }, { 0.50, 6.5, 0.7, 0.468 } };

		return temp_arr;
	}

	public double[][] readdata(String addr) {
		List<String[]> datalist = new ArrayList<String[]>();

		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(addr));
			BufferedReader reader = new BufferedReader(ir);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] lines = line.trim().split("\t");
				datalist.add(lines);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int m = datalist.size();
		int n = datalist.get(0).length;

		double[][] data = new double[m][n];
		for (int i = 0; i <= m - 1; i++) {
			for (int j = 0; j <= n - 1; j++) {
				data[i][j] = Double.parseDouble(datalist.get(i)[j]);
			}
		}
		return data;
	}

	public double run_cross_validation(double[][] results, double gamma, double c, int nr_fold) {
		svm_problem prob = getdata(results);
		svm_parameter param = initsvm(gamma, c);
		int i;
		int total_correct = 0;
		double total_error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		double[] target = new double[prob.l];

		// svm.svm_cross_validation_onebyone(prob,param,nr_fold,target);
		svm.svm_cross_validation(prob, param, nr_fold, target);
		if (param.svm_type == svm_parameter.EPSILON_SVR || param.svm_type == svm_parameter.NU_SVR) {
			for (i = 0; i < prob.l; i++) {
				double y = prob.y[i];
				double v = target[i];
				total_error += (v - y) * (v - y);
				sumv += v;
				sumy += y;
				sumvv += v * v;
				sumyy += y * y;
				sumvy += v * y;
			}
			System.out.print("Cross Validation Mean squared error = " + total_error / prob.l + "\n");
			System.out.print("Cross Validation Squared correlation coefficient = "
					+ ((prob.l * sumvy - sumv * sumy) * (prob.l * sumvy - sumv * sumy))
							/ ((prob.l * sumvv - sumv * sumv) * (prob.l * sumyy - sumy * sumy))
					+ "\n");
			return total_error / prob.l;
		} else {
			for (i = 0; i < prob.l; i++)
				if (target[i] == prob.y[i])
					++total_correct;
			// System.out.print("Cross Validation Accuracy =
			// "+100.0*total_correct/prob.l+"%\n");
			return 1.0 * total_correct / prob.l;
		}
	}

	public double[][][] trans2folder(double[][] all, int num_fold) {
		List<double[]>[] res = new ArrayList[num_fold];
		int m = all.length;
		int n = all[0].length;
		for (int i = 0; i <= num_fold - 1; i++) {
			res[i] = new ArrayList<double[]>();
		}
		for (int i = 0; i <= m - 1; i++) {
			res[i % num_fold].add(all[i]);
		}
		double[][][] fr = new double[num_fold][][];
		for (int i = 0; i <= num_fold - 1; i++) {
			int l = res[i].size();
			double[][] sub = new double[l][n];
			for (int j = 0; j <= l - 1; j++) {
				sub[j] = res[i].get(j);
			}
			fr[i] = sub;
		}
		return fr;
	}
	
	public double[][] addknnfeature(double[][] train, double[][] test) {
		int m = test.length;
		int n = test[0].length;
		double[][] fr = new double[m][];
		KNN k = new KNN(18);
		for(int i = 0;i<=m-1;i++){
			double[] kr = k.doknn(train, test[i]);
			int kl = kr.length;
			double[] nr = new double[n+kl];
		    for(int j = 0;j<=n-2;j++){
		    	nr[j] = test[i][j];
		    }
		    for(int j = n-1;j<=n+kl-2;j++){
		    	nr[j] = kr[j-n+1];
		    }
		    nr[n+kl-1] = test[i][n-1];
		    fr[i] = nr;
		}
		return fr;
	}
	

	public List<Double> labels = null;

	public double do_cross_validation(double[][] all, double gamma, double c, int num_fold) {

		if (labels == null)
			labels = new ArrayList();
		double ave_acc = 0.0;

		double[][][] all_set = trans2folder(all, num_fold);
		int m = all.length;
		int n = all[0].length;
		for (int i = 0; i <= num_fold - 1; i++) {
			double[][] Test = all_set[i];
			int testl = Test.length;
			double[][] Train = new double[m - testl][n];
			int train_index = 0;
			for (int j = 0; j <= num_fold - 1; j++) {
				if (i != j) {
					int subl = all_set[j].length;
					for (int k = 0; k <= subl - 1; k++) {
						Train[train_index] = all_set[j][k];
						train_index++;
					}
				}
			}

			double[][] KNNTrain = Train;
			double[][] KNNTest = Test;
//			double[][] KNNTrain = this.addknnfeature(Train, Train);
//			double[][] KNNTest = this.addknnfeature(Train, Test);
			n = KNNTest[0].length;
			trian(KNNTrain, gamma, c);
			for (int k = 0; k <= KNNTest.length - 1; k++) {
				labels.add(KNNTest[k][n - 1]);
			}

			// svm.scores = null;
			double p = predict(KNNTest, false);
//			System.out.print(i+" Cross Validation Accuracy = "+100.0*p+"%\n");
			ave_acc += p;
		}
		double res = ave_acc / num_fold;
//		 System.out.print("Cross Validation Accuracy = "+100.0*res+"%\n");
		// svm.scores = null;
		// svm.pre_ys = null;
		return res;
	}

	public void write2file(String addr) throws IOException {
		int r = scores.size();
		int c = scores.get(0).length;
		File f = new File(addr);
		if (f.exists()) {
			f.delete();
			f.createNewFile();
		} else {
			f.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for (int i = 0; i <= r - 1; i++) {
			for (int j = 0; j <= c - 1; j++) {
				bw.write(String.valueOf(scores.get(i)[j]) + '\t');
				bw.flush();
			}
			bw.write(String.valueOf(pre_ys.get(i)) + '\t');
			bw.write(String.valueOf(labels.get(i)) + '\n');
			bw.flush();
		}
		bw.close();
	}

	public double do_cross_validation_onebyone(double[][] all, double gamma, double c, int num_fold) {
		double ave_acc = 0.0;

		double[][][] all_set = trans2folder(all, num_fold);
		int m = all.length;
		int n = all[0].length;
		for (int i = 0; i <= num_fold - 1; i++) {
			int total_correct = 0;
			double[][] Test = all_set[i];
			int testl = Test.length;
			double[][] Train = new double[m - testl][n];
			int train_index = 0;
			for (int j = 0; j <= num_fold - 1; j++) {
				if (i != j) {
					int subl = all_set[j].length;
					for (int k = 0; k <= subl - 1; k++) {
						Train[train_index] = all_set[j][k];
						train_index++;
					}
				}
			}
			double[][] KNNTrain = Train;
			double[][] KNNTest = Test;
//			double[][] KNNTrain = this.addknnfeature(Train, Train);
//			double[][] KNNTest = this.addknnfeature(Train, Test);
			n = KNNTest[0].length;
			svm_problem subprob = getdata(KNNTrain);
			svm_parameter param = initsvm(gamma, c);
			int l = subprob.l;
			int j, k;
			k = 0;
			int sl = 0;
			List<svm_node[]> sbx = new ArrayList();
			List<Double> sby = new ArrayList();
			for (j = 0; j < l; j++) {
				if (subprob.y[j] != 0) {
					sl++;
					subprob.y[k] = 1;
					sbx.add(subprob.x[j]);
					sby.add(subprob.y[j]);

				}
				++k;
			}

			svm_problem sub2prob = new svm_problem();
			sub2prob.l = sl;
			sub2prob.x = new svm_node[sl][];
			sub2prob.y = new double[sl];
			for (int ii = 0; ii <= sl - 1; ii++) {
				sub2prob.x[ii] = sbx.get(ii);
				sub2prob.y[ii] = sby.get(ii);
			}
			svm_model submodel = svm.svm_train(subprob, param);
			svm_model submodel2 = svm.svm_train(sub2prob, param);
			svm_problem testprob = getdata(KNNTest);
			double[] target = new double[testprob.l];
			if (param.probability == 1
					&& (param.svm_type == svm_parameter.C_SVC || param.svm_type == svm_parameter.NU_SVC)) {
				double[] prob_estimates = new double[svm.svm_get_nr_class(submodel)];
				for (j = 0; j < testprob.l; j++)
					target[j] = svm.svm_predict_probability(submodel, testprob.x[j], prob_estimates);
			} else {
				for (j = 0; j < testprob.l; j++) {
					double r = svm.svm_predict(submodel, testprob.x[j]);
					if (r != 0) {
						r = svm.svm_predict(submodel2, testprob.x[j]);
					}
					target[j] = r;
				}
			}
			for (j = 0; j < testprob.l; j++) {
				if (target[j] == testprob.y[j]) {
					++total_correct;
				}
				
			}
			double p = 1.0 * total_correct / testprob.l;
			System.out.print(i+" Cross Validation Accuracy = "+100.0*p+"%\n");
			ave_acc += p;

		}
		double res = ave_acc / num_fold;
		System.out.print("Cross Validation Accuracy = "+100.0*res+"%\n");
		return res;

	}

	public void write2file(double[][] x, String addr) throws IOException {
		int r = x.length;
		int c = x[0].length;
		File f = new File(addr);
		if (f.exists()) {
			f.delete();
			f.createNewFile();
		} else {
			f.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for (int i = 0; i <= r - 1; i++) {
			for (int j = 0; j <= c - 1; j++) {
				bw.write(String.valueOf(x[i][j]) + '\t');
				bw.flush();
			}
			bw.write('\n');
			bw.flush();
		}
		bw.close();
	}
	
	
	
	public void dotest(String addr) throws IOException{
		DataProcess dp = new  DataProcess();
		Map<Integer,String> map = dp.getdata();
		String outaddr = addr+"out.txt";
		File result = new File(outaddr);
		BufferedWriter bw = new BufferedWriter(new FileWriter(result));
		int mn = map.size();
		for(int i = 1;i<=mn;i++){
			String name = map.get(i);
			String fileaddr = addr+name+".txt";			
			double[][] data = readdata(fileaddr);
			double[][] sdata = scale(0, 1, data);
			int m = data.length;
			int n = data[0].length;
			
			double res1 = do_cross_validation(sdata, 1.0/n, 20, 10);
//			double res2 = do_cross_validation_onebyone(sdata, 1.0/n, 20, 10);
			
			bw.write(name+"\t"+res1+"\t"+"\n");
			System.out.println(name+"\t"+res1+"\t"+"\n");
			bw.flush();
			
		}
		bw.close();
	}

	public static void main(String[] args) {
		SVModel svm = new SVModel();
//		String addr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/matrix_data/";
		String addr = "./matrix_data/";
//		double[][] data = svm.readdata("C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/allResult_0.01_5.txt");
//		double[][] sdata = svm.scale(0, 1, data);
		
		try {
			// svm.write2file(sdata,
			// "C:/Users/install/Desktop/hxs/TCM/hnc/sall.txt");

			// svm.trian(sdata, 0.05, 1);
			// svm.trian(data, 0.05, 1);
//			svm.do_cross_validation_onebyone(sdata, 0.11706042784540238, 2.069315007176546, 10);
//			svm.write2file("C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/scores.txt");
			svm.dotest(addr);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
