package experiment;

import java.util.ArrayList;
import java.util.HashMap;

import basic.*;
import io.*;
import mining.CAPackaging;
import mining.DataReduction;
import preprocess.ItemSet;
import preprocess.PreProcess;

/**
 * Created by hhw on 3/1/16.
 */
public class FPMingExperiment {
    public static void main(String args[]) {
        HashMap<String, String> nameMap = null;

        /** preprocess: remove(package) items with the similar names **/
        DataInput pre1din = new DataInput();
        DataReduction dr = new DataReduction(pre1din);
        ArrayList<String> itemList = dr.getNoneRepeatItemList();
        PreProcess pp = new PreProcess(itemList);
        pp.genSetMap();
        nameMap = pp.getNameMap();

        /** mine **/
        double ratio = 0.08;
        DataInput din_apriori = new DataInput(nameMap);
        DataReduction dr_apriori = new DataReduction(pre1din);
        HashMap<String, CItemSSet> mapSSet = din_apriori.DataInputByPatient("data/patientData.txt");
        CItemSSet sset = din_apriori.putPatientInASet();
        sset.SortSSet();
        ArrayList<String> itemList_mine = dr_apriori.getNoneRepeatItemList();
        CAPackaging cap_apriori = new CAPackaging(sset.getSSet(), ratio);
        cap_apriori.Apriori(itemList_mine);
        cap_apriori.printFPSetList();
        cap_apriori.outputFPSetList("data/experiment/experiment2/list_on_patient_" + ratio + ".txt");
    }
}
