package com.sin.notepad.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.PrintJob;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.undo.UndoManager;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.sin.notepad.build.BuildCode;
import com.sin.notepad.build.BuildCplusplus;
import com.sin.notepad.build.BuildJava;
import com.sin.notepad.build.BuildPython;
import com.sin.notepad.cloud.CloudService;
import com.sin.notepad.failure.FailHandler;
import com.sin.notepad.global.CodeType;
import com.sin.notepad.global.FailureType;
import com.sin.notepad.global.GlobalVariable;
import com.sin.notepad.settings.PersonalSettings;
import com.sin.notepad.system.SystemApi;
import com.sin.notepad.util.Clock;
import com.sin.notepad.util.LineOrder;
import com.sin.notepad.util.MQFontChooser;

import tinyccompiler.run.RunTinyCCompiler;
import xuefanggang.StackRun;

public class NotepadMainFrame extends JFrame implements ActionListener{
    /**
     * ���к�
     */
    private static final long serialVersionUID = 8585210209467333480L;
    //�������
    private JPanel contentPanel;
    //������山��
    private JPanel contentPanelNorth;
    //�༭��
    private static JTextArea textArea;
    //�༭���Ϸ���ComboBox
    private JComboBox<String> comboBox;
    //private JTextPane textArea;
    //�򿪲˵���
    private JMenuItem itemOpen;
    //����˵���
    private JMenuItem itemSave;
    
    //1���½� 
    //2���޸Ĺ�
    //3���������
    static int flag=0;

    //��ǰ�ļ���
    String currentFileName=null;
    
     PrintJob  p=null;//����һ��Ҫ��ӡ�Ķ���
     Graphics  g=null;//Ҫ��ӡ�Ķ���
    
    //��ǰ�ļ�·��
    static String currentPath=null;
    
    //������ɫ
    JColorChooser jcc1=null;
    Color color=Color.BLACK;
    
    //�ı�������������
    int linenum = 1;
    int columnnum = 1;
    
    //����������
    public UndoManager undoMgr = new UndoManager(); 
    
    //������
    public Clipboard clipboard = new Clipboard("ϵͳ���а�"); 
    
    private JMenuBar menuBar;
    private JMenu itemRecentFiles;		       //�����
    private JMenuItem itemExplorer;			   //���ļ�����λ��
    private JMenuItem itemSaveAs;              //���Ϊ
    private JMenuItem itemNew;				   //�½�
    private JMenuItem itemPage;				   //ҳ������
    private JSeparator separator;			   //�ָ���
    private JMenuItem itemPrint;			   //��ӡ
    private JMenuItem itemExit;				   //�˳�
    private JSeparator separator_1;			   //�ָ���
    private JMenu itemEdit;					   //�༭
    private JMenu itFormat;					   //��ʽ
    private JMenu itemCheck;				   //�鿴
    private JMenu itemHelp;					   //����
    private JMenu itemBuild;				   //���빹������
    private JMenu itemCodeView;				   //�﷨����ģʽ
    private JMenu itemCloudService;			   //�ϴ��������ļ�
    private JMenu itemTimingSaving;			   //��ʱ����
    private JMenu itemMore;					   //����
    private JMenuItem itemSearchForHelp;	   //�鿴����
    private JMenuItem itemAboutNotepad;		   //���ڼ��±�
    private JMenuItem itemUndo;				   //����
    private JMenuItem itemCut;				   //����
    private JMenuItem itemCopy;				   //����
    private JMenuItem itemPaste;			   //ճ��
    private JMenuItem itemDelete;			   //ɾ��
    private JMenuItem itemFind;				   //����
    private JMenuItem itemFindNext;			   //������һ��
    private JMenuItem itemReplace;			   //�滻
    private JMenuItem itemTurnTo;			   //ת��
    private JMenuItem itemSelectAll;		   //ȫѡ
    private JMenuItem itemTime;				   //����/ʱ��
    private JMenuItem itemFont;				   //����
    private JMenuItem itemColor;			   //������ɫ
    private JMenuItem itemFontColor;		   //������ɫ
    private JMenuItem itemBuildCplusplus;	   //����C++
    private JMenuItem itemBuildJava;		   //����Java
    private JMenuItem itemBuildPython;		   //����Python
    private JMenuItem itemUpload;			   //�ϴ��ļ�
    private JMenuItem itemDownload;			   //�����ļ�
    private JMenuItem itemCal;				//������
    private JMenuItem itemSnip;				//��ͼ
    private JMenuItem itemMagnify;			//�Ŵ�
    private JMenuItem itemOsk;				//��Ļ���̣�����Windowsƽ�����
    private ButtonGroup codeViewRadioButtonGroup;
    private ButtonGroup itemTimingButtonGruop;
    private JRadioButtonMenuItem itemCodeViewCplusplus;   //C++����
    private JRadioButtonMenuItem itemCodeViewJava;		   //Java����
    private JRadioButtonMenuItem itemCodeViewPython;	   //python����
    private JRadioButtonMenuItem itemCommonView;		   //������ͼ
    private JRadioButtonMenuItem item05Secs;			   //������ʾ
    private JRadioButtonMenuItem item15Mins;
    private JRadioButtonMenuItem item30Mins;
    private JRadioButtonMenuItem item60Mins;
    private JRadioButtonMenuItem itemUntiming;
    private JCheckBoxMenuItem itemNextLine;	   //�Զ�����
    private JScrollPane scrollPane;			   //������
    private JCheckBoxMenuItem itemStatement;   //״̬��
    private JToolBar toolState;				  
    public static JLabel toolStateLabel;
    private JLabel label2;
    private JLabel label3;
    int length=0;
    int sum=0;
    
