/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.09.01
*	설명 : 자동메일 서비스 신규등록 JavaScript
**********************************************************/


$(document).ready(function(){
	//발송결재라인 등록 팝업 내 사용자
	$(document).on("click", ".content-area .user button", function(){
		if($(this).closest("li").hasClass("active")){
			$(this).closest("li").removeClass("active");
		}else{
			$(this).closest("li").addClass("active").siblings().removeClass("active");
		}
	});
	
	// 발송결재라인등록 팝업창에서 사용자명 입력 엔터키 처리
	$("#searchUserNm").keypress(function(e) {
		if(e.which == 13) {
			popSearchUser();
		}
	});
	
	//드래그앤드롭
	$("#apprUserList").sortable();
});

// 템플릿을 선택하였을 경우 에디터에 템플릿 넣기
function goTemplate() {
	if($("#tempFlPath").val() == "") {
		alert("탬플릿을 선택하세요.");
		return;
	}
	
	$.getJSON("../tmp/tempFileView.json?tempFlPath=" + $("#tempFlPath").val(), function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);    		//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);   //스마트에디터 내용붙여넣기
			
			$("#textContent").text(res.tempVal);
			$("#textContent").text($("#textContent").text());
		} else {
			alert("Template Read Error!!");
		}
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
	$("#txtWebAgentUrl").html($("#webAgentUrl").val());
	
	if($("#webAgentAttachYn").val() == "Y") {
		$("#txtWebAgentUrl").html("첨부파일로 지정되었습니다.");
	} else {
		var contents = "^:" + $("#webAgentUrl").val() + ":^";
		oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
		oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);		//스마트에디터 내용붙여넣기
		$("#txtWebAgentUrl").html("본문삽입으로 지정되었습니다.");
	}
		
	/*
	if($("#webAgentAttachYn").is(":checked") == false) {
		var contents = "^:" + $("#webAgentUrl").val() + ":^";
		oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);   //스마트에디터 내용붙여넣기
		alert("웹에이전트가 등록되었습니다.");
	} else {
		alert("첨부파일로 지정되었습니다.");
	}
	*/
	fn.popupClose('#popup_web_agent');
}

// 웹에이전트 팝업창에서 취소 클릭시
function popWebAgentCancel() {
	$("#webAgentUrl").val("");
	$("#txtWebAgentUrl").html("URL(첨부파일) 형식이 지정되지 않았습니다.");
	//$("#webAgentAttachYn").prop("checked", false);
	//alert("웹에이전트 등록이 취소되었습니다.");
	fn.popupClose('#popup_web_agent');
}


