<%--
	/**********************************************************
	*	작성자 : 박찬용
	*	작성일시 : 2022.02.04
	*	설명 : 퀵메뉴 팝업
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<form name="insertQuickMenuForm" id="insertQuickMenuForm">
<div id="popup_quickmenu" class="poplayer popquickmenu"><!-- id값 수정 가능 -->
	<div class="inner" style="max-width:454px;">
		<header>
			<h2>Quick Menu 설정</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<!--Quick Menu// -->
				<h3>Quick Menu
					<button type="button" class="btn-refresh" id="quickMenuRefresh"><span class="hide">초기화</span></button>
				</h3>
				<div class="quickbtn" id="quickbtn">
					<span class="btn">실시간메일 통계분석
						<button type="button" class="btn-del"><span class="hide">삭제</span></button>
					</span>
					<span class="btn">데이터베이스관리
						<button type="button" class="btn-del"><span class="hide">삭제</span></button>
					</span>
					<span class="btn">통계분석
						<button type="button" class="btn-del"><span class="hide">삭제</span></button>
					</span>
					<span class="btn">실시간 서비스 등록
						<button type="button" class="btn-del"><span class="hide">삭제</span></button>
					</span>
					<span class="btn">계정관리
						<button type="button" class="btn-del"><span class="hide">삭제</span></button>
					</span>
				</div>
				<!-- //Quick Menu -->

				<!--Menu 목록// -->
				<h3>Menu 목록</h3>
				<div class="content-area">
					<ul class="toggle-check">
						<li>
							<ul class="toggle-unfold" id="userMenuView">
								<!-- ems// -->
								<li class="depth1">
									<ul><!-- class="col-box" 삭제 : ul에 적용된 col-box를 li에 적용합니다. -->
										<li class="col-box"><!-- class="col-box" 추가 -->
											<span class="col service">
												<button type="button" class="btn-toggle">EMS</button>
											</span>
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
										</li>
									</ul>

									<!-- 2depth// -->
									<ul class="depth2">
										<!-- 발송현황// -->
										<li>
											<ul><!-- class="col-box" 삭제 -->
												<li class="col-box"><!-- class="col-box" 추가 -->
													<span class="col menu">
														<button type="button" class="btn-toggle">발송현황</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>

											<!-- 3depth// -->
											<ul class="depth3">
												<li class="col-box">
													<span class="col menu">
														<button type="button" class="last">주간현황</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button type="button" class="last">월간현황</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>
											<!-- //3depth -->
										</li>
										<!-- //발송현황 -->

										<!-- 메일발송// -->
										<li>
											<ul><!-- class="col-box" 삭제 -->
												<li class="col-box"><!-- class="col-box" 추가 -->
													<span class="col menu">
														<button type="button" class="btn-toggle">메일발송</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>

											<!-- 3depth// -->
											<ul class="depth3">
												<li class="col-box">
													<span class="col menu">
														<button class="last">단기메일</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last">정기메일</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>
											<!-- //3depth -->
										</li>
										<!-- //메일발송 -->

										<!-- 메일관리// -->
										<li>
											<ul><!-- class="col-box" 삭제 -->
												<li class="col-box"><!-- class="col-box" 추가 -->
													<span class="col menu">
														<button type="button" class="btn-toggle">메일관리</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>

											<!-- 3depth// -->
											<ul class="depth3">
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="캠페인관리">캠페인관리</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="수신자그룹관리">수신자그룹관리</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="템플릿관리">템플릿관리</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="메일발송결재">메일발송결재</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>
											<!-- //3depth -->
										</li>
										<!-- //메일관리 -->

										<!-- 통계분석// -->
										<li>
											<ul><!-- class="col-box" 삭제 -->
												<li class="col-box"><!-- class="col-box" 추가 -->
													<span class="col menu">
														<button type="button" class="btn-toggle">통계분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>

											<!-- 3depth// -->
											<ul class="depth3">
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="단기메일분석">단기메일분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="정기메일분석">정기메일분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="캠페인별분석">캠페인별분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="기간별누적분석">기간별누적분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last">상세로그</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>
											<!-- //3depth -->
										</li>
										<!-- //통계분석 -->

									</ul>
									<!-- //2depth -->
								</li>
								<!-- //ems -->

								<!-- RNS// -->
								<li class="depth1">
									<ul><!-- class="col-box" 삭제 -->
										<li class="col-box"><!-- class="col-box" 추가 -->
											<span class="col service">
												<button type="button" class="btn-toggle">RNS</button>
											</span>
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
										</li>
									</ul>

									<!-- 2depth// -->
									<ul class="depth2">
										<!-- 자동메일관리// -->
										<li>
											<ul><!-- class="col-box" 삭제 -->
												<li class="col-box"><!-- class="col-box" 추가 -->
													<span class="col menu">
														<button type="button" class="btn-toggle" title="자동메일관리">자동메일관리</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>

											<!-- 3depth// -->
											<ul class="depth3">
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="템플릿관리">템플릿관리</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="서비스관리">서비스관리</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>
											<!-- //3depth -->
										</li>
										<!-- //자동메일관리 -->

										<!-- 통계분석// -->
										<li>
											<ul><!-- class="col-box" 삭제 -->
												<li class="col-box"><!-- class="col-box" 추가 -->
													<span class="col menu">
														<button type="button" class="btn-toggle">통계분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
											</ul>

											<!-- 3depth// -->
											<ul class="depth3">
												<li class="col-box">
													<span class="col menu">
														<span class="last">월별분석</span>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="서비스별분석">서비스별분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="도메인별분석">도메인별분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="수신자별분석">수신자별분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last" title="메일별분석">메일별분석</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>
												<li class="col-box">
													<span class="col menu">
														<button class="last">상세로그</button>
													</span>
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
												</li>

											</ul>
											<!-- //3depth -->
										</li>
										<!-- //통계분석 -->

									</ul>
									<!-- //2depth -->
								</li>
								<!-- //RNS -->
							</ul>
						</li>
					</ul>
				</div>
				<!-- //Menu 목록 -->

				<!-- 버튼// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" id="quickMenuInsert">저장</button>
				</div>
				<!-- //버튼 -->
			</div>

		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_quickmenu');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_quickmenu');"></span>
</div>
</form>
<!-- //팝업 -->