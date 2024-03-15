/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.07.06
*	설명 : 공통 자바스크립트
**********************************************************/


/********************************************************
 * 쿠키값 설정 (쿠키명, 쿠키값, 기간)
 ********************************************************/
function setCookie( name, value, expiredays ) {
    var todayDate = new Date();
    todayDate.setDate( todayDate.getDate() + expiredays );
    document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}

/********************************************************
 * 쿠키값 조회
 ********************************************************/
function getCookie(name) {
    var nameOfCookie = name + "=";
    var x = 0;
    while ( x <= document.cookie.length ) {
        var y = (x+nameOfCookie.length);
        if ( document.cookie.substring( x, y ) == nameOfCookie ) {
            if ( (endOfCookie=document.cookie.indexOf( ";", y )) == -1 )
                endOfCookie = document.cookie.length;
            return unescape( document.cookie.substring( y, endOfCookie ) );
        }
        x = document.cookie.indexOf( " ", x ) + 1;
        if ( x == 0 )
            break;
    }
    return "";
}

// JDBC TYPE 확인(문자형, 숫자형)
function checkDBStrType(jdbcType) {
	var yn = true;

	switch(jdbcType) {
		case 1:		//java.sql.Type.CHAR
			yn = true;
			break;
		case 2:		//java.sql.Type.NUMERIC
			yn = false;
			break;
		case 12:	//java.sql.Type.VARCHAR
			yn = true;
			break;
		case 91:	//java.sql.Type.DATE
			yn = true;
			break;
		default:
			yn = true;
	}
	return yn;
}

/**
 * 허용하지 않을 문자를 체크한다.
 */
function containsChars(inputValue,chars) {
	for (var inx = 0; inx < inputValue.length; inx++) {
		if (chars.indexOf(inputValue.charAt(inx)) != -1)
			return true;
	}
	return false;
}

/**
 * 입력값의 앞뒤 공백을 제거해준다.
 * @param		str     스트림할 값(object.value)
 * @return      str     앞뒤 공백이 제거된 입력 스트링
 */
function trim(str) {
	return endEnc(firstEnc(str));
}
function firstEnc(str) {
	var len = str.length;
	var i = 0;

	for(i = 0; str.charAt(i) == ' '; i++);
	str = str.substring(i, len);

	return str;
}
function endEnc(str) {
	var i = 0;
	var len = str.length;

	for(i = (len - 1); (str.charAt(i) == ' '); i--);
	str = str.substring(0, i + 1);

	return str;
}


/********************************************************
 * jQuery 관련
 ********************************************************/
(function($) {
	var hangulByteLenth = 3; //한글 한글자에 대한 byte 를 지정


	/********************************************************
	 * TextArea 텍스트 입력 byte 체크
	 ********************************************************/
	$.fn.checkbyte = function(d) {
		var e = {
			indicator : $("#indicator"),
			limit : 80,
			twice : false
		};
		if (d) {
			$.extend(e, d);
		};
		return this.each(function() {
			var c = $(this);
			c.bind("keyup", function(a) {
				$.check(c, e.indicator, parseInt(e.limit), e.twice);
			});
		});
	};

	/********************************************************
	 * 최대 입력 문자열 길이 초과시 문자열 자름
	 ********************************************************/
	$.limitString = function(a, b) {
		var d = new String(a);
		var e = 0;
		for ( var i = 0; i < a.length; i++) {
			var c = escape(a.charAt(i));
			if (c.length == 1)
				e++;
			else if (c.indexOf("%u") != -1)
				e += hangulByteLenth;
			else if (c.indexOf("%") != -1)
				e += c.length / 3;
			if (e > b) {
				d = d.substring(0, i);
				break;
			}
		}
		return d;
	};

	/********************************************************
	 * 문자열 byte 체크
	 ********************************************************/
	$.byteString = function(a) {
		var b = 0;
		for ( var i = 0; i < a.length; i++) {
			var c = escape(a.charAt(i));
			if (c.length == 1)
				b++;
			else if (c.indexOf("%u") != -1)
				b += hangulByteLenth;
			else if (c.indexOf("%") != -1)
				b += c.length / 3;
		}
		return b;
	};

	/********************************************************
	 * 텍스트 입력 byte 제한
	 ********************************************************/
	$.check = function(a, b, c, d) {
		var e = $.byteString(a.val());
		if (e > c) {
			alert("내용은 " + c + "byte를 넘을 수 없습니다. 초과된 부분은 자동으로 삭제됩니다.");
			a.val($.limitString(a.val(), c));
		}
		b.html($.byteString(a.val()));
	};

	
	/********************************************************
	 * input box 문자열 길이 체크
	 ********************************************************/
	$.checkLength = function(strId, strName, minLen, maxLen) {
		var text = $.trim($('#' + strId).val());
		var textLen = $.byteString(text);

		if(minLen > 0 && textLen == 0) {
			msg = "[" + strName + "] : 입력해주십시오";
			$.showMsgBox(msg, null, strId);
			return false;
		}else if(textLen < minLen || textLen > maxLen) {
			if(minLen == maxLen) {
				msg = "[" + strName + "] : " + minLen + "Byte를 입력해주십시오\r\n\r\n (주의: 한글 1자는 " + hangulByteLenth + "Byte로 계산함.)";
				$.showMsgBox(msg, null, strId);
				return false;
			} else {
				msg = "[" + strName + "] : " + minLen + " - " + maxLen + " Byte를  입력해주십시오. <br/>(주의: 한글 1자는 " + hangulByteLenth + "Byte로 계산함.)";
				$.showMsgBox(msg, null, strId);
				return false;
			}
		} else {
			return true;
		}
	};


	//배열의 중복 문자 제거..
	//배열명.unique(); <-- 사용법
	//var a = {1,2,3,4,5,1,2};
	//a.unique();
	//결과 [1,2,3,4,5]
	Array.prototype.unique = function() {
		var a = {};
		for(var i=0; i<this.length; i++)   {
			if(typeof a[this[i]] == "undefined")
				a[this[i]] = 1;
		}

		this.length = 0;
		for(var i in a)
			this[this.length] = i;

		return this;
	};

	// 주어진 패턴 공식에서 문자만 뽑아내서 갯수를 세고,
	// 각 A,B,C는 patChar[i]형태로 배열에 저장한다.
	//function extChar2(patt) {
	String.prototype.extChar = function() {
		var charReg;
		charReg = /\d/; // 삭제할 부분 숫자
		//charReg = /[a-z|A-Z]/; // 알파벳만 인정
		//charReg = /[a-z]/; // 알파벳 소문자만 인정
		charReg = /[A-Z]/; // 알파벳 대문자만 인정

		var pattern = this; // 패턴 공식 값 DB에서 읽어올 것
		var Char = "";
		var patChar = pattern.split("");

		for(var i=0; i<patChar.length; i++) {
			if(charReg.test(patChar[i])) {
				Char += patChar[i] + "/";
			}
		}

		return Char; //최종으로 뽑아낸 문자만 리턴해준다.
	};


	$.fn.numericOnly = function(param) {
			var allowList = "";
			if(param && param.allow && param.allow.length > 0 ){
				for( var i = 0 ;i < param.allow.length;i++ ){
					if(param.allow[i] == '.'){
						allowList+= "\\.";
					}
				}
			}

			return this.each(function() {
				var c = $(this);
				var regExp = new RegExp('[^0-9'+allowList+']','g');
				c.keyup(function(a) {
					if (this.value != this.value.replace(regExp, '')) {
					       this.value = this.value.replace(regExp, '');
					    }
				});
				c.change(function(a) {
					if (this.value != this.value.replace(regExp, '')) {
					       this.value = this.value.replace(regExp, '');
					    }
				});
			});
	};

})(jQuery);










