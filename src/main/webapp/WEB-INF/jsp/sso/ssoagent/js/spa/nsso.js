var intervalCheckLogon = 60000;	//millisecond
var timerCheckLogon = null;
var intervalCheckSSO = 60000;	//millisecond
var timerCheckSSO = null;

// base uri: set from 'setNssoConfiguration' method
var nssoConfigAgentBaseUri = "";

// ============
// ASP.NET
// ============
/*
// for get configuration
var nssoConfigPathNssoConfig = "NSSOConfig";
// for login back-end application
var nssoConfigPathLogonService = "LogonService";
var nssoConfigPathLogonServiceAfter = "LogonServiceAfter";
// for choice when duplication occurred
var nssoConfigPathDupReceive = "DupChoiceReceive";
var nssoConfigPathDupChoiceLogon = "DupChoiceLogon";
var nssoConfigPathDupChoiceCancel = "DupChoiceCancel";
// for input when secret code is needed
var nssoConfigPathTfaReceive = "TFAReceive";
// for check sso status of back-end application
var nssoConfigPathCheckLogon = "CheckLogon";
// for callback url from sso server (LogonServiceAfter.aspx)
//var nssoConfigPathLogonServiceAfter = "LogonServiceAfter";
*/

// ============
// Java
// ============
// for get configuration
var nssoConfigPathNssoConfig = "nssoConfig.jsp";
// for login back-end application
var nssoConfigPathLogonService = "logonService.jsp";
var nssoConfigPathLogonServiceAfter = "logonServiceAfter.jsp";
// for choice when duplication occurred
var nssoConfigPathDupReceive = "dupChoiceReceive.jsp";
var nssoConfigPathDupChoiceLogon = "dupChoiceLogon.jsp";
var nssoConfigPathDupChoiceCancel = "dupChoiceCancel.jsp";
// for input when secret code is needed
var nssoConfigPathTfaReceive = "tfaReceive.jsp";
// for check sso status of back-end application
var nssoConfigPathCheckLogon = "checkLogon.jsp";


// set from 'setNssoConfiguration' method
var ssosite = "";
var defaultUrl = "";
var urlSSOLogonService = "";
var urlSSOLogonServiceAfter = "";
var urlSSOCheckService = "";
var urlSSOLogoffService = "";
var defaultCredType = "BASIC";

// logonStatus
var ssoSuccess = false;
var ssoErrorCode = 0;
var ssoErrorMessage = "";
var ssoUserId = "";
var ssoUserAttribute = null;
var ssoMisc = null;
var ssoTfa = {};
var ssoDuplication = {};

var ssoProviderSessionValueSaved = false;
var temporaryDuplicationData = "";

function getNssoUserId() {
	return ssoSuccess ? ssoUserId : "";
}

function getNssoUserAttribute(keynm) {
	var attrReturn = "";

	if (ssoSuccess) {
		$.each(ssoUserAttribute, function (i, v) {
			if (v.key === keynm) {
				attrReturn = v.value;
			}
		});

		if (attrReturn !== "")
			return attrReturn;

		ssoErrorMessage = "There is no attribute '" + keynm + "'.";
		console.log(ssoErrorMessage);
		return "";
	} else {
		ssoErrorMessage = "User does not logon.";
		console.log(ssoErrorMessage);
		return "";
	}
}

function getNssoUserAttributeList() {
	var attrRetrun = [];

	if (ssoSuccess) {
		$.each(ssoUserAttribute, function (i, v) {
			attrRetrun[i] = v.key + "=" + v.value;
		});
	}
	return attrRetrun;
}

function getNssoMisc(key) {
	// TODO: implement get
	return "";
}

var callbackLogonFail, callbackLogonSuccess, callbackReceiveTfa, callbackReceiveDuplication;

function setNssoConfiguration(baseUri, functionLogonFail, functionLogonSuccess, functionReceiveTfa, functionReceiveDuplication) {
	nssoConfigAgentBaseUri = baseUri.endsWith("/") ? baseUri : baseUri + "/";
	callbackLogonFail = functionLogonFail;
	callbackLogonSuccess = functionLogonSuccess;
	callbackReceiveTfa = functionReceiveTfa;
	callbackReceiveDuplication = functionReceiveDuplication;

	$.ajax({
		type: 'POST',
		url: nssoConfigAgentBaseUri + nssoConfigPathNssoConfig,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/json' },
		data: {},
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			if (json.result) {
				ssosite = json.ssosite;
				defaultUrl = json.defaultUrl;
				urlSSOLogonService = json.urlSSOLogonService;
				urlSSOLogonServiceAfter = json.urlSSOLogonServiceAfter;
				urlSSOCheckService = json.urlSSOCheckService;
				urlSSOLogoffService = json.urlSSOLogoffService;
				checkLogon();
			} else {
				alert(json.errorMessage);
			}
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed setting nsso-configuration. " + error;
			callbackLogonFail();
			console.log(ssoErrorMessage);
		}
	});
}

