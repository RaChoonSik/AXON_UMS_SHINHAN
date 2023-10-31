/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2022.09.28
*	설명 : 캠페인템플릿 정보수정 JavaScript
**********************************************************/
var dateRegex = RegExp(/^\d{4}.(0[1-9]|1[012]).(0[1-9]|[12][0-9]|3[01])$/);
var checkDateInput = false;
var campTempStatus = ""; 
$(document).ready(function() {
	// 머지 설정
	goMerge();
});


// 캠페인을 선택하였을 때 발생하는 이벤트
function goCamp() {
	if($("#campInfo").val() == "") {
		$("#campNo").val("");
	} else {
		var tmp = $("#campInfo").val();
		var campNo = tmp.substring(0, tmp.indexOf("|"));
		tmp = tmp.substring(tmp.indexOf("|") + 1);
		var campTy = tmp.substring(0, tmp.indexOf("|"));
		
		$("#campNo").val( campNo );
	}
}

function setCampTempContent(contentsTyp, contentsPath) {
	if($.trim(contentsTyp) == "0" || $.trim(contentsTyp) == "2")
	$.getJSON("/ems/cam/campTempFileView.json?contentsPath=" + contentsPath, function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.contVal]);	//스마트에디터 내용붙여넣기
			oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
			$("#prohibitCheckText").val( $("#ir1").val());
		}
	});
}

//템플릿을 선택하였을 경우 에디터에 템플릿 넣기
function goTemplate() {
	if($("#tempFlPath").val() == "") {
		alert("탬플릿을 선택하세요.");
		return;
	}
	
	$.getJSON("/ems/tmp/tempFileView.json?tempFlPath=" + $("#tempFlPath").val(), function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);    		//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
		} else {
			alert("Template Read Error!!");
		}
	});
}

// 수신자그룹을 선택하였을 경우 머지부분 처리
function goMerge() {
	// 머지입력 초기화
	$("#mergeKeyCampTemp").children().remove();
	
	if($("#segNoc").val() == "") {
		//alert("수신자그룹을 선택하세요.");
		return;
	}
	
	var condHTML = "";
	var merge = $("#segNoc").val().substring($("#segNoc").val().indexOf("|")+1);
	var pos = merge.indexOf(",");
	while(pos != -1) {
		condHTML = "<option value='$:"+merge.substring(0,pos)+":$'>"+merge.substring(0,pos)+"</option>";
		$("#mergeKeyCampTemp").append(condHTML);
		merge = merge.substring(pos+1);
		pos = merge.indexOf(",");
	}
	condHTML = "<option value='$:"+merge+":$'>"+merge+"</option>";
	$("#mergeKeyCampTemp").append(condHTML);
}

//사용자그룹 선택시 사용자 목록 조회 
function getUserList(deptNo) {
	$.getJSON("/com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#userId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#userId").append(option);
		});
	});
}

// 웹에이전트 클릭시
function popWebAgent() {
	fn.popupOpen('#popup_web_agent');
}

// 웹에이전트 팝업창에서 등록 클릭시
function popWebAgentSave() {
	if($("#webAgentUrl").val() == "") {
		alert("URL을 입력하세요.");
		$("#webAgentUrl").focus();
		return;
	}
	if($("#webAgentAttachYn").val() == "Y") {
		//$("#txtWebAgentUrl").html($("#webAgentUrl").val());
		$("#txtWebAgentUrl").html("첨부파일로 지정되었습니다.");
	} else {
		var contents = "^:" + $("#webAgentUrl").val() + ":^";
		oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
		oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);		//스마트에디터 내용붙여넣기
		$("#txtWebAgentUrl").html("본문삽입으로 지정되었습니다.");
	}
	fn.popupClose('#popup_web_agent');
}

// 웹에이전트 팝업창에서 취소 클릭시
function popWebAgentCancel() {
	$("#webAgentUrl").val("");
	$("#txtWebAgentUrl").html("형식이 지정되지 않았습니다.");
	$("#webAgentAttachYn").prop("checked", false);
	alert("웹이전트 등록이 취소되었습니다.");
	fn.popupClose('#popup_web_agent');
}

