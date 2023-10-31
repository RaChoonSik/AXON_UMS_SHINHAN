<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.agent.authcheck.DupCheck" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    try {
        AuthCheck auth = new AuthCheck(request, response);
        DupCheck dupCheck = new DupCheck(auth, request, response);
        dupCheck.processLogon();
    } catch (AgentException e) {
        e.printStackTrace();
%>
<%=e.toString()%>
<%
    }
%>