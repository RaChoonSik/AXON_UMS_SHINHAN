<%@ page import="com.nets.sso.agent.AuthUtil" %>
<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.enums.AuthStatus" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String jsonData;
    try {
        AuthCheck authCheck = new AuthCheck(request, response);
        AuthStatus status = authCheck.checkLogon();
        jsonData = AuthUtil.setSpaJsonLogon(authCheck, status, false);
    } catch (AgentException ae) {
        jsonData = AuthUtil.setSpaJsonError(ae.getExceptionCode().toString(), ae.getMessage(), AuthStatus.SSOFail);
    }
%><%=jsonData%>
