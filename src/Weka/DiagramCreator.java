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

	private static String path = "C:/Users/lenna/Desktop/charts/";
	private static int width=600;
	private static int height=500;
	private static int widthBig=800;
	private static int heightBig=600;

	public static String createClusterSizeBarChart(Cluster[] clusters) {

		DefaultCategoryDataset myDataset = new DefaultCategoryDataset();
		double d;
		System.out.println("size=" + clusters.length);
		for (int i = 0; i < clusters.length; i++) {
			System.out.println("i= "+i);
			d = clusters[i].getSize();
			myDataset.addValue(d, "1", Integer.toString(i + 1));
		}
		JFreeChart myBarChart = ChartFactory.createBarChart("Clustergröße", "Cluster Nr", "Anz Pers", myDataset,PlotOrientation.VERTICAL,false,true,false);
		CategoryPlot plot=(CategoryPlot)myBarChart.getPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		
		long timestamp = System.currentTimeMillis();
		int tryCount = 0;
		String filename = null;
		while (tryCount < 3 && filename == null) {
			try {
				filename = saveChartToJPG(myBarChart, path + "ChartClusterSize" + timestamp,widthBig, heightBig);
				
				
				
			} catch (IOException e) {
				timestamp++;
				tryCount++;
				if (tryCount>3)
					System.out.println("Error: could not save Chart");
			}
		}
		return filename;
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
		JFreeChart myBarChart1 = ChartFactory.createBarChart("Anteil der Käufe der Warengruppen\nCluster " + cluster.getIndex()+" ("+cluster.getSize()+")",
				"Warengruppe", "Anteil Käufe in %", myDataset,PlotOrientation.VERTICAL,false,true,false);
		
		CategoryPlot plot=(CategoryPlot)myBarChart1.getPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        plot.getDomainAxis().setMaximumCategoryLabelLines(3);
		
		long timestamp = System.currentTimeMillis();
		int tryCount = 0;
		String filename = null;
		while (tryCount < 3 && filename == null) {
			try {
				filename = saveChartToJPG(myBarChart1, path + "ChartItemBuyRelation" + timestamp + "cl"+cluster.getIndex(), width, height);
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

		JFreeChart myPieChart = ChartFactory.createPieChart("Einkäufe pro Wochentag\nCluster " + cluster.getIndex()+" ("+cluster.getSize()+")",
				myDataset);
		PiePlot plot=(PiePlot)myPieChart.getPlot();
		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
	            "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
	        plot.setLabelGenerator(gen);

	        
		long timestamp = System.currentTimeMillis();
		int tryCount = 0;
		String filename = null;
		while (tryCount < 3 && filename == null) {
			try {
				filename = saveChartToJPG(myPieChart, path + "ChartWeekdays" + timestamp + "cl"+cluster.getIndex(), width, height);
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
			fileList[2*(i+1)-1]=DiagramCreator.createItemCountPerCluster(clusters[i]);
			fileList[2*(i+1)]=DiagramCreator.createWeekdayPieChart(clusters[i]);
		}
		return fileList;
	}

}
