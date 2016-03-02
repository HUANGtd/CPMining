package basic;

import java.util.ArrayList;

public class CItemSSet {
	private String strName = null;
	private int intSSetId = -1;
	private ArrayList<CItemSet> listSSet = null;
	
	public CItemSSet() {
		listSSet = new ArrayList<CItemSet>();
	}
	
	public CItemSSet(String name, int id) {
		listSSet = new ArrayList<CItemSet>();
		this.strName = name;
		this.intSSetId = id;
	}
	
	public void SortSSet() {
		for(CItemSet set : this.listSSet) {
			set.SortSet();
		}
	}
	
	public void addSet(CItemSet set) {
		this.listSSet.add(set);
	}
	
	public String getName() {
		return this.strName;
	}
	
	public int getId() {
		return this.intSSetId;
	}
	
	public ArrayList<CItemSet> getSSet() {
		return this.listSSet;
	}
	
	public void printSortedSSet() {
		int setCount = 0;
		System.out.print("\n************** " + this.strName + " ****************\n");
		for(CItemSet set : this.getSSet()) {
			setCount++;
			System.out.print("         No." + setCount + "\n");
			if(set.getSortedSet() == null) {
				SortSSet();
			}
			set.printSortedSet();
		}
	}
}
