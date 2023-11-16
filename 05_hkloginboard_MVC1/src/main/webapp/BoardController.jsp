
<%@page import="java.net.URLEncoder"%>
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
<title>Insert title here</title>
</head>
<body>

	<%
	//1단계: command 값 받기 - 어떤 요청인지 확인을 위해 값을 받는다
	String command = request.getParameter("command");

	//2단계: DAO 객체 생성 - DB관련 작업 수행을 위해 준비..
	BoardDao dao = new BoardDao();

	//3단계: command값 확인해서 분기 실행(요청분기)
	if (command.equals("boardList")) { //글목록 요청 처리
		//5단계: dao메서드 실행
		List<BoardDto> list = dao.getAllList();
		//6단계: Scope객체에 담기
		request.setAttribute("list", list); //requestScope에 저장: "list":list
		//7단계: 페이지 이동
		pageContext.forward("boardList.jsp");
	} else if (command.equals("insertBoardForm")) { //글 추가폼으로 이동
		response.sendRedirect("insertBoardForm.jsp");
	} else if (command.equals("insertBoard")) { //글 추가하기 실행
		//4단계: 파라미터 받기
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		boolean isS=dao.insertBoard(new BoardDto(id,title,content));
		if(isS){
			response.sendRedirect("BoardController.jsp?command=boardList");
			//객체전달할 때만 forward()
			//pageContent.forward("hkController.jsp?command=boardList");

			%>
	<script type="text/javascript">
		alert("글을 추가합니다.");
		location.href = "BoardController.jsp?command=boardList"; //글을 추가하고, 글 목록 페이지로 이동하기
		</script>
		<%
			}else{
				%>
<!-- 		<script type="text/javascript"> -->
// 			alert("글 추가 실패!");
// 			location.href = "BoardController.jsp?command=insertBoardForm"; //글 추가 실패하면, 글 추가 페이지로 이동하기
<!-- 		</script> -->
		<%
			}
		} else if(command.equals("detailBoard")){
			int seq=Integer.parseInt(request.getParameter("seq"));
			BoardDto dto=dao.getBoard(seq);
			request.setAttribute("dto", dto);
			pageContext.forward("detailBoard.jsp");
			
		} else if(command.equals("updateBoardForm")){
			int seq=Integer.parseInt(request.getParameter("seq"));
			BoardDto dto=dao.getBoard(seq);
			request.setAttribute("dto", dto);
			//객체를 전달하려면 forward()로 이동해야 함
			pageContext.forward("updateBoardForm.jsp");
		}else if(command.equals("updateBoard")){	//수정하기
			int seq=Integer. parseInt(request.getParameter("seq"));
			String title=request.getParameter("title");
			String content=request.getParameter("content");
			
			boolean isS=dao.updateBoard(new BoardDto(seq,title,content));
			if(isS){
				//수정하고, 상세조회페이지로 이동
				response.sendRedirect("BoardController.jsp?command=detailBoard&seq="+seq);
			}else{
				response.sendRedirect("error.jsp?msg="
									+URLEncoder.encode("글수정실패","utf-8"));
			}
		} else if(command.equals("deleteBoard")){
			int seq=Integer.parseInt(request.getParameter("seq"));
			boolean isS=dao.deleteBoard(seq);
			if(isS){
				response.sendRedirect("BoardController.jsp?command=boardList");
			}else{
				response.sendRedirect("error.jsp?msg="
							+URLEncoder.encode("글삭제실패","utf-8"));
			}
		} else if(command.equals("muldel")){
			//삭제할 글의 번호가 파라미터로 전달됨
			String[]seqs=request.getParameterValues("chk");	//chk=1,2,3,4,5,6,...
			
			//유효값을 서버쪽(java로 처리)에서 처리할 수 있다.
			//유효하지 않은 값을 처리하려고 서버에서 작업을 함 - 자원낭비개념
// 			if(seqs==null || seqs.length==0){
				%>
<!-- 				<script type="text/javascript"> -->
// 					alert("최소 하나 이상 체크하세요");
// 					location.href="hkController.jsp?command=boardList";
<!-- 				</script> -->
				<%
// 			}else{
				boolean isS=dao.mulDel(seqs);
				if(isS){
					response.sendRedirect("BoardController.jsp?command=boardList");
				}else{
					response.sendRedirect("error.jsp?msg="
										+ URLEncoder.encode("여러글삭제실패","utf-8"));
				}
				
// 			}
			
		}
	%>

</body>
</html>





