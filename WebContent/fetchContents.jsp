<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	String docIdString = request.getParameter("docId");
	if(docIdString.charAt(docIdString.length()-1)=='/')
		docIdString = docIdString.substring(0, docIdString.length()-1);
	int docId = Integer.parseInt(docIdString);
	int rawFileNum = docId/5000;
	int offset = docId%5000;
	String rawFile = "rawFile"+rawFileNum;
	
	BufferedReader rawFileReader = new BufferedReader(new FileReader("C:\\Users\\m0k00eu\\workspace\\documentRetrieval\\"+rawFile));
	int count=0;
	String line;
	while(count<offset && (line=rawFileReader.readLine())!=null)
	{
		if(line.compareTo("~~~~~DELIMITER~~~~~")==0)
		{
			count++;
			if(count>=offset)
				break;
		}
	}
	StringBuilder pageContent= new StringBuilder();
	while((line=rawFileReader.readLine())!=null)
	{
		if(line.compareTo("~~~~~DELIMITER~~~~~")==0)
			break;
		pageContent.append(line);
	}
	out.println(pageContent.toString());
	
%>
</body>
</html>