var umsContext = "";

/********************************************************
 * 수신자그룹 미리보기 팝업창 공통함수
 ********************************************************/
// 메일관리 수신자그룹 미리보기
function goSegInfoMail(sNo) {
	console.log(sNo);
	
	if($("#segNoc").length > 0) {
		if($("#segNoc").val() == "") {
			alert("수신자그룹을 선택하세요.");
			return;
		}
	}
	console.log($("#segNoc").val());

	var segNo = "";
	if(sNo == "") {
		segNo = $("#segNoc").val().substring(0, $("#segNoc").val().indexOf("|"));
	} else {
		segNo = sNo;
	}
	
	// 미리보기 관련 설정 초기화
	checkSearchReason = false;
	$("#searchReasonCd").val("");
	var initHtml = "";
	initHtml += "<div class='graybox'>";
	initHtml += "<div class='title-area'>";
	/*initHtml += "<h3 class='h3-title'>목록</h3>";*/
	initHtml += "<span class='total'>Total: <em>0</em></span>";
	initHtml += "</div>";
	initHtml += "<div class='grid-area'>";
	initHtml += "<table class='grid'>";
	initHtml += "<caption>그리드 정보</caption>";
	initHtml += "<tr>";
	initHtml += "<td colspan='6' class='no_data'>조회 사유를 등록해 주세요.</td>";
	initHtml += "</tr>";
	initHtml += "</table>";
	initHtml += "</div>";
	initHtml += "</div>";
	$("#previewMemberList").html(initHtml);
	
	$.getJSON(umsContext + "/ems/cam/getSegInfo.json?segNo=" + segNo, function(res) {
		if(res.result == 'Success') {
			// 수신자그룹정보 팝업창 전달
			$("#popSegInfoForm input[name='segNo']").val( res.segmentVO.segNo );
			$("#popSegInfoForm input[name='separatorChar']").val( res.segmentVO.separatorChar );
			$("#popSegInfoForm input[name='segFlPath']").val( res.segmentVO.segFlPath );
			$("#popSegInfoForm input[name='createTy']").val( res.segmentVO.createTy );
			$("#popSegInfoForm input[name='dbConnNo']").val( res.segmentVO.dbConnNo );
			$("#popSegInfoForm input[name='query']").val( res.segmentVO.query );
			$("#popSegInfoForm input[name='selectSql']").val( res.segmentVO.selectSql );
			$("#popSegInfoForm input[name='fromSql']").val( res.segmentVO.fromSql );
			$("#popSegInfoForm input[name='whereSql']").val( res.segmentVO.whereSql );
			$("#popSegInfoForm input[name='orderbySql']").val( res.segmentVO.orderbySql );
			$("#popSegInfoForm input[name='mergeKey']").val( res.segmentVO.mergeKey );
			$("#popSegInfoForm input[name='mergeCol']").val( res.segmentVO.mergeCol );
			
			// 정보표시
			$("#previewSegNm").html( res.segmentVO.segNm );	// 수신자그룹명
			if(res.segmentVO.createTy == "002" || res.segmentVO.createTy == "003") {
				$("#popCreateTyFile").show();
				$("#popCreateTyTool").hide();
				if(res.segmentVO.createTy == "002")	{
					$("#previewSql").html( res.segmentVO.query );
				} else {
					$("#previewSql").html( res.segmentVO.segFlPath );
				}
			} else {
				$("#popCreateTyFile").hide();
				$("#popCreateTyTool").show();
				
				var sql = "SELECT " + res.segmentVO.selectSql + " FROM " + res.segmentVO.fromSql;
				if(res.segmentVO.whereSql != null && res.segmentVO.whereSql != "") {
					sql += " WHERE " + res.segmentVO.whereSql;
				}
				if(res.segmentVO.orderbySql != null && res.segmentVO.orderbySql != "") {
					sql += " ORDER BY " + res.segmentVO.orderbySql;
				}
				$("#previewSql").html( sql );
				
				// 회원정보 초기화 후 설정
				$("#mergeKey").children().remove();
				var mergeKey = res.segmentVO.mergeKey.split(",");
				var mergeCol = res.segmentVO.mergeCol.split(",");
				$("#previewSearch").empty();
				for(var i=0;i<mergeKey.length;i++) {
					var option = new Option(mergeKey[i], mergeCol[i]);
					$("#previewSearch").append(option);
				}
			}
			
			
		} else {
			alert("Data Error!!");
		}
	});
	fn.popupOpen('#popup_preview_seg');
}

// 미리보기 페이징
function goPageNumSeg(pageNum) {
	$("#popSegInfoForm input[name='page']").val(pageNum);
	var getMemberUrl = "";
	if($("#popSegInfoForm input[name='createTy']").val() == "003") {
		getMemberUrl = umsContext + "/ems/seg/segFileMemberListP.ums";
	} else {
		getMemberUrl = umsContext + "/ems/seg/segDbMemberListP.ums";
	}
	var param = $("#popSegInfoForm").serialize();
	$.ajax({
		type : "POST",
		url : getMemberUrl + "?" + param + "&checkSearchReason=" + checkSearchReason + "&searchReasonCd=" + $("#searchReasonCd").val() + "&contentPath=" + window.location.pathname,
		dataType : "html",
		success : function(pageHtml){
			checkSearchReason = true;
			$("#previewMemberList").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 실패하였습니다");
		}
	});
}

// 조회사유 등록여부 체크(목록 조회 함수 goPageNumSeg에서 값 변경함)
var checkSearchReason = false;

