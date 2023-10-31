<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.09.27
	*	설명 : 그룹 권한 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp"%>

<script type="text/javascript" src="<c:url value='/js/sys/aut/groupAuthListP.js'/>"></script>
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
							<h2>그룹권한 목록</h2>
						</div>


					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">
						<form id="searchForm" name="searchForm" method="post">
							<input type="hidden" id="page" name="page" value="<c:out value='${searchVO.page}'/>">
							<input type="hidden" id="rows" name="rows" value="<c:out value='${searchVO.rows}'/>">
							<input type="hidden" id="userId" name="userId" value="">
							<input type="hidden" id="deptNo" name="deptNo" value="0">
							<fieldset>
								<legend>조회 및 목록</legend>

								<!-- 조회// -->
								<div class="graybox">
									<!-- <div class="title-area">
								<h3 class="h3-title">조회</h3>
							</div> -->

									<div class="list-area">
										<ul>
											<li>
												<label>사용자 그룹명</label>
												<div class="list-item">
													<input type="text" id="searchDeptNm" name="searchDeptNm" placeholder="사용자 그룹명을 입력해주세요.">
												</div>
											</li>
											<li>
												<label>사용자그롭</label>
												<div class="list-item">
													<div class="select">
														<select id="searchDeptNo" name="searchDeptNo" title="옵션 선택">
															<option value="">선택</option>
															<c:if test="${fn:length(deptList) > 0}">
																<c:forEach items="${deptList}" var="dept">
																	<option value="<c:out value='${dept.deptNo}'/>" <c:if test="${dept.deptNo eq searchVO.searchDeptNo}"> selected</c:if>><c:out value='${dept.deptNm}' /></option>
																</c:forEach>
															</c:if>
														</select>
													</div>
												</div>
											</li>
										</ul>
										<!-- btn-wrap// -->
										<div class="btn-wrap">
											<button type="button" class="btn big fullred" onclick="goSearch('1')">검색</button>
											<button type="button" class="btn big" onclick="goReset()">초기화</button>
										</div>
										<!-- //btn-wrap -->
									</div>
								</div>
								<!-- //조회 -->
								<!-- 사용자메뉴검색// -->
								<%@ include file="/WEB-INF/jsp/inc/pop/pop_dept_auth_search.jsp"%>

								<!-- 목록&페이징// -->
								<div id="divGroupAuthList" style="margin-top: 36px;"></div>
								<!-- //목록&페이징 -->

							</fieldset>
						</form>
					</section>
					<!-- //cont-body -->

				</div>
				<!-- // content -->
			</div>
		</div>
	</div>
</body>
</html>
