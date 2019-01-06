package com.sin.notepad.settings;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

public class PersonalSettings {
	public static Font font = new Font("Consolas",Font.PLAIN,12);
	public static Color background = Color.WHITE;		//背景颜色
	public static Color foreground = Color.BLACK;		//前景颜色，即字体颜色
	public static boolean showStatusBar = true;
	public static String settingsFile = "settings.xml";
	public static List<String> recentFiles = new ArrayList<String>();
	public static int RecentFileSize = 10;

	@Test
	public void test() throws TransformerException, ParserConfigurationException, SAXException, IOException
	{
		System.out.println(font.toString());
		System.out.println(background.toString());
		System.out.println(foreground.toString());
		//PersonalSettings.saveSettings();
		PersonalSettings.readSettingsFile();
	}
	
	public static void loadSettings() throws ParserConfigurationException, SAXException, IOException
	{
		System.out.println("In class PersonalSettings: loading personal settings" );
		readSettingsFile();
	}
	
	public static void readSettingsFile() throws ParserConfigurationException, SAXException, IOException
	{
		System.out.println("Reading settings file...");
		File file = new File(settingsFile);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(file);  
		NamedNodeMap fontAttributes = doc.getElementsByTagName("Font").item(0).getAttributes();
		NamedNodeMap backgroundAttributes = doc.getElementsByTagName("Background").item(0).getAttributes();
		NamedNodeMap foregroundAttributes = doc.getElementsByTagName("Foreground").item(0).getAttributes();
		NamedNodeMap statueBarAttributes = doc.getElementsByTagName("StatusBar").item(0).getAttributes();
		
		String fontName = fontAttributes.getNamedItem("name").getNodeValue();
		int fontStyle = Integer.valueOf(fontAttributes.getNamedItem("style").getNodeValue());
		int fontSize = Integer.valueOf(fontAttributes.getNamedItem("size").getNodeValue());
		//System.out.println(fontName + " " + fontStyle + " " + fontSize); 
		font = new Font(fontName, fontStyle, fontSize);
		
		int red = Integer.valueOf(backgroundAttributes.getNamedItem("r").getNodeValue());
		int green = Integer.valueOf(backgroundAttributes.getNamedItem("g").getNodeValue());
		int blue = Integer.valueOf(backgroundAttributes.getNamedItem("b").getNodeValue());
		background = new Color(red,green,blue);
		
		red = Integer.valueOf(foregroundAttributes.getNamedItem("r").getNodeValue());
		green = Integer.valueOf(foregroundAttributes.getNamedItem("g").getNodeValue());
		blue = Integer.valueOf(foregroundAttributes.getNamedItem("b").getNodeValue());
		foreground = new Color(red,green,blue);
		
		showStatusBar = Boolean.valueOf(statueBarAttributes.getNamedItem("show").getNodeValue());
		
		String[] str = doc.getElementsByTagName("RecentFiles").item(0).getTextContent().split("\\\n");
		for(int i=0;i<str.length;i++)
		{
			System.out.println(str[i]);
			recentFiles.add(str[i]);
		}
		
		
		
	}
	
	public static void saveSettings() throws TransformerException, ParserConfigurationException
	{
		/*
		 * TODO: 将设置按照一定的格式写入settings.xml文件
		 * <userSetting>
		 * 		<Font attribution = '...'>
		 * 		<Background attribution = '...'>
		 * 		<Foreground attribution = '...'>
		 * </userSetting>
		 */
		String output = settingsFile;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.newDocument();
        document.setXmlStandalone(true);
        
        Element root = document.createElement("UserSettings");
        Element fontSetting = document.createElement("Font");
        fontSetting.setAttribute("name", font.getName());
        fontSetting.setAttribute("style", font.getStyle()+"");
        fontSetting.setAttribute("size", font.getSize()+"");
        
        root.appendChild(fontSetting);
        
        Element backgroundSetting = document.createElement("Background");
        backgroundSetting.setAttribute("r", background.getRed()+"");
        backgroundSetting.setAttribute("g",background.getGreen()+"");
        backgroundSetting.setAttribute("b", background.getBlue()+"");
        root.appendChild(backgroundSetting);
        
        Element foregroundSetting = document.createElement("Foreground");
        foregroundSetting.setAttribute("r", foreground.getRed()+"");
        foregroundSetting.setAttribute("g", foreground.getGreen()+"");
        foregroundSetting.setAttribute("b", foreground.getBlue()+"");
        root.appendChild(foregroundSetting);
        
        Element statusBarSetting = document.createElement("StatusBar");
        statusBarSetting.setAttribute("show", showStatusBar+"");
        root.appendChild(statusBarSetting);
        
        String str = "";
        for(String s: recentFiles)
        	str+=s+"\n";
        Element recentFilesSetting = document.createElement("RecentFiles");
        recentFilesSetting.setTextContent(str);
        root.appendChild(recentFilesSetting);
        
        document.appendChild(root);
        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf = tff.newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.transform(new DOMSource(document), new StreamResult(new File(output)));
	}
	
	public static void updateRecentFiles(String filePath)
	{
    	if(recentFiles.contains(filePath))
    	{
    		recentFiles.remove(filePath);
    	}
    	if(recentFiles.size() >= RecentFileSize)
    	{
    		recentFiles.remove(0);
    	}
    	recentFiles.add(0, filePath);
    	
	}
}
