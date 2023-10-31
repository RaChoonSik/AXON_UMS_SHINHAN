package com.forbidden.forbiddata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import kr.co.enders.util.PropertiesUtil;

public class ForbiddenData {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private PropertiesUtil properties; 
	
	@SuppressWarnings("unchecked")
	public synchronized  String getProhibitWordApi(String title, String content) {
		
		String strReturn = "";
		String sendString = "";
		
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		properties = (PropertiesUtil) context.getBean("properties");
		String apiUrl = properties.getProperty("PROHIBIT_CHECK_URL"); //API URL 입력
		String apiTest = properties.getProperty("NETWORK_ENV"); //구동 환경 
		if ("OUT".equals(apiTest)) {
			return getProhibitWordApiTest(title, content );
		}
		
		//API URL 값이 없을때 기본값 리턴 
		if ("".equals(apiUrl)) {
			strReturn = makeProhibitWordEmpty();
		}
		else {
			try {
				JSONObject jsonObject = new JSONObject();
			 
				jsonObject.put("TITLE", title);
				jsonObject.put("MSG", content);
			 
				sendString = jsonObject.toString();
				logger.debug("getProhibitWordApi makeJsonData  :  " + sendString);
				
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
					logger.debug("getProhibitWordApi http Status Code : "+ HttpResult);
					
					if (HttpResult == HttpURLConnection.HTTP_OK) {
						BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
						String line = null;
						while ((line = br.readLine()) != null) {
							sb.append(line + "\n");
						}
						strReturn = sb.toString();
						br.close();
					} else {
						logger.error("getProhibitWordApi : " + con.getResponseMessage());
						strReturn = makeProhibitWordError();
					}
				} catch (Exception e) {
					logger.error("getProhibitWordApi LocalizeMessage:" + e.getLocalizedMessage());
					logger.error("getProhibitWordApi Message:" + e.getMessage());
					strReturn = makeProhibitWordError();
				}
				
			} catch (Exception e) {
				strReturn = makeProhibitWordError();
				e.printStackTrace();
			}
		}
		return strReturn;
	}
	
	
	/**
	 * 금지어 기본 결과값 
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public String makeProhibitWordEmpty() {
		
 		JSONObject jsonObject = null;
		JSONObject jsonResultData = null;
		try {
			jsonResultData = new JSONObject(); 
			jsonResultData.put("RESULTMSG", "");
			jsonResultData.put("RESULTCODE", "");  
			
			jsonObject = new JSONObject();
			jsonObject.put("HEADER", jsonResultData);
			jsonObject.put("BODY", ""); 
		}
		/*
		{ "HEADER": {"RESULTMSG": "밸리데이션 체크 에러., 메시지    지어 - 채용, 성과급, 발견",
			 * "RESULTCODE": "UMS_1002" }, "BODY": {} }
			 * */
		catch (Exception e){
			logger.error("makeProhibitWordEmpty  Error");
		}
		
		return jsonObject.toString();
	}
	
	/**
	 * 금지어 기본 API 실패 결과값 
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public String makeProhibitWordError() {
		
 		JSONObject jsonObject = null;
		JSONObject jsonResultData = null;
		try {
			jsonResultData = new JSONObject(); 
			jsonResultData.put("RESULTMSG", "");
			jsonResultData.put("RESULTCODE", "ERROR");  
			
			jsonObject = new JSONObject();
			jsonObject.put("HEADER", jsonResultData);
			jsonObject.put("BODY", ""); 
		}
		/*
		{ "HEADER": {"RESULTMSG": "밸리데이션 체크 에러., 메시지    지어 - 채용, 성과급, 발견",
			 * "RESULTCODE": "UMS_1002" }, "BODY": {} }
			 * */
		catch (Exception e){
			logger.error("makeProhibitWordError  Error");
		}
		
		return jsonObject.toString();
	}	
	
	/**
	 * 금지어 테스트
	 * @param taskVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public String getProhibitWordApiTest(String title, String content) {
		
 		JSONObject jsonObject = null;
		JSONObject jsonResultData = null;
		try {
			jsonResultData = new JSONObject(); 
			if (title.contains("PRO")) {
				jsonResultData.put("RESULTMSG", "밸리데이션 체크 에러., 메시지    지어 - 채용, 성과급, 발견");
				jsonResultData.put("RESULTCODE", "UMS_1002"); 
			} else {
				jsonResultData.put("RESULTMSG", "금칙어 항목이 없습니다");
				jsonResultData.put("RESULTCODE", "0000");  
			}
			
			jsonObject = new JSONObject();
			jsonObject.put("HEADER", jsonResultData);
			jsonObject.put("BODY", ""); 
		}
		/*
		{ "HEADER": {"RESULTMSG": "밸리데이션 체크 에러., 메시지    지어 - 채용, 성과급, 발견",
			 * "RESULTCODE": "UMS_1002" }, "BODY": {} }
			 * */
		catch (Exception e){
			logger.error("getProhibitWordApiTest  Error");
		}
		
		return jsonObject.toString();
	}
	 
	

}
