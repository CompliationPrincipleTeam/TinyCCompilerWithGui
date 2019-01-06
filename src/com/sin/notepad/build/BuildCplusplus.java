package com.sin.notepad.build;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.sin.notepad.system.SystemApi;

public class BuildCplusplus extends BuildCode{
	private static String pathName = dir + "CplusplusBuffer.cpp";
	public static void buildCplusplus(String content) throws IOException 
	{
		BuildCplusplus.writeBuffer(content);

		String build = "cmd /c g++ "+ pathName + " -o " + pathName.replace(".cpp", ".exe");
		System.out.println(build);
		SystemApi.runCmd(build);
		String run = "cmd /c start "+ pathName.replace(".cpp", ".exe");
		//System.out.println(run);
		ArrayList<String> list = SystemApi.runCmd(run);
		System.out.println(BuildCode.listToString(list));
		//ConsoleFrame console = new ConsoleFrame("C/C++ Console", BuildCode.listToString(list));
	}
	
	private static void writeBuffer(String content) throws IOException
	{
		FileWriter fw = new FileWriter(pathName,false);
		fw.write(content);
		fw.close();
	}
}
