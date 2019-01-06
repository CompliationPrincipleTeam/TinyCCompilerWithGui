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

//当一个highlight被加到text component中后，每次更新高亮时，都会去遍历highlighter的所有highlight，
//调用他们对应的painter.paing()方法,来进行绘制. 所以我们这里只需要加一次highlight, 就可以一直使用.
//而不是每次更新要高亮的行时就加一个新的highlight, 因为他会动态地计算当前行来进行高亮.
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
  * 只清除前一次的高亮，而不是整个editor
  */
 public void resetHighlight() {
     // 如果把下面的重绘放在一个SwingUtilities.invokeLater里的话，99%的绘制都是好的,
     // 但唯一一点就是前一行光标所在处一瞬间会有一个很不明显的空白，如果不仔细观察是看不到的
     Element root = editor.getDocument().getDefaultRootElement();
     int caretPos = editor.getCaretPosition();

     try {
         // Remove the highlighting from the previously highlighted
         // line
         Element lineElement = root.getElement(previousLine);

         // Might be null if an Undo action was performed on the
         // text component
         if (lineElement != null) {
             // 清除前一次光标所在行的高亮0
             int start = lineElement.getStartOffset();
             Rectangle rect = editor.modelToView(start);
             if (rect != null) {
                 editor.repaint(0, rect.y, editor.getWidth(), rect.height);

                 // 如果没有这个, 当用中文输入法输入后, 鼠标再点击时高亮当前行会有可能出问题
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
      * component if (lineElement != null) { // 清除前一次光标所在行的高亮0 int start =
      * lineElement.getStartOffset(); Rectangle rect =
      * editor.modelToView(start); if (rect != null) { editor.repaint(0,
      * rect.y, editor.getWidth(), rect.height);
      * 
      * // 如果没有这个, 当用中文输入法输入后, 鼠标再点击时高亮当前行会有可能出问题 rect =
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
