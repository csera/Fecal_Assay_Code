package dataAnalysis;

public class DataObj {
	private String name;
	private double N, area, mean, intDen;
	
	public DataObj(String n) {
		this.name = n;
	}
	public DataObj(String n, double num, double a, double m, double id) {
		this.name = n;
		this.N = num;
		this.area = a;
		this.mean = m;
		this.intDen = id;
	}
	public DataObj(double num, double a, double m, double id) {
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
	
	public void setNum(double num) {
		N = num;
	}
	public double getNum() {
		return N;
	}
	
	public void setArea(double a) {
		area = a;
	}
	public double getArea() {
		return area;
	}
	
	public void setMean(double m) {
		mean = m;
	}
	public double getMean() {
		return mean;
	}
	
	public void setIntDen(double d) {
		intDen = d;
	}
	public double getIntDen() {
		return intDen;
	}
	
	public void printProps() {
		System.out.println("Printing values of "+name);
		System.out.println("Num: "+N);
		System.out.println("Area: "+area);
		System.out.println("Mean: "+mean);
		System.out.println("IntDen: "+intDen);
	}
	
}
