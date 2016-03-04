package basic;

import java.util.ArrayList;
import java.util.HashMap;

public class PackSSSet {
	private HashMap<String, PackSSet> mapKey2PackSSet = null;
	private ArrayList<String> arrPackSSetKeys = null;
	private String strName = null;
	
	// package items that often occur together in each day
	public PackSSSet(String name) {
		this.strName = name;
		this.arrPackSSetKeys = new ArrayList<String>();
		this.mapKey2PackSSet = new HashMap<String, PackSSet>();
	}
	
	/****************** util *****************/
	public void addSSet(PackSSet pss) {
		this.arrPackSSetKeys.add(pss.getName());
		this.mapKey2PackSSet.put(pss.getName(), pss);
	}
	
	public String getName() {
		return this.strName;
	}
	
	public void setName(String s) {
		this.strName = s;
	}
	
	/****************** output *****************/
	public void printPackSSSet(int psssCount) {
		System.out.print(" " + this.strName + "<III>\n");
		int pssCount = 0;
		for(String key : this.mapKey2PackSSet.keySet()) {
			pssCount++;
			this.mapKey2PackSSet.get(key).printMapKey2PackSet(psssCount, pssCount);
		}
//		System.out.print("\n");
	}
}
