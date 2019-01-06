package com.sin.notepad.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * ϵͳ����
 *
 */
public class SystemApi {
	
	/**
	 * ��ǰϵͳ���з�
	 */
	public static final String LINE_SEPARATOR=System.getProperty("line.separator");
	
	/**
	 * ϵͳCMD�ӿ�
	 * @param command
	 * @param dir
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static ArrayList<String> runCmd(String command,File dir) throws IOException, InterruptedException
	{
		ArrayList<String> list = new ArrayList<String>();
        Runtime rt = Runtime.getRuntime(); // ����ʱϵͳ��ȡ
        Process process = null;
        Map<String, String> lineMap = new HashMap<String, String>();//��ŷ���ֵ
        try {
            process = rt.exec(command,null,dir);// ִ������
            java.io.InputStream stderr = process.getInputStream();//ִ�н�� �õ����̵ı�׼�����Ϣ��
            InputStreamReader isr = new InputStreamReader(stderr);//���ֽ���ת�����ַ���
            BufferedReader br = new BufferedReader(isr);//���ַ����Ի������ʽһ��һ�����
            String line = null;
            while ((line = br.readLine()) != null) { 
            	System.out.println(line);
            	list.add(line);
                if (!StringUtils.isEmpty(line)) {
                    String[] strLine = line.split(":");
                    if(strLine.length>=2) {
                        lineMap.put(strLine[0].trim(), strLine[1].trim());
                    }
                    
                }
            }
            br.close();
            isr.close();
            stderr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(process!=null)
        {
        	process.getInputStream().close();
        	process.destroy();
        }
        
        return list;
	}

	
	public static ArrayList<String> runCmd(String command) throws IOException
	{
		ArrayList<String> list = new ArrayList<String>();
        Runtime rt = Runtime.getRuntime(); // ����ʱϵͳ��ȡ
        Process process = null ;
        Map<String, String> lineMap = new HashMap<String, String>();//��ŷ���ֵ
        try {
            process = rt.exec(command);// ִ������
            java.io.InputStream stderr = process.getInputStream();//ִ�н�� �õ����̵ı�׼�����Ϣ��
            InputStreamReader isr = new InputStreamReader(stderr);//���ֽ���ת�����ַ���
            BufferedReader br = new BufferedReader(isr);//���ַ����Ի������ʽһ��һ�����
            String line = null;
            while ((line = br.readLine()) != null) { 
            	//System.out.println(line);
            	list.add(line);
                if (!StringUtils.isEmpty(line)) {
                    String[] strLine = line.split(":");
                    if(strLine.length>=2) {
                        lineMap.put(strLine[0].trim(), strLine[1].trim());
                    }
                    
                }
            }
            br.close();
            isr.close();
            stderr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(process!=null)
        {
        	process.getInputStream().close();
        	process.destroy();
        }
        return list;
	}
}

