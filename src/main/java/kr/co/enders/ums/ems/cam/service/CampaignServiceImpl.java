/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.22
 * 설명 : 캠페인관리 서비스 구현
 */
package kr.co.enders.ums.ems.cam.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.enders.ums.ems.ana.vo.SendLogVO;
import kr.co.enders.ums.ems.apr.vo.SecuApprovalLineVO;
import kr.co.enders.ums.ems.cam.dao.CampaignDAO;
import kr.co.enders.ums.ems.cam.vo.ApprovalOrgVO;
import kr.co.enders.ums.ems.cam.vo.AttachVO;
import kr.co.enders.ums.ems.cam.vo.CampaignTemplateAttachVO;
import kr.co.enders.ums.ems.cam.vo.CampaignTemplateProhibitWordVO;
import kr.co.enders.ums.ems.cam.vo.CampaignTemplateVO;
import kr.co.enders.ums.ems.cam.vo.CampaignTemplateWebAgentVO;
import kr.co.enders.ums.ems.cam.vo.CampaignVO;
import kr.co.enders.ums.ems.cam.vo.EmailClinicVO;
import kr.co.enders.ums.ems.cam.vo.LinkVO;
import kr.co.enders.ums.ems.cam.vo.MailMktChkVO;
import kr.co.enders.ums.ems.cam.vo.ProhibitWordVO;
import kr.co.enders.ums.ems.cam.vo.SendTestLogVO;
import kr.co.enders.ums.ems.cam.vo.TaskVO;
import kr.co.enders.ums.ems.cam.vo.TestEaiUserVO;
import kr.co.enders.ums.ems.cam.vo.TestUserVO;
import kr.co.enders.ums.ems.cam.vo.WebAgentVO;
import kr.co.enders.ums.sys.acc.vo.UserVO;
import kr.co.enders.util.Code;
import kr.co.enders.util.PropertiesUtil;
import kr.co.enders.util.StringUtil;

@Service
public class CampaignServiceImpl implements CampaignService {
	@Autowired
	private CampaignDAO campaignDAO;

	@Override
	public List<CampaignVO> getCampaignList(CampaignVO campaignVO) throws Exception {
		return campaignDAO.getCampaignList(campaignVO);
	}
	
	@Override
	public List<CampaignVO> getCampaignRnsList(CampaignVO campaignVO) throws Exception {
		/*
		if ("10".equals(campaignVO.getSearchServiceGb())) {
			return campaignDAO.getCampaignEmsOnlyList(campaignVO);
		} else if ("20".equals(campaignVO.getSearchServiceGb())) {
			return campaignDAO.getCampaignRnsOnlyList(campaignVO);
		} else {
			return campaignDAO.getCampaignRnsList(campaignVO);
		}*/
		return campaignDAO.getCampaignEmsOnlyList(campaignVO);
	}
	
	@Override
	public CampaignVO getCampaignInfo(CampaignVO campaignVO) throws Exception {
		return campaignDAO.getCampaignInfo(campaignVO);
	}

	@Override
	public int insertCampaignInfo(CampaignVO campaignVO) throws Exception {
		return campaignDAO.insertCampaignInfo(campaignVO);
	}

	@Override
	public int updateCampaignInfo(CampaignVO campaingVO) throws Exception {
		return campaignDAO.updateCampaignInfo(campaingVO);
	}

	
	public List<CampaignTemplateVO> getCampaignTemplateList(CampaignTemplateVO campaignTemplateVO) throws Exception {
		return campaignDAO.getCampaignTemplateList(campaignTemplateVO);
	}
	
	@Override
	public CampaignTemplateVO getCampaignTemplateInfo(int tid) throws Exception{
		return campaignDAO.getCampaignTemplateInfo(tid);
	}
	
	@Override
	public int getCurrCampaignTemplateTid() throws Exception{
		return campaignDAO.getCurrCampaignTemplateTid();
	}
	
	@Override
	public List<CampaignTemplateAttachVO> getCampaignTemplateAttachList(int tid) throws Exception{
		return campaignDAO.getCampaignTemplateAttachList(tid);
	}
	
	@Override
	public CampaignTemplateWebAgentVO getCampaignTemplateWebAgentInfo(int tid) throws Exception {
		return campaignDAO.getCampaignTemplateWebAgentInfo(tid);
	}
	
	@Override
	public CampaignTemplateVO getCampaignTemplateEaiCampNo(String eaiCampNo) throws Exception {
		return campaignDAO.getCampaignTemplateEaiCampNo(eaiCampNo);
	}
	
	@Override
	public List<CampaignTemplateProhibitWordVO> getCampaignTemplateProhibitWordList(int tid) throws Exception {
		return campaignDAO.getCampaignTemplateProhibitWordList(tid);
	}
	
	@Override
	public int insertCampaignTemplateInfo(CampaignTemplateVO campaignTemplateVO, List<CampaignTemplateAttachVO> attachList) throws Exception{
		int result = 0;
		
		// 서비스 정보 등록
		result += campaignDAO.insertCampaignTemplateInfo(campaignTemplateVO);
		
		// 서비스ID 조회
		//MySql, Oracle
		int tid = campaignDAO.getCurrCampaignTemplateTid();
		
		//MsSql
		//int tid = rnsAutoSendVO.getTid();
		
		// 첨부파일 등록
		if(attachList != null && attachList.size() > 0) {
			for(CampaignTemplateAttachVO attach:attachList) {
				attach.setTid(tid);
				attach.setRegId(campaignTemplateVO.getRegId());
				attach.setRegDt(campaignTemplateVO.getRegDt());
				
				result += campaignDAO.insertCampaignTemplateAttachInfo(attach);
			}
		}
		
		// 웹에이전트 둥록
		if(!StringUtil.isNull(campaignTemplateVO.getWebAgentAttachYn())) {
			CampaignTemplateWebAgentVO webAgent = new CampaignTemplateWebAgentVO();
			webAgent.setTid(tid);
			webAgent.setAttNo(0);
			webAgent.setSourceUrl(campaignTemplateVO.getWebAgentUrl());
			webAgent.setSecuAttTyp(campaignTemplateVO.getSecuAttTyp());
			webAgent.setSecuAttYn(campaignTemplateVO.getWebAgentAttachYn());
			webAgent.setRegId(campaignTemplateVO.getRegId());
			webAgent.setRegDt(campaignTemplateVO.getRegDt());
			
			result += campaignDAO.insertCampaignTemplateWebAgent(webAgent);
		}
		 
		// 준법 감시 정보가 있을 경우 
		/*
		if( "002".equals(campaignTemplateVO.getProhibitChkTyp())){ 
			CampaignTemplateProhibitWordVO proTitleWord = new CampaignTemplateProhibitWordVO();
			proTitleWord.setTid(tid);
			proTitleWord.setRegId(campaignTemplateVO.getRegId());
			proTitleWord.setRegDt(campaignTemplateVO.getRegDt());
			if (campaignTemplateVO.getProhibitTitleCnt() > 0 ){
				proTitleWord.setContentTyp("000"); 
				proTitleWord.setProhibitCnt(campaignTemplateVO.getProhibitTitleCnt());
				JSONParser parser = new JSONParser();
				Object obj = parser.parse( campaignTemplateVO.getProhibitTitleDesc() );
				JSONObject jsonObj = (JSONObject) obj;
				proTitleWord.setProhibitDesc(jsonObj);
				proTitleWord.setRegId(campaignTemplateVO.getRegId());
				proTitleWord.setRegDt(campaignTemplateVO.getRegDt());
				result += campaignDAO.insertCampaignTemplateProhibitWord(proTitleWord);
			}
			if (campaignTemplateVO.getProhibitTextCnt() > 0 ){
				proTitleWord.setContentTyp("001");
				proTitleWord.setProhibitCnt(campaignTemplateVO.getProhibitTextCnt());
				JSONParser parser = new JSONParser();
				Object obj = parser.parse( campaignTemplateVO.getProhibitTextDesc() );
				JSONObject jsonObj = (JSONObject) obj;
				proTitleWord.setProhibitDesc(jsonObj);
				proTitleWord.setRegId(campaignTemplateVO.getRegId());
				proTitleWord.setRegDt(campaignTemplateVO.getRegDt());
				result += campaignDAO.insertCampaignTemplateProhibitWord(proTitleWord);
			}
		}*/
		return tid;
	}
	
