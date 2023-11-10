<%@page import="com.hk.board.dtos.UserDto"%>
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
	//request.getParameter("name"): 전달된 값을 받을 때 사용 -> name은 input 속성
	String userId=request.getParameter("userId");
	String name=request.getParameter("name");
	
	String birthYearStr=request.getParameter("birthYear");
	int birthYear=Integer.parseInt(birthYearStr);	//정수형으로 형변환
	
	String addr=request.getParameter("addr");
	String mobile1=request.getParameter("mobile1");
	String mobile2=request.getParameter("mobile2");
	
	String heightStr=request.getParameter("height");
	int height=Integer.parseInt(heightStr);		//정수형으로 형변환
	
	UserDao dao=new UserDao();
	boolean isS=dao.insertUser(new UserDto(userId,name,birthYear,addr,mobile1,mobile2,height));
	
	if(isS){
		//회원 등록하고 초기화면으로 돌아가기
		response.sendRedirect("index.jsp");
	}else{
		//회원 등록에 실패하면 error 페이지로 가기
		response.sendRedirect("error.jsp");
	}
%>

</body>
</html>