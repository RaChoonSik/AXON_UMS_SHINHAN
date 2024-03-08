<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.09.27
	*	설명 : API 템플릿 신규등록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp"%>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/ems/cam/campTempAddP.js'/>"></script>
<script type="text/javascript">
	var globalApprLineYn = "N";
</script>
 
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
							<h2>API 템플릿 신규등록</h2>
						</div>
					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">

						<form id="campTempProhibitForm" name="campTempProhibitForm" method="post"> 
							<input type="hidden" id="prohibitCheckTitle" name="prohibitCheckTitle" value="">
							<input type="hidden" id="prohibitCheckText"  name="prohibitCheckText"  value="">
							<input type="hidden" id="prohibitCheckResult"                          value=""> 
						</form>

						 
						<form id="campTempInfoForm" name="campTempInfoForm" method="post">
							<input type="hidden" id="campNo"            name="campNo"          value="0">
							<input type="hidden" id="segNoc"            name="segNoc"          value="<c:out value="${defSegNoc}"/>">
							<input type="hidden"                        name="titleKey"        value="1">
							<input type="hidden"                        name="textKey"         value="2">
							<input type="hidden" id="serviceContent"    name="serviceContent">
							<input type="hidden"                        name="useYn"            value="Y">
							<input type="hidden" id="contentsTyp"       name="contentsTyp"      value="2">
							
							<!--  실시간 이메일 화면숨김 고정값 -->
							<input type="hidden" id="linkYn"            name="linkYn"            value="N">
							<input type="hidden" id="respLog"           name="respLog"           value="31">
							<input type="hidden" id="approvalYn"        name="approvalYn"        value="N">
							<input type="hidden" id="imgChkYn"          name="imgChkYn"          value="N">
							<input type="hidden" id="prohibitChkTyp"    name="prohibitChkTyp"    value="000">

							<!--  고객정보 Check-->
							<input type="hidden" id="titleChkYn"        name="titleChkYn"        value="N">
							<input type="hidden" id="bodyChkYn"         name="bodyChkYn"         value="N">
							<input type="hidden" id="attachFileChkYn"   name="attachFileChkYn"   value="N">
							
							<!--  금지어 관련   -->
							<input type="hidden" id="prohibitTitleCnt"  name="prohibitTitleCnt"  value="0">
							<input type="hidden" id="prohibitTitleDesc" name="prohibitTitleDesc" value="">
							<input type="hidden" id="prohibitTextCnt"   name="prohibitTextCnt"   value="0">
							<input type="hidden" id="prohibitTextDesc"  name="prohibitTextDesc"  value="">

							<!--  API 사용시 리턴 해줄 정보  -->
							<input type="hidden" id="campId"            name="campId"      value="<c:out value='${campId}'/>">
							<input type="hidden" id="cellNodeId"        name="cellNodeId"  value="<c:out value='${cellNodeId}'/>">
							<input type="hidden" id="contId"            name="contId"      value="<c:out value='${contId}'/>"> 
							 
							<fieldset>
								<legend>조건 및 메일 내용</legend>

								<!-- 조건// -->
								<div class="graybox">
									<div class="title-area">
										<h3 class="h3-title" style="margin-top: 0;">
											조건
										</h3>
										<!-- 버튼// -->
										<div class="btn-wrap">
											<span class="required" style="margin-right: 0;">*필수입력 항목</span>
										</div>
										<!-- //버튼 -->
									</div>

									<div class="list-area">
										<ul>
											<li>
												<label class="required">캠페인</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray" id="txtCampNm">선택된 캠페인이 없습니다.</p>
														<button type="button" class="btn btn-search" onclick="popCampSelect();">검색</button>
													</div>
												</div>
											</li>
											<!-- 
											<li class="col-full">
												<label class="required">연계 템플릿 번호</label>
												<div class="list-item">
													<div class="filebox">
														<input type="text" id="eaiCampNo" name="eaiCampNo" placeholder="연계 관리용 탬플릿 번호를 입력해주세요." style="width:calc(100% - 8.1rem);">
														<button type="button" class="btn btn-search" id="chkEaiCampNo" onclick="checkEaiCampNo();" value="N">중복검사</button>
													</div>
												</div>
											</li>
											 -->
											<%-- 관리자의 경우 전체 요청부서를 전시하고 그 외의 경우에는 숨김 --%>
											<c:if test="${'Y' eq NEO_ADMIN_YN}">
												<li>
													<label class="required">사용자그룹</label>
													<div class="list-item">
														<div class="select">
															<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
																<option value="0">선택</option>
																<c:if test="${fn:length(deptList) > 0}">
																	<c:forEach items="${deptList}" var="dept">
																		<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}' /></option>
																	</c:forEach>
																</c:if>
															</select>
														</div>
													</div>
												</li>
												<li>
													<label class="required">사용자명</label>
													<div class="list-item">
														<div class="select">
															<select id="userId" name="userId" title="사용자명 선택">
																<option value="">선택</option>
															</select>
														</div>
													</div>
												</li>
											</c:if>
											<li class="col-full">
												<label class="required">발송자명</label>
												<div class="list-item">
													<input type="text" name="sname" value="<c:out value='${campTempInfo.sname}'/>">
												</div>
											</li>
											<li class="col-full">
												<label class="required">발송자 이메일</label>
												<div class="list-item">
													<input type="text" name="smail" value="<crypto:decrypt colNm='MAIL_FROM_EM' data='${campTempInfo.smail}'/>">
												</div>
											</li>
											
											<li style="display:none">
												<label>웹에이전트</label>
												<div class="list-item">
													<div class="filebox" style="width: 100%;">
														<p class="label" id="txtWebAgentUrl" style="width: 230px;">형식이 지정되지 않았습니다.</p>
														<button type="button" class="btn fullblue" onclick="popWebAgent();">등록</button>
														<div class="select" style="display: none;">
															<input type="hidden" id="webAgentAttachYn" name="webAgentAttachYn" value="N">
														</div>
														<div class="select" style="width: 100px;">
															<select id="secuAttTyp" name="secuAttTyp" onchange="changeAttachYn();" title="문서유형 선택">
																<option value="NONE">NONE</option>
																<!-- <option value="PDF">PDF</option> -->
																<!-- <option value="EXCEL">EXCEL</option> -->
															</select>
														</div>
														<button type="button" class="btn fullblue" onclick="popWebAgentPreview();">미리보기</button>
													</div>
												</div>
											</li>
											<li style="display:none">>
												<label class="required"> 금칙어</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray" style="width:calc(100% - 10.4rem);" id="txtProhibitDesc">금칙어 포함 여부를 확인해주세요</p>
														<button type="button" class="btn fullblue" id="chkProhibit"  onclick="checkProhibit();">금칙어 확인</button>
													</div>
												</div>
											</li>
											<li>
												<label>옵션</label>
												<div class="list-item">
													<label><input type="checkbox" name="recvChkYn" id="recvChkYn" value="Y" checked ><span>수신확인</span></label>
												</div>
											</li>
											<li style="display:none">>
												<label class="required">수신자 그룹</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray" style="width: calc(100% - 14.2rem);" id="txtSegNm">선택된 수신자그룹이 없습니다.</p>
														<button type="button" class="btn btn-search" onclick="popSegSelect();">검색</button> 
														<button type="button" class="btn fullblue" onclick="goSegInfoMail('');">미리보기</button>
													</div>
												</div>
											</li>
											<li class="col-full"> 
												<label class="required">API 템플릿 번호</label>
												<div class="list-item">
													<div class="filebox">
														<input type="text" id="tid" name="tid" style="width:calc(100% - 10.4rem);" placeholder="API 템플릿ID를 입력해주세요.(숫자)" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" />
														<button type="button" class="btn fullblue" id="chkTid"  onclick="checkTid();">중복확인 검사</button>
													</div>
												</div>
											</li>
											<li class="col-full"> 
												<label class="required">템플릿명</label>
												<div class="list-item">
													<input type="text" id="tnm" name="tnm" placeholder="템플릿명을 입력해주세요.">
												</div>
											</li>
										</ul>
									</div>
								</div>
								<!-- //조건 -->

								<!-- 메일 내용// -->
								<div class="graybox">
									<div class="title-area">
										<h3 class="h3-title">메일 내용</h3>

										<!-- 버튼// -->
										<div class="btn-wrap">
											<button type="button" class="btn" onclick="popUploadHtml('mail');">HTML등록</button>
										</div>
										<!-- //버튼 -->
									</div>

									<div class="list-area">
										<ul>
											<li>
												<label class="required">메일 제목</label>
												<div class="list-item">
													<input type="text" id="emailSubject" name=emailSubject placeholder="메일 제목을 입력해주세요.">
												</div>
											</li>
											<li style="display:none">>
												<label>함수입력</label>
												<div class="list-item">
													<div class="select" style="width: calc(100% - 13.5rem);">
														<select id="mergeKeyCampTemp" title="함수입력 선택">
														</select>
													</div>
													<button type="button" class="btn fullblue" onclick="goTitleMerge();">제목</button>
													<button type="button" class="btn fullblue" onclick="goContMerge();">본문</button>
												</div>
											</li>
											<li class="col-full">
												<!-- 에디터 영역// -->
												<div class="editbox">
													<textarea name="ir1" id="ir1" rows="10" cols="100" style="text-align: center; width: 100%; height: 400px; display: none; border: none;"></textarea>
													<script type="text/javascript">
														var oEditors = [];

														// 추가 글꼴 목록
														//var aAdditionalFontSet = [["MS UI Gothic", "MS UI Gothic"], ["Comic Sans MS", "Comic Sans MS"],["TEST","TEST"]];

														nhn.husky.EZCreator
																.createInIFrame({
																	oAppRef : oEditors,
																	elPlaceHolder : "ir1",
																	sSkinURI : "<c:url value='/smarteditor/SmartEditor2Skin.html'/>",
																	htParams : {
																		bUseToolbar : true, // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
																		bUseVerticalResizer : true, // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
																		bUseModeChanger : true, // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
																		//aAdditionalFontList : aAdditionalFontSet,		// 추가 글꼴 목록
																		fOnBeforeUnload : function() {
																			//alert("완료!");
																		}
																	}, //boolean
																	fOnAppLoad : function() {
																		//예제 코드
																		//oEditors.getById["ir1"].exec("PASTE_HTML", ["로딩이 완료된 후에 본문에 삽입되는 text입니다."]);
																		//body_loaded();
																	},
																	fCreator : "createSEditor2"
																});

														function pasteHTML(obj) {
															var sHTML = "<img src='<c:out value='${DEFAULT_DOMAIN}${DEFAULT_IMG_PATH}'/>"
															//var sHTML = "<img src='<c:out value='${DEFAULT_DOMAIN}'/>/img/upload/"
																	+ obj
																	+ "'>";
															oEditors.getById["ir1"]
																	.exec(
																			"PASTE_HTML",
																			[ sHTML ]);
														}

														function showHTML() {
															var sHTML = oEditors.getById["ir1"]
																	.getIR();
															alert(sHTML);
														}

														function submitContents(
																elClickedObj) {
															oEditors.getById["ir1"]
																	.exec(
																			"UPDATE_CONTENTS_FIELD",
																			[]); // 에디터의 내용이 textarea에 적용됩니다.

															// 에디터의 내용에 대한 값 검증은 이곳에서 document.getElementById("ir1").value를 이용해서 처리하면 됩니다.

															try {
																elClickedObj.form
																		.submit();
															} catch (e) {
															}
														}

														function setDefaultFont() {
															var sDefaultFont = '궁서';
															var nFontSize = 24;
															oEditors.getById["ir1"]
																	.setDefaultFont(
																			sDefaultFont,
																			nFontSize);
														}
													</script>
												</div>
												<!-- //에디터 영역 -->
											</li>									
											<li class="col-full">
												<label class="vt">파일첨부</label>
												<div class="list-item">
													<button type="button" class="btn" onclick="fn.popupOpen('#popup_file');">파일선택</button>
													<ul class="filelist" id="mailAttachFileList"></ul>
												</div>
											</li> 
										</ul>
									</div>
									<!-- btn-wrap// -->
									<div class="btn-wrap btn-biggest">
										<button type="button" class="btn big fullblue" id="btnAdd" onclick="goAdd();">등록</button>
										<button type="button" class="btn big" onclick="goCancel();">취소</button>
									</div>
									<!-- //btn-wrap -->
								</div>
								<!-- //메일 내용 -->

								<!-- 웹에이전트팝업// -->
								<%@ include file="/WEB-INF/jsp/inc/pop/pop_web_agent.jsp"%>
								<!-- //웹에이전트팝업 -->
							</fieldset>
						</form>

					</section>
					<!-- //cont-body -->

				</div>
				<!-- // content -->
			</div>
		</div>
		</div>
	</div>

	<iframe id="iFrmMail" name="iFrmMail" style="width: 0px; height: 0px;"></iframe>

	<!-- 수신자그룹미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_mail.jsp"%>
	<!-- //수신자그룹미리보기팝업 -->

	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp"%>
	<!-- //조회사유팝업 -->

	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_file.jsp"%>
	<!-- //파일등록 팝업 -->

	<!-- 캠페인선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/ems/cam/pop/pop_campaign.jsp"%>
	<!-- //캠페인선택 팝업 -->

	<!-- 수신자그룹선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/ems/cam/pop/pop_camp_temp_segment.jsp"%>
	<!-- //수신자그룹선택 팝업 -->

	<!-- HTML등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_html.jsp"%>
	<!-- //HTML등록 팝업 -->

	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_webagent.jsp"%>
	<!-- //웹에이전트미리보기 팝업 -->
	<!-- 준법심의 확정 // -->
	<%@ include file="/WEB-INF/jsp/ems/cam/pop/pop_prohibit.jsp"%>
	<!-- //준법심의 확정 -->

</body>
</html>
