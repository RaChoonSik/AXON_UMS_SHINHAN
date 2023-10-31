<%@ page import="com.nets.sso.agent.AuthUtil" %>
<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.agent.configuration.SSOConfig" %>
<%@ page import="com.nets.sso.agent.configuration.SSOProvider" %>
<%@ page import="com.nets.sso.agent.configuration.SSOSite" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page import="com.nets.sso.common.enums.AuthStatus" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    //태그명
    String idTagName, pwdTagName, credTagName, returnUrlTagName, siteTagName;

    String ssoLogonUrl;        // 로그온 URL
    String ssoCheckUrl;        // 인증체크 URL
    String returnUrl;          // 되돌아올 URL

    String errorCode;          // 에러코드
    String errorMessage = "";  // 에러 메시지
    String policyVersion;      // 정책 버전
    String siteID;             //사이트 식별자

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
            response.sendRedirect("default.jsp");

        } else if (status == AuthStatus.SSOUnAvailable) {
            // ---------------------------------------------------------------------
            // 인증 상태: 서버 연결 실패
            // - 고객의 정책에 따라 자체 로그인 페이지로 이동 시키거나, SSO 인증을 위한 포탈 로그인 페이지로 이동
            // ---------------------------------------------------------------------
            errorMessage = "SSO service is not available now. Please try again in a few minutes.";
        }
        // 로그온 페이지에서, 인증 서버에 인증 확인하는 스크립트 (HTML의 Header항목에 출력하도록 해야 함)
        // 사용자가 인증을 받았다면, 인증을 수행하도록 Javascript가 동작.
        // 사용자가 인증을 받지 않았다면, 아무런 동작도 없음.
        ssoCheckUrl = auth.getTrySSOScript();

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

        // 사이트 식별자 설정
        // 로그온 UI 페이지로 전달된 사이트 식별자 조회
        siteID = request.getParameter(siteTagName);
        if (Utility.isNullOrEmpty(siteID)) {
            siteID = ssoSite.getId();       // 전달된 식별자가 없으면, 현재 사이트 식별자를 사용
        }
        // 사용자 계정을 SSO에 전달할 인증 서버의 로그온 URL 설정
        ssoLogonUrl = ssoSite.getLogonUrl(request).replace("logonService.do", "wa/logonwa.do");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>NETS*SSO</title>
    <%=ssoCheckUrl%>
    <script type="text/javascript">
        //로그온
        function OnLogon() {
            window.location.href =
                "<%=ssoLogonUrl%>?<%=siteTagName%>=<%=siteID%>&<%=credTagName%>=WINDOWSINTEGRATED&<%=returnUrlTagName%>="
                + encodeURIComponent("<%=returnUrl%>");
        }
    </script>
</head>
<body>
<input type="button" value="윈도우 통합 인증 시도" onclick="OnLogon()"/>
<hr/>
에러 코드:<%=errorCode%>
<hr/>
에러 메시지:<%=errorMessage%>
<hr/>
<a href="./logon.jsp">아이디 비번 로그온</a>
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