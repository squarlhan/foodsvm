/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testcase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import adaboost.DataSet;
import adaboost.Evaluation;

/**
 *
 * @author daq
 */
public class TestAss4 {

	public static void main(String[] args) {
		

		String csvaddr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/csv/";
//		String csvaddr = "./csv/";
		DataProcess dp = new DataProcess();
		Map<Integer, String> map = dp.getdata();
		int mn = map.size()-27;
		String[] dataPaths = new String[mn];
		for (int i = 0; i <= mn-1; i++) {
			dataPaths[i] = csvaddr + map.get(i+28) + ".csv";
		}
		// String[] dataPaths = new
		// String[]{"C:/Users/install/Desktop/hxs/TCM/hnc/allast.csv"};
		String outaddr = csvaddr + "out.txt";
		File result = new File(outaddr);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(result));

			for (String path : dataPaths) {
				DataSet dataset = new DataSet(path);
				
				// for RandomForest
				Evaluation eva = new Evaluation(dataset, "RandomForest");
				// conduct 10-cv
				eva.crossValidation();
				// print mean and standard deviation of accuracy
				bw.write(path+"\t"+eva.getAccMean()+ "\t" + eva.getAccStd()+"\t");
				System.out.println("Random Forest:" + path + ", mean and standard deviation of accuracy:" + eva.getAccMean()
						+ "," + eva.getAccStd());
			}
			for (String path : dataPaths) {
				DataSet dataset = new DataSet(path);
				
				Evaluation eva = new Evaluation(dataset, "AdaBoost");
				// conduct 10-cv
				eva.crossValidation();
				// print mean and standard deviation of accuracy
				bw.write(path+"\t"+eva.getAccMean()+ "\t" + eva.getAccStd()+"\t");
				System.out.println("Adaboost:" + path + ", mean and standard deviation of accuracy:" + eva.getAccMean()
						+ "," + eva.getAccStd());
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
