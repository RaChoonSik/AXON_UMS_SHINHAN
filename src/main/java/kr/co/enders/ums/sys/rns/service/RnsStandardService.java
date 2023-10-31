/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 도메인 정보 관리
 */
package kr.co.enders.ums.sys.rns.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.enders.ums.sys.rns.vo.DomainInfoVO;
import kr.co.enders.ums.sys.rns.vo.MailClinicInfoVO;

@Service
public interface RnsStandardService {
 
	/**
	 * 도메인 리스트 조회
	 * @param domainInfoVO
	 * @return
	 * @throws Exception
	 */
	public List<DomainInfoVO> getDomainList(DomainInfoVO domainInfoVO) throws Exception;
	 
	/**
	* 도메인 정보 조회
	* @param domainInfoVO
	* @return
	* @throws Exception
	*/	
	public DomainInfoVO getDomainInfo(DomainInfoVO domainInfoVO) throws Exception;

	/**
	 * 도메인 정보 등록
	 * @param actionLogVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManagerEms")	
	public int insertDomainInfo(DomainInfoVO domainInfoVO) throws Exception;
	
	/**
	 * 도메인 정보 수정
	 * @param actionLogVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManagerEms")	
	public int updateDomainInfo(DomainInfoVO domainInfoVO) throws Exception;

	/**
	 * 도메인 정보 삭제
	 * @param actionLogVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManagerEms")	
	public int deleteDomainInfo(DomainInfoVO domainInfoVO) throws Exception;
	
	/**
	 * 메일클리닉 일정관리 수정,저장
	 * @param mailClinicInfoVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManagerEms")	
	public int updateMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception;
	
	/**
	 * 메일클리닉 일정관리 삭제
	 * @param mailClinicInfoVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManagerEms")	
	public int deleteMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception;
	
	/**
	 * 메일클리닉 일정관리 조회
	 * @param mailClinicInfoVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManagerEms")	
	public List<MailClinicInfoVO> getMailClinicList(MailClinicInfoVO mailClinicInfoVO) throws Exception;
 
	/**
	 * 메일클리닉 일정관리 일괄수정,저장
	 * @param mailClinicInfoVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManagerEms")	
	public int updateAllMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception;
	
}