	@Override
	public int updateCampaignTemplateInfo(CampaignTemplateVO campaignTemplateVO, List<CampaignTemplateAttachVO> attachList) throws Exception{
		int result = 0;
		
		// 서비스 정보 수정
		result += campaignDAO.updateCampaignTemplateInfo(campaignTemplateVO);
		
		// 기존 첨부파일 삭제
		result += campaignDAO.deleteCampaignTemplateAttachInfo(campaignTemplateVO.getTid());
		// 첨부파일 등록
		if(attachList != null && attachList.size() > 0) {
			for(CampaignTemplateAttachVO attach:attachList) {
				attach.setRegId(campaignTemplateVO.getUpId());
				attach.setRegDt(campaignTemplateVO.getUpDt());
				
				result += campaignDAO.insertCampaignTemplateAttachInfo(attach);
			}
		}
		
		// 기존 웹에이전트 정보 삭제
		result += campaignDAO.deleteCampaignTemplateWebAgent(campaignTemplateVO.getTid());
		// 웹에이전트 둥록
		if(!StringUtil.isNull(campaignTemplateVO.getWebAgentAttachYn())) {
			CampaignTemplateWebAgentVO webAgent = new CampaignTemplateWebAgentVO();
			webAgent.setTid(campaignTemplateVO.getTid());
			webAgent.setAttNo(0);
			webAgent.setSourceUrl(campaignTemplateVO.getWebAgentUrl());
			webAgent.setSecuAttTyp(campaignTemplateVO.getSecuAttTyp());
			webAgent.setSecuAttYn(campaignTemplateVO.getWebAgentAttachYn());
			webAgent.setRegId(campaignTemplateVO.getUpId());
			webAgent.setRegDt(campaignTemplateVO.getUpDt());
			
			result += campaignDAO.insertCampaignTemplateWebAgent(webAgent);
		} 
		 
		// 준법 감시 정보가 있을 경우 
		/* 
		result += campaignDAO.deleteCampaignTemplateProhibitWord(campaignTemplateVO.getTid());
		
		if( "002".equals(campaignTemplateVO.getProhibitChkTyp())){
			if("002".equals(campaignTemplateVO.getProhibitChkTyp())){
				CampaignTemplateProhibitWordVO proTitleWord = new CampaignTemplateProhibitWordVO();
				proTitleWord.setTid(campaignTemplateVO.getTid());
				proTitleWord.setRegId(campaignTemplateVO.getUpId());
				proTitleWord.setRegDt(campaignTemplateVO.getUpDt());
				if (campaignTemplateVO.getProhibitTitleCnt() > 0 ){
					proTitleWord.setContentTyp("000"); 
					proTitleWord.setProhibitCnt(campaignTemplateVO.getProhibitTitleCnt());
					JSONParser parser = new JSONParser();
					Object obj = parser.parse( campaignTemplateVO.getProhibitTitleDesc() );
					JSONObject jsonObj = (JSONObject) obj;
					proTitleWord.setProhibitDesc(jsonObj);
					
					result += campaignDAO.insertCampaignTemplateProhibitWord(proTitleWord);
				}
				if (campaignTemplateVO.getProhibitTextCnt() > 0 ){
					proTitleWord.setContentTyp("001");
					proTitleWord.setProhibitCnt(campaignTemplateVO.getProhibitTextCnt());
					JSONParser parser = new JSONParser();
					Object obj = parser.parse( campaignTemplateVO.getProhibitTextDesc() );
					JSONObject jsonObj = (JSONObject) obj;
					proTitleWord.setProhibitDesc(jsonObj);
					
					result += campaignDAO.insertCampaignTemplateProhibitWord(proTitleWord);
				}
			}
		}  */
		return result;
	}
	
	@Override
	public int updateCampaignTemplatePartInfo(CampaignTemplateVO campaignTemplateVO) throws Exception{
		return campaignDAO.updateCampaignTemplatePartInfo(campaignTemplateVO);
	}

	@Override
	public int insertCampaignTemplateWebAgent(CampaignTemplateWebAgentVO campaignTemplateWebAgentVO) throws Exception{
		return campaignDAO.insertCampaignTemplateWebAgent(campaignTemplateWebAgentVO);
	}
	
	@Override
	public int insertCampaignTemplateAttachInfo(CampaignTemplateAttachVO campaignTemplateAttachVO) throws Exception{
		return campaignDAO.insertCampaignTemplateAttachInfo(campaignTemplateAttachVO);
	}
	
	@Override
	public int updateCampaignTemplateStatus(CampaignTemplateVO campaignTemplateVO) throws Exception{
		int result = 0;
		if(!StringUtil.isNull(campaignTemplateVO.getTids())) {
			String[] tid = campaignTemplateVO.getTids().split(",");
			for(int i=0;i<tid.length;i++) {
				CampaignTemplateVO updCampaignTemplateVO = new CampaignTemplateVO();
				updCampaignTemplateVO.setTid(Integer.parseInt(tid[i]));
				updCampaignTemplateVO.setStatus(campaignTemplateVO.getStatus());
				updCampaignTemplateVO.setUpId(campaignTemplateVO.getUpId());
				updCampaignTemplateVO.setUpDt(campaignTemplateVO.getUpDt());
				
				result += campaignDAO.updateCampaignTemplateStatus(updCampaignTemplateVO);
			}
		}
		
		return result;
	}
	
