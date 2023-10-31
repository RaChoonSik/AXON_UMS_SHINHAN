<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.11.30
	*	설명 : 정보보안서약서 동의 여부 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="util" class="com.mp.util.InitApp" scope="application" />
<jsp:useBean id="resp" class="com.mp.util.RespLog" scope="application" />
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>


<script type="text/javascript">
 
function setOath(){
	$.post("/sys/acc/setUserOath.json", function(data) {
		if(data.result =="Fail") {
			alert("정보보안서약서 동의에 실패했습니다 잠시 후 다시 이용해주세요");
		}else{
			document.location.href = "<c:url value='/ems/index.ums'/>";
		}
	}); 
}
</script>
<body>
	<div id="wrap">
		<div id="service">
			<fieldset>
				<section class="service-inner">
					<h1 class="ttl">정보보호서약서</h1>
					<div class="form-box">
						<div>
							<h6>
							롯데잇츠 고객정보 취급자 정보보호 각서<br>본인은 고객정보 취급 및 관리 업무를 수행함에 있어 다음 사항을 준수할 것을 서약합니다.
							</h6>	
							<p>1. 고객정보 취급 및 관리 업무에 대한 접근 권한을 취득 시에는 정보보호 담당자 승인을 득하고 롯데지알에스㈜의 정보보안 규정에 명시된 책임과 의무를 다 할 것을 서약합니다.</p>
							<p>2. 고객정보 취급 및 관리 업무를 수행함에 있어 업무에 관련된 범위 내에 한하여 고객정보의 접근, 취득, 활용하고 업무 중 인지한 고객관련 정보를 업무 이외에 사내 및 외부에 누설하지 않을 것을 서약합니다.</p>
							<p>3. 고객정보를 업무에 관련하여 개인 PC 등에 저장할 때에는 반드시 암호화 하며, 고객정보를 사내 타부서 또는 외부로 송부시에는 정해진 절차에 따라 부서장의 사전 승인을 득한 후 암호화 하여 송부할 것을 서약합니다.</p>
							<p>4. 고객정보 취급 및 관리 업무가 종료된 경우, 그 시점에서 본인이 보유하고 있는 고객정보관련 자료 및 파일을 즉시 업무 인수자에게 인계 및 폐기하고 업무 중 인지한 고객정보를 외부유출 및 다른 목적으로 사용하지 않을 것을 서약합니다.</p>
							<p>5. 고객 정보 유출 및 침해사고에 대하여 상시 철저히 대비하고, 보안 사고 발생 시 보안관려 규정에 명시된 침해사고 대응 지침에 의거하여 신속히 보고 및 조치할 것을 서약합니다.</p>
							<p>6. 보안관리 규정 미준수 및 관리소홀로 인한 개인정보 유출 사고로 회사에 손해가 발생할 시에는 본인은 정해진 절차에 따라 손해배상 및 법적 제재를 받더라도 수용할 것임을 서약합니다.</p>							
						</div>
						<a class="btn blue service" href="javascript:setOath();">모두동의하고 시작</a>
					</div>
				</section>
				<p class="copyright">Copyright URACLE CORPORATION. ALL RIGHTS RESERVED.</p>
			</fieldset> 
		</div>
	</div>

</body>
</html>
