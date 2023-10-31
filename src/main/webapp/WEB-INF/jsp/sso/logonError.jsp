<%@ page import="com.nets.sso.agent.AuthUtil" %>
<%@ page import="com.nets.sso.agent.configuration.SSOConfig" %>
<%@ page import="com.nets.sso.agent.configuration.SSOProvider" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.common.LiteralConst" %>
<%@ page import="com.nets.sso.common.AgentExceptionCode" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    try {
        AuthCheck auth = new AuthCheck(request, response);

        SSOProvider ssoProvider = SSOConfig.getInstance().getCurrentSSOProvider(request.getServerName());
        String errorCode = Utility.getRequestValue(request, "errorCode", Utility.EMPTY_STRING);
        String errorMessage = Utility.getRequestValue(request, "errorMessage", Utility.EMPTY_STRING);
        String siteID = Utility.getRequestValue(request, ssoProvider.getParamName(AuthUtil.ParamInfo.SITE_ID), Utility.EMPTY_STRING);
        String returnUrl = Utility.getRequestValue(request, ssoProvider.getParamName(AuthUtil.ParamInfo.RETURN_URL), Utility.EMPTY_STRING);
        String logoffUrl = auth.getSsoSite().getLogoffUrl(request) + "?" +
                auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.SITE_ID) + "=" +
                auth.getSsoSite().getId() + "&" +
                auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.RETURN_URL) + "=" +
                Utility.encodeUrl(returnUrl, LiteralConst.UTF_8);       // ThisURL은 사용자가 현재 호출한 페이지의 URL
        String actionUrl;
        if (errorCode.equals(AgentExceptionCode.SessionDuplicationCheckedLastPriority.toString()) ||
                errorCode.equals(AgentExceptionCode.NoExistUserIDSessionValue.toString()) ||
                errorCode.equals(AgentExceptionCode.TokenIdleTimeout.toString()) ||
                errorCode.equals(AgentExceptionCode.TokenExpired.toString()))
            actionUrl = logoffUrl;
        else
            actionUrl = returnUrl;
%>
<!DOCTYPE html>
<html>
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title></title>
</head>
<body>
<p>ErorCode: <%=errorCode %></p>
<p>ErorMessage: <%=errorMessage%></p>
<p>SITE ID: <%=siteID%></p>
<p>Return URL: <%=returnUrl%></p>
<input type="button" value="OK" onclick="location.href='<%=actionUrl%>'"/>
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