package com.sin.notepad.view;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.sin.notepad.global.CodeType;

/**
 * 当文本输入区的有字符插入或者删除时, 进行高亮.
 * 
 * 要进行语法高亮, 文本输入组件的document要是styled document才行. 所以不要用JTextArea. 可以使用JTextPane.
 * 
 * 
 */
class SyntaxHighlighter implements DocumentListener {
	private final static String dir = "keywords/";
	private String controllerRed = null;
	private String dataTypeBlue = null;
	private String ClassMagenta = null;
	
	private Set<String> redWords;
	private Set<String> blueWords;
	private Set<String> magentaWords;
	private Style normalStyle;
	private Style controllerWordsStyle,dataTypeWordsStyle,classWordsStyle;
	private Style noteStyle;
	public SyntaxHighlighter(JTextPane editor, CodeType type) throws IOException {
		((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		controllerWordsStyle = ((StyledDocument) editor.getDocument()).addStyle("controllerWords_Style", null);
		dataTypeWordsStyle = ((StyledDocument) editor.getDocument()).addStyle("dataTypeWords_Style", null);
		classWordsStyle = ((StyledDocument) editor.getDocument()).addStyle("classWords_Style", null);
		normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		noteStyle = ((StyledDocument) editor.getDocument()).addStyle("note_Style", null);
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		StyleConstants.setForeground(controllerWordsStyle, Color.RED);
		StyleConstants.setForeground(dataTypeWordsStyle, Color.BLUE);
		StyleConstants.setForeground(classWordsStyle, Color.MAGENTA);
		StyleConstants.setForeground(noteStyle, Color.GREEN);

		redWords = new HashSet<String>();
		blueWords = new HashSet<String>();
		magentaWords = new HashSet<String>();
		
		if(type == null)
		{
			//do nothing
			controllerRed = dataTypeBlue = ClassMagenta = null;
		}
		if(type == CodeType.Java)
		{
			controllerRed = dir + "Keywords-Java-Controller.txt";
			dataTypeBlue = dir + "Keywords-Java-DataType.txt";
			ClassMagenta = dir + "Keywords-Java-Class.txt";
		}
		else if(type == CodeType.C || type == CodeType.Cplusplus)
		{
			controllerRed = dir + "Keywords-Cplusplus-Controller.txt";
			dataTypeBlue = dir + "Keywords-Cplusplus-DataType.txt";
			ClassMagenta = dir + "Keywords-Cplusplus-Class.txt";
		}
		else if(type == CodeType.Python)
		{
			controllerRed = dataTypeBlue = ClassMagenta = dir + "Keywords-Python.txt";
		}
		else if(type == CodeType.TinyC)
		{
			controllerRed = dir + "TinyCCompiler-Regsiter.txt";
			dataTypeBlue = dir + "TinyCCompiler-Instructions.txt";
			ClassMagenta = dir + "TinyCCompiler-InterCode.txt";
		}
		else 
		{
			controllerRed = dataTypeBlue = ClassMagenta = null;
		}
		
		initKeywordsSet();
	}
	
	public void initKeywordsSet() throws IOException
	{
		if(controllerRed!=null)
			this.readKeywordsFile(controllerRed, redWords);
		if(dataTypeBlue!=null)
			this.readKeywordsFile(dataTypeBlue, blueWords);
		if(ClassMagenta!=null)
			this.readKeywordsFile(ClassMagenta, magentaWords);
	}
	
	public void readKeywordsFile(String file, Set<String> set) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s = "";
		while((s=br.readLine())!=null)
		{
			set.add(s);
			System.out.println(s);
		}
		br.close();
	}

	
	public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
		// 取得插入或者删除后影响到的单词.
		// 例如"public"在b后插入一个空格, 就变成了:"pub lic", 这时就有两个单词要处理:"pub"和"lic"
		// 这时要取得的范围是pub中p前面的位置和lic中c后面的位置
		int start = indexOfWordStart(doc, pos);
		int end = indexOfWordEnd(doc, pos + len);
		//System.out.println(start + " " + end);
		char ch;
		while (start < end) {
			ch = getCharAt(doc, start);
			if (Character.isLetter(ch) || ch == '_') 
			{
				// 如果是以字母或者下划线开头, 说明是单词
				// pos为处理后的最后一个下标
				start = colouringWord(doc, start);
			} 
			else 
			{
				SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
				++start;
			}
		}
	}

	/**
	 * 对单词进行着色, 并返回单词结束的下标.
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		int wordEnd = indexOfWordEnd(doc, pos);
		String word = doc.getText(pos, wordEnd - pos);
		//System.out.println(word);
		if(redWords.contains(word))
		{
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, controllerWordsStyle));
		}
		else if(blueWords.contains(word))
		{
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, dataTypeWordsStyle));
		}
		else if(magentaWords.contains(word))
		{
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, classWordsStyle));
		}
		else 
		{
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
		}
			

		return wordEnd;
	}

	/**
	 * 取得在文档中下标在pos处的字符.
	 * 
	 * 如果pos为doc.getLength(), 返回的是一个文档的结束符, 不会抛出异常. 如果pos<0, 则会抛出异常.
	 * 所以pos的有效值是[0, doc.getLength()]
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public char getCharAt(Document doc, int pos) throws BadLocationException {
		return doc.getText(pos, 1).charAt(0);
	}

	/**
	 * 取得下标为pos时, 它所在的单词开始的下标. ?±wor^d?± (^表示pos, ?±表示开始或结束的下标)
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
		// 从pos开始向前找到第一个非单词字符.
		for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);

		return pos;
	}

	/**
	 * 取得下标为pos时, 它所在的单词结束的下标. ?±wor^d?± (^表示pos, ?±表示开始或结束的下标)
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
		// 从pos开始向前找到第一个非单词字符.
		for (; isWordCharacter(doc, pos); ++pos);

		return pos;
	}
	

	/**
	 * 如果一个字符是字母, 数字, 下划线, 则返回true.
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {
		char ch = getCharAt(doc, pos);
		if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') { return true; }
		return false;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		try {
			colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		try {
			// 因为删除后光标紧接着影响的单词两边, 所以长度就不需要了
			colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 完成着色任务
	 * 
	 * 
	 */
	private class ColouringTask implements Runnable {
		private StyledDocument doc;
		private Style style;
		private int pos;
		private int len;

		public ColouringTask(StyledDocument doc, int pos, int len, Style style) {
			this.doc = doc;
			this.pos = pos;
			this.len = len;
			this.style = style;
		}

		public void run() {
			try {
				// 这里就是对字符进行着色
				doc.setCharacterAttributes(pos, len, style, true);
			} catch (Exception e) {}
		}
	}
}
