<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.09.27
	*	설명 : 캠페인템플릿 정보수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp"%>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/ems/cam/campTempUpdatePApi.js'/>"></script>
<script type="text/javascript">
$(document).ready(function() {
	setTimeout(function(){
		setCampTempContent("<c:out value='${campTempInfo.contentsTyp}'/>","<c:out value='${campTempInfo.contentsPath}'/>");
	},2000);
});
campTempStatus = "<c:out value='${campTempInfo.status}'/>";
</script>


<body>
	<div id="wrapper">
		<div id="wrap" class="ems">
			<div class="content-wrap">
				<!-- content// -->
				<div id="content" class="single-style">

					<!-- cont-head// -->
					<section class="cont-head">
						<div class="title">
							<h2>캠페인템플릿 정보수정</h2>
						</div>
					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">

						<form id="campTempProhibitForm" name="campTempProhibitForm" method="post"> 
							<input type="hidden" id="prohibitCheckTitle" name="prohibitCheckTitle" value="<c:out value='${campTempInfo.emailSubject}'/>">
							<input type="hidden" id="prohibitCheckText" name="prohibitCheckText" value="">
							<input type="hidden" id="prohibitCheckResult" value=""> 
						</form>
						
						<form id="searchForm" name="searchForm" method="post">
							<input type="hidden"                   name="page"              value="<c:out value='${searchVO.page}'/>">
							<input type="hidden"                   name="searchStartDt"     value="<c:out value='${searchVO.searchStartDt}'/>">
							<input type="hidden"                   name="searchEndDt"       value="<c:out value='${searchVO.searchEndDt}'/>">
							<input type="hidden"                   name="searchTnm"         value="<c:out value='${searchVO.searchTnm}'/>">
							<input type="hidden"                   name="searchDeptNo"      value="<c:out value='${searchVO.searchDeptNo}'/>">
							<input type="hidden"                   name="searchUserId"      value="<c:out value='${searchVO.searchUserId}'/>">
							<input type="hidden"                   name="searchCampNo"      value="<c:out value='${searchVO.searchCampNo}'/>">
							<input type="hidden"                   name="searchStatus"      value="<c:out value='${searchVO.searchStatus}'/>">
							<input type="hidden"                   name="tid"               value="<c:out value='${searchVO.tid}'/>">
							<input type="hidden"                   name="tids"              value="<c:out value='${searchVO.tid}'/>">
							<input type="hidden" id="status"       name="status"            value="<c:out value='${campTempInfo.status}'/>">
						</form>
				
						<form id="campTempInfoForm" name="campTempInfoForm" method="post">
							<input type="hidden" id="campNo"          name="campNo"         value="<c:out value='${campTempInfo.campNo}'/>">
							<input type="hidden"                      name="titleKey"       value="1">
							<input type="hidden"                      name="textKey"        value="2">
							<input type="hidden" id="tid"             name="tid"            value="<c:out value='${campTempInfo.tid}'/>">
							<input type="hidden" id="tids"            name="tids"           value="<c:out value='${campTempInfo.tid}'/>">
							<input type="hidden" id="serviceContent"  name="serviceContent" value="">
							<input type="hidden" id="contentsTyp"     name="contentsTyp"    value="<c:out value='${campTempInfo.contentsTyp}'/>">
							<input type="hidden"                      name="useYn"          value="<c:out value='${campTempInfo.useYn}'/>">
							<input type="hidden" id="contentsPath"    name="contentsPath"   value="<c:out value='${campTempInfo.contentsPath}'/>">
							<input type="hidden" id="approvalYn"      name="approvalYn"     value="<c:out value='${campTempInfo.approvalLineYn}'/>">
							<input type="hidden" id="imgChkYn"        name="imgChkYn"       value="<c:out value='${campTempInfo.imgChkYn}'/>">
							<input type="hidden" id="prohibitChkTyp"  name="prohibitChkTyp" value="<c:out value='${campTempInfo.prohibitChkTyp}'/>">
							
							<!--  고객정보 Check-->
							<input type="hidden" id="titleChkYn"      name="titleChkYn"       value="<c:out value='${campTempInfo.titleChkYn}'/>">
							<input type="hidden" id="bodyChkYn"       name="bodyChkYn"        value="<c:out value='${campTempInfo.bodyChkYn}'/>">
							<input type="hidden" id="attachFileChkYn" name="attachFileChkYn"  value="<c:out value='${campTempInfo.attachFileChkYn}'/>">
							
							<!--  금지어 관련   -->
							<input type="hidden" id="prohibitTitleCnt"  name="prohibitTitleCnt"  value="<c:out value='${campTempInfo.prohibitTitleCnt}'/>">
							<input type="hidden" id="prohibitTitleDesc" name="prohibitTitleDesc" value="<c:out value='${campTempInfo.prohibitTitleDesc}'/>">
							<input type="hidden" id="prohibitTextCnt"   name="prohibitTextCnt"   value="<c:out value='${campTempInfo.prohibitTextCnt}'/>">
							<input type="hidden" id="prohibitTextDesc"  name="prohibitTextDesc"  value="<c:out value='${campTempInfo.prohibitTextDesc}'/>">
							
							<!--  API로 넘어온것인지 여부 -->
							<input type="hidden" id="apiSso"                                     value="<c:out value='${NEO_SSO_LOGIN}'/>"> 
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
														<p class="label bg-gray" id="txtCampNm"><c:out value="${campTempInfo.campNm}"/></p>
														<button type="button" class="btn fullblue" onclick="popCampSelect();">검색</button>
													</div>
												</div>
											</li>
											<li class="col-full">
												<label class="required">연계 캠페인 번호</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray"><c:out value='${campId}'/></p>
														<input type="hidden" id="eaiCampNo" name="eaiCampNo" value="<c:out value='${campId}'/>">
														<!-- <button type="button" class="btn btn-search" id="chkEaiCampNo" onclick="checkEaiCampNo();" value="N">중복검사</button> -->
													</div>
												</div>
											</li>
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
																		<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == campTempInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
															<select id="userId" name="userId" onchange="getUserInfo(this.value)" title="사용자명 선택">
																<option value="">선택</option>
																<c:if test="${fn:length(userList) > 0}">
																	<c:forEach items="${userList}" var="user">
																		<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq campTempInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
																	</c:forEach>
																</c:if>
															</select>
														</div>
													</div>
												</li>
											</c:if>
											<li class="col-full">
												<label class="required">발송자명</label>
												<div class="list-item">
													<input type="hidden" name="sid" id="campTempSid" value="<c:out value='${campTempInfo.sid}'/>">
													<input type="text" name="sname" id="campTempSname" value="<c:out value='${campTempInfo.sname}'/>"> 
												</div>
											</li>
											<li class="col-full">
												<label class="required">발송자 이메일</label>
												<div class="list-item">
													<input type="text" name="smail" id="campTempSmail" value="<crypto:decrypt colNm='SMAIL' data='${campTempInfo.smail}'/>">
												</div>
											</li>
											<!--  
											<li>
												<label>웹에이전트</label>
												<div class="list-item">
													<div class="filebox" style="width: 100%;">
														<p class="label" id="txtWebAgentUrl" style="width: 230px;">
															<c:if test="${not empty webAgent}">
																<c:if test="${'Y' eq webAgent.secuAttYn}">첨부파일로 지정되었습니다.</c:if>
																<c:if test="${'N' eq webAgent.secuAttYn}">본문삽입으로 지정되었습니다.</c:if>
															</c:if>
															<c:if test="${empty webAgent}">형식이 지정되지 않았습니다.</c:if>
														</p>
														<button type="button" class="btn fullblue" onclick="popWebAgent();">수정</button>
														<div class="select" style="display: none;">
															<input type="hidden" id="webAgentAttachYn" name="webAgentAttachYn" value="<c:out value="${webAgent.secuAttYn}"/>"> 
														</div>
														<div class="select" style="width: 100px;">
															<select id="secuAttTyp" name="secuAttTyp" onchange="changeAttachYn();" title="문서유형 선택">
																<option value="NONE" <c:if test="${'NONE' eq webAgent.secuAttTyp}"> selected</c:if>>NONE</option>
																<option value="PDF" <c:if test="${'PDF' eq webAgent.secuAttTyp}"> selected</c:if>>PDF</option>
																<option value="EXCEL" <c:if test="${'EXCEL' eq webAgent.secuAttTyp}"> selected</c:if>>EXCEL</option>
															</select>
														</div>
														<button type="button" class="btn fullblue" onclick="popWebAgentPreview();">미리보기</button>
													</div>
												</div>
											</li>
											-->
											<li>
												<label>금칙어</label>
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
													<label><input type="checkbox" name="recvChkYn" id="recvChkYn" <c:if test="${'Y' eq campTempInfo.recvChkYn}"> checked </c:if> value="${ campTempInfo.recvChkYn }"><span>수신확인</span></label>
												</div>
											</li>
											<li>
												<label>수신자 그룹</label>
												<div class="list-item">
													<div class="filebox">
														<c:set var="segNm" value="" />
														<c:set var="segNoc" value="" />
														<c:forEach items="${segList}" var="seg">
															<c:if test="${seg.segNo == campTempInfo.segNo}">
																<c:set var="segNm" value="${seg.segNm}" />
																<c:set var="segNoc" value="${seg.segNo}|${seg.mergeKey}" />
															</c:if>
														</c:forEach>
														<p class="label bg-gray" style="width: calc(100% - 14.2rem);" id="txtSegNm"><c:out value="${campTempInfo.segNm}"/></p>
														<input type="hidden" id="segNoc" name="segNoc" value="<c:out value="${segNoc}"/>">
														<button type="button" class="btn fullblue" onclick="goSegInfoMail('');">미리보기</button>
													</div>
												</div>
											</li>
											<li class="col-full"> 
												<label class="required">템플릿명</label>
												<div class="list-item">
													<input type="text" id="tnm" name="tnm" value="<c:out value='${campTempInfo.tnm}'/>" placeholder="템플릿명을 입력해주세요.">
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
											<button type="button" class="btn" onclick="popUploadHtml('mail')">HTML등록</button>
										</div>
										<!-- //버튼 -->
									</div>

									<div class="list-area">
										<ul>
											<li>
												<label class="required">메일 제목</label>
												<div class="list-item">
													<input type="text" id="emailSubject" name="emailSubject" value="<c:out value='${campTempInfo.emailSubject}'/>" placeholder="템플릿명을 입력해주세요.">
												</div>
											</li>
											<li>
												<label>함수입력</label>
												<div class="list-item">
													<div class="select" style="width: calc(100% - 13.5rem);">
														<select id="mergeKeyCampTemp" title="함수입력 선택">
															<option value="">선택</option>
															<c:if test="${fn:length(mergeList) > 0}">
																<c:forEach items="${mergeList}" var="merge">
																	<option value="<c:out value='$:${merge.cdNm}:$'/>"><c:out value='${merge.cdDtl}'/></option>
																</c:forEach>
															</c:if>
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
															//var sHTML = "<img src='<c:out value='${DEFAULT_DOMAIN}'/>/img/upload/"
																var sHTML = "<img src='<c:out value='${DEFAULT_DOMAIN}${DEFAULT_IMG_PATH}'/>"
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
										</ul>
										<ul>
											<li>
												<label>등록자</label>
												<div class="list-item">
													<p class="inline-txt"><c:out value="${campTempInfo.regNm}"/></p>
												</div>
											</li>
											<li>
												<label>수정자</label>
												<div class="list-item">
													<p class="inline-txt"><c:out value="${campTempInfo.upNm}"/></p>
												</div>
											</li>
											<li>
												<label>등록일시</label>
												<div class="list-item">
													<p class="inline-txt">
														<fmt:parseDate var="regDate" value="${campTempInfo.regDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy.MM.dd HH:mm"/>
														<c:out value="${regDt}"/>
													</p>
												</div>
											</li>
											<li>
												<label>수정일시</label>
												<div class="list-item">
													<p class="inline-txt">
														<fmt:parseDate var="upDate" value="${campTempInfo.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="upDt" value="${upDate}" pattern="yyyy.MM.dd HH:mm"/>
														<c:out value="${upDt}"/>
													</p>
												</div>
											</li>
										</ul>
									</div>
									<!-- btn-wrap// -->
									<div class="btn-wrap btn-biggest">
										<button type="button" class="btn big fullblue" onclick="goUpdate('<c:out value='${campInfo.status}'/>');">수정</button>
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
