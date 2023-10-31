/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.26
 * 설명 : 캠페인관리 Controller
 */
package kr.co.enders.ums.ems.cam.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; 

import kr.co.enders.ums.com.service.CodeService;
import kr.co.enders.ums.com.service.CryptoService;
import kr.co.enders.ums.com.vo.CodeVO; 
import kr.co.enders.ums.ems.cam.service.CampaignService; 
import kr.co.enders.ums.ems.cam.vo.CampaignTemplateAttachVO;
import kr.co.enders.ums.ems.cam.vo.CampaignTemplateProhibitWordVO;
import kr.co.enders.ums.ems.cam.vo.CampaignTemplateVO;
import kr.co.enders.ums.ems.cam.vo.CampaignTemplateWebAgentVO;
import kr.co.enders.ums.ems.cam.vo.CampaignVO;
import kr.co.enders.ums.ems.cam.vo.TaskVO;
import kr.co.enders.ums.ems.seg.service.SegmentService;
import kr.co.enders.ums.ems.seg.vo.SegmentVO; 
import kr.co.enders.ums.lgn.service.LoginService;
import kr.co.enders.ums.lgn.vo.LoginVO;
import kr.co.enders.ums.sys.acc.service.AccountService;
import kr.co.enders.ums.sys.acc.vo.SysMenuVO;
import kr.co.enders.ums.sys.acc.vo.UserVO; 
import kr.co.enders.ums.sys.log.service.SystemLogService;
import kr.co.enders.ums.sys.log.vo.ActionLogVO;
import kr.co.enders.util.Code;
import kr.co.enders.util.HmacUtil;
import kr.co.enders.util.PropertiesUtil;
import kr.co.enders.util.StringUtil;

