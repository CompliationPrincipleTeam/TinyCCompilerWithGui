package com.sin.notepad.view;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.sin.notepad.global.CodeType;

public class HighlightFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextPane editor;
	private Font fontEN = new Font("Consolas", Font.PLAIN,15); //英文字体

	
	public HighlightFrame(final Component parent, String title, CodeType type) throws IOException
	{
		editor = new JTextPane();
		editor.getDocument().addDocumentListener(new SyntaxHighlighter(editor,type));
		editor.setFont(fontEN);
		JScrollPane jsp = new JScrollPane(editor);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setFont(fontEN);
		//this.getContentPane().add(editor);
		this.getContentPane().add(jsp);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(1000,750);
		this.setVisible(true);
		this.setTitle(title);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event)
			{
				int choice = JOptionPane.showConfirmDialog(parent, "是否更新当前代码文本？",
						"提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(choice == JOptionPane.YES_OPTION)
				{
					NotepadMainFrame.setTextArea(editor.getText());
				}
			}
		});
	}
	
	public JTextPane getJTextPane()
	{
		return this.editor;
	}
	
//	public static void main(String[] args) throws IOException {
//		HighlightFrame frame = new HighlightFrame("",CodeType.Python);
//
//		JTextPane editor = new JTextPane();
//		editor.getDocument().addDocumentListener(new SyntaxHighlighter(editor,CodeType.Other));
//		frame.getContentPane().add(editor);
//	}
}