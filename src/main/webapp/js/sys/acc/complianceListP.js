
$(document).ready(function() { 
 $("#searchUserNm").keypress(function(e) {
		if(e.which == 13) {
			searchUser();
		}
	});
	//드래그앤드롭
	$("#complianceUserList").sortable();
	
    //준법심의 결재라인 :: 사용자 검색
	$(document).on("click", "#userList.user button", function(){

		if($(this).closest("li").hasClass("active")){
			$(this).closest("li").removeClass("active");
		}else{
			$(this).closest("li").addClass("active").siblings().removeClass("active");
		}
	});
	
	$(document).on("click", "#complianceUserList.sortable tr", function(){
		if(!$(this).hasClass("selected")){
			$(this).addClass("selected").siblings().removeClass("selected");
		}
	});
});

// 준법심의 결재라인 하위 조직목록 조회
function getOrgUserList(upOrgCd) {
	$.getJSON(umsContext + "/ems/cam/getOrgUserList.json?upOrgCd=" + upOrgCd, function(data) {
		// 조직 트리 설정
		var orgHtml = "";
		$.each(data.orgList, function(idx,item){
			orgHtml += '<li>';
			if(item.childCnt > 0) {
				orgHtml += '<button type="button" class="btn-toggle" onclick="getOrgUserList(\'' + item.orgCd + '\',\'' + item.childCnt + '\');">' + item.orgNm + '</button>';
				orgHtml += '<ul class="depth' + (item.lvlVal + 1) + '" id="' + item.orgCd + '"></ul>';
			} else {
				orgHtml += '<button type="button" onclick="getOrgUserList(\'' + item.orgCd + '\',\'' + item.childCnt + '\');">' + item.orgNm + '</button>';
			}
		});
		$("#" + upOrgCd).html(orgHtml);
		
		// 사용자 목록 설정
		var userHtml = ""; 				
		var result = 0;					
		
		var compluserId = hideUser();
		
		$.each(data.userList, function(idx,item){
			
			var userId = $.trim(item.userId);
				result = $.inArray(userId,compluserId);
				
				if(result < 0 ){
					userHtml += '<li ondblclick="addComplianceDblClick(this);">';
					userHtml += '<button type="button" value="'+ item.userId +'|'+ item.userNm +'|'+ item.orgCd +'|'+ item.orgKorNm +'|'+ item.positionGb +'|'+ item.jobGb +'|'+ item.jobNm +'">';
					userHtml += '<strong>' + item.userNm + '</strong>';
					userHtml += '<span>' + item.orgKorNm + " | " + '</span>';
					userHtml += '<span>' + item.jobNm + '</span>';
					userHtml += '</button>';
					userHtml += '</li>';
				}
		});
		$("#userList").html(userHtml);
	});
}
// 준법심의 결재라인 사용자명 검색
function searchUser() {
	if($("#searchUserNm").val() == "") {
		alert("사용자명을 입력하세요.");
		$("#searchUserNm").focus();
		return;
	}
	
	$.getJSON(umsContext + "/ems/cam/getUserListSearch.json?searchUserNm=" + encodeURIComponent($("#searchUserNm").val()), function(data) {
		// 사용자 목록 설정
		var userHtml = "";
		var result = 0;
		
		var compluserId = hideUser();
		
		$.each(data.userList, function(idx,item){
			var userId = $.trim(item.userId);
				result = $.inArray(userId,compluserId);
				
		if(result < 0){
				userHtml += '<li ondblclick="addComplianceDblClick(this);">';
				userHtml += '<button type="button" value="'+ item.userId +'|'+ item.userNm +'|'+ item.orgCd +'|'+ item.orgKorNm +'|'+ item.positionGb +'|'+ item.jobGb +'|'+ item.jobNm +'">';
				userHtml += '<strong>' + item.userNm + '</strong>';
				userHtml += '<span>' + item.orgKorNm+ " | " + '</span>';
				userHtml += '<span>' + item.jobNm + '</span>';
				userHtml += '</button>';
				userHtml += '</li>';
			} 
		});
		
		$("#userList").html(userHtml);
		$("#searchUserNm").val("");
	});
}