// 웹에이전트 첨부파일/본문삽입 선택시
function changeAttachYn() {
	if($("#webAgentUrl").val() != "") {
		if($("#secuAttTyp").val() == "NONE"){
			$("#webAgentAttachYn").val("N");
		} else {
			$("#webAgentAttachYn").val("Y");
		}
		
		if($("#webAgentAttachYn").val() == "Y") {
			//$("#txtWebAgentUrl").html($("#webAgentUrl").val());
			$("#txtWebAgentUrl").html("첨부파일로 지정되었습니다.");
		} else {
			var contents = "^:" + $("#webAgentUrl").val() + ":^";
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);		//스마트에디터 내용붙여넣기
			$("#txtWebAgentUrl").html("본문삽입으로 지정되었습니다.");
		}
	}
}

// 웹에이전트 미리보기 클릭시
function popWebAgentPreview() {
	if($("#webAgentUrl").val() == "") {
		alert("URL을 입력하세요.");
		fn.popupOpen('#popup_web_agent');
		$("#webAgentUrl").focus();
		return;
	}
	if($("#secuAttTyp").val() == "EXCEL") {
		alert("EXCEL은 미리보기를 할 수 없습니다.");
		return;
	}
	if($("#secuAttTyp").val() == "PDF") {
		alert("PDF는 미리보기를 할 수 없습니다.");
		return;
	}
	$("#iFrmWebAgent").empty();
	
	$("#previewWebAgentUrl").html($("#webAgentUrl").val());
	
	/*
	var param = $("#campTempInfoForm").serialize();
	$.getJSON("./webAgentPreview.json?" + param, function(res) {
		if(res.result == 'Success') {
			iFrmWebAgent.location.href = res.webAgentUrl;
			fn.popupOpen('#popup_preview_webagent');
		} else {
			alert("웹에이전트 미리보기 처리중 오류가 발생하였습니다.");
		}
	});*/
	var frm = $("#campTempInfoForm")[0];
	var frmData = new FormData(frm);
	
	$.ajax({
		type: "POST",
		url: '/ems/cam/webAgentPreview.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function(data) {
			if(data.result == 'Success') {
				iFrmWebAgent.location.href = data.webAgentUrl;
				fn.popupOpen('#popup_preview_webagent');
			} else {
				alert("웹에이전트 미리보기 처리중 오류가 발생하였습니다.");
			}
		},
		error: function() {
			alert("File Upload Error!!");
		}
	});
}

// 제목 클릭시(메일제목 내용 추가)
function goTitleMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	if($("#emailSubject").val() == "") {
		$("#emailSubject").val( $("#mergeKeyCampTemp").val() );
	} else {
		var cursorPosition = $("#emailSubject")[0].selectionStart;
		$("#emailSubject").val( $("#emailSubject").val().substring(0,cursorPosition) + $("#mergeKeyCampTemp").val() + $("#emailSubject").val().substring(cursorPosition) );
	}
}

//본문 클릭시(메일 내용 추가)
function goContMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	oEditors.getById["ir1"].exec("PASTE_HTML", [$("#mergeKeyCampTemp").val()]);   
}

// HTML등록 클릭시
var uploadHtmlType = "";
function popUploadHtml(upType) {
	uploadHtmlType = upType;
	$("#fileUpload")[0].reset();
	$(".fileupload .inputfile ~ button.btn").click();
	fn.popupOpen('#popup_html');
}

