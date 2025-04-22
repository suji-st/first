<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp" %>
<%-- page 지시자 태그 안에 errorPage 속성 추가할 수 있음
	errorPage 속성은 이 jsp 페이지에서 에러가 발생하면, 지정한 jsp 페이지로 에러를 넘기면서 페이지 바꾸기함
 --%>    
<%-- jsp 페이지에서 사용하는 jsp element 중 지시자 태그 (directive tag) 에서 page 지시자는 jsp 페이지에 한번만 사용할 수 있음
	page 지시자 태그에서 주로 이 페이지의 설정 정보를 속성으로 처리함
	import 속성만 분리할 수 있음
 --%>
 <%-- jsp 페이지에서 EL (Expression Language : 표현언어)을 사용한다면, import 필요없음 --%>
 <%-- <%@ page import="org.myweb.first.member.model.dto.Member" %> --%>
<%-- <%@ taglib uri="https://jakarta.ee/jsp/jstl/core" prefix="c" %> --%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  <%-- jstl의 core 라이브러리 사용을 선언함 --%>
<%--  <%
	//로그인 상태를 확인하기 위해서 세션 객체에 저장된 로그인한 회원 정보를 추출함
	//request 에 자동 등록된 session id 를 가지고, 세션 id가 일치하는 세션 객체를 찾아옴
	Member loginUser = (Member)session.getAttribute("loginUser");
%>  --%>  <%-- EL 사용시 필요없음 --%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>first</title>
<style type="text/css">
div.lineA {
	height: 100px;
	border: 1px solid gray;
	float: left;
	position: relative;
	left: 120px;
	margin: 5px;
	padding: 5px;
}
div#banner {
	width: 500px;
	padding: 0;
}
div#banner img {
	width: 450px;
	height: 80px;
	padding: 0;
	margin-top: 10px;
	margin-left: 25px;
}
div#loginBox {
	width: 280px;
	font-size: 10pt;
	text-align: left;
	padding-left: 20px;
}
div#loginBox button {
	width: 250px;
	height: 35px;
	background-color: navy;
	color: white;
	margin-top: 10px;
	margin-bottom: 15px;
	font-size: 14pt;
	font-weight: bold;
	cursor: pointer;  /* 손가락모양 : 클릭 가능한 버튼임을 표시함 */
}
div#loginBox a {
	text-decoration: none;  /* 밑줄 없애기 */
	color: navy;
}
</style>
<%-- jQuery js 파일 로드 선언 --%>
<script type="text/javascript" src="${ pageContext.servletContext.contextPath }/resources/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
function movePage(){
	//자바스크립트로 페이지 연결 이동 또는 서블릿 컨트롤러 연결 요청시에는
	//location 내장객체의 href 속성을 사용함 : 상대경로, 절대경로 둘 다 사용 가능함
	location.href = 'loginPage.do';  //서버측으로 로그인 페이지 내보내기 요청 보냄
}

//jquery document ready
$(function(){
	//최근 등록된 공지글 3개 (top-N) 전송받아서 출력 처리
	$.ajax({
		url: 'ntop3.do', /* 요청 url */
		type: 'post',  /* 전송방식 */
		dataType: 'json',    /* 컨트롤러로 부터 응답받을 데이터의 자료형임, 생략되면 string 임 */
		success: function(data){
			console.log('success : ' + data);  // [object Object]
			
			//object --> string
			var str = JSON.stringify(data);
			
			//string --> json : parsing
			var json = JSON.parse(str);
			//Jackson 라이브러리로 json 리턴한 경우의 결과 확인
			console.log(json.list);
			
			values = '';
			for(var i in json.list){
				 /* values += '<tr><td>' + json.list[i].no 
						+ '</td><td>' + json.list[i].title
						+ '</td><td>' + json.list[i].date 
						+ '</td></tr>';  */	
				values += '<tr><td>' + json.list[i].noticeNo 
				+ '</td><td><a href="ndetail.do?no=' + json.list[i].noticeNo + '">' + json.list[i].noticeTitle
				+ '</a></td><td>' + json.list[i].noticeDate 
				+ '</td></tr>'; 
			}  //for
			
			$('#newnotice').html($('#newnotice').html() + values);
		},
		error: function(jqXHR, textStatus, errorThrown){
			console.log('error : ' + jqXHR + ', ' + textStatus + ', ' + errorThrown);
		}
	});  //ajax : ntop3.do
		
	//조회수 많은 인기 게시글 3개 (top-N) 전송받아서 출력 처리
	$.ajax({
		url: 'btop3.do',
		type: 'post',
		dataType: 'json',
		success: function(data){
			console.log('success : ' + data);  // [object Object]
			
			//object --> string
			var str = JSON.stringify(data);
			
			//string --> json : parsing
			var json = JSON.parse(str);
			console.log('blist : ' + json.blist);
			
			values = '';
			for(var i in json.blist){
				/* values += '<tr><td>' + json.blist[i].bnum 
						+ '</td><td>' + json.blist[i].btitle
						+ '</td><td>' + json.blist[i].rcount 
						+ '</td></tr>'; */
				values += '<tr><td>' + json.blist[i].boardNum 
				+ '</td><td><a href="bdetail.do?bnum=' + json.blist[i].boardNum  + '">' + json.blist[i].boardTitle
				+ '</a></td><td>' + json.blist[i].boardReadCount 
				+ '</td></tr>';
			}  //for
			
			$('#toplist').html($('#toplist').html() + values);
		},
		error: function(jqXHR, textStatus, errorThrown){
			console.log('error : ' + jqXHR + ', ' + textStatus + ', ' + errorThrown);
		}
	});  //ajax : ntop3.do
});
</script>
</head>
<body>
<header>
	<h1>spring legacy mvc project : first</h1>
