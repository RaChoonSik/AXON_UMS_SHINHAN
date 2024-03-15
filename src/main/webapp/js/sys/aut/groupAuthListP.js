$(document).ready(function() {		
	goSearch("1");
}); 

function goChagePerPageRows(rows){
	$("#searchForm input[name='rows']").val(rows);
	goSearch("1");
}

// 검색 버튼 클릭시
function goSearch(pageNo) { 
	$("#searchForm input[name='page']").val(pageNo);  
	var param = $("#searchForm").serialize();	
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./groupAuthList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divGroupAuthList").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 실패하였습니다");
		}
	});
} 

// 코드 또는 명 클릭시
function goUpdate(deptNo) { 	
	$("#deptNo").val(deptNo);
	$("#searchForm").attr("target","").attr("action","./groupAuthUpdateP.ums").submit();
} 
//삭제 EVENT 구현
function goDelete() { 
	 
	const query = 'input[name="delDeptNo"]:checked';
  	const checkboxs = document.querySelectorAll(query);
  	
	var users="";
	if(checkboxs.length < 1 ){
		alert("삭제할 사용자를  선택해주세요");
		return;
	} else {
		for (var i = 0; i < checkboxs.length; i++) {
        	users += checkboxs[i].value + ',';	
    	}
	}
	 
	$.getJSON("/sys/aut/groupAuthDelete.json?deptNos=" + users, function(data) {
		if(data) {
	 		alert("삭제에 성공 하였습니다");
			$("#page").val("1");
			$("#searchForm").attr("target", "").attr("action", "/sys/aut/groupAuthListP.ums").submit();
		} else {
			alert("삭제에 실패 하였습니다");
		}
	});  
}  

// 초기화 버튼 클릭시
function goReset() {
	$("#searchForm")[0].reset();
}

function selectAll(selectAll)  {
	$("input[name='delDeptNo']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
} 

function goPageNum(page) {
	goSearch(page);
}
 
// 사용자 권한 조회 클릭시
function popGroupAuthSearch(deptNo) {
 
	$.getJSON("/sys/aut/getGroupAuthInfo.json?deptNo=" + deptNo, function(data) {
		var groupAuthInfo = ""; 
		$.each(data.groupAuthInfo, function(idx,item){ 	 
			groupAuthInfo += '<li class="clear"> <span class="fl"><strong>' + item.lv1Nm + '</strong><span>' + ' > '  +  item.lv2Nm + '</span><span>'  + ' > '  +  item.lv3Nm + '</span></span></li>'		 
		});	 
		$("#ulGroupAuthList").html(groupAuthInfo);	
	}); 
		
 	fn.popupOpen("#popup_dept_auth_search");
}

