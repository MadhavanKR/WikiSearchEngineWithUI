<%@page import="java.util.Currency"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="wikiSearchEngine.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Query results</title>
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
		<h1 class="header"> RESULTS </h1>
<center>
<body>
<%
	String query = request.getParameter("query").trim();
 	String resultString = queryHandler.executeQuery(query);
 	//System.out.println(resultString);
 	out.println("<table border=1>");
%>
	<tr> <th>Page Name </th> <th> Document Id </th> <th> Link for Document content </th> </tr>
<%
	if(resultString.contains("not in index"))
		out.println("given query words are not present in our index files");
	else
	{
		if(resultString.contains("-666-"))
			for(String curResult:resultString.split("-666-"))
 			{
 				String temp[] = curResult.split("~~~");
 				String pageName = temp[0];
 				String docId = temp[1];
 				String httpLink = "/documentRetrieval/fetchContents.jsp?docId="+docId;
 				out.println("<tr> <td>"+pageName+"</td> <td>"+docId+"</td> <td> <a target=# href="+httpLink+"/>"+"click here"+"</a>"+"</td> </tr>");
 			}
		else
		{
			out.println("<tr> <td colspan=4>sorry! not present in our index files</td> </tr>");
		}
	}
%>
</table>
</center>
</body>
</html>