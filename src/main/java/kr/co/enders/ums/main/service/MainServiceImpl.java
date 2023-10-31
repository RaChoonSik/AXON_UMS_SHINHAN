/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 메인화면 서비스 구현
 */
package kr.co.enders.ums.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.enders.ums.main.dao.MainDAO;
import kr.co.enders.ums.sys.acc.dao.AccountDAO;
import kr.co.enders.ums.main.vo.EmsMainVO;
import kr.co.enders.ums.main.vo.MenuVO;
import kr.co.enders.ums.sys.acc.vo.ServiceVO;
import kr.co.enders.ums.sys.acc.vo.SysMenuVO;
import kr.co.enders.ums.sys.acc.vo.UserProgVO;
import kr.co.enders.ums.sys.acc.vo.UserVO;

@Service
public class MainServiceImpl implements MainService {
	@Autowired
	private MainDAO mainDAO;
	
	@Autowired
	private AccountDAO accountDAO;

	@Override
	public List<MenuVO> getTopMenuList(String uilang)  throws Exception {
		return mainDAO.getTopMenuList(uilang);
	}

	@Override
	public SysMenuVO getMenuBasicInfo(String menuId) throws Exception {
		return mainDAO.getMenuBasicInfo(menuId);
	}

	@Override
	public SysMenuVO getServiceUserMenu(SysMenuVO sysMenuVO) throws Exception {
		return mainDAO.getServiceUserMenu(sysMenuVO);
	}

	@Override
	public SysMenuVO getUserMenuAuth(SysMenuVO sysMenuVO) throws Exception {
		return mainDAO.getUserMenuAuth(sysMenuVO);
	}
 
	@Override
	public List<ServiceVO> getUserService(UserVO userVO) throws Exception {
		return mainDAO.getUserService(userVO);
	}

	@Override
	public SysMenuVO getSourcePathMenu(String sourcePath) throws Exception {
		return mainDAO.getSourcePathMenu(sourcePath);
	}
	
	@Override
	public List<EmsMainVO> getUserInfo(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getUserInfo(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getUserApprov(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getUserApprov(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailSummary(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getDayMailSummary(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailOpen(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getDayMailOpen(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailDetailErr(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getDayMailDetailErr(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailDomain(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getDayMailDomain(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailCampain(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getDayMailCampain(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getMailSendInfoDay(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getMailSendInfoDay(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getMailSendInfoMons(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getMailSendInfoMons(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getDayMailSendSch(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getDayMailSendSch(emsMainVo);
	}
	
	@Override
	public List<EmsMainVO> getUserMenu(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.getUserMenu(emsMainVo);
	}
	
	@Override
	public int insertMainQuickMenu(EmsMainVO emsMainVo) throws Exception {
		int intCnt = 1;
		
		mainDAO.deleteAllMainQuickMenu(emsMainVo);	
		if(emsMainVo.getArrDelQmenu() != null && emsMainVo.getArrDelQmenu().length > 0) {
			String menuId = "";
			for(int i=0; i < emsMainVo.getArrDelQmenu().length; i ++) {
				
				menuId = emsMainVo.getArrDelQmenu()[i];
				if(!"".equals(menuId) && menuId != null ) {
					emsMainVo.setMenuId(menuId);
					intCnt = mainDAO.insertMainQuickMenu(emsMainVo);
				}
			}
		}
		return intCnt;
	}
	
	@Override
	public int deleteMainQuickMenu(EmsMainVO emsMainVo) throws Exception {
		return mainDAO.deleteMainQuickMenu(emsMainVo);
	}

}