</header>
<%-- 메뉴바 표시 --%>
<%-- <%@ include file="menubar.jsp" %> --%>
<c:import url="/WEB-INF/views/common/menubar.jsp" />
<%-- 속성 url 은 브라우저에 표시되는 페이지 경로에 대한 url 임
	기본 웰컴 페이지 url 이 http://localhost:8080/first 임
	url 속성에 작성된 값을 기본 url 에 합쳐서 표현하면
	전체 url 이 http://localhost:8080/first/WEB-INF/views/common/menubar.jsp 작성하거나
	또는 기본 url 은 생략해도 되므로
	/WEB-INF/views/common/menubar.jsp 만 표기해도 됨
 --%>
<hr>
<div id="banner" class="lineA">
		<!-- html 태그에서 절대경로 표기는 이전 방식과 동일함, /context-root명/.....
			스프링 프로젝트는 프로젝트명이 context-root 명임
		 -->
		<img src="/first/resources/images/photo2.jpg">
	</div>
	<%-- <% if(loginUser == null){ //로그인하지 않았을 때 %>  --%> <%-- scriptlet 태그라고 함 --%>
	<%-- el 사용시에는 자바코드 사용할 수 없음. 대신 자바코드를 태그로 만들어서 제공하는 jstl 사용함 --%>
	<c:if test="${ empty sessionScope.loginUser }">
		<div id="loginBox" class="lineA"> 
		first 사이트 방문을 환영합니다.<br>
		<button onclick="movePage();">first 로그인</button><br>
		<%-- 로그인 버튼을 클릭하면 자바스크립트 movePage() 함수가 실행되어서, 로그인 페이지가 나타나게 처리함 --%>
		<a href="enrollPage.do">회원가입</a>
		<%-- 회원가입 클릭하면 회원가입페이지가 나타나게 연결 설정함 --%>
		</div>
	</c:if>
	<%-- <% }else{  //로그인 했을 때 %> --%>
	<c:if test="${ !empty sessionScope.loginUser }">
		<div id="loginBox" class="lineA">
			<%-- <%= loginUser.getUserName() %> 님 &nbsp;  --%> 
			<%-- EL 사용으로 바꾼다면, ${ 컨트롤러에서 setAttribute 할때 저장한 이름.멤버변수명 } --%>
			${ loginUser.userName } 님 &nbsp;
			<a href="logout.do">로그아웃</a> <br>
			<!-- a 태그로 서블릿 컨트롤러를 요청하면, 전송방식은 get 임 -->
			메일 0, 쪽지 0, 알림 0 <br>
			<%--  a 태그로 서비스 요청 url 작성시, 값도 같이 전송하려면 쿼리스트링(QueryString)을 사용해야 함
				url?전송이름=전송값&전송이름=전송값.......
				?전송이름=전송값&전송이름=전송값....... : 쿼리스트링이라고 함
				주의 : 쿼리스트링에는 공백 있으면 안 됨
			 --%>
			<a href="myinfo.do?userId=${ sessionScope.loginUser.userId }">내 정보 보기</a>
		</div>
	<%-- <% } %> --%>
	</c:if>
<hr style="clear:both">

<%-- 최근 등록된 공지글 3개 출력 : ajax --%>
<div style="float:left; border:1px solid navy; padding:5px; margin:5px; margin-left:150px;">
	<h4>새로운 공지사항</h4>
	<table id="newnotice" border="1" cellspacing="0" width="350">
		<tr><th>번호</th><th>제목</th><th>날짜</th></tr>
	</table>
</div>

<%-- 조회수 많은 인기게시글 3개 출력 : ajax --%>
<div style="float:left; border:1px solid navy; padding:5px; margin:5px; margin-left:50px;">
	<h4>인기 게시글</h4>
	<table id="toplist" border="1" cellspacing="0" width="350">
		<tr><th>번호</th><th>제목</th><th>조회수</th></tr>
	</table>
</div>



<hr style="clear:both">
<%-- jsp 파일 안에 별도로 작성된 다른 jsp 파일을 포함할 수 있음
	주의 : 상대경로만 사용할 수 있음
	목적 : 모든 뷰페이지에 반복 사용(출력)되는 내용이 있다면, 별도의 jsp 로 작성해서
		각 페이지에 포함시키면 됨 (중복 배제함)
 --%>
 <%-- <%@ include file="footer.jsp" %> --%>
 <c:import url="/WEB-INF/views/common/footer.jsp" />
 <%-- jstl 에서 절대경로는 /로 시작함, /가 webapp(content directory)를 의미함 --%>
</body>
</html>














