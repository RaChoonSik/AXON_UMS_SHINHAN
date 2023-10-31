<%--
	/**********************************************************
	*	작성자 : 박찬용
	*	작성일시 : 2022.01.08
	*	설명 : 도메인 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp"%>

<style>
/* RNS기준정보 > 메일클리닉 일정관리 */
.year-wrap .standard-year {
	margin-bottom: 0;
	padding-left: 1.2rem;
	margin-top: 3rem;
}

.year-wrap .standard-year span {
	position: relative;
	font-size: 18px;
	vertical-align: middle;
}

.year-wrap .standard-year span::before {
	content: '';
	position: absolute;
	top: 5px;
	left: -8px;
	width: 2px;
	height: 18px;
	background: #444;
}

.year-wrap .standard-year .select {
	width: 80px;
	margin-left: 5px;
}

.table-month {
	margin-top: 1.5rem;
	border-radius: 5px !important;
	overflow: hidden;
	border: 1px solid #e1e1e1;
}

.table-month th {
	position: relative;
	padding: 9px 20px;
	background: #fff;
	text-align: left;
	border-left: 1px solid #E1E1E1;
}

.table-month th::before {
	content: '';
	position: absolute;
	top: 50%;
	left: 0;
	width: .4rem;
	height: 2.6rem;
	background-color: #2bd3f7;
	border-top-right-radius: .6rem;
	border-bottom-right-radius: .6rem;
	-webkit-transform: translateY(-50%);
	-ms-transform: translateY(-50%);
	transform: translateY(-50%);
}

.table-month th:after {
	content: '';
	display: block;
	clear: both;
}

.table-month th .month-title {
	color: #111;
	font-size: 16px;
	font-weight: 500;
	line-height: 30px;
}

.table-month th .btn-area {
	float: right;
	margin-top: 0;
	text-align: right;
	vertical-align: top;
}

.table-month th .btn-area .btn {
	padding: 0 8px;
}

.table-month td {
	padding: 15px 20px;
	border-left: 1px solid #E1E1E1;
	border-bottom: 1px solid #E1E1E1;
	background:#fff;
}

.table-month tr>td:first-child, .table-month tr>th:first-child {
	border-left: 0;
}

.table-month td .table-item:not(:first-child) {
	margin-top: 10px;
}

.table-month td .table-item label {
	display: inline-block;
	width: 65px;
	height: 32px;
	color: #444444;
	font-size: 12px;
	font-weight: 600;
	border-radius: 6px;
	vertical-align: top;
	line-height: 32px;
}

.table-month td .table-item .select {
	display: inline-block;
	width: calc(100% - 74px);
}

/* .table-month td .btn-wrap .btn {
	min-width: 130px;
	transition: all 0.4s;
} */

@media screen and (max-width:1650px) {
	.table-month td .btn-wrap .btn {
		min-width: 80px;
	}
}
/* popup :: 일정관리 */
.popcheckmonth .cont {
	padding-bottom: 20px;
}

.popcheckmonth input[type="checkbox"]+span {
	margin-right: 20px;
}

/*  .popcheckmonth input[type="checkbox"]+span:before {
	background: url("../../img/sys/form_chk.png") no-repeat 0 0;
}

.popcheckmonth input[type="checkbox"]:checked+span:before {
	background-position-y: -25px;
}

.popcheckmonth input[type="checkbox"]:disabled+span:before {
	background-position-y: -50px;
}

.popcheckmonth input[type="checkbox"]:checked:disabled+span:before {
	background-position-y: -75px; 
}
 */

.popcheckmonth ul li:first-child {
	margin-bottom: 20px;
}

.popcheckmonth ul li label:last-child input[type="checkbox"]+span {
	margin-right: 0;
}

.popcheckmonth .btn-wrap {
	margin-top: 30px;
}
</style>
<script type="text/javascript" src="<c:url value='/js/sys/rns/mailClinicListP.js'/>"></script>

