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
			cateList.add("���鲡���");
			cateList.add("��ҩ��");
			cateList.add("����Ĳķ�");
			cateList.add("����");
			cateList.add("�г�ҩ��");
			cateList.add("��λ��");
			cateList.add("�����");
			cateList.add("������");
			cateList.add("���Ʒ�");
			cateList.add("������");
			cateList.add("����");
			cateList.add("Ѫ��");
			cateList.add("��ů��");
			cateList.add("�����");
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
