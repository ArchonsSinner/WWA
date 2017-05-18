import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class DiagramCreator {

	
	public static void createClusterSizeBarChart(Cluster[] clusters){
		
		DefaultCategoryDataset myDataset = new DefaultCategoryDataset();
		double d;
		for(int i =0;i<clusters.length;i++){
			d=clusters[i].getSize();
			myDataset.addValue(d, "1" ,Integer.toString(i+1));
		}
		JFreeChart myBarChart = ChartFactory.createBarChart("meinTitel", "Cluster Nr", "Anz Pers", myDataset);
		
		try {
			saveChartToJPG(myBarChart, "C:/Users/lasse/workspace/SPM/data/myBarChart", 800, 600);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public static void createItemCountPerCluster(Cluster cluster){
		
		DefaultCategoryDataset myDataset = new DefaultCategoryDataset();
		
		double d;
		for(String s:cluster.getPurchasesPerItemgroup().keySet()){
			
			d=cluster.getPurchasesPerItemgroup().get(s);
			d/=cluster.getSize();
			d=d*100;
			int x = (int)d;
			myDataset.addValue(x, "1" ,s);
		}
		JFreeChart myBarChart1 = ChartFactory.createBarChart("Anteil der Käufe der Warengruppen\n"+cluster.getIndex(), "Cluster Nr", "Anz Pers", myDataset);
		
		try {
			saveChartToJPG(myBarChart1, "C:/Users/lasse/workspace/SPM/data/myBarChart"+cluster.getIndex(), 800, 600);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createWeekdayPieChart(Cluster cluster){
		
		DefaultPieDataset myDataset = new DefaultPieDataset();
		double d;
		String[]weekdays={"Montag","Dienstag","Mitwoch","Donnerstag","Freitag","Samstag","Sonntag"};
		for(int i =0;i<weekdays.length;i++){
			d=cluster.getPurchasesPerDay()[i];
			myDataset.setValue( weekdays[i],d);
		}
		
		JFreeChart myPieChart = ChartFactory.createPieChart("Einkäufe pro Wochentag\n Cluster "+cluster.getIndex(), myDataset);
		
		try {
			saveChartToJPG(myPieChart, "C:/Users/lasse/workspace/SPM/data/myPieChart"+cluster.getIndex(), 800, 600);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
		
    static public final String saveChartToJPG(final JFreeChart chart, String fileName, 
    		final int width, final int height) throws IOException {
    	
        String result = null;
        if (chart != null) {
            if (fileName == null) {
                final String chartTitle = chart.getTitle().getText();
                if (chartTitle != null) {
                    fileName = chartTitle;
                } else {
                    fileName = "chart";
                }
            }
            result = fileName+".jpg";
            ChartUtilities.saveChartAsJPEG(new File(result), chart, width, height);
        }//else: input unavailable
        return result;
    }

    public static void masterCreate(Cluster[] clusters){
    	DiagramCreator.createClusterSizeBarChart(clusters);
    	for(int i=0;i<clusters.length;i++){
    		DiagramCreator.createItemCountPerCluster(clusters[i]);
    		DiagramCreator.createWeekdayPieChart(clusters[i]);
    	}
    }
    
}
