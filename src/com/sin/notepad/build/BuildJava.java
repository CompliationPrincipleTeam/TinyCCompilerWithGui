package com.sin.notepad.build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.sin.notepad.system.SystemApi;

public class BuildJava extends BuildCode{
	private static String className = "JavaBuffer";
	public static void writeBuffer(String content) throws IOException
	{
		FileWriter fw = new FileWriter(dir + "JavaBuffer.java",false);
		fw.write(content);
		fw.close();
		
	}
	

	
	public static void buildJava(String content) throws IOException, InterruptedException
	{
		FileWriter fw = new FileWriter(dir+"JavaBuffer.java",false);
		fw.write(content);
		fw.close();
		BuildJava.className = BuildJava.getClassName(content);
		BuildJava.fileRename("JavaBuffer.java", className+".java");

		String pathName = dir+className+".java";
		String build = "cmd /c javac "+pathName;
		SystemApi.runCmd(build);
		//String className = pathName.substring( pathName.lastIndexOf("/")+1,pathName.lastIndexOf(".") );
		System.out.println("className=" + className);
		String run="cmd /c start java " + className;
		System.out.println("run=" + run);
		SystemApi.runCmd(run,new File(dir));
		//System.out.println(BuildCode.listToString(list));
		//ConsoleFrame console = new ConsoleFrame("Java Console", BuildCode.listToString(list));
		
		BuildJava.fileRename(className+".java","JavaBuffer.java");
	}
	
	public static String getClassName(String code)
	{
		String name = null;
		String classLine = null;
		String[] buffer = code.split(SystemApi.LINE_SEPARATOR);
		for(int i=0;i<buffer.length;i++)
		{
			System.out.println(i+" "+buffer[i]);
			if(buffer[i].indexOf("public class")!=-1)
			{
				classLine = buffer[i];
				break;
			}
		}
		String[] buffer2 = classLine.split("\\ ");
		for(int i=0;i<buffer2.length-1;i++)
		{
			if(buffer2[i].equals("class"))
			{
				name = buffer2[i+1];
				break;
			}
		}
		if(name!=null)
		{
			name = name.replace("{","");
			name = name.replace(" ","");
		}
		System.out.println(name);
		BuildJava.className = name;
		return name;
	}
	

}
