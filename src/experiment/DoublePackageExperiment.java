package experiment;

import java.util.ArrayList;
import java.util.HashMap;

import basic.*;
import preprocess.CatePackaging;
import preprocess.ItemSet;
import preprocess.PreProcess;
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

        // set map [1-lv].package
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

        /** exp: single packaging **/
        SinglePackage(nameMap, mapKey2PackSet);

        /** exp: double packaging **/
        DoublePackage(nameMap, mapKey2PackSet);
    }

    // exp: package items within each category first
    public static void DoublePackage(HashMap<String, String> nameMap, HashMap<String, PackSet> mapKey2PackSet) {

    }

    // exp: package items by day
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

        // minging
        DataInput din = new DataInput(nameMap);
        DataReduction dr = new DataReduction(din);

        // reduce on whole
        dr.removingItemsRepeatDailyOnWhole(0.5, 0.05);
        din.removeItemsOnWhole(dr.getItemToRemoveOnWhole());

        dr.getImportantItems(20, 0.98);
        din.markImportantItem(dr.getImportantItem());
        din.MapSSet2csv("data/experiment/experiment3/single_packaging.csv");
    }
}
