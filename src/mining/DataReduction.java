package mining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basic.CItem;
import basic.CItemSSet;
import basic.CItemSet;
import io.DataInput;
import io.DataOutput;

public class DataReduction {
	private DataInput din;
	private HashMap<String, CItemSSet> mapSSetByCategory = null;
	private HashMap<String, CItemSSet> mapSSetByPatient = null;
	private ArrayList<String> listNoneRepeatItem = null;
	private int intTotalDays = 0;// total number of days which is the sum of LOS of each patient
	private HashMap<String, ArrayList<String>> itemToRemoveEachPatinet = null;
	private ArrayList<String> itemToRemoveOnWhole = null;
	private ArrayList<String> ignoreCategoryItem = null;
	private ArrayList<String> lowFQItem = null;
	private ArrayList<String> importantItem = null;
	private HashMap<String, Double> itemDayCoverageRatio = null;
	
	public DataReduction(DataInput din, String input) {
		this.din = din;
		this.mapSSetByCategory = this.din.DataInputByCategory(input);
		this.mapSSetByPatient = this.din.DataInputByPatient(input);
		this.listNoneRepeatItem = genNoneRepeatItemList(this.mapSSetByCategory);
		this.intTotalDays = getTotalDays(this.mapSSetByPatient);
        this.itemToRemoveOnWhole = new ArrayList<String>();
	}
	
	public DataReduction(DataInput din) {
		this.din = din;
		this.mapSSetByCategory = this.din.DataInputByCategory("data/patientData.txt");
		this.mapSSetByPatient = this.din.DataInputByPatient("data/patientData.txt");
		this.listNoneRepeatItem = genNoneRepeatItemList(this.mapSSetByCategory);
		this.intTotalDays = getTotalDays(this.mapSSetByPatient);
        this.itemToRemoveOnWhole = new ArrayList<String>();
	}
	
	// removing items that repeats almost on everyday [by patient]
	public void removingItemsRepeatDailyByPatient(double ratio) {
		this.itemToRemoveEachPatinet = new HashMap<String, ArrayList<String>>();
		for(String key : this.mapSSetByPatient.keySet()) {
			ArrayList<String> itemToRemove = new ArrayList<String>();
			int los = mapSSetByPatient.get(key).getSSet().size();
			HashMap<String, Integer> itemDayCoverage = new HashMap<String, Integer>();
			for(String item : this.listNoneRepeatItem) {
				itemDayCoverage.put(item, 0);
			}
			for(CItemSet set : this.mapSSetByPatient.get(key).getSSet()) {
				set.SortSet();
				for(CItem item : set.getSortedSet()) {
					int count = itemDayCoverage.get(item.getName());
					count++;
					itemDayCoverage.put(item.getName(), count);
				}
			}
			for(String k : itemDayCoverage.keySet()) {
				Double r = (double)itemDayCoverage.get(k)/los;
				if(r > ratio) {
					itemToRemove.add(k);
				}
			}
			this.itemToRemoveEachPatinet.put(key, itemToRemove);
		}

		// output
		DataOutput dout = new DataOutput(this.mapSSetByPatient);
		dout.outputReducedSSetMapByPatient("data/mining/patient_daily_reduced_bypatient.txt", this.itemToRemoveEachPatinet);
	}
	
	// removing items that repeats almost on everyday [on whole] && items that seldom happens 
	public void removingItemsRepeatDailyOnWhole(double top, double low) {
		HashMap<String, Integer> itemDayCoverage = new HashMap<String, Integer>();
		this.itemDayCoverageRatio = new HashMap<String, Double>();

		for(String item : this.listNoneRepeatItem) {
			itemDayCoverage.put(item, 0);
		}
		for(String key : this.mapSSetByPatient.keySet()) {
			for(CItemSet set : this.mapSSetByPatient.get(key).getSSet()) {
				set.SortSet();
				for(CItem item : set.getSortedSet()) {
					int count = itemDayCoverage.get(item.getName());
					count++;
					itemDayCoverage.put(item.getName(), count);
				}
			}
		}
		for(String key : itemDayCoverage.keySet()) {
			Double r = (double)itemDayCoverage.get(key) / this.intTotalDays;
			this.itemDayCoverageRatio.put(key, r);
			if((r > top) || (r < low)) {
				this.itemToRemoveOnWhole.add(key);
			}
		}
		// output
		DataOutput dout = new DataOutput(this.mapSSetByPatient);
		dout.outputReducedSSetMapByPatientOnWhole("data/mining/patient_daily_reduced_onwhole.txt", this.itemToRemoveOnWhole);
	}

