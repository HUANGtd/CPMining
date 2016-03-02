package preprocess;

import java.util.ArrayList;
import java.util.HashMap;

import basic.*;

public class CatePackaging {
	private HashMap<String, CateSet> mapName2Cateset = null;
	private HashMap<String, ArrayList<CItemSet>> mapName2arrNoneRepeatItem = null; // item -> sequence contains the item
	private HashMap<String, String> mapItem2key = null;
	private HashMap<String, Boolean> markIfMoreThanOne = null; // if cateset.size() > 1 -> true
	private HashMap<String, HashMap<String, Double>> mapAccompanyRate = null;
	private ArrayList<CItemSet> arrD = null;
	private int intMaxLength = 0;
	
	public CatePackaging(ArrayList<CItemSet> D) {
		this.arrD = D;
		this.mapName2arrNoneRepeatItem = getNoneRepeatItemMap(D);
		this.mapAccompanyRate = getAccompanyRateForAll();
	}
	
	/****************** package by find the best ratio *****************/
	public double findTheBestSupport() {
		double bestSupport = 0.0;
		double minScore = 10000.0;
		HashMap<String, CateSet> tempName2Cateset;
		HashMap<String, String> tempItem2key;
		
		// for sup = 0.3-0.8
		double sup;
		double score; 
		for(int i = 30; i < 80; i++) {
			sup = (double)i/100;
			tempItem2key = new HashMap<String, String>();
			tempName2Cateset = getInitialCatesetmap(tempItem2key);
			score = packagingWithScore(sup, tempItem2key, tempName2Cateset);
			
			if(minScore >= score) {
				minScore = score;
				bestSupport = sup;
				this.mapItem2key = tempItem2key;
				this.mapName2Cateset = tempName2Cateset;
			}
		}
		
		return bestSupport;
	}
	
	public double packagingWithScore(double ratio, HashMap<String, String> tempItem2key, HashMap<String, CateSet> tempName2Cateset) {
		double totalScore = 0.0;
		
		// get packages
		for(int i = 1; i < this.intMaxLength; i++) {
			for(CItemSet cset : this.arrD) {
				ArrayList<CItem> arrCItem = cset.getSortedSet();
				if(arrCItem.size() < i+1) {
					continue;
				}
				String citem = arrCItem.get(i).getName();
				String citem_pre = arrCItem.get(i-1).getName();
				if(!tempItem2key.get(citem).equals(citem)) {
					continue;
				} else {
					if(isTogetherAll(tempName2Cateset.get(citem).getCateSet(), tempName2Cateset.get(tempItem2key.get(citem_pre)).getCateSet(), ratio)) {
						String key = tempItem2key.get(citem_pre);
						for(String s : tempName2Cateset.get(citem).getCateSet()) {
							tempItem2key.put(s, key);
						}
						tempName2Cateset.get(key).mergeCateSet(tempName2Cateset.get(citem));
						tempName2Cateset.remove(citem);
					}
				}
			}
		}
		
		// get score
		double varianceOfAccompanyRateInPackage = 0.0;
		double varianceOfCoverRateInPackage = 0.0;
		double isolatePackageRate = 0.0;
//		double sizeRate = 0.0; // average size/ size
		int countOfIsolatePackage = 0;
//		int sumOfPackageSize = 0;
		
		for(String pack : tempName2Cateset.keySet()) {
//			sumOfPackageSize += tempName2Cateset.get(pack).getCateSet().size();
			if(tempName2Cateset.get(pack).getCateSet().size() == 1) {
				countOfIsolatePackage++;
			}	
		}
		isolatePackageRate = (double)countOfIsolatePackage / tempName2Cateset.size();
//		sizeRate = (double)sumOfPackageSize / tempName2Cateset.size();

		for(String pack : tempName2Cateset.keySet()) {
			ArrayList<String> packageSet = tempName2Cateset.get(pack).getCateSet();
			
			// get variance of accompany rate
			ArrayList<Double> accompanyRate = new ArrayList<Double>();
			for(String itemA : packageSet) {
				for(String itemB : packageSet) {
					if(!itemA.equals(itemB)) {
						accompanyRate.add((this.mapAccompanyRate.get(itemA).get(itemB))/2);
					}
				}
			}
			if(packageSet.size() == 1) {
				varianceOfAccompanyRateInPackage = isolatePackageRate;
			} else {
				varianceOfAccompanyRateInPackage = getVariance(accompanyRate);
			}
			
			// get variance of cover rate
			ArrayList<Double> coverRate = new ArrayList<Double>();
			ArrayList<CItemSet> noneRepearCItemSet4Package = getNoneRepearCItemSet4Package(packageSet);
			
			for(String item : packageSet) {
				double itemCoverRate;
				int coverTimes = 0;
				for(CItemSet cset : noneRepearCItemSet4Package) {
					if(cset.containString(item)) {
						coverTimes++;
					}
				}
				itemCoverRate = (double)coverTimes / noneRepearCItemSet4Package.size();
				coverRate.add(itemCoverRate);
			}
			if(packageSet.size() == 1) {
				varianceOfCoverRateInPackage = isolatePackageRate;
			} else {
				varianceOfCoverRateInPackage = getVariance(coverRate);
			}
			
			// get coverRate of each package in D
			int appearTimeOfPackage = 0;
			for(CItemSet cset : this.arrD) {
				if(appears(cset, packageSet)) {
					appearTimeOfPackage++;
				}
			}
			
//			totalScore += appearTimeOfPackage * varianceOfAccompanyRateInPackage / packageSet.size();
			totalScore += (varianceOfAccompanyRateInPackage + varianceOfCoverRateInPackage) / packageSet.size() / appearTimeOfPackage;
//			totalScore += appearTimeOfPackage * packageSet.size() * (varianceOfAccompanyRateInPackage + varianceOfCoverRateInPackage);
		}
		
		return totalScore / tempName2Cateset.size();
	}
	
