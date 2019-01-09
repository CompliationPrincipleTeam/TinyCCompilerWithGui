package tinyccompiler.inter;

import tinyccompiler.lexer.Word;
import tinyccompiler.symbols.Type;

/**
 * @author sin
 * ������ʱ����ti
 * ���ڱ��ʽ��ֵ
 */
public class Temp extends Expr {
	static int count=0;
	int number=0;
	public Temp(Type p) {
		super(Word.temp,p);
		number=++count;
	}
	public String toString() {
		return "t"+number;
	}
}