	public void markIgnoreCategory(ArrayList<String> categoryToIgnore) {
		this.ignoreCategoryItem = new ArrayList<String>();
		for(String key : this.mapSSetByPatient.keySet()) {
			for(CItemSet set : this.mapSSetByPatient.get(key).getSSet()) {
				set.SortSet();
				for(CItem item : set.getSortedSet()) {
					if(categoryToIgnore.contains(item.getCategory())) {
						this.ignoreCategoryItem.add(item.getName());
					}
				}
			}
		}
	}
	
	// mark important item that appears in every patient
	public void markImportantItems(int top, double sup) {
		HashMap<String, Integer> itemPatientCoverage = new HashMap<String, Integer>();
		HashMap<String, Double> itemPatientCoverageRatio = new HashMap<String, Double>();
		this.importantItem = new ArrayList<String>();
		
		for(String item : this.listNoneRepeatItem) {
			itemPatientCoverage.put(item, 0);
		}
		
		for(String key : this.mapSSetByPatient.keySet()) {
			for(CItemSet set : this.mapSSetByPatient.get(key).getSSet()) {
				set.SortSet();
				ArrayList<String> noneRepeatItemOfAPatient = new ArrayList<String>();
				for(CItem item : set.getSortedSet()) {
					if(!noneRepeatItemOfAPatient.contains(item.getName())) {
						noneRepeatItemOfAPatient.add(item.getName());
					}
					
				}
				for(String s : noneRepeatItemOfAPatient) {
					int count = itemPatientCoverage.get(s);
					count++;
					itemPatientCoverage.put(s, count);
				}
			}
		}
		
		int patientNum = this.mapSSetByPatient.size();
		for(String key : itemPatientCoverage.keySet()) {
			Double r = (double)itemPatientCoverage.get(key) / patientNum;
			if((r > sup) && !(this.itemToRemoveOnWhole.contains(key)) && !(this.ignoreCategoryItem.contains(key))) {
				importantItem.add(key);
			}
//			if((r > sup) && !(this.itemToRemoveOnWhole.contains(key))) {
//				importantItem.add(key);
//			}
		}
		
		HashMap<String, Double> importantItemRate = new HashMap<String, Double>();
		for(String key : importantItem) {
//			importantItemRate.put(key, this.itemDayCoverageRatio.get(key));
            importantItemRate.put(key, (double)itemPatientCoverage.get(key) / patientNum);
		}
		
		// sort
		List<Map.Entry<String, Double>> importance = new ArrayList<Map.Entry<String, Double>>(importantItemRate.entrySet());
		Collections.sort(importance, new Comparator<Map.Entry<String, Double>>() {   
		    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
		    	return o2.getValue().compareTo(o1.getValue());
		    }
		}); 
		
		ArrayList<String> topImportantItem = new ArrayList<String>();
		if(top > importance.size()) {
			top = importance.size();
		}
		for(int i = 0; i < top; i++) {
			topImportantItem.add(importance.get(i).getKey());
			System.out.println("#" +  (i+1) + ": " + importance.get(i).getKey() + " " + importance.get(i).getValue());
		}
		
		this.importantItem = topImportantItem;
	}
	
	// util
	public ArrayList<String> genNoneRepeatItemList(HashMap<String, CItemSSet> mapSSet) {
		ArrayList<String> itemList = new ArrayList<String>();
		CItemSet newSet = new CItemSet();
		for(String key : mapSSet.keySet()) {
			for(CItemSet set : mapSSet.get(key).getSSet()) {
				for(CItem item : set.getSet()) {
					newSet.addItem(item);
				}
			}
        }
		newSet.SortSet();
		for(CItem item : newSet.getSortedSet()) {
			itemList.add(item.getName());
		}
		return itemList;
	}
	
	public int getTotalDays(HashMap<String, CItemSSet> mapSSet) {
		int sum = 0;
		for(String key : mapSSet.keySet()) {
       		sum += mapSSet.get(key).getSSet().size();
        }
		
		return sum;
	}
	
	public ArrayList<String> getNoneRepeatItemList() {
		return this.listNoneRepeatItem;
	}
	
	public HashMap<String, ArrayList<String>> getItemToRemoveEachPatinet() {
		return this.itemToRemoveEachPatinet;
	}
	
	public ArrayList<String> getItemToRemoveOnWhole() {
		return this.itemToRemoveOnWhole;
	}
	
	public ArrayList<String> getImportantItem() {
		return this.importantItem;
	}
}
