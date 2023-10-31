/**
 * 작성자 : 김준희
 * 작성일시 : 2021.08.26
 * 설명 : 사용자메뉴 권한 관리 처리
 */
package kr.co.enders.ums.sys.aut.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.enders.ums.sys.aut.vo.FuncGrpPermVO;
import kr.co.enders.ums.sys.aut.vo.FuncGrpUserVO;
import kr.co.enders.ums.sys.aut.vo.FuncPermVO;
import kr.co.enders.ums.sys.aut.vo.FuncUserVO;
import kr.co.enders.ums.sys.aut.vo.MenuGroupMappVO;
import kr.co.enders.ums.sys.aut.vo.MenuUserMappVO;

@Repository
public class AuthDAO implements AuthMapper {
	@Autowired
	private SqlSession sqlSessionEms;

	@Override
	public List<MenuUserMappVO> getUserAuthList(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserAuthList(menuUserMappVO);
	}

	@Override
	public MenuUserMappVO getMenuUserMappInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getMenuUserMappInfo(menuUserMappVO);
	}

	@Override
	public int insertUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).insertUserAuth(menuUserMappVO);
	}

	@Override
	public int deleteUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).deleteUserAuth(menuUserMappVO);
	}  

	@Override
	public int deleteMenuUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).deleteMenuUserAuth(menuUserMappVO);
	}  
	
	@Override
	public List<MenuUserMappVO> getUserAuthInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserAuthInfo(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getUserAuthChildCount(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserAuthChildCount(menuUserMappVO);
	}

	@Override
	public List<MenuUserMappVO> getUserMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserMenuInfo(menuUserMappVO);
	}	
	
	@Override
	public List<MenuUserMappVO> getUserAccessMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserAccessMenuInfo(menuUserMappVO);
	}

	
	@Override
	public List<MenuUserMappVO> getMenuUserList(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getMenuUserList(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getMenuUserSimple(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getMenuUserSimple(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getMenuList(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getMenuList(menuUserMappVO);
	}

	@Override
	public List<FuncPermVO> getFuncPermAuthList(FuncPermVO funcPermVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getFuncPermAuthList(funcPermVO);
	}

	@Override
	public List<FuncGrpPermVO> getFuncGrpPermAuthList(FuncGrpPermVO funcGrpPermVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getFuncGrpPermAuthList(funcGrpPermVO);
	}	
	
	@Override
	public int deleteFuncPermAuthInfo(String funcCd) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).deleteFuncPermAuthInfo(funcCd);
	}
	
	@Override
	public int insertFuncPermAuthInfo(FuncPermVO funcPermVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).insertFuncPermAuthInfo(funcPermVO);
	}
	
	@Override
	public int deleteFuncGrpPermAuthInfo(String funcCd) throws Exception{
		return sqlSessionEms.getMapper(AuthMapper.class).deleteFuncGrpPermAuthInfo(funcCd);
	}
	
	@Override
	public int insertFuncGrpPermAuthInfo(FuncGrpPermVO funcGrpPermVO) throws Exception{
		return sqlSessionEms.getMapper(AuthMapper.class).insertFuncGrpPermAuthInfo(funcGrpPermVO);
	}
	
	@Override
	public List<FuncUserVO> getFuncUserList(FuncUserVO funcUserVO) throws Exception{
		return sqlSessionEms.getMapper(AuthMapper.class).getFuncUserList(funcUserVO);
	}
	
	@Override
	public List<FuncGrpUserVO> getFuncGrpUserList(FuncGrpUserVO funcGrpUserVO) throws Exception{
		return sqlSessionEms.getMapper(AuthMapper.class).getFuncGrpUserList(funcGrpUserVO);
	}
	@Override
	public List<MenuGroupMappVO> getGroupAuthInfo(MenuGroupMappVO menuGroupMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getGroupAuthInfo(menuGroupMappVO);
	}

	@Override
	public List<MenuGroupMappVO> getGroupAuthList(MenuGroupMappVO menuGroupMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getGroupAuthList(menuGroupMappVO);
	}
	
	@Override
	public int deleteGroupAuth(MenuGroupMappVO menuGroupMappVO) throws Exception{
		return sqlSessionEms.getMapper(AuthMapper.class).deleteGroupAuth(menuGroupMappVO);
	}

	@Override
	public int insertGroupAuth(MenuGroupMappVO menuGroupMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).insertGroupAuth(menuGroupMappVO);
	}

	@Override
	public List<MenuGroupMappVO> getGroupMenuInfo(MenuGroupMappVO menuGroupMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getGroupMenuInfo(menuGroupMappVO);
	}
}
