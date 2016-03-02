package preprocess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import basic.CItem;
import basic.FPSet;

public class PreProcess {
	private HashMap<String, ItemSet> arrSetList = null;
	private ArrayList<String> oriList = null;
	private HashMap<String, String> orinameMapNewname = null;
	
	public PreProcess(ArrayList<String> list) {
		this.oriList = list;
	}
	
	/****************** util *****************/
	// get map key list
	public void genSetMap() {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> newList = new ArrayList<String>();
		this.orinameMapNewname = new HashMap<String, String>();
		for(String os : this.oriList) {
			String temp = os;
			if(temp.contains("(")) {
				String s1[] = temp.split("\\(");
				temp = s1[0];
			}
			if(temp.contains("+")) {
				String s2[] = temp.split("\\+");
				temp = s2[0];
			}
			
			list.add(temp);
		}
		for(String s : list) {
			if(!newList.contains(s)) {
				newList.add(s);
			}
		}
		Collections.sort(newList, new SortByName());
		this.arrSetList = new HashMap<String, ItemSet>();
		for(String s : newList) {
			//System.out.println(s);
			this.arrSetList.put(s, new ItemSet(s));
		}
		for(String os : this.oriList) {
			for(String ns : newList) {
				if(os.indexOf(ns) == 0) {
					ItemSet set = this.arrSetList.get(ns);
					set.addItem(os);
					this.orinameMapNewname.put(os, ns);
					break;
				}
			}
		}
	}
	
	public HashMap<String, String> getNameMap() {
		return this.orinameMapNewname;
	}
	
	public HashMap<String, ItemSet> getSetList() {
		return this.arrSetList;
	}
	
	/****************** output *****************/
	public void printSetList() {
		for(String key : this.arrSetList.keySet()) {
			if(this.arrSetList.get(key).getPack().size() != 0) {
				System.out.print(key + ": \n");
				for(String s : this.arrSetList.get(key).getPack()) {
					System.out.print("          - " + s + "\n");
				}
				System.out.print("\n");
			}
		}
	}
	
	public void ouputSetList(String outFile) {
		try {
			File file = new File(outFile);
	        if(!file.exists())
	            file.createNewFile();
	        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	        int count  = 0;
	        for(String key : this.arrSetList.keySet()) {
				if(this.arrSetList.get(key).getPack().size() != 0) {
					count++;
					writer.write("#" + count + ": " + key + ": \n");
					for(String s : this.arrSetList.get(key).getPack()) {
						writer.write("          - " + s + "\n");
					}
					writer.write("\n");
				}
			}
	        writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}

class SortByName implements Comparator {
	 public int compare(Object o1, Object o2) {
		 String c1= (String) o1;
		 String c2 = (String) o2;
		 return c1.compareTo(c2);
	 }
}
