<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.agent.authcheck.DupCheck" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.AgentExceptionCode" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String jsonData;
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    try {
        AuthCheck authCheck = new AuthCheck(request, response);
        DupCheck dupCheck = new DupCheck(authCheck, request, response);
        dupCheck.processCancel();
        sb
                .append("\"result\": true,")
                .append("\"errorCode\": 0,")
                .append("\"errorMessage\": \"" + Utility.EMPTY_STRING + "\"");
    } catch (AgentException ae) {
        String errorMessage = Utility.isNullOrEmpty(ae.getMessage()) ? "Failed to cancel duplication logon." : ae.getMessage();
        sb
                .append("\"result\": false,")
                .append("\"errorCode\": " + ae.getExceptionCode().getValue() + ",")
                .append("\"errorMessage\": \"" + errorMessage + "\"");
    } catch (Exception ex) {
        String errorMessage = Utility.isNullOrEmpty(ex.getMessage()) ? "Failed to cancel duplication logon." : ex.getMessage();
        sb
                .append("\"result\": false,")
                .append("\"errorCode\": " + AgentExceptionCode.NotDefinedException.getValue() + ",")
                .append("\"errorMessage\": \"" + errorMessage + "\"");
    }
    sb.append("}");
    jsonData = sb.toString();
%><%=jsonData%>
