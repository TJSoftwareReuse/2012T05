package cm;

import static org.junit.Assert.*;

import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class PropertiesUtilTest {

	@Test
	public void testReadValue() {
		try {
			PropertiesUtil ptest=new PropertiesUtil();
			assertTrue(ptest.readValue("username").equals("Bean"));
			assertTrue(ptest.readValue("hometown").equals("Hainan"));
			assertTrue(ptest.readValue("school").equals("Tongji"));
			assertTrue(ptest.readValue("age").equals("21"));
			assertTrue(ptest.readValue("major").equals("Software Engineering"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testReadAllProperties() {
		Map<String,String> map=new HashMap<String,String>();
		map.put("username", "Bean");
		map.put("hometown", "Hainan");
		map.put("school", "Tongji");
		map.put("age", "21");
		map.put("major", "Software Engineering");
		PropertiesUtil ptest = null;
		try {
			ptest=new PropertiesUtil();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			assertTrue(ptest.readAllProperties().equals(map));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testSetValue() {
		try {
			PropertiesUtil ptest=new PropertiesUtil();
			ptest.setValue("username", "Atlas");
			assertTrue(ptest.readValue("username").equals("Atlas"));
			ptest.setValue("username", "Bean");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
