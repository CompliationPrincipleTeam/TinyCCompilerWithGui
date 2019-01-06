package com.sin.notepad.util;  
  
import java.io.IOException;
import java.util.GregorianCalendar;

import com.sin.notepad.view.NotepadMainFrame;  
  
public class Clock extends Thread{  
	private static long timingEnd = Long.MAX_VALUE;
	private static long curMillSec;
	private static int secondGap;
	private static GregorianCalendar time = null;  
    public void run() 
    {  
        while (true) 
        {  
            time = new GregorianCalendar();  
            curMillSec = time.getTimeInMillis();
            NotepadMainFrame.toolStateLabel.setText("    当前时间：" + time.getTime());
            try 
            {  
            	if(curMillSec >= timingEnd)
            	{
            		System.out.println("O泡时间到！");
            		System.out.println(time.getTime());
            		timingEnd += secondGap*1000;
            		NotepadMainFrame.timingSavingAction();
            	}
                Thread.sleep(1000);  
            } 
            catch (InterruptedException | IOException exception) 
            {  
            }  
        }  
    } 

    
    public static void setTimingEnd(int sec)
    {
    	timingEnd = curMillSec + sec*1000;
    	secondGap = sec;
    }
    
    public static void cancelTiming()
    {
    	timingEnd = Long.MAX_VALUE;
    }
    
    public static String getTimeString()
    {
    	if(time !=null)
    		return time.getTime().toString();
    	else
    		return "";
    }
    
    
//    @Test 
//    public void test()
//    {
//    	GregorianCalendar time = new GregorianCalendar(); 
//    	System.out.println(time.getTime());
//    	time.setTimeInMillis(Clock.minsToMillSeconds(time.getTimeInMillis(), 3));
//    	System.out.println(time.getTime());
//    	
//    }
}  
