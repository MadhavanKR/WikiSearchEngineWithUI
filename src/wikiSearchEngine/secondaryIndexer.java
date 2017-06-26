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
import java.nio.file.*;


public class secondaryIndexer {

	public static void main(String[] args) {
		String a = "0 1 2 3 4 5 6 7 8 9 a b c d e f g h i j k l m n o p q r s t u v w x y z";
		String[] array = a.split(" ");
		for(String x:array){
			System.out.print(x);
		}
		for(String x:array){
			FileInputStream instream = null;
			PrintWriter outstream = null;
			try{
				//outstream = new FileInputStream("secondtitle.txt"); //create a file with the name secondtitle.txt
				Path path = Paths.get("merged"+x); // we iterate over from 0 to 9 and a to z, and read every merged file that has been created, from merged0 to mergedz
				if(Files.exists(path)){ //if the file exists then create a secondary index for that file, so create an equivalent index file sindex"i" for merged file merged"i"
				//where i is the index file for words starting from letter i.
				System.out.println("merged"+x+" exists");
				outstream = new PrintWriter(new BufferedWriter(new FileWriter("sindex"+x)));//create a new secondary index 
				instream = new FileInputStream("merged"+x);//open the merged file
				}
				else
					continue;
			}
			catch (FileNotFoundException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(instream,Charset.forName("iso-8859-1")));
			Integer offset=0;
			outstream.write(offset+"\n");
			String line;
			try {
				while((line=in.readLine())!=null)
				{
					String[] word=line.split("-");
					outstream.write(word[0]+"-"+offset+"\n");
					offset=offset+1+line.codePointCount(0,line.length());

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			outstream.close();
			try {
				instream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
