package wikiSearchEngine;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class eventHandler extends DefaultHandler {
	
	public static int pageCount = 0;
	public int count=0,countText=0;
	public static String fileName = new String("rawFile");
	String allTitles;
	public int fileNum = 0;
	public static BufferedWriter currentWriter=null;
	public BufferedWriter titleWriter;
	StringBuilder pageContent;
	
	public eventHandler(){
		allTitles = new String();
		try {
			titleWriter = new BufferedWriter(new FileWriter(new File("title.txt")));
		} 
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		
		pageContent = new StringBuilder("");
		
	}
	
	
	public void startElement(String nameSpaceURI,String localName,String qName,Attributes att){
		pageContent = new StringBuilder("");
		//pageCount++;
//		if(qName.equalsIgnoreCase("title"))
//			count++;
	}
	
	public void endDocument(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("fileCount.dat")));
			String out = new String((fileNum+1)+"\n"+countText);
			System.out.println("out = "+out);
			writer.write(out);
			writer.close();
			System.out.println("Count is "+count);
			System.out.println("CountText is "+countText);
			//System.out.println(allTitles);
			titleWriter.write(allTitles);
			titleWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void endElement(String nameSpaceURI,String localName,String qName) {
		if(qName.equalsIgnoreCase("title"))
		{	
				pageCount++;
//				System.out.println(pageContent);
//				if(pageContent.toString().compareTo("")==0)
//					titleWriter.write("no content"+"\n");
//				else
//					titleWriter.write(pageContent.toString()+"\n");
			allTitles+=pageContent.toString();
			allTitles = allTitles+"~~~"+pageCount;
			allTitles+="\n";
		}
		else if(qName.equalsIgnoreCase("text"))
		{
			if(countText%5000 == 0)
			{
				try {
					if(currentWriter!=null)
						currentWriter.close();
					currentWriter = new BufferedWriter(new FileWriter(new File(fileName+fileNum)));
					currentWriter.append(pageContent);
					currentWriter.append("\n~~~~~DELIMITER~~~~~\n");
					} 
				catch (IOException e) {
				e.printStackTrace();
				}
				fileNum++;
			}
			try {
				currentWriter.append(pageContent);
				currentWriter.append("\n~~~~~DELIMITER~~~~~\n");
			} 
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
			//System.out.println(pageCount);
			//pageCount++;
			countText++;
		}
	}
	
	public void characters(char[] ch,int start,int length){
		String temp = new String(ch,start,length);
		pageContent.append(temp.toLowerCase());
	}
}
