/**
 * 작성자 : 김준희
 * 작성일시 : 2023.11.15
 * 설명 : RNS API 서비스 관리 Controller
 */
package kr.co.enders.ums.rns.svc.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.enders.ums.com.service.CodeService;
import kr.co.enders.ums.com.service.CryptoService;
import kr.co.enders.ums.com.service.ForbiddenService;
import kr.co.enders.ums.com.vo.CodeVO;
import kr.co.enders.ums.ems.apr.vo.SecuApprovalLineVO;
import kr.co.enders.ums.ems.cam.service.CampaignService;
import kr.co.enders.ums.ems.cam.vo.ApprovalOrgVO;
import kr.co.enders.ums.ems.cam.vo.TaskVO;
import kr.co.enders.ums.rns.svc.service.RnsServiceService;
import kr.co.enders.ums.rns.svc.vo.ApiRnsRecipientInfoVO;
import kr.co.enders.ums.rns.svc.vo.RnsAttachVO;
import kr.co.enders.ums.rns.svc.vo.RnsMailQueueTestVO;
import kr.co.enders.ums.rns.svc.vo.RnsMailQueueVO;
import kr.co.enders.ums.rns.svc.vo.RnsProhibitWordVO;
import kr.co.enders.ums.rns.svc.vo.RnsRecipientInfoVO;
import kr.co.enders.ums.rns.svc.vo.RnsSecuApprovalLineVO;
import kr.co.enders.ums.rns.svc.vo.RnsServiceVO;
import kr.co.enders.ums.rns.svc.vo.RnsWebAgentVO;
import kr.co.enders.ums.rns.tmp.service.RnsTemplateService;
import kr.co.enders.ums.rns.tmp.vo.RnsTemplateVO;
import kr.co.enders.ums.sys.acc.service.AccountService;
import kr.co.enders.ums.sys.acc.vo.UserVO; 
import kr.co.enders.util.Code; 
import kr.co.enders.util.PageUtil;
import kr.co.enders.util.PropertiesUtil;
import kr.co.enders.util.StringUtil;
import kr.co.enders.util.CrossScriptingFilter;

import com.forbidden.forbiddata.ForbiddenData;


