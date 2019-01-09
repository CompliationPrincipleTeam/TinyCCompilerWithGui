package tinyccompiler.global;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Global {
	public static FileWriter stackFw = null;
	public static FileReader srcFr = null;
	public static FileWriter interCodeFw = null;
	
	
	public static void init(String srcFile, String interCodeFile,String stackFile) throws IOException
	{
		Global.stackFw = new FileWriter(new File(stackFile));
		Global.srcFr = new FileReader(new File(srcFile));
		Global.interCodeFw = new FileWriter(new File(interCodeFile));
	}
	
	public static void close() throws IOException
	{
		Global.stackFw.flush();
		Global.stackFw.close();
		Global.srcFr.close();
		Global.interCodeFw.flush();
		Global.interCodeFw.close();
	}
}
