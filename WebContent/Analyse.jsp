<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Weka.Cluster,Weka.DiagramCreator,Weka.WekaClusterer"%>
<%@ page import="Weka.MarketingHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel='stylesheet' href='bulma.css'>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
	<nav class='nav' style='background-color: #BDBDBD'>
                                <div class='nav-left'>
                                    <a href="DataControl" class='nav-item'>
                                        <img src='Images/logo.gif' alt='KD logo'>
                                    </a>
                                </div>
                           </nav>
	
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
	 <label>Marketingmaßnahmen:</label><br>
	<select name="marketingSelection" multiple="yes" style="width:240px">
	
	<%MarketingHelper helper = new MarketingHelper(request,response); %>
	<%= helper.genOptions() %>
	</select>
	
	<form action="MarketingServlet" method="post">
	<input type="text" name="marketingAction">
	<input type="submit" value="Hinzufügen">
	</form>
	</div>
	
	<div style="float:right">
	<table border="1">
		<!--  Tabelle Ãberschrift   -->
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
	<img src="ImageServlet?imagepath=<%=chartsFilenames[(activeCluster * 2) - 1]%>">
	<img src="ImageServlet?imagepath=<%=chartsFilenames[activeCluster * 2]%>">
	<%
		} else {
	%>
	<img src="ImageServlet?imagepath=<%=chartsFilenames[0]%>">
	<%
		}
	%>
	</div>
</body>
</html>