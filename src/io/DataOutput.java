package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import basic.*;
import dataanalysis.*;

public class DataOutput {
	private HashMap<String, CItemSSet> mapSSet = null;
	
	public DataOutput(HashMap<String, CItemSSet> mapSSet) {
		this.mapSSet = mapSSet;
	}
	
	// mining output
	
	public void outputReducedSSetMapByPatient(String outFile, HashMap<String, ArrayList<String>> itemToRemoveEachPatinet) {
		 try {
			 File file = new File(outFile);
	         if(!file.exists())
	             file.createNewFile();
	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	         int patientCount = 0;
	         int setCount = 0;
	         int setItemCount = 0;
	         for(String key : this.mapSSet.keySet()) {
	        	 patientCount++;
	        	 setCount = 0;
	        	 setItemCount = 0;
	        	 writer.write("\n**************" + patientCount + ": "+ key + "****************\n");
	        	 for(CItemSet set : this.mapSSet.get(key).getSSet()) {
	        		 setCount++;
	        		 writer.write("         Day." + setCount + ": " + set.getName() +"\n");
	        		 set.SortSet();
	        		 for(CItem item : set.getSortedSet()) {
	        			 if(itemToRemoveEachPatinet.get(key).contains(item.getName())) {
//	        				 setItemCount++;
//	        				 writer.write("          -#"+ setItemCount + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
	        			 } else {
	        				 setItemCount++;
	        				 writer.write("           #"+ setItemCount + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
	        			 }
	        		}
	        	 }
	        	 writer.write("         Removed." +"\n");
	        	 int count = 0;
	        	 for(String s : itemToRemoveEachPatinet.get(key)) {
	        		 count++;
	        		 writer.write("          -#"+ count + ". " + s + "\n"); 
	        	 }
	         }
	         writer.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
	}
	
	public void outputReducedSSetMapByPatientOnWhole(String outFile, ArrayList<String> itemToRemove) {
		 try {
			 File file = new File(outFile);
	         if(!file.exists())
	             file.createNewFile();
	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	         int patientCount = 0;
	         int setCount = 0;
	         int setItemCount = 0;
	         for(String key : this.mapSSet.keySet()) {
	        	 patientCount++;
	        	 setCount = 0;
	        	 setItemCount = 0;
	        	 writer.write("\n**************" + patientCount + ": "+ key + "****************\n");
	        	 for(CItemSet set : this.mapSSet.get(key).getSSet()) {
	        		 setCount++;
	        		 writer.write("         Day." + setCount + ": " + set.getName() +"\n");
	        		 set.SortSet();
	        		 for(CItem item : set.getSortedSet()) {
	        			 if(itemToRemove.contains(item.getName())) {
//	        				 setItemCount++;
	        				 writer.write("          -#***" + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
	        			 } else {
	        				 setItemCount++;
	        				 writer.write("           #"+ setItemCount + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
	        			 }
	        		}
	        	 }
	         }
	         writer.close();
     } catch (IOException e) {
         e.printStackTrace();
     }
	}
	
	// analysis output
	public void outputSSetMapByCategory(String outFile) {
		 try {
			 File file = new File(outFile);
	         if(!file.exists())
	             file.createNewFile();
	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	         int cateCount = 0;
	         int setCount = 0;
	         int setItemCount = 0;
	         for(String key : this.mapSSet.keySet()) {
	        	 cateCount++;
	        	 setCount = 0;
	        	 setItemCount = 0;
//	        	 System.out.println(key);
	        	 writer.write("\n**************" + cateCount + ": "+ key + "****************\n");
	        	 for(CItemSet set : this.mapSSet.get(key).getSSet()) {
	        		 setCount++;
	        		 writer.write("         No." + setCount + "\n");
	        		 for(CItem item : set.getSet()) {
	        			 setItemCount++;
//	        			 System.out.println("           " + item.getName() + ", " + item.getCategory() + ", " + item.getDate());
	        			 writer.write("           #"+ setItemCount + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
	        		 }
	        	 }
	         }
	         writer.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
	}
	
	public void outputSortedSSetMapByCategory(String outFile) {
		 try {
			 File file = new File(outFile);
	         if(!file.exists())
	             file.createNewFile();
	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	         int cateCount = 0;
	         int setCount = 0;
	         int setItemCount = 0;
	         for(String key : this.mapSSet.keySet()) {
	        	 cateCount++;
	        	 setCount = 0;
	        	 setItemCount = 0;
	        	 writer.write("\n**************" + cateCount + ": "+ key + "****************\n");
	        	 for(CItemSet set : this.mapSSet.get(key).getSSet()) {
	        		 setCount++;
	        		 writer.write("         No." + setCount + "\n");
	        		 set.SortSet();
	        		 for(CItem item : set.getSortedSet()) {
	        			 setItemCount++;
	        			 writer.write("           #"+ setItemCount + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
	        		 }
	        	 }
	         }
	         writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void outputSSetMapByPatient(String outFile) {
		 try {
			 File file = new File(outFile);
	         if(!file.exists())
	             file.createNewFile();
	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	         int patientCount = 0;
	         int setCount = 0;
	         int setItemCount = 0;
	         for(String key : this.mapSSet.keySet()) {
	        	 patientCount++;
	        	 setCount = 0;
	        	 setItemCount = 0;
	        	 writer.write("\n**************" + patientCount + ": "+ key + "****************\n");
	        	 for(CItemSet set : this.mapSSet.get(key).getSSet()) {
	        		 setCount++;
	        		 writer.write("         Day." + setCount + ": " + set.getName() +"\n");
	        		 for(CItem item : set.getSet()) {
	        			 setItemCount++;
	        			 writer.write("           #"+ setItemCount + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
	        		 }
	        	 }
	         }
	         writer.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
	}
	
	public void outputSortedSSetMapByPatient(String outFile) {
		 try {
			 File file = new File(outFile);
	         if(!file.exists())
	             file.createNewFile();
	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	         int patientCount = 0;
	         int setCount = 0;
	         int setItemCount = 0;
	         for(String key : this.mapSSet.keySet()) {
	        	 patientCount++;
	        	 setCount = 0;
	        	 setItemCount = 0;
	        	 writer.write("\n**************" + patientCount + ": " + key + "****************\n");
	        	 for(CItemSet set : this.mapSSet.get(key).getSSet()) {
	        		 setCount++;
	        		 writer.write("         Day." + setCount + ": " + set.getName() +"\n");
	        		 set.SortSet();
	        		 for(CItem item : set.getSortedSet()) {
	        			 setItemCount++;
	        			 writer.write("           #"+ setItemCount + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
	        		 }
	        	 }
	         }
	         writer.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
	}
	
	public void outputNoneRepeatItemByCategory(String outFile) {
		 try {
			 File file = new File(outFile);
	         if(!file.exists())
	             file.createNewFile();
	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	         int cateCount = 0;
	         int itemCount = 0;
	         for(String key : this.mapSSet.keySet()) {
	        	 cateCount++;
	        	 itemCount = 0;
	        	 writer.write("\n**************" + cateCount + ": "+ key + "****************\n");
        		 for(CItem item : new CategoryAnalysis(this.mapSSet.get(key)).categoryNoneRepeat()) {
        			 itemCount++;
        			 writer.write("           #"+ itemCount + ". " + item.getName() + ", " + item.getCategory() + ", " + item.getDate() + "\n");
        		 }
	         }
	         writer.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
	}
	
	public void outputLOS(String outFile) {
		try {
			 File file = new File(outFile);
	         if(!file.exists())
	             file.createNewFile();
	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	         int patientCount = 0;
	         int lengthOfStay = 0;
	         for(String key : this.mapSSet.keySet()) {
	        	 patientCount++;
	        	 lengthOfStay = this.mapSSet.get(key).getSSet().size();
//	        	 writer.write(patientCount + "," + this.mapSSet.get(key).getSSet().get(1).getSet().get(0).getPatientId() + "," + lengthOfStay + "\n");
	        	 writer.write(patientCount + " " + this.mapSSet.get(key).getSSet().get(1).getSet().get(0).getPatientId() + " " + lengthOfStay + "\n");
	         }
	         writer.close();
     } catch (IOException e) {
         e.printStackTrace();
     }
	}
	
	public void outputCategoryByPatient(String outFile) {
		 try {
			 File file = new File(outFile);
	         if(!file.exists())
	             file.createNewFile();
	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	         int patientCount = 0;
	         for(String key : this.mapSSet.keySet()) {
	        	 patientCount++;
	        	 HashMap<String, Integer> mapCateCount = new CategoryOfPatientAnalysis(this.mapSSet.get(key)).anlysis();
	        	 writer.write(patientCount + " " + this.mapSSet.get(key).getSSet().get(0).getSet().get(0).getPatientId());
	        	 for(String skey : mapCateCount.keySet()) {
	        		 writer.write(" " + mapCateCount.get(skey).toString());
	        	 }
	        	 writer.write("\n");
	         }
	         writer.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
	}
}
