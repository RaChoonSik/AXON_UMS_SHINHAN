<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.02.07
	*	설명 :준법심의 확정 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<div id="popup_prohibit" class="poplayer popreviewresult">
	<div class="inner">
		<header>
			<h2>준법심의 결과</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<!-- 심의 조건// -->
				<input type="hidden" id="popProhibitTitleCnt"  value="0">
				<input type="hidden" id="popProhibitTitleDesc" value="">
				<input type="hidden" id="popProhibitTextCnt"   value="0">
				<input type="hidden" id="popProhibitTextDesc"  value="">
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
									<td id="popProhibitMarket">해당없음 or 은행</td>
									<th scope="col">이미지 포함</th>
									<td id="popProhibitImage">본문 이미지 포함</td>
								</tr>
								<tr>
									<th scope="col">첨부 파일</th>
									<td  id="popProhibitAttach" colspan="3">상품설명서. PDF 외 2건</td>
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
					<div id="divPopProhibitTitle" class="graybox">
					</div>
					<!-- //제목 -->

					<!-- 본문// -->
					<div id="divPopProhibitText" class="graybox">
					</div>
					<!-- //본문 -->
				</div>
				<!-- //금지어 -->

				<!-- 경고문구// -->
				<p class="caution"><!-- 3종의 문구 중 심의 결과에 맞는 문구가 노출됩니다. -->
					* 준법심의 검토 대상 정보가 발생하였습니다.<br>
					* 메일 내용에 금지어가 포함된 경우 취소 후 수정 바랍니다.<br> 
					* 현재 메일을 확정시 준법감시부 승인 후 발송됩니다.
				</p>
				<!-- //경고문구 -->
				<!-- btn-wrap// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="popProhibitSave();">확정</button>
					<button type="button" class="btn big" onclick="popProhibitCancel();">취소</button>
				</div>
				<!-- //btn-wrap -->
			</div>

		</div>

		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_prohibit');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
<!-- //팝업 -->