@Controller
@RequestMapping("/ems/cam/api")
public class ApiCampaignController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private SegmentService segmentService;
	 
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private AccountService accountService;	
	
	@Autowired
	private SystemLogService systemService;
	
	@Autowired
	private LoginService loginService;
	  
	
	@Autowired
	private PropertiesUtil properties;
	 
	
	/****************************** 캠페인테플릿 iFrmae용 화면 제공  API ******************************/
	/**
	/**
	 * 캠페인템플릿 등록 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campTempAddP")
	public String goCampTempAddP(@ModelAttribute CampaignTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("APICampaignController goCampTempAddP userId      = " + searchVO.getUserId());
		logger.debug("APICampaignController goCampTempAddP campId      = " + searchVO.getCampId());
		logger.debug("APICampaignController goCampTempAddP cellNodeId  = " + searchVO.getCellNodeId());
		logger.debug("APICampaignController goCampTempAddP contId      = " + searchVO.getContId()); 
		 
		String retUrl = null;
		String campId = "" ;
		String cellNodeId = "";
		String contId = "";
		
		if (searchVO.getUserId() == null || "".equals(searchVO.getUserId()) || searchVO.getCampId() == null || "".equals(searchVO.getCampId()) || searchVO.getCellNodeId() == null || "".equals(searchVO.getCellNodeId()) || searchVO.getContId() == null || "".equals(searchVO.getContId()) ) {
			String needParam = "";
			if (searchVO.getUserId() == null || "".equals(searchVO.getUserId())) {
				needParam += " userId,";
			} 
			if (searchVO.getCampId() == null || "".equals(searchVO.getCampId())) {
				needParam += " campId,";
			}
			if (searchVO.getCellNodeId() == null || "".equals(searchVO.getCellNodeId())) {
				needParam += " cellNodeId,";
			}
			if (searchVO.getContId() == null || "".equals(searchVO.getContId())) {
				needParam += " contId,";
			}
			if (needParam != null && needParam.length() > 0) {
				needParam =  needParam.substring(0, needParam.length() - 1);
			}
			model.addAttribute("error", needParam);					// 부족정보 
			retUrl ="/err/errorApiInfo";
			return retUrl;
		} else {
			campId = searchVO.getCampId();
			cellNodeId = searchVO.getCellNodeId();
			contId = searchVO.getContId();
		}
	
		try {
			retUrl = setSsoSessionInfo(request, session, searchVO.getUserId(), "/ems/cam/api/campTempAddPApi.ums");
			if (!"".equals(retUrl)) {
				return retUrl;
			}
		} catch (Exception e) { 
			logger.debug("APICampaignController goCampTempAddPApi Error       = " + e.getMessage());
			retUrl ="/err/errorApiUser";
			return retUrl;
		}
		
		// 캠페인 목록 조회
		CampaignVO camp = new CampaignVO();
		camp.setUilang((String)session.getAttribute("NEO_UILANG"));
		camp.setSearchStatus("000");
		camp.setPage(1);
		camp.setRows(10000);
		camp.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<CampaignVO> campList = null;
		try {
			campList = campaignService.getCampaignList(camp);
		} catch(Exception e) {
			logger.error("campaignService.getCampaignList error = " + e);
		}
		
		// 수신자정보머지키코드 목록
		CodeVO merge = new CodeVO();
		merge.setUilang((String)session.getAttribute("NEO_UILANG"));
		merge.setCdGrp("C001");
		merge.setUseYn("Y");
		List<CodeVO> mergeList = null;
		try {
			mergeList = codeService.getCodeList(merge);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C001] error = " + e);
		}
		
		// 부서목록(코드성) 조회
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자정보
		UserVO userInfo = new UserVO();
		userInfo.setUilang((String)session.getAttribute("NEO_UILANG"));
		userInfo.setUserId((String)session.getAttribute("NEO_USER_ID"));
		try {
			userInfo = accountService.getUserInfo(userInfo);
			userInfo.setMailFromEm(cryptoService.getDecrypt("MAIL_FROM_EM", userInfo.getMailFromEm()));
		} catch(Exception e) {
			logger.error("accountService.getUserInfo error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setServiceGb(10);
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		 
		String pSegNo = properties.getProperty("CAMP_SEG_NO");
		int segNo = 0 ;
		if (pSegNo != null && !"".equals(pSegNo)) {
			segNo = StringUtil.getStringToInt(pSegNo);
			seg.setSearchSegNo(segNo);
		} 
		
		List<SegmentVO> segList = null;
		String segmentNm = null; 
		String segNoc = null;
		try {
			segList = segmentService.getSegmentList(seg);
			if(segList != null && segList.size() >0 ) {
				segmentNm = segList.get(0).getSegNm();
				segNoc =  segList.get(0).getSegNo() + "|" + segList.get(0).getMergeKey();
				
			}
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		} 
		
		// 고객정보 조회사유코드 조회
		CodeVO reasonVO = new CodeVO();
		reasonVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		reasonVO.setCdGrp("C102");
		reasonVO.setUseYn("Y");
		List<CodeVO> reasonList = null;
		try {
			reasonList = codeService.getCodeList(reasonVO);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C102] error = " + e);
		}
		
		//기능권한 추가  - 실시간 테스트발송 
		CodeVO funcAuth = new CodeVO();
		funcAuth.setUilang((String)session.getAttribute("NEO_UILANG"));
		funcAuth.setUserId((String)session.getAttribute("NEO_USER_ID"));
		funcAuth.setCd("002");
		
		String testSendAuth ="N";
		try {
			testSendAuth = codeService.getUserFuncAuth(funcAuth);
		} catch(Exception e) {
			logger.error("codeService.getUserFuncAuth[002] error = " + e);
		}
		
		//환경설정 : 고객정보체크 
		funcAuth.setCd("006");
		String envSetAuth ="N";
		try {
			envSetAuth = codeService.getUserFuncAuth(funcAuth);
			logger.debug("codeService envSetAuth = " + envSetAuth);
		} catch(Exception e) {
			logger.error("codeService.getUserFuncAuth[006] error = " + e);
		}
		
		model.addAttribute("searchVO", searchVO);			// 검색항목
		model.addAttribute("mergeList", mergeList);			// 수신자정보머지키코드
		model.addAttribute("userInfo", userInfo);			// 사용자정보
		model.addAttribute("campList", campList);			// 캠페인목록
		model.addAttribute("segList", segList);				// 발송대상(세그먼트) 목록
		model.addAttribute("deptList", deptList);			// 부서 목록
		model.addAttribute("reasonList", reasonList);		// 수신자그룹조회 사유
		
		model.addAttribute("testSendAuth", testSendAuth);	// 기능권한 : 테스트발송
		model.addAttribute("envSetAuth", envSetAuth);		// 기능권한 : 고객정보체크
		
		//GRS 관련 추가 
		model.addAttribute("defSegNo", segNo);				// 기본 SegNO 
		model.addAttribute("defSegNm", segmentNm);			// 기본 SegNm
		model.addAttribute("defSegNoc", segNoc);			// 기본 SegNm머지리스트
		model.addAttribute("campId", campId);				// API 요청 캠페인아이디
		model.addAttribute("cellNodeId", cellNodeId);		// API 요청 고객군아디이
		model.addAttribute("contId", contId);				// API 요청 컨텐츠아이디
		retUrl = "ems/cam/campTempAddPApi";
		return retUrl;
	}
	
	/**
	 * 캠페인 템플릿 정보 수정 화면을 출력한다.
	 * @param searchVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/campTempUpdateP")
	public String goCampTempUpdateP(@ModelAttribute CampaignTemplateVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("APICampaignController goCampTempUpdateP tids        = " + searchVO.getTid());
		logger.debug("APICampaignController goCampTempUpdateP userId      = " + searchVO.getUserId());
		logger.debug("APICampaignController goCampTempUpdateP campId      = " + searchVO.getCampId());
		logger.debug("APICampaignController goCampTempUpdateP cellNodeId  = " + searchVO.getCellNodeId());
		logger.debug("APICampaignController goCampTempUpdateP contId      = " + searchVO.getContId()); 
		 
		String retUrl = null;
		String campId = "" ;
		String cellNodeId = "";
		String contId = "";
		
		if (searchVO.getTid() == 0 || searchVO.getUserId() == null || "".equals(searchVO.getUserId()) || searchVO.getCampId() == null || "".equals(searchVO.getCampId()) || searchVO.getCellNodeId() == null || "".equals(searchVO.getCellNodeId()) || searchVO.getContId() == null || "".equals(searchVO.getContId()) ) {
			String needParam = "";
			if (searchVO.getUserId() == null || "".equals(searchVO.getUserId())) {
				needParam += " userId,";
			} 
			if (searchVO.getCampId() == null || "".equals(searchVO.getCampId())) {
				needParam += " campId,";
			}
			if (searchVO.getCellNodeId() == null || "".equals(searchVO.getCellNodeId())) {
				needParam += " cellNodeId,";
			} 
			if (searchVO.getContId() == null || "".equals(searchVO.getContId())) {
				needParam += " contId,";
			} 
			if (searchVO.getTid() == 0) {
				needParam += " tid,";
			} 
			if (needParam != null && needParam.length() > 0) {
				needParam =  needParam.substring(0, needParam.length() - 1);
			}
			model.addAttribute("error", needParam);					// 부족정보 
			retUrl ="/err/errorApiInfo";
			return retUrl;
		} else {
			campId = searchVO.getCampId();
			cellNodeId = searchVO.getCellNodeId();
			contId = searchVO.getContId();
		}
	
 
		try {
			retUrl = setSsoSessionInfo(request, session, searchVO.getUserId(), "/ems/cam/api/campTempUpdatePApi.ums");
			if (!"".equals(retUrl)) {
				return retUrl;
			}
		} catch (Exception e) { 
			logger.debug("APICampaignController goCampTempUpdatePApi Error       = " + e.getMessage());
			retUrl ="/err/errorApiUser";
			return retUrl;
		}
		
		// 서비스 정보 조회
		CampaignTemplateVO campaignTemplateInfo = null;
		try {
			campaignTemplateInfo = campaignService.getCampaignTemplateInfo(searchVO.getTid());
			String prohibitDesc = "";
			
			if ("002".equals(campaignTemplateInfo.getProhibitChkTyp())){

				if ("Y".equals(campaignTemplateInfo.getImgChkYn())){
					prohibitDesc += "이미지 /";
				}
				if(campaignTemplateInfo.getAttchCnt() > 0) {
					prohibitDesc += "첨부파일(" + Integer.toString(campaignTemplateInfo.getAttchCnt()) + "건) /";
				}
				
				if(campaignTemplateInfo.getProhibitTitleCnt() > 0) {
					prohibitDesc += "금칙어(" + Integer.toString(campaignTemplateInfo.getProhibitTitleCnt()) + "건) /";
					campaignTemplateInfo.setProhibitTitleCnt(0);
					campaignTemplateInfo.setProhibitTextCnt(0);
					//금지어정보가져오기
					List<CampaignTemplateProhibitWordVO> prohibitWordList = null;
					 
					try {
						prohibitWordList = campaignService.getCampaignTemplateProhibitWordList(searchVO.getTid());
						if (prohibitWordList != null) {
							for (int i=0; i < prohibitWordList.size(); i++) {
								//제목 
								if("000".equals(prohibitWordList.get(i).getContentTyp())){
									campaignTemplateInfo.setProhibitTitleCnt(prohibitWordList.get(i).getProhibitCnt());
									campaignTemplateInfo.setProhibitTitleDesc(prohibitWordList.get(i).getProhibitDescString());
								}
								
								//본문 
								if("001".equals(prohibitWordList.get(i).getContentTyp())){
									campaignTemplateInfo.setProhibitTextCnt(prohibitWordList.get(i).getProhibitCnt());
									campaignTemplateInfo.setProhibitTextDesc(prohibitWordList.get(i).getProhibitDescString());
								}
							}
						} 
					} catch(Exception e) {
						logger.error("campaignService.getCampaignTemplateProhibitWordList error = " + e);
					}
				} else {
					campaignTemplateInfo.setProhibitTitleCnt(0);
					campaignTemplateInfo.setProhibitTextCnt(0);
				}
				
				if (prohibitDesc.length() > 2) {
					prohibitDesc = prohibitDesc.substring(0, prohibitDesc.length() - 2);
				}
				campaignTemplateInfo.setProhibitDesc("");
			} else { 
				prohibitDesc ="금칙어 해당사항이 없습니다";
			}
			campaignTemplateInfo.setProhibitDesc(prohibitDesc);
			
		} catch(Exception e) {
			logger.error("campaignService.getCampaignTemplateInfo error = " + e);
		}
		 
		// WebAgent 정보 조회
		CampaignTemplateWebAgentVO webAgent = null;
		try {
			webAgent = campaignService.getCampaignTemplateWebAgentInfo(searchVO.getTid());
		} catch(Exception e) {
			logger.error("campaignService.getCampaignTemplateWebAgentInfo error = " + e);
		}
		 
		// 첨부파일 목록 조회
		List<CampaignTemplateAttachVO> attachList = null;
		try {
			attachList = campaignService.getCampaignTemplateAttachList(searchVO.getTid());
		} catch(Exception e) {
			logger.error("campaignService.getCampaignTemplateAttachList error = " + e);
		}
		
		// 사용자그룹 목록 조회
		CodeVO dept = new CodeVO();
		dept.setStatus("000"); // 정상
		List<CodeVO> deptList = null;
		try {
			deptList = codeService.getDeptList(dept);
		} catch(Exception e) {
			logger.error("codeService.getDeptList error = " + e);
		}
		
		// 사용자 목록 조회
		CodeVO user = new CodeVO();
		user.setDeptNo(campaignTemplateInfo.getDeptNo());
		user.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(user);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}  
		
		//기능권한 추가  - 실시간 테스트발송 
		CodeVO funcAuth = new CodeVO();
		funcAuth.setUilang((String)session.getAttribute("NEO_UILANG"));
		funcAuth.setUserId((String)session.getAttribute("NEO_USER_ID"));
		funcAuth.setCd("002");
		
		String testSendAuth ="N";
		try {
			testSendAuth = codeService.getUserFuncAuth(funcAuth);
		} catch(Exception e) {
			logger.error("codeService.getUserFuncAuth[002] error = " + e);
		}
		
		//환경설정 : 고객정보체크 
		funcAuth.setCd("006");
		String envSetAuth ="N";
		try {
			envSetAuth = codeService.getUserFuncAuth(funcAuth);
			logger.debug("codeService envSetAuth = " + envSetAuth);
		} catch(Exception e) {
			logger.error("codeService.getUserFuncAuth[006] error = " + e);
		}
		
		// 마케팅수신동의유형
		CodeVO mktGb = new CodeVO();
		mktGb.setUilang((String)session.getAttribute("NEO_UILANG"));
		mktGb.setCdGrp("C114");
		mktGb.setUseYn("Y");
		List<CodeVO> mktGbList = null;
		try {
			mktGbList = codeService.getCodeList(mktGb);
		} catch(Exception e) {
			logger.error("codeService.getCodeList[C114] error = " + e);
		}
		
		// 수신자그룹(발송대상) 목록
		SegmentVO seg = new SegmentVO();
		seg.setServiceGb(10);
		seg.setUilang((String)session.getAttribute("NEO_UILANG"));
		seg.setSearchStatus("000");
		seg.setPage(1);
		seg.setRows(10000);
		seg.setSearchDeptNo("Y".equals((String)session.getAttribute("NEO_ADMIN_YN"))?0:(int)session.getAttribute("NEO_DEPT_NO"));
		List<SegmentVO> segList = null;
		try {
			segList = segmentService.getSegmentList(seg);
		} catch(Exception e) {
			logger.error("segmentService.getSegmentList error = " + e);
		}

		
		model.addAttribute("searchVO", searchVO);					// 검색항목
		model.addAttribute("campTempInfo", campaignTemplateInfo);	// 서비스정보
		model.addAttribute("webAgent", webAgent);					// 웹에이전트
		model.addAttribute("attachList", attachList);				// 첨부파일목록
		
		model.addAttribute("deptList", deptList); 					// 부서목록
		model.addAttribute("userList", userList); 					// 사용자목록
		model.addAttribute("mktGbList", mktGbList);					// 마케팅수신동의유형
		model.addAttribute("segList", segList);						// 수신자그룹리스트
		model.addAttribute("testSendAuth", testSendAuth);			// 기능권한 : 테스트발송
		model.addAttribute("envSetAuth", envSetAuth);				// 기능권한 : 고객정보체크
		model.addAttribute("campId", campId);						// API 요청 캠페인아이디
		model.addAttribute("cellNodeId", cellNodeId);				// API 요청 고객군아디이
		model.addAttribute("contId", contId);						// API 요청 컨텐츠아이디
		
		return "ems/cam/campTempUpdatePApi";
	}
	
	private String setSsoSessionInfo(HttpServletRequest request, HttpSession session, String userId, String contentPath) throws ServletException, IOException { 
		logger.debug("APICampaignController setSsoSessionInfo userId = " + userId);
		
		String retUrl = null;
		
		LoginVO loginVO = new LoginVO();
		loginVO.setpUserId(userId);
		
		UserVO userVO = null;
		String userStatus = "999";
		// 사용자 아이디, 비밀번호 체크
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
			session.setAttribute("NEO_SSO_LOGIN", "Y");						// SSO로 페이지 요청
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
			 
			session.setAttribute("NEO_USE_EMS", "Y");			// EMS 사용여부
			session.setAttribute("NEO_USE_RNS", "N");			// RNS 사용여부
			session.setAttribute("NEO_USE_SMS", "N");			// SMS 사용여부
			session.setAttribute("NEO_USE_PUSH", "N");			// PUSH 사용여부 
			session.setAttribute("NEO_USE_SYS", "N");			// 공통설정사용권한여부
			
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
			logVO.setContent("SSOLogin");
			logVO.setContentPath(contentPath);
			insertLoginActionLog(request, session, logVO);
			
			return "";
			
		} else {
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("SSOLogin");
			logVO.setContentPath(contentPath);
			session.setAttribute("NEO_USER_ID", loginVO.getpUserId());		// 사용자ID
			insertLoginActionLog(request, session, logVO);
			
			retUrl ="/err/errorApiUser";
			return retUrl;
		}
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
				actionLogVO.setSessionId( session.getId() );						// 세션ID
				actionLogVO.setIpAddr(req.getRemoteAddr() );					// IP주소
				actionLogVO.setUserId(req.getParameter("pUserId"));				// 사용자ID
				actionLogVO.setDeptNo(0);	// 사용자그룹
				actionLogVO.setOrgCd("");	// 조직코드
				
				actionLogVO.setStatusGb("Failure");  // 000: 인증
				actionLogVO.setContentType("000");
				actionLogVO.setMessage("인증정보 불일치");
				actionLogVO.setExtrYn("N");
				actionLogVO.setMobilYn("N");
				
				systemService.insertActionLog(actionLogVO);
			}
		} catch(Exception e) {
			logger.error("systemService.insertActionLog error = " + e);
		} 
	}
	
	/****************************** 수신거부 API ******************************/
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/receiveDenyJson")
	private void goReceiveDenyJson(@RequestParam String userId , Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("/ems/cam/receiveDenyJson  userid  = " + userId); 
		  
		JSONArray agreArr = new JSONArray();
		JSONObject agreList = new JSONObject();
		JSONObject data = new JSONObject();
		
		String strReturn  = null;
		String strReturnMessage = null;
		
		if(userId != null  && !"".equals(userId)) { 
			
			data.put("REQ_TYPE", properties.getProperty("DENY_REQ_TYPE"));
			data.put("SEND_DT", StringUtil.getDate(Code.TM_YMD));
			data.put("SEND_TIME", StringUtil.getDate(Code.TM_HMS));
			data.put("TR_NO", StringUtil.getDate(Code.TM_YMDHMS));
			data.put("TR_DT", StringUtil.getDate(Code.TM_YMD));
			data.put("TR_TIME", StringUtil.getDate(Code.TM_YMDHMSM));
			data.put("CLIENT_ID", properties.getProperty("DENY_CLIENT_ID"));
			data.put("CHNL_CD", properties.getProperty("DENY_CHNL_CD"));
			data.put("PGM_NO", properties.getProperty("DENY_PGM_NO"));
			data.put("MBR_NO",userId);
			
			//data.put("TIER_AFTR_DT", properties.getProperty("TIER_AFTR_DT"));
			
			agreList.put("AGRE_TYPE", properties.getProperty("DENY_AGRE_TYPE"));
			agreList.put("AGRE_YN", properties.getProperty("DENY_AGRE_YN"));
			agreList.put("AGRE_DT", StringUtil.getDate(Code.TM_YMDHMS));

			agreArr.put(agreList);
			
			data.put("CUST_AGRE_OBJ", agreArr);
			String reqData = data.toString();
			
			logger.debug("goReceiveDeny Api Request Send Data: " + reqData);
			
			//우선 hmac 잠시 처리 안함 
			/*
			String siteKey = properties.getProperty("SITEKEY");
			String hashResult = "";
			
			try {
				hashResult = HmacUtil.generateHMAC(siteKey, jsonData);
			} catch (InvalidKeyException e) {
//				logger.error(" invalid key error " + e);
			} catch (UnsupportedEncodingException e) {
				logger.error(" encoding error" + e);
			} catch (NoSuchAlgorithmException e) {
				logger.error(" can't find algorithm error" + e);
			}
			logger.debug("***** hashResult         :   " + hashResult );
			*/
			
			//response.addHeader("hashResult", hashResult);
			
			String sendString = "";
			String apiUrl = properties.getProperty("DENY_REQ_URL"); //API URL 입력
			
			try {
				sendString = reqData; 
				
				int TIMEOUT_VALUE = 1000;   // 1초
				 	
				try {
					URL object = new URL(apiUrl);
					HttpURLConnection con = null;
					con = (HttpURLConnection) object.openConnection();
					con.setConnectTimeout(TIMEOUT_VALUE);
					con.setReadTimeout(TIMEOUT_VALUE);
					con.setDoOutput(true);
					con.setDoInput(true);					
					con.setRequestProperty("Content-Type", "application/json");
					con.setRequestProperty("Accept", "*/*");
					con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
					con.setRequestMethod("POST");
					OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
					wr.write(sendString);
					wr.flush();
	
					StringBuilder sb = new StringBuilder();
					
					int HttpResult = con.getResponseCode();
					logger.debug("goReceiveDeny http Status Code : "+ HttpResult);
	
					if (HttpResult == HttpURLConnection.HTTP_OK) {
						BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
						String line = null;
						while ((line = br.readLine()) != null) {
							sb.append(line + "\n");
						}
						strReturn = sb.toString();
						logger.debug(" goReceiveDeny API request result :  " + strReturn);
						br.close();
						try {
							JSONObject objDenyResult = new JSONObject(strReturn);
							JSONArray objDenyResultHeader = (JSONArray) objDenyResult.get("HEAD_DATA"); 
							JSONObject denyResultHeader = objDenyResultHeader.getJSONObject(0); 
							if ("S".equals(denyResultHeader.get("RST"))) {
								strReturn = "Success";
								strReturnMessage = denyResultHeader.get("RST_MSG").toString();
							} else {
								strReturn = "fail";
								strReturnMessage = denyResultHeader.get("RST_MSG").toString();
							}
						} catch (Exception e) {
							strReturn = "exception";
							strReturnMessage ="시스템 오류가 발생했습니다 다시 시도해주세요";
							logger.error(" goReceiveDeny RST Check Error : " + e.getMessage());
						}
						strReturn = "Success";
					} else {
						logger.error(" goReceiveDeny HttpURLConnection Fail : " + con.getResponseMessage());
						strReturn ="fail"; 
						strReturnMessage ="수신거부 요청 서버에 접속할 수 없습니다 다시 시도해주세요";
					}
				} catch (Exception e) { 
					logger.error(" goReceiveDeny Api Request Exception : " + e.getMessage());
					strReturn = "exception";
					strReturnMessage ="시스템 오류가 발생했습니다 다시 시도해주세요";
				}
				
			} catch (Exception e) {
				strReturn = "exception";
				strReturnMessage ="시스템 오류가 발생했습니다 다시 시도해주세요";
				logger.error(" goReceiveDeny Exception :" + e.getMessage());
			} 
		} else {
			strReturn = "fail";
			strReturnMessage ="수신거부 요청 정보가 누락되었습니다(사용자ID)";
		}
		
		logger.error(" goReceiveDeny 결과 :" + strReturn + " / 결과 메시지 : " + strReturnMessage );
		
		try {
			sendResultJson(response, strReturn, strReturnMessage );
		} catch (Exception e) {
			logger.error(" goReceiveDeny sendResultJson :" + e.getMessage() );
		}
		
	}
	
	/****************************** 수신거부 API alert 메시지 테스트 ******************************/
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/receiveDeny")
	private String goReceiveDeny(@RequestParam String userId , Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("/ems/cam/api/receiveDeny  userid  = " + userId); 
		  
		JSONArray agreArr = new JSONArray();
		JSONObject agreList = new JSONObject();
		JSONObject data = new JSONObject();
		
		String strApiReturnMsg  = "";
		String strReturn  = "";
		String strReturnMessage = null;
		
		if(userId != null  && !"".equals(userId)) { 
			
			data.put("REQ_TYPE", properties.getProperty("DENY_REQ_TYPE"));
			data.put("SEND_DT", StringUtil.getDate(Code.TM_YMD));
			data.put("SEND_TIME", StringUtil.getDate(Code.TM_HMS));
			data.put("TR_NO", StringUtil.getDate(Code.TM_YMDHMS));
			data.put("TR_DT", StringUtil.getDate(Code.TM_YMD));
			data.put("TR_TIME", StringUtil.getDate(Code.TM_YMDHMSM));
			data.put("CLIENT_ID", properties.getProperty("DENY_CLIENT_ID"));
			data.put("CHNL_CD", properties.getProperty("DENY_CHNL_CD"));
			data.put("PGM_NO", properties.getProperty("DENY_PGM_NO"));
			data.put("MBR_NO",userId);
			
			//data.put("TIER_AFTR_DT", properties.getProperty("TIER_AFTR_DT"));
			
			agreList.put("AGRE_TYPE", properties.getProperty("DENY_AGRE_TYPE"));
			agreList.put("AGRE_YN", properties.getProperty("DENY_AGRE_YN"));
			agreList.put("AGRE_DT", StringUtil.getDate(Code.TM_YMDHMS));

			agreArr.put(agreList);
			
			data.put("CUST_AGRE_OBJ", agreArr);
			String reqData = data.toString().trim();
			
			logger.debug("goReceiveDeny Api Request Send Data:" + reqData +"");
			
			//우선 hmac 잠시 처리 안함 
			/*
			String siteKey = properties.getProperty("SITEKEY");
			String hashResult = "";
			
			try {
				hashResult = HmacUtil.generateHMAC(siteKey, jsonData);
			} catch (InvalidKeyException e) {
//				logger.error(" invalid key error " + e);
			} catch (UnsupportedEncodingException e) {
				logger.error(" encoding error" + e);
			} catch (NoSuchAlgorithmException e) {
				logger.error(" can't find algorithm error" + e);
			}
			logger.debug("***** hashResult         :   " + hashResult );
			*/
			
			//response.addHeader("hashResult", hashResult);
			
			String sendString = "";
			String apiUrl = properties.getProperty("DENY_REQ_URL"); //API URL 입력
			
			try {
				sendString = reqData; 
				
				int TIMEOUT_VALUE = 1000;   // 1초
				 	
				try {
					URL object = new URL(apiUrl);
					HttpURLConnection con = null;
					con = (HttpURLConnection) object.openConnection();
					con.setConnectTimeout(TIMEOUT_VALUE);
					con.setReadTimeout(TIMEOUT_VALUE);
					con.setDoOutput(true);
					con.setDoInput(true);					
					con.setRequestProperty("Content-Type", "application/json");
					con.setRequestProperty("Accept", "*/*");
					con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
					con.setRequestMethod("POST");
					OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
					wr.write(sendString);
					wr.flush();
	
					StringBuilder sb = new StringBuilder();
					
					int HttpResult = con.getResponseCode();
					logger.debug("goReceiveDeny http Status Code : "+ HttpResult);
	
					if (HttpResult == HttpURLConnection.HTTP_OK) {
						BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
						String line = null;
						while ((line = br.readLine()) != null) {
							sb.append(line + "\n");
						} 
						strApiReturnMsg = sb.toString();
						logger.debug(" goReceiveDeny API request result :  " + strApiReturnMsg);
						br.close();
						try {
							JSONObject objDenyResult = new JSONObject(strApiReturnMsg);
							JSONArray objDenyResultHeader = (JSONArray) objDenyResult.get("HEAD_DATA"); 
							JSONObject denyResultHeader = objDenyResultHeader.getJSONObject(0); 
							if ("S".equals(denyResultHeader.get("RST"))) {
								strReturn = "Success";
								strReturnMessage = denyResultHeader.get("RST_MSG").toString();
							} else {
								strReturn = "Fail";
								strReturnMessage = denyResultHeader.get("RST_MSG").toString();
							}
						} catch (Exception e) {
							strReturn = "Exception";
							strReturnMessage ="시스템 오류가 발생했습니다 다시 시도해주세요";
							logger.error(" goReceiveDeny RST Check Error : " + e.getMessage());
						}
					} else {
						logger.error(" goReceiveDeny HttpURLConnection Fail : " + con.getResponseMessage());
						strReturn ="Fail"; 
						strReturnMessage ="수신거부 요청 서버에 접속할 수 없습니다 다시 시도해주세요";
					}
				} catch (Exception e) { 
					logger.error(" goReceiveDeny Api Request Exception : " + e.getMessage());
					strReturn = "Exception";
					strReturnMessage ="시스템 오류가 발생했습니다 다시 시도해주세요";
				}
				
			} catch (Exception e) {
				strReturn = "Exception";
				strReturnMessage ="시스템 오류가 발생했습니다 다시 시도해주세요";
				logger.error(" goReceiveDeny Exception :" + e.getMessage());
			} 
		} else {
			strReturn = "Fail";
			strReturnMessage ="수신거부 요청 정보가 누락되었습니다(사용자ID)";
		}
		
		logger.error(" goReceiveDeny 결과 :" + strReturn + " / 결과 메시지 : " + strReturnMessage );
		
		String retUrl = ""; 
		
		if("Success".equals(strReturn) ) {
			retUrl = "redirect:/ems/cam/api/receiveDenyS.ums" ;
		} else {
			retUrl = "redirect:/ems/cam/api/receiveDenyF.ums" ;
		} 
			 
		return retUrl;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/receiveDenyTest")
	private String goReceiveDenyTest(@RequestParam String userId , Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("/ems/cam/api/goReceiveDenyTest  userid  = " + userId); 
		  
	 
		String retUrl = "";  
		if("happyjune".equals(userId) ) { 
			retUrl = "redirect:/ems/cam/api/receiveDenyS.ums" ;
		} else {
			retUrl = "redirect:/ems/cam/api/receiveDenyF.ums" ;
		} 
		return retUrl;
	}
	
	/****************************** 발송 상태 요청 API ******************************/
	@RequestMapping(value="/mailWorkStatus")
	private void getMailWorkStatus(@RequestParam  String  requestKey , Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		logger.debug("/ems/cam/api/mailWorkStatus  requestKey  = " + requestKey); 
		
		//요청키 기준으로 현재 정보를 가져와서 넘김
		String strReturn  = null;
		String strReturnMessage = null;
		
		if(requestKey != null  && !"".equals(requestKey)) { 
			// 사용자 아이디, 비밀번호 체크
			try {
				List<TaskVO> mailList = null;
				mailList = campaignService.getMailWorkStatus(requestKey);
				if(mailList != null && mailList.size() > 0) {
					strReturn = mailList.get(0).getWorkStatus();
					strReturnMessage = mailList.get(0).getWorkStatusNm(); 
				} else {
					strReturn = "None";
					strReturnMessage = "발송내역없음 요청키 :[" + requestKey + "]";
				}
			} catch(Exception e) {
				strReturn = "Erorr";
				strReturnMessage = "시스템 오류 발생 요청키 :[" + requestKey + "]";
				logger.error("getMailWorkStatus Error = " + e);
			}
		} else {
			strReturn = "Fail";
			strReturnMessage ="메일 상태 요청 정보가 누락되었습니다(requestKey)";
		}
		
		logger.error(" getMailWorkStatus 결과 :" + strReturn + " / 결과 메시지 : " + strReturnMessage );
		
		try {
			sendResultJson(response, strReturn, strReturnMessage );
		} catch (Exception e) {
			logger.error(" getMailWorkStatus sendResultJson :" + e.getMessage() );
		}
		
	}
	
	public static void sendResultJson(HttpServletResponse response, String sResultCode, String sResultMessage) throws Exception {
		
		PrintWriter writer;
		String returnValue = "{ \"resultCode\":\"%s\", \"resultMessage\":\"%s\" }";
		
		if (sResultCode == null){
			sResultCode = "9999";
		}
		
		response.setContentType("text/plain; charset=UTF-8");
		
		writer = response.getWriter();
		writer.write(String.format(returnValue, sResultCode, sResultMessage));
		
		writer.flush();
		writer.close();
		
	}
	

	/**
	 * 수신권한 처리 
	 * @return
	 */
	@RequestMapping(value="/receiveDenyS")
	public String getReceiveDenySuccess() {
		return "ems/cam/api/receiveDenyS";
	}
	
	/**
	 * 수신권한 처리 
	 * @return
	 */
	@RequestMapping(value="/receiveDenyF")
	public String getReceiveDenyFail() {
		return "ems/cam/api/receiveDenyF";
	} 

}
