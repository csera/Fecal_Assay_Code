package dataAnalysis;

public class DataObj {
	private String name;
	private int N, area, mean, intDen;
	
	public DataObj(String n) {
		this.name = n;
	}
	public DataObj(String n, int num, int a, int m, int id) {
		this.name = n;
		this.N = num;
		this.area = a;
		this.mean = m;
		this.intDen = id;
	}
	public DataObj(int num, int a, int m, int id) {
		this.N = num;
		this.area = a;
		this.mean = m;
		this.intDen = id;
	}
	
	public void setName(String n) {
		name = n;
	}
	public String getName() {
		return name;
	}
	
	public void setNum(int num) {
		N = num;
	}
	public int getNum() {
		return N;
	}
	
	public void setArea(int a) {
		area = a;
	}
	public int getArea() {
		return area;
	}
	
	public void setMean(int m) {
		mean = m;
	}
	public int getMean() {
		return mean;
	}
	
	public void setIntDen(int d) {
		intDen = d;
	}
	public int getIntDen() {
		return intDen;
	}
	
}
