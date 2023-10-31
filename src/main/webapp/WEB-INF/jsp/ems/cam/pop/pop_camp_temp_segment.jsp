<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.09.27
	*	설명 : 캠페인템플릿 내부 수신자그룹 관리
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

	<!-- 팝업// -->
	<div id="popup_camp_temp_segment" class="poplayer poptestsendinfo">
		<div class="inner">
			<header>
				<h2>수신자그룹 선택</h2>
			</header>
			<div class="popcont" id="divPopContSeg">
			</div>
			<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_camp_temp_segment');"><span class="hidden">팝업닫기</span></button>
		</div>
		<span class="poplayer-background"></span>
	</div>
	<!-- //팝업 -->
