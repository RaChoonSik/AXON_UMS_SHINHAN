$(document).ready(function() {
	goSearch();
  
}); 

function goChagePerPageRows(rows){
	$("#searchForm input[name='rows']").val(rows);
	goPageNum("1");
}

// 검색 버튼 클릭시
function goSearch() {
 
	var codeGrpSysYn ="N";
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index > 0 ) {
		var txtSearchCdGrp= $("#searchCdGrp option:selected").text();
		
		if (txtSearchCdGrp.indexOf("수정불가") > - 1){
			codeGrpSysYn ="Y"; 
		}  else {
			codeGrpSysYn ="N"; 
		} 
	}	
		 
	var param = $("#searchForm").serialize();
	
	$.ajax({
		type : "GET",
		url : "./userCodeList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divUserCodeList").html(pageHtml);
			if (codeGrpSysYn ==  "Y"){
				document.getElementById("btnAddCode").disabled = true;
			} else {
				document.getElementById("btnAddCode").disabled = false; 
			}
		},
		error : function(){
			alert("목록 조회에 실패하였습니다");
		}
	}); 
} 

function goSave(){
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index < 1 ) {
		alert("분류크드를 먼저 선택해주세요");
		return;
	}
	 
	var cdGrp = $("#searchCdGrp").val();
	var arrList = $("tr"); 
	var aaa = 0; 
	for (i = 1 ; i < arrList.length  ; i++){
		var sortNo = i ;
		var cd =$('tr:eq('+ i +')>td:eq(3)>a:eq(0)').html();
		var param = "cdGrp=" + cdGrp + "&cd=" + cd + "&sortNo=" + sortNo;
		$.post("/sys/cod/updateUserCodeSortNo.json?", param , function(data) {
			if(data) {
			 		aaa =  aaa + 1;  
			} else {
				alert("저장에 실패하였습니다");
			}
		}); 
	}
	alert("저장에 성공하였습니다");
	goSearch();
 
}
// 코드 또는 명 클릭시
function goUpdate(cdGrp, cd) {
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index < 1 ) {
		alert("분류크드를 먼저 선택해주세요");
		return;
	}
	
	$("#cdGrp").val(cdGrp);
	$("#cd").val(cd);
	$("#searchForm").attr("target","").attr("action","./userCodeUpdateP.ums").submit();
}

// 초기화 버튼 클릭시
function goReset() {
	$("#searchForm")[0].reset();
} 

// 신규등록 버튼 클릭시
function goAdd() {
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index < 1 ) {
		alert("분류크드를 먼저 선택해주세요");
		return;
	} else {
		$("#cdGrp").val($("#searchCdGrp").val());
	} 
	 
	$("#searchForm").attr("target","").attr("action","./userCodeAddP.ums").submit();	
	//document.location.href = "./userCodeAddP.ums";
} 
 
//삭제 EVENT 구현
function goDelete() {
 
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index < 1 ) {
		alert("분류크드를 먼저 선택해주세요");
		return;
	}	
		
	const query = 'input[name="delCd"]:checked';
  	const checkboxs = document.querySelectorAll(query);

	var cds="";
	if(checkboxs.length < 1 ){
		alert("삭제할 코드를  선택해주세요");
		return;
	}  else {
		for (var i = 0; i < checkboxs.length; i++) {
        	cds += checkboxs[i].value + ',';	
    	}
	} 
	
	var cdGrp = $("#searchCdGrp").val();	
	$.getJSON("/sys/cod/userCodeDelete.json?cdGrp=" + cdGrp + "&cds=" + cds, function(data) {
		if(data) {
		 		alert("삭제에 성공 하였습니다");
				$("#page").val("1");
				$("#searchForm").attr("target","").attr("action","/sys/cod/userCodeListP.ums").submit();
			 
		} else {
			alert("삭제에 실패하였습니다");
		}
	});   
} 
   
function selectAll(selectAll)  {
	$("input[name='delCd']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
}

function goPageNum(page) {
	$("#searchForm input[name='page']").val(page);
	goSearch();
}