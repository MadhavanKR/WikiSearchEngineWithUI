<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
		  .header 
		  {
      		color: white;
      		background-color: black;
      		padding: 20px;
      		text-align: center;
    	  }
    	  
    	  table 
    	  {
    		border-collapse: collapse;
		  }

		   td 
		  {
			text-align: center;
			padding: 10px;
		  }
    	  th
    	  {
    	  	text-align: center;
			padding: 10px;
    	  	background-color: #008000;
    	  }
    	 input[type=text] 
    	 {
    		padding:5px; 
    		border:2px solid #ccc; 
    		-webkit-border-radius: 5px;
    		border-radius: 5px;
		 }

		input[type=text]:focus 
		{
    		border-color:#333;
		}

		input[type=submit] 
		{
    		padding:5px 15px; 
    		background:#007DC6; 
    		border:0 none;
    		cursor:pointer;
    		-webkit-border-radius: 5px;
    		border-radius: 5px; 
		}
		a:link
		{
			background-color: #007DC6;
			color: white;
			padding: 5px 7px;
			text-align: center;
			text-decoration: none;
			display: inline-block;
		}
		
		</style>

</head>
<body>
<%
String fileName = request.getParameter("fileName");
BufferedReader indexReader = new BufferedReader(new FileReader(new File(fileName)));
String line;
out.println("<table border = 1>");
out.println("<tr> <th> word </th> <th> Index information </th> </tr>");
while((line = indexReader.readLine())!=null)
{
	String[] array = line.split("-");
	out.println("<tr> <td>"+array[0]+"</td> <td>"+array[1]+"</td> </tr>");
}
out.println("</table>");
%>
</body>