<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
 
 <c:set var="nowpage" value="1" />
 <c:if test="${ !empty requestScope.paging.currentPage }">
 	<c:set var="nowpage" value="${ requestScope.paging.currentPage }" />
 </c:if>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>first</title>
<style type="text/css">
fieldset#ss {
	width: 650px;
	position: relative;
	left: 250px;
}
form fieldset {
	width: 600px;
}
form.sform {
	background: lightgray;
	width: 650px;
	position: relative;
	left: 250px;
	display: none;  /* 안 보이게 함 */ 
}
</style>
<script type="text/javascript" src="${ pageContext.servletContext.contextPath }/resources/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	//input 태그의 name 이 item 인 radio 값이 바뀌면(change) 작동되는 이벤트 핸들러 작성
	//jQuery('태그선택자').실행할메소드(....);  => jQuery 는 줄여서 $ 로 표기함
	$('input[name=item]').on('change', function(){
		//5개의 item 중에 체크표시가 된 radio 찾음 => 반복 처리 : each() 메소드 사용
		$('input[name=item]').each(function(index){
			//선택된 radio 순번대로 하나씩 checked 인지 확인함 : is() 메소드 사용
			if($(this).is(':checked')){
				//체크 표시된 아이템에 대한 폼이 보여지게 함
				$('form.sform').eq(index).css('display', 'block');
			}else{
				//체크 표시 안된 아이템에 대한 폼이 안 보여지게 함
				$('form.sform').eq(index).css('display', 'none');
			}
		});
	});
});
</script>
</head>
<body>
<c:import url="/WEB-INF/views/common/menubar.jsp" />
<hr>
<h1 align="center">공지사항</h1>
<br>
<center>
	<button onclick="location.href='${ pageContext.servletContext.contextPath }/nlist.do?page=1';">목록</button>
	&nbsp; &nbsp;
	<c:if test="${ !empty sessionScope.loginUser and loginUser.adminYN eq 'Y' }">
		<button onclick="location.href='${ pageContext.servletContext.contextPath }/moveWrite.do';">공지글 등록</button>
	</c:if>
</center>
<br>

<%-- 항목별 검색 기능 추가 --%>
<fieldset id="ss">
	<legend>검색할 항목을 선택하세요.</legend>
	<input type="radio" name="item" id="title"> 제목 &nbsp;
	<input type="radio" name="item" id="content"> 내용 &nbsp;
	<input type="radio" name="item" id="date"> 등록날짜 &nbsp;	
</fieldset>

<%-- 검색 항목별 값 입력 전송용 폼 만들기 --%>
<%-- 제목 검색 폼 --%>
<form action="nsearchTitle.do" id="titleform" class="sform" method="get">
	<input type="hidden" name="action" value="title">
	<fieldset>
	<legend>검색할 제목을 입력하세요.</legend>
		<input type="search" name="keyword" size="50"> &nbsp;
		<input type="submit" value="검색">
	</fieldset>
</form>

<%-- 내용 검색 폼 --%>
<form action="nsearchContent.do" id="contentform" class="sform" method="get">
	<input type="hidden" name="action" value="content">
	<fieldset>
	<legend>검색할 내용에 대한 키워드를 입력하세요.</legend>
		<input type="search" name="keyword" size="50"> &nbsp;
		<input type="submit" value="검색">
	</fieldset>
</form>

<%-- 등록날짜 검색 폼 --%>
<form action="nsearchDate.do" id="dateform" class="sform" method="get">
	<input type="hidden" name="action" value="date">
	<fieldset>
	<legend>검색할 공지글 등록날짜 범위를 입력하세요.</legend>
		<input type="date" name="begin"> ~ <input type="date" name="end"> &nbsp;
		<input type="submit" value="검색">
	</fieldset>
</form>


<br><br>
<%-- 조회된 공지사항 목록 출력 --%>
<table align="center" width="500" border="1" cellspacing="0" cellpadding="0">
	<tr>
		<th>번호</th>
		<th>제목</th>
		<th>작성자</th>
		<th>첨부파일</th>
		<th>날짜</th>		
	</tr>
	<c:forEach items="${ requestScope.list }" var="notice">
		<tr align="center">
			<td>${ notice.noticeNo }</td>
			<td>
				<c:if test="${ notice.importance eq 'Y' }">
					<img src="${ pageContext.servletContext.contextPath }/resources/images/ico_notice.gif" width="15" height="15">
				</c:if>
				<a href="ndetail.do?no=${ notice.noticeNo }">${ notice.noticeTitle }</a>
			</td>
			<td>${ notice.noticeWriter }</td>
			<td>
				<c:if test="${ !empty notice.originalFilePath }">◎</c:if>
				<c:if test="${ empty notice.originalFilePath }">&nbsp;</c:if>
			</td>
			<td>${ notice.noticeDate }</td>			
		</tr>
	</c:forEach>
</table>
<br>

<%-- << < 1 2 3 4 5 6 7 8 9 10 > >> 출력 : 공통 뷰로 따로 작업해서 import 해서 사용함 --%>
<c:import url="/WEB-INF/views/common/pagingView.jsp" />

<hr>
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>