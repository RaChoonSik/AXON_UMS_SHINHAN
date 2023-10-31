/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 사용자 로그인 데이터 처리
 */
package kr.co.enders.ums.lgn.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.enders.ums.lgn.vo.LoginHistVO;
import kr.co.enders.ums.lgn.vo.LoginVO;
import kr.co.enders.ums.sys.acc.vo.SysMenuVO;
import kr.co.enders.ums.sys.acc.vo.UserProgVO;
import kr.co.enders.ums.sys.acc.vo.UserVO;

@Repository
public class LoginDAO implements LoginMapper {
	@Autowired
	SqlSession sqlSessionEms;

	@Override
	public UserVO isValidUser(LoginVO loginVO) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).isValidUser(loginVO);
	} 

	@Override
	public List<UserProgVO> getUserProgList(String userId) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).getUserProgList(userId);		
	}
 
	@Override
	public List<SysMenuVO> getUserMenuList(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).getUserMenuList(userVO);
	}
	
	@Override
	public UserVO isSSOUser(LoginVO loginVO) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).isSSOUser(loginVO);
	}

	@Override
	public String isOathUser(String userId) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).isOathUser(userId);
	}

	@Override
	public int updateLoginDt(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).updateLoginDt(userVO);
	}

	@Override
	public int updatePwdErrorCnt(String userId) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).updatePwdErrorCnt(userId);
	}

	@Override
	public int updateCertilock(String userId) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).updateCertilock(userId);
	}

	@Override
	public int getPwerrorCnt(String userId) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).getPwerrorCnt(userId);
	}

	@Override
	public UserVO getPwdErrorInfo(String userId) throws Exception {
		return sqlSessionEms.getMapper(LoginMapper.class).getPwdErrorInfo(userId);
	}	
}
