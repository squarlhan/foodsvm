package knn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KNN {
	
	private double[][] distances = null;
	private int K;
	
	
	
	public KNN(int k) {
		super();
		K = k;
	}

	public double calcdistancewithlabel(double[] a, double[] b){
		double res = 0;
	    int l = a.length;
	    for(int i = 0;i<=l-2;i++){
	    	res+=Math.pow(a[i]-b[i], 2);
	    }
	    res = Math.sqrt(res);
	    return res;
	}
	
	public double calcdistancewithoutlabel(double[] a, double[] b){
		double res = 0;
	    int l = a.length;
	    for(int i = 0;i<=l-1;i++){
	    	res+=Math.pow(a[i]-b[i], 2);
	    }
	    res = Math.sqrt(res);
	    return res;
	}
	
	public void calcdistances(double[][] all){
		int l = all.length;
		distances = new double[l][l];
		for(int i = 0;i<=l-1;i++){
			for(int j = 0;j<=l-1;j++){
				if(i==j){
					distances[i][j] = 0;
				}else if(i<j){
					distances[i][j] = calcdistancewithlabel(all[i], all[j]);
					distances[j][i] = distances[i][j];
				}
			}
		}
	}
	
	public int insert(double d, List<Double> dises){
		int s = dises.size();
		if(s>0){
			for(int i = 0;i<=s-1;i++){
				if(d<dises.get(i)){
					return i;
				}
			}
			return s;

		}
		return 0;
	}
	
	public double runknn(double[][] all, double[] b){
		double res = 0;
		Set<Double> labels= new HashSet();
		int m = all.length;
		int n = all[0].length;
		
		
		List<Double> dises = new ArrayList();
		List<Integer> indexes = new ArrayList();
		
		for(int i = 0; i<=m-1;i++){
			labels.add(all[i][n-1]);
			double dis = calcdistancewithlabel(all[i], b);
			int index = insert(dis, dises);
			dises.add(index, dis);
			indexes.add(index, i);
		}
		List<Double> lls = new ArrayList();
		lls.addAll(labels);
		int s = lls.size();
		int[] counts = new int[s];
		for(int c:counts){
			c = 0;
		}

		for(int i = 0; i<=K-1;i++){
			int index = lls.indexOf(all[indexes.get(i)][n-1]);
			counts[index]++;
		}
		
		int t = 0;
		for(int i = 0; i<=s-1;i++){
			if(counts[t]<counts[i]){
				t = i;
			}
		}
		res = lls.get(t);
		return res;
	}
	
	public double[] doknn(double[][] all, double[] b){

		Set<Double> labels= new HashSet();
		int m = all.length;
		int n = all[0].length;
		
		
		List<Double> dises = new ArrayList();
		List<Integer> indexes = new ArrayList();
		
		for(int i = 0; i<=m-1;i++){
			labels.add(all[i][n-1]);
			double dis = calcdistancewithlabel(all[i], b);
			int index = insert(dis, dises);
			dises.add(index, dis);
			indexes.add(index, i);
		}

		int[] counts = new int[labels.size()];
		for(int c:counts){
			c = 0;
		}

		for(int i = 0; i<=K-1;i++){
			int index = (int)all[indexes.get(i)][n-1];
			counts[index]++;
		}
		int sum = 0;
		for(int c:counts){
			sum+=c;
		}
		double[] res= new double[labels.size()];
		for(int i = 0; i<=labels.size()-1;i++){
			res[i] = 1.0*counts[i]/sum;
		}
		return res;
	}
	
	public double do_crossover_validation(double[][] all, int num_fold){
		double acc = 0.0;
		if(distances == null)calcdistances(all);
		
		int m = all.length;
		int n = all[0].length;
		
		for (int i = 0; i <= num_fold - 1; i++) {
			List<Integer> train = new ArrayList();
			List<Integer> test = new ArrayList();
			for(int j = 0; j<=m-1;j++){
				if(j%num_fold==0){
					test.add(j);
				}else{
					train.add(j);
				}
			}
			
			int correct = 0;
			
			for (int te : test) {
				Set<Double> labels = new HashSet();
				List<Double> dises = new ArrayList();
				List<Integer> indexes = new ArrayList();
				for (int j = 0; j <= train.size() - 1; j++) {
					labels.add(all[train.get(j)][n - 1]);
					double dis = distances[train.get(j)][te];
					int index = insert(dis, dises);
					dises.add(index, dis);
					indexes.add(index, j);
				}
				List<Double> lls = new ArrayList();
				lls.addAll(labels);
				int s = lls.size();
				int[] counts = new int[s];
				for (int c : counts) {
					c = 0;
				}

				for (int j = 0; j <= K - 1; j++) {
					int index = lls.indexOf(all[indexes.get(j)][n - 1]);
					counts[index]++;
				}

				int t = 0;
				for (int j = 0; j <= s - 1; j++) {
					if (counts[t] < counts[j]) {
						t = j;
					}
				}
				double res = lls.get(t);
				if(all[te][n-1]==res)correct++;
			}
			
			acc+=(1.0*correct/test.size());
			
		}
		
		acc = acc/num_fold;
		return acc;
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

}
