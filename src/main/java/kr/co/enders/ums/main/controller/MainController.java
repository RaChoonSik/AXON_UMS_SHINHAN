/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 메인화면 처리
 */
package kr.co.enders.ums.main.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mp.util.Code;

import kr.co.enders.ums.main.service.MainService;
import kr.co.enders.ums.main.vo.EmsMainVO;
import kr.co.enders.ums.sys.acc.vo.ServiceVO;
import kr.co.enders.ums.sys.acc.vo.SysMenuVO;
import kr.co.enders.ums.sys.acc.vo.UserVO;
import kr.co.enders.ums.sys.log.service.SystemLogService;
import kr.co.enders.ums.sys.log.vo.ActionLogVO;
import kr.co.enders.util.EncryptUtil;
import kr.co.enders.util.PageUtil;
import kr.co.enders.util.PropertiesUtil;
import kr.co.enders.util.StringUtil;

@Controller
@RequestMapping(value = "/")
public class MainController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private MainService mainService;

	@Autowired
	private SystemLogService systemService;

	@Autowired
	private PropertiesUtil properties;
	
	/**
	 * 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String goIndex(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goIndex Start.");

		// 로그인 여부 세션확인
		String userId = (String) session.getAttribute("NEO_USER_ID");
		if (userId == null || "".equals(userId)) { // 로그인 화면으로 이동
			String retUrl = getLoginUrl();
			return retUrl;
		} else { 
			return "index";
		}
	}
	
	/**
	 * 인증화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/userAuth")
	public String goUserAuth(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goUserAuth Start.");

		// 로그인 여부 세션확인
		String userId = (String) session.getAttribute("NEO_USER_ID");
		if (userId == null || "".equals(userId)) { // 로그인 화면으로 이동
			return "lgn/lgnP";
		} else { 
			return "userAuth";
		}
	}
	 

	/**
	 * 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/service")
	public String goService(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goService Start.");
	
		UserVO userVO = new UserVO();
		userVO.setUserId((String) session.getAttribute("NEO_USER_ID"));
		userVO.setDeptNo((int) session.getAttribute("NEO_DEPT_NO"));
 
		List<ServiceVO> userServiceList = null;
		ServiceVO userService = new ServiceVO();
		String licenseKey = ""; 
		String decLicenseKey = "";
		String domainKey = ""; 
		try {
			userServiceList = mainService.getUserService(userVO);
			if(userServiceList != null) {
				for(int i = 0 ; i < userServiceList.size() ; i ++) {
					userService = userServiceList.get(i);
					licenseKey  = userService.getLicenseKey();
					decLicenseKey= EncryptUtil.getJasyptDecryptedString(properties.getProperty("JASYPT.ALGORITHM"),
							properties.getProperty("LICENSE.KEYSTRING"), licenseKey);
					 
					domainKey = userService.getCustDomain()  + "+" +  userService.getServiceNm();
					if(!decLicenseKey.substring(0, domainKey.length()).equals(domainKey)) {
						userServiceList.get(i).setPayYn(0);
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
							} else {
								userServiceList.get(i).setPayYn(0);
							}  
						} catch (Exception e) {
							logger.error("mainService.getUserService error[Expire Date illegal] = " + e);
							userServiceList.get(i).setPayYn(0);
						}
					} 
				}
			}
				
		} catch (Exception e) {
			logger.error("mainService.getUserService error[C009] = " + e);
		} 
		
		model.addAttribute("userServiceList", userServiceList);
		
		return "service";
	}
	
	/**
	 * EMS 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/ems/index_20220208")
	public String goEmsMain_20220208(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goEmsMain Start.");

		// 기본 접속 화면 설정(사용자 서비스별 메뉴 권한 확인)
		SysMenuVO menuInfo = null;
		
		try {
			SysMenuVO menu = new SysMenuVO();
			menu.setUserId((String) session.getAttribute("NEO_USER_ID"));
			menu.setDeptNo((int) session.getAttribute("NEO_DEPT_NO"));
			menu.setServiceGb(10);
			menuInfo = mainService.getServiceUserMenu(menu);
		} catch (Exception e) {
			logger.error("mainService.getServiceUserMenu error = " + e);
		}

		if (menuInfo == null) {
			// 기본 접속 메뉴가 없을 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("EMS");
			logVO.setContentPath("EMS");
			insertServiceActionLog(req, session, logVO);

			return "err/access";
		} else {
			// 기본 접속 메뉴가 있는 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("EMS");
			logVO.setContentPath("EMS");
			insertServiceActionLog(req, session, logVO);

			model.addAttribute("menuInfo", menuInfo);
			return "ems/index";
		}
	}
	
	/**
	 * EMS 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/ems/index")
	public String goEmsMain(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goEmsMain Start.");
		
        String strToday = StringUtil.getDate(7);
        String strToMons = StringUtil.getDate(14);
        String strToTimes = StringUtil.getDate(8);
		
		EmsMainVO mainvo = new EmsMainVO();
		mainvo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		mainvo.setSearchYmd(strToday);
		mainvo.setSearchYm(strToMons);
		
		try {
			//사용자 정보
			List<EmsMainVO> userInfo = mainService.getUserInfo(mainvo);	
			//결제 정보
			List<EmsMainVO> userApprov = mainService.getUserApprov(mainvo);
			
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("userApprov", userApprov);
			model.addAttribute("systemTimes", strToTimes);
			
		} catch(Exception e) {
			logger.error("mainService ems main error = " + e);
		}
		
		session.setAttribute("NEO_P_MENU_ID", "M1001000");
		return "ems/indexListP";
	}
	
	/**
	 * EMS 메인화면 메일 분석
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/ems/mainMailAnalyze")
	public ModelAndView mainMailAnalyze(@ModelAttribute EmsMainVO mainvo, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## goEmsMain Start.");
		
		ModelAndView modelAndView = new ModelAndView("jsonView");
		mainvo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		
		String searchPf = mainvo.getSearchPf(); //이전 다음 버튼 이벤트		
		String strToday = StringUtil.getDate(7); //yyyyMMdd
		String strToTimes = StringUtil.getDate(8);
		String parmNow = strToday;
		
		List<EmsMainVO> dayMailSummary = null;
		List<EmsMainVO> dayMailOpen = null;
		List<EmsMainVO> dayMailDetailErr = null;
		List<EmsMainVO> dayMailDomain = null;
		List<EmsMainVO> dayMailCampain = null;
		
		try {
			if("P".equals(searchPf)) {//파라미터 날짜 -1                
				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,8),0,0,-1);
				mainvo.setSearchYmd(parmNow.substring(0,8));
				mainvo.setSearchYm(parmNow.substring(0,6));
			} else if ("F".equals(searchPf)){//파라미터 날짜 + 1
				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,8),0,0,1);
				if(Integer.parseInt(strToday) < Integer.parseInt(parmNow)) {
					parmNow = mainvo.getSearchYmd().toString();
				} 
				mainvo.setSearchYmd(parmNow.substring(0,8));
				mainvo.setSearchYm(parmNow.substring(0,6));
			} else {				
				parmNow = mainvo.getSearchYmd().toString();
				mainvo.setSearchYmd(parmNow.substring(0,8));
				mainvo.setSearchYm(parmNow.substring(0,6));
			}
			
						
			//일별 총발송,발송성공,발송실패
			dayMailSummary = mainService.getDayMailSummary(mainvo);				
			//일별 열람한 메일
			dayMailOpen = mainService.getDayMailOpen(mainvo);				
			//일별 세부에러
			dayMailDetailErr = mainService.getDayMailDetailErr(mainvo);				
			//일별 도메인별
			dayMailDomain = mainService.getDayMailDomain(mainvo);				
			//일별 캠폐인별
			dayMailCampain = mainService.getDayMailCampain(mainvo);
			
			modelAndView.addObject("dayMailSummary", dayMailSummary);
			modelAndView.addObject("dayMailOpen", dayMailOpen);
			modelAndView.addObject("dayMailDetailErr", dayMailDetailErr);
			modelAndView.addObject("dayMailDomain", dayMailDomain);
			modelAndView.addObject("dayMailCampain", dayMailCampain);
			
						
		} catch(Exception e) {
			logger.error("mainService ems main error = " + e);
		}
		
		modelAndView.addObject("systemTimes", strToTimes);
		modelAndView.addObject("serarchTimes", parmNow);
				
		return modelAndView;
	}
	
	/* MySQL 과 MS-SQL에서는 Oracle의 Connect By 사용 못하므로 월별 일력용으로 소스 수정함 기존 함수 백업한 내용임 2022-07-22 junhee*/
	/*@RequestMapping(value = "/ems/mainMailsend")
	public ModelAndView mainMailsend(@ModelAttribute EmsMainVO mainvo, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## goEmsMain Start.");
		
		ModelAndView modelAndView = new ModelAndView("jsonView");
		mainvo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		
		String searchPf = mainvo.getSearchPf(); //이전 다음 버튼 이벤트		
		String strToday = StringUtil.getDate(7); //yyyyMMdd
		String strToTimes = StringUtil.getDate(8);
		String parmNow = strToday;
		
		List<EmsMainVO> mailSendInfoDay = null;
		List<EmsMainVO> mailSendInfoMons = null;
		
        try {
        	
        	if(mainvo.getSearchYmd().toString().length() >  6) { //일간
        		if(searchPf.equals("P")) {//파라미터 날짜 -1                
    				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,8),0,0,-1);
    				mainvo.setSearchYmd(parmNow.substring(0,8));    				
    			} else if (searchPf.equals("F")){//파라미터 날짜 + 1
    				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,8),0,0,1);				 
    				mainvo.setSearchYmd(parmNow.substring(0,8));    				
    			} else {				
    				parmNow = mainvo.getSearchYmd().toString();
    				mainvo.setSearchYmd(parmNow.substring(0,8));    				
    			}
        		
        		//기간별 발송현황 일별 
    			mailSendInfoDay = mainService.getMailSendInfoDay(mainvo);
    			model.addAttribute("mailSendInfoDay", mailSendInfoDay);
        	} else { //월간
        		if(searchPf.equals("P")) {//파라미터 날짜 -1                
    				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,6)+"01",0,-1,0);    				
    				mainvo.setSearchYm(parmNow.substring(0,6));
    			} else if (searchPf.equals("F")){//파라미터 날짜 + 1
    				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,6)+"01",0,1,0);				     				
    				mainvo.setSearchYm(parmNow.substring(0,6));
    			} else {				
    				parmNow = mainvo.getSearchYmd().toString();    				
    				mainvo.setSearchYm(parmNow.substring(0,6));
    			}

        		//기간별 발송현황 월별 
    			mailSendInfoMons = mainService.getMailSendInfoMons(mainvo);
    			model.addAttribute("mailSendInfoMons", mailSendInfoMons);
        	}        				
		} catch(Exception e) {
			logger.error("mainService ems main error = " + e);
		}
		
		modelAndView.addObject("systemTimes", strToTimes);
		modelAndView.addObject("serarchTimes", parmNow);
				
		return modelAndView;
	}
	*/
	
	/**
	 * EMS 메인화면 메일 분석
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	//MySQL 과 MS-SQL에서는 쿼리에서 달력 생성이 안되므로 소스 변경함
	//2022-07-22  junhee
	//!!!!!!!!!!!!Critical Warning!!!!!!!!
	//이전 버전은 위에 주석 소스임 - 광주은행에 적용할때는 해당 함수로 할것!
	//대상되는 MainSQL-ems-oracle.xml의 소스에서도 getMailSendInfoMons 도 이전것으로 주석 풀것!!!
	//!!!!!!!!!!!!Critical Warning!!!!!!!!
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/ems/mainMailsend")
	public ModelAndView mainMailsend(@ModelAttribute EmsMainVO mainvo, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## goEmsMain Start.");
		
		ModelAndView modelAndView = new ModelAndView("jsonView");
		mainvo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		
		String searchPf = mainvo.getSearchPf(); //이전 다음 버튼 이벤트		
		String strToday = StringUtil.getDate(7); //yyyyMMdd
		String strToTimes = StringUtil.getDate(8);
		String parmNow = strToday;
		
		List<EmsMainVO> mailSendInfoDay = null;		
		List<EmsMainVO> orgMailSendInfoMons = null;
		
		Date startDay = new Date(); 
		Date endDay = new Date(); 
		
		int year = 0;
		int month = 0; 
		int day = 0;
		
		String fromYMD = "";
		String toYMD = "";
		String curYmd ="";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		curYmd = transFormat.format(calendar.getTime()).substring(0, 8);

		try {
        	
        	if(mainvo.getSearchYmd().toString().length() >  6) { //일간
        		if(searchPf.equals("P")) {//파라미터 날짜 -1                
    				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,8),0,0,-1);
    				mainvo.setSearchYmd(parmNow.substring(0,8));    				
    			} else if (searchPf.equals("F")){//파라미터 날짜 + 1
    				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,8),0,0,1);				 
    				mainvo.setSearchYmd(parmNow.substring(0,8));    				
    			} else {				
    				parmNow = mainvo.getSearchYmd().toString();
    				mainvo.setSearchYmd(parmNow.substring(0,8));    				
    			}
        		
        		//기간별 발송현황 일별 
    			mailSendInfoDay = mainService.getMailSendInfoDay(mainvo);
    			model.addAttribute("mailSendInfoDay", mailSendInfoDay);
        	} else { //월간
        		if(searchPf.equals("P")) {//파라미터 날짜 -1                
    				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,6)+"01",0,-1,0);    				
    				mainvo.setSearchYm(parmNow.substring(0,6));
    				curYmd = "";
    			} else if (searchPf.equals("F")){//파라미터 날짜 + 1
    				parmNow = AddDate(mainvo.getSearchYmd().toString().substring(0,6)+"01",0,1,0);				     				
    				mainvo.setSearchYm(parmNow.substring(0,6));
    				curYmd = "";
    			} else {				
    				parmNow = mainvo.getSearchYmd().toString();    				
    				mainvo.setSearchYm(parmNow.substring(0,6));
    			}

        		startDay = transFormat.parse(mainvo.getSearchYm() +"01" + " 00:00:00");
				calendar.setTime(startDay);
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH) + 1;
				day = endDay.getDay();
				
				int start = 1; 
				int end =calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				
				fromYMD = Integer.toString(year)+ getIntToZeroString(month, 2) + getIntToZeroString(start, 2) ;
				if("".equals(curYmd)) {
					toYMD  = Integer.toString(year)+ getIntToZeroString(month, 2) + getIntToZeroString(end, 2) ;
				} else {
					toYMD = curYmd;
					//toYMD  = Integer.toString(year)+ getIntToZeroString(month, 2) + getIntToZeroString(day, 2) ;
				}
				
				//월데이터 테이블 만들기
				try {
					startDay = transFormat.parse(fromYMD+ " 00:00:00");
					endDay = transFormat.parse(toYMD + " 00:00:00");
					logger.debug("mainMailSend startDay = " + fromYMD + "/ endDay = " + toYMD);
				} catch (Exception e) {
					logger.error("mainMailsend error = " + e);
				}
				
				long calDateDays = 0 ; 
				try {
					calDateDays = ( endDay.getTime() - startDay.getTime())  / ( 24*60*60*1000);
					calDateDays = Math.abs(calDateDays) + 1;
				} catch (Exception e) {
					logger.error("mainMailsend error = " + e);
				}
				
				List<EmsMainVO> mailSendInfoMons = new ArrayList<EmsMainVO>();
				
				for (int i = 1 ; i <= calDateDays ; i ++ ) {
					EmsMainVO scheduleMainVO = new EmsMainVO();	
					scheduleMainVO.setDays(getIntToZeroString(i, 2));
					scheduleMainVO.setCntTask(0);
					scheduleMainVO.setCntTot(0);
					scheduleMainVO.setCntSucc(0);
					scheduleMainVO.setCntFail(0);
					mailSendInfoMons.add(scheduleMainVO);
				}
				
				//기간별 발송현황 월별
				orgMailSendInfoMons = mainService.getMailSendInfoMons(mainvo);
				
				String days = "" ;
				for(int i = 0; i < mailSendInfoMons.size() ; i++) {
					days = mailSendInfoMons.get(i).getDays();
					for (int j = 0; j < orgMailSendInfoMons.size() ; j++) {
						if(days.equals(orgMailSendInfoMons.get(j).getDays())) {
							mailSendInfoMons.get(i).setCntTask(orgMailSendInfoMons.get(j).getCntTask());
							mailSendInfoMons.get(i).setCntTot(orgMailSendInfoMons.get(j).getCntTot());
							mailSendInfoMons.get(i).setCntSucc(orgMailSendInfoMons.get(j).getCntSucc());
							mailSendInfoMons.get(i).setCntFail(orgMailSendInfoMons.get(j).getCntFail());
						}
					}
				}
				
    			model.addAttribute("mailSendInfoMons", mailSendInfoMons);
        	}        				
		} catch(Exception e) {
			logger.error("mainService ems main error = " + e);
		}
		
		modelAndView.addObject("systemTimes", strToTimes);
		modelAndView.addObject("serarchTimes", parmNow);
				
		return modelAndView;
	}
	
	/**
	 * EMS 메인화면 퀵메뉴
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/ems/mainQuickMenu")
	public ModelAndView mainQuickMenu(@ModelAttribute EmsMainVO mainvo, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## goEmsMain Start.");
		
		ModelAndView modelAndView = new ModelAndView("jsonView");
		mainvo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		mainvo.setDeptNo((int) session.getAttribute("NEO_DEPT_NO"));
		
		HashMap<String,String> menu1 = new HashMap<String,String>();
		HashMap<String,String> menu2 = new HashMap<String,String>();			
		HashMap<String,Object> menu3 = new HashMap<String,Object>();			
		HashMap<String,String> menu4 = new HashMap<String,String>();
		
        try {
		
        	//사용자 메뉴권한			
			List<EmsMainVO> menuList = mainService.getUserMenu(mainvo);
			
			int serviceGb = 0;
								
			for (int idx = 0; idx < menuList.size(); idx++) {
				
				if(menuList.get(idx).getServiceGb() != serviceGb) {
					menu1.put(String.valueOf(menuList.get(idx).getServiceGb()), menuList.get(idx).getCdNm());
				}
				serviceGb = menuList.get(idx).getServiceGb();
				if(menuList.get(idx).getQmenuYn().equals("Y")) {
					menu4.put(menuList.get(idx).getMenuId(), menuList.get(idx).getMenuNm());
				}
			}
			
			List<SysMenuVO> menuListSys = (List<SysMenuVO>) session.getAttribute("NEO_MENU_LIST");
			
			for (int idx = 0; idx < menuListSys.size(); idx++) {
				if(menuListSys.get(idx).getMenulvlVal() == 1) {
					menu2.put(menuListSys.get(idx).getMenuId(), menuListSys.get(idx).getMenuNm());
				}
				
				if(menuListSys.get(idx).getMenulvlVal() == 2) {
					HashMap<String,String> menuInfo = new HashMap<String,String>();
					menuInfo.put(menuListSys.get(idx).getMenuNm(), menuListSys.get(idx).getSourcePath());
					menu3.put(menuListSys.get(idx).getMenuId(), menuInfo);
				}
			}
		
						
		} catch(Exception e) {
			logger.error("mainService quickMenu error = " + e);
		}
		
        modelAndView.addObject("userMenu1", menu1);
		modelAndView.addObject("userMenu2", menu2);
		modelAndView.addObject("userMenu3", menu3);				
		modelAndView.addObject("userQuickMenu", menu4);
		modelAndView.addObject("sessionMenu",session.getAttribute("NEO_MENU_LIST"));
				
		return modelAndView;
	}
	
	
	/**
	 * EMS 메인화면 퀵메뉴 저장
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/ems/insertMainQuickMenu")
	public ModelAndView insertMainQuickMenu(@ModelAttribute EmsMainVO mainvo, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## goEmsMain Start.");
		int result = 0;
		mainvo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		
		String[] arrayMenuId = null ; 
		
		if(mainvo.getQmenuYn() != null && mainvo.getQmenuYn() != "") {
			arrayMenuId = mainvo.getQmenuYn().split(","); //일괄적용 삭제
		}
		mainvo.setArrDelQmenu(arrayMenuId);
		
		try {
			result =  mainService.insertMainQuickMenu(mainvo); 
		} catch (Exception e) {
			result = -1;
			logger.error("mainService.insertMainQuickMenu error = " + e);
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
	 * EMS 메인화면 퀵메뉴 삭제
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/ems/deleteMainQuickMenu")
	public ModelAndView deleteMainQuickMenu(@ModelAttribute EmsMainVO mainvo, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## goEmsMain Start.");
		int result = 0;
		mainvo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		
		try {
			result =  mainService.deleteMainQuickMenu(mainvo); 
		} catch (Exception e) {
			result = -1;
			logger.error("mainService.deleteMainQuickMenu error = " + e);
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
	 * EMS 메인화면 리스트 페이지
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/ems/indexList")
	public String goEmsMainList(@ModelAttribute EmsMainVO mainvo, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("## goEmsMain Start.");
		
		mainvo.setUserId((String) session.getAttribute("NEO_USER_ID"));
		
		SimpleDateFormat nowDay = new SimpleDateFormat("yyyyMMdd");
		
        Calendar c1 = Calendar.getInstance();
        String strToday = nowDay.format(c1.getTime());        
        String searchPf = mainvo.getSearchBoardPf();
        String parmNow = strToday;
        try {
        	
        	if("P".equals(searchPf)) {//파라미터 날짜 -1                
				parmNow = AddDate(mainvo.getSearchBoardDt().toString().substring(0,8),0,0,-1);
				mainvo.setSearchBoardDt(parmNow.substring(0,8));
			} else if ("F".equals(searchPf)){//파라미터 날짜 + 1
				parmNow = AddDate(mainvo.getSearchBoardDt().toString().substring(0,8),0,0,1);
				if(Integer.parseInt(strToday) < Integer.parseInt(parmNow)) {
					parmNow = mainvo.getSearchBoardDt().toString();
				} 
				mainvo.setSearchBoardDt(parmNow.substring(0,8));
			} else {
				if(mainvo.getSearchBoardDt().toString() != "" && mainvo.getSearchBoardDt().toString() != null) {
					parmNow = mainvo.getSearchBoardDt().toString();
				}
				mainvo.setSearchBoardDt(parmNow.substring(0,8));
			}	       
		} catch(Exception e) {
			logger.error("mainService.indexList error = " + e);
		}
        
		int page = StringUtil.setNullToInt(mainvo.getPage(), 1);		//
		int rows = StringUtil.setNullToInt((int)session.getAttribute("NEO_PER_PAGE"), Integer.parseInt(properties.getProperty("LIST.ROW_PER_PAGE")));
		
		mainvo.setPage(page);
		mainvo.setRows(rows);
		int totalCount = 0;
		
		//일별 발송 일정
		List<EmsMainVO> dayMailSendSch = null;
		try {
			dayMailSendSch = mainService.getDayMailSendSch(mainvo);
			model.addAttribute("dayMailSendSch", dayMailSendSch);
			model.addAttribute("searchBoardDt", mainvo.getSearchBoardDt());
			
		} catch(Exception e) {
			logger.error("mainService.indexList error = " + e);
		}
		
		if(dayMailSendSch != null && dayMailSendSch.size() > 0) {
			totalCount = dayMailSendSch.get(0).getTotalCount();
		}
		PageUtil pageUtil = new PageUtil();
		pageUtil.init(request, mainvo.getPage(), totalCount, rows);
		model.addAttribute("pageUtil", pageUtil);			// 페이징
				
		//	<span>메뉴<c:out value="${NEO_P_MENU_ID}"/> </span>
		session.setAttribute("NEO_P_MENU_ID", "M1001000");
		return "ems/indexList";
	}

	/**
	 * RNS 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/rns/index")
	public String goRnsMain(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goRnsMain Start.");

		// 기본 접속 화면 설정(사용자 서비스별 메뉴 권한 확인)
		SysMenuVO menuInfo = null;
		
		try {
			SysMenuVO menu = new SysMenuVO();
			menu.setUserId((String) session.getAttribute("NEO_USER_ID"));
			menu.setDeptNo((int) session.getAttribute("NEO_DEPT_NO"));
			menu.setServiceGb(20);
			menuInfo = mainService.getServiceUserMenu(menu);
		} catch (Exception e) {
			logger.error("mainService.getServiceUserMenu error = " + e);
		}

		if (menuInfo == null) {
			// 기본 접속 메뉴가 없을 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("RNS");
			logVO.setContentPath("RNS");
			insertServiceActionLog(req, session, logVO);

			return "err/access";
		} else {
			// 기본 접속 메뉴가 있는 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("RNS");
			logVO.setContentPath("RNS");
			insertServiceActionLog(req, session, logVO);

			model.addAttribute("menuInfo", menuInfo);
			return "rns/index";
		}
	}

	/**
	 * 공통설정 메인화면
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/sys/index")
	public String goSysMain(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		logger.debug("## goSysMain Start.");

		// 기본 접속 화면 설정(사용자 서비스별 메뉴 권한 확인)
		SysMenuVO menuInfo = null;
		
		try {
			SysMenuVO menu = new SysMenuVO();
			menu.setUserId((String) session.getAttribute("NEO_USER_ID"));
			menu.setDeptNo((int) session.getAttribute("NEO_DEPT_NO"));
			menu.setServiceGb(99);
			menuInfo = mainService.getServiceUserMenu(menu);
		} catch (Exception e) {
			logger.error("mainService.getServiceUserMenu error = " + e);
		}

		if (menuInfo == null) {
			// 기본 접속 메뉴가 없을 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Failure");
			logVO.setContent("SYS");
			logVO.setContentPath("SYS");
			insertServiceActionLog(req, session, logVO);

			return "err/access";
		} else {
			// 기본 접속 메뉴가 있는 경우
			ActionLogVO logVO = new ActionLogVO();
			logVO.setStatusGb("Success");
			logVO.setContent("SYS");
			logVO.setContentPath("SYS");
			insertServiceActionLog(req, session, logVO);

			model.addAttribute("menuInfo", menuInfo);
			return "sys/index";
		}
	}

	public void insertServiceActionLog(HttpServletRequest req, HttpSession session, ActionLogVO actionLogVO) {
		if ("Success".equals(actionLogVO.getStatusGb())) {
			actionLogVO.setContentType("003"); // 003:SERVICE:서비스접근
			actionLogVO.setExtrYn("N");
			actionLogVO.setMobilYn("N");
		} else {
			actionLogVO.setStatusGb("Failure");
			actionLogVO.setContentType("003");
			actionLogVO.setMessage("기본 접속 메뉴가 없음.");
			actionLogVO.setExtrYn("N");
			actionLogVO.setMobilYn("N");
		}

		try {
			systemService.insertActionLog(req, session, actionLogVO);
		} catch (Exception e) {
			logger.error("systemService.insertActionLog error = " + e);
		}
	} 
	
	//날짜 계산 AddDate('20220101',0,0,1)
	private static String AddDate(String strDate, int year, int month, int day) throws Exception { 
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd"); 
		Calendar cal = Calendar.getInstance(); 
		Date dt = dtFormat.parse(strDate); 
		cal.setTime(dt); 
		cal.add(Calendar.YEAR, year); 
		cal.add(Calendar.MONTH, month); 
		cal.add(Calendar.DATE, day); 
		return dtFormat.format(cal.getTime()); 
	}
	
	private String getIntToZeroString(int target, int maxLength) {
		 
		String zeroString = Integer.toString(target);
		if (target < 10 ) { 
			zeroString = String.format("%02d", target);
		}
		return zeroString;
	}
	
	private String getLoginUrl() {
		String captcha =StringUtil.setNullToString(properties.getProperty("CAPTCHA")) ;
		String returnUrl = "lgn/lgnP"; 
		if("Y".equals(captcha)) {
			returnUrl =  "lgn/lgnPCaptcha";
		}
		return returnUrl;
	}
}
