<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link rel="stylesheet" type="text/css" href="./ssoagent/css/login.css" />
    <script type="text/javascript" src="./ssoagent/js/spa/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="./ssoagent/js/spa/nsso.js?<%=new Date().getTime()%>"></script>
    <script type="text/javascript">
        function OnLogon() {
            var userId = $("#user_id");
            var userPw = $("#user_pw");
            if (!userId.val()) { userId.focus(); return; }
            if (!userPw.val()) { userPw.focus(); return; }
            logon(userId.val(), userPw.val());
        }

        function OnLogoff() {
            location.href = urlSSOLogoffService + "?ssosite=" + ssosite + "&returnURL=" + encodeURIComponent(location.href);
        }

        function OnDupLogon() {
            $("#dupChoice").css("visibility", "hidden");
            dupChoiceLogon();
        }

        function OnDupCancel() {
            $("#dupChoice").css("visibility", "hidden");
            dupChoiceCancel();
        }

        function OnTFALogon() {
            var code = $("#code");
            if (!code.val()) { code.focus(); return; }
            logonTfa(code.val());
        }
    </script>

</head>
<body>
<div class="log_wrap">
    <header>
        <h1>Single Page Application</h1>
    </header>
    <section>
        <!-- LOGON UI -->
        <div id="logonUi" class="log_form" style="visibility: hidden">
            <div class="form_content">
                <dl class="input_id">
                    <dt>ID</dt>
                    <dd>
                        <input class="clearInput" type="text" id="user_id" />
                    </dd>
                </dl>
                <dl class="input_pass">
                    <dt>PASSWORD</dt>
                    <dd>
                        <input class="passInput" type="password" id="user_pw" />
                    </dd>
                </dl>
                <div id="infoBox" class="info_box">
                    <p id="errorMsg"></p>
                </div>
                <button type="button" onclick="OnLogon()">Logon</button>
            </div>
        </div>
        <!-- LOGON SUCCESS -->
        <div id="userInfo" class="log_form" style="visibility: hidden">
            <div class="form_content">
                <dl class="input_id">
                    <dt>ID</dt>
                    <dd>
                        <p id="UserId"></p>
                    </dd>
                </dl>
                <dl class="input_pass">
                    <dt>Attributes</dt>
                    <dd>
                        <p id="UserAttribute"></p>
                    </dd>
                </dl>
                <button type="button" onclick="OnLogoff()">Logoff</button>
            </div>
        </div>
        <!-- Duplication Choice -->
        <div id="dupChoice" class="log_form" style="visibility: hidden">
            <div class="form_content">
                <dl class="input_id">
                    <dt>Duplication Info</dt>
                    <dd>
                        <p id="dupInfo"></p>
                    </dd>
                </dl>
                <dl class="input_pass">
                    <dt>Choice</dt>
                    <dd>
                        <button type="button" onclick="OnDupLogon()">Let me logon</button>
                        <button type="button" onclick="OnDupCancel()">Cancel logon</button>
                    </dd>
                </dl>
            </div>
        </div>
        <!-- TFA Code Input -->
        <div id="tfaCode" class="log_form" style="visibility: hidden">
            <div class="form_content">
                <dl class="input_id">
                    <dt>Code</dt>
                    <dd>
                        <input class="clearInput" type="text" id="code" />
                    </dd>
                </dl>
                <button type="button" onclick="OnTFALogon()">Logon</button>
            </div>
        </div>
    </section>
</div>
</body>
<script type="text/javascript">
    $(document).ready(function () {
        /*================================
        // NSSO 환경설정
        //     1) BackEnd 와 FrontEnd 의 서비스 호스트(DNS)는 동일해야 한다. (scheme, host, port)
        //     2) 인증서버, SPA 와 관련된 시스템(BackEnd, FrontEnd)들은 HTTPS 로 서비스해야 한다.
        //     3) 중앙 인증 쿠키는 Secure 및 SameSite=None 옵션을 사용해야 한다.
        //     4) 업무 사이트는 HTTP 로 서비스해도 무방하다.
        ================================*/
        setNssoConfiguration(
            "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/${pageContext.request.contextPath}/ssoagent/spa/",
            callbackLogonFail,
            callbackLogonSuccess,
            callbackReceiveTfa,
            callbackReceiveDuplication
        )
    });

    /*================================
    // 중복 로그온 선택 시 호출되는 메서드
    ================================*/
    function callbackReceiveDuplication() {
        $("#logonUi").css("visibility", "hidden");
        $("#tfaCode").css("visibility", "hidden");
        $("#userInfo").css("visibility", "hidden");
        $("#dupChoice").css("visibility", "visible");

        $("#UserAttribute").text("time:" + ssoDuplication['time'] + ", ip:" + ssoDuplication['ip']);
    }

    /*================================
    // 2차 인증 필요 시 호출되는 메서드
    ================================*/
    function callbackReceiveTfa() {
        $("#logonUi").css("visibility", "hidden");
        $("#dupChoice").css("visibility", "hidden");
        $("#userInfo").css("visibility", "hidden");
        $("#tfaCode").css("visibility", "visible");
        $("#code").focus();
    }

    /*================================
    // 인증 성공 시 호출되는 메서드
    ================================*/
    function callbackLogonSuccess() {
        if (ssoSuccess && ssoProviderSessionValueSaved) {
            // 이동해야할 URL이 존재하는지 확인
            var returnURL = getUrlParameter("returnURL");
            if (!returnURL === false) {
                // 페이지 이동
                location.href = returnURL;
                return;
            }

            $("#logonUi").css("visibility", "hidden");
            $("#dupChoice").css("visibility", "hidden");
            $("#tfaCode").css("visibility", "hidden");
            $("#userInfo").css("visibility", "visible");

            /*==== 사용자 아이디 추출 ====*/
            $("#UserId").text(ssoUserId);

            if (null === ssoUserAttribute)
                return;

            /*==== 사용자 속성 데이터 추출 ====*/
            var logonid = getNssoUserAttribute('logonid');
            var username = getNssoUserAttribute('username');
            $("#UserAttribute").text("mail:" + logonid + ", name=" + username);
        }
    }

    /*================================
    // 인증 실패 시 호출되는 메서드
    ================================*/
    function callbackLogonFail() {
        $("#dupChoice").css("visibility", "hidden");
        $("#tfaCode").css("visibility", "hidden");
        $("#userInfo").css("visibility", "hidden");
        $("#logonUi").css("visibility", "visible");

        if (!ssoErrorMessage && ssoErrorCode === 0)
            return;
        var msg = ssoErrorMessage;
        if (ssoErrorCode !== 0)
            msg += "(" + ssoErrorCode + ")";
        $("#errorMsg").text(msg);
    }

    /*================================
    // 현재 URL에서 전달된 Parameter 값 추출
    ================================*/
    function getUrlParameter(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        var results = regex.exec(location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }
</script>
</html>