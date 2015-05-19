package warning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class DealWearning {
	
	private String warningInfo;
	private String outputPath;
	
	public DealWearning() {
		this.warningInfo = "No warning information!";
		this.outputPath = "";
	}
	public DealWearning(String info, String filepath) {
		this.warningInfo = info;
		this.outputPath = filepath;
	}
	
	public void OutputInfo() {

		try {
			File outputFile = new File(outputPath);
			if(!outputFile.exists()) {
				outputFile.createNewFile();
			}
			FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(warningInfo);
			bw.close();
			System.out.println("The warnning information has be output to file" + outputPath);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("something wrong!");
			e.printStackTrace();
		}
		
	}
}