// 준법심의 결재라인 결재라인추가(더블클릭)
function addComplianceDblClick(obj) {
	var userInfo = $(obj).children("button").val();
	var user = userInfo.split("|");
	var userId = user[0];
	var userNm = user[1];
	var orgCd = user[2];
	var orgNm = user[3];
	var posGb = user[4];
	var jobGb = user[5];
	var jobNm = user[6];
	
	var validUser = true;
	var checkcomplUsrId = userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb;
	$("#complianceUserList input[name='complianceUserId']").each(function(idx,item){
		if($(item).val() == checkcomplUsrId) {
			validUser = false;
		}
	});
	
	if(!validUser) {
		alert("이미 추가된 사용자입니다.");
		return;
	}
	
	var userSize = $("#complianceUserList input[name='complianceUserId']").length;
	if(userSize >= 2) {
		alert("2명까지만 추가할 수 있습니다.")
		return;
	}
	
	var userListHtml = "";
	userListHtml += '<tr ondblclick="deleteComplianceDblClick(this);">';
	userListHtml += '<td><input type="hidden" name="complianceUserId" value="'+ userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb +'">' + userNm + '</td>';
	userListHtml += '<td style="display:none;"><input type="hidden" name="complianceUserNm" value="'+ userNm +' / '+ orgNm +' / '+ jobNm +'"></td>';
	userListHtml += '<td>' + orgNm + '</td>';
	userListHtml += '<td>' + jobNm + '</td>';
	userListHtml += '</tr>';
	
	$("#complianceUserList").append(userListHtml);
	$(obj).remove();
}

// 준법심의 결재라인 결재라인추가
function complianceAdd() {
	if($("#userList li.active").length) {
		var userInfo = $("#userList li.active button").val();
		
		var user = userInfo.split("|");
		var userId = user[0];
		var userNm = user[1];
		var orgCd = user[2];
		var orgNm = user[3];
		var posGb = user[4];
		var jobGb = user[5];
		var jobNm = user[6];
		
		var validUser = true;
		var checkcomplUsrId = userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb;
		$("#complianceUserList input[name='complianceUserId']").each(function(idx,item){
			if($(item).val() == checkcomplUsrId) {
				validUser = false;
			}
		});
		
		if(!validUser) {
			alert("이미 추가된 사용자입니다.");
			return;
		}
		
		var userSize = $("#complianceUserList input[name='complianceUserId']").length;
		if(userSize >= 2) {
			alert("2명까지만 추가할 수 있습니다.")
			return;
		}
		
		var userListHtml = "";
		userListHtml += '<tr ondblclick="deleteComplianceDblClick(this);">';
		userListHtml += '<td><input type="hidden" name="complianceUserId" value="'+ userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb +'">' + userNm + '</td>';
		userListHtml += '<td style="display:none;"><input type="hidden" name="complianceUserNm" value="'+ userNm +' / '+ orgNm +' / '+ jobNm +'"></td>';
		userListHtml += '<td>' + orgNm + '</td>';
		userListHtml += '<td>' + jobNm + '</td>';
		userListHtml += '</tr>';
		
		$("#complianceUserList").append(userListHtml);
		$("#userList li.active").remove();
	} else {
		alert("사용자를 선택하세요.");
		return;
	}
}

