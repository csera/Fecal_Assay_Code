package dataAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;

import com.opencsv.CSVReader;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ij.plugin.PlugIn;

import imageAnalysis.Analyzer;;


/* Current issues: listAvg() does not always actually take avg of n 
 * 		--> listSD() won't work with it if expanded beyond current functionality
 */
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
		
		String workingDir = Analyzer.getDir("Select a source folder",null).toString();
		
		CSV_Compiler a = new CSV_Compiler();
		a.run(workingDir); //temp hard-coded dir
	}

	public void run(String csvRoot) { //Have to use String var type to implement PlugIn
		
		Path csvDir = Paths.get(csvRoot);
		
		List<Path> csvList = getCsvs(csvDir);
		List<DataObj> csvSummary = new ArrayList<>();
		
		for(Path p : csvList) {
			System.out.println("Working on: "+p);
			csvSummary.add(summarizeCsv(p));
		}
		
		//for testing code against a single csv
		//summarizeCsv(Paths.get("C:/Users/oddba/Pictures/ImgeJ Output/Untitled.csv"));
		
		DataObj superAvg = listAvg(csvSummary,false);
		superAvg.setName("Group avg");
		superAvg.printProps();
		
		DataObj superSD = listSD(csvSummary,superAvg);
		superSD.setName("Group SD");
		superSD.printProps();
		
		try {
			writeCsv(csvSummary,superAvg,superSD,csvRoot);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvDataTypeMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		String type = item.getFileName().toString();
		int typeStart = type.lastIndexOf(".");
		//lastIndexOf() returns -1 if no match found
		//Recall: Strings are zero-indexed
		
		if(typeStart >= 0) {
			type = type.substring(typeStart);
			
			if(type.equalsIgnoreCase(".csv")) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public DataObj summarizeCsv(Path csvPath) {
		
		List<DataObj> csvItems = parseCsv(csvPath);
		
		DataObj csvSummary = listAvg(csvItems,true);
		
		//Give csvSummary name of the file it summarizes
		String sampName = csvPath.getFileName().toString();
		int nameEnd = sampName.lastIndexOf(".");
		sampName = sampName.substring(0, nameEnd);
		csvSummary.setName(sampName);
		
		csvSummary.printProps();
		
		return csvSummary;
	}
	
	public List<DataObj> parseCsv(Path csvPath) {
		List<DataObj> csvItems = new ArrayList<>();
		
		try {
			CSVReader reader = new CSVReader(new FileReader(csvPath.toString()));

			int rowNum = 0;
			String[] line;
			
			while((line = reader.readNext()) != null) {
				if(rowNum != 0) {
					DataObj lineObj = new DataObj(
							line[0], //name is the measurement #
							1, //"number" [of data points] = 1
							Double.parseDouble(line[1]), //Area
							Double.parseDouble(line[2]), //Mean
							Double.parseDouble(line[3])); //IntDen
					
					csvItems.add(lineObj);
				}
				
				rowNum++;
				
				System.out.println(Arrays.toString(line));
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return csvItems;
	}
	
	public DataObj listAvg(List<DataObj> list, boolean fromFile) {
		DataObj avgs;

		System.out.println("averaging...");
		
		double n = 0, area = 0, mean = 0, intDen = 0;
		
		//Compute total # of data points for avgs's num if using data from a file
		if(fromFile) {
			for(DataObj o : list) {
				area += o.getArea();
				mean += o.getMean();
				intDen += o.getIntDen();
				
				n++;
			}
			
			area = area/n;
			mean = mean/n;
			intDen = intDen/n;
			
			System.out.println("Done averaging");
			avgs = new DataObj(n,area,mean,intDen);
		}
		//If actually reading a list of csv summaries, do get actual avg(n)
		else {
			double num = 0;
			
			for(DataObj o : list) {
				num += o.getNum();
				area += o.getArea();
				mean += o.getMean();
				intDen += o.getIntDen();
				
				n++;
			}
			
			num = num/n;
			area = area/n;
			mean = mean/n;
			intDen = intDen/n;
			
			System.out.println("Done averaging");
			avgs = new DataObj(num,area,mean,intDen);
		}
		
		return avgs;
	}
	
	public DataObj listSD(List<DataObj> list, DataObj avgs) {
		DataObj sdObj;
		double n = 0, sN = 0, sArea = 0, sMean = 0, sIntDen = 0;
		
		for(DataObj o : list) {
			sN += Math.pow(o.getNum()-avgs.getNum(),2);
			sArea += Math.pow(o.getArea()-avgs.getArea(),2);
			sMean += Math.pow(o.getMean()-avgs.getMean(),2);
			sIntDen += Math.pow(o.getIntDen()-avgs.getIntDen(),2);
			
			n++;
		}
		
		sN = Math.sqrt(sN/(n-1)); //only meaningful for the SD of the whole data set
		sArea = Math.sqrt(sArea/(n-1));
		sMean = Math.sqrt(sMean/(n-1));
		sIntDen = Math.sqrt(sIntDen/(n-1));
		
		sdObj = new DataObj("SD",sN,sArea,sMean,sIntDen);
		
		return sdObj;
	}
	
	private void writeCsv(List<DataObj> csvSummary, DataObj avgs, DataObj SDs, String csvRoot) 
		throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		
		//Add avgs and SDs to start of csvSummary
		csvSummary.add(0,SDs);
		csvSummary.add(0,avgs);
		
		JFileChooser saver = new JFileChooser(csvRoot);
			//default dir is the .csv source dir
			//default selection mode: files only
		saver.setDialogTitle("Save data summary as .csv (do not type .csv in name field)");
		
		if(saver.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {	
			File f = saver.getSelectedFile();
			System.out.println("Saving as: "+f+".csv");

			FileWriter writer = new FileWriter(f+".csv");
			StatefulBeanToCsvBuilder<DataObj> beanBuilder = new StatefulBeanToCsvBuilder<>(writer);
			StatefulBeanToCsv<DataObj> beanWriter = beanBuilder.build();
			
			beanWriter.write(csvSummary);
			
			writer.close();
		}
		else {
			return;
		}
		
	}
}