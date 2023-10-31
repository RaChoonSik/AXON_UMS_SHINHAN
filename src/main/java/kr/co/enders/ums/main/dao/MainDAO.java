/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 메인화면 데이터 처리
 */
package kr.co.enders.ums.main.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.enders.ums.main.vo.EmsMainVO;
import kr.co.enders.ums.main.vo.MenuVO;
import kr.co.enders.ums.sys.acc.vo.ServiceVO;
import kr.co.enders.ums.sys.acc.vo.SysMenuVO;
import kr.co.enders.ums.sys.acc.vo.UserVO;

@Repository
public class MainDAO implements MainMapper {
	@Autowired
	SqlSession sqlSessionEms;

	@Override
	public List<MenuVO> getTopMenuList(String uilang) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getTopMenuList(uilang);
	}

	@Override
	public SysMenuVO getMenuBasicInfo(String menuId) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getMenuBasicInfo(menuId);
	}

	@Override
	public SysMenuVO getServiceUserMenu(SysMenuVO sysMenuVO) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getServiceUserMenu(sysMenuVO);
	}

	@Override
	public SysMenuVO getUserMenuAuth(SysMenuVO sysMenuVO) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getUserMenuAuth(sysMenuVO);
	}

	@Override
	public List<ServiceVO> getUserService(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getUserService(userVO);
	}

	@Override
	public SysMenuVO getSourcePathMenu(String sourcePath) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getSourcePathMenu(sourcePath);
	}
	
	@Override
	public List<EmsMainVO> getUserInfo(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getUserInfo(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getUserApprov(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getUserApprov(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailSummary(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getDayMailSummary(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailOpen(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getDayMailOpen(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailDetailErr(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getDayMailDetailErr(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailDomain(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getDayMailDomain(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailCampain(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getDayMailCampain(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getMailSendInfoDay(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getMailSendInfoDay(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getMailSendInfoMons(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getMailSendInfoMons(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailSendSch(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getDayMailSendSch(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getUserMenu(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getUserMenu(emsMainVo);
	}
	
	@Override
	public int insertMainQuickMenu(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).insertMainQuickMenu(emsMainVo);
	}
	
	@Override
	public int deleteMainQuickMenu(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).deleteMainQuickMenu(emsMainVo);
	}
	
	@Override
	public int deleteAllMainQuickMenu(EmsMainVO emsMainVo) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).deleteAllMainQuickMenu(emsMainVo);
	}
}
