<%--
	/**********************************************************
	*	작성자 : 박찬용
	*	작성일시 : 2022.02.09
	*	설명 : 준법심의 기준
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<div id="popup_confirm_reviewresult" class="poplayer popreviewresult"><!-- id값 수정 가능 -->
	<div class="inner">
		<header>
			<h2>준법심의 결과확인</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<!-- 심의 조건// -->
				<div class="graybox">
					<div class="grid-area">
						<table class="grid">
							<caption>그리드 정보</caption>
							<colgroup>
								<col style="width:120px;">
								<col style="width:208px;">
								<col style="width:120px;">
								<col style="width:auto;">
							</colgroup>
							<tbody>
								<tr>
									<th scope="col">마케팅 동의유형</th>
									<td id="mktYn"></td>
									<th scope="col">이미지 포함</th>
									<td id="imgChkYn"></td>
								</tr>
								<tr>
									<th scope="col">첨부 파일</th>
									<td colspan="3" id="attCnt"></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<!-- //심의 조건 -->

				<!-- 금지어// -->
				<h3 class="pop-title">금지어</h3>

				<div class="graybox-wrap clear">
					<!-- 제목// -->
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title" id="titCnt"></h3>
						</div>

						<div class="grid-area">
							<table class="grid">
								<caption>그리드 정보</caption>
								<tbody id="titBody">
									<tr><td>&nbsp;</td></tr>
								</tbody>
							</table>
						</div>
					</div>
					<!-- //제목 -->

					<!-- 본문// -->
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title" id="bodyCnt"></h3>
						</div>

						<div class="grid-area">
							<table class="grid">
								<caption>그리드 정보</caption>
								<tbody id="bodyBody">
									<tr><td></td></tr>
								</tbody>
							</table>
						</div>
					</div>
					<!-- //본문 -->
				</div>
				<!-- //금지어 -->

				<!-- btn-wrap// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue">닫기</button>
				</div>
				<!-- //btn-wrap -->
			</div>

		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_confirm_reviewresult');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_confirm_reviewresult');"></span>
</div>
<!-- //팝업 -->