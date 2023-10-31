/**
 * 수정자 : 김준희
 * 수정일시 : 2022.11.09
 * 설명 : 사용자 로그인 Controller 
 */
package kr.co.enders.ums.lgn.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nets.sso.agent.authcheck.AuthCheck;
import com.nets.sso.common.AgentException;
import com.nets.sso.common.AgentExceptionCode;
import com.nets.sso.common.enums.AuthStatus;

import kr.co.enders.ums.com.service.CryptoService;
import kr.co.enders.ums.lgn.service.LoginService;
import kr.co.enders.ums.lgn.vo.LoginVO;
import kr.co.enders.ums.main.service.MainService;
import kr.co.enders.ums.sys.acc.service.AccountService;
import kr.co.enders.ums.sys.acc.vo.ServiceVO;
import kr.co.enders.ums.sys.acc.vo.SysMenuVO;
import kr.co.enders.ums.sys.acc.vo.UserVO;
import kr.co.enders.ums.sys.cod.controller.UserCodeController;
import kr.co.enders.ums.sys.log.service.SystemLogService;
import kr.co.enders.ums.sys.log.vo.ActionLogVO;
import kr.co.enders.util.Code;
import kr.co.enders.util.EncryptAccUtil;
import kr.co.enders.util.EncryptUtil;
import kr.co.enders.util.PropertiesUtil;
import kr.co.enders.util.StringUtil;
import kr.co.enders.util.SessionManager;

