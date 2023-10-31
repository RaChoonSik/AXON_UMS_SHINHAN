<%@ page import="com.nets.sso.agent.authcheck.AuthCheck" %>
<%@ page import="com.nets.sso.common.AgentException" %>
<%@ page import="com.nets.sso.common.AgentExceptionCode" %>
<%@ page import="com.nets.sso.common.Utility" %>
<%@ page import="com.nets.sso.common.enums.AuthStatus" %>
<%@ page import="com.nets.sso.common.webservices.ReceiveResponse" %>
<%
    //JDK 1.4
//    String returnFormat = "{ \"status\":\"{0}\", \"code\":\"{1}\", \"uriScheme\":\"{2}\", \"essoUrl\":\"{3}\" }";
    //JDK 1.5
    String returnFormat = "{ \"status\":\"%s\", \"code\":\"%s\", \"uriScheme\":\"%s\", \"essoUrl\":\"%s\" }";
    String returnString = "";
    try {
        AuthCheck auth = new AuthCheck(request, response);
        AuthStatus status = auth.checkLogon();
        //인증상태별 처리
        if (status == AuthStatus.SSOSuccess) {
            String targetID = Utility.getRequestValue(request, "targetID", Utility.EMPTY_STRING);
            String artifactParam = Utility.getRequestValue(request, "artifactParam", Utility.EMPTY_STRING);
            if (Utility.isNullOrEmpty(targetID))
                throw new AgentException(AgentExceptionCode.ArtifactMissingTargetID);

            ReceiveResponse receiveResponse = auth.requestArtifact(targetID, artifactParam);
            if (receiveResponse.getData("status").equalsIgnoreCase("true")) {
                //연동 성공
                if (receiveResponse.getData("targetType").equalsIgnoreCase("03")) {
                    //Application 형식
                    //JDK 1.4
//                    returnString = Utility.replaceAll(returnFormat, "{0}", "true");
//                    returnString = Utility.replaceAll(returnString, "{1}", receiveResponse.getData("artifactID"));
//                    returnString = Utility.replaceAll(returnString, "{2}", receiveResponse.getData("uriScheme"));
//                    returnString = Utility.replaceAll(returnString, "{3}", receiveResponse.getData("essoUrl"));
                    //JDK 1.5
                    returnString = String.format(returnFormat,
                            "true",
                            receiveResponse.getData("artifactID"),
                            receiveResponse.getData("uriScheme"),
                            receiveResponse.getData("essoUrl")
                    );
                } else {
                    //Web 형식
                    //JDK 1.4
//                    returnString = Utility.replaceAll(returnFormat, "{0}", "true");
//                    returnString = Utility.replaceAll(returnString, "{1}", receiveResponse.getData("artifactID"));
//                    returnString = Utility.replaceAll(returnString, "{2}", Utility.EMPTY_STRING);
//                    returnString = Utility.replaceAll(returnString, "{3}", receiveResponse.getData("essoUrl"));
                    //JDK 1.5
                    returnString = String.format(returnFormat,
                            "true",
                            receiveResponse.getData("artifactID"),
                            Utility.EMPTY_STRING,
                            receiveResponse.getData("essoUrl")
                    );
                }
            } else {
                //연동 실패
                //JDK 1.4
//                returnString = Utility.replaceAll(returnFormat, "{0}", "false");
//                returnString = Utility.replaceAll(returnString, "{1}", receiveResponse.getData("errorCode"));
//                returnString = Utility.replaceAll(returnString, "{2}", Utility.EMPTY_STRING);
//                returnString = Utility.replaceAll(returnString, "{3}", receiveResponse.getData("essoUrl"));
                //JDK 1.5
                returnString = String.format(returnFormat,
                        "false",
                        receiveResponse.getData("errorCode"),
                        Utility.EMPTY_STRING,
                        receiveResponse.getData("essoUrl")
                );
            }
        } else {
            throw new AgentException(AgentExceptionCode.ArtifactRequiredLogon);
        }
    } catch (AgentException e) {
        //오류 발생
        //JDK 1.4
//        returnString = Utility.replaceAll(returnFormat, "{0}", "false");
//        returnString = Utility.replaceAll(returnString, "{1}", String.valueOf(e.getExceptionCode().getValue()));
//        returnString = Utility.replaceAll(returnString, "{2}", Utility.EMPTY_STRING);
//        returnString = Utility.replaceAll(returnString, "{3}", Utility.EMPTY_STRING);
        //JDK 1.5
        returnString = String.format(returnFormat,
                "false",
                e.getExceptionCode().getValue(),
                Utility.EMPTY_STRING,
                Utility.EMPTY_STRING
        );
    }
%><%=returnString%>