<body>
	<div id="wrapper" class="sys">
		<header>
			<h1 class="logo">
				<a href="/ems/index.ums"><span class="txt-blind">LOGO</span></a>
			</h1>
			<!-- 공통 표시부// -->
			<%@ include file="/WEB-INF/jsp/inc/top.jsp"%>
			<!-- //공통 표시부 -->
		</header>
		<div id="wrap" class="sys">

			<!-- lnb// -->
			<div id="lnb">
				<!-- LEFT MENU -->
				<%@ include file="/WEB-INF/jsp/inc/menu_sys.jsp"%>
				<!-- LEFT MENU -->
			</div>
			<!-- //lnb -->
			<div class="content-wrap">
				<!-- content// -->
				<div id="content">

					<!-- cont-head// -->
					<section class="cont-head">
						<div class="title">
							<h2>메일클리닉 일정 관리</h2>
						</div>


					</section>
					<!-- //cont-head -->
					<!-- cont-body// -->
					<section class="cont-body">
						<fieldset>
							<legend>메일클리닉 일정 관리</legend>
							<!-- 일정관리// -->
							<div class="year-wrap">
								<h3 class="standard-year">
									<span>기준연도</span>
									<div class="select">
										<select title="옵션 선택" name="clsY" id="clsY"></select>
									</div>
								</h3>

								<!-- table-month// -->
								<div class="table-month">
									<table>
										<caption>일정관리 표</caption>
										<colgroup>
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
											<col style="width: 25%;">
										</colgroup>
										<tbody>
											<tr>
												<th scope="col"><strong class="month-title">1월</strong>

													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('1')">삭제</button>
													</div></th>
												<th scope="col"><strong class="month-title">2월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('2')">삭제</button>
													</div></th>
												<th scope="col"><strong class="month-title">3월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('3')">삭제</button>
													</div></th>
												<th scope="col"><strong class="month-title">4월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('4')">삭제</button>
													</div></th>
											</tr>

											<tr>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('1');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('1');">당월수정</button>
													</div>
												</td>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('2');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('2');">당월수정</button>
													</div>
												</td>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('3');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('3');">당월수정</button>
													</div>
												</td>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('4');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('4');">당월수정</button>
													</div>
												</td>
											</tr>

											<tr>
												<th scope="col"><strong class="month-title">5월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('5')">삭제</button>
													</div></th>
												<th scope="col"><strong class="month-title">6월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('6')">삭제</button>
													</div></th>
												<th scope="col"><strong class="month-title">7월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('7')">삭제</button>
													</div></th>
												<th scope="col"><strong class="month-title">8월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('8')">삭제</button>
													</div></th>
											</tr>

											<tr>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('5');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('5');">당월수정</button>
													</div>
												</td>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('6');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('6');">당월수정</button>
													</div>
												</td>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('7');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('7');">당월수정</button>
													</div>
												</td>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('8');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('8');">당월수정</button>
													</div>
												</td>
											</tr>

											<tr>
												<th scope="col"><strong class="month-title">9월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('9')">삭제</button>
													</div></th>
												<th scope="col"><strong class="month-title">10월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('10')">삭제</button>
													</div></th>
												<th scope="col"><strong class="month-title">11월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('11')">삭제</button>
													</div></th>
												<th scope="col"><strong class="month-title">12월</strong>
													<div class="btn-area">
														<button class="btn" onclick="nowMonthDel('12')">삭제</button>
													</div></th>
											</tr>

											<tr>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('9');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('9');">당월수정</button>
													</div>
												</td>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('10');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('10');">당월수정</button>
													</div>
												</td>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('11');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('11');">당월수정</button>
													</div>
												</td>
												<td>
													<div class="table-item">
														<label>실행일자</label>
														<div class="select">
															<select title="옵션 선택" name="clsDay">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>오류횟수</label>
														<div class="select">
															<select title="옵션 선택" name="clsErrCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="table-item">
														<label>마감기간</label>
														<div class="select">
															<select title="옵션 선택" name="clsEndMmCnt">
																<option>선택</option>
																<option>옵션1</option>
																<option>옵션2</option>
															</select>
														</div>
													</div>

													<div class="btn-wrap">
														<button type="button" class="btn fullred" onclick="batchApply('12');">일괄적용</button>
														<button type="button" class="btn" onclick="nowMonthUpdate('12');">당월수정</button>
													</div>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<!-- //table-month -->
							</div>
							<!-- //일정관리 -->
						</fieldset>
					</section>
					<!-- //cont-body -->
				</div>
				<!-- // content -->
			</div>
			<!-- 월 일괄팝업// -->
			<%@ include file="/WEB-INF/jsp/inc/pop/pop_mailClinic_checkMonth.jsp"%>
			<!-- //월 일괄팝업 -->
		</div>
	</div>
</body>
</html>
