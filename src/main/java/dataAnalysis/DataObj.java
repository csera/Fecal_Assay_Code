package dataAnalysis;

public class DataObj {
	private String Name;
	private double Num, Area, Mean, IntDen;
	
	public DataObj(String n) {
		this.Name = n;
	}
	public DataObj(String n, double num, double a, double m, double id) {
		this.Name = n;
		this.Num = num;
		this.Area = a;
		this.Mean = m;
		this.IntDen = id;
	}
	public DataObj(double num, double a, double m, double id) {
		this.Num = num;
		this.Area = a;
		this.Mean = m;
		this.IntDen = id;
	}
	
	public void setName(String n) {
		Name = n;
	}
	public String getName() {
		return Name;
	}
	
	public void setNum(double num) {
		Num = num;
	}
	public double getNum() {
		return Num;
	}
	
	public void setArea(double a) {
		Area = a;
	}
	public double getArea() {
		return Area;
	}
	
	public void setMean(double m) {
		Mean = m;
	}
	public double getMean() {
		return Mean;
	}
	
	public void setIntDen(double d) {
		IntDen = d;
	}
	public double getIntDen() {
		return IntDen;
	}
	
	public void printProps() {
		System.out.println("Printing values of "+Name);
		System.out.println("Num: "+Num);
		System.out.println("Area: "+Area);
		System.out.println("Mean: "+Mean);
		System.out.println("IntDen: "+IntDen+"\n");
	}
	
}
