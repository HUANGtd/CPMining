package dataanalysis;

import java.util.ArrayList;
import java.util.HashMap;

import basic.*;

public class CategoryOfPatientAnalysis {
	private CItemSSet sSet = null;
	private HashMap<String, Integer> mapCateCount = null;
	public static ArrayList<String> cateList = null;
	
	public CategoryOfPatientAnalysis(CItemSSet csSet) {
		this.sSet = csSet;
		if (cateList == null) {
			cateList = new ArrayList<String>();
			cateList.add("检验病理费");
			cateList.add("西药费");
			cateList.add("特殊耗材费");
			cateList.add("检查费");
			cateList.add("中成药费");
			cateList.add("床位费");
			cateList.add("护理费");
			cateList.add("手术费");
			cateList.add("治疗费");
			cateList.add("其它费");
			cateList.add("诊查费");
			cateList.add("血费");
			cateList.add("采暖费");
			cateList.add("麻醉费");
		}
		initMap();
	}
	
	public HashMap<String, Integer> anlysis() {
		for(CItemSet cSet : this.sSet.getSSet()) {
			for(CItem item : cSet.getSet()) {
				int count = this.mapCateCount.get(item.getCategory());
				count ++;
				this.mapCateCount.put(item.getCategory(), count);
			}
		}
		return this.mapCateCount;
	}
	
	public void initMap() {
		this.mapCateCount = new HashMap<String, Integer>();
		for(String cate : cateList) {
			int i = 0;
			this.mapCateCount.put(cate, new Integer(i));
		}
	}
}
