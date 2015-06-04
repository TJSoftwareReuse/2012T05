package cm;
import java.io.File;
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
    //配置文件的路径
    private String configPath=null;
    
    //配置文件对象
    private Properties props=null;
    
    //由用户指定配置文件路径的构造函数
    public PropertiesUtil(String pathString) throws IOException{
    	configPath = pathString;
    	File f = new File(pathString);
    	InputStream in = new FileInputStream(f);
		props = new Properties();
		props.load(in);
		in.close();
	}
    
    //根据key值读取配置的值（查询接口）
    public String readValue(String key) throws IOException {
    	File f = new File(configPath);
    	InputStream in = new FileInputStream(f);
    	props = new Properties();
		props.load(in);
    	in.close();
        return  props.getProperty(key);
    }
    
    //读取properties的全部信息
    public Map<String,String> readAllProperties() throws FileNotFoundException,IOException  {
        //保存所有的键值
        Map<String,String> map=new HashMap<String,String>();
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String Property = props.getProperty(key);
            map.put(key, Property);
        }
        return map;
    }

    // 设置某个key的值,并保存至文件。
    public void setValue(String key,String value) throws IOException {
        Properties prop = new Properties();
        InputStream fis = new FileInputStream(this.configPath);
        // 从输入流中读取属性列表（键和元素对）
        prop.load(fis);
        // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
        // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream fos = new FileOutputStream(this.configPath);
        prop.setProperty(key, value);
        // 以适合使用 load 方法加载到 Properties 表中的格式，
        // 将此 Properties 表中的属性列表（键和元素对）写入输出流
        prop.store(fos,"last update");
        //关闭文件
        fis.close();
        fos.close();
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
    	PropertiesUtil p;
        String qString, pathString;
        boolean jBool = true;
        try {
        	System.out.println("Please specify the path for config:");
        	pathString = sc.next();
            p = new PropertiesUtil(pathString);
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