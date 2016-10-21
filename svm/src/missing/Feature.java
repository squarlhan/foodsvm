package missing;

public class Feature {
	
	private int rank;
	private String name;
	private double p;	
	private double null_rate;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	public double getNull_rate() {
		return null_rate;
	}
	public void setNull_rate(double null_rate) {
		this.null_rate = null_rate;
	}
	public Feature(int rank, String name, double p, double null_rate) {
		super();
		this.rank = rank;
		this.name = name;
		this.p = p;
		this.null_rate = null_rate;
	}
	public Feature() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
