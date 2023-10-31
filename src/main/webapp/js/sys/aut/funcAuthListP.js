$(document).ready(function() {
	// 첫번째 인덱스 Jquery 하나 찝고 들어온다.
	selectFirstOption(); 
	// 초기 진입시 => 권한 매핑 사용자 / 권한 매핑 그룹 clear
	removeList();
	// 000 => 처음 초기화
	setList('000'); 
 	$("#userNm").keypress(function(e) {
		if(e.which == 13) {
			getFuncUserList('');
		}
	});
});

// 부서 팝업 하위 조직목록 조회
function getAuthOrgListView(upOrgCd) {
   $.getJSON("/sys/acc/getOrgListView.json?upOrgCd=" + upOrgCd, function(data) {		
		var orgHtml = "";
		$.each(data.orgList, function(idx,item){
			orgHtml += '<li>';
			if(item.childCnt > 0) {  
				orgHtml += '<button type="button" class="btn-toggle" onclick="getAuthOrgListView(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
				orgHtml += '<ul class="depth' + (parseInt(item.lvlVal) + 1) + '" id="' + item.orgCd + '"></ul>';
			} else {
				orgHtml += '<button type="button" onclick="getFuncUserList(\'' + item.orgCd + '\');">' + item.orgNm + '</button>';
			}
		});
		$("#" + upOrgCd).html(orgHtml);
	});
	getFuncUserList(upOrgCd);
}

// 사용자 검색 
function getFuncUserList(orgCd){
	
	// FuncCd
	var funcCd = $("#cdNo option:checked").val();
	
	var param = "" ;
	
	if (orgCd ==""){
		// 사용자 검색시 LIKE 인자 
		param = "userNm=" + $('#userNm').val() ;  
	} else {
		// 조직도에서 넘어온 사용자 검색 
		param = "orgCd=" + orgCd +"&funcCd=" + funcCd;
	} 
	
	var userListHtml = ""; 
	$("#userList").empty(); 
	$('#userNm').val("");
	
	var url = "/sys/aut/getFuncUserList.json?";
	$.post(url, param, function(data){
		$.each(data.getFuncUserList , function(idx, item){
			userListHtml += '<li class="clear"id=' + 'lu_' + item.userId +'><label class="clear">';	
			userListHtml += '<span class="fl"><strong>' + item.userNm + '</strong><span>' + item.orgKorNm + ' | </span><span>' + item.jobNm + '</span></span>';
			userListHtml += '<span class="fr"><input type="checkbox" name="chkUser" id=' + 'cu_' + item.userId +' value="'+ item.userId + '" ><span></span></span>';			
			userListHtml += '</label></li>'; 			
		});
		$("#userList").html(userListHtml);
	});
}

//그룹 검색
function getAuthGrpDeptList(){
	 
	var param = "" ;
	
	var funcCd = $("#cdNo option:checked").val();
	
	param = "funcCd=" + funcCd + "&deptNm=" + $('#deptGPNm').val() ;  
	
	var deptListHtml = "";
	 
	$("#deptGPList").empty(); 
	$('#deptGPNm').val("");
	
	var url = "/sys/aut/getFuncGrpUserList.json?";
	$.post(url, param, function(data) { 
	    $.each(data.getFuncGrpUserList, function(idx,item){	
			deptListHtml += '<li id=' + 'ld_' + item.deptNo +' class="clear"><label class="clear">';
			deptListHtml += '<span class="fl">'+ item.deptNm + '</span>';
			deptListHtml += '<span class="fr"><input type="checkbox" name="chkGPDept" id=' + 'cd_' + item.deptNo +' value="'+ item.deptNo + '" ><span></span></span>';
			deptListHtml += '</label></li>'; 
		});
		$("#deptGPList").html(deptListHtml);
	});
}