@Controller
@RequestMapping("/rns/svc/api")
public class ApiRnsServiceController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private RnsTemplateService rnsTemplateService;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RnsServiceService rnsServiceService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private PropertiesUtil properties;
	
	
	/**
	 * API : goApiSendTsEmailList 
	 * @param params
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @apiNote This Api for realtime email 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/sendTsEmailList")
	public void goApiSendTsEmailList(@RequestBody Map<String, Object> params, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("===========sendTsEmailList=====================");
		logger.debug("params : " + params);
		
		String sResultmessage ="";
		String sResultcode ="000"; 
		
		JSONObject json = new JSONObject(); 
		String key = "";
		Object value = null;
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			json.put(key, value); 
		}
	
		String requestkey = "";
		String requestoption = "";
		int templatecode = 0;
		
		String memberno = "";
		String membername = "";
		String receiveemail = "";
		String senderid = "";
		String sendername = "";
		String senderemail = "";
		String messagename = "";
		String message = "";
		String attachfile01 = "";
		String attachfile02 = "";
		String attachfile03 = "";
		String attachfile04 = "";
		String attachfile05 = "";
		
		String ctnpos ="";
		String spos = "0"; 
		String rpos = "0";
		int subid = 0;
		
		String status = "0";
		if(json.has("requestkey")) {
			requestkey = StringUtil.setNullToString(json.get("requestkey").toString());
			if("".equals(requestkey)) {
				sResultmessage += "[ 메시지요청키 누락 (requestkey)]";
				sResultcode = "E001";
			} else {
				if(requestkey.contains("_")){
					sResultmessage += "[ 메시지요청키 사용불가문자(_) 포함 (requestkey)]";
					sResultcode = "E011";
				} else {
					if(requestkey.length() >28){
						sResultmessage += "[ 메시지요청키 길이제한(28) 초과 (requestkey)]";
						sResultcode = "E011";
					} else {
						try {
							int requestKeyCount =  rnsServiceService.getCountRequestKey(requestkey);
							if (requestKeyCount > 0 ) {
								sResultmessage = "[ 이미 사용된 키값임 (" + requestkey +")]";
								sResultcode = "E007";
							}
						} catch (Exception e) {
							logger.error("rnsServiceService.getCountRequestKey error = " + e);
							sResultmessage = "[ 사용된 키 조회 오류 (" + requestkey +")]";
							sResultcode = "E006";
						}
					}
				}
			}
		} else {
			sResultcode = "E001";
			sResultmessage += "[ 메시지요청키 누락 (requestkey)]";
		}
		
		if(json.has("requestoption")) {
			requestoption = StringUtil.setNullToString(json.get("requestoption").toString());
			if("".equals(requestoption)) {
				sResultmessage += "[ 요청옵션 누락 (requestoption)]";
				sResultcode = "E001";  
			} else {
				if ("1".equals(requestoption)) {
					ctnpos ="0";
				} else if("2".equals(requestoption)) {
					if(json.has("attachfile01")) {
						attachfile01 = StringUtil.setNullToString(json.get("attachfile01").toString());
						if("".equals(attachfile01)) {
							sResultmessage += "[ 첨부파일 정보 누락 (attachfile01)]";
							sResultcode = "E001";
						} 
					} else {
						sResultmessage += "[ 첨부파일 정보 누락 (attachfile01)]";
						sResultcode = "E001";
					} 
					ctnpos ="0"; 
				} else if ("3".equals(requestoption)) {
					ctnpos ="2";
				} else if ("4".equals(requestoption)) {
					if(json.has("attachfile01")) {
						attachfile01 = StringUtil.setNullToString(json.get("attachfile01").toString());
						if("".equals(attachfile01)) {
							sResultmessage += "[ 첨부파일 정보 누락 (attachfile01)]";
							sResultcode = "E001";
						} 
					} else {
						sResultmessage += "[ 첨부파일 정보 누락 (attachfile01)]";
						sResultcode = "E001";
					} 
					ctnpos ="2";
				} else {
					sResultmessage += "[ 요청옵션 오류 (requestoption)]";
					sResultcode = "E011";  
				}
			}
		} else {
			sResultmessage += "[ 요청옵션 누락 (requestoption)]";
			sResultcode = "E001";  
		}
		
		if(json.has("templatecode")) {
			templatecode = StringUtil.setNullToInt(json.get("templatecode").toString());
			if( templatecode < 1 ) {
				sResultmessage += "[ API캠페인템플릿코드 누락 (templatecode)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ API캠페인템플릿코드 누락 (templatecode)]";
			sResultcode = "E001";  
		}
		
		if(json.has("memberno")) {
			memberno = StringUtil.setNullToString(json.get("memberno").toString());
			if("".equals(memberno)) {
				sResultmessage += "[ 수신자아이디 누락 (memberno)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 수신자아이디 누락 (memberno)]";
			sResultcode = "E001";  
		}
		
		if(json.has("membername")) {
			membername = StringUtil.setNullToString(json.get("membername").toString());
			if("".equals(membername)) {
				sResultmessage += "[ 수신자이름 누락 (membername)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 수신자이름 누락 (membername)]";
			sResultcode = "E001";  
		}
		
		if(json.has("receiveemail")) {
			receiveemail = StringUtil.setNullToString(json.get("receiveemail").toString());
			if("".equals(receiveemail)) {
				sResultmessage += "[ 수신자이메일 누락 (receiveemail)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 수신자이메일 누락 (receiveemail)]";
			sResultcode = "E001";  
		}
		
		if(json.has("senderid")) {
			senderid = StringUtil.setNullToString(json.get("senderid").toString());
			if("".equals(senderid)) {
				sResultmessage += "[ 발신자아이디 누락 (senderid)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 발신자아이디 누락 (senderid)]";
			sResultcode = "E001";  
		}
		
		if(json.has("sendername")) {
			sendername = StringUtil.setNullToString(json.get("sendername").toString());
			if("".equals(sendername)) {
				sResultmessage += "[ 발신자이름 누락 (sendername)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 발신자이름 누락 (sendername)]";
			sResultcode = "E001";  
		}
		
		if(json.has("senderemail")) {
			senderemail = StringUtil.setNullToString(json.get("senderemail").toString());
			if("".equals(senderemail)) {
				sResultmessage += "[ 발신자이메일 누락 (senderemail)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 발신자이메일 누락 (senderemail)]";
			sResultcode = "E001";  
		}
		
		if(json.has("messagename")) {
			messagename = StringUtil.setNullToString(json.get("messagename").toString());
			if("".equals(messagename)) {
				sResultmessage += "[ 메시지 제목 (messagename)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 메시지 제목 (messagename)]";
			sResultcode = "E001";  
		}
		
		if(json.has("senderemail")) {
			senderemail = StringUtil.setNullToString(json.get("senderemail").toString());
			if("".equals(senderemail)) {
				sResultmessage += "[ 발신자이메일 누락 (senderemail)]";
				sResultcode = "E001";  
			} 
		} else {
			sResultmessage += "[ 발신자이메일 누락 (senderemail)]";
			sResultcode = "E001";  
		}
		
		if(json.has("message")) {
			message = StringUtil.setNullToString(json.get("message").toString());
			if("".equals(message)) {
				if("1".equals(requestoption) || "2".equals(requestoption) ) {
					sResultmessage += "[ 메일 내용 누락 (message)]";
					sResultcode = "E001";
				} 
			} 
		} else {
			if("1".equals(requestoption) || "2".equals(requestoption) ) {
				sResultmessage += "[ 메일 내용 누락 (message)]";
				sResultcode = "E001";
			} 
		}
		 
		if ("000".equals(sResultcode)) {
			try {
				// 서비스 정보 조회 By EaiCampNo(templatecode)
				RnsServiceVO templateVO  = rnsServiceService.getServiceInfo(templatecode);
				if (templateVO == null) {
					sResultmessage = "[ API캠페인템플릿 정보 없음 (" + templatecode +")]";
					sResultcode = "E003";
				} else {
					if( templateVO.getTid() != templatecode) {
						sResultmessage = "[ API캠페인템플릿 정보 없음 (" + templatecode +")]";
						sResultcode = "E010";
					} else {
						if("3".equals(requestoption) || "4".equals(requestoption) ) {
							message = templateVO.getContentsPath();
							if(message == null || "".equals(message)) {
								sResultmessage = "[ API캠페인템플릿 정보 없음 (" + templatecode +")]";
								sResultcode = "E012";
							}
						}
					}
				} 
			} catch (Exception e) {
				logger.error("rnsServiceService.getSmsTemplate error = " + e);
				sResultmessage = "[ 서버 데이터 API캠페인템플릿 정보 조회 오류 (" + templatecode +")]";
				sResultcode = "E006";
			}
		}
		
		if (!"000".equals(sResultcode)) {
			try {
				sendApiResultJson(response, requestkey, sResultcode, sResultmessage);
			} catch (Exception e) { 
				logger.error("RnsServiceController.goApiSendTsEmailList Send Return error = " + e);
			} 
		} else { 
			String map1 ="";
			String map2 ="";
			String map3 ="";
			String map4 ="";
			String map5 ="";
			String map6 ="";
			String map7 ="";
			String map8 ="";
			String map9 ="";
			String map10 ="";
			String map11 ="";
			String map12 ="";
			String map13 ="";
			String map14 ="";
			String map15 ="";
			
			if(json.has("map1")) {map1 = StringUtil.setNullToString(json.get("map1").toString());}
			if(json.has("map2")) {map2 = StringUtil.setNullToString(json.get("map2").toString());}
			if(json.has("map3")) {map3 = StringUtil.setNullToString(json.get("map3").toString());}
			if(json.has("map4")) {map4 = StringUtil.setNullToString(json.get("map4").toString());}
			if(json.has("map5")) {map5 = StringUtil.setNullToString(json.get("map5").toString());}
			if(json.has("map6")) {map6 = StringUtil.setNullToString(json.get("map6").toString());}
			if(json.has("map7")) {map7 = StringUtil.setNullToString(json.get("map7").toString());}
			if(json.has("map8")) {map8 = StringUtil.setNullToString(json.get("map8").toString());}
			if(json.has("map9")) {map9 = StringUtil.setNullToString(json.get("map9").toString());}
			if(json.has("map10")) {map10 = StringUtil.setNullToString(json.get("map10").toString());}
			if(json.has("map11")) {map11 = StringUtil.setNullToString(json.get("map11").toString());}
			if(json.has("map12")) {map12 = StringUtil.setNullToString(json.get("map12").toString());}
			if(json.has("map13")) {map13 = StringUtil.setNullToString(json.get("map13").toString());}
			if(json.has("map14")) {map14 = StringUtil.setNullToString(json.get("map14").toString());}
			if(json.has("map15")) {map15 = StringUtil.setNullToString(json.get("map15").toString());}
			
			if(json.has("attachfile01")) {attachfile01 = StringUtil.setNullToString(json.get("attachfile01").toString());}
			if(json.has("attachfile02")) {attachfile02 = StringUtil.setNullToString(json.get("attachfile02").toString());}
			if(json.has("attachfile03")) {attachfile03 = StringUtil.setNullToString(json.get("attachfile03").toString());}
			if(json.has("attachfile04")) {attachfile04 = StringUtil.setNullToString(json.get("attachfile04").toString());}
			if(json.has("attachfile05")) {attachfile05 = StringUtil.setNullToString(json.get("attachfile05").toString());}
			
			//DB 작업 시작 
			ApiRnsRecipientInfoVO apiRnsRecipientInfoVO = new ApiRnsRecipientInfoVO();
			apiRnsRecipientInfoVO.setSubid(subid);
			apiRnsRecipientInfoVO.setTid(templatecode);
			apiRnsRecipientInfoVO.setSpos(spos);
			apiRnsRecipientInfoVO.setSid(senderid);
			apiRnsRecipientInfoVO.setSname(sendername);
			apiRnsRecipientInfoVO.setSmail(senderemail);
			apiRnsRecipientInfoVO.setRpos(rpos);
			apiRnsRecipientInfoVO.setCtnpos(ctnpos);
			apiRnsRecipientInfoVO.setSubject(messagename);
			apiRnsRecipientInfoVO.setContents(message);
			apiRnsRecipientInfoVO.setRequestKey(requestkey);
			apiRnsRecipientInfoVO.setRid(memberno);
			apiRnsRecipientInfoVO.setRname(membername);
			apiRnsRecipientInfoVO.setRmail(receiveemail);
			apiRnsRecipientInfoVO.setMap1(map1);
			apiRnsRecipientInfoVO.setMap2(map2);
			apiRnsRecipientInfoVO.setMap3(map3);
			apiRnsRecipientInfoVO.setMap4(map4);
			apiRnsRecipientInfoVO.setMap5(map5);
			apiRnsRecipientInfoVO.setMap6(map6);
			apiRnsRecipientInfoVO.setMap7(map7);
			apiRnsRecipientInfoVO.setMap8(map8);
			apiRnsRecipientInfoVO.setMap9(map9);
			apiRnsRecipientInfoVO.setMap10(map10);
			apiRnsRecipientInfoVO.setMap11(map11);
			apiRnsRecipientInfoVO.setMap12(map12);
			apiRnsRecipientInfoVO.setMap13(map13);
			apiRnsRecipientInfoVO.setMap14(map14);
			apiRnsRecipientInfoVO.setMap15(map15);
			apiRnsRecipientInfoVO.setAttachfile01(attachfile01);
			apiRnsRecipientInfoVO.setAttachfile02(attachfile02);
			apiRnsRecipientInfoVO.setAttachfile03(attachfile03);
			apiRnsRecipientInfoVO.setAttachfile04(attachfile04);
			apiRnsRecipientInfoVO.setAttachfile05(attachfile05);
			
			
			apiRnsRecipientInfoVO.setRequestKey(requestkey);
			apiRnsRecipientInfoVO.setStatus(status);
			 
			
			try {
				int result = rnsServiceService.insertApiSendTsEmail(apiRnsRecipientInfoVO);
				if (result < 1 ) {
					sResultcode = "E004";
					sResultmessage = "데이터 처리 오류";
				} else {
					sResultcode = "000";
					sResultmessage = "Success";
				}				
			} catch (Exception e) {
				logger.error("rnsServiceService.insertApiSendTsEmail error = " + e);
				sResultcode = "E009"; 
				sResultmessage = "서버에서 예외처리 오류 발생 : " + e.getMessage();
				 
			}
			
			try {
				sendApiResultJson(response, requestkey, sResultcode, sResultmessage);
			} catch (Exception e) {
				logger.error("rnsServiceService.insertApiSendTsEmail  sendApiResultJson error = " + e);
			} 
		}
	}
	
	public static void sendApiResultJson(HttpServletResponse response, String sRequestkey, String sResultcode, String sResultmessage) throws Exception {
		
		PrintWriter writer;
		String returnValue = ""; 
		
		returnValue = "{ \"requestkey\":\"%s\", \"resultcode\":\"%s\", \"resultmessage\":\"%s\"}"; 
		
		if (sResultcode == null){
			sResultcode = "9999";
		}
		if ("000".equals(sResultcode)) {
			sResultmessage ="Success";
		}
		
		response.setContentType("text/plain; charset=UTF-8");
		writer = response.getWriter();
		
		writer.write(String.format(returnValue, sRequestkey, sResultcode, sResultmessage));
		
		writer.flush();
		writer.close();
	}
}
