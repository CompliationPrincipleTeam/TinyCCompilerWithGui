package xuefanggang;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author dmrfcoder
 * @date 2019/1/7
 */
public class DynamicStackJframe extends JFrame {

    private ArrayList<JLabel> jLabelList;
    private ArrayList<JLabel> tempJLabelList;
    private long timeInterval = 1000;
    private ArrayList<String> contentStrList;

    private final int windowWidth = 1000;
    private final int windowHeight = 700;
    private final int stackStartX = 30;
    private final int stackStartY = windowHeight - 50;
    private final int jLabelWidth = 100;
    private final int jLabelHeight = 20;
    private final int tempjLabelStartX = stackStartX + 2 * jLabelWidth;
    private final int menueStartX = windowWidth - 200;


    private int preJbalelListSize = -1;

    private int curStepIndex = 0;
    private int curXindex = 0;

    private JButton nextButton;
    private JButton clearButton;


    private JLabel inOrOutTxtJlabel;

    private boolean runningFlag = false;


    private ButtonGroup buttonGroup;
    private JRadioButton autoRandioButton;
    private JRadioButton personRandioButton;

    private JTextField inputTimeTextField;

    private Thread thread;

    private boolean interraptFlag = false;

    private int threadFlag = 0;

    private enum Model {
        AUTO, PERSON
    }

    private Model model = Model.AUTO;

    public DynamicStackJframe(long timeInterval, ArrayList<String> contentStrLIst) throws HeadlessException {
        this.timeInterval = timeInterval;
        this.contentStrList = contentStrLIst;
        this.jLabelList = new ArrayList<>();
        this.tempJLabelList = new ArrayList<>();



        initView();
        initListener();
        setVisible(true);


    }


    private void initView() {
        setTitle("动态栈演示");
        setSize(windowWidth, windowHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = getToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int screenHeight = dimension.height;
        int screenWidth = dimension.width;
        int frm_Height = this.getHeight();
        int frm_width = this.getWidth();
        setLocation((screenWidth - frm_width) / 2,
                (screenHeight - frm_Height) / 2);


        getContentPane().setLayout(null);

        nextButton = new JButton("开始");
        nextButton.setBounds(menueStartX, (windowHeight - 30) / 2, 100, 30);

        add(nextButton);

        clearButton = new JButton("清除");
        clearButton.setBounds(menueStartX, 40 + (windowHeight - 30) / 2, 100, 30);

        add(clearButton);


        inOrOutTxtJlabel = new JLabel("");
        inOrOutTxtJlabel.setBounds(stackStartX + jLabelWidth + 20, (windowHeight - 30) / 2, jLabelWidth, jLabelHeight);
        add(inOrOutTxtJlabel);


        getRootPane().setDefaultButton(nextButton);

        autoRandioButton = new JRadioButton("自动模式");
        autoRandioButton.setBounds(menueStartX - 50, (windowHeight - 30) / 2 - 40, 100, 30);
        personRandioButton = new JRadioButton("手动模式");
        personRandioButton.setBounds(menueStartX + 50, (windowHeight - 30) / 2 - 40, 100, 30);

        autoRandioButton.doClick();

        add(autoRandioButton);
        add(personRandioButton);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(autoRandioButton);
        buttonGroup.add(personRandioButton);


    }

    private void autoRun() {
        for (int i = 0; i < contentStrList.size() - 1; i++) {
            if (runningFlag) {
                nextStep();
            }

            try {
                Thread.sleep(timeInterval);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        nextButton.setText("开始");
        threadFlag = 0;

    }

    private void initListener() {
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (model == Model.PERSON) {
                    if (curStepIndex < contentStrList.size()) {
                        nextStep();
                    }
                } else {
                    String text = nextButton.getText();



                    if ("开始".equals(text)) {
                        clearPage();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runningFlag = true;
                                autoRun();
                            }
                        });
                        thread.start();
                        nextButton.setText("停止");
                    }else {
                        nextButton.setText("开始");
                        thread.stop();
                    }


                }


            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model == Model.PERSON) {
                    nextButton.setText("next");
                } else {
                    nextButton.setText("开始");
                }

                clearPage();