// 수정 클릭시 (Update 에서 할일)
function goFuncAuthUpdate() {	
	// 선택된 사항 코드
	var selectedVl = $("#cdNo option:checked").val();
	
	if(selectedVl != "") {
		var dbFuncAuthUserList = document.getElementsByName("chkAuth");
		var dbFuncAuthDeptList = document.getElementsByName("chkAuthDept");
		var userIds="";
		var deptNos="";
		
		if(dbFuncAuthUserList.length > 0 ){
			for (var i = 0; i < dbFuncAuthUserList.length; i++) {
				userIds += dbFuncAuthUserList[i].value + ',';
			}
		}else {
			userIds = "-1";	
		}

		if(dbFuncAuthDeptList.length > 0 ){
			for (var i = 0; i < dbFuncAuthDeptList.length; i++) {
				deptNos += dbFuncAuthDeptList[i].value + ',';
			}
		}else{
			deptNos = "-1";			
		}
		
		$("#funcAuthForm input[name='funcCd']").val(selectedVl);
		$("#funcAuthForm input[name='userIds']").val(userIds);
		$("#funcAuthForm input[name='deptNos']").val(deptNos);
		$("#funcAuthForm input[name='pagePerm']").val(dbFuncAuthUserList.length); 
		$("#funcAuthForm input[name='pageGrpPerm']").val(dbFuncAuthDeptList.length);
		
		var param = $("#funcAuthForm").serialize();
		
		$.getJSON("./updateFuncPermsAuth.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("정상 처리되었습니다.");
				$("#page").val("1");
				$("#funcAuthForm").attr("target","").attr("action","./funcAuthListP.ums").submit();
			} else {
				alert("처리중 실패하였습니다.");
			}
		});
		
	}else{
		alert("CD 번호가 없습니다!");	
	}	
} 

function goAdd(){
	 
	var chkUser = document.getElementsByName('chkUser');
	var chkAuth = document.getElementsByName('chkAuth');
  	
	var authParentUl = document.getElementById('funcPermAuthList');
	var arrMove = new Array();  
		
	var arrRemMove = new Array(); 
	var arrRemIndex = 0; 
			
	if(chkUser.length > 0 ){
		for (var i = 0; i < chkUser.length; i++){ 
 			if (chkUser[i].checked == true ){				 
				var chkUserId = chkUser[i].value;   
				for (var j = 0 ; j < chkAuth.length ; j++){
					var chkAuthUserId = chkAuth[j].value; 
					if( chkUserId == chkAuthUserId ){ 
						arrRemMove[arrRemIndex] = chkUserId;
						arrRemIndex = arrRemIndex + 1;
					}
				}				  
				arrMove[i] = chkUserId; 
			}
		}
	}
	
	if (arrRemMove.length > 0 ){
		for(var i = 0 ; i < arrRemMove.length ; i++){
			var remUserId = arrRemMove[i]; 
			var userLi = document.getElementById('la_' + remUserId); 
			if (userLi != undefined ){
			 	userLi.parentNode.removeChild(userLi);
			}
		}
	}	
		
	if (arrMove.length > 0 ){
		for(var i = 0 ; i < arrMove.length ; i++){
			var moveUserId = arrMove[i]; 
			var userLi = document.getElementById('lu_' + moveUserId);
			var userCheck = document.getElementById('cu_' + moveUserId);
			if (userLi != undefined ){
				$(userLi).appendTo($(authParentUl));
				$(userLi).prop('id', 'la_' + moveUserId);
				
				if (userCheck != undefined ){					
					$(userCheck).prop("checked", false);
					$(userCheck).prop('name', 'chkAuth');
					$(userCheck).prop('id', 'ca_' + moveUserId);
				}
			}
		}
	}
}

function goRemove(){
	
	var chkUser = document.getElementsByName('chkUser');
	var chkAuth = document.getElementsByName('chkAuth');
  	 		
	var userParentUl = document.getElementById('userList');
	var arrMove = new Array(); 
	
 	var arrRemMove = new Array(); 
	var arrRemIndex = 0; 
	
	if(chkAuth.length > 0 ){
		for (var i = 0; i < chkAuth.length; i++){ 
 			if (chkAuth[i].checked == true ){				 
				var chkAuthUserId = chkAuth[i].value;  				
				for (var j = 0 ; j < chkUser.length ; j++){
					var chkUserId = chkUser[j].value ;
					if( chkAuthUserId == chkUserId ){
						arrRemMove[arrRemIndex] = chkUserId;
						arrRemIndex = arrRemIndex + 1;
					}
				}
				arrMove[i] = chkAuthUserId;  
			}
		}
	}
	
	if (arrRemMove.length > 0 ){
		for(var i = 0 ; i < arrRemMove.length ; i++){
			var remUserId = arrRemMove[i]; 
			var userLi = document.getElementById('lu_' + remUserId); 
			if (userLi != undefined ){
			 	userLi.parentNode.removeChild(userLi);
			}
		}
	}	
		
	if (arrMove.length > 0 ){
		for(var i = 0 ; i < arrMove.length ; i++){
			var moveUserId = arrMove[i]; 
			var mappLi = document.getElementById('la_' + moveUserId);
			var mappCheck = document.getElementById('ca_' + moveUserId);
			
			if (mappLi != undefined ){
				$(mappLi).appendTo($(userParentUl));
				$(mappLi).prop('id', 'lu_' + moveUserId);
				
				if (mappCheck != undefined ){					
					$(mappCheck).prop("checked", false);
					$(mappCheck).prop('name', 'chkUser');
					$(mappCheck).prop('id', 'cu_' + moveUserId);
				}				
			}
		}
	}
}

