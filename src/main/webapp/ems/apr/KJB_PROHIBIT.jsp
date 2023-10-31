<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.02.07
	*	설명 : 준법감시 확인 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=EDGE">
<meta name="Author" content="enders">
<meta name="keywords" content="UMS">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript">
function chkForbiddenWord(){
	
	
	/*********************************************/
	/*  프로젝트 내부 처리 : 연동할 URL 경로 작성         */
	/*********************************************/
	var reqURL = "/ems/cam/chkForbiddenWord.json";
	/*********************************************/
	
	//제목 
	var jsonTitle  = {"chk_kind" : "1", "text" : $("#mailTitle").val()};
	
	//본문 
	var jsonText = {"chk_kind" : "2", "text" : $("#prohibitText").val()};	
	
	var wordYn = "N";
	var wordCnt = 0; 
	var titleResult = "";
	var textResult = ""; 
	 
	$.ajax({ 
		url: reqURL, 
		data: JSON.stringify(jsonTitle),  
		type: "POST",
		//timeout: 5000, 
		dataType: "JSON", 
		contentType: "application/json; charset=utf-8", 
		error:	function(jqXHR, textStatus, errorThrown) {  
				alert("[준법심의 결과]제목 확인 실패! " + textStatus + " : " + errorThrown);
				return;
			},
		success: function(res) {
			//제목관련 먼저 처리
			$("#divPopProhibitTitle").empty();
			wordYn = res.forbidden_word_yn;
			wordCnt = res.forbidden_word_cnt;
		
			if (wordCnt  > 0 ){
				var wordData = res.LIST; 
				$("#popProhibitTitleCnt").val(wordCnt);
				$("#popProhibitTitleDesc").val(JSON.stringify(res));
				
				//var arrVal = Object.values(wordData);
				var arrVal = Object.keys(wordData).map(function(i){
					return wordData[i];
				}); 
				titleResult = '<div class="title-area"><h3 class="h3-title">제목(' + wordCnt + '건)</h3></div>';
				textResult = '<div class="grid-area"><table class="grid"><tbody>';
				
				for(var i=0 ; i< arrVal.length; i++){
					textResult += '<tr name="popProhibitTitleResult"><td>' + arrVal[i]+ '</td></tr>';
				}
				
				textResult += '</tbody></table></div>';
				console.log(textResult);
			} else {
				titleResult = '<div class="title-area"><h3 class="h3-title">제목</h3></div>';
				textResult = '<div class="grid-area"><table class="grid"><tbody></tbody></table></div>';
			}
			
			$("#divPopProhibitTitle").empty();
			$("#divPopProhibitTitle").append(titleResult);
			$("#divPopProhibitTitle").append(textResult);
			
			$.ajax({ 
				url: reqURL,  
				data: JSON.stringify(jsonText),  
				type: "POST",
				timeout: 5000,
				dataType: "JSON",
				contentType: "application/json; charset=utf-8",
				error:	function(res)	{
						alert("[준법심의 결과]본문 확인 실패!" );
						return;
					},
				success: function(res) {
					//본문관련 처리 
					if (wordYn == "N") {
						wordYn = res.forbidden_word_yn;
					}
					wordCnt = res.forbidden_word_cnt;
					
					if (wordCnt  > 0 ){
						$("#popProhibitTextCnt").val(wordCnt);
						$("#popProhibitTextDesc").val(JSON.stringify(res));
						var wordData = res.LIST; 
						//var arrVal = Object.values(wordData);
						var arrVal = Object.keys(wordData).map(function(i){
							return wordData[i];
						});
						titleResult = '<div class="title-area"><h3 class="h3-title">본문(' + wordCnt + '건)</h3></div>';
						textResult = '<div class="grid-area"><table class="grid"><tbody>';
						
						for(var i=0 ; i< arrVal.length; i++){
							textResult += '<tr name="popProhibitTextResult"><td>' + arrVal[i]+ '</td></tr>';
						}
						
						textResult += '</tbody></table></div>';
					} else {
						titleResult = '<div class="title-area"><h3 class="h3-title">본문</h3></div>';
						textResult = '<div class="grid-area"><table class="grid"><tbody></tbody></table></div>';
					}
					
					$("#divPopProhibitText").empty();
					$("#divPopProhibitText").append(titleResult);
					$("#divPopProhibitText").append(textResult);
					
					fn.popupOpen('#popup_prohibit');
				}
			});	
		}
	});	
}
</script>
<body></body>
</html>
