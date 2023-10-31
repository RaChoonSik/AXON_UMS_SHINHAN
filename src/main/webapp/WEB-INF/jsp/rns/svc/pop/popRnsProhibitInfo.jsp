<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.02.15
	*	설명 :RNS 준법심의 결과 내부 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="cont">
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
						<th scope="col">이미지 포함</th>
						<td id="popRnsProhibitImage" colspan="3"><c:out value="${imgDesc}"/></td>
					</tr>
					<tr>
						<th scope="col">첨부 파일</th>
						<td  id="popRnsProhibitAttach" colspan="3"><c:out value="${attachDesc}"/></td>
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
				<h3 class="h3-title"><c:out value="${titleCntInfo}"/></h3>
			</div>

			<div class="grid-area">
				<table class="grid">
					<caption>그리드 정보</caption>
					<tbody>
						<c:if test="${fn:length(prohibitTitleList) > 0}">
							<c:forEach items="${prohibitTitleList}" var="prohibitTitle">
								<tr>
									<td><c:out value="${prohibitTitle}"/></td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
		<!-- //제목 -->

		<!-- 본문// -->
		<div class="graybox">
			<div class="title-area">
				<h3 class="h3-title"><c:out value="${textCntInfo}"/></h3>
			</div>

			<div class="grid-area">
				<table class="grid">
					<caption>그리드 정보</caption>
					<tbody>
					<tbody>
						<c:if test="${fn:length(prohibitTextList) > 0}">
							<c:forEach items="${prohibitTextList}" var="prohibitText">
								<tr>
									<td><c:out value="${prohibitText}"/></td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
		<!-- //본문 -->
	</div>
	<!-- //금지어 -->

	<!-- btn-wrap// -->
	<div class="btn-wrap">
		<button type="button" class="btn big" onclick="fn.popupClose('#popup_rns_prohibit_info');">닫기</button>
	</div>
	<!-- //btn-wrap -->
</div>

		 
<!-- //팝업 -->