function goAddDept(){
	
	var chkDept = document.getElementsByName('chkGPDept');
	var chkAuthDept = document.getElementsByName('chkAuthDept');
	
	var authParentUl = document.getElementById('funcGrpPermAuthList');
	var arrMove = new Array();
	
	var arrRemMove = new Array();
	var arrRemIndex = 0;
	 
	if(chkDept.length > 0 ){
		for (var i = 0; i < chkDept.length; i++){ 
 			if (chkDept[i].checked == true ){
				var chkDeptNo = chkDept[i].value;   
				for (var j = 0 ; j < chkAuthDept.length ; j++){
					var chkAuthDeptNo = chkAuthDept[j].value; 
					if( chkDeptNo == chkAuthDeptNo ){ 
						arrRemMove[arrRemIndex] = chkDeptNo;
						arrRemIndex = arrRemIndex + 1;
					}
				}
				arrMove[i] = chkDeptNo; 
			}
		}
	}
	
	if (arrRemMove.length > 0 ){
		for(var i = 0 ; i < arrRemMove.length ; i++){
			var remDeptNo = arrRemMove[i]; 
			var deptLi = document.getElementById('ld_' + remDeptNo); 
			
			if (deptLi != undefined ){
				deptLi.parentNode.removeChild(deptLi);
			}
		}
	}	
	
	if (arrMove.length > 0 ){
		for(var i = 0 ; i < arrMove.length ; i++){
			var moveDeptNo = arrMove[i]; 
			var deptLi = document.getElementById('ld_' + moveDeptNo);
			var deptCheck = document.getElementById('cd_' + moveDeptNo);
			if (deptLi != undefined ){
				$(deptLi).appendTo($(authParentUl));
				$(deptLi).prop('id', 'lad_' + moveDeptNo);
				
				if (deptCheck != undefined ){
					// 옮겨지는 노드 
					$(deptCheck).prop("checked", false);
					$(deptCheck).prop('name', 'chkAuthDept');
					$(deptCheck).prop('id', 'cad_' + moveDeptNo);
				}
			}
		}
	}
}

function goRemoveDept(){
	 
	var chkDept = document.getElementsByName('chkGPDept');
	var chkAuthDept = document.getElementsByName('chkAuthDept');
	
	var deptParentUl = document.getElementById('deptGPList');
	var arrMove = new Array(); 
	
	var arrRemMove = new Array();
	var arrRemIndex = 0; 
	
	if(chkAuthDept.length > 0 ){
		for (var i = 0; i < chkAuthDept.length; i++){ 
 			if (chkAuthDept[i].checked == true ){
				var chkAuthDeptNo = chkAuthDept[i].value;
				for (var j = 0 ; j < chkDept.length ; j++){
					var chkDeptNo = chkDept[j].value ;
					if( chkAuthDeptNo == chkDeptNo ){
						arrRemMove[arrRemIndex] = chkDeptNo;
						arrRemIndex = arrRemIndex + 1;
					}
				}
				arrMove[i] = chkAuthDeptNo;
			}
		}
	}
	
	if (arrRemMove.length > 0 ){
		for(var i = 0 ; i < arrRemMove.length ; i++){
			var remDeptNo = arrRemMove[i]; 
			var deptLi = document.getElementById('ld_' + remDeptNo); 
			if (deptLi != undefined ){
			 	deptLi.parentNode.removeChild(deptLi);
			}
		}
	}
	
	if (arrMove.length > 0 ){
		for(var i = 0 ; i < arrMove.length ; i++){
			var moveDeptNo = arrMove[i]; 
			var mappLi = document.getElementById('lad_' + moveDeptNo);
			var mappCheck = document.getElementById('cad_' + moveDeptNo);
			
			if (mappLi != undefined ){
				console.log('ArrMove Remove Dept!!!! ');
				$(mappLi).appendTo($(deptParentUl));
				$(mappLi).prop('id', 'ld_' + moveDeptNo);
				
				if (mappCheck != undefined ){
					$(mappCheck).prop("checked", false);
					$(mappCheck).prop('name', 'chkDept');
					$(mappCheck).prop('id', 'cd_' + moveDeptNo);
				}
			}
		}
	}
}

