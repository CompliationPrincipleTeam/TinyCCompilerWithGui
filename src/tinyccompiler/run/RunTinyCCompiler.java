package tinyccompiler.run;
import java.awt.Component;
import java.io.FileWriter;
import java.io.IOException;

import com.sin.notepad.global.CodeType;
import com.sin.notepad.view.HighlightFrame;

import tinyccompiler.assembler.Assembler;
import tinyccompiler.global.Global;
import tinyccompiler.lexer.Lexer;
import tinyccompiler.parser.Parser;
import tinyccompiler.preprocessing.Preprocessing;


public class RunTinyCCompiler {
	private static final String srcFloder = "TinyCCompiler/TinyCCode/";
	private static final String preFloder = "TinyCCompiler/PreCode/";
	private static final String interFloder = "TinyCCompiler/interCode/";
	private static final String asmFloder = "TinyCCompiler/AsmCode/";

	private static final String[] codeName = {"bubble sort", "select sort", 
											  "sum",         "swap", 
											  "insert sort", "shell sort",
											  "test"};
	private static final String extensionName = ".txt";
	
	/*
	 * ���룺TinyCCode�ļ����µ��ļ�
	 * ���0��PreCode�ļ�����Ԥ����֮���Դ���룬��ȥ��ע�͵ȴ���
	 * ���1��interCode�ļ�����������м���룬����̨Ҳ�����
	 * ���2��AsmCode�ļ����������x86�ܹ��Ļ����룬����̨Ҳ�����
	 * �ļ�������һһ��Ӧ��
	 * 
	 * ��������Լ���Ƶ�Tiny-C���룿
	 * 1. ����Tiny��C���﷨��ʽ������д��txt�ı�������TinyCCode�ļ���
	 * 2. �� codeName ���飬��������ļ���
	 * 3. �ı� main ������index��ֵ
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * �ı�index��ֵ��ѡ���Ӧ�Ĵ����ļ���index=4������ʾѡ��insert sort.txt
		 */
		int index = 0;
		
		String src = srcFloder+codeName[index]+extensionName;
		String pre = preFloder+codeName[index]+"_pre"+extensionName;
		String inter = interFloder+codeName[index]+"_inter"+extensionName;
		String asm = asmFloder+codeName[index]+"_asm"+extensionName;
		
		//Ԥ����
		Preprocessing.Preprocess(src,pre);
		
		//��ʼ��
		Global.init(pre,inter,"TinyCCompiler/stack.txt");
		//Global.stackFw.write("���	����ջ		����\n");
		//ǰ��
		Lexer lex=new Lexer(Global.srcFr);
		Parser parse=new Parser(lex,Global.stackFw);
		parse.program();
		//System.out.println(lex.scan().toString());
		Global.close();
		//���
		System.out.println("-----------------------------------------------");
		Assembler assembler = new Assembler(inter,asm);
		assembler.assemble();
		assembler.asmCodeSave();
	}

	/**
	 * run���ṩ��Mini Notepad++�Ľӿ�
	 * @throws IOException 
	 */
	public static void run(final Component parent,String tinyCCode) throws IOException 
	{
		String src = srcFloder+"buffer"+extensionName;
		String pre = preFloder+"buffer"+"_pre"+extensionName;
		String inter = interFloder+"buffer"+"_inter"+extensionName;
		String asm = asmFloder+"buffer"+"_asm"+extensionName;
		
		//д��TinyCodeԴ�ļ�
		write(tinyCCode, src);
		
		//Ԥ����
		Preprocessing.Preprocess(src,pre);
		
		//��ʼ��
		Global.init(pre,inter,"TinyCCompiler/stack.txt");
		//Global.stackFw.write("���	����ջ		����\n");
		//ǰ��
		Lexer lex=new Lexer(Global.srcFr);
		Parser parse=new Parser(lex,Global.stackFw);
		parse.program();
		//System.out.println(lex.scan().toString());
		Global.close();
		//���
		System.out.println("-----------------------------------------------");
		Assembler assembler = new Assembler(inter,asm);
		assembler.assemble();
		assembler.asmCodeSave();
		
		HighlightFrame interCodeFrame = new HighlightFrame(parent, "�м����",CodeType.TinyC);
		interCodeFrame.getJTextPane().setText(assembler.getInterCodeString());
		
		HighlightFrame asmCodeFrame = new HighlightFrame(parent, "������", CodeType.TinyC);
		asmCodeFrame.getJTextPane().setText(assembler.getAsmCodeString());
		
	}
	
	public static void write(String tinyCCode,String src) throws IOException
	{

		FileWriter fw = new FileWriter(src);
		fw.write(tinyCCode);
		fw.flush();
		fw.close();
	}
}
