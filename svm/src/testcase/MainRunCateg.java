package testcase;

import java.util.ArrayList;
import java.util.HashMap;

import randomforest.DescribeTreesCateg;
import randomforest.RandomForestCateg;

public class MainRunCateg {
	
	public ArrayList<ArrayList<String>>[] trans2folder(ArrayList<ArrayList<String>> all, int num_fold){
		ArrayList<ArrayList<String>>[] res = new ArrayList[num_fold];
		int l = all.size();
		for(int i = 0; i<=num_fold-1;i++){
			res[i] = new ArrayList<ArrayList<String>>();
		}
		for(int i = 0; i<=l-1; i++){
			res[i%num_fold].add(all.get(i));
		}
		return res;
	}
	
	public double do_cross_validate(String addr,  int num_fold){
		double ave_acc = 0.0;
		DescribeTreesCateg DT = new DescribeTreesCateg(addr);
		ArrayList<ArrayList<String>> all = DT.CreateInputCateg(addr);
		ArrayList<ArrayList<String>>[] all_set = trans2folder(all, num_fold);	
		
		for(int i = 0; i<=num_fold-1;i++){
			ArrayList<ArrayList<String>> Test = all_set[i];
			ArrayList<ArrayList<String>> Train = new ArrayList<ArrayList<String>>();
			for(int j = 0; j<=num_fold-1;j++){
				if(i != j){
					Train.addAll(all_set[j]);
				}
			}
			HashMap<String, Integer> Classes = new HashMap<String, Integer>();
			for(ArrayList<String> dp : Train){
				String clas = dp.get(dp.size()-1);
				if(Classes.containsKey(clas))
					Classes.put(clas, Classes.get(clas)+1);
				else
					Classes.put(clas, 1);				
			}
			int numTrees=7;
			int M=Train.get(0).size()-1;
//			int Ms = (int)Math.round(Math.log(M)/Math.log(2)+1);
			int Ms = 3;
			int C = Classes.size();
			RandomForestCateg RFC = new RandomForestCateg(numTrees, M, Ms, C, Train, Test);
			ave_acc+=RFC.Start();
		}
			return ave_acc/num_fold;			
	}
	
	public double do_cross_validate(ArrayList<ArrayList<String>> all,  int num_fold, int numTrees, int Ms){
		double ave_acc = 0.0;

		ArrayList<ArrayList<String>>[] all_set = trans2folder(all, num_fold);	
		
		for(int i = 0; i<=num_fold-1;i++){
			ArrayList<ArrayList<String>> Test = all_set[i];
			ArrayList<ArrayList<String>> Train = new ArrayList<ArrayList<String>>();
			for(int j = 0; j<=num_fold-1;j++){
				if(i != j){
					Train.addAll(all_set[j]);
				}
			}
			HashMap<String, Integer> Classes = new HashMap<String, Integer>();
			for(ArrayList<String> dp : Train){
				String clas = dp.get(dp.size()-1);
				if(Classes.containsKey(clas))
					Classes.put(clas, Classes.get(clas)+1);
				else
					Classes.put(clas, 1);				
			}
			int M=Train.get(0).size()-1;
			int C = Classes.size();
			RandomForestCateg RFC = new RandomForestCateg(numTrees, M, Ms, C, Train, Test);
			ave_acc+=RFC.Start();
		}
		return ave_acc/num_fold;
	}
	
	public double runRF(){
		System.out.println("Random-Forest with Categorical support");
		System.out.println("Now Running");
		/*
		 * data has to be separated by either ',' or ' ' only...
		 */
		int categ=1;
		String traindata,testdata;
		if(categ>0){
			traindata="C:/Users/install/Desktop/hxs/TCM/hnc/anoval.txt";
			testdata="C:/Users/install/Desktop/hxs/TCM/hnc/anoval.txt";
		}else if(categ<0){
			traindata="Data.txt";
			testdata="Test.txt";
		}else{
			traindata="/home/mohammad/Desktop/Material/KDDTrain+.txt";
			testdata="/home/mohammad/Desktop/Material/KDDTest+.txt";
		}
		
		DescribeTreesCateg DT = new DescribeTreesCateg(traindata);
		ArrayList<ArrayList<String>> Train = DT.CreateInputCateg(traindata);
		ArrayList<ArrayList<String>> Test = DT.CreateInputCateg(testdata);
		/*
		 * For class-labels 
		 */
		HashMap<String, Integer> Classes = new HashMap<String, Integer>();
		for(ArrayList<String> dp : Train){
			String clas = dp.get(dp.size()-1);
			if(Classes.containsKey(clas))
				Classes.put(clas, Classes.get(clas)+1);
			else
				Classes.put(clas, 1);				
		}
		
		int numTrees=10;
		int M=Train.get(0).size()-1;
		int Ms = (int)Math.round(Math.log(M)/Math.log(2)+1);
		int C = Classes.size();
		RandomForestCateg RFC = new RandomForestCateg(numTrees, M, Ms, C, Train, Test);
		return RFC.Start();
	}
	
	public static void main(String[] args){
		MainRunCateg mrc = new MainRunCateg();
		double acc = mrc.do_cross_validate("C:/Users/install/Desktop/hxs/TCM/hnc/all.txt", 10);
		System.out.println("acc = "+acc);
		
	}
}
