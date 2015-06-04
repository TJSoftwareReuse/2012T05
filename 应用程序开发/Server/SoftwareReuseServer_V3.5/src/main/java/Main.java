import cm.PropertiesUtil;
import com.team8.PerformanceManagement.PM;
import org.apache.log4j.Logger;
import src.com.team8.License.License;
import warning.DealWarning;

import java.io.IOException;

/**
 * Created by mark on 5/19/15.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        //log4j Logger
        final Logger logger = Logger.getLogger(Main.class);
        //JSM activemq server address
        final String activemqURL = "tcp://localhost:61616";
        //the queue Name: get request from client
        final String RequestQueueName = "RequestQueue";
        //CM.properties route
        final String CMPath = "src/file/project.properties";
        //license count
        final int licenseCount = 4;
        //create CM
        final PropertiesUtil propertiesUtil = new PropertiesUtil(CMPath);
        //get FM_msg route
        final String FMPath = propertiesUtil.readValue("FM");
        //get PM_msg route
        final String PMPath = propertiesUtil.readValue("PM");

        //create PM
        PM.setPathName(PMPath);
        //set PM period
        PM.setPeriod(1);
        //create FM
        DealWarning dealWarning = new DealWarning(FMPath);
        //create license
        License serverLicense = new License(licenseCount);

        //create customer receive msg from client
        MyCustomer serverGetRequestCustomer = new MyCustomer(activemqURL, RequestQueueName);
        serverGetRequestCustomer.conStart();

        logger.debug("Server Start .....");
        //have available license to create
        while (true) {
            String requestMsg = serverGetRequestCustomer.getMsg(); //receive request message from client
            //PM receive msg
            PM.setPathName(propertiesUtil.readValue("PM"));
            PM.sendPMMessage("ReceiveMessage", 1);
            logger.debug(requestMsg + "/////////////");
            if (serverLicense.inLicense()) {
                logger.debug("Have license //////////");
                //get license success
                //add success FM message
                dealWarning.setOutoutPath(propertiesUtil.readValue("FM"));
                dealWarning.OutputInfo("Provide Service");
                //add PM provider service msg
                PM.setPathName(propertiesUtil.readValue("PM"));
                PM.sendPMMessage("ProvideService", 1);
                //create thread of search model

                //switch the request type
                if(propertiesUtil.readValue("RequestType").equals("name")){
                    //query by name
                    MyserverForName myserver = new MyserverForName(requestMsg);
                    Thread t = new Thread(myserver);
                    t.start();
                    logger.debug("MyserverForName create ///////");
                }else if(propertiesUtil.readValue("RequestType").equals("team")){
                    //query by teamNum
                    MyserverForTeam server = new MyserverForTeam(requestMsg);
                    Thread t = new Thread(server);
                    t.start();
                    logger.debug("MyserverForTeam create ////////");
                }else{
                    logger.error("Unknown request type");
                }
            } else {
                //get license failed
                String queueName = requestMsg + "S";
                MyProducer errorProducer = new MyProducer(activemqURL, queueName);
                errorProducer.connStart();
                errorProducer.sendMsg("Don't have license, cannot offer service anymore!!!!!");
                errorProducer.connClose();
                //add failed FM message
                logger.debug("Don't have license /////////");
                dealWarning.setOutoutPath(propertiesUtil.readValue("FM"));
                dealWarning.OutputInfo("Reject Service");
                //add PM reject service msg
                PM.setPathName(propertiesUtil.readValue("PM"));
                PM.sendPMMessage("RejectService", 1);
                //add PM return message msg
                PM.setPathName(propertiesUtil.readValue("PM"));
                PM.sendPMMessage("ReturnMessage", 1);
            }
        }
    }
}