// HTML등록 등록버튼 클릭시
function goHtmlUpload(userId) {
	if($("#vFileName").val() == "") {
		alert("파일을 선택해주세요.");
		return;
	}
	
	var extCheck = "htm,html";
	var fileName = $("#vFileName").val();
	var fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
	
	// 첨부파일 용량 체크
	var fileByte = $("#fileUrl")[0].files[0].size;
	if( fileByte > 1048500) {
		alert("HTML파일의 용량은 최대 1MB까지 입니다.");
		return;
	}
	
	if(extCheck.toLowerCase().indexOf(fileExt.toLowerCase()) >= 0) {
		var frm = $("#fileUpload")[0];
		var frmData = new FormData(frm);
		$.ajax({
			type: "POST",
			enctype: 'multipart/form-data',
			url: '/com/uploadFile.json',
			data: frmData,
			processData: false,
			contentType: false,
			success: function (result) {
				alert("HTML 업로드가 완료되었습니다.");
				$("#vFileName").val(result.oldFileName);
				$("#rFileName").val(result.newFileName);
				
				// 편집기에 파일내용 표시
				$.getJSON("/ems/tmp/tempFileView.json?tempFlPath=" + encodeURIComponent("html/" + userId + "/" + result.newFileName), function(res) {
					if(res.result == 'Success') {
						if(uploadHtmlType == "mail") {
							oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
							oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
						} else if(uploadHtmlType == "temp") {
							oEditors2.getById["ir2"].exec("SET_CONTENTS", [""]);		//스마트에디터 초기화
							oEditors2.getById["ir2"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
						}
						fn.fileReset('#popup_html');
						fn.popupClose('#popup_html');
					}
				});
			},
			error: function () {
				alert("File Upload Error!!");
			}
		});
	} else {
		alert("등록 할 수 없는 파일 형식입니다.");
		return;
	} 
}

// 사용중지 클릭시
function goDisable() {
	$("#status").val("001");
	var param = $("#searchForm").serialize();
	$.getJSON("/ems/cam/campTempUpdateStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("사용중지 되었습니다.");
			campTempStatus = "001";
			
			$("#btnDisable").hide();
			$("#btnEnable").removeClass("hide");
			$("#btnEnable").show();
			
		} else if(data.result == "Fail") {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 복구 클릭시
function goEnable() {
	$("#status").val("000");
	var param = $("#searchForm").serialize();
	$.getJSON("/ems/cam/campTempUpdateStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복구 되었습니다.");
			campTempStatus = "000";
			
			$("#btnEnable").hide();
			$("#btnDisable").removeClass("hide");
			$("#btnDisable").show();
		} else if(data.result == "Fail") {
			alert("복구 처리중 오류가 발생하였습니다.");	// 복구실패
		}
	});
}

// 삭제 클릭시
function goDelete() {

	$("#status").val("002");
	var param = $("#searchForm").serialize();
	$.getJSON("/ems/cam/campTempUpdateStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다.");
			campTempStatus = "002";
			
			$("#searchForm").attr("target","").attr("action","./campTempListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("삭제중 오류가 발생하였습니다.");
		}
	});
}
 
// 복사 클릭시
function goCopy() {
	var param = $("#searchForm").serialize();
	$.getJSON("/ems/cam/campTempCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");

			if($("#apiSso").val != "Y") {
				// 메일 목록 페이지로 이동
				$("#searchForm").attr("target","").attr("action","/ems/cam/campTempListP.ums").submit();
			} 
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});
}

// 파일첨부 팝업창에서 등록 클릭시
var totalFileCnt = 0;
var totalFileByte = 0;
function attachFileUpload() {
	if($("#upTempAttachPath").val() == "") {
		alert("파일을 선택해주세요.");
		return;
	}
	
	// 파일 중복 체크
	var fileDupCheck = false;
	$("#campTempInfoForm input[name='attachNm']").each(function(idx,item){
		if($(item).val() == $("#upTempAttachPath").val()) {
			fileDupCheck = true;
		}
	});
	if(fileDupCheck) {
		alert("이미 등록된 파일입니다.");
		return;
	}
	
	// 첨부파일 갯수 체크
	if(totalFileCnt >= 5) {
		alert("파일 첨부는 최대 5개까지 등록 가능합니다.");
		return;
	}
	
	// 첨부파일 용량 체크
	var fileByte = $("#fileUrl2")[0].files[0].size;
	if(totalFileByte + fileByte > 10485760) {
		alert("첨부파일의 용량은 최대 10MB까지 입니다.");
		return;
	}
	
	var frm = $("#attachUpload")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		enctype: 'multipart/form-data',
		url: '/com/uploadFile.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (result) {
			alert("파일 업로드가 완료되었습니다.");
			
			var s = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB'],
				e = Math.floor(Math.log(fileByte) / Math.log(1024));
			var fileSizeTxt = (fileByte / Math.pow(1024, e)).toFixed(2) + " " + s[e];
			
			var attachFileHtml = "";
			attachFileHtml += '<li>';
			attachFileHtml += '<input type="hidden" name="attachNm" value="'+ result.oldFileName +'">';
			attachFileHtml += '<input type="hidden" name="attachPath" value="'+ result.newFileName +'">';
			attachFileHtml += '<input type="hidden" name="attachSize" value="'+ fileByte +'">';
			attachFileHtml += '<span>'+ result.oldFileName +'</span>';
			attachFileHtml += '<em>'+ fileSizeTxt +'</em>';
			attachFileHtml += '<button type="button" class="btn-del" onclick="deleteAttachFile(this)"><span class="hide">삭제</span></button>';
			attachFileHtml += '</li>';
			
			totalFileCnt++;
			totalFileByte += fileByte;
			
			$("#mailAttachFileList").append(attachFileHtml);
			
			$("#popFileDelete").click();
			frm.reset();
			
			fn.fileReset('#popup_file');
			fn.popupClose('#popup_file');
		},
		error: function (e) {
			alert("File Upload Error!!");
		}
	});
}

