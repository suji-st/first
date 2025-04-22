<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<%-- isErrorPage="true" : 다른 jsp 페이지의 에러를 넘겨받아서 에러 출력 처리용 페이지에 사용하는 속성임 
	반드시 표기해야 함
	이 속성이 있어야, jsp 내장객체 중 exception 객체 사용할 수 있음
--%>    
<%-- <%@ taglib uri="https://jakarta.ee/jsp/jstl/core" prefix="c" %> --%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>first : error</title>
</head>
<body>
<h1>오류 발생</h1>
<%-- jsp 내장객체 중 exception 객체 사용 : 
	isErrorPage="true" 라고 지정했으므로, 다른 jsp 페이지에서 발생한 에러(Exception)를 자동으로 전달받게 됨
 --%>
<%--  <% if(exception != null){ //다른 jsp 페이지에서 에러가 발생되었다면 %>
 	<h3>jsp 페이지 오류 : <%= exception.getMessage() %></h3>
 <% }else{ //jsp 페이지 에러가 아니라면(컨트롤러나 모델쪽 에러인 경우) %>
 	<h3>백앤드 서버측 오류 : <%= request.getAttribute("message") %></h3>
 <% } %> --%>
 
 <%-- el 과 jstl 사용 --%>
 <c:set var="e" value="<%= exception %>" />
 <c:if test="${ !empty e }">
 	<h3>jsp 페이지 오류 : ${ e.message }</h3>
 </c:if>
 <c:if test="${ empty e }">
 	<h3>백앤드 서버측 오류 : ${ requestScope.message }</h3>
</c:if>
 
 <%-- 스프링에서는 내보낼 뷰페이지들은 모두 설정된 뷰리졸버를 거쳐서 전송 나가야 함
 	즉, 컨트롤러로 요청을 보내서 내보낼 뷰파일명을 리턴시켜야 함
  --%>
  <a href="main.do">메인 페이지로 이동</a>
</body>
</html>