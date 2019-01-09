package tinyccompiler.preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author FFXN
 * @date 2019-1-2
 * @node ��txt�ļ��е�C����Ԥ�����궨���滻���˳�ע��
 */
public class Preprocessing {

    //�������ļ�
    public static void Preprocess(String src, String dst) throws NumberFormatException, IOException, IOException {
        ArrayList<String> list = new ArrayList<String>();
        HashMap<String,String> macro=new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new FileReader(src));
        String s = "";
        String []s1={};
        //���Һ�
        while ((s = br.readLine()) != null) {
            list.add(s);
        }

        //����ע��
        String s2;
        for (int i=0;i<list.size();i++) {
            s2=list.get(i);
            if(s2.contains("//")){
                list.set(i,s2.substring(0,s2.indexOf("//")));

            }
            else{
                if (s2.contains("/*")){
                    if (s2.contains("*/")){
                        list.set(i,s2.substring(0,s2.indexOf("/*"))+s2.substring(s2.indexOf("*/")+2));
                        continue;
                    }
                    list.set(i,s2.substring(0,s2.indexOf("/*")));
                    for (i=i+1;i<list.size();i++){
                        s2=list.get(i);
                        if(s2.contains("*/")){
                            list.set(i,s2.substring(s2.indexOf("*/")+2));
                            break;
                        }
                        else{
                            list.remove(i);
                            i--;
                        }
                    }
                }
            }
        }

        //����and�����
        String key;
        for (int i=0;i<list.size();i++){
            s=list.get(i);
            if (s.contains("#define")){
                s1=s.split("\\s+");
                if (s1.length!=3){
                    System.out.println("ERROR:�궨�����\n");
                    assert(false);
                }
                macro.put(s1[1],s1[2]);
                list.remove(i);
                i--;
            }
            else {
                Iterator it = macro.keySet().iterator();
                while (it.hasNext()){
                    key=(String)it.next();
                    if(s.contains(key)){
                        s1=s.split("\\s+");
                        s="  ";
                        for (int j=0;j<s1.length;j++) {
                            if (s1[j].equals(key)){
                                s1[j]=macro.get(key);
                            }
                            s=s+" "+s1[j];
                        }

                        list.set(i,s);
                    }
                }
            }

        }

        //Ԥ�����Ĵ���д���µ��ļ�
        File file_1=new File(dst);
        if (!file_1.exists()){
            file_1.createNewFile();
        }
        FileWriter fw=new FileWriter(file_1.getAbsoluteFile());

        for (int i=0;i<list.size();i++){
            if (list.get(i).trim().length()==0){
                continue;
            }
            s=list.get(i)+"\n";
            fw.write(s,0,s.length());
        }
        fw.flush();
        fw.close();

    }

//    public static void main(String[] args) throws IOException {
//        Preprocessing.Preprocess("test.txt");
//    }

}
