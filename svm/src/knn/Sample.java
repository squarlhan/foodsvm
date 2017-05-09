package knn;

public class Sample {
	
	private double[] features;
	private int lable;
	
	
	public Sample() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Sample(double[] features, int lable) {
		super();
		this.features = features;
		this.lable = lable;
	}



	public double[] getFeatures() {
		return features;
	}
	public void setFeatures(double[] features) {
		this.features = features;
	}
	public int getLable() {
		return lable;
	}
	public void setLable(int lable) {
		this.lable = lable;
	}
	
	

}
