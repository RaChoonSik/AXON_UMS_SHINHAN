<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.01.11
	*	설명 : 준법심의 결재라인 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp"%>

<script type="text/javascript" src="<c:url value='/js/sys/acc/complianceListP.js'/>"></script>
<body>
	<div id="wrapper" class="sys">
		<header>
			<h1 class="logo">
				<a href="/ems/index.ums"><span class="txt-blind">LOGO</span></a>
			</h1>
			<!-- 공통 표시부// -->
			<%@ include file="/WEB-INF/jsp/inc/top.jsp"%>
			<!-- //공통 표시부 -->
		</header>
		<div id="wrap" class="sys">

			<!-- lnb// -->
			<div id="lnb">
				<!-- LEFT MENU -->
				<%@ include file="/WEB-INF/jsp/inc/menu_sys.jsp"%>
				<!-- LEFT MENU -->
			</div>
			<!-- //lnb -->
			<div class="content-wrap">
				<!-- content// -->
				<div id="content">

					<!-- cont-head// -->
					<section class="cont-head">
						<div class="title">
							<h2>준법심의 결재라인</h2>
						</div>

					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">
						<div class="menuauthority-wrap" style="position: relative;">
							<fieldset>
								<legend>조직도 및 결재라인</legend>

								<div class="clear">
									<!-- 조직도// -->
									<div class="graybox" style="width: 30%;">
										<div class="title-area">
											<h3 class="h3-title">조직도</h3>
										</div>

										<div class="content-area" style="height: 430px; overflow: auto;">
											<h4>그룹목록</h4>
											<ul class="toggle">
												<c:if test="${fn:length(orgList) > 0 }">
													<c:forEach items="${orgList}" var="org">
														<li>
															<button type="button" class="btn-toggle" onclick="getOrgUserList('<c:out value='${org.orgCd}'/>','<c:out value='${org.orgNm}'/>');">
																<c:out value="${org.orgNm}" />
															</button> <c:if test="${org.childCnt > 0}">
																<ul class="depth<c:out value='${org.lvlVal+1}'/>" id="<c:out value='${org.orgCd}'/>"></ul>
															</c:if>
														</li>
													</c:forEach>
												</c:if>
											</ul>
										</div>
									</div>
									<!-- //조직도 -->

									<!-- 사용자 검색// -->
									<div class="graybox" style="width: 30%;">
										<div class="title-area">
											<h3 class="h3-title">사용자</h3>
										</div>

										<div class="content-area" style="height: 430px; overflow: auto;">
											<div class="item">
												<input type="text" id="searchUserNm" name="searchUserNm" placeholder="사용자명 검색" style="width: calc(100% - 6.7rem);">
												<button type="button" class="btn fullred" onclick="searchUser()">검색</button>
											</div>

											<ul id="userList" class="user">
											</ul>
										</div>
									</div>
									<!-- //사용자 -->

									<!-- btn-area// -->
									<div class="btn-area">
										<button type="button" class="btn-add" onclick="complianceAdd()">
											<span class="hide">추가</span>
										</button>
										<button type="button" class="btn-remove" onclick="complianceRemove()">
											<span class="hide">삭제</span>
										</button>
									</div>
									<!-- //btn-area -->

									<!-- 결재라인 정보// -->
									<div class="graybox fr" style="width: 30%;">
										<div class="title-area">
											<h3 id="hTitle" class="h3-title">결재라인 정보</h3>
										</div>

										<div class="content-area">
											<h4>
												결재라인
												<button type="button" class="btn-reset" onclick="complianceReset();">
													<span class="hide">초기화</span>
												</button>
											</h4>

											<div class="grid-area" style="margin-top:0; border-radius:0">
												<table class="grid type-border">
													<caption>그리드 정보</caption>
													<colgroup>
														<col style="width: 35%;">
														<col style="width: 45%;">
														<col style="width: 20%;">
													</colgroup>
													<thead>
														<tr>
															<th scope="col">사용자명</th>
															<th scope="col">조직명</th>
															<th scope="col">직책</th>
														</tr>
													</thead>
													<tbody id="complianceUserList" class="sortable">
														<c:if test="${not empty complianceList}">
															<c:forEach items="${complianceList}" var="comLine">
																<tr ondblclick="deleteComplianceDblClick(this);">
																	<td><input type="hidden" name="complianceUserId" value="<c:out value='${comLine.userId}|${comLine.orgCd}|${comLine.positionGb}|${comLine.jobGb}'/>"> <c:out value="${comLine.userNm}" /></td>
																	<td style="display: none;"><input type="hidden" name="complianceUserNm" value="<c:out value='${comLine.userNm} / ${comLine.orgNm} / ${comLine.jobNm}'/>"></td>
																	<td><c:out value="${comLine.orgNm}" /></td>
																	<td><c:out value="${comLine.jobNm}" /></td>
																</tr>
															</c:forEach>
														</c:if>
													</tbody>
												</table>
											</div>
										</div>
									</div>
									<!-- //결재라인 정보 -->
								</div>

								<!-- btn-wrap// -->
								<div class="btn-wrap">
									<button type="button" class="btn big fullred" onclick="complianceLineSave();">저장</button>
								</div>
								<!-- //btn-wrap -->
							</fieldset>
						</div>

					</section>
					<!-- //cont-body -->

				</div>
				<!-- // content -->
			</div>
		</div>
	</div>
</body>
</html>
