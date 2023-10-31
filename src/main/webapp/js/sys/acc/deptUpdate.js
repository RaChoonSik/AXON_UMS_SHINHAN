// 수정 클릭시
function goUpdate() {	
	if($("#deptNo").val() != "") {
		// 입력 폼 검사
		if(checkForm()) {
			return;
		}
		
		const query = 'input[name="service"]:checked';
		const checkboxs = document.querySelectorAll(query);

		var services = "";

		if (checkboxs.length > 0) {
			for (var i = 0; i < checkboxs.length; i++) {
				services += checkboxs[i].value + ',';
			}
		}
		
		$("#deptInfoForm input[name='serviceGb']").val(services);
		var param = $("#deptInfoForm").serialize();
		$.getJSON("./deptUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정이 완료되었습니다");
				$("#page").val("1");
				$("#deptInfoForm").attr("target","").attr("action","./deptListP.ums").submit();
			} else {
				alert("수정에 실패하셨습니다");
			}
		});
	} else {
		alert("사용자그룹 코드번호가 없습니다!");	
	}
} 

// 입력 폼 검사
function checkForm() {
	var errstr = "";
	var errflag = false;
	if($("#deptNm").val() == "") {
		errstr += "[사용자그룹]"; 
		errflag = true;
	}
	
	if(!$('#status > option:selected').val()) {
		errstr += "[사용자그룹상태]";
		errflag = true;
	}
 
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	
	if($.byteString($("#deptNm").val()) > 38 ) {
		alert("사용자그룹은 40byte를 넘을 수 없습니다.");
		$("#deptNm").focus();
		$("#deptNm").select();
		errflag = true;
	}
	if($.byteString($("#deptDesc").val()) > 380 ) {
		alert("사용자그룹 설명은 400byte를 넘을 수 없습니다.");
		$("#deptDesc").focus();
		$("#deptDesc").select();
		errflag = true;
	} 
		
	return errflag;
}

function setSysUse()
{ 	
	if ( $('input:checkbox[id="chkUseSys"]').is(":checked") == true ){
		if(confirm("관리자 기능입니다 설정하시겠습니까?")) {
			$('input:checkbox[id="chkUseSys"]').prop("checked", true);
		} else {
			$('input:checkbox[id="chkUseSys"]').prop("checked", false); 
		}
	} else {
		if(confirm("관리자 기능 미사용으로 설정하시겠습니까?")) {
			$('input:checkbox[id="chkUseSys"]').prop("checked", false);
		} else {
			$('input:checkbox[id="chkUseSys"]').prop("checked", true);
		}		
	}
}

//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#deptInfoForm").attr("target","").attr("action","./deptListP.ums").submit();
}
