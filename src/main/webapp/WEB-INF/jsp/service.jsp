<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.05
	*	설명 : 서비스 선택화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="util" class="com.mp.util.InitApp" scope="application" />
<jsp:useBean id="resp" class="com.mp.util.RespLog" scope="application" />
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>


<script type="text/javascript">

/* $(document).ready(function() {

	if ( ${NEO_LINK} > 0 ){
		var checkboxes = document.getElementsByName('chkLink');
    	for(var i = 0; i<checkboxes.length;i++){  
    		 if(  ${NEO_LINK} == $(checkboxes[i]).val()){
    			 $(checkboxes[i]).prop("checked", true);`
    		 }
		} 
	} 
	 
	$('input[type=checkbox]').click(function() {
		var serviceGb = $(this).val()
		if($(this).is(":checked") == true){l
			var checkboxes = document.getElementsByName('chkLink');
	    	for(var i = 0; i<checkboxes.length;i++){ 
	    		console.log($(checkboxes[i]).val());
	    		 if( serviceGb != $(checkboxes[i]).val()){
	    			 $(checkboxes[i]).prop("checked", false);
	    		 }
			}    
		} else {
			serviceGb = 0; 
		}
		makeLink(serviceGb);
	   
	});
});
 */
function goEMS(payYn) {
	if(payYn == 1) { 
		document.location.href = "<c:url value='/ems/index.ums'/>";
	} else {
		alert("라이선스키가 유효하지 않습니다");
	}
}
 
/* function goLogOut() {
	document.location.href = "<c:url value='/lgn/logout.ums'/>";
} */
 
</script>

<body>
	<div id="wrap">

		<!-- service// -->
		<div id="service">
			<section class="service-inner">
				<h1 class="logo-box">
					<img src="../img/common/logo_uracle.png" alt="ums로고">  
				</h1>
				<div class="list-area">
					  <ul class="service-list">
						 <li> 
							<a href="javascript:goEMS('<c:out value='${userServiceList[0].payYn}'/>');">
								<strong> 이메일 발송 시스템</strong>
							</a>
	  					  </li> 
					</ul>
				</div>
			</section>

			 <footer class="ums-footer">
				<p>Copyright URACLE CORPORATION. ALL RIGHTS RESERVED</p> 
			</footer>
		</div>
		<!-- //service -->

	</div>

</body>
</html>
