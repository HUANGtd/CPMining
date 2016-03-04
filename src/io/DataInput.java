package io;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.csvreader.CsvWriter;

import basic.*;

public class DataInput {
	private HashMap<String, CItemSSet> mapSSet = null;
	private HashMap<String, String> nameMap = null;
	
	public DataInput() {}
	
	public DataInput(HashMap<String, String> nameMap) {
		this.nameMap = nameMap;
	}

	// map[catename, list[list[item],...]]
	public HashMap<String, CItemSSet> DataInputByCategoryDaily(String fileName) {
		this.mapSSet = new HashMap<String, CItemSSet>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
			reader = new BufferedReader(read);
			int cateCount = 0;
			String line = null;
			String cate = null;
			String date = null;
			CItemSet set = null;
			HashMap<String, CItemSet> dailyCItemSset = null;
			while((line = reader.readLine()) != null) {
				String a[] = line.split(" ");
				CItem newItem = null;
				if(this.nameMap == null) {
					newItem = new CItem(a[1], a[0], a[2], a[3]);
				} else {
					newItem = new CItem(this.nameMap.get(a[1]), a[0], a[2], a[3], a[1]);
				}

				if(cate == null || date == null || !date.equals(a[3])) {
					if(dailyCItemSset != null) {
						for(String key : dailyCItemSset.keySet()) {
							if(!mapSSet.containsKey(key)) {
								cateCount++;
								CItemSSet newSSet = new CItemSSet(key, cateCount);
								mapSSet.put(key, newSSet);
							}
							mapSSet.get(key).addSet(dailyCItemSset.get(key));
						}
					}

					dailyCItemSset = new HashMap<String, CItemSet>();
					cate = a[2];
					date = a[3];
					if(!dailyCItemSset.containsKey(cate)) {
						CItemSet newDailySSet = new CItemSet(cate);
						dailyCItemSset.put(cate, newDailySSet);
					}
					dailyCItemSset.get(cate).addItem(newItem);
				} else {
					if(!dailyCItemSset.containsKey(cate)) {
						CItemSet newDailySSet = new CItemSet(cate);
						dailyCItemSset.put(cate, newDailySSet);
					}
					dailyCItemSset.get(cate).addItem(newItem);
				}
			}
			if(dailyCItemSset != null) {
				for(String key : dailyCItemSset.keySet()) {
					if(!mapSSet.containsKey(key)) {
						cateCount++;
						CItemSSet newSSet = new CItemSSet(key, cateCount);
						mapSSet.put(key, newSSet);
					}
					mapSSet.get(key).addSet(dailyCItemSset.get(key));
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return this.mapSSet;
	}

	// map[catename, list[list[item],...]]
	public HashMap<String, CItemSSet> DataInputByCategory(String fileName) {
		this.mapSSet = new HashMap<String, CItemSSet>();
		File file = new File(fileName);
        BufferedReader reader = null;
        try {
        	InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
        	reader = new BufferedReader(read);
            int cateCount = 0;
            String line = null;
            String cate = null;
            String date = null;
            CItemSet set = null;
            while ((line = reader.readLine()) != null) {
            	String a[] = line.split(" ");
            	CItem newItem = null;
            	if(this.nameMap == null) {
            		newItem = new CItem(a[1], a[0], a[2], a[3]);
            	} else {
            		newItem = new CItem(this.nameMap.get(a[1]), a[0], a[2], a[3], a[1]);
            	}
            	if(cate == null || !cate.equals(a[2]) || date == null || !date.equals(a[3])) {
            		if(set != null) {
            			mapSSet.get(cate).addSet(set);
            		}
            		cate = a[2];
            		date = a[3];
            		if(!mapSSet.containsKey(cate)) {
            			cateCount++;
            			CItemSSet newSSet = new CItemSSet(cate, cateCount);
            			mapSSet.put(cate, newSSet);
            		}
            		set = new CItemSet(cate);
            		set.addItem(newItem);
            	} else {
            		set.addItem(newItem);
            	}
            }
			if(set != null) {
				mapSSet.get(cate).addSet(set);
			}
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return this.mapSSet;
	}
	
	// map[patientId, listbyday[list[item],...]]
	public HashMap<String, CItemSSet> DataInputByPatient(String fileName) {
		this.mapSSet = new HashMap<String, CItemSSet>();
		File file = new File(fileName);
        BufferedReader reader = null;
        try {
        	InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
        	reader = new BufferedReader(read);
            int patientCount = 0;
            String line = null;
            String patient = null;
            String date = null;
            CItemSet set = null;
            while ((line = reader.readLine()) != null) {
            	String a[] = line.split(" ");
            	CItem newItem = null;
            	if(this.nameMap == null) {
            		newItem = new CItem(a[1], a[0], a[2], a[3]);
            	} else {
            		newItem = new CItem(this.nameMap.get(a[1]), a[0], a[2], a[3], a[1]);
            	}
            	if(patient == null || !patient.equals(a[0]) || date == null || !date.equals(a[3])) {
            		if(set != null) {
            			mapSSet.get(patient).addSet(set);
            		}
            		patient = a[0];
            		date = a[3];
            		if(!mapSSet.containsKey(patient)) {
            			patientCount++;
            			CItemSSet newSSet = new CItemSSet(patient, patientCount);
            			mapSSet.put(patient, newSSet);
            		}
            		set = new CItemSet();
            		set.setName(date);
            		set.addItem(newItem);
            	} else {
            		set.addItem(newItem);
            	}
            }
			if(set != null) {
				mapSSet.get(patient).addSet(set);
			}
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return this.mapSSet;
	}
	
	public void removeItemsByPatient(HashMap<String, ArrayList<String>> itemToRemoveEachPatinet) {
		if(this.mapSSet == null) {
			System.out.println("map of set is empty!");
		}
		for(String key : this.mapSSet.keySet()) {
			ArrayList<String> itemToRemove = itemToRemoveEachPatinet.get(key);
			for(CItemSet is : this.mapSSet.get(key).getSSet()) {
				for(CItem item : is.getSet()) {
					if(itemToRemove.contains(item.getName())) {
						item.setStatus(1);
					}
				}
			}
		}
	}
	
	public void removeItemsOnWhole(ArrayList<String> itemToRemoveOnWhole) {
		if(this.mapSSet == null) {
			System.out.println("map of set is empty!");
		}
		for(String key : this.mapSSet.keySet()) {
			for(CItemSet is : this.mapSSet.get(key).getSSet()) {
				for(CItem item : is.getSet()) {
					if(itemToRemoveOnWhole.contains(item.getName())) {
						item.setStatus(1);
					}
				}
			}
		}
	}
	
	public void markImportantItem(ArrayList<String> importantItem) {
		if(this.mapSSet == null) {
			System.out.println("map of set is empty!");
		}
		for(String key : this.mapSSet.keySet()) {
			for(CItemSet is : this.mapSSet.get(key).getSSet()) {
				for(CItem item : is.getSet()) {
					if(importantItem.contains(item.getName())) {
						item.setStatus(2);
					}
				}
			}
		}
	}
	
	public CItemSSet putPatientInASet() {
		if(this.mapSSet == null) {
//			this.mapSSet = DataInputByPatient(fileName);
			System.out.println("map of set is empty!");
		}
		CItemSSet sset = new CItemSSet();
		for(String key : this.mapSSet.keySet()) {
			for(CItemSet is : this.mapSSet.get(key).getSSet()) {
				ArrayList<CItem> oldList = is.getSet();
				ArrayList<CItem> newList = new ArrayList<CItem>();
				for(CItem item : oldList) {
					if(item.getStatus() == 0) {
						newList.add(item);
					}
				}
				is.setSet(newList);
				sset.addSet(is);
			}
		}
		
		return sset;
	}
	
	public HashMap<String, CItemSSet> getSSetList() {
		return this.mapSSet;
	}
	
	/*************** 2csv file ***************/
	public void MapSSet2csv(String csvfile) {
		CsvWriter wr =new CsvWriter(csvfile,',',Charset.forName("GBK"));              
		try {
			int caseId = 0;
			for(String key : this.mapSSet.keySet()) {
				CItemSSet sset = this.mapSSet.get(key);
				sset.SortSSet();
				caseId++;
				for(CItemSet set : sset.getSSet()) {
					for(CItem item : set.getSortedSet()) {
						if(item.getStatus() == 2) {
							String date[] = item.getDate().split("/");
							String newDate = "20" + date[2] + "/" + date[0] + "/" + date[1]; 
							String[] contents = {String.valueOf(caseId), item.getPatientId(), item.getName(), newDate};
							wr.writeRecord(contents);
						}
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wr.close();
	}
	
}
