/**********************************************************
*	작성자 : 박찬용
*	작성일시 : 2022.01.27
*	설명 : 탬플릿 목록 JavaScript
**********************************************************/

$(document).ready(function() {
	/* month picker */
	var today = getTodayType();
	var options = {
		pattern: 'yyyy.mm',
		//selectedYear: 2021,       // 선택할 연도
		//startYear: 2010,         // 시작연도
		//finalYear: 2022,          // 마지막연도
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		//openOnFocus: true,       // focus시에 달력이 보일지 유무
		//disableMonths : [ ]     // 월 비활성화
	};
	$("#monthPicker").monthpicker(options);
	//초기값
	function getTodayType(){
		var date = new Date();
		return date.getFullYear() + "." + ("0"+(date.getMonth()+1)).slice(-2);
	}
	$('#monthPicker').val(today);
	
	//월간달력버튼 monthPicker
	$('.btn-calendar').on('click', function(){		
		$("#monthPicker").datepicker().datepicker("show");
	});
	//일간달력버튼 mailSendcal
	$('#calDay').on('click', function(){
		$("#mailSendcal").datepicker().datepicker("show");
	});
	//월간 달력 변경시
	$("#monthPicker").on("change",function() {
			mailSendAnalyzeMon();
	});
	//일간 달력 변경시
	$("#mailSendcal").on("change",function() {
			mailSendAnalyzeDay();
	});
	
	
		
	//퀵메뉴 저장
	$("#quickMenuInsert").on("click",function(){
		
		var param = $("#insertQuickMenuForm").serialize();	
		
		$.getJSON("./insertMainQuickMenu.json?" + param, function(data) {
			if(data.result == "Success") { 
				alert("저장 되었습니다.!");
				quickMenuSet('save');
			} else {
				alert("저장에 실패하였습니다");
			}
		});
	})
	
	$("#quickMenuRefresh").on("click",function(){
		quickMenuSet('search') 
	})
	
	//메일분석 조회	
	mailAnalyze();
	//이메일 발송 추기 일간 조히
	mailSendAnalyzeDay();
	//이메일 발송 추기 월간 조회
	mailSendAnalyzeMon();		
	//발송일정
	goSearch(1)	
	//퀵메뉴 조회
	quickMenuSet('search') 
});

function checkBoxcheck(obj) {
	var cnt = 0;
	
	for(j = 0 ; j <	$("input:checkbox[name=qmenuYn]").length ; j++) {
		if($("input:checkbox[name=qmenuYn]").eq(j).is(':checked')) {
			cnt++;
		}
	}
		
	if(cnt > 5) {			
		alert("최대 등록 가능한 메뉴는 5개입니다.");
		$(obj).prop('checked',false);		
		return false;
	}		 		
}