// 첨부파일 삭제
function deleteAttachFile(obj) {
	totalFileCnt--;
	var attachSize = $(obj).closest("li").children("input[name='attachSize']").val();
	totalFileByte = totalFileByte - attachSize;
	$(obj).closest("li").remove(); 
}

// 템플릿 코드 체크(중복 확인)
function checkEaiCampNo() {	
 
	if($("#eaiCampNo").val() == "") {
		alert("검색할 연계 템플릿 코드를 입력해주세요");
		return;
	} 
	
	var templateCd = $("#eaiCampNo").val();
	var num = templateCd.search(/[0-9]/g);
	var eng = templateCd.search(/[a-z]/ig);  
	var tid= $("#tid").val();
	
	if(templateCd.length < 2 || templateCd.length > 12){
		alert("3자리 ~ 12자리 이내로 입력해주세요"); 
		$("#eaiCampNo").focus();
		$("#eaiCampNo").select();
		return;
	}else if(templateCd.search(/\s/) != -1){	
		alert("템플릿코드는 공백 없이 입력해주세요");
		$("#eaiCampNo").focus();
		$("#eaiCampNo").select();
		return; 
	}else if( (num < 0 || eng < 0) ){
		alert("영문,숫자를 혼합하여 입력해주세요");
		$("#eaiCampNo").focus();
		$("#eaiCampNo").select(); 
		return;
	}
	
	$.getJSON("/ems/cam/campaignTemplateEaiCampNo.json?eaiCampNo=" + templateCd + "&tid=" + tid , function(data) {	
		if(data.result == "Success") {
			alert("사용가능한 연계 템플릿 코드입니다");	 
			$("#chkEaiCampNo").val("Y");
			$("#chkEaiCampNo").text("확인");
			$("#eaiCampNo").attr("disabled",true);
			$("#chkEaiCampNo").attr("disabled",true);	
		} else if(data.result == "Fail") {
			alert("이미 사용중인 연계 템플릿 코드입니다.");
		}
	}); 
}

