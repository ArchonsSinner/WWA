package Weka;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

public class DiagramCreator {
	// Pfad f�r die charts muss noch angepasst werden
	//
	private static String path = System.getProperty("user.dir") + File.separator + "WWA" + File.separator + "data" + File.separator + "charts";
	private static int width = 600;
	private static int height = 500;
	private static int widthBig = 800;
	private static int heightBig = 600;
	
	public static String getPath(){
		return path;
	}


	public static String createClusterSizeBarChart(Cluster[] clusters) {
		try{
		DefaultCategoryDataset myDataset = new DefaultCategoryDataset();
		double d;
		for (int i = 0; i < clusters.length; i++) {
			d = clusters[i].getSize();
			myDataset.addValue(d, "1", Integer.toString(i + 1));
		}
		JFreeChart myBarChart = ChartFactory.createBarChart("Clustergr��e", "Cluster Nr", "Anz Pers", myDataset,
				PlotOrientation.VERTICAL, false, true, false);
		CategoryPlot plot = (CategoryPlot) myBarChart.getPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

		long timestamp = System.currentTimeMillis();
		int tryCount = 0;
		String filename = null;
		while (tryCount < 3 && filename == null) {
			try {
				filename = saveChartToJPG(myBarChart, path + File.separator + "ChartClusterSize" + timestamp, widthBig,
						heightBig);
			} catch (IOException e) {
				timestamp++;
				tryCount++;
				if (tryCount > 3)
					System.out.println("Error: could not save Chart");
			}
		}
		return filename;
		}catch(Exception e){
			
		}
		return null;
		
	}

	public static String createItemCountPerCluster(Cluster cluster) {
		try{

		DefaultCategoryDataset myDataset = new DefaultCategoryDataset();

		double d;
		for (String s : cluster.getPurchasesPerItemgroup().keySet()) {

			d = cluster.getPurchasesPerItemgroup().get(s);
			d /= cluster.getSize();
			d = d * 100;
			int x = (int) d;
			myDataset.addValue(x, "1", s);
		}
		JFreeChart myBarChart1 = ChartFactory.createBarChart(
				"Anteil der K�ufe der Warengruppen\nCluster " + cluster.getIndex() + " (" + cluster.getSize() + ")",
				"Warengruppe", "Anteil K�ufe in %", myDataset, PlotOrientation.VERTICAL, false, true, false);

		CategoryPlot plot = (CategoryPlot) myBarChart1.getPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		plot.getDomainAxis().setMaximumCategoryLabelLines(3);

		long timestamp = System.currentTimeMillis();
		int tryCount = 0;
		String filename = null;
		while (tryCount < 3 && filename == null) {
			try {
				filename = saveChartToJPG(myBarChart1,
						path + File.separator + "ChartItemBuyRelation" + timestamp + "cl" + cluster.getIndex(), width,
						height);
			} catch (IOException e) {
				timestamp++;
				tryCount++;
				if (tryCount > 3)
					System.out.println("Error: could not save Chart");
			}
		}
		return filename;
		}catch(Exception e){
			
		}
		return null;
	}

	public static String createWeekdayPieChart(Cluster cluster) {
		try{

		DefaultPieDataset myDataset = new DefaultPieDataset();
		double d;
		String[] weekdays = { "Montag", "Dienstag", "Mitwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag" };
		for (int i = 0; i < weekdays.length; i++) {
			d = cluster.getPurchasesPerDay()[i];
			myDataset.setValue(weekdays[i], d);
		}

		JFreeChart myPieChart = ChartFactory.createPieChart(
				"Eink�ufe pro Wochentag\nCluster " + cluster.getIndex() + " (" + cluster.getSize() + ")", myDataset);
		PiePlot plot = (PiePlot) myPieChart.getPlot();
		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0}: {1} ({2})", new DecimalFormat("0"),
				new DecimalFormat("0%"));
		plot.setLabelGenerator(gen);

		long timestamp = System.currentTimeMillis();
		int tryCount = 0;
		String filename = null;
		while (tryCount < 3 && filename == null) {
			try {
				filename = saveChartToJPG(myPieChart,
						path + File.separator + "ChartWeekdays" + timestamp + "cl" + cluster.getIndex(), width, height);
			} catch (IOException e) {
				timestamp++;
				tryCount++;
				if (tryCount > 3)
					System.out.println("Error: could not save Chart");
			}
		}
		return filename;
		}catch(Exception e){
			
		}
		return null;
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
		// Pfad �berpr�fen und ggfs erstellen
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();

		// Alte Charts l�schen
		File[] fileArray = file.listFiles();
		for (int i = 1; i < fileArray.length; i++) {
			if (fileArray[i].lastModified() < System.currentTimeMillis() - (24*3600*1000))
				try{
					fileArray[i].delete();
				}catch(Exception exc){}
		}

		int chartCount = (clusters.length * 2) + 1;
		String[] fileList = new String[chartCount];
		fileList[0] = DiagramCreator.createClusterSizeBarChart(clusters);
		for (int i = 0; i < clusters.length; i++) {
			fileList[2 * (i + 1) - 1] = DiagramCreator.createItemCountPerCluster(clusters[i]);
			fileList[2 * (i + 1)] = DiagramCreator.createWeekdayPieChart(clusters[i]);
		}
		return fileList;
	}

}
