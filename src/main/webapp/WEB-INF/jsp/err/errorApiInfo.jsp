<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.12.08
	*	설명 : API 호출 정보 부족  에러 페이지
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>
 
<body>
	<div id="wrap">

		<div id="inaccessible">

			<section class="inaccess-inner">
				<h1><img src="/img/common/thumb_info.png" class="accesskey" alt="정보부족"></h1>
				<h2>페이지 호출을 위한 정보가 부족합니다 </h2>
				<p>부족정보 : <c:out value="${error}"/><p> 
			</section>

		</div>
		<!-- // inaccessible -->

	</div>

</body> 
