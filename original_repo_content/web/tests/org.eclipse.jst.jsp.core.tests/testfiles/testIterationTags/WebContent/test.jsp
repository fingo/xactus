<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/plain.tld" prefix="plain" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<!-- Should generate an error since continue; can't be used in this construct -->
<plain:simple>
<% if(true) continue; %>
</plain:simple>

<plain:loop>
<% if(true) continue; %>
</plain:loop>
</body>
</html>
