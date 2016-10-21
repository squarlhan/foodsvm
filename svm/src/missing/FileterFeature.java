package missing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileterFeature {
	
	private List<Feature> all;
	private List<Feature> fileted;
	private String addr;
	
	
	
	public FileterFeature(String addr) {
		super();
		all = new ArrayList<Feature>();
		fileted = new ArrayList<Feature>();
		this.addr = addr;
	}
	public FileterFeature() {
		super();
		all = new ArrayList<Feature>();
		fileted = new ArrayList<Feature>();
		// TODO Auto-generated constructor stub
	}
	public List<Feature> getAll() {
		return all;
	}
	public void setAll(List<Feature> all) {
		this.all = all;
	}
	public List<Feature> getFileted() {
		return fileted;
	}
	public void setFileted(List<Feature> fileted) {
		this.fileted = fileted;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	public void read(){
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(addr));
			BufferedReader reader = new BufferedReader(ir);

			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] lines = line.split("\t");
				Feature f = new Feature();
				f.setRank(Integer.parseInt(lines[0]));
				f.setName(lines[1].trim());
				f.setP(Double.valueOf(lines[2]));
				f.setNull_rate(Double.valueOf(lines[3]));
                all.add(f);
			}
			ir.close();
			reader.close();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
