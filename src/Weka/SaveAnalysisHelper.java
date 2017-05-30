package Weka;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class SaveAnalysisHelper {
	private static String path = System.getProperty("user.dir") + File.separator + "WWA" + File.separator
			+ "savedAnalysis";
	private static String filename = File.separator + "savedAnalysis.ana";
	private static LinkedList<String> filenameOrder;

	public SaveAnalysisHelper() {
		// TODO Auto-generated constructor stub
	}

	public static HashMap<String, Integer> getMap() {
		HashMap<String, Integer> map = null;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			file = new File(path + filename);
			//System.out.println(path+filename);
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			map = (HashMap<String, Integer>) ois.readObject();
			ois.close();
			Set<String> keys=map.keySet();
			File datei;
			for(String s : keys){
				datei = new File(s);
				if(!datei.exists())
					map.remove(s);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String, Integer>();
		}
		return map;

	}

	private static LinkedList<String> getOrderList(){
		LinkedList<String> ll;
		try {
			File file = new File(path + filename);
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			ois.readObject();
			ll = (LinkedList<String>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			ll = new LinkedList<String>();
		}
		
		return ll;
	}
	public static void addToMap(String key, int n) {
		HashMap<String, Integer> map = getMap();	// map laden
		filenameOrder = getOrderList();
		if (!filenameOrder.contains(key)) {			// Wenn key/Dateiname noch nicht vorhanden
			filenameOrder.add(key);					// Neuen key zu reihenfolge hinzufügen
			if (map.size() >= 5)					// Wenn zu viele Einträge
				map.remove(filenameOrder.removeFirst());// den als erstes hinzugefügten Wert aus beiden listen löschen
			map.put(key, n);						// Neuen Eintrag in HashMap
			try {
				File file = new File(path + filename);// File zum  speichern erstellen
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				oos.writeObject(map);	//Map schreiben
				oos.writeObject(filenameOrder);//Liste(Reihenfolge) schreiben
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void clearList(){
		try {
			File file = new File(path + filename);// File zum  speichern erstellen
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(new HashMap<String,Integer>());	//Map schreiben
			oos.writeObject(new LinkedList<String>());//Liste(Reihenfolge) schreiben
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	


}
