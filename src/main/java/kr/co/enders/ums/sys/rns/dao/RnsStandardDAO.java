/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.08
 * 설명 : 도메인 정보 관리
 */
package kr.co.enders.ums.sys.rns.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.enders.ums.sys.rns.vo.DomainInfoVO;
import kr.co.enders.ums.sys.rns.vo.MailClinicInfoVO;

@Repository
public class RnsStandardDAO implements RnsStandardMapper {
	@Autowired
	private SqlSession sqlSessionEms;
	
	@Override
	public List<DomainInfoVO> getDomainList(DomainInfoVO domainInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).getDomainList(domainInfoVO);
	}
	
	@Override
	public DomainInfoVO getDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).getDomainInfo(domainInfoVO);
	}
		
	@Override
	public int insertDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).insertDomainInfo(domainInfoVO);
	}	

	@Override
	public int updateDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).updateDomainInfo(domainInfoVO);
	}
	
	@Override
	public int deleteDomainInfo(DomainInfoVO domainInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).deleteDomainInfo(domainInfoVO);
	}	
	
	@Override
	public int updateMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).updateMailClinic(mailClinicInfoVO);
	}
	@Override
	public int deleteMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).deleteMailClinic(mailClinicInfoVO);
	}
	@Override
	public List<MailClinicInfoVO> getMailClinicList(MailClinicInfoVO mailClinicInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).getMailClinicList(mailClinicInfoVO);
	}
	@Override
	public int updateAllMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).updateAllMailClinic(mailClinicInfoVO);
	}
	@Override
	public int deleteAllMailClinic(MailClinicInfoVO mailClinicInfoVO) throws Exception {
		return sqlSessionEms.getMapper(RnsStandardMapper.class).deleteAllMailClinic(mailClinicInfoVO);
	}
}
