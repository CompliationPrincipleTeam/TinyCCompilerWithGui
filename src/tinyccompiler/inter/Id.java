package tinyccompiler.inter;
import tinyccompiler.lexer.Word;
import tinyccompiler.symbols.Type;

/**
 * @author sin
 * ��������
 */
public class Id extends Expr{
	public int offset;
	public Id(Word id,Type p,int b){
		super(id, p);
		offset=b;
	}
}
