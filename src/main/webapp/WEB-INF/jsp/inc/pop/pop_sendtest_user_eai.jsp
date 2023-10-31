<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 :  2022.01.26
	*	설명 : 테스트발송 EAI 사용자목록 팝업화면 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_testsend_user_eai" class="poplayer poptestsend">
	<div class="inner">
		<header>
			<h2>테스트메일 발송</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<form id="testEaiUserForm" name="testEaiUserForm" method="post">
					<input type="hidden" id="testEaiTaskNos1" name="eaiTaskNos" value="0">
					<input type="hidden" id="testEaiSubTaskNo1" name="eaiSubTaskNos" value="0">
				
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title">발송 고객 정보</h3>
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn fullblue" onClick="popTestCustList();">검색</button>
							</div>
							<!-- //btn-wrap -->
						</div>
						
						<div class="table-area">
							<table>
								<caption>발송 고객 정보</caption>
								<colgroup>
									<col style="width:90px">
									<col style="width:205px">
									<col style="width:90px">
									<col style="width:205px">
								</colgroup>
								<tbody>
									<tr>
										<th scope="row" >캠페인명</th>
										<td id="popSendTestEaiUserCampNm"></td>
										<th scope="row" >발송 결과</th>
										<td id="popSendTestEaiUserSendResCd"></td> 
									</tr>
									<tr>
										<th scope="row" >ID</th>
										<td id="popSendTestEaiUserIfId"></td>
										<th scope="row" >이메일</th>
										<td id="popSendTestEaiUserIfEmail"></td>
									</tr>
									<tr>
										<th scope="row" >고객명</th>
										<td id="popSendTestEaiUserIfName"></td>
										<th scope="row">연계 생성일</th>
										<td  id="popSendTestEaiUserIfMakeDate"></td>
									</tr>
									<tr>
										<th scope="row">이메일명</th>
										<td colspan="3" id="popSendTestEaiUserTaskNm"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<!-- //발송고객정보 -->
					
					<!-- 수신대상자 추가// -->
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title">테스트메일 수신자</h3>
							<!-- 버튼// -->
							<div class="btn-wrap"><!-- 2021-08-18 인라인 스타일 수정 -->
								<button type="button" class="btn fullblue" onClick="goTestEaiUserAdd();">추가</button>
							</div>
							<!-- //버튼 -->
						</div>
						
						<div class="list-area">
							<ul>
								<li>
									<label>수신자</label>
									<div class="list-item">
										<input type="text" id="testEaiUserNm" name="testEaiUserNm" placeholder="수신자를 입력해주세요.">
									</div>
								</li>
								<li>
									<label>이메일</label>
									<div class="list-item">
										<input type="text" id="testEaiUserEm" name="testEaiUserEm" placeholder="이메일을 입력해주세요.">
									</div>
								</li>
							</ul>
						</div>
					</div> 
				</form>
				<!-- //수신대상자 추가 -->
				
				<!-- 목록// -->
				<form id="listEaiForm" name="listEaiForm" method="post">
				<input type="hidden" id="testEaiTaskNos2" name="eaiTaskNos" value="0">
				<input type="hidden" id="testEaiSubTaskNo2" name="eaiSubTaskNos" value="0">
				<input type="hidden" id="ifId" name="ifId" value="">
				<input type="hidden" id="ifBizkey" name="bizkey" value="">
				<div id="divTestEaiUserList" style="margin-top:30px;"></div>
				</form>
				<!-- //목록 -->

				<!-- btn-wrap// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="goTestEaiMailSend();">테스트발송</button>
					<button type="button" class="btn big" onclick="goTestEaiSendCancel();">취소</button>
				</div>
				<!-- //btn-wrap -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_testsend_user_eai');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
