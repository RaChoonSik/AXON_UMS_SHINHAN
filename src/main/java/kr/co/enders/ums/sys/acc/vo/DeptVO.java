/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 부서 정보 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.acc.vo 
 */
package kr.co.enders.ums.sys.acc.vo;

import kr.co.enders.ums.com.vo.CommonVO;

public class DeptVO extends CommonVO {
	private int deptNo;			// 그룹코드,그룹번호
	private String deptNm;		// 그룹명
	private String deptDesc;	// 그룹설명
	private String status;		// 상태코드
	private String statusNm;	// 상태명
	private String useYn;		// 사용여부
	private String upId;		// 수정자	
	private String upDt;		// 수정일
	private String regId;		// 등록자
	private String regDt;		// 등록일
	private String regNm;		// 등록자이름
	private String upNm;		// 수정자이름
	private String dataAllYn;	// 대시보드전사데이터
	private String serviceGb;	// 사용가능서비스코드
	private String serviceNm; 	// 사용가능서비스명	 
	// 검색	
	private String searchDeptNm;	// 검색부서명
	private String searchStatus;	// 검색상태코드
	private String uilang;			// 언어권
	private int userDeptNo;			// 사용자그럽번호
	private String deptNos;		//사용자그룹 여러개
	
	//서비스 구분용 
	private String useEMS;
	private String useRNS;
	private String useSMS;
	private String usePUSH;
	private String useSYS;
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
	public String getDeptDesc() {
		return deptDesc;
	}
	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
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
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getUpId() {
		return upId;
	}
	public void setUpId(String upId) {
		this.upId = upId;
	}
	public String getUpDt() {
		return upDt;
	}
	public void setUpDt(String upDt) {
		this.upDt = upDt;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getRegNm() {
		return regNm;
	}
	public void setRegNm(String regNm) {
		this.regNm = regNm;
	}
	public String getUpNm() {
		return upNm;
	}
	public void setUpNm(String upNm) {
		this.upNm = upNm;
	}
	public String getDataAllYn() {
		return dataAllYn;
	}
	public void setDataAllYn(String dataAllYn) {
		this.dataAllYn = dataAllYn;
	}
	public String getServiceGb() {
		return serviceGb;
	}
	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
	}
	public String getServiceNm() {
		return serviceNm;
	}
	public void setServiceNm(String serviceNm) {
		this.serviceNm = serviceNm;
	}
	public String getSearchDeptNm() {
		return searchDeptNm;
	}
	public void setSearchDeptNm(String searchDeptNm) {
		this.searchDeptNm = searchDeptNm;
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
	public int getUserDeptNo() {
		return userDeptNo;
	}
	public void setUserDeptNo(int userDeptNo) {
		this.userDeptNo = userDeptNo;
	}
	public String getDeptNos() {
		return deptNos;
	}
	public void setDeptNos(String deptNos) {
		this.deptNos = deptNos;
	}
	public String getUseEMS() {
		return useEMS;
	}
	public void setUseEMS(String useEMS) {
		this.useEMS = useEMS;
	}
	public String getUseRNS() {
		return useRNS;
	}
	public void setUseRNS(String useRNS) {
		this.useRNS = useRNS;
	}
	public String getUseSMS() {
		return useSMS;
	}
	public void setUseSMS(String useSMS) {
		this.useSMS = useSMS;
	}
	public String getUsePUSH() {
		return usePUSH;
	}
	public void setUsePUSH(String usePUSH) {
		this.usePUSH = usePUSH;
	}
	public String getUseSYS() {
		return useSYS;
	}
	public void setUseSYS(String useSYS) {
		this.useSYS = useSYS;
	}
}
