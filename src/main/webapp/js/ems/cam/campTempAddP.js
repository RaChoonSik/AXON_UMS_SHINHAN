/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2022.09.27
*	설명 : API 템플릿 신규등록 JavaScript
**********************************************************/

var dateRegex = RegExp(/^\d{4}.(0[1-9]|1[012]).(0[1-9]|[12][0-9]|3[01])$/);
var checkDateInput = false;

$(document).ready(function() {
	// 머지 설정
	// goMerge();
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

// 수신자그룹을 선택하였을 경우 머지부분 처리
function goMerge() {
	if($("#segNoc").val() == "") {
		return;
	}
	
	// 머지입력 초기화
	$("#mergeKeyCampTemp").children().remove();
	
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

// 사용자그룹 선택시 사용자 목록 조회 
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
	
	var param = $("#mailInfoForm").serialize();
	$.getJSON("/ems/cam/webAgentPreview.json?" + param, function(res) {
		if(res.result == 'Success') {
			iFrmWebAgent.location.href = res.webAgentUrl;
			fn.popupOpen('#popup_preview_webagent');
		} else {
			alert("웹에이전트 미리보기 처리중 오류가 발생하였습니다.");	
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
			attachFileHtml += '<span>'+ result.oldFileName +'</span>';
			attachFileHtml += '<em>'+ fileSizeTxt +'</em>';
			attachFileHtml += '<button type="button" class="btn-del" onclick="deleteAttachFile(this)"><span class="hide">삭제</span></button>';
			attachFileHtml += '</li>';
			
			totalFileCnt++;
			totalFileByte += fileByte;
			
			$("#mailAttachFileList").append(attachFileHtml);
			
			$("#popFileDelete").click();
			frm.reset();
			
			$("#prohibitCheckAttachChange").val("Y");
			
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
	$("#prohibitCheckAttachChange").val("Y");
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
	var tid = 0;
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

// 등록 클릭시 (캠페인 템플릿 등록)
function goAdd () { 
	
	if($("#campNo").val() == "0") {
		alert("캠페인을 선택하세요.");
		popCampSelect();
		return;
	}
	
	if($("#checkTid").val() == "N") {
		alert("템플릿ID 중복검사를 해야합니다 템플릿ID옆의 중복검사 버튼을 클릭하여 ID중복검사를 해주세요\n");	
		errflag = true;
	}
	
	/*
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		popSegSelect();
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
	*/
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() == "0"){
			alert("사용자그룹을 선택하세요.");
			$("#userId").focus();
			return;
		}
		if($("#deptNo").val() != "0" && $("#userId").val() == "") {
			alert("사용자명을 선택하세요.");
			$("#userId").focus();
			return;
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
	 
	/*
	var templateCd= $("#eaiCampNo").val();
	if (templateCd != "") {
		if ($("#chkEaiCampNo").val() != "Y"){
			alert("연계 템플릿 코드 중복 여부를 체크하셔야합니다");
			$("#chkEaiCampNo").focus();
			return;
		}
		$("#eaiCampNo").removeAttr("disabled");
	}
	*/
	
	$("#tid").removeAttr("disabled");
	// 스마트에디터 편집기 내용 serviceContent로 복사
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#serviceContent").val( $("#ir1").val() );	
	
	/*
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
	*/
	$("#campTempInfoForm").attr("target","iFrmMail").attr("action","/ems/cam/campTempAdd.ums").submit();
	
}

// 취소 클릭시(목록으로 이동)
function goCancel() {
	//alert("등록이 취소되었습니다.");
	document.location.href = "/ems/cam/campTempListP.ums";
}

/*****************************************************************
* -------------------------------팝     업 -----------------------
*****************************************************************/
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

// 팝업 수신자 그룹   목록 클릭시
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

//TID중복 체크 
function checkTid(){
	
	var reg_id =/^[0-9]{1,8}$/; 
	var tid= $("#tid").val().trim(); 
	tid.replace(" " , "");
	
	if(tid == "") {
		alert("검색할 API 템플릿 번호를 입력해주세요");
		return;
	}
	
	if(!reg_id.test(tid)) {
		alert("API 템플릿 번호는 숫자로 1~5자로 입력하셔야합니다");
		$("#tid").focus();
		$("#tid").select();
		return; 
	} else{
		var  nTid = Number(tid) ;
		if (nTid >= 99999 || nTid < 1 ) {
			alert("API 템플릿 번호는 0 이상 99999 이하의 숫자만 가능합니다");
			$("#tid").focus();
			$("#tid").select();	
			return;
		}
	}
	
	$.getJSON("/ems/cam/campaignTemplateId.json?tid=" + tid , function(data) {
		if(data.result == "Success") {
			alert("사용가능한 API 템플릿 번호입니다.");	 
			$("#chkTid").val("Y");
			$("#chkTid").text("중복확인 완료");
			$("#tid").attr("disabled",true);
			$("#chkTid").attr("disabled",true);
		} else if(data.result == "Fail") {
			alert("이미 사용중인 API 템플릿 번호입니다.");
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