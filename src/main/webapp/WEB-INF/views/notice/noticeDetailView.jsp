<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>first</title>
</head>
<body>
<c:import url="/WEB-INF/views/common/menubar.jsp" />
<hr>
<h1 align="center">${ requestScope.notice.noticeNo } 번 공지글 상세보기</h1>
<br>

<table align="center" width="500" border="1" cellspacing="0" cellpadding="5">
	<tr><th>제 목</th><td>${ requestScope.notice.noticeTitle }</td></tr>
	<tr><th>작성자</th><td>${ requestScope.notice.noticeWriter }</td></tr>
	<tr><th>등록날짜</th>
		<td><fmt:formatDate value="${ requestScope.notice.noticeDate }" pattern="yyyy-MM-dd" /></td></tr>
	<tr><th>첨부파일</th>
		<td>
			<%-- 첨부파일이 있다면, 파일명 클릭시 다운로드 작동되게 요청 처리함 --%>
			<c:url var="nfdown" value="nfdown.do">
				<c:param name="ofile" value="${ requestScope.notice.originalFilePath }" />
				<c:param name="rfile" value="${ requestScope.notice.renameFilePath }" />
			</c:url>
			<c:if test="${ !empty requestScope.notice.originalFilePath }">
				<a href="${ nfdown }">${ requestScope.notice.originalFilePath }</a>
			</c:if>
			<c:if test="${ empty requestScope.notice.originalFilePath }">첨부파일 없음</c:if>
		</td></tr>
	<tr><th>내 용</th><td>${ requestScope.notice.noticeContent }</td></tr>
	<tr><th colspan="2">
		<button onclick="location.href='nlist.do?page=1';">목록</button> &nbsp;
		<button onclick="history.go(-1);">이전 페이지로 이동</button>
	</th></tr>
</table>

<hr>
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>