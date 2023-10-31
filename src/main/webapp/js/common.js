/**
* --------------------------------
* Common JS
* creator : chowoobin
* from : enders
* --------------------------------
*/
var fn = (function() {
	"use strict";

	return {
		//공통
		common: function() {
			fn.lnb();
			fn.conthead();

			//toggle - 형제 depth 접히는 이벤트
			fn.toggle();

			//toggleunfold - 형제 depth 안접히는 이벤트
			fn.toggleunfold();

			//tab
			fn.tab();

			//tableinfobox
			fn.tableinfobox();

			//fileupload
			fn.fileupload();

			// editpassword
			fn.editPassword();

			// show password
			fn.showPassword();

		},

		//lnb
		lnb: function() {

			//1depth 메뉴를 클릭했을 때
			$(document).on("click", ".gnb .btn-menu", function() {

				if ($(this).hasClass("selected")) {
					$(this).siblings(".dep2").slideUp();
					$(this).removeClass("selected");
				} else {
					$(".dep2").slideUp();
					$(this).siblings(".dep2").slideDown();
					$(".btn-menu").removeClass("selected");
					$(this).addClass("selected");
				}
			});

			// 2depth menu를 클릭했을 때
			$(document).on("click", ".gnb .dep2 .btn-submenu", function() {

				if ($(this).hasClass("selected")) {
					$(this).removeClass("selected");
				} else {
					$(this).addClass("selected").closest("li").siblings().find(".btn-submenu").removeClass("selected");
				}
			});

		},

		//conthead
		conthead: function() {
			//cont-head util user
			$(document).on("click", ".util .info button", function() {
				if ($(this).closest(".user").hasClass("open")) {
					$(this).closest(".user").removeClass("open");
					//$(this).text("열기");
				} else {
					$(this).closest(".user").addClass("open");
					// $(this).text("닫기");
				}
			});

			//cont-head util service
			$(document).on("click", ".util .btn-util.item01", function() {
				if ($(".util .service").hasClass("open")) {
					$(".util .service").removeClass("open");
				} else {
					$(".util .service").addClass("open");
				}
			});
		},

		//toggle
		toggle: function() {
			$(document).on("click", ".toggle .btn-toggle", function() {
				if ($(this).closest("li").hasClass("open")) {
					$(this).closest("li").removeClass("open");
				} else {
					$(this).closest("li").addClass("open").siblings().removeClass("open");
				}
			});
			$(document).on("click", ".toggle button", function() {
				if (!$(this).hasClass(".btn-toggle")) {
					$(".toggle li").removeClass("active");
					$(this).closest("li").addClass("active");
				}
			});
		},

		//toggleunfold
		toggleunfold: function() {
			$(document).on("click", ".toggle-unfold .btn-toggle", function() {
				if ($(this).closest("ul").parent("li").hasClass("open")) {
					$(this).closest("ul").parent("li").removeClass("open");
				} else {
					$(this).closest("ul").parent("li").addClass("open");
				}
			});
			$(document).on("click", ".toggle-unfold button", function() {
				if (!$(this).hasClass(".btn-toggle")) {
					$(".toggle-unfold li").removeClass("active");
					$(this).closest(".col-box").parent("li").addClass("active");
				}
			});
		},

		//tab
		tab: function() {
			//$(".tab-menu > a:first").addClass("active");
			//$(".tab-cont > div:first").addClass("active");
			$(document).on("click", ".tab-menu a", function() {
				var index = $(this).index();
				$(this).addClass("active").siblings().removeClass("active");
				$(this).closest(".tab").find(".tab-cont > div").eq(index).addClass("active").siblings().removeClass("active");//탭이 1페이지에 2개 이상일 때
			});
		},

		//tableinfobox
		tableinfobox: function() {
			$(document).on("click", ".tableinfobox .grid .btn-meta", function() {
				console.log("aa");
				$(this).closest(".grid").find(".btn-meta").removeClass("active");
				$(this).addClass("active");
			});
		},

		//html등록팝업, 파일등록팝업 내 fileupload
		fileupload: function() {
			var fileTarget = $(".fileupload input[type='file']"),
				filevalue,
				filename; // 파일명

			fileTarget.on("change", function(obj) { // 값이 변경되면
				filevalue = $(this).val().split("\\");
				filename = filevalue[filevalue.length - 1]; // 파일명
				var error = $(obj).closest(".fileupload").siblings(".list-star");

				$(this).siblings(".upload-name").val(filename);

				// 파일 형식 체크
				if (filename.substring(filename.lastIndexOf(".") + 1, filename.length).search("html") == -1) {
					if (error.hasClass("hide")) {
						error.removeClass("hide");
					}
				} else {
					error.addClass("hide");

				}

			});
		},

		//html등록팝업, 파일등록팝업 내 fileReset
		fileReset: function(obj) {
			var $fileupload = $(obj).closest(".poplayer").find(".fileupload");

			$fileupload.find("button.btn").addClass("hide");
			$fileupload.find("label.btn").removeClass("hide");
			$fileupload.find("input[type='file']").val("");
			$fileupload.find("input[type='text']").val("");
			$fileupload.find(".filesize").text("");
			$fileupload.siblings(".list-star").addClass("hide");
		},

		//html등록팝업, 파일등록팝업 내 fileCheck
		fileCheck: function(obj) {
			var filevalue = $(obj).val().split("\\"),
				filedir = filevalue[filevalue.length - 1], // 파일명
				error = $(obj).closest(".fileupload").siblings(".list-star"),
				filesize = $(obj).siblings(".filesize");

			// 파일 형식 체크
			if (filedir.substring(filedir.lastIndexOf(".") + 1, filedir.length).search("html") == -1) {
				if (error.hasClass("hide")) {
					error.removeClass("hide");
				}
			} else {
				error.addClass("hide");
			}

			// 용량 체크
			if ($(obj).val() != "") {
				var fileSize = obj.files[0].size;
				$(obj).closest(".inputfile").siblings("button.btn").removeClass("hide");
				$(obj).closest(".inputfile").siblings("label.btn").addClass("hide");
			}
			var s = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'],
				e = Math.floor(Math.log(fileSize) / Math.log(1024));
			filesize.text((fileSize / Math.pow(1024, e)).toFixed(2) + " " + s[e]);
			return (fileSize / Math.pow(1024, e)).toFixed(2) + " " + s[e];
		},

		//editPassword
		editPassword: function() {
			$(document).on("click", ".editpw > .title-area > button", function() {
				if ($(this).closest(".title-area").parent("div").hasClass("active")) {
					$(this).closest(".title-area").siblings("div").slideUp();
					$(this).closest(".title-area").parent("div").removeClass("active");

				} else {
					$(this).closest(".title-area").siblings("div").slideDown();
					$(this).closest(".title-area").parent("div").addClass("active");
				}
			});
		},

		//showPassword
		showPassword: function() {
			$(document).on("click", ".editpw .btn-pwshow", function() {
				if ($(this).hasClass("active")) {
					$(this).removeClass("active");
					$(this).prev("input").attr("type", "password");
				} else {
					$(this).addClass("active");
					$(this).prev("input").attr("type", "text");
				}

			});
		},

		//popupOpen
		popupOpen: function(obj) {
			$(obj).addClass("open");
			$("body").addClass("ov-hidden");
		},

		//popupClose
		popupClose: function(obj) {
			$(obj).removeClass("open");
			$("body").removeClass("ov-hidden");
		},
	}
})();

