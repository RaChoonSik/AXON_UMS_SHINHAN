<%@ page import="com.nets.sso.agent.AuthUtil" %>
<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.AgentExceptionCode" %>
<%@ page import="com.nets.sso.common.LiteralConst" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page import="com.nets.sso.common.enums.AuthStatus" %>
<%@ page import="java.util.Enumeration" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String logoffUrl;               // 로그오프 URL
    String userId = "";             // 사용자 아이디
    String userAttributes = "";     // 사용자 속성
    String errorCode;               // 에러 코드
    String errorMessage = "";       // 에러 메시지
    String script = "";             // 로그오프 해야 할 경우 사용할 스크립트

    try {
        // 인증 객체 선언(Request와 Response 인계)
        AuthCheck auth = new AuthCheck(request, response);
        // 인증 체크(인증 상태 값 리턴)
        AuthStatus status = auth.checkLogon();
        // 인증 체크 후 상세 에러코드 조회
        errorCode = String.valueOf(auth.getErrCode());

        // 로그오프 URL 설정
        logoffUrl = auth.getSsoSite().getLogoffUrl(request) + "?" +
                auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.SITE_ID) + "=" +
                auth.getSsoSite().getId() + "&" +
                auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.RETURN_URL) + "=" +
                Utility.encodeUrl(auth.getThisUrl(), LiteralConst.UTF_8);       // ThisURL은 사용자가 현재 호출한 페이지의 URL

        // 로그온 URL 설정
        String logonUrl = "logon.jsp?" +
                auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.RETURN_URL) + "=" +
                Utility.encodeUrl(auth.getThisUrl(), LiteralConst.UTF_8);

        //인증상태별 처리
        if (status == AuthStatus.SSOSuccess) {
            // ---------------------------------------------------------------------
            // 인증 상태: 인증 성공
            // - 인증 토큰(쿠키) 존재하고, 토큰 형식에 맞고, SSO 정책 체크 결과 유효함.
            // ---------------------------------------------------------------------

            // 사용자 아이디 추출
            userId = auth.getUserID();
            // 사용자 속성 모두 조회(전달되는 사용자 속성을 보여주기 위한 코드. 프로젝트 개발 시 필요 없으면 제거)
            if (auth.getUserInfoCollection() != null && auth.getUserInfoCollection().size() > 0) {
                for (Enumeration<String> e = auth.getUserInfoCollection().keys(); e.hasMoreElements(); ) {
                    if (!Utility.isNullOrEmpty(userAttributes))
                        userAttributes += "<br />";
                    String key = e.nextElement();
                    userAttributes += key + ":" + auth.getUserInfoCollection().get(key);
                }
            }

            // 사용자 속성 중 특정 사용자 속성 값 조회(사용자 이름, 조직 코드 등. 필요 없다면 제거)
            String somethingUserAttribute = auth.getUserInfo("AttributeName");
        } else if (status == AuthStatus.SSOFirstAccess) {
            // ---------------------------------------------------------------------
            // 인증 상태: 최초 접근
            // - 인증 토큰(쿠키)가 존재하지 않음.
            // ---------------------------------------------------------------------

            // 인증 확인을 위해 페이지 이동

            // ThisURL을 이용하여 현재 페이지로 다시 돌아오도록 함.
            // authCheck.TrySSO("되돌아올URL");
            // 직접 되돌아 올 URL을 직접 입력하여 호출 할 수도 있음
            auth.trySSO();

        } else if (status == AuthStatus.SSOFail) {
            // ---------------------------------------------------------------------
            // 인증 상태 : 인증 실패 또는 로그오프 상태
            // - 인증 오류 발생 또는 로그온 하지 않은 로그오프 상태
            // ---------------------------------------------------------------------

            // 상태 구분은 ErrorCode로 판별
            if (auth.getErrCode() == AgentExceptionCode.NoException.getValue()) {
                // 오류없음: 로그오프 상태 -> 로그온 페이지 이동
                response.sendRedirect(logonUrl);
            }
            // 인증 오류가 발생
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
        } else if (status == AuthStatus.SSOUnAvailable) {
            // ---------------------------------------------------------------------
            // 인증 상태: 서비스 불가
            // - 네트워크 장애 또는 DB or SSO 서버 정지 등
            // ---------------------------------------------------------------------

            // SSO 로그온이 아닌 로컬 로그온을 유도
            response.sendRedirect(logonUrl);
        }
%>
<!DOCTYPE html>
<html>
<head>
    <title>NETS*SSO</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        function onError(msg) {
            alert(msg);
            location.href = "<%=logoffUrl%>";
        }
    </script>
</head>
<body>
<% if (userId == null || userId.length() == 0) {%>
<a href="./logon.jsp">Logon</a>
<% } else { %>
사용자 계정:<%=userId %>
<hr/>
사용자 속성<br/>
<%=userAttributes%>
<hr/>
<a href="<%=logoffUrl%>">Logoff</a>
<% } %>
<hr/>
에러 코드:<%=errorCode%>
<hr/>
에러 메시지:<%=errorMessage%>
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