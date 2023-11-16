<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("utf-8"); %>
<%response.setContentType("text/html; charset=utf-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js">
   
</script>
<link rel="stylesheet" href="css/layout1.css" />
<script type="text/javascript">

   onload=function(){
      //id 중복체크를 완료한 후에 다른 정보를 입력할 수 있게 처리
      //입력박스에 입력하기 전에 idChk=y/n여부를 확인해서 y면 입력 실행, n이면 id입력박스로 커서 이동
      var inputs=document.querySelectorAll("input[name]");
      var idChk=document.getElementById("idChk");
      for(var i=1; i<inputs.length;i++){
         inputs[i].onfocus=function(){
            if(idChk.textContent=="n"){         
               alert("ID 중복체크를 먼저 확인해주세요");
               inputs[0].focus();   //id 입력박스로 이동
            }
         }
      }
   }
   //ID중복체크
   function idChk(){
      //입력된 ID값 구하기
      var id=document.getElementsByName("id")[0].value;
      if(id==""){
         alert("ID를 입력하세요");
      }else{
//          window.open("userController.jsp?command=idChk"
//                   + "&id="+id,"ID 확인", "width=300px, height=300px");
         $.ajax({
            url:"idChk.user",   //요청 url
            method:"post",      //전송방식
            data:{"id":id},      //전송할 데이터
            dataType:"text",   //전달받을 데이터 타입(xml,text,html,json....)
            async:false,      //$.ajax()메서드를 실행하는 방식
            success:function(data){   //데이터 받기 성공하면 함수 실행하겠다는 뜻
//                alert(data);
            	if(data=="null"){
//             		alert("아이디 사용가능");
					// 사용가능
					$("#enabledId").css("color","red").text("사용 가능합니다.");
					$("#idChk").text("y");
            	} else{
            		// 사용 못함
            		$("#enabledId").css("color","red").text("중복된 아이디입니다.");
					$("#idChk").text("n");
            	}
            },
            error:function(){   //데이터 받기 실패하면 함수 실행하겠다는 뜻
               alert("통신실패");
            }
         });
      }
   }
   
   // 패스워드 확인하기
   function isPW(form){
      if(form.password.value!=form.password2.value){
         alert("비밀번호를 확인하세요");
         form.password.value="";   //비밀번호 초기화
         form.password2.value="";
         form.password.focus();   //비밀번호를 바로 입력할 수 있도록 커서 넣기
         return false;   //false를 리턴하면 이벤트를 취소시킴 -> submit취소
      }
   }
</script>
</head>
<body>
<nav class="navbar">
   <div id="navbar">
      <ul class="navbar-nav">
         <li><a href="home.user">HOME</a></li>
         <li>ABOUT</li>
         <li>CONRECT</li>
      </ul>
   </div>
</nav>
<div class="container">
   <h1>회원가입</h1>
   <form class="form-group" action="addUser.user" method="post" onsubmit="return isPW(this)">
      <input style="width:700px" type="text" name="id" placeholder="ID" required="required"/>
      <span id="idChk" style="display:none;">n</span>
      <a href="#" onclick="idChk()">중복체크</a>
      <span id="enabledId"></span><br/>
      <input type="text" name="name" placeholder="이름" required="required"/>
      <input type="password" name="password" placeholder="PASSWORD" required="required"/>
      <input type="password" name="password2" placeholder="PASSWORD확인" required="required"/>
      <input type="text" name="address" placeholder="주소" required="required"/>
      <input type="email" name="email" placeholder="이메일" required="required"/>
      <button type="submit">가입완료</button>
      <button type="button" onclick="location.href='home.user'">HOME</button>
   </form>
</div>
<div class="footer">
   Copyright 1999-2023 . 한경닷컴 All rights reserved.   
</div>
   
</body>
</html>