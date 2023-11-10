<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
   a{text-decoration: none;}
</style>
<script type="text/javascript">
	function deleteUser(userId){
		if(confirm("정말 삭제하겠습니까?")){
			//javascript에서 form전송하는 방법: submit()이용하기
			document.forms[1].submit();
		}
	}
</script>
</head>
<body>
<h1>회원관리시스템</h1>
<p><a href = "userList.jsp">(1) 회원 조회 (조회 후 수정/삭제 가능)</a></p>
<p><a href = "userInsertForm.jsp">(2) 신규회원 등록</a></p>

<form action = "userUpdateForm.jsp" method = "get">
   <label>(3) 회원수정-회원아이디:</label>
   <input type = "text" name = "userId"/>
   <input type = "submit" value = "수정"/>
</form>
<br/>
<form action = "userDelete.jsp" method = "get">
   <label>(4) 회원삭제-회원아이디:</label>
   <input type = "text" name = "userId"/>
   <input type = "button" value = "삭제" onclick="deleteUser(userId.value)"/>
</form>
</body>
</html>