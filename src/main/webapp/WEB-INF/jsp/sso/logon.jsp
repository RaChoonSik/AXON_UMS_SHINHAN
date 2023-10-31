<%@ page import="com.nets.sso.agent.AuthUtil" %>
<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.agent.configuration.SSOConfig" %>
<%@ page import="com.nets.sso.agent.configuration.SSOProvider" %>
<%@ page import="com.nets.sso.agent.configuration.SSOSite" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page import="com.nets.sso.common.enums.AuthStatus" %>
<%@ page import="com.nets.sso.common.AgentExceptionCode" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    //태그명
    String idTagName, pwdTagName, credTagName, returnUrlTagName, siteTagName;

    String ssoLogonUrl;        // 로그온 URL
    String returnUrl;          // 되돌아올 URL

    String errorCode;          // 에러코드
    String errorMessage = "";  // 에러 메시지
    String policyVersion;      // 정책 버전
    String siteID;             //사이트 식별자
    String script = "";        // 로그오프 해야 할 경우 사용할 스크립트

    String serverName;
    SSOProvider ssoProvider;
    SSOSite ssoSite;


    //인증객체생성 및 인증확인
    try {
        // 인증 객체 선언(Request와 Response 인계)
        AuthCheck auth = new AuthCheck(request, response);

        serverName = request.getServerName();
        ssoProvider = SSOConfig.getInstance().getCurrentSSOProvider(serverName);
        ssoSite = ssoProvider.getCurrentSSOSite(serverName);

        // 정책 변경 체크 ( 코드 제거하면 안됨 )
        // SSO 서버에서 정책을 새로 배포했을 경우 변경을 감지하여 정책을 다시 받아오도록 하는 코드
        policyVersion = Utility.getRequestValue(request, "policyVersion", Utility.EMPTY_STRING);
        if (!Utility.isNullOrEmpty(policyVersion) && Integer.parseInt(policyVersion) > ssoProvider.getPolicyVer()) {
            SSOConfig.getInstance().reLoad();
            ssoProvider = SSOConfig.getInstance().getCurrentSSOProvider(serverName);
            ssoSite = ssoProvider.getCurrentSSOSite(serverName);
        }

        //일반 설정값들
        idTagName = ssoProvider.getParamName(AuthUtil.ParamInfo.USER_ID);
        pwdTagName = ssoProvider.getParamName(AuthUtil.ParamInfo.USER_PW);
        credTagName = ssoProvider.getParamName(AuthUtil.ParamInfo.CRED_TYPE);
        returnUrlTagName = ssoProvider.getParamName(AuthUtil.ParamInfo.RETURN_URL);
        siteTagName = ssoProvider.getParamName(AuthUtil.ParamInfo.SITE_ID);

        // 리턴 URL 설정 (인증 후 되돌아 올 URL)
        // 로그온 UI 페이지로 전달된 리턴 URL 값 조회
        returnUrl = request.getParameter(returnUrlTagName);
        if (Utility.isNullOrEmpty(returnUrl)) {
            // 리턴 URL 값이 전달되지 않았다면, 기본 URL을 그 값으로 설정
            returnUrl = ssoSite.getDefaultReturnUrl();
            if (Utility.isNullOrEmpty(returnUrl)) {
                // 기본 URL이 없을 경우. 현재 URL을 리턴 URL로 설정(이 부분은 필요 시 수정. ThisURL을 사용해도 무방)
                returnUrl = auth.getThisUrl();
            }
        }

        // 인증 체크(인증 상태 값 리턴)
        AuthStatus status = auth.checkLogon();
        // 인증 체크 후 상세 에러코드 조회
        errorCode = String.valueOf(auth.getErrCode());

        //인증상태별 처리
        if (status == AuthStatus.SSOSuccess) {
            // ---------------------------------------------------------------------
            // 인증 상태: 인증 성공
            // - 인증 토큰(쿠키) 존재하고, 토큰 형식에 맞고, SSO 정책 체크 결과 유효함.
            // ---------------------------------------------------------------------

            // 로그온 UI 페이지가 아닌 업무 페이지로 이동 시킴
            script = "goReturnUrl();";
        } else if (status == AuthStatus.SSOUnAvailable) {
            // ---------------------------------------------------------------------
            // 인증 상태: 서버 연결 실패
            // - 고객의 정책에 따라 자체 로그인 페이지로 이동 시키거나, SSO 인증을 위한 포탈 로그인 페이지로 이동
            // ---------------------------------------------------------------------
            errorMessage = "SSO service is not available now. Please try again in a few minutes.";
            script = "onError('" + errorMessage + "');";
        }
        else if (status.equals(AuthStatus.SSOFirstAccess)) {
            // ---------------------------------------------------------------------
            // 인증 상태: 최초 접근
            // - 다른 사이트에서 이미 인증 했는지 확인하기 위하여, SSO 인증서버로 페이지를 이동시킨다.
            // - 인증 확인 후 현재 페이지로 다시 되돌아 온다.
            // ---------------------------------------------------------------------
            auth.trySSO();
        } else if (status.equals(AuthStatus.SSOFail)) {
            // ---------------------------------------------------------------------
            // 인증 상태: 로그아웃 상태 또는 인증 에러 상태
            // - 에러코드가 '0' 이라면, 로그아웃 상태이며, '0'이 아니라면 오류상태이다.
            // ---------------------------------------------------------------------
            if (auth.getErrCode() == AgentExceptionCode.SessionDuplicationCheckedLastPriority.getValue()) {
                // 중복 로그온 발생 (로그오프 상황)
                errorMessage = "Session was invalidated due to duplicated logon. IP:" + auth.getDuplicationIP() + " Time:" + auth.getDuplicationTime();
                script = "onError('" + errorMessage + "');";
            } else if (auth.getErrCode() == AgentExceptionCode.NoExistUserIDSessionValue.getValue()) {
                // 사용자 인증 세션 부재 (로그오프 상황)
                errorMessage = "The authentication session does not exist.";
                script = "onError('" + errorMessage + "');";
            } else if (auth.getErrCode() == AgentExceptionCode.TokenIdleTimeout.getValue()) {
                // 인증 유휴 시간을 초과 (로그오프 상황)
                errorMessage = "The authentication idle time has been exceeded.";
                script = "onError('" + errorMessage + "');";
            } else if (auth.getErrCode() == AgentExceptionCode.TokenExpired.getValue()) {
                // 인증 기한 만료 (로그오프 상황)
                errorMessage = "The authentication period has expired.";
                script = "onError('" + errorMessage + "');";
            }
        }

        // 사이트 식별자 설정
        // 로그온 UI 페이지로 전달된 사이트 식별자 조회
        siteID = request.getParameter(siteTagName);
        if (Utility.isNullOrEmpty(siteID)) {
            siteID = ssoSite.getId();       // 전달된 식별자가 없으면, 현재 사이트 식별자를 사용
        }
        // 사용자 계정을 SSO에 전달할 인증 서버의 로그온 URL 설정
        ssoLogonUrl = ssoSite.getLogonUrl(request);
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>NETS*SSO - Logon</title>
    <script type="text/javascript">
        //로그온
        function OnLogon() {
            if (document.forms["form1"].txtUserID.value === "") {
                alert("사용자 ID를 입력하세요");
                return;
            }
            if (document.forms["form1"].txtPwd.value === "") {
                alert("사용자의 로그온 비밀번호를 입력하세요");
                return;
            }
            document.forms["form1"].target = "_top";
            document.forms["form1"].submit();
        }

        function goReturnUrl(){
            location.href = '<%=returnUrl%>';
        }

        function onError(){
            //
        }

        function OnInit() {
            document.forms["form1"].txtUserID.focus();
        }
    </script>
</head>
<body onLoad="OnInit();">
<form id="form1" method="post" action="<%=ssoLogonUrl%>">
    <table>
        <tr>
            <td>사용자 ID :</td>
            <td><input type="text" id="txtUserID" name="<%=idTagName%>"/></td>
        </tr>
        <tr>
            <td>비밀번호 :</td>
            <td><input type="password" id="txtPwd" name="<%=pwdTagName%>"/></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="button" value="로그온" onclick="OnLogon();"/></td>
        </tr>
    </table>
    <input type="hidden" id="credType" name="<%=credTagName%>" value="BASIC"/>
    <input type="hidden" name="<%=returnUrlTagName%>" value="<%=returnUrl%>"/>
    <input type="hidden" name="<%=siteTagName%>" value="<%=siteID%>"/>
</form>
<hr/>
에러 코드:<%=errorCode%>
<hr/>
에러 메시지:<%=errorMessage%>
<hr/>
<a href="./logonEnc.jsp">암호화 로그온</a>
<script type="text/javascript">
    <%=script%>
</script>
</body>
</html>
<%
} catch (AgentException e) {
    System.out.println("ErrorCode : " + e.getExceptionCode().toString());
    System.out.println("ErrorMessage : " + e.getMessage());
%>
<%=e.toString()%>
<%
    }
%>