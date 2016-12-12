package kmean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgap.*;

import testcase.DataProcess;

import org.encog.ml.MLCluster;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.kmeans.KMeansClustering;
import org.encog.ml.kmeans.KMeansCluster;
import org.encog.ml.kmeans.Centroid;

public class KMeans {
	
	private int hot = 3035;
	private int neutral = 5909;
	private int cold = 7706;

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

	public double writeresult(String addr, double[][] data, List<Integer> result) {
		int r = data.length;
		int c = data[0].length;
		File f = new File(addr);
		int count = 0;
		try {
			if (f.exists()) {
				f.delete();

				f.createNewFile();

			} else {
				f.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (int i = 0; i <= r - 1; i++) {
				bw.write(String.valueOf(data[i][c - 1]) + '\t');
				bw.write(String.valueOf(result.get(i)) + '\n');
				bw.flush();
				double label = 0.0;
				if(result.get(i)<=hot){
					label = 2.0;
				}else if(result.get(i)>neutral){
					label = 0.0;
				}else{
					label = 1.0;
				}
				if(data[i][c - 1]==label){
					count++;
				}
			}
			bw.write(String.valueOf(count*1.0/r) + '\n');
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count*1.0/r;
	}

	public List<Integer> runKmeans(double[][] data, int cluster_number, int max_gen) {
		int currentPopSize =data.length;
		// if all of the chromosomes contain constant genes?
		int currentChromSize = data[0].length-1;
		// Ensure all chromosomes are updated.
		// -----------------------------------
		double[][] KmeansDataSet = new double[currentPopSize][currentChromSize];
		for (int i = 0; i < currentPopSize; i++) {
			for (int j = 0; j < currentChromSize; j++) {
				KmeansDataSet[i][j] = data[i][j];
				/*
				 * if((Boolean)chrom.getGene(j).getAllele()){
				 * KmeansDataSet[i][j]=1.9; } else{ KmeansDataSet[i][j]=0.25; }
				 */
			}
		}

		final BasicMLDataSet set = new BasicMLDataSet();
		for (final double[] element : KmeansDataSet) {
			set.add(new BasicMLData(element));
		}

		int num = cluster_number;
		final KMeansClustering kmeans = new KMeansClustering(num, set);
		kmeans.iteration(max_gen);
		// System.out.println("Final WCSS: " + kmeans.getWCSS());

		int[] centroidchrom = new int[kmeans.numClusters()];
		int[] chromorderflag = new int[kmeans.numClusters()];
		List<Integer> chromorder = new ArrayList();
		int[] chromflag = new int[currentPopSize];
		int[] clusterflag = new int[currentPopSize];
		int[] cacflag = new int[currentPopSize];

		for (int k = 0; k < kmeans.getClusters().length; k++) {
			MLCluster cluster = (kmeans.getClusters())[k];
			KMeansCluster kmeanscluster = (KMeansCluster) cluster;
			Centroid centroid = kmeanscluster.getCentroid();
			int minorder = 0;
			double a = 0, MIN = Double.MAX_VALUE;
			for (int i = 0; i < cluster.size(); i++) {
				if ((a = KMeansClustering.calculateEuclideanDistance(centroid, cluster.get(i))) < MIN) {
					minorder = i;
					MIN = a;
					// System.out.print(MIN+" ");
				}
			}
			centroidchrom[k] = minorder;
			/*
			 * System.out.println("  "); System.out.print(centroidchrom[k]+"  "
			 * ); System.out.println("  "); System.out.println("  ");
			 */

		}

		for (int j = 0; j < currentPopSize; j++) {
			for (int k = 0; k < kmeans.getClusters().length; k++) {
				MLCluster cluster = (kmeans.getClusters())[k];
				KMeansCluster kmeanscluster = (KMeansCluster) cluster;
				Centroid centroid = kmeanscluster.getCentroid();
				for (int i = 0; i < cluster.size(); i++) {
					MLDataSet ds = cluster.createDataSet();
					MLDataPair pair = BasicMLDataPair.createPair(ds.getInputSize(), ds.getIdealSize());
					ds.getRecord(i, pair);
					double[] clusterdata = new double[pair.getInputArray().length];
					clusterdata = pair.getInputArray();
					boolean flag = true;
					for (int m = 0; m < clusterdata.length; m++) {
						if (clusterdata[m] != KmeansDataSet[j][m]) {
							flag = false;
							break;
						}
					}
					if (flag == true) {

						clusterflag[j] = k;
						chromflag[j] = i;

						break;
					}
				}
			}
		}

		for (int i = 0; i < centroidchrom.length; i++) {
			for (int k = 0; k < clusterflag.length; k++) {
				if (i == clusterflag[k]) {
					if (centroidchrom[i] == chromflag[k]) {
						chromorderflag[i] = k;
					}
				}
			}
		}
		for (int i = 0; i < currentPopSize; i++) {
			chromorder.add(chromorderflag[clusterflag[i]]);
		}

		return chromorder;
	}
	
	public void pipeline(){
//		String dataaddr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/matrix_data/";
//		String outaddr = "C:/Users/install/Desktop/hxs/TCM/hnc/nd/missing/matrix_data/km/";
		String dataaddr = "./matrix_data/";
		String outaddr = "./matrix_data/km/";
		DataProcess dp = new  DataProcess();
		Map<Integer,String> map = dp.getdata();
		int cluster_number = 3;
		int max_gen = 100;
		
		int mn = map.size();
		for(int i = 1;i<=mn;i++){
			String name = map.get(i);
			String fileaddr = dataaddr+name+".txt";	
			String outfileaddr = outaddr+name+".txt";	
			double[][] data = readdata(fileaddr);			
			List<Integer> result = runKmeans(data, cluster_number, max_gen);
			System.out.println(name+": "+writeresult(outfileaddr, data, result));
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KMeans km = new KMeans();
		km.pipeline();

	}

}
