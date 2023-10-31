<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.common.webservices.AgentServiceProxy" %>
<%@ page import="com.nets.sso.common.webservices.ReceiveResponse" %>
<%
    AuthCheck auth = new AuthCheck(request, response);
    AgentServiceProxy serviceProxy = new AgentServiceProxy(auth.getSsoSite());
    ReceiveResponse receiveResponse = serviceProxy.generateKey(request.getServerName());
    String keyBox = receiveResponse.getData("keyBox");
%><%=keyBox%>