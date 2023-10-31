/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 도메인 정보 관리
 */
package kr.co.enders.ums.sys.rns.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.enders.ums.com.service.CodeService;
import kr.co.enders.ums.com.vo.CodeVO;

import kr.co.enders.ums.sys.rns.service.RnsStandardService;
import kr.co.enders.ums.sys.rns.vo.DomainInfoVO;
import kr.co.enders.ums.sys.rns.vo.MailClinicInfoVO;
import kr.co.enders.util.Code;
import kr.co.enders.util.PageUtil;
import kr.co.enders.util.DBUtil;
import kr.co.enders.util.EncryptUtil;
import kr.co.enders.util.PropertiesUtil;
import kr.co.enders.util.StringUtil;

@Controller
@RequestMapping("/sys/rns")
public class RnsStandardController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private RnsStandardService rnsStandardService;
	
	@Autowired
	private PropertiesUtil properties; 
	
	/******************************************************** 도메인 관리 ********************************************************/
	/**
	 * 도메인 목록 조회 화면을 출력한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/domainListP")
	public String goDomainListP(@ModelAttribute DomainInfoVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		//searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		searchVO.setPage(page);	 
		 
		
		model.addAttribute("searchVO", searchVO);				// 검색 항목	 
		
		return "sys/rns/domainListP";
	}
	
	/**
	 * 도메인 목록을 출력한다.
	 * @param domainInfoVO
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/domainList")
	public String goDomainList(@ModelAttribute DomainInfoVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDomainList searchDomainName = " + searchVO.getSearchDomainName()); 
		
		searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		//search_map.put("empNoList", ",'1001','1002','1003','1004'"));
		// 페이지 설정
		int page = StringUtil.setNullToInt(searchVO.getPage(), 1);
		int rows = StringUtil.setNullToInt(searchVO.getRows(), (int)session.getAttribute("NEO_PER_PAGE"));
		//int rows = StringUtil.setNullToInt(searchVO.getRows(), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		//int rows = StringUtil.setNullToInt((int)session.getAttribute("NEO_PER_PAGE"), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		
		searchVO.setPage(page);
		searchVO.setRows(rows);
		int totalCount = 0;
		  
		// 로그인 이력 목록 조회 
		List<DomainInfoVO> orgDomainList = new ArrayList<DomainInfoVO>();
		List<DomainInfoVO> domainList = new ArrayList<DomainInfoVO>();
		try {
			orgDomainList = rnsStandardService.getDomainList(searchVO);
		} catch(Exception e) {
			logger.error("rnsStandardService.getDomainList error = " + e);
		}
		// 등록일시 포멧 수정
		if(orgDomainList != null) {
			for(DomainInfoVO nDomainVO:orgDomainList) {				
				nDomainVO.setRegDt(StringUtil.getFDate(nDomainVO.getRegDt().substring(0, 14), Code.DT_FMT2)); 
				domainList.add(nDomainVO);
			}
		}
		
		// 코드그룹목록(코드성) 조회 -- 개인별페이지
		CodeVO perPage = new CodeVO();
		perPage.setUilang(searchVO.getUilang());
		perPage.setCdGrp("C126");
		perPage.setUseYn("Y");
		List<CodeVO> perPageList = null;
		try {
			perPageList = codeService.getCodeList(perPage);
		} catch (Exception e) {
			logger.error("codeService.getCodeList[126] error = " + e);
		}
		
		if(domainList != null && domainList.size() > 0) {
			totalCount = domainList.get(0).getTotalCount();
		}
		
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, searchVO.getPage(), totalCount, rows);
		
		model.addAttribute("domainList", domainList);	// 사용자활동이력 목록
		model.addAttribute("pageUtil", pageUtil);	// 페이징
		model.addAttribute("perPageList", perPageList);	//개인별페이지
		model.addAttribute("searchVO", searchVO);
		
		return "sys/rns/domainList";
	}
	 
	@RequestMapping(value="/domainAddP")
	public String goDomainAddP(@ModelAttribute DomainInfoVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		model.addAttribute("searchVO", searchVO);// 검색 항목	 
		
		return "sys/rns/domainAddP";
	}
	
	@RequestMapping(value="/domainUpdateP")
	public String goDomainUpdateP(@ModelAttribute DomainInfoVO searchVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goDomainUpdateP getDomainId = " + searchVO.getDomainId());
		 
		// 로그인 이력 목록 조회 
		DomainInfoVO domainInfo = new DomainInfoVO();
		try {
			domainInfo = rnsStandardService.getDomainInfo(searchVO);
		} catch(Exception e) {
			logger.error("rnsStandardService.getDomainInfo error = " + e);
		}
		// 등록일시 포멧 수정
		if(domainInfo != null) {
			domainInfo.setRegDt(StringUtil.getFDate(domainInfo.getRegDt().substring(0, 14), Code.DT_FMT2)); 	
		}
						
		model.addAttribute("searchVO", searchVO);// 검색 항목
		model.addAttribute("domainInfo", domainInfo);	
		
		return "sys/rns/domainUpdateP";
	}
	
	/**
	 * 도메인 신규 정보를 등록한다.
	 * 
	 * @param domainInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/domainAdd")
	public ModelAndView insertDomainInfo(@ModelAttribute DomainInfoVO domainInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateDomainInfo domainName= " + domainInfoVO.getDomainName()); 
 
		domainInfoVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		domainInfoVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
				
		int result = 0;
		try {
			result = rnsStandardService.insertDomainInfo(domainInfoVO);
		} catch (Exception e) {
			logger.error("rnsStandardService.insertDinsertDomainInfoeptInfo error = " + e);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 도메인 정보를 수정한다.
	 * 
	 * @param domainInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session @return/
	 */
	@RequestMapping(value = "/domainUpdate")
	public ModelAndView updateDomainInfo(@ModelAttribute DomainInfoVO domainInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("updateDomainInfo domainId= " + domainInfoVO.getDomainId()); 
		logger.debug("updateDomainInfo domainName= " + domainInfoVO.getDomainName()); 
  
		domainInfoVO.setUpId((String) session.getAttribute("NEO_USER_ID"));
		domainInfoVO.setUpDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		int result = 0;
		try {
			result = rnsStandardService.updateDomainInfo(domainInfoVO);
		} catch (Exception e) {
			logger.error("rnsStandardService.updateDomainInfo error = " + e);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}

	/**
	 * 도메인 정보를 삭제한다
	 * 
	 * @param domainInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/domainDelete")
	public ModelAndView deleteDeptInfo(@ModelAttribute DomainInfoVO domainInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("deleteDeptInfo getDomainIds      = " + domainInfoVO.getDomainIds());
 
		String[] domainIds= domainInfoVO.getDomainIds().split(",");
		int result = 0;
		
		if (domainIds.length > 0) {
			int[] arrDomainId = new int[domainIds.length];
		 

			for (int i = 0; i < domainIds.length; i++) {
				if( !"".equals(domainIds[i])){
					arrDomainId[i] = Integer.parseInt(domainIds[i]);
				}
			}

			domainInfoVO.setArrDomainId(arrDomainId);

			try {
				result = rnsStandardService.deleteDomainInfo(domainInfoVO); 
			} catch (Exception e) {
				result = -1;
				logger.error("rnsStandardService.deleteDomainInfo error = " + e);
			}
		} else {
			result = -1;
		}
 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}	
	
	/**
	 * 메일클리닉 일정 관리 화면을 출력한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/mailClinicListP")
	public String mailClinicListP(@ModelAttribute MailClinicInfoVO mailClinicInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		//searchVO.setUilang((String)session.getAttribute("NEO_UILANG"));
		
		// 페이지 설정
		int page = StringUtil.setNullToInt(mailClinicInfoVO.getPage(), 1);
		mailClinicInfoVO.setPage(page);	 
		 
		
		model.addAttribute("searchVO", mailClinicInfoVO);				// 검색 항목	 
		
		return "sys/rns/mailClinicListP";
	}
	
	/**
	 * 메일클리닉 일정관리 삭제
	 * 
	 * @param mailClinicInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/mailClinicDelete")
	public ModelAndView mailClinicDelete(@ModelAttribute MailClinicInfoVO mailClinicInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		int result = 0;
		
		String clsM = mailClinicInfoVO.getClsM();
		mailClinicInfoVO.setClsYm(mailClinicInfoVO.getClsY() + StringUtil.setTwoDigitsString(clsM));
				
		try {
			result = rnsStandardService.deleteMailClinic(mailClinicInfoVO); 
		} catch (Exception e) {
			result = -1;
			logger.error("rnsStandardService.deleteMailClinic error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
	
	/**
	 * 메일클리닉 일정관리 수정
	 * 
	 * @param mailClinicInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/mailClinicUpdate")
	public ModelAndView mailClinicUpdate(@ModelAttribute MailClinicInfoVO mailClinicInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		mailClinicInfoVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		mailClinicInfoVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		String clsM = mailClinicInfoVO.getClsM();
		mailClinicInfoVO.setClsYm(mailClinicInfoVO.getClsY() + StringUtil.setTwoDigitsString(clsM));
				
		int result = 0;
		
		try {
			result = rnsStandardService.updateMailClinic(mailClinicInfoVO); 
		} catch (Exception e) {
			result = -1;
			logger.error("rnsStandardService.mailClinicUpdate error = " + e);
		}
		
		 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}			
	
	
	/**
	 * 메일클리닉 일정관리 조회
	 * 
	 * @param mailClinicInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/mailClinicSearch")
	public ModelAndView mailClinicSearch(@ModelAttribute MailClinicInfoVO mailClinicInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		List<MailClinicInfoVO> mailClinicList = new ArrayList<MailClinicInfoVO>();
		
		try {
			mailClinicList = rnsStandardService.getMailClinicList(mailClinicInfoVO);
		} catch(Exception e) {
			logger.error("rnsStandardService.getMailClinicList error = " + e);
		}
		
		ModelAndView modelAndView = new ModelAndView("jsonView");
		modelAndView.addObject("mailClinicList",mailClinicList);
		
		return modelAndView;
	}	
	
	/**
	 * 메일클리닉 일정관리 일괄수정
	 * 
	 * @param mailClinicInfoVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/mailClinicAllUpdate")
	public ModelAndView mailClinicAllUpdate(@ModelAttribute MailClinicInfoVO mailClinicInfoVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		mailClinicInfoVO.setRegId((String) session.getAttribute("NEO_USER_ID"));
		mailClinicInfoVO.setRegDt(StringUtil.getDate(Code.TM_YMDHMS));
		
		String[] arrayDelMonth = mailClinicInfoVO.getDelMonth().split(","); //일괄적용 삭제
		
		String clsM = mailClinicInfoVO.getClsM();
		mailClinicInfoVO.setClsYm(mailClinicInfoVO.getClsY() + StringUtil.setTwoDigitsString(clsM));
		
			
		//일괄 적용시 삭제할 월
		for(int j =0 ; j < arrayDelMonth.length ; j++) {
			if(arrayDelMonth[j] != "" && arrayDelMonth[j] != null ) {
				arrayDelMonth[j] = StringUtil.setTwoDigitsString(arrayDelMonth[j]);
			}
		}
		
		int result = 0;
		
		mailClinicInfoVO.setArrDelMonth(arrayDelMonth);
	
		try {
			result = rnsStandardService.updateAllMailClinic(mailClinicInfoVO); 
		} catch (Exception e) {
			result = -1;
			logger.error("rnsStandardService.mailClinicUpdate error = " + e);
		}
		 
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (result > 0) {
			map.put("result", "Success");
		} else {
			map.put("result", "Fail");
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);

		return modelAndView;
	}
}
