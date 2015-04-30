package warning;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;

import org.junit.Test;

public class DealWarningTest {

	@Test
	public void testDealWarning() {
		DealWarning dealwarning=new DealWarning();
		dealwarning.OutputInfo();
	}

	@Test
	public void testDealWarningString() {
		DealWarning dealwarning=new DealWarning("Wrong!");
		dealwarning.OutputInfo();
	}

	@Test
	public void testDealWarningStringString() {
		DealWarning dealwarning=new DealWarning("Wrong!","warning");
		dealwarning.OutputInfo();
		File file=new File("warning");
		assertTrue ( file.exists() );
	}

}
