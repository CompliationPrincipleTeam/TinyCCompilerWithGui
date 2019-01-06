package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.UIManager;

import org.junit.Test;

public class Helper {
	
	@Test
	public void getSystemStyle() throws IOException
	{
		  UIManager.LookAndFeelInfo  []info = UIManager.getInstalledLookAndFeels() ;  
		  for(UIManager.LookAndFeelInfo item:info)
		  {
		      System.out.println("\""+item.getClassName()+"\",");
		  }
		  Helper.helper();
	}
	
	public static void helper() throws IOException
	{
		String file = "";
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s = "";
		while((s=br.readLine())!=null)
		{
			String[] t = s.split("\\s");
			for(int i=0;i<t.length;i++)
				list.add(t[i]);
		}
		FileWriter fw = new FileWriter(new File(file),true);
		for(String str:list)
		{
			if(!str.equals(""))
			{
				fw.write(str+"\n");
				System.out.println(str);
			}
			
		}
		fw.close();
		br.close();
	}
}
