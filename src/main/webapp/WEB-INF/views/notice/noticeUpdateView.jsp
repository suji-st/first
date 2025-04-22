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
<h1 align="center">${ requestScope.notice.noticeNo } 번 공지글 수정페이지 (관리자용)</h1>
<br>

<!-- form 에서 파일이 첨부되어서 전송될 경우에는 반드시 enctype="multipart/form-data" 속성을 추가해야 함
	전송방식은 post 로 지정함
 -->
<form action="nupdate.do" method="post" enctype="multipart/form-data">
	<input type="hidden" name="noticeNo" value="${ notice.noticeNo }">
	<input type="hidden" name="originalFilePath" value="${ notice.originalFilePath }">
	<input type="hidden" name="renameFilePath" value="${ notice.renameFilePath }">
<table align="center" width="700" border="1" cellspacing="0" cellpadding="5">
	<tr><th width="120">제 목</th>
		<td><input type="text" name="noticeTitle" size="70" value="${ notice.noticeTitle }"></td></tr>
	<tr><th>작성자</th>
		<td><input type="text" name="noticeWriter" readonly value="${ sessionScope.loginUser.userId }"></td></tr>
	<tr><th>중요도</th>
		<td>
			<c:if test="${ notice.importance eq 'Y' }">
				<input type="checkbox" name="importance" value="Y" checked> 중요
			</c:if>
			<c:if test="${ notice.importance eq 'N' }">
				<input type="checkbox" name="importance" value="Y"> 중요
			</c:if>
		</td></tr>
	<tr><th>중요도 지정 종료 날짜</th>
		<td><input type="date" name="impEndDate" value="${ notice.impEndDate }"></td></tr>
	<tr><th>첨부파일</th>
		<td>
			<c:if test="${ !empty notice.originalFilePath }">
				${ notice.originalFilePath } &nbsp;
				<input type="checkbox" name="deleteFlag" value="yes"> 파일삭제 <br>
				변경 : <input type="file" name="ofile">
			</c:if>
			<c:if test="${ empty notice.originalFilePath }">
				첨부파일 없음 <br>
				추가 : <input type="file" name="ofile">
			</c:if>			
			<%-- name 속성의 이름은 property 명과 다르게 지정함
				파일업로드 실패시 파일명만 문자열로 command 객체에 저장되지 않게 하기 위함
			 --%>
		</td></tr>
	<tr><th>내 용</th>
		<td><textarea name="noticeContent" rows="5" cols="70">${ notice.noticeContent }</textarea></td></tr>
	<tr><th colspan="2">
		<input type="submit" value="수정하기"> &nbsp;
		<input type="reset" value="수정취소"> &nbsp;
		<input type="button" value="이전 페이지로 이동" onclick="history.go(-1); return false;"> &nbsp;	
		<input type="button" value="목록" onclick="location.href='nlist.do'; return false;">	
	</th></tr>
</table>
</form>

<hr>
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>