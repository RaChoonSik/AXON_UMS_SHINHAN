<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.18
	*	설명 : 수신자그룹 등록(파일연동) 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp"%>

<script type="text/javascript"
	src="<c:url value='/js/ems/seg/segFileAddP.js'/>"></script>

<body>
	<div id="wrapper">
		<header class="util">
			<h1 class="logo">
				<a href="/ems/index.ums"><span class="txt-blind">LOGO</span></a>
			</h1>
			<!-- 공통 표시부// -->
			<%@ include file="/WEB-INF/jsp/inc/top.jsp"%>
			<!-- //공통 표시부 -->
		</header>
		<div id="wrap" class="ems">

			<!-- lnb// -->
			<div id="lnb">
				<!-- LEFT MENU -->
				<%@ include file="/WEB-INF/jsp/inc/menu_ems.jsp"%>
				<!-- LEFT MENU -->
			</div>
			<!-- //lnb -->
			<div class="content-wrap">
				<!-- content// -->
				<div id="content" class="single-style">

					<!-- cont-head// -->
					<section class="cont-head">
						<div class="title">
							<h2>수신자그룹 신규등록</h2>
						</div>

					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">

						<form id="segInfoForm" name="segInfoForm" method="post">
							<input type="hidden" id="page" name="page" value="1"> <input
								type="hidden" id="segFlPath" name="segFlPath"> <input
								type="hidden" id="mergeKey" name="mergeKey"> <input
								type="hidden" id="mergeCol" name="mergeCol"> <input
								type="hidden" id="createTy" name="createTy" value="003">
							<input type="hidden" id="serviceGb" name="serviceGb" value="10">
							<fieldset>
								<legend>수신자그룹 신규등록</legend>

								<!-- tab// -->
								<div class="tab">
									<div class="tab-menu col2">
										<a href="javascript:goCreateTy('002');">직접SQL</a>
										<!-- <a href="javascript:goCreateTy('004');">리타게팅</a> -->
										<a href="javascript:goCreateTy('003');" class="active">파일연동</a>
										<!-- <a href="javascript:goCreateTy('000');">추출조건</a> -->
									</div>
									<div class="tab-cont">

										<!-- tab-cont : 파일연동// -->
										<div class="active">
											<!-- 조건// -->
											<div class="graybox">
												<div class="title-area">
													<h3 class="h3-title">조건</h3>
													<span class="required">*필수입력 항목</span>
												</div>

												<div class="list-area">
													<ul>
														<li><label class="required">파일등록</label>
															<div class="list-item">
																<input type="text" id="tempFlPath" name="tempFlPath"
																	placeholder="파일 등록"
																	style="width: calc(100% - 13.4rem);" readonly>
																<button type="button" class="btn fullblue"
																	onclick="fn.popupOpen('#popup_file_seg');">등록</button>
																<button type="button" class="btn"
																	onclick="downloadSample();">샘플</button>
															</div></li>
														<li><label class="required">구분자</label>
															<div class="list-item">
																<input type="text" id="separatorChar"
																	name="separatorChar" placeholder="구분자를 입력해주세요."
																	style="width: calc(100% - 6.7rem);" maxlength="5">
																<button type="button" class="btn fullblue"
																	onclick="fncSep('<c:out value='${NEO_USER_ID}'/>');">등록</button>
															</div></li>
														<%-- 관리자의 경우 전체 요청부서를 전시하고 그 외의 경우에는 숨김 --%>
														<c:if test="${'Y' eq NEO_ADMIN_YN}">
															<li><label class="required">사용자그룹</label>
																<div class="list-item">
																	<div class="select">
																		<select id="deptNo" name="deptNo"
																			onchange="getUserList(this.value);" title="사용자그룹 선택">
																			<option value="0">선택</option>
																			<c:if test="${fn:length(deptList) > 0}">
																				<c:forEach items="${deptList}" var="dept">
																					<option value="<c:out value='${dept.deptNo}'/>"><c:out
																							value='${dept.deptNm}' /></option>
																				</c:forEach>
																			</c:if>
																		</select>
																	</div>
																</div></li>
															<li><label class="required">사용자명</label>
																<div class="list-item">
																	<div class="select">
																		<select id="userId" name="userId" title="사용자 선택">
																			<option value="">선택</option>
																		</select>
																	</div>
																</div></li>
														</c:if>
														<li class="col-full"><label class="required">수신자그룹</label>
															<div class="list-item">
																<input type="text" id="segNm" name="segNm"
																	placeholder="수신자그룹 명칭을 입력해주세요.">
															</div></li>
														<li class="col-full"><label>설명</label>
															<div class="list-item">
																<textarea id="segDesc" name="segDesc"
																	placeholder="수신자그룹 설명을 입력해주세요."></textarea>
															</div></li>
														<li class="col-full"><label>수신그룹<br>미리보기
														</label>
															<div class="list-item">
																<p class="inline-txt color-gray" id="txtTotCnt">0명</p>
																<input type="hidden" id="totCnt" name="totCnt" value="0" />
																<button type="button" class="btn fullblue"
																	onclick="goSegInfo('<c:out value='${NEO_USER_ID}'/>');">미리보기</button>
															</div></li>
													</ul>
													<!-- btn-wrap// -->
													<div class="btn-wrap btn-biggest">
														<button type="button" class="btn big fullblue"
															onclick="goSegFileAdd();">등록</button>
														<button type="button" class="btn big"
															onclick="goCancel();">취소</button>
													</div>
													<!-- //btn-wrap -->
												</div>
											</div>
											<!-- //조건 -->

										</div>
										<!-- //tab-cont : 파일연동 -->

									</div>
								</div>
								<!-- //tab -->

							</fieldset>
						</form>

					</section>
					<!-- //cont-body -->

				</div>
				<!-- // content -->
			</div>
		</div>
	</div>

	<!-- 신규발송팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_new_mail.jsp"%>
	<!-- //신규발송팝업 -->

	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_seg.jsp"%>
	<!-- //파일등록 팝업 -->

	<!-- 미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg.jsp"%>
	<!-- //미리보기 팝업 -->

	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp"%>
	<!-- //조회사유팝업 -->

	<iframe id="iFrmDown" name="iFrmDown" style="width: 0px; height: 0px;"></iframe>
</body>
</html>
