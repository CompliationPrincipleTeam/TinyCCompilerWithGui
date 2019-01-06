package com.sin.notepad.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

//��һ��highlight���ӵ�text component�к�ÿ�θ��¸���ʱ������ȥ����highlighter������highlight��
//�������Ƕ�Ӧ��painter.paing()����,�����л���. ������������ֻ��Ҫ��һ��highlight, �Ϳ���һֱʹ��.
//������ÿ�θ���Ҫ��������ʱ�ͼ�һ���µ�highlight, ��Ϊ���ᶯ̬�ؼ��㵱ǰ�������и���.
public class CurrentLineHighlighter extends MouseAdapter 
									implements Highlighter.HighlightPainter,CaretListener 
									{
	private JTextComponent editor;
	private Color color = Color.cyan;
	private int previousLine;

	public CurrentLineHighlighter() {
	}

	public CurrentLineHighlighter(JTextComponent editor) {
		this(editor, null);
	}

	public CurrentLineHighlighter(JTextComponent editor, Color color) {
		installEditor(editor);
		setColor(color);
	}

	public void installEditor(JTextComponent editor) {
		this.editor = editor;
		editor.addCaretListener(this);
		editor.addMouseListener(this);
		editor.addMouseMotionListener(this);

		// Turn highlight on
		enableHighlight();
	}

	public void deinstallEditor() {
		editor.removeCaretListener(this);
		editor.removeMouseListener(this);
     	editor.removeMouseMotionListener(this);
     	editor = null;
	}

	public void enableHighlight() {
		// Turn highlight on
		try {
			editor.getHighlighter().addHighlight(0, 0, this);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		if (color == null) { return; }
		this.color = color;
	}

	@Override
	public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent com) {
		try {
			if (com.getSelectionStart() == com.getSelectionEnd()) { // No
				// selection
				Rectangle rect = com.modelToView(com.getCaretPosition());
				g.setColor(color);
				g.fillRect(0, rect.y, com.getWidth(), rect.height);
				g.setColor(color.brighter().darker());
				// g.drawLine(0, rect.y, com.getWidth(), rect.y);
				// g.setColor(color.darker());
				g.drawLine(0, rect.y + rect.height - 1, com.getWidth(), rect.y + rect.height - 1);
			}
		} catch (BadLocationException e) {
        	e.printStackTrace();
		}
	}

 /**
  * ֻ���ǰһ�εĸ���������������editor
  */
 public void resetHighlight() {
     // �����������ػ����һ��SwingUtilities.invokeLater��Ļ���99%�Ļ��ƶ��Ǻõ�,
     // ��Ψһһ�����ǰһ�й�����ڴ�һ˲�����һ���ܲ����ԵĿհף��������ϸ�۲��ǿ�������
     Element root = editor.getDocument().getDefaultRootElement();
     int caretPos = editor.getCaretPosition();

     try {
         // Remove the highlighting from the previously highlighted
         // line
         Element lineElement = root.getElement(previousLine);

         // Might be null if an Undo action was performed on the
         // text component
         if (lineElement != null) {
             // ���ǰһ�ι�������еĸ���0
             int start = lineElement.getStartOffset();
             Rectangle rect = editor.modelToView(start);
             if (rect != null) {
                 editor.repaint(0, rect.y, editor.getWidth(), rect.height);

                 // ���û�����, �����������뷨�����, ����ٵ��ʱ������ǰ�л��п��ܳ�����
                 rect = editor.modelToView(caretPos);
                 editor.repaint(0, rect.y, editor.getWidth(), rect.height);
             }
         }
     } catch (BadLocationException ble) {
     }
     /*
      * // We need to use invokeLater to make sure that all updates to the //
      * Document have been completed SwingUtilities.invokeLater(new
      * Runnable() { public void run() { try { Element root =
      * editor.getDocument().getDefaultRootElement(); int caretPos =
      * editor.getCaretPosition(); int currentLine =
      * root.getElementIndex(caretPos);
      * 
      * // Remove the highlighting from the previously highlighted // line if
      * (currentLine != previousLine) { Element lineElement =
      * root.getElement(previousLine);
      * 
      * // Might be null if an Undo action was performed on the // text
      * component if (lineElement != null) { // ���ǰһ�ι�������еĸ���0 int start =
      * lineElement.getStartOffset(); Rectangle rect =
      * editor.modelToView(start); if (rect != null) { editor.repaint(0,
      * rect.y, editor.getWidth(), rect.height);
      * 
      * // ���û�����, �����������뷨�����, ����ٵ��ʱ������ǰ�л��п��ܳ����� rect =
      * editor.modelToView(caretPos); editor.repaint(0, rect.y,
      * editor.getWidth(), rect.height); } } previousLine = currentLine; } }
      * catch (BadLocationException ble) { } } });
      */
 }

 @Override
 public void mousePressed(MouseEvent e) {
     resetHighlight();
     // editor.repaint();
 }

 @Override
 public void mouseDragged(MouseEvent e) {
     resetHighlight();
     // editor.repaint();
 }

 public void caretUpdate(CaretEvent e) {
     resetHighlight();
     // editor.repaint();
 }
}
