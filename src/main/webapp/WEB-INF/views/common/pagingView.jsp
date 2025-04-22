<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 
 <c:set var="paging" value="${ requestScope.paging }" />
 <%-- url 뒤에 추가할 쿼리스트링으로 사용할 변수로 저장함 --%>
 <c:set var="queryParams" 
 value="action=${ requestScope.action }&keyword=${ requestScope.keyword }&begin=${ requestScope.begin }&end=${ end }" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>
<div style="text-align: center;">
	<%-- 1페이지로 이동 --%>
	<c:if test="${ paging.currentPage eq 1 }">
		[맨처음] &nbsp;
	</c:if>
	<c:if test="${ paging.currentPage gt 1 }"> <%-- gt : greater than, > 연산자를 의미함 --%>
		<a href="${ paging.urlMapping }?page=1&${ queryParams }">[맨처음]</a> &nbsp;
	</c:if>
	
	<%-- 이전 페이지 그룹으로 이동 --%>
	<%-- 이전 그룹이 있다면 --%>
	<c:if test="${ (paging.currentPage - 10) lt paging.startPage and (paging.currentPage - 10) gt 1 }">
		<a href="${ paging.urlMapping }?page=${paging.startPage - 10}&${ queryParams }">[이전그룹]</a> &nbsp;
	</c:if>
	<%-- 이전 그룹이 없다면 --%>
	<c:if test="${ !((paging.currentPage - 10) lt paging.startPage and (paging.currentPage - 10) gt 1) }">
		[이전그룹] &nbsp;
	</c:if>
	
	<%-- 현재 출력할 페이지가 속한 페이지 그룹의 페이지 숫자 출력 : 10개 --%>
	<c:forEach begin="${ paging.startPage }" end="${ paging.endPage }" step="1" var="p">
		<c:if test="${ p eq paging.currentPage }">
			<font color="blue" size="4"><b>${ p }</b></font>
		</c:if>
		<c:if test="${ p ne paging.currentPage }">
			<a href="${ paging.urlMapping }?page=${ p }&${ queryParams }">${ p }</a>
		</c:if>
	</c:forEach>
	
	<%-- 다음 페이지 그룹으로 이동 --%>
	<%-- 다음 그룹이 있다면 --%>
	<c:if test="${ (paging.currentPage + 10) gt paging.startPage and (paging.currentPage + 10) lt paging.maxPage }">
		<a href="${ paging.urlMapping }?page=${paging.startPage + 10}&${ queryParams }">[다음그룹]</a> &nbsp;
	</c:if>
	<%-- 다음 그룹이 없다면 --%>
	<c:if test="${ !((paging.currentPage + 10) gt paging.startPage and (paging.currentPage + 10) lt paging.maxPage) }">
		[다음그룹] &nbsp;
	</c:if>
	
	<%-- 마지막 페이지로 이동 --%>
	<c:if test="${ paging.currentPage ge paging.maxPage }"> <%-- ge : greater equal, >= 연산자임 --%>
		[맨끝] &nbsp;
	</c:if>
	<c:if test="${ !(paging.currentPage ge paging.maxPage) }"> 
		<a href="${ paging.urlMapping }?page=${ paging.maxPage }&${ queryParams }">[맨끝]</a> &nbsp;
	</c:if>
</div>
</body>
</html>