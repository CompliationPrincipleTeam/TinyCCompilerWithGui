package tinyccompiler.inter;

import tinyccompiler.lexer.Token;
import tinyccompiler.symbols.Type;

/*
 * 表达式化归为中间代码
 */
public class Expr extends Node {
	public Token op;
	public Type type;
	Expr(Token tok,Type p){
		op=tok;
		type=p;
	}
	public Expr gen(){
		return this;
	}
	public Expr reduce(){
		return this;
	}
	public void jumping(int t,int f){
		emitjumps(toString(),t,f);
	}
	public void emitjumps(String test,int t,int f){
		if(t!=0&&f!=0){
			emit("if "+test+" goto L"+t);
			emit(" goto L"+f);
		}
		else if(t!=0)emit("if "+ "(" + test + ")"+" goto L"+t);
		else if(f!=0)emit("ifnot "+"(" + test + ")"+" goto L"+f);
		else ;
	}
	public String toString(){
		return op.toString();
	}
}