// 수신자그룹 조회 클릭시
function goSearchReason() {
	// 조회사유 등록 체크
	if(checkSearchReason) {
		goPageNumSeg("1");
	} else {
		fn.popupOpen('#popup_reason');
	}
}

// 조회사유 등록 클릭시
function goSearchReasonAdd() {
	if($("#searchReasonCd").val() == "") {
		alert("조회 사유를 선택해주세요.");
		return;
	} else {
		alert("조회사유가 등록되었습니다.");
		fn.popupClose('#popup_reason');
		
		goPageNumSeg("1");
	}
}

// 조회사유 취소 클릭시
function goSearchReasonCancel() {
	alert("조회 사유 등록이 취소되었습니다.");
	fn.popupClose('#popup_reason');
}






/********************************************************
 * 테스트발송 팝업창 공통함수
 ********************************************************/
// 테스트사용자 목록 조회
function getTestUserList() {
	var param = $("#testUserForm").serialize();
	$.ajax({
		type : "POST",
		url : umsContext + "/ems/cam/mailTestUserList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divTestUserList").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 실패하였습니다");
		}
	});
}

// 테스트사용자 추가 클릭시
function goTestUserAdd() {
	if($("#testUserNm").val() == "") {
		alert("수신자를 입력해주세요.");
		$("#testUserNm").focus();
		return;
	}
	if($("#testUserEm").val() == "") {
		alert("이메일을 입력해주세요.");
		$("#testUserEm").focus();
		return;
	}
	
	// 등록 처리
	var param = $("#testUserForm").serialize();
	$.ajax({
		type: "POST",
		url: umsContext + "/ems/cam/mailTestUserAdd.json?" + param,
		dataType : "json",
		success: function (res) {
			if(res.result == "Success") {
				alert("등록되었습니다.");
				$("#testUserNm").val("");
				$("#testUserEm").val("");
				
				getTestUserList();
			} else {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		},
		error: function (e) {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// 테스트사용자 목록 전체 선택
function goUserAll() {
	if($("#listForm input[name='isUserAll']").is(":checked")) {
		$("#listForm input[name='testUserNos']").prop("checked",true);
		$("#listForm input[name='testUserEm']").prop("checked",true);
	} else {
		$("#listForm input[name='testUserNos']").prop("checked",false);
		$("#listForm input[name='testUserEm']").prop("checked",false);
	}
}

// 테스트사용자 목록에서 체크박스 클릭시
function checkTestUserEm(idx) {
	if($("#listForm input[name='testUserNos']").eq(idx).is(":checked")) {
		$("#listForm input[name='testUserEm']").eq(idx).prop("checked",true);
	} else {
		$("#listForm input[name='testUserEm']").eq(idx).prop("checked",false);
	}
}

// 테스트사용자 삭제 클릭시
function goTestUserDelete() {
	var addCheck = false;
	$("#listForm input[name='testUserNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			addCheck = true;
		}
	});
	if(!addCheck) {
		alert("체크된 항목이 존재하지 않습니다.");
		return;
	}
	
	var param = $("#listForm").serialize();
	$.getJSON(umsContext + "/ems/cam/mailTestUserDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다.");
			// 목록 재조회;
			getTestUserList();
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.");
		}
	});
}

// 테스트발송 취소 클릭시
function goTestSendCancel() {
	alert("테스트 발송이 취소되었습니다.");
	fn.popupClose('#popup_testsend_user');
}

// 테스트발송 팝업창에서 테스트발송 클릭시
function goTestMailSend() {
	var addCheck = false;
	$("#listForm input[name='testUserNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			addCheck = true;
		}
	});
	if(!addCheck) {
		alert("체크된 항목이 존재하지 않습니다.");
		return;
	}

	var a = confirm("테스트 메일을 발송하겠습니까?");
	if(a) {
		var param = $("#listForm").serialize();
		$.getJSON(umsContext + "/ems/cam/mailTestSend.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("테스트메일이 발송 되었습니다.");
				fn.popupClose('#popup_testsend_user');
			} else if(data.result == "Fail") {
				alert("테스트메일이 발송중 오류가 발생하였습니다.");
			}
		});
		
	} else {
		return;
	}
}

/********************************************************
 * EAI 테스트발송 팝업창 공통함수
 ********************************************************/
// EAI 테스트사용자 목록 조회
function getTestCustList() {
	
	if ($("#searchUserCustFirst").val() == "N") {
		if($("#searchIfEmail").val() == "" && $("#searchIfId").val() == "" &&  $("#searchIfName").val() ==""){
			alert("검색 조건을 입력해주세요");
			return;
		}
	} else {
		$("#searchUserCustFirst").val("N");	
	}
	
	var param = $("#testCustForm").serialize();
	$.ajax({
		type : "POST",
		url : umsContext + "/ems/cam/mailTestCustList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divTestCustList").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 실패하였습니다");
		}
	});
}

function popTestCustList(){
	$("#searchIfEmail").val("");
	$("#searchIfId").val("");
	$("#searchIfName").val("");
	$("#searchUserCustFirst").val("Y");
	
	getTestCustList();
	
	fn.popupOpen('#popup_testsend_user_cust');
}

function popTestCustSelect(sendResCd, ifId, ifEmail,ifName, ifMakeDt,bizkey ,recevSeqno){
	$("#popSendTestEaiUserIfEmail").text(ifEmail);
	$("#popSendTestEaiUserIfId").text(ifId);
	$("#popSendTestEaiUserIfName").text(ifName);
	$("#popSendTestEaiUserIfMakeDate").text(ifMakeDt);
	/*$("#popSendTestEaiUserIfRecevSeqno").text(recevSeqno);*/
	if (sendResCd == "000"){
		$("#popSendTestEaiUserSendResCd").text("발송성공");
	} else if (sendResCd == "NOT") {
		$("#popSendTestEaiUserSendResCd").text("");
	} else {
		$("#popSendTestEaiUserSendResCd").text("발송실패");
	}
	
	$("#ifId").val(ifId);
	$("#ifBizkey").val(bizkey);
	
	$("#searchIfEmail").val("");
	$("#searchIfId").val("");
	$("#searchIfName").val(""); 
	
	fn.popupClose('#popup_testsend_user_cust');
}
// EAI 테스트사용자 목록 조회
function getTestEaiUserList( ) {
/*	if (campNo > 0) {
		$("#searchIfCampId").val(campNo);
		$("#searchIfCampNm").text(campNm);
		
		$("#popSendTestEaiUserCampNm").text(campNm);
		$("#popSendTestEaiUserTaskNm").text(taskNm);
	}
	*/
	var param = $("#testEaiUserForm").serialize();
	$.ajax({
		type : "POST",
		url : umsContext + "/ems/cam/mailTestEaiUserList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divTestEaiUserList").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 실패하였습니다");
		}
	});
}

