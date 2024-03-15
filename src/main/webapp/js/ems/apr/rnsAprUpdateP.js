/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2021.11.17
*	설명 : 실시간 이메일발송결재 결재처리 JavaScript
**********************************************************/

// 결재완료/반려 체크
var aprStepFinished = false;
var aprStepRejected = false;

$(document).ready(function(){
			
});

var conFirmSeq;
function popApprStepConfirm(seq) {
	conFirmSeq = seq;

	fn.popupOpen('#popup_confirm_approval');
	
	if($("input[name='tempComplianceYn']").eq(seq-1).val() == "Y") {
		$("#rejectView").text("준법심의 결제");		
	} else {
		$("#rejectView").text("일반 결제");	
	}	
}

// 결재 클릭시
function goApprStepConfirm() {
	
	if($("#rejectConfirmDesc").val() != "") {
		if($("#rejectConfirmDesc").val().length > 500) {
			alert("기타의견은 500자 이내로 입력해주세요");
			return;
		}	
	}
	
	var seq = conFirmSeq;
	$("#rejectDesc").val($("#rejectConfirmDesc").val());
	$("#apprStep").val($("input[name='tempApprStep']").eq(seq-1).val());
	$("#totalCount").val($("input[name='tempTotalCount']").eq(seq-1).val());
	$("#rsltCd").val("002");		
	$("#apprUserId").val($("input[name='tempUserId']").eq(seq-1).val());
	$("#complianceYn").val($("input[name='tempComplianceYn']").eq(seq-1).val());
	$("#actApprUserId").val($("input[name='tempUserId']").eq(seq-1).val());
	
	if(confirm("결재를 승인합니다.")) {
		var param = $("#rnsAprForm").serialize();
		$.getJSON("./rnsAprStepUpdate.json?" + param, function(res) {
			if(res.result == "Success") {
				alert("승인되었습니다.");
				aprStepFinished = true;
				fn.popupClose('#popup_confirm_approval')
				// 결재목록 다시 표시
				$.ajax({
					type : "POST",
					url : "./rnsAprStepListP.ums?" + param,
					dataType : "html",
					success : function(pageHtml){
						$("#divRnsApprStepList").html(pageHtml);
					},
					error : function(){
						alert("목록 조회에 실패하였습니다");
					}
				});
				
				var tempCnt = $("input[name='tempComplianceYn']").length;
				var tmpUserId = $("input[name='tmpUserId']").val();
				var tempRegId = $("input[name='tempRegId']").eq(seq-1).val();
				var tempUserId = $("input[name='tempUserId']").eq(seq-1).val();
				var tempComplianceYn = $("input[name='tempComplianceYn']").eq(seq-1).val();
				var tempComplianceUseYn = "N";
				var tempNomCnt = 0;//일반결제 갯수
				var tempComCnt = 0;//준법심의 갯수
				var tempComplianceId = ""; //준법심의자 id
				
				for(i=0;i < tempCnt;i++) {  //준법심의 있는지 체크 
					if($("input[name='tempComplianceYn']").eq(i).val() == "Y") {
						tempComplianceUseYn ="Y";
						tempComCnt++;
						tempComplianceId += ","+ $("input[name='tempUserId']").eq(i).val();						
					} else {
						tempNomCnt++;
					}		
				}
				
				tempComplianceId = tempComplianceId.substring(1,tempComplianceId.length);	
				var arryTempComplianceId = 	tempComplianceId.split(",");
				var tempComplianceId2 = "";
				for(k=0 ; k < arryTempComplianceId.length ; k++) {
					if(arryTempComplianceId[k] == $("input[name='tempUserId']").eq(seq-1).val()) {
						tempComplianceId2 += ","+ tempRegId;
					} else {
						tempComplianceId2 += ","+ arryTempComplianceId[k];
					}
				}
				tempComplianceId2 = tempComplianceId2.substring(1,tempComplianceId2.length);
				
				$("#regId").val(tempRegId);
				
				if(tempCnt == 1) { //1명만 결제할경우
					$("#apprUserId").val(tempUserId);  //사용자ID
					$("#apprStep").val($("input[name='tempApprStep']").eq(seq-1).val());//결제단계 
				} else {
					if(tempComplianceYn == "Y") {//준법심의 결제 												
						$("#apprUserId").val(tempComplianceId2);  //사용자ID						
						$("#apprStep").val($("input[name='tempApprStep']").eq(seq-1).val());//결제단계						 
						$("#rsltCd").val("002");	
					} else { //일반결제
						if(tempComplianceUseYn == "Y" ) { //준법심의결제가 있을 경우
							if($("input[name='tempComplianceYn']").eq(seq).val() == "Y") {//다음단계가 준법심의 일경우
								$("#apprUserId").val(tempComplianceId);  //사용자ID
								$("#apprStep").val($("input[name='tempApprStep']").eq(seq).val());//결제단계 
								$("#rsltCd").val("001");
							} else { //일반 다음 결제 일경우
								$("#apprUserId").val($("input[name='tempUserId']").eq(seq).val());  //사용자ID
								$("#apprStep").val($("input[name='tempApprStep']").eq(seq).val());//결제단계 
								$("#rsltCd").val("001");
							}				
						} else {//준법심의 결제가 없을 경우 
							if(seq == tempNomCnt) { //일반 결제종료 
								$("#apprUserId").val(tempUserId);  //사용자ID
								$("#apprStep").val($("input[name='tempApprStep']").eq(seq-1).val());//결제단계 				
							} else { //일반 다음 결졔
								$("#apprUserId").val($("input[name='tempUserId']").eq(seq).val());  //사용자ID
								$("#apprStep").val($("input[name='tempApprStep']").eq(seq).val());//결제단계 
								$("#rsltCd").val("001");
							}			
						}
					}
				}
				
				var arryUserId = $("#apprUserId").val().split(",");
				
				for(i = 0 ; i < arryUserId.length ; i++) {
					//alert(arryUserId[i]);
					$("#apprUserId").val(arryUserId[i]);
					// 결재 Alert
					$("#mailAprForm").attr("target","iFrmMail").attr("action","./APPROVAL_ALERT.jsp").submit();					
				}					
			} else {
				alert("승인 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 반려 클릭시
var rejectApprStep;
var rejectApprUserId;
var rejectSeq;
function popApprStepReject(seq) {	
	rejectApprStep = $("input[name='tempApprStep']").eq(seq-1).val();
	rejectApprUserId = $("input[name='tempUserId']").eq(seq-1).val();
	rejectSeq = seq;
	
	fn.popupOpen('#popup_rejct_returnreason');
	
	if($("input[name='tempComplianceYn']").eq(seq-1).val() == "Y") {
		$("#rejectView1").text("일반 결제");	
	} else {
		$("#rejectView1").text("준법심의 결제");
	}
}

// 반려 팝업창에서 등록 클릭시
function goApprStepReject() {
	if($("#rejectCancelCd").val() == "") {
		alert("반려사유를 선택해주세요.");
		return;
	}
	
	if($("#rejectCd").val() == "099") {
		if($("#rejectCancelDesc").val() == "") {
			alert("기타의견를 선택해주세요.");
			return;
		}
		if($("#rejectCancelDesc").val().length > 500) {
			alert("기타의견은 500자 이내로 입력해주세요");
			return;
		}
	}
	$("#rejectDesc").val($("#rejectCancelDesc").val());
	$("#apprStep").val(rejectApprStep);
	$("#rsltCd").val("003");
	$("#apprUserId").val(rejectApprUserId);
	$("#actApprUserId").val(rejectApprUserId);
	$("#complianceYn").val($("input[name='tempComplianceYn']").eq(rejectSeq-1).val());
	
	if(confirm("결재를 반려합니다.")) {
		var param = $("#rnsAprForm").serialize();
		$.getJSON("./rnsAprStepUpdate.json?" + param, function(res) {
			if(res.result == "Success") {
				alert("반려되었습니다.");
				aprStepRejected = true;
				
				fn.popupClose('#popup_rejct_returnreason');

				// 결재목록 다시 표시
				$.ajax({
					type : "POST",
					url : "./rnsAprStepListP.ums?" + param,
					dataType : "html",
					success : function(pageHtml){
						$("#divRnsApprStepList").html(pageHtml);
					},
					error : function(){
						alert("목록 조회에 실패하였습니다");
					}
				});
				
				var tempCnt = $("input[name='tempComplianceYn']").length;
				var tmpUserId = $("input[name='tmpUserId']").val();
				var tempRegId =  $("input[name='tempRegId']").eq(rejectSeq-1).val();
				var tempUserId = $("input[name='tempUserId']").eq(rejectSeq-1).val();
				var tempComplianceYn = $("input[name='tempComplianceYn']").eq(rejectSeq-1).val();
				var tempComplianceId = ""; //준법심의자 id
				
				for(i=0;i < tempCnt;i++) {  //준법심의 있는지 체크 
					if($("input[name='tempComplianceYn']").eq(i).val() == "Y") {		
						tempComplianceId += ","+ $("input[name='tempUserId']").eq(i).val();						
					} 	
				}
				
				tempComplianceId = tempComplianceId.substring(1,tempComplianceId.length);	
				var arryTempComplianceId = 	tempComplianceId.split(",");
				var tempComplianceId2 = "";
				for(k=0 ; k < arryTempComplianceId.length ; k++) {
					if(arryTempComplianceId[k] == $("input[name='tempUserId']").eq(rejectSeq-1).val()) {
						tempComplianceId2 += ","+ tempRegId;
					} else {
						tempComplianceId2 += ","+ arryTempComplianceId[k];
					}
				}
				tempComplianceId2 = tempComplianceId2.substring(1,tempComplianceId2.length);
				
				$("#regId").val(tempRegId);
				
				if(tempComplianceYn == "Y") {//준법심의 결제 												
					$("#apprUserId").val(tempComplianceId2);  //사용자ID						
					$("#apprStep").val($("input[name='tempApprStep']").eq(rejectSeq-1).val());//결제단계						 	
				} else { //일반결제	
					$("#apprUserId").val(tempUserId);  //사용자ID
					$("#apprStep").val($("input[name='tempApprStep']").eq(rejectSeq-1).val());//결제단계 	
				}
				
				var arryUserId = $("#apprUserId").val().split(",");
				
				for(i = 0 ; i < arryUserId.length ; i++) {
					//alert(arryUserId[i]);
					$("#apprUserId").val(arryUserId[i]);
					// 결재 Alert
					$("#mailAprForm").attr("target","iFrmMail").attr("action","./APPROVAL_ALERT.jsp").submit();					
				}	
			} else {
				alert("반려 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 반려 내용 확인
function  popRejectDisplay(seq) {
	$("#txtRejectDt").text( $("input[name='tempUpdt']").eq(seq-1).val() );
	$("#txtRejectNm").text( $("input[name='tempRejectNm']").eq(seq-1).val() );
	$("#txtRejectDesc").text( $("input[name='tempRejectDesc']").eq(seq-1).val() );
	if($("input[name='tempComplianceYn']").eq(seq-1).val() == "Y") {
		$("#txtRejectView").text("준법심의 결제");		
	} else {
		$("#txtRejectView").text("일반 결제");	
	}	
	
	fn.popupOpen('#popup_reject_display');
}

function popConfirmDisplay(seq) {
	
	$("#txtConfirmRejectDt").text( $("input[name='tempUpdt']").eq(seq-1).val() );	
	$("#txtConfirmRejectDesc").text( $("input[name='tempRejectDesc']").eq(seq-1).val() );
	
	if($("input[name='tempComplianceYn']").eq(seq-1).val() == "Y") {
		$("#txtConfirmRejectView").text("준법심의 결제");		
	} else {
		$("#txtConfirmRejectView").text("일반 결제");	
	}	
	
	fn.popupOpen('#popup_confirm_display');
}

// 테스트발송 클릭시(ums.common.js 에서 나머지 처리)
function popTestSend() {
	if(aprStepFinished) {
		alert("결재가 이미 완료되어 테스트가 불가합니다.");
		return;
	}
	if(aprStepRejected) {
		alert("결재가 이미 반려되어 테스트가 불가합니다.");
		return;
	}
	$("#testTaskNos1").val( $("#taskNo").val() );
	$("#testSubTaskNo1").val( $("#subTaskNo").val() );
	$("#testTaskNos2").val( $("#taskNo").val() );
	$("#testSubTaskNo2").val( $("#subTaskNo").val() );
	
	getTestUserList();
	
	fn.popupOpen('#popup_testsend_user');
}

// 목록 클릭시
function goList() {
	$("#searchForm").attr("target","").attr("action","./mailAprListP.ums").submit();
}

//테스트 센드 관련 처리

// 테스트발송 클릭시
function popRnsTestSend() {
	$("#tidTest").val( $("#tid").val() );
	
	fn.popupOpen('#popup_testsend_user');
}

// 테스트발송 팝업창에서 추가 클릭시
function goRnsTestUserAdd() {
	if($("#rid").val() == "") {
		alert("ID를 입력해주세요.");
		$("#rid").focus();
		return ;
	}
	if($("#rname").val() == "") {
		alert("수신자를 입력해주세요.");
		$("#rname").focus();
		return ;
	}
	if($("#rmail").val() == "") {
		alert("이메일을 입력해주세요.");
		$("#rmail").focus();
		return ;
	}
	
	// 기존 등록된 없을 경우 초기화
	if($("#rnsTestUserList input").length == 0) {
		$("#rnsTestUserList").empty();
	}
	var userHtml = "";
	userHtml += '<tr>';
	userHtml += '<td><label><input type="checkbox" name="rids" value="'+ $("#rid").val() +'|'+ $("#rname").val() +'|'+ $("#rmail").val() +'"><span></span></label></td>';
	userHtml += '<td>'+ $("#rid").val() +'</td>';
	userHtml += '<td>'+ $("#rname").val() +'</td>';
	userHtml += '<td>'+ $("#rmail").val() +'</td>';
	userHtml += '</tr>';
	
	$("#rnsTestUserList").append(userHtml);
	
	$("#rid").val("");
	$("#rname").val("");
	$("#rmail").val("");
}

// 테스트발송 팝업창에서 삭제 클릭시
function goRnsTestUserDelete() {
	$("#listForm input[name='rids']").each(function(idx,item){
		if($(item).is(":checked")) {
			$(item).closest("tr").remove();
		}
	});
	
	// 등록된 대상자가 없을 경우
	if($("#rnsTestUserList input").length == 0) {
		$("#rnsTestUserList").append('<tr><td colspan="4" class="no_data">등록된 내용이 없습니다.</td></tr>');
	}
}

// 테스트발송 팝업창에서 전체선택 클릭시
function goRnsUserAll() {
	$("#listForm input[name='rids']").each(function(idx,item){
		$(item).prop("checked", $("#listForm input[name='isUserAll']").is(":checked"));
	});
}

// 테스트발송 팝업창에서 테스트발송 클릭시
function goRnsTestMailSend() {
	alert("모지?");
	var userChecked = false;
	$("#listForm input[name='rids']").each(function(idx,item){
		if($(item).is(":checked")) {
			userChecked = true;
		};
	});
	
	if(!userChecked) {
		alert("체크된 항목이 존재하지 않습니다.");
		return;
	}
	
	var param = $("#listForm").serialize();
	$.getJSON("/rns/svc/serviceTestSend.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("테스트메일이 발송 되었습니다.");
			fn.popupClose('#popup_testsend_user');
		} else if(data.result == "Fail") {
			alert("테스트메일이 발송중 오류가 발생하였습니다.");
		}
	});
}

function goPopRnsProbibitInfo(tid) {
	var param = "tid=" + tid;
	console.log(param);
	$.ajax({
		type : "GET",
		url : "/rns/svc/pop/popRnsProhibitInfo.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
		$("#divPopRnsProhibitInfo").html(pageHtml);
			fn.popupOpen("#popup_rns_prohibit_info");
		},
		error : function(){
			alert("준법심의 결과 조회에 실패했습니다!");
		}
	});
}