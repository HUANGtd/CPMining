package experiment;

import java.util.ArrayList;
import java.util.HashMap;

import basic.*;
import mining.CatePackaging;
import preprocess.*;
import mining.*;
import io.DataInput;

public class DoublePackageExperiment {

    public static void main(String[] args) {
        HashMap<String, String> nameMap = null;
        HashMap<String, PackSet> mapKey2PackSet = new HashMap<String, PackSet>();

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

        /** experiment: single packaging **/
//        SinglePackage(nameMap, mapKey2PackSet);

        /** experiment: double packaging **/
        DoublePackage(nameMap, mapKey2PackSet);
    }

    // experiment: package items within each category first
    public static void DoublePackage(HashMap<String, String> nameMap, HashMap<String, PackSet> mapKey2PackSet) {
        HashMap<String, PackSSet> mapKey2PackSset = new HashMap<String, PackSSet>();
        HashMap<String, PackSSSet> mapKey2PackSsset = new HashMap<String,PackSSSet>();
        double support_inCate = 0.8;
        double support_onWhole = 0.8;

        /** packaging inner 14 categories **/
        DataInput doubleDin_inCate = new DataInput(nameMap);
        HashMap<String, String> mapAllItem2package = new HashMap<String, String>(); // put all categories together
        HashMap<String, CItemSSet> mapSSet_inCate = doubleDin_inCate.DataInputByCategoryDaily("data/patientData.txt");
        for(String key : mapSSet_inCate.keySet()) {
            CItemSSet sset_inCate = mapSSet_inCate.get(key);
            sset_inCate.SortSSet();
            CatePackaging cp_inCate = new CatePackaging(sset_inCate.getSSet());
            cp_inCate.Packaging(support_inCate);

            // replace items with package
            HashMap<String, Boolean> cpMark = cp_inCate.getMarkMap();
            HashMap<String, String> cpMap = cp_inCate.getItem2keyMap();
            for(String kk : cpMap.keySet()) {
                String oldName = cpMap.get(kk);
                if(cpMark.get(oldName)) {
                    String newName = new String(oldName/* + "<I>"*/);
                    cpMap.put(kk, newName);
                }
            }
            mapAllItem2package.putAll(cpMap);

            // set PackSSet
            HashMap<String, CateSet> mapCateSet = cp_inCate.getName2Cateset();
            for(String ky : mapCateSet.keySet()) {
                PackSSet pss = new PackSSet(ky, key);
                for(String s : mapCateSet.get(ky).getCateSet()) {
                    pss.addSet(mapKey2PackSet.get(s));
                }
                mapKey2PackSset.put(ky, pss);
            }
        }
        for(String key : nameMap.keySet()) {
            String previousKey = nameMap.get(key);
            String newKey = new String(mapAllItem2package.get(previousKey));
            nameMap.put(key, newKey);
        }

        /** packaging on whole **/
        DataInput doubleDin_onWhole = new DataInput(nameMap);
        HashMap<String, CItemSSet> mapSSet_onWhole = doubleDin_onWhole.DataInputByPatient("data/patientData.txt");
        CItemSSet sset_onWhole = doubleDin_onWhole.putPatientInASet();
        sset_onWhole.SortSSet();
        CatePackaging cp_onWhole = new CatePackaging(sset_onWhole.getSSet());
        cp_onWhole.Packaging(support_onWhole);

        // replace items with package
        HashMap<String, Boolean> cpMark_pack = cp_onWhole.getMarkMap();
        HashMap<String, String> cpMap_pack = cp_onWhole.getItem2keyMap();
        for(String kkk : cpMap_pack.keySet()) {
            String oldName2 = cpMap_pack.get(kkk);
            if(cpMark_pack.get(oldName2)) {
                String newName2 = new String(oldName2/* + "<II>"*/);
                cpMap_pack.put(kkk, newName2);
            }
        }
        for(String key : nameMap.keySet()) {
            String previousKey2 = nameMap.get(key);
            String newKey2 = new String(cpMap_pack.get(previousKey2));
            nameMap.put(key, newKey2);
        }

        // set PackSSSet
        HashMap<String, CateSet> mapCateSet_pack = cp_onWhole.getName2Cateset();
        for(String key : mapCateSet_pack.keySet()) {
            PackSSSet psss = new PackSSSet(key);
            for(String s : mapCateSet_pack.get(key).getCateSet()) {
                psss.addSSet(mapKey2PackSset.get(s));
            }
            mapKey2PackSsset.put(key, psss);
        }

        // output mapKey2Psss
		printMapKey2Psss(mapKey2PackSsset, nameMap);

        // post process
        PostProcess(nameMap, "data/experiment/experiment3/double_packaging.csv");
    }

    // experiment: package items by day
    public static void SinglePackage(HashMap<String, String> nameMap, HashMap<String, PackSet> mapKey2PackSet) {
        DataInput singleDin = new DataInput(nameMap);
        HashMap<String, PackSSet> mapKey2PackSset = new HashMap<String, PackSSet>();
        double support = 0.8;

        HashMap<String, CItemSSet> mapSSet = singleDin.DataInputByPatient("data/patientData.txt");
        CItemSSet csset = singleDin.putPatientInASet();
        csset.SortSSet();
        CatePackaging singlePack = new CatePackaging(csset.getSSet());
        singlePack.Packaging(support);

        // replace items with package-name
        HashMap<String, Boolean> cpMark_pack = singlePack.getMarkMap();
        HashMap<String, String> cpMap_pack = singlePack.getItem2keyMap();
        for(String kkk : cpMap_pack.keySet()) {
            String oldName2 = cpMap_pack.get(kkk);
            if(cpMark_pack.get(oldName2)) {
                String newName2 = new String(oldName2);
                cpMap_pack.put(kkk, newName2);
            }
        }
        for(String key : nameMap.keySet()) {
            String previousKey2 = nameMap.get(key);
            String newKey2 = new String(cpMap_pack.get(previousKey2));
            nameMap.put(key, newKey2);
        }

        // set map
        HashMap<String, CateSet> mapCateSet = singlePack.getName2Cateset();
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
        PostProcess(nameMap, "data/experiment/experiment3/single_packaging.csv");
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

    public static void printMapKey2Psss(HashMap<String, PackSSSet> mapKey2Psss, HashMap<String, String> nameMap) {
        // counting number
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
}