function checkLogon() {
	$.ajax({
		type: 'POST',
		url: nssoConfigAgentBaseUri + nssoConfigPathCheckLogon,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/json', 'SSOAgent-Type': 'SPA' },
		data: {},
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			parseCheckLogon(json);
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed check logon status. " + error;
			callbackLogonFail();
			console.log(ssoErrorMessage);
		}
	});
}

function parseCheckLogon(json) {
	ssoErrorCode = json.errorCode;
	if (json.authStatus === "SSOSuccess") {
		ssoSuccess = true;
		ssoUserId = json.userId;
		ssoUserAttribute = json.userAttribute;
		ssoErrorMessage = "";
		if (ssoProviderSessionValueSaved)
			callbackLogonSuccess();
		else
			callSSOProviderLogonServiceAfter();

		if (timerCheckLogon === null) {
			timerCheckLogon = setInterval(checkLogon, intervalCheckLogon);
			clearInterval(timerCheckSSO); timerCheckSSO = null;
		}
	} else {
		ssoSuccess = false;
		ssoUserId = "";
		ssoUserAttribute = null;
		ssoErrorMessage = json.errorMessage;
		console.log("parseCheckLogon:" + ssoErrorCode + ", " + ssoErrorMessage);
		callbackLogonFail();
		if (timerCheckSSO === null) {
			timerCheckSSO = setInterval(checkSSO, intervalCheckSSO);
			clearInterval(timerCheckLogon); timerCheckLogon = null;
			checkSSO();
		}
	}
}

function checkSSO() {
	$.ajax({
		type: 'POST',
		url: urlSSOCheckService,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'SSOAgent-Type': 'SPA' },
		data: {
			ssosite: ssosite,
			returnURL: defaultUrl
		},
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			parseFromSSO(json);
		},
		error: function (request, status, error) {
			console.log("checkSSO: " + error);
		}
	});
}

function parseFromSSO(json) {
	ssoErrorCode = json.errorCode;
	ssoErrorMessage = json.errorMessage;
	if (json.result) {
		// continue sso
		var policyVersion = json.policyVersion;
		if (json.errorCode === 0) {
			// received logon data
			var ssoResponse = json.hasOwnProperty("ssoResponse") ? json.ssoResponse : "";
			var artifactId = json.hasOwnProperty("artifactID") ? json.artifactID : "";
			callSSOProviderLogonServiceAfter();
			logonWebApplication(ssoResponse, artifactId, policyVersion);
		} else if (json.errorCode === 11100028) {
			// Two Factor Authentication
			receiveTfa(json.ntfa, json.signature, policyVersion);
		} else if (json.errorCode === 11060009) {
			// Choice Duplication
			temporaryDuplicationData = json.ssoRequest;
			receiveDuplication(json.ssoRequest, policyVersion);
		} else {
			if (!ssoErrorMessage)
				ssoErrorMessage = "Failed parsing data from SSO. (" + json.errorCode + ")";
			console.log(ssoErrorMessage);
			callbackLogonFail();
		}
	} else {
		callbackLogonFail();
	}
	ssoMisc = json.hasOwnProperty("misc") ? json.misc : null;
}

function logonWebApplication(ssoResponse, artifactId, pv) {
	$.ajax({
		type: 'POST',
		url: nssoConfigAgentBaseUri + nssoConfigPathLogonService,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'SSOAgent-Type': 'SPA' },
		data: {
			ssosite: ssosite,
			ssoResponse: ssoResponse,
			artifactID: artifactId,
			policyVersion: pv
		},
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			parseCheckLogon(json);
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed logon web-application. " + error;
			console.log(ssoErrorMessage);
		}
	});
}

function receiveTfa(ntfa, sign, sv) {
	$.ajax({
		type: 'POST',
		url: nssoConfigAgentBaseUri + nssoConfigPathTfaReceive,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'SSOAgent-Type': 'SPA' },
		data: {
			ntfa: ntfa,
			signature: sign,
			policyVersion: sv
		},
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			parseReceiveTfa(json);
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed logon tfa. " + error;
			console.log(ssoErrorMessage);
		}
	});
}


function parseReceiveTfa(json) {
	ssoErrorCode = json.errorCode;
	ssoErrorMessage = json.errorMessage;
	if (json.result) {
		// continue sso
		if (json.errorCode === 0) {
			ssoTfa['userID'] = json.userID;
			ssoTfa['tfaID'] = json.tfaID;
			ssoTfa['targetYN'] = json.targetYN;
			//ssoTfa['device'] = json.device;
			ssoTfa['code'] = json.code;
			ssoTfa['method'] = json.method;
			ssoTfa['timeoutMinutes'] = json.timeoutMinutes;
			ssoMisc = json.hasOwnProperty("misc") ? json.misc : null;
			clearInterval(timerCheckSSO);
			callbackReceiveTfa();
		} else {
			if (!ssoErrorMessage)
				ssoErrorMessage = "Failed parsing tfa-data from SSO. (" + json.errorCode + ")";
			console.log(ssoErrorMessage);
		}
	}
}

