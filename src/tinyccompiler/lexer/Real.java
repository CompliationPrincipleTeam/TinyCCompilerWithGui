package tinyccompiler.lexer;

/**
 * @author sin
 *	处理实数
 */
public class Real extends Token {
	public final float value;
	public Real(float v){super(Tag.REAL);value=v;}
	public String toString (){return ""+value;}
}