// EAI 테스트사용자 추가 클릭시
function goTestEaiUserAdd() {
	if($("#testEaiUserNm").val() == "") {
		alert("수신자를 입력해주세요.");
		$("#testEaiUserNm").focus();
		return;
	}
	if($("#testEaiUserEm").val() == "") {
		alert("이메일을 입력해주세요.");
		$("#testEaiUserEm").focus();
		return;
	}
	
	// 등록 처리
	var param = $("#testEaiUserForm").serialize();
	$.ajax({
		type: "POST",
		url: umsContext + "/ems/cam/mailTestEaiUserAdd.json?" + param,
		dataType : "json",
		success: function (res) {
			if(res.result == "Success") {
				alert("등록되었습니다.");
				$("#testEaiUserNm").val("");
				$("#testEaiUserEm").val("");
				
				getTestEaiUserList();
			} else {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		},
		error: function (e) {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// EAI 테스트사용자 목록 전체 선택
function goEaiUserAll() {
	if($("#listEaiForm input[name='isEaiUserAll']").is(":checked")) {
		$("#listEaiForm input[name='testEaiUserNos']").prop("checked",true);
		$("#listEaiForm input[name='testEaiUserEm']").prop("checked",true);
	} else {
		$("#listEaiForm input[name='testEaiUserNos']").prop("checked",false);
		$("#listEaiForm input[name='testEaiUserEm']").prop("checked",false);
	}
}

// EAI 테스트사용자 목록에서 체크박스 클릭시
function checkTestEaiUserEm(idx) {
	if($("#listEaiForm input[name='testEaiUserNos']").eq(idx).is(":checked")) {
		$("#listEaiForm input[name='testEaiUserEm']").eq(idx).prop("checked",true);
	} else {
		$("#listEaiForm input[name='testEaiUserEm']").eq(idx).prop("checked",false);
	}
}

// EAI 테스트사용자 삭제 클릭시
function goTestEaiUserDelete() {
	var addCheck = false;
	$("#listEaiForm input[name='testEaiUserNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			addCheck = true;
		}
	});
	if(!addCheck) {
		alert("체크된 항목이 존재하지 않습니다.");
		return;
	}
	
	var param = $("#listEaiForm").serialize();
	$.getJSON(umsContext + "/ems/cam/mailTestEaiUserDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다.");
			// 목록 재조회;
			getTestEaiUserList();
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.");
		}
	});
}

// EAI 테스트발송 취소 클릭시
function goTestEaiSendCancel() {
	alert("테스트 발송이 취소되었습니다.");
	fn.popupClose('#popup_testsend_user_eai');
}

// EAI 테스트발송 팝업창에서 테스트발송 클릭시
function goTestEaiMailSend() {
	var addCheck = false;
	if ($("#popSendTestEaiUserIfId").text () == "") {
		alert("EAI 검색을  해주세요");
		return;
	}
	
	$("#listEaiForm input[name='testEaiUserNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			addCheck = true;
		}
	});
	if(!addCheck) {
		alert("체크된 항목이 존재하지 않습니다.");
		return;
	}

	var a = confirm("테스트 메일을 발송하겠습니까?");
	if(a) {
		var param = $("#listEaiForm").serialize();
		$.getJSON(umsContext + "/ems/cam/mailTestEaiSend.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("테스트메일이 발송 되었습니다.");
				fn.popupClose('#popup_testsend_user_eai');
			} else if(data.result == "Fail") {
				alert("테스트메일이 발송중 오류가 발생하였습니다.");
			}
		});
		
	} else {
		return;
	}
}

/********************************************************
 * top.jsp 메일결재알림
 ********************************************************/
function goMailApprLine(userId,deptNo) {
	var apprUserId = "<input type='hidden' name='searchAprUserId' value='" + userId + "'>";
	var apprDeptNo = "<input type='hidden' name='searchAprDeptNo' value='" + deptNo + "'>";
	var topNoti = "<input type='hidden' name='topNotiYn' value='Y'>";
	$("#pMenuId").val("M1003000");
	$("#menuId").val("M1003004");
	$("#runMenuForm").append(apprUserId);
	$("#runMenuForm").append(apprDeptNo);
	$("#runMenuForm").append(topNoti);
	$("#runMenuForm").attr("target","").attr("method","post").attr("action", umsContext + "/ems/apr/mailAprListP.ums").submit();
}

/********************************************************
 * top.jsp 사용자정보수정
 ********************************************************/
function goUserUpdate() {	
	 
	$.getJSON("/sys/acc/popUserInfo.json", function(data) {
		if (data.result == "Success"){
			var charset = data.userInfo.charset;
			var tzCd =  data.userInfo.tzCd;
			var uilang =  data.userInfo.uilang;
			var perPage =  data.userInfo.perPage;
		
			if(data.charsetList.length > 0) {
				$("#popCharset").children("option:not(:first)").remove();
				$.each(data.charsetList, function(idx,item){
					var option = document.createElement('option');
					option.setAttribute('value', item.cd);
					option.innerText = item.cdNm;					
					if (item.cd == charset){
						option.setAttribute('selected', '');
					}
					$("#popCharset").append(option);
				});
			}
			if(data.tzCdList.length > 0) {
				$("#popTzCd").children("option:not(:first)").remove();
				$.each(data.tzCdList, function(idx,item){
					var option = document.createElement('option');
					option.setAttribute('value', item.cd);
					option.innerText = item.cdNm;
					if (item.cd == tzCd){
						option.setAttribute('selected', '');
						$("#popTzCd").val(item.cdNm);
					}
					$("#popTzCd").append(option);
				});
			}
			if(data.uilangList.length > 0) {
				$("#popUilang").children("option:not(:first)").remove();
				$.each(data.uilangList, function(idx,item){
					var option = document.createElement('option');
					option.setAttribute('value', item.cd);
					option.innerText = item.cdNm;
					if (item.cd == uilang){
						option.setAttribute('selected', '');
					}
					$("#popUilang").append(option);
				});
			}
			if(data.perPageList.length > 0) {
				$("#popPerPage").children("option:not(:first)").remove();
				$.each(data.perPageList, function(idx,item){
					var option = document.createElement('option');
					option.setAttribute('value', item.cdNm);
					option.innerText = item.cdNm;
					if (item.cdNm == perPage){
						option.setAttribute('selected', '');
					}
					$("#popPerPage").append(option);
				});
			}			
			fn.popupOpen("#popup_user_info");
		} else {
			alert(data.resultMessage);
		}
	}); 
}

