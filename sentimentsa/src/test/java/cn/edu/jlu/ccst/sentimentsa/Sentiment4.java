package cn.edu.jlu.ccst.sentimentsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Sentiment4 {

	public void pipeline(){
		try {
			
//			sentimentbydir("C:/Users/install/Desktop/hxs/recent/data/BBCextract_Trump/", "C:/Users/install/Desktop/hxs/recent/data/BBCextract_Trump.txt");
			
			List<String> names = new ArrayList<String>();
//			names.add("BBC");
//			names.add("CNN");
//			names.add("BEAST");
//			names.add("TELE");
			names.add("FOX");
//			names.add("REUTERS");
//			names.add("ALJ");
			
			for(String name:names){
				sentimentbydir("./"+name+"extract_Clinton/", "./"+name+"extract_Clinton.txt");
				sentimentbydir("./"+name+"extract_Obama/", "./"+name+"extract_Obama.txt");
				sentimentbydir("./"+name+"extract_Trump/", "./"+name+"extract_Trump.txt");
			}
			
			
//			sentimentbydir("C:/Users/install/Desktop/hxs/recent/data/CNN/", "C:/Users/install/Desktop/hxs/recent/data/CNN.txt");
//			sentimentbydir("C:/Users/install/Desktop/hxs/recent/data/ALJ/", "C:/Users/install/Desktop/hxs/recent/data/ALJ.txt");
//			sentimentbydir("C:/Users/install/Desktop/hxs/recent/data/BEAST/", "C:/Users/install/Desktop/hxs/recent/data/BEAST.txt");
//			sentimentbydir("C:/Users/install/Desktop/hxs/recent/data/BBC/", "C:/Users/install/Desktop/hxs/recent/data/BBC.txt");
//			sentimentbydir("C:/Users/install/Desktop/hxs/recent/data/TELE/", "C:/Users/install/Desktop/hxs/recent/data/TELE.txt");
//			sentimentbydir("C:/Users/install/Desktop/hxs/recent/data/REUTERS/", "C:/Users/install/Desktop/hxs/recent/data/REUTERS.txt");
//			sentimentbydir("C:/Users/install/Desktop/hxs/recent/data/FOX/", "C:/Users/install/Desktop/hxs/recent/data/FOX.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sentimentbydir(String rooturl, String addrout) throws IOException, UnirestException{
		File root = new File(rooturl);
		File[] sublists = root.listFiles();
		List<File> files;
		BufferedWriter bw = new BufferedWriter(new FileWriter(addrout));

		for(File f: sublists){
			//merge files
			files = new ArrayList<File>();
			files.addAll(Arrays.asList(f.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if(dir.isDirectory()){
                        return true;
                    }
                    name = dir.getName();
                    return name.endsWith(".txt")|| name.endsWith(".TXT");
                }
            })));
			
			
			
			for (File fs : files) {
				InputStreamReader ir = new InputStreamReader(new FileInputStream(fs));
				BufferedReader reader = new BufferedReader(ir);
			    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			    	String news = line.trim();
			    	if(news.length()>2){
					    HttpResponse<JsonNode> response_v = Unirest.post("https://community-sentiment.p.mashape.com/text/")
								.header("X-Mashape-Key", "E5YJeouzVfmshPvxZxQ2tO1SuMWvp1GOa9mjsnc4ExJ18AsbUq")
								.header("Content-Type", "application/x-www-form-urlencoded")
								.header("Accept", "application/json")
								.field("txt", news)
								.asJson();
//					    {
//					    	  "result": {
//					    	    "confidence": "96.7434",
//					    	    "sentiment": "Positive"
//					    	  }
//					    	}
//					    HttpResponse<JsonNode> response_c = Unirest.get("https://mtnfog-cloud-nlp-v1.p.mashape.com/sentiment?text="+news)
//					    		.header("X-Mashape-Key", "E5YJeouzVfmshPvxZxQ2tO1SuMWvp1GOa9mjsnc4ExJ18AsbUq")
//					    		.header("Accept", "application/json")
//					    		.asJson();
//					    4
//					    HttpResponse<JsonNode> response_e = Unirest.get("https://sentimentapi.p.mashape.com/products/api/extractsentiment?text="+news)
//					    		.header("X-Mashape-Key", "E5YJeouzVfmshPvxZxQ2tO1SuMWvp1GOa9mjsnc4ExJ18AsbUq")
//					    		.header("Accept", "application/json")
//					    		.asJson();
//					    {
//					    	  "rating": "2.0"
//					    	}
					    
//					    HttpResponse<JsonNode> response_a = Unirest.post("https://text-sentiment.p.mashape.com/analyze")
//					    		.header("X-Mashape-Key", "E5YJeouzVfmshPvxZxQ2tO1SuMWvp1GOa9mjsnc4ExJ18AsbUq")
//					    		.header("Content-Type", "application/x-www-form-urlencoded")
//					    		.header("Accept", "application/json")
//					    		.field("text", news)
//					    		.asJson();
//					    {
//					    	  "text": "I am a happy boy",
//					    	  "totalLines": 1,
//					    	  "pos": 1,
//					    	  "neg": 0,
//					    	  "mid": 0,
//					    	  "pos_percent": "100%",
//					    	  "neg_percent": "0%",
//					    	  "mid_percent": "0%"
//					    	}
					    
					    bw.write(f.getName()+" ; "+response_v.getBody()+"\n");
					    bw.flush();
					    					    
			    	}
			    }
			    

			    ir.close();
			    reader.close();   
			  
			}
			
			
		}
		bw.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		try {
//			HttpResponse<JsonNode> response = Unirest.post("https://community-sentiment.p.mashape.com/text/")
//					.header("X-Mashape-Key", "E5YJeouzVfmshPvxZxQ2tO1SuMWvp1GOa9mjsnc4ExJ18AsbUq")
//					.header("Content-Type", "application/x-www-form-urlencoded")
//					.header("Accept", "application/json")
//					.field("txt", "Today is  a good day")
//					.asJson();
//			System.out.println(response.getBody());
//		} catch (UnirestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Sentiment4 se = new Sentiment4();
		se.pipeline();

	}

}
