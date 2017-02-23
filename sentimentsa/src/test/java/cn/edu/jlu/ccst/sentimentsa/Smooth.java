package cn.edu.jlu.ccst.sentimentsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Smooth {
	
	List<List<Double>> mydata;
	
	
	
	public Smooth(String addr){
		mydata = new ArrayList<List<Double>>();
		
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(addr));
			BufferedReader reader = new BufferedReader(ir);
			 for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				 String[] lines = line.trim().split("\t");
				 if(lines.length>1){
					 List<Double> lined = new ArrayList<Double>();
					 lined.add(Double.parseDouble(lines[1]));
					 lined.add(Double.parseDouble(lines[2]));
					 lined.add(Double.parseDouble(lines[3]));
					 mydata.add(lined);
				 }
			 }
			 reader.close();
			 ir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void smoothbywindow(int window, String addr){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(addr));
			int size = mydata.size();
			int size1 = mydata.get(0).size();
			List<List<Double>> res = mydata;
			for(int i = window-1; i<= size-1;i++){
				int count1 = 0;
				int count2 = 0;
				int count3 = 0;
				double sum1 = 0.0;
				double sum2 = 0.0;
				double sum3 = 0.0;
				for(int j = 0; j<=window-1;j++){
					if(mydata.get(i-j).get(0)!=0)count1++;
					if(mydata.get(i-j).get(1)!=0)count2++;
					if(mydata.get(i-j).get(2)!=0)count3++;
					sum1+=mydata.get(i-j).get(0);
					sum2+=mydata.get(i-j).get(1);
					sum3+=mydata.get(i-j).get(2);
				}
				if(count1==0)count1 = 1;
				if(count2==0)count2 = 1;
				if(count3==0)count3 = 1;
				res.get(i).set(0, sum1/count1);
				res.get(i).set(1, sum2/count2);
				res.get(i).set(2, sum3/count3);
				
				
			}
			
			for(int i = 0; i<= size-1;i++){
				for(int j = 0; j<= size1-1;j++){
					if(res.get(i).get(j)!=0){
						bw.write(res.get(i).get(j)+"\t");
					}else{
						bw.write(" \t");
					}
					
				}
				bw.write(" \n");
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String name = "fox";
		Smooth s = new Smooth("C:/Users/install/Desktop/"+name+".txt");
		s.smoothbywindow(7, "C:/Users/install/Desktop/"+name+"s.txt");
	}

}
