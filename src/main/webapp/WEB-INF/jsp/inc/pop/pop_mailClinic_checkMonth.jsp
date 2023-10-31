<%--
	/**********************************************************
	*	작성자 : 박찬용
	*	작성일시 : 2022.01.26
	*	설명 : 일별 일정 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 팝업 : 일정 적용// -->
<div id="popup_checkmonth" class="poplayer popcheckmonth"><!-- id값 수정 가능 -->
	<div class="inner small">
		<header>
			<h2>일정 적용</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<ul>
					<li>
						<label for="chk_01"><input type="checkbox" name="chk_month" id="chk_01" value="1"><span>1월</span></label>
						<label for="chk_02"><input type="checkbox" name="chk_month" id="chk_02" value="2"><span>2월</span></label>
						<label for="chk_03"><input type="checkbox" name="chk_month" id="chk_03" value="3"><span>3월</span></label>
						<label for="chk_04"><input type="checkbox" name="chk_month" id="chk_04" value="4"><span>4월</span></label>
						<label for="chk_05"><input type="checkbox" name="chk_month" id="chk_05" value="5"><span>5월</span></label>
						<label for="chk_06"><input type="checkbox" name="chk_month" id="chk_06" value="6"><span>6월</span></label>
					</li>
					<li>
						<label for="chk_07"><input type="checkbox" name="chk_month" id="chk_07" value="7"><span>7월</span></label>
						<label for="chk_08"><input type="checkbox" name="chk_month" id="chk_08" value="8"><span>8월</span></label>
						<label for="chk_09"><input type="checkbox" name="chk_month" id="chk_09" value="9"><span>9월</span></label>
						<label for="chk_10"><input type="checkbox" name="chk_month" id="chk_10" value="10"><span style="margin-right:14px;">10월</span></label>
						<label for="chk_11"><input type="checkbox" name="chk_month" id="chk_11" value="11"><span style="margin-right:14px;">11월</span></label>
						<label for="chk_12"><input type="checkbox" name="chk_month" id="chk_12" value="12"><span>12월</span></label>
					</li>
				</ul>
				
				<!-- btn-wrap// -->
				<div class="btn-wrap">
					<button type="button" class="btn fullred" id="allMonthUpdate">적용</button>
				</div>
				<!-- //btn-wrap -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_checkmonth');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_checkmonth');"></span>
</div>
<!-- //팝업 : 일정 적용-->