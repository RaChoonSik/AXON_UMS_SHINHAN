/**
 * 작성자 : 김준희
 * 작성일시 : 2022.09.27
 * 설명 : 캠페인 템플릿  관리 VO
 */
package kr.co.enders.ums.ems.cam.vo;

import java.util.Date;

import kr.co.enders.ums.com.vo.CommonVO;

public class CampaignTemplateVO extends CommonVO {
	private int tid;				// 서비스ID
	private String tnm;				// 서비스명
	private String tdesc;			// 비고
	private String sname;			// 발송자명
	private String smail;			// 발송자이메일
	private String sid;				// 발송자ID
	private String emailSubject;	// 이메일제목
	private String contentsPath;	// 컨텐츠경로
	private String contentsTyp;		// 컨텐츠타입
	private String contentsTypNm;	// 컨텐츠타입명
	private int attchCnt;			// 첨부파일수
	private String recvChkYn;		// 수신확인여부
	private String userId;			// 사용자ID
	private String userNm;			// 사용자명
	private int deptNo;				// 사용자그룹
	private String deptNm;			// 사용자그룹명
	private String useYn;			// 사용여부
	private String status;			// 상태
	private String statusNm;		// 상태명
	private String eaiCampNo;		// EAI 번호
	private String workStatus;		// 발송상태
	private String workStatusNm;	// 발송상태명
	private String regId;			// 생성자ID
	private String regNm;			// 생성자명
	private String regDt;			// 생성일시
	private String upId;			// 수정자ID
	private String upNm;			// 수정자명
	private String upDt;			// 수정일시
	private int campNo;				// 캠페인번호
	private int segNo;				// 수신자그룹 번호 
	
	private String segNoc;			// 번호가 캐릭터로 사용되는경우 segno + mergekey
	private String segNm;			// 수신자그룹명
	private String mergeKey;		// 머지키
	private String mergeCol;		// 머지컬럼
	private String campNm;			// 캠페인명
	
	private String orgKorNm;		// 부서
	private String imgChkYn;		// 이미지 포함 여부 
	private String prohibitChkTyp;  // 금칙어
	private String titleChkYn;		// 제목고객정보체크여부
	private String bodyChkYn;		// 본문고객정보체크여부
	private String attachFileChkYn;	// 첨부파일고객정보체크여부
	private String secuMailChkYn;	// 보안메일고객정보체크여부		
	private String mktYn;			// 마케팅 동의
	private String mktNm;			// 마케팅 내용
	private int prohibitCnt;		// 금칙어 갯수	
	// 추가정보
	private String webAgentUrl;			// 웹에이전트URL
	private String webAgentAttachYn;	// 웹에이전트첨부여부
	private String secuAttTyp;			// 웹에이전트첨부유형
	private String serviceContent;		// 메일내용
	private String attachNm;			// 첨부파일명
	private String attachPath;			// 첨부파일경로
	private String tids;				// 서비스ID멀티선택
	private Date sdate;					// 발송일시
	private long mid;					// 메세지ID
	private int succCnt;				// 성공건수
	private int failCnt;				// 실패건수
	
	private String mailMktGb;			// 마케팅수신유형
	private String mailMktNm;			// 마케팅수신유형명
	//결재정보 추가 
	private String apprUserId;			// 결재자ID
	private String approvalLineYn;		// 발송결재라인해당여부
	private String approvalProcYn;		// 발송결재라인진행여부
	private String approvalProcAppYn;	// 발송결재라인실승인여부
	private String prohibitUserId;		// 준법심의 결재자ID
	
	// 검색
	private String searchStartDt;		// 검색시작일
	private String searchEndDt;			// 검색종료일
	private String searchTnm;			// 검색서비스명
	private int searchDeptNo;			// 검색사용자그룹
	private String searchUserId;		// 검색사용자ID
	private String searchContentsTyp;	// 검색컨텐츠타입
	private String searchStatus;		// 검색상태
	private String uilang;				// 언어권
	private String adminYn;				// 관리자여부
	private int searchCampNo;					// 검색캠페인번호
	private int searchSegNo;					// 검색수신자그룹번호
		
