<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
 
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>first</title>
</head>
<body>
<c:import url="/WEB-INF/views/common/menubar.jsp" />
<hr>
<h1 align="center">새 공지글 등록 페이지</h1>
<br>

<!-- form 에서 파일이 첨부되어서 전송될 경우에는 반드시 enctype="multipart/form-data" 속성을 추가해야 함
	전송방식은 post 로 지정함
 -->
<form action="ninsert.do" method="post" enctype="multipart/form-data">
<table align="center" width="700" border="1" cellspacing="0" cellpadding="5">
	<tr><th width="120">제 목</th>
		<td><input type="text" name="noticeTitle" size="70"></td></tr>
	<tr><th>작성자</th>
		<td><input type="text" name="noticeWriter" readonly value="${ sessionScope.loginUser.userId }"></td></tr>
	<tr><th>중요도</th>
		<td><input type="checkbox" name="importance" value="Y"> 중요</td></tr>
	<tr><th>중요도 지정 종료 날짜</th>
		<td><input type="date" name="impEndDate"></td></tr>
	<tr><th>첨부파일</th>
		<td>
			<input type="file" name="ofile">
			<%-- name 속성의 이름은 property 명과 다르게 지정함
				파일업로드 실패시 파일명만 문자열로 command 객체에 저장되지 않게 하기 위함
			 --%>
		</td></tr>
	<tr><th>내 용</th>
		<td><textarea name="noticeContent" rows="5" cols="70"></textarea></td></tr>
	<tr><th colspan="2">
		<input type="submit" value="등록하기"> &nbsp;
		<input type="reset" value="등록취소"> &nbsp;
		<input type="button" value="목록" onclick="history.go(-1); return false;"> &nbsp;		
	</th></tr>
</table>
</form>

<hr>
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>