@Controller
@RequestMapping("/lgn")
public class LoginController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private MainService mainService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private AccountService accountService;
 
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private SystemLogService systemService;
	
	@Autowired
	private PropertiesUtil properties;	
	/**
	 * 로그인 화면을 출력한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/lgnP")
	public String goLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("## goLogin Form Start");
		 
		String returnUrl = getLoginUrl();
		return returnUrl;
	}
	
	/**
	 * 로그인 화면을 출력한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/lgnSession")
	public String goLoginSession(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("## goLoginSession");
		
		return "lgn/lgnSession";
	}
	
	/**
	 * 사용자 로그인을 처리한다.
	 * @param loginVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/lgn")
	public String loginProcess(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## login process Start");
		 
		// 아이디, 비밀번호 확인
		logger.debug("loginProcess pUserId = " + loginVO.getpUserId());
		logger.debug("loginProcess pUserPwd = " + loginVO.getpUserPwd());
		
		String cipherUserPwd = loginVO.getpUserPwd();
		String cipherUserId = loginVO.getpUserId();
		try {
			loginVO.setpUserId(EncryptAccUtil.getEncryptedSHA256(cipherUserId, properties.getProperty("ACCOUNT.KEYSTRING")));
			loginVO.setpUserPwd(EncryptAccUtil.getEncryptedSHA256(cipherUserPwd, properties.getProperty("ACCOUNT.KEYSTRING")));
		} catch(Exception e) { 
			logger.error("loginProcess EncryptAccUtil Error = " + e);
		}
		
		//String plainUserPwd = loginVO.getpUserPwd();
		String encPasswd = EncryptUtil.getEncryptedSHA256(loginVO.getpUserPwd());
		loginVO.setpUserPwd(encPasswd);
		UserVO userVO = null;
		String userStatus = "999";
		
		// 사용자 아이디, 비밀번호 체크
		try {
			userVO = loginService.isValidUser(loginVO);
		} catch(Exception e) {
			logger.error("loginService.isValidUser Error = " + e);
		}
			
		if(userVO != null ) {
			if(userVO.getStatus() != null && !"".equals(userVO.getStatus())) {
				userStatus = userVO.getStatus();
			}
		}
		
		//String retUrl = "lgn/lgn" ;
		String retUrl = getLoginUrl();
		String clientIp = StringUtil.getClientIP(request);
		if("000".equals(userStatus)) {
			
			//로그인 일자 update
			String lstAccessDt = StringUtil.getFDate(userVO.getLstaccessDt(), Code.DT_FMT1);
			String lstAccessIp = "";
			
			if(userVO.getLstaccessIp() == null || userVO.getLstaccessIp().isEmpty()) {
				lstAccessIp = "IP정보없음";
			} else {
				lstAccessIp = userVO.getLstaccessIp();
			}
			
			try {
				userVO.setLstaccessDt(StringUtil.getDate(Code.TM_YMDHMS));
				userVO.setLstaccessIp(clientIp);
				loginService.updateLoginDt(userVO);
			} catch (Exception e1) {
				logger.error("loginService.updateLoginDt" + e1);
			}
			
			// 세션값 설정	
			session.setAttribute("NEO_SSO_LOGIN", "N");						// SSO 세션으로 넘어온 사용자 아님
			session.setAttribute("NEO_USER_ID", userVO.getUserId());		// 사용자ID
			session.setAttribute("NEO_USER_NM", userVO.getUserNm());		// 사용자명
			session.setAttribute("NEO_DEPT_NO", userVO.getDeptNo());		// 부서번호
			session.setAttribute("NEO_DEPT_NM", userVO.getDeptNm());		// 부서명
			session.setAttribute("NEO_TZ_CD", userVO.getTzCd());			// 타임존코드
			session.setAttribute("NEO_TZ_TERM", userVO.getTzTerm());		// 타임존시간차
			session.setAttribute("NEO_UILANG", userVO.getUilang());			// 언어권
			session.setAttribute("NEO_CHARSET", userVO.getCharset());		// 문자셋
			session.setAttribute("NEO_FONT", userVO.getFont());				// 폰트
			session.setAttribute("NEO_ORG_CD", userVO.getOrgCd());			// 조직코드
			session.setAttribute("NEO_ORG_NM", userVO.getOrgKorNm());		// 조직명
			session.setAttribute("NEO_JOB_GB", userVO.getJobGb());			// 직책코드
			session.setAttribute("NEO_JOB_NM", userVO.getJobNm());			// 직책명
			session.setAttribute("NEO_LINK", userVO.getLinkService());		// 사용자 링크 서비스
			
			session.setAttribute("NEO_MAIL_FROM_NM", userVO.getMailFromNm()); 	// 발송자명 
			session.setAttribute("NEO_MAIL_FROM_EM", userVO.getMailFromEm()); 	// 발송자이메일주소
			session.setAttribute("NEO_USER_TEL", userVO.getUserTel());			// 연락처
			session.setAttribute("NEO_USER_EM", userVO.getUserEm());			// 이메일
			session.setAttribute("NEO_REPLY_TO_EM", userVO.getReplyToEm());		// 회신이메일
			session.setAttribute("NEO_RETURN_EM", userVO.getReturnEm()); 		// return 이메일 
			session.setAttribute("NEO_PER_PAGE", StringUtil.setNullToInt(userVO.getPerPage(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")))); // 사용자별 목록 조회 페이지
			
			session.setAttribute("NEO_USE_EMS", "N");			// EMS 사용여부
			session.setAttribute("NEO_USE_RNS", "N");			// RNS 사용여부
			session.setAttribute("NEO_USE_SMS", "N");			// SMS 사용여부
			session.setAttribute("NEO_USE_PUSH", "N");			// PUSH 사용여부
			
			//정보보안 처리
			session.setAttribute("NEO_USER_IP", lstAccessIp);	// 최근접속IP
			session.setAttribute("NEO_LOGIN_DT",lstAccessDt);	// 최근 접속 시간
			session.setAttribute("NEO_CONNECTIP", clientIp);	// 현재접속 IP
			session.setAttribute("NEO_MULTILOGIN", false);		// 현재접속 IP
			
			int [][] arrUserService = checkLicense(userVO);
			
			int service = 0;
			int useYn = 0;
			for(int i=0; i < arrUserService.length ; i++) {
				service = arrUserService[i][0] ;
				switch(service) {
				case 10:
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_EMS", "Y");			// EMS 사용여부
					}
					break;
				case 20: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_RNS", "Y");			// RMS 사용여부
					}
					break;
				case 30: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_SMS", "Y");			// SMS 사용여부
					}
					break;
				case 40: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_PUSH", "Y");			// PUSH 사용여부
					}
					break;
				default: 
					break;
				}
			}
			
			if(userVO.getUseSYS() != null && !"".equals(userVO.getUseSYS()) && "1".equals(userVO.getUseSYS())) {
				session.setAttribute("NEO_USE_SYS", "Y");			// 공통설정사용권한여부
			} else {
				session.setAttribute("NEO_USE_SYS", "N");			// 공통설정사용권한여부
			}
			 
			// 관리자 여부
			if(userVO.getDeptNo() == 1) {
				session.setAttribute("NEO_ADMIN_YN", "Y");
			} else {
				session.setAttribute("NEO_ADMIN_YN", "N");
			}
			
			// 사용자 프로그램 사용권한 조회(데이터 등록 환경에 따라 쿼리 변동 가능성 있음)
			// 사용자 메뉴
			List<SysMenuVO> menuList = null;
			try {
				menuList = loginService.getUserMenuList(userVO);
			} catch(Exception e) {
				logger.error("loginService.getUserMenuList Error = " + e);
			}
			// 세션에 사용가능 메뉴 목록 저장
			session.setAttribute("NEO_MENU_LIST", menuList);
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("Login");
			logVO.setContentPath("/lgn/lgn.ums");
			insertLoginActionLog(request, session, logVO); 
			
			retUrl =  "redirect:/ems/index.ums";
		 
			
			//정보보안 처리
			//다중로그인 처리 및 팝업 출력 메시지 설정
			/*
			SessionManager.addUser(session);
			if((boolean)session.getAttribute("NEO_MULTILOGIN") == true){
				session.setAttribute("NEO_MULTILOGIN", false);
				session.setAttribute("NEO_BLOCKED_OTEHR", "Y");
			} else {
				session.setAttribute("NEO_BLOCKED_OTEHR", "N");
			}
			retUrl =  "redirect:/ems/index.ums";
			*/
			
			return retUrl;
			
		} else {
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("Login");
			logVO.setContentPath("/lgn/lgn.ums");
			insertLoginActionLog(request, session, logVO);
			
			if(userVO != null ) {
				model.addAttribute("result","E");
			} else {
				model.addAttribute("result","N");
			}
			retUrl = getLoginUrl();
			return retUrl;
			//return "lgn/lgnP";
		}
	}
	
	/**
	 * 초기화된 비밀번호 사용자 확인 
	 * 
	 * @param userVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/checkInitPwd")
	public ModelAndView checkInitPwd(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("checkInitPwd pUserId = " + loginVO.getpUserId());
		logger.debug("checkInitPwd pUserPwd = " + loginVO.getpUserPwd());
		
		//암호화된 내용 복호화 
		int result = 0;
		String cipherUserPwd = loginVO.getpUserPwd();
		String cipherUserId = loginVO.getpUserId();
		try {
			loginVO.setpUserId(EncryptAccUtil.getEncryptedSHA256(cipherUserId, properties.getProperty("ACCOUNT.KEYSTRING")));
			loginVO.setpUserPwd(EncryptAccUtil.getEncryptedSHA256(cipherUserPwd, properties.getProperty("ACCOUNT.KEYSTRING")));
		} catch(Exception e) {
			result = -7;
			logger.error("checkInitPwd EncryptAccUtil Error = " + e);
		} 
		
		//getEncryptedPBKDF2
		String encPasswd = EncryptUtil.getEncryptedSHA256(loginVO.getpUserPwd());
		loginVO.setpUserPwd(encPasswd);
		UserVO userVO = null; 
		
		// 사용자 아이디, 비밀번호 체크
		try {
			userVO = loginService.isValidUser(loginVO);
		} catch(Exception e) {
			logger.error("loginService.isValidUser Error = " + e);
		} 
		
		//
		HashMap<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
		Date expireDate = null;
		
		String returnResult = "";
		String returnMessage = "";
		if(userVO != null) {
			if ("Y".equals(userVO.getSuperYn())) {
				returnResult = "Success";
			} else {
				if("Y".equals(userVO.getPwInitYn())) {
					try {
						if(userVO.getPwinitlimtDt() != null && !"".equals(userVO.getPwinitlimtDt())) 
						{
							expireDate = mSimpleDateFormat.parse(StringUtil.getFDate(userVO.getPwinitlimtDt(), Code.DT_FMT2));
						} else {
							expireDate = mSimpleDateFormat.parse(StringUtil.getFDate(StringUtil.getDate(Code.TM_YMDHMS), Code.DT_FMT2));
						}
					} catch (ParseException e) {
						logger.debug("loginService.PwinitlimtDt Parsing error");
					}
					
					Date currentDate = new Date();
					int compare = expireDate.compareTo( currentDate );
					if(compare >=  0 ) { 
						returnResult = "PwdInit";
					} else {
						returnResult = "Fail";
						returnMessage = "초기화된 비밀번호 사용 기한이 경과되었습니다 (사용기한 :  " + expireDate  + ")  관리자에게 문의해 주세요";
					}
				} else {
					if(userVO.getUserNm() != null && !"".equals(userVO.getUserNm())){ 
						if(userVO.getCertilockGb() == null || !"Y".equals(userVO.getCertilockGb())) { 
							int loginLimit = StringUtil.setNullToInt(properties.getProperty("LOGIN_LIMIT_DAYS"));
							if(userVO.getLstaccessDt() !=null && !"".equals(userVO.getLstaccessDt())) { 
								int daysDiff = StringUtil.getCalcDateDiff(userVO.getLstaccessDt(), "");
								if(daysDiff == -99999) {
									returnResult = "Fail";
									returnMessage = "로그인 처리에 실패했습니다 잠시 후 다시 이용해주세요";
								} else {
									if(daysDiff > loginLimit ) { 
										returnResult =  "Limit";
										returnMessage =  loginLimit + "(일) 동안 접속 이력이 없어 계정사용이 중지 되었습니다 본인인증이 필요합니다 ";
									} else {
										//암호 만기 확인 
										if(userVO.getPwresetdueDt() !=null && !"".equals(userVO.getPwresetdueDt())) { 
											daysDiff = StringUtil.getCalcDateDiff(userVO.getPwresetdueDt(), "");
											if(daysDiff == -99999) {
												returnResult = "Fail";
												returnMessage = "로그인 처리에 실패했습니다 잠시 후 다시 이용해주세요";
											} else {
												if(daysDiff > 0 ) { 
													returnResult =  "Reset";
													returnMessage =  "암호 사용 기한(" + StringUtil.getFDate(userVO.getPwresetdueDt(), Code.DT_FMT2) + ")이 경과되었습니다 새로운 비밀번호로 변경해주세요";
												} else {
													returnResult = "Success";
												}
											}
										} else {
											returnResult = "Success";
										}
									}
								}
							} else { 
								//암호만기 확인 
								if(userVO.getPwresetdueDt() !=null && !"".equals(userVO.getPwresetdueDt())) { 
									int daysDiff = StringUtil.getCalcDateDiff(userVO.getPwresetdueDt(), "");
									if(daysDiff == -99999) {
										returnResult = "Fail";
										returnMessage = "로그인 처리에 실패했습니다 잠시 후 다시 이용해주세요";
									} else {
										if(daysDiff > 0 ) { 
											returnResult =  "Reset";
											returnMessage =  "암호 사용 기한(" + StringUtil.getFDate(userVO.getPwresetdueDt(), Code.DT_FMT2) + ")이 경과되었습니다 새로운 비밀번호로 변경해주세요";
										} else {
											returnResult = "Success";
										}
									}
								} else {
									returnResult = "Success";
								}
							}
						} else {
							returnResult = "Fail";
							returnMessage = "잠금 처리된 계정입니다 관리자에게 문의 해주세요";
						}
					} else {
						//암호 초기화 여부, 인증잠김 여부, 비밀번호 실패 횟수 
						if("Y".equals(userVO.getPwInitYn())) {
							try {
								expireDate = mSimpleDateFormat.parse(StringUtil.getFDate(userVO.getPwinitlimtDt(), Code.DT_FMT2));
							} catch (ParseException e) {
								logger.debug("loginService.updatePwdErrorCnt error");
							}
							
							Date currentDate = new Date();
							int compare = expireDate.compareTo( currentDate );
							if(compare >=  0 ) { 
								returnResult = "PwdInit";
							} else {
								returnResult = "Fail";
								returnMessage = "초기화된 비밀번호 사용 기간이 만료되었습니다 관리자에게 문의해 주세요";
							}
						} else {
							if(userVO.getCertilockGb() != null && "Y".equals(userVO.getCertilockGb())) { 
								returnResult = "Fail";
								returnMessage = "잠금 처리된 계정입니다 관리자에게 문의 해주세요";
							} else {
								returnResult = "Fail";
								int updPwdErrorCntResult = -1;
								try {
									updPwdErrorCntResult = loginService.updatePwdErrorCnt(userVO.getUserId());
								} catch (Exception e) {
									logger.debug("loginService.updatePwdErrorCnt error ");
								}
													
								if(updPwdErrorCntResult > 0 ) {
									int errCnt = userVO.getPwerrorCnt()+1;
									
									int pwLockCnt = StringUtil.setNullToInt(properties.getProperty("PW_LOCK_CNT"));
									if( userVO.getPwerrorCnt() + 1 == pwLockCnt ) {
										try {
											updPwdErrorCntResult = loginService.updateCertilock(userVO.getUserId());
										} catch (Exception e) {
											logger.debug("loginService.updateCertilock error ");
										}
										if (updPwdErrorCntResult < 1 ) {
											returnMessage = "로그인 처리에 실패하였습니다";
										} else {
											returnMessage = "비밀번호 " + StringUtil.setNullToInt(properties.getProperty("PW_LOCK_CNT")) + "회 오류로 사용이 제한되었습니다 관리자에게 문의해주세요.";
										}
									} else {
										returnMessage = "계정명(ID) 또는 비밀번호를 잘못 입력하셨습니다. 입력하신 정보를 확인하십시오. (" + errCnt + "/" +  StringUtil.setNullToInt(properties.getProperty("PW_LOCK_CNT"))+")";
									}
								} else {
									returnMessage = "로그인 처리에 실패하였습니다";
								}
							}
						}
					} 
				}
			}
		} else {
			returnResult = "Fail";
			returnMessage = "로그인계정명(ID) 또는 비밀번호를 확인 해 주세요 ";
		}
		
		if("Fail".equals(returnResult)) {
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("Login");
			logVO.setContentPath("/lgn/lgn.ums");
			logVO.setMessage(returnMessage);
			insertLoginActionLog(request, session, logVO);
		}
		map.put("result", returnResult);
		map.put("msg", returnMessage);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	/**
	 * 사용자 로그인을 처리한다.
	 * @param loginVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/ssolgn")
	public String ssoLoginProcess(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## ssolgn process Start-------------------->");
		
		String authUserId = "";
		String authMessage = "";
		try {
			logger.debug("## login process AuthCheck");
			AuthCheck auth = new AuthCheck(request, response); 
			AuthStatus status = auth.checkLogon();
			if (status == AuthStatus.SSOSuccess) {
				logger.debug("loginService.ssoLoginProcess AuthStatus SSOSuccess."); 
				authUserId = auth.getUserID(); 
			} else if (status == AuthStatus.SSOFirstAccess) {
				logger.debug("loginService.ssoLoginProcess AuthStatus SSOFirstAccess. Try SSO"); 
				authMessage = "SSO: SSOFirstAccess"; 
				auth.trySSO();
			} else if (status == AuthStatus.SSOFail) {
				logger.error("loginService.ssoLoginProcess AuthStatus SSOFail."); 
				if (auth.getErrCode() == AgentExceptionCode.NoException.getValue()) {
					logger.error("loginService.ssoLoginProcess AuthStatus SSOFail :  AgentExceptionCode.NoException." ); 
					authMessage = "SSO: SSOFail : NoException";  
				} else if (auth.getErrCode() == AgentExceptionCode.NoExistUserIDSessionValue.getValue()) { 
					logger.error("loginService.ssoLoginProcess AuthStatus SSOFail :  AgentExceptionCode.NoExistUserIDSessionValue." ); 
					authMessage = "SSO: SSOFail : NoExistUserIDSessionValue";  
				} else if (auth.getErrCode() == AgentExceptionCode.TokenIdleTimeout.getValue()) { 
					logger.error("loginService.ssoLoginProcess AuthStatus SSOFail :  AgentExceptionCode.TokenIdleTimeout." ); 
					authMessage = "SSO: SSOFail : TokenIdleTimeout";  
				}
			} else if (status == AuthStatus.SSOUnAvailable) {
				logger.error("loginService.ssoLoginProcess AuthStatus SSOUnAvailable." ); 
				authMessage = "SSO: SSOUnAvailable";   
			}
		} catch (AgentException e1) {
			logger.error("loginService.isSSOUser AuthCheck AgentException = " + e1.getMessage());
			authMessage = "SSO: AgentException";
		} catch (Exception e1) {
			logger.error("loginService.isSSOUser AuthCheck Exception = " + e1.getMessage());
			authMessage = "SSO: Exception";
		} finally {
			if(authUserId != null && !"".equals(authUserId)) {
				loginVO.setpUserId(authUserId);
			}
			
			if(loginVO.getpUserId() == null || "".equals(loginVO.getpUserId())) {
				ActionLogVO logVO = new ActionLogVO();
				logVO.setStatusGb("Failure");
				logVO.setContent("SSOLogin");
				logVO.setContentPath("/lgn/ssolgn.ums");
				logVO.setMessage(authMessage);
				insertLoginActionLog(request, session, logVO); 
				return getLoginUrl();
			}
		}
		
		logger.debug("loginProcess pUserId = " + loginVO.getpUserId()); 
		
		UserVO userVO = null;
		String userStatus = "999";
		
		try {
			userVO = loginService.isSSOUser(loginVO);
		} catch(Exception e) {
			logger.error("loginService.isSSOUser Error = " + e);
		}
		
		if(userVO != null ) {
			if(userVO.getStatus() != null && !"".equals(userVO.getStatus())) {
				userStatus = userVO.getStatus();
			}
		}
		
		if("000".equals(userStatus)) {
			// 세션값 설정
			session.setAttribute("NEO_SSO_LOGIN", "N");						// SSO 세션으로 넘어온 사용자 아님
			session.setAttribute("NEO_USER_ID", userVO.getUserId());		// 사용자ID
			session.setAttribute("NEO_USER_NM", userVO.getUserNm());		// 사용자명
			session.setAttribute("NEO_DEPT_NO", userVO.getDeptNo());		// 부서번호
			session.setAttribute("NEO_DEPT_NM", userVO.getDeptNm());		// 부서명
			session.setAttribute("NEO_TZ_CD", userVO.getTzCd());			// 타임존코드
			session.setAttribute("NEO_TZ_TERM", userVO.getTzTerm());		// 타임존시간차
			session.setAttribute("NEO_UILANG", userVO.getUilang());			// 언어권
			session.setAttribute("NEO_CHARSET", userVO.getCharset());		// 문자셋
			session.setAttribute("NEO_FONT", userVO.getFont());				// 폰트
			session.setAttribute("NEO_ORG_CD", userVO.getOrgCd());			// 조직코드
			session.setAttribute("NEO_ORG_NM", userVO.getOrgKorNm());		// 조직명
			session.setAttribute("NEO_JOB_GB", userVO.getJobGb());			// 직책코드
			session.setAttribute("NEO_JOB_NM", userVO.getJobNm());			// 직책명
			session.setAttribute("NEO_LINK", userVO.getLinkService());		// 사용자 링크 서비스
			
			session.setAttribute("NEO_MAIL_FROM_NM", userVO.getMailFromNm()); 	// 발송자명 
			session.setAttribute("NEO_MAIL_FROM_EM", userVO.getMailFromEm()); 	// 발송자이메일주소
			session.setAttribute("NEO_USER_TEL", userVO.getUserTel());			// 연락처
			session.setAttribute("NEO_USER_EM", userVO.getUserEm());			// 이메일
			session.setAttribute("NEO_REPLY_TO_EM", userVO.getReplyToEm());		// 회신이메일
			session.setAttribute("NEO_RETURN_EM", userVO.getReturnEm()); 		// return 이메일 
			session.setAttribute("NEO_PER_PAGE", StringUtil.setNullToInt(userVO.getPerPage(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")))); // 사용자별 목록 조회 페이지
			
			session.setAttribute("NEO_USE_EMS", "N");			// EMS 사용여부
			session.setAttribute("NEO_USE_RNS", "N");			// RNS 사용여부
			session.setAttribute("NEO_USE_SMS", "N");			// SMS 사용여부
			session.setAttribute("NEO_USE_PUSH", "N");			// PUSH 사용여부
			
			int [][] arrUserService = checkLicense(userVO);
			
			int service = 0;
			int useYn = 0;
			for(int i=0; i < arrUserService.length ; i++) {
				service = arrUserService[i][0] ;
				switch(service) {
				case 10:
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_EMS", "Y");			// EMS 사용여부
					}
					break;
				case 20: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_RNS", "Y");			// RMS 사용여부
					}
					break;
				case 30: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_SMS", "Y");			// SMS 사용여부
					}
					break;
				case 40: 
					useYn =  arrUserService[i][1] ;
					if (useYn == 1) {
						session.setAttribute("NEO_USE_PUSH", "Y");			// PUSH 사용여부
					}
					break;
				default: 
					break;
				}
			}
			if(userVO.getUseSYS() != null && !"".equals(userVO.getUseSYS()) && "1".equals(userVO.getUseSYS())) {
				session.setAttribute("NEO_USE_SYS", "Y");			// 공통설정사용권한여부
			} else {
				session.setAttribute("NEO_USE_SYS", "N");			// 공통설정사용권한여부
			} 
			  
			// 관리자 여부
			if(userVO.getDeptNo() == 1) {
				session.setAttribute("NEO_ADMIN_YN", "Y");
			} else {
				session.setAttribute("NEO_ADMIN_YN", "N");
			}
			
			// 사용자 프로그램 사용권한 조회
			List<SysMenuVO> menuList = null;
			try {
				menuList = loginService.getUserMenuList(userVO);
			} catch(Exception e) {
				logger.error("loginService.getUserMenuList Error = " + e);
			} 
			
			session.setAttribute("NEO_MENU_LIST", menuList);
			
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("SSOLogin");
			logVO.setContentPath("/lgn/ssolgn.ums");
			insertLoginActionLog(request, session, logVO);
			
			model.addAttribute("result","Y"); 
			
			String retUrl = getLoginUrl();
			
			if(loginVO.getpDirectMenu() != null && !"".equals(loginVO.getpDirectMenu())) {
				String directMenu = loginVO.getpDirectMenu();
				if ( "10".equals(directMenu)) {
					retUrl= "redirect:/ems/apr/mailAprListP.ums?pMenuId=" + "M1003000" + "&menuId=" + "M1003004" + "&searchAprUserId=" + userVO.getUserId() + "&topNotiYn=Y" ;
				} else if( "20".equals(directMenu)) {
					retUrl= "redirect:/ems/apr/mailAprListP.ums?pMenuId=" + "M1003000" + "&menuId=" + "M1003004" ;
				} else if( "30".equals(directMenu)) {
					retUrl= "redirect:/ems/cam/taskListP.ums?pMenuId=" + "M1002000" + "&menuId=" + "M1002001" ;
				} else if( "40".equals(directMenu)) {
					retUrl= "redirect:/rns/svc/serviceListP.ums?pMenuId=" + "M2001000" + "&menuId=" + "M2001002" ;
				} else {
					retUrl =  "redirect:/ems/index.ums";
				}
			} else {
				retUrl = "redirect:/ems/index.ums";
			}
			
			return retUrl;
		} else {
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("SSOLogin");
			logVO.setContentPath("/lgn/ssolgn.ums");
			session.setAttribute("NEO_USER_ID", loginVO.getpUserId());
			insertLoginActionLog(request, session, logVO);

			if(userVO != null ) {
				model.addAttribute("result","E");
			} else {
				model.addAttribute("result","N");
			}
			return "/err/errorUser";
		}
	}
	
	/**
	 * 로그아웃 처리
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logout")
	public String goLogout(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		ActionLogVO logVO = new ActionLogVO();
		logVO.setStatusGb("Success");
		logVO.setContent("LogOut");
		logVO.setContentPath("/lgn/logout.ums");
		insertLoginActionLog(request, session, logVO); 
		
		// 세션정보 초기화
		session.invalidate();
		return "lgn/logout";
	}
	
	/**
	 * 세션 타임아웃 처리
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/timeout")
	public String goSessionTimeout(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		String requestUri = request.getRequestURI();
		
		model.addAttribute("requestUri", requestUri);
		
		return "lgn/timeout";
	}
	
	public void insertLoginActionLog(HttpServletRequest req, HttpSession session, ActionLogVO actionLogVO) {
		
		try {
			if("Success".equals(actionLogVO.getStatusGb())) {
				actionLogVO.setContentType("000"); // 000: 인증
				actionLogVO.setExtrYn("N");
				actionLogVO.setMobilYn("N");
				systemService.insertActionLog(req, session, actionLogVO);
			} else {
				
				actionLogVO.setLogDt(StringUtil.getDate(Code.TM_YMDHMSM) );		// 로그일시
				actionLogVO.setSessionId( session.getId() );					// 세션ID
				actionLogVO.setIpAddr(req.getRemoteAddr() );					// IP주소 
				String pUserId = ""; 
				if(req.getParameter("pUserId") != null && !"".equals(req.getParameter("pUserId"))){
					pUserId = EncryptAccUtil.getEncryptedSHA256(req.getParameter("pUserId"), properties.getProperty("ACCOUNT.KEYSTRING"));
				}
				actionLogVO.setUserId(pUserId);	// 사용자ID
				actionLogVO.setDeptNo(0);	// 사용자그룹
				actionLogVO.setOrgCd("");	// 조직코드
				
				actionLogVO.setStatusGb("Failure");  // 000: 인증
				actionLogVO.setContentType("000");
				if(actionLogVO.getMessage() == null || "".equals(actionLogVO)) {
					actionLogVO.setMessage("인증정보 불일치");
				}
				actionLogVO.setExtrYn("N");
				actionLogVO.setMobilYn("N");
				
				systemService.insertActionLog(actionLogVO);
			}
		} catch(Exception e) {
			logger.error("systemService.insertActionLog error = " + e);
		} 
	}
	
	public int[][] checkLicense(UserVO userVO) {
		
		List<ServiceVO> userServiceList = null;
		ServiceVO userService = new ServiceVO();
		String licenseKey = ""; 
		String decLicenseKey = "";
		String domainKey = ""; 
		int [][] arrUserService = null;
		int arrIndex = 0 ; 
		try {
			userServiceList = mainService.getUserService(userVO);
			if(userServiceList != null) {
				arrUserService = new int [userServiceList.size()][2];
				
				for(int i = 0 ; i < userServiceList.size() ; i ++) {
					userService = userServiceList.get(i);
					if ( userService.getUseYn().equals("1")) {
						arrUserService[arrIndex][0] = userService.getServiceGb();
						licenseKey  = userService.getLicenseKey();
						decLicenseKey= EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"),
								properties.getProperty("LICENSE.KEYSTRING"), licenseKey);
						 
						domainKey = userService.getCustDomain()  + "+" +  userService.getServiceNm();
						if(!decLicenseKey.substring(0, domainKey.length()).equals(domainKey)) {
							userServiceList.get(i).setPayYn(0);
							arrUserService[arrIndex][1] = 0;
						} else {
							decLicenseKey = decLicenseKey.replace(domainKey, "");
							
							SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
							Date expireDate = null;
							try {
								expireDate = mSimpleDateFormat.parse(StringUtil.getFDate(decLicenseKey.substring(1, 9), Code.DT_FMT2));
								Date currentDate = new Date();
								int compare = expireDate.compareTo( currentDate );  
								if(compare >  0  ) {
									userServiceList.get(i).setPayYn(1);
									arrUserService[arrIndex][1] = 1;
								} else {
									userServiceList.get(i).setPayYn(0);
									arrUserService[arrIndex][1] = 0;
								}  
							} catch (Exception e) {
								logger.error("mainService.getUserService error[Expire Date illegal] = " + e);
								userServiceList.get(i).setPayYn(0);
								arrUserService[arrIndex][1] = 0;
							}
						} 						
						arrIndex += 1; 
					} 
				}
			}
				
		} catch (Exception e) {
			logger.error("mainService.getUserService error[C009] = " + e);
		} 
		
		return arrUserService;
	}
	
	/**
	 * 사용자 본인 인증 한다 
	 * 
	 * @param loginVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userAuthSendMail")
	public ModelAndView userAuthSendMail(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("userAuthSendMail pUserId = " + loginVO.getpUserId()); 
		
		//암호화된 내용 복호화 
		int result = 0; 
		String cipherUserId = loginVO.getpUserId();
		try {
			loginVO.setpUserId(EncryptAccUtil.getEncryptedSHA256(cipherUserId, properties.getProperty("ACCOUNT.KEYSTRING")));
			int pwLimit = StringUtil.setNullToInt(properties.getProperty("PW_INIT_LIMIT"));
			String noReplyEm  =  "noreply@lotteeatz.com";
			if(!StringUtil.isNull(properties.getProperty("NO_REPLY_MAIL"))){
				noReplyEm = properties.getProperty("NO_REPLY_MAIL");
			}
			
			// 사용자 정보를 수정한다.
			try { 	
				String strInitPwd =  StringUtil.makeRandomString(Code.RAND_TYPE_D);
				String strPwinitlimtDt = StringUtil.getCalcDateFromCurr(pwLimit, "H", "yyyyMMddHHmm");	
				strInitPwd.toUpperCase();
				
				UserVO searchUserVO = new UserVO();
				searchUserVO.setUserId(loginVO.getpUserId());
				searchUserVO.setUilang("000");
				UserVO userVO = accountService.getUserInfo(searchUserVO);
				
				if(userVO != null) {
					if(userVO.getUserEm() != null && !"".equals(userVO.getUserEm()) ) { 
						userVO.setUserPwd(EncryptUtil.getEncryptedSHA256(strInitPwd)); 
						userVO.setPwinitlimtDt(strPwinitlimtDt);
				 		userVO.setUpId(loginVO.getpUserId());
						userVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));

						result = accountService.initUserPwd(userVO);
						if (result  > 0 ) {
							UserVO mailUserVO = new UserVO();
							strPwinitlimtDt = StringUtil.getFDate(strPwinitlimtDt);
							mailUserVO.setUserId(userVO.getUserId());
							mailUserVO.setUserPwd(strInitPwd); 
							mailUserVO.setUserNm(userVO.getUserNm());
							mailUserVO.setUserEm(userVO.getUserEm());
							mailUserVO.setReplyToEm(cryptoService.getEncrypt("SMAIL", noReplyEm)); 
							strPwinitlimtDt = StringUtil.getFDate(strPwinitlimtDt); 
							mailUserVO.setPwinitlimtDt(strPwinitlimtDt);
							accountService.sendInitUserPwdMail(mailUserVO);
						} 
					} else {
						result = -1;
					}
				} else {
					result = -1;
				} 

			} catch (Exception e) {
				logger.error("accountService.userAuthSendMail error = " + e);
			}
		} catch(Exception e) {
			result = -7;
			logger.error("userAuthSendMail EncryptAccUtil Error = " + e);
		} 
		

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
			map.put("message", "등록된 메일로 초기화된 암호정보가 발송되었습니다 메일을 확인 해 주세요");
		} else {
			map.put("result", "Fail");
			map.put("message", "해당 계정명(ID)로 등록된 사용자가 없거나 해당 사용자에 등록된 이메일주소가 없습니다 관리자에게 문의하세요");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	
	/**
	 * 관리자 인증  
	 * 
	 * @param logiVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/goSysUserAuth")
	public ModelAndView checkSysUser(@ModelAttribute LoginVO loginVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("checkInitPwd pUserId = " + loginVO.getpUserId());
		logger.debug("checkInitPwd pUserPwd = " + loginVO.getpUserPwd());
		
		//암호화된 내용 복호화 
		int result = 0;
		String cipherUserPwd = loginVO.getpUserPwd();
		String cipherUserId = loginVO.getpUserId();
		try {
			loginVO.setpUserId(EncryptAccUtil.getEncryptedSHA256(cipherUserId, properties.getProperty("ACCOUNT.KEYSTRING")));
			loginVO.setpUserPwd(EncryptAccUtil.getEncryptedSHA256(cipherUserPwd, properties.getProperty("ACCOUNT.KEYSTRING")));
		} catch(Exception e) {
			result = -7;
			logger.error("checkInitPwd EncryptAccUtil Error = " + e);
		} 
		
		//getEncryptedPBKDF2
		String encPasswd = EncryptUtil.getEncryptedSHA256(loginVO.getpUserPwd());
		loginVO.setpUserPwd(encPasswd);
		UserVO userVO = null; 
		
		String returnResult = "";
		String returnMessage = "";
		
		if (session.getAttribute("NEO_USER_ID") != null && !"".equals(session.getAttribute("NEO_USER_ID"))){
			if(session.getAttribute("NEO_USER_ID") .equals(loginVO.getpUserId())) {
				// 사용자 아이디, 비밀번호 체크
				try {
					userVO = loginService.isValidUser(loginVO);
				} catch(Exception e) {
					result = -6;
					returnMessage = "관리자 인증 처리에 오류가 발생하였습니다";
					logger.error("loginService.isValidUser Error = " + e);
				}
			} else {
				result = -8;
				returnMessage = "로그인 세션에 계정명(ID)가 없습니다 로그아웃 후 다시 이용해주세요";
			}
		} else {
			result = -9;
			returnMessage = "로그인 세션에 계정명(ID)가 없습니다 로그아웃 후 다시 이용해주세요";
		}
		
		//
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if (result > -1 ) {
			if(userVO != null) {
				if(userVO.getUserNm() != null && !"".equals(userVO.getUserNm())){ 
					returnResult = "Success";
				} else {
					returnResult = "Fail";
					returnMessage = "로그인계정명(ID) 또는 비밀번호를 확인 해 주세요 ";
				}
			} else {
				returnResult = "Fail";
				returnMessage = "로그인계정명(ID) 또는 비밀번호를 확인 해 주세요 ";
			}
		}  else {
			returnResult = "Fail";
		}
		
		map.put("result", returnResult);
		map.put("msg", returnMessage);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	/**
	 * 사용자 접근 권한 오류
	 * @return
	 */
	@RequestMapping(value="default")
	public String getDefault() {
		return "lgn/sso/default";
	}
	
	public String getLoginUrl() {
		String captcha =StringUtil.setNullToString(properties.getProperty("CAPTCHA")) ;
		String returnUrl = "lgn/lgnP"; 
		if("Y".equals(captcha)) {
			returnUrl =  "lgn/lgnPCaptcha";
		}
		return returnUrl;
	}
}
