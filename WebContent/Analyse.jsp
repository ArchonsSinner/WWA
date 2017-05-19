<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="Weka.Cluster,Weka.DiagramCreator,Weka.WekaClusterer,java.util.HashSet"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ANALyse</title>
</head>
<body>


	<%
		int Clusteranzahl = 5;
		String path = "C:/Users/lenna/Desktop/eclipse/";
		int activeCluster = 0;
		if (request.getParameter("activeCluster")!=null)
			activeCluster = Integer.parseInt(request.getParameter("activeCluster"));
		if (request.getParameter("Clusteranzahl")!=null)
			Clusteranzahl = Integer.parseInt(request.getParameter("Clusteranzahl"));
		WekaClusterer.setNumClusters(Clusteranzahl);
		Cluster[] clusters = WekaClusterer.clustering(path);
		String[] chartsFilenames = DiagramCreator.masterCreate(clusters);
		if (activeCluster> clusters.length)
			activeCluster = 0;
		String[] attributeNames = clusters[0].getValues().keySet().toArray(new String[0]);
		
	%>
<div style="float:up">
<a href="Analyse.jsp">Gesamt</a></td>
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
	<!-- Überschriften -->
	<table border="1" style="float:left">
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
</body>
</html>