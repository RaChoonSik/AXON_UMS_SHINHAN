/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.12
 * 설명 : 에러 화면 Controller
 */
package kr.co.enders.ums.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sso")
public class SsoController {
 
	/**
	 * 사용자 접근 권한 오류
	 * @return
	 */
	@RequestMapping(value="/default")
	public String getdefault() {
		return "sso/default";
	}
	
	@RequestMapping(value="/defaultSpa")
	public String defaultSpa() {
		return "sso/defaultSpa";
	}
	
	@RequestMapping(value="/logon")
	public String logon() {
		return "sso/logon";
	}
	
	@RequestMapping(value="/logonEnc")
	public String logonEnc() {
		return "sso/logonEnc";
	}
	
	
	@RequestMapping(value="/logonEncServerSide")
	public String logonEncServerSide() {
		return "sso/logonEncServerSide";
	}
	
	@RequestMapping(value="/logonError")
	public String logonError() {
		return "sso/logonError";
	}
	
	@RequestMapping(value="/logonTfa")
	public String logonTfa() {
		return "sso/logonTfa";
	}
	
	@RequestMapping(value="/logonWa")
	public String logonWa() {
		return "sso/logonWa";
	}
	
	@RequestMapping(value="/web2app")
	public String web2app() {
		return "sso/web2app";
	}
	
	@RequestMapping(value="/ssoagent/dupChoice")
	public String dupChoice2() {
		return "sso/ssoagent/dupChoice";
	}
	@RequestMapping(value="/ssoagent/dupChoiceCancel")
	public String dupChoiceCancel2() {
		return "sso/ssoagent/dupChoiceCancel";
	}
	@RequestMapping(value="/ssoagent/dupChoiceLogon")
	public String dupChoiceLogon2() {
		return "sso/ssoagent/dupChoiceLogon";
	}	
	@RequestMapping(value="/ssoagent/generateKey")
	public String generateKey2() {
		return "sso/ssoagent/generateKey";
	}
	@RequestMapping(value="/ssoagent/reloadPolicy")
	public String reloadPolicy2() {
		return "sso/ssoagent/reloadPolicy";
	}
	@RequestMapping(value="/ssoagent/requestArtifact")
	public String requestArtifact2() {
		return "sso/ssoagent/requestArtifact";
	}
	@RequestMapping(value="/ssoagent/tfaInput")
	public String tfaInput2() {
		return "sso/ssoagent/tfaInput";
	}
	@RequestMapping(value="/ssoagent/viewConfig")
	public String viewConfig2() {
		return "sso/ssoagent/viewConfig";
	}
	@RequestMapping(value="/ssoagent/viewHeaders")
	public String viewHeaders2() {
		return "sso/ssoagent/viewHeaders";
	}		
}
