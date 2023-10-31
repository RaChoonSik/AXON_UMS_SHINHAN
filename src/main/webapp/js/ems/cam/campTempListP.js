/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2022.09.26
*	설명 : 캠페인 템플릿 등록현황 JavaScript
**********************************************************/


$(document).ready(function() {
	// 화면 로딩시 검색 실행
	goSearch("1");
});

function goChagePerPageRows(rows){
	$("#searchForm input[name='rows']").val(rows);
	goSearch("1");
}
// 검색 버튼 클릭
function goSearch(pageNo) {
	if($("#searchStartDt").val() > $("#searchEndDt")) {
		alert("검색 시 시작일은 종료일보다 클 수 없습니다.");
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
	
	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./campTempList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divCampTempList").html(pageHtml);
		},
		error : function(){
			alsert("목록 조회에 실패하였습니다");
		}
	});
}

// 목록 전체 체크
function goAll() {
	if($("#searchForm input[name='isAll']").is(":checked") == true) {
		$("#searchForm input[name='tids']").each(function(idx,item){
			if($(item).is(":disabled") == false) {
				$("#searchForm input[name='tids']").eq(idx).prop("checked", true);
				$("#searchForm input[name='status']").eq(idx).prop("checked", true);
			}
		});
	} else {
		$("#searchForm input[name='tids']").prop("checked", false);
		$("#searchForm input[name='status']").prop("checked", false);
	}
}

// 목록에서 체크박스 클릭시 tid와 status를 같이 체크해 준다.
function goTid(i) {
	var isChecked = $("#searchForm input[name='tids']").eq(i).is(":checked");
	$("#searchForm input[name='status']").eq(i).prop("checked", isChecked);
}

// 삭제된 목록의 체크박스 클릭시
function goDeleteClick() {
	alert("삭제된 목록은 선택할 수 없습니다.");
	return false;
}

// 신규등록 클릭시
function goServiceAdd() {
	document.location.href = "./campTempAddP.ums";
}

// 복사 클릭시
function goCopy() {
	var checkedCount = 0;

	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) checkedCount++;
	});
	if(checkedCount == 0 || checkedCount > 1) {
		alert("복사할 목록을 하나만 선택해 주세요.!!");
		return;
	}

	var param = $("#searchForm").serialize();
	$.getJSON("./campTempCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");
			
			// 메일 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});
}

// 사용중지 클릭시
function goDisable() {
	var isChecked = false;
	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});

	if(!isChecked) {
		alert("사용중지 할 목록을 선택해 주세요!!");
		return;
	}
	
	$("#status").val("001");
	var param = $("#searchForm").serialize();
	$.getJSON("./campTempUpdateStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("사용중지되었습니다.");
			
			// 서비스 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 삭제 클릭시
function goDelete() {
	var isChecked = false;
	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});

	if(!isChecked) {
		alert("삭제할 목록을 선택해 주세요!!");
		return;
	}
	
	$("#status").val("002");
	var param = $("#searchForm").serialize();
	$.getJSON("./campTempUpdateStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다.");
			
			// 서비스 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.")
		}
	});
}

// 목록에서 제목 클릭시 : 메일 업데이트 화면으로 이동
function goUpdate(tid) {
	$("#tid").val( tid );
	$("#searchForm").attr("target","").attr("action","./campTempUpdateP.ums").submit();
}

// 목록에서 제목 클릭시 : 메일 업데이트 화면으로 이동 - 결재완료, 발송승인인 경우 
function goUpdatePart(tid, approvalProcAppYn) {
	$("#tid").val( tid );
	$("#searchForm").attr("target","").attr("action","./campTempUpdateP.ums").submit();
}

// 페이징
function goPageNum(page) {
	goSearch(page);
}