//퀵메뉴 조회
function quickMenuSet(obj) {
	
	var strHtml = "";
	$("#userMenuView").empty();
	
	$.getJSON("./mainQuickMenu.json" , function(data) {	
		
		$.each( data.userMenu1, function( key1, value1 ){
			if(key1 == 10) {
				strHtml += "<li class=\"depth1 open\">"
				strHtml += "<ul>"
				strHtml += "<li class=\"col-box\">"
				strHtml += "<span class=\"col service\">"
				strHtml += "<button type=\"button\" class=\"btn-toggle\">"+value1 +"</button>"
				strHtml += "</span>"
				strHtml += "</li>"
				strHtml += "</ul>"
				$.each( data.sessionMenu, function( key1, value1 ){										
					if(value1.menulvlVal == 1 && value1.serviceGb == 10) {
						strHtml += "<ul class=\"depth2\">"
						strHtml += "<li class=\"open\">"
						strHtml += "<ul>"
						strHtml += "<li class=\"col-box\">"
						strHtml += "<span class=\"col menu\">"
						strHtml += "<button type=\"button\" class=\"btn-toggle\">"+value1.menuNm +"</button>"
						strHtml += "</span>"
						strHtml += "</li>"
						strHtml += "</ul>"
						strHtml += "<ul class=\"depth3\">";					
						$.each( data.sessionMenu, function( key2, value2 ){
							if(value1.menuId == value2.parentmenuId && value2.menulvlVal == 2 ) {
								strHtml += "<li class=\"col-box\">";
								strHtml += "<span class=\"col menu\">";
								strHtml += "<button type=\"button\" class=\"last\">"+ value2.menuNm +"</button>";
								strHtml += "</span>";
								strHtml += "<span class=\"col checkbox\">";
								
								var strValues1 = "";
								$.each(data.userMenu3[value2.menuId],function(key,value){
									strValues1 = value;
								})
								
								strHtml += "<label><input type=\"checkbox\" name=\"qmenuYn\" id=\""+  strValues1 +"\" onchange=\"checkBoxcheck(this)\" value=\""+value2.menuId+"\"><span></span></label>";
								strHtml += "</span>";
								strHtml += "</li>";														
							}						
						})	
						strHtml += "</ul>";
						strHtml += "</li>";		
						strHtml += "</ul>";				
					}
				})
				strHtml += "</li>"
			}
			if(key1 == 20) {
				strHtml += "<li class=\"depth1 open\">"
				strHtml += "<ul>"
				strHtml += "<li class=\"col-box\">"
				strHtml += "<span class=\"col service\">"
				strHtml += "<button type=\"button\" class=\"btn-toggle\">"+value1 +"</button>"
				strHtml += "</span>"
				strHtml += "</li>"
				strHtml += "</ul>"
				$.each( data.sessionMenu, function( key1, value1 ){										
					if(value1.menulvlVal == 1 && value1.serviceGb == 20) {
						strHtml += "<ul class=\"depth2\">"
						strHtml += "<li class=\"open\">"
						strHtml += "<ul>"
						strHtml += "<li class=\"col-box\">"
						strHtml += "<span class=\"col menu\">"
						strHtml += "<button type=\"button\" class=\"btn-toggle\">"+value1.menuNm +"</button>"
						strHtml += "</span>"
						strHtml += "</li>"
						strHtml += "</ul>"
						strHtml += "<ul class=\"depth3\">";					
						$.each( data.sessionMenu, function( key2, value2 ){
							if(value1.menuId == value2.parentmenuId && value2.menulvlVal == 2 ) {
								strHtml += "<li class=\"col-box\">";
								strHtml += "<span class=\"col menu\">";
								strHtml += "<button type=\"button\" class=\"last\">"+ value2.menuNm +"</button>";
								strHtml += "</span>";
								strHtml += "<span class=\"col checkbox\">";
								var strValues2 = "";
								$.each(data.userMenu3[value2.menuId],function(key,value){
									strValues2 = value;
								})
								strHtml += "<label><input type=\"checkbox\" name=\"qmenuYn\" id=\""+strValues2+"\" onchange=\"checkBoxcheck(this)\" value=\""+value2.menuId+"\"><span></span></label>";
								strHtml += "</span>";
								strHtml += "</li>";														
							}						
						})	
						strHtml += "</ul>";
						strHtml += "</li>";		
						strHtml += "</ul>";				
					}
				})
				strHtml += "</li>"
			}
			if(key1 == 99) {
				strHtml += "<li class=\"depth1 open\">"
				strHtml += "<ul>"
				strHtml += "<li class=\"col-box\">"
				strHtml += "<span class=\"col service\">"
				strHtml += "<button type=\"button\" class=\"btn-toggle\">"+value1 +"</button>"
				strHtml += "</span>"
				strHtml += "</li>"
				strHtml += "</ul>"
				$.each( data.sessionMenu, function( key1, value1 ){										
					if(value1.menulvlVal == 1 && value1.serviceGb == 99) {
						strHtml += "<ul class=\"depth2\">"
						strHtml += "<li class=\"open\">"
						strHtml += "<ul>"
						strHtml += "<li class=\"col-box\">"
						strHtml += "<span class=\"col menu\">"
						strHtml += "<button type=\"button\" class=\"btn-toggle\">"+value1.menuNm +"</button>"
						strHtml += "</span>"
						strHtml += "</li>"
						strHtml += "</ul>"
						strHtml += "<ul class=\"depth3\">";					
						$.each( data.sessionMenu, function( key2, value2 ){
							if(value1.menuId == value2.parentmenuId && value2.menulvlVal == 2 ) {
								strHtml += "<li class=\"col-box\">";
								strHtml += "<span class=\"col menu\">";
								strHtml += "<button type=\"button\" class=\"last\">"+ value2.menuNm +"</button>";
								strHtml += "</span>";
								strHtml += "<span class=\"col checkbox\">";
								var strValues3 = "";
								$.each(data.userMenu3[value2.menuId],function(key,value){
									strValues3 = value;
								})
								strHtml += "<label><input type=\"checkbox\" name=\"qmenuYn\" id=\""+strValues3+"\" onchange=\"checkBoxcheck(this)\" value=\""+value2.menuId+"\"><span></span></label>";
								strHtml += "</span>";
								strHtml += "</li>";														
							}						
						})	
						strHtml += "</ul>";
						strHtml += "</li>";		
						strHtml += "</ul>";				
					}
				})
				strHtml += "</li>"
			}
		})	
		
		
		$("#userMenuView").append(strHtml);
		
		
		//팝업 quickmenu 목록 생성
		var quickbtnHtml = "";
		var mainQuickMenuHtml = "";
		var mainQuickMenuHtml2 = "";
		$("#quickbtn").empty();					
		$.each( data.userQuickMenu, function( key3, value3 ){
			for(j = 0 ; j <	$("input:checkbox[name=qmenuYn]").length ; j++) {
				if($("input:checkbox[name=qmenuYn]").eq(j).val() == key3) {
					$("input:checkbox[name=qmenuYn]").eq(j).prop('checked',true);
					mainQuickMenuHtml2 += "<a href=\""+$("input:checkbox[name=qmenuYn]").eq(j).attr('id')+"\" class=\"btn\">"+value3+"</a>";
				}
			}
			quickbtnHtml +="<span class=\"btn\">" + value3;
			quickbtnHtml +="<button type=\"button\" class=\"btn-del\" onclick=\"quickMenuDel('"+key3+"')\"><span class=\"hide\">삭제</span></button>";
			quickbtnHtml +="</span >";			
									
		});								
		$("#quickbtn").append(quickbtnHtml);
		
		//메인 quickmenu 생성 
		$("#mainQuickMenu").empty();
		mainQuickMenuHtml = "<h3>Quick Menu</h3>";		
		mainQuickMenuHtml += mainQuickMenuHtml2;						
		mainQuickMenuHtml += "<button type=\"button\" class=\"btn-setting\" onclick=\"fn.popupOpen('#popup_quickmenu');\"><span class=\"hide\">설정</span></button>";						
		$("#mainQuickMenu").append(mainQuickMenuHtml);	
		
	});	
	
	if(obj == "save")  {
		fn.popupClose('#popup_quickmenu');
	}				
}

