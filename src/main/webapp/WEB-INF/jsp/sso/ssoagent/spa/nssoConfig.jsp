<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.agent.configuration.SSOProvider" %>
<%@ page import="com.nets.sso.agent.configuration.SSOSite" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String jsonData;
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    try {
        AuthCheck authCheck = new AuthCheck(request, response);
        SSOProvider ssoProvider = authCheck.getSsoProvider();
        SSOSite ssoSite = authCheck.getSsoSite();
        String logonUrl = ssoSite.getLogonUrl(request);
        String logoffUrl = ssoSite.getLogoffUrl(request);
        String ssoServiceUrl = ssoSite.getSSOServiceUrl(request);
        sb
                .append("\"result\": true,")
                .append("\"ssosite\": \"").append(ssoSite.getId()).append("\",")
                .append("\"defaultUrl\": \"").append(ssoSite.getDefaultReturnUrl()).append("\",")
                .append("\"urlSSOLogonService\": \"").append(logonUrl).append("\",")
                .append("\"urlSSOLogonServiceAfter\": \"").append(Utility.replaceAll(logonUrl, ssoProvider.getLogonServicePage(), "logonServiceAfter.do")).append("\",")
                .append("\"urlSSOCheckService\": \"").append(ssoServiceUrl).append("\",")
                .append("\"urlSSOLogoffService\": \"").append(logoffUrl).append("\"");
    } catch (AgentException ae) {
        sb
                .append("\"result\": false,")
                .append("\"errorCode\": ").append(ae.getExceptionCode().getValue()).append(",")
                .append("\"errorMessage\": \"").append(ae.getMessage()).append("\"");
    }
    sb.append("}");
    jsonData = sb.toString();
%><%=jsonData%>