	@Override
	public int copyCampTemplateInfo(CampaignTemplateVO campaignTemplateVO, PropertiesUtil properties) throws Exception {
		int result = 0;
		
		// 서비스 정보 조회
		CampaignTemplateVO campTempCopy = campaignDAO.getCampaignTemplateInfo(campaignTemplateVO.getTid());
		
		// Content 파일 복사
		if("1".equals(campTempCopy.getContentsTyp().trim())) {
			campTempCopy.setContentsPath(null);
		} else {
			String currDtm = StringUtil.getDate(Code.TM_YMDHMSM);
			String oldFullPath = campTempCopy.getContentsPath();
			String newFullPath = campTempCopy.getContentsPath().substring(0, campTempCopy.getContentsPath().lastIndexOf("/")+1) + currDtm + ".tmp";
			
			File oldFile = new File(oldFullPath);
			File newFile = new File(newFullPath);
			
			FileInputStream input = new FileInputStream(oldFile);
			FileOutputStream output = new FileOutputStream(newFile);
			
			byte[] buff = new byte[1024];
			int read;
			while((read = input.read(buff)) > 0) {
				output.write(buff, 0, read);
				output.flush();
			}
			input.close();
			output.close();

			campTempCopy.setContentsPath(newFullPath);
		}

		// 복사한 서비스 값 설정
		campTempCopy.setDeptNo(campaignTemplateVO.getDeptNo());
		campTempCopy.setUserId(campaignTemplateVO.getUserId());
		campTempCopy.setRegId(campaignTemplateVO.getRegId());
		campTempCopy.setRegDt(campaignTemplateVO.getRegDt());
		campTempCopy.setEaiCampNo("");
		campTempCopy.setStatus("000");
		campTempCopy.setWorkStatus("201");
		campTempCopy.setTnm(campTempCopy.getTnm() + (campTempCopy.getTnm().indexOf(" - [copy]")> 0 ? "": " - [copy]") );
		
		// 캠페인템플릿 등록
		result += campaignDAO.insertCampaignTemplateInfo(campTempCopy);
		
		// 신규 등록 서비스ID 조회
		int tid = campaignDAO.getCurrCampaignTemplateTid();
		
		// 첨부파일 목록 읽기
		List<CampaignTemplateAttachVO> attachList = campaignDAO.getCampaignTemplateAttachList(campaignTemplateVO.getTid());
		if(attachList != null && attachList.size() > 0) {
			for(CampaignTemplateAttachVO attach : attachList) {
				
				// 첨부파일 복사
				String newAttachPath = "attach/" + System.currentTimeMillis() + "_" + attach.getAttNm().replaceAll(" ", "_");
				String oldFullAttachPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + attach.getAttFlPath();
				String newFullAttachPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + newAttachPath;
				
				File oldAttachFile = new File(oldFullAttachPath);
				File newAttachFile = new File(newFullAttachPath);
				
				FileInputStream inputAttach = new FileInputStream(oldAttachFile);
				FileOutputStream outputAttach = new FileOutputStream(newAttachFile);
				
				byte[] buff = new byte[1024];
				int read;
				while((read = inputAttach.read(buff)) > 0) {
					outputAttach.write(buff, 0, read);
					outputAttach.flush();
				}
				inputAttach.close();
				outputAttach.close();
				
				// 복사한 첨부파일 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
				attach.setTid(tid);
				attach.setAttFlPath(newAttachPath);
				attach.setRegId(campaignTemplateVO.getRegId());
				attach.setRegDt(campaignTemplateVO.getRegDt());
				
				result += campaignDAO.insertCampaignTemplateAttachInfo(attach);
			}
		}
		
		// WebAgent 읽기
		CampaignTemplateWebAgentVO webAgent = campaignDAO.getCampaignTemplateWebAgentInfo(campaignTemplateVO.getTid());
		if(webAgent != null) {
			webAgent.setTid(tid);
			webAgent.setRegId(campaignTemplateVO.getRegId());
			webAgent.setRegDt(campaignTemplateVO.getRegDt());
			
			result += campaignDAO.insertCampaignTemplateWebAgent(webAgent);
		}
		
		return result;
	}
	
	@Override
	public List<TaskVO> getMailList(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailList(taskVO);
	}

	@Override
	public int updateMailAdmit(TaskVO taskVO) throws Exception {
		int result = 0;
		
		// 캠페인 주업무 승인
		result += campaignDAO.updateTaskStatusAdmit(taskVO);
		
		// 캠페인 보조업무 승인
		result += campaignDAO.updateSubTaskStatusAdmit(taskVO);
		
		return result;
	}

	@Override
	public int insertMailInfo(TaskVO taskVO, List<AttachVO> attachList, List<LinkVO> linkList) throws Exception {
		int result = 0;
		
		// 메일 주업무 등록
		result += campaignDAO.insertTaskInfo(taskVO);
		
		// 메일 보조업무 등록
		//MySql, Oracle
		int taskNo = campaignDAO.getTaskNo();
		//MsSql
		//int taskNo = taskVO.getTaskNo();
 
		int subTaskNo = campaignDAO.getSubTaskNo(taskNo);
		taskVO.setTaskNo(taskNo);
		taskVO.setSubTaskNo(subTaskNo);
		result += campaignDAO.insertSubTaskInfo(taskVO);
		
		// 첨부파일 등록
		if(attachList != null && attachList.size() > 0) {
			for(AttachVO attach:attachList) {
				attach.setTaskNo(taskNo);
				result += campaignDAO.insertAttachInfo(attach);
			}
		}
		
		// 링크정보 등록
		if(linkList != null && linkList.size() > 0) {
			for(LinkVO link:linkList) {
				result += campaignDAO.insertLinkInfo(link);
			}
		}
		
		// 웹에이전트 정보 등록
		if(!StringUtil.isNull(taskVO.getWebAgentUrl())) {
			WebAgentVO webAgent = new WebAgentVO();
			webAgent.setTaskNo(taskNo);
			webAgent.setAttNo(1);
			webAgent.setSourceUrl(taskVO.getWebAgentUrl());
			webAgent.setSecuAttYn(taskVO.getWebAgentAttachYn());
			webAgent.setSecuAttTyp(taskVO.getSecuAttTyp());
			webAgent.setRegId(taskVO.getRegId());
			webAgent.setRegDt(taskVO.getRegDt());
			
			result += campaignDAO.insertWebAgent(webAgent);
		}
		
		// 발송결재라인정보 등록
		if(!StringUtil.isNull(taskVO.getApprUserId())) {
			String[] apprUserStr = taskVO.getApprUserId().split(",");
			for(int i=0;i<apprUserStr.length;i++) {
				//appUserId => userId|orgCd|posGb|jobGb
				String[] userInfo = apprUserStr[i].split("\\|");
				SecuApprovalLineVO apprLine = new SecuApprovalLineVO();
				for(int j=0;j<userInfo.length;j++) {
					apprLine.setTaskNo(taskNo);
					apprLine.setApprStep(i+1);
					apprLine.setApprUserId(userInfo[0]);
					apprLine.setOrgCd(userInfo[1]);
					apprLine.setPositionGb(userInfo[2]);
					apprLine.setJobGb(userInfo[3]);
					apprLine.setRsltCd("000");
					apprLine.setComplianceYn("N");
					apprLine.setRegId(taskVO.getRegId());
					apprLine.setRegDt(taskVO.getRegDt());
				}
				
				result += campaignDAO.insertApprovalLine(apprLine);
			}
		}
		
		// 준법 감시 정보가 있을 경우 
		if( "002".equals(taskVO.getProhibitChkTyp())){
			if(!StringUtil.isNull(taskVO.getProhibitUserId())) {
				String[] prohibitUserStr = taskVO.getProhibitUserId().split(",");
				for(int i=0;i<prohibitUserStr.length;i++) {
					String[] userInfo = prohibitUserStr[i].split("\\|");
					SecuApprovalLineVO apprLine = new SecuApprovalLineVO();
					for(int j=0;j<userInfo.length;j++) {
						apprLine.setTaskNo(taskNo);
						apprLine.setApprStep(i+3);
						apprLine.setApprUserId(userInfo[0]);
						apprLine.setOrgCd(userInfo[1]);
						apprLine.setPositionGb(userInfo[2]);
						apprLine.setJobGb(userInfo[3]);
						apprLine.setRsltCd("000");
						apprLine.setComplianceYn("Y");
						apprLine.setRegId(taskVO.getRegId());
						apprLine.setRegDt(taskVO.getRegDt());
					}
					
					result += campaignDAO.insertApprovalLine(apprLine);
				}
				if("002".equals(taskVO.getProhibitChkTyp())){
					ProhibitWordVO proTitleWord = new ProhibitWordVO();
					proTitleWord.setTaskNo(taskNo);
					proTitleWord.setRegId(taskVO.getRegId());
					proTitleWord.setRegDt(taskVO.getRegDt());
					if (taskVO.getProhibitTitleCnt() > 0 ){
						proTitleWord.setContentTyp("000"); 
						proTitleWord.setProhibitCnt(taskVO.getProhibitTitleCnt());
						JSONParser parser = new JSONParser();
						Object obj = parser.parse( taskVO.getProhibitTitleDesc() );
						JSONObject jsonObj = (JSONObject) obj;
						proTitleWord.setProhibitDesc(jsonObj);
						proTitleWord.setRegId(taskVO.getRegId());
						proTitleWord.setRegDt(taskVO.getRegDt());
						result += campaignDAO.insertProhibitWord(proTitleWord);
					}
					if (taskVO.getProhibitTextCnt() > 0 ){
						proTitleWord.setContentTyp("001");
						proTitleWord.setProhibitCnt(taskVO.getProhibitTextCnt());
						JSONParser parser = new JSONParser();
						Object obj = parser.parse( taskVO.getProhibitTextDesc() );
						JSONObject jsonObj = (JSONObject) obj;
						proTitleWord.setProhibitDesc(jsonObj);
						proTitleWord.setRegId(taskVO.getRegId());
						proTitleWord.setRegDt(taskVO.getRegDt());
						result += campaignDAO.insertProhibitWord(proTitleWord);
					}
				}
			} 
		}
		
		// 발송결과종별정보 등록
		if(!StringUtil.isNull(taskVO.getSndTpeGb())) {
			EmailClinicVO emailClinic = new EmailClinicVO();
			emailClinic.setTaskNo(taskNo);
			emailClinic.setSndTpeGb(taskVO.getSndTpeGb());
			emailClinic.setRegId(taskVO.getRegId());
			emailClinic.setRegDt(taskVO.getRegDt());
			
			result += campaignDAO.insertEmailClinic(emailClinic);
		}
		
		// 마케팅수신동의유형 등록
		if(!StringUtil.isNull(taskVO.getMailMktGb())) {
			MailMktChkVO mailMkt = new MailMktChkVO();
			mailMkt.setTaskNo(taskNo);
			mailMkt.setMailMktGb(taskVO.getMailMktGb());
			mailMkt.setRegId(taskVO.getRegId());
			mailMkt.setRegDt(taskVO.getRegDt());
			
			result += campaignDAO.insertMailMktChkInfo(mailMkt);
		}
		 
 

		return result;
	}

