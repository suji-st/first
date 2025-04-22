<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>first</title>

<%-- 아래의 자바스크립트 함수에서 사용할 url 변수 만들기함 --%>
<c:url var="replyf" value="breplyform.do">
	<c:param name="bnum" value="${ board.boardNum }" />
	<c:param name="page" value="${ requestScope.currentPage }" />
</c:url>

<c:url var="bdel" value="bdelete.do">
	<c:param name="boardNum" value="${ board.boardNum }" />
	<c:param name="boardLev" value="${ board.boardLev }" />
	<c:param name="boardRenameFileName" value="${ board.boardRenameFileName }" />
</c:url>

<c:url var="bup" value="bupview.do">
	<c:param name="bnum" value="${ board.boardNum }" />
	<c:param name="page" value="${ requestScope.currentPage }" />
</c:url>

<script type="text/javascript">
//댓글달기 버튼 클릭시 실행 함수
function requestReply(){
	location.href = '${ replyf }';
}

//삭제하기 버튼 클릭시 실행 함수
function requestDelete(){
	location.href = '${ bdel }';
}

//수정페이지로 이동 버튼 클릭시 실행 함수
function requestUpdatePage(){
	location.href = '${ bup }';
}
</script>
</head>
<body>
<c:import url="/WEB-INF/views/common/menubar.jsp" />
<hr>
<h1 align="center">${ requestScope.board.boardNum } 번 게시글 상세보기</h1>
<br>

<table align="center" width="500" border="1" cellspacing="0" cellpadding="5">
	<tr><th>제 목</th><td>${ requestScope.board.boardTitle }</td></tr>
	<tr><th>작성자</th><td>${ requestScope.board.boardWriter }</td></tr>
	<tr><th>등록날짜</th>
		<td><fmt:formatDate value="${ requestScope.board.boardDate }" pattern="yyyy-MM-dd" /></td></tr>
	<tr><th>첨부파일</th>
		<td>
			<%-- 첨부파일이 있다면, 파일명 클릭시 다운로드 작동되게 요청 처리함 --%>
			<c:url var="bfdown" value="bfdown.do">
				<c:param name="ofile" value="${ requestScope.board.boardOriginalFileName }" />
				<c:param name="rfile" value="${ requestScope.board.boardRenameFileName }" />
			</c:url>
			<c:if test="${ !empty requestScope.board.boardOriginalFileName }">
				<a href="${ bfdown }">${ requestScope.board.boardOriginalFileName }</a>
			</c:if>
			<c:if test="${ empty requestScope.board.boardOriginalFileName }">첨부파일 없음</c:if>
		</td></tr>
	<tr><th>내 용</th><td>${ requestScope.board.boardContent }</td></tr>
	<tr><th colspan="2">
		<%-- 로그인한 경우에 표시되게 함 --%>
		<c:if test="${ !empty sessionScope.loginUser }">
			<%-- 본인이 작성한 글 또는 관리자이면 수정, 삭제 버튼 제공함 --%>
			<c:if test="${ loginUser.userId eq board.boardWriter or loginUser.adminYN eq 'Y' }">
				<button onclick="requestUpdatePage(); return false;">수정페이지로 이동</button> &nbsp;
				<button onclick="requestDelete(); return false;">글삭제</button> &nbsp;
			</c:if>
			
			<%-- 본인이 작성한 글이 아니거나 관리자이면 댓글달기 버튼 표시함 --%>
			<c:if test="${ loginUser.userId ne board.boardWriter or loginUser.adminYN eq 'Y'  }">
				<%-- 글 레벨이 3보다 작은(2까지) 경우에만 댓글달기 버튼 표시함 : 대댓글까지만 등록하게 할 것임 --%>
				<c:if test="${ board.boardLev lt 3 }">
					<button onclick="requestReply(); return false;">댓글달기</button> &nbsp;
				</c:if>
			</c:if>
		</c:if>
	
		<button onclick="location.href='blist.do?page=${ requestScope.currentPage }';">목록</button> &nbsp;
		<button onclick="history.go(-1);">이전 페이지로 이동</button>
	</th></tr>
</table>

<hr>
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>