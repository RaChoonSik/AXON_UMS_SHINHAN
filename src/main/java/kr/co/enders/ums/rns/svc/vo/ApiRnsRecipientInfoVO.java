/**
 * 작성자 : 김준희
 * 작성일시 : 2023.11.15
 * 설명 : RNS 발송 그룹 저장 VO
 */
package kr.co.enders.ums.rns.svc.vo;

import kr.co.enders.ums.com.vo.CommonVO;

public class ApiRnsRecipientInfoVO extends CommonVO {
	private long mid;				// 메시지아이디
	private int subid;				// 재발송아이디
	private int tid;				// 서비스ID
	private String spos;			// 발송자소속코드
	private String sid;				// 발송자아이디
	private String sname;			// 발송자명
	private String smail;			// 발송자이메일
	private String rpos;			// 수신자그룹위치
	private String ctnpos;			// 컨텐츠위치
	private String subject;			// 메일제목
	private String contents;		// 컨템츠템플릿
	private String requestKey;		// 요청키 
	private String rid;				// 수신자 아이디
	private String rname; 			// 수신자 명
	private String rmail; 			// 수신자 이메일 주소
	 
	private String map1; 			// 자동발송머지항목01
	private String map2; 			// 자동발송머지항목02
	private String map3; 			// 자동발송머지항목03
	private String map4; 			// 자동발송머지항목04
	private String map5; 			// 자동발송머지항목05
	private String map6; 			// 자동발송머지항목06 
	private String map7; 			// 자동발송머지항목07
	private String map8; 			// 자동발송머지항목08
	private String map9; 			// 자동발송머지항목09
	private String map10; 			// 자동발송머지항목10
	private String map11; 			// 자동발송머지항목11
	private String map12; 			// 자동발송머지항목12
	private String map13; 			// 자동발송머지항목13
	private String map14; 			// 자동발송머지항목14
	private String map15; 			// 자동발송머지항목15
	private String attachfile01;	// 첨부파일1
	
	//미사용 
	private String query;	 
	private String status;
	private String dbcode; 
	private int refmid;
	private String charset; 
	private String attachfile02;
	private String attachfile03;
	private String attachfile04;
	private String attachfile05;
	private String enckey;
	private String bizkey;
	
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public int getSubid() {
		return subid;
	}
	public void setSubid(int subid) {
		this.subid = subid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getSpos() {
		return spos;
	}
	public void setSpos(String spos) {
		this.spos = spos;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
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
	public String getRpos() {
		return rpos;
	}
	public void setRpos(String rpos) {
		this.rpos = rpos;
	}
	public String getCtnpos() {
		return ctnpos;
	}
	public void setCtnpos(String ctnpos) {
		this.ctnpos = ctnpos;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getRequestKey() {
		return requestKey;
	}
	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getRmail() {
		return rmail;
	}
	public void setRmail(String rmail) {
		this.rmail = rmail;
	}
	public String getMap1() {
		return map1;
	}
	public void setMap1(String map1) {
		this.map1 = map1;
	}
	public String getMap2() {
		return map2;
	}
	public void setMap2(String map2) {
		this.map2 = map2;
	}
	public String getMap3() {
		return map3;
	}
	public void setMap3(String map3) {
		this.map3 = map3;
	}
	public String getMap4() {
		return map4;
	}
	public void setMap4(String map4) {
		this.map4 = map4;
	}
	public String getMap5() {
		return map5;
	}
	public void setMap5(String map5) {
		this.map5 = map5;
	}
	public String getMap6() {
		return map6;
	}
	public void setMap6(String map6) {
		this.map6 = map6;
	}
	public String getMap7() {
		return map7;
	}
	public void setMap7(String map7) {
		this.map7 = map7;
	}
	public String getMap8() {
		return map8;
	}
	public void setMap8(String map8) {
		this.map8 = map8;
	}
	public String getMap9() {
		return map9;
	}
	public void setMap9(String map9) {
		this.map9 = map9;
	}
	public String getMap10() {
		return map10;
	}
	public void setMap10(String map10) {
		this.map10 = map10;
	}
	public String getMap11() {
		return map11;
	}
	public void setMap11(String map11) {
		this.map11 = map11;
	}
	public String getMap12() {
		return map12;
	}
	public void setMap12(String map12) {
		this.map12 = map12;
	}
	public String getMap13() {
		return map13;
	}
	public void setMap13(String map13) {
		this.map13 = map13;
	}
	public String getMap14() {
		return map14;
	}
	public void setMap14(String map14) {
		this.map14 = map14;
	}
	public String getMap15() {
		return map15;
	}
	public void setMap15(String map15) {
		this.map15 = map15;
	}
	public String getAttachfile01() {
		return attachfile01;
	}
	public void setAttachfile01(String attachfile01) {
		this.attachfile01 = attachfile01;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDbcode() {
		return dbcode;
	}
	public void setDbcode(String dbcode) {
		this.dbcode = dbcode;
	}
	public int getRefmid() {
		return refmid;
	}
	public void setRefmid(int refmid) {
		this.refmid = refmid;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getAttachfile02() {
		return attachfile02;
	}
	public void setAttachfile02(String attachfile02) {
		this.attachfile02 = attachfile02;
	}
	public String getAttachfile03() {
		return attachfile03;
	}
	public void setAttachfile03(String attachfile03) {
		this.attachfile03 = attachfile03;
	}
	public String getAttachfile04() {
		return attachfile04;
	}
	public void setAttachfile04(String attachfile04) {
		this.attachfile04 = attachfile04;
	}
	public String getAttachfile05() {
		return attachfile05;
	}
	public void setAttachfile05(String attachfile05) {
		this.attachfile05 = attachfile05;
	}
	public String getEnckey() {
		return enckey;
	}
	public void setEnckey(String enckey) {
		this.enckey = enckey;
	}
	public String getBizkey() {
		return bizkey;
	}
	public void setBizkey(String bizkey) {
		this.bizkey = bizkey;
	}
	
	
	
}