	@Override
	public int updateMailStatus(TaskVO taskVO) throws Exception {
		int result = 0;
		if(!StringUtil.isNull(taskVO.getTaskNos())) {
			String[] taskNo = taskVO.getTaskNos().split(",");
			String[] subTaskNo = taskVO.getSubTaskNos().split(",");
			for(int i=0;i<taskNo.length;i++) {
				TaskVO task = new TaskVO();
				task.setTaskNo(Integer.parseInt(taskNo[i]));
				task.setSubTaskNo(Integer.parseInt(subTaskNo[i]));
				task.setStatus(taskVO.getStatus());
				task.setUpId(taskVO.getUpDt());
				task.setUpDt(taskVO.getUpDt());
				
//				if("001".equals(taskVO.getStatus())) {
//					task.setWorkStatus("000");
//				}
				
				result += campaignDAO.updateTaskStatus(task);		// 주업무 업데이트
				result += campaignDAO.updateSubTaskStatus(task);	// 보조업무 업데이트
			}
		}
		
		return result;
	}

	@Override
	public int copyMailInfo(TaskVO taskVO, PropertiesUtil properties) throws Exception {
		int result = 0;
		
		// 기존 주업무 읽기
		TaskVO copyTask = campaignDAO.getTaskInfo(taskVO.getTaskNo());
		
		// Content 파일 복사
		String currDtm = StringUtil.getDate(Code.TM_YMDHMSM);
		String newFlPath = copyTask.getContFlPath().substring(0, copyTask.getContFlPath().lastIndexOf("/")+1) + currDtm + ".tmp";
		String oldFullFlPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + copyTask.getContFlPath();
		String newFullFlPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + newFlPath;
		
		File oldFile = new File(oldFullFlPath);
		File newFile = new File(newFullFlPath);
		
		FileInputStream input = new FileInputStream(oldFile);
		FileOutputStream output = new FileOutputStream(newFile);
		
		byte[] buff = new byte[1024];
		int read;
		while((read = input.read(buff)) > 0) {
			output.write(buff, 0, read);
			output.flush();
		}
		input.close();
		output.close();
		
		// 복사한 주업무 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
		copyTask.setUserId(taskVO.getUserId());
		copyTask.setExeUserId("");
		copyTask.setDeptNo(taskVO.getDeptNo());
		copyTask.setContFlPath(newFlPath);
		copyTask.setStatus("000");
		copyTask.setRecoStatus("000");
		//copyTask.setWorkStatus("201");
		copyTask.setWorkStatus("000");
		copyTask.setRegId(taskVO.getRegId());
		copyTask.setRegDt(currDtm.substring(0,14));
		copyTask.setTaskNm( copyTask.getTaskNm() + (copyTask.getTaskNm().indexOf(" - [copy]")> 0 ? "": " - [copy]") );
		
		result += campaignDAO.insertTaskInfoForCopy(copyTask);
		
		// 신규 등록 업무번호 조회
		// 메일 보조업무 등록
		//MySql, Oracle
		int taskNo = campaignDAO.getTaskNo();
		//MsSql
		//int taskNo = copyTask.getTaskNo();
		
		// 기존 보조업무 읽기
		TaskVO copySubTask = campaignDAO.getSubTaskInfo(taskVO);
		
		// 복사한 보조업무 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
		copySubTask.setSubTaskNo(1);
		copySubTask.setTaskNo(taskNo);
		copySubTask.setSendDt(StringUtil.getDate(Code.TM_YMDHM));
		copySubTask.setEndDt("");
		//기존 발송대기 --> 결재대기 
		//copySubTask.setWorkStatus("201");
		//copySubTask.setStatus("000");
		//기존 결재대기 --> 발송대기 
		copySubTask.setWorkStatus("000");
		copySubTask.setStatus("000");
		
		result += campaignDAO.insertSubTaskInfoForCopy(copySubTask);
		
		// 첨부파일 목록 읽기
		List<AttachVO> copyAttachList = campaignDAO.getAttachList(taskVO.getTaskNo());
		if(copyAttachList != null && copyAttachList.size() > 0) {
			for(AttachVO attach:copyAttachList) {
				
				// 첨부파일 복사
				String newAttachPath = "attach/" + System.currentTimeMillis() + "_" + attach.getAttNm().replaceAll(" ", "_");
				String oldFullAttachPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + attach.getAttFlPath();
				String newFullAttachPath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + newAttachPath;
				
				File oldAttachFile = new File(oldFullAttachPath);
				File newAttachFile = new File(newFullAttachPath);
				
				FileInputStream inputAttach = new FileInputStream(oldAttachFile);
				FileOutputStream outputAttach = new FileOutputStream(newAttachFile);
				
				buff = new byte[1024];
				while((read = inputAttach.read(buff)) > 0) {
					outputAttach.write(buff, 0, read);
					outputAttach.flush();
				}
				inputAttach.close();
				outputAttach.close();
				
				// 복사한 첨부파일 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
				attach.setAttFlPath(newAttachPath);
				attach.setTaskNo(taskNo);
				
				result += campaignDAO.insertAttachInfo(attach);
			}
		}
		
		// WebAgent 읽기
		WebAgentVO webAgent = campaignDAO.getWebAgentInfo(taskVO.getTaskNo());
		if(webAgent != null) {
			webAgent.setTaskNo(taskNo);
			webAgent.setRegId(taskVO.getRegId());
			webAgent.setRegDt(currDtm.substring(0,14));
			
			result += campaignDAO.insertWebAgent(webAgent);
		}
		
		// 발송결과종별정보 읽기
		if(!StringUtil.isNull(copyTask.getSndTpeGb())) {
			EmailClinicVO emailClinic = new EmailClinicVO();
			emailClinic.setTaskNo(taskNo);
			emailClinic.setSndTpeGb(copyTask.getSndTpeGb());
			emailClinic.setRegId(taskVO.getRegId());
			emailClinic.setRegDt(currDtm.substring(0,14));
			
			result += campaignDAO.insertEmailClinic(emailClinic);
		}
		
		// 마케팅수신동의유형 등록
		if(!StringUtil.isNull(copyTask.getMailMktGb())) {
			MailMktChkVO mailMkt = new MailMktChkVO();
			mailMkt.setTaskNo(taskNo);
			mailMkt.setMailMktGb(copyTask.getMailMktGb());
			mailMkt.setRegId(taskVO.getRegId());
			mailMkt.setRegDt(currDtm.substring(0,14));
			
			result += campaignDAO.insertMailMktChkInfo(mailMkt);
		}
		
		//결재함 등록 
		List<SecuApprovalLineVO> apprLineList = campaignDAO.getApprovalLineList(taskVO.getTaskNo());
		if(apprLineList != null && apprLineList.size() > 0) {
			for(SecuApprovalLineVO apprLine:apprLineList) {
				apprLine.setTaskNo(taskNo);
				apprLine.setApprDt(null);
				apprLine.setUpDt(null);
				apprLine.setUpId(null);
				apprLine.setRsltCd("000");
				apprLine.setRejectCd(null);
				apprLine.setRegId(copyTask.getRegId());
				apprLine.setRegDt(copyTask.getRegDt());
				result += campaignDAO.insertApprovalLine(apprLine);
			}
		}
		
		String prohibitDesc = "";
		List<ProhibitWordVO> prohibitWordList = campaignDAO.getProhibitWordList(taskVO.getTaskNo());
		if(prohibitWordList != null && prohibitWordList.size() > 0) {
			for(ProhibitWordVO prohibitWord:prohibitWordList) {
				prohibitWord.setTaskNo(taskNo);
				prohibitDesc = prohibitWord.getProhibitDescString() ;
				
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(prohibitDesc);
				JSONObject jsonObj = (JSONObject) obj;
				prohibitWord.setProhibitDesc(jsonObj);
				prohibitWord.setRegId(copyTask.getRegId());
				prohibitWord.setRegDt(copyTask.getRegDt());
				result += campaignDAO.insertProhibitWord(prohibitWord);
			}
		}
		
		return result;
	}

