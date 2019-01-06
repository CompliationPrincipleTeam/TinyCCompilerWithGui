package com.sin.notepad.build;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.sin.notepad.system.SystemApi;

public class BuildPython extends BuildCode {
	private static String pathName = dir + "PythonBuffer.py";
	public static void buildPython(String content) throws IOException
	{
		BuildPython.writeBuffer(content);
		String cmd = "cmd /c start python "+pathName;
		ArrayList<String> list = SystemApi.runCmd(cmd);
		System.out.println(BuildCode.listToString(list));
		//ConsoleFrame console = new ConsoleFrame("Python Console", BuildCode.listToString(list));
	}
	
	public static void writeBuffer(String content) throws IOException
	{
		FileWriter fw = new FileWriter(pathName,false);
		fw.write(content);
		fw.close();
	}
}
