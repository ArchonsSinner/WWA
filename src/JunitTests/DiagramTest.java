package JunitTests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.junit.BeforeClass;
import org.junit.Test;

import Weka.Cluster;
import Weka.DiagramCreator;
import Weka.WekaClusterer;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

public class DiagramTest {

	static Cluster[] clusters = null;
	static Instances data = null;

	@Test
	public void testDiagramCreate() {
		String[] files = DiagramCreator.masterCreate(clusters);
		for (int i = 0; i< files.length;i++){
			assertNotNull("Pfad ist gesetzt Nr"+i,files[i]);
			File file = new File(files[i]);
			assertTrue("Datei " + files[i] + "existiert",file.exists());
		}
		File file = new File(DiagramCreator.getPath());
		File[] dateien=file.listFiles();
		for(int i=0;i<dateien.length;i++){
			assertTrue("Datei nicht älter als 1 Tag", dateien[i].lastModified()+ (24*3600*1000)>System.currentTimeMillis());
		}
	}

	@BeforeClass
	public static void before() {

		try {
			String csvDat = TestRunner.pathToCsv;
			String arffDat = "kd.arff";
			CSVLoader loader = new CSVLoader();

			loader.setSource(new File(csvDat));
			data = loader.getDataSet();

			// save ARFF
			BufferedWriter writer = new BufferedWriter(new FileWriter(arffDat));
			writer.write(data.toString());
			writer.flush();
			writer.close();

			DataSource source;
			source = new DataSource(arffDat);

			data = source.getDataSet();
			WekaClusterer.setNumClusters(3);
			clusters = WekaClusterer.clustering(TestRunner.pathToCsv);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
