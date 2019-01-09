package tinyccompiler.inter;

import tinyccompiler.lexer.Token;

/**
 * @author sin
 * not运算
 * 用！表示
 * 不支持"not"关键字
 */
public class Not extends Logical {
	public Not(Token tok, Expr x2) {
		super(tok,x2, x2);
	}
	public void jumping(int t,int f) {
		expr2.jumping(f,t);
	}
	public String toString() {
		return op.toString()+" "+expr2.toString();
	}
}
