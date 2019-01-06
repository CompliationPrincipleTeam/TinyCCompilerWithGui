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
 * 系统参数
 *
 */
public class SystemApi {
	
	/**
	 * 当前系统换行符
	 */
	public static final String LINE_SEPARATOR=System.getProperty("line.separator");
	
	/**
	 * 系统CMD接口
	 * @param command
	 * @param dir
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static ArrayList<String> runCmd(String command,File dir) throws IOException, InterruptedException
	{
		ArrayList<String> list = new ArrayList<String>();
        Runtime rt = Runtime.getRuntime(); // 运行时系统获取
        Process process = null;
        Map<String, String> lineMap = new HashMap<String, String>();//存放返回值
        try {
            process = rt.exec(command,null,dir);// 执行命令
            java.io.InputStream stderr = process.getInputStream();//执行结果 得到进程的标准输出信息流
            InputStreamReader isr = new InputStreamReader(stderr);//将字节流转化成字符流
            BufferedReader br = new BufferedReader(isr);//将字符流以缓存的形式一行一行输出
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
        Runtime rt = Runtime.getRuntime(); // 运行时系统获取
        Process process = null ;
        Map<String, String> lineMap = new HashMap<String, String>();//存放返回值
        try {
            process = rt.exec(command);// 执行命令
            java.io.InputStream stderr = process.getInputStream();//执行结果 得到进程的标准输出信息流
            InputStreamReader isr = new InputStreamReader(stderr);//将字节流转化成字符流
            BufferedReader br = new BufferedReader(isr);//将字符流以缓存的形式一行一行输出
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

