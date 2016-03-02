package preprocess;

import java.util.ArrayList;

public class ItemSet {
	private String strName = null;
	private ArrayList<String> listPack = null;
	
	public ItemSet() {
		this.listPack = new ArrayList<String>();
	}
	
	public ItemSet(ArrayList<String> list) {
		this.listPack = list;
	}
	
	public ItemSet(String name) {
		setName(name);
		this.listPack = new ArrayList<String>();
	}
	
	public void setName(String name) {
		this.strName = name;
	}
	
	public void addItem(String s) {
		this.listPack.add(s);
	}
	
	public ArrayList<String> getPack() {
		return this.listPack;
	}
}
