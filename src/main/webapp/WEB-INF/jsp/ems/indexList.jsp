<%--
	/**********************************************************
	*	작성자 : 박찬용
	*	작성일시 : 2022.01.27
	*	설명 : EMS 메인 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<!-- 발송일정 페이징// -->
<div class="box bgwhite">
	<h3 class="date">${fn:substring(searchBoardDt,4,6)}월 ${fn:substring(searchBoardDt,6,8)}일 발송일정
		<button type="button" class="btn-prev" onclick="goSearchDate(1,'P','${searchBoardDt}')" ><span class="hide">이전</span></button>
		<button type="button" class="btn-next" onclick="goSearchDate(1,'F','${searchBoardDt}')"><span class="hide">다음</span></button>	
	</h3>
	
	<!-- 목록// -->
	<table class="grid type-border">
		<caption>그리드 정보</caption>
		<colgroup>
			<col style="width:10%;">
			<col style="width:auto;">
			<col style="width:10%;">
			<col style="width:15%;">
			<col style="width:12%;">
			<col style="width:12%;">
			<col style="width:8%;">
			<col style="width:6%;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">발송일시</th>
				<th scope="col">메일명</th>
				<th scope="col">메일유형</th>
				<th scope="col">캠페인명</th>
				<th scope="col">수신자그룹</th>
				<th scope="col">사용자명</th>
				<th scope="col">발송상태</th>
				<th scope="col">준법심의</th>
			</tr>
		</thead>
		<tbody>
			<!-- 데이터가 있을 경우// -->
			<c:if test="${fn:length(dayMailSendSch) > 0}">
			<c:forEach items="${dayMailSendSch}" var="template" varStatus="tempStatus">
			<tr>
				<td>${template.sendHh24mi}</td>
				<td><a href="javascript:goUpdate('${template.taskNo}','${template.subTaskNo}','${template.approvalProcAppYn}','${template.workStatus}');" class="bold">${template.taskNm}</a></td>
				<td>${template.sendModeNm}</td>
				<td>${template.campNm}</td>
				<td>${template.segNm}</td>
				<td>${template.userNm}</td>
				<td>${template.workStatusNm}</td>
				<td>${template.compliancdYn}</td>
			</tr>
			</c:forEach>			
			<!-- //데이터가 있을 경우 -->
			</c:if>
	
			<c:if test="${empty dayMailSendSch}">
			<!-- 데이터가 없을 경우// -->
			<tr>
				<td colspan="8" class="no_data">표시할 정보가 없습니다.</td>
			</tr>
			<!-- //데이터가 없을 경우 -->
			</c:if>
		</tbody>
	</table>
	<!-- //목록 -->

	<!-- 페이징// -->
	<div class="paging">
		${pageUtil.pageHtml}
	</div>
	<!-- //페이징 --> 
</div>
