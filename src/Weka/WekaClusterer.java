package Weka;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaClusterer {

	private static int ClusterCount = 1;
	private static SimpleKMeans model = new SimpleKMeans();

	public static Cluster[] clustering(String path1) throws Exception {

		try{
		Cluster[] clusters = new Cluster[ClusterCount];
		String path = path1;
		String csvDat = path;// + "kd.csv"; Wurde rausgenommen, damit der Pfad + Dateinamen direkt übergeben werden kann. Weil ansonsten würde er nur den Pfad nehmen und die Datei müsste im Forfeld richtig beannt sein.
		String arffDat = path + "kd.arff";
		CSVLoader loader = new CSVLoader();

		
			loader.setSource(new File(csvDat));
			Instances data = loader.getDataSet();		
			

		    // save ARFF
		    BufferedWriter writer = new BufferedWriter(new FileWriter(arffDat));
		    writer.write(data.toString());
		    writer.flush();
		    writer.close(); 
		
			
//			ArffSaver saver = new ArffSaver();
//			saver.setInstances(data);
//			saver.setFile(new File(arffDat));
//			saver.writeBatch();
			DataSource source = new DataSource(arffDat);

			data = source.getDataSet();
			new File(arffDat).delete();
			model.setNumClusters(ClusterCount);
			model.buildClusterer(data);

			// System.out.println(model);

			Instances centroids = model.getClusterCentroids();
			for (int i = 0; i < centroids.numInstances(); i++) {
				Instance ins = centroids.instance(i);
				clusters[i] = WekaClusterer.createCluster(ins);
				clusters[i].setSize(model.getClusterSizes()[i]);

				// System.out.println("Instance " + i +" "+ret[i].size);

			}
			String[] weekdays = { "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag" };
			for (int i = 0; i < data.numInstances(); i++) {
				Instance ins = data.instance(i);
				int clusterNR = model.clusterInstance(ins);
				for (int iAtt = 0; iAtt < data.numAttributes(); iAtt++) {
					Attribute att = ins.attribute(iAtt);

					if (att.name().equals("Einkaufstag")) {
						for (int j = 0; j < 7; j++) {
							if (ins.stringValue(att).equals(weekdays[j])) {
								clusters[clusterNR].getPurchasesPerDay()[j]++;
							}
						}
					}
					if (att.isNumeric()) {
						if (!att.name().equals("Einkaufssumme")) {
							if (ins.value(att) > 0) {
								HashMap<String, Integer> map = clusters[clusterNR].getPurchasesPerItemgroup();
								if (map.containsKey(att.name())) {
									map.put(att.name(), map.get(att.name()).intValue() + 1);
								} else {

									clusters[clusterNR].getPurchasesPerItemgroup().put(att.name(), 1);
								}
							}
						}
					}
				}
			}

		
		Cluster.resetCount();
		return clusters;
		}catch(Exception exc){
			throw exc;
		}
	}

	private static Cluster createCluster(Instance ins) {
		Cluster c = new Cluster();
		for (int j = 0; j < ins.numAttributes(); j++) {
			String key = ins.attribute(j).name();
			String value = "";
			if (!ins.attribute(j).isNumeric())
				value = ins.stringValue(j);
			else
				if (Double.toString(ins.value(j)).length()>4)
					value = Double.toString(ins.value(j)).substring(0, 3);
				else
					value = Double.toString(ins.value(j));
			c.getValues().put(key, value);

		}

		// System.out.println(c.toString());
		return c;
	}

	public static void setNumClusters(int n) {
		ClusterCount = n;
	}
}