//퀵메뉴 삭제
function quickMenuDel(obj) {
	
	var param = $("#searchForm").serialize();	
	param +="&qmenuYn=" + obj;
	
	$.getJSON("./deleteMainQuickMenu.json?" + param, function(data) {
		if(data.result == "Success") { 
			alert("삭제되었습니다.!");
			quickMenuSet('search');
		} else {
			alert("삭제에 실패하였습니다");
		}
	});		
} 

//바형태 차트 그리기
function barChartSet(obj,arryTime,maxTask,maxTot,arryTask,arryTot,arrySucc,arryFail) {
	
	/* 기간별 발송 현황 막대그래프 */
	// 일간 그래프
	var barChart  = echarts.init(document.querySelector('#'+obj), null);
	var barChartoption = {
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'cross',
				crossStyle: {
					color: '#999'
				}
			}
		},
		legend: {
			top: 'bottom',
			bottom: '0',
			//icon:'circle',
			itemGap: 15,
			textStyle: {
				color: "#444",
				fontFamily: 'NotoSansKR',
				fontSize: 12,
				fontWeight: '500'
			},
			data: ['발송캠페인수', '총발송', '발송성공', '발송실패']
		},
		grid: {
			left: '0',
			right: '0',
			bottom: '35px',
			y: '30px',
			containLabel: true,
		},
		xAxis: [
			{
			type: 'category',
			data: arryTime ,// X축 데이터
				axisPointer: {
					type: 'shadow' 
				}
			}
		],
		yAxis: [
			{
				type: 'value',
				min: 0,
				max: maxTask,//좌측 task건
				//interval: 50,
				axisLabel: {
					formatter: '{value} 건'
				}
			},
			{
				type: 'value',
				min: 0,
				max: maxTot,//우측 발송건
				//interval: 50,
				axisLabel: {
					formatter: '{value} 건'
				}
			}
		],
		series: [
			{
				name: '발송캠페인수',
				type: 'bar',				
				data: arryTask
			},
			{
				name: '총발송',
				type: 'line',				
				yAxisIndex: 1,
				data: arryTot
			},
			{
				name: '발송성공',
				type: 'line',				
				yAxisIndex: 1,
				data: arrySucc
			},
			{
				name: '발송실패',
				type: 'line',				
				yAxisIndex: 1,
				data: arryFail
			},
		]
	};
	barChart.setOption(barChartoption);

	
	
	//그래프 반응형
	window.addEventListener('resize',function(){
		barChart.resize();	
	});
	
	
}