// 웹에이전트 첨부파일/본문삽입 선택시
function changeAttachYn() {
	if($("#webAgentUrl").val() != "") {
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
	
	$("#iFrmWebAgent").empty();
	
	$("#previewWebAgentUrl").html($("#webAgentUrl").val());
	
	var param = $("#serviceInfoForm").serialize();
	$.getJSON("./webAgentPreview.json?" + param, function(res) {
		if(res.result == 'Success') {
			iFrmWebAgent.location.href = res.webAgentUrl;
			fn.popupOpen('#popup_preview_webagent');
		} else {
			alert("보안메일 미리보기 처리중 오류가 발생하였습니다.");
		}
	});
}


// 제목 클릭시(메일제목 내용 추가)
function goTitleMerge() {
	if($("#mergeKey").val() == "") {
		alert("머지입력을 선택하세요.");
		return;
	}
	if($("#emailSubject").val() == "") {
		$("#emailSubject").val( $("#mergeKey").val() );
	} else {
		var cursorPosition = $("#emailSubject")[0].selectionStart;
		$("#emailSubject").val( $("#emailSubject").val().substring(0,cursorPosition) + $("#mergeKey").val() + $("#emailSubject").val().substring(cursorPosition) );
	}
}

//본문 클릭시(메일 내용 추가)
function goContMerge() {
	if($("#mergeKey").val() == "") {
		alert("머지입력을 선택하세요.");
		return;
	}
	oEditors.getById["ir1"].exec("PASTE_HTML", [$("#mergeKey").val()]);   
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
			url: '../../com/uploadFileDir.json',
			data: frmData,
			processData: false,
			contentType: false,
			success: function (result) {
				alert("HTML 업로드가 완료되었습니다.");
				$("#vFileName").val(result.oldFileName);
				$("#rFileName").val(result.newFileName);
				
				// 편집기에 파일내용 표시
				$.getJSON("../tmp/tempFileView.json?tempFlPath=" + encodeURIComponent("html/" + userId + "/" + result.newFileName), function(res) {
					if(res.result == 'Success') {
						oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
						oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
						fn.fileReset('#popup_html');
						fn.popupClose('#popup_html');
					}
				});
			},
			error: function (e) {
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
	$("#serviceInfoForm input[name='attachNm']").each(function(idx,item){
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
		url: '../../com/uploadFileDir.json',
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

// 등록 클릭시 (서비스 등록)
function goServiceAdd() {
	if($("#contentsTyp").val() == "") {
		alert("콘텐츠타입은 필수입력 항목입니다.");
		$("#contentsTyp").focus();
		return;
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
		fn.popupOpen('#popup_setting');
		return;
	}
	
	if($("#smail").val() == "") {
		alert("발송자 이메일은 필수입력 항목입니다.");
		$("#smail").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	
	// 콘텐츠 타입에 따른 처리
	if($.trim($("#contentsTyp").val()) == "1") {
		if($("#webAgentUrl").val() == "") {
			alert("보안메일항목은 필수입력 항목입니다.");
			fn.popupOpen('#popup_web_agent');
			return;
		}
	}
	
	/*if($("#prohibitChkTyp").val() == "000") {
		alert("준법심의 항목이 검증되지 않았습니다");	
		return;
	}*/
		
	/*if($("#approvalYn").val() == "N") {
		alert("발송 결재라인을 등록하세요");
		popMailApproval();
		return;
	}*/
	
	/*
	if($.trim($("#contentsTyp").val()) == "1") {		// 웹컨텐츠
		$("#webAgentAttachYn").val("N");
	} else if($.trim($("#contentsTyp").val()) == "2") {	// 탬플릿컨텐츠
		$("#webAgentAttachYn").val("Y");
	} else {											// DB컨텐츠
		$("#webAgentUrl").val("");
		$("#webAgentAttachYn").val("");
	}
	*/
	if($("#webAgentUrl").val() == "" ){
		 if($.trim($("#contentsTyp").val()) == "2") {	// 탬플릿컨텐츠
			$("#webAgentAttachYn").val("");
		} else {											// DB컨텐츠
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
		alert("서비스명은 필수입력 항목입니다.");
		$("#tnm").focus();
		return;
	}
	
	if($("#emailSubject").val() == "") {
		alert("메일제목은 필수입력 항목입니다.");
		$("#emailSubject").focus();
		return;
	}
 
	
	// 스마트에디터 편집기 내용 composerValue로 복사
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#serviceContent").val( $("#ir1").val() );	
	
	//확정하고 다시 변경 했을수도 있으므로 본문 및 제목이 변경되었는지 확인
	/*if ( $("#prohibitCheckTitle").val() != $("#emailSubject").val()) {
		alert ("제목이 변경되었습니다 준법심의를 확인해주세요");
		return; 
	}*/
	/*if ( $("#prohibitCheckText").val() != $("#serviceContent").val()) {
		alert ("본문이 변경되었습니다 준법심의를 재확인해주세요");
		return; 
	}*/
	
	var orgText = $("#serviceContent").val();
	var imgText = orgText.replace("/img/upload/kjtemplate/", "");
	var imgIndex = imgText.indexOf("/img/upload/");
	var checkValue = ""; 
	
	if(imgIndex > - 1) {
		checkValue ="Y";
	} else {
		checkValue ="N";
	}
	/*if ($("#prohibitCheckImg").val() != checkValue ) {
		alert("이미지 포함여부가 변경되었습니다 준법심의를 재확인해주세요");
		return;
	} else {
		$("#imgChkYn").val($("#prohibitCheckImg").val());
	}*/
	
	// 첨부파일  갯수 
	var attachLength = $("#mailAttachFileList input[name='attachNm']").length; 
	if(attachLength == 0 ){
		checkValue ="N";
	} else {
		checkValue ="Y";
	}

	/*if ($("#prohibitCheckAttach").val() != checkValue ) {
		alert("첨부파일 정보가 변경되었습니다 준법심의를 재확인해주세요");
		return;
	}*/
	
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
	
	$("#serviceInfoForm").attr("target","iFrmService").attr("action","./serviceAdd.ums").submit();
}

// 취소 클릭시(목록으로 이동)
function goServiceCancel() {
	alert("등록이 취소되었습니다.");
	document.location.href = "./serviceListP.ums";
} 
 
/*
function popProhibit(){
	setProhibitLine();
	//popProhibitReset();
	//내용 변경
	// 금지어  -  본문에서  : double quot 제외한 정보
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	var orgText = $("#ir1").val();
	console.log(orgText);
	//var removeDblQuote = orgText.replace(/^"(.+(?="$))"$/, '$1');
	var removeDblQuote = orgText.replace(/\"/gi, "");
	
	console.log(removeDblQuote);
	$("#prohibitText").val(removeDblQuote);
	
	// 이미지  포함 여부 --  본문에서 “/img/upload/kjtemplate/” 을 제외하고 “/img/upload/” 
	var imgText = orgText.replace("/img/upload/kjtemplate/", "");
	var imgIndex = imgText.indexOf("/img/upload/");
	if(imgIndex > - 1) {
		$("#popProhibitImage").text("본문 이미지 포함");
	} else {
		$("#popProhibitImage").text("본문 이미지 없음");
	}
	
	// 첨부파일  갯수 
	var attachLength = $("#mailAttachFileList input[name='attachNm']").length; 
	if(attachLength == 0 ){
		$("#popProhibitAttach").text("첨부파일 없음");
	} else {
		$("#mailAttachFileList input[name='attachNm']").each(function(idx,item){
			if (idx == 0) {
				if (attachLength == 1) {
					$("#popProhibitAttach").text($(item).val());
				} else {
					$("#popProhibitAttach").text($(item).val() + " 외 " + (attachLength -1) + "건");
				}
			}
		});
	}
	
	chkForbiddenWord();
}
*/

function popProhibit(){
	setProhibitLine();
 
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);	
	$("#serviceContent").val($("#ir1").val());	
	
	// 이미지  포함 여부 --  본문에서 “/img/upload/kjtemplate/” 을 제외하고 “/img/upload/” 
	var orgText = $("#serviceContent").val();
	var imgText = orgText.replace("/img/upload/kjtemplate/", "");
	var imgIndex = imgText.indexOf("/img/upload/");
	if(imgIndex > - 1) {
		$("#imgChkYn").val("Y");
	} else {
		$("#imgChkYn").val("N");
	}
	 
	
	var frm = $("#serviceInfoForm")[0];
	var frmData = new FormData(frm);
	
	$.ajax({
		type: "POST",
		url: '/rns/svc/pop/popRnsProhibtWordApi.ums',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (data) {
			$("#divPopRnsProhit").html(data);
			fn.popupOpen("#popup_rns_prohibit");
		},
		error: function () {
			alert("Pop Prohibit Page Loading Error!!");
		}
	});
	
	/*var param = $("#serviceInfoForm").serialize();
	 
	console.log(param);
	$.ajax({
		type : "POST",
		url : "/rns/svc/pop/popRnsProhibtWordApi.ums?" + param,
		dataType : "html", 
		success : function(pageHtml){
			$("#divPopRnsProhit").html(pageHtml);
			fn.popupOpen("#popup_rns_prohibit");
		},
		error : function(){
			alert("Pop Prohibit Page Loading Error!!");
		}
	});	
	*/
}


//준법심의 확정 
function popRnsProhibitSave(){
	var prohibit = false;
	var prohibitDesc = "";
	
	var isProhibitCheckCnt = 0;
	var isProhibitCheckImg = false;
	var isProhibitCheckAttach = 0;

	//준법심의 여부인지 먼저 확인 
	isProhibitCheckCnt = parseInt($("#popProhibitTitleCnt").val()) + parseInt( $("#popProhibitTextCnt").val());
	if (isProhibitCheckCnt > 0) prohibit = true;  
 	//이미지정보 
	var orgText = $("#ir1").val();
	var imgText = orgText.replace("/img/upload/kjtemplate/", "");
	var imgIndex = imgText.indexOf("/img/upload/");
	if(imgIndex > - 1) {
		isProhibitCheckImg = true;
		prohibit = true;
	}
	
	// 첨부파일
	var isProhibitCheckAttach = $("#mailAttachFileList input[name='attachNm']").length; 
	if (isProhibitCheckAttach > 0) prohibit = true;  

	if (prohibit) {
		if ($("#prohibitLineCheck").val() == "N" ) {
			alert("준법심의 결재라인이 등록되어 있지 않습니다 공통설정에서 준법심의 결재라인을 등록해주세요.");
			return;
		}
	}
	
	//POPUP의 정보 메인페이지로 가져온다 
	//확정당시의 준법심의 대상 항목을 저장한다
	$("#prohibitTitleCnt").val($("#popProhibitTitleCnt").val());
	$("#prohibitTextCnt").val($("#popProhibitTextCnt").val());
	$("#prohibitTitleDesc").val($("#popProhibitTitleDesc").val());
	$("#prohibitTextDesc").val($("#popProhibitTextDesc").val());
	
	//금지어 
	$("#prohibitCheckTitle").val( $("#emailSubject").val());
	$("#prohibitCheckText").val( $("#ir1").val());
	//이미지정보 
	var orgText = $("#ir1").val();
	var imgText = orgText.replace("/img/upload/kjtemplate/", "");
	var imgIndex = imgText.indexOf("/img/upload/");
	if(isProhibitCheckImg) {
		$("#prohibitCheckImg").val("Y");
		prohibitDesc = "이미지 /";
	} else {
		$("#prohibitCheckImg").val("N");
	}
	
	// 첨부파일
	if(isProhibitCheckAttach > 0 ){
		$("#prohibitCheckAttach").val("Y");
		prohibitDesc += "첨부 파일(" + isProhibitCheckAttach +"건) /";
	} else {
		$("#prohibitCheckAttach").val("N");
	}
	$("#prohibitCheckAttachChange").val("N");

	if (isProhibitCheckCnt > 0){
		prohibitDesc += "금지어(" + isProhibitCheckCnt +"건)/"
	}
		
	if(prohibit) {
		setProhibitLine();
		$("#prohibitChkTyp").val("002");
		prohibitDesc = prohibitDesc.slice(0, -1);
		
	} else {
		$("#prohibitUserList").empty();
		$("#prohibitLineCheck").val("N");
		$("#prohibitChkTyp").val("001");
		prohibitDesc = "준법심의 해당 항목이 없습니다"
	} 
	
	$("#txtProhibitYn").html(prohibitDesc);
	fn.popupClose('#popup_rns_prohibit');
}

//준법심의 취소
function popRnsProhibitCancel() {
	fn.popupClose('#popup_rns_prohibit');
}

function popProhibitReset() {	
	$("#prohibitChkTyp").val("000");
	$("#prohibitTextCnt").val("0");
	$("#prohibitTextDesc").val("");
	$("#prohibitTitleCnt").val("0");
	$("#prohibitTitleDesc").val("");
	$("#prohibitCheckAttach").val("N");
	$("#prohibitCheckImg").val("N");
}

// 준법심의 결재라인 가져오기 
function setProhibitLine() { 
	$("#prohibitUserList").empty();
	$("#prohibitLineCheck").val("N");
	$.getJSON(umsContext + "/ems/cam/getProhibitUserListSearch.json", function(data) {
		// 사용자 목록 설정
		$.each(data.prohibitLineList, function(idx,item){
			var userHtml = "";
			var checkapprUsrId = item.apprUserId +'|' + item.orgCd +'|' + item.positionGb +'|' + item.jobGb ;
			$("#apprUserList input[name='apprUserId']").each(function(idx,item){
				if($(item).val() == checkapprUsrId) {
					validUser = false; 
				}
			});
			userHtml += '<tr>';
			userHtml += '<td><input type="hidden" name="prohibitUserId" value="' + item.apprUserId +'|' + item.orgCd +'|' + item.positionGb +'|' + item.jobGb +'">' + item.apprUserNm + '</td>';
			userHtml += '<td style="display:none;"><input type="hidden" name="prohibitUserNm" value="' + item.apprUserNm +'/' + item.orgNm +'/' + item.jobNm +'"></td>';
			userHtml += '<td>' + item.orgNm +'</td>';
			userHtml += '<td>' + item.jobNm +'</td>';
			userHtml += '</tr>';
			$("#prohibitUserList").append(userHtml);
			$("#prohibitLineCheck").val("Y");
		});
	});
}

// 준법심의 결재라인 존재유무 확인  
function getProhibitLine() { 
	var existProhibitLine = false;
	$.getJSON(umsContext + "/ems/cam/getProhibitUserListSearch.json", function(data) {
		if(data.prohibitLineList.length > 0 ) {
			existProhibitLine = true;
		} else {
			existProhibitLine = false;
		}
	});
	
	return existProhibitLine;
}
