package com.sin.notepad.main;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sin.notepad.global.GlobalVariable;
import com.sin.notepad.view.NotepadMainFrame;

public class Main {
    public static void main(String[] args) 
    throws ClassNotFoundException, InstantiationException, 
    IllegalAccessException, UnsupportedLookAndFeelException 
    {
    	UIManager.setLookAndFeel(GlobalVariable.STYLE[4]);
    	
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NotepadMainFrame frame = new NotepadMainFrame();
                    frame.setVisible(true);
                    frame.setTitle("Mini Notepad++ By SinKinBen");
                    frame.setIconImage(new ImageIcon("icon/notepad.png").getImage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