//파이형태 차트 그리기
function pieChartSet(objName,objData) {
	var pieChartDom = document.getElementById(objName);
	var pieChart1 = echarts.init(pieChartDom);	
	var pieChartOption;
	pieChartOption = {
		tooltip: {
			trigger: 'item'
		},
		series: [
			{	
				name: '도메인',//툴팁 대제목
				type: 'pie',
				top: '10px',						
				radius: ['63%', '90%'],
				avoidLabelOverlap: false,
				itemStyle: {
					borderRadius: 3,
					borderColor: '#fff',
					borderWidth: 2
				},
				label: {
					show: false,
				},
				emphasis: {
					label: {//파이차트 가운데 명칭
						show: false,
					}
				},
				labelLine: {
					show: false
				},
				data:objData
			}
		],
	};
	pieChartOption && pieChart1.setOption(pieChartOption);
	
	
	
	//그래프 반응형
	window.addEventListener('resize',function(){
		pieChart1.resize();		
	});
	
}


//메일 분석 호출 
function mailAnalyze(strDt) {
	
	var sysTime = $("#mailAnalyzePfTime").val();
	var param = $("#searchForm").serialize();	
	var nowTv =$("#mailAnalyzeTime").val();
	
	param +="&searchYmd=" + sysTime ;	
	param +="&searchPf=" + strDt ;				
	$("#mailAnalyzeTimeView").text(nowTv.substr(4,2)+"월   "+nowTv.substr(6,2)+"일   "+nowTv.substr(8,2)+":"+nowTv.substr(10,2));
			
	$.getJSON("./mainMailAnalyze.json?" + param, function(data) {	
		
		for(i=0 ; i < data.dayMailSummary.length ; i++) {			
			$("#dayMailSummaryCntTot").text(data.dayMailSummary[i].cntTot);
			$("#dayMailSummaryCntSucc").text(data.dayMailSummary[i].cntSucc);
			$("#dayMailSummaryCntFail").text(data.dayMailSummary[i].cntFail);			
		}	
		for(i=0 ; i < data.dayMailOpen.length ; i++) {
			console.log(data.dayMailOpen[i]);
			$("#dayMailOpenOpnTot").text(data.dayMailOpen[i].opnTot);			
		}	
		
		//도메인별 데이터 세팅		
		var dayMailDomainHtml = "";
		var arrayDomain = new Array();
		$("#dayMailDomainHtml").empty();
		for(i=0 ; i < data.dayMailDomain.length ; i++) {
			dayMailDomainHtml +="<tr>";
			dayMailDomainHtml +="<td>"+(i+1)+"</td>";
			dayMailDomainHtml +="<td>"+data.dayMailDomain[i].custDomain+"</td>";			
			dayMailDomainHtml +="<td>"+data.dayMailDomain[i].cntTot+"건</td>";		
			var searchData = new Object();
			searchData.value = data.dayMailDomain[i].cntTot;																	
			searchData.name = data.dayMailDomain[i].custDomain;
			arrayDomain.push(searchData);
		}		
		$("#dayMailDomainHtml").append(dayMailDomainHtml);
		pieChartSet("pieChart1",arrayDomain);
		
		//켐페인별 데이터 세팅
		var dayMailCampainHtml = "";
		var arrayCampain = new Array();
		$("#dayMailCampainHtml").empty();
		for(i=0 ; i < data.dayMailCampain.length ; i++) {
			dayMailCampainHtml +="<tr>";
			dayMailCampainHtml +="<td>"+(i+1)+"</td>";
			dayMailCampainHtml +="<td>"+data.dayMailCampain[i].campNm+"</td>";			
			dayMailCampainHtml +="<td>"+data.dayMailCampain[i].cntTot+"건</td>";	
			var searchData = new Object();
			searchData.value = data.dayMailCampain[i].cntTot;																	
			searchData.name = data.dayMailCampain[i].campNm;
			arrayCampain.push(searchData);																		
		}
		$("#dayMailCampainHtml").append(dayMailCampainHtml);
		pieChartSet("pieChart2",arrayCampain);
		//세부에러 데이터 세팅
		var dayMailDetailErrHtml = "";
		var arrayDetailErr = new Array();
		$("#dayMailDetailErrHtml").empty();
		for(i=0 ; i < data.dayMailDetailErr.length ; i++) {
			dayMailDetailErrHtml +="<tr>";
			dayMailDetailErrHtml +="<td>"+(i+1)+"</td>";
			dayMailDetailErrHtml +="<td>"+data.dayMailDetailErr[i].sendRcodeNm+"</td>";			
			dayMailDetailErrHtml +="<td>"+data.dayMailDetailErr[i].cntErr+"건</td>";	
			var searchData = new Object();
			searchData.value = data.dayMailDetailErr[i].cntErr;																	
			searchData.name = data.dayMailDetailErr[i].sendRcodeNm;
			arrayDetailErr.push(searchData);																		
		}
		$("#dayMailDetailErrHtml").append(dayMailDetailErrHtml);
		pieChartSet("pieChart3",arrayDetailErr);
				
		$("#mailAnalyzeTime").val(data.systemTimes);
		$("#mailAnalyzePfTime").val(data.serarchTimes);
		
		
		$("#mailAnalyzeDateView").text(data.serarchTimes.substr(4,2)+"월   "+data.serarchTimes.substr(6,2)+"일  메일분석");
		
	});
	
}	

