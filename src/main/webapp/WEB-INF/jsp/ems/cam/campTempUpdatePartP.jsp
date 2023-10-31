<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.09.27
	*	설명 : 캠페인템플릿 부분수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp"%>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/ems/cam/campTempUpdatePartP.js'/>"></script>
<script type="text/javascript">
$(document).ready(function() {
	setTimeout(function(){
		setCampTempContent("<c:out value='${campTempInfo.contentsTyp}'/>","<c:out value='${campTempInfo.contentsPath}'/>");
	},1000);
});
campTempStatus = "<c:out value='${campTempInfo.status}'/>";
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
							<h2>캠페인템플릿 정보수정</h2>
						</div>
					</section>
					<!-- //cont-head -->

					<!-- cont-body// -->
					<section class="cont-body">
						<form id="searchForm" name="searchForm" method="post">
							<input type="hidden"                   name="page"              value="<c:out value='${searchVO.page}'/>">
							<input type="hidden"                   name="searchStartDt"     value="<c:out value='${searchVO.searchStartDt}'/>">
							<input type="hidden"                   name="searchEndDt"       value="<c:out value='${searchVO.searchEndDt}'/>">
							<input type="hidden"                   name="searchTnm"         value="<c:out value='${searchVO.searchTnm}'/>">
							<input type="hidden"                   name="searchDeptNo"      value="<c:out value='${searchVO.searchDeptNo}'/>">
							<input type="hidden"                   name="searchUserId"      value="<c:out value='${searchVO.searchUserId}'/>">
							<input type="hidden"                   name="searchCampNo"      value="<c:out value='${searchVO.searchCampNo}'/>">
							<input type="hidden"                   name="searchStatus"      value="<c:out value='${searchVO.searchStatus}'/>">
							<input type="hidden"                   name="tid"               value="<c:out value='${campTempInfo.tid}'/>">
							<input type="hidden"                   name="tids"              value="<c:out value='${campTempInfo.tid}'/>">
							<input type="hidden" id="status"       name="status"            value="<c:out value='${campTempInfo.status}'/>">
						</form>
				
						<form id="campTempInfoForm" name="campTempInfoForm" method="post">
							<input type="hidden" id="tid"             name="tid"            value="<c:out value='${campTempInfo.tid}'/>">
							<input type="hidden" id="tids"            name="tids"           value="<c:out value='${campTempInfo.tid}'/>">
							<input type="hidden" id="contentsPath"                          value="<c:out value='${campTempInfo.contentsPath}'/>">
							<input type="hidden" id="secuAttTyp"                            value="<c:out value='${webAgent.secuAttTyp}'/>">
							
							<!--  고객정보 Check-->
							<input type="hidden" id="titleChkYn"      name="titleChkYn"       value="<c:out value='${campTempInfo.titleChkYn}'/>">
							<input type="hidden" id="bodyChkYn"       name="bodyChkYn"        value="<c:out value='${campTempInfo.bodyChkYn}'/>">
							<input type="hidden" id="attachFileChkYn" name="attachFileChkYn"  value="<c:out value='${campTempInfo.attachFileChkYn}'/>">
							
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
											<span class="required" style="margin-right: 0;">*수정 항목</span>
										</div>
										<!-- //버튼 -->
									</div>

									<div class="list-area">
										<ul>
											<li>
												<label>캠페인</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray"><c:out value="${campTempInfo.campNm}"/></p>
													</div>
												</div>
											</li>
											<li class="col-full">
												<label>연계 탬플릿 번호</label>
												<div class="list-item">
													<p class="label bg-gray"><c:out value="${campTempInfo.eaiCampNo}"/></p>
												</div>
											</li>
											<%-- 관리자의 경우 전체 요청부서를 전시하고 그 외의 경우에는 숨김 --%>
											<c:if test="${'Y' eq NEO_ADMIN_YN}">
												<li>
													<label>사용자그룹</label>
													<div class="list-item">
														<p class="label bg-gray"><c:out value="${campTempInfo.deptNm}"/></p>
													</div>
												</li>
												<li>
													<label>사용자명</label>
													<div class="list-item">
														<p class="label bg-gray"><c:out value="${campTempInfo.userNm}"/></p>
													</div>
												</li>
											</c:if>
											<li class="col-full">
												<label class="required">발송자명</label>
												<div class="list-item">
													<input type="hidden" name="sid" value="<c:out value='${campTempInfo.sid}'/>">
													<input type="text" name="sname" value="<c:out value='${campTempInfo.sname}'/>">
												</div>
											</li>
											<li class="col-full">
												<label class="required">발송자 이메일</label>
												<div class="list-item">
													<input type="text" name="smail" value="<crypto:decrypt colNm='SMAIL' data='${campTempInfo.smail}'/>">
												</div>
											</li>	
											<li>
												<label>웹에이전트</label>
												<div class="list-item">
													<div class="filebox" style="width: 100%;">
														<p class="label bg-gray" style="width: 230px;">
															<c:if test="${not empty webAgent}">
																<c:if test="${'Y' eq webAgent.secuAttYn}">첨부파일로 지정되었습니다.(<c:out value="${webAgent.secuAttTyp}" />)</c:if>
																<c:if test="${'N' eq webAgent.secuAttYn}">본문삽입으로 지정되었습니다.</c:if>
															</c:if>
															<c:if test="${empty webAgent}">형식이 지정되지 않았습니다.</c:if>
														</p>
														<button type="button" class="btn fullblue" onclick="popWebAgentPreview();">미리보기</button>
													</div>
												</div>
											</li>	
											<li>
												<label>금칙어</label>
												<div class="list-item">
													<div class="filebox">
														<a href="javascript:goPopProbibitInfo('<c:out value='${campTempInfo.tid}'/>');"><c:out value='${campTempInfo.prohibitDesc}' /></a>
													</div>
												</div>
											</li>
											<li>
												<label  class="required">고객정보체크</label>
												<div class="list-item">
													<c:if test="${'Y' eq envSetAuth}">
														<label><input type="checkbox" name="infoCheckYn" <c:if test="${'Y' eq campTempInfo.titleChkYn}"> checked </c:if>><span>메일 제목</span></label>
														<label><input type="checkbox" name="infoCheckYn" <c:if test="${'Y' eq campTempInfo.bodyChkYn}"> checked </c:if>><span>메일 본문</span></label>
														<label><input type="checkbox" name="infoCheckYn" <c:if test="${'Y' eq campTempInfo.attachFileChkYn}"> checked </c:if>><span>일반 첨부파일</span></label>
													</c:if>
													<c:if test="${'Y' ne envSetAuth}">
														<label><input type="checkbox" name="infoCheckYn" disabled <c:if test="${'Y' eq campTempInfo.titleChkYn}"> checked </c:if>><span>메일 제목</span></label>
														<label><input type="checkbox" name="infoCheckYn" disabled <c:if test="${'Y' eq campTempInfo.bodyChkYn}"> checked </c:if>><span>메일 본문</span></label>
														<label><input type="checkbox" name="infoCheckYn" disabled <c:if test="${'Y' eq campTempInfo.attachFileChkYn}"> checked </c:if>><span>일반 첨부파일</span></label>
													</c:if>
												</div>
											</li>
											<li>
												<label>수신자그룹</label>
												<div class="list-item">
													<div class="filebox">
														<c:set var="segNoc" value="" />
														<c:forEach items="${segList}" var="seg">
															<c:if test="${seg.segNo == mailInfo.segNo}">
																<c:set var="segNm" value="${seg.segNm}" />
																<c:set var="segNoc" value="${seg.segNo}|${seg.mergeKey}" />
															</c:if>
														</c:forEach>
														<p class="label bg-gray" style="width: calc(100% - 14.2rem);">
															<c:out value="${mailInfo.segNm}" />
														</p>
														<input type="hidden" id="segNoc" name="segNoc" value="<c:out value="${segNoc}"/>">
														<button type="button" class="btn fullblue" onclick="goSegInfoMail('');">미리보기</button>
													</div>
												</div>
											</li>
											<li class="col-full"> 
												<label>템플릿명</label>
												<div class="list-item">
													<p class="label bg-gray"><c:out value="${campTempInfo.tnm}"/></p>
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
											<c:if test="${'002' ne campTempInfo.status}">
												<button type="button" class="btn<c:if test="${'001' eq campTempInfo.status}"> hide</c:if>" id="btnDisable" onclick="goDisable();">사용중지</button>
												<button type="button" class="btn<c:if test="${'000' eq campTempInfo.status}"> hide</c:if>" id="btnEnable" onclick="goEnable();">복구</button>
												<button type="button" class="btn" onclick="goDelete();">삭제</button>
												<button type="button" class="btn" onclick="goCopy();">복사</button>
											</c:if>
											
										</div>
										<!-- //버튼 -->
									</div>

									<div class="list-area">
										<ul>
											<li>
												<label>메일 제목</label>
												<div class="list-item">
													<div class="filebox">
														<p class="label bg-gray">
															<c:out value='${campTempInfo.emailSubject}' />
														</p>
													</div>
												</div>
											</li>
											<li class="col-full">
												<!-- 에디터 영역// -->
												<div class="editbox" id="serviceContents">
												</div>
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
										<c:if test="${'002' ne campTempInfo.status}">
											<button type="button" class="btn big fullblue" onclick="goUpdate();">수정</button>
											<button type="button" class="btn big" onclick="goCancel();">취소</button>
										</c:if>
										<c:if test="${'002' eq campTempInfo.status}">
											<button type="button" class="btn big" onclick="goCancel();">목록</button>
										</c:if>
									</div>
									<!-- //btn-wrap -->
								</div>
								<!-- //메일 내용 -->
 
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
	
	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_web_agent.jsp"%>
	<!-- //웹에이전트미리보기 팝업 -->

	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_webagent.jsp"%>
	<!-- //웹에이전트미리보기 팝업 -->
	
	<!-- 준법심의 결과조회팝업 // -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_prohibit_info.jsp"%>
	<!-- //준법심의 결과조회팝업  -->
	
</body>
</html>
