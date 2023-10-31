/**
 * 작성자 : 김준희
 * 작성일시 : 2021.07.07
 * 설명 : 사용자메뉴사용 권한
 */
package kr.co.enders.ums.sys.aut.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.enders.ums.sys.aut.dao.AuthDAO;
import kr.co.enders.ums.sys.aut.vo.FuncGrpPermVO;
import kr.co.enders.ums.sys.aut.vo.FuncGrpUserVO;
import kr.co.enders.ums.sys.aut.vo.FuncPermVO;
import kr.co.enders.ums.sys.aut.vo.FuncUserVO;
import kr.co.enders.ums.sys.aut.vo.MenuGroupMappVO;
import kr.co.enders.ums.sys.aut.vo.MenuUserMappVO; 

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private AuthDAO authDAO;

	@Override
	public List<MenuUserMappVO> getUserAuthList(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserAuthList(menuUserMappVO);
	}

	@Override
	public MenuUserMappVO getMenuUserMappInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getMenuUserMappInfo(menuUserMappVO);
	}

	@Override
	public int insertUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.insertUserAuth(menuUserMappVO);
	}
 
	@Override
	public int deleteUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return  authDAO.deleteUserAuth(menuUserMappVO);
	}
	 
	@Override
	public int deleteMenuUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return  authDAO.deleteMenuUserAuth(menuUserMappVO);
	}
	 
	
	@Override
	public List<MenuUserMappVO> getUserAuthInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserAuthInfo(menuUserMappVO);
	}

	@Override
	public List<MenuUserMappVO> getUserAuthChildCount(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserAuthChildCount(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getUserMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserMenuInfo(menuUserMappVO);
	}	
	
	@Override
	public List<MenuUserMappVO> getUserAccessMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserAccessMenuInfo(menuUserMappVO);
	}

	@Override
	public List<MenuUserMappVO> getMenuUserList(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getMenuUserList(menuUserMappVO);
	}
	

	@Override
	public List<MenuUserMappVO> getMenuUserSimple(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getMenuUserSimple(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getMenuList(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getMenuList(menuUserMappVO);
	}
	
	@Override
	public List<FuncPermVO> getFuncPermAuthList(FuncPermVO funcPermVO) throws Exception {
		return authDAO.getFuncPermAuthList(funcPermVO);
	}

	@Override
	public List<FuncGrpPermVO> getFuncGrpPermAuthList(FuncGrpPermVO funcGrpPermVO) throws Exception {
		return authDAO.getFuncGrpPermAuthList(funcGrpPermVO);
	}

	@Override
	public int deleteFuncPermAuthInfo(String funcCd) throws Exception {
		return authDAO.deleteFuncPermAuthInfo(funcCd);
	}
	
	@Override
	public int insertFuncPermAuthInfo(FuncPermVO funcPermVO) throws Exception {
		return authDAO.insertFuncPermAuthInfo(funcPermVO);
	}
	
	@Override
	public int deleteFuncGrpPermAuthInfo(String funcCd) throws Exception{
		return authDAO.deleteFuncGrpPermAuthInfo(funcCd);
	}
	
	@Override
	public int insertFuncGrpPermAuthInfo(FuncGrpPermVO funcGrpPermVO) throws Exception{
		return authDAO.insertFuncGrpPermAuthInfo(funcGrpPermVO);
	}

	@Override
	public List<FuncUserVO> getFuncUserList(FuncUserVO funcUserVO) throws Exception {
		return authDAO.getFuncUserList(funcUserVO);
	}

	@Override
	public List<FuncGrpUserVO> getFuncGrpUserList(FuncGrpUserVO funcGrpUserVO) throws Exception {
		return authDAO.getFuncGrpUserList(funcGrpUserVO);
	}

	@Override
	public List<MenuGroupMappVO> getGroupAuthInfo(MenuGroupMappVO menuGroupMappVO) throws Exception {
		return authDAO.getGroupAuthInfo(menuGroupMappVO);
	}

	@Override
	public List<MenuGroupMappVO> getGroupAuthList(MenuGroupMappVO menuGroupMappVO) throws Exception {
		return authDAO.getGroupAuthList(menuGroupMappVO);
	}

	@Override
	public int deleteGroupAuth(MenuGroupMappVO menuGroupMappVO) throws Exception {
		return authDAO.deleteGroupAuth(menuGroupMappVO);
	}

	@Override
	public int insertGroupAuth(MenuGroupMappVO menuGroupMappVO) throws Exception {
		return authDAO.insertGroupAuth(menuGroupMappVO);
	}

	@Override
	public List<MenuGroupMappVO> getGroupMenuInfo(MenuGroupMappVO menuGroupMappVO) throws Exception {
		return authDAO.getGroupMenuInfo(menuGroupMappVO);
	}
	
}