	@Override
	public List<TestEaiUserVO> getTestCustList(TestEaiUserVO testEaiUserVO) throws Exception {
		return campaignDAO.getTestCustList(testEaiUserVO);
	}
	
	@Override
	public List<TestUserVO> getTestUserList(String userId) throws Exception {
		return campaignDAO.getTestUserList(userId);
	}

	@Override
	public int insertTestUserInfo(TestUserVO testUserVO) throws Exception {
		return campaignDAO.insertTestUserInfo(testUserVO);
	}

	@Override
	public int updateTestUserInfo(TestUserVO testUserVO) throws Exception {
		return campaignDAO.updateTestUserInfo(testUserVO);
	}

	@Override
	public int deleteTestUserInfo(TestUserVO testUserVO) throws Exception {
		return campaignDAO.deleteTestUserInfo(testUserVO);
	}

	@Override
	public int sendTestMail(TestUserVO testUserVO, HttpSession session) throws Exception {
		int result = 0;
		
		String sendDt = StringUtil.getDate(Code.TM_YMDHM);
		
		// 기존 주업무 읽기
		TaskVO sendTask = campaignDAO.getTaskInfo(Integer.parseInt(testUserVO.getTaskNos()));
		
		// 테스트발송 주업무 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
		sendTask.setUserId((String)session.getAttribute("NEO_USER_ID"));
		sendTask.setExeUserId((String)session.getAttribute("NEO_USER_ID"));
		sendTask.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		sendTask.setStatus("000");
		sendTask.setRecoStatus("001");
		sendTask.setRegId((String)session.getAttribute("NEO_USER_ID"));
		sendTask.setRegDt(sendDt+"00");
		sendTask.setTaskNm(sendTask.getTaskNm() + " - [test]");
		
		result += campaignDAO.insertTaskInfoForTestSend(sendTask);
		
		// 메일 보조업무 등록
		//MySql, Oracle
		int taskNo = campaignDAO.getTaskNo();
		//MsSql
		//int taskNo = sendTask.getTaskNo();
		
		// 기존 보조업무 읽기
		TaskVO taskVO = new TaskVO();
		taskVO.setTaskNo(Integer.parseInt(testUserVO.getTaskNos()));
		taskVO.setSubTaskNo(Integer.parseInt(testUserVO.getSubTaskNos()));
		
		TaskVO sendSubTask = campaignDAO.getSubTaskInfo(taskVO);
		
		// 테스트발송 보조업무 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
		sendSubTask.setTaskNo(taskNo);
		sendSubTask.setSubTaskNo(1);
		sendSubTask.setSendDt(sendDt);
		sendSubTask.setEndDt("");
		sendSubTask.setWorkStatus("001");
		sendSubTask.setSendTestYn("Y");
		sendSubTask.setSendTestEm(testUserVO.getTestUserEm());
		sendSubTask.setSendTestTaskNo(taskVO.getTaskNo());
		sendSubTask.setSendTestSubTaskNo(taskVO.getSubTaskNo());
		//EAI 용 정보에 NULL 넣기 
		sendSubTask.setTestSendId("");
		sendSubTask.setRtyTyp("");
		sendSubTask.setRtyCode("");
		
		result += campaignDAO.insertSubTaskInfoForTestSend(sendSubTask);
		
		
		// 첨부파일 목록 읽기
		List<AttachVO> copyAttachList = campaignDAO.getAttachList(taskVO.getTaskNo());
		if(copyAttachList != null && copyAttachList.size() > 0) {
			for(AttachVO attach : copyAttachList) {
				// 복사한 첨부파일 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
				attach.setTaskNo(taskNo);
				
				result += campaignDAO.insertAttachInfo(attach);
			}
		}
		
		// WebAgent 읽기
		WebAgentVO webAgent = campaignDAO.getWebAgentInfo(Integer.parseInt(testUserVO.getTaskNos()));
		if(webAgent != null) {
			webAgent.setTaskNo(taskNo);
			webAgent.setRegId((String)session.getAttribute("NEO_USER_ID"));
			webAgent.setRegDt(sendDt+"00");
			
			result += campaignDAO.insertWebAgent(webAgent);
		}
		
		return result;
	}

