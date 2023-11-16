<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.09.26
	*	설명 : API 템플릿 등록현황 목록 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp"%>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<div class="title-area-left">
			<span class="total">Total: <em><c:out value="${pageUtil.totalRow}" /></em>건</span>
			<div class="btn-wrap select03">
				<!-- 페이지 정렬// -->
				<div class="select">
					<select onchange="goChagePerPageRows(this.value);" title="페이지당 결과">
						<c:if test="${fn:length(perPageList) > 0}">
							<c:forEach items="${perPageList}" var="perPage">
								<option value="<c:out value='${perPage.cdNm}'/>" <c:if test="${perPage.cdNm eq searchVO.rows}"> selected</c:if>><c:out value='${perPage.cdNm}' />개씩 보기
								</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<!-- //페이지 정렬 -->
			</div>
		</div>
		<!-- <h3 class="h3-title">목록</h3> -->

		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn fullblue plus" onclick="goServiceAdd();">신규등록</button>
			<button type="button" class="btn" onclick="goCopy();">복사</button>
			<button type="button" class="btn" onclick="goDisable();">사용중지</button>
			<button type="button" class="btn" onclick="goDelete();">삭제</button>
		</div>
		<!-- //버튼 -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:10%;">
				<col style="width:5%;">
				<col style="width:23%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:13%;">
				<col style="width:8%;">
				<col style="width:10%;">
				<!-- <col style="width:8%;"> -->
				<!-- <col style="width:8%;"> -->
				<!-- <col style="width:10%;"> -->
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">No</th>
					<th scope="col">
						<label><input type="checkbox" name="isAll" onclick="goAll();"><span></span></label>
					</th>
					<th scope="col">API템플릿번호</th>
					<th scope="col">템플릿명</th>
					<th scope="col">사용자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">캠페인</th>
					<th scope="col">상태</th>
					<th scope="col">등록일시</th>
					<!-- <th scope="col">발송상태</th> -->
					<!-- <th scope="col">금칙어</th> -->
					<!-- <th scope="col">연계템플릿번호</th> -->
					<th scope="col">수신확인</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(campTempList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${campTempList}" var="campTemp" varStatus="campTempStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - campTempStatus.index}"/></td>
							<td>
								<c:choose>
									<c:when test="${'002' eq campTemp.status}">
										<label><input type="checkbox" name="tids" value="<c:out value='${campTemp.tid}'/>" onclick="return goDeleteClick();" disabled><span></span></label>
									</c:when>
									<c:otherwise>
										<label><input type="checkbox" name="tids" value="<c:out value='${campTemp.tid}'/>" onclick="goTid('<c:out value='${serviceStatus.index}'/>')"><span></span></label>
									</c:otherwise>
								</c:choose>
								<input type="checkbox" name="status" value="<c:out value="${campTemp.status}"/>" style="display:none;">
								<input type="checkbox" name="workStatus" value="000" style="display:none;">
								<input type="checkbox" name="apprProcYn" value="<c:out value='${campTemp.approvalProcYn}'/>" style="display:none;">
							</td>
							<td><c:out value="${campTemp.tid}"/></td>
							<td>
								<c:choose>
									<c:when test="${'000' eq campTemp.status}">
										<a href="javascript:goUpdate('<c:out value='${campTemp.tid}'/>');" class="bold"><c:out value="${campTemp.tnm}"/></a>
									</c:when>
									<c:when test="${'000' ne campTemp.status}">
	 									<a href="javascript:goUpdatePart('<c:out value='${campTemp.tid}'/>','N');" class="bold"><c:out value="${campTemp.tnm}"/></a>
									</c:when>
								</c:choose>
							</td>
							<td><c:out value="${campTemp.deptNm}"/></td>
							<td><c:out value="${campTemp.userNm}"/></td>
							<td><c:out value="${campTemp.campNm}"/></td>
							<td><c:out value="${campTemp.statusNm}"/></td>
							<td>
								<fmt:parseDate var="regDate" value="${campTemp.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy.MM.dd HH:mm"/> 
								<c:out value="${regDt}"/>
							</td>
							<!-- 
							<td>
								<c:choose>
									<%-- 결재 완료--%>
									<c:when test="${'204' eq campTemp.workStatus}">
										<a href="javascript:popRnsApprovalState('<c:out value='${campTemp.tid}'/>');" class="medium">
										<c:out value="${campTemp.workStatusNm}"/></a>
									</c:when>
									<%-- 발송승인/발송중/발송완료/결재진행/결재반려 --%>
									<c:when test="${'001' eq campTemp.workStatus || '002' eq campTemp.workStatus || '003' eq campTemp.workStatus || '202' eq campTemp.workStatus || '203' eq campTemp.workStatus}">
										<c:if test="${'000' eq campTemp.status}">
											<a href="javascript:popRnsApprovalState('<c:out value='${campTemp.tid}'/>');" class="medium">
											<c:out value="${campTemp.workStatusNm}"/></a>
										</c:if>
										<c:if test="${'000' ne campTemp.status}">
											<c:out value="${campTemp.workStatusNm}"/>
										</c:if>
									</c:when>
									<%-- 결재대기 --%>
									<c:when test="${'201' eq campTemp.workStatus}">
										<c:if test="${'000' eq campTemp.status}">
											<a href="javascript:popSubmitApproval('<c:out value='${campTemp.tid}'/>','<c:out value='${campTemp.userId}'/>');" class="medium"><c:out value="${campTemp.workStatusNm}"/></a>
										</c:if>
										<c:if test="${'000' ne campTemp.status}">
											<c:out value="${campTemp.workStatusNm}"/>
										</c:if>
									</c:when>
									<%-- 발송실패 --%>
									<c:otherwise>
										<%-- <a href="javascript:goFail('<c:out value='${service.workStatusDtl}'/>');" class="medium"><c:out value="${service.workStatusNm}"/></a> --%>
										<c:out value="${campTemp.workStatusNm}"/>
									</c:otherwise>
								</c:choose>
							</td>
							 -->
							 <!-- 
							<td>
								<c:if test="${'002' eq campTemp.prohibitChkTyp}">
									<a href="javascript:goPopCampTempProbibitInfo('<c:out value='${campTemp.tid}'/>');" class="bold">해당</a>
								</c:if>
							</td>
							 -->
							<%-- <td><c:out value="${campTemp.eaiCampNo}"/></td> --%>
							<td><c:out value="${campTemp.recvChkYn}"/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>
				<c:if test="${empty campTempList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="10" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
		</table>
	</div>
	<!-- 페이징// -->
	<div class="paging">${pageUtil.pageHtml}</div>
	<!-- //페이징 -->
</div>
<!-- //목록 -->
