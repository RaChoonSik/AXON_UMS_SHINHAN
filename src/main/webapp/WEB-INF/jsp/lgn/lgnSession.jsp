<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.11.14
	*	설명 : 다중 접속으로 블락된 세션에 대한 처리 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<script type="text/javascript">
	alert("다중 접속으로 로그아웃되었습니다 다시 로그인 해주세요");
	window.location.href = "<c:url value='/lgn/lgnP.ums'/>";
</script>

