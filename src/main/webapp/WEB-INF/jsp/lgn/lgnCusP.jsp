<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2024.01.26
	*	설명 : 로그인 아이디 비밀번호 입력 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript" src="<c:url value='/js/aes.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/sha256.js'/>"></script>

<script type="text/javascript">
$(document).ready(function() {		
	$("#pUserId").focus();
	$("#pUserId").keypress(function(e) {
		if(e.which == 13) {
			e.preventDefault();
			if($("#pUserId").val() == "") {
				alert("아이디를 입력해주세요.");
			} else {
				$("#pUserPwd").focus();
			}
		}
	});
	
	$("#pUserPwd").keypress(function(e) {
		if(e.which == 13) {
			e.preventDefault();
			if($("#pUserPwd").val() == "") {
				alert("비밀번호를  입력해주세요.");
			} else {
				$("#answer").focus();
			}
		}
	});
	
	$("#answer").keypress(function(e) {
		if(e.which == 13) {
			e.preventDefault();
			if($("#answer").val() == "") {
				alert("보안문자를  입력해주세요.");
			} else {
				goLogin();
			}
		}
	});
	
	
	$("#lgnBtn").click(function(e) {
		goLogin();
	});
	
});

function goLogin() {

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

	$("#eUserId").val(encUserId);
	$("#eUserPwd").val(encUserPwd);
	
	var param = $("#loginForm").serialize();
	
	$.ajax({
		type : "POST",
		//SP 추가시 
		url : "../lgn/checkInitPwd.json?" + param, 
		//url : "/lgn/checkInitPwd.json?" + param,
		dataType : "json",
		async:false,
		success : function(data){
			if(data.result == "PwdInit") {
				$("#needUserId").val($("#pUserId").val());
				fn.popupOpen("#popup_user_editpassword");
			} else if(data.result == "Reset") {
				alert(data.msg);
				$("#needUserId").val($("#pUserId").val());
				fn.popupOpen("#popup_user_editpassword");
			} else if (data.result == "Limit") {
				const btnAuth = document.getElementById('btnAuthMail');
				btnAuth.style.display = 'block';
				alert(data.msg);
			} else if (data.result == "Success") {
				$("#loginForm").submit();
			} else  {
				alert(data.msg);
				//getImage();
				$("#answer").val("");
			}
		},
		error: function (e) {
			alert("로그인 처리에 오류가 발생했습니다");
			//getImage();
			$("#answer").val("");
		}
	});
}

function popSaveInitPasswordChange(){
	var pUserId =$("#pUserId").val();
	var pUserPwd =$("#pUserPwd").val();
	
	if( popCheckInitPasswordChange(pUserId,pUserPwd)){  
		var encUserId = CryptoJS.AES.encrypt($("#needUserId").val(), "!END#ERSUMS");
		var encUserPwd = CryptoJS.AES.encrypt($("#popUserEditPwd").val(), "!END#ERSUMS");
		
		$("#popUserEditPwdUserId").val(encUserId);
		$("#popUserEditPwdPassword").val(encUserPwd);
		
		var param = $("#popUserInitPwdForm").serialize();
		console.log(param);
		
		$.ajax({
			type : "POST",
			url : "/sys/acc/userUpdateInitPassword.json?" + param,
			dataType : "json",
			success : function(data){
				if(data.result == "Success") {
					alert(data.message); 
					$("#needUserId").val(""); 
					$("#pUserPwd").val("");
					fn.popupClose('#popup_user_editpassword'); 
					//$("#answer").val("");
					//getImage();
				} else {
					alert(data.message);
					$("#popUserEditPwd").focus();
					$("#popUserEditPwd").select();
				}
			},
			error: function (e) {
				alert("비밀번호 변경 처리에 오류가 발생했습니다");
			}
		});
		
		
	}
}
function goAuthMail(){
	var encUserId = CryptoJS.AES.encrypt($("#pUserId").val(), "!END#ERSUMS");
	var encUserPwd = CryptoJS.AES.encrypt($("#pUserPwd").val(), "!END#ERSUMS");

	$("#eUserId").val(encUserId);
	$("#eUserPwd").val(encUserPwd);
	
	var param = $("#loginForm").serialize();
	
	$.ajax({
		type : "POST",
		url : "/lgn/userAuthSendMail.json?" + param,
		dataType : "json",
		success : function(data){
			if(data.result == "Success") {
				alert(data.message);
			} else  {
				alert(data.message);
			}
		},
		error: function (e) {
			alert("본인 인증 처리에 오류가 발생했습니다");
		}
	});
}


</script>

<body>
	<div id="wrap">

		<!-- login// -->
		<div id="login">
			<form id="loginForm" name="loginForm" action="<c:url value='/lgn/lgn.ums'/>" method="post">
				<fieldset>
					<legend>로그인</legend>
					<section class="login-inner">
						<h1 class="ttl">신한 EZ 손해보험 이메일 발송 시스템</h1>
						<%-- <h1><img src="<c:url value='/img/common/logo_white.png'/>" class="logo" alt="AXon UMS"></h1> --%>
						<div class="form-box">				
							<label for="pUserId">아이디</label>		
							<input tabindex="1" type="text" id="pUserId" placeholder="아이디를 입력하세요.">
							<label for="pUserPwd">비밀번호</label>
							<input tabindex="2" type="password" id="pUserPwd" placeholder="비밀번호를 입력하세요.">
							<input type="hidden" id="eUserId" name="pUserId" placeholder="아이디를 입력하세요.">
							<input type="hidden" id="eUserPwd" name="pUserPwd" placeholder="비밀번호를 입력하세요.">
							<i class="fa fa-eye fa-lg"></i>
							<div class="error">
								<c:if test="${'N' eq result}">
									<p>*  계정명(ID) 또는 비밀번호를 잘못 입력하셨습니다. 입력하신 정보를 확인하십시오.</p>	
								</c:if>
								<c:if test="${'E' eq result}">
									<p>* 유효하지 않은 사용자입니다. 관리자에게 문의해주세요.</p>	
								</c:if>
							</div>
							
							<a tabindex="4" href="javascript:goLogin();" class="btn blue login">로그인</a>
							<button type="button" class="btn big" onclick="goAuthMail();" id="btnAuthMail" style="display: none;margin-top: 2rem;">본인인증</button>
							<br/><bf/>							
						</div>	
					</section>
					<p class="copyright">Copyright URACLE CORPORATION. ALL RIGHTS RESERVED.</p>
				</fieldset>
			</form>
		</div>
		<!-- // login -->

	</div>
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_user_password.jsp" %>
</body>
</html>
