package tinyccompiler.inter;

import tinyccompiler.symbols.Type;

/**
 * @author sin
 * ʵ������Ϊ��ʶ�����Ҳ�Ϊһ�����ʽ�ĸ�ֵ���
 */
public class Set extends Stmt {
	public Expr expr;
	public Id id;
	public Set(Id i,Expr x) {
		expr=x;
		id=i;
		if(check(id.type,expr.type)==null)error("type error");
	}
	public Type check(Type p1,Type p2) {
		if(Type.numeric(p1)&&Type.numeric(p2))return p2;
		else if(p1==Type.Bool&&p2==Type.Bool)return p2;
		else return null;
	}
	public void gen(int b ,int a) {
		emit(id.toString()+" = "+expr.gen().toString());
	}
}