    GregorianCalendar c=new GregorianCalendar();
    int hour=c.get(Calendar.HOUR_OF_DAY);
    int min=c.get(Calendar.MINUTE);
    int second=c.get(Calendar.SECOND);    
    private JPopupMenu popupMenu;         //�Ҽ������˵�
    private JMenuItem popM_Undo;		  //����
    private JMenuItem popM_Cut;			  //����
    private JMenuItem popM_Copy;		  //����
    private JMenuItem popM_Paste;		  //ճ��
    private JMenuItem popM_Delete;		  //ɾ��
    private JMenuItem popM_SelectAll;	  //ȫѡ
    private JMenuItem popM_toLeft;		  //���ҵ�����Ķ�˳��
    private JMenuItem popM_showUnicode;   //��ʾUnicode�����ַ�
    private JMenuItem popM_closeIMe;      //�ر�IME
    private JMenuItem popM_InsertUnicode; //����Unicode�����ַ�
    private JMenuItem popM_RestartSelect; //������ѡ
    private JSeparator separator_2;       //�ָ���
    private JSeparator separator_3;		  //�ָ���
    private JSeparator separator_4;       //�ָ���
    private JSeparator separator_5;       //�ָ���
    private JMenuItem itemRedo;			  //�ָ�
    private JSeparator separator_6;		  //�ָ���
    private JSeparator separator_7;		  //�ָ���
    private JSeparator separator_8;		  //�ָ���
    private JMenuItem popM_Redo;		  //�ָ�
    private LineOrder lineOrder;
    /**
     * Create the frame.
     * ���캯��
     * @throws IOException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    public NotepadMainFrame() 
    throws IOException, ParserConfigurationException, SAXException
    {
        setTitle("�ޱ���");    
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		System.out.println("Close Button is clicked!!");
        		try {
					PersonalSettings.saveSettings();
					FailHandler.writeLog(FailureType.Normal);
					// XXX:�˳��Ƿ񱸷ݵ�Buffer.txt
					backup();
				} catch (TransformerException | ParserConfigurationException | IOException e1) {
					e1.printStackTrace();
				}
        	}
        }); 
        setBounds(100, 100, 1000, 772);
        
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu itemFile = new JMenu("�ļ�(F)");
        itemFile.setMnemonic('F');	//���ÿ�ݼ�"F"
        menuBar.add(itemFile);
        
        
        itemNew = new JMenuItem("�½�(N)",'N');
        itemNew.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
                java.awt.Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"N"
        itemNew.addActionListener(this);
        itemFile.add(itemNew);
        
        itemOpen = new JMenuItem("��(O)",'O');
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
                            java.awt.Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"O"
        itemOpen.addActionListener(this);
        itemFile.add(itemOpen);
        
        itemSave = new JMenuItem("����(S)");
        itemSave.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                java.awt.Event.CTRL_MASK));   //���ÿ�ݼ�Ctrl+"S"
        itemSave.addActionListener(this);
        itemFile.add(itemSave);
        
        itemSaveAs = new JMenuItem("���Ϊ(A)");
        itemSaveAs.addActionListener(this);
        itemFile.add(itemSaveAs);
        
        itemRecentFiles = new JMenu("�����");
        itemFile.add(itemRecentFiles);
        
        itemExplorer = new JMenuItem("���ļ�����λ��");
        itemExplorer.addActionListener(this);
        itemFile.add(itemExplorer);
        
        
        separator = new JSeparator();  //��ӷָ���
        itemFile.add(separator);
        
        itemPage = new JMenuItem("ҳ������(U)",'U');
        itemPage.addActionListener(this);
        itemFile.add(itemPage);
        
        itemPrint = new JMenuItem("��ӡ(P)...",'P');
        itemPrint.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P,
                java.awt.Event.CTRL_MASK));   //�ÿ�ݼ�Ctrl+"P"
        itemPrint.addActionListener(this);
        itemFile.add(itemPrint);
        
        separator_1 = new JSeparator();
        itemFile.add(separator_1);
        
        itemExit = new JMenuItem("�˳�(X)",'X');
        itemExit.addActionListener(this);
        itemFile.add(itemExit);
        
        itemEdit = new JMenu("�༭(E)");
        itemEdit.setMnemonic('E');
        menuBar.add(itemEdit);
        
        itemUndo = new JMenuItem("����(U)",'U');
        itemUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,
                java.awt.Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"Z"
        itemUndo.addActionListener(this);
        itemEdit.add(itemUndo);
        
        itemRedo = new JMenuItem("�ָ�(R)");
        itemRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
                java.awt.Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"R"
        itemRedo.addActionListener(this);
        itemEdit.add(itemRedo);
        
        itemCut = new JMenuItem("����(T)",'T');
        itemCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
                java.awt.Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"X"
        itemCut.addActionListener(this);
        
        separator_6 = new JSeparator();
        itemEdit.add(separator_6);
        itemEdit.add(itemCut);
        
        itemCopy = new JMenuItem("����(C)",'C');
        itemCopy.addActionListener(this);
        itemCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
                java.awt.Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"C"
        itemEdit.add(itemCopy);
        
        itemPaste = new JMenuItem("ճ��(P)",'P');
        itemPaste.addActionListener(this);
        itemPaste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
                java.awt.Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"V"
        itemEdit.add(itemPaste);
        
        itemDelete = new JMenuItem("ɾ��(L)",'L');
        itemDelete.addActionListener(this);
        itemDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,  
                InputEvent.CTRL_MASK));    //���ÿ�ݼ�Ctrl+"D"
        itemEdit.add(itemDelete);
        
        separator_7 = new JSeparator();
        itemEdit.add(separator_7);
        
        itemFind = new JMenuItem("����(F)",'F');
        itemFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                Event.CTRL_MASK));   //���ÿ�ݼ�Ctrl+"F"
        itemFind.addActionListener(this);
        itemEdit.add(itemFind);
        
        itemFindNext = new JMenuItem("������һ��(N)",'N');
        itemFindNext.setAccelerator(KeyStroke.getKeyStroke("F3"));
        itemFindNext.addActionListener(this);
        itemEdit.add(itemFindNext);
        
        itemReplace = new JMenuItem("�滻(R)",'R');
        itemReplace.addActionListener(this);
        itemReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"H"
        itemEdit.add(itemReplace);
        
        itemTurnTo = new JMenuItem("ת��(G)",'G');
        itemTurnTo.addActionListener(this);
        itemTurnTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"G"
        itemEdit.add(itemTurnTo);
        
        itemSelectAll = new JMenuItem("ȫѡ(A)",'A');
        itemSelectAll.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,
                java.awt.Event.CTRL_MASK));  //���ÿ�ݼ�Ctrl+"A"
        itemSelectAll.addActionListener(this);
        
        separator_8 = new JSeparator();
        itemEdit.add(separator_8);
        itemEdit.add(itemSelectAll);
        
        itemTime = new JMenuItem("ʱ��/����(D)",'D');
        itemTime.addActionListener(this);
        itemTime.setAccelerator(KeyStroke.getKeyStroke("F5"));
        itemEdit.add(itemTime);
        
        itFormat = new JMenu("��ʽ(O)");
        itFormat.setMnemonic('O');
        menuBar.add(itFormat);
        
        itemNextLine = new JCheckBoxMenuItem("�Զ�����(W)");
        itemNextLine.addActionListener(this);
        itFormat.add(itemNextLine);
        
        itemFont = new JMenuItem("�����С(F)...");
        itemFont.addActionListener(this);
        itFormat.add(itemFont);
        
        itemColor = new JMenuItem("������ɫ(C)...");
        itemColor.addActionListener(this);
        itFormat.add(itemColor);
        
        itemFontColor = new JMenuItem("������ɫ(I)...");
        itemFontColor.addActionListener(this);
        itFormat.add(itemFontColor);
        
        itemCheck = new JMenu("�鿴(V)");
        itemCheck.setMnemonic('V');
        menuBar.add(itemCheck);
        
        itemStatement = new JCheckBoxMenuItem("״̬��(S)");
        itemStatement.setSelected(true);
        itemStatement.addActionListener(this);
        itemCheck.add(itemStatement);
        
        
        itemBuild = new JMenu("����(B)");
        itemBuild.setMnemonic('B');
        itemBuildCplusplus = new JMenuItem("����C/C++");
        itemBuildJava = new JMenuItem("����Java");
        itemBuildPython = new JMenuItem("����Python");
        itemBuildCplusplus.addActionListener(this);
        itemBuildJava.addActionListener(this);
        itemBuildPython.addActionListener(this);
        itemBuild.add(itemBuildCplusplus);
        itemBuild.add(itemBuildJava);
        itemBuild.add(itemBuildPython);
        menuBar.add(itemBuild);
        
        itemCloudService = new JMenu("�ƶ�(C)");
        itemCloudService.setMnemonic('C');
        itemUpload = new JMenuItem("�ϴ�(U)");
        itemDownload = new JMenuItem("����(D)");
        itemUpload.addActionListener(this);
        itemDownload.addActionListener(this);
        
        itemCloudService.add(itemUpload);
        itemCloudService.add(itemDownload);
        menuBar.add(itemCloudService);
        
        initCodeViewMenu();
        initMoreMenu();
        
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        //���ñ߿򲼾�
        contentPanel.setLayout(new BorderLayout(0, 0));
//        XXX: annote by sin
//        setContentPane(contentPanel);
        
        textArea = new JTextArea();
        //add by sin
//		textArea = new JTextPane();
//		textArea.getDocument().addDocumentListener(new SyntaxHighlighter(textArea));
        
        //VERTICAL��ֱ    HORIZONTALˮƽ
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        //contentPane2=new JPanel();
        //contentPane2.setSize(10,textArea.getSize().height);
        //contentPane.add(contentPane2, BorderLayout.WEST);
        lineOrder = new LineOrder(); //����к�
        scrollPane.setRowHeaderView(lineOrder);
        
        popupMenu = new JPopupMenu();
        addPopup(textArea, popupMenu);
        
        popM_Undo = new JMenuItem("����(U)");
        popM_Undo.addActionListener(this);
        popupMenu.add(popM_Undo);
        
        popM_Redo = new JMenuItem("�ָ�(R)");
        popM_Redo.addActionListener(this);
        popupMenu.add(popM_Redo);
        
        separator_2 = new JSeparator();
        popupMenu.add(separator_2);
        
        popM_Cut = new JMenuItem("����(T)");
        popM_Cut.addActionListener(this);
        popupMenu.add(popM_Cut);
        
        popM_Copy = new JMenuItem("����(C)");
        popM_Copy.addActionListener(this);
        popupMenu.add(popM_Copy);
        
        popM_Paste = new JMenuItem("ճ��(P)");
        popM_Paste.addActionListener(this);
        popupMenu.add(popM_Paste);
        
        popM_Delete = new JMenuItem("ɾ��(D)");
        popM_Delete.addActionListener(this);
        popupMenu.add(popM_Delete);
        
        separator_3 = new JSeparator();
        popupMenu.add(separator_3);
        
        popM_SelectAll = new JMenuItem("ȫѡ(A)");
        popM_SelectAll.addActionListener(this);
        popupMenu.add(popM_SelectAll);
        
        separator_4 = new JSeparator();
        popupMenu.add(separator_4);
        
        popM_toLeft = new JMenuItem("���ҵ�����Ķ�˳��(R)");
        popM_toLeft.addActionListener(this);
        popupMenu.add(popM_toLeft);
        
        popM_showUnicode = new JMenuItem("��ʾUnicode�����ַ�(S)");
        popM_showUnicode.addActionListener(this);
        popupMenu.add(popM_showUnicode);
        
        popM_InsertUnicode = new JMenuItem("����Unicode�����ַ�(I)");
        popM_InsertUnicode.addActionListener(this);
        popupMenu.add(popM_InsertUnicode);
        
        separator_5 = new JSeparator();
        popupMenu.add(separator_5);
        
        popM_closeIMe = new JMenuItem("�ر�IME(L)");
        popM_closeIMe.addActionListener(this);
        popupMenu.add(popM_closeIMe);
        
        popM_RestartSelect = new JMenuItem("������ѡ(R)");
        popM_RestartSelect.addActionListener(this);
        popupMenu.add(popM_RestartSelect);
        //��ӵ�����С��м䡿
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        //northPanel -> North
        //XXX:���north���
        initNorthPanel();
        contentPanel.add(contentPanelNorth, BorderLayout.NORTH);
        
        //��ӳ���������
        textArea.getDocument().addUndoableEditListener(undoMgr);
                
        //����״̬��
        toolState = new JToolBar();
        toolState.setSize(textArea.getSize().width, 10);//toolState.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolStateLabel = new JLabel("    ��ǰϵͳʱ�䣺" + hour + ":" + min + ":" + second+" ");
        toolState.add(toolStateLabel);  //���ϵͳʱ��
        toolState.addSeparator();
        label2 = new JLabel("    �� " + linenum + " ��, �� " + columnnum+" ��  ");
        toolState.add(label2);  //�����������
        toolState.addSeparator();
        
        label3 = new JLabel("    һ�� " +length+" ��  ");
        toolState.add(label3);  //�������ͳ��
        textArea.addCaretListener(new CaretListener() {        //��¼����������
            public void caretUpdate(CaretEvent e) {
                //sum=0;
                JTextArea editArea = (JTextArea)e.getSource();
 
                try {
                    int caretpos = editArea.getCaretPosition();
                    linenum = editArea.getLineOfOffset(caretpos);
                    columnnum = caretpos - textArea.getLineStartOffset(linenum);
                    linenum += 1;
                    label2.setText("    �� " + linenum + " ��, �� " + (columnnum+1)+" ��  ");
                    //sum+=columnnum+1;
                    //length+=sum;
                    length=NotepadMainFrame.textArea.getText().toString().length();
                    label3.setText("    һ�� " +length+" ��  ");
                }
                catch(Exception ex) { }
            }});
        
        contentPanel.add(toolState, BorderLayout.SOUTH);  //��״̬����ӵ������
        toolState.setVisible(true);
        toolState.setFloatable(false);
        Clock clock=new Clock();
        clock.start();//����ʱ���߳�
        
        
        
        // ���������˵�
        final JPopupMenu jp=new JPopupMenu();    //��������ʽ�˵������������ǲ˵���
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3)//ֻ��Ӧ����Ҽ������¼�
                {
                    jp.show(e.getComponent(),e.getX(),e.getY());//�����λ����ʾ����ʽ�˵�
                }
            }
        });
        
        isChanged();
        this.MainFrameWindowListener();
        this.loadSettings();
        setContentPane(contentPanel);
        
    }
    
    private void initCodeViewMenu()
    {
        itemCodeView = new JMenu("��ͼ(S)");
        itemCodeView.setMnemonic('S');
        itemCommonView = new JRadioButtonMenuItem("������ͼ");
        itemCodeViewCplusplus = new JRadioButtonMenuItem("C/C++��ͼ");
        itemCodeViewJava = new JRadioButtonMenuItem("Java��ͼ");
        itemCodeViewPython = new JRadioButtonMenuItem("Python��ͼ");
        itemCommonView.addActionListener(this);
        itemCodeViewCplusplus.addActionListener(this);
        itemCodeViewJava.addActionListener(this);
        itemCodeViewPython.addActionListener(this);
        
        codeViewRadioButtonGroup = new ButtonGroup();
        codeViewRadioButtonGroup.add(itemCommonView);
        codeViewRadioButtonGroup.add(itemCodeViewCplusplus);
        codeViewRadioButtonGroup.add(itemCodeViewJava);
        codeViewRadioButtonGroup.add(itemCodeViewPython );
        itemCommonView.setSelected(true);
        itemCodeView.add(itemCommonView);
        itemCodeView.add(itemCodeViewCplusplus);
        itemCodeView.add(itemCodeViewJava);
        itemCodeView.add(itemCodeViewPython );
        menuBar.add(itemCodeView);
    }
    
    private void initMoreMenu()
    {
    	itemMore = new JMenu("����(M)");
    	itemMore.setMnemonic('M');
    	
        
        itemCal = new JMenuItem("������");
        itemCal.addActionListener(this);
        itemMore.add(itemCal);
        itemSnip = new JMenuItem("��ͼ");
        itemSnip.addActionListener(this);
        itemMore.add(itemSnip);
        itemMagnify = new JMenuItem("�Ŵ�");
        itemMagnify.addActionListener(this);
        itemMore.add(itemMagnify); 
        itemOsk = new JMenuItem("��Ļ����");
        itemOsk.addActionListener(this);
        itemMore.add(itemOsk);
        
        
    	itemTimingSaving = new JMenu("��ʱ����");
    	item05Secs = new JRadioButtonMenuItem("5��");
    	item15Mins = new JRadioButtonMenuItem("15����");
    	item30Mins = new JRadioButtonMenuItem("30����");
    	item60Mins = new JRadioButtonMenuItem("60����");
    	itemUntiming = new JRadioButtonMenuItem("��");
    	
    	itemTimingButtonGruop = new ButtonGroup();
    	itemTimingButtonGruop.add(item05Secs);
    	itemTimingButtonGruop.add(item15Mins);
    	itemTimingButtonGruop.add(item30Mins);
    	itemTimingButtonGruop.add(item60Mins);
    	itemTimingButtonGruop.add(itemUntiming);
    	
    	itemTimingSaving.add(item05Secs);
    	itemTimingSaving.add(item15Mins);
    	itemTimingSaving.add(item30Mins);
    	itemTimingSaving.add(item60Mins);
    	itemTimingSaving.add(itemUntiming);
    	
    	itemMore.add(itemTimingSaving);
    	
    	itemUntiming.setSelected(true);
    	itemUntiming.addActionListener
		(
			new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Clock.cancelTiming();
			}
		});
    	
		item05Secs.addActionListener
		(
			new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println(item05Secs.getText()+" is selected");
				Clock.setTimingEnd(5);
			}
		});
		item15Mins.addActionListener
		(
			new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println(item15Mins.getText()+" is selected");
				Clock.setTimingEnd(15 * 60);
			}
		});
		item30Mins.addActionListener
		(
			new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println(item30Mins.getText()+" is selected");
				Clock.setTimingEnd(30 * 60);
				
			}
		});
		item60Mins.addActionListener
		(
			new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println(item60Mins.getText()+" is selected");
				Clock.setTimingEnd(60 * 60);
			}
		});
        itemHelp = new JMenu("����(H)");
        itemHelp.setMnemonic('H');
        
        itemSearchForHelp = new JMenuItem("�鿴����(H)",'H');
        itemSearchForHelp.addActionListener(this);
        itemHelp.add(itemSearchForHelp);
        
        itemAboutNotepad = new JMenuItem("���ڼ��±�(A)",'A');
        itemAboutNotepad.addActionListener(this);
        itemHelp.add(itemAboutNotepad);
        itemMore.add(itemHelp);
    	menuBar.add(itemMore);
    	
    }
    
    private void initNorthPanel()
    {
        contentPanelNorth = new JPanel(new GridLayout(2, 1));
        contentPanelNorth.setPreferredSize(new Dimension(getWidth(), 55));
        comboBox = new JComboBox<String>();
        JPanel iconButtonPanel = new JPanel(new GridLayout(1, 10));
        JButton openButton = new JButton(new ImageIcon("icon/open.png"));
        JButton saveButton = new JButton(new ImageIcon("icon/save.png"));
        JButton findButton = new JButton(new ImageIcon("icon/find.png"));
        JButton revokeButton = new JButton(new ImageIcon("icon/revoke.png"));
        JButton recoverButton = new JButton(new ImageIcon("icon/recover.png"));
        JButton uploadButton = new JButton(new ImageIcon("icon/upload.png"));
        JButton downloadButton = new JButton(new ImageIcon("icon/download.png"));
        JButton calButton = new JButton(new ImageIcon("icon/calculator.png"));
        JButton keyBoardButton = new JButton(new ImageIcon("icon/keyboard.png"));
        JButton snipButton = new JButton(new ImageIcon("icon/snippingtool.png"));
        JButton clockButton = new JButton(new ImageIcon("icon/clock.png"));
        JButton magnifyButton = new JButton(new ImageIcon("icon/magnify.png"));
        JButton buildButton = new JButton(new ImageIcon("icon/build.png"));
        JButton stackButton = new JButton(new ImageIcon("icon/stack.png"));
        iconButtonPanel.add(openButton);
        iconButtonPanel.add(saveButton);
        iconButtonPanel.add(findButton);
        iconButtonPanel.add(revokeButton);
        iconButtonPanel.add(recoverButton);
        iconButtonPanel.add(uploadButton);
        iconButtonPanel.add(downloadButton);
        iconButtonPanel.add(clockButton);
        iconButtonPanel.add(calButton);
        iconButtonPanel.add(keyBoardButton);
        iconButtonPanel.add(snipButton);
        iconButtonPanel.add(magnifyButton);
        iconButtonPanel.add(buildButton);
        iconButtonPanel.add(stackButton);
        stackButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				StackRun.run();
				
			}
		});
        openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
        saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
        findButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mySearch();
			}
		});
        revokeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(undoMgr.canUndo())
					undoMgr.undo();
			}
		});
        recoverButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(undoMgr.canRedo())
					undoMgr.redo();
			}
		});
        
        uploadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				uploadAction();
			}
		});
        
        downloadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				downloadAction();
			}
		});
        
        clockButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Clock.setTimingEnd(10);
			}
		});
//        JPopupMenu clockPopupMenu = new JPopupMenu();
//        clockPopupMenu.add(item10Secs);
//        clockPopupMenu.add(item15Mins);
//        clockPopupMenu.add(item30Mins);
//        clockPopupMenu.add(item60Mins);
//        clockButton.setComponentPopupMenu(clockPopupMenu);
        
        calButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				runCalculator();
			}
		});
        
        keyBoardButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				runOsk();
			}
		});
        
        snipButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				runSnippingTool();
			}
		});
        
        magnifyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				runMagnify();
			}
		});
        
        buildButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// TODO Auto-generated method stub
				//���ݺ�׺build code
				//buildAction();
				/*
				 * ��ʱ��Ϊ����ԭ��ĵ��ýӿ�
				 *
				 */
				runTinyCCompiler();
			}
		});
        
    	comboBox.addItemListener
    	(
    			new ItemListener()
    			{
    				public void itemStateChanged(ItemEvent event)
    				{
    					if(event.getStateChange() == ItemEvent.SELECTED)
    					{
    						System.out.println(comboBox.getSelectedItem());
    						openRecentFile((String)comboBox.getSelectedItem());
    					}
    				}
    			}
    	);
    	contentPanelNorth.add(iconButtonPanel);
        contentPanelNorth.add(comboBox);
    }
    
    /*===============================1====================================*/
    /**
     * �Ƿ��б仯
     */
    private void isChanged() {
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //�������ҽ����˶�ʹ�ÿ�ݼ�������û�������ַ�ȴû�иı�textArea�����ݵ��ж�
                Character c=e.getKeyChar();
                if(c != null && !textArea.getText().toString().equals("")){
                    flag=2;
                }
            }
        });
    }
    /*===================================================================*/
    
    
    /*===============================2====================================*/
    /**
     * �½��Ļ򱣴�����˳�ֻ������ѡ��
     */
    private void MainFrameWindowListener() throws IOException{
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                if(flag==2 && currentPath==null){
                    //���ǵ���С����
                    //1�������������±�Ϊ0�����½��ĵ�Ϊ1���������޸ĺ�
                    int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽�ޱ���?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if(result==JOptionPane.OK_OPTION){
                        NotepadMainFrame.this.saveAs();
                    }else if(result==JOptionPane.NO_OPTION){
                        NotepadMainFrame.this.dispose();
                        NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                    NotepadMainFrame.this.dispose();
                    NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }else if(flag==2 && currentPath!=null){
                    //���ǵ���С����
                    //1�������������±�Ϊ0�����½��ĵ�Ϊ1���������޸ĺ�
                    int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽"+currentPath+"?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if(result==JOptionPane.OK_OPTION){
                        NotepadMainFrame.this.save();
                    }else if(result==JOptionPane.NO_OPTION){
                        NotepadMainFrame.this.dispose();
                        NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                }else{
                    //���ǵ���С����
                    int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "ȷ���رգ�", "ϵͳ��ʾ", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if(result==JOptionPane.OK_OPTION){
                        NotepadMainFrame.this.dispose();
                        NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                }
            }
        });
    }
    /*===================================================================*/
    
    
    /*==============================3=====================================*/
    /**
     * ��Ϊ����
     */
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==itemOpen)
        {            //��
            openFile();
        }
        else if(e.getSource()==itemSave)
        {        //����
            //������ļ��Ǵ򿪵ģ��Ϳ���ֱ�ӱ���
            save();
        }
        else if(e.getSource()==itemSaveAs)
        {    //���Ϊ
            saveAs();
        }
        else if(e.getSource()==itemNew)
        {        //�½�
            newFile();
        }
        else if(e.getSource()==itemExit)
        {        //�˳�
            exit();  
        }
        else if(e.getSource()==itemPage){        //ҳ������
            ///ҳ�����ã��ٶȵ��ģ���֪��������÷�
            PageFormat pf = new PageFormat();
            PrinterJob.getPrinterJob().pageDialog(pf); 
        }
        else if(e.getSource()==itemPrint){        //��ӡ
            //��ӡ��
            Print();
        }
        else if(e.getSource() == itemExplorer)
        {
        	runExplorer();
        }
        else if(e.getSource()==itemUndo || e.getSource()==popM_Undo){        //����
            if(undoMgr.canUndo()){
                undoMgr.undo();
            }
        }else if(e.getSource()==itemRedo || e.getSource()==popM_Redo){        //�ָ�
            if(undoMgr.canRedo()){
                undoMgr.redo();
            }
        }else if(e.getSource()==itemCut || e.getSource()==popM_Cut){        //����
            cut();
        }else if(e.getSource()==itemCopy || e.getSource()==popM_Copy){        //����
            copy();
        }else if(e.getSource()==itemPaste || e.getSource()==popM_Paste){    //ճ��
            paste();
        }else if(e.getSource()==itemDelete || e.getSource()==popM_Delete){    //ɾ��
            String tem=textArea.getText().toString();
            textArea.setText(tem.substring(0,textArea.getSelectionStart())); 
        }else if(e.getSource()==itemFind){        //����
            mySearch();
        }else if(e.getSource()==itemFindNext){    //������һ��
            mySearch();
        }else if(e.getSource()==itemReplace){    //�滻
            mySearch();
        }else if(e.getSource()==itemTurnTo){    //ת��
            turnTo();
        }else if(e.getSource()==itemSelectAll || e.getSource()==popM_SelectAll){    //ѡ��ȫ��
            textArea.selectAll();
        }else if(e.getSource()==itemTime){        //ʱ��/����
            textArea.append(hour+":"+min+" "+c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH)+"\n");
        }else if(e.getSource()==itemNextLine){    //�����Զ�����
            //�����ı����Ļ��в��ԡ��������Ϊ true�����еĳ��ȴ���������Ŀ��ʱ�������С�������Ĭ��Ϊ false�� 
            if(itemNextLine.isSelected()){
                textArea.setLineWrap(true);
            }else{
                textArea.setLineWrap(false);
            }
        }else if(e.getSource()==itemFont){        //���������С
        	changeFont();
            
        }else if(e.getSource()==itemColor){        //���ñ�����ɫ
            jcc1 = new JColorChooser();
            JOptionPane.showMessageDialog(this, jcc1,"ѡ�񱳾���ɫ��ɫ",-1);
            color = jcc1.getColor();
            textArea.setBackground(color);
            
            //���¸�������-������ɫ
            PersonalSettings.background = color;
        }else if(e.getSource()==itemFontColor){    //����������ɫ
            jcc1=new JColorChooser();
            JOptionPane.showMessageDialog(this, jcc1, "ѡ��������ɫ", -1);
            color = jcc1.getColor();
            //String string=textArea.getSelectedText();
            textArea.setForeground(color);
            
            //���¸�������-������ɫ
            PersonalSettings.foreground = color;
        }else if(e.getSource()==itemStatement){    //����״̬
            if(itemStatement.isSelected()){
                //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                toolState.setVisible(true);
            }else{
                //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                toolState.setVisible(false);
            }
            //���¸�������-״̬����ʾ
            PersonalSettings.showStatusBar = itemStatement.isSelected();
        }else if(e.getSource()==itemSearchForHelp){
            JOptionPane.showMessageDialog(this, "NO HELP","Help",1);
        }else if(e.getSource()==itemAboutNotepad){
            JOptionPane.showMessageDialog(this, "sin's Notepad++\nAuthor:161630230-������","���˵�� ",1);
        }
        //��չ���ܼ����¼�
        //Build Java
        else if(e.getSource()==itemBuildJava) 
        {
        	buildJavaAction();
        }
        //build python
        else if(e.getSource()==itemBuildPython)
        {
        	buildPythonAction();
        }
        //build c/c++
        else if(e.getSource()==itemBuildCplusplus)
        {
        	buildCplusplusAction();
        }
        else if(e.getSource()==itemCodeViewPython)
        {
        	HighlightFrame python;
			try {
				python = new HighlightFrame(NotepadMainFrame.this,"Python View",CodeType.Python);
				python.getJTextPane().setText(textArea.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
        else if(e.getSource()==itemCodeViewCplusplus)
        {
        	HighlightFrame cplusplus;
			try {
				cplusplus = new HighlightFrame(NotepadMainFrame.this,"C/C++ View",CodeType.Cplusplus);
				cplusplus.getJTextPane().setText(textArea.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	
        }
        else if(e.getSource()==itemCodeViewJava)
        {
        	HighlightFrame java;
			try {
				java = new HighlightFrame(NotepadMainFrame.this,"Java View",CodeType.Java);
				java.getJTextPane().setText(textArea.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
        else if(e.getSource() == itemUpload)
        {
        	uploadAction();
        }
        else if(e.getSource() == itemDownload)
        {
        	downloadAction();
        }
        else if(e.getSource() == itemCal)
        {
        	runCalculator();
        }
        else if(e.getSource() == itemSnip)
        {
        	runSnippingTool();
        }
        else if(e.getSource() == itemMagnify)
        {
        	runMagnify();
        }
        else if(e.getSource() == itemOsk)
        {
        	runOsk();
        }
        
        
    }    
    /*===================================================================*/


    private void turnTo() {
        final JDialog gotoDialog = new JDialog(this, "ת��������");
        JLabel gotoLabel = new JLabel("����(L):");
        final JTextField linenum = new JTextField(5);
        linenum.setText("1");
        linenum.selectAll();

        JButton okButton = new JButton("ȷ��");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int totalLine = textArea.getLineCount();
                int[] lineNumber = new int[totalLine + 1];
                String s = textArea.getText();
                int pos = 0, t = 0;

                while (true) {
                    pos = s.indexOf('\12', pos);
                    // System.out.println("����pos:"+pos);
                    if (pos == -1)
                        break;
                    lineNumber[t++] = pos++;
                }

                int gt = 1;
                try {
                    gt = Integer.parseInt(linenum.getText());
                } catch (NumberFormatException efe) {
                    JOptionPane.showMessageDialog(null, "����������!", "��ʾ", JOptionPane.WARNING_MESSAGE);
                    linenum.requestFocus(true);
                    return;
                }

                if (gt < 2 || gt >= totalLine) {
                    if (gt < 2)
                        textArea.setCaretPosition(0);
                    else
                        textArea.setCaretPosition(s.length());
                } else
                    textArea.setCaretPosition(lineNumber[gt - 2] + 1);

                gotoDialog.dispose();//�رմ���
            }

        });

        JButton cancelButton = new JButton("ȡ��");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gotoDialog.dispose();
            }
        });

        //�������ӵ�������
        Container con = gotoDialog.getContentPane();
        con.setLayout(new FlowLayout());
        con.add(gotoLabel);
        con.add(linenum);
        con.add(okButton);
        con.add(cancelButton);

        gotoDialog.setSize(200, 100);
        gotoDialog.setResizable(false);
        gotoDialog.setLocation(300, 280);
        gotoDialog.setVisible(true);
    }
    
    
    /*===============================8====================================*/
    /**
     * �˳���ť���ʹ��ڵĺ��ʵ��һ���Ĺ���
     */
    private void exit() {
    	
        if(flag==2 && currentPath==null){
            //���ǵ���С����
            //1�������������±�Ϊ0�����½��ĵ�Ϊ1���������޸ĺ�
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽�ޱ���?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                NotepadMainFrame.this.saveAs();
            }else if(result==JOptionPane.NO_OPTION){
                NotepadMainFrame.this.dispose();
                NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }else if(flag==2 && currentPath!=null){
            //���ǵ���С����
            //1�������������±�Ϊ0�����½��ĵ�Ϊ1���������޸ĺ�
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽"+currentPath+"?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                NotepadMainFrame.this.save();
            }else if(result==JOptionPane.NO_OPTION){
                NotepadMainFrame.this.dispose();
                NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }else{
            //���ǵ���С����
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "ȷ���رգ�", "ϵͳ��ʾ", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                NotepadMainFrame.this.dispose();
                NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }
    }
    /*===================================================================*/


    /*===============================4====================================*/
    /**
     * �½��ļ���ֻ�иĹ��ĺͱ��������Ҫ����
     */
    private void newFile() {
        if(flag==0 || flag==1){        //���������±�Ϊ0�����½��ĵ�Ϊ1
            return;
        }else if(flag==2 && currentPath==null){        //�޸ĺ�
            //1�������������±�Ϊ0�����½��ĵ�Ϊ1���������޸ĺ�
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽�ޱ���?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();        //���Ϊ                
            }else if(result==JOptionPane.NO_OPTION){
                NotepadMainFrame.textArea.setText("");
                this.setTitle("�ޱ���");
                flag=1;
            }
            return;
        }else if(flag==2 && NotepadMainFrame.currentPath!=null ){
            //2����������ļ�Ϊ3���������޸ĺ�
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽"+currentPath+"?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.save();        //ֱ�ӱ��棬��·��
            }else if(result==JOptionPane.NO_OPTION){
                textArea.setText("");
                this.setTitle("�ޱ���");
                flag=1;
            }
        }else if(flag==3){        //������ļ�
            textArea.setText("");
            flag=1;
            this.setTitle("�ޱ���");
        }
    }
    /*===================================================================*/
    
    
    /*===============================5====================================*/
    /**
     * ���Ϊ
     */
    private void saveAs() {
        //�򿪱����
        JFileChooser choose=new JFileChooser(BuildCode.projectPath());
        //ѡ���ļ�
        int result=choose.showSaveDialog(this);
        if(result==JFileChooser.APPROVE_OPTION){
            //ȡ��ѡ����ļ�[�ļ������Լ������]
            File file=choose.getSelectedFile();
            FileWriter fw=null;
            //����
            try {
                fw=new FileWriter(file);
                fw.write(textArea.getText());
                currentFileName=file.getName();
                currentPath=file.getAbsolutePath();
                //����Ƚ��٣���Ҫд
                fw.flush();
                NotepadMainFrame.flag=3;
                this.setTitle(currentPath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }finally{
                try {
                    if(fw!=null) fw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    /*===================================================================*/
    

    /*===============================6====================================*/
    /**
     * ����
     */
    private void save() {
        if(currentPath==null){
            this.saveAs();
            if(currentPath==null){
                return;
            }
        }
        FileWriter fw=null;
        //����
        try {
            fw=new FileWriter(new  File(currentPath));
            fw.write(textArea.getText());
            //����Ƚ��٣���Ҫд
            fw.flush();
            flag=3;
            this.setTitle(currentPath);
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally{
            try {
                if(fw!=null) fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    /*===================================================================*/
    
    
    /*================================7===================================*/
    /**
     * ���ļ�
     */
    private void openFile() {
        if(flag==2 && currentPath==null){
            //1�������������±�Ϊ0�����½��ĵ�Ϊ1���������޸ĺ�
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽�ޱ���?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();
            }
        }else if(flag==2 && currentPath!=null){
            //2�����򿪵��ļ�2��������ļ�3���������޸�
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽"+currentPath+"?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.save();
            }
        }
        //���ļ�ѡ���
        JFileChooser choose=new JFileChooser(BuildCode.projectPath());
        //ѡ���ļ�
        int result=choose.showOpenDialog(this);
        if(result==JFileChooser.APPROVE_OPTION){
            //ȡ��ѡ����ļ�
            File file=choose.getSelectedFile();
            //���Ѵ��ڵ��ļ�����ǰ���ļ���������
            currentFileName=file.getName();
            //�����ļ�ȫ·��
            currentPath=file.getAbsolutePath();
            flag=3;
            this.setTitle(currentPath);
            BufferedReader br=null;
            try {
            	//XXX:UPDATE operation
            	PersonalSettings.updateRecentFiles(currentPath);
            	updateRecentFileMenu();
            	updateCurrentFileList(currentPath);
                //�����ļ���[�ַ���]
                InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"GBK");
                br=new BufferedReader(isr);//��̬��
                //��ȡ����
                StringBuffer sb=new StringBuffer();
                String line=null;
                while((line=br.readLine())!=null){
                    sb.append(line+SystemApi.LINE_SEPARATOR);
                }
                //��ʾ���ı���[���]
                textArea.setText(sb.toString());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally{
                try {
                    if(br!=null) br.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    /*================================================================*/
    
    /*
     * XXX ��ͨ��������򿪡�ѡ���ļ�
     */
    private void openRecentFile(String path)
    {
        if(flag==2 && currentPath==null){
            //1�������������±�Ϊ0�����½��ĵ�Ϊ1���������޸ĺ�
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽�ޱ���?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();
            }
        }else if(flag==2 && currentPath!=null){
            //2�����򿪵��ļ�2��������ļ�3���������޸�
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "�Ƿ񽫸��ı��浽"+currentPath+"?", "���±�", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.save();
            }
        }
        //ȡ��ѡ����ļ�
        File file=new File(path);
        //���Ѵ��ڵ��ļ�����ǰ���ļ���������
        currentFileName=file.getName();
        //�����ļ�ȫ·��
        currentPath=path;
        flag=3;
        this.setTitle(currentPath);
        BufferedReader br=null;
        try {
        	//XXX:UPDATE operation
        	PersonalSettings.updateRecentFiles(currentPath);
        	updateRecentFileMenu();
        	updateCurrentFileList(currentPath);
            //�����ļ���[�ַ���]
            InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"GBK");
            br=new BufferedReader(isr);//��̬��
            //��ȡ����
            StringBuffer sb=new StringBuffer();
            String line=null;
            while((line=br.readLine())!=null){
                sb.append(line+SystemApi.LINE_SEPARATOR);
            }
            //��ʾ���ı���[���]
            textArea.setText(sb.toString());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally{
            try {
                if(br!=null) br.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        undoMgr.discardAllEdits();

    }
    
    /*=============================9===================================*/
    public void Print()
    {
        try{
            p = getToolkit().getPrintJob(this,"ok",null);//����һ��Printfjob ���� p
            g = p.getGraphics();//p ��ȡһ�����ڴ�ӡ�� Graphics �Ķ���
            //g.translate(120,200);//�ı��齨��λ�� 
            textArea.printAll(g);
            p.end();//�ͷŶ��� g  
        }
        catch(Exception a){

        } 
    }
    /*================================================================*/
    
    
    private static void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }
            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }
    
    public void cut(){
        copy();
        //��ǿ�ʼλ��
        int start = textArea.getSelectionStart();
        //��ǽ���λ��
        int end = textArea.getSelectionEnd();
        //ɾ����ѡ��
        textArea.replaceRange("", start, end);
        
    }
    
    public void copy(){
        //�϶�ѡȡ�ı�
        String temp = textArea.getSelectedText();
        //�ѻ�ȡ�����ݸ��Ƶ������ַ����������̳��˼�����ӿ�
        StringSelection text = new StringSelection(temp);
        //�����ݷ��ڼ�����
        this.clipboard.setContents(text, null);
    }
    
     public void paste(){
         //Transferable�ӿڣ��Ѽ����������ת��������
         Transferable contents = this.clipboard.getContents(this);
         //DataFalvor���ж��Ƿ��ܰѼ����������ת����������������
         DataFlavor flavor = DataFlavor.stringFlavor;
         //�������ת��
         if(contents.isDataFlavorSupported(flavor)){
             String str;
             try {//��ʼת��
                str=(String)contents.getTransferData(flavor);
                //���Ҫճ��ʱ������Ѿ�ѡ����һЩ�ַ�
                if(textArea.getSelectedText()!=null){
                    //��λ��ѡ���ַ��Ŀ�ʼλ��
                    int start = textArea.getSelectionStart();
                    //��λ��ѡ���ַ���ĩβλ��
                    int end = textArea.getSelectionEnd();
                    //��ճ���������滻�ɱ�ѡ�е�����
                    textArea.replaceRange(str, start, end);
                }else{
                    //��ȡ�������TextArea��λ��
                    int mouse = textArea.getCaretPosition();
                    //��������ڵ�λ��ճ������
                    textArea.insert(str, mouse);
                }
             } catch(UnsupportedFlavorException e) {
                e.printStackTrace();
             } catch (IOException e) {
                e.printStackTrace();
             } catch(IllegalArgumentException e){
                e.printStackTrace();
             }
         }
     }
     
    public void mySearch() {
        final JDialog findDialog = new JDialog(this, "�������滻", true);

        Container con = findDialog.getContentPane();
        con.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel searchContentLabel = new JLabel("��������(N) :");
        JLabel replaceContentLabel = new JLabel("�滻Ϊ(P)�� :");
        final JTextField findText = new JTextField(22);
        final JTextField replaceText = new JTextField(22);
        final JCheckBox matchcase = new JCheckBox("���ִ�Сд");
        ButtonGroup bGroup = new ButtonGroup();
        final JRadioButton up = new JRadioButton("����(U)");
        final JRadioButton down = new JRadioButton("����(D)");
        down.setSelected(true);
        bGroup.add(up);
        bGroup.add(down);
        JButton searchNext = new JButton("������һ��(F)");
        JButton replace = new JButton("�滻(R)");
        final JButton replaceAll = new JButton("ȫ���滻(A)");
        searchNext.setPreferredSize(new Dimension(150, 22));
        replace.setPreferredSize(new Dimension(150, 22));
        replaceAll.setPreferredSize(new Dimension(150, 22));
        // "�滻"��ť���¼�����
        replace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (replaceText.getText().length() == 0 && textArea.getSelectedText() != null)
                    textArea.replaceSelection("");
                if (replaceText.getText().length() > 0 && textArea.getSelectedText() != null)
                    textArea.replaceSelection(replaceText.getText());
            }
        });

        // "�滻ȫ��"��ť���¼�����
        replaceAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                textArea.setCaretPosition(0); // �����ŵ��༭����ͷ
                int a = 0, b = 0, replaceCount = 0;

                if (findText.getText().length() == 0) {
                    JOptionPane.showMessageDialog(findDialog, "����д��������!", "��ʾ", JOptionPane.WARNING_MESSAGE);
                    findText.requestFocus(true);
                    return;
                }
                while (a > -1) {

                    int FindStartPos = textArea.getCaretPosition();
                    String str1, str2, str3, str4, strA, strB;
                    str1 = textArea.getText();
                    str2 = str1.toLowerCase();
                    str3 = findText.getText();
                    str4 = str3.toLowerCase();

                    if (matchcase.isSelected()) {
                        strA = str1;
                        strB = str3;
                    } else {
                        strA = str2;
                        strB = str4;
                    }

                    if (up.isSelected()) {
                        if (textArea.getSelectedText() == null) {
                            a = strA.lastIndexOf(strB, FindStartPos - 1);
                        } else {
                            a = strA.lastIndexOf(strB, FindStartPos - findText.getText().length() - 1);
                        }
                    } else if (down.isSelected()) {
                        if (textArea.getSelectedText() == null) {
                            a = strA.indexOf(strB, FindStartPos);
                        } else {
                            a = strA.indexOf(strB, FindStartPos - findText.getText().length() + 1);
                        }

                    }

                    if (a > -1) {
                        if (up.isSelected()) {
                            textArea.setCaretPosition(a);
                            b = findText.getText().length();
                            textArea.select(a, a + b);
                        } else if (down.isSelected()) {
                            textArea.setCaretPosition(a);
                            b = findText.getText().length();
                            textArea.select(a, a + b);
                        }
                    } else {
                        if (replaceCount == 0) {
                            JOptionPane.showMessageDialog(findDialog, "�Ҳ��������ҵ�����!", "���±�", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(findDialog, "�ɹ��滻" + replaceCount + "��ƥ������!", "�滻�ɹ�", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    if (replaceText.getText().length() == 0 && textArea.getSelectedText() != null) {
                        textArea.replaceSelection("");
                        replaceCount++;
                    }
                    if (replaceText.getText().length() > 0 && textArea.getSelectedText() != null) {
                        textArea.replaceSelection(replaceText.getText());
                        replaceCount++;
                    }
                }// end while
            }
        }); /* "�滻ȫ��"��ť���¼�������� */

        // "������һ��"��ť�¼�����
        searchNext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int a = 0, b = 0;
                int FindStartPos = textArea.getCaretPosition();
                String str1, str2, str3, str4, strA, strB;
                str1 = textArea.getText();
                str2 = str1.toLowerCase();
                str3 = findText.getText();
                str4 = str3.toLowerCase();
                // "���ִ�Сд"��CheckBox��ѡ��
                if (matchcase.isSelected()) {
                    strA = str1;
                    strB = str3;
                } else {
                    strA = str2;
                    strB = str4;
                }

                if (up.isSelected()) {
                    if (textArea.getSelectedText() == null) {
                        a = strA.lastIndexOf(strB, FindStartPos - 1);
                    } else {
                        a = strA.lastIndexOf(strB, FindStartPos - findText.getText().length() - 1);
                    }
                } else if (down.isSelected()) {
                    if (textArea.getSelectedText() == null) {
                        a = strA.indexOf(strB, FindStartPos);
                    } else {
                        a = strA.indexOf(strB, FindStartPos - findText.getText().length() + 1);
                    }

                }
                if (a > -1) {
                    if (up.isSelected()) {
                        textArea.setCaretPosition(a);
                        b = findText.getText().length();
                        textArea.select(a, a + b);
                    } else if (down.isSelected()) {
                        textArea.setCaretPosition(a);
                        b = findText.getText().length();
                        textArea.select(a, a + b);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "�Ҳ��������ҵ�����!", "���±�", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });/* "������һ��"��ť�¼�������� */
        // "ȡ��"��ť���¼�����
        JButton cancel = new JButton("ȡ��");
        cancel.setPreferredSize(new Dimension(150, 22));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findDialog.dispose();
            }
        });

        // ����"�������滻"�Ի���Ľ���
        JPanel bottomPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel topPanel = new JPanel();

        JPanel direction = new JPanel();
        direction.setBorder(BorderFactory.createTitledBorder("���� "));
        direction.add(up);
        direction.add(down);
        direction.setPreferredSize(new Dimension(170, 100));
        JPanel replacePanel = new JPanel();
        replacePanel.setLayout(new GridLayout(2, 1));
        replacePanel.add(replace);
        replacePanel.add(replaceAll);

        topPanel.add(searchContentLabel);
        topPanel.add(findText);
        topPanel.add(searchNext);
        centerPanel.add(replaceContentLabel);
        centerPanel.add(replaceText);
        centerPanel.add(replacePanel);
        bottomPanel.add(matchcase);
        bottomPanel.add(direction);
        bottomPanel.add(cancel);

        con.add(topPanel);
        con.add(centerPanel);
        con.add(bottomPanel);

        // ����"�������滻"�Ի���Ĵ�С���ɸ��Ĵ�С(��)��λ�úͿɼ���
        findDialog.setSize(500, 300);
        findDialog.setResizable(true);
        findDialog.setLocation(230, 280);
        findDialog.setVisible(true);
    }
    
    private void loadSettings() throws ParserConfigurationException, SAXException, IOException
    {
    	PersonalSettings.loadSettings();
    	textArea.setFont(PersonalSettings.font);
    	textArea.setBackground(PersonalSettings.background);
    	textArea.setForeground(PersonalSettings.foreground);
    	toolState.setVisible(PersonalSettings.showStatusBar);
    	itemStatement.setSelected(PersonalSettings.showStatusBar);
    	updateRecentFileMenu();

    	
    }
    private void updateCurrentFileList(String newFile)
    {
    	//XXX: combobox update
    	if(GlobalVariable.currentFileList.contains(newFile))
    	{
    		return;
    	}
    	else
    	{
    		GlobalVariable.currentFileList.add(newFile);
    		comboBox.addItem(newFile);
    		comboBox.setSelectedItem(newFile);
    	}
    }
    
    private void updateRecentFileMenu()
    {
    	itemRecentFiles.removeAll();
    	for(String s: PersonalSettings.recentFiles)
    	{
    		final JMenuItem item = new JMenuItem(s);
    		item.addActionListener
    		(
    			new ActionListener() {
    			public void actionPerformed(ActionEvent e)
    			{
    				openRecentFile(item.getText());
    			}
    		});
			itemRecentFiles.add(item);
    	}
    }
    
    private void backup() throws IOException
    {
    	FileWriter fw = new FileWriter(new File("Buffer.txt"));
    	fw.write(textArea.getText());
    	fw.close();
    }
    
    //XXX
    public static void timingSavingAction() throws IOException
    {
    	if(currentPath == null)
    	{
        	FileWriter fw = new FileWriter(new File("Buffer.txt"));
        	fw.write(textArea.getText());
        	fw.close();
    		return;
    	}
    	else
    	{
        	FileWriter fw = new FileWriter(new File(currentPath));
        	fw.write(textArea.getText());
        	fw.close();
        	flag=3;
    	}
    }
    
    
    //XXX : download action
    private void downloadAction()
    {
    	System.out.println("Download is clicked!!!");
    	System.out.println("Current File Path=" + currentPath);
    	System.out.println("Current File Name=" + currentFileName);
    	try {
			CloudService.downloadFile(currentFileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    //XXX: upload action
    private void uploadAction()
    {
    	System.out.println("Upload is clicked!");
    	System.out.println("Current File Path=" + currentPath);
    	System.out.println("Current File Name=" + currentFileName);
    	try {
			CloudService.uploadFileByPath(currentPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    //XXX: build java action
    private void buildJavaAction()
    {
    	System.out.println("Building Java...");
    	try {
			BuildJava.buildJava(textArea.getText());
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
    	
    	System.out.println("Build java sucessfully!");
    }
    
    //XXX: build c/c++ action
    private void buildCplusplusAction()
    {
    	System.out.println("Building C/C++...");
    	try {
			BuildCplusplus.buildCplusplus(textArea.getText());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    private void buildPythonAction()
    {
    	System.out.println("Building Python...");
    	try {
			BuildPython.buildPython(textArea.getText());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    private void runCalculator()
    {
    	try {
    		System.out.println("running calculator...");
			SystemApi.runCmd("calc");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    private void runExplorer()
    {
    	if(currentPath == null)
    		return;
    	try {
			SystemApi.runCmd("explorer " + currentPath.substring(0,currentPath.lastIndexOf("\\")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    private void runSnippingTool()
    {
    	System.out.println("running snipping tool...");
    	try {
			SystemApi.runCmd("cmd /c start SnippingTool");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    private void runMagnify()
    {
    	try {
			SystemApi.runCmd("cmd /c start magnify");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    private void runOsk()
    {
    	try {
			SystemApi.runCmd("cmd /c start osk");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    private void changeFont()
    {
        // ��������ѡ��������������ΪԤ��ֵ
        MQFontChooser fontChooser = new MQFontChooser(textArea.getFont());
        fontChooser.setSize(500, 500);
        fontChooser.showFontDialog(this);
        Font font = fontChooser.getSelectFont();
        // ���������õ�JTextArea��
        textArea.setFont(font);
        
        //���¸�������-����
        PersonalSettings.font = font;
        //�����к���ʾ
//        lineOrder.setLineHeight(textArea.getRows());
//        this.repaint();
    }
    
    private void buildAction()
    {
    	if(currentFileName.indexOf(".c")!=-1 || currentFileName.indexOf(".cpp")!=-1)
    		buildCplusplusAction();
    	else if(currentFileName.indexOf(".py")!=-1)
    		buildPythonAction();
    	else if(currentFileName.indexOf(".java")!=-1)
    		buildJavaAction();
    	else 
    		JOptionPane.showMessageDialog(this, "������ѡ���������", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //XXX:����textArea�Ľӿ�
    public static void setTextArea(String content)
    {
    	textArea.setText(content);
    }
    
    //XXX:get file path
    public static String getCurrentFilePath() {
		return currentPath;
	}
    
    //XXX:TinyCCompiler
    private void runTinyCCompiler()
    {
    	try 
    	{
    		if(!textArea.getText().equals("") && textArea.getText()!=null)
    			RunTinyCCompiler.run(this,textArea.getText());
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }

}