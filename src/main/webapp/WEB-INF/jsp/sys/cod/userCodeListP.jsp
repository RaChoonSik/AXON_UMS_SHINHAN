<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.05
	*	설명 : 코드목록 관리 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp"%>

<script type="text/javascript" src="<c:url value='/js/sys/cod/userCodeListP.js'/>"></script>

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
							<h2>공통코드 관리</h2>
						</div>

					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">

						<form id="searchForm" name="searchForm" method="post">
							<input type="hidden" id="page" name="page" value="<c:out value='${searchVO.page}'/>"> <input type="hidden" id="rows" name="rows" value="<c:out value='${searchVO.rows}'/>"> <input type="hidden" id="cdGrp" name="cdGrp" value=""> <input type="hidden" id="cd" name="cd" value="">
							<fieldset>
								<legend>조회 및 목록</legend>

								<!-- 조회// -->
								<div class="graybox">
									<!-- <div class="title-area">
								<h3 class="h3-title">조회</h3>
							</div> -->

									<div class="list-area">
										<ul>
											<li class="col-full"><label>분류코드</label>
												<div class="list-item">
													<div class="select">
														<select id="searchCdGrp" name="searchCdGrp" title="분류코드 선택">
															<option value="0">필수 선택</option>
															<c:if test="${fn:length(cdGrpList) > 0}">
																<c:forEach items="${cdGrpList}" var="cdGrp">
																	<option value="<c:out value='${cdGrp.cdGrp}'/>" <c:if test="${cdGrp.cdGrp eq searchVO.searchCdGrp}"> selected</c:if>>
																		<c:choose>
																			<c:when test="${cdGrp.sysYn eq 'Y'}">
																		[<c:out value='${cdGrp.cdGrp}' />]<c:out value='${cdGrp.cdGrpNm}' /> - 수정불가
																	</c:when>
																			<c:otherwise>
																		[<c:out value='${cdGrp.cdGrp}' />]<c:out value='${cdGrp.cdGrpNm}' />
																			</c:otherwise>
																		</c:choose>
																	</option>
																</c:forEach>
															</c:if>
														</select>
													</div>
												</div></li>
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


								<!-- 목록&페이징// -->
								<div id="divUserCodeList"></div>
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

