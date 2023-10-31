<%@ page import="com.nets.sso.agent.AuthUtil" %>
<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.agent.authcheck.TFAReceiver" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.enums.AuthStatus" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String jsonData;
    try {
        String errorMessage;
        AuthCheck auth = new AuthCheck(request, response);
        TFAReceiver tfaReceiver = new TFAReceiver(auth);
        if (!tfaReceiver.getEnable()) {
            errorMessage = "NETS*SSO TFA is disabled.";
            jsonData = AuthUtil.setSpaJsonError("11100023", errorMessage, null);
        } else {
            if (!auth.getSsoProvider().getTfa().getTwoStep()) {
                errorMessage = "2-step process of NETS*SSO TFA is disabled.";
                jsonData = AuthUtil.setSpaJsonError("11100004", errorMessage, null);
            } else {
                if (!tfaReceiver.getTargetYN()) {
                    errorMessage = tfaReceiver.getUserID() + " is not user who must be authenticated with TFA.";
                    jsonData = AuthUtil.setSpaJsonError("13100003", errorMessage, null);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("{")
                            .append("\"result\": true,")
                            .append("\"errorCode\": 0,")
                            .append("\"userID\": \"").append(tfaReceiver.getUserID()).append("\",")
                            .append("\"tfaID\": \"").append(tfaReceiver.getTfaID()).append("\",")
                            .append("\"targetYN\": ").append(String.valueOf(tfaReceiver.getTargetYN()).toLowerCase()).append(",")
                            .append("\"device\": \"").append(tfaReceiver.getDevice()).append("\",")
                            .append("\"code\": \"").append(tfaReceiver.getCode()).append("\",")
                            .append("\"method\": \"").append(tfaReceiver.getMethod()).append("\",")
                            .append("\"timeoutMinutes\": ").append(auth.getSsoProvider().getTfa().getTimeoutMinutes())
                            .append("}");
                    jsonData = sb.toString();
                }
            }
        }
    } catch (AgentException ae) {
        jsonData = AuthUtil.setSpaJsonError(ae.getExceptionCode().toString(), ae.getMessage(), AuthStatus.SSOFail);
    }
%><%=jsonData%>