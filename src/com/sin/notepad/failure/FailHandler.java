package com.sin.notepad.failure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.sin.notepad.global.FailureType;
import com.sin.notepad.system.SystemApi;
import com.sin.notepad.util.Clock;
import com.sin.notepad.view.NotepadMainFrame;

public class FailHandler {
	private static final String path = "log/log";
	
	public static void writeLog(FailureType type)
	{
		try {
			FileWriter fw = new FileWriter(new File(path),true);
			fw.write(Clock.getTimeString() + "\t" + "State="+ type + "\t" + 
					"File="+NotepadMainFrame.getCurrentFilePath()+
					SystemApi.LINE_SEPARATOR);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
