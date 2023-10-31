<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.10.21
	*	설명 : 관리자의 경우 시스템서비스 접근시 아이디 비밀번호 확인 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

	<div id="popup_sys_user_auth" class="poplayer popeditpw"><!-- id값 수정 가능 -->
		
		<div class="inner small">
			<header>
				<h2>관리자 인증</h2>
			</header>
			<form id="popSysUserAuth" name="popSysUserAuth" method="post">
				<input type="hidden" id="sysUserId" name="pUserId">
				<input type="hidden" id="sysUserPwd" name="pUserPwd">

				<div class="popcont">
					<div class="cont"> 
						<ul>
							<li>
								<label>아이디</label>
								<div>
									<input tabindex="1" type="text" id="pUserId" placeholder="아이디를 입력해주세요.">
								</div>
							</li>
							<li>
								<label>비밀번호</label>
								<div>
									<input tabindex="2" type="password" id="pUserPwd" placeholder="비밀번호를 입력해주세요." onkeypress="if(event.keyCode==13) {goSysUserAuth(); return false;}">
								</div>
							</li>
						</ul>
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onclick="goSysUserAuth();">관리자 인증</button>
						</div>
						<!-- //버튼 -->
					</div>
				</div>
			</form>
			<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_sys_user_auth');"><span class="hidden">팝업닫기</span></button>
		</div>
		<span class="poplayer-background"></span>
	</div>