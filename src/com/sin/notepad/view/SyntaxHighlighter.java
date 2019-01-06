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
 * ���ı������������ַ��������ɾ��ʱ, ���и���.
 * 
 * Ҫ�����﷨����, �ı����������documentҪ��styled document����. ���Բ�Ҫ��JTextArea. ����ʹ��JTextPane.
 * 
 * 
 */
class SyntaxHighlighter implements DocumentListener {
	private final static String dir = "keywords/";
	private String controller = null;
	private String dataType = null;
	private String Class = null;
	
	private Set<String> controllerWords;
	private Set<String> dataTypeWords;
	private Set<String> classWords;
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

		controllerWords = new HashSet<String>();
		dataTypeWords = new HashSet<String>();
		classWords = new HashSet<String>();
		
		if(type == null)
		{
			//do nothing
			controller = dataType = Class = null;
		}
		if(type == CodeType.Java)
		{
			controller = dir + "Keywords-Java-Controller.txt";
			dataType = dir + "Keywords-Java-DataType.txt";
			Class = dir + "Keywords-Java-Class.txt";
		}
		else if(type == CodeType.C || type == CodeType.Cplusplus)
		{
			controller = dir + "Keywords-Cplusplus-Controller.txt";
			dataType = dir + "Keywords-Cplusplus-DataType.txt";
			Class = dir + "Keywords-Cplusplus-Class.txt";
		}
		else if(type == CodeType.Python)
		{
			controller = dataType = Class = dir + "Keywords-Python.txt";
		}
		else 
		{
			controller = dataType = Class = null;
		}
		
		initKeywordsSet();
	}
	
	public void initKeywordsSet() throws IOException
	{
		if(controller!=null)
			this.readKeywordsFile(controller, controllerWords);
		if(dataType!=null)
			this.readKeywordsFile(dataType, dataTypeWords);
		if(Class!=null)
			this.readKeywordsFile(Class, classWords);
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
		// ȡ�ò������ɾ����Ӱ�쵽�ĵ���.
		// ����"public"��b�����һ���ո�, �ͱ����:"pub lic", ��ʱ������������Ҫ����:"pub"��"lic"
		// ��ʱҪȡ�õķ�Χ��pub��pǰ���λ�ú�lic��c�����λ��
		int start = indexOfWordStart(doc, pos);
		int end = indexOfWordEnd(doc, pos + len);
		//System.out.println(start + " " + end);
		char ch;
		while (start < end) {
			ch = getCharAt(doc, start);
			if (Character.isLetter(ch) || ch == '_') 
			{
				// ���������ĸ�����»��߿�ͷ, ˵���ǵ���
				// posΪ���������һ���±�
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
	 * �Ե��ʽ�����ɫ, �����ص��ʽ������±�.
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
		if(controllerWords.contains(word))
		{
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, controllerWordsStyle));
		}
		else if(dataTypeWords.contains(word))
		{
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, dataTypeWordsStyle));
		}
		else if(classWords.contains(word))
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
	 * ȡ�����ĵ����±���pos�����ַ�.
	 * 
	 * ���posΪdoc.getLength(), ���ص���һ���ĵ��Ľ�����, �����׳��쳣. ���pos<0, ����׳��쳣.
	 * ����pos����Чֵ��[0, doc.getLength()]
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
	 * ȡ���±�Ϊposʱ, �����ڵĵ��ʿ�ʼ���±�. ?��wor^d?�� (^��ʾpos, ?����ʾ��ʼ��������±�)
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
		// ��pos��ʼ��ǰ�ҵ���һ���ǵ����ַ�.
		for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);

		return pos;
	}

	/**
	 * ȡ���±�Ϊposʱ, �����ڵĵ��ʽ������±�. ?��wor^d?�� (^��ʾpos, ?����ʾ��ʼ��������±�)
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
		// ��pos��ʼ��ǰ�ҵ���һ���ǵ����ַ�.
		for (; isWordCharacter(doc, pos); ++pos);

		return pos;
	}
	

	/**
	 * ���һ���ַ�����ĸ, ����, �»���, �򷵻�true.
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
			// ��Ϊɾ�����������Ӱ��ĵ�������, ���Գ��ȾͲ���Ҫ��
			colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * �����ɫ����
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
				// ������Ƕ��ַ�������ɫ
				doc.setCharacterAttributes(pos, len, style, true);
			} catch (Exception e) {}
		}
	}
}