//이메일 발송 추이 
function mailSendAnalyzeDay(strDt) {
	
	var sysTime = $("#mailSendcal").val().replace(/\./gi, "");	
	var nowTv =$("#mailSendTime").val();
	$("#mailSendTimeView").text(nowTv.substr(4,2)+"월   "+nowTv.substr(6,2)+"일   "+nowTv.substr(8,2)+":"+nowTv.substr(10,2));
		
	var param = $("#searchForm").serialize();
	param +="&searchYmd=" + sysTime ;	
	param +="&searchPf=" + strDt ;					
			
	$.getJSON("./mainMailsend.json?" + param, function(data) {

		var arryTime = []; 
		var arryTask = [];
		var arryTot = [];
		var arrySucc = [];
		var arryFail = [];

		for(i=0 ; i < data.mailSendInfoDay.length ; i++) {						
		
			arryTime.push(data.mailSendInfoDay[i].times +":00");
			arryTask.push(data.mailSendInfoDay[i].cntTask);
			arryTot.push(data.mailSendInfoDay[i].cntTot);
			arrySucc.push(data.mailSendInfoDay[i].cntSucc);
			arryFail.push(data.mailSendInfoDay[i].cntFail);
			
		}
		
		barChartSet("barChart",arryTime,Math.max.apply(null,arryTask),Math.max.apply(null,arryTot),arryTask,arryTot,arrySucc,arryFail);
						
		$("#mailSendTime").val(data.systemTimes);
		$("#mailSendcal").val(data.serarchTimes.substr(0,4)+"."+data.serarchTimes.substr(4,2)+"."+data.serarchTimes.substr(6,2));
		
	});
			
}

