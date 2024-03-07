/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 서비스 구현 ==>계정관리 서비스 구현
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.enders.ums.sys.service ==> kr.co.enders.ums.sys.acc.service
 *                계정 관련 외의 함수 제거 
 */
package kr.co.enders.ums.sys.acc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.enders.ums.sys.acc.dao.AccountDAO;
import kr.co.enders.ums.sys.acc.vo.ComplianceVO;
import kr.co.enders.ums.sys.acc.vo.DeptProgVO;
import kr.co.enders.ums.sys.acc.vo.DeptVO;
import kr.co.enders.ums.sys.acc.vo.OrganizationVO;
import kr.co.enders.ums.sys.acc.vo.ServiceVO;
import kr.co.enders.ums.sys.acc.vo.UserOrgVO;
import kr.co.enders.ums.sys.acc.vo.UserProgVO;
import kr.co.enders.ums.sys.acc.vo.UserVO;
import kr.co.enders.ums.sys.aut.vo.FuncUserVO;
import kr.co.enders.ums.sys.aut.vo.MenuUserMappVO;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	private AccountDAO accountDAO;

	@Override
	public List<DeptVO> getDeptList(DeptVO deptVO) throws Exception {
		return accountDAO.getDeptList(deptVO);
	}

	@Override
	public DeptVO getDeptInfo(DeptVO deptVO) throws Exception {
		return accountDAO.getDeptInfo(deptVO);
	}

	@Override
	public int insertDeptInfo(DeptVO deptVO) throws Exception {
		return accountDAO.insertDeptInfo(deptVO);
	}

	@Override
	public int updateDeptInfo(DeptVO deptVO) throws Exception {
		return  accountDAO.updateDeptInfo(deptVO);
	}

	@Override
	public int deleteDeptInfo(DeptVO deptVO) throws Exception {
		return  accountDAO.deleteDeptInfo(deptVO);
	}
	
	@Override
	public OrganizationVO getOrgInfo(String orgCd) throws Exception {
		return accountDAO.getOrgInfo(orgCd);
	} 
	
	@Override
	public List<OrganizationVO> getOrgList(OrganizationVO orgVO) throws Exception {
		return accountDAO.getOrgList(orgVO);
	}
	
	@Override
	public List<OrganizationVO> getOrgInfoList(OrganizationVO orgVO) throws Exception {
		return accountDAO.getOrgInfoList(orgVO);
	}
	
	@Override
	public String getOrgListTreeParent(String orgCd) throws Exception {
		return accountDAO.getOrgListTreeParent(orgCd);
	}
	 
	@Override
	public List<OrganizationVO> getOrgChildList(String upOrgCd) throws Exception {
		return accountDAO.getOrgChildList(upOrgCd);	
	}
	
	@Override
	public OrganizationVO getOrgInfoView(String orgCd) throws Exception {
		return accountDAO.getOrgInfoView(orgCd);
	} 
	 
	@Override
	public List<OrganizationVO> getOrgListView(OrganizationVO orgVO) throws Exception {
		return accountDAO.getOrgListView(orgVO);
	} 
	
	@Override
	public List<OrganizationVO> getOrgChildListView(String upOrgCd) throws Exception {
		return accountDAO.getOrgChildListView(upOrgCd);
	} 
	
	@Override
	public String getOrgCodeMax(String lvlVal) throws Exception {
		return accountDAO.getOrgCodeMax(lvlVal);
	}
	
	@Override
	public int getOrgUserCount(OrganizationVO orgVO) throws Exception {
		return accountDAO.getOrgUserCount(orgVO);
	}
	
	@Override
	public int insertOragnizationInfo(OrganizationVO orgVO) throws Exception {
		return accountDAO.insertOragnizationInfo(orgVO);		
	}

	@Override
	public int updateOrganizationInfo(OrganizationVO orgVO) throws Exception {
		return accountDAO.updateOrganizationInfo(orgVO);
	}
	
	@Override
	public int deleteOrganizationInfo(OrganizationVO orgVO) throws Exception {
		return accountDAO.deleteOrganizationInfo(orgVO);
	} 
	
	@Override
	public List<UserVO> getUserList(UserVO userVO) throws Exception {
		return accountDAO.getUserList(userVO);
	}
	
	@Override
	public List<UserVO> getOrgUserList(UserVO userVO) throws Exception {
		return accountDAO.getOrgUserList(userVO);
	}
	 
	@Override
	public List<UserVO> getUserListSimple(UserVO userVO) throws Exception {
		return accountDAO.getUserListSimple(userVO);
	}	
	
	@Override
	public UserVO getUserInfo(UserVO userVO) throws Exception {
		return accountDAO.getUserInfo(userVO);
	}
	
	@Override
	public UserVO getUserInfoDetail(UserVO userVO) throws Exception {
		return accountDAO.getUserInfoDetail(userVO);
	}
	
	@Override public List<UserVO> getUserServiceList(UserVO userVO) throws Exception {
		return accountDAO.getUserServiceList(userVO); 
	}
	
	@Override public List<ServiceVO> getServiceList(ServiceVO serviceVO) throws Exception {
		return accountDAO.getServiceList(serviceVO); 
	}
	
	@Override
	public List<UserVO> userIdCheck(String userId) throws Exception {
		return accountDAO.userIdCheck(userId);
	}

	@Override
	public int insertUserInfo(UserVO userVO) throws Exception {
		return accountDAO.insertUserInfo(userVO);		
	}

	@Override
	public int updateUserInfo(UserVO userVO) throws Exception {
		return accountDAO.updateUserInfo(userVO);
	}
	
	@Override
	public int deleteUserInfo(UserVO userVO) throws Exception {
		return accountDAO.deleteUserInfo(userVO);
	}
	
	@Override
	public int insertUserProgInfo(UserProgVO userProgVO) throws Exception {
		return accountDAO.insertUserProgInfo(userProgVO); 
	}
	
	@Override
	public int deleteUserProgInfo(UserProgVO userProgVO) throws Exception {
		return accountDAO.deleteUserProgInfo(userProgVO);
	}
	
	@Override
	public int insertUserOrgInfo(UserOrgVO userOrgVO) throws Exception {
		return accountDAO.insertUserOrgInfo(userOrgVO); 
	}
	
	@Override
	public int updateUserOrgInfo(UserOrgVO userOrgVO) throws Exception {
		return accountDAO.updateUserOrgInfo(userOrgVO);
	}
	
	@Override
	public int deleteUserOrgInfo(UserOrgVO userOrgVO) throws Exception {
		return accountDAO.deleteUserOrgInfo(userOrgVO);		
	}
	
	@Override
	public int updateServiceLink(UserVO userVO) throws Exception {
		return accountDAO.updateServiceLink(userVO);		
	}
	
	@Override
	public int deleteServiceLink(UserVO userVO) throws Exception {
		return accountDAO.deleteServiceLink(userVO);		
	}
	
	@Override
	public int checkUserPwd(UserVO userVO) throws Exception {
		return accountDAO.checkUserPwd(userVO);
	} 
	
	@Override
	public int updateUserPwd(UserVO userVO) throws Exception {
		return accountDAO.updateUserPwd(userVO);
	}

	@Override
	public int deleteUserSysMenu(UserVO userVO) throws Exception {
		return accountDAO.deleteUserSysMenu(userVO);
	}
	
	@Override
	public int insertUserDefaultMenu(UserProgVO userProgVO) throws Exception {
		return accountDAO.insertUserDefaultMenu(userProgVO);
	}
	
	@Override
	public List<ComplianceVO> selectComplianceUserList(ComplianceVO complianceVO) throws Exception {
		return accountDAO.getComplianceUserList(complianceVO);
	}
	@Override
	public int updateComplianceUser(ComplianceVO complianceVO) throws Exception {
		
		int result = accountDAO.deleteComplianceUser(complianceVO.getServiceGb());
		
		if(result > -1) {
			if(complianceVO.getUserIds() != null && !"".equals(complianceVO.getUserIds())) {
				String[] userIds = complianceVO.getUserIds().split(",");
				for(int i=0; i<userIds.length; i++) {
					ComplianceVO complVO = new ComplianceVO();
					complVO.setServiceGb(complianceVO.getServiceGb());
					complVO.setUserId(userIds[i]);
					complVO.setComplianceNo(i+1);
					complVO.setRegId(complianceVO.getRegId());
					complVO.setRegDt(complianceVO.getRegDt());
					result += accountDAO.insertComplianceUser(complVO);
				}
			}
		}
		return result;
	}

	@Override
	public List<DeptVO> getDeptServiceList(DeptVO deptVO) throws Exception {
		return accountDAO.getDeptServiceList(deptVO);
	}

	@Override
	public int insertDeptProgInfo(DeptProgVO deptProgVO) throws Exception {
		return accountDAO.insertDeptProgInfo(deptProgVO);
	}

	@Override
	public int deleteDeptProgInfo(DeptProgVO deptProgVO) throws Exception {
		return accountDAO.deleteDeptProgInfo(deptProgVO);
	}

	@Override
	public int deleteDeptSysMenu(DeptVO deptVO) throws Exception {
		return accountDAO.deleteDeptSysMenu(deptVO);
	}

	@Override
	public int insertDeptDefaultMenu(DeptProgVO deptProgVO) throws Exception {
		return accountDAO.insertDeptDefaultMenu(deptProgVO);
	}

	@Override
	public int deleteUserOath(String userId) throws Exception {
		return accountDAO.deleteUserOath(userId);
	}

	@Override
	public int updateUserOath(String userId) throws Exception {
		return accountDAO.updateUserOath(userId);
	}

	@Override
	public int initUserPwd(UserVO userVO) throws Exception {
		return accountDAO.initUserPwd(userVO);
	}

	@Override
	public UserVO sendInitUserPwdMail(UserVO userVO) throws Exception {
		return accountDAO.sendInitUserPwdMail(userVO);
	} 
	
	@Override
	public int copyUserInfoVO(UserVO copyUserInfoVO) throws Exception {
		int result = -1;
		
		result = accountDAO.copyUserInfo(copyUserInfoVO);
		if(result < 1 ) {
			return result;
		}
		
		UserOrgVO copyUserOrgVO= new UserOrgVO();
		copyUserOrgVO.setUserId(copyUserInfoVO.getUserId());
		copyUserOrgVO.setCopyUserId(copyUserInfoVO.getCopyUserId());
		result = accountDAO.copyUserOrgInfo(copyUserOrgVO);
		if(result < 1 ) {
			return result;
		}
		
		UserProgVO copyUserProgVO = new UserProgVO();
		copyUserProgVO.setUserId(copyUserInfoVO.getUserId());
		copyUserProgVO.setCopyUserId(copyUserInfoVO.getCopyUserId());
		result = accountDAO.copyUserServiceInfo(copyUserProgVO);
		if(result < 1 ) {
			return result;
		}
		
		MenuUserMappVO copyMenuUserMappVO = new MenuUserMappVO();
		copyMenuUserMappVO.setUserId(copyUserInfoVO.getUserId());
		copyMenuUserMappVO.setCopyUserId(copyUserInfoVO.getCopyUserId());
		result = accountDAO.copyMenuUserMappInfo(copyMenuUserMappVO);
		if(result < 1 ) {
			return result;
		}
		
		
		//사요ㅈ
		//public UserOrgVO copyUserInfo(UserVO copyUserInfoVO)  throws Exception;	NEO_USER 
		
		/*//조직 UserOrgVO
		public UserOrgVO copyUserOrgVO(UserOrgVO UserOrgVO)  throws Exception;	NEO_USERORG_MAPP
		
		//기능 FuncUserVO
		public FuncUserVO copyFuncUserVO(FuncUserVO funcUserVO)  throws Exception;	neo_func_perm

		
		//메뉴  UserProgVO
		public UserProgVO copyUserServiceVO(UserProgVO userProgVO)  throws Exception;	neo_user_prog

		
		//DB 연결 :DbCOnnUserVO
		public DbConnUserVO copyDbConnUserVO(DbConnUserVO dbConnUserVO)  throws Exception;	neo_dbconn_perm

		
		//메뉴 사용 자 : MenuUserMappVO
		public MenuUserMappVO copyMenuUserMapp(MenuUserMappVO menuUserMappVO)  throws Exception; neo_menuuser_mapp

		*/
		 
		
		return result;
		
	}
	
	
}