$(document).ready(function() {

	//datepicker
	$('.datepicker input').datepicker({
		dateFormat: 'yy.mm.dd', //날짜 포맷이다.
		//prevText: '이전 달',	// 마우스 오버시 이전달 텍스트
		//nextText: '다음 달',	// 마우스 오버시 다음달 텍스트
		closeText: '닫기', // 닫기 버튼 텍스트 변경
		currentText: '오늘', // 오늘 텍스트 변경
		changeMonth: true,
		changeYear: true,
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더중 월 표시를 위한 부분
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더 중 월 표시를 위한 부분
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],	//한글 캘린더 요일 표시 부분
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],	//한글 요일 표시 부분
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],	// 한글 요일 표시 부분
		showMonthAfterYear: true,	// true : 년 월	false : 월 년 순으로 보여줌
		yearSuffix: '년',
		showButtonPanel: true,	// 오늘로 가는 버튼과 달력 닫기 버튼 보기 옵션
	}).datepicker('setDate', new Date());

	//시작일.
	$('.datepickerrange.fromDate input').datepicker({
		dateFormat: 'yy.mm.dd', //날짜 포맷이다.
		closeText: '닫기', // 닫기 버튼 텍스트 변경
		currentText: '오늘', // 오늘 텍스트 변경
		changeMonth: true,
		changeYear: true,
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더중 월 표시를 위한 부분
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더 중 월 표시를 위한 부분
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],	//한글 캘린더 요일 표시 부분
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],	//한글 요일 표시 부분
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],	// 한글 요일 표시 부분
		showMonthAfterYear: true,	// true : 년 월	false : 월 년 순으로 보여줌
		yearSuffix: '년',
		showButtonPanel: true,	// 오늘로 가는 버튼과 달력 닫기 버튼 보기 옵션

		onClose: function(selectedDate) {
			// 시작일(fromDate) datepicker가 닫힐때
			// 종료일(toDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
			$(".datepickerrange.toDate input").datepicker("option", "minDate", selectedDate);
		}
	});

	//종료일
	$('.datepickerrange.toDate input').datepicker({
		dateFormat: 'yy.mm.dd', //날짜 포맷이다.
		closeText: '닫기', // 닫기 버튼 텍스트 변경
		currentText: '오늘', // 오늘 텍스트 변경
		changeMonth: true,
		changeYear: true,
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더중 월 표시를 위한 부분
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더 중 월 표시를 위한 부분
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],	//한글 캘린더 요일 표시 부분
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],	//한글 요일 표시 부분
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],	// 한글 요일 표시 부분
		showMonthAfterYear: true,	// true : 년 월	false : 월 년 순으로 보여줌
		yearSuffix: '년',
		showButtonPanel: true,	// 오늘로 가는 버튼과 달력 닫기 버튼 보기 옵션

		onClose: function(selectedDate) {
			// 종료일(toDate) datepicker가 닫힐때
			// 시작일(fromDate)의 선택할수있는 최대 날짜(maxDate)를 선택한 종료일로 지정 
			$(".datepickerrange.fromDate input").datepicker("option", "maxDate", selectedDate);
		}
	});

});


$(window).on("load", function() {
	//init
	fn.common();
});
