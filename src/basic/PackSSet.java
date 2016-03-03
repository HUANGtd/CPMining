package basic;

import java.util.ArrayList;
import java.util.HashMap;

public class PackSSet {
	private HashMap<String, PackSet> mapKey2PackSet = null;
	private ArrayList<String> arrPackSetKeys = null;
	private String strName = null;
	private String strCategory = null;
	
	// package items that often occur together in each category
	public PackSSet(String name, String cate) {
		this.mapKey2PackSet = new HashMap<String, PackSet>();
		this.arrPackSetKeys = new ArrayList<String>();
		this.strName = name;
		this.strCategory = cate;
	}
	
	/****************** util *****************/
	public HashMap<String, PackSet> getKey2PackSetMap() {
		return this.mapKey2PackSet;
	}
	
	public ArrayList<String> getPackSetKeys() {
		return this.arrPackSetKeys;
	}
	
	public void addSet(PackSet ps) {
		this.arrPackSetKeys.add(ps.getName());
		this.mapKey2PackSet.put(ps.getName(), ps);
	}
	
	public String getName() {
		return this.strName;
	}
	
	public void setName(String s) {
		this.strName = s;
	}
	
	public String getCategory() {
		return this.strCategory;
	}
	
	/****************** output *****************/
	public void printMapKey2PackSet(int psssCount, int pssCount) {
		System.out.print("          - " + "#" + psssCount + "." + pssCount + " " + this.strName + "<II>\n");// 5 spaces
		int psCount = 0;
		for(String key : this.mapKey2PackSet.keySet()) {
			psCount++;
			this.mapKey2PackSet.get(key).printPackSet(psssCount, pssCount, psCount);
		}
//		System.out.print("\n");
	}
}