function popUpdate() {	

	if(popChckForm()) {
		return;
	}
	
	if(popChckData()) {
		return;
	}

	var tzTerm = document.querySelector("#popTzCd").innerText;;
	
	$("#popTzTerm").val(tzTerm);
	
	var param = $("#popUserInfoForm").serialize();
	/*var param =  "deptNo=" + $("#popDeptNo").val() + "&userNm=" + $("#popUserNm").val()+ "&userTel=" + $("#popUserTel").val();
	param +=  "&userEm=" + $("#popUserEm").val() + "&mailFromNm=" + $("#popMailFromNm").val()+ "&mailFromEm=" + $("#popMailFromEm").val();
	param +=  "&replyToEm=" + $("#popReplyToEm").val() + "&returnEm=" + $("#popReturnEm").val()+ "&charset=" + $("#popCharset").val();
	param +=  "&tzCd=" + $("#popTzCd").val() + "&uilang=" + $("#popUilang").val()+ "&tzTerm=" + $("#popTzTerm").val();
	*/
	console.log(param);
	$.getJSON("/sys/acc/popUserUpdate.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수정이 완료되었습니다");
		 	fn.popupClose('#popup_user_info');
		} else {
			alert("수정에 실패하셨습니다");
		}
	}); 
} 

// 입력 폼 검사
function popChckForm() {

	var errstr = "";
	var errflag = false;
  
	if($("#popUserNm").val() == "") {
		errstr += "[사용자명]"; 
		errflag = true;
	} 
	
	if($("#popUserEm").val() == "") {
		errstr += "[사용자이메일주소]"; 
		errflag = true;
	}
	
	if($("#popReplyToEm").val() == "") {
		errstr += "[회신이메일주소]"; 
		errflag = true;
	}
	
	if($("#popMailFromEm").val() == "") {
		errstr += "[발송자이메일]"; 
		errflag = true;
	}

	if($("#popMailFromNm").val() == "") {
		errstr += "[발송자명]"; 
		errflag = true;
	}
 
	if($('#popCharset').val() == "0") {
		errstr += "[메일문자셋]";
		errflag = true;
	}
	
	if($('#popTzCd').val() == "0") {
		errstr += "[타임존]";
		errflag = true;
	}
	
	if($('#popUilang').val() == "0") {
		errstr += "[UI언어권]";
		errflag = true;
	} 
	
	if($('#popPerPage').val() == "0") {
		errstr += "[페이지당 조회결과건수]";
		errflag = true;
	}
	
		
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	 
	return errflag;
}

// 입력 폼 검사
function popChckData() {

	var errstr = "";
	var errflag = false;

	//글자수외에 Validation 체크 추가해야함..지금 말고..좀따..힘드렁 마이 힘드렁..겔겔겔.....
	if($.byteString($("#popUserNm").val()) > 38 ) {
		alert("사용자명은 40byte를 넘을 수 없습니다.");
		$("#popUserNm").focus();
		$("#popUserNm").select();
		errflag = true;
	}
	
	if (popCheckEmail()){
		errflag = true;
	}
	
	if (popCheckTelNo()){
		errflag = true;
	}
	
	return errflag;
}

function popCheckEmail(){
	
	var email ="";
	var errstr = "";
	var errflag = false;	
	var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
	
	email = $("#popUserEm").val()
	if(!reg_email.test(email)) {
		errstr += "[이메일]";
		errflag = true; 
	} 
	
	email = $("#popMailFromEm").val()
	if(!reg_email.test(email)) {
		errstr += "[발송자이메일]";
		errflag = true; 
	} 
		 
	email = $("#popReplyToEm").val()
	if(!reg_email.test(email)) {
		errstr += "[회신이메일]";
		errflag = true; 
	}  
	
	email = $("#popReturnEm").val()
	if(!reg_email.test(email)) {
		errstr += "[RETURN이메일]";
		errflag = true; 
	}
	
	if(errflag) {
		alert("입력하신 이메일 주소는 유효하지 않습니다 이메일주소를 확인해주세요(예:gildong@enders.co.kr).\n" + errstr);
	}	
	 
	return errflag;
} 

function popCheckTelNo(){
	
	var telNo ="";
	var errstr = "";
	var errflag = false;	
	var reg_telNo =/^[0-9]{6,20}$/; 
	telNo = $("#popUserTel").val()
	if(!reg_telNo.test(telNo)) {
		errstr = "[연락처]";
		errflag = true; 
	} 
	 
	if(errflag) {
		alert("연락처는 6~20자리 이하 숫자만 가능합니다\n" + errstr);
	}
	 
	return errflag;
}

function popCheckPerPage() {
	var perPage ="";
	var errstr = "";
	var errflag = false;	
	var reg_PageNum =/^[0-9]*$/; 
	perPage = $("#popPerPage").val()
	if(!reg_PageNum.test(perPage)) {
		errstr = "[페이지당 조회건수]";
		errflag = true; 
	} 
	
	if (!errflag){
		var perPageNum = $("#popPerPage").val();
		if (perPageNum < 10 || perPageNum > 100 ){
			errstr = "[페이지당 조회건수]";
			errflag = true;
		}
	}
	 
	if(errflag) {
		alert("페이지당 조회 건수는 10~100사이의  숫자가능합니다\n" + errstr);
	}
	 
	return errflag;
}

function popCloseUserEditPassword(){
	$("#needUserId").val("");
	//$("#pUserId").val("");
	$("#pUserPwd").val("");
	$("#popUserEditPwd").val("");
	$("#popUserEditPwdChk").val("");
	fn.popupClose('#popup_user_editpassword');
}

function popCheckInitPasswordChange(userId, cpw){

	var pw = $("#popUserEditPwd").val();	
	$("#popUserEditPasswordMessage").text("");

	if (pw.indexOf(userId) > -1 ) {
		$("#popUserEditPasswordMessage").text("*계정명(ID)가 포함된 암호는 사용 할 수 없습니다"); 
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		return false;
	}
	
	if (pw == cpw) {
		$("#popUserEditPasswordMessage").text("*현재의 비밀번호와 동일한 비밀번호로 설정 할 수 없습니다");
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		return false;
	}
	
	var chkPw = $("#popUserEditPwdChk").val();
	if (pw != chkPw) {
		$("#popUserEditPasswordMessage").text("*비밀번호가 일치하지 않습니다"); 
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		return false;
	}
	
	
	var num = pw.search(/[0-9]/g);
	var eng = pw.search(/[a-z]/ig);
	var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
	
	if(pw.length < 8 || pw.length > 20){
		$("#popUserEditPasswordMessage").text("*8자리 ~ 20자리 이내로 입력해주세요."); 
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		return false;
	}else if(pw.search(/\s/) != -1){	
		$("#popUserEditPasswordMessage").text("*비밀번호는 공백 없이 입력해주세요.");
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		
		return false;
	}else if( num < 0  ||  eng  < 0 || spe < 0 ){		
		$("#popUserEditPasswordMessage").text("*영문,숫자, 특수문자 3가지 이상을 혼합하여 입력해주세요.");
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		
		return false;
	} else if(/([0-9a-zA-Z])\1{3,}/.test(pw)){
		$("#popUserEditPasswordMessage").text("*같은 문자를 4번 이상 사용하실 수 없습니다.");
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		return false;
	} else if(pwContinue(pw)) {
		$("#popUserEditPasswordMessage").text("*연속된 문자 또는 숫자를 3번 이상 사용하실 수 없습니다.");
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		return false;
	} else {
		return true;
	}
	
}

