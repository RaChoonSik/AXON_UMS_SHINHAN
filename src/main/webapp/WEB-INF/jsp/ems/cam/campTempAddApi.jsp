<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.09.28
	*	설명 : 캠페인 등록 처리 후 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<script type="text/javascript" src="<c:url value='/js/jquery_2.1.1.min.js'/>"></script>
<script type="text/javascript">
<c:choose>
	<c:when test="${result eq 'Success'}">
		alert("등록되었습니다.");
		window.parent.document.getElementById("btnAdd").disabled = true;
	</c:when>
	<c:when test="${result eq 'ApiFail'}">
		alert("등록에는 성공하였으나 처리 결과 API 전송에 실패하였습니다 " + "\n" + "${apiResult}" );  
		window.parent.document.getElementById("btnAdd").disabled = true;
	</c:when>
	<c:otherwise> 
		alert("등록에 실패하였습니다");
	</c:otherwise>
</c:choose>
</script>
