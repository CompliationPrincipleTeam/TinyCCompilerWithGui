package tinyccompiler.assembler;

import tinyccompiler.global.AsmCodeType;

public class AsmCode {
	String instruction = "";
	String dst = "";
	String src = "";
	String label = "";
	AsmCodeType type = null;
	
	public AsmCode(String instrction, String dst, String src)
	{
		this.type = AsmCodeType.TwoOpAsmCode;
		this.instruction = instrction;
		this.dst = dst;
		this.src = src;
	}
	
	public AsmCode(String instrction, String dst)
	{
		this.type = AsmCodeType.OneOpAsmCode;
		this.instruction = instrction;
		this.dst = dst;
	}
	
	public AsmCode(String label) {
		this.label = label;
		this.type = AsmCodeType.Label;
	}
	
	public String toString() {
		if(type == AsmCodeType.Label)
			return label;
		else if(type == AsmCodeType.OneOpAsmCode)
			return instruction + " " + dst;
		else if(type == AsmCodeType.TwoOpAsmCode)
			return instruction + " " + dst + "," + src;
		else
		{
			throw new IllegalArgumentException(type +"类型无效");
		}
	}
}