// 수정 클릭시
function goUpdate() {
	if(campTempStatus == "002") {
		alert("삭제된 캠페인템플릿입니다.\n삭제된 캠페인템플릿은 수정할 수 없습니다.");
		return;
	}
	
	if($("#campNo").val() == "0") {
		alert("캠페인은 필수입력 항목입니다.");
		popCampSelect();
		return;
	}
	
	if($("#prohibitCheckResult").val() == "") {
		alert("금칙어 항목이 검증되지 않았습니다");	
		$("#chkProhibit").focus();
		return;
	} else {
		if($("#prohibitCheckResult").val() != "N") {
			alert("금칙어 항목이 있습니다 저장 하실수 없습니다");	
			return;
		}
	}
	
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() != "0" && $("#userId").val() == "") {
			alert("사용자명을 선택하세요.");
			$("#userId").focus();
			return;
		}
	}
	if($("#sname").val() == "") {
		alert("발송자명은 필수입력 항목입니다.");
		$("#sname").focus(); 
		return;
	}
	if($("#smail").val() == "") {
		alert("발송자 이메일은 필수입력 항목입니다.");
		$("#smail").focus();
		return;
	}
	
	if($("#webAgentUrl").val() == "" ){
		if($.trim($("#contentsTyp").val()) == "2") {	// 탬플릿컨텐츠
			$("#webAgentAttachYn").val("");
		} else {										// DB컨텐츠
			$("#webAgentAttachYn").val("");
		}
	} else {
		if($.trim($("#contentsTyp").val()) == "1") {		// 웹컨텐츠
			$("#webAgentAttachYn").val("N");
		} else if($.trim($("#contentsTyp").val()) == "2") {	// 탬플릿컨텐츠
			$("#webAgentAttachYn").val("Y");
		} else {											// DB컨텐츠
			$("#webAgentAttachYn").val("Y");
		}
	}
	
	if($("#tnm").val() == "") {
		alert("템플릿명은 필수입력 항목입니다.");
		$("#tnm").focus();
		return;
	}
	if($("#emailSubject").val() == "") {
		alert("메일제목은 필수입력 항목입니다.");
		$("#emailSubject").focus();
		return;
	}
	
	if($("input[name='infoCheckYn']").eq(0).is(":checked")) {
		$("#titleChkYn").val("Y");
	} else {
		$("#titleChkYn").val("N");
	}
	
	if($("input[name='infoCheckYn']").eq(1).is(":checked")) {
		$("#bodyChkYn").val("Y");
	} else {
		$("#bodyChkYn").val("N");
	}
	
	if($("input[name='infoCheckYn']").eq(2).is(":checked")) {
		$("#attachFileChkYn").val("Y");
	} else {
		$("#attachFileChkYn").val("N");
	}
	
	if($("#recvChkYn").is(":checked")) {
		$("#recvChkYn").val("Y");
	} else {
		$("#recvChkYn").val("N");
	}
	
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#serviceContent").val( $("#ir1").val() );

	if ( $("#prohibitCheckTitle").val() != $("#emailSubject").val()) {
		alert("제목이 변경되었습니다 금칙어를 확인해주세요");
		$("#chkProhibit").focus();
		return;
	}
	if ( $("#prohibitCheckText").val() != $("#serviceContent").val()) {
		alert("내용이 변경되었습니다 금칙어를 확인해주세요");
		$("#chkProhibit").focus();
		return;
	}
	
	
	if($("input[name='infoCheckYn']").eq(0).is(":checked")) {
		$("#titleChkYn").val("Y");
	} else {
		$("#titleChkYn").val("N");
	}
	
	if($("input[name='infoCheckYn']").eq(1).is(":checked")) {
		$("#bodyChkYn").val("Y");
	} else {
		$("#bodyChkYn").val("N");
	}
	
	if($("input[name='infoCheckYn']").eq(2).is(":checked")) {
		$("#attachFileChkYn").val("Y");
	} else {
		$("#attachFileChkYn").val("N");
	}
	
	var templateCd= $("#eaiCampNo").val();
	if (templateCd != "") {
		if ($("#chkEaiCampNo").val() != "Y"){
			alert("연계 템플릿 코드 중복 여부를 체크하셔야합니다");
			$("#chkEaiCampNo").focus();
			return;
		}
		$("#eaiCampNo").removeAttr("disabled");
	}
	
	$("#campTempInfoForm").attr("target","iFrmMail").attr("action","/ems/cam/campTempUpdate.ums").submit();
}

// 취소 클릭시
function goCancel() {
	$("#searchForm").attr("target","").attr("action","/ems/cam/campTempListP.ums").submit();
}

/*****************************************************************
* -------------------------------팝     업 -----------------------
*****************************************************************/
//+환경설정 팝업 
// 환경설정 클릭시
function popEnvSetting() {
	if($("#envSetAuth").val() == "N"){
		alert("환경 설정 조회 권한이 없습니다");
		return;
	}	
	fn.popupOpen('#popup_setting');
}

// 환경설정 팝업창에서 등록 클릭시
function popSettingSave() {
	if($("#mailFromNm").val() == "") {
		alert("발송자명을 입력하세요.");
		$("#mailFromNm").focus();
		return;
	}
	if($("#mailFromEm").val() == "") {
		alert("발송자 이메일을 입력하세요.");
		$("#mailFromEm").focus();
		return;
	}
	if($("#replyToEm").val() == "") {
		alert("반송 이메일을 입력하세요.");
		$("#replyToEm").focus();
		return;
	}
	if($("#returnEm").val() == "") {
		alert("리턴 이메일을 입력하세요.");
		$("#returnEm").focus();
		return;
	}
	$("#txtMailFromNm").html( $("#mailFromNm").val() );
	
	if($("input[name='infoCheckYn']").eq(0).is(":checked")) {
		$("#titleChkYn").val("Y");
	} else {
		$("#titleChkYn").val("N");
	}
	
	if($("input[name='infoCheckYn']").eq(1).is(":checked")) {
		$("#bodyChkYn").val("Y");
	} else {
		$("#bodyChkYn").val("N");
	}
	
	if($("input[name='infoCheckYn']").eq(2).is(":checked")) {
		$("#attachFileChkYn").val("Y");
	} else {
		$("#attachFileChkYn").val("N");
	}

	alert("환경설정이 완료되었습니다.");
	fn.popupClose('#popup_setting');
}
//-환경 설정 팝업

//+캠페인 팝업 
// 캠페인명 선택 클릭시(캠페인선택 팝업)
function popCampSelect() {
	$("#popCampSearchForm input[name='searchStartDt']").val("");
	$("#popCampSearchForm input[name='searchEndDt']").val("");
	$("#popCampSearchForm input[name='searchCampNm']").val("");
	goPopCampSearch("1");
	fn.popupOpen("#popup_campaign");
}

