<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- page 지시자 태그 안의 import 속성만 따로 분리 작성할 수 있음 --%>
<%-- el 사용시에는 아래 구문은 필요없음 --%>
<%-- <%@ page import="org.myweb.first.member.model.dto.Member" %>
<%
	//로그인 확인을 위해서 내장된 session 객체를 사용해서, 저장된 회원 정보를 추출함
	Member loginUser = (Member)session.getAttribute("loginUser");
%> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%-- <%@ taglib uri="https://jakarta.ee/jsp/jstl/core" prefix="c" %> --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<style type="text/css">
nav > ul#menubar {
	list-style: none;
	position: relative;
	left: 100px;
}
nav > ul#menubar li {
	float: left;
	width: 120px;
	height: 30px;
	margin-right: 5px;
	padding: 0;
}
nav > ul#menubar li a {
	text-decoration: none;
	width: 120px;
	height: 30px;
	display: block;
	background: orange;
	color: navy;
	text-align: center;
	font-weight: bold;
	margin: 0;
	text-shadow: 1px 1px 2px white;
	padding-top: 5px;
}
nav > ul#menubar li a:hover {
	text-decoration: none;
	width: 120px;
	height: 30px;
	display: block;
	background: navy;
	color: white;
	text-align: center;
	font-weight: bold;
	margin: 0;
	text-shadow: 1px 1px 2px white;
	padding-top: 5px;
} 
hr { clear: both; }
</style>
</head>
<body>
<%-- 로그인하지 않았을 때 --%>
<%-- <% if(loginUser == null){ %> --%>
<c:if test="${ empty sessionScope.loginUser }">  
<%-- session 에 저장된 loginUser 를 찾으려면, el 내장객체인 sessionScope.저장이름 으로 표기해도 됨 
	sessionScope 를 생략하면 loginUser 를 찾는 순서가
	pageScope - requestScope - sessionScope - applicationScope 순으로 찾음
	sessionScope.loginUser == 자바코드로는 session.getAttribute("loginUser") 와 같음
--%>
	<nav>
		<ul id="menubar">
		<%--
			스프링에서의 el 에서 절대경로 표기 : 
			context-root 명으로 표기하지 않고
			${ pageContext.servletContext.contextPath } 표기함
		 --%>
		 	<li><a href="${ pageContext.servletContext.contextPath }/main.do">Home</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/nlist.do?page=1">공지사항</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/blist.do?page=1">게시글</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/moveAOP.do">AOP란</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/moveAjax.do">ajax 처리</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/moveAPI.do">test api</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/moveJSTL.do">test jstl</a></li>
		</ul>
	</nav>
</c:if>
<%-- 일반 회원이 로그인했을 때 --%>
<%-- <% }else if(loginUser.getAdminYN().equals("N")){ %> --%>
<c:if test="${ !empty sessionScope.loginUser and sessionScope.loginUser.adminYN eq 'N' }">
	<nav>
		<ul id="menubar">		
		 	<li><a href="${ pageContext.servletContext.contextPath }/main.do">Home</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/nlist.do?page=1">공지사항</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/blist.do?page=1">게시글</a></li>		 	
		</ul>
	</nav>
</c:if>
<%-- 관리자가 로그인했을 때 --%>
<%-- <% }else if(loginUser.getAdminYN().equals("Y")){ %> --%>
<c:if test="${ !empty sessionScope.loginUser and sessionScope.loginUser.adminYN eq 'Y' }">
	<nav>
		<ul id="menubar">		
		 	<li><a href="${ pageContext.servletContext.contextPath }/main.do">Home</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/nlist.do?page=1">공지사항관리</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/blist.do?page=1">게시글관리</a></li>
		 	<li><a href="${ pageContext.servletContext.contextPath }/mlist.do?page=1">회원관리</a></li>		 	
		</ul>
	</nav>
<%-- <% } %> --%>
</c:if>

</body>
</html>











