<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.30
	*	설명 : 결재 반려사유코드 등록
	*	수정자 : 박찬용
	*	수정일시 : 2022.02.1
	*	수정설명 : 디자인 변경 및 입력항목 추가
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<div id="popup_rejct_returnreason" class="poplayer popreturnreason"><!-- id값 수정 가능 -->
	<div class="inner">
		<header>
			<h2>반려 사유 등록</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">

					<div class="table-area">
						<table class="grid">
							<caption>반려 사유 등록</caption>
							<colgroup>
								<col style="width:110px">
								<col style="width:auto">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">결재유형</th>
									<td id="rejectView1">일반 결재 or 준법심의 결재</td>
								</tr>
								<tr>
									<th scope="row">반려사유</th>
									<td>
										<div class="select">
											<select id="rejectCd" name="rejectCd" title="반려사유 선택">
												<option value="">선택</option>
												<c:if test="${fn:length(rejectList) > 0}">
													<c:forEach items="${rejectList}" var="reject">
														<option value="<c:out value='${reject.cd}'/>"><c:out value='${reject.cdNm}'/></option>
													</c:forEach>
												</c:if>
											</select>
										</div>
									</td>
								</tr>
								<tr>
									<th scope="row">기타의견</th>
									<td>
										<textarea placeholder="의견을 등록해주세요." id="rejectCancelDesc" name="rejectCancelDesc"></textarea>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<!-- 버튼// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="goApprStepReject();">반려</button>					
				</div>
				<!-- //버튼 -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_rejct_returnreason');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_rejct_returnreason');"></span>
</div>
<!-- //팝업 -->