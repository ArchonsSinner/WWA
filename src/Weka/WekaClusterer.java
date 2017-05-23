package Weka;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaClusterer {

	private static int ClusterCount = 1;
	private static SimpleKMeans model = new SimpleKMeans();

	public static Cluster[] clustering(String path1) {

		Cluster[] clusters = new Cluster[ClusterCount];
		String path = path1;
		String csvDat = path + "kd.csv";
		String arffDat = path + "kd.arff";
		CSVLoader loader = new CSVLoader();

		try {
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

		} catch (IOException e) {
		} catch (Exception e) {
		}
		Cluster.resetCount();
		return clusters;
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
	
	

	public static void main(String[] args) {
		WekaClusterer.setNumClusters(5);
		DiagramCreator.masterCreate(WekaClusterer.clustering("C:/Users/lenna/Desktop/eclipse/"));
	}

}