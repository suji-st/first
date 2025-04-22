<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%-- <%@ taglib uri="https://jakarta.ee/jsp/jstl/core" prefix="c" %> --%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>testCore</title>
</head>
<body>
<h1>jstl core library test page</h1>
<hr>
<h4>c:set 으로 변수 만들기</h4>
<c:set var="num1" value="100" />
<c:set var="num2" value="200" />
num1 변수 값 : ${ num1 } <br>
num2 변수 값 : ${ num2 } <br>
<c:set var="sum" value="${ num1 + num2 }" /> 
sum 변수 값 : ${ sum } <br>
<%
	int n1 = 111, n2 = 222;
%>
<c:set var="total" value="<%= n1 + n2 %>" /> 
total 변수 값 : ${total } <br>
<hr>

<h4>c:set 으로 배열 만들기</h4>
<c:set var="array" scope="request"> <%-- scope="request" : array 변수를 request 객체에 저장함, page, request, session, application --%>
	red, green, blue, pink, magenta
</c:set>
array 배열의 값 : ${array } <br>
<hr>

<h4>c:remove 로 변수 total 제거</h4>
<c:remove var="total" scope="page" /> <%-- page 객체에 저장된 total 변수 제거함, scope 의 page (기본값) --%>
total 변수 값 : ${total } <br>
<hr>

<H4>c:out : 출력 기능 제공 태그</H4>
그대로 출력 : <c:out value="<h2>태그로 출력하기</h2>" escapeXml="true" /> <br>
태그 해석해서 출력 : <c:out value="<h2>태그로 출력하기</h2>" escapeXml="false" /> <br>
전송온 값 출력 : <c:out value="${param.name }" default="홍길동" /> <br>
<%-- ${param.전송온이름} == request.getParameter("전송온이름") 과 같음
	실행 테스트시에는 브라우저 주소표시줄에 기존 url 뒤에 직접 입력함 : 
	http://localhost:8080/first/moveCore.do?name=김철수
 --%>
<hr>

<h4>c:if 태그 : if문과 같은 처리를 하는 태그</h4>
<c:if test="${num1 < num2 }"> <%-- test 속성에 조건식 표기함, el 사용함 "${ 비교 조건식 }" --%>
	num2 가 크다.
</c:if>
<c:if test="${num1 >= num2 }">
	num1 가 크거나 같다.
</c:if>
<hr>

<h4>c:choose 태그 : switch 문과 같은 처리를 하는 태그</h4>
<c:set var="no" value="1" />
<c:choose>
	<c:when test="${no eq 1 }">
		<font color="magenta">반갑습니다.</font> <br>
	</c:when>
	<c:when test="${no eq 2 }">
		<font color="blue">안녕하세요.</font> <br>
	</c:when>
	<c:otherwise>
		환영합니다.<br>
	</c:otherwise>
</c:choose>
<hr>

<h4>c:forEach 태그 : for 문, for each 문 처리를 하는 태그</h4>
<c:forEach begin="1" end="10">
반복 확인<br>
</c:forEach>
<hr>
<c:forEach var="cnt" begin="1" end="10" step="2">
	${cnt } <br>
</c:forEach>
<hr>
<c:forEach var="fsize" begin="1" end="7">
	<font size="${fsize }">글자크기 ${fsize }</font> <br>
</c:forEach>
<hr>
<c:forEach items="${array }" var="color">
	<font color="${color }">글자색 적용 : ${color }</font> <br>
</c:forEach>
<hr>
<%
	java.util.ArrayList<String> bookList = new java.util.ArrayList<String>();
	bookList.add("java");
	bookList.add("oracle");
	bookList.add("jdbc");
	bookList.add("html5");
	bookList.add("css3");
	bookList.add("javascript");
	bookList.add("jquery");
	bookList.add("servlet");
	bookList.add("jsp");
	bookList.add("mybatis");
	bookList.add("spring");
	bookList.add("ajax");
	bookList.add("el & jstl");
%>
<c:forEach items="<%= bookList %>" var="book" varStatus="status">
	${status.count } : ${book } <br>
</c:forEach>
<hr>

<h4>c:forTokens 태그 : java.util.StringTokenizer 클래스와 같은 기능을 수행하는 태그</h4>
<ul>
	<c:forTokens var="color" items="yellow pink blue green red" delims=" ">
		<li>${ color }</li>
	</c:forTokens>
</ul>
<hr>
<c:forTokens var="color" items="yellow/pink*blue-green red" delims="/*- ">
	${ color } <br>
</c:forTokens>
<hr>

<h4>c:url 태그 : 다른 jsp 페이지나 컨트롤러로 연결 요청 처리할 때 사용하는 태그 (url 재작성을 위한 태그임)</h4>
<!-- <a href="moveFmt.do?no=123">testFmt.jsp 페이지로 이동</a> <br> -->
<c:url value="moveFmt.do" var="tf">
	<c:param name="no" value="123" />
</c:url>
<a href="${tf }">testFmt.jsp 페이지로 이동</a> <br>
<hr>

<h4>c:import 태그 : jsp 지시자(directive) 태그의 include 지시자 또는 jsp:include 표준 액션태그와 같은 기능을 수행함</h4>
별도로 작성된 jsp 또는 html 파일을 원하는 위치에 가져다 표시할 수 있음 (포함시키다.) <br>
전체 뷰 페이지 반복되는 영역 또는 메뉴바, 햄버거 메뉴 등을 표시할 때 주로 사용됨 <br>
<hr>
<c:import url="/WEB-INF/views/common/footer.jsp" /> 
<%-- url="http://localhost:8080/first/WEB-INF/views/common/footer.jsp"
	기본 웰컴 url : http://localhost:8080/first : 생략할 수 있음
 --%>
<hr>

<h4>c:catch 태그 : 자바의 try catch 문과 같음</h4>
<c:catch var="e">
	<c:set var="num" value="null" />
	<c:set var="num3" value="77" />
	나눈 결과 : ${ num3 / num } <br> <%-- Exception 발생 - e 변수가 예외 정보를 저장함 --%>
</c:catch>
<c:if test="${ e != null }">
	에러 메세지 : ${e.message } <br>  <%-- out.print(e.getMessage()); 과 같음 --%>
</c:if>
<hr>

<h4>c:redirect 태그 : response.sendRedirect() 메소드와 같은 태그임</h4>
<c:set var="test" value="0" />
<c:if test="${test eq 1 }">
	<c:redirect url="moveFmt.do" />
</c:if>





<BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>
</body>
</html>