// 팝업 캠페인 목록 검색
function goPopCampSearch(pageNum) {
	$("#popCampSearchForm input[name='page']").val(pageNum);
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popCampList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContCamp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 신규등록 클릭시
function goPopCampAdd() {
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popCampAdd.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContCamp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 목록에서 캠페인명 클릭시
function goPopCampUpdate(campNo) {
	$("#popCampSearchForm input[name='campNo']").val(campNo);
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popCampUpdate.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContCamp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 목록에서 선택 클릭시
function setPopCampInfo(campNo, campNm, campTy) {
	$("#campNo").val(campNo);
	$("#campTy").val(campTy);
	$("#txtCampNm").html(campNm); 
	
	fn.popupClose("#popup_campaign");
}

// 팝업 캠페인 목록 페이징
function goPageNumPopCamp(pageNum) {
	goPopCampSearch(pageNum);
}

// 팝업 캠페인 등록 클릭시
function goPopCampInfoAdd() {
	if($("#popCampInfoForm select[name='campTy']").val() == "") {
		alert("캠페인목적은 필수입력 항목입니다.");
		$("#popCampInfoForm select[name='campTy']").focus();
		return;
	}
	if($("#popCampInfoForm input[name='campNm']").val() == "") {
		alert("캠페인명은 필수입력 항목입니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popCampInfoForm input[name='campNm']").val()) > 100) {
		alert("캠페인명은 100byte를 넘을 수 없습니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		$("#popCampInfoForm input[name='campNm']").select();
		return;
	}
	if($.byteString($("#popCampInfoForm textarea[name='campDesc']").val()) > 4000) {
		alert("캠페인 설명은 4000byte를 넘을 수 없습니다.");
		$("#popCampInfoForm textarea[name='campDesc']").focus();
		$("#popCampInfoForm textarea[name='campDesc']").select();
		return;
	}
	// 등록 처리
	var frm = $("#popCampInfoForm")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		url: '/ems/cam/campAdd.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (res) {
			if(res.result == "Success") {
				alert("등록되었습니다.");
				goPopCampList();
			} else {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		},
		error: function () {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// 팝업 캠페인 신규등록화면/수정화면 목록 클릭시
function goPopCampList() {
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popCampList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContCamp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 수정 클릭시
function goPopCampInfoUpdate() {
	if($("#popCampInfoForm select[name='campTy']").val() == "") {
		alert("캠페인목적은 필수입력 항목입니다.");
		$("#popCampInfoForm select[name='campTy']").focus();
		return;
	}
	if($("#popCampInfoForm select[name='status']").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#popCampInfoForm select[name='status']").focus();
		return;
	}
	if($("#popCampInfoForm input[name='campNm']").val() == "") {
		alert("캠페인명은 필수입력 항목입니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popCampInfoForm input[name='campNm']").val()) > 100) {
		alert("캠페인명은 100byte를 넘을 수 없습니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		$("#popCampInfoForm input[name='campNm']").select();
		return;
	}
	if($.byteString($("#popCampInfoForm textarea[name='campDesc']").val()) > 4000) {
		alert("캠페인 설명은 4000byte를 넘을 수 없습니다.");
		$("#popCampInfoForm textarea[name='campDesc']").focus();
		$("#popCampInfoForm textarea[name='campDesc']").select();
		return;
	}
	// 수정 처리
	var frm = $("#popCampInfoForm")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		url: '/ems/cam/campUpdate.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (res) {
			if(res.result == "Success") {
				alert("수정되었습니다.");
				goPopCampList();
			} else {
				alert("수정 처리중 오류가 발생하였습니다.");
			}
		},
		error: function () {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}
//-캠페인 팝업 

//+탬플릿 팝업
// 탬플릿명 선택 클릭시(탬플릿선택 팝업)
function popTempSelect() {
	$("#popTempSearchForm input[name='searchStartDt']").val("");
	$("#popTempSearchForm input[name='searchEndDt']").val("");
	$("#popTempSearchForm input[name='searchTempNm']").val("");
	goPopTempSearch("1");
	fn.popupOpen("#popup_template");
}

// 팝업 탬플릿 목록 검색
function goPopTempSearch(pageNum) {
	$("#popTempSearchForm input[name='page']").val(pageNum);
	var param = $("#popTempSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popTempList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContTemp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 탬플릿 신규등록 클릭시
function goPopTempAdd() {
	var param = $("#popTempSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popTempAdd.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContTemp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 탬플릿 목록에서 탬플릿명 클릭시
function goPopTempUpdate(tempNo) {
	$("#popTempSearchForm input[name='tempNo']").val(tempNo);
	var param = $("#popTempSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popTempUpdate.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContTemp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 탬플릿 목록에서 선택 클릭시
function setPopTempInfo(tempNo, tempNm, tempFlPath) {
	$("#txtTempNm").html(tempNm);
	$.getJSON("/ems/tmp/tempFileView.json?tempFlPath=" + tempFlPath, function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);    //스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);   //스마트에디터 내용붙여넣기
	
			fn.popupClose("#popup_template");
		} else {
			alert("Template Read Error!!");
		}
	});
}

// 팝업 탬플릿 목록 페이징
function goPageNumPopTemp(pageNum) {
	goPopTempSearch(pageNum);
}

// 팝업 탬플릿 등록 클릭시
function goPopTempInfoAdd() {
	if($("#popTempInfoForm input[name='tempNm']").val() == "") {
		alert("탬플릿명은 필수입력 항목입니다.");
		$("#popTempInfoForm input[name='tempNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popTempInfoForm input[name='tempNm']").val()) > 100) {
		alert("탬플릿명은 100byte를 넘을 수 없습니다.");
		$("#popTempInfoForm input[name='tempNm']").focus();
		$("#popTempInfoForm input[name='tempNm']").select();
		return;
	}
	if($.byteString($("#popTempInfoForm textarea[name='tempDesc']").val()) > 200) {
		alert("탬플릿 설명은 4000byte를 넘을 수 없습니다.");
		$("#popTempInfoForm textarea[name='tempDesc']").focus();
		$("#popTempInfoForm textarea[name='tempDesc']").select();
		return;
	}
	
	// 등록 처리
	oEditors2.getById["ir2"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#popTempInfoForm input[name='tempVal']").val( $("#ir2").val() );
	var frm = $("#popTempInfoForm")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		url: '/ems/tmp/tempAdd.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (res) {
			if(res.result == "Success") {
				alert("등록되었습니다.");
				goPopTempList();
			} else {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		},
		error: function () {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// 팝업 탬플릿 신규등록화면/수정화면 목록 클릭시
function goPopTempList() {
	var param = $("#popTempSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popTempList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContTemp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 탬플릿 파일을 읽어 편집기에 값 설정
function setPopTempContent(tempFlPath) {
	$.getJSON("/ems/tmp/tempFileView.json?tempFlPath=" + tempFlPath, function(res) {
		if(res.result == 'Success') {
			oEditors2.getById["ir2"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
			oEditors2.getById["ir2"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
		}
	});
}

// 팝업 탬플릿 수정 클릭시
function goPopTempInfoUpdate() {
	if($("#popTempInfoForm select[name='status']").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#popTempInfoForm select[name='status']").focus();
		return;
	}
	if($("#popTempInfoForm input[name='tempNm']").val() == "") {
		alert("탬플릿명은 필수입력 항목입니다.");
		$("#popTempInfoForm input[name='tempNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popTempInfoForm input[name='tempNm']").val()) > 100) {
		alert("탬플릿명은 100byte를 넘을 수 없습니다.");
		$("#popTempInfoForm input[name='tempNm']").focus();
		$("#popTempInfoForm input[name='tempNm']").select();
		return;
	}
	if($.byteString($("#popTempInfoForm textarea[name='tempDesc']").val()) > 200) {
		alert("탬플릿 설명은 4000byte를 넘을 수 없습니다.");
		$("#popTempInfoForm textarea[name='tempDesc']").focus();
		$("#popTempInfoForm textarea[name='tempDesc']").select();
		return;
	}
	
	// 수정 처리
	oEditors2.getById["ir2"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#popTempInfoForm input[name='tempVal']").val( $("#ir2").val() );
	var frm = $("#popTempInfoForm")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		url: '/ems/tmp/tempUpdate.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (res) {
			if(res.result == "Success") {
				alert("수정되었습니다.");
				goPopTempList();
			} else {
				alert("수정 처리중 오류가 발생하였습니다.");
			}
		},
		error: function () {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}
//-템플릿 팝업

//+수신자그룹 팝업
// 수신자그룹 선택 클릭시(수신자그룹선택 팝업)
function popSegSelect() {

	$("#popSegSearchForm input[name='searchStartDt']").val("");
	$("#popSegSearchForm input[name='searchEndDt']").val("");
	$("#popSegSearchForm input[name='searchSegNm']").val("");
	goPopSegSearch("1");
	fn.popupOpen("#popup_camp_temp_segment");
}

// 팝업 수신자그룹 목록 검색
function goPopSegSearch(pageNum) {
	$("#popSegSearchForm input[name='page']").val(pageNum);
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popCampTempSegList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContSeg").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹 신규등록 클릭시
function goPopSegAdd() {
	var segAddUrl = "";
	if($("#popSegSearchForm select[name='addCreateTy']").val() == "") {
		alert("신규등록 할 유형을 선택해주세요.");
		$("#popSegSearchForm select[name='addCreateTy']").focus();
		return;
	}
	if($("#popSegSearchForm select[name='addCreateTy']").val() == "003") {
		segAddUrl = "/ems/cam/pop/popSegAddFile.ums";
	} else {
		segAddUrl = "/ems/cam/pop/popSegAddSql.ums";
	}
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : segAddUrl + "?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContSeg").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹 목록에서 수신자그룹명 클릭시
function goPopSegUpdate(segNo, createTy) {
	var segUpdateUrl = "";
	if(createTy == "003") {
		segUpdateUrl = "/ems/cam/pop/popSegUpdateFile.ums";
	} else if(createTy == "002") {
		segUpdateUrl = "/ems/cam/pop/popSegUpdateSql.ums";
	}
	$("#popSegSearchForm input[name='segNo']").val(segNo);
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : segUpdateUrl + "?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContSeg").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹 목록에서 선택 클릭시
function setPopSegInfo(segNoc, segNm) {
	$("#segNoc").val(segNoc);
	$("#txtSegNm").html(segNm);
	
	// 머지입력 초기화
	$("#mergeKeyCampTemp").children().remove();
	
	var condHTML = "";
	var merge = segNoc.substring(segNoc.indexOf("|")+1);
	var pos = merge.indexOf(",");
	while(pos != -1) {
		condHTML = "<option value='$:"+merge.substring(0,pos)+":$'>"+merge.substring(0,pos)+"</option>";
		$("#mergeKeyCampTemp").append(condHTML);
		merge = merge.substring(pos+1);
		pos = merge.indexOf(",");
	}
	condHTML = "<option value='$:"+merge+":$'>"+merge+"</option>";
	$("#mergeKeyCampTemp").append(condHTML);
	
	fn.popupClose("#popup_camp_temp_segment");
}

// 팝업 수신자그룹 목록 페이징
function goPageNumPopSeg(pageNum) {
	goPopSegSearch(pageNum);
}
 
// 대상수 구하기
function goSegCnt() {
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segCount.json?" + param, function(data) {
		$("#txtTotCnt").html(data.totCnt + "명");
		$("#totCnt").val(data.totCnt);
	});
}
 

// 팝업 탬플릿 신규등록화면/수정화면 목록 클릭시
function goPopSegList() {
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popSegList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContSeg").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
} 

//금칙어 체크 
function checkProhibit(){
	
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);	
	$("#serviceContent").val($("#ir1").val());	
	
	var frm = $("#campTempInfoForm")[0];
	var frmData = new FormData(frm);
	
	$.ajax({
		type: "POST",
		url: '/ems/cam/checkCampTempProhibitWordApi.ums',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (data) {
			if(data.result == 'Success') {
				if(data.resultMsg != ""){
					alert("금칙어 확인 결과 : " + data.resultMsg ) 
				} 
				$("#prohibitCheckTitle").val( $("#emailSubject").val());
				$("#prohibitCheckText").val( $("#ir1").val()); 
				$("#prohibitCheckResult").val( data.resultCode); 
				$("#txtProhibitDesc").text( data.resultMsg ); 
			} else {
				alert("금칙어 확인에 실패하였습니다 .");
			}
		},
		error: function () {
			alert("금칙어 확인에 실패하였습니다 .");
		}
	});
}

// 메일 파일을 읽어 편집기에 값 설정
function setMailContent(contFlPath) {
	$.getJSON("/ems/cam/mailFileView.json?contFlPath=" + contFlPath, function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.contVal]);	//스마트에디터 내용붙여넣기
			oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);				
			$("#prohibitCheckText").val( $("#ir1").val());
		}
	});
} 