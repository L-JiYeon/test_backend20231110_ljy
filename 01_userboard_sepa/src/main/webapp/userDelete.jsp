<%@page import="com.hk.board.daos.UserDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
	String userId = request.getParameter("userId");
	
	UserDao dao=new UserDao();
	boolean isS=dao.deleteUser(userId);	//회원정보 삭제
	if(isS){
		response.sendRedirect("index.jsp");
	}else{
		response.sendRedirect("error.jsp");
	}
%>

</body>
</html>