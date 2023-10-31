<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.02.07
	*	설명 :준법심의 결과
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<div id="popup_rns_prohibit" class="poplayer popreviewresult">
	<div class="inner">
		<header>
			<h2>준법심의 결과</h2>
		</header>
		<div class="popcont" id="divPopRnsProhit">
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_rns_prohibit');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
<!-- //팝업 -->
