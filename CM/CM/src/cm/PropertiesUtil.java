package cm;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class PropertiesUtil {
    //�����ļ���·��
    private String configPath="config.properties";
    
    //�����ļ�����
    private Properties props=null;
    
    //Ĭ�Ϲ��캯����Ĭ���ǴӸ���Ŀ��srcĿ¼�¶�ȡconfig.properties
    public PropertiesUtil() throws IOException{
        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
        props = new Properties();
        props.load(in);
        //�ر���Դ
        in.close();
    }
    
    //����keyֵ��ȡ���õ�ֵ����ѯ�ӿڣ�
    public String readValue(String key) throws IOException {
        return  props.getProperty(key);
    }
    
    //��ȡproperties��ȫ����Ϣ
    public Map<String,String> readAllProperties() throws FileNotFoundException,IOException  {
        //�������еļ�ֵ
        Map<String,String> map=new HashMap<String,String>();
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String Property = props.getProperty(key);
            map.put(key, Property);
        }
        return map;
    }

    // ����ĳ��key��ֵ,���������ļ���
    public void setValue(String key,String value) throws IOException {
        Properties prop = new Properties();
        InputStream fis = new FileInputStream(this.configPath);
        // ���������ж�ȡ�����б�������Ԫ�ضԣ�
        prop.load(fis);
        // ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�
        // ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����
        OutputStream fos = new FileOutputStream(this.configPath);
        prop.setProperty(key, value);
        // ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��
        // ���� Properties ���е������б�������Ԫ�ضԣ�д�������
        prop.store(fos,"last update");
        //�ر��ļ�
        fis.close();
        fos.close();
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
    	PropertiesUtil p;
        String qString;
        boolean jBool = true;
        try {
            p = new PropertiesUtil();
            System.out.println(p.readAllProperties());
            while (jBool) {
				System.out.println("Please input the property you want to query: (if you would like to end, please input \"end\")");
				qString = sc.next();
				if (qString.equals("end"))
					jBool = false;
				else 
					System.out.println(p.readValue(qString));	
			}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}