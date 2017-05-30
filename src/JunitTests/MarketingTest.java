package JunitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.*;

import org.junit.Assert;
import org.junit.Test;

import Weka.MarketingHelper;

public class MarketingTest {
	private MarketingHelper helper;

	@Test
	public void test() throws IOException, ClassNotFoundException {
		if(!prepareTest())
			fail("Not yet implemented");
		
		getSetTest();
		addValueTest();
	}
	
	private boolean prepareTest(){
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		 helper = new MarketingHelper(request, response);
		
		 if(helper != null)
			 return true;
		 else
			 return false;
	}
	
	private void getSetTest() throws IOException, ClassNotFoundException{
		File path = new File(System.getProperty("user.dir") + File.separator + "WWA" + File.separator + "Marketing");
		File setFile = new File(System.getProperty("user.dir") + File.separator + "WWA" + File.separator + "Marketing" + File.separator + "marketingSetFile");
		Set<String> testSet = new HashSet<String>();
		
		if(path.exists()){
			if(setFile.exists()){
				Assert.assertNotEquals(testSet, helper.getSet());
			}
			else{
				Assert.assertEquals(testSet, helper.getSet());
			}
		}
		else{
			Assert.assertEquals(testSet, helper.getSet());
		}
	}
	
	private void addValueTest() throws IOException, ClassNotFoundException{
		String value = Double.toString(Math.random());
		Set<String> testSet;
		
		Assert.assertTrue(helper.addValue(value));
		testSet = helper.getSet();
		
		Assert.assertTrue(testSet.contains(value));
	}
	
	private void deleteValueTest(String value) throws FileNotFoundException, IOException, ClassNotFoundException{
		Set<String> testSet;
		
		Assert.assertTrue(helper.deleteValue(value));
		testSet = helper.getSet();
		
		Assert.assertTrue(!testSet.contains(value));
	}
}
