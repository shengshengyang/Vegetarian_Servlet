<%@ page import="bean.ForumBean" %>
<%@ page import="java.sql.*" %>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="bean.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%User userForum = (User)request.getSession().getAttribute("user");%>
<%
response.setContentType("text/html;charset=UTF-8");
response.setHeader("Cache-Control","no-cache"); // HTTP 1.1
response.setHeader("Pragma","no-cache"); // HTTP 1.0
response.setDateHeader ("Expires", -1); // Prevents caching at the proxy server
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增文章</title>
</head>
<body>
<jsp:useBean id="vge" class="bean.ForumBean" scope="session" />
<h2>
新增資料如下請確認
</h2>
<form action=".\ForumServlet" method="post">
<table  cellspacing="2" cellpadding="1" border="1" width="80%">
 <!--<tr bgcolor="#7AFEC6">
   <td>編號:</td>
    <td><jsp:getProperty name="vge" property="vgeid" /></td>
</tr>  -->
<tr bgcolor="#7AFEC6">
    <td>名稱:</td>
    <td><jsp:getProperty name="vge" property="vgename" /></td>
</tr>
<tr bgcolor="#7AFEC6">
    <td>標題:</td>
    <td><jsp:getProperty name="vge" property="vgetheme" /></td>
</tr>
<tr bgcolor="#7AFEC6">
    <td>發表:</td>
    <td><jsp:getProperty name="vge" property="vgecontent" /></td>
</tr>



</table>
<center>

<p><input type="submit" name="confirm" value="確認" ></p>
<p><input type="submit" name="ForumHome" value="回首頁" ></p>
</center>
</form>
</body>
</html>