//이메일 발송 추이 
function mailSendAnalyzeMon(strDt) {
	
	var sysTime = $("#monthPicker").val().replace(/\./gi, "");		
	var nowTv =$("#mailSendTime").val();	
	$("#mailSendTimeView").text(nowTv.substr(4,2)+"월   "+nowTv.substr(6,2)+"일   "+nowTv.substr(8,2)+":"+nowTv.substr(10,2));
	
	var param = $("#searchForm").serialize();
	param +="&searchYmd=" + sysTime ;	
	param +="&searchPf=" + strDt ;				
			
	$.getJSON("./mainMailsend.json?" + param, function(data) {	
		
		var arryTime = []; 
		var arryTask = [];
		var arryTot = [];
		var arrySucc = [];
		var arryFail = [];

		for(i=0 ; i < data.mailSendInfoMons.length ; i++) {						
		
			arryTime.push(data.mailSendInfoMons[i].days +"일");
			arryTask.push(data.mailSendInfoMons[i].cntTask);
			arryTot.push(data.mailSendInfoMons[i].cntTot);
			arrySucc.push(data.mailSendInfoMons[i].cntSucc);
			arryFail.push(data.mailSendInfoMons[i].cntFail);
			
			
		}
		
		barChartSet("barChart2",arryTime,Math.max.apply(null,arryTask),Math.max.apply(null,arryTot),arryTask,arryTot,arrySucc,arryFail);
						
		$("#mailSendTime").val(data.systemTimes);
		$("#monthPicker").val(data.serarchTimes.substr(0,4)+"."+data.serarchTimes.substr(4,2));
		
	});
			
}

//검색 버튼 클릭
function goSearch(pageNum) {
			
	$("#searchForm input[name='page']").val(pageNum);
	var param = $("#searchForm").serialize();
	
	$.ajax({
		type : "GET",
		url : "./indexList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#tempBoard").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 실패하였습니다");
		}
	});
}
//검색 버튼 클릭
function goSearchDate(pageNum,searchPf,searchDt) {
			
	$("#searchForm input[name='searchBoardDt']").val(searchDt);		
	$("#searchForm input[name='page']").val(pageNum);
	$("#searchForm input[name='searchBoardPf']").val(searchPf);
		
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./indexList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#tempBoard").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 실패하였습니다");
		}
	});
}

//페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}
function gomailAprListP(startDt,endDt,userId,deptNo,workStatus) {
	$("#mailForm input[name='searchStartDt']").val(startDt);		
	$("#mailForm input[name='searchEndDt']").val(endDt);		
	$("#mailForm input[name='searchDeptNo']").val(deptNo);		
	$("#mailForm input[name='searchUserId']").val(userId);
	$("#mailForm input[name='searchWorkStatus']").val(workStatus);	
	
	$.getJSON("/ems/sch/scheduleGrant.json?searchSendRepeat=" + "M1003004", function(data) {
		if(data.result == "Success") {				
			$("#mailForm").attr("action", "/ems/apr/mailAprListP.ums");
			$("#mailForm").attr("method", "post");
			$("#mailForm").submit();
		} else if(data.result == "Fail") {
				alert("메일발송결재 권한이 없습니다"); 
		}
	});
}
function goUpdate(taskNo,subTaskNo,approvalProcAppYn,workStatus) {
	
	$("#searchFormMail input[name='taskNo']").val(taskNo);		
	$("#searchFormMail input[name='subTaskNo']").val(subTaskNo);
	$("#searchFormMail input[name='approvalProcAppYn']").val(approvalProcAppYn);	
	
	
	$.getJSON("/ems/sch/scheduleGrant.json?searchSendRepeat=" + "M1002001", function(data) {
		if(data.result == "Success") {				
			if(workStatus=="000" || workStatus=="201") {	
				$("#searchFormMail").attr("target","").attr("action","/ems/cam/taskUpdateP.ums").submit();
			} else {
				if(workStatus=="202" && approvalProcAppYn == "N") {
					$("#searchFormMail").attr("target","").attr("action","/ems/cam/taskUpdateP.ums").submit();	
				} else {
					$("#searchFormMail").attr("target","").attr("action","/ems/cam/taskUpdateDateP.ums").submit();
				}
			}	
		} else if(data.result == "Fail") {
			alert("메일발송 권한이 없습니다!"); 
		}
	});
						
}

