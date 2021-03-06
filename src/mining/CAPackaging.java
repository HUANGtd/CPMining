package mining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import basic.*;

public class CAPackaging {
	private ArrayList<CItemSet> arrD = null;
	private double dMinSup = 0.0;
	private int sequenceNum = 0;
	private ArrayList<FPSet> listFPSet = null; 
	
	public CAPackaging(ArrayList<CItemSet> D, double minsup) {
		this.arrD = D;
		this.dMinSup = minsup;
		this.sequenceNum = D.size();
		this.listFPSet = new ArrayList<FPSet>();
	}
	
	/****************** apriori *****************/
	public void Apriori(ArrayList<String> itemList) {
		FPSet L1 = findF1Itemsets(itemList);
		this.listFPSet.add(L1); // listFPSet[0] = L1, listFPSet[k-1] = Lk
		printFPSetList();
		for(int k = 2; ; k++) {
			if(this.listFPSet.get(k-2).getSequenceSet().size() == 0) {
				break; // ***** 
			}
			FPSet Ck = AprioriJoin(this.listFPSet.get(k-2));
			// Lk={c��Ck | c.count �� min_sup}
			FPSet Lk = new FPSet(k);
			for(FPSequence fpseq : Ck.getSequenceSet()) {
				double support = getSupport(fpseq);
				fpseq.setSupport(support);
				if(support > this.dMinSup) {
					Lk.addSequence(fpseq);
				}
			}
			this.listFPSet.add(Lk);
			Lk.printFPSet();
		}
	}
	
	public FPSet AprioriJoin(FPSet Lkminus1) {
		int k = Lkminus1.getLength();
		FPSet Ck = new FPSet(k+1);
		for(int i = 0; i < Lkminus1.getSequenceSet().size(); i++) {
			FPSequence fpseq1 = Lkminus1.getSequenceSet().get(i);
			for(int j = i+1; j < Lkminus1.getSequenceSet().size(); j++) {
				FPSequence fpseq2 = Lkminus1.getSequenceSet().get(j);
				if(CanJoinOrNot(fpseq1, fpseq2, k)) {
					FPSequence nfpseq = new FPSequence(k+1);
					for(int m = 0; m < k; m++) {
						nfpseq.fillItem(m, fpseq1.getSequence()[m]);
					}
					nfpseq.fillItem(k, fpseq2.getSequence()[k-1]);
					if(!hasInfrequentSubSequence(nfpseq, Lkminus1, k)) {
						Ck.addSequence(nfpseq);
					}
				}
			}
		}
		
		return Ck;
	}
	
	public Boolean hasInfrequentSubSequence(FPSequence c, FPSet Lkminus1, int k) {
		for(int i = 0; i < k+1; i++) {
			FPSequence nfpseq = new FPSequence(k);
			int index = 0;
			for(int j = 0; j < k+1; j++) {
				if(j == i) {
					continue;
				}
				nfpseq.fillItem(index, c.getSequence()[j]);
				index++;
			}
			if(!Lkminus1.hasSequence(nfpseq)) {
				return true;
			}
		}
		
		return false;
	}
	
	// (l1[1]=l2[1])��...��(l1[k-2]=l2[k-2])��(l1[k-1]<l2[k-1])
	public Boolean CanJoinOrNot(FPSequence fpseq1, FPSequence fpseq2, int k) {
		for(int i = 0; i < k-1; i++) {
			if(!fpseq1.getSequence()[i].equals(fpseq2.getSequence()[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	public FPSet findF1Itemsets(ArrayList<String> itemList) {
		FPSet f1set= new FPSet(1);
		for(String key : itemList) {
			FPItem fpi = new FPItem(key);
			FPSequence fpseq = new FPSequence(1);
			fpseq.fillItem(0, fpi);
			double sup = getSupport(fpseq);
			fpseq.setSupport(sup);
			if(sup > this.dMinSup) {
				f1set.addSequence(fpseq);
			}
		}
		
		return f1set;
	}
	
	public double getSupport(FPSequence seq) {
		int appearTime = 0;
		for(CItemSet cSet : this.arrD) {
//			if(cSet.hasSequence(seq)) {
//				appearTime++;
//			}
			if(cSet.containSequence(seq)) {
				appearTime++;
			}
		}
		
		return (double)appearTime/this.sequenceNum;
	}
	
	/****************** output *****************/
	public void printFPSetList() {
		for(FPSet fpset : this.listFPSet) {
			if(fpset.getSequenceSet().size() != 0) {
				fpset.printFPSet();
			System.out.println();
			}
		}
	}
	
	public void outputFPSetList(String outFile) {
		try {
			File file = new File(outFile);
	        if(!file.exists())
	            file.createNewFile();
	        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	        for(FPSet fpset : this.listFPSet) {
				if(fpset.getSequenceSet().size() != 0) {
					fpset.outputFPSet(writer);
					writer.write("\n");
				}
			}
	        writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
