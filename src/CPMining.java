import java.util.ArrayList;
import java.util.HashMap;

import basic.*;
import mining.CatePackaging;
import preprocess.ItemSet;
import preprocess.PreProcess;
import mining.*;
import dataanalysis.DataAnalysis;
import io.DataInput;

public class CPMining {

	public static void main(String[] args) {
		
		/******************* analysis **************/
		//Analysis();
		
		/******************* preprocess **************/
		HashMap<String, String> nameMap = preProcess();

		/******************* minging **************/ 
        CAMining(nameMap);
	}

	// clinial activity mining 
	public static void CAMining(HashMap<String, String> nameMap) {
		DataInput din = new DataInput(nameMap);
		DataReduction dr = new DataReduction(din);
		
		/******************* write to csv file **************/
//		DataInput din2csv = new DataInput(nameMap);
//		HashMap<String, CItemSSet> mapSSet2csv = din2csv.DataInputByPatient("data/patientData.txt");
//		din2csv.MapSSet2csv("data/patient_packaged_removed.csv");
		
		/******************* reduction[by patient] **************/
		HashMap<String, CItemSSet> mapSSet = din.DataInputByPatient("data/patientData.txt");

        /** reduce on whole **/
        dr.removingItemsRepeatDailyOnWhole(0.5, 0.05);
        din.removeItemsOnWhole(dr.getItemToRemoveOnWhole());
		 
        dr.markImportantItems(20, 0.98);
        din.markImportantItem(dr.getImportantItem());
        din.MapSSet2csv("data/patient_packaged_removed.csv");
		
		/** reduce by patient **/
//		dr.removingItemsRepeatDailyByPatient(0.8);
//		din.removeItemsByPatient(dr.getItemToRemoveEachPatinet());
		
//		din.MapSSet2csv("data/patient_packaged_removed.csv");
		
		/******************* mine by patient using apriori **************/
		/*
		double ratio_mine = 0.1;
		CItemSSet sset_mine = din.putPatientInASet();
		sset_mine.SortSSet();// ***
		ArrayList<String> itemList_mine = dr.getNoneRepeatItemList();
		FSetPackaging cap1 = new FSetPackaging(sset_mine.getSSet(), ratio_mine);
		for(String s : itemList2) {
			System.out.println(s);
		}
		cap1.Apriori(itemList_mine);
		cap1.printFPSetList();
		cap1.outputFPSetList("data/mining/list_on_patient_" + ratio_mine + ".txt");
		*/
		
		/******************* mine by category **************/
		/*
		double ratio = 0.04;
		HashMap<String, CItemSSet> mapSSet_cate = din.DataInputByCategory("data/patientData.txt");
		DataAnalysis da = new DataAnalysis(din);
		CItemSSet sset = mapSSet_cate.get("检验病理费");
		ArrayList<String> itemList = da.getNoneRepeatItemListMap(mapSSet_cate).get("检验病理费");
		sset.SortSSet();
		FSetPackaging cap = new FSetPackaging(sset.getSSet(), ratio);
		cap.Apriori(itemList);
		cap.printFPSetList();
		cap.outputFPSetList("data/mining/list_on_cate_" + ratio + ".txt");
		*/
	}
	
