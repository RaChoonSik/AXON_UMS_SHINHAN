<%--
	작성자 : 손장호

--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp"%>

<script type="text/javascript" src="<c:url value='/js/sys/aut/funcAuthListP.js'/>"></script>

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
							<h2>기능권한 관리</h2>
						</div>

					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">
						<form id="funcAuthForm" name="funcAuthForm" method="post">
							<input hidden="hidden" id="funcCd" name="funcCd" value=""> <input hidden="hidden" id="deptNos" name="deptNos" value=""> <input hidden="hidden" id="userIds" name="userIds" value=""> <input hidden="hidden" id="pagePerm" name="pagePerm" value="0"> <input hidden="hidden" id="pageGrpPerm" name="pageGrpPerm" value="0">
							<fieldset>
								<legend>조회 및 기능권한</legend>

								<!-- 조회// -->
								<div class="graybox">
									<!-- <div class="title-area">
										<h3 class="h3-title">조회</h3>
									</div> -->

									<div class="list-area">
										<ul>
											<li><label class="required">기능 목록</label>
												<div class="list-item">
													<div class="select" style="width: calc(100% - 6.7rem);">

														<select id="cdNo" name="cdNo" title="옵션 선택" onchange="getSelectedValue();">
															<c:if test="${fn:length(funcCodeList) > 0}">
																<c:forEach items="${funcCodeList}" var="dept">
																	<option value="<c:out value='${dept.cd}'/>"><c:out value='${dept.cdNm}' /></option>
																</c:forEach>
															</c:if>
														</select>
													</div>
													<button type="button" class="btn fullred" onclick="goMailTypeSearch()">검색</button>
												</div></li>
										</ul>
									</div>
								</div>
								<!-- //조회 -->

								<!-- 기능권한// -->
								<div class="graybox clear editpw active">
									<div class="title-area clear">
										<h3 class="h3-title">
											사용자 권한 매핑 <span id="showAdd"></span>
										</h3>

										<button type="button">
											<span class="hidden">여닫기버튼</span>
										</button>
									</div>

									<!-- 사용자 권한 매핑// -->
									<div class="menuauthority-wrap clear" style="display: block;">
										<!-- 조직도// -->
										<div class="graybox" style="width: 30%;">
											<div class="title-area">
												<h3 class="h3-title">조직도</h3>
											</div>

											<div class="content-area" >
												<h4>그룹목록</h4>
												<ul class="toggle">
													<c:if test="${fn:length(orgList) > 0}">
														<c:forEach items="${orgList}" var="org">
															<li>
																<button type="button" class="btn-toggle" onclick="getAuthOrgListView('<c:out value='${org.orgCd}'/>','<c:out value='${org.orgNm}'/>');">
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
												<h3 class="h3-title">사용자 검색</h3>
											</div>

											<div class="content-area">
												<div class="item">
													<!-- 2021-08-17 클래스 수정 -->
													<input type="text" id="userNm" placeholder="관리자" style="width: calc(100% - 6.7rem);">
													<button type="button" class="btn fullred" onclick="getFuncUserList(this.value)">검색</button>
												</div>
												<ul id="userList" class="user" style="height: 430px;margin-top:10px; overflow-y: auto;">
												</ul>
											</div>
										</div>
										<!-- //사용자 검색 -->

										<!-- btn-area// -->
										<div class="btn-area">
											<button type="button" class="btn-remove" onclick="goRemove()">
												<span class="hide">삭제</span>
											</button>
											<button type="button" class="btn-add" onclick="goAdd()">
												<span class="hide">추가</span>
											</button>
										</div>
										<!-- //btn-area -->

										<!-- 권한 매핑 사용자// -->
										<div class="graybox fr" style="width: 30%; margin-right: 0;">
											<div class="title-area">
												<h3 class="h3-title">권한 매핑 사용자</h3>
											</div>
											<div class="content-area outcome" style="height: 430px; overflow: auto;">
												<ul id="funcPermAuthList" class="user">
													<!-- 권한 매핑 사용자 동적 처리 -->
												</ul>
											</div>
										</div>
										<!-- //권한 매핑 사용자 -->
									</div>
									<!-- //사용자 권한 매핑 -->
								</div>
								<!-- //기능권한 -->

								<!-- 그룹권한// -->
								<div class="graybox clear editpw active">
									<div class="title-area clear">
										<h3 class="h3-title">그룹 권한 매핑</h3>
										<button type="button">
											<span class="hidden">여닫기버튼</span>
										</button>
									</div>

									<!-- 그룹 권한 매핑// -->
									<div class="menuauthority-wrap clear" style="display: block; padding: 20px;">

										<!-- 사용자 그룹// -->
										<div class="graybox" style="width: 45%; height: 380px;">
											<div class="title-area">
												<h3 class="h3-title">사용자 그룹</h3>
											</div>

											<div class="content-area">
												<div class="item">
													<!-- 2021-08-17 클래스 수정 -->
													<input type="text" id="deptGPNm" placeholder="그룹명 검색" style="width: calc(100% - 6.7rem);">
													<button type="button" class="btn fullred" onclick="getAuthGrpDeptList('')">검색</button>
												</div>
												<ul class="user" style="height: 320px; overflow: auto;" id="deptGPList">
													<c:if test="${fn:length(deptGpList) > 0}">
														<c:forEach items="${deptGpList}" var="deptGP">
															<li id="ld_<c:out value='${deptGP.deptNo}'/>"><label class="clear"> <span class="fl"><strong><c:out value="${deptGP.deptNm}" /></strong></span> <span class="fr"> <input type="checkbox" name="chkGPDept" id="cd_<c:out value='${deptGP.deptNo}'/>" value="<c:out value='${deptGP.deptNo}'/>"><span></span>
																</span>
															</label></li>
														</c:forEach>
													</c:if>
												</ul>
											</div>
										</div>
										<!-- //사용자 그룹 -->

										<!-- btn-area// -->
										<div class="btn-area" style="right: 48.8%;">
											<button type="button" class="btn-remove" onclick="goRemoveDept()">
												<span class="hide">삭제</span>
											</button>
											<button type="button" class="btn-add" onclick="goAddDept()">
												<span class="hide">추가</span>
											</button>
										</div>
										<!-- //btn-area -->

										<!-- 권한 매핑 사용자// -->
										<div class="graybox fr" style="width: 45%; height: 380px;">
											<div class="title-area">
												<h3 class="h3-title">권한 매핑 그룹</h3>
											</div>
											<div class="popsendenroll content-area outcome" style="height: 320px; overflow: auto;">
												<ul class="user" id="funcGrpPermAuthList">
													<!-- 권한 매핑 그룹 동적 처리 -->
												</ul>
											</div>
										</div>
										<!-- //권한 매핑 사용자 -->
									</div>
									<!-- //그룹 권한 매핑 -->
								</div>
								<!-- //그룹권한 -->

								<!-- btn-wrap// -->
								<div class="btn-wrap">
									<button type="button" class="btn big fullred" onclick="goFuncAuthUpdate()">저장</button>
								</div>
								<!-- //btn-wrap -->

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