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

import org.apache.commons.lang3.StringUtils;

public class DataProcess {

	public Map<Integer, String> getdata() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "allResult_0.01_5");
		map.put(2, "allResult_0.01_6");
		map.put(3, "allResult_0.01_7");
		map.put(4, "allResult_0.01_8");
		map.put(5, "allResult_0.01_9");
		map.put(6, "allResult_0.01_10");
		map.put(7, "allResult_0.02_5");
		map.put(8, "allResult_0.02_6");
		map.put(9, "allResult_0.02_7");
		map.put(10, "allResult_0.02_8");
		map.put(11, "allResult_0.02_9");
		map.put(12, "allResult_0.02_10");
		map.put(13, "allResult_0.03_5");
		map.put(14, "allResult_0.03_6");
		map.put(15, "allResult_0.03_7");
		map.put(16, "allResult_0.03_8");
		map.put(17, "allResult_0.03_9");
		map.put(18, "allResult_0.03_10");
		map.put(19, "allResult_0.04_5");
		map.put(20, "allResult_0.04_6");
		map.put(21, "allResult_0.04_7");
		map.put(22, "allResult_0.04_8");
		map.put(23, "allResult_0.04_9");
		map.put(24, "allResult_0.04_10");
		map.put(25, "allResult_0.05_5");
		map.put(26, "allResult_0.05_6");
		map.put(27, "allResult_0.05_7");
		map.put(28, "allResult_0.05_8");
		map.put(29, "allResult_0.05_9");
		map.put(30, "allResult_0.05_10");
		map.put(31, "allResult_0.08_5");
		map.put(32, "allResult_0.08_6");
		map.put(33, "allResult_0.08_7");
		map.put(34, "allResult_0.08_8");
		map.put(35, "allResult_0.08_9");
		map.put(36, "allResult_0.08_10");
		map.put(37, "allResult_0.16_5");
		map.put(38, "allResult_0.16_6");
		map.put(39, "allResult_0.16_7");
		map.put(40, "allResult_0.16_8");
		map.put(41, "allResult_0.16_9");
		map.put(42, "allResult_0.16_10");
		map.put(43, "allResult_0.21_5");
		map.put(44, "allResult_0.21_6");
		map.put(45, "allResult_0.21_7");
		map.put(46, "allResult_0.22_5");
		map.put(47, "allResult_0.22_6");
		map.put(48, "allResult_0.22_7");
		map.put(49, "allResult_0.24_5");
		map.put(50, "allResult_0.24_6");
		map.put(51, "allResult_0.24_7");
		map.put(52, "allResult_0.25_5");
		map.put(53, "allResult_0.25_6");
		map.put(54, "allResult_0.25_7");

		return map;

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

	public void convert2cvs(String inaddr, String outaddr) {
		Map<Integer, String> map = getdata();
		try {
			int mn = map.size();
			for (int i = 1; i <= mn; i++) {
				String name = map.get(i);
				String fileaddr = inaddr + name+".txt";
				InputStreamReader ir = new InputStreamReader(new FileInputStream(fileaddr));
				BufferedReader reader = new BufferedReader(ir);
				File result = new File(outaddr + name+".csv");
				BufferedWriter bw = new BufferedWriter(new FileWriter(result));
				boolean flag = true;
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					String outstr = "";
					if (flag) {
						int length = StringUtils.countMatches(line.trim(), "\t");
						for(int j = 0;j<=length-1;j++){
							outstr+="0,";
						}
						outstr+="1\n";
						flag = !flag;
					}
					outstr += line.trim().replaceAll("\t", ",");
					bw.write(outstr + "\n");
					bw.flush();
				}

				ir.close();
				reader.close();
				bw.close();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inaddr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/matrix_data/";
		String outaddr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/csv/";
		
		DataProcess dp = new DataProcess();
		dp.convert2cvs(inaddr, outaddr);

	}

}
