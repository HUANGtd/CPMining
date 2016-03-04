package basic;

import java.util.ArrayList;

public class PackSet {
	private ArrayList<String> packset = null;
	private String strName = null;
	
	// package items with the similar names
	public PackSet(String name) {
		this.packset = new ArrayList<String>();
		this.strName = name;
	}
	
	/****************** util *****************/
	public void addItem(String s) {
		this.packset.add(s);
	}
	
	public ArrayList<String> getPackSet() {
		return this.packset;
	}
	
	public String getName() {
		return this.strName;
	}
	
	/****************** output *****************/
	public void printPackSet(int psssCount, int pssCount, int psCount) {
		System.out.print("               -" + "#" + psssCount + "." + pssCount + "." + psCount + " " + this.strName + "<I>\n");// 10 spaces
		int count = 0;
		for(String s : this.packset) {
			count++;
			System.out.print("                    -" + "#" + psssCount + "." + pssCount + "." + psCount + "." + count + " " + s + "\n");// 15 spaces
		}
	}
}
