<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.agent.authcheck.DupCheck" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ssoRequest = Utility.cleanXSS(request.getParameter("ssoRequest"));
    String dupIP, dupTime, dupUserID, timeoutMinutes;
    try {
        AuthCheck auth = new AuthCheck(request, response);
        DupCheck dupCheck = new DupCheck(auth, request, response);
        dupIP = dupCheck.getDupIP();
        dupTime = dupCheck.getDupTime();
        dupUserID = dupCheck.getDupUserID();
        timeoutMinutes = dupCheck.getTimeoutMinutes();
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>NETS*SSO Duplication</title>
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
            document.forms["form1"].target = "_top";
            document.forms["form1"].action = "./dupChoiceLogon.jsp";
            document.forms["form1"].submit();
        }
        function OnCancel() {
            document.forms["form1"].target = "_top";
            document.forms["form1"].action = "./dupChoiceCancel.jsp";
            document.forms["form1"].submit();
        }
        function OnInit() {
            var minutes = 60 * <%=timeoutMinutes%>;
            var display = document.querySelector('#time');
            startTimer(minutes, display);
        }
    </script>
</head>
<body onLoad="OnInit();">
<form id="form1" method="post" action="">
    <input type="hidden" name="ssoRequest" value="<%=ssoRequest%>"/>
    <table>
        <tr>
            <td colspan="2">Duplication</td>
        </tr>
        <tr>
            <td>ID:</td>
            <td><%=dupUserID%>
            </td>
        </tr>
        <tr>
            <td>IP:</td>
            <td><%=dupIP%>
            </td>
        </tr>
        <tr>
            <td>Time:</td>
            <td><%=dupTime%>
            </td>
        </tr>
        <tr>
            <td>Select Choice</td>
            <td>
                <input type="button" value="Yes, let me logon" onclick="OnLogon()">
                <input type="button" value="No, Let him logon" onclick="OnCancel()">
                left: <span id="time"></span>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
<%
} catch (AgentException e) {
    e.printStackTrace();
%>
<%=e.toString()%>
<%
    }
%>