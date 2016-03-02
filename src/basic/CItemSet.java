package basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CItemSet {
	private String strSetName = null;
	private int intSetId = -1;
	private String strCategory = null;
	private ArrayList<CItem> itemset = null;
	private ArrayList<CItem> sortedItemset = null;
	
	public CItemSet() {
		this.itemset = new ArrayList<CItem>();
	}
	
	public CItemSet(String cate) {
		this.itemset = new ArrayList<CItem>();
		this.strCategory = cate;
	} 
	
	public CItemSet(String name, int id) {
		this.itemset = new ArrayList<CItem>();
		this.strSetName = name;
		this.intSetId = id;
	}
	
	// sort
	public void SortSet() {
		ArrayList<String> listNames = new ArrayList<String>();
		if(this.itemset == null) {
			System.out.println("Set not existing.");
			return;
		}
		this.sortedItemset = new ArrayList<CItem>();
		for(CItem item : this.itemset) {
			if(!listNames.contains(item.getName())) {
				listNames.add(item.getName());
				this.sortedItemset.add(item);
			}	
		}
		Collections.sort(this.sortedItemset, new SortByName());
	}
	
	// in the same order
	public Boolean containSequence(FPSequence fps) {
		int length = fps.getLength();
		FPItem[] fpis = fps.getSequence();
		int mark = 0;
		for(CItem item : this.sortedItemset) {
			if(item.cmpName(fpis[mark].getName())) {
				mark++;
			}
			if(mark == length) {
				return true;
			}
		}
		if(mark == length) {
			return true;
		}
		
		return false;
	}
	
	// exactly has
	public Boolean hasSequence(FPSequence fps) {
		int length = fps.getLength();
		FPItem[] fpis = fps.getSequence();
		int mark = 0;
		for(CItem item : this.sortedItemset) {
			if(item.cmpName(fpis[mark].getName())) {
				mark++;
			} else {
				mark = 0;
			}
			if(mark == length) {
				return true;
			}
		}
		
		return false;
	}
	
	public Boolean containString(String s) {
		// contains item which name is s
		if(this.sortedItemset == null) {
			SortSet();
		}
		for(CItem item : this.sortedItemset) {
			if(item.getName().equals(s)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addItem(CItem item) {
		this.itemset.add(item);
	}
	
	public String getName() {
		return this.strSetName;
	}
	
	public int getId() {
		return this.intSetId;
	}
	
	public ArrayList<CItem> getSet() {
		return this.itemset;
	}
	
	public void setSet(ArrayList<CItem> newitemset) {
		this.itemset = newitemset;
	}
	
	public ArrayList<CItem> getSortedSet() {
		return this.sortedItemset;
	}
	
	public void setName(String name) {
		this.strSetName = name;
	}
	
	public void setId(int id) {
		this.intSetId = id;
	}
	
	public String getCategory() {
		return this.strCategory;
	}

	public void setCategory(String category) {
		this.strCategory = category;
	}
	
	public void printSortedSet() {
		int setItemCount = 0;
		for(CItem item : this.getSortedSet()) {
			setItemCount++;
			 System.out.print("           #"+ setItemCount + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
		}
	}
	
}

class SortByName implements Comparator {
	 public int compare(Object o1, Object o2) {
		 CItem c1= (CItem) o1;
		 CItem c2 = (CItem) o2;
		 return c1.getName().compareTo(c2.getName());
	 }
}
