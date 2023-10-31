/**
* --------------------------------
* SYS JS
* creator : chowoobin
* from : enders
* --------------------------------
*/
$(window).on('load', function(){

    //발송결재라인 등록 팝업 - 사용자명 폰트색상
	$(document).on("click", ".popsendenroll .user button", function(){
		if($(this).closest("li").hasClass("active")){
			$(this).closest("li").removeClass("active");
		}else{
			$(this).closest("li").addClass("active").siblings().removeClass("active");
		}
	});
});
/********************************************************
 * 부서 조회 팝업창 공통함수  
 ********************************************************/
// 부서 조회 클릭시
function popOrgSearch() {
	fn.popupOpen("#popup_org_search");
}
  
// 부서 팝업 하위 조직목록 조회
function getOrgList(upOrgCd, upOrgNm) {
	
	$("#popup_org_search input[name='selUpOrgCd']").val(upOrgCd);
	$("#popup_org_search input[name='selUpOrgNm']").val(upOrgNm);
	$("#popup_org_search input[name='selOrgCd']").val("");
	$("#popup_org_search input[name='selOrgNm']").val("");
 
	$.getJSON("/sys/acc/getOrgList.json?upOrgCd=" + upOrgCd, function(data) {
		// 조직 트리 설정
		var orgHtml = "";		
		$.each(data.orgList, function(idx,item){
			orgHtml += '<li>';
			
			if(item.childCnt > 0) { 

				orgHtml += '<button type="button" class="btn-toggle" onclick="getOrgList(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
				orgHtml += '<ul class="depth' + (parseInt(item.lvlVal) + 1) + '" id="' + item.orgCd + '"></ul>';
			} else {
				orgHtml += '<button type="button" onclick="orgSelect(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
			}
		});
		$("#" + upOrgCd).html(orgHtml);
		 
	});
}

// 부서 선택
function orgSelect(orgCd, orgNm) { 		
	$("#popup_org_search input[name='selOrgCd']").val(orgCd);
	$("#popup_org_search input[name='selOrgNm']").val(orgNm); 
}

// 부서 선택
function popOrgSelect() { 
 	var orgCd =$("#popup_org_search input[name='selOrgCd']").val();
	var orgNm =$("#popup_org_search input[name='selOrgNm']").val();
	 
	if (orgCd == ""){
		orgCd = $("#popup_org_search input[name='selUpOrgCd']").val();
	}
	
	if (orgNm == ""){
		orgNm = $("#popup_org_search input[name='selUpOrgNm']").val();
	}
	
	$("#orgKorNm").val(orgNm);
	$("#orgCd").val(orgCd);
	fn.popupClose('#popup_org_search');
}

/********************************************************
 * 부서 조회 팝업창 공통함수 (VIEW 사용 광주은행)
 ********************************************************/
// 부서 조회 클릭시
function popOrgSearchView() {
	fn.popupOpen("#popup_org_search_view");
}
  
// 부서 팝업 하위 조직목록 조회
function getOrgListView(upOrgCd, upOrgNm) {
	
	$("#popup_org_search_view input[name='selUpOrgCd']").val(upOrgCd);
	$("#popup_org_search_view input[name='selUpOrgNm']").val(upOrgNm);
	$("#popup_org_search_view input[name='selOrgCd']").val("");
	$("#popup_org_search_view input[name='selOrgNm']").val("");
 
	$.getJSON("/sys/acc/getOrgListView.json?upOrgCd=" + upOrgCd, function(data) {
		// 조직 트리 설정
		var orgHtml = "";
		$.each(data.orgList, function(idx,item){
			orgHtml += '<li>';
			if(item.childCnt > 0) { 

				orgHtml += '<button type="button" class="btn-toggle" onclick="getOrgListView(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
				orgHtml += '<ul class="depth' + (parseInt(item.lvlVal) + 1) + '" id="' + item.orgCd + '"></ul>';
			} else {
				orgHtml += '<button type="button" onclick="orgSelectView(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
			}
		});
		$("#" + upOrgCd).html(orgHtml);
		 
	});
}

// 부서 선택
function orgSelectView(orgCd, orgNm) { 	
	$("#popup_org_search_view input[name='selOrgCd']").val(orgCd);
	$("#popup_org_search_view input[name='selOrgNm']").val(orgNm); 
}

// 부서 선택
function popOrgSelectView() { 
 	var orgCd =$("#popup_org_search_view input[name='selOrgCd']").val();
	var orgNm =$("#popup_org_search_view input[name='selOrgNm']").val();
	 
	if (orgCd == ""){
		orgCd = $("#popup_org_search_view input[name='selUpOrgCd']").val();
	}
	
	if (orgNm == ""){
		orgNm = $("#popup_org_search_view input[name='selUpOrgNm']").val();
	} 
	
	$("#orgKorNm").val(orgNm);
	$("#orgCd").val(orgCd);
	fn.popupClose('#popup_org_search_view');
}
