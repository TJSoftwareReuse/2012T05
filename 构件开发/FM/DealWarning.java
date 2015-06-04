//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by MarK decompiler)
//

package warning;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DealWarning {
	//the route of output file
    private String outputPath;
    
    //last message
    private String preMsg;

    public DealWarning(String filepath) {
        this.outputPath = filepath;
        preMsg = "";
    }
    
    public void setOutoutPath(String path){
    	outputPath = path;
    }

    public void OutputInfo(String info) {
        try {
            if(this.outputPath == "") {
                long e = (new Date()).getTime();
                String bw = Long.toString(e);
                this.outputPath = "./error_" + bw + ".txt";
            }

            File e1 = new File(this.outputPath);
            if(!e1.exists()) {
                e1.createNewFile();
            }

            FileWriter fw = new FileWriter(e1.getAbsoluteFile(), true);
            BufferedWriter bw1 = new BufferedWriter(fw);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = dateFormat.format(new Date());
            Logger logger = Logger.getLogger(DealWarning.class);

            //if current msg is same as the last msg
            if(info.equals(preMsg)){
                //only output into log file
                logger.debug(date + "\t" + info + " [Hide]\n");
            }else{
                String write_result = date + "\t" + info + "\n";
                bw1.write(write_result);
                preMsg = info;
                logger.debug(date + "\t" + info + "\n");
                bw1.close();
            }
        } catch (Exception var8) {
            Logger logger = Logger.getLogger(DealWarning.class);
            logger.error("ERROR!!!");
            var8.printStackTrace();
        }

    }
}
