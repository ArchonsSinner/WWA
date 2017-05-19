import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class DiagramCreator {

	private static String path = "data/wwa/charts/";

	public static String createClusterSizeBarChart(Cluster[] clusters) {

		DefaultCategoryDataset myDataset = new DefaultCategoryDataset();
		double d;
		for (int i = 0; i < clusters.length; i++) {
			d = clusters[i].getSize();
			myDataset.addValue(d, "1", Integer.toString(i + 1));
		}
		JFreeChart myBarChart = ChartFactory.createBarChart("meinTitel", "Cluster Nr", "Anz Pers", myDataset);
		long timestamp = System.currentTimeMillis();
		int tryCount = 0;
		String filename = null;
		while (tryCount < 3 && filename == null) {
			try {
				filename = saveChartToJPG(myBarChart, path + "ChartClusterSize" + timestamp, 800, 600);
			} catch (IOException e) {
				timestamp++;
				tryCount++;
				if (tryCount>3)
					System.out.println("Error: could not save Chart");
			}
		}
		return "";
	}

	public static String createItemCountPerCluster(Cluster cluster) {

		DefaultCategoryDataset myDataset = new DefaultCategoryDataset();

		double d;
		for (String s : cluster.getPurchasesPerItemgroup().keySet()) {

			d = cluster.getPurchasesPerItemgroup().get(s);
			d /= cluster.getSize();
			d = d * 100;
			int x = (int) d;
			myDataset.addValue(x, "1", s);
		}
		JFreeChart myBarChart1 = ChartFactory.createBarChart("Anteil der Käufe der Warengruppen\n" + cluster.getIndex(),
				"Cluster Nr", "Anz Pers", myDataset);
		long timestamp = System.currentTimeMillis();
		int tryCount = 0;
		String filename = null;
		while (tryCount < 3 && filename == null) {
			try {
				filename = saveChartToJPG(myBarChart1, path + "ChartItemBuyRelation" + timestamp + "cl"+cluster.getIndex(), 800, 600);
			} catch (IOException e) {
				timestamp++;
				tryCount++;
				if (tryCount>3)
					System.out.println("Error: could not save Chart");
			}
		}
		return filename;
	}

	public static String createWeekdayPieChart(Cluster cluster) {

		DefaultPieDataset myDataset = new DefaultPieDataset();
		double d;
		String[] weekdays = { "Montag", "Dienstag", "Mitwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag" };
		for (int i = 0; i < weekdays.length; i++) {
			d = cluster.getPurchasesPerDay()[i];
			myDataset.setValue(weekdays[i], d);
		}

		JFreeChart myPieChart = ChartFactory.createPieChart("Einkäufe pro Wochentag\n Cluster " + cluster.getIndex(),
				myDataset);

		long timestamp = System.currentTimeMillis();
		int tryCount = 0;
		String filename = null;
		while (tryCount < 3 && filename == null) {
			try {
				filename = saveChartToJPG(myPieChart, path + "ChartWeekdays" + timestamp + "cl"+cluster.getIndex(), 800, 600);
			} catch (IOException e) {
				timestamp++;
				tryCount++;
				if (tryCount>3)
					System.out.println("Error: could not save Chart");
			}
		}
		return filename;
	}

	static public final String saveChartToJPG(final JFreeChart chart, String fileName, final int width,
			final int height) throws IOException {

		String result = null;
		if (chart != null) {
			result = fileName + ".jpg";
			ChartUtilities.saveChartAsJPEG(new File(result), chart, width, height);
		}
		return result;
	}

	public static String[] masterCreate(Cluster[] clusters) {
		int chartCount=(clusters.length*2)+1;
		String[] fileList = new String[chartCount];
		fileList[0]=DiagramCreator.createClusterSizeBarChart(clusters);
		for (int i = 0; i < clusters.length; i++) {
			fileList[i]=DiagramCreator.createItemCountPerCluster(clusters[i]);
			fileList[i]=DiagramCreator.createWeekdayPieChart(clusters[i]);
		}
		return fileList;
	}

}
