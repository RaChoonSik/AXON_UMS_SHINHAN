/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.23
 * 설명 : 메일발송결재 서비스 구현
 */
package kr.co.enders.ums.ems.apr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.enders.ums.ems.apr.dao.SecuApprovalLineDAO;
import kr.co.enders.ums.ems.apr.vo.SecuApprovalLineVO;
import kr.co.enders.ums.ems.cam.vo.AttachVO;
import kr.co.enders.ums.ems.cam.vo.TaskVO;

@Service
public class SecuApprovalLineServiceImpl implements SecuApprovalLineService {
	@Autowired
	private SecuApprovalLineDAO approvalLineDAO;

	@Override
	public List<SecuApprovalLineVO> getApprovalLineList(SecuApprovalLineVO approvalLineVO) throws Exception {
		if("10".equals(approvalLineVO.getSearchSvcType())) {
			return approvalLineDAO.getApprovalLineEmsList(approvalLineVO);
		} else if("20".equals(approvalLineVO.getSearchSvcType())) {
			return approvalLineDAO.getApprovalLineRnsList(approvalLineVO);
		} else {
			return approvalLineDAO.getApprovalLineList(approvalLineVO);
		}
	}

	@Override
	public SecuApprovalLineVO getMailInfo(SecuApprovalLineVO approvalLineVO) throws Exception {
		return approvalLineDAO.getMailInfo(approvalLineVO);
	}

	@Override
	public List<AttachVO> getAttachList(int taskNo) throws Exception {
		return approvalLineDAO.getAttachList(taskNo);
	}

	@Override
	public List<SecuApprovalLineVO> getMailApprLineList(int taskNo) throws Exception {
		return approvalLineDAO.getMailApprLineList(taskNo);
	}

	@Override
	public int updateMailAprStep(SecuApprovalLineVO approvalLineVO) throws Exception {
		int result = 0;		
		// 결재승인
		if("002".equals(approvalLineVO.getRsltCd())) {
			
			// 발송결재라인 상태수정
			result += approvalLineDAO.updateMailAprStep(approvalLineVO);
			
			List<SecuApprovalLineVO>  nextVO = approvalLineDAO.nextMailAprStep(approvalLineVO);
			
			String tmpComplianceYn = approvalLineVO.getComplianceYn();
			
			if(nextVO != null && nextVO.size() > 0) {//다음 승인 처리 								
				if("N".equals(nextVO.get(0).getComplianceYn())) {//일반결제
					approvalLineVO.setApprStep(nextVO.get(0).getApprStep());					
					approvalLineVO.setRsltCd("001");
					approvalLineVO.setActApprUserId("");
					approvalLineVO.setRejectDesc("");
					approvalLineVO.setApprDt("");
					result += approvalLineDAO.updateMailAprStep(approvalLineVO);
				} else {//준법결제
					for(int i = 0 ; i < nextVO.size();i++) {												
						approvalLineVO.setApprStep(nextVO.get(i).getApprStep());
						
						if("N".equals(tmpComplianceYn)) { 
							approvalLineVO.setRsltCd("001");
							approvalLineVO.setActApprUserId("");
							approvalLineVO.setRejectDesc("");
							approvalLineVO.setApprDt("");
						} else {
							approvalLineVO.setRsltCd("002");
						}						
						result += approvalLineDAO.updateMailAprStep(approvalLineVO);
					}
				}
			} 
			
			
			if(approvalLineVO.getRsltCd() == "002") {//결제 라인 마무리 일때 
				//마지막 승인 처리 
				TaskVO taskVO = new TaskVO();
				taskVO.setTaskNo(approvalLineVO.getTaskNo());
				taskVO.setSubTaskNo(approvalLineVO.getSubTaskNo());
				taskVO.setWorkStatus("204");	// 발송상태:결재완료
				taskVO.setExeUserId(approvalLineVO.getUpId());
				
				// 보조업무 승인
				result += approvalLineDAO.updateSubTaskStatusAdmit(taskVO);
			}
		}
		
		// 결재반려
		if("003".equals(approvalLineVO.getRsltCd())) {
			
			List<SecuApprovalLineVO>  nowVO = approvalLineDAO.nowMailAprStep(approvalLineVO);
			if("Y".equals(nowVO.get(0).getComplianceYn())) {//준법감시부 반려
				// 발송결재라인 상태수정
				for(int i = 0 ; i < nowVO.size();i++) {
					approvalLineVO.setApprStep(nowVO.get(i).getApprStep());					
					result += approvalLineDAO.updateMailAprStep(approvalLineVO);
				}				
			} else {
				// 발송결재라인 상태수정
				result += approvalLineDAO.updateMailAprStep(approvalLineVO);
			}
			
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskNo(approvalLineVO.getTaskNo());
			taskVO.setSubTaskNo(approvalLineVO.getSubTaskNo());
			taskVO.setWorkStatus("203");	// 발송상태:결재반려
			taskVO.setExeUserId(approvalLineVO.getUpId());
			
			result += approvalLineDAO.updateSubTaskStatusAdmit(taskVO);
		}
		
		return result;
	}

	@Override
	public int getApprCount(String userId) throws Exception {
		return approvalLineDAO.getApprCount(userId);
	}

	@Override
	public String getRejectNm(String rejectCd) throws Exception {
		return approvalLineDAO.getRejectNm(rejectCd);
	}
	
	@Override
	public List<SecuApprovalLineVO> getRnsProhibitInfo(SecuApprovalLineVO approvalLineVO) throws Exception {		
		return approvalLineDAO.getRnsProhibitInfo(approvalLineVO);		
	}
	
	@Override
	public List<SecuApprovalLineVO> getProhibitList(SecuApprovalLineVO approvalLineVO) throws Exception {		
		return approvalLineDAO.getProhibitList(approvalLineVO);		
	}
	
	@Override
	public List<SecuApprovalLineVO> getMailProhibitInfo(SecuApprovalLineVO approvalLineVO) throws Exception {		
		return approvalLineDAO.getMailProhibitInfo(approvalLineVO);		
	}
	
	
}
