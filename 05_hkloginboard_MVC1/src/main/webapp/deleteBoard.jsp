<%@page import="com.hk.user.daos.BoardDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("utf-8");%>
<%response.setContentType("text/html; charset=utf-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%

	String seq=request.getParameter("seq");
	int sseq=Integer.parseInt(seq);
	
	BoardDao dao=new BoardDao();
	boolean isS=dao.deleteBoard(sseq);	//삭제실행
	
	if(isS){
		%>
	<!-- 		html 부분 -->
	<script type="text/javascript">
					alert("글을 삭제합니다.");
					location.href="boardList.jsp";
			</script>

	<%
	}else{
		%>
	<!-- 		html 부분 -->
	<script type="text/javascript">
					alert("글 삭제 실패!");
					location.href="detailBoard.jsp?seq=<%=seq%>";
			</script>

	<%
	}
%>


</body>
</html>