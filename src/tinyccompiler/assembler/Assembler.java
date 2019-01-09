package tinyccompiler.assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import tinyccompiler.global.AsmCodeType;

/*
 * 
 */
public class Assembler {
	private static final String arrayPattern = "[a-z]+[0-9]*\\[[a-z]+[0-9]*\\]";
	private ArrayList<String> interCodeList = new ArrayList<>();
	private ArrayList<AsmCode> asmCodeList = new ArrayList<>();
	private String interCodePath = "";
	private String asmCodePath = "";
	private FileWriter asmFw = null;
	
	
	public Assembler(String interCodePath,String asmCodePath) throws IOException
	{
		this.interCodePath = interCodePath;
		this.asmCodePath = asmCodePath;
		initinterCodeList(interCodePath);
		asmFw = new FileWriter(new File(asmCodePath));
//		assemble();
//		for(AsmCode ins: asmCodeList)
//		{
//			if(ins.type == AsmCodeType.Label)
//				System.out.println(ins.toString());
//			else {
//				System.out.println("   "+ins.toString());
//			}
//		}
	}
	
	private void initinterCodeList(String path) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String interCode = null;
		while((interCode=br.readLine())!=null)
		{
			interCodeList.add(interCode);
		}
		br.close();
	}
	
	public void assemble()
	{
		for(String interCode: interCodeList)
		{
			assmbleCode(interCode);
		}
	}
	
	//一次处理一个中间代码
	private void assmbleCode(String interCode)
	{
		if(isLabel(interCode))
		{
			asmCodeList.add(new AsmCode(interCode));
			return;
		}
		
		if(match(interCode, "ifnot"))
		{
			int left = interCode.indexOf("(");
			int right = interCode.indexOf(")");
			//提取condition
			String condition = interCode.substring(left+1, right);
			String[] s = condition.split("\\s+");
			String op1 = s[0];
			String op2 = s[2];
			String type = s[1];
			
			//去除condition
			interCode = interCode.replace(" ("+condition+") ", " ");
			
			//cmp指令
			generate("mov", "eax", op1);
			generate("mov", "ebx", op2);
			generate("cmp", "eax", "ebx");
			
			//获取跳转label
			String label = interCode.split("\\s+")[2];
			
			generate("j"+negLogic(type), label, null);
			
		}
		else if(match(interCode, "if"))
		{
			throw new IllegalArgumentException("暂未实现if");
		}
		else if(match(interCode,"goto"))
		{
			String label = interCode.split("\\s+")[1];
			generate("jmp", label,null);
		}
		else if (match(interCode, " = "))
		{
			/*
			 * TODO: 完善src中涉及的四则运算
			 * 完善寄存器分配
			 */
			String[] strings = interCode.split(" = ");
			String dst = strings[0];
			String src = strings[1];

			if(match(src, " + "))
			{
				String[] strings2 = src.split(" \\+ ");
				generate("mov", "eax", strings2[0]);
				generate("add", "eax", strings2[1]);
				//gen("mov", dst, "eax");
			}
			else if(match(src," - "))
			{
				String[] strings2 = src.split(" - ");
				generate("mov", "eax", strings2[0]);
				generate("sub", "eax", strings2[1]);
				//gen("mov", dst, "eax");
			}
			else if(match(src, " * "))
			{
				String[] strings2 = src.split(" \\* ");
				generate("mov", "eax", strings2[0]);
				generate("mov", "ebx", strings2[1]);
				generate("imul", "ebx", null);
				//gen("mov", dst, "eax");
			}
			else if(match(src, " / "))
			{
				String[] strings2 = src.split(" / ");
				generate("mov", "eax", strings2[0]);
				generate("mov", "ebx", strings2[1]);
				generate("mov", "edx", "0");
				generate("idiv", "ebx",null);
				//gen("mov", dst, "eax");
			}
			else if(regexMatch(src,arrayPattern))
			{
				String[] res = arrayAddressSplit(src);
				String arrayName = res[0];
				String arrayIndex = res[1];
				generate("lea", "ebx", "["+arrayName+"]");
				generate("mov", "esi", arrayIndex);
				generate("mov", "eax", "[ebx+esi]");
				
			}
			else 
			{
				generate("mov", "eax", src);
				//gen("mov", dst, "eax");
			}
			
			//src运算结果在eax
			//如果dst是数组a[ti]形式
			if(regexMatch(dst, arrayPattern))
			{
				String[] res = arrayAddressSplit(dst);
				String arrayName = res[0];
				String arrayIndex = res[1];
				generate("lea", "ebx", "["+arrayName+"]");
				generate("mov", "esi", arrayIndex);
				dst = "[ebx+esi]";
//				System.out.println("match dst" + dst);
//				System.out.println(dst);
			}
			generate("mov", dst, "eax");
			
		}
	}
	
	private boolean match(String parent,String sub)
	{
		return parent.indexOf(sub)!=-1;
	}
	
	private boolean regexMatch(String dst ,String pattern)
	{
		if(Pattern.matches(pattern, dst))
		{
			//System.out.println("match"+dst);
			return true;
		}
		return false;
	}
	
	
	private boolean isLabel(String s) {
		return s.charAt(0)=='L' && s.charAt(s.length()-1)==':';
	}
	
	private String negLogic(String logic)
	{
		if(logic.equals("<"))
			return "ge";
		else if(logic.equals(">"))
			return "le";
		else if(logic.equals(">="))
			return "l";
		else if(logic.equals("<="))
			return "g";
		else {
			throw new IllegalArgumentException("不明逻辑运算" + logic);
		}
	}
	/*
	 * res[0]是数组名
	 * res[1]是索引
	 */
	private String[] arrayAddressSplit(String arrayOp)
	{
		int left = arrayOp.indexOf('[');
		int right = arrayOp.lastIndexOf(']');
		String index = arrayOp.substring(left+1, right);
		String arrayName = arrayOp.substring(0,left);
		String[] res = {arrayName,index};
		return res;
	}
	
	private void generate(String instruction, String op1, String op2)
	{
		if(op2!=null && op2.equals("")==false)
			asmCodeList.add(new AsmCode(instruction, op1, op2));
		else
			asmCodeList.add(new AsmCode(instruction, op1));
	}
	
	public void asmCodeSave() throws IOException
	{
		for(AsmCode ins: asmCodeList)
		{
			if(ins.type == AsmCodeType.Label)
			{
				System.out.println(ins.toString());
				asmFw.write(ins.toString()+"\n");
				
			}
			else {
				System.out.println("   "+ins.toString());
				asmFw.write("   "+ins.toString()+"\n");
				
			}
		}
		asmFw.flush();
		asmFw.close();
	}
	
	private String list2String(ArrayList<String> list)
	{
		String string="";
		for(String s:list)
			string+=s+"\n";
		return string;
	}
	
	public String getAsmCodeString()
	{
		String res = "";
		for(AsmCode ins:asmCodeList)
		{
			if(ins.type == AsmCodeType.Label)
			{
				res+=ins.toString()+"\n";
				
			}
			else {
				res+="   "+ins.toString()+"\n";
				
			}
		}
		return res;
	}
	
	public String getInterCodeString() {
		return list2String(interCodeList);
	}
	
	public static void main(String[] args) throws IOException
	{
		Assembler assembler = new Assembler("interCode/bubble sort_inter.txt","AsmCode/bubble sort_asm.txt");
		assembler.assemble();
		assembler.asmCodeSave();
	}
}
