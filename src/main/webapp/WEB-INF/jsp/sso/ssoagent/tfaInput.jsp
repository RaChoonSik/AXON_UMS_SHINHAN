<%@ page import="com.nets.sso.agent.AuthUtil" %>
<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.agent.authcheck.TFAReceiver" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
try {
    String logonUrl;           // 로그온 URL
    String idTagName, tfaIdTagName, credTagName, returnUrlTagName, siteTagName, codeTagName, deviceTagName;

    AuthCheck auth = new AuthCheck(request, response);

    idTagName = auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.USER_ID);
    tfaIdTagName = auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.TFA_ID);
    credTagName = auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.CRED_TYPE);
    returnUrlTagName = auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.RETURN_URL);
    siteTagName = auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.SITE_ID);
    codeTagName = auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.CODE);
    deviceTagName = auth.getSsoProvider().getParamName(AuthUtil.ParamInfo.DEVICE);

    logonUrl = String.format("%s?%s=%s",
            auth.getSsoSite().getLogonUrl(request),
            siteTagName,
            auth.getSsoSite().getId()
    );

    TFAReceiver tfaReceiver = new TFAReceiver(auth);
    if (tfaReceiver.getEnable() && auth.getSsoProvider().getTfa().getTwoStep() && tfaReceiver.getTargetYN()) {
        String tfaMethod = tfaReceiver.getMethod();
        //JDK 1.4 (추가로 전달된 정보)
//            Map misc = tfaReceiver.getMiscellaneous();
        //JDK 1.5 (추가로 전달된 정보)
        Map<String, String> misc = tfaReceiver.getMiscellaneous();
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>NETS*SSO TFA</title>
    <script type="text/javascript">

        function startTimer(duration, display) {
            if (0 == duration) {
                display.textContent = "limitless";
            } else {
                var timer = duration, minutes, seconds;
                setInterval(function () {
                    minutes = parseInt(timer / 60, 10);
                    seconds = parseInt(timer % 60, 10);

                    minutes = minutes < 10 ? "0" + minutes : minutes;
                    seconds = seconds < 10 ? "0" + seconds : seconds;

                    display.textContent = minutes + ":" + seconds;

                    if (--timer < 0) {
                        timer = duration;
                    }
                }, 1000);
            }
        }

        function OnLogon() {
            if (document.getElementById("txtCode").value == "") {
                alert("보안 코드를 입력해주세요.");
                return;
            }
            document.forms["form1"].target = "_top";
            document.forms["form1"].submit();
        }

        function OnInit() {
            document.getElementById("txtCode").focus();
            var minutes = 60 * <%=auth.getSsoProvider().getTfa().getTimeoutMinutes()%>;
            var display = document.querySelector('#time');
            startTimer(minutes, display);
        }
    </script>
</head>
<body onLoad="OnInit();">
<form id="form1" method="post" action="<%=logonUrl%>">
    <input type="hidden" id="<%=idTagName%>" name="<%=idTagName%>" value="<%=tfaReceiver.getUserID()%>"/>
    <input type="hidden" name="<%=tfaIdTagName%>" value="<%=tfaReceiver.getTfaID()%>"/>
    <input type="hidden" name="<%=returnUrlTagName%>" value="<%=tfaReceiver.getReturnUrl()%>"/>
    <input type="hidden" id="credType" name="<%=credTagName%>" value="NTFA"/>
    <% if (Utility.isNotEmpty(tfaReceiver.getDevice())) {%>
    <input type="hidden" name="<%=deviceTagName%>" value="<%=tfaReceiver.getDevice()%>"/>
    <% } %>
    <input type="hidden" name="password" value="<%=tfaReceiver.getMiscellaneous("password")%>"/>
    <table>
        <tr>
            <td colspan="2">Two factor authentication</td>
        </tr>
        <tr>
            <td>User ID :</td>
            <td><%=tfaReceiver.getUserID()%>
            </td>
        </tr>
        <tr>
            <td>Secret Code :</td>
            <td><input type="text" id="txtCode" name="<%=codeTagName%>"/> left: <span id="time"></span></td>
        </tr>
        <tr>
            <td colspan="2"><input type="button" value="Logon" onclick="OnLogon();"/></td>
        </tr>
    </table>
</form>
</body>
</html>
<%
    } else {
        if (!auth.getSsoProvider().getTfa().getEnable()) { %>NETS*SSO TFA is disabled.<% } else if (!auth.getSsoProvider().getTfa().getTwoStep()) { %>2 step process of NETS*SSO TFA is disabled.<% } else if (!tfaReceiver.getTargetYN()) { %>"<%=tfaReceiver.getUserID()%>" is not user who must be authenticated with TFA.<% }
    %><br/><a href="../logon.jsp">logon page</a><%
    }
} catch (AgentException e) {
    e.printStackTrace();
%>
<%=e.toString()%>
<%
}
%>