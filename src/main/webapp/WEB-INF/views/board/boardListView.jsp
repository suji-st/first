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
<h1 align="center">자유 게시글</h1>
<br>
<center>
	<button onclick="location.href='${ pageContext.servletContext.contextPath }/blist.do?page=1';">목록</button> &nbsp;
	<c:if test="${ !empty sessionScope.loginUser }">
		<button onclick="location.href='bwform.do';">글쓰기</button>
	</c:if>
</center>
<br>

<br><br>
<%-- 조회된 게시글 목록 출력 --%>
<table align="center" width="650" border="1" cellspacing="0" cellpadding="0">
	<tr>
		<th>번호</th>
		<th>제목</th>
		<th>작성자</th>
		<th>첨부파일</th>
		<th>날짜</th>
		<th>조회수</th>		
	</tr>
	<c:forEach items="${ requestScope.list }" var="board">
		<tr align="center">
			<td>${ board.boardNum }</td>
			<td>
				<%-- 원글과 댓글, 대댓글을 구분하기 위해 제목글자 앞에 ▶ 표시함 --%>
				<c:if test="${ board.boardLev eq 2 }">
				&nbsp; ▶
				</c:if>
				<c:if test="${ board.boardLev eq 3 }">
				&nbsp; &nbsp; ▶▶
				</c:if>
				
				<c:url var="bd" value="bdetail.do">
					<c:param name="bnum" value="${ board.boardNum }" />
					<c:param name="page" value="${ nowpage }" />
				</c:url>
				<a href="${ bd }">${ board.boardTitle }</a>
			</td>
			<td>${ board.boardWriter }</td>
			<td>
				<c:if test="${ !empty board.boardOriginalFileName }">◎</c:if>
				<c:if test="${ empty board.boardOriginalFileName }">&nbsp;</c:if>
			</td>
			<td>${ board.boardDate }</td>
			<td>${ board.boardReadCount }</td>				
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