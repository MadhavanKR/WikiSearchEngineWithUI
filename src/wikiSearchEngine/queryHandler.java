package wikiSearchEngine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Map.Entry;


//class scorecompa implements Comparator<Entry<Integer, Double>>{
//	 
//    @Override
//    public int compare(Entry<Integer, Double> e1,Entry<Integer, Double> e2) {
//    	
//    				if (e1.getValue()<e2.getValue())
//    					return 1;			
//    				else
//    					return -1;
//    	
//    }
//}  



public class queryHandler {

	/**
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public static String executeQuery(String qry) throws IOException 
	{
		// TODO Auto-generated method stub
		Stemmer stm=null;
		String qryword=null;
		FileInputStream fstream=null;
		BufferedReader br=null;
		char startletter='0';
		String line;
		Double idf,score;
		Integer docid;
		HashMap<Integer,Integer> title=new HashMap<Integer,Integer>();
		StringBuilder result = new StringBuilder();

		
		fstream=new FileInputStream("C:\\Users\\m0k00eu\\workspace\\documentRetrieval\\thirdtitle.txt");
		RandomAccessFile raf=null;
		br=new BufferedReader(new InputStreamReader(fstream));
		int lineno=1;
		while((line=br.readLine())!=null)
		{
			title.put(lineno, Integer.parseInt(line));
			lineno++;
		}
		
		br.close();
		//System.out.print(title.get(16812));
		while(true)
		{
			//System.out.print("Enter Search Query : ");
			@SuppressWarnings("resource")
			//System.out.println(qry);
			int type=qry.indexOf(':');
			HashMap<Integer,Double> index=new HashMap<Integer,Double>();
			
			if(type==-1)	// text query
			{
				for(String qrywords:qry.split(" "))
				{
					stm=new Stemmer();
					if(qrywords.length()<2)
						continue;
					stm.add(qrywords.toLowerCase().toCharArray(),qrywords.length());
					stm.stem();
					qryword=stm.toString();
					//System.out.println("stemmed word = "+qryword);
					startletter=qryword.charAt(0);
					/*if(startletter>='0' && startletter<='9' )
					{
						startletter='0';
					}*/
					try {
						fstream=new FileInputStream("C:\\Users\\m0k00eu\\workspace\\documentRetrieval\\sindex"+startletter);
						br=new BufferedReader(new InputStreamReader(fstream));
						}
					catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						}
					while((line=br.readLine())!=null)
					{
						if(line.startsWith(qryword+"-"))
							break;
					}
					if(line==null)
						System.out.println(qryword+" not in index ");
					else
						{
					//		System.out.println(line);
							String[] offsets=line.split("-");
							
							raf = new RandomAccessFile("C:\\Users\\m0k00eu\\workspace\\documentRetrieval\\merged"+startletter, "r");
							raf.seek(Long.parseLong(offsets[1]));
							line=raf.readLine();
							//System.out.println(line);
							
							String[] docs=line.split("-");		// docs=123 b1|34 b2t1|
							
							String[] doc=docs[1].toString().split("\\|");	//doc= "123 b1","34 b2t1"
							//System.out.println("docs[0]="+docs[0]);
							//System.out.println("docs[1]="+docs[1]);
							//System.out.println("docs: ");
//							for(int x=0;x<doc.length;x++)
//								System.out.print(doc[x]+" -- ");
//							System.out.println();
//				//			System.out.println(doc[1]);
							
							idf=(double) doc.length;
							idf=Math.log(32600/idf);
							score=0.0;
							for(String docposting:doc)			
							{
								if(doc.length<2)
									continue;
						//		System.out.println(docposting);
									
								String[] docpost=docposting.split(" ");		//docpost="123","b1"
								//System.out.println("docpost[1] = "+docpost[1]);
								docid=Integer.parseInt(docpost[0].toString());					
								String[] value=docpost[1].split("\\P{Digit}");
								String[] key=docpost[1].split("\\P{Alpha}");
//								System.out.println("key length= "+key.length);
							//	System.out.println("value length = "+value[1]);
								int ind=0;
								score=0.0;
								for(String k:key)
								{
									//System.out.println("key : "+k);
									//System.out.println("value : "+value[ind]);
									if(k.length()==0)
										continue;
									while(value[ind].length()==0)
										ind++;
									switch(k)
									{
										case "t":score=score+100*(Integer.parseInt(value[ind].toString()));
											break;
										case "b":score=score+15*(Integer.parseInt(value[ind].toString()));
											break;
										case "c":score=score+30*(Integer.parseInt(value[ind].toString()));
											break;
										case "i":score=score+30*(Integer.parseInt(value[ind].toString()));
											break;		
										case "r":score=score+30*(Integer.parseInt(value[ind].toString()));
											break;
										case "l":score=score+30*(Integer.parseInt(value[ind].toString()));
											break;
										default:continue;	
									}
									ind++;
								}
								score=Math.log(score+1);
								score=score*idf;
								if(index.get(docid)==null)
								{
									//System.out.println("inserting <"+docid+", "+score+"> pair");
									index.put(docid, score);
								}
								else
								{
									Double temp=index.get(docid);
									temp=temp+score;
									//System.out.println("inserting <"+docid+", "+temp+"> pair");
									index.put(docid,temp);
								}
							}
									
						}
					//System.out.println(index.toString());
					if(br!=null)
						br.close();
					if(raf!=null)
						raf.close();
				}
				/*TreeSet<Entry<Integer,Double>> ts=new TreeSet<Entry<Integer,Double>>(new scorecompa()); 
				ts.addAll(index.entrySet());
				for(int i=0;i<10;i++)
				{
					System.out.println(title.get(ts.pollFirst().getKey()));
				}*/
			}
			else
			{
				String[] words=qry.split(":");
				HashMap<String,ArrayList<String>> hm=new HashMap<String,ArrayList<String>>();
				char prev=words[0].charAt(0);
				int j;
				for(int i=1;i<words.length-1;i++)
				{
					String[] word=words[i].split(" ");
					ArrayList<String> val=new ArrayList<String>();
					if(hm.get(String.valueOf(prev))!=null)
					{
						val=hm.get(String.valueOf(prev));
						
					}
					
					for(j=0;j<word.length-1;j++)
					{
						val.add(word[j]);
					}
					//String key=new String(prev.);
						hm.put(String.valueOf(prev),val);
					prev=word[j].charAt(0);
				}
				ArrayList<String> val=new ArrayList<String>();
				//String[] word=
				if(hm.get(String.valueOf(prev))!=null)
				{
					val=hm.get(String.valueOf(prev));
					
				}
				for(String s:words[words.length-1].split(" "))
				{
					val.add(s);
				}
				hm.put(String.valueOf(prev),val);
		//		System.out.print(hm.toString());
				for(String key:hm.keySet())
				{
					val=hm.get(key);
					for(String value:val)
					{
						stm=new Stemmer();
						if(value.length()<2)
							continue;
						stm.add(value.toLowerCase().toCharArray(),value.length());
						stm.stem();
						value=stm.toString();
						//System.out.print(value);
						startletter=value.charAt(0);
						/*if(startletter>='0' && startletter<='9' )
						{
							startletter='0';
						}*/
						try {
							fstream=new FileInputStream("C:\\Users\\m0k00eu\\workspace\\documentRetrieval\\sindex"+startletter);
							br=new BufferedReader(new InputStreamReader(fstream));
							}
						catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							}
						while((line=br.readLine())!=null)
						{
							if(line.startsWith(value+"-"))
								break;
						}
						if(line==null)
							result.append(value+" not in index ");
						else
						{
							//System.out.println(line);
							String[] offsets=line.split("-");
							raf = new RandomAccessFile("C:\\Users\\m0k00eu\\workspace\\documentRetrieval\\merged"+startletter, "r");
							raf.seek(Long.parseLong(offsets[1]));
							line=raf.readLine();
							String[] docs=line.split("-");		// docs=123 b1|34 b2t1|
							
							String[] doc=docs[1].toString().split("\\|");	//doc= "123 b1","34 b2t1"
			//				System.out.println(docs[1]);
							
				//			System.out.println(doc[1]);
							
							idf=(double) doc.length;
							idf=Math.log(32600/idf);
							score=0.0;
							for(String docposting:doc)			
							{
								if(doc.length<2)
									continue;
								if(docposting.indexOf(key)==-1)
									continue;
								String[] docpost=docposting.split(" ");		//docpost="123","b1"
								//System.out.println(docpost[1]);
								docid=Integer.parseInt(docpost[0].toString());
								
								String[] value1=docpost[1].split("\\P{Digit}");
								String[] key1=docpost[1].split("\\P{Alpha}");
								int ind=0;
								score=0.0;
								for(String k:key1)
								{
									//System.out.println("key : "+k);
									//System.out.println("value : "+value[ind]);
									if(k.length()==0)
										continue;
									while(value1[ind].length()==0)
										ind++;
									int keyscore=1;
									if(k==key)
										keyscore=7;
									switch(k)
									{
										case "t":score=score+100*(Integer.parseInt(value1[ind].toString()))*keyscore;
											break;
										case "b":score=score+15*(Integer.parseInt(value1[ind].toString()))*keyscore;
											break;
										case "c":score=score+30*(Integer.parseInt(value1[ind].toString()))*keyscore;
											break;
										case "i":score=score+30*(Integer.parseInt(value1[ind].toString()))*keyscore;
											break;		
										case "r":score=score+30*(Integer.parseInt(value1[ind].toString()))*keyscore;
											break;
										case "l":score=score+30*(Integer.parseInt(value1[ind].toString()))*keyscore;
											break;
										default:continue;	
									}
									ind++;
								}
								score=Math.log(score+1);
								score=score*idf;
								if(index.get(docid)==null)
								{
									index.put(docid, score);
								}
								else
								{
									Double temp=index.get(docid);
									temp=temp+score;
									index.put(docid,temp);
								}
							}
							
						}
					}
				}	
					//System.out.println(index.toString());
					
					br.close();
					raf.close();
				}
				TreeSet<Entry<Integer,Double>> ts=new TreeSet<Entry<Integer,Double>>(new scorecompa()); 
				ts.addAll(index.entrySet());
				Integer in1;
				for(int i=0;i<10;i++)
				{
					if(ts.size()==0)
						break;
		//			System.out.println(ts.pollFirst().toString());
					Integer ind=ts.pollFirst().getKey();
					//System.out.println("ind = "+ind+" and tertiary = "+(ind/500+1));
					Integer tertiary=ind/500+1;
					Integer offset=ind%500;
					//System.out.println("docid : "+ind);
					//System.out.println("tert : "+tertiary);
					//System.out.println("offset : "+offset);
					in1=title.get(tertiary);
//					if(in1 == null)
//						System.out.println("in1 is null");
//					//System.out.println(title.size());
					//System.out.println(title.get(16812));
					//System.out.println("in : "+in1);
					raf = new RandomAccessFile("C:\\Users\\m0k00eu\\workspace\\documentRetrieval\\secondtitle.txt", "r");
					raf.seek(in1);
					String l;
					int count=0;
					while((l=raf.readLine())!=null)
					{
						if(count==offset)
							break;
						count++;
					}
				//	String[] offsets=l.split("-");
					//System.out.println(l);
					raf.close();
					raf=new RandomAccessFile("C:\\Users\\m0k00eu\\workspace\\documentRetrieval\\title.txt", "r");
					//System.out.println(title.get(ts.pollFirst().getKey()));
					raf.seek(Integer.parseInt(l));
					result.append(raf.readLine()+"-666-");
					raf.close();
				}
				System.out.println(result.toString());
				return result.toString();
			}
		}
}
		

	

	
