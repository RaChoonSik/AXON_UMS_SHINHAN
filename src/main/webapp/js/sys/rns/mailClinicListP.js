var gClsMonth = "";
var gClsDay = "";
var gClsErrCnt = "";
var gClsEndMmCnt = "";

$(document).ready(function () {
	
	//해당 년월의 마지막 일자 계산하여 실행일자 채우는 함수 
	clsDaySet();
	mailClinicSearch();
	//연도 변경시 해당 연도 데이터 조회
	$("select[name=clsY]").on("change",function() {
		mailClinicSearch()
	})
	
	$("#allMonthUpdate").on("click",function(){
		allMonthUpdate();
	})
});

//데이터 조회
function mailClinicSearch() {	

	var param = $("#mailClinicForm").serialize();
	param +="clsY=" + $("select[name=clsY]").val();
	
	for(k=0;k < 12;k++) {
		$("select[name=clsDay]").eq(k).val("");
		$("select[name=clsErrCnt]").eq(k).val("");
		$("select[name=clsEndMmCnt]").eq(k).val("");
	}
		
	$.getJSON("./mailClinicSearch.json?" + param, function(data) {				
		for(i=0 ; i < data.mailClinicList.length ; i++) {
			var mon = data.mailClinicList[i].clsYm.substring(4,6);
			mon = parseInt(mon);
			
			$("select[name=clsDay]").eq(mon-1).val(data.mailClinicList[i].clsDt*1);
			$("select[name=clsErrCnt]").eq(mon-1).val(data.mailClinicList[i].clsErrCnt);
			$("select[name=clsEndMmCnt]").eq(mon-1).val(data.mailClinicList[i].clsEndMmCnt);
		}		
	});	
}

//해당 년월의 마지막 일자 계산하여 실행일자 채우는 함수
function clsDaySet() {
	var i,j,k,l,m;	
	for(i=1 ; i < 13 ; i++) {
		var lastdays = new Date($("#clsY").val(), i, 0).getDate(); //해당년월 마지막일자
						
		$("select[name=clsDay]").eq(i-1).empty();
		$("select[name=clsErrCnt]").eq(i-1).empty();
		$("select[name=clsEndMmCnt]").eq(i-1).empty();
		
		$("select[name=clsDay]").eq(i-1).append("<option value=''>선택</option>");
		$("select[name=clsErrCnt]").eq(i-1).append("<option value=''>선택</option>");
		$("select[name=clsEndMmCnt]").eq(i-1).append("<option value=''>선택</option>");
		
		for(j=1 ; j < lastdays+1 ; j++) {			
			$("select[name=clsDay]").eq(i-1).append("<option value='"+j+"'>"+j+"일</option>");
		}	
		for(k=1 ; k < 11 ; k++) {
			$("select[name=clsErrCnt]").eq(i-1).append("<option value='"+k+"'>"+k+"회</option>");
		}	
		for(ㅣ=1 ; ㅣ < 13 ; ㅣ++) {
			$("select[name=clsEndMmCnt]").eq(i-1).append("<option value='"+ㅣ+"'>"+ㅣ+"개월</option>");
		}
	}
	
	$("select[name=clsY]").empty();
	for(m=2021 ; m < 2050 ; m++) {
			$("select[name=clsY]").append("<option value='"+m+"'>"+m+"</option>");
	}
	
	for(n=0 ; n < 12 ; n++) {
		
		$("input:checkbox[name=chk_month]").eq(n).prop("checked", true);
	}
	
	var now = new Date();	
	var year = now.getFullYear();	
	
	$("select[name=clsY]").val(year);
}

//당월수정
function nowMonthUpdate(obj) {
	var seq = (obj * 1)-1;	
	var clsDay = $("select[name=clsDay]").eq(seq).val();
	var clsErrCnt = $("select[name=clsErrCnt]").eq(seq).val();
	var clsEndMmCnt = $("select[name=clsEndMmCnt]").eq(seq).val();
	var clsY = $("select[name=clsY]").val();
	var clsM = obj;
	var errstr = "";
	var errflag = false;
	
	if(clsDay == "") {
		errstr += "[실행일자]"; 
		errflag = true;
	} 
		 
	if(errflag) {
		alert("다음 정보를 선택하세요.\n" + errstr);
		return false;
	}
	
	if(clsErrCnt == "") {
		errstr += "[오류횟수]"; 
		errflag = true;
	} 
		 
	if(errflag) {
		alert("다음 정보를 선택하세요.\n" + errstr);
		return false;
	}
	
	if(clsEndMmCnt == "") {
		errstr += "[마감기간]"; 
		errflag = true;
	} 
		 
	if(errflag) {
		alert("다음 정보를 선택하세요.\n" + errstr);
		return false;
	}
	
	var param = $("#mailClinicForm").serialize();
	
	param +="clsDt=" + clsDay;
	param +="&clsErrCnt=" + clsErrCnt;
	param +="&clsEndMmCnt=" + clsEndMmCnt;
	param +="&clsY=" + clsY;
	param +="&clsM=" + clsM;
	
	$.getJSON("./mailClinicUpdate.json?" + param, function(data) {
		if(data.result == "Success") { 
			alert(clsY+"."+clsM+" 수정되었습니다.!");
		} else {
			alert(clsY+"."+clsM+" 수정에 실패하였습니다");
		}
	});
	
}