function pwContinue(validpw){   //연속된 문자, 숫자 체크(3자리)
 var cnt = 0;
 var cnt2 = 0;
 var tmp = "";
 var tmp2 = "";
 var tmp3 = "";
 
 for(i=0; i<validpw.length; i++){
  tmp = validpw.charAt(i);
  tmp2 = validpw.charAt(i+1);
  tmp3 = validpw.charAt(i+2);
  
  if(tmp.charCodeAt(0) - tmp2.charCodeAt(0) == 1 && tmp2.charCodeAt(0) - tmp3.charCodeAt(0) == 1){
   cnt = cnt + 1;
  }
  if(tmp.charCodeAt(0) - tmp2.charCodeAt(0) == -1 && tmp2.charCodeAt(0) - tmp3.charCodeAt(0) == -1){
   cnt2 = cnt2 + 1;
  }
 }
 if(cnt > 0 || cnt2 > 0){
  return true;
 }else{
  return false;
 }
}

// 비밀번호에 3자리 이상 연속된 동일한 문자(예:aaa)는 사용할 수 없습니다.
function pwSame(validpw){   //동일 문자, 숫자 체크(3자리)
 var tmp = "";
 var cnt = 0; 
 for(i=0; i<validpw.length; i++){
  tmp = validpw.charAt(i);
  if(tmp == validpw.charAt(i+1) && tmp == validpw.charAt(i+2)){
   cnt = cnt + 1;
  }
 }
 if(cnt > 0){
  return true;
 }else{
  return false;
 }
}
 

/********************************************************
 * 신규발송 팝업에서 바로가기 클릭
 ********************************************************/
function goNewMail(sendRepeat) {
	// 단기메일 등록
	if(sendRepeat == "000") {
		$.getJSON("/ems/sch/scheduleGrant.json?searchSendRepeat=" + "M1002001", function(data) {
			if(data.result == "Success") {
				$("#pMenuId").val("M1002000");
				$("#menuId").val("M1002001");
				$("#runMenuForm").attr("target","").attr("action","/ems/cam/taskAddP.ums").submit();
			} else if(data.result == "Fail") {
				alert("메일발송 메뉴 접근 권한이 없습니다"); 
			}
		});
	// 정기메일 등록	
	} else if(sendRepeat == "001") {
		$.getJSON("/ems/sch/scheduleGrant.json?searchSendRepeat=" + "M1002002", function(data) {
			if(data.result == "Success") {
				$("#pMenuId").val("M1002000");
				$("#menuId").val("M1002002");
				$("#runMenuForm").attr("target","").attr("action","/ems/cam/taskAddP.ums").submit();
			} else if(data.result == "Fail") {
				alert("정기메일 메뉴 접근 권한이 없습니다"); 
			} 
		});	 
	}
}

/********************************************************
 * 발송결재라인 팝업창 공통함수
 ********************************************************/
// 발송결재라인 등록 클릭시
var firstPopMailApprOpen = true;
function popMailApproval() {
	if(firstPopMailApprOpen) {
		firstPopMailApprOpen = false;
		$("#hiddenApprLineList").val($("#apprUserList").html());
		fn.popupOpen("#popup_mail_approval");
	} else {
		$("#apprUserList").html($("#hiddenApprLineList").val());
		fn.popupOpen("#popup_mail_approval");
	}
}

// 발송결재라인팝업 하위 조직목록 조회
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
		$.each(data.userList, function(idx,item){
			userHtml += '<li ondblclick="addApprovalLineDblClick(this);">';
			userHtml += '<button type="button" value="'+ item.userId +'|'+ item.userNm +'|'+ item.orgCd +'|'+ item.orgKorNm +'|'+ item.positionGb +'|'+ item.jobGb +'|'+ item.jobNm +'">';
			userHtml += '<strong>' + item.userNm + '</strong>';
			userHtml += '<span>' + item.orgKorNm + '</span>';
			userHtml += '<span>' + item.jobNm + '</span>';
			userHtml += '</button>';
			userHtml += '</li>';
		});
		$("#popUserList").html(userHtml);
	});
}

// 발송결재라인팝업 사용자명 검색
function popSearchUser() {
	if($("#searchUserNm").val() == "") {
		alert("사용자명을 입력하세요.");
		$("#searchUserNm").focus();
		return;
	}
	$.getJSON(umsContext + "/ems/cam/getUserListSearch.json?searchUserNm=" + encodeURIComponent($("#searchUserNm").val()), function(data) {
		// 사용자 목록 설정
		var userHtml = "";
		$.each(data.userList, function(idx,item){
			userHtml += '<li ondblclick="addApprovalLineDblClick(this);">';
			userHtml += '<button type="button" value="'+ item.userId +'|'+ item.userNm +'|'+ item.orgCd +'|'+ item.orgKorNm +'|'+ item.positionGb +'|'+ item.jobGb +'|'+ item.jobNm +'">';
			userHtml += '<strong>' + item.userNm + '</strong>';
			userHtml += '<span>' + item.orgKorNm + '</span>';
			userHtml += '<span>' + item.jobNm + '</span>';
			userHtml += '</button>';
			userHtml += '</li>';
		});
		$("#popUserList").html(userHtml);
		$("#searchUserNm").val("");
	});
}

