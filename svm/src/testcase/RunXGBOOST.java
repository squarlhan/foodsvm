package testcase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.dmlc.xgboost4j.Booster;
import org.dmlc.xgboost4j.DMatrix;
import org.dmlc.xgboost4j.util.Trainer;

import org.dmlc.xgboost4j.demo.util.CustomEval;
import org.dmlc.xgboost4j.demo.util.Params;
import org.dmlc.xgboost4j.util.XGBoostError;

public class RunXGBOOST {

	public void convert2LibSVM(String inaddr, String outaddr) {
		List<String[]> datalist = new ArrayList<String[]>();

		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(inaddr));
			BufferedReader reader = new BufferedReader(ir);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] lines = line.trim().split("\t");
				datalist.add(lines);
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(outaddr));

			int m = datalist.size();
			int n = datalist.get(0).length;

			for (int i = 0; i <= m - 1; i++) {
				String row = datalist.get(i)[n - 1];
				for (int j = 0; j <= n - 2; j++) {
					row = row + "\t" + j + ":" + datalist.get(i)[j];
				}
				bw.write(row + "\n");
				bw.flush();
			}

			bw.close();
			ir.close();
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void convertpipeline() {
		String inaddr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/matrix_data/";
		String outaddr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/libsvm/";
		// String dataaddr = "./matrix_data/";
		// String outaddr = "./matrix_data/km/";
		DataProcess dp = new DataProcess();
		Map<Integer, String> map = dp.getdata();

		int mn = map.size();
		for (int i = 1; i <= mn; i++) {
			String name = map.get(i);
			String fileaddr = inaddr + name + ".txt";
			String outfileaddr = outaddr + name + ".txt";
			convert2LibSVM(fileaddr, outfileaddr);
		}
	}

	public double do_cross_validation(Params param, DMatrix trainMat, int round, int nfold) {
		if (param == null) {
			param = new Params() {
				{
					put("eta", 0.1);
					put("max_depth", 6);
					put("silent", 1);
					put("nthread", 6);
					put("objective", "multi:softmax");
					put("gamma", 1.0);
					put("num_class", 3);
					put("eval_metric", "merror");
				}
			};
		}

		String[] evalHist = new String[round];
		try {
			evalHist = Trainer.crossValiation(param, trainMat, round, nfold, null, null, null);

		} catch (XGBoostError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sr = evalHist[round - 1].trim();
		String[] srs = sr.split("\t");
		String[] srs1 = srs[1].split(":");
		double test_rate = Double.parseDouble(srs1[1].trim());
		return 1 - test_rate;
	}

	public void pipeline() {
		String inaddr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/libsvm/";
		// String inaddr = "./libsvm/";
		DataProcess dp = new DataProcess();
		Map<Integer, String> map = dp.getdata();

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(inaddr + "out.txt"));

			int mn = map.size();
			for (int i = 53; i <= 53; i++) {
				String name = map.get(i);
				String fileaddr = inaddr + name + ".txt";
				int round = 1000;
				int nfold = 10;
				DMatrix trainMat;
				try {
					trainMat = new DMatrix(fileaddr);
					double acc = do_cross_validation(null, trainMat, round, nfold);
					bw.write(name + ": " + acc + "\n");
					bw.flush();
				} catch (XGBoostError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			bw.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public double runboost(DMatrix trainMat, Params param) {
		int round = 1002;
		double res = 1.0;
		try {
			List<Map.Entry<String, DMatrix>> watchs = new ArrayList<>();
			Booster booster = Trainer.train(param, trainMat, round, watchs, null, null);
			float[][] predicts2 = booster.predict(trainMat);
			CustomEval eval = new CustomEval();
			res = 1 - eval.eval(predicts2, trainMat);
		} catch (XGBoostError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public void runtest() throws XGBoostError {
		DMatrix trainMat = new DMatrix(
				"C:/Users/install/Desktop/hxs/TCM/hnc/xgboost-master/demo/data/agaricus.txt.train");
		DMatrix testMat = new DMatrix(
				"C:/Users/install/Desktop/hxs/TCM/hnc/xgboost-master/demo/data/agaricus.txt.test");

		// specify parameters
		Params param = new Params() {
			{
				put("eta", 0.001);
				put("max_depth", 3);
				put("eval_metric", "error");
				put("silent", 1);
				put("objective", "binary:logistic");
			}
		};

		// specify watchList
		List<Map.Entry<String, DMatrix>> watchs = new ArrayList<>();
		// watchs.add(new AbstractMap.SimpleEntry<>("train", trainMat));
		// watchs.add(new AbstractMap.SimpleEntry<>("test", testMat));

		// train a booster
		int round = 1002;
		Booster booster = Trainer.train(param, trainMat, round, watchs, null, null);
		String[] evalHist = Trainer.crossValiation(param, trainMat, round, 5, null, null, null);
		String sr = evalHist[round - 1].trim();
		String[] srs = sr.split("\t");
		String[] srs1 = srs[1].split(":");
		double test_rate = Double.parseDouble(srs1[1].trim());
		System.out.println(test_rate);
		// predict use 1 tree
		float[][] predicts1 = booster.predict(testMat, false, 1);
		// by default all trees are used to do predict
		float[][] predicts2 = booster.predict(testMat);

		// use a simple evaluation class to check error result
		CustomEval eval = new CustomEval();
		System.out.println("error of predicts1: " + eval.eval(predicts1, testMat));
		System.out.println("error of predicts2: " + eval.eval(predicts2, testMat));
	}

	public static void main(String[] args) throws XGBoostError {
		// load file from text file, also binary buffer generated by xgboost4j

		RunXGBOOST rx = new RunXGBOOST();
		 rx.runtest();
//		rx.pipeline();

	}

}
