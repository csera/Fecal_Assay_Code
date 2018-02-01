package imageAnalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ij.IJ;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.plugin.filter.ParticleAnalyzer;

import dataAnalysis.CSV_Compiler;

//TODO: make this multi-threaded to boost performance
/* Current issues:
 * 		- can't call macro unless a hard-coded aboslute path is used
 */

/* Code outline
* 
* imgProcessor(folderPath) - iterate through all files in folderPath
*   foreach(file)
*       myThresholderMacro
*       particle_analyzer --> save results as csv
*       getOutputImage --> save
* 
* 
* --new class: csvSummary--
* int N, grey, area
* 
* public csvSummary(int n, int meanGrey, int meanArea)
* 	N = n
* 	grey = meanGrey
* 	area = meanArea
* 
* setN
* getN
* 
* setGrey
* getGrey
* 
* setArea
* getArea
* 
*/

public class Analyzer implements PlugIn {

	//This method is just so the code can be run from the IDE
	public static void main(String[] args) {
		//final ImageJ ij = new ImageJ();
		Analyzer a = new Analyzer();
		a.run("");
	}
	
	/* Outline:
	 * iterate ImgProcesor
	 * 	in: img
	 *  out: csv
	 * 
	 *  ArrayList<csvSummary> csvList = new ArrayList<csvSummary>
	 *   
	 *   iterate CsvSummarizer
	 * 		in: csv
	 * 		do: avg the areas, avg the mean greys, get n
	 * 		out: new element for csvList
	 * 	
	 * 	csvWriter
	 * 		out: csv from csvList
	 */
	public void run(String s) {
		IJ.log("Run initiated");
		Path imgRoot = getDir("Select a source folder");
		
		if(imgRoot == null) {
			System.exit(0);
		}
		
		List<Path> imgs = getImgs(imgRoot);
		
		Path saveDir = getDir("Select a folder to save in");
		
		if(saveDir == null) {
			System.exit(0);
		}
		
		for(Path p : imgs) {
			analyze(p, saveDir);
		}
		
		int cont = JOptionPane.showOptionDialog(null,
				"At this time, do you want to perform data compilation \n"+
				"and analysis on the .csv files just created?",
				"Continue analysis?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,null,null);
		if(cont == JOptionPane.YES_OPTION) {
			System.out.println("Proceding to data analysis");
			CSV_Compiler dataAnalyzer = new CSV_Compiler();
			dataAnalyzer.run(saveDir.toString());
		}
		else {
			System.out.println("Exit program");
			System.exit(0);
		}
		
	}
	
	public static Path getDir(String title) {
		Path dir;
		
		JFileChooser dirSel = new JFileChooser(new File("."));
		dirSel.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirSel.setDialogTitle(title);
		
		if(dirSel.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	
			File f = dirSel.getSelectedFile();
			dir = f.toPath();
			System.out.println(title+" = "+dir);
			
			return dir;
		}
		else {
			return null;
		}
	}
	
	//TODO: filter out any non-img file types declared
	public static List<Path> getImgs(Path p) {
		List<Path> imgs;
		
		try {
			
			imgs = Files.list(p) //returns a *Stream* of all files & folders in Path p
					.filter(Files::isRegularFile) //excludes any folders
					.collect(Collectors.toList()); //collects returned val --> List
			
			System.out.println("Files found:");
			imgs.forEach(System.out::println);
			
			return imgs;
		} catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	/*TODO: get IJ.runMacroFile working with a relative path...
	 * 		get proper measurements (it's doing IntDen & RawIntDen)
	 */
	//Note: rt.save() returns null if no particles are detected
	/* imgProcessor(folderPath) - iterate through all files in folderPath
	*   foreach(file)
	*       myThresholderMacro
	*       particle_analyzer --> save results as csv
	*       getOutputImage --> save
	*/
	public static void analyze(Path p, Path saveDir) {
		String pString = p.toString();
		
		System.out.println("Working on: \n"+p);
		
		IJ.open(pString);
		//IJ.runMacroFile("/resources/green_threshold.ijm"); //not finding
		IJ.runMacroFile("C:/Users/oddba/Fiji/plugins/Macros/green_threshold.ijm");
			//the above works... @_@
		
		//Make rt explicitly to be able to reference later
		ResultsTable rt = new ResultsTable();
		
		//Make ParticleAnalyzer with custom settings
		ParticleAnalyzer pa = new ParticleAnalyzer(
			ParticleAnalyzer.SHOW_OVERLAY_OUTLINES + ParticleAnalyzer.INCLUDE_HOLES + 
			ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES + ParticleAnalyzer.IN_SITU_SHOW,
			Measurements.AREA + Measurements.MEAN + 
				Measurements.INTEGRATED_DENSITY,
			rt,250,Double.POSITIVE_INFINITY,0.2,1.0);
		
		pa.analyze(IJ.getImage());
		
		String sampName = p.getFileName().toString();
		int nameEnd = sampName.lastIndexOf(".");
		sampName = sampName.substring(0, nameEnd);
		
		System.out.println("Saving "+saveDir.toString()+"\\"+sampName+".csv");
		rt.save(saveDir.toString()+"/"+sampName+".csv");
		System.out.println("Saving "+saveDir.toString()+"\\"+sampName+".jpg");
		IJ.save(saveDir.toString()+"/"+sampName+".jpg");
		
	}

}