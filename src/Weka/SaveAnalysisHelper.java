package Weka;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SaveAnalysisHelper {
	private static String path = System.getProperty("user.dir") + File.separator + "WWA" + File.separator
			+ "savedAnalysis";
	private static String filename = "savedAnalysis.ana";
	public SaveAnalysisHelper() {
		// TODO Auto-generated constructor stub
	}

	public static HashMap<String,Integer> getMap() {
		HashMap<String, Integer> map = null;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			file = new File(path+filename);
			map = (HashMap<String, Integer>) new ObjectInputStream(new FileInputStream(file)).readObject();
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String, Integer>();
		}
		return map;

	}

	public static void setMap(HashMap map) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		try {

			file = new File(path+filename);
			new ObjectOutputStream(new FileOutputStream(file)).writeObject(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
