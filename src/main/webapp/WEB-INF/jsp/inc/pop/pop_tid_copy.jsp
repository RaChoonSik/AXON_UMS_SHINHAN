<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2024.03.08
	*	설명 : 캠플릿 템플릿 복사시 TID 입력 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_tid_copy" class="poplayer popregisteragent"><!-- id값 수정 가능 -->
	<div class="inner small">
		<header>
			<h2>API 캠페인 템플릿 복사</h2>
		</header>

		<div class="popcont">
			<div class="cont"> 
				<div class="filebox">
					<input type="text" id="popCopyTid" style="width:calc(100% - 10.4rem);" placeholder="API 템플릿ID를 입력해주세요.(숫자)" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" />
					<button type="button" class="btn fullblue" id="popChkTid"  onclick="checkTid();">중복확인 검사</button>
				</div> 

				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" id="btnPopCopy" onclick="goPopCopy();" disabled>복사</button>
					<!-- <button type="button" class="btn big" onclick="fn.popupClose('#popup_tid_copy');">취소</button> -->
					<button type="button" class="btn big" onclick="goPopCopyCancel();">취소</button>
				</div>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_tid_copy');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>