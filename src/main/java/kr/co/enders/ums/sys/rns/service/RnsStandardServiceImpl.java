/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 도메인 정보 관리
 */
package kr.co.enders.ums.sys.rns.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.enders.ums.sys.rns.dao.RnsStandardDAO;
import kr.co.enders.ums.sys.rns.vo.DomainInfoVO;
import kr.co.enders.ums.sys.rns.vo.MailClinicInfoVO;
import kr.co.enders.util.Code;
import kr.co.enders.util.StringUtil;

@Service
public class RnsStandardServiceImpl implements RnsStandardService {
	@Autowired
	private RnsStandardDAO rnsStandardDAO;
	
	private Logger logger = Logger.getLogger(this.getClass());
 
	@Override
	public List<DomainInfoVO> getDomainList(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.getDomainList(domainInfoVO);
	}
	public DomainInfoVO getDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.getDomainInfo(domainInfoVO);
	}
	@Override
	public int insertDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.insertDomainInfo(domainInfoVO);
	}

	@Override 
	public int updateDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.updateDomainInfo(domainInfoVO);
	}
	
	@Override 
	public int deleteDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return rnsStandardDAO.deleteDomainInfo(domainInfoVO);
	}	
	
	@Override 
	public int updateMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception {			
		return rnsStandardDAO.updateMailClinic(mailClinicInfoVO);
	}
	
	@Override 
	public int deleteMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception {
		return rnsStandardDAO.deleteMailClinic(mailClinicInfoVO);
	}
	
	@Override 
	public List<MailClinicInfoVO> getMailClinicList(MailClinicInfoVO mailClinicInfoVO) throws Exception {
		return rnsStandardDAO.getMailClinicList(mailClinicInfoVO);
	}
	
	@Override 
	public int updateAllMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception {	
		
		int rnt = -1;
		
		rnt = rnsStandardDAO.updateAllMailClinic(mailClinicInfoVO);
						
		if(mailClinicInfoVO.getArrDelMonth().length > 0) { //일괄적용일 경우			
			rnsStandardDAO.deleteAllMailClinic(mailClinicInfoVO);
		}
		
		return rnt;
	}
}