// 준법심의 결재라인 결재라인제거
function complianceRemove() {
	if(!$("#complianceUserList tr.selected").length) {
		alert("사용자를 선택하세요.");
		return;
	}
	
	var userId = "";
	var userNm = "";
	var orgCd  = "";
	var orgNm  = "";
	var posGb  = "";
	var jobGb  = "";
	var jobNm  = "";
	var curUserId = $("#curUserId").val();
	
	$("#complianceUserList tr.selected td").each(function(idx,item){
		if(idx == 0) {
			var comUserId = $(item).find("input").val();
			var userInfo = comUserId.split("|");
			userId = userInfo[0];
			userNm = $(item).text();
			orgCd = userInfo[1];
			posGb = userInfo[2];
			jobGb = userInfo[3];
		} else if(idx == 2) {
			orgNm = $(item).text();
		} else if(idx == 3) {
			jobNm = $(item).text();
		}
	});
	
	var checkUserInfo = true;
	$("#userList li button").each(function(idx,item){
		if($(item).val() == userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm) {
			checkUserInfo = false;
		}
	});
	
	if(curUserId != "ADMIN" && curUserId == userId){
		checkUserInfo = false;
	}
		
	if(checkUserInfo) {
		var userHtml = "";
		userHtml += '<li ondblclick="addComplianceDblClick(this);">';
		userHtml += '<button type="button" value="'+ userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm +'">';
		userHtml += '<strong>' + userNm + '</strong>';
		userHtml += '<span>' + orgNm + '</span>';
		userHtml += '<span>' + jobNm + '</span>';
		userHtml += '</button>';
		userHtml += '</li>';
		$("#userList").append(userHtml);
	}

	$("#complianceUserList tr.selected").remove();
}
//준법심의 결재라인 더블클릭제거
function deleteComplianceDblClick(obj) {
	var userId = "";
	var userNm = "";
	var orgCd  = "";
	var orgNm  = "";
	var posGb  = "";
	var jobGb  = "";
	var jobNm  = "";
	$(obj).find("td").each(function(idx,item){
		if(idx == 0) {
			var comUserId = $(item).find("input").val();
			var userInfo = comUserId.split("|");
			userId = userInfo[0];
			userNm = $(item).text();
			orgCd = userInfo[1];
			posGb = userInfo[2];
			jobGb = userInfo[3];
		} else if(idx == 2) {
			orgNm = $(item).text();
		} else if(idx == 3) {
			jobNm = $(item).text();
		}
	});

	var checkUserInfo = true;
	$("#userList li button").each(function(idx,item){
		if($(item).val() == userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm) {
			checkUserInfo = false;
		}
	});
	
	if(checkUserInfo) {
		var userHtml = "";
		userHtml += '<li ondblclick="addComplianceDblClick(this);">';
		userHtml += '<button type="button" value="'+ userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm +'">';
		userHtml += '<strong>' + userNm + '</strong>';
		userHtml += '<span>' + orgNm + " | " + '</span>';
		userHtml += '<span>' + jobNm + '</span>';
		userHtml += '</button>';
		userHtml += '</li>';
		$("#userList").append(userHtml);
	}
	
	$(obj).remove();
}

// 준법심의 결재라인 결재라인 초기화
function complianceReset() {
	$("#complianceUserList").empty();
}
// 준법결재라인팝업 결재라인 등록
function complianceLineSave() {
	
	var complianceUserList = document.getElementsByName("complianceUserId");
	var userIds="";	
	
	if(complianceUserList.length > 0 ){  
		for (var i = 0; i < complianceUserList.length; i++) {
			var exInfo = complianceUserList[i].value; 
			var exUser = exInfo.split("|"); 
			userIds += exUser[0] + ',';
		}
	} 
	$.getJSON("/sys/acc/complianceListUpdate.json?userIds=" + userIds, function(data) {
			if(data) { 
				alert("정상 처리되었습니다");
				document.location.href = "./complianceListP.ums";
			} else {
				alert("저장 실패 하였습니다 ");
				return;
			}
		});
}
//준법심의 결재라인 사용자 추출
//준법심의 결재라인 사용자 추출
function hideUser(){
	
	var compluserId = new Array;	// 준법심의 결재라인 User id 
	
	$("input[name = complianceUserId]").each(function(index,item){
		var	userInfo = $(item).val();
		var	complUser = userInfo.split("|");
			compluserId[index] = complUser[0];
	});
	return compluserId;
}