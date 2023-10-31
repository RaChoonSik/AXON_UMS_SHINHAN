package kr.co.enders.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTest {
	@SuppressWarnings("null")
	public static void main(String[] args) {
		/*
		String ems =EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", "AOE7zpjB9kx/rhakVNUYqlhrT5Z1Hbuy1M0sSPsFtGmuGNPAcMkpzsqRH372EOYG");	
		String rns =EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", "WIfNEUs6eMIcq4coe1WwjayGY+rNjtkJbJbuUdHiIsGwi/EHVc7D/Pp3Hdk9o/hq");
		String sms =EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", "CqygLkqZ0XZ92cRWN62ZxSZHV2+x01OshqNHXLsJIjaU40w4TwvPksgK8LSJX6TG");
		String push =EncryptUtil.getJasyptDecryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", "E0ep/chY5VYE59tPp90U/JjnbQQGOMzXLzWBEr6E8CoCJjUtll6j+bvUiyHPXFKs");
		
		System.out.println(ems);
		System.out.println(rns);
		System.out.println(sms);
		System.out.println(push);
		
		System.out.println(EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", ems));
		System.out.println(EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", rns));
		System.out.println(EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", sms));
		System.out.println(EncryptUtil.getJasyptEncryptedString("PBEWithMD5AndDES", "andi@enders.co.kr", push));
		
		*/
		
 
		//String s1 =EncryptUtil.getJasyptEncryptedFixString("PBEWithMD5AndDES", "NOT_RNNO", "hun1110@enders.co.kr");
		
		/*
		String s1 = "SELECT ID , NAME , EMAIL_ENC AS EMAIL , ENCKEY , BIZKEY, NAME AS 이름 ,  EMAIL_ENC AS  이메일, EMAIL_ENC  FROM TMP_CUST_INFO_10000 WHERE  BIZKEY = $:BIZKEY:$";
		System.out.println(s1);
		
		Pattern pattern = Pattern.compile("[$](.*?)[$]"); 

		Matcher matcher = pattern.matcher(s1);
		String s2 = matcher.replaceAll("?");
		System.out.println(s2);
		*/
		/*
		String s2 =EncryptUtil.getJasyptEncryptedFixString("PBEWithMD5AndDES", "NOT_RNNO", "AXon@enders.co.kr");
		String s3 =EncryptUtil.getJasyptEncryptedFixString("PBEWithMD5AndDES", "NOT_RNNO", "test00055@stoneman.club");
		String s4 =EncryptUtil.getJasyptEncryptedFixString("PBEWithMD5AndDES", "NOT_RNNO", "test00056@stoneman.club");
		String s5 =EncryptUtil.getJasyptEncryptedFixString("PBEWithMD5AndDES", "NOT_RNNO", "test00057@stoneman.club");
		String s6 =EncryptUtil.getJasyptEncryptedFixString("PBEWithMD5AndDES", "NOT_RNNO", "test00058@stoneman.club");
		String s7 =EncryptUtil.getJasyptEncryptedFixString("PBEWithMD5AndDES", "NOT_RNNO", "test00059@stoneman.club");
		
		System.out.println(s1);
		System.out.println(s2);
		System.out.println(s3);
		System.out.println(s4);
		System.out.println(s5);
		System.out.println(s6);
		System.out.println(s7);
		 */
		
		/*
		String s7 =EncryptUtil.getJasyptDecryptedFixString("PBEWithMD5AndDES", "NOT_RNNO", "McyHUskoA3o91wU7U0L0n6/Vcl0OWJo0NLz7zTnEQQQw6UX9aLHiHHb0j5LZYfU79BLdtzR2ADdPYgG1pZL3VA==");
		System.out.println(s7);
		*/
		/*
		 * String dbUrl = "jdbc:mysql://127.0.0.1:3306/ums?characterEncoding=utf8";
		 * 
		 * String schema = ""; if(dbUrl.indexOf("?") > 0) { dbUrl =
		 * dbUrl.substring(0,dbUrl.indexOf("?")); } schema =
		 * dbUrl.split("/")[dbUrl.split("/").length-1];
		 * 
		 * System.out.println(schema);
		 */
		
		/*
		String gsrEnc =EncryptUtil.getGrsEncryptedString( "grs",  "10.66.2.124",  9003, "dbsec", "M_KEY", "AES256", "94junhe@naver.com" );
		System.out.println(gsrEnc);
		
		
		String gsrDec =EncryptUtil.getGrsDecryptedString( "grs",  "10.66.2.124",  9003, "dbsec", "M_KEY", "AES256", gsrEnc );
		System.out.println(gsrDec);
		*/
		String content = "$:MAP1:$의 $:MAP2:$ 캠페인 결재 기안 내 미승인으로 인한 반려 알림";
		String title ="[$:MAP1:$] 개인정보 이용내역 안내	" ; 
		List<String> arrMmergeCol = new ArrayList<String>();
		String mergeCols = "";
		Pattern patternD = Pattern.compile("[$](.*?)[$]");
		Pattern patternC = Pattern.compile("[:](.*?)[:]");

		Matcher matcherD = patternD.matcher(content);
		
		while (matcherD.find()) { 
			String col = matcherD.group(1) ;
			Matcher matcherC = patternC.matcher(col);
			while (matcherC.find()) {
				arrMmergeCol.add(matcherC.group(1));
				if(matcherC.group(1) ==  null) {
					break;
				}
			}  
			if(matcherD.group(1) ==  null) {
				break;
			}
		}
		
		matcherD = patternD.matcher(title);
		 
		while (matcherD.find()) { 
			String col = matcherD.group(1) ;
			Matcher matcherC = patternC.matcher(col);
			while (matcherC.find()) {
				arrMmergeCol.add(matcherC.group(1));
				if(matcherC.group(1) ==  null) {
					break;
				}
			}  
			if(matcherD.group(1) ==  null) {
				break;
			}
		} 
		Set<String> set = new HashSet<String>(arrMmergeCol);
		List<String> newList =new ArrayList<String>(set);
		
		
		if (newList != null && newList.size() > 0 ) {
			for (int j= 0 ; j < newList.size() ; j ++ ) {
				mergeCols +=   newList.get(j) +",";
			}
		}
	
		
		System.out.println(mergeCols);
	}
}
