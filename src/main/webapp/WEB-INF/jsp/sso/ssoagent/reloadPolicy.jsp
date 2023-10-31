<%@ page import="com.nets.sso.agent.configuration.SSOConfig" %>
<%@ page import="com.nets.sso.agent.configuration.SSOProvider" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    int prePolicyVer = 0;
    int postPolicyVer = 0;
    try {
        prePolicyVer = SSOConfig.getInstance().getCurrentSSOProvider(request.getServerName()).getPolicyVer();
    } catch (Exception e) {
    }
    SSOConfig.getInstance().reLoad();
    SSOProvider ssoProvider = SSOConfig.getInstance().getCurrentSSOProvider(request.getServerName());
    if (null == ssoProvider) {
%>
Can not find configuration for '<%=request.getServerName()%>'.
<%
} else {
    postPolicyVer = SSOConfig.getInstance().getCurrentSSOProvider(request.getServerName()).getPolicyVer();
%>
policy version:<%=prePolicyVer%> -> <%=postPolicyVer%>
<%
    }
%>
