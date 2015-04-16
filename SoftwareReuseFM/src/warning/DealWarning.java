package warning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class DealWarning {
	
	private String warningInfo;
	private String outputPath;
	
	//无参数
	public DealWarning() {
		this.warningInfo = "No warning information!";
		this.outputPath = "";
	}
	//一个参数
	public DealWarning(String info) {
		this.warningInfo = info;
		this.outputPath = "";
	}
	//两个参数
	public DealWarning(String info, String filepath) {
		this.warningInfo = info;
		this.outputPath = filepath;
	}
	
	public void OutputInfo() {

		try {
			if(outputPath=="") {
				long date = new Date().getTime();
				String path  = Long.toString(date);
				outputPath = "./error_" + path + ".txt";
			}
			File outputFile = new File(outputPath);
			if(!outputFile.exists()) {
				outputFile.createNewFile();
			}
			FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(warningInfo);
			bw.close();
			System.out.println("success!");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("something wrong!");
			e.printStackTrace();
		}
		
	}
}
