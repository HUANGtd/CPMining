package dataanalysis;

import java.util.ArrayList;
import java.util.HashMap;

import basic.CItem;
import basic.CItemSSet;
import basic.CItemSet;
import io.*;

/**
 * Created by hhw on 3/1/16.
 */
public class DataAnalysis {
	private DataInput din = null;
	
	public DataAnalysis(DataInput din) {
		this.din = din;
	}
	
	public HashMap<String, ArrayList<String>> getNoneRepeatItemListMap(HashMap<String, CItemSSet> mapSSet) {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		for(String key : mapSSet.keySet()) {
			ArrayList<String> itemList = new ArrayList<String>();
			CItemSet newSet = new CItemSet();
			for(CItemSet set : mapSSet.get(key).getSSet()) {
				for(CItem item : set.getSet()) {
					newSet.addItem(item);
				}
			}
			newSet.SortSet();
			for(CItem item : newSet.getSortedSet()) {
				itemList.add(item.getName());
			}
			map.put(key, itemList);
		}
		
		return map;
	}
	
	public void CategoryItemSequenceAnalysis() {
		// by category
		DataOutput dout = new DataOutput(this.din.DataInputByCategory("data/patientData.txt"));
		dout.outputSSetMapByCategory("data/analysis/category.txt");
		dout.outputSortedSSetMapByCategory("data/analysis/category_sorted.txt");
	}
	
	public void PatientItemSequenceAnalysis() {
		// by patient
		DataOutput dout = new DataOutput(this.din.DataInputByPatient("data/patientData.txt"));
		dout.outputSSetMapByPatient("data/analysis/patient.txt");
		dout.outputSortedSSetMapByPatient("data/analysis/patient_sorted.txt");
	}
	
	public void CategoryByPatient() {
		DataOutput dout = new DataOutput(din.DataInputByPatient("data/patientData.txt"));
		dout.outputCategoryByPatient("data/analysis/cateory_by_patient.txt");
	}
	
	public void NoneReapeatItemByCategory() {
		DataOutput dout = new DataOutput(din.DataInputByCategory("data/patientData.txt"));
		dout.outputNoneRepeatItemByCategory("data/analysis/category_none_repeat.txt");
	}
	
	public void LOSAnalysis() {
		DataOutput dout = new DataOutput(din.DataInputByPatient("data/patientData.txt"));
		dout.outputLOS("data/analysis/LOS.txt");
	}
	
	public void CategoryAnalysisOfPatient() {
		DataOutput dout = new DataOutput(din.DataInputByPatient("data/patientData.txt"));
		dout.outputCategoryByPatient("data/analysis/cateory_patient.txt");
	}
}
