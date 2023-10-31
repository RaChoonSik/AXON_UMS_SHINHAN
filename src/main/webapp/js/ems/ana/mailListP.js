/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.09.04
*	설명 : 통계분석 단기메일 발송목록 화면 JavaScript
**********************************************************/

$(document).ready(function() {
	goSearch("1");
});

// 검색 버튼 클릭시
function goSearch(pageNum) {
	if( $("#searchStartDt").val() > $("#searchEndDt").val() ) {
		alert("검색 시 시작일은 종료일보다 클 수 없습니다.");		// COMJSALT017
		return;
	}
	
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
		
	$("#searchForm input[name='page']").val(pageNum);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./mailList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divMailList").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 오류가 발생하였습니다");
		}
	});
}

// 초기화 버튼 클릭시
function goInit() {
	$("#searchForm")[0].reset();
}

// 목록에서 전체선택 클릭시
function goAll() {
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($("#searchForm input[name='isAll']").is(":checked")) {
			$(item).prop("checked", true);
			$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", true);
		} else {
			$(item).prop("checked", false);
			$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", false);
		}
	});
}

// 목록에서 메일명 클릭시
function goMailStat(taskNo, subTaskNo) {
	$("#taskNo").val(taskNo);
	$("#subTaskNo").val(subTaskNo);
	
	$("#searchForm").attr("target","").attr("action","./mailStatP.ums").submit();
}

// 정기메일분석 클릭시
function goTask() {
	$("#page").val("1");
	$("#searchCampNo").val("0");
	$("#searchTaskNm").val("");
	$("#searchDeptNo").val("0");
	$("#searchUserId").val("");
	
	$("#searchForm").attr("target","").attr("action","./taskListP.ums?pMenuId=M1004000&menuId=M1004002").submit();
}


// 병합분석 클릭시
function goJoin() {
	var checkCnt = 0;

	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", true);
			checkCnt++;
		} else {
			$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", false);
		}
	});

	if(checkCnt < 2) {
		alert("병합할 대상을 2개이상 선택하세요.");
	} else {
		$("#searchForm").attr("target","").attr("action","./mailJoinP.ums").submit();
	}
}

// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}
