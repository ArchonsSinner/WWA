package Weka;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Cluster {

	private static int count=0;
	private int index;
	private int size;
	private HashMap<String,String> values; 
	private int[] purchasesPerDay;
	private HashMap<String,Integer> purchasesPerItemgroup;	
	
	public Cluster() {
		this.values = new HashMap<String, String>();
		this.purchasesPerDay= new int[7];//Index 0 ist Montag
		this.purchasesPerItemgroup = new HashMap<String,Integer>();
		this.index=++count;
	}
	
	public int getIndex() {
		return index;
	}

	public String toString(){
		String ret="";
		Set<String>keys=this.values.keySet();
		for(String s:keys)
			ret+=s+ " :  "+this.values.get(s)+" ; ";
		
		return ret;
	}


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}


	public Map<String, String> getValues() {
		return values;
	}


	public void setValues(HashMap<String, String> values) {
		this.values = values;
	}


	public int[] getPurchasesPerDay() {
		return purchasesPerDay;
	}


	public void setPurchasesPerDay(int[] purchasesPerDay) {
		this.purchasesPerDay = purchasesPerDay;
	}


	public HashMap<String, Integer> getPurchasesPerItemgroup() {
		return purchasesPerItemgroup;
	}


	public void setPurchasesPerItemgroup(HashMap<String, Integer> purchasesPerItemgroup) {
		this.purchasesPerItemgroup = purchasesPerItemgroup;
	}
}