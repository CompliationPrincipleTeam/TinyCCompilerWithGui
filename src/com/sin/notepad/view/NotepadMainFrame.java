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
     * 序列号
     */
    private static final long serialVersionUID = 8585210209467333480L;
    //内容面板
    private JPanel contentPanel;
    //内容面板北区
    private JPanel contentPanelNorth;
    //编辑区
    private static JTextArea textArea;
    //编辑区上方的ComboBox
    private JComboBox<String> comboBox;
    //private JTextPane textArea;
    //打开菜单项
    private JMenuItem itemOpen;
    //保存菜单项
    private JMenuItem itemSave;
    
    //1：新建 
    //2：修改过
    //3：保存过的
    static int flag=0;

    //当前文件名
    String currentFileName=null;
    
     PrintJob  p=null;//声明一个要打印的对象
     Graphics  g=null;//要打印的对象
    
    //当前文件路径
    static String currentPath=null;
    
    //背景颜色
    JColorChooser jcc1=null;
    Color color=Color.BLACK;
    
    //文本的行数和列数
    int linenum = 1;
    int columnnum = 1;
    
    //撤销管理器
    public UndoManager undoMgr = new UndoManager(); 
    
    //剪贴板
    public Clipboard clipboard = new Clipboard("系统剪切板"); 
    
    private JMenuBar menuBar;
    private JMenu itemRecentFiles;		       //最近打开
    private JMenuItem itemExplorer;			   //打开文件所在位置
    private JMenuItem itemSaveAs;              //另存为
    private JMenuItem itemNew;				   //新建
    private JMenuItem itemPage;				   //页面设置
    private JSeparator separator;			   //分隔线
    private JMenuItem itemPrint;			   //打印
    private JMenuItem itemExit;				   //退出
    private JSeparator separator_1;			   //分隔线
    private JMenu itemEdit;					   //编辑
    private JMenu itFormat;					   //格式
    private JMenu itemCheck;				   //查看
    private JMenu itemHelp;					   //帮助
    private JMenu itemBuild;				   //代码构建运行
    private JMenu itemCodeView;				   //语法高亮模式
    private JMenu itemCloudService;			   //上传或下载文件
    private JMenu itemTimingSaving;			   //定时保存
    private JMenu itemMore;					   //更多
    private JMenuItem itemSearchForHelp;	   //查看帮助
    private JMenuItem itemAboutNotepad;		   //关于记事本
    private JMenuItem itemUndo;				   //撤销
    private JMenuItem itemCut;				   //剪切
    private JMenuItem itemCopy;				   //复制
    private JMenuItem itemPaste;			   //粘贴
    private JMenuItem itemDelete;			   //删除
    private JMenuItem itemFind;				   //查找
    private JMenuItem itemFindNext;			   //查找下一个
    private JMenuItem itemReplace;			   //替换
    private JMenuItem itemTurnTo;			   //转到
    private JMenuItem itemSelectAll;		   //全选
    private JMenuItem itemTime;				   //日期/时间
    private JMenuItem itemFont;				   //字体
    private JMenuItem itemColor;			   //字体颜色
    private JMenuItem itemFontColor;		   //背景颜色
    private JMenuItem itemBuildCplusplus;	   //构建C++
    private JMenuItem itemBuildJava;		   //构建Java
    private JMenuItem itemBuildPython;		   //构建Python
    private JMenuItem itemUpload;			   //上传文件
    private JMenuItem itemDownload;			   //下载文件
    private JMenuItem itemCal;				//计算器
    private JMenuItem itemSnip;				//截图
    private JMenuItem itemMagnify;			//放大镜
    private JMenuItem itemOsk;				//屏幕键盘，兼容Windows平板电脑
    private ButtonGroup codeViewRadioButtonGroup;
    private ButtonGroup itemTimingButtonGruop;
    private JRadioButtonMenuItem itemCodeViewCplusplus;   //C++高亮
    private JRadioButtonMenuItem itemCodeViewJava;		   //Java高亮
    private JRadioButtonMenuItem itemCodeViewPython;	   //python高亮
    private JRadioButtonMenuItem itemCommonView;		   //基本视图
    private JRadioButtonMenuItem item05Secs;			   //用于演示
    private JRadioButtonMenuItem item15Mins;
    private JRadioButtonMenuItem item30Mins;
    private JRadioButtonMenuItem item60Mins;
    private JRadioButtonMenuItem itemUntiming;
    private JCheckBoxMenuItem itemNextLine;	   //自动换行
    private JScrollPane scrollPane;			   //滚动栏
    private JCheckBoxMenuItem itemStatement;   //状态栏
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
    private JPopupMenu popupMenu;         //右键弹出菜单
    private JMenuItem popM_Undo;		  //撤销
    private JMenuItem popM_Cut;			  //剪切
    private JMenuItem popM_Copy;		  //复制
    private JMenuItem popM_Paste;		  //粘贴
    private JMenuItem popM_Delete;		  //删除
    private JMenuItem popM_SelectAll;	  //全选
    private JMenuItem popM_toLeft;		  //从右到左的阅读顺序
    private JMenuItem popM_showUnicode;   //显示Unicode控制字符
    private JMenuItem popM_closeIMe;      //关闭IME
    private JMenuItem popM_InsertUnicode; //插入Unicode控制字符
    private JMenuItem popM_RestartSelect; //汉字重选
    private JSeparator separator_2;       //分隔线
    private JSeparator separator_3;		  //分隔线
    private JSeparator separator_4;       //分隔线
    private JSeparator separator_5;       //分隔线
    private JMenuItem itemRedo;			  //恢复
    private JSeparator separator_6;		  //分隔线
    private JSeparator separator_7;		  //分隔线
    private JSeparator separator_8;		  //分隔线
    private JMenuItem popM_Redo;		  //恢复
    private LineOrder lineOrder;
    /**
     * Create the frame.
     * 构造函数
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
        setTitle("无标题");    
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		System.out.println("Close Button is clicked!!");
        		try {
					PersonalSettings.saveSettings();
					FailHandler.writeLog(FailureType.Normal);
					// XXX:退出是否备份到Buffer.txt
					backup();
				} catch (TransformerException | ParserConfigurationException | IOException e1) {
					e1.printStackTrace();
				}
        	}
        }); 
        setBounds(100, 100, 1000, 772);
        
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu itemFile = new JMenu("文件(F)");
        itemFile.setMnemonic('F');	//设置快捷键"F"
        menuBar.add(itemFile);
        
        
        itemNew = new JMenuItem("新建(N)",'N');
        itemNew.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
                java.awt.Event.CTRL_MASK));  //设置快捷键Ctrl+"N"
        itemNew.addActionListener(this);
        itemFile.add(itemNew);
        
        itemOpen = new JMenuItem("打开(O)",'O');
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
                            java.awt.Event.CTRL_MASK));  //设置快捷键Ctrl+"O"
        itemOpen.addActionListener(this);
        itemFile.add(itemOpen);
        
        itemSave = new JMenuItem("保存(S)");
        itemSave.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                java.awt.Event.CTRL_MASK));   //设置快捷键Ctrl+"S"
        itemSave.addActionListener(this);
        itemFile.add(itemSave);
        
        itemSaveAs = new JMenuItem("另存为(A)");
        itemSaveAs.addActionListener(this);
        itemFile.add(itemSaveAs);
        
        itemRecentFiles = new JMenu("最近打开");
        itemFile.add(itemRecentFiles);
        
        itemExplorer = new JMenuItem("打开文件所在位置");
        itemExplorer.addActionListener(this);
        itemFile.add(itemExplorer);
        
        
        separator = new JSeparator();  //添加分隔线
        itemFile.add(separator);
        
        itemPage = new JMenuItem("页面设置(U)",'U');
        itemPage.addActionListener(this);
        itemFile.add(itemPage);
        
        itemPrint = new JMenuItem("打印(P)...",'P');
        itemPrint.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P,
                java.awt.Event.CTRL_MASK));   //置快捷键Ctrl+"P"
        itemPrint.addActionListener(this);
        itemFile.add(itemPrint);
        
        separator_1 = new JSeparator();
        itemFile.add(separator_1);
        
        itemExit = new JMenuItem("退出(X)",'X');
        itemExit.addActionListener(this);
        itemFile.add(itemExit);
        
        itemEdit = new JMenu("编辑(E)");
        itemEdit.setMnemonic('E');
        menuBar.add(itemEdit);
        
        itemUndo = new JMenuItem("撤销(U)",'U');
        itemUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,
                java.awt.Event.CTRL_MASK));  //设置快捷键Ctrl+"Z"
        itemUndo.addActionListener(this);
        itemEdit.add(itemUndo);
        
        itemRedo = new JMenuItem("恢复(R)");
        itemRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
                java.awt.Event.CTRL_MASK));  //设置快捷键Ctrl+"R"
        itemRedo.addActionListener(this);
        itemEdit.add(itemRedo);
        
        itemCut = new JMenuItem("剪切(T)",'T');
        itemCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
                java.awt.Event.CTRL_MASK));  //设置快捷键Ctrl+"X"
        itemCut.addActionListener(this);
        
        separator_6 = new JSeparator();
        itemEdit.add(separator_6);
        itemEdit.add(itemCut);
        
        itemCopy = new JMenuItem("复制(C)",'C');
        itemCopy.addActionListener(this);
        itemCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
                java.awt.Event.CTRL_MASK));  //设置快捷键Ctrl+"C"
        itemEdit.add(itemCopy);
        
        itemPaste = new JMenuItem("粘贴(P)",'P');
        itemPaste.addActionListener(this);
        itemPaste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
                java.awt.Event.CTRL_MASK));  //设置快捷键Ctrl+"V"
        itemEdit.add(itemPaste);
        
        itemDelete = new JMenuItem("删除(L)",'L');
        itemDelete.addActionListener(this);
        itemDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,  
                InputEvent.CTRL_MASK));    //设置快捷键Ctrl+"D"
        itemEdit.add(itemDelete);
        
        separator_7 = new JSeparator();
        itemEdit.add(separator_7);
        
        itemFind = new JMenuItem("查找(F)",'F');
        itemFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                Event.CTRL_MASK));   //设置快捷键Ctrl+"F"
        itemFind.addActionListener(this);
        itemEdit.add(itemFind);
        
        itemFindNext = new JMenuItem("查找下一个(N)",'N');
        itemFindNext.setAccelerator(KeyStroke.getKeyStroke("F3"));
        itemFindNext.addActionListener(this);
        itemEdit.add(itemFindNext);
        
        itemReplace = new JMenuItem("替换(R)",'R');
        itemReplace.addActionListener(this);
        itemReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                Event.CTRL_MASK));  //设置快捷键Ctrl+"H"
        itemEdit.add(itemReplace);
        
        itemTurnTo = new JMenuItem("转到(G)",'G');
        itemTurnTo.addActionListener(this);
        itemTurnTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                Event.CTRL_MASK));  //设置快捷键Ctrl+"G"
        itemEdit.add(itemTurnTo);
        
        itemSelectAll = new JMenuItem("全选(A)",'A');
        itemSelectAll.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,
                java.awt.Event.CTRL_MASK));  //设置快捷键Ctrl+"A"
        itemSelectAll.addActionListener(this);
        
        separator_8 = new JSeparator();
        itemEdit.add(separator_8);
        itemEdit.add(itemSelectAll);
        
        itemTime = new JMenuItem("时间/日期(D)",'D');
        itemTime.addActionListener(this);
        itemTime.setAccelerator(KeyStroke.getKeyStroke("F5"));
        itemEdit.add(itemTime);
        
        itFormat = new JMenu("格式(O)");
        itFormat.setMnemonic('O');
        menuBar.add(itFormat);
        
        itemNextLine = new JCheckBoxMenuItem("自动换行(W)");
        itemNextLine.addActionListener(this);
        itFormat.add(itemNextLine);
        
        itemFont = new JMenuItem("字体大小(F)...");
        itemFont.addActionListener(this);
        itFormat.add(itemFont);
        
        itemColor = new JMenuItem("背景颜色(C)...");
        itemColor.addActionListener(this);
        itFormat.add(itemColor);
        
        itemFontColor = new JMenuItem("字体颜色(I)...");
        itemFontColor.addActionListener(this);
        itFormat.add(itemFontColor);
        
        itemCheck = new JMenu("查看(V)");
        itemCheck.setMnemonic('V');
        menuBar.add(itemCheck);
        
        itemStatement = new JCheckBoxMenuItem("状态栏(S)");
        itemStatement.setSelected(true);
        itemStatement.addActionListener(this);
        itemCheck.add(itemStatement);
        
        
        itemBuild = new JMenu("构建(B)");
        itemBuild.setMnemonic('B');
        itemBuildCplusplus = new JMenuItem("构建C/C++");
        itemBuildJava = new JMenuItem("构建Java");
        itemBuildPython = new JMenuItem("构建Python");
        itemBuildCplusplus.addActionListener(this);
        itemBuildJava.addActionListener(this);
        itemBuildPython.addActionListener(this);
        itemBuild.add(itemBuildCplusplus);
        itemBuild.add(itemBuildJava);
        itemBuild.add(itemBuildPython);
        menuBar.add(itemBuild);
        
        itemCloudService = new JMenu("云端(C)");
        itemCloudService.setMnemonic('C');
        itemUpload = new JMenuItem("上传(U)");
        itemDownload = new JMenuItem("下载(D)");
        itemUpload.addActionListener(this);
        itemDownload.addActionListener(this);
        
        itemCloudService.add(itemUpload);
        itemCloudService.add(itemDownload);
        menuBar.add(itemCloudService);
        
        initCodeViewMenu();
        initMoreMenu();
        
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        //设置边框布局
        contentPanel.setLayout(new BorderLayout(0, 0));
//        XXX: annote by sin
//        setContentPane(contentPanel);
        
        textArea = new JTextArea();
        //add by sin
//		textArea = new JTextPane();
//		textArea.getDocument().addDocumentListener(new SyntaxHighlighter(textArea));
        
        //VERTICAL垂直    HORIZONTAL水平
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        //contentPane2=new JPanel();
        //contentPane2.setSize(10,textArea.getSize().height);
        //contentPane.add(contentPane2, BorderLayout.WEST);
        lineOrder = new LineOrder(); //添加行号
        scrollPane.setRowHeaderView(lineOrder);
        
        popupMenu = new JPopupMenu();
        addPopup(textArea, popupMenu);
        
        popM_Undo = new JMenuItem("撤销(U)");
        popM_Undo.addActionListener(this);
        popupMenu.add(popM_Undo);
        
        popM_Redo = new JMenuItem("恢复(R)");
        popM_Redo.addActionListener(this);
        popupMenu.add(popM_Redo);
        
        separator_2 = new JSeparator();
        popupMenu.add(separator_2);
        
        popM_Cut = new JMenuItem("剪切(T)");
        popM_Cut.addActionListener(this);
        popupMenu.add(popM_Cut);
        
        popM_Copy = new JMenuItem("复制(C)");
        popM_Copy.addActionListener(this);
        popupMenu.add(popM_Copy);
        
        popM_Paste = new JMenuItem("粘贴(P)");
        popM_Paste.addActionListener(this);
        popupMenu.add(popM_Paste);
        
        popM_Delete = new JMenuItem("删除(D)");
        popM_Delete.addActionListener(this);
        popupMenu.add(popM_Delete);
        
        separator_3 = new JSeparator();
        popupMenu.add(separator_3);
        
        popM_SelectAll = new JMenuItem("全选(A)");
        popM_SelectAll.addActionListener(this);
        popupMenu.add(popM_SelectAll);
        
        separator_4 = new JSeparator();
        popupMenu.add(separator_4);
        
        popM_toLeft = new JMenuItem("从右到左的阅读顺序(R)");
        popM_toLeft.addActionListener(this);
        popupMenu.add(popM_toLeft);
        
        popM_showUnicode = new JMenuItem("显示Unicode控制字符(S)");
        popM_showUnicode.addActionListener(this);
        popupMenu.add(popM_showUnicode);
        
        popM_InsertUnicode = new JMenuItem("插入Unicode控制字符(I)");
        popM_InsertUnicode.addActionListener(this);
        popupMenu.add(popM_InsertUnicode);
        
        separator_5 = new JSeparator();
        popupMenu.add(separator_5);
        
        popM_closeIMe = new JMenuItem("关闭IME(L)");
        popM_closeIMe.addActionListener(this);
        popupMenu.add(popM_closeIMe);
        
        popM_RestartSelect = new JMenuItem("汉字重选(R)");
        popM_RestartSelect.addActionListener(this);
        popupMenu.add(popM_RestartSelect);
        //添加到面板中【中间】
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        //northPanel -> North
        //XXX:添加north面板
        initNorthPanel();
        contentPanel.add(contentPanelNorth, BorderLayout.NORTH);
        
        //添加撤销管理器
        textArea.getDocument().addUndoableEditListener(undoMgr);
                
        //设置状态栏
        toolState = new JToolBar();
        toolState.setSize(textArea.getSize().width, 10);//toolState.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolStateLabel = new JLabel("    当前系统时间：" + hour + ":" + min + ":" + second+" ");
        toolState.add(toolStateLabel);  //添加系统时间
        toolState.addSeparator();
        label2 = new JLabel("    第 " + linenum + " 行, 第 " + columnnum+" 列  ");
        toolState.add(label2);  //添加行数列数
        toolState.addSeparator();
        
        label3 = new JLabel("    一共 " +length+" 字  ");
        toolState.add(label3);  //添加字数统计
        textArea.addCaretListener(new CaretListener() {        //记录行数和列数
            public void caretUpdate(CaretEvent e) {
                //sum=0;
                JTextArea editArea = (JTextArea)e.getSource();
 
                try {
                    int caretpos = editArea.getCaretPosition();
                    linenum = editArea.getLineOfOffset(caretpos);
                    columnnum = caretpos - textArea.getLineStartOffset(linenum);
                    linenum += 1;
                    label2.setText("    第 " + linenum + " 行, 第 " + (columnnum+1)+" 列  ");
                    //sum+=columnnum+1;
                    //length+=sum;
                    length=NotepadMainFrame.textArea.getText().toString().length();
                    label3.setText("    一共 " +length+" 字  ");
                }
                catch(Exception ex) { }
            }});
        
        contentPanel.add(toolState, BorderLayout.SOUTH);  //将状态栏添加到面板上
        toolState.setVisible(true);
        toolState.setFloatable(false);
        Clock clock=new Clock();
        clock.start();//开启时钟线程
        
        
        
        // 创建弹出菜单
        final JPopupMenu jp=new JPopupMenu();    //创建弹出式菜单，下面三项是菜单项
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3)//只响应鼠标右键单击事件
                {
                    jp.show(e.getComponent(),e.getX(),e.getY());//在鼠标位置显示弹出式菜单
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
        itemCodeView = new JMenu("视图(S)");
        itemCodeView.setMnemonic('S');
        itemCommonView = new JRadioButtonMenuItem("基本视图");
        itemCodeViewCplusplus = new JRadioButtonMenuItem("C/C++视图");
        itemCodeViewJava = new JRadioButtonMenuItem("Java视图");
        itemCodeViewPython = new JRadioButtonMenuItem("Python视图");
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
    	itemMore = new JMenu("更多(M)");
    	itemMore.setMnemonic('M');
    	
        
        itemCal = new JMenuItem("计算器");
        itemCal.addActionListener(this);
        itemMore.add(itemCal);
        itemSnip = new JMenuItem("截图");
        itemSnip.addActionListener(this);
        itemMore.add(itemSnip);
        itemMagnify = new JMenuItem("放大镜");
        itemMagnify.addActionListener(this);
        itemMore.add(itemMagnify); 
        itemOsk = new JMenuItem("屏幕键盘");
        itemOsk.addActionListener(this);
        itemMore.add(itemOsk);
        
        
    	itemTimingSaving = new JMenu("定时保存");
    	item05Secs = new JRadioButtonMenuItem("5秒");
    	item15Mins = new JRadioButtonMenuItem("15分钟");
    	item30Mins = new JRadioButtonMenuItem("30分钟");
    	item60Mins = new JRadioButtonMenuItem("60分钟");
    	itemUntiming = new JRadioButtonMenuItem("无");
    	
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
        itemHelp = new JMenu("帮助(H)");
        itemHelp.setMnemonic('H');
        
        itemSearchForHelp = new JMenuItem("查看帮助(H)",'H');
        itemSearchForHelp.addActionListener(this);
        itemHelp.add(itemSearchForHelp);
        
        itemAboutNotepad = new JMenuItem("关于记事本(A)",'A');
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
				//根据后缀build code
				//buildAction();
				/*
				 * 暂时改为编译原理的调用接口
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
     * 是否有变化
     */
    private void isChanged() {
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //在这里我进行了对使用快捷键，但是没有输入字符却没有改变textArea中内容的判断
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
     * 新建的或保存过的退出只有两个选择
     */
    private void MainFrameWindowListener() throws IOException{
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                if(flag==2 && currentPath==null){
                    //这是弹出小窗口
                    //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
                    int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到无标题?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if(result==JOptionPane.OK_OPTION){
                        NotepadMainFrame.this.saveAs();
                    }else if(result==JOptionPane.NO_OPTION){
                        NotepadMainFrame.this.dispose();
                        NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                    NotepadMainFrame.this.dispose();
                    NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }else if(flag==2 && currentPath!=null){
                    //这是弹出小窗口
                    //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
                    int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到"+currentPath+"?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if(result==JOptionPane.OK_OPTION){
                        NotepadMainFrame.this.save();
                    }else if(result==JOptionPane.NO_OPTION){
                        NotepadMainFrame.this.dispose();
                        NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                }else{
                    //这是弹出小窗口
                    int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "确定关闭？", "系统提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
     * 行为动作
     */
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==itemOpen)
        {            //打开
            openFile();
        }
        else if(e.getSource()==itemSave)
        {        //保存
            //如果该文件是打开的，就可以直接保存
            save();
        }
        else if(e.getSource()==itemSaveAs)
        {    //另存为
            saveAs();
        }
        else if(e.getSource()==itemNew)
        {        //新建
            newFile();
        }
        else if(e.getSource()==itemExit)
        {        //退出
            exit();  
        }
        else if(e.getSource()==itemPage){        //页面设置
            ///页面设置，百度到的，不知道具体的用法
            PageFormat pf = new PageFormat();
            PrinterJob.getPrinterJob().pageDialog(pf); 
        }
        else if(e.getSource()==itemPrint){        //打印
            //打印机
            Print();
        }
        else if(e.getSource() == itemExplorer)
        {
        	runExplorer();
        }
        else if(e.getSource()==itemUndo || e.getSource()==popM_Undo){        //撤销
            if(undoMgr.canUndo()){
                undoMgr.undo();
            }
        }else if(e.getSource()==itemRedo || e.getSource()==popM_Redo){        //恢复
            if(undoMgr.canRedo()){
                undoMgr.redo();
            }
        }else if(e.getSource()==itemCut || e.getSource()==popM_Cut){        //剪切
            cut();
        }else if(e.getSource()==itemCopy || e.getSource()==popM_Copy){        //复制
            copy();
        }else if(e.getSource()==itemPaste || e.getSource()==popM_Paste){    //粘贴
            paste();
        }else if(e.getSource()==itemDelete || e.getSource()==popM_Delete){    //删除
            String tem=textArea.getText().toString();
            textArea.setText(tem.substring(0,textArea.getSelectionStart())); 
        }else if(e.getSource()==itemFind){        //查找
            mySearch();
        }else if(e.getSource()==itemFindNext){    //查找下一个
            mySearch();
        }else if(e.getSource()==itemReplace){    //替换
            mySearch();
        }else if(e.getSource()==itemTurnTo){    //转到
            turnTo();
        }else if(e.getSource()==itemSelectAll || e.getSource()==popM_SelectAll){    //选择全部
            textArea.selectAll();
        }else if(e.getSource()==itemTime){        //时间/日期
            textArea.append(hour+":"+min+" "+c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH)+"\n");
        }else if(e.getSource()==itemNextLine){    //设置自动换行
            //设置文本区的换行策略。如果设置为 true，则当行的长度大于所分配的宽度时，将换行。此属性默认为 false。 
            if(itemNextLine.isSelected()){
                textArea.setLineWrap(true);
            }else{
                textArea.setLineWrap(false);
            }
        }else if(e.getSource()==itemFont){        //设置字体大小
        	changeFont();
            
        }else if(e.getSource()==itemColor){        //设置背景颜色
            jcc1 = new JColorChooser();
            JOptionPane.showMessageDialog(this, jcc1,"选择背景颜色颜色",-1);
            color = jcc1.getColor();
            textArea.setBackground(color);
            
            //更新个人设置-背景颜色
            PersonalSettings.background = color;
        }else if(e.getSource()==itemFontColor){    //设置字体颜色
            jcc1=new JColorChooser();
            JOptionPane.showMessageDialog(this, jcc1, "选择字体颜色", -1);
            color = jcc1.getColor();
            //String string=textArea.getSelectedText();
            textArea.setForeground(color);
            
            //更新个人设置-背景颜色
            PersonalSettings.foreground = color;
        }else if(e.getSource()==itemStatement){    //设置状态
            if(itemStatement.isSelected()){
                //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                toolState.setVisible(true);
            }else{
                //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                toolState.setVisible(false);
            }
            //更新个人设置-状态栏显示
            PersonalSettings.showStatusBar = itemStatement.isSelected();
        }else if(e.getSource()==itemSearchForHelp){
            JOptionPane.showMessageDialog(this, "NO HELP","Help",1);
        }else if(e.getSource()==itemAboutNotepad){
            JOptionPane.showMessageDialog(this, "sin's Notepad++\nAuthor:161630230-冼健邦","软件说明 ",1);
        }
        //扩展功能监听事件
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
        final JDialog gotoDialog = new JDialog(this, "转到下列行");
        JLabel gotoLabel = new JLabel("行数(L):");
        final JTextField linenum = new JTextField(5);
        linenum.setText("1");
        linenum.selectAll();

        JButton okButton = new JButton("确定");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int totalLine = textArea.getLineCount();
                int[] lineNumber = new int[totalLine + 1];
                String s = textArea.getText();
                int pos = 0, t = 0;

                while (true) {
                    pos = s.indexOf('\12', pos);
                    // System.out.println("引索pos:"+pos);
                    if (pos == -1)
                        break;
                    lineNumber[t++] = pos++;
                }

                int gt = 1;
                try {
                    gt = Integer.parseInt(linenum.getText());
                } catch (NumberFormatException efe) {
                    JOptionPane.showMessageDialog(null, "请输入行数!", "提示", JOptionPane.WARNING_MESSAGE);
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

                gotoDialog.dispose();//关闭窗体
            }

        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gotoDialog.dispose();
            }
        });

        //将组件添加到容器里
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
     * 退出按钮，和窗口的红叉实现一样的功能
     */
    private void exit() {
    	
        if(flag==2 && currentPath==null){
            //这是弹出小窗口
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到无标题?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                NotepadMainFrame.this.saveAs();
            }else if(result==JOptionPane.NO_OPTION){
                NotepadMainFrame.this.dispose();
                NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }else if(flag==2 && currentPath!=null){
            //这是弹出小窗口
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到"+currentPath+"?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                NotepadMainFrame.this.save();
            }else if(result==JOptionPane.NO_OPTION){
                NotepadMainFrame.this.dispose();
                NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }else{
            //这是弹出小窗口
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "确定关闭？", "系统提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                NotepadMainFrame.this.dispose();
                NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }
    }
    /*===================================================================*/


    /*===============================4====================================*/
    /**
     * 新建文件，只有改过的和保存过的需要处理
     */
    private void newFile() {
        if(flag==0 || flag==1){        //刚启动记事本为0，刚新建文档为1
            return;
        }else if(flag==2 && currentPath==null){        //修改后
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到无标题?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();        //另存为                
            }else if(result==JOptionPane.NO_OPTION){
                NotepadMainFrame.textArea.setText("");
                this.setTitle("无标题");
                flag=1;
            }
            return;
        }else if(flag==2 && NotepadMainFrame.currentPath!=null ){
            //2、（保存的文件为3）条件下修改后
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到"+currentPath+"?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.save();        //直接保存，有路径
            }else if(result==JOptionPane.NO_OPTION){
                textArea.setText("");
                this.setTitle("无标题");
                flag=1;
            }
        }else if(flag==3){        //保存的文件
            textArea.setText("");
            flag=1;
            this.setTitle("无标题");
        }
    }
    /*===================================================================*/
    
    
    /*===============================5====================================*/
    /**
     * 另存为
     */
    private void saveAs() {
        //打开保存框
        JFileChooser choose=new JFileChooser(BuildCode.projectPath());
        //选择文件
        int result=choose.showSaveDialog(this);
        if(result==JFileChooser.APPROVE_OPTION){
            //取得选择的文件[文件名是自己输入的]
            File file=choose.getSelectedFile();
            FileWriter fw=null;
            //保存
            try {
                fw=new FileWriter(file);
                fw.write(textArea.getText());
                currentFileName=file.getName();
                currentPath=file.getAbsolutePath();
                //如果比较少，需要写
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
     * 保存
     */
    private void save() {
        if(currentPath==null){
            this.saveAs();
            if(currentPath==null){
                return;
            }
        }
        FileWriter fw=null;
        //保存
        try {
            fw=new FileWriter(new  File(currentPath));
            fw.write(textArea.getText());
            //如果比较少，需要写
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
     * 打开文件
     */
    private void openFile() {
        if(flag==2 && currentPath==null){
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到无标题?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();
            }
        }else if(flag==2 && currentPath!=null){
            //2、（打开的文件2，保存的文件3）条件下修改
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到"+currentPath+"?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.save();
            }
        }
        //打开文件选择框
        JFileChooser choose=new JFileChooser(BuildCode.projectPath());
        //选择文件
        int result=choose.showOpenDialog(this);
        if(result==JFileChooser.APPROVE_OPTION){
            //取得选择的文件
            File file=choose.getSelectedFile();
            //打开已存在的文件，提前将文件名存起来
            currentFileName=file.getName();
            //存在文件全路径
            currentPath=file.getAbsolutePath();
            flag=3;
            this.setTitle(currentPath);
            BufferedReader br=null;
            try {
            	//XXX:UPDATE operation
            	PersonalSettings.updateRecentFiles(currentPath);
            	updateRecentFileMenu();
            	updateCurrentFileList(currentPath);
                //建立文件流[字符流]
                InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"GBK");
                br=new BufferedReader(isr);//动态绑定
                //读取内容
                StringBuffer sb=new StringBuffer();
                String line=null;
                while((line=br.readLine())!=null){
                    sb.append(line+SystemApi.LINE_SEPARATOR);
                }
                //显示在文本框[多框]
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
     * XXX ：通过“最近打开”选择文件
     */
    private void openRecentFile(String path)
    {
        if(flag==2 && currentPath==null){
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到无标题?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();
            }
        }else if(flag==2 && currentPath!=null){
            //2、（打开的文件2，保存的文件3）条件下修改
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到"+currentPath+"?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.save();
            }
        }
        //取得选择的文件
        File file=new File(path);
        //打开已存在的文件，提前将文件名存起来
        currentFileName=file.getName();
        //存在文件全路径
        currentPath=path;
        flag=3;
        this.setTitle(currentPath);
        BufferedReader br=null;
        try {
        	//XXX:UPDATE operation
        	PersonalSettings.updateRecentFiles(currentPath);
        	updateRecentFileMenu();
        	updateCurrentFileList(currentPath);
            //建立文件流[字符流]
            InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"GBK");
            br=new BufferedReader(isr);//动态绑定
            //读取内容
            StringBuffer sb=new StringBuffer();
            String line=null;
            while((line=br.readLine())!=null){
                sb.append(line+SystemApi.LINE_SEPARATOR);
            }
            //显示在文本框[多框]
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
            p = getToolkit().getPrintJob(this,"ok",null);//创建一个Printfjob 对象 p
            g = p.getGraphics();//p 获取一个用于打印的 Graphics 的对象
            //g.translate(120,200);//改变组建的位置 
            textArea.printAll(g);
            p.end();//释放对象 g  
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
        //标记开始位置
        int start = textArea.getSelectionStart();
        //标记结束位置
        int end = textArea.getSelectionEnd();
        //删除所选段
        textArea.replaceRange("", start, end);
        
    }
    
    public void copy(){
        //拖动选取文本
        String temp = textArea.getSelectedText();
        //把获取的内容复制到连续字符器，这个类继承了剪贴板接口
        StringSelection text = new StringSelection(temp);
        //把内容放在剪贴板
        this.clipboard.setContents(text, null);
    }
    
     public void paste(){
         //Transferable接口，把剪贴板的内容转换成数据
         Transferable contents = this.clipboard.getContents(this);
         //DataFalvor类判断是否能把剪贴板的内容转换成所需数据类型
         DataFlavor flavor = DataFlavor.stringFlavor;
         //如果可以转换
         if(contents.isDataFlavorSupported(flavor)){
             String str;
             try {//开始转换
                str=(String)contents.getTransferData(flavor);
                //如果要粘贴时，鼠标已经选中了一些字符
                if(textArea.getSelectedText()!=null){
                    //定位被选中字符的开始位置
                    int start = textArea.getSelectionStart();
                    //定位被选中字符的末尾位置
                    int end = textArea.getSelectionEnd();
                    //把粘贴的内容替换成被选中的内容
                    textArea.replaceRange(str, start, end);
                }else{
                    //获取鼠标所在TextArea的位置
                    int mouse = textArea.getCaretPosition();
                    //在鼠标所在的位置粘贴内容
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
        final JDialog findDialog = new JDialog(this, "查找与替换", true);

        Container con = findDialog.getContentPane();
        con.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel searchContentLabel = new JLabel("查找内容(N) :");
        JLabel replaceContentLabel = new JLabel("替换为(P)　 :");
        final JTextField findText = new JTextField(22);
        final JTextField replaceText = new JTextField(22);
        final JCheckBox matchcase = new JCheckBox("区分大小写");
        ButtonGroup bGroup = new ButtonGroup();
        final JRadioButton up = new JRadioButton("向上(U)");
        final JRadioButton down = new JRadioButton("向下(D)");
        down.setSelected(true);
        bGroup.add(up);
        bGroup.add(down);
        JButton searchNext = new JButton("查找下一个(F)");
        JButton replace = new JButton("替换(R)");
        final JButton replaceAll = new JButton("全部替换(A)");
        searchNext.setPreferredSize(new Dimension(150, 22));
        replace.setPreferredSize(new Dimension(150, 22));
        replaceAll.setPreferredSize(new Dimension(150, 22));
        // "替换"按钮的事件处理
        replace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (replaceText.getText().length() == 0 && textArea.getSelectedText() != null)
                    textArea.replaceSelection("");
                if (replaceText.getText().length() > 0 && textArea.getSelectedText() != null)
                    textArea.replaceSelection(replaceText.getText());
            }
        });

        // "替换全部"按钮的事件处理
        replaceAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                textArea.setCaretPosition(0); // 将光标放到编辑区开头
                int a = 0, b = 0, replaceCount = 0;

                if (findText.getText().length() == 0) {
                    JOptionPane.showMessageDialog(findDialog, "请填写查找内容!", "提示", JOptionPane.WARNING_MESSAGE);
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
                            JOptionPane.showMessageDialog(findDialog, "找不到您查找的内容!", "记事本", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(findDialog, "成功替换" + replaceCount + "个匹配内容!", "替换成功", JOptionPane.INFORMATION_MESSAGE);
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
        }); /* "替换全部"按钮的事件处理结束 */

        // "查找下一个"按钮事件处理
        searchNext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int a = 0, b = 0;
                int FindStartPos = textArea.getCaretPosition();
                String str1, str2, str3, str4, strA, strB;
                str1 = textArea.getText();
                str2 = str1.toLowerCase();
                str3 = findText.getText();
                str4 = str3.toLowerCase();
                // "区分大小写"的CheckBox被选中
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
                    JOptionPane.showMessageDialog(null, "找不到您查找的内容!", "记事本", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });/* "查找下一个"按钮事件处理结束 */
        // "取消"按钮及事件处理
        JButton cancel = new JButton("取消");
        cancel.setPreferredSize(new Dimension(150, 22));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findDialog.dispose();
            }
        });

        // 创建"查找与替换"对话框的界面
        JPanel bottomPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel topPanel = new JPanel();

        JPanel direction = new JPanel();
        direction.setBorder(BorderFactory.createTitledBorder("方向 "));
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

        // 设置"查找与替换"对话框的大小、可更改大小(否)、位置和可见性
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
        // 构造字体选择器，参数字体为预设值
        MQFontChooser fontChooser = new MQFontChooser(textArea.getFont());
        fontChooser.setSize(500, 500);
        fontChooser.showFontDialog(this);
        Font font = fontChooser.getSelectFont();
        // 将字体设置到JTextArea中
        textArea.setFont(font);
        
        //更新个人设置-字体
        PersonalSettings.font = font;
        //调整行号显示
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
    		JOptionPane.showMessageDialog(this, "请自行选择代码类型", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //XXX:设置textArea的接口
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