<%@include file="header.jsp" %>
<%@page import="com.hk.user.dtos.BoardDto"%>
<%@page import="java.util.List"%>
<%@page import="com.hk.user.daos.BoardDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("utf-8");
%>
<%
response.setContentType("text/html; charset=utf-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 목록 조회</title>

<script type="text/javascript">
	function insertBoardForm() {
		location.href = "BoardController.jsp?command=insertBoardForm";
	}

	//전체선택 박스 구현
	function allSel(bool) {
		var chks = document.getElementsByName("chk"); //[chk,chk,chk...]
		for (var i = 0; i < chks.length; i++) {
			chks[i].checked = bool; //true면 체크, false면 체크 해제
		}
	}
	
	//체크박스의 체크여부 확인하고, submit 실행하기
	//true를 반환하면 submit / false 반환하면 submit X
	function isAllCheck(){
		if(confirm("정말 삭제할 거니?")){
			var count=0;
			var chks=document.getElementsByName("chk");	//[chk,chk,chk,...]
			for(var i=0;i<chks.length;i++){
				if(chks[i].checked){	//체크여부 확인: 체크되면 true
					count++;
				}
			}
			if(count==0){
				alert("최소 하나 이상 체크하세요");
			}
			
			return count>0?true:false;
		}
		return false;
	}
	
</script>

</head>

<%
//실행부: java코드를 실행하는 영역
List<BoardDto> list = (List<BoardDto>) request.getAttribute("list");
%>

<body>

	<h1>게시판</h1>
	<h2>글목록</h2>
	<form action="BoardController.jsp" method="post" onsubmit="return isAllCheck()">
		<input type="hidden" name="command" value="muldel" />
		<table border="1">
			<col width="50px" />
			<col width="50px" />
			<col width="100px" />
			<col width="300px" />
			<col width="200px" />
			<tr>
				<th><input type="checkbox" name="all"
					onclick="allSel(this.checked)" /></th>
				<th>seq</th>
				<th>작성자</th>
				<th>제목</th>
				<th>작성일</th>
			</tr>
			<%
			for (int i = 0; i < list.size(); i++) {
				BoardDto dto = list.get(i); //list[dto,dto,...] --> dto꺼내기
			%>
			<tr>
				<td><input type="checkbox" name="chk" value="<%=dto.getSeq()%>" /></td>
				<td><%=dto.getSeq()%></td>
				<td><%=dto.getId()%></td>
				<td><a
					href="BoardController.jsp?command=detailBoard&seq=<%=dto.getSeq()%>"><%=dto.getTitle()%></a></td>
				<td><%=dto.getRegDate()%></td>
			</tr>
			<%
			}
			%>
			<tr>
				<td colspan="5">
					<button type="button" onclick="insertBoardForm()">글추가</button> <input
					type="submit" value="삭제" />
				</td>
			</tr>
		</table>
	</form>

<%@include file="footer.jsp" %>
</body>
</html>