package basic;

import java.io.BufferedWriter;
import java.io.IOException;

public class FPSequence {
	private int intLength = 0;
	private FPItem[] sequence = null;
	private double dSupport = 0.0;
	
	public FPSequence(int length) {
		this.intLength = length;
		this.sequence = new FPItem[length];
	}
	
	public FPItem[] getSequence() {
		return this.sequence;
	}
	
	public void fillItem(int index, FPItem fpi) {
		this.sequence[index] = fpi;
	}
	
	public int getLength() {
		return this.intLength;
	}
	
	public void setSupport(double sup) {
		this.dSupport = sup;
	}
	
	public double getSupport() {
		return this.dSupport;
	}
	
	public Boolean equalsSequence(FPItem[] seq) {
		if(seq.length != this.intLength) {
			return false;
		}
		for(int i = 0; i < this.intLength; i++) {
			if(!this.sequence[i].equalsFPItem(seq[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	public void printFPSequence() {
		System.out.print("[" + this.sequence[0].getName());
		for(int i = 1; i < this.intLength; i++) {
			System.out.print(" -> " + this.sequence[i].getName());
		}
		System.out.print("]  ratio: " + this.dSupport + "\n");
	}
	
	public void outputFPSequence(BufferedWriter writer) throws IOException {
		writer.write("[" + this.sequence[0].getName());
		for(int i = 1; i < this.intLength; i++) {
			writer.write(" -> " + this.sequence[i].getName());
		}
		writer.write("]  ratio: " + this.dSupport + "\n");
	}
}
