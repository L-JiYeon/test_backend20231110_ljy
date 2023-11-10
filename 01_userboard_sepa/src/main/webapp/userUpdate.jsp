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
	boolean isS=dao.updateUser(new UserDto(userId,name,birthYear,addr,mobile1,mobile2,height));
	
	if(isS){
		//javascript를 활용해서 이동해보기
		%>
		<script type="text/javascript">
			alert("회원정보 수정함");
			location.href="userUpdateForm.jsp?userId=<%=userId%>";
		</script>
		<%
// 		response.sendRedirect("userUpdateForm.jsp?userId="+userId);
	}else{
		response.sendRedirect("error.jsp");
	}

%>



</body>
</html>



