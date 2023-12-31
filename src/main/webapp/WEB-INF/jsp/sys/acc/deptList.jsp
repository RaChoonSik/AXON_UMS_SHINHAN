<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.18
	*	설명 : 사용자그룹 목록 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp"%>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<div class="title-area-left">
			<!-- //총 건 -->
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
				<!-- //페이지 정렬 -->
			</div>
		</div>
		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn fullred plus" onclick="goAdd();">신규등록</button>
			<button type="button" class="btn" onclick="goDelete();">삭제</button>
		</div>
	</div>
	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width: 5%;">
				<col style="width: 5%;">
				<col style="width: 20%;">
				<col style="width: 20%;">
				<col style="width: 20%;">
				<col style="width: 15%;">
				<col style="width: 15%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col"><label for="deptAllChk"><input type="checkbox" id="deptAllChk" name="deptAllChk" onclick='selectAll(this)'><span></span></label></th>
					<th scope="col">사용자그룹</th>
					<th scope="col">사용자그룹상태</th>
					<th scope="col">대시보드 전사 데이터</th>
					<th scope="col">등록자</th>
					<th scope="col">등록일</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(deptList) > 0}">
					<c:forEach items="${deptList}" var="dept" varStatus="deptStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - deptStatus.index}" /></td>
							<td align="center"><label for="checkbox_<c:out value='${deptStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${deptStatus.count}'/>" name="delDeptNo" value="<c:out value='${dept.deptNo}'/>"><span></span></label></td>
							<td><a href="javascript:goUpdate('<c:out value='${dept.deptNo}'/>')" class="bold"><c:out value='${dept.deptNm}' /></a></td>
							<td><c:out value="${dept.statusNm}" /></td>
							<td><c:out value="${dept.dataAllYn}" /></td>
							<td><c:out value="${dept.regNm}" /></td>
							<td><c:out value="${dept.regDt}" /></td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty deptList}">
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
