package playground.mmoyo.analysis.counts.chen;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class CountsComparingGraph {
	String dirPath;
	String SEPARATOR = "/";
	
	public CountsComparingGraph(String dirPath) {
		this.dirPath = dirPath;
	}

	private void compareGraphs() throws IOException{
		File dir = new File(this.dirPath);    	
		File headerFile = new File ("../shared-svn/studies/countries/de/berlin-bvg09/ptManuel/lines344_M44/counts/chen/comparingGraphs/volume_comparison/header.png");
		
		//assuming the iteration 0 
		String ITERPATH = "/ITERS/it.0/graphs/stationCounts/";    //extract files from kml to  [graphs/stationCounts/]  or  [graphs/stationCounts/]  
		
		//get algorithms names and graphs names
		List<String> algList = new ArrayList <String>(); 
		List<String> graphList = new ArrayList <String>();
		boolean isFirst = true;
		for (int i= 0; i< dir.list().length ; i++){
			String alg = dir.list()[i];
			if (!(alg.equals(".svn") || alg.equals("info.txt"))){
				algList.add(alg);
				if (isFirst){
					File graphDir1 = new File(this.dirPath + SEPARATOR +  alg + ITERPATH);   ///stationCounts
					for (int ii= 0; ii< graphDir1.list().length; ii++){
						String graphName= graphDir1.list()[ii];
						if (!(graphName.equals(".svn") || graphName.equals("info.txt"))){
							graphList.add(graphName);
						}
					}
				}
			}
		}
		
		//merge graphs in one image
		BufferedImage headImg = ImageIO.read(headerFile);
		for (String graphName : graphList ){
			
			String file1Path = this.dirPath + SEPARATOR +  algList.get(0) + ITERPATH + graphName;
			String file2Path = this.dirPath + SEPARATOR +  algList.get(1) + ITERPATH + graphName;
			String file3Path = this.dirPath + SEPARATOR +  algList.get(2) + ITERPATH + graphName;
			
			System.out.println( "processing: "  + graphName + " " + algList.get(0) + " " + algList.get(1) + " " + algList.get(2));
			
			File graphFile1 = new File(file1Path);
			File graphFile2 = new File(file2Path);
			File graphFile3 = new File(file3Path);

			BufferedImage img1 = null;
			BufferedImage img2 = null;
			BufferedImage img3 = null;
			
			if(graphFile1.exists()) img1 = ImageIO.read(graphFile1); else System.out.println("graph does not exist: " + file1Path);
			if(graphFile2.exists()) img2 = ImageIO.read(graphFile2); else System.out.println("graph does not exist: " + file2Path);
			if(graphFile3.exists()) img3 = ImageIO.read(graphFile3); else System.out.println("graph does not exist: " + file3Path); 
			 
			//300= height    400= width
			BufferedImage combined = new BufferedImage(400*3, 300 + headImg.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = combined.getGraphics();
			g.drawImage(headImg,0,0, null);
			g.drawImage(img1, 0, headImg.getHeight(), null);
			g.drawImage(img2, 400, headImg.getHeight(), null);
			g.drawImage(img3, 400 + 400, headImg.getHeight(), null);
			g.dispose();
		
			File outputFile = new File("../playgrounds/mmoyo/output/" + graphName);
			ImageIO.write(combined,"png", outputFile);	
		}
			
	}
	
	public static void main(String[] args) {
		String dir = "../shared-svn/studies/countries/de/berlin-bvg09/ptManuel/lines344_M44/counts/chen/KMZ_counts_scalefactor50";
	
		CountsComparingGraph countsComparingGraph2 = new CountsComparingGraph(dir);

		try {
			countsComparingGraph2.compareGraphs();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