// 발송결재라인팝업 결재라인추가(더블클릭) - 일반결재라인 -> 준법심의 결재라인 순서대로 넣음 
function addApprovalLineDblClick(obj) {
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
	var checkapprUsrId = userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb;
	$("#apprUserList input[name='apprUserId']").each(function(idx,item){
		if($(item).val() == checkapprUsrId) {
			validUser = false;
		}
	});
	
	$("#prohibitUserList input[name='prohibitUserId']").each(function(idx,item){
		if($(item).val() == checkapprUsrId) {
			validUser = false;
		}
	});
			
	if(!validUser) {
		alert("이미 추가된 사용자입니다.");
		return;
	}
	
	var userSize = $("#apprUserList input[name='apprUserId']").length;
	if(userSize >= 2) {
		alert("일반결재라인 2명까지만 추가할 수 있습니다.");
		return;
	} else {
		var userListHtml = "";
		userListHtml += '<tr ondblclick="deleteApprovalLineDblClick(this);">';
		userListHtml += '<td><input type="hidden" name="apprUserId" value="'+ userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb +'">' + userNm + '</td>';
		userListHtml += '<td style="display:none;"><input type="hidden" name="apprUserNm" value="'+ userNm +' / '+ orgNm +' / '+ jobNm +'"></td>';
		userListHtml += '<td>' + orgNm + '</td>';
		userListHtml += '<td>' + jobNm + '</td>';
		userListHtml += '</tr>';
		
		$("#apprUserList").append(userListHtml);
		$(obj).remove();
	} 
}

// 발송결재라인팝업 결재라인추가
function popApprovalLineAdd() {
	if($("#popUserList li.active").length) {
		var userInfo = $("#popUserList li.active button").val();
		
		var user = userInfo.split("|");
		var userId = user[0];
		var userNm = user[1];
		var orgCd = user[2];
		var orgNm = user[3];
		var posGb = user[4];
		var jobGb = user[5];
		var jobNm = user[6];
		
		var validUser = true;
		var checkapprUsrId = userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb;
		$("#apprUserList input[name='apprUserId']").each(function(idx,item){
			if($(item).val() == checkapprUsrId) {
				validUser = false;
			}
		});
		
		$("#prohibitUserList input[name='prohibitUserId']").each(function(idx,item){
			if($(item).val() == checkapprUsrId) {
				validUser = false;
			}
		});
				
		if(!validUser) {
			alert("이미 추가된 사용자입니다.");
			return;
		}
		
		var userSize = $("#apprUserList input[name='apprUserId']").length;
		if(userSize >= 2) {
			alert("2명까지만 추가할 수 있습니다.")
			return;
		}
		
		var userListHtml = "";
		userListHtml += '<tr ondblclick="deleteApprovalLineDblClick(this);">';
		userListHtml += '<td><input type="hidden" name="apprUserId" value="'+ userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb +'">' + userNm + '</td>';
		userListHtml += '<td style="display:none;"><input type="hidden" name="apprUserNm" value="'+ userNm +' / '+ orgNm +' / '+ jobNm +'"></td>';
		userListHtml += '<td>' + orgNm + '</td>';
		userListHtml += '<td>' + jobNm + '</td>';
		userListHtml += '</tr>';
		
		$("#apprUserList").append(userListHtml);
		$("#popUserList li.active").remove();
	} else {
		alert("사용자를 선택하세요.");
		return;
	}
}

// 발송결재라인팝업 결재라인제거
function popApprovalLineRemove() {
	if(!$("#apprUserList tr.selected").length) {
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
	
	$("#apprUserList tr.selected td").each(function(idx,item){
		if(idx == 0) {
			var aprUserId = $(item).find("input").val();
			var userInfo = aprUserId.split("|");
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
	$("#popUserList li button").each(function(idx,item){
		if($(item).val() == userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm) {
			checkUserInfo = false;
		}
	});
	
	if(curUserId != "ADMIN" && curUserId == userId){
		checkUserInfo = false;
	}
		
	if(checkUserInfo) {
		var userHtml = "";
		userHtml += '<li ondblclick="addApprovalLineDblClick(this);">';
		userHtml += '<button type="button" value="'+ userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm +'">';
		userHtml += '<strong>' + userNm + '</strong>';
		userHtml += '<span>' + orgNm + '</span>';
		userHtml += '<span>' + jobNm + '</span>';
		userHtml += '</button>';
		userHtml += '</li>';
		$("#popUserList").append(userHtml);
	}

	$("#apprUserList tr.selected").remove();
}
  
function deleteApprovalLineDblClick(obj) {
	var userId = "";
	var userNm = "";
	var orgCd  = "";
	var orgNm  = "";
	var posGb  = "";
	var jobGb  = "";
	var jobNm  = "";
	var curUserId = $("#curUserId").val();
	$(obj).find("td").each(function(idx,item){
		if(idx == 0) {
			var aprUserId = $(item).find("input").val();
			var userInfo = aprUserId.split("|");
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
	$("#popUserList li button").each(function(idx,item){
		if($(item).val() == userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm) {
			checkUserInfo = false;
		}
	});
	
	if(curUserId != "ADMIN" && curUserId == userId){
		checkUserInfo = false;
	}
	
	if(checkUserInfo) {
		var userHtml = "";
		userHtml += '<li ondblclick="addApprovalLineDblClick(this);">';
		userHtml += '<button type="button" value="'+ userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm +'">';
		userHtml += '<strong>' + userNm + '</strong>';
		userHtml += '<span>' + orgNm + '</span>';
		userHtml += '<span>' + jobNm + '</span>';
		userHtml += '</button>';
		userHtml += '</li>';
		$("#popUserList").append(userHtml);
	}
	
	$(obj).remove();
}

// 발송결재라인팝업 결재라인 (일반 + 준법심의) 초기화
function popApprovalReset() {
	$("#apprUserList").empty();
}
 
// 발송결재라인팝업 결재라인과 준법심의 결재 라인 등록 
function popApprovalLineSave() {
	
	var curUserId = $("#curUserId").val();
	var checkUserInfo = true;
	var checkDupInfo = true;
	$("#apprUserList input[name='apprUserId']").each(function(idx,item){
		var aprUserId = $(item).val();
		var userInfo = aprUserId.split("|");
		var userId = userInfo[0];
		if(curUserId != "ADMIN" && curUserId == userId){
			checkUserInfo = false;
		}
		
		$("#prohibitUserList input[name='prohibitUserId']").each(function(idxP,itemP){
			var aprUserIdP = $(itemP).val();
			var userInfoP = aprUserIdP.split("|");
			var userIdP = userInfoP[0];
			if(curUserId != "ADMIN" && curUserId == userIdP){
				checkUserInfo = false;
			}
			if(userId == userIdP){
				checkDupInfo = false;
			}
		});		
	});
	/*
	$("#prohibitUserList input[name='prohibitUserId']").each(function(idx,item){
		var aprUserId = $(item).val();
		var userInfo = aprUserId.split("|");
		userId = userInfo[0];
		if(curUserId != "ADMIN" && curUserId == userId){
			checkUserInfo = false;
		}
	});
	*/
	if (checkUserInfo == false){
		alert("결재라인에 본인을 등록할수 없습니다.");
		return;
	}
	if (checkDupInfo == false){
		alert("준법심의 결재라인에 등록된 결재자를 등록할수 없습니다.");
		return;
	}
		
	var approvlaLineDesc = ""; 
	
	if($("#apprUserList input[name='apprUserId']").length == 0) {
		$("#txtApprovalLineYn").html("일반 결재라인이 등록되지 않았습니다.");
		$("#approvalYn").val("N");
	} else {
		//2021.11.01 김준희 수정 
		//$("#txtApprovalLineYn").html("발송결재라인이 설정되었습니다.");

		var prohibitLen = $("#prohibitUserList input[name='prohibitUserId']").length;
		
		$("#apprUserList input[name='apprUserNm']").each(function(idx,item){
			if (idx == 0) {
				if($("#apprUserList input[name='apprUserId']").length == 1) {
					approvlaLineDesc = $(item).val();
					if ( prohibitLen > 0 ){
						approvlaLineDesc = $(item).val() + " 외 준법심의결재 " + prohibitLen + "명";
					}
				} else {
					approvlaLineDesc = $(item).val() + " 외 1 명";
					if ( prohibitLen > 0 ){
						approvlaLineDesc = approvlaLineDesc +  "  준법심의결재 " + prohibitLen + "명";
					}
				}
			} 
			$("#approvalYn").val("Y");
		});
		$("#txtApprovalLineYn").html(approvlaLineDesc);
		
	}
	$("#hiddenApprLineList").val( $("#apprUserList").html() );
	
	alert("발송결재라인이 설정되었습니다.");
	fn.popupClose('#popup_mail_approval');
}

// 발송결재라인팝업 결재라인 등록취소
function popApprovalLineCancel() {
	popApprovalReset();
	alert("발송결재라인이 등록이 취소되었습니다.");
	$("#txtApprovalLineYn").html("발송결재라인이 등록되지 않았습니다.");
	fn.popupClose('#popup_mail_approval');
}

// 첨부파일 사이즈 표시
function getFileSizeDisplay(fSize) {
	var s = ['Byte', 'KB', 'MB', 'GB', 'TB', 'PB'],
		e = Math.floor(Math.log(fSize) / Math.log(1024));
	var fileSizeTxt = (fSize / Math.pow(1024, e)).toFixed(2) + " " + s[e];
	document.write(fileSizeTxt);
}

// 첨부파일 다운로드
function goFileDown(fileNm, filePath) {
	var param = "downType=009&attachNm=" + encodeURIComponent(fileNm) + "&attachPath=" + encodeURIComponent(filePath);
	iFrmMail.location.href = umsContext + "/com/down.ums?" + param;
}


/********************************************************
 * 사용자그룹 => 사용자 Cascading
 ********************************************************/
// 사용자그룹 선택시 사용자 목록 조회(기본)
function getUserList(deptNo) {
	$.getJSON(umsContext + "/com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#userId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#userId").append(option);
		});
	});
}

// 사용자그룹 선택시 사용자 목록 조회(검색항목)
function getUserListSearch(deptNo) {
	$.getJSON(umsContext + "/com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#searchUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchUserId").append(option);
		});
	});
}

