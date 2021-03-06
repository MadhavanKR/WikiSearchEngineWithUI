package wikiSearchEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;



public class secondaryTitleIndexer {

	public static void main(String[] args) {
		FileInputStream instream = null;
		PrintWriter outstream = null;

		try {
			//outstream = new FileInputStream("secondtitle.txt");
			outstream = new PrintWriter(new BufferedWriter(new FileWriter("secondtitle.txt")));
			instream = new FileInputStream("title.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(instream,Charset.forName("iso-8859-1")));
		Integer offset=0;
		outstream.write(offset+"\n");
		String line;
		try {
			while((line=in.readLine())!=null)
			{
				offset=offset+1+line.codePointCount(0,line.length());
				outstream.write(offset+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outstream.close();
		try {
			instream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
