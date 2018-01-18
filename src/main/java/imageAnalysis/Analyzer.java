package imageAnalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;

import ij.IJ;
import ij.plugin.PlugIn;

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
	public static void main(String[] args) {
		Analyzer a = new Analyzer();
		a.run();
	}
	
	public static Path getDir(String title) {
		Path dir;
		
		//DirSelector dirWin = new DirSelector();
		//dir = dirWin.getDir();
		
		JFileChooser dirSel = new JFileChooser(new File("."));
		dirSel.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirSel.setDialogTitle(title);
		
		if(dirSel.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	
			File f = dirSel.getSelectedFile();
			dir = f.toPath();
			System.out.println("Dir: "+dir);
			
			return dir;
		}
		else {
			return null;
		}
	}
	
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
	
	/* imgProcessor(folderPath) - iterate through all files in folderPath
	*   foreach(file)
	*       myThresholderMacro
	*       particle_analyzer --> save results as csv
	*       getOutputImage --> save
	*/
	public static void analyze(List<Path> imgs, Path saveDir) {
		
		for(Path p : imgs) {
			
		}
	}

	@Override
	public void run() {
		Path imgRoot = getDir("Select a source folder");
		
		if(imgRoot == null) {
			System.exit(0);
		}
		
		List<Path> imgs = getImgs(imgRoot);
		
		Path saveDir = getDir("Select a save destination");
		
		analyze(imgs, saveDir);
	}

}
