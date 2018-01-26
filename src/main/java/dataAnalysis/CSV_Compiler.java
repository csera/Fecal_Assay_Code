package dataAnalysis;

import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		
		Path csvDir = Paths.get(csvRoot);
		
		List<Path> csvList = getCsvs(csvDir);
		
	}
	
	public List<Path> getCsvs (Path dir){
		List<Path> csvList;
		try {
			
			csvList = Files.list(dir) //returns a *Stream* of all files & folders in Path p
					.filter(item -> isCsv(item)) //filters for .csv files
					.collect(Collectors.toList()); //collects output --> List
			
			System.out.println("Files found:");
			csvList.forEach(System.out::println);
			
			return csvList;
		} catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public boolean isCsv(Path item) {
		boolean csv;
		
		String type = item.getFileName().toString();
		int typeStart = type.lastIndexOf("."); //recall: Strings are zero-indexed
		type = type.substring(typeStart);
		
		if(type.equalsIgnoreCase(".csv")) {
			csv = true;
		}
		else {
			csv = false;
		}
		
		return csv;
	}
	
	public DataObj listAvg(List<DataObj> list) {
		DataObj avgs;
		
		int n = 0, area = 0, mean = 0, intDen = 0;
		
		for(DataObj o : list) {
			area += o.getArea();
			mean += o.getMean();
			intDen += o.getIntDen();
			
			n++;
		}
		
		avgs = new DataObj(n,area,mean,intDen);
		
		return avgs;
	}
	
	public DataObj listSD(List<DataObj> list, DataObj avgs) {
		DataObj sdObj;
		int n = 0, sN = 0, sArea = 0, sMean = 0, sIntDen = 0;
		
		for(DataObj o : list) {
			sN += (int) Math.pow(o.getNum()-avgs.getNum(),2);
			sArea += (int) Math.pow(o.getArea()-avgs.getArea(),2);
			sMean += (int) Math.pow(o.getMean()-avgs.getMean(),2);
			sIntDen += (int) Math.pow(o.getIntDen()-avgs.getIntDen(),2);
			
			n++;
		}
		
		sN = (int) Math.sqrt(sN/(n-1));
		sArea = (int) Math.sqrt(sArea/(n-1));
		sMean = (int) Math.sqrt(sMean/(n-1));
		sIntDen = (int) Math.sqrt(sIntDen/(n-1));
		
		sdObj = new DataObj("SD",sN,sArea,sMean,sIntDen);
		
		return sdObj;
	}

}