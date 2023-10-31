/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.23
*	설명 : 메일발송결재 목록 JavaScript
**********************************************************/

$(document).ready(function() {
	// 화면 로딩시 검색 실행
	goSearch("1");
});

function goChagePerPageRows(rows){
	$("#searchForm input[name='rows']").val(rows);
	goSearch("1");
}

//사용자그룹 선택시 사용자 목록 조회 
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#searchUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchUserId").append(option);
		});
	});
}
goProhibitView
//승인예정그룹 선택시 사용자 목록 조회 
function getUserListApr(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#searchAprUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchAprUserId").append(option);
		});
	});
}

// 검색 클릭시
function goSearch(pageNum) {
	
	$("#page").val(pageNum);
	
	var endDt = $("#searchEndDt").val().split('.');
	var startDt = $("#searchStartDt").val().split('.');
	var endDate = new Date(endDt[0], endDt[1], endDt[2]);
	var startDate = new Date(startDt[0], startDt[1], startDt[2]);
	
	var diff = endDate - startDate;
	var currDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
	if ( parseInt(diff/currDay) > 365 ){
		alert("검색 기간은 1년을 넘길수 없습니다");
		return;
	}
		
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./mailAprList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divMailList").html(pageHtml);
		},
		error : function(){
			alsert("목록 조회에 실패하였습니다");
		}
	});
}

// 초기화 클릭시
function goInit(adminYn, deptNo) {
	$("#searchForm")[0].reset();
	$("#searchTaskNm").val("");
	$("#searchCampNo").val("0");
	if(adminYn == "Y") {
		$("#searchDeptNo").val("0");
	} else {
		$("#searchDeptNo").val(deptNo);
	}
	$("#searchUserId").val("");
	$("#searchWorkStatus").val("");
	$("#searchAprDeptNo").val("0");
	$("#searchAprUserId").val("");
}

// 목록에서 메일명 클릭시
function goAprUpdate(taskNo, subTaskNo, svcType) {
	if (svcType == "10") {
		$("#taskNo").val( taskNo );
		$("#subTaskNo").val( subTaskNo );
		
		
		$("#searchForm").attr("target","").attr("action","./mailAprUpdateP.ums").submit();
	} else {
		$("#taskNo").val( taskNo ); 
		
		
		$("#searchForm").attr("target","").attr("action","./rnsAprUpdateP.ums").submit();
	}

}

// 목록에서 준법 팝업 클릭시 
function goProhibitView(taskNo, svcType) {
	$("#tid").val( taskNo );
	$("#taskNo").val( taskNo );
	var urls = "";
	if (svcType == "10") {
		urls = "./mailProhibitSearch.json?";
	}  else {
		urls = "./rnsProhibitSearch.json?";
	}
	
	var param = $("#searchForm").serialize();	
		
	$.getJSON(urls + param, function(data) {
		
		fn.popupOpen('#popup_confirm_reviewresult');
		
		
		for(i=0 ; i < data.prohibitInfo.length ; i++) {
			
			if(data.prohibitInfo[i].mktYn == null || data.prohibitInfo[i].mktYn == ""){
				$("#mktYn").empty();
				$("#mktYn").text("해당사항없음");	
			} else {
				$("#mktYn").empty();
				$("#mktYn").text(data.prohibitInfo[i].mktYn);
			}
			
			$("#imgChkYn").empty();
			if(data.prohibitInfo[i].imgChkYn == "Y"){					
				$("#mktYn").text("본문 이미지 포함");	
			}
			
			$("#attCnt").empty();	
			if(data.prohibitInfo[i].attCnt > 0){				
				if(data.prohibitInfo[i].attCnt > 1){	
					$("#attCnt").text(data.prohibitInfo[i].attNm + "외"+(data.prohibitInfo[i].attCnt-1)+"건");
				} else {
					$("#attCnt").text(data.prohibitInfo[i].attNm);
				}	
			}													
		}

		
		var obj = "";
		var obj2 = "";
		
		for(i=0 ; i < data.prohibitList.length ; i++) {					
			obj = jQuery.parseJSON(data.prohibitList[i].prohibitDesc);	
			
			console.log(data.prohibitList[i].contentTyp)
			
			if(data.prohibitList[i].contentTyp == "000") { //제목
				$("#titCnt").empty();
				
				
				if(obj.forbidden_word_yn == "Y") {
					$("#titCnt").text("제목 ("+obj.forbidden_word_cnt+")");
				} else {
					$("#titCnt").text("제목");
				}
				
				$("#titBody").empty();										
				$.each(obj.LIST, function( key1, value1 ) {
					$("#titBody").append("<tr><td>"+value1+"</td></tr>");												
				});
				
				if(obj.LIST.length < 1) {
					$("#titBody").append("<tr><td></td></tr>");
				}
				
			}
			if(data.prohibitList[i].contentTyp == "001") {//본문
				$("#bodyCnt").empty();
				
				
				if(obj.forbidden_word_yn == "Y") {
					$("#bodyCnt").text("본문 ("+obj.forbidden_word_cnt+")");
				} else {
					$("#bodyCnt").text("본문");
				}
				
				$("#bodyBody").empty();										
				$.each(obj.LIST, function( key1, value1 ) {
					$("#bodyBody").append("<tr><td>"+value1+"</td></tr>");												
				});
				if(obj.LIST.length < 1) {
					$("#bodyBody").append("<tr><td></td></tr>");
				}
			}
		}											
	});	
}


// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}

function goPopProbibitInfo(taskNo, subTaskNo) {
	var param = "taskNo=" + taskNo + "&subTaskNo=" + subTaskNo;
	console.log(param);
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popProhibitInfo.ums?" + param,
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