package tinyccompiler.inter;

import tinyccompiler.lexer.Tag;
import tinyccompiler.lexer.Word;
import tinyccompiler.symbols.Type;
/*
 * ����Ԫ�ط���
 */
public class Access extends Op {
	public Id array;
	public Expr index;
	public Access(Id a,Expr i,Type p) {
		super(new Word("[]",Tag.INDEX),p);
		array=a;
		index=i;
	}
	public Expr gen() {
		return new Access(array,index.reduce(),type);
	}
	public void jumping(int t,int f) {
		emitjumps(reduce().toString(),t,f);
	}
	public String toString() {
		return array.toString()+"["+index.toString()+"]";
	}

}
