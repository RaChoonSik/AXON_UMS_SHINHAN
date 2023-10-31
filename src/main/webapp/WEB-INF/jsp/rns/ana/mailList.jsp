<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22
	*	설명 : 자동메일 메일별 발송결과 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp"%>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld"%>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<div class="title-area-left">
			<!-- <h3 class="h3-title">목록</h3> -->
			<span class="total">Total: <em><c:out value="${pageUtil.totalRow}" /></em></span>
			<div class="btn-wrap select03">

				<div class="btn-wrap">
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
		</div>
		<!-- <button type="button" class="btn fullpurple" onclick="goExcel();">엑셀다운로드</button> -->
	</div>

	<div class="grid-area pb20">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width: 10%;">
				<col style="width: auto;">
				<col style="width: 12%;">
				<col style="width: 15%;">
				<col style="width: 15%;">
				<col style="width: 10%;">
				<col style="width: 6%;">
				<col style="display: none;">
				<col style="display: none;">
				<col style="display: none;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">메일제목</th>
					<th scope="col">서비스구분</th>
					<th scope="col">등록일시</th>
					<th scope="col">발송일시</th>
					<th scope="col">발송상태</th>
					<th scope="col">재발송</th>
					<th scope="col" style="display: none;">발송자명</th>
					<th scope="col" style="display: none;">발송자이메일주소</th>
					<th scope="col" style="display: none;">발송템플릿</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(mailDataList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${mailDataList}" var="mailData" varStatus="mailDataStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - mailDataStatus.index}" /></td>
							<td><a href="javascript:;" onclick="goMailDetil('<c:out value="${mailData.mid}"/>', '<c:out value="${mailData.deptNm}"/>', '<c:out value="${mailData.attchCnt}"/>', this);" class="bold"><c:out value="${mailData.subject}" /></a></td>
							<td><c:out value="${mailData.tnm}" /></td>
							<td><c:out value="${mailData.cdate}" /></td>
							<td><c:out value="${mailData.sdate}" /></td>
							<td><c:out value="${mailData.status}" /></td>
							<c:if test="${mailData.subid eq 0}">
								<td>본발송</td>
							</c:if>
							<c:if test="${mailData.subid ne 0}">
								<td><c:out value="${mailData.subid}" />차</td>
							</c:if>
							<td style="display: none;"><c:out value="${mailData.sname}" /></td>
							<%-- <td style="display:none;"><c:out value="${mailData.smail}"/></td> --%>
							<td style="display: none;"><crypto:decrypt colNm='SMAIL' data='${mailData.smail}' /></td>
							<td style="display: none;"><c:out value="${mailData.contents}" /></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty mailDataList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="7" class="no_data">등록된 내용이 없습니다.</td>
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
