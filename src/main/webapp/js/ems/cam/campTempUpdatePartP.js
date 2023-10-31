/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2022.09.28
*	설명 : 캠페인템플릿  정보 부분 수정 JavaScript
**********************************************************/


function setCampTempContent(contentsTyp, contentsPath) {
	if($.trim(contentsTyp) == "0" || $.trim(contentsTyp) == "2")
	$.getJSON("./campTempFileView.json?contentsPath=" + contentsPath, function(res) {
		if(res.result == 'Success') {
			$("#serviceContents").html( res.contVal );
		} else{
			$("#serviceContents").html("<span>캠페인템플릿 미리보기에 실패했습니다</span>");
		}
	});
}
 
// 웹에이전트 미리보기 클릭시
function popWebAgentPreview() {
	if($("#webAgentUrl").val() == "") {
		alert("등록된 웹에이전트가 없습니다.");
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
	
/*	var param = $("#campTempInfoForm").serialize();
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
		url: './webAgentPreview.json',
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

// 사용중지 클릭시
function goDisable() {
	$("#status").val("001");
	var param = $("#searchForm").serialize();
	$.getJSON("./campTempUpdateStatus.json?" + param, function(data) {
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
	$.getJSON("./campTempUpdateStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복구 되었습니다.");
			/*campTempStatus = "000";
			
			$("#btnEnable").hide();
			$("#btnDisable").removeClass("hide");
			$("#btnDisable").show();*/
			$("#searchForm", parent.document).attr("target","").attr("action","./campTempUpdatePartP.ums").submit();
		} else if(data.result == "Fail") {
			alert("복구 처리중 오류가 발생하였습니다.");	// 복구실패
		}
	});
}

// 삭제 클릭시
function goDelete() {

	$("#status").val("002");
	var param = $("#searchForm").serialize();
	$.getJSON("./campTempUpdateStatus.json?" + param, function(data) {
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
	$.getJSON("./campTempCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");
			
			// 메일 목록 페이지로 이동
			$("#searchForm").attr("target","").attr("action","./campTempListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});
}
	
// 수정 클릭시
function goUpdate() {

	if(campTempStatus == "002") {
		alert("삭제된 메일입니다.\n삭제된 메일은 수정할 수 없습니다.");
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
	
	$("#campTempInfoForm").attr("target","iFrmMail").attr("action","./campTempUpdatePart.ums").submit();
	
}

// 취소 클릭시
function goCancel() {
	$("#searchForm").attr("target","").attr("action","./campTempListP.ums").submit();
}
 
//준법심의 결과 보기
function goPopProbibitInfo(tid) {
	
	var param = "tid=" + tid;
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./pop/popCampTempProhibitInfo.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopProhibitInfo").html(pageHtml);
		},
		error : function(){
			alert("준법심의 결과 조회에 실패했습니다!");
		}
	}); 
	fn.popupOpen("#popup_prohibit_info");
}