	// util for scoring
	public boolean appears(CItemSet cset, ArrayList<String> packageSet) {
		for(String item : packageSet) {
			if(cset.containString(item)) {
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<CItemSet> getNoneRepearCItemSet4Package(ArrayList<String> packageSet) {
		ArrayList<CItemSet> noneRepearCItemSet4Package = new ArrayList<CItemSet>();
		for(String item : packageSet) {
			ArrayList<CItemSet> noneRepearCItemSet4ItemOfPackage = this.mapName2arrNoneRepeatItem.get(item);
			for(CItemSet cset : noneRepearCItemSet4ItemOfPackage) {
				if(!noneRepearCItemSet4Package.contains(cset)) {
					noneRepearCItemSet4Package.add(cset);
				}
			}
		}
		
		return noneRepearCItemSet4Package;
	}
	
	/****************** package with given ratio *****************/
	public void Packaging(double ratio) {
		this.mapItem2key = new HashMap<String, String>();
		this.mapName2Cateset = getInitialCatesetmap(this.mapItem2key);
//		System.out.println(this.mapName2Cateset.size() + " "+ ratio);
		for(int i = 1; i < this.intMaxLength; i++) {
			for(CItemSet cset : this.arrD) {
				ArrayList<CItem> arrCItem = cset.getSortedSet();
				if(arrCItem.size() < i+1) {
					continue;
				}
				String citem = arrCItem.get(i).getName();
				String citem_pre = arrCItem.get(i-1).getName();
				if(!this.mapItem2key.get(citem).equals(citem)) {
					continue;
				} else {
					if(isTogetherAll(this.mapName2Cateset.get(citem).getCateSet(), this.mapName2Cateset.get(this.mapItem2key.get(citem_pre)).getCateSet(), ratio)) {
						String key = this.mapItem2key.get(citem_pre);
						for(String s : this.mapName2Cateset.get(citem).getCateSet()) {
							this.mapItem2key.put(s, key);
						}
						this.mapName2Cateset.get(key).mergeCateSet(this.mapName2Cateset.get(citem));
						this.mapName2Cateset.remove(citem);
					}
//					if(isTogether(citem, citem_pre, ratio)) {
//						String key = this.mapItem2key.get(citem_pre);
//						for(String s : this.mapName2Cateset.get(citem).getCateSet()) {
//							this.mapItem2key.put(s, key);
//						}
//						this.mapName2Cateset.get(key).mergeCateSet(this.mapName2Cateset.get(citem));
//						this.mapName2Cateset.remove(citem);
//					}
				}
			}
		}
	}
	
	/****************** util *****************/
	public double getVariance(ArrayList<Double> list) {
		double variance = 0.0;
		double average = 0.0;
		double sumOfList = 0.0;
		double sumOfVariance = 0.0;
	
		for(Double d : list) {
			sumOfList += d;
		}
		average = (double)sumOfList / list.size();
		
		for(Double d : list) {
			sumOfVariance += (d - average) * (d - average);
		}
		variance = (double)sumOfVariance / list.size();
		
		return variance;
	}
	
	
	public Boolean isTogetherAll(ArrayList<String> arrA, ArrayList<String> arrB, double ratio) {
		for(String sA : arrA) {
			for(String sB : arrB) {
				if(!isTogether(sA, sB, ratio)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public Boolean isTogether(String sA, String sB, double ratio) {
//		double AinB = getRatioAinB(sA, sB);
//		double BinA = getRatioAinB(sB, sA);
		double AinB = this.mapAccompanyRate.get(sA).get(sB);
		double BinA = this.mapAccompanyRate.get(sB).get(sA);
		if((AinB >= ratio) && (BinA >= ratio)) {
			return true;
		} else {
			return false;
		}
	}
	
	public double getRatioAinB(String sA, String sB) {
		// P(B|A)
		ArrayList<CItemSet> arrSet = this.mapName2arrNoneRepeatItem.get(sA);
		int timesA = arrSet.size();
		int timesB = 0;
		for(CItemSet set : arrSet) {
			if(set.containString(sB)) {
				timesB++;
			}
		}
		
		return (double)timesB/timesA;
	}
	
	public HashMap<String, CateSet> getInitialCatesetmap(HashMap<String, String> mapItems2keys) {
		HashMap<String, CateSet> map = new HashMap<String, CateSet>();
//		this.mapItem2key = new HashMap<String, String>();
		for(String key : this.mapName2arrNoneRepeatItem.keySet()) {
			CateSet cset = new CateSet(key, key+"[etc.]");
			cset.addItem(key);
			map.put(key, cset);
			mapItems2keys.put(key, key);
		}
		
		return map;
	}
	
	public HashMap<String, ArrayList<CItemSet>> getNoneRepeatItemMap(ArrayList<CItemSet> D) {
		HashMap<String, ArrayList<CItemSet>> map = new HashMap<String, ArrayList<CItemSet>>();
		for(CItemSet cset : D) {
			for(CItem citem : cset.getSortedSet()) {
				String name = citem.getName();
				if(!map.containsKey(name)) {
					ArrayList<CItemSet> arr = new ArrayList<CItemSet>();
					arr.add(cset);
					map.put(name, arr);
				} else {
					ArrayList<CItemSet> arr = map.get(name);
					arr.add(cset);
				}
			}
			int length = cset.getSortedSet().size();
			if(this.intMaxLength < length) {
				this.intMaxLength = length;
			}
		}
		
		return map;
	}
	
	public HashMap<String, HashMap<String, Double>> getAccompanyRateForAll() {
		HashMap<String, HashMap<String, Double>> mapAccompanyRateForAll = new HashMap<String, HashMap<String, Double>>();
		
		for(String A : this.mapName2arrNoneRepeatItem.keySet()) {
			HashMap<String, Double> accompanyRate = new HashMap<String, Double>();
			for(String B : this.mapName2arrNoneRepeatItem.keySet()) {
				accompanyRate.put(B, getRatioAinB(A, B));
			}
			mapAccompanyRateForAll.put(A, accompanyRate);
		}
		
		return mapAccompanyRateForAll;
	}
	
	public HashMap<String, String> getItem2keyMap() {
		return this.mapItem2key;
	}
	
	public HashMap<String, Boolean> getMarkMap() {
		this.markIfMoreThanOne = new HashMap<String, Boolean>();
		for(String key : this.mapName2Cateset.keySet()) {
			if(this.mapName2Cateset.get(key).getCateSet().size() > 1) {
				this.markIfMoreThanOne.put(key, true);
			} else {
				this.markIfMoreThanOne.put(key, false);
			}
		}
		
		return this.markIfMoreThanOne;
	}
	
	public HashMap<String, CateSet> getName2Cateset() {
		return this.mapName2Cateset;
	}
	
	/****************** output *****************/
	public void printMapCateset() {
		int setCount = 0;
		for(String key : this.mapName2Cateset.keySet()) {
			setCount++;
			System.out.print("N0." + setCount + ": [");
			this.mapName2Cateset.get(key).printCateSet();
			System.out.print("\n");
		}
	}
}
