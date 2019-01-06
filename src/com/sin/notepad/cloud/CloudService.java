package com.sin.notepad.cloud;

import java.io.IOException;

import org.junit.Test;

import com.sin.notepad.build.BuildCode;
import com.sin.notepad.system.SystemApi;

public class CloudService {
	private static String cloudPath = "sinkinben@127.0.0.1:/home/sinkinben/MiniNotepadPlusplus";
	private static String localPath = BuildCode.projectPath();
	private static String command = "cmd /c start .\\pscp -P 20022";
	
	public static void uploadFileByPath(String pathName) throws IOException
	{
		if(pathName == null)
			throw new IllegalArgumentException("上传失败：当前没有打开任何文件。");
		else
		{
			String cmd = command + " " + pathName + " " + cloudPath;
			System.out.println(cmd);
			SystemApi.runCmd(cmd);
		}
	}
	
	public static void downloadFile(String fileName) throws IOException
	{
		if(fileName == null)
		{
			throw new IllegalArgumentException("下载失败：当前没有打开任何文件。");
		}
		else
		{
			String cmd = command + " " + cloudPath+"/"+fileName + " " + localPath+"/download";
			System.out.println(cmd);
			SystemApi.runCmd(cmd);
		}
	}
	
	@Test
	public void test() throws IOException
	{
		//CloudService.downloadFile("log.txt");
		//CloudService.uploadFileByPath("download/log.txt");
	}
	
	
//	public static void main(String[] args) throws IOException
//	{
//		String upload = "cmd /c start .\\pscp -P 20022 log.txt sinkinben@127.0.0.1:/home/sinkinben/MiniNotepadPlusplus";
//		String download = "cmd /c start .\\pscp -P 20022 sinkinben@127.0.0.1:/home/sinkinben/MiniNotepadPlusplus/log.txt "
//				+ BuildCode.projectPath();
//		System.out.println(download);
//		BuildCode.runCmd(download);
//		
//	}
}
