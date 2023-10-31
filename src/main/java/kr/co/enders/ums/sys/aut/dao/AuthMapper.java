/**
 * 작성자 : 김준희
 * 작성일시 : 2021.08.26
 * 설명 : 사용자메뉴 권한 매퍼 
 */
package kr.co.enders.ums.sys.aut.dao;

import java.util.List;
import kr.co.enders.ums.sys.aut.vo.FuncPermVO;
import kr.co.enders.ums.sys.aut.vo.FuncUserVO;
import kr.co.enders.ums.sys.aut.vo.MenuGroupMappVO;
import kr.co.enders.ums.sys.aut.vo.FuncGrpPermVO;
import kr.co.enders.ums.sys.aut.vo.FuncGrpUserVO;
import kr.co.enders.ums.sys.aut.vo.MenuUserMappVO;  

public interface AuthMapper {
	/**
	 * 사용자메뉴 권한  목록 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserAuthList(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자메뉴 권한  정보 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public MenuUserMappVO getMenuUserMappInfo(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자메뉴 권한  정보 등록
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public int insertUserAuth(MenuUserMappVO menuUserMappVO) throws Exception;

	/**
	 * 사용자메뉴 권한  정보 삭제
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public int deleteUserAuth(MenuUserMappVO menuUserMappVO) throws Exception;

	/**
	 * 메뉴 사용 권한  정보 삭제
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMenuUserAuth(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자메뉴 권한  목록 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserAuthInfo(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자메뉴 하위 메뉴 갯수 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserAuthChildCount(MenuUserMappVO menuUserMappVO) throws Exception;
	 
	/**
	 * 사용자 권한 부여가능한 메뉴 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자 메뉴 목록 조회 - 메뉴기준-
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserAccessMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception;

	/**
	 * 메뉴별 사용자 목록 조회 - 메뉴기준
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getMenuUserList(MenuUserMappVO menuUserMappVO) throws Exception;	
	
	/**
	 * 메뉴별 사용자 목록 조회 - 사용자만
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getMenuUserSimple(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	* 공통설정을 제외한 메뉴 조회
 	* @param menuUserMappVO
 	* @return
 	* @throws Exception
 	*/
	public List<MenuUserMappVO> getMenuList(MenuUserMappVO menuUserMappVO) throws Exception;
	
	
	/**
	 * 권한 매핑 사용자 조회 
	 * @param funcPermVO
	 * @return
	 * @throws Exception
	 */
	public List<FuncPermVO> getFuncPermAuthList(FuncPermVO funcPermVO) throws Exception;
	
	/**
	 * 사용자 그룹조회
	 * @param funcGrpPermVO
	 * @return
	 * @throws Exception
	 */
	public List<FuncGrpPermVO> getFuncGrpPermAuthList(FuncGrpPermVO funcGrpPermVO) throws Exception;
	
	/**
	 * 
	 * @param funcCd
	 * @return
	 * @throws Exception
	 */
	public int deleteFuncPermAuthInfo(String funcCd) throws Exception;
	
	/**
	 * 
	 * @param funcPermVO
	 * @return
	 * @throws Exception
	 */
	public int insertFuncPermAuthInfo(FuncPermVO funcPermVO) throws Exception;
	
	/**
	 * 
	 * @param funcCd
	 * @return
	 * @throws Exception
	 */
	public int deleteFuncGrpPermAuthInfo(String funcCd) throws Exception;
	
	/**
	 * 
	 * @param funcGrpPermVO
	 * @return
	 * @throws Exception
	 */
	public int insertFuncGrpPermAuthInfo(FuncGrpPermVO funcGrpPermVO) throws Exception;
	
	/**
	 * 
	 * @param funcUserVO
	 * @return
	 * @throws Exception
	 */
	public List<FuncUserVO> getFuncUserList (FuncUserVO funcUserVO) throws Exception;
	
	/**
	 * 
	 * @param funcGrpUserVO
	 * @return
	 * @throws Exception
	 */
	public List<FuncGrpUserVO> getFuncGrpUserList(FuncGrpUserVO funcGrpUserVO) throws Exception;

	/**
	 * 사용자그룹 권한 목록 조회
	 * @param menuGroupMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuGroupMappVO> getGroupAuthList(MenuGroupMappVO menuGroupMappVO) throws Exception;
	/**
	 * 사용자그룹 부여가능한 메뉴 조회
	 * @param menuGroupMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuGroupMappVO> getGroupAuthInfo(MenuGroupMappVO menuGroupMappVO) throws Exception;
	/**
	 * 사용자그룹 권한 삭제
	 * @param menuGroupMappVO
	 * @return
	 * @throws Exception
	 */
	public int deleteGroupAuth(MenuGroupMappVO menuGroupMappVO) throws Exception;
	/**
	 * 사용자그룹 권한 등록
	 * @param menuGroupMappVO
	 * @return
	 * @throws Exception
	 */
	public int insertGroupAuth(MenuGroupMappVO menuGroupMappVO) throws Exception;
	/**
	 * 사용자그룹 권한 등록
	 * @param menuGroupMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuGroupMappVO> getGroupMenuInfo(MenuGroupMappVO menuGroupMappVO) throws Exception;
}

