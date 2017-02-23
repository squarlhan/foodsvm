package cn.edu.jlu.ccst.sentimentsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;

public class Tongji_Sentiment {

	public void tongji(String inaddr, String outaddr) {
		String hold_date = "";
		int daily_counts = 0;
		HashMap<String, List<Double>> result = new HashMap<String, List<Double>>();
		
		Double neg_counts = 0.0;
		Double neu_counts = 0.0;
		Double pos_counts = 0.0;
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(inaddr));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outaddr));
			BufferedReader reader = new BufferedReader(ir);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				
				String myline = line.trim();
				String[] lines = myline.split(" ; ");
				JSONObject js = (new JsonNode(lines[1].trim())).getObject();
				String lable = ((JSONObject) js.get("result")).getString("sentiment");
				Double confi = Double.parseDouble(((JSONObject) js.get("result")).getString("confidence"));
				if(hold_date == ""||hold_date.equals(lines[0].trim())){
					hold_date = lines[0].trim();
					daily_counts++;
					switch (lable){
					 case "Negative": neg_counts+=confi;
                     break;
					 case "Neutral":  neu_counts+=confi;
                     break;
					 case "Positive": pos_counts+=confi;
                     break;
					}				
				}else {
					List<Double> cons_couts = new ArrayList<Double>();
					bw.write(hold_date+"\t"+neg_counts+"\t"+neu_counts+"\t"+pos_counts+"\t"+daily_counts+"\n");
					bw.flush();
					cons_couts.add(neg_counts*1.0);
					cons_couts.add(neu_counts*1.0);
					cons_couts.add(pos_counts*1.0);
					cons_couts.add(daily_counts*1.0);
					result.put(hold_date, cons_couts);
					hold_date = lines[0].trim();
					daily_counts=0;
					neg_counts=0.0;
					neu_counts=0.0;
					pos_counts=0.0;
					daily_counts++;
					switch (lable){
					 case "Negative": neg_counts+=confi;
                     break;
					 case "Neutral":  neu_counts+=confi;
                     break;
					 case "Positive": pos_counts+=confi;
                     break;
					}		
					
				}
				

			}
			
			
			reader.close();
			ir.close();
			bw.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tongji_Sentiment ts = new Tongji_Sentiment();
		ts.tongji("C:/Users/install/Desktop/hxs/recent/data/BBCextract_Trump.txt", "C:/Users/install/Desktop/hxs/recent/data/R_BBCextract_Trump.txt");
	}

}
