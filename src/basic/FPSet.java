package basic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FPSet {
	private int n = 0;// all the frequent patterns in this set are n-frequent patterns
	private ArrayList<FPSequence> listSequence = null;
	
	public FPSet(int n) {
		this.n = n;
		this.listSequence = new ArrayList<FPSequence>();
	}
	
	public ArrayList<FPSequence> getSequenceSet() {
		return this.listSequence;
	}
	
	public void addSequence(FPSequence fps) {
		this.listSequence.add(fps);
	}
	
	public int getLength() {
		return this.n;
	}
	
	public Boolean hasSequence(FPSequence fpseq) {
		for(FPSequence seq : this.listSequence) {
			if(seq.equalsSequence(fpseq.getSequence())) {
				return true;
			}
		}
		
		return false;
	}
	
	/****************** output *****************/
	public void printFPSet() {
		int num = 0;
		if(this.listSequence.size() == 0) {
			return;
		}
		System.out.print(n + "-Set:\n");
		System.out.print("{\n");
		for(FPSequence fpseq : listSequence) {
			num ++;
			System.out.print("     #" + num + ": ");
			fpseq.printFPSequence();
		}
		System.out.print("}\n");
	}
	
	public void outputFPSet(BufferedWriter writer) throws IOException {
		int num = 0;		
		if(this.listSequence.size() == 0) {
			return;
		}
		writer.write(n + "-Set:\n");
		writer.write("{\n");
		for(FPSequence fpseq : this.listSequence) {
			num ++;
			writer.write("     #" + num + ": ");
			fpseq.outputFPSequence(writer);
		}
		writer.write("}\n");
	}
}
