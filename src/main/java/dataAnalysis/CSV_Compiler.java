package dataAnalysis;

import java.io.FileReader;
import java.nio.file.Path;

import ij.plugin.PlugIn;

/* Code outline:
 * 
 * main
 * 		Get file dir
 * 		dataFiles = list of .csv files in the dir
 * 		
 * 		List<obj> avgs = avgFiles(dataFiles)
 * 			//where file name & # of data points are also recorded
 * 
 * 		Hashmap<name,dataObj> compStats = avgAndSD(avgs)
 * 			//each obj has props: name, measurements, SD for each meas
 * 
 * 		fileWriter fw = new fileWriter
 * 		format to write in:
 * 			" , Area, Mean, IntDen \n"+
 * 			"Dataset Avgs, "+ compStats.area.getAvg+", "+ 
 * 				compStats.mean.getAvg+", "+compStats.intDen.getAvg+"\n"+
 * 			"Dataset SDs, "+ compStats.area.getSD+", "+ 
 * 				compStats.mean.getSD+", "+compStats.intDen.getSD+"\n"+
 * 			foreach(a in avgs)
 * 				a.name +", "+a.area+", "+a.mean+", "+a.intDen+"\n"
 * 		
 * 		save(fw)
 * 
 * avgFiles(dataFiles)
 * 		Hashmap<name,dataObj> avgs = new Hashmap<>()
 *		
 *		foreach(Path p in dataFiles)
 *			dataObj area = new dataObj()
 *			dataObj mean = new dataObj()
 *			dataObj intDen = new dataObj()
 *			
 *			miscObj o = importCSV(p) //there must be a csv library...
 *			avgs.put(o.name,
 * 
 * avgAndSD(dataSet avgs)
 * 		Hashmap<name,dataObj> compStats = new Hashmap<>()
 *		
 *		dataObj area = new dataObj()
 *		dataObj mean = new dataObj()
 *		dataObj intDen = new dataObj()
 *		
 *		area.setAvg = avgs.area.average()
 *		area.setSD = avgs.area.SD()
 *		mean.setAvg = avgs.mean.average()
 *		mean.setSD = avgs.mean.SD()
 *		intDen.setAvg = avgs.intDen.average()
 *		intDen.setSD = avgs.intDen.SD()
 *		
 *		compStats.put("area", area)
 *		compStats.put("mean", area)
 *		compStats.put("intDen", area)
 *
 *		return compStats
 */

public class CSV_Compiler implements PlugIn {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CSV_Compiler a = new CSV_Compiler();
		a.run("C:/Users/oddba/Pictures/ImgeJ Output");
	}

	public void run(String csvRoot) {
		
		
		
	}
	
	

}