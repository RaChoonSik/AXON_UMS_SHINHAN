<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.09.27
	*	설명 : 사용자그룹 권한 목록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp"%>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<!-- <h3 class="h3-title">목록</h3> -->
		<!-- //총 건 -->
		<div class="title-area-left">
			<span class="total">Total: <em><c:out value="${pageUtil.totalRow}" /></em></span>
			<!-- 페이지 정렬// -->
			<div class="btn-wrap select03">
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
			</div>
			<!-- //페이지 정렬 -->
		</div>
		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn" onclick="goDelete();">삭제</button>
		</div>
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width: 5%;">
				<col style="width: 5%;">
				<col style="width: 10%;">
				<col style="width: 10%;">
				<col style="width: 10%;">
				<col style="width: 10%;">
				<col style="width: 10%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col"><label for="userAllChk"><input type="checkbox" id="userAllChk" name="userAllChk" onclick='selectAll(this)'><span></span></label></th>
					<th scope="col">사용자그룹</th>
					<th scope="col">사용자그룹명</th>
					<th scope="col">접근메뉴</th>
					<th scope="col">등록자</th>
					<th scope="col">등록일</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(groupAuthList) > 0}">
					<c:forEach items="${groupAuthList}" var="groupAuth" varStatus="groupAuthStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - groupAuthStatus.index}" /></td>
							<td align="center"><label for="checkbox_<c:out value='${groupAuthStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${groupAuthStatus.count}'/>" name="delDeptNo" value="<c:out value='${groupAuth.deptNo}'/>"><span></span></label></td>
							<td><a href="javascript:goUpdate('<c:out value='${groupAuth.deptNo}'/>')" class="bold"><c:out value='${groupAuth.deptNo}' /></a></td>
							<td><a href="javascript:goUpdate('<c:out value='${groupAuth.deptNo}'/>')" class="bold"><c:out value='${groupAuth.deptNm}' /></a></td>
							<td><a href="javascript:popGroupAuthSearch('<c:out value='${groupAuth.deptNo}'/>')" class="bold"><c:out value='${groupAuth.menuCount}' /></a></td>
							<td><c:out value="${groupAuth.regNm}" /></td>
							<td><c:out value="${groupAuth.regDt}" /></td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty groupAuthList}">
					<tr>
						<td colspan="7" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
				</c:if>
				<!-- //데이터가 없을 경우 -->
			</tbody>
		</table>
	</div>
	<!-- 페이징// -->
	<div class="paging">${pageUtil.pageHtml}</div>
	<!-- //페이징 -->
</div>
<!-- //목록 -->