	public static HashMap<String, String> preProcess() {
		HashMap<String, String> nameMap = null;
		HashMap<String, PackSet> mapKey2Ps = new HashMap<String, PackSet>();
		HashMap<String, PackSSet> mapKey2Pss = new HashMap<String, PackSSet>();
		HashMap<String, PackSSSet> mapKey2Psss = new HashMap<String, PackSSSet>();
		
		/****************** pre1: package items with the similar names *****************/
		DataInput pre1din = new DataInput();
		DataReduction dr = new DataReduction(pre1din);
		ArrayList<String> itemList = dr.getNoneRepeatItemList();
		PreProcess pp = new PreProcess(itemList);
		pp.genSetMap();
//		pp.outputSetList("data/preprocess/item_replace.txt");
		nameMap = pp.getNameMap();
		
		// set basic.PackSet
		HashMap<String, ItemSet> setList = pp.getSetList();
		for(String key : setList.keySet()) {
			if(setList.get(key).getPack().size() != 0) {
				PackSet ps = new PackSet(key);
				for(String s : setList.get(key).getPack()) {
					ps.addItem(s);
				}
				mapKey2Ps.put(key, ps);
			}
		}
		
		/****************** pre2: package items that often occur together in each category *****************/
		DataInput pre2din = new DataInput(nameMap);
//		double ratio_cp = 0.8;
		HashMap<String, String> mapAllItem2package = new HashMap<String, String>();
		HashMap<String, CItemSSet> mapSSet_cp = pre2din.DataInputByCategory("data/patientData.txt");
		DataAnalysis da_cp = new DataAnalysis(pre2din);
		double bestSup2;
		for(String key : mapSSet_cp.keySet()) {
			CItemSSet sset_cp = mapSSet_cp.get(key);
			sset_cp.SortSSet();
			CatePackaging cp = new CatePackaging(sset_cp.getSSet());
			
			bestSup2 = cp.findTheBestSupport();
//			System.out.println(key + ": " + bestSup2);
//			cp.printMapCateset();
			
			// replace items with package
			HashMap<String, Boolean> cpMark = cp.getMarkMap();
			HashMap<String, String> cpMap = cp.getItem2keyMap();
			for(String kk : cpMap.keySet()) {
				String oldName = cpMap.get(kk);
				if(cpMark.get(oldName)) {
//					String newName = new String(oldName + "<I>");
					 String newName = new String(oldName/* + "<I>"*/);
					cpMap.put(kk, newName);
				}
			}
			mapAllItem2package.putAll(cpMap);
			
			// set basic.PackSSet
			HashMap<String, CateSet> mapCateSet = cp.getName2Cateset();
			for(String ky : mapCateSet.keySet()) {
				PackSSet pss = new PackSSet(ky, key);
				for(String s : mapCateSet.get(ky).getCateSet()) {
					pss.addSet(mapKey2Ps.get(s));
				}
				mapKey2Pss.put(ky, pss);
			}
		}
		for(String key : nameMap.keySet()) {
			String previousKey = nameMap.get(key);
			String newKey = new String(mapAllItem2package.get(previousKey));
			nameMap.put(key, newKey);
		}
		
		/****************** pre3: package items that often occur together in each day *****************/
//		double ratio_pack = 0.5;
		double bestSup3;
		DataInput pre3din = new DataInput(nameMap);
		HashMap<String, CItemSSet> mapSSet_pack = pre3din.DataInputByPatient("data/patientData.txt");
		CItemSSet csset = pre3din.putPatientInASet();
		csset.SortSSet();
		CatePackaging cp_pack = new CatePackaging(csset.getSSet());
		bestSup3 = cp_pack.findTheBestSupport();
//		System.out.println("best support: " + bestSup3);
		
		// replace items with package
		HashMap<String, Boolean> cpMark_pack = cp_pack.getMarkMap();
		HashMap<String, String> cpMap_pack = cp_pack.getItem2keyMap();
		for(String kkk : cpMap_pack.keySet()) {
			String oldName2 = cpMap_pack.get(kkk);
			if(cpMark_pack.get(oldName2)) {
//				 String newName2 = new String(oldName2 + "<II>");
				 String newName2 = new String(oldName2/* + "<II>"*/);
				cpMap_pack.put(kkk, newName2);
			}
		}
		for(String key : nameMap.keySet()) {
			String previousKey2 = nameMap.get(key);
			String newKey2 = new String(cpMap_pack.get(previousKey2));
			nameMap.put(key, newKey2);
		}
		
		// set basic.PackSSSet
		HashMap<String, CateSet> mapCateSet_pack = cp_pack.getName2Cateset();
		for(String key : mapCateSet_pack.keySet()) {
			PackSSSet psss = new PackSSSet(key);
			for(String s : mapCateSet_pack.get(key).getCateSet()) {
//				System.out.print(s);
				psss.addSSet(mapKey2Pss.get(s));
			}
			mapKey2Psss.put(key, psss);
		}
		
		// output mapKey2Psss
//		printMapKey2Psss(mapKey2Psss, nameMap);
		
		return nameMap;
	}
	
	public static void printMapKey2Psss(HashMap<String, PackSSSet> mapKey2Psss, HashMap<String, String> nameMap) {
		// counting num
		int totalDays = 0;
		DataInput din = new DataInput(nameMap);
		HashMap<String, Integer> packTimebyday = new HashMap<String, Integer>();
		HashMap<String, String> packCate = new HashMap<String, String>();
		HashMap<String, CItemSSet> mapSSet = din.DataInputByPatient("data/patientData.txt");
		CItemSSet csset = din.putPatientInASet();
		csset.SortSSet();
		for(CItemSet cset : csset.getSSet()) {
			totalDays++;
			for(CItem item : cset.getSortedSet()) {
				String key = item.getName();
				if(packTimebyday.containsKey(key)) {
					int count = packTimebyday.get(key);
					packTimebyday.put(key, count+1);
				} else {
					packTimebyday.put(key, 1);
				}
				
				if(!packCate.containsKey(key)) {
					packCate.put(key, item.getCategory());
				}
			}
		}
		
		// printing
		int psssCount = 0;
		for(String key : mapKey2Psss.keySet()) {
			if((double)packTimebyday.get(key)/totalDays > 0.05) {
				psssCount++;
				System.out.print("#" + psssCount + "(" + packTimebyday.get(key) + "/" + totalDays + ")" + "[" + packCate.get(key) + "]: ");
				mapKey2Psss.get(key).printPackSSSet(psssCount);
				System.out.print("\n");
			}
		}
	}
	
	public static void Analysis() {
		DataInput din = new DataInput();
		
		DataAnalysis da = new DataAnalysis(din);
		
		//1
		da.CategoryItemSequenceAnalysis();
		System.out.println("Analysis result: [data/analysis/category.txt]");
		System.out.println("Analysis result: [data/analysis/category_sorted.txt]");
		System.out.println("Analysis result: [data/analysis/patient.txt]");
		System.out.println("Analysis result: [data/analysis/patient_sorted.txt]");
		
		// 2
		da.NoneReapeatItemByCategory();
		System.out.println("Analysis result: [data/analysis/category_none_repeat.txt]");
		
		//3 LOS
		da.LOSAnalysis();
		System.out.println("Analysis result: [data/analysis/LOS.txt]");
		
		// 4
		da.CategoryAnalysisOfPatient();
		System.out.println("Analysis result: [data/analysis/cateory_patient.txt]");
	}
}
