package testcase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import knn.KNN;

public class KNNTest {
	
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		KNNTest kt = new KNNTest();
		double[][] data = kt.readdata("C:/Users/install/Desktop/hxs/TCM/hnc/ga.txt");
		double[][] sdata = kt.scale(0, 1, data);
//		double label = k.runknn(sdata, sdata[1]);
		for(int i=10;i<=200;i++){
			KNN k = new KNN(i);
			k.calcdistances(sdata);
			double acc = k.do_crossover_validation(sdata, 10);
//			System.out.println(i+"	"+acc);
			System.out.println(acc);
		}
//		double acc = k.do_crossover_validation(data, 10);
//		System.out.println(acc);

	}

}