                if (threadFlag == 1) {
                    threadFlag = 0;
                    thread.interrupt();

                }


            }
        });

        autoRandioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (model != Model.AUTO) {
                    initAuto();
                    model = Model.AUTO;
                }
            }
        });

        personRandioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (model != Model.PERSON) {
                    initPerson();
                    model = Model.PERSON;

                }
            }
        });
    }


    private void clearPage() {
        preJbalelListSize = -1;
        curStepIndex = 0;
        curXindex = 0;

        for (JLabel jLabel : jLabelList) {
            remove(jLabel);

        }
        jLabelList.clear();

        inOrOutTxtJlabel.setText("");

        for (JLabel jLabel : tempJLabelList) {
            remove(jLabel);
        }
        tempJLabelList.clear();
        repaint();
    }

    private void initPerson() {
        clearPage();
        nextButton.setText("next");
    }

    private void initAuto() {
        clearPage();
        nextButton.setText("开始");

    }

    private void nextStep() {
        System.out.println("sur step:" + curStepIndex);
        curStepIndex++;

        String curStackContent = contentStrList.get(curStepIndex);
        String[] curSplit = curStackContent.split(",");
        if (curSplit.length > preJbalelListSize) {


            inOrOutTxtJlabel.setText("入栈：");
            inOrOutTxtJlabel.setBounds(stackStartX + jLabelWidth + 20, (windowHeight - jLabelHeight) / 2, jLabelWidth, jLabelHeight);


            if (" ".equals(curSplit[curSplit.length - 1])) {
                int a = 0;
            }
            JLabel jLabel = new JLabel(curSplit[curSplit.length - 1]);
            jLabel.setBounds(stackStartX, stackStartY - curXindex * jLabelHeight, jLabelWidth, jLabelHeight);
            jLabel.setOpaque(true);
            jLabel.setBackground(Color.GREEN);
            jLabel.setBorder(BorderFactory.createLineBorder(Color.red));

            addJlabel(jLabel);
            curXindex++;


        } else if (curSplit.length < preJbalelListSize) {

            int outCount = preJbalelListSize - curSplit.length;


            inOrOutTxtJlabel.setText("出栈：");
            inOrOutTxtJlabel.setBounds(stackStartX + jLabelWidth + 20, (windowHeight - jLabelHeight) / 2, jLabelWidth, jLabelHeight);


            removeJlabel(outCount);


        } else {
            JLabel jLabel = jLabelList.get(jLabelList.size() - 1);
            if (!curSplit[curSplit.length - 1].equals(jLabel.getText())) {
                clearTempLabel();
                inOrOutTxtJlabel.setText("规约：" + jLabel.getText() + "-->" + curSplit[curSplit.length - 1]);
                inOrOutTxtJlabel.setBounds(stackStartX + jLabelWidth + 20, (windowHeight - 30) / 2, jLabelWidth + 50, jLabelHeight);

                jLabel.setText(curSplit[curSplit.length - 1]);
                jLabel.setBackground(Color.YELLOW);
                jLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            } else {
                //  inOrOutTxtJlabel.setText("");

            }

        }

        preJbalelListSize = curSplit.length;
        repaint();

    }

    private void addJlabel(JLabel jLabel) {
        jLabelList.add(jLabel);
        add(jLabel);


        clearTempLabel();

        JLabel tempjLabel;
        tempjLabel = new JLabel(jLabel.getText());


        tempjLabel.setBounds(stackStartX + 2 * jLabelWidth, stackStartY - (windowHeight - 30) / 2, jLabelWidth, jLabelHeight);
        tempjLabel.setOpaque(true);
        tempjLabel.setBackground(Color.GREEN);
        tempjLabel.setBorder(BorderFactory.createLineBorder(Color.red));
        add(tempjLabel);
        tempJLabelList.add(tempjLabel);


    }

    private void removeJlabel(int count) {
        int startY = (windowHeight - count * jLabelHeight) / 2;

        clearTempLabel();
        for (int i = 0; i < count; i++) {
            curXindex--;
            JLabel prejLabel = jLabelList.get(jLabelList.size() - 1);
            jLabelList.remove(prejLabel);
            remove(prejLabel);

            JLabel jLabel = new JLabel(prejLabel.getText());


            jLabel.setBounds(tempjLabelStartX, startY - i * jLabelHeight, jLabelWidth, jLabelHeight);
            jLabel.setOpaque(true);
            jLabel.setBackground(Color.RED);
            jLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN));

            tempJLabelList.add(jLabel);
            add(jLabel);

        }

    }

    private void clearTempLabel() {
        for (JLabel jLabel : tempJLabelList) {
            remove(jLabel);
        }
        tempJLabelList.clear();
    }

    @Override
    public void repaint(long time, int x, int y, int width, int height) {
        for (JLabel jLabel : jLabelList) {
            add(jLabel);
        }
        super.repaint(time, x, y, width, height);
    }
}