<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="Weka.Cluster,Weka.DiagramCreator,Weka.WekaClusterer" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>


<%	int Clusteranzahl =5;
	String path ="C:/Users/lenna/Desktop/eclipse/";
	int activeCluster=0;
	
	WekaClusterer.setNumClusters(Clusteranzahl);
	Cluster[] clusters =WekaClusterer.clustering(path);
	String[] chartsFilenames=DiagramCreator.masterCreate(clusters); %>
	
	<a href="">Gesamt</a>
	<% for(int i=0;i<clusters.length;i++){
		if(i==activeCluster){%>
			<b>Cluster <%= clusters[i].getIndex() %></b>
		<%}else{ %>
			<a href="">Cluster <%= clusters[i].getIndex() %></a>
		<%}
	} %>
</body>
</html>