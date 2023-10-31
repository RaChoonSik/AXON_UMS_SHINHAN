<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.enums.AuthStatus" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String logonId = "";          // 로그온 된 사용자 아이디
    try {
        AuthCheck auth = new AuthCheck(request, response);
        AuthStatus status = auth.checkLogon();
        //인증상태별 처리
        if (status == AuthStatus.SSOFirstAccess) {
            auth.trySSO();
        } else if (status == AuthStatus.SSOSuccess) {
            logonId = auth.getUserID();
        } else if (status == AuthStatus.SSOFail) {
        } else if (status == AuthStatus.SSOUnAvailable) {
        }
%>
<!DOCTYPE html>
<head>
    <title>NETS*SSO</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="./ssoagent/js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript">
        function callArtifact(targetID, artifactParam) {
            var artifact = getArtifact(targetID, artifactParam);
            if (artifact != null)
                runArtifact(artifact);
        }

        function getArtifact(targetID, artifactParam) {
            try {
                var url = "./ssoagent/requestArtifact.jsp";
                var param = "targetID=" + targetID + "&artifactParam=" + artifactParam + "&time=" + new Date().getTime();
                return doAjax(url, param);
            } catch (e) {
                alert(e);
                return null;
            }
        }

        function runArtifact(artifact) {
            var artifactUrl = "";
            if (artifact.status == "false") {
                alert('인증 연계에 실패 했습니다.(' + artifact.code + ')');
                return;
            }
            if (artifact.uriScheme == "") {
                alert("URI Scheme가 설정되어 있지 않습니다.");
                return;
            }
            runApp(artifact);
        }

        function runApp(artifact) {
            var uriScheme = decodeURIComponent(artifact.uriScheme);
            // alert(uriScheme + artifact.code);
            location.href = uriScheme + artifact.code;
        }

        function doAjax(requestUrl, requestParam) {
            var returnData = null;
            $.ajax({
                url: requestUrl,
                type: 'GET',
                async: false,
                data: requestParam,
                dataType: "json",
                timeout: 10000,
                contentType: "application/x-www-form-urlencoded",
                success: function (data) {
                    returnData = data;
                },
                error: function (request, status, error) {
                },
                fail: function () {
                    alert("인터넷 연결 상태를 확인해주세요.");
                }
            });
            return returnData;
        }
    </script>
</head>
<body>
로그인 사용자 : <%=logonId %><br/><br/>
<input type="button" onclick="callArtifact('99ceef8edb5f4fda9e865cae1f57c915', '');" value="App 실행">
<br><br/>
<a href="default.jsp">기본 페이지</a>
</body>
</html>
<%
} catch (AgentException e) {
    e.printStackTrace();
%><%=e.toString()%><%
    }
%>