package tinyccompiler.inter;

import tinyccompiler.lexer.Token;
import tinyccompiler.symbols.Type;


/**
 * @author sin
 * ��ֵ���ʽ
 */
public class Op extends Expr {
	public Op(Token tok,Type p) {
		super(tok,p);
	}
	public Expr reduce() {
		Expr x=gen();
		Temp t=new Temp(type);
		emit(t.toString()+" = "+x.toString());
		return t;
	}
}
