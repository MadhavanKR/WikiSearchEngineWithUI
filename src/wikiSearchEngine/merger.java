package wikiSearchEngine;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


public class merger {

	public static void main(String[] args) 
	{
		ArrayList<FileInputStream> fstream=new ArrayList<FileInputStream>();
		ArrayList<BufferedReader> br=new ArrayList<BufferedReader>();
		HashMap<String,ArrayList<Integer>> index=new HashMap<String,ArrayList<Integer>>();
		char mergedcount='0';
		int cnt=0;
		PrintWriter out=null;
		try 
		{
			out = new PrintWriter(new BufferedWriter(new FileWriter("merged"+mergedcount)));
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		BufferedReader fileCountReader;
		try 
		{
			fileCountReader = new BufferedReader(new FileReader(new File("fileCount.dat")));
			cnt = Integer.parseInt(fileCountReader.readLine());
		} 
		catch (NumberFormatException | IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean[] finished=new boolean[cnt]; 
		for(int i=0;i<cnt;i++) //open add all the index files into an arraylist
		{
		  try 
		  {
			fstream.add(new FileInputStream("indexFile"+i));
		  }
		  catch (FileNotFoundException e) 
		  {
			e.printStackTrace();
		  }
		}
				
		for(FileInputStream f:fstream) //add all the fileInputStream to BufferedReader arraylist coz its easy to read as buffered reader
		{
			br.add(new BufferedReader(new InputStreamReader(f)));
		}
		ArrayList<StringBuilder> sb=new ArrayList<StringBuilder>(); //create an arraylist of stringbuilders
		StringBuilder temp=null;
		String Line;
		BufferedReader tempreader=null;
		for(int i=0;i<cnt;i++)
		{
			tempreader=br.get(i); //get the i'th index file and read from it
			temp=new StringBuilder();
			try 
			{			
				Line=tempreader.readLine(); //read the first line
				temp.append(Line); //append the first line to the temporary stringbuilder
				sb.add(i,temp); // add the stringbuilder in to the arraylist to the index same as that of the index file. for ex, if we read from index20 file, then we make an entry to 2oth index of the arraylist
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
					
		}
		int indexcnt=0;
	    for(StringBuilder t:sb) //"index" variable is an hash mapp of string to arraylist<integer>
		{
		//for every entry in the arraylist<stringbuilders> , split the string on "-" symbol
		String[] keyword=t.toString().split("-"); 
		if(index.get(keyword[0])==null) //check if the entry is made for the first keyword in the hash map
		{		
			ArrayList<Integer> value=new ArrayList<Integer>(); 
			value.add(indexcnt); //if no entry is made, create and arraylist<integer> and add current index file number to it and add it to the hashmpa index
			index.put(keyword[0],value);
		}
		else
		{
			//if the entry is already made, then add the new index number to the existing arraylist<integer>
			ArrayList<Integer> value=new ArrayList<Integer>();
			value=index.get(keyword[0]);
			value.add(indexcnt);
			index.put(keyword[0], value);
		}
		indexcnt++;
		}
//			System.out.print(index.size());
		TreeSet<String> ts= new TreeSet<String>(index.keySet());
		StringBuilder output=null;
		//cnt=cnt;
		while(cnt>0)
		{
			
			//System.out.println(index.size());
			//cnt--;
			ArrayList<Integer> value=new ArrayList<Integer>();
			String term=new String();
			for(String t:ts)
			{
				value=index.get(t); 
				//System.out.println(t+value.toString());
				term=t; //read one key and break
				break;
			}	
			ts.remove(term);// we will "hopefully" process the term in coming lines hence we are removing the term from the treeset
			//System.out.println(value.toString());
			output=new StringBuilder();
			if(value.size()==1) //if the arraylist<integer> has only one element in it
			{
				Integer i=value.get(0); //this will have the index file number in which the term was present
				//output.append(term+"-");
				output.append(sb.get(i)); //append the string present in the index of "i" from the arraylist<stringbuilder> 
				//System.out.println(sb.get(i));
				//sb.remove(i);
				tempreader=br.get(i); //now read from the index file with the index"i" where i is the number of the index file
				temp=new StringBuilder();
				if(finished[i]) //finished[i] holds the flag to indicate whether we have completed processing index file i
					continue;
				try
				{
					Line=tempreader.readLine();
					if(Line!=null)
					{
						temp.append(Line);
						sb.set(i,temp);
						
						String[] keyword=Line.split("-");
						if(index.get(keyword[0])==null)
						{
								
							ArrayList<Integer> val=new ArrayList<Integer>();
							val.add(i);
							index.put(keyword[0],val);
							ts.add(keyword[0]);
						}
						else
						{
							ArrayList<Integer> val=new ArrayList<Integer>();
							val=index.get(keyword[0]);
							val.add(i);
							index.put(keyword[0], val);
							ts.add(keyword[0]);
						}
							
					}
					else
					{
						cnt--;
						System.out.println("cnt : "+cnt);
						finished[i]=true;
					}
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else
		{
			output.append(term+"-");
			for(Integer a:value)
			{
				String[] split=sb.get(a).toString().split("-");
				output.append(split[1]);
				tempreader=br.get(a);
				temp=new StringBuilder();
				if(finished[a])
					continue;
				try 
				{
					Line=tempreader.readLine();
					if(Line!=null)
					{
						temp.append(Line);
						//sb.remove(a);
						sb.set(a,temp);
						String[] keyword=Line.split("-");
						if(index.get(keyword[0])==null)
						{
							ArrayList<Integer> val=new ArrayList<Integer>();
							val.add(a);
							index.put(keyword[0],val);
							ts.add(keyword[0]);
						}
						else
						{
							ArrayList<Integer> val=new ArrayList<Integer>();
							val=index.get(keyword[0]);
							val.add(a);
							index.put(keyword[0], val);
							ts.add(keyword[0]);
						}
								
				}
					else
					{
						cnt--;
						System.out.println("cnt : "+cnt);
						finished[a]=true;
					}
			} 
			catch (IOException e) 
			{
							// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	index.remove(term);
		
	if(output.charAt(0)>='a' && mergedcount!=output.charAt(0))
	{
		out.close();
		mergedcount=output.charAt(0);
		try 
		{
			out = new PrintWriter(new BufferedWriter(new FileWriter("merged"+mergedcount)));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	out.write(output.toString()+"\n");
	output=null;
}
}

}
