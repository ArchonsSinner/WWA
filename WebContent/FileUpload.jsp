<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Datei Auswahl</title>
</head>
<body>

	<%
		//if (session.getAttribute("username") == null || session.getAttribute("username") == "") {
			//response.sendRedirect("Login.jsp");
		//};
	%>

	<form action="FileUpload" method="post" enctype="multipart/form-data">
		<label>Bitte wählen Sie eine .xls Datei zur Analyse aus.</label><input
			type="file" name="file" /><br> <input type="submit" />
	</form>

</body>
</html>