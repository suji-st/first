<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@ taglib uri="https://jakarta.ee/jsp/jstl/core" prefix="c" %> --%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  <%-- jstl의 core 라이브러리 사용을 선언함 --%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>first</title>
<style type="text/css">
table th { background-color: #9ff; }
table#outer { border: 2px solid navy; }
</style>
<%-- jQuery js 파일 로드 선언 --%>
<script type="text/javascript" src="${ pageContext.servletContext.contextPath }/resources/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
function dupIdCheck(){
	//입력한 사용자 아이디가 사용 가능한지 확인하는 함수 : ajax 기술 사용해야 함 
	// 서버측에 요청하고 응답받을 때 페이지 새로고침되면 안됨 - 해결 : ajax 기술
	
	//jQuery.ajax({ settings });
	$.ajax({
		url: 'idchk.do',
		type: 'post',
		data: { userId: $('#userId').val() },  <%-- 전송보낼값 지정 속성임 { 전송이름: 전송값 }--%>
		<%-- 응답오는 값이 문자열이면, dataType 옵션은 생략해도 됨 --%>
		success: function(data){
			console.log('success : ' + data); //문자열이 전송오면, 그대로 문자열임
			if(data === 'ok'){
				alert('사용 가능한 아이디입니다.');
				$('#userPwd').focus();
			}else{
				alert('이미 사용중인 아이디입니다. 다시 입력하세요.');
				$('#userId').select();
			}
		},
		error: function(jqXHR, textStatus, errorThrown){  //요청이 실패했을 때 실행되는 콜백함수
			console.log('error : ' + jqXHR + ', ' + textStatus + ', ' + errorThrown);
		}
	});
}

function validate(){
	//서버측으로 보내기 전에 입력값들이 유효한지 검사하는 함수
	
	//암호와 암호확인이 일치하는지 확인
	var pwdValue = $('#userPwd').val();
	var pwdValue2 = document.getElementById('userPwd2').value;
	console.log(pwdValue + ', ' + pwdValue2);
	
	if(pwdValue !== pwdValue2){
		alert('암호와 암호확인이 일치하지 않습니다. 다시 입력하세요.');
		document.getElementById('userPwd').value = ''; //기록된 값 지우기
		$('#userPwd2').val('');  //기록된 값 지우기
		document.getElementById('userPwd').focus();  //입력커서 지정함
		return false;  //전송 취소함
	}
	
	//아이디의 값 형식이 요구대로 구성되었는지 확인
	//암호의 값 형식이 요구대로 구성되었는지 확인
	//정규표현식 사용함
	
	return true;  //전송 보냄
}  // validate() closed

//제이쿼리 : $(function(){ ... }); 축약형이고, jQuery(document).ready(function(){ ... }); 과 같음
window.onload = function(){
	// input type="file" 이 선택한 사진파일 이미지 미리보기 처리
	var photofile = document.getElementById('photofile');  // input file 태그 정보 가지고 옴
	photofile.addEventListener('change', function(event){
		const files = event.currentTarget.files;
		const file = files[0];
		console.log(file.name);  // 선택한 파일명 확인
		
		//선택한 파일을 img 태그의 src 속성 값으로 적용함 : 이미지 변경될 것임
		const photo = document.getElementById('photo');
		//photo.src = '서버측에 있는 이미지파일의 상대|절대경로/' + file.name; (서버측에 있는 이미지 파일임)
		//현재 선택한 사진파일은 클라이언트 컴퓨터에 있는 파일임, src 속성에 적용할 수 없음
		
		//클라이언트 컴퓨터에 있는 사진파일을 읽어들여서 출력되게 처리해야 함
		// 파일입력 -- 출력처리
		const reader = new FileReader();  //자바스크립트의 파일읽기 객체 생성함
		//람다(lambda) 스트림 방식 사용
		//이벤트 콜백함수 실행구문(기존 방식) : reader.onload = function(e){ 읽어들이기 처리 };
		reader.onload = (e) => {  //e : event 객체
			photo.setAttribute('src', e.target.result);  //e.target : 이벤트 발생 대상 (읽어들일 파일정보)
			photo.setAttribute('data-file', file.name);
		};
		reader.readAsDataURL(file);  // 읽어서 img 에 적용 출력함
	});  // input file 태그에 change 이벤트 지정
};

</script>
</head>
<body>
<%-- 메뉴바 표시 --%>
<c:import url="/WEB-INF/views/common/menubar.jsp" />
<%-- 속성 url 은 브라우저에 표시되는 페이지 경로에 대한 url 임
	기본 웰컴 페이지 url 이 http://localhost:8080/first 임
	url 속성에 작성된 값을 기본 url 에 합쳐서 표현하면
	전체 url 이 http://localhost:8080/first/WEB-INF/views/common/menubar.jsp 작성하거나
	또는 기본 url 은 생략해도 되므로
	/WEB-INF/views/common/menubar.jsp 만 표기해도 됨
 --%>
<hr>

<h1 align="center">회원 가입 페이지</h1>
<br>
<%-- form 에서 파일이 첨부되어서 전송이 될 경우에는, 반드시 enctype="multipart/form-data" 속성을 추가해야 함 --%>
<form action="enroll.do" method="post" onsubmit="return validate();" enctype="multipart/form-data" >
<%-- form 에서 파일 첨부가 없다면 --%>
<!-- <form action="enroll.do" method="post" onsubmit="return validate();"> -->
<table id="outer" align="center" width="700" cellspacing="5" cellpadding="0">
	<tr><th colspan="2">회원 정보를 입력하세요. (* 표시는 필수입력 항목입니다.)</th></tr>
	<tr><th width="120">*아이디</th>
	<%-- input 태그의 name 속성의 이름은 member.dto.Member 클래스의 property 명과 같게 함 --%>
		<td><input type="text" name="userId" id="userId" required> &nbsp; 
			<input type="button" value="중복검사" onclick="return dupIdCheck();">
		</td></tr>
	<tr><th>사진첨부</th>
		<td>
		<%-- 사진을 파일로 첨부해서 전송한다면, input type="file" 로 지정하면 됨, 사진 미리보기 안됨 --%>
		<%-- <input type="file" name="photoFileName"> --%>
		<%-- 첨부된 사진 미리보기가 되도록 하려면, 사진 미리보기용 영역 지정 : 서버로는 전송 안 됨 --%>
		<div id="myphoto" style="margin:0; width:150px; height:160px; padding:0; border:1px solid navy;">
			<%-- 사진 첨부가 없을 경우를 위한 미리보기용 이미지 출력되게 함 --%>
			<img src="/first/resources/images/photo1.jpg" id="photo" 
			style="width:150px;height:160px;border:1px solid navy;display:block;margin:0;padding:0;" 
			alt="사진을 드래그 드롭하세요.">
		</div> <br>
		<input type="file" id="photofile" name="photofile">
		<%-- 첨부파일의 name 은 필드명(property)와 별개로 지정함
			파일업로드 실패시 파일명만 문자열로 command 객체(dto)에 저장되지 않게 하기 위함
		 --%>
		</td></tr>
	<tr><th>*암호</th>
		<td><input type="password" name="userPwd" id="userPwd" required></td></tr>
	<tr><th>*암호확인</th>
		<td><input type="password" id="userPwd2" required></td></tr>
	<tr><th>*이름</th>
		<td><input type="text" name="userName" id="userName" required></td></tr>
	<tr><th>*성별</th>
		<td>
			<input type="radio" name="gender" value="M"> 남자 &nbsp;
			<input type="radio" name="gender" value="F"> 여자 
		</td></tr>
	<tr><th>*나이</th>
		<td><input type="number" name="age" min="19" max="100" value="20" required></td></tr>
	<tr><th>*전화번호</th>
		<td><input type="tel" name="phone" required></td></tr>
	<tr><th>*이메일</th>
		<td><input type="email" name="email" required></td></tr>
	<tr><th colspan="2">
		<input type="submit" value="가입하기"> &nbsp; 
		<input type="reset" value="작성취소"> &nbsp;
		<a href="main.do">Home</a>
	</th></tr>	
</table>
</form>




<hr style="clear:both">
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>