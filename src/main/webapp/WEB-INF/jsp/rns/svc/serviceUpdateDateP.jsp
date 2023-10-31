<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2022.02.17
	*	설명 : 자동메일 서비스 부부수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_rns.jsp"%>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld"%>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/rns/svc/serviceUpdateDateP.js'/>"></script>
<script type="text/javascript">
$(document).ready(function() {
	setTimeout(function(){
		setServiceContent("<c:out value='${serviceInfo.contentsTyp}'/>","<c:out value='${serviceInfo.contentsPath}'/>");
	},1000);
});
rnsStatus = "<c:out value='${serviceInfo.status}'/>";
rnsWorkStatus = "<c:out value='${serviceInfo.workStatus}'/>";
rnsApprLineYn = "<c:out value='${serviceInfo.approvalLineYn}'/>";
rnsApprProcYn = "<c:out value='${serviceInfo.approvalProcYn}'/>";
</script>

<body>
	<div id="wrapper" class="rns">
		<header>
			<h1 class="logo">
				<a href="/ems/index.ums"><span class="txt-blind">LOGO</span></a>
			</h1>
			<!-- 공통 표시부// -->
			<%@ include file="/WEB-INF/jsp/inc/top.jsp"%>
			<!-- //공통 표시부 -->
		</header>
		<div id="wrap" class="rns">

			<!-- lnb// -->
			<div id="lnb">
				<!-- LEFT MENU -->
				<%@ include file="/WEB-INF/jsp/inc/menu_rns.jsp"%>
				<!-- LEFT MENU -->
			</div>
			<!-- //lnb -->
			<div class="content-wrap">
				<!-- content// -->
				<div id="content" class="single-style">

					<!-- cont-head// -->
					<section class="cont-head">
						<div class="title">
							<h2>자동메일 서비스 정보수정</h2>
						</div>

					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">
						<form id="rnsProhibitForm" name="rnsProhibitForm" method="post">
							<input type="hidden" id="prohibitTitle" name="prohibitTitle" value="">
							<input type="hidden" id="prohibitText" name="prohibitText" value="">
							<input type="hidden" id="prohibitCheckTitle" name="prohibitCheckTitle" value="<c:out value='${serviceInfo.emailSubject}'/>">
							<input type="hidden" id="prohibitCheckText" name="prohibitCheckText" value="">
							<input type="hidden" id="prohibitCheckImg" name="prohibitCheckImg" value="<c:out value='${serviceInfo.imgChkYn}'/>">
							<c:if test="${serviceInfo.attchCnt > 0}">
								<c:set var="checkAttach" value="Y" />
							</c:if>
							<c:if test="${serviceInfo.attchCnt == 0}">
								<c:set var="checkAttach" value="N" />
							</c:if>
							<input type="hidden" id="prohibitCheckAttach" name="prohibitCheckAttach" value="<c:out value='${checkAttach}'/>">
							<input type="hidden" id="prohibitCheckAttachChange" value="N">
							<input type="hidden" id="prohibitLineCheck" value="N">

						</form>
						<form id="searchForm" name="searchForm" method="post">
							<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
							<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
							<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
							<input type="hidden" name="searchTnm" value="<c:out value='${searchVO.searchTnm}'/>">
							<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
							<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
							<input type="hidden" name="searchContentsTyp" value="<c:out value='${searchVO.searchContentsTyp}'/>">
							<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
							<input type="hidden" name="tid" value="<c:out value='${searchVO.tid}'/>">
							<input type='hidden' id="tids" name='tids' value="<c:out value='${searchVO.tid}'/>">
							<input type="hidden" id="status" name="status" value="" />
							<input type="hidden" id="testSendAuth" value="<c:out value='${testSendAuth}'/>">
						</form>

						<form id="serviceInfoForm" name="serviceInfoForm" method="post">
							<input type="hidden" name="titleKey" value="1">
							<input type="hidden" name="textKey" value="2">
							<input type="hidden" id="tid" name="tid" value="<c:out value='${serviceInfo.tid}'/>">
							<input type="hidden" id="serviceContent" name="serviceContent" value="">
							<input type="hidden" name="useYn" value="<c:out value='${serviceInfo.useYn}'/>">
							<input type="hidden" name="contentsPath" value="<c:out value='${serviceInfo.contentsPath}'/>">
							<input type="hidden" id="approvalYn" name="approvalYn" value="<c:out value='${serviceInfo.approvalLineYn}'/>">
							<input type="hidden" id="imgChkYn" name="imgChkYn" value="<c:out value='${serviceInfo.imgChkYn}'/>">
							<input type="hidden" id="prohibitChkTyp" name="prohibitChkTyp" value="<c:out value='${serviceInfo.prohibitChkTyp}'/>">

							<!--  고객정보 Check-->
							<input type="hidden" id="titleChkYn" name="titleChkYn" value="<c:out value='${serviceInfo.titleChkYn}'/>">
							<input type="hidden" id="bodyChkYn" name="bodyChkYn" value="<c:out value='${serviceInfo.bodyChkYn}'/>">
							<input type="hidden" id="attachFileChkYn" name="attachFileChkYn" value="<c:out value='${serviceInfo.attachFileChkYn}'/>">

							<!--  금지어 관련   -->
							<input type="hidden" id="prohibitTitleCnt" name="prohibitTitleCnt" value="<c:out value='${serviceInfo.prohibitTitleCnt}'/>">
							<input type="hidden" id="prohibitTitleDesc" name="prohibitTitleDesc" value="<c:out value='${serviceInfo.prohibitTitleDesc}'/>">
							<input type="hidden" id="prohibitTextCnt" name="prohibitTextCnt" value="<c:out value='${serviceInfo.prohibitTextCnt}'/>">
							<input type="hidden" id="prohibitTextDesc" name="prohibitTextDesc" value="<c:out value='${serviceInfo.prohibitTextDesc}'/>">

							<fieldset>
								<legend>조건 및 메일 내용</legend>

								<!-- 조건// -->
								<div class="graybox">
									<div class="title-area">
										<h3 class="h3-title">조건</h3>
										<span class="required">*수정 항목</span>
									</div>

									<div class="list-area">
										<ul>
											<li>
												<label>콘텐츠타입</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray"><c:out value='${serviceInfo.contentsTypNm}' /></p>
													</div>
												</div>
											</li>
											<li>
												<label>사용자그룹</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray">시스템관리부</p>
													</div>
												</div>
											</li>
											<li>
												<label>사용자명</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray">허초희</p>
													</div>
												</div>
											</li>
											<%-- <li>
												<label>EAI 번호</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray"><c:out value='${serviceInfo.eaiCampNo}' /></p>
													</div>
												</div>
											</li> --%>
											<%-- <li>
												<label class="required">발송자명</label>
												<div class="list-item">
													<input type="hidden" name="sid" value="<c:out value='${serviceInfo.sid}'/>">
													<input type="text" name="sname" value="<c:out value='${serviceInfo.sname}'/>">
												</div>
											</li> --%>
											<li>
												<label class="required">발송자 이메일</label>
												<div class="list-item">
													<input type="text" name="smail" value="<crypto:decrypt colNm="SMAIL" data="${serviceInfo.smail}"/>">
												</div>
											</li>
											<%-- <li class="lineheight32">
												<label>준법심의</label>
												<div class="list-item">
													<a href="javascript:goPopProbibitInfo('<c:out value='${serviceInfo.tid}'/>');"><c:out value='${serviceInfo.prohibitDesc}' /></a>
												</div>
											</li> --%>
											<%-- <li>
												<label>보안메일</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray" style="width: calc(100% - 68px);"><c:if test="${not empty webAgent}">
																<c:if test="${'Y' eq webAgent.secuAttYn}">첨부파일로 지정되었습니다.
															(<c:out value="${webAgent.secuAttTyp}" />)
														</c:if>
																<c:if test="${'N' eq webAgent.secuAttYn}">본문삽입으로 지정되었습니다.</c:if>
															</c:if> <c:if test="${empty webAgent}">
														형식이 지정되지 않았습니다.
													</c:if></p>
														<button type="button" class="btn fullpurple" onclick="popWebAgentPreview();">미리보기</button>
													</div>
												</div>
											</li> --%>
											<li>
												<label>웹에이전트</label>
												<div class="list-item">
													<div class="filebox" style="width: 100%;">
														<p class="label" id="txtWebAgentUrl" style="width: 230px;">형식이 지정되지 않았습니다.</p>
														<button type="button" class="btn fullblue" onclick="popWebAgent();">등록</button>
														<div class="select" style="display: none;">
															<input type="hidden" id="webAgentAttachYn" name="webAgentAttachYn" value="N">
															<!-- <select id="webAgentAttachYn" name="webAgentAttachYn" onchange="changeAttachYn();" title="웹에이전트첨부 선택">
														<option value="Y">첨부파일</option>
														<option value="N">본문삽입</option>
													</select> -->
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
											<%-- <li class="lineheight32">
												<label>발송결재라인</label>
												<div class="list-item">
													<c:if test="${'N' eq serviceInfo.approvalLineYn}">
														<p class="label bg-gray">발송결재라인이 등록되지 않았습니다.</p>
													</c:if>
													<c:if test="${'Y' eq serviceInfo.approvalLineYn}">
														<a href="javascript:popRnsApprovalState('<c:out value='${serviceInfo.tid}'/>');"> <c:if test="${fn:length(apprLineList) > 0}">
																<c:set var="arrpLength" value="${fn:length(apprLineList) - 1}" />
																<c:set var="prohibitLLength" value="${fn:length(prohibitLineList)}" />
																<c:forEach items="${apprLineList}" var="appr">
																	<c:if test="${appr.apprStep == 1}">
																		<c:if test="${arrpLength > 0 }">
																			<c:if test="${prohibitLLength > 0 }">
																				<c:out value='${appr.apprUserNm}' /> / <c:out value='${appr.orgNm}' /> / <c:out value='${appr.jobNm}' /> 외 
																		<c:out value='${arrpLength}' />명 준법심의결재 <c:out value='${prohibitLLength}' />명
																	</c:if>
																			<c:if test="${prohibitLLength == 0 }">
																				<c:out value='${appr.apprUserNm}' /> / <c:out value='${appr.orgNm}' /> / <c:out value='${appr.jobNm}' /> 외 
																		<c:out value='${arrpLength}' />명 
																	</c:if>
																		</c:if>
																		<c:if test="${arrpLength == 0 }">
																			<c:out value='${appr.apprUserNm}' /> / <c:out value='${appr.orgNm}' /> / <c:out value='${appr.jobNm}' />
																			<c:if test="${prohibitLLength > 0 }">
																		준법심의결재 <c:out value='${prohibitLLength}' />명
																	</c:if>
																		</c:if>
																	</c:if>
																</c:forEach>
															</c:if>
														</a>
													</c:if>

												</div>
											</li> --%>
											<li>
												<label style="letter-spacing: -0.03em;">고객정보 Check</label>
												<div class="list-item">
													<c:if test="${'Y' eq envSetAuth}">
														<label>
															<input type="checkbox" name="infoCheckYn" <c:if test="${'Y' eq serviceInfo.titleChkYn}"> checked </c:if>>
															<span>메일 제목</span>
														</label>
														<label>
															<input type="checkbox" name="infoCheckYn" <c:if test="${'Y' eq serviceInfo.bodyChkYn}"> checked </c:if>>
															<span>메일 본문</span>
														</label>
														<label>
															<input type="checkbox" name="infoCheckYn" <c:if test="${'Y' eq serviceInfo.attachFileChkYn}"> checked </c:if>>
															<span>일반 첨부파일</span>
														</label>
													</c:if>
													<c:if test="${'Y' ne envSetAuth}">
														<label>
															<input type="checkbox" name="infoCheckYn" disabled <c:if test="${'Y' eq serviceInfo.titleChkYn}"> checked </c:if>>
															<span>메일 제목</span>
														</label>
														<label>
															<input type="checkbox" name="infoCheckYn" disabled <c:if test="${'Y' eq serviceInfo.bodyChkYn}"> checked </c:if>>
															<span>메일 본문</span>
														</label>
														<label>
															<input type="checkbox" name="infoCheckYn" disabled <c:if test="${'Y' eq serviceInfo.attachFileChkYn}"> checked </c:if>>
															<span>일반 첨부파일</span>
														</label>
													</c:if>
												</div>
											</li>
											<%-- <li>
												<label>마케팅 동의여부</label>
												<div class="list-item">
													<div class="select">
														<select id="mailMktGb" name="mailMktGb" title="마케팅동의여부 선택">
															<option value="000">선택</option>
															<c:if test="${fn:length(mktGbList) > 0}">
																<c:forEach items="${mktGbList}" var="mktGb">
																	<option value="<c:out value='${mktGb.cd}'/>" <c:if test="${mktGb.cd eq serviceInfo.mailMktGb}"> selected</c:if>><c:out value='${mktGb.cdNm}' /></option>
																</c:forEach>
															</c:if>
														</select>
													</div>
												</div>
											</li> --%>
											<li class="col-full">
												<label class="required">서비스명</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray"><c:out value='${serviceInfo.tnm}' /></p>
													</div>
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
											<button type="button" class="btn" onclick="popRnsTestSend();">테스트발송</button>
											<%--삭제가 아닌것중에 상태가 발송대기,결재대기,발송승인 상태에서 사용중지/복구/삭제 가능 --%>
											<c:if test="${'002' ne serviceInfo.status && '202' ne serviceInfo.workStatus}">
												<button type="button" class="btn<c:if test="${'001' eq serviceInfo.status}"> hide</c:if>" id="btnDisable" onclick="goDisable();">사용중지</button>
												<button type="button" class="btn<c:if test="${'000' eq serviceInfo.status}"> hide</c:if>" id="btnEnable" onclick="goEnable();">복구</button>
												<button type="button" class="btn" onclick="goDelete();">삭제</button>
											</c:if>
											<c:if test="${'002' ne serviceInfo.status}">
												<button type="button" class="btn" onclick="goCopy();">복사</button>
											</c:if>
										</div>
										<!-- //버튼 -->
									</div>

									<div class="list-area">
										<ul>
											<li class="col-full">
												<label>메일 제목</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray"><c:out value='${serviceInfo.emailSubject}' /></p>
													</div>
												</div>
											</li>
											<li class="col-full">
												<!-- 에디터 영역// -->
												<div id="serviceContents" class="editbox"></div>
												<!-- //에디터 영역 -->
											</li>
											<li class="col-full">
												<label class="vt">파일첨부</label>
												<div class="list-item">
													<ul class="filelist" id="mailAttachFileList">
														<c:if test="${fn:length(attachList) > 0}">
															<c:set var="totalCount" value="${0}" />
															<c:set var="totalSize" value="${0}" />
															<c:forEach items="${attachList}" var="attach">
																<c:set var="totalCount" value="${totalCount + 1}" />
																<c:set var="totalSize" value="${totalSize + attach.attFlSize}" />
																<li>
																	<input type="hidden" name="attachNm" value="<c:out value='${attach.attNm}'/>">
																	<c:set var="attFlPath" value="${fn:substring(attach.attFlPath, fn:indexOf(attach.attFlPath,'/')+1, fn:length(attach.attFlPath))}" />
																	<input type="hidden" name="attachPath" value="<c:out value='${attFlPath}'/>">
																	<input type="hidden" name="attachSize" value="<c:out value='${attach.attFlSize}'/>">
																	<span><a href="javascript:goFileDown('<c:out value="${attach.attNm}"/>','<c:out value='${attach.attFlPath}'/>');"><c:out value='${attach.attNm}' /></a></span>
																</li>
															</c:forEach>
															<script type="text/javascript">
													totalFileCnt = <c:out value='${totalCount}'/>;
													totalFileByte = <c:out value='${totalSize}'/>;
													</script>
														</c:if>
													</ul>
												</div>
											</li>
										</ul>
										<ul>
											<li>
												<label>등록자</label>
												<div class="list-item">
													<p class="inline-txt"><c:out value="${serviceInfo.regNm}" /></p>
												</div>
											</li>
											<li>
												<label>수정자</label>
												<div class="list-item">
													<p class="inline-txt"><c:out value="${serviceInfo.upNm}" /></p>
												</div>
											</li>
											<li>
												<label>등록일시</label>
												<div class="list-item">
													<p class="inline-txt"><fmt:parseDate var="regDate" value="${serviceInfo.regDt}" pattern="yyyyMMddHHmmss" /> <fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy.MM.dd HH:mm" /> <c:out value="${regDt}" /></p>
												</div>
											</li>
											<li>
												<label>수정일시</label>
												<div class="list-item">
													<p class="inline-txt"><fmt:parseDate var="upDate" value="${serviceInfo.upDt}" pattern="yyyyMMddHHmmss" /> <fmt:formatDate var="upDt" value="${upDate}" pattern="yyyy.MM.dd HH:mm" /> <c:out value="${upDt}" /></p>
												</div>
											</li>
										</ul>
									</div>
									<!-- btn-wrap// -->
									<div class="btn-wrap">
										<c:if test="${'Y' eq searchVO.approvalProcAppYn}">
											<button type="button" class="btn big" onclick="goServiceCancel();">닫기</button>
										</c:if>
										<c:if test="${'Y' ne searchVO.approvalProcAppYn}">
											<button type="button" class="btn big fullpurple" onclick="goServiceUpdate('<c:out value='${serviceInfo.status}'/>');">수정</button>
											<button type="button" class="btn big" onclick="goServiceCancel();">취소</button>
										</c:if>
									</div>
									<!-- //btn-wrap -->
								</div>
								<!-- //메일 내용 -->

								<!-- 웹에이전트팝업// -->
								<%@ include file="/WEB-INF/jsp/inc/pop/pop_web_agent_rns.jsp"%>
								<!-- //웹에이전트팝업 -->

								<!-- 발송결재라인정보팝업// -->
								<%@ include file="/WEB-INF/jsp/inc/pop/pop_mail_approval.jsp"%>
								<!-- //발송결재라인정보팝업 -->

							</fieldset>
						</form>

					</section>
					<!-- //cont-body -->

				</div>
				<!-- // content -->
			</div>
		</div>
	</div>

	<iframe id="iFrmService" name="iFrmService" style="width: 0px; height: 0px;"></iframe>


	<!-- RNS테스트메일발송팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_sendtest_user_rns.jsp"%>
	<!-- //테스트메일발송팝업 -->

	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_webagent.jsp"%>
	<!-- //웹에이전트미리보기 팝업 -->

	<!-- 발송결재라인정보팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_rns_approval_info.jsp"%>
	<!-- //발송결재라인정보팝업 -->

	<!-- RNS 발송결재상태팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_rns_approval_state.jsp"%>
	<!-- //발송결재라인정보팝업 -->
	<!-- RNS 준법심의 결과조회팝업 // -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_rns_prohibit_info.jsp"%>
	<!-- //준법심의 결과조회팝업  -->
</body>
</html>