function receiveDuplication(dupData, sv) {
	$.ajax({
		type: 'POST',
		url: nssoConfigAgentBaseUri + nssoConfigPathDupReceive,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'SSOAgent-Type': 'SPA' },
		data: {
			ssoRequest: dupData,
			policyVersion: sv
		},
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			parseReceiveDuplication(json);
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed logon tfa. " + error;
			console.log(ssoErrorMessage);
		}
	});
}


function parseReceiveDuplication(json) {
	ssoErrorCode = json.errorCode;
	ssoErrorMessage = json.errorMessage;
	if (json.result) {
		// continue sso
		if (json.errorCode === 0) {
			ssoDuplication['userID'] = json.userID;
			ssoDuplication['time'] = json.time;
			ssoDuplication['ip'] = json.ip;
			ssoDuplication['timeoutMinutes'] = json.timeoutMinutes;
			clearInterval(timerCheckSSO);
			callbackReceiveDuplication();
		} else {
			if (!ssoErrorMessage)
				ssoErrorMessage = "Failed parsing duplication-data from SSO. (" + json.errorCode + ")";
			console.log(ssoErrorMessage);
		}
	}
}

function logon(id, pw) {
	$.ajax({
		type: 'POST',
		url: urlSSOLogonService,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'SSOAgent-Type': 'SPA' },
		data: {
			userID: id,
			password: pw,
			credType: defaultCredType,
			ssosite: ssosite,
			returnURL: defaultUrl
		},
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			parseFromSSO(json);
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed logon. " + error;
			console.log(ssoErrorMessage);
		}
	});
}

function logonTfa(secretCode) {
	$.ajax({
		type: 'POST',
		url: urlSSOLogonService,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'SSOAgent-Type': 'SPA' },
		data: {
			userID: ssoTfa['userID'],
			tfaID: ssoTfa['tfaID'],
			code: secretCode,
			credType: "NTFA",
			ssosite: ssosite,
			returnURL: defaultUrl
		},
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			parseFromSSO(json);
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed logon(TFA). " + error;
			console.log(ssoErrorMessage);
		}
	});
}

function dupChoiceCancel() {
	$.ajax({
		type: 'POST',
		url: nssoConfigAgentBaseUri + nssoConfigPathDupChoiceCancel,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'SSOAgent-Type': 'SPA' },
		data: { ssorequest: temporaryDuplicationData },
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			ssoErrorCode = json.errorCode;
			ssoErrorMessage = json.errorMessage;
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed cancel duplication choice. " + error;
			console.log(ssoErrorMessage);
		}
	});
}

function dupChoiceLogon() {
	$.ajax({
		type: 'POST',
		url: nssoConfigAgentBaseUri + nssoConfigPathDupChoiceLogon,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'SSOAgent-Type': 'SPA' },
		data: { ssorequest: temporaryDuplicationData },
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			ssoErrorCode = json.errorCode;
			ssoErrorMessage = json.errorMessage;
			if (ssoErrorCode === 0) {
				dupLogon(json.credential);
			}
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed logon duplication choice. " + error;
			console.log(ssoErrorMessage);
		}
	});
	timerCheckSSO = setInterval(checkSSO, intervalCheckSSO);
}

function dupLogon(dupCredential) {
	$.ajax({
		type: 'POST',
		url: urlSSOLogonService,
		xhrFields: { withCredentials: true },
		headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'SSOAgent-Type': 'SPA' },
		data: {
			credential: dupCredential,
			credType: "DUPCHOICE",
			ssosite: ssosite,
			returnURL: defaultUrl
		},
		async: true,
		success: function (data) {
			json = JSON.parse(data);
			parseFromSSO(json);
		},
		error: function (request, status, error) {
			ssoErrorMessage = "Failed logon(Dup). " + error;
			console.log(ssoErrorMessage);
		}
	});
}

function callSSOProviderLogonServiceAfter() {
	removeElementSSOiframe();

	var ifrm = document.createElement('iframe');
	ifrm.setAttribute('id', 'iframeSSO');
	ifrm.setAttribute('height', '0px');
	ifrm.setAttribute('width', '0px');
	document.body.appendChild(ifrm);
	ifrm.setAttribute('src', urlSSOLogonServiceAfter + "?callBackUrl=" + nssoConfigAgentBaseUri + nssoConfigPathLogonServiceAfter);
}

function callbackSSOProviderLogonServiceAfter(value) {
	ssoProviderSessionValueSaved = value;
	callbackLogonSuccess();
}

function removeElementSSOiframe() {
	var ifrm = document.getElementById('iframeSSO');
	if (ifrm !== null)
		ifrm.parentNode.removeChild(ifrm);
}

String.prototype.startsWith = function (str) {
	if (this.length < str.length) { return false; }
	return this.indexOf(str) === 0;
};
String.prototype.endsWith = function (str) {
	if (this.length < str.length) { return false; }
	return this.lastIndexOf(str) + str.length === this.length;
};