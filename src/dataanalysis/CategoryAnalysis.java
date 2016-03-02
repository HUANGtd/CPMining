package dataanalysis;

import java.util.ArrayList;

import basic.*;

// ���������item�Ļ���
public class CategoryAnalysis {
	private CItemSSet cateSet = null;
	
	public CategoryAnalysis(CItemSSet cate) {
		this.cateSet = cate;
	}
	
	public ArrayList<CItem> categoryNoneRepeat() {
		CItemSet cate = new CItemSet();
		for(CItemSet set : this.cateSet.getSSet()) {
			cate.getSet().addAll(set.getSet());
		}
		cate.SortSet();
		return cate.getSortedSet();
	}
}
