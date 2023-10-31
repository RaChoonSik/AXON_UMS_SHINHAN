<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.05
	*	설명 : 상단 메뉴 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp"%>
<script type="text/javascript" src="<c:url value='/js/sys.js'/>"></script>
<script type="text/javascript">
	function goService() {
		document.location.href = "<c:url value='/service.ums'/>";
	}

	function goEms() {
		document.location.href = "<c:url value='/ems/index.ums'/>";
	}

	function goRns() {
		document.location.href = "<c:url value='/rns/index.ums'/>";
	}

	function goSms() {
		alert("준비중");
	}

	function goPush() {
		alert("준비중");
	}

	function goSys(useSys) {
		if (useSys == "N") {
			alert("관리자 기능 사용 권한이 없습니다");
			return;
		} else {
			//popSysUserAuth();
			document.location.href = "<c:url value='/sys/index.ums'/>";
		}
	}
	
	function popSysUserAuth(){
		$("#pUserId").val("");
		$("#pUserPwd").val("");
		$("#eUserId").val("");
		$("#eUserPwd").val("");
		fn.popupOpen("#popup_sys_user_auth");
	}
	
	function goLogout() {
		document.location.href = "<c:url value='/lgn/logout.ums'/>";
	}

	function goUserInfo(userId) {
		popUserInfo(userId);
	}
	/*
	function goUMS() {
		document.location.href = "<c:url value='http://ums.lottegrs.com:8080/loginSSO'/>";
	}
	*/
	
	
	/********************************************************
	 * 시스템 관리자 인증 
	 ********************************************************/
	function goSysUserAuth(){
		if($("#pUserId").val() == "") {
			alert("아이디를 입력해주세요.");
			$("#pUserId").focus();
			return;
		} 
		if($("#pUserPwd").val() == "") {
			alert("비밀번호를 입력해주세요.");
			$("#pUserPwd").focus();
			return;
		}
		
		var encUserId = CryptoJS.AES.encrypt($("#pUserId").val(), "!END#ERSUMS");
		var encUserPwd = CryptoJS.AES.encrypt($("#pUserPwd").val(), "!END#ERSUMS");

		$('#sysUserId').val(encUserId);
		$('#sysUserPwd').val(encUserPwd);
		
		var param = $("#popSysUserAuth").serialize();
		
		$.ajax({
			type : "POST",
			url : "/lgn/goSysUserAuth.json?" + param,
			dataType : "json",
			async:false,
			success : function(data){
				if (data.result == "Success") {
					document.location.href = "<c:url value='/sys/index.ums'/>";
				} else  {
					alert(data.msg);
				}
			},
			error: function (e) {
				alert("관리자 인증 처리에 오류가 발생했습니다");
			}
		});
		
	}

</script>
<div class="util">
	<div class="header-right">
		<div class="user-info-box info">
			<span class="grade"><c:out value='${NEO_DEPT_NM}' /></span>
			<div class="drop-menu user">
				<button type="button" class="info">
					<c:if test="${NEO_USE_SYS ne 'N' }">
					<c:out value='${NEO_USER_NM}' /> / 최종접속정보 : <c:out value='${NEO_LOGIN_DT}' /> (<c:out value='${NEO_USER_IP}'/>)					
					</c:if>
					<c:if test="${NEO_USE_SYS eq 'N' }">
					<c:out value='${NEO_USER_NM}' />
					</c:if>
				</button>
				<ul class="drop-btn-list link">
					<!-- 
					<li>
						<button type="button" onclick="goUMS()">UMS 바로가기</button>
					</li>
					--> 
					<li>
						<button type="button" onclick="goUserUpdate()">개인 정보수정</button>
					</li>
					<c:set var="uri" value="${pageContext.request.requestURI}" />
					<c:if test="${NEO_USE_SYS ne 'N' }">
						<c:if test = "${!fn:contains(uri, '/sys/')}">
						<li>
							<button type="button" onclick="goSys('${NEO_USE_SYS}')">공통설정</button>
						</li>
						</c:if>
					</c:if>
					<c:if test="${NEO_USE_EMS eq 'Y'}">
						<c:if test = "${!fn:contains(uri, '/ems/')}">
						<li>
							<button type="button" onclick="goEms('${NEO_USE_SYS}')">메일발송관리</button>
						</li>
						</c:if>
					</c:if>
					<li>
						<button type="button" onclick="goLogout()">로그아웃</button>
					</li>
				</ul>
			</div>
		</div>
		<%@ include file="/WEB-INF/jsp/inc/pop/pop_user_info.jsp"%>
		<%@ include file="/WEB-INF/jsp/inc/pop/pop_sys_user_auth.jsp"%>
	</div>
</div>
