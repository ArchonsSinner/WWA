package JunitTests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Test;

import Weka.Cluster;
import Weka.WekaClusterer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaTest {
	static Cluster c=null;
	static Instance ins=null;
	static Instances data=null;

	@Test
	public void testClusterCreate(){
		for(int i=0;i<ins.numAttributes();i++){
			String valueC;
			String valueI;
			if(ins.attribute(i).isNumeric()){
				valueI=Double.toString(ins.value(i));
			}else{
				valueI=ins.stringValue(i);
			}
			valueC=c.getValues().get(ins.attribute(i).name());
			
			assertEquals("Attributwerte vergleichen",valueC,valueI);
		}
		
	}
	
	@BeforeClass
	public static void before(){
		
		try {
		String csvDat =TestRunner.pathToCsv;
		String arffDat ="kd.arff";
		CSVLoader loader = new CSVLoader();

		
			loader.setSource(new File(csvDat));
			data = loader.getDataSet();		
			

		    // save ARFF
		    BufferedWriter writer = new BufferedWriter(new FileWriter(arffDat));
		    writer.write(data.toString());
		    writer.flush();
		    writer.close(); 
		
			
//			ArffSaver saver = new ArffSaver();
//			saver.setInstances(data);
//			saver.setFile(new File(arffDat));
//			saver.writeBatch();
			DataSource source;
			
				source = new DataSource(arffDat);
			

			data = source.getDataSet();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ins=data.instance(3);
		
		
		try {
			Method meth=WekaClusterer.class.getDeclaredMethod("createCluster", Instance.class);
			meth.setAccessible(true);
			c=(Cluster)meth.invoke(c, data.instance(3));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testClustering(){
		try {
			WekaClusterer.setNumClusters(3);
			Cluster[]clusters = WekaClusterer.clustering(TestRunner.pathToCsv);
		 
		
		assertNotNull("Prüfen ob Cluster vorhanden sind",clusters);
		assertEquals("Clustersize überprüfen", 3,clusters.length);
		for(int i=0;i<clusters.length;i++){
			assertNotNull("PurchasesPerDay not Null"+i,clusters[i].getPurchasesPerDay());
			assertNotNull("PurchasesPerItemgroup not Null"+i,clusters[i].getPurchasesPerItemgroup());
			for(int j=0;j<7;j++)
				assertNotNull("PurchasesPerDay not Null Day"+j,clusters[i].getPurchasesPerDay()[j]);
			for(Integer j: clusters[i].getPurchasesPerItemgroup().values())
				assertNotNull("Inhalt von PurchasesPerItemgroup not Null"+i,j);
		}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