	@Override
	public int sendTestEaiMail(TestEaiUserVO testEaiUserVO, HttpSession session) throws Exception {
		int result = 0;
		
		String sendDt = StringUtil.getDate(Code.TM_YMDHM);
		
		// 기존 주업무 읽기
		TaskVO sendTask = campaignDAO.getTaskInfo(Integer.parseInt(testEaiUserVO.getEaiTaskNos()));
		
		// 테스트발송 주업무 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
		sendTask.setUserId((String)session.getAttribute("NEO_USER_ID"));
		sendTask.setExeUserId((String)session.getAttribute("NEO_USER_ID"));
		sendTask.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		sendTask.setStatus("000");
		sendTask.setRecoStatus("001");
		sendTask.setRegId((String)session.getAttribute("NEO_USER_ID"));
		sendTask.setRegDt(sendDt+"00");
		sendTask.setTaskNm(sendTask.getTaskNm() + " - [testEAI]");

		
		result += campaignDAO.insertTaskInfoForTestSend(sendTask);
		
		
		// 메일 보조업무 등록
		//MySql, Oracle
		int taskNo = campaignDAO.getTaskNo();
		//MsSql
		//int taskNo = sendTask.getTaskNo();
		
		// 기존 보조업무 읽기
		TaskVO taskVO = new TaskVO();
		taskVO.setTaskNo(Integer.parseInt(testEaiUserVO.getEaiTaskNos()));
		taskVO.setSubTaskNo(Integer.parseInt(testEaiUserVO.getEaiSubTaskNos()));
		
		TaskVO sendSubTask = campaignDAO.getSubTaskInfo(taskVO);
		
		// 테스트발송 보조업무 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
		sendSubTask.setTaskNo(taskNo);
		sendSubTask.setSubTaskNo(1);
		sendSubTask.setSendDt(sendDt);
		sendSubTask.setEndDt("");
		sendSubTask.setWorkStatus("001");
		sendSubTask.setSendTestYn("Y");
		sendSubTask.setSendTestEm(testEaiUserVO.getTestEaiUserEm());
		sendSubTask.setSendTestTaskNo(taskVO.getTaskNo());
		sendSubTask.setSendTestSubTaskNo(taskVO.getSubTaskNo());
		//EAI 용 
		sendSubTask.setTestSendId(testEaiUserVO.getIfId());
		sendSubTask.setRtyTyp("002");
		sendSubTask.setRtyCode(testEaiUserVO.getBizkey());
		
		result += campaignDAO.insertSubTaskInfoForTestSend(sendSubTask);
		
		// 첨부파일 목록 읽기
		List<AttachVO> copyAttachList = campaignDAO.getAttachList(taskVO.getTaskNo());
		if(copyAttachList != null && copyAttachList.size() > 0) {
			for(AttachVO attach : copyAttachList) {
				// 복사한 첨부파일 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
				attach.setTaskNo(taskNo);
				
				result += campaignDAO.insertAttachInfo(attach);
			}
		}
		
		// WebAgent 읽기
		WebAgentVO webAgent = campaignDAO.getWebAgentInfo(Integer.parseInt(testEaiUserVO.getEaiTaskNos()));
		if(webAgent != null) {
			webAgent.setTaskNo(taskNo);
			webAgent.setRegId((String)session.getAttribute("NEO_USER_ID"));
			webAgent.setRegDt(sendDt+"00");
			
			result += campaignDAO.insertWebAgent(webAgent);
		}
		
		return result;
	}

	
	@Override
	public int reSendMail(TaskVO reSendtaskVO, HttpSession session) throws Exception {
		int result = 0; 
		
		String sendDt = StringUtil.getDate(Code.TM_YMDHM);
		
		// 기존 주업무 읽기
		TaskVO sendTask = campaignDAO.getTaskInfo(reSendtaskVO.getTaskNo());
		
		// 재발송 주업무 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
		sendTask.setUserId((String)session.getAttribute("NEO_USER_ID"));
		sendTask.setExeUserId((String)session.getAttribute("NEO_USER_ID"));
		sendTask.setDeptNo((int)session.getAttribute("NEO_DEPT_NO"));
		sendTask.setStatus("000");
		sendTask.setSendRepeat("000");
		sendTask.setRecoStatus("001");
		
		sendTask.setRegId((String)session.getAttribute("NEO_USER_ID"));
		sendTask.setRegDt(sendDt+"00");
		
		sendTask.setTaskNm(sendTask.getTaskNm().replace(" - [재발송]", ""));
		sendTask.setTaskNm(sendTask.getTaskNm() + " - [재발송]");
		
		result += campaignDAO.insertTaskInfoForReSend(sendTask);
		
		// 메일 보조업무 등록
		//MySql, Oracle
		int taskNo = campaignDAO.getTaskNo();
		//MsSql
		//int taskNo = sendTask.getTaskNo();
		
		int subTaskNo = campaignDAO.getSubTaskNo(taskNo);
		
		// 기존 보조업무 읽기
		TaskVO taskVO = new TaskVO();
		taskVO.setTaskNo(reSendtaskVO.getTaskNo());
		taskVO.setSubTaskNo(reSendtaskVO.getSubTaskNo());
		
		TaskVO sendSubTask = campaignDAO.getSubTaskInfo(taskVO);
		
		// 재발송 보조업무 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
		sendSubTask.setTaskNo(taskNo);
		sendSubTask.setSubTaskNo(subTaskNo);
		sendSubTask.setSendDt(sendDt);
		sendSubTask.setEndDt("");
		sendSubTask.setStatus("000");
 
		sendSubTask.setWorkStatus("001");
		sendSubTask.setSendTestYn("N");
		sendSubTask.setRtyTyp(reSendtaskVO.getRtyTyp());
		
		if(sendSubTask.getRtyTaskNo() == 0 ) {
			sendSubTask.setRtyTaskNo(reSendtaskVO.getTaskNo());
		}
		
		if(sendSubTask.getRtySubTaskNo() == 0 ) {
			sendSubTask.setRtySubTaskNo(reSendtaskVO.getSubTaskNo());
		}
		
		if ("001".equals(reSendtaskVO.getRtyTyp())){
			sendSubTask.setRtyCode(reSendtaskVO.getRtyCode());
		} else {
			sendSubTask.setRtyCode(reSendtaskVO.getBizkey());
		}
		
		result += campaignDAO.insertSubTaskInfoForReSend(sendSubTask);
		
		// 첨부파일 목록 읽기
		List<AttachVO> copyAttachList = campaignDAO.getAttachList(reSendtaskVO.getTaskNo());
		if(copyAttachList != null && copyAttachList.size() > 0) {
			for(AttachVO attach : copyAttachList) {
				// 복사한 첨부파일 값 설정(설정하지 않은 경우 기존값 사용) 및 등록
				attach.setTaskNo(taskNo);
				
				result += campaignDAO.insertAttachInfo(attach);
			}
		}
		
		// WebAgent 읽기
		WebAgentVO webAgent = campaignDAO.getWebAgentInfo(reSendtaskVO.getTaskNo());
		if(webAgent != null) {
			webAgent.setTaskNo(taskNo);
			webAgent.setRegId((String)session.getAttribute("NEO_USER_ID"));
			webAgent.setRegDt(sendDt+"00");
			
			result += campaignDAO.insertWebAgent(webAgent);
		}
		
		return result;
	}
	
	@Override
	public TaskVO getMailInfo(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailInfo(taskVO);
	}

	@Override
	public List<AttachVO> getAttachList(int taskNo) throws Exception {
		return campaignDAO.getAttachList(taskNo);
	}

