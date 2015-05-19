import cm.PropertiesUtil;
import com.team8.PerformanceManagement.PM;
import src.com.team8.License.License;
import warning.DealWarning;

import java.io.IOException;

/**
 * Created by mark on 5/19/15.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        //JSM activemq server address
        final String activemqURL = "tcp://localhost:61616";
        //the queue Name: get request from client
        final String RequestQueueName = "RequestQueue";
        //CM.properties route
        final String CMPath = "src/file/project.properties";
        //license count
        final int licenseCount = 4;
        //create CM
        PropertiesUtil propertiesUtil = new PropertiesUtil(CMPath);
        //get FM_msg route
        final String FMPath = propertiesUtil.readValue("FM");
        //get PM_msg route
        final String PMPath = propertiesUtil.readValue("PM");

        //create PM
        PM.setPathName(PMPath);
        //create FM
        DealWarning dealWarning = new DealWarning(FMPath);
        //create license
        License serverLicense = new License(licenseCount);

        //create customer receive msg from client
        MyCustomer serverGetRequestCustomer = new MyCustomer(activemqURL, RequestQueueName);
        serverGetRequestCustomer.conStart();

        //have available license to create
        while (true) {
            String requestMsg = serverGetRequestCustomer.getMsg(); //receive request message from client
            //PM receive msg
            PM.sendPMMessage("ReceiveMessage", 1);
            if (serverLicense.inLicense()) {
                //get license success
                //add success FM message
                dealWarning.OutputInfo("Provide Service");
                //add PM provider service msg
                PM.sendPMMessage("ProvideService", 1);
                //create thread of search model
//                Myserver server = new Myserver(requestMsg);
//                Thread t = new Thread(server);
//                t.start();
            } else {
                //get license failed
                //add failed FM message
                dealWarning.OutputInfo("Reject Service");
                //add PM reject service msg
                PM.sendPMMessage("RejectService", 1);
                //add PM return message msg
                PM.sendPMMessage("ReturnMessage", 1);
            }

        }
    }
}
