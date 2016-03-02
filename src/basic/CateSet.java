package basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CateSet {
	private ArrayList<String> arrCateSet = null;
	private String strName = null;
	private String strKey = null;
			
	public CateSet(String key) {
		this.strKey = key;
		this.arrCateSet = new ArrayList<String>();
	}
	
	public CateSet(String key, String name) {
		this.strKey = key;
		this.strName = name;
		this.arrCateSet = new ArrayList<String>();
	}
	
	public void mergeCateSet(CateSet cs) {
		ArrayList<String> acs = cs.getCateSet();
		this.arrCateSet.addAll(acs);
		sortCateSet();
	}
	
	/****************** util *****************/
	public void addItem(String s) {
		this.arrCateSet.add(s);
	}
	
	public void sortCateSet() {
		Collections.sort(this.arrCateSet, new StringSortByName());
	}
	
	public void setName(String newname) {
		this.strName = newname;
	}
	
	public String getName() {
		return this.strName;
	}
	
	public String getKey() {
		return this.strKey;
	}
	
	public ArrayList<String> getCateSet() {
		return this.arrCateSet;
	}
	
	/****************** output *****************/
	public void printCateSet() {
		int itemCount = 0;
		System.out.print(this.strName + "]\n");
		System.out.print("     +-------------\n"); // 5 spaces
		for(String s : this.arrCateSet) {
			itemCount++;
			System.out.print("     | #" + itemCount + ". " + s + "\n");
		}
		System.out.print("     +-------------\n");
	}
}

class StringSortByName implements Comparator {
	 public int compare(Object o1, Object o2) {
		 String c1= (String) o1;
		 String c2 = (String) o2;
		 return c1.compareTo(c2);
	 }
}