<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.01
	*	설명 : 자동메일 서비스 부분 수정 처리 후 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<script type="text/javascript" src="<c:url value='/js/jquery_2.1.1.min.js'/>"></script>
<script type="text/javascript">
<c:choose>
	<c:when test="${result eq 'Success'}">
		alert("수정되었습니다.");
		$("#searchForm", parent.document).attr("target","").attr("action","./serviceListP.ums").submit();
	</c:when>
	<c:when test="${result eq 'filter'}">
		alert("보안에 위배되는 스크립트가 포함되었습니다. 관리자에게 문의하세요");
	</c:when>
	<c:otherwise>
		alert("수정 처리중 오류가 발생하였습니다.");
	</c:otherwise>
</c:choose>
</script>
