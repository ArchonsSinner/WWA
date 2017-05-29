<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="Weka.Cluster,Weka.DiagramCreator,Weka.WekaClusterer"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Analyse</title>
</head>
<body>


	<%
	Cluster[] clusters =null;
	String[] chartsFilenames = null;
	int activeCluster = 0;
	String[] attributeNames = null;
try{
		if(session.getAttribute("uname") == null || session.getAttribute("username") == ""){
			response.sendRedirect("Login");
			return;
		}
	
		clusters = (Cluster[])request.getSession().getAttribute("clusters");
		chartsFilenames = (String[])request.getSession().getAttribute("charts");

		
		if (request.getParameter("activeCluster")!=null)
			activeCluster = Integer.parseInt(request.getParameter("activeCluster"));
		if (activeCluster> clusters.length)
			activeCluster = 0;
		attributeNames = clusters[0].getValues().keySet().toArray(new String[0]);
}catch(Exception Exc){
	response.sendRedirect("Error.html");
	return;
}
	
		
	%>
<div style="float:up">
<a href="Analyse.jsp">Gesamt</a>
			<%
				for (int i = 0; i < clusters.length; i++) {
					if (i + 1 == activeCluster) {
			%>
			<b>Cluster <%=clusters[i].getIndex()%></b>
			<%
					} else {
			%>
			<a href="Analyse.jsp?activeCluster=<%=i+1 %>">Cluster <%=i+1%></a>
			<%
					}
				}
			%>
</div>

	<div style="float:left">
	
	<div style="float:left">
	test<br>
	test<br>
	test<br>
	test<br>
	test<br>
	test<br>
	test<br>
	test<br>
	test<br>
	
	<!-- 
	Hier Kommt irgendwas f�r die Marketing Ma�nahmen rein
	Wird ganz links neben der Tabelle angezeigt
	 -->
	</div>
	
	<div style="float:right">
	<table border="1">
		<!--  Tabelle �berschrift   -->
		<tr >
			<td>Attribut</td>
			<%
				for (int i = 0; i < clusters.length; i++) {
					if (i + 1 == activeCluster|| activeCluster==0) {
			%>
	
			<td>Cluster <%=i+1%></td>
			<%
					}	
				}
			%>
		</tr>
		<!--  Tabelle  Daten   -->
		<% for(int row=0;row<clusters[0].getValues().size();row++){%>
		<tr>
		<td><%=attributeNames[row] %></td>
		<%
				for (int i = 0; i < clusters.length; i++) {
					if (i + 1 == activeCluster || activeCluster==0) {
			%>
			
			<td> <%=(clusters[i].getValues().get(attributeNames[row])) %></td>
			<%				
					}
				}
			%>
		</tr>
		<%
		} 
		%>
		
	</table>
	</div>	
	</div>
	
	<div>
	<!--  Grafiken -->
	<%
		if (activeCluster > 0) {

	%>
	
	<img src="<%=chartsFilenames[(activeCluster * 2) - 1]%>">
	<img src="<%=chartsFilenames[activeCluster * 2]%>">
	<%
		} else {
	%>
	<img src="<%=chartsFilenames[0]%>">
	<%
		}
	%>
	</div>
</body>
</html>