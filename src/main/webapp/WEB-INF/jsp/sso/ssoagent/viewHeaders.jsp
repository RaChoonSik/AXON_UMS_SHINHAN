<%@ page import="java.util.Enumeration" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>SSO 통합인증 테스트 사이트</title>
    <meta http-equiv="Accept-CH-Lifetime" content="86400">
    <meta http-equiv="Accept-CH" content="UA, UA-Platform, UA-Arch, UA-Model, UA-Mobile, UA-Full-Version">
</head>
<body>
<%
    //JDK 1.4
//    Enumeration eHeader = request.getHeaderNames();
    //JDK1.5
    Enumeration<String> eHeader = request.getHeaderNames();
    while (eHeader.hasMoreElements()) {
        String hName = (String) eHeader.nextElement();
        String hValue = request.getHeader(hName);
%>
<%=hName + ":" + hValue%><br/>
<%
    }
%>
<br/>
requeset.getServerName():<%=request.getServerName()%><br/>
requeset.getRemoteAddr():<%=request.getRemoteAddr()%><br/>
</body>
</html>