/********************************************************
 * XSS 스크립트 처리  -- JS에서 체크 하는 거 뺌  (운영전환후  실제 소스에서도 삭제할것)
 ********************************************************/
function checkXssFilter(editText) {
	/*
	var filterAry =new Array('javascript', 'script', 'iframe', 'document', 'vbscript', 'applet','embed', 'object',
	 'frame','grameset', 'layer', 'bgsound','alert', 'onblur', 'onchange',
	'ondblclick', 'onerror', 'onfocus','onload', 'onmouse', 'onscroll','onsubmit', 'onunload');
	
	var exist = false;

	var filterAry =new Array('javascript', 'script', 'iframe', 'document', 'vbscript', 'applet','embed', 'object',
	 'frame','grameset', 'layer', 'bgsound','alert', 'onblur', 'onchange','onclick'
	, 'ondblclick', 'onerror', 'onfocus','onload', 'onmouse', 'onscroll','onsubmit', 'onunload');
	
	editText = editText.toLowerCase();
	
	for(var i=0; i < filterAry.length; i++){
		if (editText.search(filterAry[i]) > -1){
			exist = true;
		}
	}
	
	var regExp =/^[o]n[A-Za-z]+=/;
	if( regExp.test(editText) ) {
		exist = true;
	}
		
	return exist;
	*/
	return false;
	
}

function cleanXssFilter(editText) {
	var retStr = editText ;
		var filterAry =new Array(/javascript/g, /script/g, /iframe/g, /document/g, /vbscript/g,/applet/g,/embed/g,/object/g,
	 /frame/g,/grameset/g,/layer/g,/bgsound/g,/alert/g,/onblur/g,/onchange/g,
	 /ondblclick/g,/onerror/g,/onfocus/g,/onload/g,/onmouse/g,/onscroll/g,/onsubmit/g,/onunload/g);
	
	var replaceAry =new Array('x-javascript', 'x-script', 'x-iframe', 'x-document', 'x-vbscript', 'x-applet','x-embed', 'x-object',
	 'x-frame','x-grameset', 'x-layer', 'x-bgsound','x-alert', 'x-onblur', 'x-onchange'
	, 'x-ondblclick', 'x-onerror', 'x-onfocus','x-onload', 'x-onmouse', 'x-onscroll','x-onsubmit', 'x-onunload');
	
 /*
	var filterAry =new Array(/javascript/g, /script/g, /iframe/g, /document/g, /vbscript/g,/applet/g,/embed/g,/object/g,
	 /frame/g,/grameset/g,/layer/g,/bgsound/g,/alert/g,/onblur/g,/onchange/g,/onclick/g,
	 /ondblclick/g,/onerror/g,/onfocus/g,/onload/g,/onmouse/g,/onscroll/g,/onsubmit/g,/onunload/g);
	
	var replaceAry =new Array('x-javascript', 'x-script', 'x-iframe', 'x-document', 'x-vbscript', 'x-applet','x-embed', 'x-object',
	 'x-frame','x-grameset', 'x-layer', 'x-bgsound','x-alert', 'x-onblur', 'x-onchange','x-onclick'
	, 'x-ondblclick', 'x-onerror', 'x-onfocus','x-onload', 'x-onmouse', 'x-onscroll','x-onsubmit', 'x-onunload');
*/
	editText = editText.toLowerCase();
	
	for(var i=0; i < filterAry.length; i++){
		editText = editText.replace(filterAry[i], replaceAry[i]);
	}
	
	var regExp =/^[o]n[A-Za-z]+=/;
	editText = editText.replace(regExp, "xssfilter");
	
	retStr= editText; 
	return retStr; 
}

