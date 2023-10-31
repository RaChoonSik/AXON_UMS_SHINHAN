<%@ page import="com.google.gson.JsonElement" %>
<%@ page import="com.google.gson.JsonParser" %>
<%@ page import="com.nets.sso.agent.AuthUtil" %>
<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.agent.configuration.SSOConfig" %>
<%@ page import="com.nets.sso.agent.configuration.SSOProvider" %>
<%@ page import="com.nets.sso.agent.configuration.SSOSite" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.crypto.SSOCrypt" %>
<%@ page import="com.nets.sso.common.webservices.AgentServiceProxy" %>
<%@ page import="com.nets.sso.common.webservices.ReceiveResponse" %>
<%
    try {
        // 비대칭 키 받아오기.
        AuthCheck auth = new AuthCheck(request, response);
        String serverName = request.getServerName();
        SSOProvider ssoProvider = SSOConfig.getInstance().getCurrentSSOProvider(serverName);
        SSOSite ssoSite = ssoProvider.getCurrentSSOSite(serverName);

        // 태그명 들
        String tagUserID, tagUserPW, tagCredType, tagReturnUrl, tagSiteID, tagKeyID;
        tagUserID = ssoProvider.getParamName(AuthUtil.ParamInfo.USER_ID);
        tagUserPW = ssoProvider.getParamName(AuthUtil.ParamInfo.USER_PW);
        tagCredType = ssoProvider.getParamName(AuthUtil.ParamInfo.CRED_TYPE);
        tagReturnUrl = ssoProvider.getParamName(AuthUtil.ParamInfo.RETURN_URL);
        tagSiteID = ssoProvider.getParamName(AuthUtil.ParamInfo.SITE_ID);
        tagKeyID = ssoProvider.getParamName(AuthUtil.ParamInfo.KEY_ID);
        String siteID = request.getParameter(tagSiteID);

        // 비대칭키(공개키) 받아오기
        AgentServiceProxy serviceProxy = new AgentServiceProxy(auth.getSsoSite());
        ReceiveResponse receiveResponse = serviceProxy.generateKey(request.getServerName());
        String keyBox = receiveResponse.getData("keyBox");

        // 전달 받은 키 조회
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(keyBox);
        String keyID = element.getAsJsonObject().get("id").getAsString();
        String modulus = element.getAsJsonObject().get("modulus").getAsString();
        String exponent = element.getAsJsonObject().get("exponent").getAsString();

        // 암호화 문자열 준비(input/ouput)
        String plainText = "Original Plain Text";
        String encryptedValue = SSOCrypt.encrypRSA(plainText, modulus, exponent);
%>
<!DOCTYPE html>
<html>
<head>
    <title>NETS*SSO</title>
    <script type="text/javascript">
        function autoPost() {
            document.forms["form1"].submit();
        }
    </script>
</head>
<body onload="autoPost()">
<form name="" action="" method="post">
    <input type="hidden" id="userID" name="<%=tagUserID%>" value=""/>
    <input type="hidden" id="userPW" name="<%=tagUserPW%>" value=""/>
    <input type="hidden" id="keyID" name="<%=tagKeyID%>" value=""/>
    <input type="hidden" id="credType" name="<%=tagCredType%>" value="ENCRYPTEDBASIC"/>
    <input type="hidden" id="ssosite" name="<%=tagSiteID%>" value="<%=siteID%>"/>
    <input type="hidden" name="<%=tagReturnUrl%>" value=""/>
</form>
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