// Code 검색 => 선택창에서 선택된 정보 모두 가져옴  
function goMailTypeSearch(){
	// 선택된 사항 Text
	var selectedVt = $("#cdNo option:checked").text();
	// 선택된 사항 코드
	var selectedVl = $("#cdNo option:checked").val();
	// 선택된 Index 
	var selectedIdx = $("#cdNo option").index($("#cdNo option:selected"));
	// 선택된 해당 코드에 맞춰서 권한 매핑 사용자 / 권한 매핑 그룹 출력 한다. (select)
	// console.log('Mail 전송별 Code :: ' +selectedVt +"|" +selectedVl +"|" + selectedIdx);
	$("#showAdd").text(' | ' + selectedVt);
	// nodeClear (기능목록 선택되어질때마다 각 연관된 리스트 모두 clear)
	removeList();
	// 선택된 funcCd 이쪽에 넣는다.검색 버튼 눌렀을시에
	setList(selectedVl); 
}

// 최초 select 0 일때만에 처리 떄문에  
function getSelectedValue(){
	var selectedIdx = $("#cdNo option").index($("#cdNo option:selected"));
    // console.log("Selected IDX :: " + selectedIdx);	
	if(selectedIdx == 0){
		// 선택된 사항 Text
		var selectedVt = $("#cdNo option:checked").text();
		$("#showAdd").text(' | ' + selectedVt);		
	}
}

// CD 값에 맞춰서 갱신 해주어야 하는 리스트 일체
function setList(funcCd){
	// 000 => 처음 초기화
	getFuncPermAuthList(funcCd);
	getFuncGrpPermAuthList(funcCd);	 	
}

// 첫번째 option select
function selectFirstOption(){
	$("#cdNo option:eq(0)").prop("selected", true);
	getSelectedValue();
}

// 권한 매핑 사용자 , 권한 매핑 그룹 리스트 상황에 맞게 cLear
function removeList(){
	// 권한 매핑 사용자
	$("#funcPermAuthList").empty();
	// 권한 매핑 그룹
	$("#funcGrpPermAuthList").empty();
	// 사용자 검색
	$("#userList").empty();	
}

// select 를 통해 선택된 사항 정보 일체 (추후 Object 로 관리)  
//function seletedOptionsValue(){
	// 선택된 사항 Text
	//var selectedVt = $("#cdNo option:checked").text();
	// 선택된 사항 코드
	//var selectedVl = $("#cdNo option:checked").val();
	// 선택된 Index 
	//var selectedIdx = $("#cdNo option").index($("#cdNo option:selected"));
//}

// 권한 매핑 사용자 (MainKey funcCd => Parameter)
function getFuncPermAuthList(funcCd){
	var param = "";
	param = "funcCd=" + funcCd; // funcCd 가져와야 함
	var url = "/sys/aut/getFuncPermAuthList.json?";
	
	var funcPermHtmlList = '';
	
	$.post(url, param, function(data){
		// 응답값 전달 확인
		$.each(data.getFuncPermAuthList, function(idx, item){
			// 정보 있을때만 출력
			funcPermHtmlList += '<li id=' + 'la_' + item.userId +' class="clear"><label class="clear">';
			funcPermHtmlList += '<span class="fl"><strong>' + item.userNm + '</strong><span>' + item.orgNm + ' | </span><span>' + item.jobNm + '</span></span>';
			funcPermHtmlList += '<span class="fr"><input type="checkbox" name="chkAuth" id=' + 'ca_' + item.userId +' value="'+ item.userId + '" ><span></span>';
			funcPermHtmlList += '</label></li>';
			
		});
		
		$("#funcPermAuthList").html(funcPermHtmlList);
	});
}

// 권한 매핑 그룹 (MainKey funcCd => Parameter)
function getFuncGrpPermAuthList(funcCd){
	var param = "";
	param = "funcCd=" + funcCd; // funcCd 가져와야 함
	var url = "/sys/aut/getFuncGrpPermAuthList.json?";
	var funcGrpPermListHtml = "";
	$.post(url, param, function(data){
		// 응답값 전달 확인
		$.each(data.getFuncGrpPermAuthList, function(idx, item){
			// 정보 있을때만 출력 (어차피 없으면 출력 안된다.)
			funcGrpPermListHtml += '<li id=' + 'lad_' + item.deptNo +' class="clear"><label class="clear">';
			funcGrpPermListHtml += '<span class="fl">'+ item.deptNm + '</span>';
			funcGrpPermListHtml += '<span class="fr"><input type="checkbox" name="chkAuthDept" id=' + 'cad_' + item.deptNo +' value="'+ item.deptNo + '" ><span></span></span>';
			funcGrpPermListHtml += '</label></li>';			
		});
		$("#funcGrpPermAuthList").html(funcGrpPermListHtml);
	});		
}




	