	//금지어 관련 정보 
	private String prohibitContentTyp;	// 준법심의 제목 : 000 , 본문 : 001 
	private int prohibitTitleCnt;		// 준법심의 제목 결과 건수 
	private String prohibitTitleDesc;	// 준법심의 제목 결과
	private int prohibitTextCnt;		// 준법심의 본문 결과 건수 
	private String prohibitTextDesc;	// 준법심의 본문 결과
	private String prohibitDesc;		// 준법심의 확인 결과
	
	//금지어 테스트용 
	private String titleKey;	// 준법심의 제목 키값 
	private String textKey;		// 준법심의 본문 키값 
	
	//API 연계 관련 추가항목 
	private String campId;			// 캠페인아이디
	private String cellNodeId;		// 고객군아디이
	private String contId;			// 컨텐츠아이디
	private String emailTmplId;		// 템플릿아이디
	private String mappingValues;	// 사용된치환변수
	//복사시 API 템플릿 번호 수기 입력 
	private int copyTid;			// 복사 생성되는 Tid 번호(타입 : 숫자)
	
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getTnm() {
		return tnm;
	}
	public void setTnm(String tnm) {
		this.tnm = tnm;
	}
	public String getTdesc() {
		return tdesc;
	}
	public void setTdesc(String tdesc) {
		this.tdesc = tdesc;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSmail() {
		return smail;
	}
	public void setSmail(String smail) {
		this.smail = smail;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getContentsPath() {
		return contentsPath;
	}
	public void setContentsPath(String contentsPath) {
		this.contentsPath = contentsPath;
	}
	public String getContentsTyp() {
		return contentsTyp;
	}
	public void setContentsTyp(String contentsTyp) {
		this.contentsTyp = contentsTyp;
	}
	public String getContentsTypNm() {
		return contentsTypNm;
	}
	public void setContentsTypNm(String contentsTypNm) {
		this.contentsTypNm = contentsTypNm;
	}
	public int getAttchCnt() {
		return attchCnt;
	}
	public void setAttchCnt(int attchCnt) {
		this.attchCnt = attchCnt;
	}
	public String getRecvChkYn() {
		return recvChkYn;
	}
	public void setRecvChkYn(String recvChkYn) {
		this.recvChkYn = recvChkYn;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public int getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusNm() {
		return statusNm;
	}
	public void setStatusNm(String statusNm) {
		this.statusNm = statusNm;
	}
	public String getEaiCampNo() {
		return eaiCampNo;
	}
	public void setEaiCampNo(String eaiCampNo) {
		this.eaiCampNo = eaiCampNo;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getWorkStatusNm() {
		return workStatusNm;
	}
	public void setWorkStatusNm(String workStatusNm) {
		this.workStatusNm = workStatusNm;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegNm() {
		return regNm;
	}
	public void setRegNm(String regNm) {
		this.regNm = regNm;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getUpId() {
		return upId;
	}
	public void setUpId(String upId) {
		this.upId = upId;
	}
	public String getUpNm() {
		return upNm;
	}
	public void setUpNm(String upNm) {
		this.upNm = upNm;
	}
	public String getUpDt() {
		return upDt;
	}
	public void setUpDt(String upDt) {
		this.upDt = upDt;
	}
	public int getCampNo() {
		return campNo;
	}
	public void setCampNo(int campNo) {
		this.campNo = campNo;
	}
	public int getSegNo() {
		return segNo;
	}
	public void setSegNo(int segNo) {
		this.segNo = segNo;
	}
	public String getSegNoc() {
		return segNoc;
	}
	public void setSegNoc(String segNoc) {
		this.segNoc = segNoc;
	}
	public String getSegNm() {
		return segNm;
	}
	public void setSegNm(String segNm) {
		this.segNm = segNm;
	}
	public String getMergeKey() {
		return mergeKey;
	}
	public void setMergeKey(String mergeKey) {
		this.mergeKey = mergeKey;
	}
	public String getMergeCol() {
		return mergeCol;
	}
	public void setMergeCol(String mergeCol) {
		this.mergeCol = mergeCol;
	}
	public String getCampNm() {
		return campNm;
	}
	public void setCampNm(String campNm) {
		this.campNm = campNm;
	}
	public String getOrgKorNm() {
		return orgKorNm;
	}
	public void setOrgKorNm(String orgKorNm) {
		this.orgKorNm = orgKorNm;
	}
	public String getImgChkYn() {
		return imgChkYn;
	}
	public void setImgChkYn(String imgChkYn) {
		this.imgChkYn = imgChkYn;
	}
	public String getProhibitChkTyp() {
		return prohibitChkTyp;
	}
	public void setProhibitChkTyp(String prohibitChkTyp) {
		this.prohibitChkTyp = prohibitChkTyp;
	}
	public String getTitleChkYn() {
		return titleChkYn;
	}
	public void setTitleChkYn(String titleChkYn) {
		this.titleChkYn = titleChkYn;
	}
	public String getBodyChkYn() {
		return bodyChkYn;
	}
	public void setBodyChkYn(String bodyChkYn) {
		this.bodyChkYn = bodyChkYn;
	}
	public String getAttachFileChkYn() {
		return attachFileChkYn;
	}
	public void setAttachFileChkYn(String attachFileChkYn) {
		this.attachFileChkYn = attachFileChkYn;
	}
	public String getSecuMailChkYn() {
		return secuMailChkYn;
	}
	public void setSecuMailChkYn(String secuMailChkYn) {
		this.secuMailChkYn = secuMailChkYn;
	}
	public String getMktYn() {
		return mktYn;
	}
	public void setMktYn(String mktYn) {
		this.mktYn = mktYn;
	}
	public String getMktNm() {
		return mktNm;
	}
	public void setMktNm(String mktNm) {
		this.mktNm = mktNm;
	}
	public int getProhibitCnt() {
		return prohibitCnt;
	}
	public void setProhibitCnt(int prohibitCnt) {
		this.prohibitCnt = prohibitCnt;
	}
	public String getWebAgentUrl() {
		return webAgentUrl;
	}
	public void setWebAgentUrl(String webAgentUrl) {
		this.webAgentUrl = webAgentUrl;
	}
	public String getWebAgentAttachYn() {
		return webAgentAttachYn;
	}
	public void setWebAgentAttachYn(String webAgentAttachYn) {
		this.webAgentAttachYn = webAgentAttachYn;
	}
	public String getSecuAttTyp() {
		return secuAttTyp;
	}
	public void setSecuAttTyp(String secuAttTyp) {
		this.secuAttTyp = secuAttTyp;
	}
	public String getServiceContent() {
		return serviceContent;
	}
	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}
	public String getAttachNm() {
		return attachNm;
	}
	public void setAttachNm(String attachNm) {
		this.attachNm = attachNm;
	}
	public String getAttachPath() {
		return attachPath;
	}
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	public String getTids() {
		return tids;
	}
	public void setTids(String tids) {
		this.tids = tids;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public int getSuccCnt() {
		return succCnt;
	}
	public void setSuccCnt(int succCnt) {
		this.succCnt = succCnt;
	}
	public int getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
	}
	public String getMailMktGb() {
		return mailMktGb;
	}
	public void setMailMktGb(String mailMktGb) {
		this.mailMktGb = mailMktGb;
	}
	public String getMailMktNm() {
		return mailMktNm;
	}
	public void setMailMktNm(String mailMktNm) {
		this.mailMktNm = mailMktNm;
	}
	public String getApprUserId() {
		return apprUserId;
	}
	public void setApprUserId(String apprUserId) {
		this.apprUserId = apprUserId;
	}
	public String getApprovalLineYn() {
		return approvalLineYn;
	}
	public void setApprovalLineYn(String approvalLineYn) {
		this.approvalLineYn = approvalLineYn;
	}
	public String getApprovalProcYn() {
		return approvalProcYn;
	}
	public void setApprovalProcYn(String approvalProcYn) {
		this.approvalProcYn = approvalProcYn;
	}
	public String getApprovalProcAppYn() {
		return approvalProcAppYn;
	}
	public void setApprovalProcAppYn(String approvalProcAppYn) {
		this.approvalProcAppYn = approvalProcAppYn;
	}
	public String getProhibitUserId() {
		return prohibitUserId;
	}
	public void setProhibitUserId(String prohibitUserId) {
		this.prohibitUserId = prohibitUserId;
	}
	public String getSearchStartDt() {
		return searchStartDt;
	}
	public void setSearchStartDt(String searchStartDt) {
		this.searchStartDt = searchStartDt;
	}
	public String getSearchEndDt() {
		return searchEndDt;
	}
	public void setSearchEndDt(String searchEndDt) {
		this.searchEndDt = searchEndDt;
	}
	public String getSearchTnm() {
		return searchTnm;
	}
	public void setSearchTnm(String searchTnm) {
		this.searchTnm = searchTnm;
	}
	public int getSearchDeptNo() {
		return searchDeptNo;
	}
	public void setSearchDeptNo(int searchDeptNo) {
		this.searchDeptNo = searchDeptNo;
	}
	public String getSearchUserId() {
		return searchUserId;
	}
	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}
	public String getSearchContentsTyp() {
		return searchContentsTyp;
	}
	public void setSearchContentsTyp(String searchContentsTyp) {
		this.searchContentsTyp = searchContentsTyp;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public String getUilang() {
		return uilang;
	}
	public void setUilang(String uilang) {
		this.uilang = uilang;
	}
	public String getAdminYn() {
		return adminYn;
	}
	public void setAdminYn(String adminYn) {
		this.adminYn = adminYn;
	}
	public int getSearchCampNo() {
		return searchCampNo;
	}
	public void setSearchCampNo(int searchCampNo) {
		this.searchCampNo = searchCampNo;
	}
	public int getSearchSegNo() {
		return searchSegNo;
	}
	public void setSearchSegNo(int searchSegNo) {
		this.searchSegNo = searchSegNo;
	}
	public String getProhibitContentTyp() {
		return prohibitContentTyp;
	}
	public void setProhibitContentTyp(String prohibitContentTyp) {
		this.prohibitContentTyp = prohibitContentTyp;
	}
	public int getProhibitTitleCnt() {
		return prohibitTitleCnt;
	}
	public void setProhibitTitleCnt(int prohibitTitleCnt) {
		this.prohibitTitleCnt = prohibitTitleCnt;
	}
	public String getProhibitTitleDesc() {
		return prohibitTitleDesc;
	}
	public void setProhibitTitleDesc(String prohibitTitleDesc) {
		this.prohibitTitleDesc = prohibitTitleDesc;
	}
	public int getProhibitTextCnt() {
		return prohibitTextCnt;
	}
	public void setProhibitTextCnt(int prohibitTextCnt) {
		this.prohibitTextCnt = prohibitTextCnt;
	}
	public String getProhibitTextDesc() {
		return prohibitTextDesc;
	}
	public void setProhibitTextDesc(String prohibitTextDesc) {
		this.prohibitTextDesc = prohibitTextDesc;
	}
	public String getProhibitDesc() {
		return prohibitDesc;
	}
	public void setProhibitDesc(String prohibitDesc) {
		this.prohibitDesc = prohibitDesc;
	}
	public String getTitleKey() {
		return titleKey;
	}
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}
	public String getTextKey() {
		return textKey;
	}
	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}
	/**
	 * @return the campId
	 */
	public String getCampId() {
		return campId;
	}
	/**
	 * @return the cellNodeId
	 */
	public String getCellNodeId() {
		return cellNodeId;
	}
	/**
	 * @return the contId
	 */
	public String getContId() {
		return contId;
	}
	/**
	 * @return the emailTmplId
	 */
	public String getEmailTmplId() {
		return emailTmplId;
	}
	/**
	 * @return the mappingValues
	 */
	public String getMappingValues() {
		return mappingValues;
	}
	/**
	 * @param campId the campId to set
	 */
	public void setCampId(String campId) {
		this.campId = campId;
	}
	/**
	 * @param cellNodeId the cellNodeId to set
	 */
	public void setCellNodeId(String cellNodeId) {
		this.cellNodeId = cellNodeId;
	}
	/**
	 * @param contId the contId to set
	 */
	public void setContId(String contId) {
		this.contId = contId;
	}
	/**
	 * @param emailTmplId the emailTmplId to set
	 */
	public void setEmailTmplId(String emailTmplId) {
		this.emailTmplId = emailTmplId;
	}
	/**
	 * @param mappingValues the mappingValues to set
	 */
	public void setMappingValues(String mappingValues) {
		this.mappingValues = mappingValues;
	}
	public int getCopyTid() {
		return copyTid;
	}
	public void setCopyTid(int copyTid) {
		this.copyTid = copyTid;
	} 
}
