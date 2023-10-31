<%@ page import="com.nets.sso.agent.AuthUtil" %>
<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page import="com.nets.sso.common.enums.AuthStatus" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String jsonData;
    String errorCode = Utility.getRequestValue(request, "errorCode", Utility.EMPTY_STRING);
    String errorMessage = Utility.getRequestValue(request, "errorMessage", Utility.EMPTY_STRING);
    if (!Utility.isNullOrEmpty(errorCode)) {
        jsonData = AuthUtil.setSpaJsonError(errorCode, errorMessage, null);
    } else {
        try {
            AuthCheck authCheck = new AuthCheck(request, response);
            boolean logonYN = authCheck.logon(false);
            if (logonYN)
                jsonData = AuthUtil.setSpaJsonLogon(authCheck, AuthStatus.SSOSuccess, true);
            else
                jsonData = AuthUtil.setSpaJsonError(errorCode, errorMessage, AuthStatus.SSOFail);
        } catch (AgentException ae) {
            jsonData = AuthUtil.setSpaJsonError(ae.getExceptionCode().toString(), errorMessage, AuthStatus.SSOFail);
        }
    }
%><%=jsonData%>
