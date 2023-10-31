<%--
	/**********************************************************
	*	작성자 : 박찬용
	*	작성일시 : 2022.01.27
	*	설명 : EMS 메인 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp"%>
<script type="text/javascript" src="<c:url value='/js/ems/indexListP.js'/>"></script>
<script>
	var blockedOther = '<%=(String)session.getAttribute("NEO_BLOCKED_OTEHR")%>';
	if (blockedOther == "Y"){
		var blockedIp =  '<%=(String)session.getAttribute("NEO_USER_IP")%>';
		var message = "다중 접속으로 기 접속 계정은 종료됩니다 기 접속 IP [" + blockedIp +"]";
		alert(message);
	} 
</script>

<body>
	<form name="searchFormMail" id="searchFormMail" method="post">
		<input type="hidden" id="taskNo" name="taskNo" value="">
		<input type="hidden" id="subTaskNo" name="subTaskNo" value="">
		<input type="hidden" id="approvalProcAppYn" name="approvalProcAppYn" value="">
	</form>
	<form name="mailForm" id="mailForm">
		<input type="hidden" id="searchStartDt" name="searchStartDt" value="">
		<input type="hidden" id="searchEndDt" name="searchEndDt" value="">
		<input type="hidden" id="searchDeptNo" name="searchDeptNo" value="">
		<input type="hidden" id="searchUserId" name="searchUserId" value="">
		<input type="hidden" id="searchWorkStatus" name="searchWorkStatus" value="">
	</form>
	<form name="searchForm" id="searchForm">
		<input type="hidden" name="mailAnalyzeTime" id="mailAnalyzeTime" value="${systemTimes}">
		<input type="hidden" name="mailSendTime" id="mailSendTime" value="${systemTimes}">
		<input type="hidden" name="mailAnalyzePfTime" id="mailAnalyzePfTime" value="${systemTimes}">
		<input type="hidden" name="searchBoardDt" id="searchBoardDt" value="${systemTimes}">
		<input type="hidden" id="page" name="page" value="1">
		<input type="hidden" id="tempNo" name="tempNo" value="0">
		<input type="hidden" id="searchBoardPf" name="searchBoardPf" value="1">
	</form>
	<div id="wrapper">
		<header class="util">
			<h1 class="logo">
				<a href="/ems/index.ums"><span class="txt-blind">LOGO</span></a>
			</h1>
			<!-- 공통 표시부// -->
			<%@ include file="/WEB-INF/jsp/inc/top.jsp"%>
			<!-- //공통 표시부 -->
		</header>
		<div id="wrap">
			<!-- lnb// -->
			<div id="lnb">
				<!-- LEFT MENU -->
				<%@ include file="/WEB-INF/jsp/inc/menu_ems.jsp"%>
				<!-- LEFT MENU -->
			</div>
			<!-- //lnb -->
			<div class="content-wrap">
				<!-- content// -->
				<div id="content">

					<!-- cont-head// -->
					<section class="cont-head">
						<!-- <div class="title">
							<h2>홈</h2>
						</div> -->
					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">

						<!-- main// -->
						<div class="main">
							<div class="dashboard">
								<div class="colbox">
									<div class="col">
										<div class="admin bgwhite">
											<h3>${userInfo[0].userNm}</h3>
											<p class="department">${userInfo[0].orgKorNm}|${userInfo[0].jobNm}</p>
											<a href="javascript:goUserUpdate();" class="btn">정보수정</a>

											<%-- <div class="grid-area">
												<table class="grid">
													<caption>그리드 정보</caption>
													<colgroup>
														<col style="width: 33.3%;">
														<col style="width: auto;">
														<col style="width: 33.3%;">
													</colgroup>
													<thead>
														<tr>
															<th scope="col">결재진행</th>
															<th scope="col">결재반려</th>
															<th scope="col">결재완료</th>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td>
																<c:if test="${userApprov[0].cnt202 > 0}">
																	<a href="javascript:gomailAprListP('${userApprov[0].mindate202}','${userApprov[0].maxdate202}','${userInfo[0].userId}','${userInfo[0].deptNo}','202');">${userApprov[0].cnt202}건</a>
																</c:if>
															</td>
															<td>
																<c:if test="${userApprov[0].cnt203 > 0}">
																	<a href="javascript:gomailAprListP('${userApprov[0].mindate2034}','${userApprov[0].maxdate2034}','${userInfo[0].userId}','${userInfo[0].deptNo}','203');">${userApprov[0].cnt203}건</a>
																</c:if>
															</td>
															<td>
																<c:if test="${userApprov[0].cnt204 > 0}">
																	<a href="javascript:gomailAprListP('${userApprov[0].mindate2034}','${userApprov[0].maxdate2034}','${userInfo[0].userId}','${userInfo[0].deptNo}','204');">${userApprov[0].cnt204}건</a>
																</c:if>
															</td>
														</tr>
													</tbody>
												</table>
											</div> --%>
										</div>
										<div class="quickmenu bgwhite" id="mainQuickMenu">
											<h3>Quick Menu</h3>

											<button type="button" class="btn-setting" onclick="fn.popupOpen('#popup_quickmenu');">
												<span class="hide">설정</span>
											</button>
										</div>
									</div>

									<div class="col">
										<div class="date-chart bgwhite">
											<div class="chart-title clear">
												<h3>
													<span id="mailAnalyzeDateView">12월 10일 메일분석</span>
													<button type="button" class="btn-prev" onclick="mailAnalyze('P')">
														<span class="hide">이전</span>
													</button>
													<button type="button" class="btn-next" onclick="mailAnalyze('F')">
														<span class="hide">다음</span>
													</button>
												</h3>
												<div class="fr">
													<span id="mailAnalyzeTimeView">12월 10일 HH:MM</span>
													<button type="button" class="btn-refresh" onclick="mailAnalyze('')">
														<span class="hide">새로고침</span>
													</button>
												</div>
											</div>
											<div class="summary">
												<ul>
													<li><span class="head">총 발송</span> <strong class="data"><em id="dayMailSummaryCntTot"></em>건</strong></li>
													<li><span class="head">발송 성공</span> <strong class="data"><em id="dayMailSummaryCntSucc"></em>건</strong></li>
													<li><span class="head">발송 실패</span> <strong class="data"><em id="dayMailSummaryCntFail"></em>건</strong></li>
													<li><span class="head">열람한 메일</span> <strong class="data"><em id="dayMailOpenOpnTot"></em>건</strong></li>
												</ul>
											</div>

											<!-- tab// -->
											<div class="tab">
												<div class="tab-menu">
													<a href="javascript:;" class="active">도메인별</a> <a href="javascript:;">캠페인별</a> <a href="javascript:;">세부에러</a>
												</div>
												<div class="tab-cont">

													<!-- tab-cont : 도메인별// -->
													<div class="active clear" style="height: 0;">
														<div id="pieChart1" class="piegraph">
															<canvas width="100" height="100"></canvas>
														</div>
														<div class="grid-area">
															<table class="grid type-border">
																<caption>그리드 정보</caption>
																<colgroup>
																	<col style="width: 10%;">
																	<col style="width: auto;">
																	<col style="width: 30%;">
																</colgroup>
																<thead>
																	<tr>
																		<th scope="col">NO</th>
																		<th scope="col">도메인</th>
																		<th scope="col">발송건수</th>
																	</tr>
																</thead>
																<tbody id="dayMailDomainHtml">
																	<tr>
																		<td></td>
																		<td></td>
																		<td>건</td>
																	</tr>
																</tbody>
															</table>
														</div>
													</div>
													<!-- //tab-cont : 도메인별 -->

													<!-- tab-cont : 캠페인별// -->
													<div class="clear" style="height: 0;">
														<div id="pieChart2" class="piegraph">
															<canvas width="100" height="100"></canvas>
														</div>
														<div class="grid-area">
															<table class="grid type-border">
																<caption>그리드 정보</caption>
																<colgroup>
																	<col style="width: 20%;">
																	<col style="width: auto;">
																	<col style="width: 20%;">
																</colgroup>
																<thead>
																	<tr>
																		<th scope="col">NO</th>
																		<th scope="col">캠페인명</th>
																		<th scope="col">발송건수</th>
																	</tr>
																</thead>
																<tbody id="dayMailCampainHtml">
																	<tr>
																		<td></td>
																		<td></td>
																		<td>건</td>
																	</tr>
																</tbody>
															</table>
														</div>
													</div>
													<!-- //tab-cont : 캠페인별 -->

													<!-- tab-cont : 세부에러// -->
													<div class="clear" style="height: 0;">
														<div id="pieChart3" class="piegraph">
															<canvas width="100" height="100"></canvas>
														</div>
														<div class="grid-area">
															<table class="grid type-border">
																<caption>그리드 정보</caption>
																<colgroup>
																	<col style="width: 20%;">
																	<col style="width: auto;">
																	<col style="width: 20%;">
																</colgroup>
																<thead>
																	<tr>
																		<th scope="col">NO</th>
																		<th scope="col">세부에러</th>
																		<th scope="col">발송건수</th>
																	</tr>
																</thead>
																<tbody id="dayMailDetailErrHtml">
																	<tr>
																		<td></td>
																		<td></td>
																		<td>건</td>
																	</tr>
																</tbody>
															</table>
														</div>

													</div>
													<!-- //tab-cont : 세부에러 -->
												</div>
											</div>
											<!-- //tab -->
										</div>
									</div>
								</div>

								<!-- 기간별// -->
								<div class="daily-chart bgwhite">
									<div class="chart-title clear">
										<h3>이메일 발송 추이</h3>
										<div class="fr">
											<span id="mailSendTimeView">12월 10일 HH:MM</span>
											<button type="button" class="btn-refresh" onclick="mailSendAnalyzeDay('');mailSendAnalyzeMon('')">
												<span class="hide">새로고침</span>
											</button>
										</div>
									</div>

									<!-- tab// -->
									<div class="tab type-visible">
										<div class="tab-menu">
											<a href="javascript:;" class="active">시간대</a> <a href="javascript:;">일별</a>
										</div>
										<div class="tab-cont">
											<!-- 일간 그래프// -->
											<div class="active">
												<!-- date-picker// -->
												<div class="calendar-date">
													<button type="button" class="btn-prev" onclick="mailSendAnalyzeDay('P')">이전</button>
													<div class="datepicker">
														<input type="text" id="mailSendcal" name="mailSendcal" readonly>
													</div>
													<button type="button" class="btn-next" onclick="mailSendAnalyzeDay('F')">이후</button>
													<button type="button" class="btn-calendar" id="calDay">달력</button>
												</div>
												<!-- //date-picker -->
												<div class="barchartwrap">
													<div id="barChart" style="height: 270px;"></div>
												</div>
											</div>
											<!-- //일간 그래프 -->

											<!-- 월간 그래프// -->
											<div>
												<!-- calendar-date// -->
												<div class="calendar-date">
													<button type="button" class="btn-prev" onclick="mailSendAnalyzeMon('P')">이전</button>
													<input type="text" id="monthPicker" name="monthPicker" class="input-date" dateformat="" readonly />
													<!-- <input type="button" id="btn_monthPicker" name="btn_monthPicker" value="클릭" /> -->
													<button type="button" class="btn-next" onclick="mailSendAnalyzeMon('F')">이후</button>
													<button type="button" class="btn-calendar">달력</button>
												</div>
												<!-- //calendar-date -->
												<div class="barchartwrap">
													<div id="barChart2" style="height: 270px;"></div>
												</div>
											</div>
											<!-- //월간 그래프 -->

										</div>
									</div>
									<!-- //tab -->
								</div>
								<!-- //기간별 -->

							</div>
							<!-- //대시보드 -->

							<!-- 발송일정 페이징// -->
							<div class="gridboard" id="tempBoard"></div>
							<!-- //발송일정 페이징 -->
						</div>
						<!-- //main -->
					</section>
					<!-- //cont-body -->

				</div>
				<!-- // content -->
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/inc/pop/pop_quickmenu.jsp"%>
</body>
</html>
