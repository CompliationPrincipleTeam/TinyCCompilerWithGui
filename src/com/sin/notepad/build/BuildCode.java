package com.sin.notepad.build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BuildCode {
	protected final static String dir = "codebuffer/";
	Runtime runtime = Runtime.getRuntime(); // 运行时系统获取

	public static String projectPath()
	{
		//System.out.println(System.getProperty("user.dir").replace("\\", "/"));
		return System.getProperty("user.dir").replace("\\", "/");
	}
	
	
	public static String listToString(ArrayList<String> list)
	{
		String res = "";
		for(String s:list)
			res+=s+"\n";
		return res;
	}
	
	public static void fileRename(String src, String dst) throws IOException
	{
		File file = new File(dir+src);
		if(!file.exists())
			file.createNewFile();
		file.renameTo(new File(dir+dst));
	}
}