	@Override
	public int updateMailInfo(TaskVO taskVO, List<AttachVO> attachList, List<LinkVO> linkList) throws Exception {
		int result = 0;
		
		// 주업무 정보 수정
		// send_term_end_dt가 00000000가 들어가는경우 배제하기 위함 (어느순간인지 재연이 안되서 임시임)
		if(taskVO.getSendTermEndDt() !=null) {
			String tmpEnd = taskVO.getSendTermEndDt();
			if(tmpEnd.length()> 8 && "00000000".equals(tmpEnd.substring(0,8))){
				taskVO.setSendTermEndDt("999912312359");
			}
		}
		result += campaignDAO.updateTaskInfo(taskVO);
		
		// 보조업무 정보 수정
		result += campaignDAO.updateSubTaskInfo(taskVO);
		
		// 기존 첨부파일 삭제
		result += campaignDAO.deleteAttachInfo(taskVO.getTaskNo());
		// 첨부파일 등록
		if(attachList != null && attachList.size() > 0) {
			for(AttachVO attach:attachList) {
				result +=campaignDAO.insertAttachInfo(attach);
			}
		}
		
		// 링크정보 등록
		if(linkList != null && linkList.size() > 0) {
			for(LinkVO link:linkList) {
				result +=campaignDAO.insertLinkInfo(link);
			}
		}
		
		// 웹에이전트 정보 조회
		WebAgentVO webAgentVO = campaignDAO.getWebAgentInfo(taskVO.getTaskNo());
		if(webAgentVO != null) {
			// 웹에이전트 정보 삭제
			if(StringUtil.isNull(taskVO.getWebAgentUrl())) {
				result += campaignDAO.deleteWebAgent(webAgentVO);
			
			// 웹에이전트 정보 수정
			} else {
				WebAgentVO webAgent = new WebAgentVO();
				webAgent.setTaskNo(taskVO.getTaskNo());
				webAgent.setAttNo(1);
				webAgent.setSourceUrl(taskVO.getWebAgentUrl());
				webAgent.setSecuAttYn(taskVO.getWebAgentAttachYn());
				webAgent.setSecuAttTyp(taskVO.getSecuAttTyp());
				webAgent.setUpId(taskVO.getUpId());
				webAgent.setUpDt(taskVO.getUpDt());
				
				result += campaignDAO.updateWebAgent(webAgent);
			}
		} else {
			// 웹에이전트 정보 등록
			if(!StringUtil.isNull(taskVO.getWebAgentUrl())) {
				WebAgentVO webAgent = new WebAgentVO();
				webAgent.setTaskNo(taskVO.getTaskNo());
				webAgent.setAttNo(1);
				webAgent.setSourceUrl(taskVO.getWebAgentUrl());
				webAgent.setSecuAttYn(taskVO.getWebAgentAttachYn());
				webAgent.setSecuAttTyp(taskVO.getSecuAttTyp());
				webAgent.setRegId(taskVO.getUpId());
				webAgent.setRegDt(taskVO.getUpDt());
				
				result += campaignDAO.insertWebAgent(webAgent);
			}
			
		}
		
		// 기존 발송결재라인 정보 삭제
		result += campaignDAO.deleteApprovalLine(taskVO.getTaskNo());
		// 기존 준법심의 정보 삭제 
		result += campaignDAO.deleteProhibitWord(taskVO.getTaskNo());
		
		// 발송결재라인정보 등록
		if(!StringUtil.isNull(taskVO.getApprUserId())) {
			String[] apprUserStr = taskVO.getApprUserId().split(",");
			for(int i=0;i<apprUserStr.length;i++) {
				//appUserId => userId|orgCd|posGb|jobGb
				String[] userInfo = apprUserStr[i].split("\\|");
				SecuApprovalLineVO apprLine = new SecuApprovalLineVO();
				for(int j=0;j<userInfo.length;j++) {
					apprLine.setTaskNo(taskVO.getTaskNo());
					apprLine.setApprStep(i+1);
					apprLine.setApprUserId(userInfo[0]);
					apprLine.setOrgCd(userInfo[1]);
					apprLine.setPositionGb(userInfo[2]);
					apprLine.setJobGb(userInfo[3]);
					apprLine.setRsltCd("000");
					apprLine.setComplianceYn("N");
					apprLine.setRegId(taskVO.getUpId());
					apprLine.setRegDt(taskVO.getUpDt());
				}
				
				result += campaignDAO.insertApprovalLine(apprLine);
			}
		}
		
		// 준법 감시 정보가 있을 경우 
		if( "002".equals(taskVO.getProhibitChkTyp())){
			if(!StringUtil.isNull(taskVO.getProhibitUserId())) {
				String[] prohibitUserStr = taskVO.getProhibitUserId().split(",");
				for(int i=0;i<prohibitUserStr.length;i++) {
					String[] userInfo = prohibitUserStr[i].split("\\|");
					SecuApprovalLineVO apprLine = new SecuApprovalLineVO();
					for(int j=0;j<userInfo.length;j++) {
						apprLine.setTaskNo(taskVO.getTaskNo());
						apprLine.setApprStep(i+3);
						apprLine.setApprUserId(userInfo[0]);
						apprLine.setOrgCd(userInfo[1]);
						apprLine.setPositionGb(userInfo[2]);
						apprLine.setJobGb(userInfo[3]);
						apprLine.setRsltCd("000");
						apprLine.setComplianceYn("Y");
						apprLine.setRegId(taskVO.getUpId());
						apprLine.setRegDt(taskVO.getUpDt());
					}
					result += campaignDAO.insertApprovalLine(apprLine);
				}
				if("002".equals(taskVO.getProhibitChkTyp())){
					ProhibitWordVO proTitleWord = new ProhibitWordVO();
					proTitleWord.setTaskNo(taskVO.getTaskNo());
					proTitleWord.setRegId(taskVO.getRegId());
					proTitleWord.setRegDt(taskVO.getRegDt());
					if (taskVO.getProhibitTitleCnt() > 0 ){
						proTitleWord.setContentTyp("000"); 
						proTitleWord.setProhibitCnt(taskVO.getProhibitTitleCnt());
						JSONParser parser = new JSONParser();
						Object obj = parser.parse( taskVO.getProhibitTitleDesc() );
						JSONObject jsonObj = (JSONObject) obj;
						proTitleWord.setProhibitDesc(jsonObj);
						
						result += campaignDAO.insertProhibitWord(proTitleWord);
					}
					if (taskVO.getProhibitTextCnt() > 0 ){
						proTitleWord.setContentTyp("001");
						proTitleWord.setProhibitCnt(taskVO.getProhibitTextCnt());
						JSONParser parser = new JSONParser();
						Object obj = parser.parse( taskVO.getProhibitTextDesc() );
						JSONObject jsonObj = (JSONObject) obj;
						proTitleWord.setProhibitDesc(jsonObj);
						
						result += campaignDAO.insertProhibitWord(proTitleWord);
					}
				}
			} 
		}	
		// 발송결과종별정보 조회
		EmailClinicVO emailClinicVO = campaignDAO.getEmailClinicInfo(taskVO.getTaskNo());
		if(emailClinicVO != null) {
			// 발송결과종별정보
			if(StringUtil.isNull(taskVO.getSndTpeGb())) {
				EmailClinicVO emailClinic = new EmailClinicVO();
				emailClinic.setTaskNo(taskVO.getTaskNo());
				
				result += campaignDAO.deleteEmailClinic(emailClinic);
			} else {
				EmailClinicVO emailClinic = new EmailClinicVO();
				emailClinic.setTaskNo(taskVO.getTaskNo());
				emailClinic.setSndTpeGb(taskVO.getSndTpeGb());
				emailClinic.setUpId(taskVO.getUpId());
				emailClinic.setUpDt(taskVO.getUpDt());
				
				result += campaignDAO.updateEmailClinic(emailClinic);
			}
		} else {
			if(!StringUtil.isNull(taskVO.getSndTpeGb())) {
				EmailClinicVO emailClinic = new EmailClinicVO();
				emailClinic.setTaskNo(taskVO.getTaskNo());
				emailClinic.setSndTpeGb(taskVO.getSndTpeGb());
				emailClinic.setRegId(taskVO.getUpId());
				emailClinic.setRegDt(taskVO.getUpDt());
				
				result += campaignDAO.insertEmailClinic(emailClinic);
			}
		}
		
		// 마케팅수신동의유형 조회
		MailMktChkVO mailMktChkVO = campaignDAO.getMailMktChkInfo(taskVO.getTaskNo());
		if(mailMktChkVO != null) {
			// 마케팅수신동의유형 삭제
			if(StringUtil.isNull(taskVO.getMailMktGb())) {
				result += campaignDAO.deleteMailMktChkInfo(taskVO.getTaskNo());
			
			// 마케팅수신동의유형 수정
			} else {
				MailMktChkVO mailMkt = new MailMktChkVO();
				mailMkt.setTaskNo(taskVO.getTaskNo());
				mailMkt.setMailMktGb(taskVO.getMailMktGb());
				mailMkt.setUpId(taskVO.getUpId());
				mailMkt.setUpDt(taskVO.getUpDt());
				
				result += campaignDAO.updateMailMktChkInfo(mailMkt);
			}
		} else {
			// 마케팅수신동의유형 등록
			if(!StringUtil.isNull(taskVO.getMailMktGb())) {
				MailMktChkVO mailMkt = new MailMktChkVO();
				mailMkt.setTaskNo(taskVO.getTaskNo());
				mailMkt.setMailMktGb(taskVO.getMailMktGb());
				mailMkt.setRegId(taskVO.getUpId());
				mailMkt.setRegDt(taskVO.getUpDt());
				
				result += campaignDAO.insertMailMktChkInfo(mailMkt);
			}
		}
		
		return result;
	}

	@Override
	public int updateMailInfoDate(TaskVO taskVO) throws Exception {
		int result = 0;
		
		// 주업무 정보 수정
		if(taskVO.getSendTermEndDt() !=null) {
			String tmpEnd = taskVO.getSendTermEndDt();
			if(tmpEnd.length()> 8 && "00000000".equals(tmpEnd.substring(0,8))){
				taskVO.setSendTermEndDt("999912312359");
			}
		}
		result += campaignDAO.updateTaskInfoDate(taskVO);
		
		// 보조업무 정보 수정
		result += campaignDAO.updateSubTaskInfoDate(taskVO);
		
		return result;
	}
	
