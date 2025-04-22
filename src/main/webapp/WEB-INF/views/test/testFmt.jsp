<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%-- <%@ taglib uri="https://jakarta.ee/jsp/jstl/core" prefix="c" %> --%>   
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>   
<%-- <%@ taglib prefix="fmt" uri="https://jakarta.ee/jsp/jstl/fmt" %> --%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>testFmt</title>
</head>
<body>
<h1>jstl fmt library test page</h1>
<hr>

<h4>fmt:formatDate 태그 : 날짜와 시간에 포멧 적용하는 태그</h4>
value 속성에는 반드시 java.util.Date 객체를 사용해야 함 (java.sql.Date 사용 가능 : sub class 임) <br>
<fmt:formatDate value="<%= new java.util.Date() %>" /> <br> <%-- 기본 포멧 : yyyy.MM.dd. --%>

<c:set var="current" value="<%= new java.util.Date() %>" />
<fmt:formatDate value="${current }" type="time" /> <br> <%-- 시간만 포메팅 --%>
<fmt:formatDate value="${current }" type="date" /> <br> <%-- 날짜만 포매팅 : 기본 --%>
<fmt:formatDate value="${current }" type="both" /> <br> <%-- 날짜와 시간 둘 다 포매팅 --%>
<hr>

[short] : <fmt:formatDate value="${current }" type="both" dateStyle="short" timeStyle="short" /> <br>
[medium] : <fmt:formatDate value="${current }" type="both" dateStyle="medium" timeStyle="medium" /> <br>
[long] : <fmt:formatDate value="${current }" type="both" dateStyle="long" timeStyle="long" /> <br>
[full] : <fmt:formatDate value="${current }" type="both" dateStyle="full" timeStyle="full" /> <br>
<hr>

현재 날짜 : <fmt:formatDate value="${current }" type="date" pattern="yyyy/MM/dd (E)" /> <br>
현재 시간 : <fmt:formatDate value="${current }" type="time" pattern="(a) hh:mm:ss" /> <br>
<hr>

<h4>fmt:setLocale 태그 : 지역대 설정하는 태그</h4>
<fmt:setLocale value="ko_kr" />

<h4>fmt:timeZone 태그 : 시간대 설정하는 태그임, fmt:setTimeZone 사용해도 됨</h4>
한국 현재 시간 : <fmt:formatDate value="${current }" type="both" /> <br>
뉴욕 현재 시간 : 
<fmt:timeZone value="America/New_York">
	<fmt:formatDate value="<%= new java.util.Date() %>" type="both" /> <br>
</fmt:timeZone>
런던 현재 시간 : 
<fmt:timeZone value="Europe/London">
	<fmt:formatDate value="<%= new java.util.Date() %>" type="both" /> <br>
</fmt:timeZone>
<hr>

<h4>fmt:formatNumber 태그 : 숫자에 포멧 지정 태그</h4>
숫자 그대로 출력 : <fmt:formatNumber value="123456789" groupingUsed="false" /> <br>
천단위 구분기호 적용 : <fmt:formatNumber value="123456789" groupingUsed="true" /> <br>
실수값 소숫점 아래 자릿수 지정 : pattern 속성 사용 (#, 0 으로 자릿수 지정함) <br>
# 을 사용한 경우 : <fmt:formatNumber value="1.234567" pattern="#.##" /> <br>
# 을 사용한 경우 : <fmt:formatNumber value="1.2" pattern="#.##" /> <br>
0 을 사용한 경우 :  <fmt:formatNumber value="1.2" pattern="#.00" /> <br>
<hr>

<h4>type 속성으로 백분율, 통화기호 표시 처리함</h4>
<fmt:formatNumber value="0.12" type="percent" /> <br>
<fmt:formatNumber value="123456789" type="currency" /> <br>
<fmt:formatNumber value="123456789" type="currency" currencySymbol="$" /> <br>



<br><br><br><br><br><br><br><br><br><br><br><br><br>
</body>
</html>








