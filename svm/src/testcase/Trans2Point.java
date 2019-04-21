package testcase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccst.dl.ap.affinitymain.InteractionData;
import ccst.dl.ap.affinitymain.NewRunAlgorithm;
import ccst.dl.ap.algorithm.abs.AffinityPropagationAlgorithm.AffinityConnectingMethod;

public class Trans2Point {
	
	private Collection<InteractionData> dists;
	private HashMap<Integer, String> dicts;
	private HashMap<String, Integer> convdicts;
	
	public List<String> readnames(String addr){
		List<String> names = new ArrayList<String>();
		dicts = new HashMap<Integer, String>();
		convdicts = new HashMap<String, Integer>();
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(addr));
			BufferedReader reader = new BufferedReader(ir);
			int i = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				names.add(line.trim());
				dicts.put(i, line.trim());
				convdicts.put(line.trim(), i);
				i++;
			}
			ir.close();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return names;

	}
	
	public double dotrans(String nameaddr, String inaddr, String outaddr){
		List<String>  names = readnames(nameaddr);
		List<Double> vals = new ArrayList<Double>();
		dists = new HashSet<InteractionData>();
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(inaddr));
			BufferedReader reader = new BufferedReader(ir);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outaddr));
			int j = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] lines = line.trim().split("\t");
				int n = lines.length;
				for(int i=0;i<=n-1;i++){
					double val = Double.parseDouble(lines[i]);
					if(Math.abs(val)<1){
//					if(Math.abs(val)>=0.3&&Math.abs(val)<1){
						bw.write(names.get(j)+", "+names.get(i)+", "+val+"\n");
						InteractionData id = new InteractionData(convdicts.get(names.get(j)).toString(),
								convdicts.get(names.get(i)).toString(),val);
						dists.add(id);
						vals.add(val);
						bw.flush();
					}
				}
				j++;
				
			}
			
			ir.close();
			reader.close();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object[] valarr = vals.toArray();
		Arrays.sort(valarr);
		
		return (double) valarr[valarr.length/2];
		
	}
	
	public void runap(String nameaddr, String inaddr, String outaddr){
		String kind = "clusters";
		AffinityConnectingMethod connMode = AffinityConnectingMethod.ORIGINAL;
		boolean takeLog = false;
		boolean refine = true;
		Integer steps = null;
		double preferences = dotrans(nameaddr, inaddr, outaddr);
		NewRunAlgorithm alg = new NewRunAlgorithm(dists, 0.8, 100,
				null, preferences, kind);
		alg.setTakeLog(takeLog);
		alg.setConnMode(connMode);
		alg.setSteps(steps);
		alg.setRefine(refine);

		alg.setParemeters();
		List<Integer> results = alg.run();
		if(results==null||results.size()==0){
			System.err.println("Cluster Error, 0 result!");	
		}
		Set<Integer> centers = new HashSet<Integer>();
		centers.addAll(results);
		HashMap<String, Set<String>> rs= new HashMap<String, Set<String>>();
		for(int i = 0; i<= results.size()-1;i++){
			System.out.println(results.get(i));
			String c = dicts.get(results.get(i));
			if(rs.get(c)==null){
				Set<String> names = new HashSet<String>();
				names.add(dicts.get(i));
				rs.put(c, names);
			}else{
				Set<String> names = rs.get(c);
				names.add(dicts.get(i));
				rs.put(c, names);
			}
		}
		System.out.println(rs);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String nameaddr = "D:/hxs/TCM/hnc/nd/missing/matrix_data/names.txt";
		String inaddr = "D:/hxs/TCM/hnc/nd/missing/matrix_data/corelation.txt";
		String outaddr = "D:/hxs/TCM/hnc/nd/missing/matrix_data/corall.txt";
		
		Trans2Point tp = new Trans2Point();
		tp.dotrans(nameaddr, inaddr, outaddr);
//		tp.runap(nameaddr, inaddr, outaddr);

	}

}