	@Override
	public List<TaskVO> getMailTestTaskList(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailTestTaskList(taskVO);
	}

	@Override
	public List<SendTestLogVO> getMailTestSendLogList(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailTestSendLogList(taskVO);
	}

	@Override
	public TaskVO getMailPeriod(int taskNo) throws Exception {
		return campaignDAO.getMailPeriod(taskNo);
	}
	
	@Override
	public List<TaskVO> getCampMailList(TaskVO taskVO) throws Exception {
		return campaignDAO.getCampMailList(taskVO);
	}

	@Override
	public List<TaskVO> getMailListUnion(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailListUnion(taskVO);
	}

	@Override
	public List<TaskVO> getMailListOnetime(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailListOnetime(taskVO);
	}

	@Override
	public List<ApprovalOrgVO> getOrgListLvl1() throws Exception {
		return campaignDAO.getOrgListLvl1();
	}

	@Override
	public List<ApprovalOrgVO> getOrgListChild(String upOrgCd) throws Exception {
		return campaignDAO.getOrgListChild(upOrgCd);
	}

	@Override
	public List<UserVO> getUserListOrg(String orgCd) throws Exception {
		return campaignDAO.getUserListOrg(orgCd);
	}

	@Override
	public List<UserVO> getUserListSearch(String userNm) throws Exception {
		return campaignDAO.getUserListSearch(userNm);
	}

	@Override
	public WebAgentVO getWebAgentInfo(int taskNo) throws Exception {
		return campaignDAO.getWebAgentInfo(taskNo);
	}

	@Override
	public int insertWebAgent(WebAgentVO webAgentVO) throws Exception {
		return campaignDAO.insertWebAgent(webAgentVO);
	}

	@Override
	public int updateWebAgent(WebAgentVO webAgentVO) throws Exception {
		return campaignDAO.updateWebAgent(webAgentVO);
	}

	@Override
	public int deleteWebAgent(WebAgentVO webAgentVO) throws Exception {
		return campaignDAO.deleteWebAgent(webAgentVO);
	}

	@Override
	public int insertEmailClinic(EmailClinicVO emailClinicVO) throws Exception {
		return campaignDAO.insertEmailClinic(emailClinicVO);
	}

	@Override
	public int updateEmailClinic(EmailClinicVO emailClinicVO) throws Exception {
		return campaignDAO.updateEmailClinic(emailClinicVO);
	}

	@Override
	public int deleteEmailClinic(EmailClinicVO emailClinicVO) throws Exception {
		return campaignDAO.deleteEmailClinic(emailClinicVO);
	}
	
	@Override
	public List<ProhibitWordVO> getProhibitWordList(int taskNo) throws Exception {
		return campaignDAO.getProhibitWordList(taskNo);
	}
	
	@Override
	public int insertProhibitWord(ProhibitWordVO prohibitWordVO) throws Exception {
		return campaignDAO.insertProhibitWord(prohibitWordVO);
	}
	
	@Override
	public int deleteProhibitWord(int taskNo) throws Exception {
		return campaignDAO.deleteProhibitWord(taskNo);
	}

	@Override
	public List<SecuApprovalLineVO> getApprovalLineList(int taskNo) throws Exception {
		return campaignDAO.getApprovalLineList(taskNo);
	}

	@Override
	public int insertApprovalLine(SecuApprovalLineVO approvalLineVO) throws Exception {
		return campaignDAO.insertApprovalLine(approvalLineVO);
	}

	@Override
	public int updateApprovalLine(SecuApprovalLineVO approvalLineVO) throws Exception {
		return campaignDAO.updateApprovalLine(approvalLineVO);
	}

	@Override
	public int deleteApprovalLine(int taskNo) throws Exception {
		return campaignDAO.deleteApprovalLine(taskNo);
	}

	@Override
	public EmailClinicVO getEmailClinicInfo(int taskNo) throws Exception {
		return campaignDAO.getEmailClinicInfo(taskNo);
	}

	@Override
	public List<TaskVO> getMailListRepeat(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailListRepeat(taskVO);
	}

	@Override
	public int updateSubmitApproval(TaskVO taskVO) throws Exception {
		int result = 0;
		result += campaignDAO.updateSubmitApproval(taskVO);
		result += campaignDAO.updateSubmitApprovalLine(taskVO);
		return result;
	}

	@Override
	public List<TaskVO> getMailTestList(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailTestList(taskVO);
	}

	@Override
	public List<TaskVO> getSubTaskList(TaskVO taskVO) throws Exception {
		return campaignDAO.getSubTaskList(taskVO);
	}

	@Override
	public List<SendLogVO> getMailTestResultList(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailTestResultList(taskVO);
	}

	@Override
	public String getFirstApprUserId(int taskNo) throws Exception {
		return campaignDAO.getFirstApprUserId(taskNo);
	}

	@Override
	public int cancelMailApproval(TaskVO taskVO) throws Exception {
		int result = 0;
		if(!StringUtil.isNull(taskVO.getTaskNos())) {
			String[] taskNo = taskVO.getTaskNos().split(",");
			String[] subTaskNo = taskVO.getSubTaskNos().split(",");
			for(int i=0;i<taskNo.length;i++) {
				TaskVO task = new TaskVO();
				task.setTaskNo(Integer.parseInt(taskNo[i]));
				task.setSubTaskNo(Integer.parseInt(subTaskNo[i]));
				task.setStatus(taskVO.getStatus());
				task.setUpId(taskVO.getUpId());
				task.setUpDt(taskVO.getUpDt());
				
				// 보조업무상태 수정
				//task.setWorkStatus("000"); // 발송대기로 변경
				//2021.11.01 김준희수정
				task.setWorkStatus("201"); // 결재대기로 변경
				result += campaignDAO.updateSubTaskStatus(task);
				
				// 결재라인 의  RSLT = 000 으로 변경 
				SecuApprovalLineVO secuapprovalLineVO = new SecuApprovalLineVO();
				secuapprovalLineVO.setApprDt(taskVO.getUpDt());
				secuapprovalLineVO.setRsltCd("000");
				secuapprovalLineVO.setRejectCd(null);
				secuapprovalLineVO.setUpId(task.getUpId());
				secuapprovalLineVO.setUpDt(task.getUpDt());
				secuapprovalLineVO.setTaskNo(task.getTaskNo());
				
				result += campaignDAO.updateApprovalLine(secuapprovalLineVO);
			}
		}
		
		return result;
		/*
		int result = 0;
		if(!StringUtil.isNull(taskVO.getTaskNos())) {
			String[] taskNo = taskVO.getTaskNos().split(",");
			String[] subTaskNo = taskVO.getSubTaskNos().split(",");
			for(int i=0;i<taskNo.length;i++) {
				TaskVO task = new TaskVO();
				task.setTaskNo(Integer.parseInt(taskNo[i]));
				task.setSubTaskNo(Integer.parseInt(subTaskNo[i]));
				task.setStatus(taskVO.getStatus());
				task.setUpId(taskVO.getUpDt());
				task.setUpDt(taskVO.getUpDt());
				
				if("001".equals(taskVO.getStatus())) {
					task.setWorkStatus("000");
				}
				
				result += campaignDAO.updateTaskStatus(task);		// 주업무 업데이트
				result += campaignDAO.updateSubTaskStatus(task);	// 보조업무 업데이트
			}
		}
		
		return result;

		 */
	}
	
	@Override
	public List<TaskVO> getMailSendHist(TaskVO taskVO) throws Exception {
		return campaignDAO.getMailSendHist(taskVO);
	}

	@Override
	public List<TaskVO> getMailWorkStatus(String requestKey) throws Exception {
		return campaignDAO.getMailWorkStatus(requestKey);
	}

}
