package experiment;

import java.util.ArrayList;
import java.util.HashMap;

import basic.*;
import io.*;
import mining.FSetPackaging;
import mining.DataReduction;
import preprocess.*;

/**
 * Created by hhw on 3/1/16.
 */
public class FPMingExperiment {
    public static void main(String args[]) {
        HashMap<String, String> nameMap = null;
        HashMap<String, PackSet> mapKey2PackSet = new HashMap<String, PackSet>();
        HashMap<String, PackSSet> mapKey2PackSset = new HashMap<String, PackSSet>();

        /** preprocess: remove(package) items with the similar names **/
        DataInput pre1din = new DataInput();
        DataReduction dr = new DataReduction(pre1din);
        ArrayList<String> itemList = dr.getNoneRepeatItemList();
        PreProcess pp = new PreProcess(itemList);
        pp.genSetMap();
        nameMap = pp.getNameMap();

        // set map [preprocess].package
        HashMap<String, ItemSet> setList = pp.getSetList();

        for(String key : setList.keySet()) {
            if(setList.get(key).getPack().size() != 0) {
                PackSet ps = new PackSet(key);
                for(String s : setList.get(key).getPack()) {
                    ps.addItem(s);
                }
                mapKey2PackSet.put(key, ps);
            }
        }

        /** mine with apriori **/
        double ratio = 0.06;
        DataInput din_apriori = new DataInput(nameMap);
        DataReduction dr_apriori = new DataReduction(din_apriori);
        HashMap<String, CItemSSet> mapSSet = din_apriori.DataInputByPatient("data/patientData.txt");

        CItemSSet sset = din_apriori.putPatientInASet();
        sset.SortSSet();
        ArrayList<String> itemList_mine = dr_apriori.getNoneRepeatItemList();
        FSetPackaging cap_apriori = new FSetPackaging(itemList_mine, sset.getSSet(), ratio);
        cap_apriori.Apriori();
//      cap_apriori.printFPSetList();
//      cap_apriori.outputFPSetList("data/experiment/experiment2/list_on_patient_" + ratio + ".txt");

        /** packaging **/
        cap_apriori.packageFPSets();

        // replace items with package-name
        HashMap<String, String> cpMap_pack = cap_apriori.getItem2keyMap();
        for(String key : nameMap.keySet()) {
            String previousKey2 = nameMap.get(key);
            String newKey2 = new String(cpMap_pack.get(previousKey2));
            nameMap.put(key, newKey2);
        }

        // set map
        HashMap<String, CateSet> mapCateSet = cap_apriori.getMapName2Cateset();
        for(String key : mapCateSet.keySet()) {
            PackSSet pss = new PackSSet(key, "none");
            for(String s : mapCateSet.get(key).getCateSet()) {
                pss.addSet(mapKey2PackSet.get(s));
            }
            mapKey2PackSset.put(key, pss);
        }

        // print mapKey2PackSset
        printMapKey2Pss(mapKey2PackSset, nameMap);

        // post process
//        PostProcess(nameMap, "data/experiment/experiment2/apriori_packaging.csv");
    }

    /******* util *******/
    public static void PostProcess(HashMap<String, String> nameMap, String resultPath) {
        DataInput din = new DataInput(nameMap);
        DataReduction dr = new DataReduction(din);

        // reduce frequent packages on whole
        dr.removingItemsRepeatDailyOnWhole(0.5, 0.05);
        din.removeItemsOnWhole(dr.getItemToRemoveOnWhole());

        // select important packages
        dr.markImportantItems(20, 0.98);
        din.markImportantItem(dr.getImportantItem());

        // output to .csv file
        din.MapSSet2csv(resultPath);
    }

    public static void printMapKey2Pss(HashMap<String, PackSSet> mapKey2Pss, HashMap<String, String> nameMap) {
        // counting number
        int totalDays = 0;
        DataInput din = new DataInput(nameMap);
        HashMap<String, Integer> packTimebyday = new HashMap<String, Integer>();
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
            }
        }

        // printing
        int pssCount = 0;
        for(String key : mapKey2Pss.keySet()) {
            if((double)packTimebyday.get(key)/totalDays > 0.05) {
                pssCount++;
                System.out.print("#" + pssCount + "(" + packTimebyday.get(key) + "/" + totalDays + "): ");
                mapKey2Pss.get(key).printMapKey2PackSet(0, pssCount);
                System.out.print("\n");
            }
        }
    }

}