//삭제
function nowMonthDel(obj) {	
	var seq = (obj * 1)-1;	
	var clsY = $("select[name=clsY]").val();
	var clsM = obj;
	
	var param = $("#mailClinicForm").serialize();
	param +="clsY=" + clsY;
	param +="&clsM=" + clsM;
	
	$.getJSON("./mailClinicDelete.json?" + param, function(data) {
		if(data.result == "Success") { 
			alert(clsY+"."+clsM+" 삭제되었습니다.!");
			$("select[name=clsDay]").eq(seq).val('');
			$("select[name=clsErrCnt]").eq(seq).val('');
			$("select[name=clsEndMmCnt]").eq(seq).val('');
			mailClinicSearch();
		} else {
			alert(clsY+"."+clsM+" 삭제에 실패하였습니다");
		}
	});	
}

//일괄적용팝업
function batchApply(obj) {			
	var seq = (obj * 1)-1;	
	gClsDay = $("select[name=clsDay]").eq(seq).val();
	gClsErrCnt = $("select[name=clsErrCnt]").eq(seq).val();
	gClsEndMmCnt = $("select[name=clsEndMmCnt]").eq(seq).val();
	gClsMonth = obj;
	
	var errstr = "";
	var errflag = false;
	
	if(gClsDay == "") {
		errstr += "[실행일자]"; 
		errflag = true;
	} 
		 
	if(errflag) {
		alert("다음 정보를 선택하세요.\n" + errstr);
		return false;
	}
	
	if(gClsErrCnt == "") {
		errstr += "[오류횟수]"; 
		errflag = true;
	} 
		 
	if(errflag) {
		alert("다음 정보를 선택하세요.\n" + errstr);
		return false;
	}
	
	if(gClsEndMmCnt == "") {
		errstr += "[마감기간]"; 
		errflag = true;
	} 
		 
	if(errflag) {
		alert("다음 정보를 선택하세요.\n" + errstr);
		return false;
	}
	
	
	fn.popupOpen('#popup_checkmonth');
}


//일괄적용
function allMonthUpdate() {	
	var insertMonth = 0;
	var delMonth = "";
	var chklen = $("input:checkbox[name=chk_month]").length;
	var errstr = "";
	var errflag = false;
	var clsY =$("select[name=clsY]").val();
	var clsM =gClsMonth;
			
	for(i=0 ; i < chklen ; i++) {		
		if(!$("input:checkbox[name=chk_month]").eq(i).is(':checked')) {			
			delMonth +=","+$("input:checkbox[name=chk_month]").eq(i).val();
		} else {
			insertMonth++;
		}		
	}
		
	delMonth = delMonth.substring(1,delMonth.length);
		
	if(insertMonth < 1) {
		errstr += "[일괄적용 월]"; 
		errflag = true;
	} 
		 
	if(errflag) {
		alert("다음 정보를 선택하세요.\n" + errstr);
		return false;
	}
	
	var param = $("#mailClinicForm").serialize();
	param +="clsDt=" + gClsDay;
	param +="&clsErrCnt=" + gClsErrCnt;
	param +="&clsEndMmCnt=" + gClsEndMmCnt;	
	param +="&delMonth=" + delMonth;
	param +="&clsY="+clsY;
	param +="&clsM="+clsM;
				
	$.getJSON("./mailClinicAllUpdate.json?" + param, function(data) {
		if(data.result == "Success") { 
			alert("수정되었습니다.!");
			fn.popupClose('#popup_checkmonth');				
			mailClinicSearch();
				
		} else {
			alert("수정에 실패하였습니다");
			fn.popupClose('#popup_checkmonth');					
		}
		checkReset();
	});				
}

function checkReset() {
	for(i=0 ; i < 12 ; i++) {	
		$("input:checkbox[name=chk_month]").eq(i).prop("checked", true);
	}		
}
