package tinyccompiler.inter;
import java.io.IOException;

import tinyccompiler.global.Global;
import tinyccompiler.lexer.Lexer;


/**
 * @author sin
 * 生成中间代码
 */
public class Node {
	int lexline=0;
	Node(){lexline=Lexer.line;};
	void error(String s)
	{
		throw new Error("near line"+lexline+": "+s);
	}
	static int labels =0;
	public int newlabel(){return ++labels;}
	public void emitlabel(int i)
	{
		System.out.println("L"+i+":");
		try {
			Global.interCodeFw.write("L"+i+":"+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void emit(String s)
	{
		System.out.println("\t"+s);
		try {
			Global.interCodeFw.write(s+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
